package io.netty.handler.proxy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.base64.Base64;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultHttpHeadersFactory;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpHeadersFactory;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.AsciiString;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.ObjectUtil;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/* loaded from: classes4.dex */
public final class HttpProxyHandler extends ProxyHandler {
    private static final String AUTH_BASIC = "basic";
    private static final String PROTOCOL = "http";
    private final CharSequence authorization;
    private final HttpClientCodecWrapper codecWrapper;
    private final boolean ignoreDefaultPortsInConnectHostHeader;
    private HttpHeaders inboundHeaders;
    private final HttpHeaders outboundHeaders;
    private final String password;
    private HttpResponseStatus status;
    private final String username;

    public HttpProxyHandler(SocketAddress proxyAddress) {
        this(proxyAddress, null);
    }

    public HttpProxyHandler(SocketAddress proxyAddress, HttpHeaders headers) {
        this(proxyAddress, headers, false);
    }

    public HttpProxyHandler(SocketAddress proxyAddress, HttpHeaders headers, boolean ignoreDefaultPortsInConnectHostHeader) {
        super(proxyAddress);
        this.codecWrapper = new HttpClientCodecWrapper();
        this.username = null;
        this.password = null;
        this.authorization = null;
        this.outboundHeaders = headers;
        this.ignoreDefaultPortsInConnectHostHeader = ignoreDefaultPortsInConnectHostHeader;
    }

    public HttpProxyHandler(SocketAddress proxyAddress, String username, String password) {
        this(proxyAddress, username, password, null);
    }

    public HttpProxyHandler(SocketAddress proxyAddress, String username, String password, HttpHeaders headers) {
        this(proxyAddress, username, password, headers, false);
    }

    public HttpProxyHandler(SocketAddress proxyAddress, String username, String password, HttpHeaders headers, boolean ignoreDefaultPortsInConnectHostHeader) {
        super(proxyAddress);
        this.codecWrapper = new HttpClientCodecWrapper();
        this.username = (String) ObjectUtil.checkNotNull(username, "username");
        this.password = (String) ObjectUtil.checkNotNull(password, "password");
        ByteBuf authz = Unpooled.copiedBuffer(username + ':' + password, CharsetUtil.UTF_8);
        try {
            ByteBuf authzBase64 = Base64.encode(authz, false);
            try {
                this.authorization = new AsciiString("Basic " + authzBase64.toString(CharsetUtil.US_ASCII));
                authzBase64.release();
                this.outboundHeaders = headers;
                this.ignoreDefaultPortsInConnectHostHeader = ignoreDefaultPortsInConnectHostHeader;
            } catch (Throwable th) {
                authzBase64.release();
                throw th;
            }
        } finally {
            authz.release();
        }
    }

    @Override // io.netty.handler.proxy.ProxyHandler
    public String protocol() {
        return PROTOCOL;
    }

    @Override // io.netty.handler.proxy.ProxyHandler
    public String authScheme() {
        return this.authorization != null ? AUTH_BASIC : "none";
    }

    public String username() {
        return this.username;
    }

    public String password() {
        return this.password;
    }

    @Override // io.netty.handler.proxy.ProxyHandler
    protected void addCodec(ChannelHandlerContext ctx) throws Exception {
        ChannelPipeline p = ctx.pipeline();
        String name = ctx.name();
        p.addBefore(name, null, this.codecWrapper);
    }

    @Override // io.netty.handler.proxy.ProxyHandler
    protected void removeEncoder(ChannelHandlerContext ctx) throws Exception {
        this.codecWrapper.codec.removeOutboundHandler();
    }

    @Override // io.netty.handler.proxy.ProxyHandler
    protected void removeDecoder(ChannelHandlerContext ctx) throws Exception {
        this.codecWrapper.codec.removeInboundHandler();
    }

