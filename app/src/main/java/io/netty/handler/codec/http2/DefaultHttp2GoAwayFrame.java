package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.DefaultByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;

/* loaded from: classes4.dex */
public final class DefaultHttp2GoAwayFrame extends DefaultByteBufHolder implements Http2GoAwayFrame {
    private final long errorCode;
    private int extraStreamIds;
    private final int lastStreamId;

    public DefaultHttp2GoAwayFrame(Http2Error error) {
        this(error.code());
    }

    public DefaultHttp2GoAwayFrame(long errorCode) {
        this(errorCode, Unpooled.EMPTY_BUFFER);
    }

    public DefaultHttp2GoAwayFrame(Http2Error error, ByteBuf content) {
        this(error.code(), content);
    }

    public DefaultHttp2GoAwayFrame(long errorCode, ByteBuf content) {
        this(-1, errorCode, content);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DefaultHttp2GoAwayFrame(int lastStreamId, long errorCode, ByteBuf content) {
        super(content);
        this.errorCode = errorCode;
        this.lastStreamId = lastStreamId;
    }

    @Override // io.netty.handler.codec.http2.Http2Frame
    public String name() {
        return "GOAWAY";
    }

    @Override // io.netty.handler.codec.http2.Http2GoAwayFrame
    public long errorCode() {
        return this.errorCode;
    }

    @Override // io.netty.handler.codec.http2.Http2GoAwayFrame
    public int extraStreamIds() {
        return this.extraStreamIds;
    }

    @Override // io.netty.handler.codec.http2.Http2GoAwayFrame
    public Http2GoAwayFrame setExtraStreamIds(int extraStreamIds) {
        ObjectUtil.checkPositiveOrZero(extraStreamIds, "extraStreamIds");
        this.extraStreamIds = extraStreamIds;
        return this;
    }

    @Override // io.netty.handler.codec.http2.Http2GoAwayFrame
    public int lastStreamId() {
        return this.lastStreamId;
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public Http2GoAwayFrame copy() {
        return new DefaultHttp2GoAwayFrame(this.lastStreamId, this.errorCode, content().copy());
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public Http2GoAwayFrame duplicate() {
        return (Http2GoAwayFrame) super.duplicate();
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public Http2GoAwayFrame retainedDuplicate() {
        return (Http2GoAwayFrame) super.retainedDuplicate();
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public Http2GoAwayFrame replace(ByteBuf content) {
        return new DefaultHttp2GoAwayFrame(this.errorCode, content).setExtraStreamIds(this.extraStreamIds);
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public Http2GoAwayFrame retain() {
        super.retain();
        return this;
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public Http2GoAwayFrame retain(int increment) {
        super.retain(increment);
        return this;
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public Http2GoAwayFrame touch() {
        super.touch();
        return this;
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public Http2GoAwayFrame touch(Object hint) {
        super.touch(hint);
        return this;
    }

    @Override // io.netty.buffer.DefaultByteBufHolder
    public boolean equals(Object o) {
        if (!(o instanceof DefaultHttp2GoAwayFrame)) {
            return false;
        }
        DefaultHttp2GoAwayFrame other = (DefaultHttp2GoAwayFrame) o;
        return this.errorCode == other.errorCode && this.extraStreamIds == other.extraStreamIds && super.equals(other);
    }

    @Override // io.netty.buffer.DefaultByteBufHolder
    public int hashCode() {
        int hash = super.hashCode();
        return (((hash * 31) + ((int) (this.errorCode ^ (this.errorCode >>> 32)))) * 31) + this.extraStreamIds;
    }

    @Override // io.netty.buffer.DefaultByteBufHolder
    public String toString() {
        return StringUtil.simpleClassName(this) + "(errorCode=" + this.errorCode + ", content=" + content() + ", extraStreamIds=" + this.extraStreamIds + ", lastStreamId=" + this.lastStreamId + ')';
    }
}
