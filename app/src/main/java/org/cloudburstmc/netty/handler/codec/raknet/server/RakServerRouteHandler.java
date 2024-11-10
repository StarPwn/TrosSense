package org.cloudburstmc.netty.handler.codec.raknet.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import org.cloudburstmc.netty.channel.raknet.RakChildChannel;
import org.cloudburstmc.netty.channel.raknet.RakServerChannel;
import org.cloudburstmc.netty.channel.raknet.config.RakMetrics;

/* loaded from: classes5.dex */
public class RakServerRouteHandler extends ChannelDuplexHandler {
    public static final String NAME = "rak-server-route-handler";
    private final RakServerChannel parent;

    public RakServerRouteHandler(RakServerChannel parent) {
        this.parent = parent;
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof DatagramPacket)) {
            ctx.fireChannelRead(msg);
            return;
        }
        DatagramPacket packet = (DatagramPacket) msg;
        try {
            RakChildChannel channel = this.parent.getChildChannel(packet.sender());
            if (channel == null) {
                ctx.fireChannelRead((Object) packet.retain());
                return;
            }
            RakMetrics metrics = channel.config().getMetrics();
            if (metrics != null) {
                metrics.bytesIn(((ByteBuf) packet.content()).readableBytes());
            }
            ByteBuf buffer = ((ByteBuf) packet.content()).retain();
            channel.rakPipeline().fireChannelRead((Object) buffer).fireChannelReadComplete();
        } finally {
            packet.release();
        }
    }
}
