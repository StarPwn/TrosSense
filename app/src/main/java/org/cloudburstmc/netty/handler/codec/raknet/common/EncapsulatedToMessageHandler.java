package org.cloudburstmc.netty.handler.codec.raknet.common;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.cloudburstmc.netty.channel.raknet.packet.EncapsulatedPacket;

@ChannelHandler.Sharable
/* loaded from: classes5.dex */
public class EncapsulatedToMessageHandler extends SimpleChannelInboundHandler<EncapsulatedPacket> {
    public static final EncapsulatedToMessageHandler INSTANCE = new EncapsulatedToMessageHandler();
    public static final String NAME = "encapsulated-to-message";

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.SimpleChannelInboundHandler
    public void channelRead0(ChannelHandlerContext ctx, EncapsulatedPacket packet) throws Exception {
        ctx.fireChannelRead((Object) packet.toMessage().retain());
    }
}
