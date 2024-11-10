package io.netty.handler.codec.http.websocketx;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.internal.ObjectUtil;
import java.net.SocketAddress;
import java.net.URI;
import java.util.List;

/* loaded from: classes4.dex */
public class WebSocketClientProtocolHandler extends WebSocketProtocolHandler {
    private final WebSocketClientProtocolConfig clientConfig;
    private final WebSocketClientHandshaker handshaker;

    /* loaded from: classes4.dex */
    public enum ClientHandshakeStateEvent {
        HANDSHAKE_TIMEOUT,
        HANDSHAKE_ISSUED,
        HANDSHAKE_COMPLETE
    }

    @Override // io.netty.handler.codec.http.websocketx.WebSocketProtocolHandler, io.netty.channel.ChannelOutboundHandler
    public /* bridge */ /* synthetic */ void bind(ChannelHandlerContext channelHandlerContext, SocketAddress socketAddress, ChannelPromise channelPromise) throws Exception {
        super.bind(channelHandlerContext, socketAddress, channelPromise);
    }

    @Override // io.netty.handler.codec.http.websocketx.WebSocketProtocolHandler, io.netty.channel.ChannelOutboundHandler
    public /* bridge */ /* synthetic */ void close(ChannelHandlerContext channelHandlerContext, ChannelPromise channelPromise) throws Exception {
        super.close(channelHandlerContext, channelPromise);
    }

    @Override // io.netty.handler.codec.http.websocketx.WebSocketProtocolHandler, io.netty.channel.ChannelOutboundHandler
    public /* bridge */ /* synthetic */ void connect(ChannelHandlerContext channelHandlerContext, SocketAddress socketAddress, SocketAddress socketAddress2, ChannelPromise channelPromise) throws Exception {
        super.connect(channelHandlerContext, socketAddress, socketAddress2, channelPromise);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.http.websocketx.WebSocketProtocolHandler, io.netty.handler.codec.MessageToMessageDecoder
    public /* bridge */ /* synthetic */ void decode(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame, List list) throws Exception {
        decode(channelHandlerContext, webSocketFrame, (List<Object>) list);
    }

    @Override // io.netty.handler.codec.http.websocketx.WebSocketProtocolHandler, io.netty.channel.ChannelOutboundHandler
    public /* bridge */ /* synthetic */ void deregister(ChannelHandlerContext channelHandlerContext, ChannelPromise channelPromise) throws Exception {
        super.deregister(channelHandlerContext, channelPromise);
    }

    @Override // io.netty.handler.codec.http.websocketx.WebSocketProtocolHandler, io.netty.channel.ChannelOutboundHandler
    public /* bridge */ /* synthetic */ void disconnect(ChannelHandlerContext channelHandlerContext, ChannelPromise channelPromise) throws Exception {
        super.disconnect(channelHandlerContext, channelPromise);
    }

    @Override // io.netty.handler.codec.http.websocketx.WebSocketProtocolHandler, io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler, io.netty.channel.ChannelInboundHandler
    public /* bridge */ /* synthetic */ void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable th) throws Exception {
        super.exceptionCaught(channelHandlerContext, th);
    }

    @Override // io.netty.handler.codec.http.websocketx.WebSocketProtocolHandler, io.netty.channel.ChannelOutboundHandler
    public /* bridge */ /* synthetic */ void flush(ChannelHandlerContext channelHandlerContext) throws Exception {
        super.flush(channelHandlerContext);
    }

    @Override // io.netty.handler.codec.http.websocketx.WebSocketProtocolHandler, io.netty.channel.ChannelOutboundHandler
    public /* bridge */ /* synthetic */ void read(ChannelHandlerContext channelHandlerContext) throws Exception {
        super.read(channelHandlerContext);
    }

    @Override // io.netty.handler.codec.http.websocketx.WebSocketProtocolHandler, io.netty.channel.ChannelOutboundHandler
    public /* bridge */ /* synthetic */ void write(ChannelHandlerContext channelHandlerContext, Object obj, ChannelPromise channelPromise) throws Exception {
        super.write(channelHandlerContext, obj, channelPromise);
    }

    public WebSocketClientHandshaker handshaker() {
        return this.handshaker;
    }

    public WebSocketClientProtocolHandler(WebSocketClientProtocolConfig clientConfig) {
        super(((WebSocketClientProtocolConfig) ObjectUtil.checkNotNull(clientConfig, "clientConfig")).dropPongFrames(), clientConfig.sendCloseFrame(), clientConfig.forceCloseTimeoutMillis());
        this.handshaker = WebSocketClientHandshakerFactory.newHandshaker(clientConfig.webSocketUri(), clientConfig.version(), clientConfig.subprotocol(), clientConfig.allowExtensions(), clientConfig.customHeaders(), clientConfig.maxFramePayloadLength(), clientConfig.performMasking(), clientConfig.allowMaskMismatch(), clientConfig.forceCloseTimeoutMillis(), clientConfig.absoluteUpgradeUrl(), clientConfig.generateOriginHeader());
        this.clientConfig = clientConfig;
    }

    public WebSocketClientProtocolHandler(WebSocketClientHandshaker handshaker, WebSocketClientProtocolConfig clientConfig) {
        super(((WebSocketClientProtocolConfig) ObjectUtil.checkNotNull(clientConfig, "clientConfig")).dropPongFrames(), clientConfig.sendCloseFrame(), clientConfig.forceCloseTimeoutMillis());
        this.handshaker = handshaker;
        this.clientConfig = clientConfig;
    }

