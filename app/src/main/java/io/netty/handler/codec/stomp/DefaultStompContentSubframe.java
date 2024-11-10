package io.netty.handler.codec.stomp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.DefaultByteBufHolder;
import io.netty.handler.codec.DecoderResult;

/* loaded from: classes4.dex */
public class DefaultStompContentSubframe extends DefaultByteBufHolder implements StompContentSubframe {
    private DecoderResult decoderResult;

    public DefaultStompContentSubframe(ByteBuf content) {
        super(content);
        this.decoderResult = DecoderResult.SUCCESS;
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public StompContentSubframe copy() {
        return (StompContentSubframe) super.copy();
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public StompContentSubframe duplicate() {
        return (StompContentSubframe) super.duplicate();
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public StompContentSubframe retainedDuplicate() {
        return (StompContentSubframe) super.retainedDuplicate();
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public StompContentSubframe replace(ByteBuf content) {
        return new DefaultStompContentSubframe(content);
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public StompContentSubframe retain() {
        super.retain();
        return this;
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public StompContentSubframe retain(int increment) {
        super.retain(increment);
        return this;
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public StompContentSubframe touch() {
        super.touch();
        return this;
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public StompContentSubframe touch(Object hint) {
        super.touch(hint);
        return this;
    }

    @Override // io.netty.handler.codec.DecoderResultProvider
    public DecoderResult decoderResult() {
        return this.decoderResult;
    }

    @Override // io.netty.handler.codec.DecoderResultProvider
    public void setDecoderResult(DecoderResult decoderResult) {
        this.decoderResult = decoderResult;
    }

    @Override // io.netty.buffer.DefaultByteBufHolder
    public String toString() {
        return "DefaultStompContent{decoderResult=" + this.decoderResult + '}';
    }
}
