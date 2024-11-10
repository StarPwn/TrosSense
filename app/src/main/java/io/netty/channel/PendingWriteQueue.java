package io.netty.channel;

import androidx.core.app.NotificationCompat;
import io.netty.buffer.AbstractReferenceCountedByteBuf;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.PromiseCombiner;
import io.netty.util.internal.ObjectPool;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/* loaded from: classes4.dex */
public final class PendingWriteQueue {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private long bytes;
    private final EventExecutor executor;
    private PendingWrite head;
    private final ChannelOutboundInvoker invoker;
    private int size;
    private PendingWrite tail;
    private final PendingBytesTracker tracker;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) PendingWriteQueue.class);
    private static final int PENDING_WRITE_OVERHEAD = SystemPropertyUtil.getInt("io.netty.transport.pendingWriteSizeOverhead", 64);

    public PendingWriteQueue(ChannelHandlerContext ctx) {
        this.tracker = PendingBytesTracker.newTracker(ctx.channel());
        this.invoker = ctx;
        this.executor = ctx.executor();
    }

    public PendingWriteQueue(Channel channel) {
        this.tracker = PendingBytesTracker.newTracker(channel);
        this.invoker = channel;
        this.executor = channel.eventLoop();
    }

    public boolean isEmpty() {
        if (this.executor.inEventLoop()) {
            return this.head == null;
        }
        throw new AssertionError();
    }

    public int size() {
        if (!this.executor.inEventLoop()) {
            throw new AssertionError();
        }
        return this.size;
    }

    public long bytes() {
        if (!this.executor.inEventLoop()) {
            throw new AssertionError();
        }
        return this.bytes;
    }

    private int size(Object msg) {
        int messageSize = this.tracker.size(msg);
        if (messageSize < 0) {
            messageSize = 0;
        }
        return PENDING_WRITE_OVERHEAD + messageSize;
    }

    public void add(Object msg, ChannelPromise promise) {
        if (!this.executor.inEventLoop()) {
            throw new AssertionError();
        }
        ObjectUtil.checkNotNull(msg, NotificationCompat.CATEGORY_MESSAGE);
        ObjectUtil.checkNotNull(promise, "promise");
        int messageSize = size(msg);
        PendingWrite write = PendingWrite.newInstance(msg, messageSize, promise);
        PendingWrite currentTail = this.tail;
        if (currentTail == null) {
            this.head = write;
            this.tail = write;
        } else {
            currentTail.next = write;
            this.tail = write;
        }
        this.size++;
        this.bytes += messageSize;
        this.tracker.incrementPendingOutboundBytes(write.size);
        if (msg instanceof AbstractReferenceCountedByteBuf) {
            ((AbstractReferenceCountedByteBuf) msg).touch();
        } else {
            ReferenceCountUtil.touch(msg);
        }
    }

    public ChannelFuture removeAndWriteAll() {
        if (!this.executor.inEventLoop()) {
            throw new AssertionError();
        }
        if (isEmpty()) {
            return null;
        }
        ChannelPromise p = this.invoker.newPromise();
        PromiseCombiner combiner = new PromiseCombiner(this.executor);
        try {
            PendingWrite write = this.head;
            while (write != null) {
                this.tail = null;
                this.head = null;
                this.size = 0;
                this.bytes = 0L;
                while (write != null) {
                    PendingWrite next = write.next;
                    Object msg = write.msg;
                    ChannelPromise promise = write.promise;
                    recycle(write, false);
                    if (!(promise instanceof VoidChannelPromise)) {
                        combiner.add((Promise) promise);
                    }
                    this.invoker.write(msg, promise);
                    write = next;
                }
                write = this.head;
            }
            combiner.finish(p);
        } catch (Throwable cause) {
            p.setFailure(cause);
        }
        assertEmpty();
        return p;
    }

    public void removeAndFailAll(Throwable cause) {
        if (!this.executor.inEventLoop()) {
            throw new AssertionError();
        }
        ObjectUtil.checkNotNull(cause, "cause");
        PendingWrite write = this.head;
        while (write != null) {
            this.tail = null;
            this.head = null;
            this.size = 0;
            this.bytes = 0L;
            while (write != null) {
                PendingWrite next = write.next;
                ReferenceCountUtil.safeRelease(write.msg);
                ChannelPromise promise = write.promise;
                recycle(write, false);
                safeFail(promise, cause);
                write = next;
            }
            write = this.head;
        }
        assertEmpty();
    }

    public void removeAndFail(Throwable cause) {
        if (!this.executor.inEventLoop()) {
            throw new AssertionError();
        }
        ObjectUtil.checkNotNull(cause, "cause");
        PendingWrite write = this.head;
        if (write == null) {
            return;
        }
        ReferenceCountUtil.safeRelease(write.msg);
        ChannelPromise promise = write.promise;
        safeFail(promise, cause);
        recycle(write, true);
    }

    private void assertEmpty() {
        if (this.tail != null || this.head != null || this.size != 0) {
            throw new AssertionError();
        }
    }

    public ChannelFuture removeAndWrite() {
        if (!this.executor.inEventLoop()) {
            throw new AssertionError();
        }
        PendingWrite write = this.head;
        if (write == null) {
            return null;
        }
        Object msg = write.msg;
        ChannelPromise promise = write.promise;
        recycle(write, true);
        return this.invoker.write(msg, promise);
    }

    public ChannelPromise remove() {
        if (!this.executor.inEventLoop()) {
            throw new AssertionError();
        }
        PendingWrite write = this.head;
        if (write == null) {
            return null;
        }
        ChannelPromise promise = write.promise;
        ReferenceCountUtil.safeRelease(write.msg);
        recycle(write, true);
        return promise;
    }

    public Object current() {
        if (!this.executor.inEventLoop()) {
            throw new AssertionError();
        }
        PendingWrite write = this.head;
        if (write == null) {
            return null;
        }
        return write.msg;
    }

    private void recycle(PendingWrite write, boolean update) {
        PendingWrite next = write.next;
        long writeSize = write.size;
        if (update) {
            if (next == null) {
                this.tail = null;
                this.head = null;
                this.size = 0;
                this.bytes = 0L;
            } else {
                this.head = next;
                this.size--;
                this.bytes -= writeSize;
                if (this.size <= 0 || this.bytes < 0) {
                    throw new AssertionError();
                }
            }
        }
        write.recycle();
        this.tracker.decrementPendingOutboundBytes(writeSize);
    }

    private static void safeFail(ChannelPromise promise, Throwable cause) {
        if (!(promise instanceof VoidChannelPromise) && !promise.tryFailure(cause)) {
            logger.warn("Failed to mark a promise as failure because it's done already: {}", promise, cause);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static final class PendingWrite {
        private static final ObjectPool<PendingWrite> RECYCLER = ObjectPool.newPool(new ObjectPool.ObjectCreator<PendingWrite>() { // from class: io.netty.channel.PendingWriteQueue.PendingWrite.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // io.netty.util.internal.ObjectPool.ObjectCreator
            public PendingWrite newObject(ObjectPool.Handle<PendingWrite> handle) {
                return new PendingWrite(handle);
            }
        });
        private final ObjectPool.Handle<PendingWrite> handle;
        private Object msg;
        private PendingWrite next;
        private ChannelPromise promise;
        private long size;

        private PendingWrite(ObjectPool.Handle<PendingWrite> handle) {
            this.handle = handle;
        }

        static PendingWrite newInstance(Object msg, int size, ChannelPromise promise) {
            PendingWrite write = RECYCLER.get();
            write.size = size;
            write.msg = msg;
            write.promise = promise;
            return write;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void recycle() {
            this.size = 0L;
            this.next = null;
            this.msg = null;
            this.promise = null;
            this.handle.recycle(this);
        }
    }
}
