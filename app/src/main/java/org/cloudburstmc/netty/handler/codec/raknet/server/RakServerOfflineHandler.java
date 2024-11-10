package org.cloudburstmc.netty.handler.codec.raknet.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import net.jodah.expiringmap.ExpirationListener;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.cloudburstmc.netty.channel.raknet.RakChildChannel;
import org.cloudburstmc.netty.channel.raknet.RakPing;
import org.cloudburstmc.netty.channel.raknet.RakServerChannel;
import org.cloudburstmc.netty.channel.raknet.config.RakChannelOption;
import org.cloudburstmc.netty.handler.codec.raknet.AdvancedChannelInboundHandler;
import org.cloudburstmc.netty.util.RakUtils;

/* loaded from: classes5.dex */
public class RakServerOfflineHandler extends AdvancedChannelInboundHandler<DatagramPacket> {
    public static final String NAME = "rak-offline-handler";
    private static final InternalLogger log = InternalLoggerFactory.getInstance((Class<?>) RakServerOfflineHandler.class);
    private final RakServerChannel channel;
    private final ExpiringMap<InetSocketAddress, Integer> pendingConnections = ExpiringMap.builder().expiration(10, TimeUnit.SECONDS).expirationPolicy(ExpirationPolicy.CREATED).expirationListener(new ExpirationListener() { // from class: org.cloudburstmc.netty.handler.codec.raknet.server.RakServerOfflineHandler$$ExternalSyntheticLambda0
        @Override // net.jodah.expiringmap.ExpirationListener
        public final void expired(Object obj, Object obj2) {
            ReferenceCountUtil.release(obj2);
        }
    }).build();
    private final ExpiringMap<InetAddress, AtomicInteger> packetsCounter = ExpiringMap.builder().expiration(1, TimeUnit.SECONDS).expirationPolicy(ExpirationPolicy.CREATED).build();

