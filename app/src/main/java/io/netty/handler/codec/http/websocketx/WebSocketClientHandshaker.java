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
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.EmptyHttpHeaders;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpScheme;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.NetUtil;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.ObjectUtil;
import java.net.URI;
import java.nio.channels.ClosedChannelException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/* loaded from: classes4.dex */
public abstract class WebSocketClientHandshaker {
    protected static final int DEFAULT_FORCE_CLOSE_TIMEOUT_MILLIS = 10000;
    private final boolean absoluteUpgradeUrl;
    private volatile String actualSubprotocol;
    protected final HttpHeaders customHeaders;
    private final String expectedSubprotocol;
    private volatile boolean forceCloseComplete;
    private volatile int forceCloseInit;
    private volatile long forceCloseTimeoutMillis;
    protected final boolean generateOriginHeader;
    private volatile boolean handshakeComplete;
    private final int maxFramePayloadLength;
    private final URI uri;
    private final WebSocketVersion version;
    private static final String HTTP_SCHEME_PREFIX = HttpScheme.HTTP + "://";
    private static final String HTTPS_SCHEME_PREFIX = HttpScheme.HTTPS + "://";
    private static final AtomicIntegerFieldUpdater<WebSocketClientHandshaker> FORCE_CLOSE_INIT_UPDATER = AtomicIntegerFieldUpdater.newUpdater(WebSocketClientHandshaker.class, "forceCloseInit");

    protected abstract FullHttpRequest newHandshakeRequest();

    protected abstract WebSocketFrameEncoder newWebSocketEncoder();

    protected abstract WebSocketFrameDecoder newWebsocketDecoder();

    protected abstract void verify(FullHttpResponse fullHttpResponse);

    protected WebSocketClientHandshaker(URI uri, WebSocketVersion version, String subprotocol, HttpHeaders customHeaders, int maxFramePayloadLength) {
        this(uri, version, subprotocol, customHeaders, maxFramePayloadLength, 10000L);
    }

    protected WebSocketClientHandshaker(URI uri, WebSocketVersion version, String subprotocol, HttpHeaders customHeaders, int maxFramePayloadLength, long forceCloseTimeoutMillis) {
        this(uri, version, subprotocol, customHeaders, maxFramePayloadLength, forceCloseTimeoutMillis, false);
    }

    protected WebSocketClientHandshaker(URI uri, WebSocketVersion version, String subprotocol, HttpHeaders customHeaders, int maxFramePayloadLength, long forceCloseTimeoutMillis, boolean absoluteUpgradeUrl) {
        this(uri, version, subprotocol, customHeaders, maxFramePayloadLength, forceCloseTimeoutMillis, absoluteUpgradeUrl, true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public WebSocketClientHandshaker(URI uri, WebSocketVersion version, String subprotocol, HttpHeaders customHeaders, int maxFramePayloadLength, long forceCloseTimeoutMillis, boolean absoluteUpgradeUrl, boolean generateOriginHeader) {
        this.forceCloseTimeoutMillis = 10000L;
        this.uri = uri;
        this.version = version;
        this.expectedSubprotocol = subprotocol;
        this.customHeaders = customHeaders;
        this.maxFramePayloadLength = maxFramePayloadLength;
        this.forceCloseTimeoutMillis = forceCloseTimeoutMillis;
        this.absoluteUpgradeUrl = absoluteUpgradeUrl;
        this.generateOriginHeader = generateOriginHeader;
    }

    public URI uri() {
        return this.uri;
    }

    public WebSocketVersion version() {
        return this.version;
    }

    public int maxFramePayloadLength() {
        return this.maxFramePayloadLength;
    }

    public boolean isHandshakeComplete() {
        return this.handshakeComplete;
    }

    private void setHandshakeComplete() {
        this.handshakeComplete = true;
    }

    public String expectedSubprotocol() {
        return this.expectedSubprotocol;
    }

    public String actualSubprotocol() {
        return this.actualSubprotocol;
    }

    private void setActualSubprotocol(String actualSubprotocol) {
        this.actualSubprotocol = actualSubprotocol;
    }

    public long forceCloseTimeoutMillis() {
        return this.forceCloseTimeoutMillis;
    }

    protected boolean isForceCloseComplete() {
        return this.forceCloseComplete;
    }

    public WebSocketClientHandshaker setForceCloseTimeoutMillis(long forceCloseTimeoutMillis) {
        this.forceCloseTimeoutMillis = forceCloseTimeoutMillis;
        return this;
    }

    public ChannelFuture handshake(Channel channel) {
        ObjectUtil.checkNotNull(channel, "channel");
        return handshake(channel, channel.newPromise());
    }

    public final ChannelFuture handshake(Channel channel, final ChannelPromise promise) {
        String originName;
        ChannelPipeline pipeline = channel.pipeline();
        HttpResponseDecoder decoder = (HttpResponseDecoder) pipeline.get(HttpResponseDecoder.class);
        if (decoder == null) {
            HttpClientCodec codec = (HttpClientCodec) pipeline.get(HttpClientCodec.class);
            if (codec == null) {
                promise.setFailure((Throwable) new IllegalStateException("ChannelPipeline does not contain an HttpResponseDecoder or HttpClientCodec"));
                return promise;
            }
        }
        if (this.uri.getHost() == null) {
            if (this.customHeaders == null || !this.customHeaders.contains(HttpHeaderNames.HOST)) {
                promise.setFailure((Throwable) new IllegalArgumentException("Cannot generate the 'host' header value, webSocketURI should contain host or passed through customHeaders"));
                return promise;
            }
            if (this.generateOriginHeader && !this.customHeaders.contains(HttpHeaderNames.ORIGIN)) {
                if (this.version == WebSocketVersion.V07 || this.version == WebSocketVersion.V08) {
                    originName = HttpHeaderNames.SEC_WEBSOCKET_ORIGIN.toString();
                } else {
                    originName = HttpHeaderNames.ORIGIN.toString();
                }
                promise.setFailure((Throwable) new IllegalArgumentException("Cannot generate the '" + originName + "' header value, webSocketURI should contain host or disable generateOriginHeader or pass value through customHeaders"));
                return promise;
            }
        }
        FullHttpRequest request = newHandshakeRequest();
        channel.writeAndFlush(request).addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker.1
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(ChannelFuture future) {
                if (future.isSuccess()) {
                    ChannelPipeline p = future.channel().pipeline();
                    ChannelHandlerContext ctx = p.context(HttpRequestEncoder.class);
                    if (ctx == null) {
                        ctx = p.context(HttpClientCodec.class);
                    }
                    if (ctx == null) {
                        promise.setFailure((Throwable) new IllegalStateException("ChannelPipeline does not contain an HttpRequestEncoder or HttpClientCodec"));
                        return;
                    } else {
                        p.addAfter(ctx.name(), "ws-encoder", WebSocketClientHandshaker.this.newWebSocketEncoder());
                        promise.setSuccess();
                        return;
                    }
                }
                promise.setFailure(future.cause());
            }
        });
        return promise;
    }

