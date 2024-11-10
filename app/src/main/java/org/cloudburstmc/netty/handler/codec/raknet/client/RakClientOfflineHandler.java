package org.cloudburstmc.netty.handler.codec.raknet.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ConnectTimeoutException;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ScheduledFuture;
import java.net.Inet6Address;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import org.cloudburstmc.netty.channel.raknet.RakChannel;
import org.cloudburstmc.netty.channel.raknet.RakConstants;
import org.cloudburstmc.netty.channel.raknet.RakDisconnectReason;
import org.cloudburstmc.netty.channel.raknet.RakOfflineState;
import org.cloudburstmc.netty.channel.raknet.config.RakChannelOption;
import org.cloudburstmc.netty.handler.codec.raknet.common.ConnectedPingHandler;
import org.cloudburstmc.netty.handler.codec.raknet.common.ConnectedPongHandler;
import org.cloudburstmc.netty.handler.codec.raknet.common.DisconnectNotificationHandler;
import org.cloudburstmc.netty.handler.codec.raknet.common.EncapsulatedToMessageHandler;
import org.cloudburstmc.netty.handler.codec.raknet.common.RakAcknowledgeHandler;
import org.cloudburstmc.netty.handler.codec.raknet.common.RakDatagramCodec;
import org.cloudburstmc.netty.handler.codec.raknet.common.RakSessionCodec;
import org.cloudburstmc.netty.util.RakUtils;

/* loaded from: classes5.dex */
public class RakClientOfflineHandler extends SimpleChannelInboundHandler<ByteBuf> {
    public static final String NAME = "rak-client-handler";
    private int connectionAttempts;
    private final RakChannel rakChannel;
    private ScheduledFuture<?> retryFuture;
    private RakOfflineState state = RakOfflineState.HANDSHAKE_1;
    private final ChannelPromise successPromise;
    private ScheduledFuture<?> timeoutFuture;

