package io.netty.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.ArrayDeque;

/* loaded from: classes4.dex */
public abstract class AbstractCoalescingBufferQueue {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) AbstractCoalescingBufferQueue.class);
    private final ArrayDeque<Object> bufAndListenerPairs;
    private int readableBytes;
    private final PendingBytesTracker tracker;

    protected abstract ByteBuf compose(ByteBufAllocator byteBufAllocator, ByteBuf byteBuf, ByteBuf byteBuf2);

    protected abstract ByteBuf removeEmptyValue();

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractCoalescingBufferQueue(Channel channel, int initSize) {
        this.bufAndListenerPairs = new ArrayDeque<>(initSize);
        this.tracker = channel == null ? null : PendingBytesTracker.newTracker(channel);
    }

    public final void addFirst(ByteBuf buf, ChannelPromise promise) {
        addFirst(buf, toChannelFutureListener(promise));
    }

    private void addFirst(ByteBuf buf, ChannelFutureListener listener) {
        buf.touch();
        if (listener != null) {
            this.bufAndListenerPairs.addFirst(listener);
        }
        this.bufAndListenerPairs.addFirst(buf);
        incrementReadableBytes(buf.readableBytes());
    }

    public final void add(ByteBuf buf) {
        add(buf, (ChannelFutureListener) null);
    }

    public final void add(ByteBuf buf, ChannelPromise promise) {
        add(buf, toChannelFutureListener(promise));
    }

    public final void add(ByteBuf buf, ChannelFutureListener listener) {
        buf.touch();
        this.bufAndListenerPairs.add(buf);
        if (listener != null) {
            this.bufAndListenerPairs.add(listener);
        }
        incrementReadableBytes(buf.readableBytes());
    }

    public final ByteBuf removeFirst(ChannelPromise aggregatePromise) {
        Object entry = this.bufAndListenerPairs.poll();
        if (entry == null) {
            return null;
        }
        if (!(entry instanceof ByteBuf)) {
            throw new AssertionError();
        }
        ByteBuf result = (ByteBuf) entry;
        decrementReadableBytes(result.readableBytes());
        Object entry2 = this.bufAndListenerPairs.peek();
        if (entry2 instanceof ChannelFutureListener) {
            aggregatePromise.addListener((GenericFutureListener<? extends Future<? super Void>>) entry2);
            this.bufAndListenerPairs.poll();
        }
        return result;
    }

    /* JADX WARN: Code restructure failed: missing block: B:44:0x0041, code lost:            r6.bufAndListenerPairs.addFirst(r4);     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x0046, code lost:            if (r8 <= 0) goto L25;     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x0048, code lost:            r1 = r4.readRetainedSlice(r8);     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x004d, code lost:            if (r0 != null) goto L23;     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x004f, code lost:            r5 = r1;     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x0055, code lost:            r0 = r5;        r8 = 0;     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x0051, code lost:            r5 = compose(r7, r0, r1);     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final io.netty.buffer.ByteBuf remove(io.netty.buffer.ByteBufAllocator r7, int r8, io.netty.channel.ChannelPromise r9) {
        /*
            r6 = this;
            java.lang.String r0 = "bytes"
            io.netty.util.internal.ObjectUtil.checkPositiveOrZero(r8, r0)
            java.lang.String r0 = "aggregatePromise"
            io.netty.util.internal.ObjectUtil.checkNotNull(r9, r0)
            java.util.ArrayDeque<java.lang.Object> r0 = r6.bufAndListenerPairs
            boolean r0 = r0.isEmpty()
            if (r0 == 0) goto L21
            int r0 = r6.readableBytes
            if (r0 != 0) goto L1b
            io.netty.buffer.ByteBuf r0 = r6.removeEmptyValue()
            return r0
        L1b:
            java.lang.AssertionError r0 = new java.lang.AssertionError
            r0.<init>()
            throw r0
        L21:
            int r0 = r6.readableBytes
            int r8 = java.lang.Math.min(r8, r0)
            r0 = 0
            r1 = 0
            r2 = r8
        L2a:
            java.util.ArrayDeque<java.lang.Object> r3 = r6.bufAndListenerPairs     // Catch: java.lang.Throwable -> L84
            java.lang.Object r3 = r3.poll()     // Catch: java.lang.Throwable -> L84
            if (r3 != 0) goto L33
            goto L57
        L33:
            boolean r4 = r3 instanceof io.netty.buffer.ByteBuf     // Catch: java.lang.Throwable -> L84
            if (r4 == 0) goto L6e
            r4 = r3
            io.netty.buffer.ByteBuf r4 = (io.netty.buffer.ByteBuf) r4     // Catch: java.lang.Throwable -> L84
            r1 = r4
            int r4 = r1.readableBytes()     // Catch: java.lang.Throwable -> L84
            if (r4 <= r8) goto L58
            java.util.ArrayDeque<java.lang.Object> r5 = r6.bufAndListenerPairs     // Catch: java.lang.Throwable -> L84
            r5.addFirst(r1)     // Catch: java.lang.Throwable -> L84
            if (r8 <= 0) goto L57
            io.netty.buffer.ByteBuf r5 = r1.readRetainedSlice(r8)     // Catch: java.lang.Throwable -> L84
            r1 = r5
            if (r0 != 0) goto L51
            r5 = r1
            goto L55
        L51:
            io.netty.buffer.ByteBuf r5 = r6.compose(r7, r0, r1)     // Catch: java.lang.Throwable -> L84
        L55:
            r0 = r5
            r8 = 0
        L57:
            goto L91
        L58:
            int r8 = r8 - r4
            if (r0 != 0) goto L67
            int r5 = r6.readableBytes     // Catch: java.lang.Throwable -> L84
            if (r4 != r5) goto L61
            r5 = r1
            goto L65
        L61:
            io.netty.buffer.ByteBuf r5 = r6.composeFirst(r7, r1)     // Catch: java.lang.Throwable -> L84
        L65:
            r0 = r5
            goto L6c
        L67:
            io.netty.buffer.ByteBuf r5 = r6.compose(r7, r0, r1)     // Catch: java.lang.Throwable -> L84
            r0 = r5
        L6c:
            r1 = 0
            goto L83
        L6e:
            boolean r4 = r3 instanceof io.netty.channel.DelegatingChannelPromiseNotifier     // Catch: java.lang.Throwable -> L84
            if (r4 == 0) goto L79
            r4 = r3
            io.netty.channel.DelegatingChannelPromiseNotifier r4 = (io.netty.channel.DelegatingChannelPromiseNotifier) r4     // Catch: java.lang.Throwable -> L84
            r9.addListener(r4)     // Catch: java.lang.Throwable -> L84
            goto L83
        L79:
            boolean r4 = r3 instanceof io.netty.channel.ChannelFutureListener     // Catch: java.lang.Throwable -> L84
            if (r4 == 0) goto L83
            r4 = r3
            io.netty.channel.ChannelFutureListener r4 = (io.netty.channel.ChannelFutureListener) r4     // Catch: java.lang.Throwable -> L84
            r9.addListener(r4)     // Catch: java.lang.Throwable -> L84
        L83:
            goto L2a
        L84:
            r3 = move-exception
            io.netty.util.ReferenceCountUtil.safeRelease(r1)
            io.netty.util.ReferenceCountUtil.safeRelease(r0)
            r9.setFailure(r3)
            io.netty.util.internal.PlatformDependent.throwException(r3)
        L91:
            int r3 = r2 - r8
            r6.decrementReadableBytes(r3)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.channel.AbstractCoalescingBufferQueue.remove(io.netty.buffer.ByteBufAllocator, int, io.netty.channel.ChannelPromise):io.netty.buffer.ByteBuf");
    }

    public final int readableBytes() {
        return this.readableBytes;
    }

    public final boolean isEmpty() {
        return this.bufAndListenerPairs.isEmpty();
    }

    public final void releaseAndFailAll(ChannelOutboundInvoker invoker, Throwable cause) {
        releaseAndCompleteAll(invoker.newFailedFuture(cause));
    }

    public final void copyTo(AbstractCoalescingBufferQueue dest) {
        dest.bufAndListenerPairs.addAll(this.bufAndListenerPairs);
        dest.incrementReadableBytes(this.readableBytes);
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x0068 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0066 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void writeAndRemoveAll(io.netty.channel.ChannelHandlerContext r7) {
        /*
            r6 = this;
            r0 = 0
            r1 = 0
        L2:
            java.util.ArrayDeque<java.lang.Object> r2 = r6.bufAndListenerPairs
            java.lang.Object r2 = r2.poll()
            if (r2 != 0) goto L24
            if (r1 == 0) goto L1a
            int r3 = r1.readableBytes()     // Catch: java.lang.Throwable -> L63
            r6.decrementReadableBytes(r3)     // Catch: java.lang.Throwable -> L63
            io.netty.channel.ChannelPromise r3 = r7.voidPromise()     // Catch: java.lang.Throwable -> L63
            r7.write(r1, r3)     // Catch: java.lang.Throwable -> L63
        L1a:
            if (r0 != 0) goto L1e
            return
        L1e:
            java.lang.IllegalStateException r2 = new java.lang.IllegalStateException
            r2.<init>(r0)
            throw r2
        L24:
            boolean r3 = r2 instanceof io.netty.buffer.ByteBuf     // Catch: java.lang.Throwable -> L63
            if (r3 == 0) goto L3d
            if (r1 == 0) goto L38
            int r3 = r1.readableBytes()     // Catch: java.lang.Throwable -> L63
            r6.decrementReadableBytes(r3)     // Catch: java.lang.Throwable -> L63
            io.netty.channel.ChannelPromise r3 = r7.voidPromise()     // Catch: java.lang.Throwable -> L63
            r7.write(r1, r3)     // Catch: java.lang.Throwable -> L63
        L38:
            r3 = r2
            io.netty.buffer.ByteBuf r3 = (io.netty.buffer.ByteBuf) r3     // Catch: java.lang.Throwable -> L63
            r1 = r3
            goto L62
        L3d:
            boolean r3 = r2 instanceof io.netty.channel.ChannelPromise     // Catch: java.lang.Throwable -> L63
            if (r3 == 0) goto L50
            int r3 = r1.readableBytes()     // Catch: java.lang.Throwable -> L63
            r6.decrementReadableBytes(r3)     // Catch: java.lang.Throwable -> L63
            r3 = r2
            io.netty.channel.ChannelPromise r3 = (io.netty.channel.ChannelPromise) r3     // Catch: java.lang.Throwable -> L63
            r7.write(r1, r3)     // Catch: java.lang.Throwable -> L63
            r1 = 0
            goto L62
        L50:
            int r3 = r1.readableBytes()     // Catch: java.lang.Throwable -> L63
            r6.decrementReadableBytes(r3)     // Catch: java.lang.Throwable -> L63
            io.netty.channel.ChannelFuture r3 = r7.write(r1)     // Catch: java.lang.Throwable -> L63
            r4 = r2
            io.netty.channel.ChannelFutureListener r4 = (io.netty.channel.ChannelFutureListener) r4     // Catch: java.lang.Throwable -> L63
            r3.addListener(r4)     // Catch: java.lang.Throwable -> L63
            r1 = 0
        L62:
            goto L6f
        L63:
            r3 = move-exception
            if (r0 != 0) goto L68
            r0 = r3
            goto L6f
        L68:
            io.netty.util.internal.logging.InternalLogger r4 = io.netty.channel.AbstractCoalescingBufferQueue.logger
            java.lang.String r5 = "Throwable being suppressed because Throwable {} is already pending"
            r4.info(r5, r0, r3)
        L6f:
            goto L2
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.channel.AbstractCoalescingBufferQueue.writeAndRemoveAll(io.netty.channel.ChannelHandlerContext):void");
    }

    public String toString() {
        return "bytes: " + this.readableBytes + " buffers: " + (size() >> 1);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final ByteBuf composeIntoComposite(ByteBufAllocator alloc, ByteBuf cumulation, ByteBuf next) {
        CompositeByteBuf composite = alloc.compositeBuffer(size() + 2);
        try {
            composite.addComponent(true, cumulation);
            composite.addComponent(true, next);
        } catch (Throwable cause) {
            composite.release();
            ReferenceCountUtil.safeRelease(next);
            PlatformDependent.throwException(cause);
        }
        return composite;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final ByteBuf copyAndCompose(ByteBufAllocator alloc, ByteBuf cumulation, ByteBuf next) {
        ByteBuf newCumulation = alloc.ioBuffer(cumulation.readableBytes() + next.readableBytes());
        try {
            newCumulation.writeBytes(cumulation).writeBytes(next);
        } catch (Throwable cause) {
            newCumulation.release();
            ReferenceCountUtil.safeRelease(next);
            PlatformDependent.throwException(cause);
        }
        cumulation.release();
        next.release();
        return newCumulation;
    }

    protected ByteBuf composeFirst(ByteBufAllocator allocator, ByteBuf first) {
        return first;
    }

    protected final int size() {
        return this.bufAndListenerPairs.size();
    }

    private void releaseAndCompleteAll(ChannelFuture future) {
        Throwable pending = null;
        while (true) {
            Object entry = this.bufAndListenerPairs.poll();
            if (entry == null) {
                break;
            }
            try {
                if (entry instanceof ByteBuf) {
                    ByteBuf buffer = (ByteBuf) entry;
                    decrementReadableBytes(buffer.readableBytes());
                    ReferenceCountUtil.safeRelease(buffer);
                } else {
                    ((ChannelFutureListener) entry).operationComplete(future);
                }
            } catch (Throwable t) {
                if (pending == null) {
                    pending = t;
                } else {
                    logger.info("Throwable being suppressed because Throwable {} is already pending", pending, t);
                }
            }
        }
        if (pending != null) {
            throw new IllegalStateException(pending);
        }
    }

    private void incrementReadableBytes(int increment) {
        int nextReadableBytes = this.readableBytes + increment;
        if (nextReadableBytes < this.readableBytes) {
            throw new IllegalStateException("buffer queue length overflow: " + this.readableBytes + " + " + increment);
        }
        this.readableBytes = nextReadableBytes;
        if (this.tracker != null) {
            this.tracker.incrementPendingOutboundBytes(increment);
        }
    }

    private void decrementReadableBytes(int decrement) {
        this.readableBytes -= decrement;
        if (this.readableBytes < 0) {
            throw new AssertionError();
        }
        if (this.tracker != null) {
            this.tracker.decrementPendingOutboundBytes(decrement);
        }
    }

    private static ChannelFutureListener toChannelFutureListener(ChannelPromise promise) {
        if (promise.isVoid()) {
            return null;
        }
        return new DelegatingChannelPromiseNotifier(promise);
    }
}
