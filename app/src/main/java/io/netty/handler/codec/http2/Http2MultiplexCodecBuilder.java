package io.netty.handler.codec.http2;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.handler.codec.http2.Http2HeadersEncoder;
import io.netty.util.internal.ObjectUtil;

@Deprecated
/* loaded from: classes4.dex */
public class Http2MultiplexCodecBuilder extends AbstractHttp2ConnectionHandlerBuilder<Http2MultiplexCodec, Http2MultiplexCodecBuilder> {
    final ChannelHandler childHandler;
    private Http2FrameWriter frameWriter;
    private ChannelHandler upgradeStreamHandler;

    Http2MultiplexCodecBuilder(boolean server, ChannelHandler childHandler) {
        server(server);
        this.childHandler = checkSharable((ChannelHandler) ObjectUtil.checkNotNull(childHandler, "childHandler"));
        gracefulShutdownTimeoutMillis(0L);
    }

    private static ChannelHandler checkSharable(ChannelHandler handler) {
        if ((handler instanceof ChannelHandlerAdapter) && !((ChannelHandlerAdapter) handler).isSharable() && !handler.getClass().isAnnotationPresent(ChannelHandler.Sharable.class)) {
            throw new IllegalArgumentException("The handler must be Sharable");
        }
        return handler;
    }

    Http2MultiplexCodecBuilder frameWriter(Http2FrameWriter frameWriter) {
        this.frameWriter = (Http2FrameWriter) ObjectUtil.checkNotNull(frameWriter, "frameWriter");
        return this;
    }

    public static Http2MultiplexCodecBuilder forClient(ChannelHandler childHandler) {
        return new Http2MultiplexCodecBuilder(false, childHandler);
    }

    public static Http2MultiplexCodecBuilder forServer(ChannelHandler childHandler) {
        return new Http2MultiplexCodecBuilder(true, childHandler);
    }

    public Http2MultiplexCodecBuilder withUpgradeStreamHandler(ChannelHandler upgradeStreamHandler) {
        if (isServer()) {
            throw new IllegalArgumentException("Server codecs don't use an extra handler for the upgrade stream");
        }
        this.upgradeStreamHandler = upgradeStreamHandler;
        return this;
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2Settings initialSettings() {
        return super.initialSettings();
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2MultiplexCodecBuilder initialSettings(Http2Settings settings) {
        return (Http2MultiplexCodecBuilder) super.initialSettings(settings);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public long gracefulShutdownTimeoutMillis() {
        return super.gracefulShutdownTimeoutMillis();
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2MultiplexCodecBuilder gracefulShutdownTimeoutMillis(long gracefulShutdownTimeoutMillis) {
        return (Http2MultiplexCodecBuilder) super.gracefulShutdownTimeoutMillis(gracefulShutdownTimeoutMillis);
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
    public Http2MultiplexCodecBuilder maxReservedStreams(int maxReservedStreams) {
        return (Http2MultiplexCodecBuilder) super.maxReservedStreams(maxReservedStreams);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public boolean isValidateHeaders() {
        return super.isValidateHeaders();
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2MultiplexCodecBuilder validateHeaders(boolean validateHeaders) {
        return (Http2MultiplexCodecBuilder) super.validateHeaders(validateHeaders);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2FrameLogger frameLogger() {
        return super.frameLogger();
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2MultiplexCodecBuilder frameLogger(Http2FrameLogger frameLogger) {
        return (Http2MultiplexCodecBuilder) super.frameLogger(frameLogger);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public boolean encoderEnforceMaxConcurrentStreams() {
        return super.encoderEnforceMaxConcurrentStreams();
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2MultiplexCodecBuilder encoderEnforceMaxConcurrentStreams(boolean encoderEnforceMaxConcurrentStreams) {
        return (Http2MultiplexCodecBuilder) super.encoderEnforceMaxConcurrentStreams(encoderEnforceMaxConcurrentStreams);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public int encoderEnforceMaxQueuedControlFrames() {
        return super.encoderEnforceMaxQueuedControlFrames();
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2MultiplexCodecBuilder encoderEnforceMaxQueuedControlFrames(int maxQueuedControlFrames) {
        return (Http2MultiplexCodecBuilder) super.encoderEnforceMaxQueuedControlFrames(maxQueuedControlFrames);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2HeadersEncoder.SensitivityDetector headerSensitivityDetector() {
        return super.headerSensitivityDetector();
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2MultiplexCodecBuilder headerSensitivityDetector(Http2HeadersEncoder.SensitivityDetector headerSensitivityDetector) {
        return (Http2MultiplexCodecBuilder) super.headerSensitivityDetector(headerSensitivityDetector);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2MultiplexCodecBuilder encoderIgnoreMaxHeaderListSize(boolean ignoreMaxHeaderListSize) {
        return (Http2MultiplexCodecBuilder) super.encoderIgnoreMaxHeaderListSize(ignoreMaxHeaderListSize);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    @Deprecated
    public Http2MultiplexCodecBuilder initialHuffmanDecodeCapacity(int initialHuffmanDecodeCapacity) {
        return (Http2MultiplexCodecBuilder) super.initialHuffmanDecodeCapacity(initialHuffmanDecodeCapacity);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2MultiplexCodecBuilder autoAckSettingsFrame(boolean autoAckSettings) {
        return (Http2MultiplexCodecBuilder) super.autoAckSettingsFrame(autoAckSettings);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2MultiplexCodecBuilder autoAckPingFrame(boolean autoAckPingFrame) {
        return (Http2MultiplexCodecBuilder) super.autoAckPingFrame(autoAckPingFrame);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2MultiplexCodecBuilder decoupleCloseAndGoAway(boolean decoupleCloseAndGoAway) {
        return (Http2MultiplexCodecBuilder) super.decoupleCloseAndGoAway(decoupleCloseAndGoAway);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2MultiplexCodecBuilder flushPreface(boolean flushPreface) {
        return (Http2MultiplexCodecBuilder) super.flushPreface(flushPreface);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public int decoderEnforceMaxConsecutiveEmptyDataFrames() {
        return super.decoderEnforceMaxConsecutiveEmptyDataFrames();
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2MultiplexCodecBuilder decoderEnforceMaxConsecutiveEmptyDataFrames(int maxConsecutiveEmptyFrames) {
        return (Http2MultiplexCodecBuilder) super.decoderEnforceMaxConsecutiveEmptyDataFrames(maxConsecutiveEmptyFrames);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2MultiplexCodecBuilder decoderEnforceMaxRstFramesPerWindow(int maxRstFramesPerWindow, int secondsPerWindow) {
        return (Http2MultiplexCodecBuilder) super.decoderEnforceMaxRstFramesPerWindow(maxRstFramesPerWindow, secondsPerWindow);
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2MultiplexCodec build() {
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
        return (Http2MultiplexCodec) super.build();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    public Http2MultiplexCodec build(Http2ConnectionDecoder decoder, Http2ConnectionEncoder encoder, Http2Settings initialSettings) {
        Http2MultiplexCodec codec = new Http2MultiplexCodec(encoder, decoder, initialSettings, this.childHandler, this.upgradeStreamHandler, decoupleCloseAndGoAway(), flushPreface());
        codec.gracefulShutdownTimeoutMillis(gracefulShutdownTimeoutMillis());
        return codec;
    }
}
