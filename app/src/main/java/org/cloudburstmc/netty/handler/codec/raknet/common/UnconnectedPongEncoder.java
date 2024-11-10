package org.cloudburstmc.netty.handler.codec.raknet.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.socket.DatagramPacket;
import org.cloudburstmc.netty.channel.raknet.RakPong;
import org.cloudburstmc.netty.channel.raknet.config.RakChannelOption;

@ChannelHandler.Sharable
/* loaded from: classes5.dex */
public class UnconnectedPongEncoder extends ChannelOutboundHandlerAdapter {
    public static final UnconnectedPongEncoder INSTANCE = new UnconnectedPongEncoder();
    public static final String NAME = "rak-unconnected-pong-encoder";

    private UnconnectedPongEncoder() {
    }

    @Override // io.netty.channel.ChannelOutboundHandlerAdapter, io.netty.channel.ChannelOutboundHandler
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!(msg instanceof RakPong)) {
            ctx.write(msg, promise);
            return;
        }
        RakPong pong = (RakPong) msg;
        ByteBuf magicBuf = (ByteBuf) ctx.channel().config().getOption(RakChannelOption.RAK_UNCONNECTED_MAGIC);
        long guid = ((Long) ctx.channel().config().getOption(RakChannelOption.RAK_GUID)).longValue();
        ByteBuf pongData = pong.getPongData();
        ByteBuf pongBuffer = ctx.alloc().ioBuffer(magicBuf.readableBytes() + 19 + pongData.readableBytes());
        pongBuffer.writeByte(28);
        pongBuffer.writeLong(pong.getPingTime());
        pongBuffer.writeLong(guid);
        pongBuffer.writeBytes(magicBuf, magicBuf.readerIndex(), magicBuf.readableBytes());
        pongBuffer.writeShort(pongData.readableBytes());
        pongBuffer.writeBytes(pongData, pongData.readerIndex(), pongData.readableBytes());
        ctx.write(new DatagramPacket(pongBuffer, pong.getSender()));
    }
}
