package org.cloudburstmc.netty.channel.raknet;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ServerChannel;
import io.netty.channel.socket.DatagramChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.PromiseCombiner;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.cloudburstmc.netty.channel.proxy.ProxyChannel;
import org.cloudburstmc.netty.channel.raknet.config.DefaultRakServerConfig;
import org.cloudburstmc.netty.channel.raknet.config.RakServerChannelConfig;
import org.cloudburstmc.netty.handler.codec.raknet.common.UnconnectedPongEncoder;
import org.cloudburstmc.netty.handler.codec.raknet.server.RakServerOfflineHandler;
import org.cloudburstmc.netty.handler.codec.raknet.server.RakServerRateLimiter;
import org.cloudburstmc.netty.handler.codec.raknet.server.RakServerRouteHandler;
import org.cloudburstmc.netty.handler.codec.raknet.server.RakServerTailHandler;

/* loaded from: classes5.dex */
public class RakServerChannel extends ProxyChannel<DatagramChannel> implements ServerChannel {
    private final Map<SocketAddress, RakChildChannel> childChannelMap;
    private final RakServerChannelConfig config;

    public RakServerChannel(DatagramChannel channel) {
        super(channel);
        this.childChannelMap = new ConcurrentHashMap();
        this.config = new DefaultRakServerConfig(this);
        pipeline().addLast(UnconnectedPongEncoder.NAME, UnconnectedPongEncoder.INSTANCE);
        pipeline().addLast(RakServerRateLimiter.NAME, new RakServerRateLimiter(this));
        pipeline().addLast(RakServerOfflineHandler.NAME, new RakServerOfflineHandler(this));
        pipeline().addLast(RakServerRouteHandler.NAME, new RakServerRouteHandler(this));
        pipeline().addLast(RakServerTailHandler.NAME, RakServerTailHandler.INSTANCE);
    }

    public RakChildChannel createChildChannel(InetSocketAddress address, long clientGuid, int protocolVersion, int mtu) {
        if (this.childChannelMap.containsKey(address)) {
            return null;
        }
        RakChildChannel channel = new RakChildChannel(address, this, clientGuid, protocolVersion, mtu);
        channel.closeFuture().addListener(new GenericFutureListener() { // from class: org.cloudburstmc.netty.channel.raknet.RakServerChannel$$ExternalSyntheticLambda0
            @Override // io.netty.util.concurrent.GenericFutureListener
            public final void operationComplete(Future future) {
                RakServerChannel.this.onChildClosed((ChannelFuture) future);
            }
        });
        pipeline().fireChannelRead((Object) channel).fireChannelReadComplete();
        this.childChannelMap.put(address, channel);
        return channel;
    }

    public RakChildChannel getChildChannel(SocketAddress address) {
        return this.childChannelMap.get(address);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onChildClosed(ChannelFuture channelFuture) {
        RakChildChannel channel = (RakChildChannel) channelFuture.channel();
        channel.rakPipeline().fireChannelInactive();
        channel.rakPipeline().fireChannelUnregistered();
        this.childChannelMap.remove(channel.remoteAddress());
    }

    @Override // org.cloudburstmc.netty.channel.proxy.ProxyChannel
    public void onCloseTriggered(final ChannelPromise promise) {
        final PromiseCombiner combiner = new PromiseCombiner(eventLoop());
        this.childChannelMap.values().forEach(new Consumer() { // from class: org.cloudburstmc.netty.channel.raknet.RakServerChannel$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                PromiseCombiner.this.add(((RakChildChannel) obj).close());
            }
        });
        ChannelPromise combinedPromise = newPromise();
        combinedPromise.addListener(new GenericFutureListener() { // from class: org.cloudburstmc.netty.channel.raknet.RakServerChannel$$ExternalSyntheticLambda2
            @Override // io.netty.util.concurrent.GenericFutureListener
            public final void operationComplete(Future future) {
                RakServerChannel.this.m2036xadb1aff4(promise, future);
            }
        });
        combiner.finish(combinedPromise);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCloseTriggered$1$org-cloudburstmc-netty-channel-raknet-RakServerChannel, reason: not valid java name */
    public /* synthetic */ void m2036xadb1aff4(ChannelPromise promise, Future future) throws Exception {
        super.onCloseTriggered(promise);
    }

    public boolean tryBlockAddress(InetAddress address, long time, TimeUnit unit) {
        RakServerRateLimiter rateLimiter = (RakServerRateLimiter) pipeline().get(RakServerRateLimiter.class);
        if (rateLimiter != null) {
            return rateLimiter.blockAddress(address, time, unit);
        }
        return false;
    }

    @Override // org.cloudburstmc.netty.channel.proxy.ProxyChannel, io.netty.channel.Channel
    public RakServerChannelConfig config() {
        return this.config;
    }
}
