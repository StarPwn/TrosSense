package io.netty.handler.flush;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.internal.ObjectUtil;
import java.util.concurrent.Future;

/* loaded from: classes4.dex */
public class FlushConsolidationHandler extends ChannelDuplexHandler {
    public static final int DEFAULT_EXPLICIT_FLUSH_AFTER_FLUSHES = 256;
    private final boolean consolidateWhenNoReadInProgress;
    private ChannelHandlerContext ctx;
    private final int explicitFlushAfterFlushes;
    private int flushPendingCount;
    private final Runnable flushTask;
    private Future<?> nextScheduledFlush;
    private boolean readInProgress;

    public FlushConsolidationHandler() {
        this(256, false);
    }

    public FlushConsolidationHandler(int explicitFlushAfterFlushes) {
        this(explicitFlushAfterFlushes, false);
    }

    public FlushConsolidationHandler(int explicitFlushAfterFlushes, boolean consolidateWhenNoReadInProgress) {
        this.explicitFlushAfterFlushes = ObjectUtil.checkPositive(explicitFlushAfterFlushes, "explicitFlushAfterFlushes");
        this.consolidateWhenNoReadInProgress = consolidateWhenNoReadInProgress;
        this.flushTask = consolidateWhenNoReadInProgress ? new Runnable() { // from class: io.netty.handler.flush.FlushConsolidationHandler.1
            @Override // java.lang.Runnable
            public void run() {
                if (FlushConsolidationHandler.this.flushPendingCount > 0 && !FlushConsolidationHandler.this.readInProgress) {
                    FlushConsolidationHandler.this.flushPendingCount = 0;
                    FlushConsolidationHandler.this.nextScheduledFlush = null;
                    FlushConsolidationHandler.this.ctx.flush();
                }
            }
        } : null;
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
    }

    @Override // io.netty.channel.ChannelDuplexHandler, io.netty.channel.ChannelOutboundHandler
    public void flush(ChannelHandlerContext ctx) throws Exception {
        if (this.readInProgress) {
            int i = this.flushPendingCount + 1;
            this.flushPendingCount = i;
            if (i == this.explicitFlushAfterFlushes) {
                flushNow(ctx);
                return;
            }
            return;
        }
        if (this.consolidateWhenNoReadInProgress) {
            int i2 = this.flushPendingCount + 1;
            this.flushPendingCount = i2;
            if (i2 == this.explicitFlushAfterFlushes) {
                flushNow(ctx);
                return;
            } else {
                scheduleFlush(ctx);
                return;
            }
        }
        flushNow(ctx);
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        resetReadAndFlushIfNeeded(ctx);
        ctx.fireChannelReadComplete();
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.readInProgress = true;
        ctx.fireChannelRead(msg);
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler, io.netty.channel.ChannelInboundHandler
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        resetReadAndFlushIfNeeded(ctx);
        ctx.fireExceptionCaught(cause);
    }

    @Override // io.netty.channel.ChannelDuplexHandler, io.netty.channel.ChannelOutboundHandler
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        resetReadAndFlushIfNeeded(ctx);
        ctx.disconnect(promise);
    }

    @Override // io.netty.channel.ChannelDuplexHandler, io.netty.channel.ChannelOutboundHandler
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        resetReadAndFlushIfNeeded(ctx);
        ctx.close(promise);
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        if (!ctx.channel().isWritable()) {
            flushIfNeeded(ctx);
        }
        ctx.fireChannelWritabilityChanged();
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        flushIfNeeded(ctx);
    }

    private void resetReadAndFlushIfNeeded(ChannelHandlerContext ctx) {
        this.readInProgress = false;
        flushIfNeeded(ctx);
    }

    private void flushIfNeeded(ChannelHandlerContext ctx) {
        if (this.flushPendingCount > 0) {
            flushNow(ctx);
        }
    }

    private void flushNow(ChannelHandlerContext ctx) {
        cancelScheduledFlush();
        this.flushPendingCount = 0;
        ctx.flush();
    }

    private void scheduleFlush(ChannelHandlerContext ctx) {
        if (this.nextScheduledFlush == null) {
            this.nextScheduledFlush = ctx.channel().eventLoop().submit(this.flushTask);
        }
    }

    private void cancelScheduledFlush() {
        if (this.nextScheduledFlush != null) {
            this.nextScheduledFlush.cancel(false);
            this.nextScheduledFlush = null;
        }
    }
}
