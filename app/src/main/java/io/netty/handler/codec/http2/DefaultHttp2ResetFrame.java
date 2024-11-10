package io.netty.handler.codec.http2;

import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;

/* loaded from: classes4.dex */
public final class DefaultHttp2ResetFrame extends AbstractHttp2StreamFrame implements Http2ResetFrame {
    private final long errorCode;

    public DefaultHttp2ResetFrame(Http2Error error) {
        this.errorCode = ((Http2Error) ObjectUtil.checkNotNull(error, "error")).code();
    }

    public DefaultHttp2ResetFrame(long errorCode) {
        this.errorCode = errorCode;
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2StreamFrame, io.netty.handler.codec.http2.Http2StreamFrame
    public DefaultHttp2ResetFrame stream(Http2FrameStream stream) {
        super.stream(stream);
        return this;
    }

    @Override // io.netty.handler.codec.http2.Http2Frame
    public String name() {
        return "RST_STREAM";
    }

    @Override // io.netty.handler.codec.http2.Http2ResetFrame
    public long errorCode() {
        return this.errorCode;
    }

    public String toString() {
        return StringUtil.simpleClassName(this) + "(stream=" + stream() + ", errorCode=" + this.errorCode + ')';
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2StreamFrame
    public boolean equals(Object o) {
        if (!(o instanceof DefaultHttp2ResetFrame)) {
            return false;
        }
        DefaultHttp2ResetFrame other = (DefaultHttp2ResetFrame) o;
        return super.equals(o) && this.errorCode == other.errorCode;
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2StreamFrame
    public int hashCode() {
        int hash = super.hashCode();
        return (hash * 31) + ((int) (this.errorCode ^ (this.errorCode >>> 32)));
    }
}
