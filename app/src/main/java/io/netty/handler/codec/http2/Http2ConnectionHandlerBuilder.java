package io.netty.handler.codec.http2;

import io.netty.handler.codec.http2.Http2HeadersEncoder;

/* loaded from: classes4.dex */
public final class Http2ConnectionHandlerBuilder extends AbstractHttp2ConnectionHandlerBuilder<Http2ConnectionHandler, Http2ConnectionHandlerBuilder> {
    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2ConnectionHandlerBuilder validateHeaders(boolean validateHeaders) {
        return (Http2ConnectionHandlerBuilder) super.validateHeaders(validateHeaders);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2ConnectionHandlerBuilder initialSettings(Http2Settings settings) {
        return (Http2ConnectionHandlerBuilder) super.initialSettings(settings);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2Settings initialSettings() {
        return super.initialSettings();
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2ConnectionHandlerBuilder frameListener(Http2FrameListener frameListener) {
        return (Http2ConnectionHandlerBuilder) super.frameListener(frameListener);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2ConnectionHandlerBuilder gracefulShutdownTimeoutMillis(long gracefulShutdownTimeoutMillis) {
        return (Http2ConnectionHandlerBuilder) super.gracefulShutdownTimeoutMillis(gracefulShutdownTimeoutMillis);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2ConnectionHandlerBuilder server(boolean isServer) {
        return (Http2ConnectionHandlerBuilder) super.server(isServer);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2ConnectionHandlerBuilder connection(Http2Connection connection) {
        return (Http2ConnectionHandlerBuilder) super.connection(connection);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2ConnectionHandlerBuilder maxReservedStreams(int maxReservedStreams) {
        return (Http2ConnectionHandlerBuilder) super.maxReservedStreams(maxReservedStreams);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2ConnectionHandlerBuilder codec(Http2ConnectionDecoder decoder, Http2ConnectionEncoder encoder) {
        return (Http2ConnectionHandlerBuilder) super.codec(decoder, encoder);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2ConnectionHandlerBuilder frameLogger(Http2FrameLogger frameLogger) {
        return (Http2ConnectionHandlerBuilder) super.frameLogger(frameLogger);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2ConnectionHandlerBuilder encoderEnforceMaxConcurrentStreams(boolean encoderEnforceMaxConcurrentStreams) {
        return (Http2ConnectionHandlerBuilder) super.encoderEnforceMaxConcurrentStreams(encoderEnforceMaxConcurrentStreams);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2ConnectionHandlerBuilder encoderIgnoreMaxHeaderListSize(boolean encoderIgnoreMaxHeaderListSize) {
        return (Http2ConnectionHandlerBuilder) super.encoderIgnoreMaxHeaderListSize(encoderIgnoreMaxHeaderListSize);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2ConnectionHandlerBuilder headerSensitivityDetector(Http2HeadersEncoder.SensitivityDetector headerSensitivityDetector) {
        return (Http2ConnectionHandlerBuilder) super.headerSensitivityDetector(headerSensitivityDetector);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    @Deprecated
    public Http2ConnectionHandlerBuilder initialHuffmanDecodeCapacity(int initialHuffmanDecodeCapacity) {
        return (Http2ConnectionHandlerBuilder) super.initialHuffmanDecodeCapacity(initialHuffmanDecodeCapacity);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2ConnectionHandlerBuilder decoupleCloseAndGoAway(boolean decoupleCloseAndGoAway) {
        return (Http2ConnectionHandlerBuilder) super.decoupleCloseAndGoAway(decoupleCloseAndGoAway);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2ConnectionHandlerBuilder flushPreface(boolean flushPreface) {
        return (Http2ConnectionHandlerBuilder) super.flushPreface(flushPreface);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2ConnectionHandler build() {
        return super.build();
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    protected Http2ConnectionHandler build(Http2ConnectionDecoder decoder, Http2ConnectionEncoder encoder, Http2Settings initialSettings) {
        return new Http2ConnectionHandler(decoder, encoder, initialSettings, decoupleCloseAndGoAway(), flushPreface());
    }
}
