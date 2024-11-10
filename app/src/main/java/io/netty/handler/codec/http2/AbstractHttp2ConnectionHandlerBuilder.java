package io.netty.handler.codec.http2;

import io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder;
import io.netty.handler.codec.http2.Http2ConnectionHandler;
import io.netty.handler.codec.http2.Http2HeadersEncoder;
import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public abstract class AbstractHttp2ConnectionHandlerBuilder<T extends Http2ConnectionHandler, B extends AbstractHttp2ConnectionHandlerBuilder<T, B>> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final Http2HeadersEncoder.SensitivityDetector DEFAULT_HEADER_SENSITIVITY_DETECTOR = Http2HeadersEncoder.NEVER_SENSITIVE;
    private static final int DEFAULT_MAX_RST_FRAMES_PER_CONNECTION_FOR_SERVER = 200;
    private Http2Connection connection;
    private Http2ConnectionDecoder decoder;
    private boolean decoupleCloseAndGoAway;
    private Http2ConnectionEncoder encoder;
    private Boolean encoderEnforceMaxConcurrentStreams;
    private Boolean encoderIgnoreMaxHeaderListSize;
    private Http2FrameListener frameListener;
    private Http2FrameLogger frameLogger;
    private Http2HeadersEncoder.SensitivityDetector headerSensitivityDetector;
    private Boolean isServer;
    private Integer maxReservedStreams;
    private Integer maxRstFramesPerWindow;
    private Boolean validateHeaders;
    private Http2Settings initialSettings = Http2Settings.defaultSettings();
    private long gracefulShutdownTimeoutMillis = Http2CodecUtil.DEFAULT_GRACEFUL_SHUTDOWN_TIMEOUT_MILLIS;
    private boolean flushPreface = true;
    private Http2PromisedRequestVerifier promisedRequestVerifier = Http2PromisedRequestVerifier.ALWAYS_VERIFY;
    private boolean autoAckSettingsFrame = true;
    private boolean autoAckPingFrame = true;
    private int maxQueuedControlFrames = 10000;
    private int maxConsecutiveEmptyFrames = 2;
    private int secondsPerWindow = 30;

    protected abstract T build(Http2ConnectionDecoder http2ConnectionDecoder, Http2ConnectionEncoder http2ConnectionEncoder, Http2Settings http2Settings) throws Exception;

    /* JADX INFO: Access modifiers changed from: protected */
    public Http2Settings initialSettings() {
        return this.initialSettings;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public B initialSettings(Http2Settings settings) {
        this.initialSettings = (Http2Settings) ObjectUtil.checkNotNull(settings, "settings");
        return self();
    }

    protected Http2FrameListener frameListener() {
        return this.frameListener;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public B frameListener(Http2FrameListener frameListener) {
        this.frameListener = (Http2FrameListener) ObjectUtil.checkNotNull(frameListener, "frameListener");
        return self();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public long gracefulShutdownTimeoutMillis() {
        return this.gracefulShutdownTimeoutMillis;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public B gracefulShutdownTimeoutMillis(long gracefulShutdownTimeoutMillis) {
        if (gracefulShutdownTimeoutMillis < -1) {
            throw new IllegalArgumentException("gracefulShutdownTimeoutMillis: " + gracefulShutdownTimeoutMillis + " (expected: -1 for indefinite or >= 0)");
        }
        this.gracefulShutdownTimeoutMillis = gracefulShutdownTimeoutMillis;
        return self();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isServer() {
        if (this.isServer != null) {
            return this.isServer.booleanValue();
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public B server(boolean isServer) {
        enforceConstraint("server", "connection", this.connection);
        enforceConstraint("server", "codec", this.decoder);
        enforceConstraint("server", "codec", this.encoder);
        this.isServer = Boolean.valueOf(isServer);
        return self();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int maxReservedStreams() {
        if (this.maxReservedStreams != null) {
            return this.maxReservedStreams.intValue();
        }
        return 100;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public B maxReservedStreams(int maxReservedStreams) {
        enforceConstraint("server", "connection", this.connection);
        enforceConstraint("server", "codec", this.decoder);
        enforceConstraint("server", "codec", this.encoder);
        this.maxReservedStreams = Integer.valueOf(ObjectUtil.checkPositiveOrZero(maxReservedStreams, "maxReservedStreams"));
        return self();
    }

    protected Http2Connection connection() {
        return this.connection;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public B connection(Http2Connection connection) {
        enforceConstraint("connection", "maxReservedStreams", this.maxReservedStreams);
        enforceConstraint("connection", "server", this.isServer);
        enforceConstraint("connection", "codec", this.decoder);
        enforceConstraint("connection", "codec", this.encoder);
        this.connection = (Http2Connection) ObjectUtil.checkNotNull(connection, "connection");
        return self();
    }

    protected Http2ConnectionDecoder decoder() {
        return this.decoder;
    }

    protected Http2ConnectionEncoder encoder() {
        return this.encoder;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public B codec(Http2ConnectionDecoder decoder, Http2ConnectionEncoder encoder) {
        enforceConstraint("codec", "server", this.isServer);
        enforceConstraint("codec", "maxReservedStreams", this.maxReservedStreams);
        enforceConstraint("codec", "connection", this.connection);
        enforceConstraint("codec", "frameLogger", this.frameLogger);
        enforceConstraint("codec", "validateHeaders", this.validateHeaders);
        enforceConstraint("codec", "headerSensitivityDetector", this.headerSensitivityDetector);
        enforceConstraint("codec", "encoderEnforceMaxConcurrentStreams", this.encoderEnforceMaxConcurrentStreams);
        ObjectUtil.checkNotNull(decoder, "decoder");
        ObjectUtil.checkNotNull(encoder, "encoder");
        if (decoder.connection() != encoder.connection()) {
            throw new IllegalArgumentException("The specified encoder and decoder have different connections.");
        }
        this.decoder = decoder;
        this.encoder = encoder;
        return self();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isValidateHeaders() {
        if (this.validateHeaders != null) {
            return this.validateHeaders.booleanValue();
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public B validateHeaders(boolean validateHeaders) {
        enforceNonCodecConstraints("validateHeaders");
        this.validateHeaders = Boolean.valueOf(validateHeaders);
        return self();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Http2FrameLogger frameLogger() {
        return this.frameLogger;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public B frameLogger(Http2FrameLogger frameLogger) {
        enforceNonCodecConstraints("frameLogger");
        this.frameLogger = (Http2FrameLogger) ObjectUtil.checkNotNull(frameLogger, "frameLogger");
        return self();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean encoderEnforceMaxConcurrentStreams() {
        if (this.encoderEnforceMaxConcurrentStreams != null) {
            return this.encoderEnforceMaxConcurrentStreams.booleanValue();
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public B encoderEnforceMaxConcurrentStreams(boolean encoderEnforceMaxConcurrentStreams) {
        enforceNonCodecConstraints("encoderEnforceMaxConcurrentStreams");
        this.encoderEnforceMaxConcurrentStreams = Boolean.valueOf(encoderEnforceMaxConcurrentStreams);
        return self();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int encoderEnforceMaxQueuedControlFrames() {
        return this.maxQueuedControlFrames;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public B encoderEnforceMaxQueuedControlFrames(int maxQueuedControlFrames) {
        enforceNonCodecConstraints("encoderEnforceMaxQueuedControlFrames");
        this.maxQueuedControlFrames = ObjectUtil.checkPositiveOrZero(maxQueuedControlFrames, "maxQueuedControlFrames");
        return self();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Http2HeadersEncoder.SensitivityDetector headerSensitivityDetector() {
        return this.headerSensitivityDetector != null ? this.headerSensitivityDetector : DEFAULT_HEADER_SENSITIVITY_DETECTOR;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public B headerSensitivityDetector(Http2HeadersEncoder.SensitivityDetector headerSensitivityDetector) {
        enforceNonCodecConstraints("headerSensitivityDetector");
        this.headerSensitivityDetector = (Http2HeadersEncoder.SensitivityDetector) ObjectUtil.checkNotNull(headerSensitivityDetector, "headerSensitivityDetector");
        return self();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public B encoderIgnoreMaxHeaderListSize(boolean ignoreMaxHeaderListSize) {
        enforceNonCodecConstraints("encoderIgnoreMaxHeaderListSize");
        this.encoderIgnoreMaxHeaderListSize = Boolean.valueOf(ignoreMaxHeaderListSize);
        return self();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Deprecated
    public B initialHuffmanDecodeCapacity(int initialHuffmanDecodeCapacity) {
        return self();
    }

    protected B promisedRequestVerifier(Http2PromisedRequestVerifier promisedRequestVerifier) {
        enforceNonCodecConstraints("promisedRequestVerifier");
        this.promisedRequestVerifier = (Http2PromisedRequestVerifier) ObjectUtil.checkNotNull(promisedRequestVerifier, "promisedRequestVerifier");
        return self();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Http2PromisedRequestVerifier promisedRequestVerifier() {
        return this.promisedRequestVerifier;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int decoderEnforceMaxConsecutiveEmptyDataFrames() {
        return this.maxConsecutiveEmptyFrames;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public B decoderEnforceMaxConsecutiveEmptyDataFrames(int maxConsecutiveEmptyFrames) {
        enforceNonCodecConstraints("maxConsecutiveEmptyFrames");
        this.maxConsecutiveEmptyFrames = ObjectUtil.checkPositiveOrZero(maxConsecutiveEmptyFrames, "maxConsecutiveEmptyFrames");
        return self();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public B decoderEnforceMaxRstFramesPerWindow(int maxRstFramesPerWindow, int secondsPerWindow) {
        enforceNonCodecConstraints("decoderEnforceMaxRstFramesPerWindow");
        this.maxRstFramesPerWindow = Integer.valueOf(ObjectUtil.checkPositiveOrZero(maxRstFramesPerWindow, "maxRstFramesPerWindow"));
        this.secondsPerWindow = ObjectUtil.checkPositiveOrZero(secondsPerWindow, "secondsPerWindow");
        return self();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public B autoAckSettingsFrame(boolean autoAckSettings) {
        enforceNonCodecConstraints("autoAckSettingsFrame");
        this.autoAckSettingsFrame = autoAckSettings;
        return self();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isAutoAckSettingsFrame() {
        return this.autoAckSettingsFrame;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public B autoAckPingFrame(boolean autoAckPingFrame) {
        enforceNonCodecConstraints("autoAckPingFrame");
        this.autoAckPingFrame = autoAckPingFrame;
        return self();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isAutoAckPingFrame() {
        return this.autoAckPingFrame;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public B decoupleCloseAndGoAway(boolean decoupleCloseAndGoAway) {
        this.decoupleCloseAndGoAway = decoupleCloseAndGoAway;
        return self();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean decoupleCloseAndGoAway() {
        return this.decoupleCloseAndGoAway;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public B flushPreface(boolean flushPreface) {
        this.flushPreface = flushPreface;
        return self();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean flushPreface() {
        return this.flushPreface;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public T build() {
        if (this.encoder != null) {
            if (this.decoder == null) {
                throw new AssertionError();
            }
            return buildFromCodec(this.decoder, this.encoder);
        }
        Http2Connection connection = this.connection;
        if (connection == null) {
            connection = new DefaultHttp2Connection(isServer(), maxReservedStreams());
        }
        return buildFromConnection(connection);
    }

    private T buildFromConnection(Http2Connection connection) {
        Http2FrameWriter writer;
        Http2ConnectionEncoder encoder;
        Long maxHeaderListSize = this.initialSettings.maxHeaderListSize();
        Http2FrameReader reader = new DefaultHttp2FrameReader(new DefaultHttp2HeadersDecoder(isValidateHeaders(), maxHeaderListSize == null ? Http2CodecUtil.DEFAULT_HEADER_LIST_SIZE : maxHeaderListSize.longValue(), -1));
        if (this.encoderIgnoreMaxHeaderListSize == null) {
            writer = new DefaultHttp2FrameWriter(headerSensitivityDetector());
        } else {
            writer = new DefaultHttp2FrameWriter(headerSensitivityDetector(), this.encoderIgnoreMaxHeaderListSize.booleanValue());
        }
        if (this.frameLogger != null) {
            reader = new Http2InboundFrameLogger(reader, this.frameLogger);
            writer = new Http2OutboundFrameLogger(writer, this.frameLogger);
        }
        Http2ConnectionEncoder encoder2 = new DefaultHttp2ConnectionEncoder(connection, writer);
        boolean encoderEnforceMaxConcurrentStreams = encoderEnforceMaxConcurrentStreams();
        if (this.maxQueuedControlFrames != 0) {
            encoder2 = new Http2ControlFrameLimitEncoder(encoder2, this.maxQueuedControlFrames);
        }
        if (!encoderEnforceMaxConcurrentStreams) {
            encoder = encoder2;
        } else {
            if (connection.isServer()) {
                encoder2.close();
                reader.close();
                throw new IllegalArgumentException("encoderEnforceMaxConcurrentStreams: " + encoderEnforceMaxConcurrentStreams + " not supported for server");
            }
            encoder = new StreamBufferingEncoder(encoder2);
        }
        DefaultHttp2ConnectionDecoder decoder = new DefaultHttp2ConnectionDecoder(connection, encoder, reader, promisedRequestVerifier(), isAutoAckSettingsFrame(), isAutoAckPingFrame(), isValidateHeaders());
        return buildFromCodec(decoder, encoder);
    }

    private T buildFromCodec(Http2ConnectionDecoder decoder, Http2ConnectionEncoder encoder) {
        int maxRstFrames;
        int maxConsecutiveEmptyDataFrames = decoderEnforceMaxConsecutiveEmptyDataFrames();
        if (maxConsecutiveEmptyDataFrames > 0) {
            decoder = new Http2EmptyDataFrameConnectionDecoder(decoder, maxConsecutiveEmptyDataFrames);
        }
        if (this.maxRstFramesPerWindow == null) {
            if (isServer()) {
                maxRstFrames = 200;
            } else {
                maxRstFrames = 0;
            }
        } else {
            maxRstFrames = this.maxRstFramesPerWindow.intValue();
        }
        if (maxRstFrames > 0 && this.secondsPerWindow > 0) {
            decoder = new Http2MaxRstFrameDecoder(decoder, maxRstFrames, this.secondsPerWindow);
        }
        try {
            T handler = build(decoder, encoder, this.initialSettings);
            handler.gracefulShutdownTimeoutMillis(this.gracefulShutdownTimeoutMillis);
            if (handler.decoder().frameListener() == null) {
                handler.decoder().frameListener(this.frameListener);
            }
            return handler;
        } catch (Throwable t) {
            encoder.close();
            decoder.close();
            throw new IllegalStateException("failed to build an Http2ConnectionHandler", t);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final B self() {
        return this;
    }

    private void enforceNonCodecConstraints(String rejected) {
        enforceConstraint(rejected, "server/connection", this.decoder);
        enforceConstraint(rejected, "server/connection", this.encoder);
    }

    private static void enforceConstraint(String methodName, String rejectorName, Object value) {
        if (value != null) {
            throw new IllegalStateException(methodName + "() cannot be called because " + rejectorName + "() has been called already.");
        }
    }
}
