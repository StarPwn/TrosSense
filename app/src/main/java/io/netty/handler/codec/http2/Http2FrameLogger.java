package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.logging.LogLevel;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.logging.InternalLogLevel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/* loaded from: classes4.dex */
public class Http2FrameLogger extends ChannelHandlerAdapter {
    private static final int BUFFER_LENGTH_THRESHOLD = 64;
    private final InternalLogLevel level;
    private final InternalLogger logger;

    /* loaded from: classes4.dex */
    public enum Direction {
        INBOUND,
        OUTBOUND
    }

    public Http2FrameLogger(LogLevel level) {
        this(checkAndConvertLevel(level), InternalLoggerFactory.getInstance((Class<?>) Http2FrameLogger.class));
    }

    public Http2FrameLogger(LogLevel level, String name) {
        this(checkAndConvertLevel(level), InternalLoggerFactory.getInstance((String) ObjectUtil.checkNotNull(name, "name")));
    }

    public Http2FrameLogger(LogLevel level, Class<?> clazz) {
        this(checkAndConvertLevel(level), InternalLoggerFactory.getInstance((Class<?>) ObjectUtil.checkNotNull(clazz, "clazz")));
    }

    private Http2FrameLogger(InternalLogLevel level, InternalLogger logger) {
        this.level = level;
        this.logger = logger;
    }

    private static InternalLogLevel checkAndConvertLevel(LogLevel level) {
        return ((LogLevel) ObjectUtil.checkNotNull(level, "level")).toInternalLevel();
    }

    public boolean isEnabled() {
        return this.logger.isEnabled(this.level);
    }

    public void logData(Direction direction, ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endStream) {
        if (isEnabled()) {
            this.logger.log(this.level, "{} {} DATA: streamId={} padding={} endStream={} length={} bytes={}", ctx.channel(), direction.name(), Integer.valueOf(streamId), Integer.valueOf(padding), Boolean.valueOf(endStream), Integer.valueOf(data.readableBytes()), toString(data));
        }
    }

    public void logHeaders(Direction direction, ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endStream) {
        if (isEnabled()) {
            this.logger.log(this.level, "{} {} HEADERS: streamId={} headers={} padding={} endStream={}", ctx.channel(), direction.name(), Integer.valueOf(streamId), headers, Integer.valueOf(padding), Boolean.valueOf(endStream));
        }
    }

    public void logHeaders(Direction direction, ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endStream) {
        if (isEnabled()) {
            this.logger.log(this.level, "{} {} HEADERS: streamId={} headers={} streamDependency={} weight={} exclusive={} padding={} endStream={}", ctx.channel(), direction.name(), Integer.valueOf(streamId), headers, Integer.valueOf(streamDependency), Short.valueOf(weight), Boolean.valueOf(exclusive), Integer.valueOf(padding), Boolean.valueOf(endStream));
        }
    }

    public void logPriority(Direction direction, ChannelHandlerContext ctx, int streamId, int streamDependency, short weight, boolean exclusive) {
        if (isEnabled()) {
            this.logger.log(this.level, "{} {} PRIORITY: streamId={} streamDependency={} weight={} exclusive={}", ctx.channel(), direction.name(), Integer.valueOf(streamId), Integer.valueOf(streamDependency), Short.valueOf(weight), Boolean.valueOf(exclusive));
        }
    }

    public void logRstStream(Direction direction, ChannelHandlerContext ctx, int streamId, long errorCode) {
        if (isEnabled()) {
            this.logger.log(this.level, "{} {} RST_STREAM: streamId={} errorCode={}", ctx.channel(), direction.name(), Integer.valueOf(streamId), Long.valueOf(errorCode));
        }
    }

    public void logSettingsAck(Direction direction, ChannelHandlerContext ctx) {
        this.logger.log(this.level, "{} {} SETTINGS: ack=true", ctx.channel(), direction.name());
    }

    public void logSettings(Direction direction, ChannelHandlerContext ctx, Http2Settings settings) {
        if (isEnabled()) {
            this.logger.log(this.level, "{} {} SETTINGS: ack=false settings={}", ctx.channel(), direction.name(), settings);
        }
    }

    public void logPing(Direction direction, ChannelHandlerContext ctx, long data) {
        if (isEnabled()) {
            this.logger.log(this.level, "{} {} PING: ack=false bytes={}", ctx.channel(), direction.name(), Long.valueOf(data));
        }
    }

    public void logPingAck(Direction direction, ChannelHandlerContext ctx, long data) {
        if (isEnabled()) {
            this.logger.log(this.level, "{} {} PING: ack=true bytes={}", ctx.channel(), direction.name(), Long.valueOf(data));
        }
    }

    public void logPushPromise(Direction direction, ChannelHandlerContext ctx, int streamId, int promisedStreamId, Http2Headers headers, int padding) {
        if (isEnabled()) {
            this.logger.log(this.level, "{} {} PUSH_PROMISE: streamId={} promisedStreamId={} headers={} padding={}", ctx.channel(), direction.name(), Integer.valueOf(streamId), Integer.valueOf(promisedStreamId), headers, Integer.valueOf(padding));
        }
    }

    public void logGoAway(Direction direction, ChannelHandlerContext ctx, int lastStreamId, long errorCode, ByteBuf debugData) {
        if (isEnabled()) {
            this.logger.log(this.level, "{} {} GO_AWAY: lastStreamId={} errorCode={} length={} bytes={}", ctx.channel(), direction.name(), Integer.valueOf(lastStreamId), Long.valueOf(errorCode), Integer.valueOf(debugData.readableBytes()), toString(debugData));
        }
    }

    public void logWindowsUpdate(Direction direction, ChannelHandlerContext ctx, int streamId, int windowSizeIncrement) {
        if (isEnabled()) {
            this.logger.log(this.level, "{} {} WINDOW_UPDATE: streamId={} windowSizeIncrement={}", ctx.channel(), direction.name(), Integer.valueOf(streamId), Integer.valueOf(windowSizeIncrement));
        }
    }

    public void logUnknownFrame(Direction direction, ChannelHandlerContext ctx, byte frameType, int streamId, Http2Flags flags, ByteBuf data) {
        if (isEnabled()) {
            this.logger.log(this.level, "{} {} UNKNOWN: frameType={} streamId={} flags={} length={} bytes={}", ctx.channel(), direction.name(), Integer.valueOf(frameType & 255), Integer.valueOf(streamId), Short.valueOf(flags.value()), Integer.valueOf(data.readableBytes()), toString(data));
        }
    }

    private String toString(ByteBuf buf) {
        if (this.level == InternalLogLevel.TRACE || buf.readableBytes() <= 64) {
            return ByteBufUtil.hexDump(buf);
        }
        int length = Math.min(buf.readableBytes(), 64);
        return ByteBufUtil.hexDump(buf, buf.readerIndex(), length) + "...";
    }
}
