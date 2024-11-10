package io.netty.handler.codec.http2;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http2.Http2Exception;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.concurrent.TimeUnit;

/* loaded from: classes4.dex */
final class Http2MaxRstFrameListener extends Http2FrameListenerDecorator {
    private long lastRstFrameNano;
    private final int maxRstFramesPerWindow;
    private final long nanosPerWindow;
    private int receivedRstInWindow;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) Http2MaxRstFrameListener.class);
    private static final Http2Exception RST_FRAME_RATE_EXCEEDED = Http2Exception.newStatic(Http2Error.ENHANCE_YOUR_CALM, "Maximum number of RST frames reached", Http2Exception.ShutdownHint.HARD_SHUTDOWN, Http2MaxRstFrameListener.class, "onRstStreamRead(..)");

    /* JADX INFO: Access modifiers changed from: package-private */
    public Http2MaxRstFrameListener(Http2FrameListener listener, int maxRstFramesPerWindow, int secondsPerWindow) {
        super(listener);
        this.lastRstFrameNano = System.nanoTime();
        this.maxRstFramesPerWindow = maxRstFramesPerWindow;
        this.nanosPerWindow = TimeUnit.SECONDS.toNanos(secondsPerWindow);
    }

    @Override // io.netty.handler.codec.http2.Http2FrameListenerDecorator, io.netty.handler.codec.http2.Http2FrameListener
    public void onRstStreamRead(ChannelHandlerContext ctx, int streamId, long errorCode) throws Http2Exception {
        long currentNano = System.nanoTime();
        if (currentNano - this.lastRstFrameNano >= this.nanosPerWindow) {
            this.lastRstFrameNano = currentNano;
            this.receivedRstInWindow = 1;
        } else {
            this.receivedRstInWindow++;
            if (this.receivedRstInWindow > this.maxRstFramesPerWindow) {
                logger.debug("{} Maximum number {} of RST frames reached within {} seconds, closing connection with {} error", ctx.channel(), Integer.valueOf(this.maxRstFramesPerWindow), Long.valueOf(TimeUnit.NANOSECONDS.toSeconds(this.nanosPerWindow)), RST_FRAME_RATE_EXCEEDED.error(), RST_FRAME_RATE_EXCEEDED);
                throw RST_FRAME_RATE_EXCEEDED;
            }
        }
        super.onRstStreamRead(ctx, streamId, errorCode);
    }
}
