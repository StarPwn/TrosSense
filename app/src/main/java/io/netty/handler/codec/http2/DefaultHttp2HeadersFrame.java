package io.netty.handler.codec.http2;

import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;

/* loaded from: classes4.dex */
public final class DefaultHttp2HeadersFrame extends AbstractHttp2StreamFrame implements Http2HeadersFrame {
    private final boolean endStream;
    private final Http2Headers headers;
    private final int padding;

    public DefaultHttp2HeadersFrame(Http2Headers headers) {
        this(headers, false);
    }

    public DefaultHttp2HeadersFrame(Http2Headers headers, boolean endStream) {
        this(headers, endStream, 0);
    }

    public DefaultHttp2HeadersFrame(Http2Headers headers, boolean endStream, int padding) {
        this.headers = (Http2Headers) ObjectUtil.checkNotNull(headers, "headers");
        this.endStream = endStream;
        Http2CodecUtil.verifyPadding(padding);
        this.padding = padding;
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2StreamFrame, io.netty.handler.codec.http2.Http2StreamFrame
    public DefaultHttp2HeadersFrame stream(Http2FrameStream stream) {
        super.stream(stream);
        return this;
    }

    @Override // io.netty.handler.codec.http2.Http2Frame
    public String name() {
        return "HEADERS";
    }

    @Override // io.netty.handler.codec.http2.Http2HeadersFrame
    public Http2Headers headers() {
        return this.headers;
    }

    @Override // io.netty.handler.codec.http2.Http2HeadersFrame
    public boolean isEndStream() {
        return this.endStream;
    }

    @Override // io.netty.handler.codec.http2.Http2HeadersFrame
    public int padding() {
        return this.padding;
    }

    public String toString() {
        return StringUtil.simpleClassName(this) + "(stream=" + stream() + ", headers=" + this.headers + ", endStream=" + this.endStream + ", padding=" + this.padding + ')';
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2StreamFrame
    public boolean equals(Object o) {
        if (!(o instanceof DefaultHttp2HeadersFrame)) {
            return false;
        }
        DefaultHttp2HeadersFrame other = (DefaultHttp2HeadersFrame) o;
        return super.equals(other) && this.headers.equals(other.headers) && this.endStream == other.endStream && this.padding == other.padding;
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2StreamFrame
    public int hashCode() {
        return (((((super.hashCode() * 31) + this.headers.hashCode()) * 31) + (!this.endStream ? 1 : 0)) * 31) + this.padding;
    }
}
