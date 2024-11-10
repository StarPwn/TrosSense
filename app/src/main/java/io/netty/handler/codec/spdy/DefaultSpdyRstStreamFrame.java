package io.netty.handler.codec.spdy;

import io.netty.util.internal.StringUtil;

/* loaded from: classes4.dex */
public class DefaultSpdyRstStreamFrame extends DefaultSpdyStreamFrame implements SpdyRstStreamFrame {
    private SpdyStreamStatus status;

    public DefaultSpdyRstStreamFrame(int streamId, int statusCode) {
        this(streamId, SpdyStreamStatus.valueOf(statusCode));
    }

    public DefaultSpdyRstStreamFrame(int streamId, SpdyStreamStatus status) {
        super(streamId);
        setStatus(status);
    }

    @Override // io.netty.handler.codec.spdy.DefaultSpdyStreamFrame, io.netty.handler.codec.spdy.SpdyStreamFrame, io.netty.handler.codec.spdy.SpdyDataFrame
    public SpdyRstStreamFrame setStreamId(int streamId) {
        super.setStreamId(streamId);
        return this;
    }

    @Override // io.netty.handler.codec.spdy.DefaultSpdyStreamFrame, io.netty.handler.codec.spdy.SpdyStreamFrame, io.netty.handler.codec.spdy.SpdyDataFrame
    public SpdyRstStreamFrame setLast(boolean last) {
        super.setLast(last);
        return this;
    }

    @Override // io.netty.handler.codec.spdy.SpdyRstStreamFrame
    public SpdyStreamStatus status() {
        return this.status;
    }

    @Override // io.netty.handler.codec.spdy.SpdyRstStreamFrame
    public SpdyRstStreamFrame setStatus(SpdyStreamStatus status) {
        this.status = status;
        return this;
    }

    public String toString() {
        return StringUtil.simpleClassName(this) + StringUtil.NEWLINE + "--> Stream-ID = " + streamId() + StringUtil.NEWLINE + "--> Status: " + status();
    }
}
