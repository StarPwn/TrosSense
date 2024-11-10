package io.netty.handler.codec.http2;

import io.netty.handler.codec.http.HttpScheme;
import io.netty.handler.codec.http2.Http2HeadersEncoder;

/* loaded from: classes4.dex */
public final class HttpToHttp2ConnectionHandlerBuilder extends AbstractHttp2ConnectionHandlerBuilder<HttpToHttp2ConnectionHandler, HttpToHttp2ConnectionHandlerBuilder> {
    private HttpScheme httpScheme;

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public HttpToHttp2ConnectionHandlerBuilder validateHeaders(boolean validateHeaders) {
        return (HttpToHttp2ConnectionHandlerBuilder) super.validateHeaders(validateHeaders);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public HttpToHttp2ConnectionHandlerBuilder initialSettings(Http2Settings settings) {
        return (HttpToHttp2ConnectionHandlerBuilder) super.initialSettings(settings);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public HttpToHttp2ConnectionHandlerBuilder frameListener(Http2FrameListener frameListener) {
        return (HttpToHttp2ConnectionHandlerBuilder) super.frameListener(frameListener);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public HttpToHttp2ConnectionHandlerBuilder gracefulShutdownTimeoutMillis(long gracefulShutdownTimeoutMillis) {
        return (HttpToHttp2ConnectionHandlerBuilder) super.gracefulShutdownTimeoutMillis(gracefulShutdownTimeoutMillis);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public HttpToHttp2ConnectionHandlerBuilder server(boolean isServer) {
        return (HttpToHttp2ConnectionHandlerBuilder) super.server(isServer);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public HttpToHttp2ConnectionHandlerBuilder connection(Http2Connection connection) {
        return (HttpToHttp2ConnectionHandlerBuilder) super.connection(connection);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public HttpToHttp2ConnectionHandlerBuilder codec(Http2ConnectionDecoder decoder, Http2ConnectionEncoder encoder) {
        return (HttpToHttp2ConnectionHandlerBuilder) super.codec(decoder, encoder);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public HttpToHttp2ConnectionHandlerBuilder frameLogger(Http2FrameLogger frameLogger) {
        return (HttpToHttp2ConnectionHandlerBuilder) super.frameLogger(frameLogger);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public HttpToHttp2ConnectionHandlerBuilder encoderEnforceMaxConcurrentStreams(boolean encoderEnforceMaxConcurrentStreams) {
        return (HttpToHttp2ConnectionHandlerBuilder) super.encoderEnforceMaxConcurrentStreams(encoderEnforceMaxConcurrentStreams);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public HttpToHttp2ConnectionHandlerBuilder headerSensitivityDetector(Http2HeadersEncoder.SensitivityDetector headerSensitivityDetector) {
        return (HttpToHttp2ConnectionHandlerBuilder) super.headerSensitivityDetector(headerSensitivityDetector);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    @Deprecated
    public HttpToHttp2ConnectionHandlerBuilder initialHuffmanDecodeCapacity(int initialHuffmanDecodeCapacity) {
        return (HttpToHttp2ConnectionHandlerBuilder) super.initialHuffmanDecodeCapacity(initialHuffmanDecodeCapacity);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public HttpToHttp2ConnectionHandlerBuilder decoupleCloseAndGoAway(boolean decoupleCloseAndGoAway) {
        return (HttpToHttp2ConnectionHandlerBuilder) super.decoupleCloseAndGoAway(decoupleCloseAndGoAway);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public HttpToHttp2ConnectionHandlerBuilder flushPreface(boolean flushPreface) {
        return (HttpToHttp2ConnectionHandlerBuilder) super.flushPreface(flushPreface);
    }

    public HttpToHttp2ConnectionHandlerBuilder httpScheme(HttpScheme httpScheme) {
        this.httpScheme = httpScheme;
        return self();
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public HttpToHttp2ConnectionHandler build() {
        return (HttpToHttp2ConnectionHandler) super.build();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public HttpToHttp2ConnectionHandler build(Http2ConnectionDecoder decoder, Http2ConnectionEncoder encoder, Http2Settings initialSettings) {
        return new HttpToHttp2ConnectionHandler(decoder, encoder, initialSettings, isValidateHeaders(), decoupleCloseAndGoAway(), flushPreface(), this.httpScheme);
    }
}
