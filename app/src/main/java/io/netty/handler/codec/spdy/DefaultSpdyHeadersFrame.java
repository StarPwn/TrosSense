package io.netty.handler.codec.spdy;

import io.netty.util.internal.StringUtil;
import java.util.Map;

/* loaded from: classes4.dex */
public class DefaultSpdyHeadersFrame extends DefaultSpdyStreamFrame implements SpdyHeadersFrame {
    private final SpdyHeaders headers;
    private boolean invalid;
    private boolean truncated;

    public DefaultSpdyHeadersFrame(int streamId) {
        this(streamId, true);
    }

    public DefaultSpdyHeadersFrame(int streamId, boolean validate) {
        super(streamId);
        this.headers = new DefaultSpdyHeaders(validate);
    }

    @Override // io.netty.handler.codec.spdy.DefaultSpdyStreamFrame, io.netty.handler.codec.spdy.SpdyStreamFrame, io.netty.handler.codec.spdy.SpdyDataFrame
    public SpdyHeadersFrame setStreamId(int streamId) {
        super.setStreamId(streamId);
        return this;
    }

    @Override // io.netty.handler.codec.spdy.DefaultSpdyStreamFrame, io.netty.handler.codec.spdy.SpdyStreamFrame, io.netty.handler.codec.spdy.SpdyDataFrame
    public SpdyHeadersFrame setLast(boolean last) {
        super.setLast(last);
        return this;
    }

    @Override // io.netty.handler.codec.spdy.SpdyHeadersFrame
    public boolean isInvalid() {
        return this.invalid;
    }

    @Override // io.netty.handler.codec.spdy.SpdyHeadersFrame
    public SpdyHeadersFrame setInvalid() {
        this.invalid = true;
        return this;
    }

    @Override // io.netty.handler.codec.spdy.SpdyHeadersFrame
    public boolean isTruncated() {
        return this.truncated;
    }

    @Override // io.netty.handler.codec.spdy.SpdyHeadersFrame
    public SpdyHeadersFrame setTruncated() {
        this.truncated = true;
        return this;
    }

    @Override // io.netty.handler.codec.spdy.SpdyHeadersFrame
    public SpdyHeaders headers() {
        return this.headers;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder().append(StringUtil.simpleClassName(this)).append("(last: ").append(isLast()).append(')').append(StringUtil.NEWLINE).append("--> Stream-ID = ").append(streamId()).append(StringUtil.NEWLINE).append("--> Headers:").append(StringUtil.NEWLINE);
        appendHeaders(buf);
        buf.setLength(buf.length() - StringUtil.NEWLINE.length());
        return buf.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void appendHeaders(StringBuilder buf) {
        for (Map.Entry<CharSequence, CharSequence> e : headers()) {
            buf.append("    ");
            buf.append(e.getKey());
            buf.append(": ");
            buf.append(e.getValue());
            buf.append(StringUtil.NEWLINE);
        }
    }
}
