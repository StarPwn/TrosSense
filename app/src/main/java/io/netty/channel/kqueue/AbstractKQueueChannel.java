package io.netty.channel.kqueue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ConnectTimeoutException;
import io.netty.channel.EventLoop;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.socket.ChannelInputShutdownEvent;
import io.netty.channel.socket.ChannelInputShutdownReadComplete;
import io.netty.channel.socket.SocketChannelConfig;
import io.netty.channel.unix.FileDescriptor;
import io.netty.channel.unix.UnixChannel;
import io.netty.channel.unix.UnixChannelUtil;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.ObjectUtil;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AlreadyConnectedException;
import java.nio.channels.ConnectionPendingException;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.UnresolvedAddressException;
import java.util.concurrent.TimeUnit;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public abstract class AbstractKQueueChannel extends AbstractChannel implements UnixChannel {
    private static final ChannelMetadata METADATA = new ChannelMetadata(false);
    protected volatile boolean active;
    private ChannelPromise connectPromise;
    private Future<?> connectTimeoutFuture;
    boolean inputClosedSeenErrorOnRead;
    private volatile SocketAddress local;
    private boolean readFilterEnabled;
    boolean readReadyRunnablePending;
    private volatile SocketAddress remote;
    private SocketAddress requestedRemoteAddress;
    final BsdSocket socket;
    private boolean writeFilterEnabled;

    @Override // io.netty.channel.Channel
    public abstract KQueueChannelConfig config();

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.AbstractChannel
    public abstract AbstractKQueueUnsafe newUnsafe();

    /* JADX INFO: Access modifiers changed from: package-private */
    public AbstractKQueueChannel(Channel parent, BsdSocket fd, boolean active) {
        super(parent);
        this.socket = (BsdSocket) ObjectUtil.checkNotNull(fd, "fd");
        this.active = active;
        if (active) {
            this.local = fd.localAddress();
            this.remote = fd.remoteAddress();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AbstractKQueueChannel(Channel parent, BsdSocket fd, SocketAddress remote) {
        super(parent);
        this.socket = (BsdSocket) ObjectUtil.checkNotNull(fd, "fd");
        this.active = true;
        this.remote = remote;
        this.local = fd.localAddress();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isSoErrorZero(BsdSocket fd) {
        try {
            return fd.getSoError() == 0;
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override // io.netty.channel.unix.UnixChannel
    public final FileDescriptor fd() {
        return this.socket;
    }

    @Override // io.netty.channel.Channel
    public boolean isActive() {
        return this.active;
    }

    @Override // io.netty.channel.Channel
    public ChannelMetadata metadata() {
        return METADATA;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.AbstractChannel
    public void doClose() throws Exception {
        this.active = false;
        this.inputClosedSeenErrorOnRead = true;
        this.socket.close();
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doDisconnect() throws Exception {
        doClose();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetCachedAddresses() {
        this.local = this.socket.localAddress();
        this.remote = this.socket.remoteAddress();
    }

    @Override // io.netty.channel.AbstractChannel
    protected boolean isCompatible(EventLoop loop) {
        return loop instanceof KQueueEventLoop;
    }

    @Override // io.netty.channel.Channel
    public boolean isOpen() {
        return this.socket.isOpen();
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doDeregister() throws Exception {
        ((KQueueEventLoop) eventLoop()).remove(this);
        this.readFilterEnabled = false;
        this.writeFilterEnabled = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregisterFilters() throws Exception {
        readFilter(false);
        writeFilter(false);
        clearRdHup0();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearRdHup0() {
        evSet0(Native.EVFILT_SOCK, Native.EV_DELETE_DISABLE, Native.NOTE_RDHUP);
    }

    @Override // io.netty.channel.AbstractChannel
    protected final void doBeginRead() throws Exception {
        AbstractKQueueUnsafe unsafe = (AbstractKQueueUnsafe) unsafe();
        unsafe.readPending = true;
        readFilter(true);
        if (unsafe.maybeMoreDataToRead) {
            unsafe.executeReadReadyRunnable(config());
        }
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doRegister() throws Exception {
        this.readReadyRunnablePending = false;
        ((KQueueEventLoop) eventLoop()).add(this);
        if (this.writeFilterEnabled) {
            evSet0(Native.EVFILT_WRITE, Native.EV_ADD_CLEAR_ENABLE);
        }
        if (this.readFilterEnabled) {
            evSet0(Native.EVFILT_READ, Native.EV_ADD_CLEAR_ENABLE);
        }
        evSet0(Native.EVFILT_SOCK, Native.EV_ADD, Native.NOTE_RDHUP);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final ByteBuf newDirectBuffer(ByteBuf buf) {
        return newDirectBuffer(buf, buf);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final ByteBuf newDirectBuffer(Object holder, ByteBuf buf) {
        int readableBytes = buf.readableBytes();
        if (readableBytes == 0) {
            ReferenceCountUtil.release(holder);
            return Unpooled.EMPTY_BUFFER;
        }
        ByteBufAllocator alloc = alloc();
        if (alloc.isDirectBufferPooled()) {
            return newDirectBuffer0(holder, buf, alloc, readableBytes);
        }
        ByteBuf directBuf = ByteBufUtil.threadLocalDirectBuffer();
        if (directBuf == null) {
            return newDirectBuffer0(holder, buf, alloc, readableBytes);
        }
        directBuf.writeBytes(buf, buf.readerIndex(), readableBytes);
        ReferenceCountUtil.safeRelease(holder);
        return directBuf;
    }

    private static ByteBuf newDirectBuffer0(Object holder, ByteBuf buf, ByteBufAllocator alloc, int capacity) {
        ByteBuf directBuf = alloc.directBuffer(capacity);
        directBuf.writeBytes(buf, buf.readerIndex(), capacity);
        ReferenceCountUtil.safeRelease(holder);
        return directBuf;
    }

    protected static void checkResolvable(InetSocketAddress addr) {
        if (addr.isUnresolved()) {
            throw new UnresolvedAddressException();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final int doReadBytes(ByteBuf byteBuf) throws Exception {
        int localReadAmount;
        int writerIndex = byteBuf.writerIndex();
        unsafe().recvBufAllocHandle().attemptedBytesRead(byteBuf.writableBytes());
        if (byteBuf.hasMemoryAddress()) {
            localReadAmount = this.socket.readAddress(byteBuf.memoryAddress(), writerIndex, byteBuf.capacity());
        } else {
            int localReadAmount2 = byteBuf.writableBytes();
            ByteBuffer buf = byteBuf.internalNioBuffer(writerIndex, localReadAmount2);
            localReadAmount = this.socket.read(buf, buf.position(), buf.limit());
        }
        if (localReadAmount > 0) {
            byteBuf.writerIndex(writerIndex + localReadAmount);
        }
        return localReadAmount;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final int doWriteBytes(ChannelOutboundBuffer in, ByteBuf buf) throws Exception {
        if (buf.hasMemoryAddress()) {
            int localFlushedAmount = this.socket.writeAddress(buf.memoryAddress(), buf.readerIndex(), buf.writerIndex());
            if (localFlushedAmount > 0) {
                in.removeBytes(localFlushedAmount);
                return 1;
            }
            return Integer.MAX_VALUE;
        }
        ByteBuffer nioBuf = buf.nioBufferCount() == 1 ? buf.internalNioBuffer(buf.readerIndex(), buf.readableBytes()) : buf.nioBuffer();
        int localFlushedAmount2 = this.socket.write(nioBuf, nioBuf.position(), nioBuf.limit());
        if (localFlushedAmount2 > 0) {
            nioBuf.position(nioBuf.position() + localFlushedAmount2);
            in.removeBytes(localFlushedAmount2);
            return 1;
        }
        return Integer.MAX_VALUE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean shouldBreakReadReady(ChannelConfig config) {
        return this.socket.isInputShutdown() && (this.inputClosedSeenErrorOnRead || !isAllowHalfClosure(config));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isAllowHalfClosure(ChannelConfig config) {
        if (config instanceof KQueueDomainSocketChannelConfig) {
            return ((KQueueDomainSocketChannelConfig) config).isAllowHalfClosure();
        }
        return (config instanceof SocketChannelConfig) && ((SocketChannelConfig) config).isAllowHalfClosure();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void clearReadFilter() {
        if (isRegistered()) {
            EventLoop loop = eventLoop();
            final AbstractKQueueUnsafe unsafe = (AbstractKQueueUnsafe) unsafe();
            if (loop.inEventLoop()) {
                unsafe.clearReadFilter0();
                return;
            } else {
                loop.execute(new Runnable() { // from class: io.netty.channel.kqueue.AbstractKQueueChannel.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (!unsafe.readPending && !AbstractKQueueChannel.this.config().isAutoRead()) {
                            unsafe.clearReadFilter0();
                        }
                    }
                });
                return;
            }
        }
        this.readFilterEnabled = false;
    }

    void readFilter(boolean readFilterEnabled) throws IOException {
        if (this.readFilterEnabled != readFilterEnabled) {
            this.readFilterEnabled = readFilterEnabled;
            evSet(Native.EVFILT_READ, readFilterEnabled ? Native.EV_ADD_CLEAR_ENABLE : Native.EV_DELETE_DISABLE);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void writeFilter(boolean writeFilterEnabled) throws IOException {
        if (this.writeFilterEnabled != writeFilterEnabled) {
            this.writeFilterEnabled = writeFilterEnabled;
            evSet(Native.EVFILT_WRITE, writeFilterEnabled ? Native.EV_ADD_CLEAR_ENABLE : Native.EV_DELETE_DISABLE);
        }
    }

    private void evSet(short filter, short flags) {
        if (isRegistered()) {
            evSet0(filter, flags);
        }
    }

    private void evSet0(short filter, short flags) {
        evSet0(filter, flags, 0);
    }

    private void evSet0(short filter, short flags, int fflags) {
        if (isOpen()) {
            ((KQueueEventLoop) eventLoop()).evSet(this, filter, flags, fflags);
        }
    }

    /* loaded from: classes4.dex */
    public abstract class AbstractKQueueUnsafe extends AbstractChannel.AbstractUnsafe {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private KQueueRecvByteAllocatorHandle allocHandle;
        boolean maybeMoreDataToRead;
        boolean readPending;
        private final Runnable readReadyRunnable;

        abstract void readReady(KQueueRecvByteAllocatorHandle kQueueRecvByteAllocatorHandle);

        @Override // io.netty.channel.AbstractChannel.AbstractUnsafe, io.netty.channel.Channel.Unsafe
        public /* bridge */ /* synthetic */ void close(ChannelPromise x0) {
            super.close(x0);
        }

        public AbstractKQueueUnsafe() {
            super();
            this.readReadyRunnable = new Runnable() { // from class: io.netty.channel.kqueue.AbstractKQueueChannel.AbstractKQueueUnsafe.1
                @Override // java.lang.Runnable
                public void run() {
                    AbstractKQueueChannel.this.readReadyRunnablePending = false;
                    AbstractKQueueUnsafe.this.readReady(AbstractKQueueUnsafe.this.recvBufAllocHandle());
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public final void readReady(long numberBytesPending) {
            KQueueRecvByteAllocatorHandle allocHandle = recvBufAllocHandle();
            allocHandle.numberBytesPending(numberBytesPending);
            readReady(allocHandle);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public final void readReadyBefore() {
            this.maybeMoreDataToRead = false;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public final void readReadyFinally(ChannelConfig config) {
            this.maybeMoreDataToRead = this.allocHandle.maybeMoreDataToRead();
            if (this.allocHandle.isReadEOF() || (this.readPending && this.maybeMoreDataToRead)) {
                executeReadReadyRunnable(config);
            } else if (!this.readPending && !config.isAutoRead()) {
                clearReadFilter0();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public final boolean failConnectPromise(Throwable cause) {
            if (AbstractKQueueChannel.this.connectPromise != null) {
                ChannelPromise connectPromise = AbstractKQueueChannel.this.connectPromise;
                AbstractKQueueChannel.this.connectPromise = null;
                if (connectPromise.tryFailure(cause instanceof ConnectException ? cause : new ConnectException("failed to connect").initCause(cause))) {
                    closeIfClosed();
                    return true;
                }
                return false;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public final void writeReady() {
            if (AbstractKQueueChannel.this.connectPromise != null) {
                finishConnect();
            } else if (!AbstractKQueueChannel.this.socket.isOutputShutdown()) {
                super.flush0();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void shutdownInput(boolean readEOF) {
            if (readEOF && AbstractKQueueChannel.this.connectPromise != null) {
                finishConnect();
            }
            if (!AbstractKQueueChannel.this.socket.isInputShutdown()) {
                if (AbstractKQueueChannel.isAllowHalfClosure(AbstractKQueueChannel.this.config())) {
                    try {
                        AbstractKQueueChannel.this.socket.shutdown(true, false);
                    } catch (IOException e) {
                        fireEventAndClose(ChannelInputShutdownEvent.INSTANCE);
                        return;
                    } catch (NotYetConnectedException e2) {
                    }
                    clearReadFilter0();
                    AbstractKQueueChannel.this.pipeline().fireUserEventTriggered((Object) ChannelInputShutdownEvent.INSTANCE);
                    return;
                }
                close(voidPromise());
                return;
            }
            if (!readEOF && !AbstractKQueueChannel.this.inputClosedSeenErrorOnRead) {
                AbstractKQueueChannel.this.inputClosedSeenErrorOnRead = true;
                AbstractKQueueChannel.this.pipeline().fireUserEventTriggered((Object) ChannelInputShutdownReadComplete.INSTANCE);
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public final void readEOF() {
            KQueueRecvByteAllocatorHandle allocHandle = recvBufAllocHandle();
            allocHandle.readEOF();
            if (AbstractKQueueChannel.this.isActive()) {
                readReady(allocHandle);
            } else {
                shutdownInput(true);
            }
            AbstractKQueueChannel.this.clearRdHup0();
        }

        @Override // io.netty.channel.AbstractChannel.AbstractUnsafe, io.netty.channel.Channel.Unsafe
        public KQueueRecvByteAllocatorHandle recvBufAllocHandle() {
            if (this.allocHandle == null) {
                this.allocHandle = new KQueueRecvByteAllocatorHandle((RecvByteBufAllocator.ExtendedHandle) super.recvBufAllocHandle());
            }
            return this.allocHandle;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // io.netty.channel.AbstractChannel.AbstractUnsafe
        public final void flush0() {
            if (!AbstractKQueueChannel.this.writeFilterEnabled) {
                super.flush0();
            }
        }

        final void executeReadReadyRunnable(ChannelConfig config) {
            if (AbstractKQueueChannel.this.readReadyRunnablePending || !AbstractKQueueChannel.this.isActive() || AbstractKQueueChannel.this.shouldBreakReadReady(config)) {
                return;
            }
            AbstractKQueueChannel.this.readReadyRunnablePending = true;
            AbstractKQueueChannel.this.eventLoop().execute(this.readReadyRunnable);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public final void clearReadFilter0() {
            if (!AbstractKQueueChannel.this.eventLoop().inEventLoop()) {
                throw new AssertionError();
            }
            try {
                this.readPending = false;
                AbstractKQueueChannel.this.readFilter(false);
            } catch (IOException e) {
                AbstractKQueueChannel.this.pipeline().fireExceptionCaught((Throwable) e);
                AbstractKQueueChannel.this.unsafe().close(AbstractKQueueChannel.this.unsafe().voidPromise());
            }
        }

        private void fireEventAndClose(Object evt) {
            AbstractKQueueChannel.this.pipeline().fireUserEventTriggered(evt);
            close(voidPromise());
        }

        @Override // io.netty.channel.Channel.Unsafe
        public void connect(final SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
            if (!promise.isDone() && ensureOpen(promise)) {
                try {
                    if (AbstractKQueueChannel.this.connectPromise != null) {
                        throw new ConnectionPendingException();
                    }
                    boolean wasActive = AbstractKQueueChannel.this.isActive();
                    if (!AbstractKQueueChannel.this.doConnect(remoteAddress, localAddress)) {
                        AbstractKQueueChannel.this.connectPromise = promise;
                        AbstractKQueueChannel.this.requestedRemoteAddress = remoteAddress;
                        final int connectTimeoutMillis = AbstractKQueueChannel.this.config().getConnectTimeoutMillis();
                        if (connectTimeoutMillis > 0) {
                            AbstractKQueueChannel.this.connectTimeoutFuture = AbstractKQueueChannel.this.eventLoop().schedule(new Runnable() { // from class: io.netty.channel.kqueue.AbstractKQueueChannel.AbstractKQueueUnsafe.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    ChannelPromise connectPromise = AbstractKQueueChannel.this.connectPromise;
                                    if (connectPromise != null && !connectPromise.isDone() && connectPromise.tryFailure(new ConnectTimeoutException("connection timed out after " + connectTimeoutMillis + " ms: " + remoteAddress))) {
                                        AbstractKQueueUnsafe.this.close(AbstractKQueueUnsafe.this.voidPromise());
                                    }
                                }
                            }, connectTimeoutMillis, TimeUnit.MILLISECONDS);
                        }
                        promise.addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.channel.kqueue.AbstractKQueueChannel.AbstractKQueueUnsafe.3
                            @Override // io.netty.util.concurrent.GenericFutureListener
                            public void operationComplete(ChannelFuture future) {
                                if (future.isCancelled()) {
                                    if (AbstractKQueueChannel.this.connectTimeoutFuture != null) {
                                        AbstractKQueueChannel.this.connectTimeoutFuture.cancel(false);
                                    }
                                    AbstractKQueueChannel.this.connectPromise = null;
                                    AbstractKQueueUnsafe.this.close(AbstractKQueueUnsafe.this.voidPromise());
                                }
                            }
                        });
                    } else {
                        fulfillConnectPromise(promise, wasActive);
                    }
                } catch (Throwable t) {
                    closeIfClosed();
                    promise.tryFailure(annotateConnectException(t, remoteAddress));
                }
            }
        }

        private void fulfillConnectPromise(ChannelPromise promise, boolean wasActive) {
            if (promise == null) {
                return;
            }
            AbstractKQueueChannel.this.active = true;
            boolean active = AbstractKQueueChannel.this.isActive();
            boolean promiseSet = promise.trySuccess();
            if (!wasActive && active) {
                AbstractKQueueChannel.this.pipeline().fireChannelActive();
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

        /* JADX WARN: Code restructure failed: missing block: B:19:0x0046, code lost:            if (r6.this$0.connectTimeoutFuture != null) goto L18;     */
        /* JADX WARN: Code restructure failed: missing block: B:20:0x0048, code lost:            r6.this$0.connectTimeoutFuture.cancel(false);     */
        /* JADX WARN: Code restructure failed: missing block: B:21:0x0051, code lost:            r6.this$0.connectPromise = null;     */
        /* JADX WARN: Code restructure failed: missing block: B:22:?, code lost:            return;     */
        /* JADX WARN: Code restructure failed: missing block: B:30:0x0073, code lost:            if (r6.this$0.connectTimeoutFuture != null) goto L18;     */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private void finishConnect() {
            /*
                r6 = this;
                io.netty.channel.kqueue.AbstractKQueueChannel r0 = io.netty.channel.kqueue.AbstractKQueueChannel.this
                io.netty.channel.EventLoop r0 = r0.eventLoop()
                boolean r0 = r0.inEventLoop()
                if (r0 == 0) goto L91
                r0 = 0
                r1 = 0
                r2 = 0
                io.netty.channel.kqueue.AbstractKQueueChannel r3 = io.netty.channel.kqueue.AbstractKQueueChannel.this     // Catch: java.lang.Throwable -> L57
                boolean r3 = r3.isActive()     // Catch: java.lang.Throwable -> L57
                boolean r4 = r6.doFinishConnect()     // Catch: java.lang.Throwable -> L57
                if (r4 != 0) goto L35
                r0 = 1
                if (r0 != 0) goto L34
                io.netty.channel.kqueue.AbstractKQueueChannel r4 = io.netty.channel.kqueue.AbstractKQueueChannel.this
                io.netty.util.concurrent.Future r4 = io.netty.channel.kqueue.AbstractKQueueChannel.access$500(r4)
                if (r4 == 0) goto L2f
                io.netty.channel.kqueue.AbstractKQueueChannel r4 = io.netty.channel.kqueue.AbstractKQueueChannel.this
                io.netty.util.concurrent.Future r4 = io.netty.channel.kqueue.AbstractKQueueChannel.access$500(r4)
                r4.cancel(r1)
            L2f:
                io.netty.channel.kqueue.AbstractKQueueChannel r1 = io.netty.channel.kqueue.AbstractKQueueChannel.this
                io.netty.channel.kqueue.AbstractKQueueChannel.access$002(r1, r2)
            L34:
                return
            L35:
                io.netty.channel.kqueue.AbstractKQueueChannel r4 = io.netty.channel.kqueue.AbstractKQueueChannel.this     // Catch: java.lang.Throwable -> L57
                io.netty.channel.ChannelPromise r4 = io.netty.channel.kqueue.AbstractKQueueChannel.access$000(r4)     // Catch: java.lang.Throwable -> L57
                r6.fulfillConnectPromise(r4, r3)     // Catch: java.lang.Throwable -> L57
                if (r0 != 0) goto L76
                io.netty.channel.kqueue.AbstractKQueueChannel r3 = io.netty.channel.kqueue.AbstractKQueueChannel.this
                io.netty.util.concurrent.Future r3 = io.netty.channel.kqueue.AbstractKQueueChannel.access$500(r3)
                if (r3 == 0) goto L51
            L48:
                io.netty.channel.kqueue.AbstractKQueueChannel r3 = io.netty.channel.kqueue.AbstractKQueueChannel.this
                io.netty.util.concurrent.Future r3 = io.netty.channel.kqueue.AbstractKQueueChannel.access$500(r3)
                r3.cancel(r1)
            L51:
                io.netty.channel.kqueue.AbstractKQueueChannel r1 = io.netty.channel.kqueue.AbstractKQueueChannel.this
                io.netty.channel.kqueue.AbstractKQueueChannel.access$002(r1, r2)
                goto L76
            L57:
                r3 = move-exception
                io.netty.channel.kqueue.AbstractKQueueChannel r4 = io.netty.channel.kqueue.AbstractKQueueChannel.this     // Catch: java.lang.Throwable -> L77
                io.netty.channel.ChannelPromise r4 = io.netty.channel.kqueue.AbstractKQueueChannel.access$000(r4)     // Catch: java.lang.Throwable -> L77
                io.netty.channel.kqueue.AbstractKQueueChannel r5 = io.netty.channel.kqueue.AbstractKQueueChannel.this     // Catch: java.lang.Throwable -> L77
                java.net.SocketAddress r5 = io.netty.channel.kqueue.AbstractKQueueChannel.access$400(r5)     // Catch: java.lang.Throwable -> L77
                java.lang.Throwable r5 = r6.annotateConnectException(r3, r5)     // Catch: java.lang.Throwable -> L77
                r6.fulfillConnectPromise(r4, r5)     // Catch: java.lang.Throwable -> L77
                if (r0 != 0) goto L76
                io.netty.channel.kqueue.AbstractKQueueChannel r3 = io.netty.channel.kqueue.AbstractKQueueChannel.this
                io.netty.util.concurrent.Future r3 = io.netty.channel.kqueue.AbstractKQueueChannel.access$500(r3)
                if (r3 == 0) goto L51
                goto L48
            L76:
                return
            L77:
                r3 = move-exception
                if (r0 != 0) goto L90
                io.netty.channel.kqueue.AbstractKQueueChannel r4 = io.netty.channel.kqueue.AbstractKQueueChannel.this
                io.netty.util.concurrent.Future r4 = io.netty.channel.kqueue.AbstractKQueueChannel.access$500(r4)
                if (r4 == 0) goto L8b
                io.netty.channel.kqueue.AbstractKQueueChannel r4 = io.netty.channel.kqueue.AbstractKQueueChannel.this
                io.netty.util.concurrent.Future r4 = io.netty.channel.kqueue.AbstractKQueueChannel.access$500(r4)
                r4.cancel(r1)
            L8b:
                io.netty.channel.kqueue.AbstractKQueueChannel r1 = io.netty.channel.kqueue.AbstractKQueueChannel.this
                io.netty.channel.kqueue.AbstractKQueueChannel.access$002(r1, r2)
            L90:
                throw r3
            L91:
                java.lang.AssertionError r0 = new java.lang.AssertionError
                r0.<init>()
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: io.netty.channel.kqueue.AbstractKQueueChannel.AbstractKQueueUnsafe.finishConnect():void");
        }

        private boolean doFinishConnect() throws Exception {
            if (AbstractKQueueChannel.this.socket.finishConnect()) {
                AbstractKQueueChannel.this.writeFilter(false);
                if (AbstractKQueueChannel.this.requestedRemoteAddress instanceof InetSocketAddress) {
                    AbstractKQueueChannel.this.remote = UnixChannelUtil.computeRemoteAddr((InetSocketAddress) AbstractKQueueChannel.this.requestedRemoteAddress, AbstractKQueueChannel.this.socket.remoteAddress());
                }
                AbstractKQueueChannel.this.requestedRemoteAddress = null;
                return true;
            }
            AbstractKQueueChannel.this.writeFilter(true);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.AbstractChannel
    public void doBind(SocketAddress local) throws Exception {
        if (local instanceof InetSocketAddress) {
            checkResolvable((InetSocketAddress) local);
        }
        this.socket.bind(local);
        this.local = this.socket.localAddress();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
        if (localAddress instanceof InetSocketAddress) {
            checkResolvable((InetSocketAddress) localAddress);
        }
        InetSocketAddress remoteSocketAddr = remoteAddress instanceof InetSocketAddress ? (InetSocketAddress) remoteAddress : null;
        if (remoteSocketAddr != null) {
            checkResolvable(remoteSocketAddr);
        }
        if (this.remote != null) {
            throw new AlreadyConnectedException();
        }
        if (localAddress != null) {
            this.socket.bind(localAddress);
        }
        boolean connected = doConnect0(remoteAddress, localAddress);
        if (connected) {
            this.remote = remoteSocketAddr == null ? remoteAddress : UnixChannelUtil.computeRemoteAddr(remoteSocketAddr, this.socket.remoteAddress());
        }
        this.local = this.socket.localAddress();
        return connected;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean doConnect0(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
        boolean success = false;
        try {
            boolean connected = this.socket.connect(remoteAddress);
            if (!connected) {
                writeFilter(true);
            }
            success = true;
            return connected;
        } finally {
            if (!success) {
                doClose();
            }
        }
    }

    @Override // io.netty.channel.AbstractChannel
    protected SocketAddress localAddress0() {
        return this.local;
    }

    @Override // io.netty.channel.AbstractChannel
    protected SocketAddress remoteAddress0() {
        return this.remote;
    }
}
