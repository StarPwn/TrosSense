package org.cloudburstmc.netty.handler.codec.raknet.server;

import com.trossense.bl;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.Inet6Address;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;
import org.cloudburstmc.netty.channel.raknet.RakChildChannel;
import org.cloudburstmc.netty.channel.raknet.RakConstants;
import org.cloudburstmc.netty.channel.raknet.RakDisconnectReason;
import org.cloudburstmc.netty.channel.raknet.RakPriority;
import org.cloudburstmc.netty.channel.raknet.RakReliability;
import org.cloudburstmc.netty.channel.raknet.config.RakServerChannelConfig;
import org.cloudburstmc.netty.channel.raknet.packet.EncapsulatedPacket;
import org.cloudburstmc.netty.channel.raknet.packet.RakMessage;
import org.cloudburstmc.netty.handler.codec.raknet.common.RakSessionCodec;
import org.cloudburstmc.netty.util.RakUtils;

@ChannelHandler.Sharable
/* loaded from: classes5.dex */
public class RakServerOnlineInitialHandler extends SimpleChannelInboundHandler<EncapsulatedPacket> {
    public static final String NAME = "rak-server-online-initial-handler";
    private static final InternalLogger log = InternalLoggerFactory.getInstance((Class<?>) RakServerOnlineInitialHandler.class);
    private final RakChildChannel channel;
    private final AtomicInteger retriesCounter = new AtomicInteger(5);

    public RakServerOnlineInitialHandler(RakChildChannel channel) {
        this.channel = channel;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.SimpleChannelInboundHandler
    public void channelRead0(ChannelHandlerContext ctx, EncapsulatedPacket message) throws Exception {
        ByteBuf buf = message.getBuffer();
        int packetId = buf.getUnsignedByte(buf.readerIndex());
        switch (packetId) {
            case 9:
                onConnectionRequest(ctx, buf);
                return;
            case 19:
                buf.skipBytes(1);
                ctx.pipeline().remove(this);
                this.channel.eventLoop().execute(new Runnable() { // from class: org.cloudburstmc.netty.handler.codec.raknet.server.RakServerOnlineInitialHandler$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        RakServerOnlineInitialHandler.this.m2050xe6265cd6();
                    }
                });
                return;
            default:
                ctx.fireChannelRead((Object) message.retain());
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$channelRead0$0$org-cloudburstmc-netty-handler-codec-raknet-server-RakServerOnlineInitialHandler, reason: not valid java name */
    public /* synthetic */ void m2050xe6265cd6() {
        this.channel.setActive(true);
        this.channel.pipeline().fireChannelActive();
    }

    private void onConnectionRequest(ChannelHandlerContext ctx, ByteBuf buffer) {
        buffer.skipBytes(1);
        long guid = this.channel.config().getGuid();
        long serverGuid = buffer.readLong();
        long timestamp = buffer.readLong();
        boolean security = buffer.readBoolean();
        if (this.retriesCounter.decrementAndGet() < 0) {
            sendConnectionRequestFailed(ctx, guid);
            log.warn("[{}] Connection request failed due to too many retries", this.channel.remoteAddress());
        } else if (serverGuid != guid || security) {
            sendConnectionRequestFailed(ctx, guid);
        } else {
            sendConnectionRequestAccepted(ctx, timestamp);
        }
    }

    private void sendConnectionRequestAccepted(ChannelHandlerContext ctx, long time) {
        InetSocketAddress address = this.channel.remoteAddress();
        boolean ipv6 = address.getAddress() instanceof Inet6Address;
        ByteBuf outBuf = ctx.alloc().ioBuffer(ipv6 ? 628 : bl.b1);
        outBuf.writeByte(16);
        RakUtils.writeAddress(outBuf, address);
        outBuf.writeShort(0);
        for (InetSocketAddress socketAddress : ipv6 ? RakConstants.LOCAL_IP_ADDRESSES_V6 : RakConstants.LOCAL_IP_ADDRESSES_V4) {
            RakUtils.writeAddress(outBuf, socketAddress);
        }
        outBuf.writeLong(time);
        outBuf.writeLong(System.currentTimeMillis());
        ctx.writeAndFlush(new RakMessage(outBuf, RakReliability.UNRELIABLE, RakPriority.IMMEDIATE));
    }

    private void sendConnectionRequestFailed(ChannelHandlerContext ctx, long guid) {
        ByteBuf magicBuf = ((RakServerChannelConfig) ctx.channel().config()).getUnconnectedMagic();
        int length = magicBuf.readableBytes() + 9;
        ByteBuf reply = ctx.alloc().ioBuffer(length);
        reply.writeByte(17);
        reply.writeBytes(magicBuf, magicBuf.readerIndex(), magicBuf.readableBytes());
        reply.writeLong(guid);
        sendRaw(ctx, reply);
        ctx.fireUserEventTriggered((Object) RakDisconnectReason.CONNECTION_REQUEST_FAILED).close();
    }

    private void sendRaw(ChannelHandlerContext ctx, ByteBuf buf) {
        ctx.pipeline().context(RakSessionCodec.NAME).writeAndFlush(buf);
    }
}