    public RakClientOfflineHandler(RakChannel rakChannel, ChannelPromise promise) {
        this.rakChannel = rakChannel;
        this.successPromise = promise;
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        final Channel channel = ctx.channel();
        long timeout = ((Long) this.rakChannel.config().getOption(RakChannelOption.RAK_CONNECT_TIMEOUT)).longValue();
        this.timeoutFuture = channel.eventLoop().schedule(new Runnable() { // from class: org.cloudburstmc.netty.handler.codec.raknet.client.RakClientOfflineHandler$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                RakClientOfflineHandler.this.onTimeout();
            }
        }, timeout, TimeUnit.MILLISECONDS);
        this.retryFuture = channel.eventLoop().scheduleAtFixedRate(new Runnable() { // from class: org.cloudburstmc.netty.handler.codec.raknet.client.RakClientOfflineHandler$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                RakClientOfflineHandler.this.m2040x90916995(channel);
            }
        }, 0L, 1L, TimeUnit.SECONDS);
        this.successPromise.addListener(new GenericFutureListener() { // from class: org.cloudburstmc.netty.handler.codec.raknet.client.RakClientOfflineHandler$$ExternalSyntheticLambda3
            @Override // io.netty.util.concurrent.GenericFutureListener
            public final void operationComplete(Future future) {
                RakClientOfflineHandler.this.m2041xd2a896f4(channel, future);
            }
        });
        this.successPromise.addListener(new GenericFutureListener() { // from class: org.cloudburstmc.netty.handler.codec.raknet.client.RakClientOfflineHandler$$ExternalSyntheticLambda4
            @Override // io.netty.util.concurrent.GenericFutureListener
            public final void operationComplete(Future future) {
                RakClientOfflineHandler.this.m2042x14bfc453(channel, future);
            }
        });
        this.retryFuture.addListener(new GenericFutureListener() { // from class: org.cloudburstmc.netty.handler.codec.raknet.client.RakClientOfflineHandler$$ExternalSyntheticLambda5
            @Override // io.netty.util.concurrent.GenericFutureListener
            public final void operationComplete(Future future) {
                RakClientOfflineHandler.this.m2043x56d6f1b2(future);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$handlerAdded$1$org-cloudburstmc-netty-handler-codec-raknet-client-RakClientOfflineHandler, reason: not valid java name */
    public /* synthetic */ void m2041xd2a896f4(Channel channel, Future future) throws Exception {
        safeCancel(this.timeoutFuture, channel);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$handlerAdded$2$org-cloudburstmc-netty-handler-codec-raknet-client-RakClientOfflineHandler, reason: not valid java name */
    public /* synthetic */ void m2042x14bfc453(Channel channel, Future future) throws Exception {
        safeCancel(this.retryFuture, channel);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$handlerAdded$3$org-cloudburstmc-netty-handler-codec-raknet-client-RakClientOfflineHandler, reason: not valid java name */
    public /* synthetic */ void m2043x56d6f1b2(Future future) throws Exception {
        if (future.cause() != null) {
            this.successPromise.tryFailure(future.cause());
        }
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        safeCancel(this.timeoutFuture, ctx.channel());
        safeCancel(this.retryFuture, ctx.channel());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: onRetryAttempt, reason: merged with bridge method [inline-methods] */
    public void m2040x90916995(Channel channel) {
        switch (this.state) {
            case HANDSHAKE_1:
                sendOpenConnectionRequest1(channel);
                this.connectionAttempts++;
                return;
            case HANDSHAKE_2:
                sendOpenConnectionRequest2(channel);
                return;
            default:
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onTimeout() {
        this.successPromise.tryFailure(new ConnectTimeoutException());
    }

    private void onSuccess(ChannelHandlerContext ctx) {
        RakSessionCodec sessionCodec = new RakSessionCodec(this.rakChannel);
        ctx.pipeline().addAfter(NAME, RakDatagramCodec.NAME, new RakDatagramCodec());
        ctx.pipeline().addAfter(RakDatagramCodec.NAME, RakAcknowledgeHandler.NAME, new RakAcknowledgeHandler(sessionCodec));
        ctx.pipeline().addAfter(RakAcknowledgeHandler.NAME, RakSessionCodec.NAME, sessionCodec);
        ctx.pipeline().addAfter(RakSessionCodec.NAME, ConnectedPingHandler.NAME, new ConnectedPingHandler());
        ctx.pipeline().addAfter(ConnectedPingHandler.NAME, ConnectedPongHandler.NAME, new ConnectedPongHandler(sessionCodec));
        ctx.pipeline().addAfter(ConnectedPongHandler.NAME, DisconnectNotificationHandler.NAME, DisconnectNotificationHandler.INSTANCE);
        ctx.pipeline().addAfter(DisconnectNotificationHandler.NAME, EncapsulatedToMessageHandler.NAME, EncapsulatedToMessageHandler.INSTANCE);
        ctx.pipeline().addAfter(DisconnectNotificationHandler.NAME, RakClientOnlineInitialHandler.NAME, new RakClientOnlineInitialHandler(this.rakChannel, this.successPromise));
        ctx.pipeline().fireChannelActive();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.SimpleChannelInboundHandler
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        if (!buf.isReadable()) {
            return;
        }
        if (this.state == RakOfflineState.HANDSHAKE_COMPLETED) {
            ctx.fireChannelRead((Object) buf.retain());
            return;
        }
        short packetId = buf.readUnsignedByte();
        ByteBuf magicBuf = (ByteBuf) this.rakChannel.config().getOption(RakChannelOption.RAK_UNCONNECTED_MAGIC);
        if (!buf.isReadable(magicBuf.readableBytes()) || !ByteBufUtil.equals(buf.readSlice(magicBuf.readableBytes()), magicBuf)) {
            this.successPromise.tryFailure(new CorruptedFrameException("RakMagic does not match"));
            return;
        }
        switch (packetId) {
            case 6:
                onOpenConnectionReply1(ctx, buf);
                return;
            case 8:
                onOpenConnectionReply2(ctx, buf);
                onSuccess(ctx);
                return;
            case 18:
                this.rakChannel.pipeline().fireUserEventTriggered((Object) RakDisconnectReason.ALREADY_CONNECTED);
                this.successPromise.tryFailure(new ChannelException("Already connected"));
                return;
            case 20:
                this.rakChannel.pipeline().fireUserEventTriggered((Object) RakDisconnectReason.NO_FREE_INCOMING_CONNECTIONS);
                this.successPromise.tryFailure(new ChannelException("No free incoming connections"));
                return;
            case 25:
                this.rakChannel.pipeline().fireUserEventTriggered((Object) RakDisconnectReason.INCOMPATIBLE_PROTOCOL_VERSION);
                this.successPromise.tryFailure(new IllegalStateException("Incompatible raknet version"));
                return;
            case 26:
                this.rakChannel.pipeline().fireUserEventTriggered((Object) RakDisconnectReason.IP_RECENTLY_CONNECTED);
                this.successPromise.tryFailure(new ChannelException("Address recently connected"));
                return;
            default:
                return;
        }
    }

    private void onOpenConnectionReply1(ChannelHandlerContext ctx, ByteBuf buffer) {
        long serverGuid = buffer.readLong();
        boolean security = buffer.readBoolean();
        int mtu = buffer.readShort();
        if (security) {
            this.successPromise.tryFailure(new SecurityException());
            return;
        }
        this.rakChannel.config().setOption(RakChannelOption.RAK_MTU, Integer.valueOf(mtu));
        this.rakChannel.config().setOption(RakChannelOption.RAK_REMOTE_GUID, Long.valueOf(serverGuid));
        this.state = RakOfflineState.HANDSHAKE_2;
        sendOpenConnectionRequest2(ctx.channel());
    }

    private void onOpenConnectionReply2(ChannelHandlerContext ctx, ByteBuf buffer) {
        buffer.readLong();
        RakUtils.readAddress(buffer);
        int mtu = buffer.readShort();
        buffer.readBoolean();
        this.rakChannel.config().setOption(RakChannelOption.RAK_MTU, Integer.valueOf(mtu));
        this.state = RakOfflineState.HANDSHAKE_COMPLETED;
    }

    private void sendOpenConnectionRequest1(Channel channel) {
        int mtuSize = ((Integer) this.rakChannel.config().getOption(RakChannelOption.RAK_MTU)).intValue() - (this.connectionAttempts * 91);
        if (mtuSize < 576) {
            mtuSize = RakConstants.MINIMUM_MTU_SIZE;
        }
        ByteBuf magicBuf = (ByteBuf) this.rakChannel.config().getOption(RakChannelOption.RAK_UNCONNECTED_MAGIC);
        int rakVersion = ((Integer) this.rakChannel.config().getOption(RakChannelOption.RAK_PROTOCOL_VERSION)).intValue();
        InetSocketAddress address = (InetSocketAddress) this.rakChannel.remoteAddress();
        ByteBuf request = channel.alloc().ioBuffer(mtuSize);
        request.writeByte(5);
        request.writeBytes(magicBuf.slice(), magicBuf.readableBytes());
        request.writeByte(rakVersion);
        request.writeZero(((((mtuSize - 1) - magicBuf.readableBytes()) - 1) - (address.getAddress() instanceof Inet6Address ? 40 : 20)) - 8);
        channel.writeAndFlush(request);
    }

    private void sendOpenConnectionRequest2(Channel channel) {
        int mtuSize = ((Integer) this.rakChannel.config().getOption(RakChannelOption.RAK_MTU)).intValue();
        ByteBuf magicBuf = (ByteBuf) this.rakChannel.config().getOption(RakChannelOption.RAK_UNCONNECTED_MAGIC);
        ByteBuf request = channel.alloc().ioBuffer(34);
        request.writeByte(7);
        request.writeBytes(magicBuf, magicBuf.readerIndex(), magicBuf.readableBytes());
        RakUtils.writeAddress(request, (InetSocketAddress) channel.remoteAddress());
        request.writeShort(mtuSize);
        request.writeLong(((Long) this.rakChannel.config().getOption(RakChannelOption.RAK_GUID)).longValue());
        channel.writeAndFlush(request);
    }

    private static void safeCancel(final ScheduledFuture<?> future, Channel channel) {
        channel.eventLoop().execute(new Runnable() { // from class: org.cloudburstmc.netty.handler.codec.raknet.client.RakClientOfflineHandler$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                RakClientOfflineHandler.lambda$safeCancel$4(ScheduledFuture.this);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$safeCancel$4(ScheduledFuture future) {
        if (!future.isCancelled()) {
            future.cancel(false);
        }
    }
}
