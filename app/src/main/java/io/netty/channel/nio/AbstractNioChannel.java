package io.netty.channel.nio;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ConnectTimeoutException;
import io.netty.channel.EventLoop;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ConnectionPendingException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.util.concurrent.TimeUnit;

/* loaded from: classes4.dex */
public abstract class AbstractNioChannel extends AbstractChannel {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) AbstractNioChannel.class);
    private final SelectableChannel ch;
    private final Runnable clearReadPendingRunnable;
    private ChannelPromise connectPromise;
    private Future<?> connectTimeoutFuture;
    protected final int readInterestOp;
    boolean readPending;
    private SocketAddress requestedRemoteAddress;
    volatile SelectionKey selectionKey;

    /* loaded from: classes4.dex */
    public interface NioUnsafe extends Channel.Unsafe {
        SelectableChannel ch();

        void finishConnect();

        void forceFlush();

        void read();
    }

    protected abstract boolean doConnect(SocketAddress socketAddress, SocketAddress socketAddress2) throws Exception;

    protected abstract void doFinishConnect() throws Exception;

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractNioChannel(Channel parent, SelectableChannel ch, int readInterestOp) {
        super(parent);
        this.clearReadPendingRunnable = new Runnable() { // from class: io.netty.channel.nio.AbstractNioChannel.1
            @Override // java.lang.Runnable
            public void run() {
                AbstractNioChannel.this.clearReadPending0();
            }
        };
        this.ch = ch;
        this.readInterestOp = readInterestOp;
        try {
            ch.configureBlocking(false);
        } catch (IOException e) {
            try {
                ch.close();
            } catch (IOException e2) {
                logger.warn("Failed to close a partially initialized socket.", (Throwable) e2);
            }
            throw new ChannelException("Failed to enter non-blocking mode.", e);
        }
    }

    @Override // io.netty.channel.Channel
    public boolean isOpen() {
        return this.ch.isOpen();
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public NioUnsafe unsafe() {
        return (NioUnsafe) super.unsafe();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: javaChannel */
    public SelectableChannel mo214javaChannel() {
        return this.ch;
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public NioEventLoop eventLoop() {
        return (NioEventLoop) super.eventLoop();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public SelectionKey selectionKey() {
        if (this.selectionKey == null) {
            throw new AssertionError();
        }
        return this.selectionKey;
    }

    @Deprecated
    protected boolean isReadPending() {
        return this.readPending;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Deprecated
    public void setReadPending(final boolean readPending) {
        if (isRegistered()) {
            EventLoop eventLoop = eventLoop();
            if (eventLoop.inEventLoop()) {
                setReadPending0(readPending);
                return;
            } else {
                eventLoop.execute(new Runnable() { // from class: io.netty.channel.nio.AbstractNioChannel.2
                    @Override // java.lang.Runnable
                    public void run() {
                        AbstractNioChannel.this.setReadPending0(readPending);
                    }
                });
                return;
            }
        }
        this.readPending = readPending;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void clearReadPending() {
        if (isRegistered()) {
            EventLoop eventLoop = eventLoop();
            if (eventLoop.inEventLoop()) {
                clearReadPending0();
                return;
            } else {
                eventLoop.execute(this.clearReadPendingRunnable);
                return;
            }
        }
        this.readPending = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setReadPending0(boolean readPending) {
        this.readPending = readPending;
        if (!readPending) {
            ((AbstractNioUnsafe) unsafe()).removeReadOp();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearReadPending0() {
        this.readPending = false;
        ((AbstractNioUnsafe) unsafe()).removeReadOp();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public abstract class AbstractNioUnsafe extends AbstractChannel.AbstractUnsafe implements NioUnsafe {
        static final /* synthetic */ boolean $assertionsDisabled = false;

        /* JADX INFO: Access modifiers changed from: protected */
        public AbstractNioUnsafe() {
            super();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public final void removeReadOp() {
            SelectionKey key = AbstractNioChannel.this.selectionKey();
            if (!key.isValid()) {
                return;
            }
            int interestOps = key.interestOps();
            if ((AbstractNioChannel.this.readInterestOp & interestOps) != 0) {
                key.interestOps((~AbstractNioChannel.this.readInterestOp) & interestOps);
            }
        }

        @Override // io.netty.channel.nio.AbstractNioChannel.NioUnsafe
        public final SelectableChannel ch() {
            return AbstractNioChannel.this.mo214javaChannel();
        }

        @Override // io.netty.channel.Channel.Unsafe
        public final void connect(final SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
            if (!promise.isDone() && ensureOpen(promise)) {
                try {
                    if (AbstractNioChannel.this.connectPromise != null) {
                        throw new ConnectionPendingException();
                    }
                    boolean wasActive = AbstractNioChannel.this.isActive();
                    if (!AbstractNioChannel.this.doConnect(remoteAddress, localAddress)) {
                        AbstractNioChannel.this.connectPromise = promise;
                        AbstractNioChannel.this.requestedRemoteAddress = remoteAddress;
                        final int connectTimeoutMillis = AbstractNioChannel.this.config().getConnectTimeoutMillis();
                        if (connectTimeoutMillis > 0) {
                            AbstractNioChannel.this.connectTimeoutFuture = AbstractNioChannel.this.eventLoop().schedule(new Runnable() { // from class: io.netty.channel.nio.AbstractNioChannel.AbstractNioUnsafe.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    ChannelPromise connectPromise = AbstractNioChannel.this.connectPromise;
                                    if (connectPromise != null && !connectPromise.isDone() && connectPromise.tryFailure(new ConnectTimeoutException("connection timed out after " + connectTimeoutMillis + " ms: " + remoteAddress))) {
                                        AbstractNioUnsafe.this.close(AbstractNioUnsafe.this.voidPromise());
                                    }
                                }
                            }, connectTimeoutMillis, TimeUnit.MILLISECONDS);
                        }
                        promise.addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.channel.nio.AbstractNioChannel.AbstractNioUnsafe.2
                            @Override // io.netty.util.concurrent.GenericFutureListener
                            public void operationComplete(ChannelFuture future) {
                                if (future.isCancelled()) {
                                    if (AbstractNioChannel.this.connectTimeoutFuture != null) {
                                        AbstractNioChannel.this.connectTimeoutFuture.cancel(false);
                                    }
                                    AbstractNioChannel.this.connectPromise = null;
                                    AbstractNioUnsafe.this.close(AbstractNioUnsafe.this.voidPromise());
                                }
                            }
                        });
                    } else {
                        fulfillConnectPromise(promise, wasActive);
                    }
                } catch (Throwable t) {
                    promise.tryFailure(annotateConnectException(t, remoteAddress));
                    closeIfClosed();
                }
            }
        }

        private void fulfillConnectPromise(ChannelPromise promise, boolean wasActive) {
            if (promise == null) {
                return;
            }
            boolean active = AbstractNioChannel.this.isActive();
            boolean promiseSet = promise.trySuccess();
            if (!wasActive && active) {
                AbstractNioChannel.this.pipeline().fireChannelActive();
            }
            if (!promiseSet) {
                close(voidPromise());
            }
        }

        private void fulfillConnectPromise(ChannelPromise promise, Throwable cause) {
            if (promise == null) {
                return;
            }
            promise.tryFailure(cause);
            closeIfClosed();
        }

        /* JADX WARN: Code restructure failed: missing block: B:10:0x0056, code lost:            return;     */
        /* JADX WARN: Code restructure failed: missing block: B:17:0x0053, code lost:            if (r5.this$0.connectTimeoutFuture == null) goto L9;     */
        @Override // io.netty.channel.nio.AbstractNioChannel.NioUnsafe
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public final void finishConnect() {
            /*
                r5 = this;
                io.netty.channel.nio.AbstractNioChannel r0 = io.netty.channel.nio.AbstractNioChannel.this
                io.netty.channel.nio.NioEventLoop r0 = r0.eventLoop()
                boolean r0 = r0.inEventLoop()
                if (r0 == 0) goto L6f
                r0 = 0
                r1 = 0
                io.netty.channel.nio.AbstractNioChannel r2 = io.netty.channel.nio.AbstractNioChannel.this     // Catch: java.lang.Throwable -> L39
                boolean r2 = r2.isActive()     // Catch: java.lang.Throwable -> L39
                io.netty.channel.nio.AbstractNioChannel r3 = io.netty.channel.nio.AbstractNioChannel.this     // Catch: java.lang.Throwable -> L39
                r3.doFinishConnect()     // Catch: java.lang.Throwable -> L39
                io.netty.channel.nio.AbstractNioChannel r3 = io.netty.channel.nio.AbstractNioChannel.this     // Catch: java.lang.Throwable -> L39
                io.netty.channel.ChannelPromise r3 = io.netty.channel.nio.AbstractNioChannel.access$200(r3)     // Catch: java.lang.Throwable -> L39
                r5.fulfillConnectPromise(r3, r2)     // Catch: java.lang.Throwable -> L39
                io.netty.channel.nio.AbstractNioChannel r2 = io.netty.channel.nio.AbstractNioChannel.this
                io.netty.util.concurrent.Future r2 = io.netty.channel.nio.AbstractNioChannel.access$400(r2)
                if (r2 == 0) goto L33
            L2a:
                io.netty.channel.nio.AbstractNioChannel r2 = io.netty.channel.nio.AbstractNioChannel.this
                io.netty.util.concurrent.Future r2 = io.netty.channel.nio.AbstractNioChannel.access$400(r2)
                r2.cancel(r0)
            L33:
                io.netty.channel.nio.AbstractNioChannel r0 = io.netty.channel.nio.AbstractNioChannel.this
                io.netty.channel.nio.AbstractNioChannel.access$202(r0, r1)
                goto L56
            L39:
                r2 = move-exception
                io.netty.channel.nio.AbstractNioChannel r3 = io.netty.channel.nio.AbstractNioChannel.this     // Catch: java.lang.Throwable -> L57
                io.netty.channel.ChannelPromise r3 = io.netty.channel.nio.AbstractNioChannel.access$200(r3)     // Catch: java.lang.Throwable -> L57
                io.netty.channel.nio.AbstractNioChannel r4 = io.netty.channel.nio.AbstractNioChannel.this     // Catch: java.lang.Throwable -> L57
                java.net.SocketAddress r4 = io.netty.channel.nio.AbstractNioChannel.access$300(r4)     // Catch: java.lang.Throwable -> L57
                java.lang.Throwable r4 = r5.annotateConnectException(r2, r4)     // Catch: java.lang.Throwable -> L57
                r5.fulfillConnectPromise(r3, r4)     // Catch: java.lang.Throwable -> L57
                io.netty.channel.nio.AbstractNioChannel r2 = io.netty.channel.nio.AbstractNioChannel.this
                io.netty.util.concurrent.Future r2 = io.netty.channel.nio.AbstractNioChannel.access$400(r2)
                if (r2 == 0) goto L33
                goto L2a
            L56:
                return
            L57:
                r2 = move-exception
                io.netty.channel.nio.AbstractNioChannel r3 = io.netty.channel.nio.AbstractNioChannel.this
                io.netty.util.concurrent.Future r3 = io.netty.channel.nio.AbstractNioChannel.access$400(r3)
                if (r3 == 0) goto L69
                io.netty.channel.nio.AbstractNioChannel r3 = io.netty.channel.nio.AbstractNioChannel.this
                io.netty.util.concurrent.Future r3 = io.netty.channel.nio.AbstractNioChannel.access$400(r3)
                r3.cancel(r0)
            L69:
                io.netty.channel.nio.AbstractNioChannel r0 = io.netty.channel.nio.AbstractNioChannel.this
                io.netty.channel.nio.AbstractNioChannel.access$202(r0, r1)
                throw r2
            L6f:
                java.lang.AssertionError r0 = new java.lang.AssertionError
                r0.<init>()
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: io.netty.channel.nio.AbstractNioChannel.AbstractNioUnsafe.finishConnect():void");
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // io.netty.channel.AbstractChannel.AbstractUnsafe
        public final void flush0() {
            if (!isFlushPending()) {
                super.flush0();
            }
        }

        @Override // io.netty.channel.nio.AbstractNioChannel.NioUnsafe
        public final void forceFlush() {
            super.flush0();
        }

        private boolean isFlushPending() {
            SelectionKey selectionKey = AbstractNioChannel.this.selectionKey();
            return selectionKey.isValid() && (selectionKey.interestOps() & 4) != 0;
        }
    }

    @Override // io.netty.channel.AbstractChannel
    protected boolean isCompatible(EventLoop loop) {
        return loop instanceof NioEventLoop;
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doRegister() throws Exception {
        boolean selected = false;
        while (true) {
            try {
                this.selectionKey = mo214javaChannel().register(eventLoop().unwrappedSelector(), 0, this);
                return;
            } catch (CancelledKeyException e) {
                if (!selected) {
                    eventLoop().selectNow();
                    selected = true;
                } else {
                    throw e;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.AbstractChannel
    public void doDeregister() throws Exception {
        eventLoop().cancel(selectionKey());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.AbstractChannel
    public void doBeginRead() throws Exception {
        SelectionKey selectionKey = this.selectionKey;
        if (!selectionKey.isValid()) {
            return;
        }
        this.readPending = true;
        int interestOps = selectionKey.interestOps();
        if ((this.readInterestOp & interestOps) == 0) {
            selectionKey.interestOps(this.readInterestOp | interestOps);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final ByteBuf newDirectBuffer(ByteBuf buf) {
        int readableBytes = buf.readableBytes();
        if (readableBytes == 0) {
            ReferenceCountUtil.safeRelease(buf);
            return Unpooled.EMPTY_BUFFER;
        }
        ByteBufAllocator alloc = alloc();
        if (alloc.isDirectBufferPooled()) {
            ByteBuf directBuf = alloc.directBuffer(readableBytes);
            directBuf.writeBytes(buf, buf.readerIndex(), readableBytes);
            ReferenceCountUtil.safeRelease(buf);
            return directBuf;
        }
        ByteBuf directBuf2 = ByteBufUtil.threadLocalDirectBuffer();
        if (directBuf2 != null) {
            directBuf2.writeBytes(buf, buf.readerIndex(), readableBytes);
            ReferenceCountUtil.safeRelease(buf);
            return directBuf2;
        }
        return buf;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final ByteBuf newDirectBuffer(ReferenceCounted holder, ByteBuf buf) {
        int readableBytes = buf.readableBytes();
        if (readableBytes == 0) {
            ReferenceCountUtil.safeRelease(holder);
            return Unpooled.EMPTY_BUFFER;
        }
        ByteBufAllocator alloc = alloc();
        if (alloc.isDirectBufferPooled()) {
            ByteBuf directBuf = alloc.directBuffer(readableBytes);
            directBuf.writeBytes(buf, buf.readerIndex(), readableBytes);
            ReferenceCountUtil.safeRelease(holder);
            return directBuf;
        }
        ByteBuf directBuf2 = ByteBufUtil.threadLocalDirectBuffer();
        if (directBuf2 != null) {
            directBuf2.writeBytes(buf, buf.readerIndex(), readableBytes);
            ReferenceCountUtil.safeRelease(holder);
            return directBuf2;
        }
        if (holder != buf) {
            buf.retain();
            ReferenceCountUtil.safeRelease(holder);
        }
        return buf;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.AbstractChannel
    public void doClose() throws Exception {
        ChannelPromise promise = this.connectPromise;
        if (promise != null) {
            promise.tryFailure(new ClosedChannelException());
            this.connectPromise = null;
        }
        Future<?> future = this.connectTimeoutFuture;
        if (future != null) {
            future.cancel(false);
            this.connectTimeoutFuture = null;
        }
    }
}
