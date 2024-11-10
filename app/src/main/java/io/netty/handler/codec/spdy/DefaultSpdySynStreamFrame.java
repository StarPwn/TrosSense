package io.netty.handler.codec.spdy;

import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;

/* loaded from: classes4.dex */
public class DefaultSpdySynStreamFrame extends DefaultSpdyHeadersFrame implements SpdySynStreamFrame {
    private int associatedStreamId;
    private byte priority;
    private boolean unidirectional;

    public DefaultSpdySynStreamFrame(int streamId, int associatedStreamId, byte priority) {
        this(streamId, associatedStreamId, priority, true);
    }

    public DefaultSpdySynStreamFrame(int streamId, int associatedStreamId, byte priority, boolean validateHeaders) {
        super(streamId, validateHeaders);
        setAssociatedStreamId(associatedStreamId);
        setPriority(priority);
    }

    @Override // io.netty.handler.codec.spdy.DefaultSpdyHeadersFrame, io.netty.handler.codec.spdy.DefaultSpdyStreamFrame, io.netty.handler.codec.spdy.SpdyStreamFrame, io.netty.handler.codec.spdy.SpdyDataFrame
    public SpdySynStreamFrame setStreamId(int streamId) {
        super.setStreamId(streamId);
        return this;
    }

    @Override // io.netty.handler.codec.spdy.DefaultSpdyHeadersFrame, io.netty.handler.codec.spdy.DefaultSpdyStreamFrame, io.netty.handler.codec.spdy.SpdyStreamFrame, io.netty.handler.codec.spdy.SpdyDataFrame
    public SpdySynStreamFrame setLast(boolean last) {
        super.setLast(last);
        return this;
    }

    @Override // io.netty.handler.codec.spdy.DefaultSpdyHeadersFrame, io.netty.handler.codec.spdy.SpdyHeadersFrame
    public SpdySynStreamFrame setInvalid() {
        super.setInvalid();
        return this;
    }

    @Override // io.netty.handler.codec.spdy.SpdySynStreamFrame
    public int associatedStreamId() {
        return this.associatedStreamId;
    }

    @Override // io.netty.handler.codec.spdy.SpdySynStreamFrame
    public SpdySynStreamFrame setAssociatedStreamId(int associatedStreamId) {
        ObjectUtil.checkPositiveOrZero(associatedStreamId, "associatedStreamId");
        this.associatedStreamId = associatedStreamId;
        return this;
    }

    @Override // io.netty.handler.codec.spdy.SpdySynStreamFrame
    public byte priority() {
        return this.priority;
    }

    @Override // io.netty.handler.codec.spdy.SpdySynStreamFrame
    public SpdySynStreamFrame setPriority(byte priority) {
        if (priority < 0 || priority > 7) {
            throw new IllegalArgumentException("Priority must be between 0 and 7 inclusive: " + ((int) priority));
        }
        this.priority = priority;
        return this;
    }

    @Override // io.netty.handler.codec.spdy.SpdySynStreamFrame
    public boolean isUnidirectional() {
        return this.unidirectional;
    }

    @Override // io.netty.handler.codec.spdy.SpdySynStreamFrame
    public SpdySynStreamFrame setUnidirectional(boolean unidirectional) {
        this.unidirectional = unidirectional;
        return this;
    }

    @Override // io.netty.handler.codec.spdy.DefaultSpdyHeadersFrame
    public String toString() {
        StringBuilder buf = new StringBuilder().append(StringUtil.simpleClassName(this)).append("(last: ").append(isLast()).append("; unidirectional: ").append(isUnidirectional()).append(')').append(StringUtil.NEWLINE).append("--> Stream-ID = ").append(streamId()).append(StringUtil.NEWLINE);
        if (this.associatedStreamId != 0) {
            buf.append("--> Associated-To-Stream-ID = ").append(associatedStreamId()).append(StringUtil.NEWLINE);
        }
        buf.append("--> Priority = ").append((int) priority()).append(StringUtil.NEWLINE).append("--> Headers:").append(StringUtil.NEWLINE);
        appendHeaders(buf);
        buf.setLength(buf.length() - StringUtil.NEWLINE.length());
        return buf.toString();
    }
}
