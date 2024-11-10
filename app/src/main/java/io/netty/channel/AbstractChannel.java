package io.netty.channel;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.socket.ChannelOutputShutdownEvent;
import io.netty.channel.socket.ChannelOutputShutdownException;
import io.netty.util.DefaultAttributeMap;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.NotYetConnectedException;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

/* loaded from: classes4.dex */
public abstract class AbstractChannel extends DefaultAttributeMap implements Channel {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) AbstractChannel.class);
    private final CloseFuture closeFuture;
    private boolean closeInitiated;
    private volatile EventLoop eventLoop;
    private final ChannelId id;
    private Throwable initialCloseCause;
    private volatile SocketAddress localAddress;
    private final Channel parent;
    private final DefaultChannelPipeline pipeline;
    private volatile boolean registered;
    private volatile SocketAddress remoteAddress;
    private String strVal;
    private boolean strValActive;
    private final Channel.Unsafe unsafe;
    private final VoidChannelPromise unsafeVoidPromise;

    protected abstract void doBeginRead() throws Exception;

    protected abstract void doBind(SocketAddress socketAddress) throws Exception;

    protected abstract void doClose() throws Exception;

    protected abstract void doDisconnect() throws Exception;

    protected abstract void doWrite(ChannelOutboundBuffer channelOutboundBuffer) throws Exception;

    protected abstract boolean isCompatible(EventLoop eventLoop);

    protected abstract SocketAddress localAddress0();

    protected abstract AbstractUnsafe newUnsafe();

    protected abstract SocketAddress remoteAddress0();

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractChannel(Channel parent) {
        this.unsafeVoidPromise = new VoidChannelPromise(this, false);
        this.closeFuture = new CloseFuture(this);
        this.parent = parent;
        this.id = newId();
        this.unsafe = newUnsafe();
        this.pipeline = newChannelPipeline();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractChannel(Channel parent, ChannelId id) {
        this.unsafeVoidPromise = new VoidChannelPromise(this, false);
        this.closeFuture = new CloseFuture(this);
        this.parent = parent;
        this.id = id;
        this.unsafe = newUnsafe();
        this.pipeline = newChannelPipeline();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final int maxMessagesPerWrite() {
        ChannelConfig config = config();
        if (config instanceof DefaultChannelConfig) {
            return ((DefaultChannelConfig) config).getMaxMessagesPerWrite();
        }
        Integer value = (Integer) config.getOption(ChannelOption.MAX_MESSAGES_PER_WRITE);
        if (value == null) {
            return Integer.MAX_VALUE;
        }
        return value.intValue();
    }

    @Override // io.netty.channel.Channel
    public final ChannelId id() {
        return this.id;
    }

    protected ChannelId newId() {
        return DefaultChannelId.newInstance();
    }

    protected DefaultChannelPipeline newChannelPipeline() {
        return new DefaultChannelPipeline(this);
    }

    @Override // io.netty.channel.Channel
    public boolean isWritable() {
        ChannelOutboundBuffer buf = this.unsafe.outboundBuffer();
        return buf != null && buf.isWritable();
    }

    @Override // io.netty.channel.Channel
    public long bytesBeforeUnwritable() {
        ChannelOutboundBuffer buf = this.unsafe.outboundBuffer();
        if (buf != null) {
            return buf.bytesBeforeUnwritable();
        }
        return 0L;
    }

    @Override // io.netty.channel.Channel
    public long bytesBeforeWritable() {
        ChannelOutboundBuffer buf = this.unsafe.outboundBuffer();
        if (buf != null) {
            return buf.bytesBeforeWritable();
        }
        return Long.MAX_VALUE;
    }

    @Override // io.netty.channel.Channel
    public Channel parent() {
        return this.parent;
    }

    @Override // io.netty.channel.Channel
    public ChannelPipeline pipeline() {
        return this.pipeline;
    }

    @Override // io.netty.channel.Channel
    public ByteBufAllocator alloc() {
        return config().getAllocator();
    }

    @Override // io.netty.channel.Channel
    public EventLoop eventLoop() {
        EventLoop eventLoop = this.eventLoop;
        if (eventLoop == null) {
            throw new IllegalStateException("channel not registered to an event loop");
        }
        return eventLoop;
    }

    @Override // io.netty.channel.Channel
    public SocketAddress localAddress() {
        SocketAddress localAddress = this.localAddress;
        if (localAddress == null) {
            try {
                SocketAddress localAddress2 = unsafe().localAddress();
                this.localAddress = localAddress2;
                return localAddress2;
            } catch (Error e) {
                throw e;
            } catch (Throwable th) {
                return null;
            }
        }
        return localAddress;
    }

    @Deprecated
    protected void invalidateLocalAddress() {
        this.localAddress = null;
    }

    @Override // io.netty.channel.Channel
    public SocketAddress remoteAddress() {
        SocketAddress remoteAddress = this.remoteAddress;
        if (remoteAddress == null) {
            try {
                SocketAddress remoteAddress2 = unsafe().remoteAddress();
                this.remoteAddress = remoteAddress2;
                return remoteAddress2;
            } catch (Error e) {
                throw e;
            } catch (Throwable th) {
                return null;
            }
        }
        return remoteAddress;
    }

    @Deprecated
    protected void invalidateRemoteAddress() {
        this.remoteAddress = null;
    }

    @Override // io.netty.channel.Channel
    public boolean isRegistered() {
        return this.registered;
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture bind(SocketAddress localAddress) {
        return this.pipeline.bind(localAddress);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture connect(SocketAddress remoteAddress) {
        return this.pipeline.connect(remoteAddress);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress) {
        return this.pipeline.connect(remoteAddress, localAddress);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture disconnect() {
        return this.pipeline.disconnect();
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture close() {
        return this.pipeline.close();
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture deregister() {
        return this.pipeline.deregister();
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public Channel flush() {
        this.pipeline.flush();
        return this;
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture bind(SocketAddress localAddress, ChannelPromise promise) {
        return this.pipeline.bind(localAddress, promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture connect(SocketAddress remoteAddress, ChannelPromise promise) {
        return this.pipeline.connect(remoteAddress, promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
        return this.pipeline.connect(remoteAddress, localAddress, promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture disconnect(ChannelPromise promise) {
        return this.pipeline.disconnect(promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture close(ChannelPromise promise) {
        return this.pipeline.close(promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture deregister(ChannelPromise promise) {
        return this.pipeline.deregister(promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public Channel read() {
        this.pipeline.read();
        return this;
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture write(Object msg) {
        return this.pipeline.write(msg);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture write(Object msg, ChannelPromise promise) {
        return this.pipeline.write(msg, promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture writeAndFlush(Object msg) {
        return this.pipeline.writeAndFlush(msg);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture writeAndFlush(Object msg, ChannelPromise promise) {
        return this.pipeline.writeAndFlush(msg, promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelPromise newPromise() {
        return this.pipeline.newPromise();
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelProgressivePromise newProgressivePromise() {
        return this.pipeline.newProgressivePromise();
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture newSucceededFuture() {
        return this.pipeline.newSucceededFuture();
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture newFailedFuture(Throwable cause) {
        return this.pipeline.newFailedFuture(cause);
    }

    @Override // io.netty.channel.Channel
    public ChannelFuture closeFuture() {
        return this.closeFuture;
    }

    @Override // io.netty.channel.Channel
    public Channel.Unsafe unsafe() {
        return this.unsafe;
    }

    public final int hashCode() {
        return this.id.hashCode();
    }

    public final boolean equals(Object o) {
        return this == o;
    }

    @Override // java.lang.Comparable
    public final int compareTo(Channel o) {
        if (this == o) {
            return 0;
        }
        return id().compareTo(o.id());
    }

    public String toString() {
        boolean active = isActive();
        if (this.strValActive == active && this.strVal != null) {
            return this.strVal;
        }
        SocketAddress remoteAddr = remoteAddress();
        SocketAddress localAddr = localAddress();
        if (remoteAddr != null) {
            StringBuilder buf = new StringBuilder(96).append("[id: 0x").append(this.id.asShortText()).append(", L:").append(localAddr).append(active ? " - " : " ! ").append("R:").append(remoteAddr).append(']');
            this.strVal = buf.toString();
        } else if (localAddr != null) {
            StringBuilder buf2 = new StringBuilder(64).append("[id: 0x").append(this.id.asShortText()).append(", L:").append(localAddr).append(']');
            this.strVal = buf2.toString();
        } else {
            StringBuilder buf3 = new StringBuilder(16).append("[id: 0x").append(this.id.asShortText()).append(']');
            this.strVal = buf3.toString();
        }
        this.strValActive = active;
        return this.strVal;
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public final ChannelPromise voidPromise() {
        return this.pipeline.voidPromise();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public abstract class AbstractUnsafe implements Channel.Unsafe {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private boolean inFlush0;
        private boolean neverRegistered = true;
        private volatile ChannelOutboundBuffer outboundBuffer;
        private RecvByteBufAllocator.Handle recvHandle;

        /* JADX INFO: Access modifiers changed from: protected */
        public AbstractUnsafe() {
            this.outboundBuffer = new ChannelOutboundBuffer(AbstractChannel.this);
        }

        private void assertEventLoop() {
            if (AbstractChannel.this.registered && !AbstractChannel.this.eventLoop.inEventLoop()) {
                throw new AssertionError();
            }
        }

        @Override // io.netty.channel.Channel.Unsafe
        public RecvByteBufAllocator.Handle recvBufAllocHandle() {
            if (this.recvHandle == null) {
                this.recvHandle = AbstractChannel.this.config().getRecvByteBufAllocator().newHandle();
            }
            return this.recvHandle;
        }

        @Override // io.netty.channel.Channel.Unsafe
        public final ChannelOutboundBuffer outboundBuffer() {
            return this.outboundBuffer;
        }

        @Override // io.netty.channel.Channel.Unsafe
        public final SocketAddress localAddress() {
            return AbstractChannel.this.localAddress0();
        }

        @Override // io.netty.channel.Channel.Unsafe
        public final SocketAddress remoteAddress() {
            return AbstractChannel.this.remoteAddress0();
        }

        @Override // io.netty.channel.Channel.Unsafe
        public final void register(EventLoop eventLoop, final ChannelPromise promise) {
            ObjectUtil.checkNotNull(eventLoop, "eventLoop");
            if (AbstractChannel.this.isRegistered()) {
                promise.setFailure((Throwable) new IllegalStateException("registered to an event loop already"));
                return;
            }
            if (AbstractChannel.this.isCompatible(eventLoop)) {
                AbstractChannel.this.eventLoop = eventLoop;
                if (eventLoop.inEventLoop()) {
                    register0(promise);
                    return;
                }
                try {
                    eventLoop.execute(new Runnable() { // from class: io.netty.channel.AbstractChannel.AbstractUnsafe.1
                        @Override // java.lang.Runnable
                        public void run() {
                            AbstractUnsafe.this.register0(promise);
                        }
                    });
                    return;
                } catch (Throwable t) {
                    AbstractChannel.logger.warn("Force-closing a channel whose registration task was not accepted by an event loop: {}", AbstractChannel.this, t);
                    closeForcibly();
                    AbstractChannel.this.closeFuture.setClosed();
                    safeSetFailure(promise, t);
                    return;
                }
            }
            promise.setFailure((Throwable) new IllegalStateException("incompatible event loop type: " + eventLoop.getClass().getName()));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void register0(ChannelPromise promise) {
            try {
                if (promise.setUncancellable() && ensureOpen(promise)) {
                    boolean firstRegistration = this.neverRegistered;
                    AbstractChannel.this.doRegister();
                    this.neverRegistered = false;
                    AbstractChannel.this.registered = true;
                    AbstractChannel.this.pipeline.invokeHandlerAddedIfNeeded();
                    safeSetSuccess(promise);
                    AbstractChannel.this.pipeline.fireChannelRegistered();
                    if (AbstractChannel.this.isActive()) {
                        if (firstRegistration) {
                            AbstractChannel.this.pipeline.fireChannelActive();
                        } else if (AbstractChannel.this.config().isAutoRead()) {
                            beginRead();
                        }
                    }
                }
            } catch (Throwable t) {
                closeForcibly();
                AbstractChannel.this.closeFuture.setClosed();
                safeSetFailure(promise, t);
            }
        }

        @Override // io.netty.channel.Channel.Unsafe
        public final void bind(SocketAddress localAddress, ChannelPromise promise) {
            assertEventLoop();
            if (!promise.setUncancellable() || !ensureOpen(promise)) {
                return;
            }
            if (Boolean.TRUE.equals(AbstractChannel.this.config().getOption(ChannelOption.SO_BROADCAST)) && (localAddress instanceof InetSocketAddress) && !((InetSocketAddress) localAddress).getAddress().isAnyLocalAddress() && !PlatformDependent.isWindows() && !PlatformDependent.maybeSuperUser()) {
                AbstractChannel.logger.warn("A non-root user can't receive a broadcast packet if the socket is not bound to a wildcard address; binding to a non-wildcard address (" + localAddress + ") anyway as requested.");
            }
            boolean wasActive = AbstractChannel.this.isActive();
            try {
                AbstractChannel.this.doBind(localAddress);
                if (!wasActive && AbstractChannel.this.isActive()) {
                    invokeLater(new Runnable() { // from class: io.netty.channel.AbstractChannel.AbstractUnsafe.2
                        @Override // java.lang.Runnable
                        public void run() {
                            AbstractChannel.this.pipeline.fireChannelActive();
                        }
                    });
                }
                safeSetSuccess(promise);
            } catch (Throwable t) {
                safeSetFailure(promise, t);
                closeIfClosed();
            }
        }

        @Override // io.netty.channel.Channel.Unsafe
        public final void disconnect(ChannelPromise promise) {
            assertEventLoop();
            if (!promise.setUncancellable()) {
                return;
            }
            boolean wasActive = AbstractChannel.this.isActive();
            try {
                AbstractChannel.this.doDisconnect();
                AbstractChannel.this.remoteAddress = null;
                AbstractChannel.this.localAddress = null;
                if (wasActive && !AbstractChannel.this.isActive()) {
                    invokeLater(new Runnable() { // from class: io.netty.channel.AbstractChannel.AbstractUnsafe.3
                        @Override // java.lang.Runnable
                        public void run() {
                            AbstractChannel.this.pipeline.fireChannelInactive();
                        }
                    });
                }
                safeSetSuccess(promise);
                closeIfClosed();
            } catch (Throwable t) {
                safeSetFailure(promise, t);
                closeIfClosed();
            }
        }

        @Override // io.netty.channel.Channel.Unsafe
        public void close(ChannelPromise promise) {
            assertEventLoop();
            ClosedChannelException closedChannelException = StacklessClosedChannelException.newInstance(AbstractChannel.class, "close(ChannelPromise)");
            close(promise, closedChannelException, closedChannelException, false);
        }

        public final void shutdownOutput(ChannelPromise promise) {
            assertEventLoop();
            shutdownOutput(promise, null);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r2v4, types: [java.lang.Throwable, io.netty.channel.ChannelPipeline, io.netty.channel.DefaultChannelPipeline] */
        private void shutdownOutput(ChannelPromise promise, Throwable cause) {
            if (!promise.setUncancellable()) {
                return;
            }
            ChannelOutboundBuffer outboundBuffer = this.outboundBuffer;
            if (outboundBuffer == null) {
                promise.setFailure((Throwable) new ClosedChannelException());
                return;
            }
            this.outboundBuffer = null;
            if (cause == null) {
                new ChannelOutputShutdownException("Channel output shutdown");
            } else {
                new ChannelOutputShutdownException("Channel output shutdown", cause);
            }
            try {
                AbstractChannel.this.doShutdownOutput();
                promise.setSuccess();
            } finally {
                try {
                } finally {
                }
            }
        }

        private void closeOutboundBufferForShutdown(ChannelPipeline pipeline, ChannelOutboundBuffer buffer, Throwable cause) {
            buffer.failFlushed(cause, false);
            buffer.close(cause, true);
            pipeline.fireUserEventTriggered((Object) ChannelOutputShutdownEvent.INSTANCE);
        }

        private void close(final ChannelPromise promise, final Throwable cause, final ClosedChannelException closeCause, final boolean notify) {
            if (promise.setUncancellable()) {
                if (AbstractChannel.this.closeInitiated) {
                    if (AbstractChannel.this.closeFuture.isDone()) {
                        safeSetSuccess(promise);
                        return;
                    } else {
                        if (!(promise instanceof VoidChannelPromise)) {
                            AbstractChannel.this.closeFuture.addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.channel.AbstractChannel.AbstractUnsafe.4
                                @Override // io.netty.util.concurrent.GenericFutureListener
                                public void operationComplete(ChannelFuture future) throws Exception {
                                    promise.setSuccess();
                                }
                            });
                            return;
                        }
                        return;
                    }
                }
                AbstractChannel.this.closeInitiated = true;
                final boolean wasActive = AbstractChannel.this.isActive();
                final ChannelOutboundBuffer outboundBuffer = this.outboundBuffer;
                this.outboundBuffer = null;
                Executor closeExecutor = prepareToClose();
                if (closeExecutor != null) {
                    closeExecutor.execute(new Runnable() { // from class: io.netty.channel.AbstractChannel.AbstractUnsafe.5
                        @Override // java.lang.Runnable
                        public void run() {
                            try {
                                AbstractUnsafe.this.doClose0(promise);
                            } finally {
                                AbstractUnsafe.this.invokeLater(new Runnable() { // from class: io.netty.channel.AbstractChannel.AbstractUnsafe.5.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        if (outboundBuffer != null) {
                                            outboundBuffer.failFlushed(cause, notify);
                                            outboundBuffer.close(closeCause);
                                        }
                                        AbstractUnsafe.this.fireChannelInactiveAndDeregister(wasActive);
                                    }
                                });
                            }
                        }
                    });
                    return;
                }
                try {
                    doClose0(promise);
                    if (outboundBuffer != null) {
                        outboundBuffer.failFlushed(cause, notify);
                        outboundBuffer.close(closeCause);
                    }
                    if (this.inFlush0) {
                        invokeLater(new Runnable() { // from class: io.netty.channel.AbstractChannel.AbstractUnsafe.6
                            @Override // java.lang.Runnable
                            public void run() {
                                AbstractUnsafe.this.fireChannelInactiveAndDeregister(wasActive);
                            }
                        });
                    } else {
                        fireChannelInactiveAndDeregister(wasActive);
                    }
                } finally {
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void doClose0(ChannelPromise promise) {
            try {
                AbstractChannel.this.doClose();
                AbstractChannel.this.closeFuture.setClosed();
                safeSetSuccess(promise);
            } catch (Throwable t) {
                AbstractChannel.this.closeFuture.setClosed();
                safeSetFailure(promise, t);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void fireChannelInactiveAndDeregister(boolean wasActive) {
            deregister(voidPromise(), wasActive && !AbstractChannel.this.isActive());
        }

        @Override // io.netty.channel.Channel.Unsafe
        public final void closeForcibly() {
            assertEventLoop();
            try {
                AbstractChannel.this.doClose();
            } catch (Exception e) {
                AbstractChannel.logger.warn("Failed to close a channel.", (Throwable) e);
            }
        }

        @Override // io.netty.channel.Channel.Unsafe
        public final void deregister(ChannelPromise promise) {
            assertEventLoop();
            deregister(promise, false);
        }

        private void deregister(final ChannelPromise promise, final boolean fireChannelInactive) {
            if (promise.setUncancellable()) {
                if (!AbstractChannel.this.registered) {
                    safeSetSuccess(promise);
                } else {
                    invokeLater(new Runnable() { // from class: io.netty.channel.AbstractChannel.AbstractUnsafe.7
                        /* JADX WARN: Code restructure failed: missing block: B:11:0x005f, code lost:            return;     */
                        /* JADX WARN: Code restructure failed: missing block: B:21:0x005c, code lost:            if (r4.this$1.this$0.registered == false) goto L10;     */
                        @Override // java.lang.Runnable
                        /*
                            Code decompiled incorrectly, please refer to instructions dump.
                            To view partially-correct code enable 'Show inconsistent code' option in preferences
                        */
                        public void run() {
                            /*
                                r4 = this;
                                r0 = 0
                                io.netty.channel.AbstractChannel$AbstractUnsafe r1 = io.netty.channel.AbstractChannel.AbstractUnsafe.this     // Catch: java.lang.Throwable -> L3b
                                io.netty.channel.AbstractChannel r1 = io.netty.channel.AbstractChannel.this     // Catch: java.lang.Throwable -> L3b
                                r1.doDeregister()     // Catch: java.lang.Throwable -> L3b
                                boolean r1 = r2
                                if (r1 == 0) goto L17
                                io.netty.channel.AbstractChannel$AbstractUnsafe r1 = io.netty.channel.AbstractChannel.AbstractUnsafe.this
                                io.netty.channel.AbstractChannel r1 = io.netty.channel.AbstractChannel.this
                                io.netty.channel.DefaultChannelPipeline r1 = io.netty.channel.AbstractChannel.access$500(r1)
                                r1.fireChannelInactive()
                            L17:
                                io.netty.channel.AbstractChannel$AbstractUnsafe r1 = io.netty.channel.AbstractChannel.AbstractUnsafe.this
                                io.netty.channel.AbstractChannel r1 = io.netty.channel.AbstractChannel.this
                                boolean r1 = io.netty.channel.AbstractChannel.access$000(r1)
                                if (r1 == 0) goto L33
                            L21:
                                io.netty.channel.AbstractChannel$AbstractUnsafe r1 = io.netty.channel.AbstractChannel.AbstractUnsafe.this
                                io.netty.channel.AbstractChannel r1 = io.netty.channel.AbstractChannel.this
                                io.netty.channel.AbstractChannel.access$002(r1, r0)
                                io.netty.channel.AbstractChannel$AbstractUnsafe r0 = io.netty.channel.AbstractChannel.AbstractUnsafe.this
                                io.netty.channel.AbstractChannel r0 = io.netty.channel.AbstractChannel.this
                                io.netty.channel.DefaultChannelPipeline r0 = io.netty.channel.AbstractChannel.access$500(r0)
                                r0.fireChannelUnregistered()
                            L33:
                                io.netty.channel.AbstractChannel$AbstractUnsafe r0 = io.netty.channel.AbstractChannel.AbstractUnsafe.this
                                io.netty.channel.ChannelPromise r1 = r3
                                r0.safeSetSuccess(r1)
                                goto L5f
                            L3b:
                                r1 = move-exception
                                io.netty.util.internal.logging.InternalLogger r2 = io.netty.channel.AbstractChannel.access$300()     // Catch: java.lang.Throwable -> L60
                                java.lang.String r3 = "Unexpected exception occurred while deregistering a channel."
                                r2.warn(r3, r1)     // Catch: java.lang.Throwable -> L60
                                boolean r1 = r2
                                if (r1 == 0) goto L54
                                io.netty.channel.AbstractChannel$AbstractUnsafe r1 = io.netty.channel.AbstractChannel.AbstractUnsafe.this
                                io.netty.channel.AbstractChannel r1 = io.netty.channel.AbstractChannel.this
                                io.netty.channel.DefaultChannelPipeline r1 = io.netty.channel.AbstractChannel.access$500(r1)
                                r1.fireChannelInactive()
                            L54:
                                io.netty.channel.AbstractChannel$AbstractUnsafe r1 = io.netty.channel.AbstractChannel.AbstractUnsafe.this
                                io.netty.channel.AbstractChannel r1 = io.netty.channel.AbstractChannel.this
                                boolean r1 = io.netty.channel.AbstractChannel.access$000(r1)
                                if (r1 == 0) goto L33
                                goto L21
                            L5f:
                                return
                            L60:
                                r1 = move-exception
                                boolean r2 = r2
                                if (r2 == 0) goto L70
                                io.netty.channel.AbstractChannel$AbstractUnsafe r2 = io.netty.channel.AbstractChannel.AbstractUnsafe.this
                                io.netty.channel.AbstractChannel r2 = io.netty.channel.AbstractChannel.this
                                io.netty.channel.DefaultChannelPipeline r2 = io.netty.channel.AbstractChannel.access$500(r2)
                                r2.fireChannelInactive()
                            L70:
                                io.netty.channel.AbstractChannel$AbstractUnsafe r2 = io.netty.channel.AbstractChannel.AbstractUnsafe.this
                                io.netty.channel.AbstractChannel r2 = io.netty.channel.AbstractChannel.this
                                boolean r2 = io.netty.channel.AbstractChannel.access$000(r2)
                                if (r2 == 0) goto L8c
                                io.netty.channel.AbstractChannel$AbstractUnsafe r2 = io.netty.channel.AbstractChannel.AbstractUnsafe.this
                                io.netty.channel.AbstractChannel r2 = io.netty.channel.AbstractChannel.this
                                io.netty.channel.AbstractChannel.access$002(r2, r0)
                                io.netty.channel.AbstractChannel$AbstractUnsafe r0 = io.netty.channel.AbstractChannel.AbstractUnsafe.this
                                io.netty.channel.AbstractChannel r0 = io.netty.channel.AbstractChannel.this
                                io.netty.channel.DefaultChannelPipeline r0 = io.netty.channel.AbstractChannel.access$500(r0)
                                r0.fireChannelUnregistered()
                            L8c:
                                io.netty.channel.AbstractChannel$AbstractUnsafe r0 = io.netty.channel.AbstractChannel.AbstractUnsafe.this
                                io.netty.channel.ChannelPromise r2 = r3
                                r0.safeSetSuccess(r2)
                                throw r1
                            */
                            throw new UnsupportedOperationException("Method not decompiled: io.netty.channel.AbstractChannel.AbstractUnsafe.AnonymousClass7.run():void");
                        }
                    });
                }
            }
        }

        @Override // io.netty.channel.Channel.Unsafe
        public final void beginRead() {
            assertEventLoop();
            try {
                AbstractChannel.this.doBeginRead();
            } catch (Exception e) {
                invokeLater(new Runnable() { // from class: io.netty.channel.AbstractChannel.AbstractUnsafe.8
                    @Override // java.lang.Runnable
                    public void run() {
                        AbstractChannel.this.pipeline.fireExceptionCaught((Throwable) e);
                    }
                });
                close(voidPromise());
            }
        }

        @Override // io.netty.channel.Channel.Unsafe
        public final void write(Object msg, ChannelPromise promise) {
            assertEventLoop();
            ChannelOutboundBuffer outboundBuffer = this.outboundBuffer;
            if (outboundBuffer == null) {
                try {
                    ReferenceCountUtil.release(msg);
                    return;
                } finally {
                    safeSetFailure(promise, newClosedChannelException(AbstractChannel.this.initialCloseCause, "write(Object, ChannelPromise)"));
                }
            }
            try {
                msg = AbstractChannel.this.filterOutboundMessage(msg);
                int size = AbstractChannel.this.pipeline.estimatorHandle().size(msg);
                if (size < 0) {
                    size = 0;
                }
                outboundBuffer.addMessage(msg, size, promise);
            } catch (Throwable t) {
                try {
                    ReferenceCountUtil.release(msg);
                } finally {
                    safeSetFailure(promise, t);
                }
            }
        }

        @Override // io.netty.channel.Channel.Unsafe
        public final void flush() {
            assertEventLoop();
            ChannelOutboundBuffer outboundBuffer = this.outboundBuffer;
            if (outboundBuffer == null) {
                return;
            }
            outboundBuffer.addFlush();
            flush0();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void flush0() {
            ChannelOutboundBuffer outboundBuffer;
            if (this.inFlush0 || (outboundBuffer = this.outboundBuffer) == null || outboundBuffer.isEmpty()) {
                return;
            }
            this.inFlush0 = true;
            if (!AbstractChannel.this.isActive()) {
                try {
                    if (!outboundBuffer.isEmpty()) {
                        if (!AbstractChannel.this.isOpen()) {
                            outboundBuffer.failFlushed(newClosedChannelException(AbstractChannel.this.initialCloseCause, "flush0()"), false);
                        } else {
                            outboundBuffer.failFlushed(new NotYetConnectedException(), true);
                        }
                    }
                    return;
                } finally {
                }
            }
            try {
                AbstractChannel.this.doWrite(outboundBuffer);
            } finally {
                try {
                } finally {
                }
            }
        }

        protected final void handleWriteError(Throwable t) {
            if ((t instanceof IOException) && AbstractChannel.this.config().isAutoClose()) {
                AbstractChannel.this.initialCloseCause = t;
                close(voidPromise(), t, newClosedChannelException(t, "flush0()"), false);
                return;
            }
            try {
                shutdownOutput(voidPromise(), t);
            } catch (Throwable t2) {
                AbstractChannel.this.initialCloseCause = t;
                close(voidPromise(), t2, newClosedChannelException(t, "flush0()"), false);
            }
        }

        private ClosedChannelException newClosedChannelException(Throwable cause, String method) {
            ClosedChannelException exception = StacklessClosedChannelException.newInstance(AbstractUnsafe.class, method);
            if (cause != null) {
                exception.initCause(cause);
            }
            return exception;
        }

        @Override // io.netty.channel.Channel.Unsafe
        public final ChannelPromise voidPromise() {
            assertEventLoop();
            return AbstractChannel.this.unsafeVoidPromise;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public final boolean ensureOpen(ChannelPromise promise) {
            if (!AbstractChannel.this.isOpen()) {
                safeSetFailure(promise, newClosedChannelException(AbstractChannel.this.initialCloseCause, "ensureOpen(ChannelPromise)"));
                return false;
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public final void safeSetSuccess(ChannelPromise promise) {
            if (!(promise instanceof VoidChannelPromise) && !promise.trySuccess()) {
                AbstractChannel.logger.warn("Failed to mark a promise as success because it is done already: {}", promise);
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public final void safeSetFailure(ChannelPromise promise, Throwable cause) {
            if (!(promise instanceof VoidChannelPromise) && !promise.tryFailure(cause)) {
                AbstractChannel.logger.warn("Failed to mark a promise as failure because it's done already: {}", promise, cause);
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public final void closeIfClosed() {
            if (AbstractChannel.this.isOpen()) {
                return;
            }
            close(voidPromise());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void invokeLater(Runnable task) {
            try {
                AbstractChannel.this.eventLoop().execute(task);
            } catch (RejectedExecutionException e) {
                AbstractChannel.logger.warn("Can't invoke task later as EventLoop rejected it", (Throwable) e);
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public final Throwable annotateConnectException(Throwable cause, SocketAddress remoteAddress) {
            if (cause instanceof ConnectException) {
                return new AnnotatedConnectException((ConnectException) cause, remoteAddress);
            }
            if (cause instanceof NoRouteToHostException) {
                return new AnnotatedNoRouteToHostException((NoRouteToHostException) cause, remoteAddress);
            }
            if (cause instanceof SocketException) {
                return new AnnotatedSocketException((SocketException) cause, remoteAddress);
            }
            return cause;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public Executor prepareToClose() {
            return null;
        }
    }

    protected void doRegister() throws Exception {
    }

    protected void doShutdownOutput() throws Exception {
        doClose();
    }

    protected void doDeregister() throws Exception {
    }

    protected Object filterOutboundMessage(Object msg) throws Exception {
        return msg;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void validateFileRegion(DefaultFileRegion region, long position) throws IOException {
        DefaultFileRegion.validate(region, position);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static final class CloseFuture extends DefaultChannelPromise {
        CloseFuture(AbstractChannel ch) {
            super(ch);
        }

        @Override // io.netty.channel.DefaultChannelPromise, io.netty.channel.ChannelPromise
        public ChannelPromise setSuccess() {
            throw new IllegalStateException();
        }

        @Override // io.netty.channel.DefaultChannelPromise, io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Promise, io.netty.channel.ChannelPromise
        public ChannelPromise setFailure(Throwable cause) {
            throw new IllegalStateException();
        }

        @Override // io.netty.channel.DefaultChannelPromise, io.netty.channel.ChannelPromise
        public boolean trySuccess() {
            throw new IllegalStateException();
        }

        @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Promise
        public boolean tryFailure(Throwable cause) {
            throw new IllegalStateException();
        }

        boolean setClosed() {
            return super.trySuccess();
        }
    }

    /* loaded from: classes4.dex */
    private static final class AnnotatedConnectException extends ConnectException {
        private static final long serialVersionUID = 3901958112696433556L;

        AnnotatedConnectException(ConnectException exception, SocketAddress remoteAddress) {
            super(exception.getMessage() + ": " + remoteAddress);
            initCause(exception);
        }

        @Override // java.lang.Throwable
        public Throwable fillInStackTrace() {
            return this;
        }
    }

    /* loaded from: classes4.dex */
    private static final class AnnotatedNoRouteToHostException extends NoRouteToHostException {
        private static final long serialVersionUID = -6801433937592080623L;

        AnnotatedNoRouteToHostException(NoRouteToHostException exception, SocketAddress remoteAddress) {
            super(exception.getMessage() + ": " + remoteAddress);
            initCause(exception);
        }

        @Override // java.lang.Throwable
        public Throwable fillInStackTrace() {
            return this;
        }
    }

    /* loaded from: classes4.dex */
    private static final class AnnotatedSocketException extends SocketException {
        private static final long serialVersionUID = 3896743275010454039L;

        AnnotatedSocketException(SocketException exception, SocketAddress remoteAddress) {
            super(exception.getMessage() + ": " + remoteAddress);
            initCause(exception);
        }

        @Override // java.lang.Throwable
        public Throwable fillInStackTrace() {
            return this;
        }
    }
}
