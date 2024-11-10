package io.netty.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ChannelHandler.Sharable
/* loaded from: classes4.dex */
public abstract class ChannelInitializer<C extends Channel> extends ChannelInboundHandlerAdapter {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) ChannelInitializer.class);
    private final Set<ChannelHandlerContext> initMap = Collections.newSetFromMap(new ConcurrentHashMap());

    protected abstract void initChannel(C c) throws Exception;

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public final void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        if (initChannel(ctx)) {
            ctx.pipeline().fireChannelRegistered();
            removeState(ctx);
        } else {
            ctx.fireChannelRegistered();
        }
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler, io.netty.channel.ChannelInboundHandler
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (logger.isWarnEnabled()) {
            logger.warn("Failed to initialize a channel. Closing: " + ctx.channel(), cause);
        }
        ctx.close();
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        if (ctx.channel().isRegistered() && initChannel(ctx)) {
            removeState(ctx);
        }
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        this.initMap.remove(ctx);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private boolean initChannel(ChannelHandlerContext ctx) throws Exception {
        boolean isRemoved;
        if (this.initMap.add(ctx)) {
            try {
                initChannel((ChannelInitializer<C>) ctx.channel());
                if (isRemoved) {
                    return true;
                }
            } catch (Throwable cause) {
                try {
                    exceptionCaught(ctx, cause);
                    if (ctx.isRemoved()) {
                        return true;
                    }
                } finally {
                    if (!ctx.isRemoved()) {
                        ctx.pipeline().remove(this);
                    }
                }
            }
            return true;
        }
        return false;
    }

    private void removeState(final ChannelHandlerContext ctx) {
        if (ctx.isRemoved()) {
            this.initMap.remove(ctx);
        } else {
            ctx.executor().execute(new Runnable() { // from class: io.netty.channel.ChannelInitializer.1
                @Override // java.lang.Runnable
                public void run() {
                    ChannelInitializer.this.initMap.remove(ctx);
                }
            });
        }
    }
}
