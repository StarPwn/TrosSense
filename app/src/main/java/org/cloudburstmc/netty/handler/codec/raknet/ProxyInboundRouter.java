package org.cloudburstmc.netty.handler.codec.raknet;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import java.nio.channels.ClosedChannelException;
import org.cloudburstmc.netty.channel.proxy.ProxyChannel;

/* loaded from: classes5.dex */
public class ProxyInboundRouter implements ChannelInboundHandler {
    public static final String NAME = "rak-proxy-inbound-router";
    private final ProxyChannel<?> proxiedChannel;

    public ProxyInboundRouter(ProxyChannel<?> proxiedChannel) {
        this.proxiedChannel = proxiedChannel;
    }

    @Override // io.netty.channel.ChannelInboundHandler
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        this.proxiedChannel.pipeline().fireChannelRegistered();
    }

    @Override // io.netty.channel.ChannelInboundHandler
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        this.proxiedChannel.pipeline().fireChannelUnregistered();
    }

    @Override // io.netty.channel.ChannelHandler
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    }

    @Override // io.netty.channel.ChannelInboundHandler
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    }

    @Override // io.netty.channel.ChannelInboundHandler
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.proxiedChannel.pipeline().fireChannelInactive();
    }

    @Override // io.netty.channel.ChannelHandler
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    }

    @Override // io.netty.channel.ChannelInboundHandler
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.proxiedChannel.pipeline().fireChannelRead(msg);
    }

    @Override // io.netty.channel.ChannelInboundHandler
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        this.proxiedChannel.pipeline().fireChannelReadComplete();
    }

    @Override // io.netty.channel.ChannelInboundHandler
    public void userEventTriggered(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.proxiedChannel.pipeline().fireUserEventTriggered(msg);
    }

    @Override // io.netty.channel.ChannelInboundHandler
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        this.proxiedChannel.pipeline().fireChannelWritabilityChanged();
    }

    @Override // io.netty.channel.ChannelInboundHandler
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable throwable) throws Exception {
        if (!(throwable instanceof ClosedChannelException)) {
            this.proxiedChannel.pipeline().fireExceptionCaught(throwable);
        }
    }
}
