package org.cloudburstmc.netty.channel.proxy;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelProgressivePromise;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultChannelPipeline;
import io.netty.channel.EventLoop;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Objects;
import org.cloudburstmc.netty.handler.codec.raknet.ProxyInboundRouter;
import org.cloudburstmc.netty.handler.codec.raknet.ProxyOutboundRouter;
import org.cloudburstmc.netty.util.RakUtils;

/* loaded from: classes5.dex */
public abstract class ProxyChannel<T extends Channel> implements Channel {
    protected final T channel;
    protected final ChannelPipeline pipeline;

    /* JADX INFO: Access modifiers changed from: protected */
    public ProxyChannel(T parent) {
        Objects.requireNonNull(parent, "parent");
        this.channel = parent;
        this.pipeline = newChannelPipeline();
        parent.pipeline().addLast(ProxyInboundRouter.NAME, new ProxyInboundRouter(this));
        this.pipeline.addLast(ProxyOutboundRouter.NAME, new ProxyOutboundRouter(this));
    }

    public void onCloseTriggered(ChannelPromise promise) {
        this.channel.close(correctPromise(promise));
    }

    public ChannelPromise correctPromise(final ChannelPromise remotePromise) {
        ChannelPromise localPromise = this.channel.newPromise();
        localPromise.addListener(new GenericFutureListener() { // from class: org.cloudburstmc.netty.channel.proxy.ProxyChannel$$ExternalSyntheticLambda0
            @Override // io.netty.util.concurrent.GenericFutureListener
            public final void operationComplete(Future future) {
                ProxyChannel.lambda$correctPromise$0(ChannelPromise.this, future);
            }
        });
        return localPromise;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$correctPromise$0(ChannelPromise remotePromise, Future future) throws Exception {
        if (future.isSuccess()) {
            remotePromise.trySuccess();
        } else {
            remotePromise.tryFailure(future.cause());
        }
    }

    protected DefaultChannelPipeline newChannelPipeline() {
        return RakUtils.newChannelPipeline(this);
    }

    protected final ChannelPipeline internalPipeline() {
        return this.channel.pipeline();
    }

    @Override // io.netty.channel.Channel
    public ChannelId id() {
        return this.channel.id();
    }

    @Override // io.netty.channel.Channel
    public EventLoop eventLoop() {
        return this.channel.eventLoop();
    }

    @Override // io.netty.channel.Channel
    public Channel parent() {
        return this.channel;
    }

    @Override // io.netty.channel.Channel
    public ChannelConfig config() {
        return this.channel.config();
    }

    @Override // io.netty.channel.Channel
    public boolean isOpen() {
        return this.channel.isOpen();
    }

    @Override // io.netty.channel.Channel
    public boolean isRegistered() {
        return this.channel.isRegistered();
    }

    @Override // io.netty.channel.Channel
    public boolean isActive() {
        return this.channel.isActive();
    }

    @Override // io.netty.channel.Channel
    public ChannelMetadata metadata() {
        return this.channel.metadata();
    }

    @Override // io.netty.channel.Channel
    public InetSocketAddress localAddress() {
        return (InetSocketAddress) this.channel.localAddress();
    }

    @Override // io.netty.channel.Channel
    public InetSocketAddress remoteAddress() {
        return (InetSocketAddress) this.channel.remoteAddress();
    }

    @Override // io.netty.channel.Channel
    public ChannelFuture closeFuture() {
        return this.channel.closeFuture();
    }

    @Override // io.netty.channel.Channel
    public boolean isWritable() {
        return this.channel.isWritable();
    }

    @Override // io.netty.channel.Channel
    public long bytesBeforeUnwritable() {
        return this.channel.bytesBeforeUnwritable();
    }

    @Override // io.netty.channel.Channel
    public long bytesBeforeWritable() {
        return this.channel.bytesBeforeWritable();
    }

    @Override // io.netty.channel.Channel
    public Channel.Unsafe unsafe() {
        return this.channel.unsafe();
    }

    @Override // io.netty.channel.Channel
    public ChannelPipeline pipeline() {
        return this.pipeline;
    }

    @Override // io.netty.channel.Channel
    public ByteBufAllocator alloc() {
        return config().getAllocator();
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture bind(SocketAddress localAddress) {
        return this.pipeline.bind(localAddress);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture connect(SocketAddress remoteAddress) {
        return this.pipeline.connect(remoteAddress);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress) {
        return this.pipeline.connect(remoteAddress, localAddress);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture disconnect() {
        return this.pipeline.disconnect();
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture close() {
        return this.pipeline.close();
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture deregister() {
        return this.pipeline.deregister();
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture bind(SocketAddress localAddress, ChannelPromise promise) {
        return this.pipeline.bind(localAddress, promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture connect(SocketAddress remoteAddress, ChannelPromise promise) {
        return this.pipeline.connect(remoteAddress, promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
        return this.pipeline.connect(remoteAddress, localAddress, promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture disconnect(ChannelPromise promise) {
        return this.pipeline.disconnect(promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture close(ChannelPromise promise) {
        return this.pipeline.close(promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture deregister(ChannelPromise promise) {
        return this.pipeline.deregister(promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public Channel read() {
        this.pipeline.read();
        return this;
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture write(Object msg) {
        return this.pipeline.write(msg);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture write(Object msg, ChannelPromise promise) {
        return this.pipeline.write(msg, promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public Channel flush() {
        this.pipeline.flush();
        return this;
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture writeAndFlush(Object msg, ChannelPromise promise) {
        return this.pipeline.writeAndFlush(msg, promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture writeAndFlush(Object msg) {
        return this.pipeline.writeAndFlush(msg);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelPromise newPromise() {
        return this.pipeline.newPromise();
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelProgressivePromise newProgressivePromise() {
        return this.pipeline.newProgressivePromise();
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture newSucceededFuture() {
        return this.pipeline.newSucceededFuture();
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture newFailedFuture(Throwable cause) {
        return this.pipeline.newFailedFuture(cause);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelPromise voidPromise() {
        return this.pipeline.voidPromise();
    }

    @Override // io.netty.util.AttributeMap
    public <U> Attribute<U> attr(AttributeKey<U> key) {
        return this.channel.attr(key);
    }

    @Override // io.netty.util.AttributeMap
    public <U> boolean hasAttr(AttributeKey<U> key) {
        return this.channel.hasAttr(key);
    }

    @Override // java.lang.Comparable
    public int compareTo(Channel o) {
        return this.channel.compareTo(o);
    }
}
