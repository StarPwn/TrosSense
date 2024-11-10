package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ObjectUtil;
import java.util.List;

/* loaded from: classes4.dex */
public class DecoratingHttp2ConnectionDecoder implements Http2ConnectionDecoder {
    private final Http2ConnectionDecoder delegate;

    public DecoratingHttp2ConnectionDecoder(Http2ConnectionDecoder delegate) {
        this.delegate = (Http2ConnectionDecoder) ObjectUtil.checkNotNull(delegate, "delegate");
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionDecoder
    public void lifecycleManager(Http2LifecycleManager lifecycleManager) {
        this.delegate.lifecycleManager(lifecycleManager);
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionDecoder
    public Http2Connection connection() {
        return this.delegate.connection();
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionDecoder
    public Http2LocalFlowController flowController() {
        return this.delegate.flowController();
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionDecoder
    public void frameListener(Http2FrameListener listener) {
        this.delegate.frameListener(listener);
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionDecoder
    public Http2FrameListener frameListener() {
        return this.delegate.frameListener();
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionDecoder
    public void decodeFrame(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Http2Exception {
        this.delegate.decodeFrame(ctx, in, out);
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionDecoder
    public Http2Settings localSettings() {
        return this.delegate.localSettings();
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionDecoder
    public boolean prefaceReceived() {
        return this.delegate.prefaceReceived();
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionDecoder, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.delegate.close();
    }
}
