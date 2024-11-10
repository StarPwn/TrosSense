package io.netty.handler.codec.http.websocketx.extensions.compression;

import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionFilter;

/* loaded from: classes4.dex */
class PerFrameDeflateDecoder extends DeflateDecoder {
    PerFrameDeflateDecoder(boolean noContext) {
        super(noContext, WebSocketExtensionFilter.NEVER_SKIP);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PerFrameDeflateDecoder(boolean noContext, WebSocketExtensionFilter extensionDecoderFilter) {
        super(noContext, extensionDecoderFilter);
    }

    @Override // io.netty.handler.codec.MessageToMessageDecoder
    public boolean acceptInboundMessage(Object msg) throws Exception {
        if (!super.acceptInboundMessage(msg)) {
            return false;
        }
        WebSocketFrame wsFrame = (WebSocketFrame) msg;
        if (extensionDecoderFilter().mustSkip(wsFrame)) {
            return false;
        }
        return ((msg instanceof TextWebSocketFrame) || (msg instanceof BinaryWebSocketFrame) || (msg instanceof ContinuationWebSocketFrame)) && (wsFrame.rsv() & 4) > 0;
    }

    @Override // io.netty.handler.codec.http.websocketx.extensions.compression.DeflateDecoder
    protected int newRsv(WebSocketFrame msg) {
        return msg.rsv() ^ 4;
    }

    @Override // io.netty.handler.codec.http.websocketx.extensions.compression.DeflateDecoder
    protected boolean appendFrameTail(WebSocketFrame msg) {
        return true;
    }
}
