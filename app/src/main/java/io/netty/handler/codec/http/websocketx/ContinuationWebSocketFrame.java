package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

/* loaded from: classes4.dex */
public class ContinuationWebSocketFrame extends WebSocketFrame {
    public ContinuationWebSocketFrame() {
        this(Unpooled.buffer(0));
    }

    public ContinuationWebSocketFrame(ByteBuf binaryData) {
        super(binaryData);
    }

    public ContinuationWebSocketFrame(boolean finalFragment, int rsv, ByteBuf binaryData) {
        super(finalFragment, rsv, binaryData);
    }

    public ContinuationWebSocketFrame(boolean finalFragment, int rsv, String text) {
        this(finalFragment, rsv, fromText(text));
    }

    public String text() {
        return content().toString(CharsetUtil.UTF_8);
    }

    private static ByteBuf fromText(String text) {
        if (text == null || text.isEmpty()) {
            return Unpooled.EMPTY_BUFFER;
        }
        return Unpooled.copiedBuffer(text, CharsetUtil.UTF_8);
    }

    @Override // io.netty.handler.codec.http.websocketx.WebSocketFrame, io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public ContinuationWebSocketFrame copy() {
        return (ContinuationWebSocketFrame) super.copy();
    }

    @Override // io.netty.handler.codec.http.websocketx.WebSocketFrame, io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public ContinuationWebSocketFrame duplicate() {
        return (ContinuationWebSocketFrame) super.duplicate();
    }

    @Override // io.netty.handler.codec.http.websocketx.WebSocketFrame, io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public ContinuationWebSocketFrame retainedDuplicate() {
        return (ContinuationWebSocketFrame) super.retainedDuplicate();
    }

    @Override // io.netty.handler.codec.http.websocketx.WebSocketFrame, io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public ContinuationWebSocketFrame replace(ByteBuf content) {
        return new ContinuationWebSocketFrame(isFinalFragment(), rsv(), content);
    }

    @Override // io.netty.handler.codec.http.websocketx.WebSocketFrame, io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public ContinuationWebSocketFrame retain() {
        super.retain();
        return this;
    }

    @Override // io.netty.handler.codec.http.websocketx.WebSocketFrame, io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public ContinuationWebSocketFrame retain(int increment) {
        super.retain(increment);
        return this;
    }

    @Override // io.netty.handler.codec.http.websocketx.WebSocketFrame, io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public ContinuationWebSocketFrame touch() {
        super.touch();
        return this;
    }

    @Override // io.netty.handler.codec.http.websocketx.WebSocketFrame, io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public ContinuationWebSocketFrame touch(Object hint) {
        super.touch(hint);
        return this;
    }
}
