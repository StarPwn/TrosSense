package io.netty.channel.epoll;

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
import io.netty.channel.unix.IovArray;
import io.netty.channel.unix.Socket;
import io.netty.channel.unix.UnixChannel;
import io.netty.channel.unix.UnixChannelUtil;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.ObjectUtil;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AlreadyConnectedException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ConnectionPendingException;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.UnresolvedAddressException;
import java.util.concurrent.TimeUnit;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public abstract class AbstractEpollChannel extends AbstractChannel implements UnixChannel {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final ChannelMetadata METADATA = new ChannelMetadata(false);
    protected volatile boolean active;
    private ChannelPromise connectPromise;
    private Future<?> connectTimeoutFuture;
    boolean epollInReadyRunnablePending;
    protected int flags;
    boolean inputClosedSeenErrorOnRead;
    private volatile SocketAddress local;
    private volatile SocketAddress remote;
    private SocketAddress requestedRemoteAddress;
    protected final LinuxSocket socket;

    @Override // io.netty.channel.Channel
    public abstract EpollChannelConfig config();

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.AbstractChannel
    public abstract AbstractEpollUnsafe newUnsafe();

    AbstractEpollChannel(LinuxSocket fd) {
        this((Channel) null, fd, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AbstractEpollChannel(Channel parent, LinuxSocket fd, boolean active) {
        super(parent);
        this.flags = Native.EPOLLET;
        this.socket = (LinuxSocket) ObjectUtil.checkNotNull(fd, "fd");
        this.active = active;
        if (active) {
            this.local = fd.localAddress();
            this.remote = fd.remoteAddress();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AbstractEpollChannel(Channel parent, LinuxSocket fd, SocketAddress remote) {
        super(parent);
        this.flags = Native.EPOLLET;
        this.socket = (LinuxSocket) ObjectUtil.checkNotNull(fd, "fd");
        this.active = true;
        this.remote = remote;
        this.local = fd.localAddress();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isSoErrorZero(Socket fd) {
        try {
            return fd.getSoError() == 0;
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setFlag(int flag) throws IOException {
        if (!isFlagSet(flag)) {
            this.flags |= flag;
            modifyEvents();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearFlag(int flag) throws IOException {
        if (isFlagSet(flag)) {
            this.flags &= ~flag;
            modifyEvents();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isFlagSet(int flag) {
        return (this.flags & flag) != 0;
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
        try {
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
            if (isRegistered()) {
                EventLoop loop = eventLoop();
                if (loop.inEventLoop()) {
                    doDeregister();
                } else {
                    loop.execute(new Runnable() { // from class: io.netty.channel.epoll.AbstractEpollChannel.1
                        @Override // java.lang.Runnable
                        public void run() {
                            try {
                                AbstractEpollChannel.this.doDeregister();
                            } catch (Throwable cause) {
                                AbstractEpollChannel.this.pipeline().fireExceptionCaught(cause);
                            }
                        }
                    });
                }
            }
        } finally {
            this.socket.close();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetCachedAddresses() {
        this.local = this.socket.localAddress();
        this.remote = this.socket.remoteAddress();
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doDisconnect() throws Exception {
        doClose();
    }

    @Override // io.netty.channel.AbstractChannel
    protected boolean isCompatible(EventLoop loop) {
        return loop instanceof EpollEventLoop;
    }

    @Override // io.netty.channel.Channel
    public boolean isOpen() {
        return this.socket.isOpen();
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doDeregister() throws Exception {
        ((EpollEventLoop) eventLoop()).remove(this);
    }

    @Override // io.netty.channel.AbstractChannel
    protected final void doBeginRead() throws Exception {
        AbstractEpollUnsafe unsafe = (AbstractEpollUnsafe) unsafe();
        unsafe.readPending = true;
        setFlag(Native.EPOLLIN);
        if (unsafe.maybeMoreDataToRead) {
            unsafe.executeEpollInReadyRunnable(config());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean shouldBreakEpollInReady(ChannelConfig config) {
        return this.socket.isInputShutdown() && (this.inputClosedSeenErrorOnRead || !isAllowHalfClosure(config));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isAllowHalfClosure(ChannelConfig config) {
        if (config instanceof EpollDomainSocketChannelConfig) {
            return ((EpollDomainSocketChannelConfig) config).isAllowHalfClosure();
        }
        return (config instanceof SocketChannelConfig) && ((SocketChannelConfig) config).isAllowHalfClosure();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void clearEpollIn() {
        if (isRegistered()) {
            EventLoop loop = eventLoop();
            final AbstractEpollUnsafe unsafe = (AbstractEpollUnsafe) unsafe();
            if (loop.inEventLoop()) {
                unsafe.clearEpollIn0();
                return;
            } else {
                loop.execute(new Runnable() { // from class: io.netty.channel.epoll.AbstractEpollChannel.2
                    @Override // java.lang.Runnable
                    public void run() {
                        if (!unsafe.readPending && !AbstractEpollChannel.this.config().isAutoRead()) {
                            unsafe.clearEpollIn0();
                        }
                    }
                });
                return;
            }
        }
        this.flags &= ~Native.EPOLLIN;
    }

    private void modifyEvents() throws IOException {
        if (isOpen() && isRegistered()) {
            ((EpollEventLoop) eventLoop()).modify(this);
        }
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doRegister() throws Exception {
        this.epollInReadyRunnablePending = false;
        ((EpollEventLoop) eventLoop()).add(this);
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
            localReadAmount = this.socket.recvAddress(byteBuf.memoryAddress(), writerIndex, byteBuf.capacity());
        } else {
            int localReadAmount2 = byteBuf.writableBytes();
            ByteBuffer buf = byteBuf.internalNioBuffer(writerIndex, localReadAmount2);
            localReadAmount = this.socket.recv(buf, buf.position(), buf.limit());
        }
        if (localReadAmount > 0) {
            byteBuf.writerIndex(writerIndex + localReadAmount);
        }
        return localReadAmount;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final int doWriteBytes(ChannelOutboundBuffer in, ByteBuf buf) throws Exception {
        if (buf.hasMemoryAddress()) {
            int localFlushedAmount = this.socket.sendAddress(buf.memoryAddress(), buf.readerIndex(), buf.writerIndex());
            if (localFlushedAmount > 0) {
                in.removeBytes(localFlushedAmount);
                return 1;
            }
            return Integer.MAX_VALUE;
        }
        ByteBuffer nioBuf = buf.nioBufferCount() == 1 ? buf.internalNioBuffer(buf.readerIndex(), buf.readableBytes()) : buf.nioBuffer();
        int localFlushedAmount2 = this.socket.send(nioBuf, nioBuf.position(), nioBuf.limit());
        if (localFlushedAmount2 > 0) {
            nioBuf.position(nioBuf.position() + localFlushedAmount2);
            in.removeBytes(localFlushedAmount2);
            return 1;
        }
        return Integer.MAX_VALUE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final long doWriteOrSendBytes(ByteBuf data, InetSocketAddress remoteAddress, boolean fastOpen) throws IOException {
        if (fastOpen && remoteAddress == null) {
            throw new AssertionError("fastOpen requires a remote address");
        }
        if (data.hasMemoryAddress()) {
            long memoryAddress = data.memoryAddress();
            if (remoteAddress == null) {
                return this.socket.sendAddress(memoryAddress, data.readerIndex(), data.writerIndex());
            }
            return this.socket.sendToAddress(memoryAddress, data.readerIndex(), data.writerIndex(), remoteAddress.getAddress(), remoteAddress.getPort(), fastOpen);
        }
        if (data.nioBufferCount() > 1) {
            IovArray array = ((EpollEventLoop) eventLoop()).cleanIovArray();
            array.add(data, data.readerIndex(), data.readableBytes());
            int cnt = array.count();
            if (cnt == 0) {
                throw new AssertionError();
            }
            if (remoteAddress == null) {
                return this.socket.writevAddresses(array.memoryAddress(0), cnt);
            }
            return this.socket.sendToAddresses(array.memoryAddress(0), cnt, remoteAddress.getAddress(), remoteAddress.getPort(), fastOpen);
        }
        ByteBuffer nioData = data.internalNioBuffer(data.readerIndex(), data.readableBytes());
        if (remoteAddress == null) {
            return this.socket.send(nioData, nioData.position(), nioData.limit());
        }
        return this.socket.sendTo(nioData, nioData.position(), nioData.limit(), remoteAddress.getAddress(), remoteAddress.getPort(), fastOpen);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public abstract class AbstractEpollUnsafe extends AbstractChannel.AbstractUnsafe {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private EpollRecvByteAllocatorHandle allocHandle;
        private final Runnable epollInReadyRunnable;
        boolean maybeMoreDataToRead;
        boolean readPending;

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract void epollInReady();

        /* JADX INFO: Access modifiers changed from: protected */
        public AbstractEpollUnsafe() {
            super();
            this.epollInReadyRunnable = new Runnable() { // from class: io.netty.channel.epoll.AbstractEpollChannel.AbstractEpollUnsafe.1
                @Override // java.lang.Runnable
                public void run() {
                    AbstractEpollChannel.this.epollInReadyRunnablePending = false;
                    AbstractEpollUnsafe.this.epollInReady();
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public final void epollInBefore() {
            this.maybeMoreDataToRead = false;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public final void epollInFinally(ChannelConfig config) {
            this.maybeMoreDataToRead = this.allocHandle.maybeMoreDataToRead();
            if (this.allocHandle.isReceivedRdHup() || (this.readPending && this.maybeMoreDataToRead)) {
                executeEpollInReadyRunnable(config);
            } else if (!this.readPending && !config.isAutoRead()) {
                AbstractEpollChannel.this.clearEpollIn();
            }
        }

        final void executeEpollInReadyRunnable(ChannelConfig config) {
            if (AbstractEpollChannel.this.epollInReadyRunnablePending || !AbstractEpollChannel.this.isActive() || AbstractEpollChannel.this.shouldBreakEpollInReady(config)) {
                return;
            }
            AbstractEpollChannel.this.epollInReadyRunnablePending = true;
            AbstractEpollChannel.this.eventLoop().execute(this.epollInReadyRunnable);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public final void epollRdHupReady() {
            recvBufAllocHandle().receivedRdHup();
            if (AbstractEpollChannel.this.isActive()) {
                epollInReady();
            } else {
                shutdownInput(true);
            }
            clearEpollRdHup();
        }

        private void clearEpollRdHup() {
            try {
                AbstractEpollChannel.this.clearFlag(Native.EPOLLRDHUP);
            } catch (IOException e) {
                AbstractEpollChannel.this.pipeline().fireExceptionCaught((Throwable) e);
                close(voidPromise());
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void shutdownInput(boolean rdHup) {
            if (!AbstractEpollChannel.this.socket.isInputShutdown()) {
                if (AbstractEpollChannel.isAllowHalfClosure(AbstractEpollChannel.this.config())) {
                    try {
                        AbstractEpollChannel.this.socket.shutdown(true, false);
                    } catch (IOException e) {
                        fireEventAndClose(ChannelInputShutdownEvent.INSTANCE);
                        return;
                    } catch (NotYetConnectedException e2) {
                    }
                    clearEpollIn0();
                    AbstractEpollChannel.this.pipeline().fireUserEventTriggered((Object) ChannelInputShutdownEvent.INSTANCE);
                    return;
                }
                close(voidPromise());
                return;
            }
            if (!rdHup && !AbstractEpollChannel.this.inputClosedSeenErrorOnRead) {
                AbstractEpollChannel.this.inputClosedSeenErrorOnRead = true;
                AbstractEpollChannel.this.pipeline().fireUserEventTriggered((Object) ChannelInputShutdownReadComplete.INSTANCE);
            }
        }

        private void fireEventAndClose(Object evt) {
            AbstractEpollChannel.this.pipeline().fireUserEventTriggered(evt);
            close(voidPromise());
        }

        @Override // io.netty.channel.AbstractChannel.AbstractUnsafe, io.netty.channel.Channel.Unsafe
        public EpollRecvByteAllocatorHandle recvBufAllocHandle() {
            if (this.allocHandle == null) {
                this.allocHandle = newEpollHandle((RecvByteBufAllocator.ExtendedHandle) super.recvBufAllocHandle());
            }
            return this.allocHandle;
        }

        EpollRecvByteAllocatorHandle newEpollHandle(RecvByteBufAllocator.ExtendedHandle handle) {
            return new EpollRecvByteAllocatorHandle(handle);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // io.netty.channel.AbstractChannel.AbstractUnsafe
        public final void flush0() {
            if (!AbstractEpollChannel.this.isFlagSet(Native.EPOLLOUT)) {
                super.flush0();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public final void epollOutReady() {
            if (AbstractEpollChannel.this.connectPromise != null) {
                finishConnect();
            } else if (!AbstractEpollChannel.this.socket.isOutputShutdown()) {
                super.flush0();
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public final void clearEpollIn0() {
            if (!AbstractEpollChannel.this.eventLoop().inEventLoop()) {
                throw new AssertionError();
            }
            try {
                this.readPending = false;
                AbstractEpollChannel.this.clearFlag(Native.EPOLLIN);
            } catch (IOException e) {
                AbstractEpollChannel.this.pipeline().fireExceptionCaught((Throwable) e);
                AbstractEpollChannel.this.unsafe().close(AbstractEpollChannel.this.unsafe().voidPromise());
            }
        }

        @Override // io.netty.channel.Channel.Unsafe
        public void connect(final SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
            if (!promise.isDone() && ensureOpen(promise)) {
                try {
                    if (AbstractEpollChannel.this.connectPromise != null) {
                        throw new ConnectionPendingException();
                    }
                    boolean wasActive = AbstractEpollChannel.this.isActive();
                    if (!AbstractEpollChannel.this.doConnect(remoteAddress, localAddress)) {
                        AbstractEpollChannel.this.connectPromise = promise;
                        AbstractEpollChannel.this.requestedRemoteAddress = remoteAddress;
                        final int connectTimeoutMillis = AbstractEpollChannel.this.config().getConnectTimeoutMillis();
                        if (connectTimeoutMillis > 0) {
                            AbstractEpollChannel.this.connectTimeoutFuture = AbstractEpollChannel.this.eventLoop().schedule(new Runnable() { // from class: io.netty.channel.epoll.AbstractEpollChannel.AbstractEpollUnsafe.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    ChannelPromise connectPromise = AbstractEpollChannel.this.connectPromise;
                                    if (connectPromise != null && !connectPromise.isDone() && connectPromise.tryFailure(new ConnectTimeoutException("connection timed out after " + connectTimeoutMillis + " ms: " + remoteAddress))) {
                                        AbstractEpollUnsafe.this.close(AbstractEpollUnsafe.this.voidPromise());
                                    }
                                }
                            }, connectTimeoutMillis, TimeUnit.MILLISECONDS);
                        }
                        promise.addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.channel.epoll.AbstractEpollChannel.AbstractEpollUnsafe.3
                            @Override // io.netty.util.concurrent.GenericFutureListener
                            public void operationComplete(ChannelFuture future) {
                                if (future.isCancelled()) {
                                    if (AbstractEpollChannel.this.connectTimeoutFuture != null) {
                                        AbstractEpollChannel.this.connectTimeoutFuture.cancel(false);
                                    }
                                    AbstractEpollChannel.this.connectPromise = null;
                                    AbstractEpollUnsafe.this.close(AbstractEpollUnsafe.this.voidPromise());
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
            AbstractEpollChannel.this.active = true;
            boolean active = AbstractEpollChannel.this.isActive();
            boolean promiseSet = promise.trySuccess();
            if (!wasActive && active) {
                AbstractEpollChannel.this.pipeline().fireChannelActive();
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
                io.netty.channel.epoll.AbstractEpollChannel r0 = io.netty.channel.epoll.AbstractEpollChannel.this
                io.netty.channel.EventLoop r0 = r0.eventLoop()
                boolean r0 = r0.inEventLoop()
                if (r0 == 0) goto L91
                r0 = 0
                r1 = 0
                r2 = 0
                io.netty.channel.epoll.AbstractEpollChannel r3 = io.netty.channel.epoll.AbstractEpollChannel.this     // Catch: java.lang.Throwable -> L57
                boolean r3 = r3.isActive()     // Catch: java.lang.Throwable -> L57
                boolean r4 = r6.doFinishConnect()     // Catch: java.lang.Throwable -> L57
                if (r4 != 0) goto L35
                r0 = 1
                if (r0 != 0) goto L34
                io.netty.channel.epoll.AbstractEpollChannel r4 = io.netty.channel.epoll.AbstractEpollChannel.this
                io.netty.util.concurrent.Future r4 = io.netty.channel.epoll.AbstractEpollChannel.access$300(r4)
                if (r4 == 0) goto L2f
                io.netty.channel.epoll.AbstractEpollChannel r4 = io.netty.channel.epoll.AbstractEpollChannel.this
                io.netty.util.concurrent.Future r4 = io.netty.channel.epoll.AbstractEpollChannel.access$300(r4)
                r4.cancel(r1)
            L2f:
                io.netty.channel.epoll.AbstractEpollChannel r1 = io.netty.channel.epoll.AbstractEpollChannel.this
                io.netty.channel.epoll.AbstractEpollChannel.access$102(r1, r2)
            L34:
                return
            L35:
                io.netty.channel.epoll.AbstractEpollChannel r4 = io.netty.channel.epoll.AbstractEpollChannel.this     // Catch: java.lang.Throwable -> L57
                io.netty.channel.ChannelPromise r4 = io.netty.channel.epoll.AbstractEpollChannel.access$100(r4)     // Catch: java.lang.Throwable -> L57
                r6.fulfillConnectPromise(r4, r3)     // Catch: java.lang.Throwable -> L57
                if (r0 != 0) goto L76
                io.netty.channel.epoll.AbstractEpollChannel r3 = io.netty.channel.epoll.AbstractEpollChannel.this
                io.netty.util.concurrent.Future r3 = io.netty.channel.epoll.AbstractEpollChannel.access$300(r3)
                if (r3 == 0) goto L51
            L48:
                io.netty.channel.epoll.AbstractEpollChannel r3 = io.netty.channel.epoll.AbstractEpollChannel.this
                io.netty.util.concurrent.Future r3 = io.netty.channel.epoll.AbstractEpollChannel.access$300(r3)
                r3.cancel(r1)
            L51:
                io.netty.channel.epoll.AbstractEpollChannel r1 = io.netty.channel.epoll.AbstractEpollChannel.this
                io.netty.channel.epoll.AbstractEpollChannel.access$102(r1, r2)
                goto L76
            L57:
                r3 = move-exception
                io.netty.channel.epoll.AbstractEpollChannel r4 = io.netty.channel.epoll.AbstractEpollChannel.this     // Catch: java.lang.Throwable -> L77
                io.netty.channel.ChannelPromise r4 = io.netty.channel.epoll.AbstractEpollChannel.access$100(r4)     // Catch: java.lang.Throwable -> L77
                io.netty.channel.epoll.AbstractEpollChannel r5 = io.netty.channel.epoll.AbstractEpollChannel.this     // Catch: java.lang.Throwable -> L77
                java.net.SocketAddress r5 = io.netty.channel.epoll.AbstractEpollChannel.access$200(r5)     // Catch: java.lang.Throwable -> L77
                java.lang.Throwable r5 = r6.annotateConnectException(r3, r5)     // Catch: java.lang.Throwable -> L77
                r6.fulfillConnectPromise(r4, r5)     // Catch: java.lang.Throwable -> L77
                if (r0 != 0) goto L76
                io.netty.channel.epoll.AbstractEpollChannel r3 = io.netty.channel.epoll.AbstractEpollChannel.this
                io.netty.util.concurrent.Future r3 = io.netty.channel.epoll.AbstractEpollChannel.access$300(r3)
                if (r3 == 0) goto L51
                goto L48
            L76:
                return
            L77:
                r3 = move-exception
                if (r0 != 0) goto L90
                io.netty.channel.epoll.AbstractEpollChannel r4 = io.netty.channel.epoll.AbstractEpollChannel.this
                io.netty.util.concurrent.Future r4 = io.netty.channel.epoll.AbstractEpollChannel.access$300(r4)
                if (r4 == 0) goto L8b
                io.netty.channel.epoll.AbstractEpollChannel r4 = io.netty.channel.epoll.AbstractEpollChannel.this
                io.netty.util.concurrent.Future r4 = io.netty.channel.epoll.AbstractEpollChannel.access$300(r4)
                r4.cancel(r1)
            L8b:
                io.netty.channel.epoll.AbstractEpollChannel r1 = io.netty.channel.epoll.AbstractEpollChannel.this
                io.netty.channel.epoll.AbstractEpollChannel.access$102(r1, r2)
            L90:
                throw r3
            L91:
                java.lang.AssertionError r0 = new java.lang.AssertionError
                r0.<init>()
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: io.netty.channel.epoll.AbstractEpollChannel.AbstractEpollUnsafe.finishConnect():void");
        }

        private boolean doFinishConnect() throws Exception {
            if (AbstractEpollChannel.this.socket.finishConnect()) {
                AbstractEpollChannel.this.clearFlag(Native.EPOLLOUT);
                if (AbstractEpollChannel.this.requestedRemoteAddress instanceof InetSocketAddress) {
                    AbstractEpollChannel.this.remote = UnixChannelUtil.computeRemoteAddr((InetSocketAddress) AbstractEpollChannel.this.requestedRemoteAddress, AbstractEpollChannel.this.socket.remoteAddress());
                }
                AbstractEpollChannel.this.requestedRemoteAddress = null;
                return true;
            }
            AbstractEpollChannel.this.setFlag(Native.EPOLLOUT);
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
        boolean connected = doConnect0(remoteAddress);
        if (connected) {
            this.remote = remoteSocketAddr == null ? remoteAddress : UnixChannelUtil.computeRemoteAddr(remoteSocketAddr, this.socket.remoteAddress());
        }
        this.local = this.socket.localAddress();
        return connected;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean doConnect0(SocketAddress remote) throws Exception {
        boolean success = false;
        try {
            boolean connected = this.socket.connect(remote);
            if (!connected) {
                setFlag(Native.EPOLLOUT);
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
