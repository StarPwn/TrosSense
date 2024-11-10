package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundInvoker;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.EmptyHttpHeaders;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.nio.channels.ClosedChannelException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/* loaded from: classes4.dex */
public abstract class WebSocketServerHandshaker {
    public static final String SUB_PROTOCOL_WILDCARD = "*";
    protected static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) WebSocketServerHandshaker.class);
    private final WebSocketDecoderConfig decoderConfig;
    private String selectedSubprotocol;
    private final String[] subprotocols;
    private final String uri;
    private final WebSocketVersion version;

    protected abstract FullHttpResponse newHandshakeResponse(FullHttpRequest fullHttpRequest, HttpHeaders httpHeaders);

    protected abstract WebSocketFrameEncoder newWebSocketEncoder();

    protected abstract WebSocketFrameDecoder newWebsocketDecoder();

    protected WebSocketServerHandshaker(WebSocketVersion version, String uri, String subprotocols, int maxFramePayloadLength) {
        this(version, uri, subprotocols, WebSocketDecoderConfig.newBuilder().maxFramePayloadLength(maxFramePayloadLength).build());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public WebSocketServerHandshaker(WebSocketVersion version, String uri, String subprotocols, WebSocketDecoderConfig decoderConfig) {
        this.version = version;
        this.uri = uri;
        if (subprotocols != null) {
            String[] subprotocolArray = subprotocols.split(",");
            for (int i = 0; i < subprotocolArray.length; i++) {
                subprotocolArray[i] = subprotocolArray[i].trim();
            }
            this.subprotocols = subprotocolArray;
        } else {
            this.subprotocols = EmptyArrays.EMPTY_STRINGS;
        }
        this.decoderConfig = (WebSocketDecoderConfig) ObjectUtil.checkNotNull(decoderConfig, "decoderConfig");
    }

    public String uri() {
        return this.uri;
    }

    public Set<String> subprotocols() {
        Set<String> ret = new LinkedHashSet<>();
        Collections.addAll(ret, this.subprotocols);
        return ret;
    }

    public WebSocketVersion version() {
        return this.version;
    }

    public int maxFramePayloadLength() {
        return this.decoderConfig.maxFramePayloadLength();
    }

    public WebSocketDecoderConfig decoderConfig() {
        return this.decoderConfig;
    }

    public ChannelFuture handshake(Channel channel, FullHttpRequest req) {
        return handshake(channel, req, (HttpHeaders) null, channel.newPromise());
    }

    public final ChannelFuture handshake(Channel channel, FullHttpRequest req, HttpHeaders responseHeaders, final ChannelPromise promise) {
        final String encoderName;
        if (logger.isDebugEnabled()) {
            logger.debug("{} WebSocket version {} server handshake", channel, version());
        }
        FullHttpResponse response = newHandshakeResponse(req, responseHeaders);
        ChannelPipeline p = channel.pipeline();
        if (p.get(HttpObjectAggregator.class) != null) {
            p.remove(HttpObjectAggregator.class);
        }
        if (p.get(HttpContentCompressor.class) != null) {
            p.remove(HttpContentCompressor.class);
        }
        ChannelHandlerContext ctx = p.context(HttpRequestDecoder.class);
        if (ctx == null) {
            ChannelHandlerContext ctx2 = p.context(HttpServerCodec.class);
            if (ctx2 != null) {
                p.addBefore(ctx2.name(), "wsencoder", newWebSocketEncoder());
                p.addBefore(ctx2.name(), "wsdecoder", newWebsocketDecoder());
                encoderName = ctx2.name();
            } else {
                promise.setFailure((Throwable) new IllegalStateException("No HttpDecoder and no HttpServerCodec in the pipeline"));
                response.release();
                return promise;
            }
        } else {
            p.replace(ctx.name(), "wsdecoder", newWebsocketDecoder());
            String encoderName2 = p.context(HttpResponseEncoder.class).name();
            p.addBefore(encoderName2, "wsencoder", newWebSocketEncoder());
            encoderName = encoderName2;
        }
        channel.writeAndFlush(response).addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker.1
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    ChannelPipeline p2 = future.channel().pipeline();
                    p2.remove(encoderName);
                    promise.setSuccess();
                    return;
                }
                promise.setFailure(future.cause());
            }
        });
        return promise;
    }

    public ChannelFuture handshake(Channel channel, HttpRequest req) {
        return handshake(channel, req, (HttpHeaders) null, channel.newPromise());
    }

    public final ChannelFuture handshake(final Channel channel, HttpRequest req, final HttpHeaders responseHeaders, final ChannelPromise promise) {
        if (req instanceof FullHttpRequest) {
            return handshake(channel, (FullHttpRequest) req, responseHeaders, promise);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("{} WebSocket version {} server handshake", channel, version());
        }
        ChannelPipeline p = channel.pipeline();
        ChannelHandlerContext ctx = p.context(HttpRequestDecoder.class);
        if (ctx == null && (ctx = p.context(HttpServerCodec.class)) == null) {
            promise.setFailure((Throwable) new IllegalStateException("No HttpDecoder and no HttpServerCodec in the pipeline"));
            return promise;
        }
        String aggregatorCtx = ctx.name();
        if (HttpUtil.isContentLengthSet(req) || HttpUtil.isTransferEncodingChunked(req) || this.version == WebSocketVersion.V00) {
            aggregatorCtx = "httpAggregator";
            p.addAfter(ctx.name(), "httpAggregator", new HttpObjectAggregator(8192));
        }
        p.addAfter(aggregatorCtx, "handshaker", new ChannelInboundHandlerAdapter() { // from class: io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker.2
            static final /* synthetic */ boolean $assertionsDisabled = false;
            private FullHttpRequest fullHttpRequest;

            @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
            public void channelRead(ChannelHandlerContext ctx2, Object msg) throws Exception {
                if (msg instanceof HttpObject) {
                    try {
                        handleHandshakeRequest(ctx2, (HttpObject) msg);
                        return;
                    } finally {
                        ReferenceCountUtil.release(msg);
                    }
                }
                super.channelRead(ctx2, msg);
            }

            @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler, io.netty.channel.ChannelInboundHandler
            public void exceptionCaught(ChannelHandlerContext ctx2, Throwable cause) throws Exception {
                ctx2.pipeline().remove(this);
                promise.tryFailure(cause);
                ctx2.fireExceptionCaught(cause);
            }

            @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
            public void channelInactive(ChannelHandlerContext ctx2) throws Exception {
                try {
                    if (!promise.isDone()) {
                        promise.tryFailure(new ClosedChannelException());
                    }
                    ctx2.fireChannelInactive();
                } finally {
                    releaseFullHttpRequest();
                }
            }

            @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
            public void handlerRemoved(ChannelHandlerContext ctx2) throws Exception {
                releaseFullHttpRequest();
            }

            private void handleHandshakeRequest(ChannelHandlerContext ctx2, HttpObject httpObject) {
                if (httpObject instanceof FullHttpRequest) {
                    ctx2.pipeline().remove(this);
                    WebSocketServerHandshaker.this.handshake(channel, (FullHttpRequest) httpObject, responseHeaders, promise);
                    return;
                }
                if (httpObject instanceof LastHttpContent) {
                    if (this.fullHttpRequest == null) {
                        throw new AssertionError();
                    }
                    FullHttpRequest handshakeRequest = this.fullHttpRequest;
                    this.fullHttpRequest = null;
                    try {
                        ctx2.pipeline().remove(this);
                        WebSocketServerHandshaker.this.handshake(channel, handshakeRequest, responseHeaders, promise);
                        return;
                    } finally {
                        handshakeRequest.release();
                    }
                }
                if (httpObject instanceof HttpRequest) {
                    HttpRequest httpRequest = (HttpRequest) httpObject;
                    this.fullHttpRequest = new DefaultFullHttpRequest(httpRequest.protocolVersion(), httpRequest.method(), httpRequest.uri(), Unpooled.EMPTY_BUFFER, httpRequest.headers(), EmptyHttpHeaders.INSTANCE);
                    if (httpRequest.decoderResult().isFailure()) {
                        this.fullHttpRequest.setDecoderResult(httpRequest.decoderResult());
                    }
                }
            }

            private void releaseFullHttpRequest() {
                if (this.fullHttpRequest != null) {
                    this.fullHttpRequest.release();
                    this.fullHttpRequest = null;
                }
            }
        });
        try {
            ctx.fireChannelRead(ReferenceCountUtil.retain(req));
        } catch (Throwable cause) {
            promise.setFailure(cause);
        }
        return promise;
    }

    public ChannelFuture close(Channel channel, CloseWebSocketFrame frame) {
        ObjectUtil.checkNotNull(channel, "channel");
        return close(channel, frame, channel.newPromise());
    }

    public ChannelFuture close(Channel channel, CloseWebSocketFrame frame, ChannelPromise promise) {
        return close0(channel, frame, promise);
    }

    public ChannelFuture close(ChannelHandlerContext ctx, CloseWebSocketFrame frame) {
        ObjectUtil.checkNotNull(ctx, "ctx");
        return close(ctx, frame, ctx.newPromise());
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [io.netty.channel.ChannelFuture] */
    public ChannelFuture close(ChannelHandlerContext ctx, CloseWebSocketFrame frame, ChannelPromise promise) {
        ObjectUtil.checkNotNull(ctx, "ctx");
        return close0(ctx, frame, promise).addListener((GenericFutureListener<? extends Future<? super Void>>) ChannelFutureListener.CLOSE);
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [io.netty.channel.ChannelFuture] */
    private ChannelFuture close0(ChannelOutboundInvoker invoker, CloseWebSocketFrame frame, ChannelPromise promise) {
        return invoker.writeAndFlush(frame, promise).addListener((GenericFutureListener<? extends Future<? super Void>>) ChannelFutureListener.CLOSE);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String selectSubprotocol(String requestedSubprotocols) {
        if (requestedSubprotocols == null || this.subprotocols.length == 0) {
            return null;
        }
        String[] requestedSubprotocolArray = requestedSubprotocols.split(",");
        for (String p : requestedSubprotocolArray) {
            String requestedSubprotocol = p.trim();
            for (String supportedSubprotocol : this.subprotocols) {
                if ("*".equals(supportedSubprotocol) || requestedSubprotocol.equals(supportedSubprotocol)) {
                    this.selectedSubprotocol = requestedSubprotocol;
                    return requestedSubprotocol;
                }
            }
        }
        return null;
    }

    public String selectedSubprotocol() {
        return this.selectedSubprotocol;
    }
}
