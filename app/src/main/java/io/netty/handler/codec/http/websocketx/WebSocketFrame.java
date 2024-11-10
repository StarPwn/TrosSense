package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.DefaultByteBufHolder;
import io.netty.util.internal.StringUtil;

/* loaded from: classes4.dex */
public abstract class WebSocketFrame extends DefaultByteBufHolder {
    private final boolean finalFragment;
    private final int rsv;

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public abstract WebSocketFrame replace(ByteBuf byteBuf);

    /* JADX INFO: Access modifiers changed from: protected */
    public WebSocketFrame(ByteBuf binaryData) {
        this(true, 0, binaryData);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public WebSocketFrame(boolean finalFragment, int rsv, ByteBuf binaryData) {
        super(binaryData);
        this.finalFragment = finalFragment;
        this.rsv = rsv;
    }

    public boolean isFinalFragment() {
        return this.finalFragment;
    }

    public int rsv() {
        return this.rsv;
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public WebSocketFrame copy() {
        return (WebSocketFrame) super.copy();
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public WebSocketFrame duplicate() {
        return (WebSocketFrame) super.duplicate();
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public WebSocketFrame retainedDuplicate() {
        return (WebSocketFrame) super.retainedDuplicate();
    }

    @Override // io.netty.buffer.DefaultByteBufHolder
    public String toString() {
        return StringUtil.simpleClassName(this) + "(data: " + contentToString() + ')';
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public WebSocketFrame retain() {
        super.retain();
        return this;
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public WebSocketFrame retain(int increment) {
        super.retain(increment);
        return this;
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public WebSocketFrame touch() {
        super.touch();
        return this;
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public WebSocketFrame touch(Object hint) {
        super.touch(hint);
        return this;
    }
}
