package io.netty.handler.codec.http2;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class Http2ControlFrameLimitEncoder extends DecoratingHttp2ConnectionEncoder {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) Http2ControlFrameLimitEncoder.class);
    private Http2LifecycleManager lifecycleManager;
    private boolean limitReached;
    private final int maxOutstandingControlFrames;
    private int outstandingControlFrames;
    private final ChannelFutureListener outstandingControlFramesListener;

    static /* synthetic */ int access$010(Http2ControlFrameLimitEncoder x0) {
        int i = x0.outstandingControlFrames;
        x0.outstandingControlFrames = i - 1;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Http2ControlFrameLimitEncoder(Http2ConnectionEncoder delegate, int maxOutstandingControlFrames) {
        super(delegate);
        this.outstandingControlFramesListener = new ChannelFutureListener() { // from class: io.netty.handler.codec.http2.Http2ControlFrameLimitEncoder.1
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(ChannelFuture future) {
                Http2ControlFrameLimitEncoder.access$010(Http2ControlFrameLimitEncoder.this);
            }
        };
        this.maxOutstandingControlFrames = ObjectUtil.checkPositive(maxOutstandingControlFrames, "maxOutstandingControlFrames");
    }

    @Override // io.netty.handler.codec.http2.DecoratingHttp2ConnectionEncoder, io.netty.handler.codec.http2.Http2ConnectionEncoder
    public void lifecycleManager(Http2LifecycleManager lifecycleManager) {
        this.lifecycleManager = lifecycleManager;
        super.lifecycleManager(lifecycleManager);
    }

    @Override // io.netty.handler.codec.http2.DecoratingHttp2FrameWriter, io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writeSettingsAck(ChannelHandlerContext ctx, ChannelPromise promise) {
        ChannelPromise newPromise = handleOutstandingControlFrames(ctx, promise);
        if (newPromise == null) {
            return promise;
        }
        return super.writeSettingsAck(ctx, newPromise);
    }

    @Override // io.netty.handler.codec.http2.DecoratingHttp2FrameWriter, io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writePing(ChannelHandlerContext ctx, boolean ack, long data, ChannelPromise promise) {
        if (ack) {
            ChannelPromise newPromise = handleOutstandingControlFrames(ctx, promise);
            if (newPromise == null) {
                return promise;
            }
            return super.writePing(ctx, ack, data, newPromise);
        }
        return super.writePing(ctx, ack, data, promise);
    }

    @Override // io.netty.handler.codec.http2.DecoratingHttp2FrameWriter, io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writeRstStream(ChannelHandlerContext ctx, int streamId, long errorCode, ChannelPromise promise) {
        ChannelPromise newPromise = handleOutstandingControlFrames(ctx, promise);
        if (newPromise == null) {
            return promise;
        }
        return super.writeRstStream(ctx, streamId, errorCode, newPromise);
    }

    /* JADX WARN: Type inference failed for: r0v6, types: [io.netty.channel.ChannelPromise] */
    private ChannelPromise handleOutstandingControlFrames(ChannelHandlerContext ctx, ChannelPromise promise) {
        if (!this.limitReached) {
            if (this.outstandingControlFrames == this.maxOutstandingControlFrames) {
                ctx.flush();
            }
            if (this.outstandingControlFrames == this.maxOutstandingControlFrames) {
                this.limitReached = true;
                Http2Exception exception = Http2Exception.connectionError(Http2Error.ENHANCE_YOUR_CALM, "Maximum number %d of outstanding control frames reached", Integer.valueOf(this.maxOutstandingControlFrames));
                logger.info("Maximum number {} of outstanding control frames reached. Closing channel {}", Integer.valueOf(this.maxOutstandingControlFrames), ctx.channel(), exception);
                this.lifecycleManager.onError(ctx, true, exception);
                ctx.close();
            }
            this.outstandingControlFrames++;
            return promise.unvoid().addListener((GenericFutureListener<? extends Future<? super Void>>) this.outstandingControlFramesListener);
        }
        return promise;
    }
}
