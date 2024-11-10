package org.cloudburstmc.netty.handler.codec.raknet.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.cloudburstmc.netty.channel.raknet.RakDisconnectReason;
import org.cloudburstmc.netty.channel.raknet.packet.EncapsulatedPacket;
import org.cloudburstmc.netty.handler.codec.raknet.AdvancedChannelInboundHandler;

@ChannelHandler.Sharable
/* loaded from: classes5.dex */
public class DisconnectNotificationHandler extends AdvancedChannelInboundHandler<EncapsulatedPacket> {
    public static final String NAME = "rak-disconnect-notification-handler";
    private static final InternalLogger log = InternalLoggerFactory.getInstance((Class<?>) DisconnectNotificationHandler.class);
    public static final DisconnectNotificationHandler INSTANCE = new DisconnectNotificationHandler();

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.netty.handler.codec.raknet.AdvancedChannelInboundHandler
    public boolean acceptInboundMessage(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!super.acceptInboundMessage(ctx, msg)) {
            return false;
        }
        ByteBuf buf = ((EncapsulatedPacket) msg).getBuffer();
        return buf.getUnsignedByte(buf.readerIndex()) == 21;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.netty.handler.codec.raknet.AdvancedChannelInboundHandler
    public void channelRead0(ChannelHandlerContext ctx, EncapsulatedPacket packet) throws Exception {
        ByteBuf buf = packet.getBuffer();
        buf.readUnsignedByte();
        if (log.isTraceEnabled()) {
            log.trace("RakNet Session ({} => {}) by remote peer!", ctx.channel().localAddress(), ctx.channel().remoteAddress());
        }
        ctx.fireUserEventTriggered((Object) RakDisconnectReason.CLOSED_BY_REMOTE_PEER);
        ctx.fireChannelInactive();
    }
}
