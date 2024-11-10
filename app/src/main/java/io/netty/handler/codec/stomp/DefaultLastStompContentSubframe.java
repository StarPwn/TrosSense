package io.netty.handler.codec.stomp;

import io.netty.buffer.ByteBuf;

/* loaded from: classes4.dex */
public class DefaultLastStompContentSubframe extends DefaultStompContentSubframe implements LastStompContentSubframe {
    public DefaultLastStompContentSubframe(ByteBuf content) {
        super(content);
    }

    @Override // io.netty.handler.codec.stomp.DefaultStompContentSubframe, io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public LastStompContentSubframe copy() {
        return (LastStompContentSubframe) super.copy();
    }

    @Override // io.netty.handler.codec.stomp.DefaultStompContentSubframe, io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public LastStompContentSubframe duplicate() {
        return (LastStompContentSubframe) super.duplicate();
    }

    @Override // io.netty.handler.codec.stomp.DefaultStompContentSubframe, io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public LastStompContentSubframe retainedDuplicate() {
        return (LastStompContentSubframe) super.retainedDuplicate();
    }

    @Override // io.netty.handler.codec.stomp.DefaultStompContentSubframe, io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public LastStompContentSubframe replace(ByteBuf content) {
        return new DefaultLastStompContentSubframe(content);
    }

    @Override // io.netty.handler.codec.stomp.DefaultStompContentSubframe, io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public DefaultLastStompContentSubframe retain() {
        super.retain();
        return this;
    }

    @Override // io.netty.handler.codec.stomp.DefaultStompContentSubframe, io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public LastStompContentSubframe retain(int increment) {
        super.retain(increment);
        return this;
    }

    @Override // io.netty.handler.codec.stomp.DefaultStompContentSubframe, io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public LastStompContentSubframe touch() {
        super.touch();
        return this;
    }

    @Override // io.netty.handler.codec.stomp.DefaultStompContentSubframe, io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public LastStompContentSubframe touch(Object hint) {
        super.touch(hint);
        return this;
    }

    @Override // io.netty.handler.codec.stomp.DefaultStompContentSubframe, io.netty.buffer.DefaultByteBufHolder
    public String toString() {
        return "DefaultLastStompContent{decoderResult=" + decoderResult() + '}';
    }
}
