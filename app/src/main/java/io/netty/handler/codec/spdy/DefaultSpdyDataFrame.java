package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;

/* loaded from: classes4.dex */
public class DefaultSpdyDataFrame extends DefaultSpdyStreamFrame implements SpdyDataFrame {
    private final ByteBuf data;

    public DefaultSpdyDataFrame(int streamId) {
        this(streamId, Unpooled.buffer(0));
    }

    public DefaultSpdyDataFrame(int streamId, ByteBuf data) {
        super(streamId);
        this.data = validate((ByteBuf) ObjectUtil.checkNotNull(data, "data"));
    }

    private static ByteBuf validate(ByteBuf data) {
        if (data.readableBytes() > 16777215) {
            throw new IllegalArgumentException("data payload cannot exceed 16777215 bytes");
        }
        return data;
    }

    @Override // io.netty.handler.codec.spdy.DefaultSpdyStreamFrame, io.netty.handler.codec.spdy.SpdyStreamFrame, io.netty.handler.codec.spdy.SpdyDataFrame
    public SpdyDataFrame setStreamId(int streamId) {
        super.setStreamId(streamId);
        return this;
    }

    @Override // io.netty.handler.codec.spdy.DefaultSpdyStreamFrame, io.netty.handler.codec.spdy.SpdyStreamFrame, io.netty.handler.codec.spdy.SpdyDataFrame
    public SpdyDataFrame setLast(boolean last) {
        super.setLast(last);
        return this;
    }

    @Override // io.netty.handler.codec.spdy.SpdyDataFrame, io.netty.buffer.ByteBufHolder
    public ByteBuf content() {
        return ByteBufUtil.ensureAccessible(this.data);
    }

    @Override // io.netty.buffer.ByteBufHolder
    public SpdyDataFrame copy() {
        return replace(content().copy());
    }

    @Override // io.netty.buffer.ByteBufHolder
    public SpdyDataFrame duplicate() {
        return replace(content().duplicate());
    }

    @Override // io.netty.buffer.ByteBufHolder
    public SpdyDataFrame retainedDuplicate() {
        return replace(content().retainedDuplicate());
    }

    @Override // io.netty.buffer.ByteBufHolder
    public SpdyDataFrame replace(ByteBuf content) {
        SpdyDataFrame frame = new DefaultSpdyDataFrame(streamId(), content);
        frame.setLast(isLast());
        return frame;
    }

    @Override // io.netty.util.ReferenceCounted
    public int refCnt() {
        return this.data.refCnt();
    }

    @Override // io.netty.util.ReferenceCounted
    public SpdyDataFrame retain() {
        this.data.retain();
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public SpdyDataFrame retain(int increment) {
        this.data.retain(increment);
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public SpdyDataFrame touch() {
        this.data.touch();
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public SpdyDataFrame touch(Object hint) {
        this.data.touch(hint);
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release() {
        return this.data.release();
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release(int decrement) {
        return this.data.release(decrement);
    }

    public String toString() {
        StringBuilder buf = new StringBuilder().append(StringUtil.simpleClassName(this)).append("(last: ").append(isLast()).append(')').append(StringUtil.NEWLINE).append("--> Stream-ID = ").append(streamId()).append(StringUtil.NEWLINE).append("--> Size = ");
        if (refCnt() == 0) {
            buf.append("(freed)");
        } else {
            buf.append(content().readableBytes());
        }
        return buf.toString();
    }
}
