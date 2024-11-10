package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;

/* loaded from: classes4.dex */
public class DefaultHttpContent extends DefaultHttpObject implements HttpContent {
    private final ByteBuf content;

    public DefaultHttpContent(ByteBuf content) {
        this.content = (ByteBuf) ObjectUtil.checkNotNull(content, "content");
    }

    @Override // io.netty.buffer.ByteBufHolder
    public ByteBuf content() {
        return this.content;
    }

    @Override // io.netty.buffer.ByteBufHolder
    public HttpContent copy() {
        return replace(this.content.copy());
    }

    @Override // io.netty.buffer.ByteBufHolder
    public HttpContent duplicate() {
        return replace(this.content.duplicate());
    }

    @Override // io.netty.buffer.ByteBufHolder
    public HttpContent retainedDuplicate() {
        return replace(this.content.retainedDuplicate());
    }

    @Override // io.netty.buffer.ByteBufHolder
    public HttpContent replace(ByteBuf content) {
        return new DefaultHttpContent(content);
    }

    @Override // io.netty.util.ReferenceCounted
    public int refCnt() {
        return this.content.refCnt();
    }

    @Override // io.netty.util.ReferenceCounted
    public HttpContent retain() {
        this.content.retain();
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public HttpContent retain(int increment) {
        this.content.retain(increment);
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public HttpContent touch() {
        this.content.touch();
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public HttpContent touch(Object hint) {
        this.content.touch(hint);
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release() {
        return this.content.release();
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release(int decrement) {
        return this.content.release(decrement);
    }

    public String toString() {
        return StringUtil.simpleClassName(this) + "(data: " + content() + ", decoderResult: " + decoderResult() + ')';
    }
}
