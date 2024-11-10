package io.netty.handler.codec.spdy;

import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public abstract class DefaultSpdyStreamFrame implements SpdyStreamFrame {
    private boolean last;
    private int streamId;

    /* JADX INFO: Access modifiers changed from: protected */
    public DefaultSpdyStreamFrame(int streamId) {
        setStreamId(streamId);
    }

    @Override // io.netty.handler.codec.spdy.SpdyStreamFrame
    public int streamId() {
        return this.streamId;
    }

    @Override // io.netty.handler.codec.spdy.SpdyStreamFrame, io.netty.handler.codec.spdy.SpdyDataFrame
    public SpdyStreamFrame setStreamId(int streamId) {
        ObjectUtil.checkPositive(streamId, "streamId");
        this.streamId = streamId;
        return this;
    }

    @Override // io.netty.handler.codec.spdy.SpdyStreamFrame
    public boolean isLast() {
        return this.last;
    }

    @Override // io.netty.handler.codec.spdy.SpdyStreamFrame, io.netty.handler.codec.spdy.SpdyDataFrame
    public SpdyStreamFrame setLast(boolean last) {
        this.last = last;
        return this;
    }
}
