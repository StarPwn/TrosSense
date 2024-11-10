package io.netty.bootstrap;

import io.netty.bootstrap.AbstractBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.resolver.AddressResolver;
import io.netty.resolver.AddressResolverGroup;
import io.netty.resolver.DefaultAddressResolverGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Collection;

/* loaded from: classes.dex */
public class Bootstrap extends AbstractBootstrap<Bootstrap, Channel> {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) Bootstrap.class);
    private final BootstrapConfig config;
    private volatile boolean disableResolver;
    private ExternalAddressResolver externalResolver;
    private volatile SocketAddress remoteAddress;

    public Bootstrap() {
        this.config = new BootstrapConfig(this);
    }

    private Bootstrap(Bootstrap bootstrap) {
        super(bootstrap);
        this.config = new BootstrapConfig(this);
        this.externalResolver = bootstrap.externalResolver;
        this.disableResolver = bootstrap.disableResolver;
        this.remoteAddress = bootstrap.remoteAddress;
    }

    public Bootstrap resolver(AddressResolverGroup<?> resolver) {
        this.externalResolver = resolver == null ? null : new ExternalAddressResolver(resolver);
        this.disableResolver = false;
        return this;
    }

    public Bootstrap disableResolver() {
        this.externalResolver = null;
        this.disableResolver = true;
        return this;
    }

    public Bootstrap remoteAddress(SocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
        return this;
    }

    public Bootstrap remoteAddress(String inetHost, int inetPort) {
        this.remoteAddress = InetSocketAddress.createUnresolved(inetHost, inetPort);
        return this;
    }

    public Bootstrap remoteAddress(InetAddress inetHost, int inetPort) {
        this.remoteAddress = new InetSocketAddress(inetHost, inetPort);
        return this;
    }

    public ChannelFuture connect() {
        validate();
        SocketAddress remoteAddress = this.remoteAddress;
        if (remoteAddress == null) {
            throw new IllegalStateException("remoteAddress not set");
        }
        return doResolveAndConnect(remoteAddress, this.config.localAddress());
    }

    public ChannelFuture connect(String inetHost, int inetPort) {
        return connect(InetSocketAddress.createUnresolved(inetHost, inetPort));
    }

    public ChannelFuture connect(InetAddress inetHost, int inetPort) {
        return connect(new InetSocketAddress(inetHost, inetPort));
    }

    public ChannelFuture connect(SocketAddress remoteAddress) {
        ObjectUtil.checkNotNull(remoteAddress, "remoteAddress");
        validate();
        return doResolveAndConnect(remoteAddress, this.config.localAddress());
    }

    public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress) {
        ObjectUtil.checkNotNull(remoteAddress, "remoteAddress");
        validate();
        return doResolveAndConnect(remoteAddress, localAddress);
    }

    private ChannelFuture doResolveAndConnect(final SocketAddress remoteAddress, final SocketAddress localAddress) {
        ChannelFuture regFuture = initAndRegister();
        final Channel channel = regFuture.channel();
        if (regFuture.isDone()) {
            if (!regFuture.isSuccess()) {
                return regFuture;
            }
            return doResolveAndConnect0(channel, remoteAddress, localAddress, channel.newPromise());
        }
        final AbstractBootstrap.PendingRegistrationPromise promise = new AbstractBootstrap.PendingRegistrationPromise(channel);
        regFuture.addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.bootstrap.Bootstrap.1
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(ChannelFuture future) throws Exception {
                Throwable cause = future.cause();
                if (cause != null) {
                    promise.setFailure(cause);
                } else {
                    promise.registered();
                    Bootstrap.this.doResolveAndConnect0(channel, remoteAddress, localAddress, promise);
                }
            }
        });
        return promise;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ChannelFuture doResolveAndConnect0(final Channel channel, SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) {
        try {
        } catch (Throwable cause) {
            promise.tryFailure(cause);
        }
        if (this.disableResolver) {
            doConnect(remoteAddress, localAddress, promise);
            return promise;
        }
        EventLoop eventLoop = channel.eventLoop();
        try {
            AddressResolver<SocketAddress> resolver = ExternalAddressResolver.getOrDefault(this.externalResolver).getResolver(eventLoop);
            if (resolver.isSupported(remoteAddress) && !resolver.isResolved(remoteAddress)) {
                Future<SocketAddress> resolveFuture = resolver.resolve(remoteAddress);
                if (resolveFuture.isDone()) {
                    Throwable resolveFailureCause = resolveFuture.cause();
                    if (resolveFailureCause != null) {
                        channel.close();
                        promise.setFailure(resolveFailureCause);
                    } else {
                        doConnect(resolveFuture.getNow(), localAddress, promise);
                    }
                    return promise;
                }
                resolveFuture.addListener(new FutureListener<SocketAddress>() { // from class: io.netty.bootstrap.Bootstrap.2
                    @Override // io.netty.util.concurrent.GenericFutureListener
                    public void operationComplete(Future<SocketAddress> future) throws Exception {
                        if (future.cause() == null) {
                            Bootstrap.doConnect(future.getNow(), localAddress, promise);
                        } else {
                            channel.close();
                            promise.setFailure(future.cause());
                        }
                    }
                });
                return promise;
            }
            doConnect(remoteAddress, localAddress, promise);
            return promise;
        } catch (Throwable cause2) {
            channel.close();
            return promise.setFailure(cause2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void doConnect(final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise connectPromise) {
        final Channel channel = connectPromise.channel();
        channel.eventLoop().execute(new Runnable() { // from class: io.netty.bootstrap.Bootstrap.3
            @Override // java.lang.Runnable
            public void run() {
                if (localAddress == null) {
                    channel.connect(remoteAddress, connectPromise);
                } else {
                    channel.connect(remoteAddress, localAddress, connectPromise);
                }
                connectPromise.addListener((GenericFutureListener<? extends Future<? super Void>>) ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        });
    }

    @Override // io.netty.bootstrap.AbstractBootstrap
    void init(Channel channel) {
        ChannelPipeline p = channel.pipeline();
        p.addLast(this.config.handler());
        setChannelOptions(channel, newOptionsArray(), logger);
        setAttributes(channel, newAttributesArray());
        Collection<ChannelInitializerExtension> extensions = getInitializerExtensions();
        if (!extensions.isEmpty()) {
            for (ChannelInitializerExtension extension : extensions) {
                try {
                    extension.postInitializeClientChannel(channel);
                } catch (Exception e) {
                    logger.warn("Exception thrown from postInitializeClientChannel", (Throwable) e);
                }
            }
        }
    }

    @Override // io.netty.bootstrap.AbstractBootstrap
    public Bootstrap validate() {
        super.validate();
        if (this.config.handler() == null) {
            throw new IllegalStateException("handler not set");
        }
        return this;
    }

    @Override // io.netty.bootstrap.AbstractBootstrap
    /* renamed from: clone, reason: avoid collision after fix types in other method */
    public Bootstrap mo213clone() {
        return new Bootstrap(this);
    }

    public Bootstrap clone(EventLoopGroup group) {
        Bootstrap bs = new Bootstrap(this);
        bs.group = group;
        return bs;
    }

    @Override // io.netty.bootstrap.AbstractBootstrap
    public final AbstractBootstrapConfig<Bootstrap, Channel> config() {
        return this.config;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final SocketAddress remoteAddress() {
        return this.remoteAddress;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final AddressResolverGroup<?> resolver() {
        if (this.disableResolver) {
            return null;
        }
        return ExternalAddressResolver.getOrDefault(this.externalResolver);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class ExternalAddressResolver {
        final AddressResolverGroup<SocketAddress> resolverGroup;

        /* JADX WARN: Multi-variable type inference failed */
        ExternalAddressResolver(AddressResolverGroup<?> addressResolverGroup) {
            this.resolverGroup = addressResolverGroup;
        }

        static AddressResolverGroup<SocketAddress> getOrDefault(ExternalAddressResolver externalResolver) {
            if (externalResolver == null) {
                return DefaultAddressResolverGroup.INSTANCE;
            }
            return externalResolver.resolverGroup;
        }
    }
}
