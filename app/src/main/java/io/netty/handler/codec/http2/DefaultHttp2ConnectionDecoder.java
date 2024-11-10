package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpStatusClass;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http2.Http2Connection;
import io.netty.handler.codec.http2.Http2FrameReader;
import io.netty.handler.codec.http2.Http2Headers;
import io.netty.handler.codec.http2.Http2HeadersDecoder;
import io.netty.handler.codec.http2.Http2Stream;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes4.dex */
public class DefaultHttp2ConnectionDecoder implements Http2ConnectionDecoder {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) DefaultHttp2ConnectionDecoder.class);
    private final boolean autoAckPing;
    private final Http2Connection connection;
    private final Http2Connection.PropertyKey contentLengthKey;
    private final Http2ConnectionEncoder encoder;
    private final Http2FrameReader frameReader;
    private Http2FrameListener internalFrameListener;
    private Http2LifecycleManager lifecycleManager;
    private Http2FrameListener listener;
    private final Http2PromisedRequestVerifier requestVerifier;
    private final Http2SettingsReceivedConsumer settingsReceivedConsumer;
    private final boolean validateHeaders;

    public DefaultHttp2ConnectionDecoder(Http2Connection connection, Http2ConnectionEncoder encoder, Http2FrameReader frameReader) {
        this(connection, encoder, frameReader, Http2PromisedRequestVerifier.ALWAYS_VERIFY);
    }

    public DefaultHttp2ConnectionDecoder(Http2Connection connection, Http2ConnectionEncoder encoder, Http2FrameReader frameReader, Http2PromisedRequestVerifier requestVerifier) {
        this(connection, encoder, frameReader, requestVerifier, true);
    }

    public DefaultHttp2ConnectionDecoder(Http2Connection connection, Http2ConnectionEncoder encoder, Http2FrameReader frameReader, Http2PromisedRequestVerifier requestVerifier, boolean autoAckSettings) {
        this(connection, encoder, frameReader, requestVerifier, autoAckSettings, true);
    }

    @Deprecated
    public DefaultHttp2ConnectionDecoder(Http2Connection connection, Http2ConnectionEncoder encoder, Http2FrameReader frameReader, Http2PromisedRequestVerifier requestVerifier, boolean autoAckSettings, boolean autoAckPing) {
        this(connection, encoder, frameReader, requestVerifier, autoAckSettings, true, true);
    }

    public DefaultHttp2ConnectionDecoder(Http2Connection connection, Http2ConnectionEncoder encoder, Http2FrameReader frameReader, Http2PromisedRequestVerifier requestVerifier, boolean autoAckSettings, boolean autoAckPing, boolean validateHeaders) {
        this.internalFrameListener = new PrefaceFrameListener();
        this.validateHeaders = validateHeaders;
        this.autoAckPing = autoAckPing;
        if (autoAckSettings) {
            this.settingsReceivedConsumer = null;
        } else {
            if (!(encoder instanceof Http2SettingsReceivedConsumer)) {
                throw new IllegalArgumentException("disabling autoAckSettings requires the encoder to be a " + Http2SettingsReceivedConsumer.class);
            }
            this.settingsReceivedConsumer = (Http2SettingsReceivedConsumer) encoder;
        }
        this.connection = (Http2Connection) ObjectUtil.checkNotNull(connection, "connection");
        this.contentLengthKey = this.connection.newKey();
        this.frameReader = (Http2FrameReader) ObjectUtil.checkNotNull(frameReader, "frameReader");
        this.encoder = (Http2ConnectionEncoder) ObjectUtil.checkNotNull(encoder, "encoder");
        this.requestVerifier = (Http2PromisedRequestVerifier) ObjectUtil.checkNotNull(requestVerifier, "requestVerifier");
        if (connection.local().flowController() == null) {
            connection.local().flowController(new DefaultHttp2LocalFlowController(connection));
        }
        connection.local().flowController().frameWriter(encoder.frameWriter());
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionDecoder
    public void lifecycleManager(Http2LifecycleManager lifecycleManager) {
        this.lifecycleManager = (Http2LifecycleManager) ObjectUtil.checkNotNull(lifecycleManager, "lifecycleManager");
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionDecoder
    public Http2Connection connection() {
        return this.connection;
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionDecoder
    public final Http2LocalFlowController flowController() {
        return this.connection.local().flowController();
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionDecoder
    public void frameListener(Http2FrameListener listener) {
        this.listener = (Http2FrameListener) ObjectUtil.checkNotNull(listener, "listener");
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionDecoder
    public Http2FrameListener frameListener() {
        return this.listener;
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionDecoder
    public boolean prefaceReceived() {
        return FrameReadListener.class == this.internalFrameListener.getClass();
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionDecoder
    public void decodeFrame(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Http2Exception {
        this.frameReader.readFrame(ctx, in, this.internalFrameListener);
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionDecoder
    public Http2Settings localSettings() {
        Http2Settings settings = new Http2Settings();
        Http2FrameReader.Configuration config = this.frameReader.configuration();
        Http2HeadersDecoder.Configuration headersConfig = config.headersConfiguration();
        Http2FrameSizePolicy frameSizePolicy = config.frameSizePolicy();
        settings.initialWindowSize(flowController().initialWindowSize());
        settings.maxConcurrentStreams(this.connection.remote().maxActiveStreams());
        settings.headerTableSize(headersConfig.maxHeaderTableSize());
        settings.maxFrameSize(frameSizePolicy.maxFrameSize());
        settings.maxHeaderListSize(headersConfig.maxHeaderListSize());
        if (!this.connection.isServer()) {
            settings.pushEnabled(this.connection.local().allowPushTo());
        }
        return settings;
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionDecoder, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.frameReader.close();
    }

    protected long calculateMaxHeaderListSizeGoAway(long maxHeaderListSize) {
        return Http2CodecUtil.calculateMaxHeaderListSizeGoAway(maxHeaderListSize);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int unconsumedBytes(Http2Stream stream) {
        return flowController().unconsumedBytes(stream);
    }

    void onGoAwayRead0(ChannelHandlerContext ctx, int lastStreamId, long errorCode, ByteBuf debugData) throws Http2Exception {
        this.listener.onGoAwayRead(ctx, lastStreamId, errorCode, debugData);
        this.connection.goAwayReceived(lastStreamId, errorCode, debugData);
    }

    void onUnknownFrame0(ChannelHandlerContext ctx, byte frameType, int streamId, Http2Flags flags, ByteBuf payload) throws Http2Exception {
        this.listener.onUnknownFrame(ctx, frameType, streamId, flags, payload);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void verifyContentLength(Http2Stream stream, int data, boolean isEnd) throws Http2Exception {
        ContentLength contentLength = (ContentLength) stream.getProperty(this.contentLengthKey);
        if (contentLength != null) {
            try {
                contentLength.increaseReceivedBytes(this.connection.isServer(), stream.id(), data, isEnd);
            } finally {
                if (isEnd) {
                    stream.removeProperty(this.contentLengthKey);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class FrameReadListener implements Http2FrameListener {
        private FrameReadListener() {
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public int onDataRead(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endOfStream) throws Http2Exception {
            Http2Exception error;
            Http2Stream stream = DefaultHttp2ConnectionDecoder.this.connection.stream(streamId);
            Http2LocalFlowController flowController = DefaultHttp2ConnectionDecoder.this.flowController();
            int readable = data.readableBytes();
            int bytesToReturn = readable + padding;
            try {
                boolean shouldIgnore = shouldIgnoreHeadersOrDataFrame(ctx, streamId, stream, endOfStream, "DATA");
                if (shouldIgnore) {
                    flowController.receiveFlowControlledFrame(stream, data, padding, endOfStream);
                    flowController.consumeBytes(stream, bytesToReturn);
                    verifyStreamMayHaveExisted(streamId, endOfStream, "DATA");
                    return bytesToReturn;
                }
                switch (stream.state()) {
                    case OPEN:
                    case HALF_CLOSED_LOCAL:
                        error = null;
                        break;
                    case HALF_CLOSED_REMOTE:
                    case CLOSED:
                        Http2Exception error2 = Http2Exception.streamError(stream.id(), Http2Error.STREAM_CLOSED, "Stream %d in unexpected state: %s", Integer.valueOf(stream.id()), stream.state());
                        error = error2;
                        break;
                    default:
                        Http2Exception error3 = Http2Exception.streamError(stream.id(), Http2Error.PROTOCOL_ERROR, "Stream %d in unexpected state: %s", Integer.valueOf(stream.id()), stream.state());
                        error = error3;
                        break;
                }
                int unconsumedBytes = DefaultHttp2ConnectionDecoder.this.unconsumedBytes(stream);
                try {
                    try {
                        flowController.receiveFlowControlledFrame(stream, data, padding, endOfStream);
                        int unconsumedBytes2 = DefaultHttp2ConnectionDecoder.this.unconsumedBytes(stream);
                        try {
                            if (error != null) {
                                throw error;
                            }
                            DefaultHttp2ConnectionDecoder.this.verifyContentLength(stream, readable, endOfStream);
                            int bytesToReturn2 = DefaultHttp2ConnectionDecoder.this.listener.onDataRead(ctx, streamId, data, padding, endOfStream);
                            if (endOfStream) {
                                DefaultHttp2ConnectionDecoder.this.lifecycleManager.closeStreamRemote(stream, ctx.newSucceededFuture());
                            }
                            flowController.consumeBytes(stream, bytesToReturn2);
                            return bytesToReturn2;
                        } catch (Http2Exception e) {
                            e = e;
                            unconsumedBytes = unconsumedBytes2;
                            int delta = unconsumedBytes - DefaultHttp2ConnectionDecoder.this.unconsumedBytes(stream);
                            int i = bytesToReturn - delta;
                            throw e;
                        } catch (RuntimeException e2) {
                            e = e2;
                            unconsumedBytes = unconsumedBytes2;
                            int delta2 = unconsumedBytes - DefaultHttp2ConnectionDecoder.this.unconsumedBytes(stream);
                            int i2 = bytesToReturn - delta2;
                            throw e;
                        } catch (Throwable th) {
                            e = th;
                            flowController.consumeBytes(stream, bytesToReturn);
                            throw e;
                        }
                    } catch (Http2Exception e3) {
                        e = e3;
                    } catch (RuntimeException e4) {
                        e = e4;
                    }
                } catch (Throwable th2) {
                    e = th2;
                }
            } catch (Http2Exception e5) {
                flowController.receiveFlowControlledFrame(stream, data, padding, endOfStream);
                flowController.consumeBytes(stream, bytesToReturn);
                throw e5;
            } catch (Throwable t) {
                throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, t, "Unhandled error on data stream id %d", Integer.valueOf(streamId));
            }
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endOfStream) throws Http2Exception {
            onHeadersRead(ctx, streamId, headers, 0, (short) 16, false, padding, endOfStream);
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endOfStream) throws Http2Exception {
            Http2Stream stream;
            boolean allowHalfClosedRemote;
            boolean isTrailers;
            Http2Stream stream2 = DefaultHttp2ConnectionDecoder.this.connection.stream(streamId);
            if (stream2 == null && !DefaultHttp2ConnectionDecoder.this.connection.streamMayHaveExisted(streamId)) {
                Http2Stream stream3 = DefaultHttp2ConnectionDecoder.this.connection.remote().createStream(streamId, endOfStream);
                boolean allowHalfClosedRemote2 = stream3.state() == Http2Stream.State.HALF_CLOSED_REMOTE;
                stream = stream3;
                allowHalfClosedRemote = allowHalfClosedRemote2;
                isTrailers = false;
            } else if (stream2 == null) {
                stream = stream2;
                allowHalfClosedRemote = false;
                isTrailers = false;
            } else {
                boolean isTrailers2 = stream2.isHeadersReceived();
                stream = stream2;
                allowHalfClosedRemote = false;
                isTrailers = isTrailers2;
            }
            if (!shouldIgnoreHeadersOrDataFrame(ctx, streamId, stream, endOfStream, "HEADERS")) {
                boolean isInformational = !DefaultHttp2ConnectionDecoder.this.connection.isServer() && HttpStatusClass.valueOf(headers.status()) == HttpStatusClass.INFORMATIONAL;
                if (((isInformational || !endOfStream) && stream.isHeadersReceived()) || stream.isTrailersReceived()) {
                    throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, "Stream %d received too many headers EOS: %s state: %s", Integer.valueOf(streamId), Boolean.valueOf(endOfStream), stream.state());
                }
                switch (stream.state()) {
                    case OPEN:
                    case HALF_CLOSED_LOCAL:
                        break;
                    case HALF_CLOSED_REMOTE:
                        if (!allowHalfClosedRemote) {
                            throw Http2Exception.streamError(stream.id(), Http2Error.STREAM_CLOSED, "Stream %d in unexpected state: %s", Integer.valueOf(stream.id()), stream.state());
                        }
                        break;
                    case CLOSED:
                        throw Http2Exception.streamError(stream.id(), Http2Error.STREAM_CLOSED, "Stream %d in unexpected state: %s", Integer.valueOf(stream.id()), stream.state());
                    case RESERVED_REMOTE:
                        stream.open(endOfStream);
                        break;
                    default:
                        Http2Stream stream4 = stream;
                        throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Stream %d in unexpected state: %s", Integer.valueOf(stream4.id()), stream4.state());
                }
                if (isTrailers) {
                    if (DefaultHttp2ConnectionDecoder.this.validateHeaders && headers.size() > 0) {
                        Iterator<Map.Entry<CharSequence, CharSequence>> iterator = headers.iterator();
                        while (iterator.hasNext()) {
                            CharSequence name = iterator.next().getKey();
                            if (Http2Headers.PseudoHeaderName.hasPseudoHeaderFormat(name)) {
                                throw Http2Exception.streamError(stream.id(), Http2Error.PROTOCOL_ERROR, "Found invalid Pseudo-Header in trailers: %s", name);
                            }
                        }
                    }
                } else {
                    List<? extends CharSequence> contentLength = headers.getAll(HttpHeaderNames.CONTENT_LENGTH);
                    if (contentLength != null && !contentLength.isEmpty()) {
                        try {
                            long cLength = HttpUtil.normalizeAndGetContentLength(contentLength, false, true);
                            if (cLength != -1) {
                                headers.setLong(HttpHeaderNames.CONTENT_LENGTH, cLength);
                                stream.setProperty(DefaultHttp2ConnectionDecoder.this.contentLengthKey, new ContentLength(cLength));
                            }
                        } catch (IllegalArgumentException e) {
                            throw Http2Exception.streamError(stream.id(), Http2Error.PROTOCOL_ERROR, e, "Multiple content-length headers received", new Object[0]);
                        }
                    }
                }
                stream.headersReceived(isInformational);
                DefaultHttp2ConnectionDecoder.this.verifyContentLength(stream, 0, endOfStream);
                DefaultHttp2ConnectionDecoder.this.encoder.flowController().updateDependencyTree(streamId, streamDependency, weight, exclusive);
                Http2Stream stream5 = stream;
                DefaultHttp2ConnectionDecoder.this.listener.onHeadersRead(ctx, streamId, headers, streamDependency, weight, exclusive, padding, endOfStream);
                if (endOfStream) {
                    DefaultHttp2ConnectionDecoder.this.lifecycleManager.closeStreamRemote(stream5, ctx.newSucceededFuture());
                }
            }
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public void onPriorityRead(ChannelHandlerContext ctx, int streamId, int streamDependency, short weight, boolean exclusive) throws Http2Exception {
            DefaultHttp2ConnectionDecoder.this.encoder.flowController().updateDependencyTree(streamId, streamDependency, weight, exclusive);
            DefaultHttp2ConnectionDecoder.this.listener.onPriorityRead(ctx, streamId, streamDependency, weight, exclusive);
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public void onRstStreamRead(ChannelHandlerContext ctx, int streamId, long errorCode) throws Http2Exception {
            Http2Stream stream = DefaultHttp2ConnectionDecoder.this.connection.stream(streamId);
            if (stream == null) {
                verifyStreamMayHaveExisted(streamId, false, "RST_STREAM");
                return;
            }
            switch (stream.state()) {
                case CLOSED:
                    return;
                case RESERVED_REMOTE:
                default:
                    DefaultHttp2ConnectionDecoder.this.listener.onRstStreamRead(ctx, streamId, errorCode);
                    DefaultHttp2ConnectionDecoder.this.lifecycleManager.closeStream(stream, ctx.newSucceededFuture());
                    return;
                case IDLE:
                    throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "RST_STREAM received for IDLE stream %d", Integer.valueOf(streamId));
            }
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public void onSettingsAckRead(ChannelHandlerContext ctx) throws Http2Exception {
            Http2Settings settings = DefaultHttp2ConnectionDecoder.this.encoder.pollSentSettings();
            if (settings != null) {
                applyLocalSettings(settings);
            }
            DefaultHttp2ConnectionDecoder.this.listener.onSettingsAckRead(ctx);
        }

        private void applyLocalSettings(Http2Settings settings) throws Http2Exception {
            Boolean pushEnabled = settings.pushEnabled();
            Http2FrameReader.Configuration config = DefaultHttp2ConnectionDecoder.this.frameReader.configuration();
            Http2HeadersDecoder.Configuration headerConfig = config.headersConfiguration();
            Http2FrameSizePolicy frameSizePolicy = config.frameSizePolicy();
            if (pushEnabled != null) {
                if (!DefaultHttp2ConnectionDecoder.this.connection.isServer()) {
                    DefaultHttp2ConnectionDecoder.this.connection.local().allowPushTo(pushEnabled.booleanValue());
                } else {
                    throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Server sending SETTINGS frame with ENABLE_PUSH specified", new Object[0]);
                }
            }
            Long maxConcurrentStreams = settings.maxConcurrentStreams();
            if (maxConcurrentStreams != null) {
                DefaultHttp2ConnectionDecoder.this.connection.remote().maxActiveStreams((int) Math.min(maxConcurrentStreams.longValue(), 2147483647L));
            }
            Long headerTableSize = settings.headerTableSize();
            if (headerTableSize != null) {
                headerConfig.maxHeaderTableSize(headerTableSize.longValue());
            }
            Long maxHeaderListSize = settings.maxHeaderListSize();
            if (maxHeaderListSize != null) {
                headerConfig.maxHeaderListSize(maxHeaderListSize.longValue(), DefaultHttp2ConnectionDecoder.this.calculateMaxHeaderListSizeGoAway(maxHeaderListSize.longValue()));
            }
            Integer maxFrameSize = settings.maxFrameSize();
            if (maxFrameSize != null) {
                frameSizePolicy.maxFrameSize(maxFrameSize.intValue());
            }
            Integer initialWindowSize = settings.initialWindowSize();
            if (initialWindowSize != null) {
                DefaultHttp2ConnectionDecoder.this.flowController().initialWindowSize(initialWindowSize.intValue());
            }
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public void onSettingsRead(ChannelHandlerContext ctx, Http2Settings settings) throws Http2Exception {
            if (DefaultHttp2ConnectionDecoder.this.settingsReceivedConsumer == null) {
                DefaultHttp2ConnectionDecoder.this.encoder.writeSettingsAck(ctx, ctx.newPromise());
                DefaultHttp2ConnectionDecoder.this.encoder.remoteSettings(settings);
            } else {
                DefaultHttp2ConnectionDecoder.this.settingsReceivedConsumer.consumeReceivedSettings(settings);
            }
            DefaultHttp2ConnectionDecoder.this.listener.onSettingsRead(ctx, settings);
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public void onPingRead(ChannelHandlerContext ctx, long data) throws Http2Exception {
            if (DefaultHttp2ConnectionDecoder.this.autoAckPing) {
                DefaultHttp2ConnectionDecoder.this.encoder.writePing(ctx, true, data, ctx.newPromise());
            }
            DefaultHttp2ConnectionDecoder.this.listener.onPingRead(ctx, data);
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public void onPingAckRead(ChannelHandlerContext ctx, long data) throws Http2Exception {
            DefaultHttp2ConnectionDecoder.this.listener.onPingAckRead(ctx, data);
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public void onPushPromiseRead(ChannelHandlerContext ctx, int streamId, int promisedStreamId, Http2Headers headers, int padding) throws Http2Exception {
            if (!DefaultHttp2ConnectionDecoder.this.connection().isServer()) {
                Http2Stream parentStream = DefaultHttp2ConnectionDecoder.this.connection.stream(streamId);
                if (!shouldIgnoreHeadersOrDataFrame(ctx, streamId, parentStream, false, "PUSH_PROMISE")) {
                    switch (parentStream.state()) {
                        case OPEN:
                        case HALF_CLOSED_LOCAL:
                            if (DefaultHttp2ConnectionDecoder.this.requestVerifier.isAuthoritative(ctx, headers)) {
                                if (DefaultHttp2ConnectionDecoder.this.requestVerifier.isCacheable(headers)) {
                                    if (DefaultHttp2ConnectionDecoder.this.requestVerifier.isSafe(headers)) {
                                        DefaultHttp2ConnectionDecoder.this.connection.remote().reservePushStream(promisedStreamId, parentStream);
                                        DefaultHttp2ConnectionDecoder.this.listener.onPushPromiseRead(ctx, streamId, promisedStreamId, headers, padding);
                                        return;
                                    }
                                    throw Http2Exception.streamError(promisedStreamId, Http2Error.PROTOCOL_ERROR, "Promised request on stream %d for promised stream %d is not known to be safe", Integer.valueOf(streamId), Integer.valueOf(promisedStreamId));
                                }
                                throw Http2Exception.streamError(promisedStreamId, Http2Error.PROTOCOL_ERROR, "Promised request on stream %d for promised stream %d is not known to be cacheable", Integer.valueOf(streamId), Integer.valueOf(promisedStreamId));
                            }
                            throw Http2Exception.streamError(promisedStreamId, Http2Error.PROTOCOL_ERROR, "Promised request on stream %d for promised stream %d is not authoritative", Integer.valueOf(streamId), Integer.valueOf(promisedStreamId));
                        default:
                            throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Stream %d in unexpected state for receiving push promise: %s", Integer.valueOf(parentStream.id()), parentStream.state());
                    }
                }
                return;
            }
            throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "A client cannot push.", new Object[0]);
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public void onGoAwayRead(ChannelHandlerContext ctx, int lastStreamId, long errorCode, ByteBuf debugData) throws Http2Exception {
            DefaultHttp2ConnectionDecoder.this.onGoAwayRead0(ctx, lastStreamId, errorCode, debugData);
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public void onWindowUpdateRead(ChannelHandlerContext ctx, int streamId, int windowSizeIncrement) throws Http2Exception {
            Http2Stream stream = DefaultHttp2ConnectionDecoder.this.connection.stream(streamId);
            if (stream != null && stream.state() != Http2Stream.State.CLOSED && !streamCreatedAfterGoAwaySent(streamId)) {
                DefaultHttp2ConnectionDecoder.this.encoder.flowController().incrementWindowSize(stream, windowSizeIncrement);
                DefaultHttp2ConnectionDecoder.this.listener.onWindowUpdateRead(ctx, streamId, windowSizeIncrement);
            } else {
                verifyStreamMayHaveExisted(streamId, false, "WINDOW_UPDATE");
            }
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public void onUnknownFrame(ChannelHandlerContext ctx, byte frameType, int streamId, Http2Flags flags, ByteBuf payload) throws Http2Exception {
            DefaultHttp2ConnectionDecoder.this.onUnknownFrame0(ctx, frameType, streamId, flags, payload);
        }

        private boolean shouldIgnoreHeadersOrDataFrame(ChannelHandlerContext ctx, int streamId, Http2Stream stream, boolean endOfStream, String frameName) throws Http2Exception {
            if (stream == null) {
                if (streamCreatedAfterGoAwaySent(streamId)) {
                    DefaultHttp2ConnectionDecoder.logger.info("{} ignoring {} frame for stream {}. Stream sent after GOAWAY sent", ctx.channel(), frameName, Integer.valueOf(streamId));
                    return true;
                }
                verifyStreamMayHaveExisted(streamId, endOfStream, frameName);
                throw Http2Exception.streamError(streamId, Http2Error.STREAM_CLOSED, "Received %s frame for an unknown stream %d", frameName, Integer.valueOf(streamId));
            }
            if (!stream.isResetSent() && !streamCreatedAfterGoAwaySent(streamId)) {
                return false;
            }
            if (DefaultHttp2ConnectionDecoder.logger.isInfoEnabled()) {
                InternalLogger internalLogger = DefaultHttp2ConnectionDecoder.logger;
                Object[] objArr = new Object[3];
                objArr[0] = ctx.channel();
                objArr[1] = frameName;
                objArr[2] = stream.isResetSent() ? "RST_STREAM sent." : "Stream created after GOAWAY sent. Last known stream by peer " + DefaultHttp2ConnectionDecoder.this.connection.remote().lastStreamKnownByPeer();
                internalLogger.info("{} ignoring {} frame for stream {}", objArr);
            }
            return true;
        }

        private boolean streamCreatedAfterGoAwaySent(int streamId) {
            Http2Connection.Endpoint<?> remote = DefaultHttp2ConnectionDecoder.this.connection.remote();
            return DefaultHttp2ConnectionDecoder.this.connection.goAwaySent() && remote.isValidStreamId(streamId) && streamId > remote.lastStreamKnownByPeer();
        }

        private void verifyStreamMayHaveExisted(int streamId, boolean endOfStream, String frameName) throws Http2Exception {
            if (!DefaultHttp2ConnectionDecoder.this.connection.streamMayHaveExisted(streamId)) {
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Stream %d does not exist for inbound frame %s, endOfStream = %b", Integer.valueOf(streamId), frameName, Boolean.valueOf(endOfStream));
            }
        }
    }

    /* loaded from: classes4.dex */
    private final class PrefaceFrameListener implements Http2FrameListener {
        private PrefaceFrameListener() {
        }

        private void verifyPrefaceReceived() throws Http2Exception {
            if (!DefaultHttp2ConnectionDecoder.this.prefaceReceived()) {
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Received non-SETTINGS as first frame.", new Object[0]);
            }
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public int onDataRead(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endOfStream) throws Http2Exception {
            verifyPrefaceReceived();
            return DefaultHttp2ConnectionDecoder.this.internalFrameListener.onDataRead(ctx, streamId, data, padding, endOfStream);
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endOfStream) throws Http2Exception {
            verifyPrefaceReceived();
            DefaultHttp2ConnectionDecoder.this.internalFrameListener.onHeadersRead(ctx, streamId, headers, padding, endOfStream);
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endOfStream) throws Http2Exception {
            verifyPrefaceReceived();
            DefaultHttp2ConnectionDecoder.this.internalFrameListener.onHeadersRead(ctx, streamId, headers, streamDependency, weight, exclusive, padding, endOfStream);
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public void onPriorityRead(ChannelHandlerContext ctx, int streamId, int streamDependency, short weight, boolean exclusive) throws Http2Exception {
            verifyPrefaceReceived();
            DefaultHttp2ConnectionDecoder.this.internalFrameListener.onPriorityRead(ctx, streamId, streamDependency, weight, exclusive);
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public void onRstStreamRead(ChannelHandlerContext ctx, int streamId, long errorCode) throws Http2Exception {
            verifyPrefaceReceived();
            DefaultHttp2ConnectionDecoder.this.internalFrameListener.onRstStreamRead(ctx, streamId, errorCode);
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public void onSettingsAckRead(ChannelHandlerContext ctx) throws Http2Exception {
            verifyPrefaceReceived();
            DefaultHttp2ConnectionDecoder.this.internalFrameListener.onSettingsAckRead(ctx);
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public void onSettingsRead(ChannelHandlerContext ctx, Http2Settings settings) throws Http2Exception {
            if (!DefaultHttp2ConnectionDecoder.this.prefaceReceived()) {
                DefaultHttp2ConnectionDecoder.this.internalFrameListener = new FrameReadListener();
            }
            DefaultHttp2ConnectionDecoder.this.internalFrameListener.onSettingsRead(ctx, settings);
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public void onPingRead(ChannelHandlerContext ctx, long data) throws Http2Exception {
            verifyPrefaceReceived();
            DefaultHttp2ConnectionDecoder.this.internalFrameListener.onPingRead(ctx, data);
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public void onPingAckRead(ChannelHandlerContext ctx, long data) throws Http2Exception {
            verifyPrefaceReceived();
            DefaultHttp2ConnectionDecoder.this.internalFrameListener.onPingAckRead(ctx, data);
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public void onPushPromiseRead(ChannelHandlerContext ctx, int streamId, int promisedStreamId, Http2Headers headers, int padding) throws Http2Exception {
            verifyPrefaceReceived();
            DefaultHttp2ConnectionDecoder.this.internalFrameListener.onPushPromiseRead(ctx, streamId, promisedStreamId, headers, padding);
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public void onGoAwayRead(ChannelHandlerContext ctx, int lastStreamId, long errorCode, ByteBuf debugData) throws Http2Exception {
            DefaultHttp2ConnectionDecoder.this.onGoAwayRead0(ctx, lastStreamId, errorCode, debugData);
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public void onWindowUpdateRead(ChannelHandlerContext ctx, int streamId, int windowSizeIncrement) throws Http2Exception {
            verifyPrefaceReceived();
            DefaultHttp2ConnectionDecoder.this.internalFrameListener.onWindowUpdateRead(ctx, streamId, windowSizeIncrement);
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public void onUnknownFrame(ChannelHandlerContext ctx, byte frameType, int streamId, Http2Flags flags, ByteBuf payload) throws Http2Exception {
            DefaultHttp2ConnectionDecoder.this.onUnknownFrame0(ctx, frameType, streamId, flags, payload);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class ContentLength {
        private final long expected;
        private long seen;

        ContentLength(long expected) {
            this.expected = expected;
        }

        void increaseReceivedBytes(boolean server, int streamId, int bytes, boolean isEnd) throws Http2Exception {
            this.seen += bytes;
            if (this.seen < 0) {
                throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, "Received amount of data did overflow and so not match content-length header %d", Long.valueOf(this.expected));
            }
            if (this.seen > this.expected) {
                throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, "Received amount of data %d does not match content-length header %d", Long.valueOf(this.seen), Long.valueOf(this.expected));
            }
            if (isEnd) {
                if ((this.seen != 0 || server) && this.expected > this.seen) {
                    throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, "Received amount of data %d does not match content-length header %d", Long.valueOf(this.seen), Long.valueOf(this.expected));
                }
            }
        }
    }
}
