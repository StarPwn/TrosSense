package org.cloudburstmc.netty.channel.raknet;

import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.socket.DatagramChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.cloudburstmc.netty.channel.proxy.ProxyChannel;
import org.cloudburstmc.netty.channel.raknet.config.DefaultRakClientConfig;
import org.cloudburstmc.netty.channel.raknet.config.RakChannelConfig;
import org.cloudburstmc.netty.handler.codec.raknet.ProxyInboundRouter;
import org.cloudburstmc.netty.handler.codec.raknet.client.RakClientProxyRouteHandler;
import org.cloudburstmc.netty.handler.codec.raknet.client.RakClientRouteHandler;
import org.cloudburstmc.netty.handler.codec.raknet.common.UnconnectedPingEncoder;
import org.cloudburstmc.netty.handler.codec.raknet.common.UnconnectedPongDecoder;

/* loaded from: classes5.dex */
public class RakClientChannel extends ProxyChannel<DatagramChannel> implements RakChannel {
    private static final ChannelMetadata metadata = new ChannelMetadata(true);
    private final RakChannelConfig config;
    private final ChannelPromise connectPromise;

    public RakClientChannel(DatagramChannel channel) {
        super(channel);
        this.config = new DefaultRakClientConfig(this);
        pipeline().addLast(RakClientRouteHandler.NAME, new RakClientRouteHandler(this));
        rakPipeline().addFirst(RakClientProxyRouteHandler.NAME, new RakClientProxyRouteHandler(this));
        rakPipeline().addBefore(ProxyInboundRouter.NAME, UnconnectedPingEncoder.NAME, new UnconnectedPingEncoder(this));
        rakPipeline().addAfter(UnconnectedPingEncoder.NAME, UnconnectedPongDecoder.NAME, new UnconnectedPongDecoder(this));
        this.connectPromise = newPromise();
        this.connectPromise.addListener(new GenericFutureListener() { // from class: org.cloudburstmc.netty.channel.raknet.RakClientChannel$$ExternalSyntheticLambda0
            @Override // io.netty.util.concurrent.GenericFutureListener
            public final void operationComplete(Future future) {
                RakClientChannel.this.m2035x99636985(future);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$org-cloudburstmc-netty-channel-raknet-RakClientChannel, reason: not valid java name */
    public /* synthetic */ void m2035x99636985(Future future) throws Exception {
        if (future.isSuccess()) {
            onConnectionEstablished();
        } else {
            close();
        }
    }

    private void onConnectionEstablished() {
        pipeline().fireChannelActive();
    }

    @Override // org.cloudburstmc.netty.channel.proxy.ProxyChannel, io.netty.channel.Channel
    public RakChannelConfig config() {
        return this.config;
    }

    public ChannelPromise getConnectPromise() {
        return this.connectPromise;
    }

    @Override // org.cloudburstmc.netty.channel.proxy.ProxyChannel, io.netty.channel.Channel
    public boolean isActive() {
        return super.isActive() && this.connectPromise.isSuccess();
    }

    @Override // org.cloudburstmc.netty.channel.raknet.RakChannel
    public ChannelPipeline rakPipeline() {
        return parent().pipeline();
    }

    @Override // org.cloudburstmc.netty.channel.proxy.ProxyChannel, io.netty.channel.Channel
    public ChannelMetadata metadata() {
        return metadata;
    }
}
