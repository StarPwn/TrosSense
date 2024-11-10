package org.cloudburstmc.netty.handler.codec.raknet.common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import org.cloudburstmc.netty.channel.raknet.RakClientChannel;
import org.cloudburstmc.netty.channel.raknet.RakPong;
import org.cloudburstmc.netty.channel.raknet.config.RakChannelOption;
import org.cloudburstmc.netty.handler.codec.raknet.AdvancedChannelInboundHandler;

/* loaded from: classes5.dex */
public class UnconnectedPongDecoder extends AdvancedChannelInboundHandler<DatagramPacket> {
    public static final String NAME = "rak-unconnected-pong-deencoder";
    private final RakClientChannel channel;

    public UnconnectedPongDecoder(RakClientChannel channel) {
        this.channel = channel;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.netty.handler.codec.raknet.AdvancedChannelInboundHandler
    public boolean acceptInboundMessage(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!super.acceptInboundMessage(ctx, msg)) {
            return false;
        }
        DatagramPacket packet = (DatagramPacket) msg;
        ByteBuf buf = (ByteBuf) packet.content();
        return buf.isReadable() && buf.getUnsignedByte(buf.readerIndex()) == 28;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.netty.handler.codec.raknet.AdvancedChannelInboundHandler
    public void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        ByteBuf pongData;
        ByteBuf buf = (ByteBuf) packet.content();
        buf.readUnsignedByte();
        long pingTime = buf.readLong();
        long guid = buf.readLong();
        ByteBuf magicBuf = (ByteBuf) this.channel.config().getOption(RakChannelOption.RAK_UNCONNECTED_MAGIC);
        if (buf.isReadable(magicBuf.readableBytes()) && ByteBufUtil.equals(buf.readSlice(magicBuf.readableBytes()), magicBuf)) {
            ByteBuf pongData2 = Unpooled.EMPTY_BUFFER;
            if (!buf.isReadable(2)) {
                pongData = pongData2;
            } else {
                ByteBuf pongData3 = buf.readRetainedSlice(buf.readUnsignedShort());
                pongData = pongData3;
            }
            ctx.fireChannelRead((Object) new RakPong(pingTime, guid, pongData, packet.sender()));
        }
    }
}
