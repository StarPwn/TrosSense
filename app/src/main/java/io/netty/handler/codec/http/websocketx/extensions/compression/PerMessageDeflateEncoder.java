package io.netty.handler.codec.http.websocketx.extensions.compression;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionFilter;
import java.util.List;

/* loaded from: classes4.dex */
class PerMessageDeflateEncoder extends DeflateEncoder {
    private boolean compressing;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.http.websocketx.extensions.compression.DeflateEncoder, io.netty.handler.codec.MessageToMessageEncoder
    public /* bridge */ /* synthetic */ void encode(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame, List list) throws Exception {
        encode(channelHandlerContext, webSocketFrame, (List<Object>) list);
    }

    PerMessageDeflateEncoder(int compressionLevel, int windowSize, boolean noContext) {
        super(compressionLevel, windowSize, noContext, WebSocketExtensionFilter.NEVER_SKIP);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PerMessageDeflateEncoder(int compressionLevel, int windowSize, boolean noContext, WebSocketExtensionFilter extensionEncoderFilter) {
        super(compressionLevel, windowSize, noContext, extensionEncoderFilter);
    }

    @Override // io.netty.handler.codec.MessageToMessageEncoder
    public boolean acceptOutboundMessage(Object msg) throws Exception {
        if (!super.acceptOutboundMessage(msg)) {
            return false;
        }
        WebSocketFrame wsFrame = (WebSocketFrame) msg;
        if (!extensionEncoderFilter().mustSkip(wsFrame)) {
            return (((wsFrame instanceof TextWebSocketFrame) || (wsFrame instanceof BinaryWebSocketFrame)) && (wsFrame.rsv() & 4) == 0) || ((wsFrame instanceof ContinuationWebSocketFrame) && this.compressing);
        }
        if (this.compressing) {
            throw new IllegalStateException("Cannot skip per message deflate encoder, compression in progress");
        }
        return false;
    }

    @Override // io.netty.handler.codec.http.websocketx.extensions.compression.DeflateEncoder
    protected int rsv(WebSocketFrame msg) {
        return ((msg instanceof TextWebSocketFrame) || (msg instanceof BinaryWebSocketFrame)) ? msg.rsv() | 4 : msg.rsv();
    }

    @Override // io.netty.handler.codec.http.websocketx.extensions.compression.DeflateEncoder
    protected boolean removeFrameTail(WebSocketFrame msg) {
        return msg.isFinalFragment();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.handler.codec.http.websocketx.extensions.compression.DeflateEncoder
    public void encode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {
        super.encode2(ctx, msg, out);
        if (msg.isFinalFragment()) {
            this.compressing = false;
        } else if ((msg instanceof TextWebSocketFrame) || (msg instanceof BinaryWebSocketFrame)) {
            this.compressing = true;
        }
    }
}
