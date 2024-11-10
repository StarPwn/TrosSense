package io.netty.handler.codec.http.websocketx;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.ObjectUtil;
import java.util.concurrent.TimeUnit;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public class WebSocketClientProtocolHandshakeHandler extends ChannelInboundHandlerAdapter {
    private static final long DEFAULT_HANDSHAKE_TIMEOUT_MS = 10000;
    private ChannelHandlerContext ctx;
    private ChannelPromise handshakePromise;
    private final long handshakeTimeoutMillis;
    private final WebSocketClientHandshaker handshaker;

    WebSocketClientProtocolHandshakeHandler(WebSocketClientHandshaker handshaker) {
        this(handshaker, DEFAULT_HANDSHAKE_TIMEOUT_MS);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WebSocketClientProtocolHandshakeHandler(WebSocketClientHandshaker handshaker, long handshakeTimeoutMillis) {
        this.handshaker = handshaker;
        this.handshakeTimeoutMillis = ObjectUtil.checkPositive(handshakeTimeoutMillis, "handshakeTimeoutMillis");
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        this.handshakePromise = ctx.newPromise();
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.handshaker.handshake(ctx.channel()).addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandshakeHandler.1
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    WebSocketClientProtocolHandshakeHandler.this.handshakePromise.tryFailure(future.cause());
                    ctx.fireExceptionCaught(future.cause());
                } else {
                    ctx.fireUserEventTriggered((Object) WebSocketClientProtocolHandler.ClientHandshakeStateEvent.HANDSHAKE_ISSUED);
                }
            }
        });
        applyHandshakeTimeout();
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (!this.handshakePromise.isDone()) {
            this.handshakePromise.tryFailure(new WebSocketClientHandshakeException("channel closed with handshake in progress"));
        }
        super.channelInactive(ctx);
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof FullHttpResponse)) {
            ctx.fireChannelRead(msg);
            return;
        }
        FullHttpResponse response = (FullHttpResponse) msg;
        try {
            if (!this.handshaker.isHandshakeComplete()) {
                this.handshaker.finishHandshake(ctx.channel(), response);
                this.handshakePromise.trySuccess();
                ctx.fireUserEventTriggered((Object) WebSocketClientProtocolHandler.ClientHandshakeStateEvent.HANDSHAKE_COMPLETE);
                ctx.pipeline().remove(this);
                return;
            }
            throw new IllegalStateException("WebSocketClientHandshaker should have been non finished yet");
        } finally {
            response.release();
        }
    }

    private void applyHandshakeTimeout() {
        final ChannelPromise localHandshakePromise = this.handshakePromise;
        if (this.handshakeTimeoutMillis <= 0 || localHandshakePromise.isDone()) {
            return;
        }
        final Future<?> timeoutFuture = this.ctx.executor().schedule(new Runnable() { // from class: io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandshakeHandler.2
            @Override // java.lang.Runnable
            public void run() {
                if (!localHandshakePromise.isDone() && localHandshakePromise.tryFailure(new WebSocketClientHandshakeException("handshake timed out"))) {
                    WebSocketClientProtocolHandshakeHandler.this.ctx.flush().fireUserEventTriggered((Object) WebSocketClientProtocolHandler.ClientHandshakeStateEvent.HANDSHAKE_TIMEOUT).close();
                }
            }
        }, this.handshakeTimeoutMillis, TimeUnit.MILLISECONDS);
        localHandshakePromise.addListener((GenericFutureListener<? extends Future<? super Void>>) new FutureListener<Void>() { // from class: io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandshakeHandler.3
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(Future<Void> f) throws Exception {
                timeoutFuture.cancel(false);
            }
        });
    }

    ChannelFuture getHandshakeFuture() {
        return this.handshakePromise;
    }
}
