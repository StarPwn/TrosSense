package io.netty.handler.codec.http2;

import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
final class Http2MaxRstFrameDecoder extends DecoratingHttp2ConnectionDecoder {
    private final int maxRstFramesPerWindow;
    private final int secondsPerWindow;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Http2MaxRstFrameDecoder(Http2ConnectionDecoder delegate, int maxRstFramesPerWindow, int secondsPerWindow) {
        super(delegate);
        this.maxRstFramesPerWindow = ObjectUtil.checkPositive(maxRstFramesPerWindow, "maxRstFramesPerWindow");
        this.secondsPerWindow = ObjectUtil.checkPositive(secondsPerWindow, "secondsPerWindow");
    }

    @Override // io.netty.handler.codec.http2.DecoratingHttp2ConnectionDecoder, io.netty.handler.codec.http2.Http2ConnectionDecoder
    public void frameListener(Http2FrameListener listener) {
        if (listener != null) {
            super.frameListener(new Http2MaxRstFrameListener(listener, this.maxRstFramesPerWindow, this.secondsPerWindow));
        } else {
            super.frameListener(null);
        }
    }

    @Override // io.netty.handler.codec.http2.DecoratingHttp2ConnectionDecoder, io.netty.handler.codec.http2.Http2ConnectionDecoder
    public Http2FrameListener frameListener() {
        Http2FrameListener frameListener = frameListener0();
        if (frameListener instanceof Http2MaxRstFrameListener) {
            return ((Http2MaxRstFrameListener) frameListener).listener;
        }
        return frameListener;
    }

    Http2FrameListener frameListener0() {
        return super.frameListener();
    }
}
