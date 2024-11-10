package org.cloudburstmc.netty.handler.codec.raknet.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.socket.DatagramPacket;
import org.cloudburstmc.netty.channel.raknet.RakClientChannel;
import org.cloudburstmc.netty.channel.raknet.RakPing;
import org.cloudburstmc.netty.channel.raknet.config.RakChannelOption;

/* loaded from: classes5.dex */
public class UnconnectedPingEncoder extends ChannelOutboundHandlerAdapter {
    public static final String NAME = "rak-unconnected-ping-encoder";
    private final RakClientChannel channel;

    public UnconnectedPingEncoder(RakClientChannel channel) {
        this.channel = channel;
    }

    @Override // io.netty.channel.ChannelOutboundHandlerAdapter, io.netty.channel.ChannelOutboundHandler
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!(msg instanceof RakPing)) {
            ctx.write(msg, promise);
            return;
        }
        RakPing ping = (RakPing) msg;
        ByteBuf magicBuf = (ByteBuf) this.channel.config().getOption(RakChannelOption.RAK_UNCONNECTED_MAGIC);
        long guid = ((Long) this.channel.config().getOption(RakChannelOption.RAK_GUID)).longValue();
        ByteBuf pingBuffer = ctx.alloc().ioBuffer(magicBuf.readableBytes() + 17);
        pingBuffer.writeByte(1);
        pingBuffer.writeLong(ping.getPingTime());
        pingBuffer.writeBytes(magicBuf, magicBuf.readerIndex(), magicBuf.readableBytes());
        pingBuffer.writeLong(guid);
        ctx.write(new DatagramPacket(pingBuffer, ping.getSender()), promise);
    }
}
