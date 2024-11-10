package io.netty.bootstrap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public class ServerBootstrap extends AbstractBootstrap<ServerBootstrap, ServerChannel> {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) ServerBootstrap.class);
    private final Map<AttributeKey<?>, Object> childAttrs;
    private volatile EventLoopGroup childGroup;
    private volatile ChannelHandler childHandler;
    private final Map<ChannelOption<?>, Object> childOptions;
    private final ServerBootstrapConfig config;

    public ServerBootstrap() {
        this.childOptions = new LinkedHashMap();
        this.childAttrs = new ConcurrentHashMap();
        this.config = new ServerBootstrapConfig(this);
    }

    private ServerBootstrap(ServerBootstrap bootstrap) {
        super(bootstrap);
        this.childOptions = new LinkedHashMap();
        this.childAttrs = new ConcurrentHashMap();
        this.config = new ServerBootstrapConfig(this);
        this.childGroup = bootstrap.childGroup;
        this.childHandler = bootstrap.childHandler;
        synchronized (bootstrap.childOptions) {
            this.childOptions.putAll(bootstrap.childOptions);
        }
        this.childAttrs.putAll(bootstrap.childAttrs);
    }

    @Override // io.netty.bootstrap.AbstractBootstrap
    public ServerBootstrap group(EventLoopGroup group) {
        return group(group, group);
    }

    public ServerBootstrap group(EventLoopGroup parentGroup, EventLoopGroup childGroup) {
        super.group(parentGroup);
        if (this.childGroup != null) {
            throw new IllegalStateException("childGroup set already");
        }
        this.childGroup = (EventLoopGroup) ObjectUtil.checkNotNull(childGroup, "childGroup");
        return this;
    }

    public <T> ServerBootstrap childOption(ChannelOption<T> childOption, T value) {
        ObjectUtil.checkNotNull(childOption, "childOption");
        synchronized (this.childOptions) {
            if (value == null) {
                this.childOptions.remove(childOption);
            } else {
                this.childOptions.put(childOption, value);
            }
        }
        return this;
    }

    public <T> ServerBootstrap childAttr(AttributeKey<T> childKey, T value) {
        ObjectUtil.checkNotNull(childKey, "childKey");
        if (value == null) {
            this.childAttrs.remove(childKey);
        } else {
            this.childAttrs.put(childKey, value);
        }
        return this;
    }

    public ServerBootstrap childHandler(ChannelHandler childHandler) {
        this.childHandler = (ChannelHandler) ObjectUtil.checkNotNull(childHandler, "childHandler");
        return this;
    }

    @Override // io.netty.bootstrap.AbstractBootstrap
    void init(Channel channel) {
        setChannelOptions(channel, newOptionsArray(), logger);
        setAttributes(channel, newAttributesArray());
        ChannelPipeline p = channel.pipeline();
        final EventLoopGroup currentChildGroup = this.childGroup;
        final ChannelHandler currentChildHandler = this.childHandler;
        final Map.Entry<ChannelOption<?>, Object>[] currentChildOptions = newOptionsArray(this.childOptions);
        final Map.Entry<AttributeKey<?>, Object>[] currentChildAttrs = newAttributesArray(this.childAttrs);
        final Collection<ChannelInitializerExtension> extensions = getInitializerExtensions();
        p.addLast(new ChannelInitializer<Channel>() { // from class: io.netty.bootstrap.ServerBootstrap.1
            @Override // io.netty.channel.ChannelInitializer
            public void initChannel(final Channel ch) {
                final ChannelPipeline pipeline = ch.pipeline();
                ChannelHandler handler = ServerBootstrap.this.config.handler();
                if (handler != null) {
                    pipeline.addLast(handler);
                }
                ch.eventLoop().execute(new Runnable() { // from class: io.netty.bootstrap.ServerBootstrap.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        pipeline.addLast(new ServerBootstrapAcceptor(ch, currentChildGroup, currentChildHandler, currentChildOptions, currentChildAttrs, extensions));
                    }
                });
            }
        });
        if (!extensions.isEmpty() && (channel instanceof ServerChannel)) {
            ServerChannel serverChannel = (ServerChannel) channel;
            for (ChannelInitializerExtension extension : extensions) {
                try {
                    extension.postInitializeServerListenerChannel(serverChannel);
                } catch (Exception e) {
                    logger.warn("Exception thrown from postInitializeServerListenerChannel", (Throwable) e);
                }
            }
        }
    }

    @Override // io.netty.bootstrap.AbstractBootstrap
    public ServerBootstrap validate() {
        super.validate();
        if (this.childHandler == null) {
            throw new IllegalStateException("childHandler not set");
        }
        if (this.childGroup == null) {
            logger.warn("childGroup is not set. Using parentGroup instead.");
            this.childGroup = this.config.group();
        }
        return this;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ServerBootstrapAcceptor extends ChannelInboundHandlerAdapter {
        private final Map.Entry<AttributeKey<?>, Object>[] childAttrs;
        private final EventLoopGroup childGroup;
        private final ChannelHandler childHandler;
        private final Map.Entry<ChannelOption<?>, Object>[] childOptions;
        private final Runnable enableAutoReadTask;
        private final Collection<ChannelInitializerExtension> extensions;

        ServerBootstrapAcceptor(final Channel channel, EventLoopGroup childGroup, ChannelHandler childHandler, Map.Entry<ChannelOption<?>, Object>[] childOptions, Map.Entry<AttributeKey<?>, Object>[] childAttrs, Collection<ChannelInitializerExtension> extensions) {
            this.childGroup = childGroup;
            this.childHandler = childHandler;
            this.childOptions = childOptions;
            this.childAttrs = childAttrs;
            this.extensions = extensions;
            this.enableAutoReadTask = new Runnable() { // from class: io.netty.bootstrap.ServerBootstrap.ServerBootstrapAcceptor.1
                @Override // java.lang.Runnable
                public void run() {
                    channel.config().setAutoRead(true);
                }
            };
        }

        @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            final Channel child = (Channel) msg;
            child.pipeline().addLast(this.childHandler);
            AbstractBootstrap.setChannelOptions(child, this.childOptions, ServerBootstrap.logger);
            AbstractBootstrap.setAttributes(child, this.childAttrs);
            if (!this.extensions.isEmpty()) {
                for (ChannelInitializerExtension extension : this.extensions) {
                    try {
                        extension.postInitializeServerChildChannel(child);
                    } catch (Exception e) {
                        ServerBootstrap.logger.warn("Exception thrown from postInitializeServerChildChannel", (Throwable) e);
                    }
                }
            }
            try {
                this.childGroup.register(child).addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.bootstrap.ServerBootstrap.ServerBootstrapAcceptor.2
                    @Override // io.netty.util.concurrent.GenericFutureListener
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (!future.isSuccess()) {
                            ServerBootstrapAcceptor.forceClose(child, future.cause());
                        }
                    }
                });
            } catch (Throwable t) {
                forceClose(child, t);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void forceClose(Channel child, Throwable t) {
            child.unsafe().closeForcibly();
            ServerBootstrap.logger.warn("Failed to register an accepted channel: {}", child, t);
        }

        @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler, io.netty.channel.ChannelInboundHandler
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ChannelConfig config = ctx.channel().config();
            if (config.isAutoRead()) {
                config.setAutoRead(false);
                ctx.channel().eventLoop().schedule(this.enableAutoReadTask, 1L, TimeUnit.SECONDS);
            }
            ctx.fireExceptionCaught(cause);
        }
    }

    @Override // io.netty.bootstrap.AbstractBootstrap
    /* renamed from: clone, reason: avoid collision after fix types in other method */
    public ServerBootstrap mo213clone() {
        return new ServerBootstrap(this);
    }

    @Deprecated
    public EventLoopGroup childGroup() {
        return this.childGroup;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final ChannelHandler childHandler() {
        return this.childHandler;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Map<ChannelOption<?>, Object> childOptions() {
        Map<ChannelOption<?>, Object> copiedMap;
        synchronized (this.childOptions) {
            copiedMap = copiedMap(this.childOptions);
        }
        return copiedMap;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Map<AttributeKey<?>, Object> childAttrs() {
        return copiedMap(this.childAttrs);
    }

    @Override // io.netty.bootstrap.AbstractBootstrap
    public final AbstractBootstrapConfig<ServerBootstrap, ServerChannel> config() {
        return this.config;
    }
}
