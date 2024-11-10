package io.netty.channel;

import io.netty.buffer.AbstractReferenceCountedByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.util.Recycler;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.internal.InternalThreadLocalMap;
import io.netty.util.internal.ObjectPool;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PromiseNotificationUtil;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

/* loaded from: classes4.dex */
public final class ChannelOutboundBuffer {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Channel channel;
    private volatile Runnable fireChannelWritabilityChangedTask;
    private int flushed;
    private Entry flushedEntry;
    private boolean inFail;
    private int nioBufferCount;
    private long nioBufferSize;
    private Entry tailEntry;
    private volatile long totalPendingSize;
    private Entry unflushedEntry;
    private volatile int unwritable;
    static final int CHANNEL_OUTBOUND_BUFFER_ENTRY_OVERHEAD = SystemPropertyUtil.getInt("io.netty.transport.outboundBufferEntrySizeOverhead", 96);
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) ChannelOutboundBuffer.class);
    private static final FastThreadLocal<ByteBuffer[]> NIO_BUFFERS = new FastThreadLocal<ByteBuffer[]>() { // from class: io.netty.channel.ChannelOutboundBuffer.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // io.netty.util.concurrent.FastThreadLocal
        public ByteBuffer[] initialValue() throws Exception {
            return new ByteBuffer[1024];
        }
    };
    private static final AtomicLongFieldUpdater<ChannelOutboundBuffer> TOTAL_PENDING_SIZE_UPDATER = AtomicLongFieldUpdater.newUpdater(ChannelOutboundBuffer.class, "totalPendingSize");
    private static final AtomicIntegerFieldUpdater<ChannelOutboundBuffer> UNWRITABLE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(ChannelOutboundBuffer.class, "unwritable");

    /* loaded from: classes4.dex */
    public interface MessageProcessor {
        boolean processMessage(Object obj) throws Exception;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ChannelOutboundBuffer(AbstractChannel channel) {
        this.channel = channel;
    }

    public void addMessage(Object msg, int size, ChannelPromise promise) {
        Entry entry = Entry.newInstance(msg, size, total(msg), promise);
        if (this.tailEntry == null) {
            this.flushedEntry = null;
        } else {
            Entry tail = this.tailEntry;
            tail.next = entry;
        }
        this.tailEntry = entry;
        if (this.unflushedEntry == null) {
            this.unflushedEntry = entry;
        }
        if (msg instanceof AbstractReferenceCountedByteBuf) {
            ((AbstractReferenceCountedByteBuf) msg).touch();
        } else {
            ReferenceCountUtil.touch(msg);
        }
        incrementPendingOutboundBytes(entry.pendingSize, false);
    }

    public void addFlush() {
        Entry entry = this.unflushedEntry;
        if (entry != null) {
            if (this.flushedEntry == null) {
                this.flushedEntry = entry;
            }
            do {
                this.flushed++;
                if (!entry.promise.setUncancellable()) {
                    int pending = entry.cancel();
                    decrementPendingOutboundBytes(pending, false, true);
                }
                entry = entry.next;
            } while (entry != null);
            this.unflushedEntry = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void incrementPendingOutboundBytes(long size) {
        incrementPendingOutboundBytes(size, true);
    }

    private void incrementPendingOutboundBytes(long size, boolean invokeLater) {
        if (size == 0) {
            return;
        }
        long newWriteBufferSize = TOTAL_PENDING_SIZE_UPDATER.addAndGet(this, size);
        if (newWriteBufferSize > this.channel.config().getWriteBufferHighWaterMark()) {
            setUnwritable(invokeLater);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void decrementPendingOutboundBytes(long size) {
        decrementPendingOutboundBytes(size, true, true);
    }

    private void decrementPendingOutboundBytes(long size, boolean invokeLater, boolean notifyWritability) {
        if (size == 0) {
            return;
        }
        long newWriteBufferSize = TOTAL_PENDING_SIZE_UPDATER.addAndGet(this, -size);
        if (notifyWritability && newWriteBufferSize < this.channel.config().getWriteBufferLowWaterMark()) {
            setWritable(invokeLater);
        }
    }

    private static long total(Object msg) {
        if (msg instanceof ByteBuf) {
            return ((ByteBuf) msg).readableBytes();
        }
        if (msg instanceof FileRegion) {
            return ((FileRegion) msg).count();
        }
        if (msg instanceof ByteBufHolder) {
            return ((ByteBufHolder) msg).content().readableBytes();
        }
        return -1L;
    }

    public Object current() {
        Entry entry = this.flushedEntry;
        if (entry == null) {
            return null;
        }
        return entry.msg;
    }

    public long currentProgress() {
        Entry entry = this.flushedEntry;
        if (entry == null) {
            return 0L;
        }
        return entry.progress;
    }

    public void progress(long amount) {
        Entry e = this.flushedEntry;
        if (e == null) {
            throw new AssertionError();
        }
        ChannelPromise p = e.promise;
        long progress = e.progress + amount;
        e.progress = progress;
        if (p == null) {
            throw new AssertionError();
        }
        Class<?> promiseClass = p.getClass();
        if (promiseClass == VoidChannelPromise.class || promiseClass == DefaultChannelPromise.class) {
            return;
        }
        if (p instanceof DefaultChannelProgressivePromise) {
            ((DefaultChannelProgressivePromise) p).tryProgress(progress, e.total);
        } else if (p instanceof ChannelProgressivePromise) {
            ((ChannelProgressivePromise) p).tryProgress(progress, e.total);
        }
    }

    public boolean remove() {
        Entry e = this.flushedEntry;
        if (e == null) {
            clearNioBuffers();
            return false;
        }
        Object msg = e.msg;
        ChannelPromise promise = e.promise;
        int size = e.pendingSize;
        removeEntry(e);
        if (!e.cancelled) {
            if (msg instanceof AbstractReferenceCountedByteBuf) {
                try {
                    ((AbstractReferenceCountedByteBuf) msg).release();
                } catch (Throwable t) {
                    logger.warn("Failed to release a ByteBuf: {}", msg, t);
                }
            } else {
                ReferenceCountUtil.safeRelease(msg);
            }
            safeSuccess(promise);
            decrementPendingOutboundBytes(size, false, true);
        }
        e.unguardedRecycle();
        return true;
    }

    public boolean remove(Throwable cause) {
        return remove0(cause, true);
    }

    private boolean remove0(Throwable cause, boolean notifyWritability) {
        Entry e = this.flushedEntry;
        if (e == null) {
            clearNioBuffers();
            return false;
        }
        Object msg = e.msg;
        ChannelPromise promise = e.promise;
        int size = e.pendingSize;
        removeEntry(e);
        if (!e.cancelled) {
            ReferenceCountUtil.safeRelease(msg);
            safeFail(promise, cause);
            decrementPendingOutboundBytes(size, false, notifyWritability);
        }
        e.unguardedRecycle();
        return true;
    }

    private void removeEntry(Entry e) {
        int i = this.flushed - 1;
        this.flushed = i;
        if (i == 0) {
            this.flushedEntry = null;
            if (e == this.tailEntry) {
                this.tailEntry = null;
                this.unflushedEntry = null;
                return;
            }
            return;
        }
        this.flushedEntry = e.next;
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0041, code lost:            clearNioBuffers();     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0044, code lost:            return;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void removeBytes(long r9) {
        /*
            r8 = this;
        L1:
            java.lang.Object r0 = r8.current()
            boolean r1 = r0 instanceof io.netty.buffer.ByteBuf
            r2 = 0
            if (r1 != 0) goto L16
            int r1 = (r9 > r2 ? 1 : (r9 == r2 ? 0 : -1))
            if (r1 != 0) goto L10
            goto L41
        L10:
            java.lang.AssertionError r1 = new java.lang.AssertionError
            r1.<init>()
            throw r1
        L16:
            r1 = r0
            io.netty.buffer.ByteBuf r1 = (io.netty.buffer.ByteBuf) r1
            int r4 = r1.readerIndex()
            int r5 = r1.writerIndex()
            int r5 = r5 - r4
            long r6 = (long) r5
            int r6 = (r6 > r9 ? 1 : (r6 == r9 ? 0 : -1))
            if (r6 > 0) goto L35
            int r2 = (r9 > r2 ? 1 : (r9 == r2 ? 0 : -1))
            if (r2 == 0) goto L31
            long r2 = (long) r5
            r8.progress(r2)
            long r2 = (long) r5
            long r9 = r9 - r2
        L31:
            r8.remove()
            goto L1
        L35:
            int r2 = (r9 > r2 ? 1 : (r9 == r2 ? 0 : -1))
            if (r2 == 0) goto L41
            int r2 = (int) r9
            int r2 = r2 + r4
            r1.readerIndex(r2)
            r8.progress(r9)
        L41:
            r8.clearNioBuffers()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.channel.ChannelOutboundBuffer.removeBytes(long):void");
    }

    private void clearNioBuffers() {
        int count = this.nioBufferCount;
        if (count > 0) {
            this.nioBufferCount = 0;
            Arrays.fill(NIO_BUFFERS.get(), 0, count, (Object) null);
        }
    }

    public ByteBuffer[] nioBuffers() {
        return nioBuffers(Integer.MAX_VALUE, 2147483647L);
    }

    public ByteBuffer[] nioBuffers(int maxCount, long maxBytes) {
        ByteBuf buf;
        int readerIndex;
        int readableBytes;
        if (maxCount <= 0) {
            throw new AssertionError();
        }
        if (maxBytes <= 0) {
            throw new AssertionError();
        }
        long nioBufferSize = 0;
        int nioBufferCount = 0;
        InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.get();
        ByteBuffer[] nioBuffers = NIO_BUFFERS.get(threadLocalMap);
        for (Entry entry = this.flushedEntry; isFlushedEntry(entry) && (entry.msg instanceof ByteBuf); entry = entry.next) {
            if (!entry.cancelled && (readableBytes = buf.writerIndex() - (readerIndex = (buf = (ByteBuf) entry.msg).readerIndex())) > 0) {
                if (maxBytes - readableBytes < nioBufferSize && nioBufferCount != 0) {
                    break;
                }
                nioBufferSize += readableBytes;
                int count = entry.count;
                if (count == -1) {
                    int nioBufferCount2 = buf.nioBufferCount();
                    count = nioBufferCount2;
                    entry.count = nioBufferCount2;
                }
                int neededSpace = Math.min(maxCount, nioBufferCount + count);
                if (neededSpace > nioBuffers.length) {
                    nioBuffers = expandNioBufferArray(nioBuffers, neededSpace, nioBufferCount);
                    NIO_BUFFERS.set(threadLocalMap, nioBuffers);
                }
                if (count == 1) {
                    ByteBuffer nioBuf = entry.buf;
                    if (nioBuf == null) {
                        ByteBuffer internalNioBuffer = buf.internalNioBuffer(readerIndex, readableBytes);
                        nioBuf = internalNioBuffer;
                        entry.buf = internalNioBuffer;
                    }
                    nioBuffers[nioBufferCount] = nioBuf;
                    nioBufferCount++;
                } else {
                    nioBufferCount = nioBuffers(entry, buf, nioBuffers, nioBufferCount, maxCount);
                }
                if (nioBufferCount >= maxCount) {
                    break;
                }
            }
        }
        this.nioBufferCount = nioBufferCount;
        this.nioBufferSize = nioBufferSize;
        return nioBuffers;
    }

    private static int nioBuffers(Entry entry, ByteBuf buf, ByteBuffer[] nioBuffers, int nioBufferCount, int maxCount) {
        ByteBuffer nioBuf;
        ByteBuffer[] nioBufs = entry.bufs;
        if (nioBufs == null) {
            ByteBuffer[] nioBuffers2 = buf.nioBuffers();
            nioBufs = nioBuffers2;
            entry.bufs = nioBuffers2;
        }
        for (int i = 0; i < nioBufs.length && nioBufferCount < maxCount && (nioBuf = nioBufs[i]) != null; i++) {
            if (nioBuf.hasRemaining()) {
                nioBuffers[nioBufferCount] = nioBuf;
                nioBufferCount++;
            }
        }
        return nioBufferCount;
    }

    private static ByteBuffer[] expandNioBufferArray(ByteBuffer[] array, int neededSpace, int size) {
        int newCapacity = array.length;
        do {
            newCapacity <<= 1;
            if (newCapacity < 0) {
                throw new IllegalStateException();
            }
        } while (neededSpace > newCapacity);
        ByteBuffer[] newArray = new ByteBuffer[newCapacity];
        System.arraycopy(array, 0, newArray, 0, size);
        return newArray;
    }

    public int nioBufferCount() {
        return this.nioBufferCount;
    }

    public long nioBufferSize() {
        return this.nioBufferSize;
    }

    public boolean isWritable() {
        return this.unwritable == 0;
    }

    public boolean getUserDefinedWritability(int index) {
        return (this.unwritable & writabilityMask(index)) == 0;
    }

    public void setUserDefinedWritability(int index, boolean writable) {
        if (writable) {
            setUserDefinedWritability(index);
        } else {
            clearUserDefinedWritability(index);
        }
    }

    private void setUserDefinedWritability(int index) {
        int oldValue;
        int newValue;
        int mask = ~writabilityMask(index);
        do {
            oldValue = this.unwritable;
            newValue = oldValue & mask;
        } while (!UNWRITABLE_UPDATER.compareAndSet(this, oldValue, newValue));
        if (oldValue != 0 && newValue == 0) {
            fireChannelWritabilityChanged(true);
        }
    }

    private void clearUserDefinedWritability(int index) {
        int oldValue;
        int newValue;
        int mask = writabilityMask(index);
        do {
            oldValue = this.unwritable;
            newValue = oldValue | mask;
        } while (!UNWRITABLE_UPDATER.compareAndSet(this, oldValue, newValue));
        if (oldValue == 0 && newValue != 0) {
            fireChannelWritabilityChanged(true);
        }
    }

    private static int writabilityMask(int index) {
        if (index < 1 || index > 31) {
            throw new IllegalArgumentException("index: " + index + " (expected: 1~31)");
        }
        return 1 << index;
    }

    private void setWritable(boolean invokeLater) {
        int oldValue;
        int newValue;
        do {
            oldValue = this.unwritable;
            newValue = oldValue & (-2);
        } while (!UNWRITABLE_UPDATER.compareAndSet(this, oldValue, newValue));
        if (oldValue != 0 && newValue == 0) {
            fireChannelWritabilityChanged(invokeLater);
        }
    }

    private void setUnwritable(boolean invokeLater) {
        int oldValue;
        int newValue;
        do {
            oldValue = this.unwritable;
            newValue = oldValue | 1;
        } while (!UNWRITABLE_UPDATER.compareAndSet(this, oldValue, newValue));
        if (oldValue == 0) {
            fireChannelWritabilityChanged(invokeLater);
        }
    }

    private void fireChannelWritabilityChanged(boolean invokeLater) {
        final ChannelPipeline pipeline = this.channel.pipeline();
        if (invokeLater) {
            Runnable task = this.fireChannelWritabilityChangedTask;
            if (task == null) {
                Runnable runnable = new Runnable() { // from class: io.netty.channel.ChannelOutboundBuffer.2
                    @Override // java.lang.Runnable
                    public void run() {
                        pipeline.fireChannelWritabilityChanged();
                    }
                };
                task = runnable;
                this.fireChannelWritabilityChangedTask = runnable;
            }
            this.channel.eventLoop().execute(task);
            return;
        }
        pipeline.fireChannelWritabilityChanged();
    }

    public int size() {
        return this.flushed;
    }

    public boolean isEmpty() {
        return this.flushed == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void failFlushed(Throwable cause, boolean notify) {
        if (this.inFail) {
            return;
        }
        try {
            this.inFail = true;
            do {
            } while (remove0(cause, notify));
        } finally {
            this.inFail = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void close(final Throwable cause, final boolean allowChannelOpen) {
        if (this.inFail) {
            this.channel.eventLoop().execute(new Runnable() { // from class: io.netty.channel.ChannelOutboundBuffer.3
                @Override // java.lang.Runnable
                public void run() {
                    ChannelOutboundBuffer.this.close(cause, allowChannelOpen);
                }
            });
            return;
        }
        this.inFail = true;
        if (!allowChannelOpen && this.channel.isOpen()) {
            throw new IllegalStateException("close() must be invoked after the channel is closed.");
        }
        if (!isEmpty()) {
            throw new IllegalStateException("close() must be invoked after all flushed writes are handled.");
        }
        try {
            for (Entry e = this.unflushedEntry; e != null; e = e.unguardedRecycleAndGetNext()) {
                int size = e.pendingSize;
                TOTAL_PENDING_SIZE_UPDATER.addAndGet(this, -size);
                if (!e.cancelled) {
                    ReferenceCountUtil.safeRelease(e.msg);
                    safeFail(e.promise, cause);
                }
            }
            this.inFail = false;
            clearNioBuffers();
        } catch (Throwable th) {
            this.inFail = false;
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void close(ClosedChannelException cause) {
        close(cause, false);
    }

    private static void safeSuccess(ChannelPromise promise) {
        PromiseNotificationUtil.trySuccess(promise, null, promise instanceof VoidChannelPromise ? null : logger);
    }

    private static void safeFail(ChannelPromise promise, Throwable cause) {
        PromiseNotificationUtil.tryFailure(promise, cause, promise instanceof VoidChannelPromise ? null : logger);
    }

    @Deprecated
    public void recycle() {
    }

    public long totalPendingWriteBytes() {
        return this.totalPendingSize;
    }

    public long bytesBeforeUnwritable() {
        long bytes = (this.channel.config().getWriteBufferHighWaterMark() - this.totalPendingSize) + 1;
        if (bytes <= 0 || !isWritable()) {
            return 0L;
        }
        return bytes;
    }

    public long bytesBeforeWritable() {
        long bytes = (this.totalPendingSize - this.channel.config().getWriteBufferLowWaterMark()) + 1;
        if (bytes <= 0 || isWritable()) {
            return 0L;
        }
        return bytes;
    }

    public void forEachFlushedMessage(MessageProcessor processor) throws Exception {
        ObjectUtil.checkNotNull(processor, "processor");
        Entry entry = this.flushedEntry;
        if (entry == null) {
            return;
        }
        do {
            if (!entry.cancelled && !processor.processMessage(entry.msg)) {
                return;
            } else {
                entry = entry.next;
            }
        } while (isFlushedEntry(entry));
    }

    private boolean isFlushedEntry(Entry e) {
        return (e == null || e == this.unflushedEntry) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static final class Entry {
        private static final ObjectPool<Entry> RECYCLER = ObjectPool.newPool(new ObjectPool.ObjectCreator<Entry>() { // from class: io.netty.channel.ChannelOutboundBuffer.Entry.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // io.netty.util.internal.ObjectPool.ObjectCreator
            public Entry newObject(ObjectPool.Handle<Entry> handle) {
                return new Entry(handle);
            }
        });
        ByteBuffer buf;
        ByteBuffer[] bufs;
        boolean cancelled;
        int count;
        private final Recycler.EnhancedHandle<Entry> handle;
        Object msg;
        Entry next;
        int pendingSize;
        long progress;
        ChannelPromise promise;
        long total;

        private Entry(ObjectPool.Handle<Entry> handle) {
            this.count = -1;
            this.handle = (Recycler.EnhancedHandle) handle;
        }

        static Entry newInstance(Object msg, int size, long total, ChannelPromise promise) {
            Entry entry = RECYCLER.get();
            entry.msg = msg;
            entry.pendingSize = ChannelOutboundBuffer.CHANNEL_OUTBOUND_BUFFER_ENTRY_OVERHEAD + size;
            entry.total = total;
            entry.promise = promise;
            return entry;
        }

        int cancel() {
            if (this.cancelled) {
                return 0;
            }
            this.cancelled = true;
            int pSize = this.pendingSize;
            ReferenceCountUtil.safeRelease(this.msg);
            this.msg = Unpooled.EMPTY_BUFFER;
            this.pendingSize = 0;
            this.total = 0L;
            this.progress = 0L;
            this.bufs = null;
            this.buf = null;
            return pSize;
        }

        void unguardedRecycle() {
            this.next = null;
            this.bufs = null;
            this.buf = null;
            this.msg = null;
            this.promise = null;
            this.progress = 0L;
            this.total = 0L;
            this.pendingSize = 0;
            this.count = -1;
            this.cancelled = false;
            this.handle.unguardedRecycle(this);
        }

        Entry unguardedRecycleAndGetNext() {
            Entry next = this.next;
            unguardedRecycle();
            return next;
        }
    }
}
