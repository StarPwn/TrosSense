package org.cloudburstmc.netty.handler.codec.raknet.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.cloudburstmc.netty.channel.raknet.packet.EncapsulatedPacket;
import org.cloudburstmc.netty.handler.codec.raknet.AdvancedChannelInboundHandler;

/* loaded from: classes5.dex */
public class ConnectedPongHandler extends AdvancedChannelInboundHandler<EncapsulatedPacket> {
    public static final String NAME = "rak-connected-pong-handler";
    private final RakSessionCodec sessionCodec;

    public ConnectedPongHandler(RakSessionCodec sessionCodec) {
        this.sessionCodec = sessionCodec;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.netty.handler.codec.raknet.AdvancedChannelInboundHandler
    public boolean acceptInboundMessage(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!super.acceptInboundMessage(ctx, msg)) {
            return false;
        }
        ByteBuf buf = ((EncapsulatedPacket) msg).getBuffer();
        return buf.getUnsignedByte(buf.readerIndex()) == 3;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.netty.handler.codec.raknet.AdvancedChannelInboundHandler
    public void channelRead0(ChannelHandlerContext ctx, EncapsulatedPacket packet) throws Exception {
        ByteBuf buf = packet.getBuffer();
        buf.readUnsignedByte();
        long pingTime = buf.readLong();
        this.sessionCodec.recalculatePongTime(pingTime);
    }
}
