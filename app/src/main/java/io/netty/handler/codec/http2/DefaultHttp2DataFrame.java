package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;

/* loaded from: classes4.dex */
public final class DefaultHttp2DataFrame extends AbstractHttp2StreamFrame implements Http2DataFrame {
    private final ByteBuf content;
    private final boolean endStream;
    private final int initialFlowControlledBytes;
    private final int padding;

    public DefaultHttp2DataFrame(ByteBuf content) {
        this(content, false);
    }

    public DefaultHttp2DataFrame(boolean endStream) {
        this(Unpooled.EMPTY_BUFFER, endStream);
    }

    public DefaultHttp2DataFrame(ByteBuf content, boolean endStream) {
        this(content, endStream, 0);
    }

    public DefaultHttp2DataFrame(ByteBuf content, boolean endStream, int padding) {
        this.content = (ByteBuf) ObjectUtil.checkNotNull(content, "content");
        this.endStream = endStream;
        Http2CodecUtil.verifyPadding(padding);
        this.padding = padding;
        if (content().readableBytes() + padding > 2147483647L) {
            throw new IllegalArgumentException("content + padding must be <= Integer.MAX_VALUE");
        }
        this.initialFlowControlledBytes = content().readableBytes() + padding;
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2StreamFrame, io.netty.handler.codec.http2.Http2StreamFrame
    public DefaultHttp2DataFrame stream(Http2FrameStream stream) {
        super.stream(stream);
        return this;
    }

    @Override // io.netty.handler.codec.http2.Http2Frame
    public String name() {
        return "DATA";
    }

    @Override // io.netty.handler.codec.http2.Http2DataFrame
    public boolean isEndStream() {
        return this.endStream;
    }

    @Override // io.netty.handler.codec.http2.Http2DataFrame
    public int padding() {
        return this.padding;
    }

    @Override // io.netty.handler.codec.http2.Http2DataFrame, io.netty.buffer.ByteBufHolder
    public ByteBuf content() {
        return ByteBufUtil.ensureAccessible(this.content);
    }

    @Override // io.netty.handler.codec.http2.Http2DataFrame
    public int initialFlowControlledBytes() {
        return this.initialFlowControlledBytes;
    }

    @Override // io.netty.handler.codec.http2.Http2DataFrame, io.netty.buffer.ByteBufHolder
    public DefaultHttp2DataFrame copy() {
        return replace(content().copy());
    }

    @Override // io.netty.handler.codec.http2.Http2DataFrame, io.netty.buffer.ByteBufHolder
    public DefaultHttp2DataFrame duplicate() {
        return replace(content().duplicate());
    }

    @Override // io.netty.handler.codec.http2.Http2DataFrame, io.netty.buffer.ByteBufHolder
    public DefaultHttp2DataFrame retainedDuplicate() {
        return replace(content().retainedDuplicate());
    }

    @Override // io.netty.handler.codec.http2.Http2DataFrame, io.netty.buffer.ByteBufHolder
    public DefaultHttp2DataFrame replace(ByteBuf content) {
        return new DefaultHttp2DataFrame(content, this.endStream, this.padding);
    }

    @Override // io.netty.util.ReferenceCounted
    public int refCnt() {
        return this.content.refCnt();
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release() {
        return this.content.release();
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release(int decrement) {
        return this.content.release(decrement);
    }

    @Override // io.netty.util.ReferenceCounted
    public DefaultHttp2DataFrame retain() {
        this.content.retain();
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public DefaultHttp2DataFrame retain(int increment) {
        this.content.retain(increment);
        return this;
    }

    public String toString() {
        return StringUtil.simpleClassName(this) + "(stream=" + stream() + ", content=" + this.content + ", endStream=" + this.endStream + ", padding=" + this.padding + ')';
    }

    @Override // io.netty.util.ReferenceCounted
    public DefaultHttp2DataFrame touch() {
        this.content.touch();
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public DefaultHttp2DataFrame touch(Object hint) {
        this.content.touch(hint);
        return this;
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2StreamFrame
    public boolean equals(Object o) {
        if (!(o instanceof DefaultHttp2DataFrame)) {
            return false;
        }
        DefaultHttp2DataFrame other = (DefaultHttp2DataFrame) o;
        return super.equals(other) && this.content.equals(other.content()) && this.endStream == other.endStream && this.padding == other.padding;
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2StreamFrame
    public int hashCode() {
        return (((((super.hashCode() * 31) + this.content.hashCode()) * 31) + (!this.endStream ? 1 : 0)) * 31) + this.padding;
    }
}
