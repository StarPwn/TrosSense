package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http2.Http2FrameLogger;
import io.netty.handler.codec.http2.Http2FrameReader;
import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public class Http2InboundFrameLogger implements Http2FrameReader {
    private final Http2FrameLogger logger;
    private final Http2FrameReader reader;

    public Http2InboundFrameLogger(Http2FrameReader reader, Http2FrameLogger logger) {
        this.reader = (Http2FrameReader) ObjectUtil.checkNotNull(reader, "reader");
        this.logger = (Http2FrameLogger) ObjectUtil.checkNotNull(logger, "logger");
    }

    @Override // io.netty.handler.codec.http2.Http2FrameReader
    public void readFrame(ChannelHandlerContext ctx, ByteBuf input, final Http2FrameListener listener) throws Http2Exception {
        this.reader.readFrame(ctx, input, new Http2FrameListener() { // from class: io.netty.handler.codec.http2.Http2InboundFrameLogger.1
            @Override // io.netty.handler.codec.http2.Http2FrameListener
            public int onDataRead(ChannelHandlerContext ctx2, int streamId, ByteBuf data, int padding, boolean endOfStream) throws Http2Exception {
                Http2InboundFrameLogger.this.logger.logData(Http2FrameLogger.Direction.INBOUND, ctx2, streamId, data, padding, endOfStream);
                return listener.onDataRead(ctx2, streamId, data, padding, endOfStream);
            }

            @Override // io.netty.handler.codec.http2.Http2FrameListener
            public void onHeadersRead(ChannelHandlerContext ctx2, int streamId, Http2Headers headers, int padding, boolean endStream) throws Http2Exception {
                Http2InboundFrameLogger.this.logger.logHeaders(Http2FrameLogger.Direction.INBOUND, ctx2, streamId, headers, padding, endStream);
                listener.onHeadersRead(ctx2, streamId, headers, padding, endStream);
            }

            @Override // io.netty.handler.codec.http2.Http2FrameListener
            public void onHeadersRead(ChannelHandlerContext ctx2, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endStream) throws Http2Exception {
                Http2InboundFrameLogger.this.logger.logHeaders(Http2FrameLogger.Direction.INBOUND, ctx2, streamId, headers, streamDependency, weight, exclusive, padding, endStream);
                listener.onHeadersRead(ctx2, streamId, headers, streamDependency, weight, exclusive, padding, endStream);
            }

            @Override // io.netty.handler.codec.http2.Http2FrameListener
            public void onPriorityRead(ChannelHandlerContext ctx2, int streamId, int streamDependency, short weight, boolean exclusive) throws Http2Exception {
                Http2InboundFrameLogger.this.logger.logPriority(Http2FrameLogger.Direction.INBOUND, ctx2, streamId, streamDependency, weight, exclusive);
                listener.onPriorityRead(ctx2, streamId, streamDependency, weight, exclusive);
            }

            @Override // io.netty.handler.codec.http2.Http2FrameListener
            public void onRstStreamRead(ChannelHandlerContext ctx2, int streamId, long errorCode) throws Http2Exception {
                Http2InboundFrameLogger.this.logger.logRstStream(Http2FrameLogger.Direction.INBOUND, ctx2, streamId, errorCode);
                listener.onRstStreamRead(ctx2, streamId, errorCode);
            }

            @Override // io.netty.handler.codec.http2.Http2FrameListener
            public void onSettingsAckRead(ChannelHandlerContext ctx2) throws Http2Exception {
                Http2InboundFrameLogger.this.logger.logSettingsAck(Http2FrameLogger.Direction.INBOUND, ctx2);
                listener.onSettingsAckRead(ctx2);
            }

            @Override // io.netty.handler.codec.http2.Http2FrameListener
            public void onSettingsRead(ChannelHandlerContext ctx2, Http2Settings settings) throws Http2Exception {
                Http2InboundFrameLogger.this.logger.logSettings(Http2FrameLogger.Direction.INBOUND, ctx2, settings);
                listener.onSettingsRead(ctx2, settings);
            }

            @Override // io.netty.handler.codec.http2.Http2FrameListener
            public void onPingRead(ChannelHandlerContext ctx2, long data) throws Http2Exception {
                Http2InboundFrameLogger.this.logger.logPing(Http2FrameLogger.Direction.INBOUND, ctx2, data);
                listener.onPingRead(ctx2, data);
            }

            @Override // io.netty.handler.codec.http2.Http2FrameListener
            public void onPingAckRead(ChannelHandlerContext ctx2, long data) throws Http2Exception {
                Http2InboundFrameLogger.this.logger.logPingAck(Http2FrameLogger.Direction.INBOUND, ctx2, data);
                listener.onPingAckRead(ctx2, data);
            }

            @Override // io.netty.handler.codec.http2.Http2FrameListener
            public void onPushPromiseRead(ChannelHandlerContext ctx2, int streamId, int promisedStreamId, Http2Headers headers, int padding) throws Http2Exception {
                Http2InboundFrameLogger.this.logger.logPushPromise(Http2FrameLogger.Direction.INBOUND, ctx2, streamId, promisedStreamId, headers, padding);
                listener.onPushPromiseRead(ctx2, streamId, promisedStreamId, headers, padding);
            }

            @Override // io.netty.handler.codec.http2.Http2FrameListener
            public void onGoAwayRead(ChannelHandlerContext ctx2, int lastStreamId, long errorCode, ByteBuf debugData) throws Http2Exception {
                Http2InboundFrameLogger.this.logger.logGoAway(Http2FrameLogger.Direction.INBOUND, ctx2, lastStreamId, errorCode, debugData);
                listener.onGoAwayRead(ctx2, lastStreamId, errorCode, debugData);
            }

            @Override // io.netty.handler.codec.http2.Http2FrameListener
            public void onWindowUpdateRead(ChannelHandlerContext ctx2, int streamId, int windowSizeIncrement) throws Http2Exception {
                Http2InboundFrameLogger.this.logger.logWindowsUpdate(Http2FrameLogger.Direction.INBOUND, ctx2, streamId, windowSizeIncrement);
                listener.onWindowUpdateRead(ctx2, streamId, windowSizeIncrement);
            }

            @Override // io.netty.handler.codec.http2.Http2FrameListener
            public void onUnknownFrame(ChannelHandlerContext ctx2, byte frameType, int streamId, Http2Flags flags, ByteBuf payload) throws Http2Exception {
                Http2InboundFrameLogger.this.logger.logUnknownFrame(Http2FrameLogger.Direction.INBOUND, ctx2, frameType, streamId, flags, payload);
                listener.onUnknownFrame(ctx2, frameType, streamId, flags, payload);
            }
        });
    }

    @Override // io.netty.handler.codec.http2.Http2FrameReader, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.reader.close();
    }

    @Override // io.netty.handler.codec.http2.Http2FrameReader
    public Http2FrameReader.Configuration configuration() {
        return this.reader.configuration();
    }
}