    public RakServerOfflineHandler(RakServerChannel channel) {
        this.channel = channel;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Failed to find 'out' block for switch in B:11:0x0020. Please report as an issue. */
    @Override // org.cloudburstmc.netty.handler.codec.raknet.AdvancedChannelInboundHandler
    public boolean acceptInboundMessage(ChannelHandlerContext ctx, Object msg) throws Exception {
        boolean z = false;
        if (!super.acceptInboundMessage(ctx, msg)) {
            return false;
        }
        DatagramPacket packet = (DatagramPacket) msg;
        ByteBuf buf = (ByteBuf) packet.content();
        if (!buf.isReadable()) {
            return false;
        }
        int startIndex = buf.readerIndex();
        try {
            int packetId = buf.readUnsignedByte();
            switch (packetId) {
                case 1:
                    if (buf.isReadable(8)) {
                        buf.readLong();
                    }
                case 5:
                case 7:
                    ByteBuf magicBuf = (ByteBuf) ctx.channel().config().getOption(RakChannelOption.RAK_UNCONNECTED_MAGIC);
                    if (buf.isReadable(magicBuf.readableBytes())) {
                        if (ByteBufUtil.equals(buf.readSlice(magicBuf.readableBytes()), magicBuf)) {
                            z = true;
                        }
                    }
                    return z;
                default:
                    return false;
            }
        } finally {
            buf.readerIndex(startIndex);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.netty.handler.codec.raknet.AdvancedChannelInboundHandler
    public void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        ByteBuf buf = (ByteBuf) packet.content();
        short packetId = buf.readUnsignedByte();
        ByteBuf magicBuf = (ByteBuf) ctx.channel().config().getOption(RakChannelOption.RAK_UNCONNECTED_MAGIC);
        long guid = ((Long) ctx.channel().config().getOption(RakChannelOption.RAK_GUID)).longValue();
        AtomicInteger counter = this.packetsCounter.computeIfAbsent(packet.sender().getAddress(), new Function() { // from class: org.cloudburstmc.netty.handler.codec.raknet.server.RakServerOfflineHandler$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return RakServerOfflineHandler.lambda$channelRead0$1((InetAddress) obj);
            }
        });
        if (counter.incrementAndGet() > this.channel.config().getUnconnectedPacketLimit()) {
            log.warn("[{}] Sent too many packets per second", packet.sender());
            this.channel.tryBlockAddress(packet.sender().getAddress(), 10L, TimeUnit.SECONDS);
            return;
        }
        switch (packetId) {
            case 1:
                onUnconnectedPing(ctx, packet, magicBuf, guid);
                return;
            case 5:
                onOpenConnectionRequest1(ctx, packet, magicBuf, guid);
                return;
            case 7:
                onOpenConnectionRequest2(ctx, packet, magicBuf, guid);
                return;
            default:
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ AtomicInteger lambda$channelRead0$1(InetAddress s) {
        return new AtomicInteger();
    }

    private void onUnconnectedPing(ChannelHandlerContext ctx, DatagramPacket packet, ByteBuf magicBuf, long guid) {
        long pingTime = ((ByteBuf) packet.content()).readLong();
        boolean handlePing = ((Boolean) ctx.channel().config().getOption(RakChannelOption.RAK_HANDLE_PING)).booleanValue();
        if (handlePing) {
            ctx.fireChannelRead((Object) new RakPing(pingTime, packet.sender()));
            return;
        }
        ByteBuf advertisement = (ByteBuf) ctx.channel().config().getOption(RakChannelOption.RAK_ADVERTISEMENT);
        int packetLength = (advertisement != null ? advertisement.readableBytes() : -2) + 35;
        ByteBuf out = ctx.alloc().ioBuffer(packetLength);
        out.writeByte(28);
        out.writeLong(pingTime);
        out.writeLong(guid);
        out.writeBytes(magicBuf, magicBuf.readerIndex(), magicBuf.readableBytes());
        if (advertisement != null) {
            out.writeShort(advertisement.readableBytes());
            out.writeBytes(advertisement, advertisement.readerIndex(), advertisement.readableBytes());
        }
        ctx.writeAndFlush(new DatagramPacket(out, packet.sender()));
    }

    private void onOpenConnectionRequest1(ChannelHandlerContext ctx, DatagramPacket packet, ByteBuf magicBuf, long guid) {
        ByteBuf buffer = (ByteBuf) packet.content();
        InetSocketAddress sender = packet.sender();
        buffer.skipBytes(magicBuf.readableBytes());
        int protocolVersion = buffer.readUnsignedByte();
        int mtu = buffer.readableBytes() + 1 + magicBuf.readableBytes() + 1 + (sender.getAddress() instanceof Inet6Address ? 40 : 20) + 8;
        int[] supportedProtocols = (int[]) ctx.channel().config().getOption(RakChannelOption.RAK_SUPPORTED_PROTOCOLS);
        if (supportedProtocols != null && Arrays.binarySearch(supportedProtocols, protocolVersion) < 0) {
            int latestVersion = supportedProtocols[supportedProtocols.length - 1];
            sendIncompatibleVersion(ctx, packet.sender(), latestVersion, magicBuf, guid);
            return;
        }
        Integer version = this.pendingConnections.put(sender, Integer.valueOf(protocolVersion));
        if (version != null && log.isTraceEnabled()) {
            log.trace("Received duplicate open connection request 1 from {}", sender);
        }
        ByteBuf replyBuffer = ctx.alloc().ioBuffer(28, 28);
        replyBuffer.writeByte(6);
        replyBuffer.writeBytes(magicBuf, magicBuf.readerIndex(), magicBuf.readableBytes());
        replyBuffer.writeLong(guid);
        replyBuffer.writeBoolean(false);
        replyBuffer.writeShort(RakUtils.clamp(mtu, ((Integer) ctx.channel().config().getOption(RakChannelOption.RAK_MIN_MTU)).intValue(), ((Integer) ctx.channel().config().getOption(RakChannelOption.RAK_MAX_MTU)).intValue()));
        ctx.writeAndFlush(new DatagramPacket(replyBuffer, sender));
    }

    private void onOpenConnectionRequest2(ChannelHandlerContext ctx, DatagramPacket packet, ByteBuf magicBuf, long guid) {
        ByteBuf buffer = (ByteBuf) packet.content();
        InetSocketAddress sender = packet.sender();
        buffer.skipBytes(magicBuf.readableBytes());
        Integer version = this.pendingConnections.remove(sender);
        if (version == null) {
            if (log.isTraceEnabled()) {
                log.trace("Received open connection request 2 from {} without open connection request 1", sender);
            }
            int[] supportedProtocols = (int[]) ctx.channel().config().getOption(RakChannelOption.RAK_SUPPORTED_PROTOCOLS);
            int latestVersion = supportedProtocols == null ? 11 : supportedProtocols[supportedProtocols.length - 1];
            sendIncompatibleVersion(ctx, sender, latestVersion, magicBuf, guid);
            return;
        }
        RakUtils.readAddress(buffer);
        int mtu = buffer.readUnsignedShort();
        long clientGuid = buffer.readLong();
        if (mtu >= ((Integer) ctx.channel().config().getOption(RakChannelOption.RAK_MIN_MTU)).intValue() && mtu <= ((Integer) ctx.channel().config().getOption(RakChannelOption.RAK_MAX_MTU)).intValue()) {
            RakServerChannel serverChannel = (RakServerChannel) ctx.channel();
            RakChildChannel channel = serverChannel.createChildChannel(sender, clientGuid, version.intValue(), mtu);
            if (channel == null) {
                sendAlreadyConnected(ctx, sender, magicBuf, guid);
                return;
            }
            ByteBuf replyBuffer = ctx.alloc().ioBuffer(31);
            replyBuffer.writeByte(8);
            replyBuffer.writeBytes(magicBuf, magicBuf.readerIndex(), magicBuf.readableBytes());
            replyBuffer.writeLong(guid);
            RakUtils.writeAddress(replyBuffer, packet.sender());
            replyBuffer.writeShort(mtu);
            replyBuffer.writeBoolean(false);
            ctx.writeAndFlush(new DatagramPacket(replyBuffer, packet.sender()));
            return;
        }
        sendAlreadyConnected(ctx, sender, magicBuf, guid);
    }

    private void sendIncompatibleVersion(ChannelHandlerContext ctx, InetSocketAddress sender, int protocolVersion, ByteBuf magicBuf, long guid) {
        ByteBuf buffer = ctx.alloc().ioBuffer(26, 26);
        buffer.writeByte(25);
        buffer.writeByte(protocolVersion);
        buffer.writeBytes(magicBuf, magicBuf.readerIndex(), magicBuf.readableBytes());
        buffer.writeLong(guid);
        ctx.writeAndFlush(new DatagramPacket(buffer, sender));
    }

    private void sendAlreadyConnected(ChannelHandlerContext ctx, InetSocketAddress sender, ByteBuf magicBuf, long guid) {
        ByteBuf buffer = ctx.alloc().ioBuffer(25, 25);
        buffer.writeByte(18);
        buffer.writeBytes(magicBuf, magicBuf.readerIndex(), magicBuf.readableBytes());
        buffer.writeLong(guid);
        ctx.writeAndFlush(new DatagramPacket(buffer, sender));
    }
}
