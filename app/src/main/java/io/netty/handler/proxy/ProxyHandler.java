package io.netty.handler.proxy;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.PendingWriteQueue;
import io.netty.handler.codec.rtsp.RtspHeaders;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.SocketAddress;
import java.nio.channels.ConnectionPendingException;
import java.util.concurrent.TimeUnit;

/* loaded from: classes4.dex */
public abstract class ProxyHandler extends ChannelDuplexHandler {
    static final String AUTH_NONE = "none";
    private static final long DEFAULT_CONNECT_TIMEOUT_MILLIS = 10000;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) ProxyHandler.class);
    private Future<?> connectTimeoutFuture;
    private volatile ChannelHandlerContext ctx;
    private volatile SocketAddress destinationAddress;
    private boolean finished;
    private boolean flushedPrematurely;
    private PendingWriteQueue pendingWrites;
    private final SocketAddress proxyAddress;
    private boolean suppressChannelReadComplete;
    private volatile long connectTimeoutMillis = DEFAULT_CONNECT_TIMEOUT_MILLIS;
    private final LazyChannelPromise connectPromise = new LazyChannelPromise();
    private final ChannelFutureListener writeListener = new ChannelFutureListener() { // from class: io.netty.handler.proxy.ProxyHandler.1
        @Override // io.netty.util.concurrent.GenericFutureListener
        public void operationComplete(ChannelFuture future) throws Exception {
            if (!future.isSuccess()) {
                ProxyHandler.this.setConnectFailure(future.cause());
            }
        }
    };

    protected abstract void addCodec(ChannelHandlerContext channelHandlerContext) throws Exception;

    public abstract String authScheme();

    protected abstract boolean handleResponse(ChannelHandlerContext channelHandlerContext, Object obj) throws Exception;

    protected abstract Object newInitialMessage(ChannelHandlerContext channelHandlerContext) throws Exception;

    public abstract String protocol();

    protected abstract void removeDecoder(ChannelHandlerContext channelHandlerContext) throws Exception;

    protected abstract void removeEncoder(ChannelHandlerContext channelHandlerContext) throws Exception;

    /* JADX INFO: Access modifiers changed from: protected */
    public ProxyHandler(SocketAddress proxyAddress) {
        this.proxyAddress = (SocketAddress) ObjectUtil.checkNotNull(proxyAddress, "proxyAddress");
    }

    public final <T extends SocketAddress> T proxyAddress() {
        return (T) this.proxyAddress;
    }

    public final <T extends SocketAddress> T destinationAddress() {
        return (T) this.destinationAddress;
    }

    public final boolean isConnected() {
        return this.connectPromise.isSuccess();
    }

    public final Future<Channel> connectFuture() {
        return this.connectPromise;
    }

    public final long connectTimeoutMillis() {
        return this.connectTimeoutMillis;
    }

    public final void setConnectTimeoutMillis(long connectTimeoutMillis) {
        if (connectTimeoutMillis <= 0) {
            connectTimeoutMillis = 0;
        }
        this.connectTimeoutMillis = connectTimeoutMillis;
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public final void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        addCodec(ctx);
        if (ctx.channel().isActive()) {
            sendInitialMessage(ctx);
        }
    }

    @Override // io.netty.channel.ChannelDuplexHandler, io.netty.channel.ChannelOutboundHandler
    public final void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        if (this.destinationAddress != null) {
            promise.setFailure((Throwable) new ConnectionPendingException());
        } else {
            this.destinationAddress = remoteAddress;
            ctx.connect(this.proxyAddress, localAddress, promise);
        }
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public final void channelActive(ChannelHandlerContext ctx) throws Exception {
        sendInitialMessage(ctx);
        ctx.fireChannelActive();
    }

    private void sendInitialMessage(ChannelHandlerContext ctx) throws Exception {
        long connectTimeoutMillis = this.connectTimeoutMillis;
        if (connectTimeoutMillis > 0) {
            this.connectTimeoutFuture = ctx.executor().schedule(new Runnable() { // from class: io.netty.handler.proxy.ProxyHandler.2
                @Override // java.lang.Runnable
                public void run() {
                    if (!ProxyHandler.this.connectPromise.isDone()) {
                        ProxyHandler.this.setConnectFailure(new ProxyConnectException(ProxyHandler.this.exceptionMessage(RtspHeaders.Values.TIMEOUT)));
                    }
                }
            }, connectTimeoutMillis, TimeUnit.MILLISECONDS);
        }
        Object initialMessage = newInitialMessage(ctx);
        if (initialMessage != null) {
            sendToProxyServer(initialMessage);
        }
        readIfNeeded(ctx);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void sendToProxyServer(Object msg) {
        this.ctx.writeAndFlush(msg).addListener((GenericFutureListener<? extends Future<? super Void>>) this.writeListener);
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public final void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (this.finished) {
            ctx.fireChannelInactive();
        } else {
            setConnectFailure(new ProxyConnectException(exceptionMessage("disconnected")));
        }
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler, io.netty.channel.ChannelInboundHandler
    public final void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (this.finished) {
            ctx.fireExceptionCaught(cause);
        } else {
            setConnectFailure(cause);
        }
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public final void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (this.finished) {
            this.suppressChannelReadComplete = false;
            ctx.fireChannelRead(msg);
            return;
        }
        this.suppressChannelReadComplete = true;
        Throwable cause = null;
        try {
            boolean done = handleResponse(ctx, msg);
            if (done) {
                setConnectSuccess();
            }
            ReferenceCountUtil.release(msg);
            if (0 == 0) {
                return;
            }
        } catch (Throwable t) {
            cause = t;
            ReferenceCountUtil.release(msg);
        }
        setConnectFailure(cause);
    }

    private void setConnectSuccess() {
        this.finished = true;
        cancelConnectTimeoutFuture();
        if (!this.connectPromise.isDone()) {
            boolean removedCodec = true & safeRemoveEncoder();
            this.ctx.fireUserEventTriggered((Object) new ProxyConnectionEvent(protocol(), authScheme(), this.proxyAddress, this.destinationAddress));
            if (removedCodec & safeRemoveDecoder()) {
                writePendingWrites();
                if (this.flushedPrematurely) {
                    this.ctx.flush();
                }
                this.connectPromise.trySuccess(this.ctx.channel());
                return;
            }
            Exception cause = new ProxyConnectException("failed to remove all codec handlers added by the proxy handler; bug?");
            failPendingWritesAndClose(cause);
        }
    }

    private boolean safeRemoveDecoder() {
        try {
            removeDecoder(this.ctx);
            return true;
        } catch (Exception e) {
            logger.warn("Failed to remove proxy decoders:", (Throwable) e);
            return false;
        }
    }

    private boolean safeRemoveEncoder() {
        try {
            removeEncoder(this.ctx);
            return true;
        } catch (Exception e) {
            logger.warn("Failed to remove proxy encoders:", (Throwable) e);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setConnectFailure(Throwable cause) {
        this.finished = true;
        cancelConnectTimeoutFuture();
        if (!this.connectPromise.isDone()) {
            if (!(cause instanceof ProxyConnectException)) {
                cause = new ProxyConnectException(exceptionMessage(cause.toString()), cause);
            }
            safeRemoveDecoder();
            safeRemoveEncoder();
            failPendingWritesAndClose(cause);
        }
    }

    private void failPendingWritesAndClose(Throwable cause) {
        failPendingWrites(cause);
        this.connectPromise.tryFailure(cause);
        this.ctx.fireExceptionCaught(cause);
        this.ctx.close();
    }

    private void cancelConnectTimeoutFuture() {
        if (this.connectTimeoutFuture != null) {
            this.connectTimeoutFuture.cancel(false);
            this.connectTimeoutFuture = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final String exceptionMessage(String msg) {
        if (msg == null) {
            msg = "";
        }
        StringBuilder buf = new StringBuilder(msg.length() + 128).append(protocol()).append(", ").append(authScheme()).append(", ").append(this.proxyAddress).append(" => ").append(this.destinationAddress);
        if (!msg.isEmpty()) {
            buf.append(", ").append(msg);
        }
        return buf.toString();
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public final void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        if (this.suppressChannelReadComplete) {
            this.suppressChannelReadComplete = false;
            readIfNeeded(ctx);
        } else {
            ctx.fireChannelReadComplete();
        }
    }

    @Override // io.netty.channel.ChannelDuplexHandler, io.netty.channel.ChannelOutboundHandler
    public final void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (this.finished) {
            writePendingWrites();
            ctx.write(msg, promise);
        } else {
            addPendingWrite(ctx, msg, promise);
        }
    }

    @Override // io.netty.channel.ChannelDuplexHandler, io.netty.channel.ChannelOutboundHandler
    public final void flush(ChannelHandlerContext ctx) throws Exception {
        if (this.finished) {
            writePendingWrites();
            ctx.flush();
        } else {
            this.flushedPrematurely = true;
        }
    }

    private static void readIfNeeded(ChannelHandlerContext ctx) {
        if (!ctx.channel().config().isAutoRead()) {
            ctx.read();
        }
    }

    private void writePendingWrites() {
        if (this.pendingWrites != null) {
            this.pendingWrites.removeAndWriteAll();
            this.pendingWrites = null;
        }
    }

    private void failPendingWrites(Throwable cause) {
        if (this.pendingWrites != null) {
            this.pendingWrites.removeAndFailAll(cause);
            this.pendingWrites = null;
        }
    }

    private void addPendingWrite(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        PendingWriteQueue pendingWrites = this.pendingWrites;
        if (pendingWrites == null) {
            PendingWriteQueue pendingWriteQueue = new PendingWriteQueue(ctx);
            pendingWrites = pendingWriteQueue;
            this.pendingWrites = pendingWriteQueue;
        }
        pendingWrites.add(msg, promise);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class LazyChannelPromise extends DefaultPromise<Channel> {
        private LazyChannelPromise() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // io.netty.util.concurrent.DefaultPromise
        public EventExecutor executor() {
            if (ProxyHandler.this.ctx != null) {
                return ProxyHandler.this.ctx.executor();
            }
            throw new IllegalStateException();
        }
    }
}
