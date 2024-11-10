package io.netty.handler.codec.compression;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.util.concurrent.TimeUnit;

/* loaded from: classes4.dex */
final class EncoderUtil {
    private static final int THREAD_POOL_DELAY_SECONDS = 10;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void closeAfterFinishEncode(final ChannelHandlerContext ctx, ChannelFuture finishFuture, final ChannelPromise promise) {
        if (!finishFuture.isDone()) {
            final Future<?> future = ctx.executor().schedule(new Runnable() { // from class: io.netty.handler.codec.compression.EncoderUtil.1
                @Override // java.lang.Runnable
                public void run() {
                    ChannelHandlerContext.this.close(promise);
                }
            }, 10L, TimeUnit.SECONDS);
            finishFuture.addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.handler.codec.compression.EncoderUtil.2
                @Override // io.netty.util.concurrent.GenericFutureListener
                public void operationComplete(ChannelFuture f) {
                    Future.this.cancel(true);
                    if (!promise.isDone()) {
                        ctx.close(promise);
                    }
                }
            });
        } else {
            ctx.close(promise);
        }
    }

    private EncoderUtil() {
    }
}
