package io.netty.handler.codec.http2;

import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
final class Http2EmptyDataFrameConnectionDecoder extends DecoratingHttp2ConnectionDecoder {
    private final int maxConsecutiveEmptyFrames;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Http2EmptyDataFrameConnectionDecoder(Http2ConnectionDecoder delegate, int maxConsecutiveEmptyFrames) {
        super(delegate);
        this.maxConsecutiveEmptyFrames = ObjectUtil.checkPositive(maxConsecutiveEmptyFrames, "maxConsecutiveEmptyFrames");
    }

    @Override // io.netty.handler.codec.http2.DecoratingHttp2ConnectionDecoder, io.netty.handler.codec.http2.Http2ConnectionDecoder
    public void frameListener(Http2FrameListener listener) {
        if (listener != null) {
            super.frameListener(new Http2EmptyDataFrameListener(listener, this.maxConsecutiveEmptyFrames));
        } else {
            super.frameListener(null);
        }
    }

    @Override // io.netty.handler.codec.http2.DecoratingHttp2ConnectionDecoder, io.netty.handler.codec.http2.Http2ConnectionDecoder
    public Http2FrameListener frameListener() {
        Http2FrameListener frameListener = frameListener0();
        if (frameListener instanceof Http2EmptyDataFrameListener) {
            return ((Http2EmptyDataFrameListener) frameListener).listener;
        }
        return frameListener;
    }

    Http2FrameListener frameListener0() {
        return super.frameListener();
    }
}
