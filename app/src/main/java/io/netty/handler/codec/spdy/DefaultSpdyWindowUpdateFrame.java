package io.netty.handler.codec.spdy;

import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;

/* loaded from: classes4.dex */
public class DefaultSpdyWindowUpdateFrame implements SpdyWindowUpdateFrame {
    private int deltaWindowSize;
    private int streamId;

    public DefaultSpdyWindowUpdateFrame(int streamId, int deltaWindowSize) {
        setStreamId(streamId);
        setDeltaWindowSize(deltaWindowSize);
    }

    @Override // io.netty.handler.codec.spdy.SpdyWindowUpdateFrame
    public int streamId() {
        return this.streamId;
    }

    @Override // io.netty.handler.codec.spdy.SpdyWindowUpdateFrame
    public SpdyWindowUpdateFrame setStreamId(int streamId) {
        ObjectUtil.checkPositiveOrZero(streamId, "streamId");
        this.streamId = streamId;
        return this;
    }

    @Override // io.netty.handler.codec.spdy.SpdyWindowUpdateFrame
    public int deltaWindowSize() {
        return this.deltaWindowSize;
    }

    @Override // io.netty.handler.codec.spdy.SpdyWindowUpdateFrame
    public SpdyWindowUpdateFrame setDeltaWindowSize(int deltaWindowSize) {
        ObjectUtil.checkPositive(deltaWindowSize, "deltaWindowSize");
        this.deltaWindowSize = deltaWindowSize;
        return this;
    }

    public String toString() {
        return StringUtil.simpleClassName(this) + StringUtil.NEWLINE + "--> Stream-ID = " + streamId() + StringUtil.NEWLINE + "--> Delta-Window-Size = " + deltaWindowSize();
    }
}