    public WebSocketClientProtocolHandler(URI webSocketURL, WebSocketVersion version, String subprotocol, boolean allowExtensions, HttpHeaders customHeaders, int maxFramePayloadLength, boolean handleCloseFrames, boolean performMasking, boolean allowMaskMismatch) {
        this(webSocketURL, version, subprotocol, allowExtensions, customHeaders, maxFramePayloadLength, handleCloseFrames, performMasking, allowMaskMismatch, 10000L);
    }

    public WebSocketClientProtocolHandler(URI webSocketURL, WebSocketVersion version, String subprotocol, boolean allowExtensions, HttpHeaders customHeaders, int maxFramePayloadLength, boolean handleCloseFrames, boolean performMasking, boolean allowMaskMismatch, long handshakeTimeoutMillis) {
        this(WebSocketClientHandshakerFactory.newHandshaker(webSocketURL, version, subprotocol, allowExtensions, customHeaders, maxFramePayloadLength, performMasking, allowMaskMismatch), handleCloseFrames, handshakeTimeoutMillis);
    }

    public WebSocketClientProtocolHandler(URI webSocketURL, WebSocketVersion version, String subprotocol, boolean allowExtensions, HttpHeaders customHeaders, int maxFramePayloadLength, boolean handleCloseFrames) {
        this(webSocketURL, version, subprotocol, allowExtensions, customHeaders, maxFramePayloadLength, handleCloseFrames, 10000L);
    }

    public WebSocketClientProtocolHandler(URI webSocketURL, WebSocketVersion version, String subprotocol, boolean allowExtensions, HttpHeaders customHeaders, int maxFramePayloadLength, boolean handleCloseFrames, long handshakeTimeoutMillis) {
        this(webSocketURL, version, subprotocol, allowExtensions, customHeaders, maxFramePayloadLength, handleCloseFrames, true, false, handshakeTimeoutMillis);
    }

    public WebSocketClientProtocolHandler(URI webSocketURL, WebSocketVersion version, String subprotocol, boolean allowExtensions, HttpHeaders customHeaders, int maxFramePayloadLength) {
        this(webSocketURL, version, subprotocol, allowExtensions, customHeaders, maxFramePayloadLength, 10000L);
    }

    public WebSocketClientProtocolHandler(URI webSocketURL, WebSocketVersion version, String subprotocol, boolean allowExtensions, HttpHeaders customHeaders, int maxFramePayloadLength, long handshakeTimeoutMillis) {
        this(webSocketURL, version, subprotocol, allowExtensions, customHeaders, maxFramePayloadLength, true, handshakeTimeoutMillis);
    }

    public WebSocketClientProtocolHandler(WebSocketClientHandshaker handshaker, boolean handleCloseFrames) {
        this(handshaker, handleCloseFrames, 10000L);
    }

    public WebSocketClientProtocolHandler(WebSocketClientHandshaker handshaker, boolean handleCloseFrames, long handshakeTimeoutMillis) {
        this(handshaker, handleCloseFrames, true, handshakeTimeoutMillis);
    }

    public WebSocketClientProtocolHandler(WebSocketClientHandshaker handshaker, boolean handleCloseFrames, boolean dropPongFrames) {
        this(handshaker, handleCloseFrames, dropPongFrames, 10000L);
    }

    public WebSocketClientProtocolHandler(WebSocketClientHandshaker handshaker, boolean handleCloseFrames, boolean dropPongFrames, long handshakeTimeoutMillis) {
        this(handshaker, handleCloseFrames, dropPongFrames, handshakeTimeoutMillis, true);
    }

    public WebSocketClientProtocolHandler(WebSocketClientHandshaker handshaker, boolean handleCloseFrames, boolean dropPongFrames, long handshakeTimeoutMillis, boolean withUTF8Validator) {
        super(dropPongFrames);
        this.handshaker = handshaker;
        this.clientConfig = WebSocketClientProtocolConfig.newBuilder().handleCloseFrames(handleCloseFrames).handshakeTimeoutMillis(handshakeTimeoutMillis).withUTF8Validator(withUTF8Validator).build();
    }

    public WebSocketClientProtocolHandler(WebSocketClientHandshaker handshaker) {
        this(handshaker, 10000L);
    }

    public WebSocketClientProtocolHandler(WebSocketClientHandshaker handshaker, long handshakeTimeoutMillis) {
        this(handshaker, true, handshakeTimeoutMillis);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.handler.codec.http.websocketx.WebSocketProtocolHandler
    public void decode(ChannelHandlerContext ctx, WebSocketFrame frame, List<Object> out) throws Exception {
        if (this.clientConfig.handleCloseFrames() && (frame instanceof CloseWebSocketFrame)) {
            ctx.close();
        } else {
            super.decode(ctx, frame, out);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.http.websocketx.WebSocketProtocolHandler
    public WebSocketClientHandshakeException buildHandshakeException(String message) {
        return new WebSocketClientHandshakeException(message);
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerAdded(ChannelHandlerContext ctx) {
        ChannelPipeline cp = ctx.pipeline();
        if (cp.get(WebSocketClientProtocolHandshakeHandler.class) == null) {
            ctx.pipeline().addBefore(ctx.name(), WebSocketClientProtocolHandshakeHandler.class.getName(), new WebSocketClientProtocolHandshakeHandler(this.handshaker, this.clientConfig.handshakeTimeoutMillis()));
        }
        if (this.clientConfig.withUTF8Validator() && cp.get(Utf8FrameValidator.class) == null) {
            ctx.pipeline().addBefore(ctx.name(), Utf8FrameValidator.class.getName(), new Utf8FrameValidator());
        }
    }
}
