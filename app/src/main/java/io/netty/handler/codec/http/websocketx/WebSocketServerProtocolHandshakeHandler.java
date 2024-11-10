package io.netty.handler.codec.http.websocketx;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.ObjectUtil;
import java.util.concurrent.TimeUnit;

/* loaded from: classes4.dex */
class WebSocketServerProtocolHandshakeHandler extends ChannelInboundHandlerAdapter {
    private ChannelHandlerContext ctx;
    private ChannelPromise handshakePromise;
    private boolean isWebSocketPath;
    private final WebSocketServerProtocolConfig serverConfig;

    /* JADX INFO: Access modifiers changed from: package-private */
    public WebSocketServerProtocolHandshakeHandler(WebSocketServerProtocolConfig serverConfig) {
        this.serverConfig = (WebSocketServerProtocolConfig) ObjectUtil.checkNotNull(serverConfig, "serverConfig");
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerAdded(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        this.handshakePromise = ctx.newPromise();
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
        HttpObject httpObject = (HttpObject) msg;
        if (httpObject instanceof HttpRequest) {
            final HttpRequest req = (HttpRequest) httpObject;
            this.isWebSocketPath = isWebSocketPath(req);
            if (!this.isWebSocketPath) {
                ctx.fireChannelRead(msg);
                return;
            }
            try {
                WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(getWebSocketLocation(ctx.pipeline(), req, this.serverConfig.websocketPath()), this.serverConfig.subprotocols(), this.serverConfig.decoderConfig());
                final WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(req);
                final ChannelPromise localHandshakePromise = this.handshakePromise;
                if (handshaker == null) {
                    WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
                } else {
                    WebSocketServerProtocolHandler.setHandshaker(ctx.channel(), handshaker);
                    ctx.pipeline().remove(this);
                    ChannelFuture handshakeFuture = handshaker.handshake(ctx.channel(), req);
                    handshakeFuture.addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandshakeHandler.1
                        @Override // io.netty.util.concurrent.GenericFutureListener
                        public void operationComplete(ChannelFuture future) {
                            if (!future.isSuccess()) {
                                localHandshakePromise.tryFailure(future.cause());
                                ctx.fireExceptionCaught(future.cause());
                            } else {
                                localHandshakePromise.trySuccess();
                                ctx.fireUserEventTriggered((Object) WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE);
                                ctx.fireUserEventTriggered((Object) new WebSocketServerProtocolHandler.HandshakeComplete(req.uri(), req.headers(), handshaker.selectedSubprotocol()));
                            }
                        }
                    });
                    applyHandshakeTimeout();
                }
                return;
            } finally {
                ReferenceCountUtil.release(req);
            }
        }
        if (!this.isWebSocketPath) {
            ctx.fireChannelRead(msg);
        } else {
            ReferenceCountUtil.release(msg);
        }
    }

    private boolean isWebSocketPath(HttpRequest req) {
        String websocketPath = this.serverConfig.websocketPath();
        String uri = req.uri();
        boolean checkStartUri = uri.startsWith(websocketPath);
        boolean checkNextUri = "/".equals(websocketPath) || checkNextUri(uri, websocketPath);
        return this.serverConfig.checkStartsWith() ? checkStartUri && checkNextUri : uri.equals(websocketPath);
    }

    private boolean checkNextUri(String uri, String websocketPath) {
        char nextUri;
        int len = websocketPath.length();
        return uri.length() <= len || (nextUri = uri.charAt(len)) == '/' || nextUri == '?';
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, HttpRequest req, HttpResponse res) {
        ChannelFuture f = ctx.writeAndFlush(res);
        if (!HttpUtil.isKeepAlive(req) || res.status().code() != 200) {
            f.addListener((GenericFutureListener<? extends Future<? super Void>>) ChannelFutureListener.CLOSE);
        }
    }

    private static String getWebSocketLocation(ChannelPipeline cp, HttpRequest req, String path) {
        String protocol = "ws";
        if (cp.get(SslHandler.class) != null) {
            protocol = "wss";
        }
        String host = req.headers().get(HttpHeaderNames.HOST);
        return protocol + "://" + host + path;
    }

    private void applyHandshakeTimeout() {
        final ChannelPromise localHandshakePromise = this.handshakePromise;
        long handshakeTimeoutMillis = this.serverConfig.handshakeTimeoutMillis();
        if (handshakeTimeoutMillis <= 0 || localHandshakePromise.isDone()) {
            return;
        }
        final Future<?> timeoutFuture = this.ctx.executor().schedule(new Runnable() { // from class: io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandshakeHandler.2
            @Override // java.lang.Runnable
            public void run() {
                if (!localHandshakePromise.isDone() && localHandshakePromise.tryFailure(new WebSocketServerHandshakeException("handshake timed out"))) {
                    WebSocketServerProtocolHandshakeHandler.this.ctx.flush().fireUserEventTriggered((Object) WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_TIMEOUT).close();
                }
            }
        }, handshakeTimeoutMillis, TimeUnit.MILLISECONDS);
        localHandshakePromise.addListener((GenericFutureListener<? extends Future<? super Void>>) new FutureListener<Void>() { // from class: io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandshakeHandler.3
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(Future<Void> f) {
                timeoutFuture.cancel(false);
            }
        });
    }
}
