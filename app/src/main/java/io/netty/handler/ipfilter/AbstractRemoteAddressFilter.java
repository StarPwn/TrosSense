package io.netty.handler.ipfilter;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.net.SocketAddress;

/* loaded from: classes4.dex */
public abstract class AbstractRemoteAddressFilter<T extends SocketAddress> extends ChannelInboundHandlerAdapter {
    protected abstract boolean accept(ChannelHandlerContext channelHandlerContext, T t) throws Exception;

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        handleNewChannel(ctx);
        ctx.fireChannelRegistered();
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (!handleNewChannel(ctx)) {
            throw new IllegalStateException("cannot determine to accept or reject a channel: " + ctx.channel());
        }
        ctx.fireChannelActive();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private boolean handleNewChannel(ChannelHandlerContext ctx) throws Exception {
        SocketAddress remoteAddress = ctx.channel().remoteAddress();
        if (remoteAddress == null) {
            return false;
        }
        ctx.pipeline().remove(this);
        if (accept(ctx, remoteAddress)) {
            channelAccepted(ctx, remoteAddress);
            return true;
        }
        ChannelFuture rejectedFuture = channelRejected(ctx, remoteAddress);
        if (rejectedFuture != null) {
            rejectedFuture.addListener((GenericFutureListener<? extends Future<? super Void>>) ChannelFutureListener.CLOSE);
            return true;
        }
        ctx.close();
        return true;
    }

    protected void channelAccepted(ChannelHandlerContext ctx, T remoteAddress) {
    }

    protected ChannelFuture channelRejected(ChannelHandlerContext ctx, T remoteAddress) {
        return null;
    }
}
