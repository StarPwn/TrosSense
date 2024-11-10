package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.DefaultByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.util.internal.StringUtil;

/* loaded from: classes4.dex */
public final class DefaultHttp2UnknownFrame extends DefaultByteBufHolder implements Http2UnknownFrame {
    private final Http2Flags flags;
    private final byte frameType;
    private Http2FrameStream stream;

    public DefaultHttp2UnknownFrame(byte frameType, Http2Flags flags) {
        this(frameType, flags, Unpooled.EMPTY_BUFFER);
    }

    public DefaultHttp2UnknownFrame(byte frameType, Http2Flags flags, ByteBuf data) {
        super(data);
        this.frameType = frameType;
        this.flags = flags;
    }

    @Override // io.netty.handler.codec.http2.Http2UnknownFrame, io.netty.handler.codec.http2.Http2StreamFrame
    public Http2FrameStream stream() {
        return this.stream;
    }

    @Override // io.netty.handler.codec.http2.Http2UnknownFrame, io.netty.handler.codec.http2.Http2StreamFrame
    public DefaultHttp2UnknownFrame stream(Http2FrameStream stream) {
        this.stream = stream;
        return this;
    }

    @Override // io.netty.handler.codec.http2.Http2UnknownFrame
    public byte frameType() {
        return this.frameType;
    }

    @Override // io.netty.handler.codec.http2.Http2UnknownFrame
    public Http2Flags flags() {
        return this.flags;
    }

    @Override // io.netty.handler.codec.http2.Http2Frame
    public String name() {
        return "UNKNOWN";
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public DefaultHttp2UnknownFrame copy() {
        return replace(content().copy());
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public DefaultHttp2UnknownFrame duplicate() {
        return replace(content().duplicate());
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public DefaultHttp2UnknownFrame retainedDuplicate() {
        return replace(content().retainedDuplicate());
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public DefaultHttp2UnknownFrame replace(ByteBuf content) {
        return new DefaultHttp2UnknownFrame(this.frameType, this.flags, content).stream(this.stream);
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public DefaultHttp2UnknownFrame retain() {
        super.retain();
        return this;
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public DefaultHttp2UnknownFrame retain(int increment) {
        super.retain(increment);
        return this;
    }

    @Override // io.netty.buffer.DefaultByteBufHolder
    public String toString() {
        return StringUtil.simpleClassName(this) + "(frameType=" + ((int) this.frameType) + ", stream=" + this.stream + ", flags=" + this.flags + ", content=" + contentToString() + ')';
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public DefaultHttp2UnknownFrame touch() {
        super.touch();
        return this;
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public DefaultHttp2UnknownFrame touch(Object hint) {
        super.touch(hint);
        return this;
    }

    @Override // io.netty.buffer.DefaultByteBufHolder
    public boolean equals(Object o) {
        if (!(o instanceof DefaultHttp2UnknownFrame)) {
            return false;
        }
        DefaultHttp2UnknownFrame other = (DefaultHttp2UnknownFrame) o;
        Http2FrameStream otherStream = other.stream();
        return (this.stream == otherStream || (otherStream != null && otherStream.equals(this.stream))) && this.flags.equals(other.flags()) && this.frameType == other.frameType() && super.equals(other);
    }

    @Override // io.netty.buffer.DefaultByteBufHolder
    public int hashCode() {
        int hash = super.hashCode();
        int hash2 = (((hash * 31) + this.frameType) * 31) + this.flags.hashCode();
        if (this.stream != null) {
            return (hash2 * 31) + this.stream.hashCode();
        }
        return hash2;
    }
}
