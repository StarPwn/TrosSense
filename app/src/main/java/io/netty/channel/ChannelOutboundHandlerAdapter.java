package io.netty.channel;

import io.netty.channel.ChannelHandlerMask;
import java.net.SocketAddress;

/* loaded from: classes4.dex */
public class ChannelOutboundHandlerAdapter extends ChannelHandlerAdapter implements ChannelOutboundHandler {
    @Override // io.netty.channel.ChannelOutboundHandler
    @ChannelHandlerMask.Skip
    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        ctx.bind(localAddress, promise);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    @ChannelHandlerMask.Skip
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        ctx.connect(remoteAddress, localAddress, promise);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    @ChannelHandlerMask.Skip
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.disconnect(promise);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    @ChannelHandlerMask.Skip
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.close(promise);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    @ChannelHandlerMask.Skip
    public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.deregister(promise);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    @ChannelHandlerMask.Skip
    public void read(ChannelHandlerContext ctx) throws Exception {
        ctx.read();
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    @ChannelHandlerMask.Skip
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ctx.write(msg, promise);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    @ChannelHandlerMask.Skip
    public void flush(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
