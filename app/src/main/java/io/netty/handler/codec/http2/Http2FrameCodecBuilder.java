package io.netty.handler.codec.http2;

import io.netty.handler.codec.http2.Http2HeadersEncoder;
import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public class Http2FrameCodecBuilder extends AbstractHttp2ConnectionHandlerBuilder<Http2FrameCodec, Http2FrameCodecBuilder> {
    private Http2FrameWriter frameWriter;

    protected Http2FrameCodecBuilder() {
    }

    Http2FrameCodecBuilder(boolean server) {
        server(server);
        gracefulShutdownTimeoutMillis(0L);
    }

    public static Http2FrameCodecBuilder forClient() {
        return new Http2FrameCodecBuilder(false);
    }

    public static Http2FrameCodecBuilder forServer() {
        return new Http2FrameCodecBuilder(true);
    }

    Http2FrameCodecBuilder frameWriter(Http2FrameWriter frameWriter) {
        this.frameWriter = (Http2FrameWriter) ObjectUtil.checkNotNull(frameWriter, "frameWriter");
        return this;
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2Settings initialSettings() {
        return super.initialSettings();
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2FrameCodecBuilder initialSettings(Http2Settings settings) {
        return (Http2FrameCodecBuilder) super.initialSettings(settings);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public long gracefulShutdownTimeoutMillis() {
        return super.gracefulShutdownTimeoutMillis();
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2FrameCodecBuilder gracefulShutdownTimeoutMillis(long gracefulShutdownTimeoutMillis) {
        return (Http2FrameCodecBuilder) super.gracefulShutdownTimeoutMillis(gracefulShutdownTimeoutMillis);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public boolean isServer() {
        return super.isServer();
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public int maxReservedStreams() {
        return super.maxReservedStreams();
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2FrameCodecBuilder maxReservedStreams(int maxReservedStreams) {
        return (Http2FrameCodecBuilder) super.maxReservedStreams(maxReservedStreams);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public boolean isValidateHeaders() {
        return super.isValidateHeaders();
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2FrameCodecBuilder validateHeaders(boolean validateHeaders) {
        return (Http2FrameCodecBuilder) super.validateHeaders(validateHeaders);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2FrameLogger frameLogger() {
        return super.frameLogger();
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2FrameCodecBuilder frameLogger(Http2FrameLogger frameLogger) {
        return (Http2FrameCodecBuilder) super.frameLogger(frameLogger);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public boolean encoderEnforceMaxConcurrentStreams() {
        return super.encoderEnforceMaxConcurrentStreams();
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2FrameCodecBuilder encoderEnforceMaxConcurrentStreams(boolean encoderEnforceMaxConcurrentStreams) {
        return (Http2FrameCodecBuilder) super.encoderEnforceMaxConcurrentStreams(encoderEnforceMaxConcurrentStreams);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public int encoderEnforceMaxQueuedControlFrames() {
        return super.encoderEnforceMaxQueuedControlFrames();
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2FrameCodecBuilder encoderEnforceMaxQueuedControlFrames(int maxQueuedControlFrames) {
        return (Http2FrameCodecBuilder) super.encoderEnforceMaxQueuedControlFrames(maxQueuedControlFrames);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2HeadersEncoder.SensitivityDetector headerSensitivityDetector() {
        return super.headerSensitivityDetector();
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2FrameCodecBuilder headerSensitivityDetector(Http2HeadersEncoder.SensitivityDetector headerSensitivityDetector) {
        return (Http2FrameCodecBuilder) super.headerSensitivityDetector(headerSensitivityDetector);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2FrameCodecBuilder encoderIgnoreMaxHeaderListSize(boolean ignoreMaxHeaderListSize) {
        return (Http2FrameCodecBuilder) super.encoderIgnoreMaxHeaderListSize(ignoreMaxHeaderListSize);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    @Deprecated
    public Http2FrameCodecBuilder initialHuffmanDecodeCapacity(int initialHuffmanDecodeCapacity) {
        return (Http2FrameCodecBuilder) super.initialHuffmanDecodeCapacity(initialHuffmanDecodeCapacity);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2FrameCodecBuilder autoAckSettingsFrame(boolean autoAckSettings) {
        return (Http2FrameCodecBuilder) super.autoAckSettingsFrame(autoAckSettings);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2FrameCodecBuilder autoAckPingFrame(boolean autoAckPingFrame) {
        return (Http2FrameCodecBuilder) super.autoAckPingFrame(autoAckPingFrame);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2FrameCodecBuilder decoupleCloseAndGoAway(boolean decoupleCloseAndGoAway) {
        return (Http2FrameCodecBuilder) super.decoupleCloseAndGoAway(decoupleCloseAndGoAway);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2FrameCodecBuilder flushPreface(boolean flushPreface) {
        return (Http2FrameCodecBuilder) super.flushPreface(flushPreface);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public int decoderEnforceMaxConsecutiveEmptyDataFrames() {
        return super.decoderEnforceMaxConsecutiveEmptyDataFrames();
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2FrameCodecBuilder decoderEnforceMaxConsecutiveEmptyDataFrames(int maxConsecutiveEmptyFrames) {
        return (Http2FrameCodecBuilder) super.decoderEnforceMaxConsecutiveEmptyDataFrames(maxConsecutiveEmptyFrames);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2FrameCodecBuilder decoderEnforceMaxRstFramesPerWindow(int maxRstFramesPerWindow, int secondsPerWindow) {
        return (Http2FrameCodecBuilder) super.decoderEnforceMaxRstFramesPerWindow(maxRstFramesPerWindow, secondsPerWindow);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2FrameCodec build() {
        DefaultHttp2HeadersDecoder defaultHttp2HeadersDecoder;
        Http2ConnectionEncoder encoder;
        Http2FrameWriter frameWriter = this.frameWriter;
        if (frameWriter != null) {
            DefaultHttp2Connection connection = new DefaultHttp2Connection(isServer(), maxReservedStreams());
            Long maxHeaderListSize = initialSettings().maxHeaderListSize();
            if (maxHeaderListSize == null) {
                defaultHttp2HeadersDecoder = new DefaultHttp2HeadersDecoder(isValidateHeaders());
            } else {
                defaultHttp2HeadersDecoder = new DefaultHttp2HeadersDecoder(isValidateHeaders(), maxHeaderListSize.longValue());
            }
            Http2FrameReader frameReader = new DefaultHttp2FrameReader(defaultHttp2HeadersDecoder);
            if (frameLogger() != null) {
                frameWriter = new Http2OutboundFrameLogger(frameWriter, frameLogger());
                frameReader = new Http2InboundFrameLogger(frameReader, frameLogger());
            }
            Http2ConnectionEncoder encoder2 = new DefaultHttp2ConnectionEncoder(connection, frameWriter);
            if (!encoderEnforceMaxConcurrentStreams()) {
                encoder = encoder2;
            } else {
                encoder = new StreamBufferingEncoder(encoder2);
            }
            Http2ConnectionDecoder decoder = new DefaultHttp2ConnectionDecoder(connection, encoder, frameReader, promisedRequestVerifier(), isAutoAckSettingsFrame(), isAutoAckPingFrame(), isValidateHeaders());
            int maxConsecutiveEmptyDataFrames = decoderEnforceMaxConsecutiveEmptyDataFrames();
            if (maxConsecutiveEmptyDataFrames > 0) {
                decoder = new Http2EmptyDataFrameConnectionDecoder(decoder, maxConsecutiveEmptyDataFrames);
            }
            return build(decoder, encoder, initialSettings());
        }
        return (Http2FrameCodec) super.build();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2FrameCodec build(Http2ConnectionDecoder decoder, Http2ConnectionEncoder encoder, Http2Settings initialSettings) {
        Http2FrameCodec codec = new Http2FrameCodec(encoder, decoder, initialSettings, decoupleCloseAndGoAway(), flushPreface());
        codec.gracefulShutdownTimeoutMillis(gracefulShutdownTimeoutMillis());
        return codec;
    }
}
