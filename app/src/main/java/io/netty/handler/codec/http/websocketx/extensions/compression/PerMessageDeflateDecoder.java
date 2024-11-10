package io.netty.handler.codec.http.websocketx.extensions.compression;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionFilter;
import java.util.List;

/* loaded from: classes4.dex */
class PerMessageDeflateDecoder extends DeflateDecoder {
    private boolean compressing;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.http.websocketx.extensions.compression.DeflateDecoder, io.netty.handler.codec.MessageToMessageDecoder
    public /* bridge */ /* synthetic */ void decode(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame, List list) throws Exception {
        decode(channelHandlerContext, webSocketFrame, (List<Object>) list);
    }

    PerMessageDeflateDecoder(boolean noContext) {
        super(noContext, WebSocketExtensionFilter.NEVER_SKIP);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PerMessageDeflateDecoder(boolean noContext, WebSocketExtensionFilter extensionDecoderFilter) {
        super(noContext, extensionDecoderFilter);
    }

    @Override // io.netty.handler.codec.MessageToMessageDecoder
    public boolean acceptInboundMessage(Object msg) throws Exception {
        if (!super.acceptInboundMessage(msg)) {
            return false;
        }
        WebSocketFrame wsFrame = (WebSocketFrame) msg;
        if (!extensionDecoderFilter().mustSkip(wsFrame)) {
            return (((wsFrame instanceof TextWebSocketFrame) || (wsFrame instanceof BinaryWebSocketFrame)) && (wsFrame.rsv() & 4) > 0) || ((wsFrame instanceof ContinuationWebSocketFrame) && this.compressing);
        }
        if (this.compressing) {
            throw new IllegalStateException("Cannot skip per message deflate decoder, compression in progress");
        }
        return false;
    }

    @Override // io.netty.handler.codec.http.websocketx.extensions.compression.DeflateDecoder
    protected int newRsv(WebSocketFrame msg) {
        return (msg.rsv() & 4) > 0 ? msg.rsv() ^ 4 : msg.rsv();
    }

    @Override // io.netty.handler.codec.http.websocketx.extensions.compression.DeflateDecoder
    protected boolean appendFrameTail(WebSocketFrame msg) {
        return msg.isFinalFragment();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.handler.codec.http.websocketx.extensions.compression.DeflateDecoder
    public void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {
        super.decode2(ctx, msg, out);
        if (msg.isFinalFragment()) {
            this.compressing = false;
        } else if ((msg instanceof TextWebSocketFrame) || (msg instanceof BinaryWebSocketFrame)) {
            this.compressing = true;
        }
    }
}