    @Override // io.netty.handler.proxy.ProxyHandler
    protected Object newInitialMessage(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress raddr = (InetSocketAddress) destinationAddress();
        String hostString = HttpUtil.formatHostnameForHttp(raddr);
        int port = raddr.getPort();
        String url = hostString + ":" + port;
        String hostHeader = (this.ignoreDefaultPortsInConnectHostHeader && (port == 80 || port == 443)) ? hostString : url;
        HttpHeadersFactory headersFactory = DefaultHttpHeadersFactory.headersFactory().withValidation(false);
        FullHttpRequest req = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.CONNECT, url, Unpooled.EMPTY_BUFFER, headersFactory, headersFactory);
        req.headers().set(HttpHeaderNames.HOST, hostHeader);
        if (this.authorization != null) {
            req.headers().set(HttpHeaderNames.PROXY_AUTHORIZATION, this.authorization);
        }
        if (this.outboundHeaders != null) {
            req.headers().add(this.outboundHeaders);
        }
        return req;
    }

    @Override // io.netty.handler.proxy.ProxyHandler
    protected boolean handleResponse(ChannelHandlerContext ctx, Object response) throws Exception {
        if (response instanceof HttpResponse) {
            if (this.status != null) {
                throw new HttpProxyConnectException(exceptionMessage("too many responses"), null);
            }
            HttpResponse res = (HttpResponse) response;
            this.status = res.status();
            this.inboundHeaders = res.headers();
        }
        boolean finished = response instanceof LastHttpContent;
        if (finished) {
            if (this.status == null) {
                throw new HttpProxyConnectException(exceptionMessage("missing response"), this.inboundHeaders);
            }
            if (this.status.code() != 200) {
                throw new HttpProxyConnectException(exceptionMessage("status: " + this.status), this.inboundHeaders);
            }
        }
        return finished;
    }

    /* loaded from: classes4.dex */
    public static final class HttpProxyConnectException extends ProxyConnectException {
        private static final long serialVersionUID = -8824334609292146066L;
        private final HttpHeaders headers;

        public HttpProxyConnectException(String message, HttpHeaders headers) {
            super(message);
            this.headers = headers;
        }

        public HttpHeaders headers() {
            return this.headers;
        }
    }

    /* loaded from: classes4.dex */
    private static final class HttpClientCodecWrapper implements ChannelInboundHandler, ChannelOutboundHandler {
        final HttpClientCodec codec;

        private HttpClientCodecWrapper() {
            this.codec = new HttpClientCodec();
        }

        @Override // io.netty.channel.ChannelHandler
        public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
            this.codec.handlerAdded(ctx);
        }

        @Override // io.netty.channel.ChannelHandler
        public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
            this.codec.handlerRemoved(ctx);
        }

        @Override // io.netty.channel.ChannelInboundHandler
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            this.codec.exceptionCaught(ctx, cause);
        }

        @Override // io.netty.channel.ChannelInboundHandler
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            this.codec.channelRegistered(ctx);
        }

        @Override // io.netty.channel.ChannelInboundHandler
        public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
            this.codec.channelUnregistered(ctx);
        }

        @Override // io.netty.channel.ChannelInboundHandler
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            this.codec.channelActive(ctx);
        }

        @Override // io.netty.channel.ChannelInboundHandler
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            this.codec.channelInactive(ctx);
        }

        @Override // io.netty.channel.ChannelInboundHandler
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            this.codec.channelRead(ctx, msg);
        }

        @Override // io.netty.channel.ChannelInboundHandler
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            this.codec.channelReadComplete(ctx);
        }

        @Override // io.netty.channel.ChannelInboundHandler
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            this.codec.userEventTriggered(ctx, evt);
        }

        @Override // io.netty.channel.ChannelInboundHandler
        public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
            this.codec.channelWritabilityChanged(ctx);
        }

        @Override // io.netty.channel.ChannelOutboundHandler
        public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
            this.codec.bind(ctx, localAddress, promise);
        }

        @Override // io.netty.channel.ChannelOutboundHandler
        public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
            this.codec.connect(ctx, remoteAddress, localAddress, promise);
        }

        @Override // io.netty.channel.ChannelOutboundHandler
        public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
            this.codec.disconnect(ctx, promise);
        }

        @Override // io.netty.channel.ChannelOutboundHandler
        public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
            this.codec.close(ctx, promise);
        }

        @Override // io.netty.channel.ChannelOutboundHandler
        public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
            this.codec.deregister(ctx, promise);
        }

        @Override // io.netty.channel.ChannelOutboundHandler
        public void read(ChannelHandlerContext ctx) throws Exception {
            this.codec.read(ctx);
        }

        @Override // io.netty.channel.ChannelOutboundHandler
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            this.codec.write(ctx, msg, promise);
        }

        @Override // io.netty.channel.ChannelOutboundHandler
        public void flush(ChannelHandlerContext ctx) throws Exception {
            this.codec.flush(ctx);
        }
    }
}
