package org.cloudburstmc.netty.handler.codec.raknet.client;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.PromiseCombiner;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import org.cloudburstmc.netty.channel.raknet.RakClientChannel;
import org.cloudburstmc.netty.handler.codec.raknet.common.UnconnectedPongDecoder;

/* loaded from: classes5.dex */
public class RakClientRouteHandler extends ChannelDuplexHandler {
    public static final String NAME = "rak-client-route-handler";
    private final RakClientChannel channel;

    public RakClientRouteHandler(RakClientChannel channel) {
        this.channel = channel;
    }

    @Override // io.netty.channel.ChannelDuplexHandler, io.netty.channel.ChannelOutboundHandler
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        if (!(remoteAddress instanceof InetSocketAddress)) {
            promise.tryFailure(new IllegalArgumentException("Provided remote address must be InetSocketAddress"));
            return;
        }
        if (this.channel.parent().isActive()) {
            throw new IllegalStateException("Channel is already bound!");
        }
        ChannelFuture parentFuture = this.channel.parent().connect(remoteAddress, localAddress);
        parentFuture.addListener(new GenericFutureListener() { // from class: org.cloudburstmc.netty.handler.codec.raknet.client.RakClientRouteHandler$$ExternalSyntheticLambda0
            @Override // io.netty.util.concurrent.GenericFutureListener
            public final void operationComplete(Future future) {
                RakClientRouteHandler.this.m2044x2e93de35(future);
            }
        });
        PromiseCombiner combiner = new PromiseCombiner(this.channel.eventLoop());
        combiner.add(parentFuture);
        combiner.add((Future) this.channel.getConnectPromise());
        combiner.finish(promise);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$connect$0$org-cloudburstmc-netty-handler-codec-raknet-client-RakClientRouteHandler, reason: not valid java name */
    public /* synthetic */ void m2044x2e93de35(Future future) throws Exception {
        if (future.isSuccess()) {
            this.channel.rakPipeline().addAfter(UnconnectedPongDecoder.NAME, RakClientOfflineHandler.NAME, new RakClientOfflineHandler(this.channel, this.channel.getConnectPromise()));
        }
    }

    @Override // io.netty.channel.ChannelDuplexHandler, io.netty.channel.ChannelOutboundHandler
    public void read(ChannelHandlerContext ctx) throws Exception {
    }
}
