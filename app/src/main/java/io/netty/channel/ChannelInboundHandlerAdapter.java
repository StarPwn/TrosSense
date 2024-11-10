package io.netty.channel;

import io.netty.channel.ChannelHandlerMask;

/* loaded from: classes4.dex */
public class ChannelInboundHandlerAdapter extends ChannelHandlerAdapter implements ChannelInboundHandler {
    @Override // io.netty.channel.ChannelInboundHandler
    @ChannelHandlerMask.Skip
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelRegistered();
    }

    @Override // io.netty.channel.ChannelInboundHandler
    @ChannelHandlerMask.Skip
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelUnregistered();
    }

    @Override // io.netty.channel.ChannelInboundHandler
    @ChannelHandlerMask.Skip
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();
    }

    @Override // io.netty.channel.ChannelInboundHandler
    @ChannelHandlerMask.Skip
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelInactive();
    }

    @ChannelHandlerMask.Skip
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.fireChannelRead(msg);
    }

    @Override // io.netty.channel.ChannelInboundHandler
    @ChannelHandlerMask.Skip
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelReadComplete();
    }

    @Override // io.netty.channel.ChannelInboundHandler
    @ChannelHandlerMask.Skip
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        ctx.fireUserEventTriggered(evt);
    }

    @Override // io.netty.channel.ChannelInboundHandler
    @ChannelHandlerMask.Skip
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelWritabilityChanged();
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler, io.netty.channel.ChannelInboundHandler
    @ChannelHandlerMask.Skip
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }
}