    public final void finishHandshake(Channel channel, FullHttpResponse response) {
        verify(response);
        String receivedProtocol = response.headers().get(HttpHeaderNames.SEC_WEBSOCKET_PROTOCOL);
        String receivedProtocol2 = receivedProtocol != null ? receivedProtocol.trim() : null;
        String expectedProtocol = this.expectedSubprotocol != null ? this.expectedSubprotocol : "";
        boolean protocolValid = false;
        if (expectedProtocol.isEmpty() && receivedProtocol2 == null) {
            protocolValid = true;
            setActualSubprotocol(this.expectedSubprotocol);
        } else if (!expectedProtocol.isEmpty() && receivedProtocol2 != null && !receivedProtocol2.isEmpty()) {
            String[] split = expectedProtocol.split(",");
            int length = split.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                String protocol = split[i];
                if (!protocol.trim().equals(receivedProtocol2)) {
                    i++;
                } else {
                    protocolValid = true;
                    setActualSubprotocol(receivedProtocol2);
                    break;
                }
            }
        }
        if (!protocolValid) {
            throw new WebSocketClientHandshakeException(String.format("Invalid subprotocol. Actual: %s. Expected one of: %s", receivedProtocol2, this.expectedSubprotocol), response);
        }
        setHandshakeComplete();
        final ChannelPipeline p = channel.pipeline();
        HttpContentDecompressor decompressor = (HttpContentDecompressor) p.get(HttpContentDecompressor.class);
        if (decompressor != null) {
            p.remove(decompressor);
        }
        HttpObjectAggregator aggregator = (HttpObjectAggregator) p.get(HttpObjectAggregator.class);
        if (aggregator != null) {
            p.remove(aggregator);
        }
        final ChannelHandlerContext ctx = p.context(HttpResponseDecoder.class);
        if (ctx == null) {
            ChannelHandlerContext ctx2 = p.context(HttpClientCodec.class);
            if (ctx2 == null) {
                throw new IllegalStateException("ChannelPipeline does not contain an HttpRequestEncoder or HttpClientCodec");
            }
            final HttpClientCodec codec = (HttpClientCodec) ctx2.handler();
            codec.removeOutboundHandler();
            p.addAfter(ctx2.name(), "ws-decoder", newWebsocketDecoder());
            channel.eventLoop().execute(new Runnable() { // from class: io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker.2
                @Override // java.lang.Runnable
                public void run() {
                    p.remove(codec);
                }
            });
            return;
        }
        if (p.get(HttpRequestEncoder.class) != null) {
            p.remove(HttpRequestEncoder.class);
        }
        p.addAfter(ctx.name(), "ws-decoder", newWebsocketDecoder());
        channel.eventLoop().execute(new Runnable() { // from class: io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker.3
            @Override // java.lang.Runnable
            public void run() {
                p.remove(ctx.handler());
            }
        });
    }

    public final ChannelFuture processHandshake(Channel channel, HttpResponse response) {
        return processHandshake(channel, response, channel.newPromise());
    }

    public final ChannelFuture processHandshake(final Channel channel, HttpResponse response, final ChannelPromise promise) {
        if (response instanceof FullHttpResponse) {
            try {
                finishHandshake(channel, (FullHttpResponse) response);
                promise.setSuccess();
            } catch (Throwable cause) {
                promise.setFailure(cause);
            }
        } else {
            ChannelPipeline p = channel.pipeline();
            ChannelHandlerContext ctx = p.context(HttpResponseDecoder.class);
            if (ctx == null && (ctx = p.context(HttpClientCodec.class)) == null) {
                return promise.setFailure((Throwable) new IllegalStateException("ChannelPipeline does not contain an HttpResponseDecoder or HttpClientCodec"));
            }
            String aggregatorCtx = ctx.name();
            if (this.version == WebSocketVersion.V00) {
                aggregatorCtx = "httpAggregator";
                p.addAfter(ctx.name(), "httpAggregator", new HttpObjectAggregator(8192));
            }
            p.addAfter(aggregatorCtx, "handshaker", new ChannelInboundHandlerAdapter() { // from class: io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker.4
                static final /* synthetic */ boolean $assertionsDisabled = false;
                private FullHttpResponse fullHttpResponse;

                @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
                public void channelRead(ChannelHandlerContext ctx2, Object msg) throws Exception {
                    if (msg instanceof HttpObject) {
                        try {
                            handleHandshakeResponse(ctx2, (HttpObject) msg);
                            return;
                        } finally {
                            ReferenceCountUtil.release(msg);
                        }
                    }
                    super.channelRead(ctx2, msg);
                }

                @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler, io.netty.channel.ChannelInboundHandler
                public void exceptionCaught(ChannelHandlerContext ctx2, Throwable cause2) throws Exception {
                    ctx2.pipeline().remove(this);
                    promise.setFailure(cause2);
                }

                @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
                public void channelInactive(ChannelHandlerContext ctx2) throws Exception {
                    try {
                        if (!promise.isDone()) {
                            promise.tryFailure(new ClosedChannelException());
                        }
                        ctx2.fireChannelInactive();
                    } finally {
                        releaseFullHttpResponse();
                    }
                }

                @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
                public void handlerRemoved(ChannelHandlerContext ctx2) throws Exception {
                    releaseFullHttpResponse();
                }

                private void handleHandshakeResponse(ChannelHandlerContext ctx2, HttpObject response2) {
                    if (response2 instanceof FullHttpResponse) {
                        ctx2.pipeline().remove(this);
                        tryFinishHandshake((FullHttpResponse) response2);
                        return;
                    }
                    if (response2 instanceof LastHttpContent) {
                        if (this.fullHttpResponse == null) {
                            throw new AssertionError();
                        }
                        FullHttpResponse handshakeResponse = this.fullHttpResponse;
                        this.fullHttpResponse = null;
                        try {
                            ctx2.pipeline().remove(this);
                            tryFinishHandshake(handshakeResponse);
                            return;
                        } finally {
                            handshakeResponse.release();
                        }
                    }
                    if (response2 instanceof HttpResponse) {
                        HttpResponse httpResponse = (HttpResponse) response2;
                        this.fullHttpResponse = new DefaultFullHttpResponse(httpResponse.protocolVersion(), httpResponse.status(), Unpooled.EMPTY_BUFFER, httpResponse.headers(), EmptyHttpHeaders.INSTANCE);
                        if (httpResponse.decoderResult().isFailure()) {
                            this.fullHttpResponse.setDecoderResult(httpResponse.decoderResult());
                        }
                    }
                }

                private void tryFinishHandshake(FullHttpResponse fullHttpResponse) {
                    try {
                        WebSocketClientHandshaker.this.finishHandshake(channel, fullHttpResponse);
                        promise.setSuccess();
                    } catch (Throwable cause2) {
                        promise.setFailure(cause2);
                    }
                }

                private void releaseFullHttpResponse() {
                    if (this.fullHttpResponse != null) {
                        this.fullHttpResponse.release();
                        this.fullHttpResponse = null;
                    }
                }
            });
            try {
                ctx.fireChannelRead(ReferenceCountUtil.retain(response));
            } catch (Throwable cause2) {
                promise.setFailure(cause2);
            }
        }
        return promise;
    }

    public ChannelFuture close(Channel channel, CloseWebSocketFrame frame) {
        ObjectUtil.checkNotNull(channel, "channel");
        return close(channel, frame, channel.newPromise());
    }

    public ChannelFuture close(Channel channel, CloseWebSocketFrame frame, ChannelPromise promise) {
        ObjectUtil.checkNotNull(channel, "channel");
        return close0(channel, channel, frame, promise);
    }

    public ChannelFuture close(ChannelHandlerContext ctx, CloseWebSocketFrame frame) {
        ObjectUtil.checkNotNull(ctx, "ctx");
        return close(ctx, frame, ctx.newPromise());
    }

    public ChannelFuture close(ChannelHandlerContext ctx, CloseWebSocketFrame frame, ChannelPromise promise) {
        ObjectUtil.checkNotNull(ctx, "ctx");
        return close0(ctx, ctx.channel(), frame, promise);
    }

    private ChannelFuture close0(final ChannelOutboundInvoker invoker, final Channel channel, CloseWebSocketFrame frame, ChannelPromise promise) {
        invoker.writeAndFlush(frame, promise);
        final long forceCloseTimeoutMillis = this.forceCloseTimeoutMillis;
        if (forceCloseTimeoutMillis > 0 && channel.isActive() && this.forceCloseInit == 0) {
            promise.addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker.5
                @Override // io.netty.util.concurrent.GenericFutureListener
                public void operationComplete(ChannelFuture future) {
                    if (future.isSuccess() && channel.isActive() && WebSocketClientHandshaker.FORCE_CLOSE_INIT_UPDATER.compareAndSet(handshaker, 0, 1)) {
                        final java.util.concurrent.Future<?> forceCloseFuture = channel.eventLoop().schedule(new Runnable() { // from class: io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker.5.1
                            @Override // java.lang.Runnable
                            public void run() {
                                if (channel.isActive()) {
                                    invoker.close();
                                    WebSocketClientHandshaker.this.forceCloseComplete = true;
                                }
                            }
                        }, forceCloseTimeoutMillis, TimeUnit.MILLISECONDS);
                        channel.closeFuture().addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker.5.2
                            @Override // io.netty.util.concurrent.GenericFutureListener
                            public void operationComplete(ChannelFuture future2) throws Exception {
                                forceCloseFuture.cancel(false);
                            }
                        });
                    }
                }
            });
            return promise;
        }
        return promise;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String upgradeUrl(URI wsURL) {
        if (this.absoluteUpgradeUrl) {
            return wsURL.toString();
        }
        String path = wsURL.getRawPath();
        String path2 = (path == null || path.isEmpty()) ? "/" : path;
        String query = wsURL.getRawQuery();
        return (query == null || query.isEmpty()) ? path2 : path2 + '?' + query;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static CharSequence websocketHostValue(URI wsURL) {
        int port = wsURL.getPort();
        if (port == -1) {
            return wsURL.getHost();
        }
        String host = wsURL.getHost();
        String scheme = wsURL.getScheme();
        if (port == HttpScheme.HTTP.port()) {
            if (HttpScheme.HTTP.name().contentEquals(scheme) || WebSocketScheme.WS.name().contentEquals(scheme)) {
                return host;
            }
            return NetUtil.toSocketAddressString(host, port);
        }
        if (port == HttpScheme.HTTPS.port()) {
            if (HttpScheme.HTTPS.name().contentEquals(scheme) || WebSocketScheme.WSS.name().contentEquals(scheme)) {
                return host;
            }
            return NetUtil.toSocketAddressString(host, port);
        }
        return NetUtil.toSocketAddressString(host, port);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static CharSequence websocketOriginValue(URI wsURL) {
        String schemePrefix;
        int defaultPort;
        String scheme = wsURL.getScheme();
        int port = wsURL.getPort();
        if (WebSocketScheme.WSS.name().contentEquals(scheme) || HttpScheme.HTTPS.name().contentEquals(scheme) || (scheme == null && port == WebSocketScheme.WSS.port())) {
            schemePrefix = HTTPS_SCHEME_PREFIX;
            defaultPort = WebSocketScheme.WSS.port();
        } else {
            schemePrefix = HTTP_SCHEME_PREFIX;
            defaultPort = WebSocketScheme.WS.port();
        }
        String host = wsURL.getHost().toLowerCase(Locale.US);
        if (port != defaultPort && port != -1) {
            return schemePrefix + NetUtil.toSocketAddressString(host, port);
        }
        return schemePrefix + host;
    }
}
