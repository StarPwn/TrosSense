package org.cloudburstmc.netty.handler.codec.raknet.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.nio.channels.ClosedChannelException;
import org.cloudburstmc.netty.channel.raknet.RakChildChannel;
import org.cloudburstmc.netty.channel.raknet.config.RakMetrics;

/* loaded from: classes5.dex */
public class RakChildDatagramHandler extends ChannelOutboundHandlerAdapter {
    public static final String NAME = "rak-child-datagram-handler";
    private volatile boolean canFlush = false;
    private final RakChildChannel channel;

    public RakChildDatagramHandler(RakChildChannel channel) {
        this.channel = channel;
    }

    @Override // io.netty.channel.ChannelOutboundHandlerAdapter, io.netty.channel.ChannelOutboundHandler
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        boolean isDatagram = msg instanceof DatagramPacket;
        if (!isDatagram && !(msg instanceof ByteBuf)) {
            ctx.write(msg, promise);
            return;
        }
        this.canFlush = true;
        promise.trySuccess();
        DatagramPacket datagram = isDatagram ? (DatagramPacket) msg : new DatagramPacket((ByteBuf) msg, this.channel.remoteAddress());
        RakMetrics metrics = this.channel.config().getMetrics();
        if (metrics != null) {
            metrics.bytesOut(((ByteBuf) datagram.content()).readableBytes());
        }
        Channel parent = this.channel.parent().parent();
        parent.write(datagram).addListener(new GenericFutureListener() { // from class: org.cloudburstmc.netty.handler.codec.raknet.server.RakChildDatagramHandler$$ExternalSyntheticLambda0
            @Override // io.netty.util.concurrent.GenericFutureListener
            public final void operationComplete(Future future) {
                RakChildDatagramHandler.this.m2049xec9473f9((ChannelFuture) future);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$write$0$org-cloudburstmc-netty-handler-codec-raknet-server-RakChildDatagramHandler, reason: not valid java name */
    public /* synthetic */ void m2049xec9473f9(ChannelFuture future) throws Exception {
        if (!future.isSuccess() && !(future.cause() instanceof ClosedChannelException)) {
            this.channel.pipeline().fireExceptionCaught(future.cause());
            this.channel.close();
        }
    }

    @Override // io.netty.channel.ChannelOutboundHandlerAdapter, io.netty.channel.ChannelOutboundHandler
    public void flush(ChannelHandlerContext ctx) throws Exception {
        if (this.canFlush) {
            this.canFlush = false;
            ctx.flush();
        }
    }
}
