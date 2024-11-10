package org.cloudburstmc.netty.handler.codec.raknet.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import java.net.InetSocketAddress;
import org.cloudburstmc.netty.channel.raknet.RakChannel;
import org.cloudburstmc.netty.channel.raknet.RakConstants;
import org.cloudburstmc.netty.channel.raknet.RakPriority;
import org.cloudburstmc.netty.channel.raknet.RakReliability;
import org.cloudburstmc.netty.channel.raknet.config.RakChannelOption;
import org.cloudburstmc.netty.channel.raknet.packet.EncapsulatedPacket;
import org.cloudburstmc.netty.channel.raknet.packet.RakMessage;
import org.cloudburstmc.netty.util.RakUtils;

/* loaded from: classes5.dex */
public class RakClientOnlineInitialHandler extends SimpleChannelInboundHandler<EncapsulatedPacket> {
    public static final String NAME = "rak-client-online-initial-handler";
    private final RakChannel rakChannel;
    private final ChannelPromise successPromise;

    public RakClientOnlineInitialHandler(RakChannel rakChannel, ChannelPromise promise) {
        this.rakChannel = rakChannel;
        this.successPromise = promise;
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        sendConnectionRequest(ctx);
    }

    private void sendConnectionRequest(ChannelHandlerContext ctx) {
        long guid = ((Long) this.rakChannel.config().getOption(RakChannelOption.RAK_GUID)).longValue();
        ByteBuf buffer = ctx.alloc().ioBuffer(18);
        buffer.writeByte(9);
        buffer.writeLong(guid);
        buffer.writeLong(System.currentTimeMillis());
        buffer.writeBoolean(false);
        ctx.writeAndFlush(new RakMessage(buffer, RakReliability.RELIABLE_ORDERED, RakPriority.IMMEDIATE));
    }

    private void onSuccess(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        channel.pipeline().remove(RakClientOfflineHandler.NAME);
        channel.pipeline().remove(NAME);
        this.successPromise.trySuccess();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.SimpleChannelInboundHandler
    public void channelRead0(ChannelHandlerContext ctx, EncapsulatedPacket message) throws Exception {
        ByteBuf buf = message.getBuffer();
        int packetId = buf.getUnsignedByte(buf.readerIndex());
        switch (packetId) {
            case 16:
                onConnectionRequestAccepted(ctx, buf);
                onSuccess(ctx);
                return;
            case 17:
                this.successPromise.tryFailure(new IllegalStateException("Connection denied"));
                return;
            default:
                ctx.fireChannelRead((Object) message.retain());
                return;
        }
    }

    private void onConnectionRequestAccepted(ChannelHandlerContext ctx, ByteBuf buf) {
        buf.skipBytes(1);
        RakUtils.readAddress(buf);
        buf.readUnsignedShort();
        int count = 0;
        long pingTime = 0;
        while (buf.isReadable(23)) {
            try {
                RakUtils.readAddress(buf);
                count++;
            } catch (IndexOutOfBoundsException e) {
            }
        }
        pingTime = buf.readLong();
        buf.readLong();
        ByteBuf buffer = ctx.alloc().ioBuffer();
        buffer.writeByte(19);
        RakUtils.writeAddress(buffer, (InetSocketAddress) ctx.channel().remoteAddress());
        for (int i = 0; i < count; i++) {
            RakUtils.writeAddress(buffer, RakConstants.LOCAL_ADDRESS);
        }
        buffer.writeLong(pingTime);
        buffer.writeLong(System.currentTimeMillis());
        ctx.writeAndFlush(new RakMessage(buffer, RakReliability.RELIABLE_ORDERED, RakPriority.IMMEDIATE));
    }
}
