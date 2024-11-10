package io.netty.handler.codec.http.websocketx.extensions.compression;

import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionFilter;

/* loaded from: classes4.dex */
class PerFrameDeflateEncoder extends DeflateEncoder {
    PerFrameDeflateEncoder(int compressionLevel, int windowSize, boolean noContext) {
        super(compressionLevel, windowSize, noContext, WebSocketExtensionFilter.NEVER_SKIP);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PerFrameDeflateEncoder(int compressionLevel, int windowSize, boolean noContext, WebSocketExtensionFilter extensionEncoderFilter) {
        super(compressionLevel, windowSize, noContext, extensionEncoderFilter);
    }

    @Override // io.netty.handler.codec.MessageToMessageEncoder
    public boolean acceptOutboundMessage(Object msg) throws Exception {
        if (!super.acceptOutboundMessage(msg)) {
            return false;
        }
        WebSocketFrame wsFrame = (WebSocketFrame) msg;
        if (extensionEncoderFilter().mustSkip(wsFrame)) {
            return false;
        }
        return ((msg instanceof TextWebSocketFrame) || (msg instanceof BinaryWebSocketFrame) || (msg instanceof ContinuationWebSocketFrame)) && wsFrame.content().readableBytes() > 0 && (wsFrame.rsv() & 4) == 0;
    }

    @Override // io.netty.handler.codec.http.websocketx.extensions.compression.DeflateEncoder
    protected int rsv(WebSocketFrame msg) {
        return msg.rsv() | 4;
    }

    @Override // io.netty.handler.codec.http.websocketx.extensions.compression.DeflateEncoder
    protected boolean removeFrameTail(WebSocketFrame msg) {
        return true;
    }
}
