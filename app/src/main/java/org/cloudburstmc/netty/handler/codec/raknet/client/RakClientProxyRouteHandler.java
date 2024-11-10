package org.cloudburstmc.netty.handler.codec.raknet.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.socket.DatagramPacket;
import org.cloudburstmc.netty.channel.raknet.RakClientChannel;
import org.cloudburstmc.netty.channel.raknet.config.RakMetrics;

/* loaded from: classes5.dex */
public class RakClientProxyRouteHandler extends ChannelDuplexHandler {
    public static final String NAME = "rak-client-proxy-route-handler";
    private final RakClientChannel channel;

    public RakClientProxyRouteHandler(RakClientChannel channel) {
        this.channel = channel;
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof DatagramPacket)) {
            ctx.fireChannelRead(msg);
            return;
        }
        DatagramPacket packet = (DatagramPacket) msg;
        RakMetrics metrics = this.channel.config().getMetrics();
        if (metrics != null) {
            metrics.bytesIn(((ByteBuf) packet.content()).readableBytes());
        }
        DatagramPacket datagram = packet.retain();
        try {
            if (packet.sender() != null && !packet.sender().equals(this.channel.remoteAddress())) {
                ctx.fireChannelRead((Object) datagram);
            }
            ctx.fireChannelRead(datagram.content());
        } finally {
            datagram.release();
        }
    }

    @Override // io.netty.channel.ChannelDuplexHandler, io.netty.channel.ChannelOutboundHandler
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        boolean isDatagram = msg instanceof DatagramPacket;
        if (!isDatagram && !(msg instanceof ByteBuf)) {
            ctx.write(msg, promise);
            return;
        }
        DatagramPacket datagram = isDatagram ? (DatagramPacket) msg : new DatagramPacket((ByteBuf) msg, this.channel.remoteAddress());
        RakMetrics metrics = this.channel.config().getMetrics();
        if (metrics != null) {
            metrics.bytesOut(((ByteBuf) datagram.content()).readableBytes());
        }
        ctx.write(datagram, promise);
    }
}
