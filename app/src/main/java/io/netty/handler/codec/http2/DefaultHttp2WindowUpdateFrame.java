package io.netty.handler.codec.http2;

import io.netty.util.internal.StringUtil;

/* loaded from: classes4.dex */
public class DefaultHttp2WindowUpdateFrame extends AbstractHttp2StreamFrame implements Http2WindowUpdateFrame {
    private final int windowUpdateIncrement;

    public DefaultHttp2WindowUpdateFrame(int windowUpdateIncrement) {
        this.windowUpdateIncrement = windowUpdateIncrement;
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2StreamFrame, io.netty.handler.codec.http2.Http2StreamFrame
    public DefaultHttp2WindowUpdateFrame stream(Http2FrameStream stream) {
        super.stream(stream);
        return this;
    }

    @Override // io.netty.handler.codec.http2.Http2Frame
    public String name() {
        return "WINDOW_UPDATE";
    }

    @Override // io.netty.handler.codec.http2.Http2WindowUpdateFrame
    public int windowSizeIncrement() {
        return this.windowUpdateIncrement;
    }

    public String toString() {
        return StringUtil.simpleClassName(this) + "(stream=" + stream() + ", windowUpdateIncrement=" + this.windowUpdateIncrement + ')';
    }
}
