package io.netty.channel;

import androidx.core.app.NotificationCompat;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.AbstractChannel;
import io.netty.channel.DefaultChannelPipeline;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ResourceLeakHint;
import io.netty.util.concurrent.AbstractEventExecutor;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.OrderedEventExecutor;
import io.netty.util.internal.ObjectPool;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PromiseNotificationUtil;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.ThrowableUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.SocketAddress;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public abstract class AbstractChannelHandlerContext implements ChannelHandlerContext, ResourceLeakHint {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int ADD_COMPLETE = 2;
    private static final int ADD_PENDING = 1;
    private static final int INIT = 0;
    private static final int REMOVE_COMPLETE = 3;
    private final int executionMask;
    final EventExecutor executor;
    private volatile int handlerState = 0;
    private Tasks invokeTasks;
    private final String name;
    volatile AbstractChannelHandlerContext next;
    private final boolean ordered;
    private final DefaultChannelPipeline pipeline;
    volatile AbstractChannelHandlerContext prev;
    private ChannelFuture succeededFuture;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) AbstractChannelHandlerContext.class);
    private static final AtomicIntegerFieldUpdater<AbstractChannelHandlerContext> HANDLER_STATE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(AbstractChannelHandlerContext.class, "handlerState");

    /* JADX INFO: Access modifiers changed from: package-private */
    public AbstractChannelHandlerContext(DefaultChannelPipeline pipeline, EventExecutor executor, String name, Class<? extends ChannelHandler> handlerClass) {
        this.name = (String) ObjectUtil.checkNotNull(name, "name");
        this.pipeline = pipeline;
        this.executor = executor;
        this.executionMask = ChannelHandlerMask.mask(handlerClass);
        this.ordered = executor == null || (executor instanceof OrderedEventExecutor);
    }

    @Override // io.netty.channel.ChannelHandlerContext
    public Channel channel() {
        return this.pipeline.channel();
    }

    @Override // io.netty.channel.ChannelHandlerContext
    public ChannelPipeline pipeline() {
        return this.pipeline;
    }

    @Override // io.netty.channel.ChannelHandlerContext
    public ByteBufAllocator alloc() {
        return channel().config().getAllocator();
    }

    @Override // io.netty.channel.ChannelHandlerContext
    public EventExecutor executor() {
        if (this.executor == null) {
            return channel().eventLoop();
        }
        return this.executor;
    }

    @Override // io.netty.channel.ChannelHandlerContext
    public String name() {
        return this.name;
    }

    @Override // io.netty.channel.ChannelInboundInvoker
    public ChannelHandlerContext fireChannelRegistered() {
        invokeChannelRegistered(findContextInbound(2));
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void invokeChannelRegistered(AbstractChannelHandlerContext next) {
        EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeChannelRegistered();
        } else {
            executor.execute(new Runnable() { // from class: io.netty.channel.AbstractChannelHandlerContext.1
                @Override // java.lang.Runnable
                public void run() {
                    AbstractChannelHandlerContext.this.invokeChannelRegistered();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invokeChannelRegistered() {
        if (invokeHandler()) {
            try {
                ChannelHandler handler = handler();
                DefaultChannelPipeline.HeadContext headContext = this.pipeline.head;
                if (handler == headContext) {
                    headContext.channelRegistered(this);
                } else if (handler instanceof ChannelInboundHandlerAdapter) {
                    ((ChannelInboundHandlerAdapter) handler).channelRegistered(this);
                } else {
                    ((ChannelInboundHandler) handler).channelRegistered(this);
                }
                return;
            } catch (Throwable t) {
                invokeExceptionCaught(t);
                return;
            }
        }
        fireChannelRegistered();
    }

    @Override // io.netty.channel.ChannelInboundInvoker
    public ChannelHandlerContext fireChannelUnregistered() {
        invokeChannelUnregistered(findContextInbound(4));
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void invokeChannelUnregistered(AbstractChannelHandlerContext next) {
        EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeChannelUnregistered();
        } else {
            executor.execute(new Runnable() { // from class: io.netty.channel.AbstractChannelHandlerContext.2
                @Override // java.lang.Runnable
                public void run() {
                    AbstractChannelHandlerContext.this.invokeChannelUnregistered();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invokeChannelUnregistered() {
        if (invokeHandler()) {
            try {
                ChannelHandler handler = handler();
                DefaultChannelPipeline.HeadContext headContext = this.pipeline.head;
                if (handler == headContext) {
                    headContext.channelUnregistered(this);
                } else if (handler instanceof ChannelInboundHandlerAdapter) {
                    ((ChannelInboundHandlerAdapter) handler).channelUnregistered(this);
                } else {
                    ((ChannelInboundHandler) handler).channelUnregistered(this);
                }
                return;
            } catch (Throwable t) {
                invokeExceptionCaught(t);
                return;
            }
        }
        fireChannelUnregistered();
    }

    @Override // io.netty.channel.ChannelInboundInvoker
    public ChannelHandlerContext fireChannelActive() {
        invokeChannelActive(findContextInbound(8));
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void invokeChannelActive(AbstractChannelHandlerContext next) {
        EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeChannelActive();
        } else {
            executor.execute(new Runnable() { // from class: io.netty.channel.AbstractChannelHandlerContext.3
                @Override // java.lang.Runnable
                public void run() {
                    AbstractChannelHandlerContext.this.invokeChannelActive();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invokeChannelActive() {
        if (invokeHandler()) {
            try {
                ChannelHandler handler = handler();
                DefaultChannelPipeline.HeadContext headContext = this.pipeline.head;
                if (handler == headContext) {
                    headContext.channelActive(this);
                } else if (handler instanceof ChannelInboundHandlerAdapter) {
                    ((ChannelInboundHandlerAdapter) handler).channelActive(this);
                } else {
                    ((ChannelInboundHandler) handler).channelActive(this);
                }
                return;
            } catch (Throwable t) {
                invokeExceptionCaught(t);
                return;
            }
        }
        fireChannelActive();
    }

    @Override // io.netty.channel.ChannelInboundInvoker
    public ChannelHandlerContext fireChannelInactive() {
        invokeChannelInactive(findContextInbound(16));
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void invokeChannelInactive(AbstractChannelHandlerContext next) {
        EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeChannelInactive();
        } else {
            executor.execute(new Runnable() { // from class: io.netty.channel.AbstractChannelHandlerContext.4
                @Override // java.lang.Runnable
                public void run() {
                    AbstractChannelHandlerContext.this.invokeChannelInactive();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invokeChannelInactive() {
        if (invokeHandler()) {
            try {
                ChannelHandler handler = handler();
                DefaultChannelPipeline.HeadContext headContext = this.pipeline.head;
                if (handler == headContext) {
                    headContext.channelInactive(this);
                } else if (handler instanceof ChannelInboundHandlerAdapter) {
                    ((ChannelInboundHandlerAdapter) handler).channelInactive(this);
                } else {
                    ((ChannelInboundHandler) handler).channelInactive(this);
                }
                return;
            } catch (Throwable t) {
                invokeExceptionCaught(t);
                return;
            }
        }
        fireChannelInactive();
    }

    @Override // io.netty.channel.ChannelInboundInvoker
    public ChannelHandlerContext fireExceptionCaught(Throwable cause) {
        invokeExceptionCaught(findContextInbound(1), cause);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void invokeExceptionCaught(AbstractChannelHandlerContext next, final Throwable cause) {
        ObjectUtil.checkNotNull(cause, "cause");
        EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeExceptionCaught(cause);
            return;
        }
        try {
            executor.execute(new Runnable() { // from class: io.netty.channel.AbstractChannelHandlerContext.5
                @Override // java.lang.Runnable
                public void run() {
                    AbstractChannelHandlerContext.this.invokeExceptionCaught(cause);
                }
            });
        } catch (Throwable t) {
            if (logger.isWarnEnabled()) {
                logger.warn("Failed to submit an exceptionCaught() event.", t);
                logger.warn("The exceptionCaught() event that was failed to submit was:", cause);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invokeExceptionCaught(Throwable cause) {
        if (invokeHandler()) {
            try {
                handler().exceptionCaught(this, cause);
                return;
            } catch (Throwable error) {
                if (logger.isDebugEnabled()) {
                    logger.debug("An exception {}was thrown by a user handler's exceptionCaught() method while handling the following exception:", ThrowableUtil.stackTraceToString(error), cause);
                    return;
                } else {
                    if (logger.isWarnEnabled()) {
                        logger.warn("An exception '{}' [enable DEBUG level for full stacktrace] was thrown by a user handler's exceptionCaught() method while handling the following exception:", error, cause);
                        return;
                    }
                    return;
                }
            }
        }
        fireExceptionCaught(cause);
    }

    @Override // io.netty.channel.ChannelInboundInvoker
    public ChannelHandlerContext fireUserEventTriggered(Object event) {
        invokeUserEventTriggered(findContextInbound(128), event);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void invokeUserEventTriggered(AbstractChannelHandlerContext next, final Object event) {
        ObjectUtil.checkNotNull(event, NotificationCompat.CATEGORY_EVENT);
        EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeUserEventTriggered(event);
        } else {
            executor.execute(new Runnable() { // from class: io.netty.channel.AbstractChannelHandlerContext.6
                @Override // java.lang.Runnable
                public void run() {
                    AbstractChannelHandlerContext.this.invokeUserEventTriggered(event);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invokeUserEventTriggered(Object event) {
        if (invokeHandler()) {
            try {
                ChannelHandler handler = handler();
                DefaultChannelPipeline.HeadContext headContext = this.pipeline.head;
                if (handler == headContext) {
                    headContext.userEventTriggered(this, event);
                } else if (handler instanceof ChannelInboundHandlerAdapter) {
                    ((ChannelInboundHandlerAdapter) handler).userEventTriggered(this, event);
                } else {
                    ((ChannelInboundHandler) handler).userEventTriggered(this, event);
                }
                return;
            } catch (Throwable t) {
                invokeExceptionCaught(t);
                return;
            }
        }
        fireUserEventTriggered(event);
    }

    @Override // io.netty.channel.ChannelInboundInvoker
    public ChannelHandlerContext fireChannelRead(Object msg) {
        invokeChannelRead(findContextInbound(32), msg);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void invokeChannelRead(AbstractChannelHandlerContext next, Object msg) {
        final Object m = next.pipeline.touch(ObjectUtil.checkNotNull(msg, NotificationCompat.CATEGORY_MESSAGE), next);
        EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeChannelRead(m);
        } else {
            executor.execute(new Runnable() { // from class: io.netty.channel.AbstractChannelHandlerContext.7
                @Override // java.lang.Runnable
                public void run() {
                    AbstractChannelHandlerContext.this.invokeChannelRead(m);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invokeChannelRead(Object msg) {
        if (invokeHandler()) {
            try {
                ChannelHandler handler = handler();
                DefaultChannelPipeline.HeadContext headContext = this.pipeline.head;
                if (handler == headContext) {
                    headContext.channelRead(this, msg);
                } else if (handler instanceof ChannelDuplexHandler) {
                    ((ChannelDuplexHandler) handler).channelRead(this, msg);
                } else {
                    ((ChannelInboundHandler) handler).channelRead(this, msg);
                }
                return;
            } catch (Throwable t) {
                invokeExceptionCaught(t);
                return;
            }
        }
        fireChannelRead(msg);
    }

    @Override // io.netty.channel.ChannelInboundInvoker
    public ChannelHandlerContext fireChannelReadComplete() {
        invokeChannelReadComplete(findContextInbound(64));
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void invokeChannelReadComplete(AbstractChannelHandlerContext next) {
        EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeChannelReadComplete();
            return;
        }
        Tasks tasks = next.invokeTasks;
        if (tasks == null) {
            Tasks tasks2 = new Tasks(next);
            tasks = tasks2;
            next.invokeTasks = tasks2;
        }
        executor.execute(tasks.invokeChannelReadCompleteTask);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invokeChannelReadComplete() {
        if (invokeHandler()) {
            try {
                ChannelHandler handler = handler();
                DefaultChannelPipeline.HeadContext headContext = this.pipeline.head;
                if (handler == headContext) {
                    headContext.channelReadComplete(this);
                } else if (handler instanceof ChannelDuplexHandler) {
                    ((ChannelDuplexHandler) handler).channelReadComplete(this);
                } else {
                    ((ChannelInboundHandler) handler).channelReadComplete(this);
                }
                return;
            } catch (Throwable t) {
                invokeExceptionCaught(t);
                return;
            }
        }
        fireChannelReadComplete();
    }

    @Override // io.netty.channel.ChannelInboundInvoker
    public ChannelHandlerContext fireChannelWritabilityChanged() {
        invokeChannelWritabilityChanged(findContextInbound(256));
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void invokeChannelWritabilityChanged(AbstractChannelHandlerContext next) {
        EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeChannelWritabilityChanged();
            return;
        }
        Tasks tasks = next.invokeTasks;
        if (tasks == null) {
            Tasks tasks2 = new Tasks(next);
            tasks = tasks2;
            next.invokeTasks = tasks2;
        }
        executor.execute(tasks.invokeChannelWritableStateChangedTask);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invokeChannelWritabilityChanged() {
        if (invokeHandler()) {
            try {
                ChannelHandler handler = handler();
                DefaultChannelPipeline.HeadContext headContext = this.pipeline.head;
                if (handler == headContext) {
                    headContext.channelWritabilityChanged(this);
                } else if (handler instanceof ChannelInboundHandlerAdapter) {
                    ((ChannelInboundHandlerAdapter) handler).channelWritabilityChanged(this);
                } else {
                    ((ChannelInboundHandler) handler).channelWritabilityChanged(this);
                }
                return;
            } catch (Throwable t) {
                invokeExceptionCaught(t);
                return;
            }
        }
        fireChannelWritabilityChanged();
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture bind(SocketAddress localAddress) {
        return bind(localAddress, newPromise());
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture connect(SocketAddress remoteAddress) {
        return connect(remoteAddress, newPromise());
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress) {
        return connect(remoteAddress, localAddress, newPromise());
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture disconnect() {
        return disconnect(newPromise());
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture close() {
        return close(newPromise());
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture deregister() {
        return deregister(newPromise());
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture bind(final SocketAddress localAddress, final ChannelPromise promise) {
        ObjectUtil.checkNotNull(localAddress, "localAddress");
        if (isNotValidPromise(promise, false)) {
            return promise;
        }
        final AbstractChannelHandlerContext next = findContextOutbound(512);
        EventExecutor executor = next.executor();
        if (!executor.inEventLoop()) {
            safeExecute(executor, new Runnable() { // from class: io.netty.channel.AbstractChannelHandlerContext.8
                @Override // java.lang.Runnable
                public void run() {
                    next.invokeBind(localAddress, promise);
                }
            }, promise, null, false);
        } else {
            next.invokeBind(localAddress, promise);
        }
        return promise;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invokeBind(SocketAddress localAddress, ChannelPromise promise) {
        if (invokeHandler()) {
            try {
                ChannelHandler handler = handler();
                DefaultChannelPipeline.HeadContext headContext = this.pipeline.head;
                if (handler == headContext) {
                    headContext.bind(this, localAddress, promise);
                } else if (handler instanceof ChannelDuplexHandler) {
                    ((ChannelDuplexHandler) handler).bind(this, localAddress, promise);
                } else if (handler instanceof ChannelOutboundHandlerAdapter) {
                    ((ChannelOutboundHandlerAdapter) handler).bind(this, localAddress, promise);
                } else {
                    ((ChannelOutboundHandler) handler).bind(this, localAddress, promise);
                }
                return;
            } catch (Throwable t) {
                notifyOutboundHandlerException(t, promise);
                return;
            }
        }
        bind(localAddress, promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture connect(SocketAddress remoteAddress, ChannelPromise promise) {
        return connect(remoteAddress, null, promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture connect(final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) {
        ObjectUtil.checkNotNull(remoteAddress, "remoteAddress");
        if (isNotValidPromise(promise, false)) {
            return promise;
        }
        final AbstractChannelHandlerContext next = findContextOutbound(1024);
        EventExecutor executor = next.executor();
        if (!executor.inEventLoop()) {
            safeExecute(executor, new Runnable() { // from class: io.netty.channel.AbstractChannelHandlerContext.9
                @Override // java.lang.Runnable
                public void run() {
                    next.invokeConnect(remoteAddress, localAddress, promise);
                }
            }, promise, null, false);
        } else {
            next.invokeConnect(remoteAddress, localAddress, promise);
        }
        return promise;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invokeConnect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
        if (invokeHandler()) {
            try {
                ChannelHandler handler = handler();
                DefaultChannelPipeline.HeadContext headContext = this.pipeline.head;
                if (handler == headContext) {
                    headContext.connect(this, remoteAddress, localAddress, promise);
                } else if (handler instanceof ChannelDuplexHandler) {
                    ((ChannelDuplexHandler) handler).connect(this, remoteAddress, localAddress, promise);
                } else if (handler instanceof ChannelOutboundHandlerAdapter) {
                    ((ChannelOutboundHandlerAdapter) handler).connect(this, remoteAddress, localAddress, promise);
                } else {
                    ((ChannelOutboundHandler) handler).connect(this, remoteAddress, localAddress, promise);
                }
                return;
            } catch (Throwable t) {
                notifyOutboundHandlerException(t, promise);
                return;
            }
        }
        connect(remoteAddress, localAddress, promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture disconnect(final ChannelPromise promise) {
        if (!channel().metadata().hasDisconnect()) {
            return close(promise);
        }
        if (isNotValidPromise(promise, false)) {
            return promise;
        }
        final AbstractChannelHandlerContext next = findContextOutbound(2048);
        EventExecutor executor = next.executor();
        if (!executor.inEventLoop()) {
            safeExecute(executor, new Runnable() { // from class: io.netty.channel.AbstractChannelHandlerContext.10
                @Override // java.lang.Runnable
                public void run() {
                    next.invokeDisconnect(promise);
                }
            }, promise, null, false);
        } else {
            next.invokeDisconnect(promise);
        }
        return promise;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invokeDisconnect(ChannelPromise promise) {
        if (invokeHandler()) {
            try {
                ChannelHandler handler = handler();
                DefaultChannelPipeline.HeadContext headContext = this.pipeline.head;
                if (handler == headContext) {
                    headContext.disconnect(this, promise);
                } else if (handler instanceof ChannelDuplexHandler) {
                    ((ChannelDuplexHandler) handler).disconnect(this, promise);
                } else if (handler instanceof ChannelOutboundHandlerAdapter) {
                    ((ChannelOutboundHandlerAdapter) handler).disconnect(this, promise);
                } else {
                    ((ChannelOutboundHandler) handler).disconnect(this, promise);
                }
                return;
            } catch (Throwable t) {
                notifyOutboundHandlerException(t, promise);
                return;
            }
        }
        disconnect(promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture close(final ChannelPromise promise) {
        if (isNotValidPromise(promise, false)) {
            return promise;
        }
        final AbstractChannelHandlerContext next = findContextOutbound(4096);
        EventExecutor executor = next.executor();
        if (!executor.inEventLoop()) {
            safeExecute(executor, new Runnable() { // from class: io.netty.channel.AbstractChannelHandlerContext.11
                @Override // java.lang.Runnable
                public void run() {
                    next.invokeClose(promise);
                }
            }, promise, null, false);
        } else {
            next.invokeClose(promise);
        }
        return promise;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invokeClose(ChannelPromise promise) {
        if (invokeHandler()) {
            try {
                ChannelHandler handler = handler();
                DefaultChannelPipeline.HeadContext headContext = this.pipeline.head;
                if (handler == headContext) {
                    headContext.close(this, promise);
                } else if (handler instanceof ChannelDuplexHandler) {
                    ((ChannelDuplexHandler) handler).close(this, promise);
                } else if (handler instanceof ChannelOutboundHandlerAdapter) {
                    ((ChannelOutboundHandlerAdapter) handler).close(this, promise);
                } else {
                    ((ChannelOutboundHandler) handler).close(this, promise);
                }
                return;
            } catch (Throwable t) {
                notifyOutboundHandlerException(t, promise);
                return;
            }
        }
        close(promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture deregister(final ChannelPromise promise) {
        if (isNotValidPromise(promise, false)) {
            return promise;
        }
        final AbstractChannelHandlerContext next = findContextOutbound(8192);
        EventExecutor executor = next.executor();
        if (!executor.inEventLoop()) {
            safeExecute(executor, new Runnable() { // from class: io.netty.channel.AbstractChannelHandlerContext.12
                @Override // java.lang.Runnable
                public void run() {
                    next.invokeDeregister(promise);
                }
            }, promise, null, false);
        } else {
            next.invokeDeregister(promise);
        }
        return promise;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invokeDeregister(ChannelPromise promise) {
        if (invokeHandler()) {
            try {
                ChannelHandler handler = handler();
                DefaultChannelPipeline.HeadContext headContext = this.pipeline.head;
                if (handler == headContext) {
                    headContext.deregister(this, promise);
                } else if (handler instanceof ChannelDuplexHandler) {
                    ((ChannelDuplexHandler) handler).deregister(this, promise);
                } else if (handler instanceof ChannelOutboundHandlerAdapter) {
                    ((ChannelOutboundHandlerAdapter) handler).deregister(this, promise);
                } else {
                    ((ChannelOutboundHandler) handler).deregister(this, promise);
                }
                return;
            } catch (Throwable t) {
                notifyOutboundHandlerException(t, promise);
                return;
            }
        }
        deregister(promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelHandlerContext read() {
        AbstractChannelHandlerContext next = findContextOutbound(16384);
        EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeRead();
        } else {
            Tasks tasks = next.invokeTasks;
            if (tasks == null) {
                Tasks tasks2 = new Tasks(next);
                tasks = tasks2;
                next.invokeTasks = tasks2;
            }
            executor.execute(tasks.invokeReadTask);
        }
        return this;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invokeRead() {
        if (invokeHandler()) {
            try {
                ChannelHandler handler = handler();
                DefaultChannelPipeline.HeadContext headContext = this.pipeline.head;
                if (handler == headContext) {
                    headContext.read(this);
                } else if (handler instanceof ChannelDuplexHandler) {
                    ((ChannelDuplexHandler) handler).read(this);
                } else if (handler instanceof ChannelOutboundHandlerAdapter) {
                    ((ChannelOutboundHandlerAdapter) handler).read(this);
                } else {
                    ((ChannelOutboundHandler) handler).read(this);
                }
                return;
            } catch (Throwable t) {
                invokeExceptionCaught(t);
                return;
            }
        }
        read();
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture write(Object msg) {
        return write(msg, newPromise());
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture write(Object msg, ChannelPromise promise) {
        write(msg, false, promise);
        return promise;
    }

    void invokeWrite(Object msg, ChannelPromise promise) {
        if (invokeHandler()) {
            invokeWrite0(msg, promise);
        } else {
            write(msg, promise);
        }
    }

    private void invokeWrite0(Object msg, ChannelPromise promise) {
        try {
            ChannelHandler handler = handler();
            DefaultChannelPipeline.HeadContext headContext = this.pipeline.head;
            if (handler == headContext) {
                headContext.write(this, msg, promise);
            } else if (handler instanceof ChannelDuplexHandler) {
                ((ChannelDuplexHandler) handler).write(this, msg, promise);
            } else if (handler instanceof ChannelOutboundHandlerAdapter) {
                ((ChannelOutboundHandlerAdapter) handler).write(this, msg, promise);
            } else {
                ((ChannelOutboundHandler) handler).write(this, msg, promise);
            }
        } catch (Throwable t) {
            notifyOutboundHandlerException(t, promise);
        }
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelHandlerContext flush() {
        AbstractChannelHandlerContext next = findContextOutbound(65536);
        EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeFlush();
        } else {
            Tasks tasks = next.invokeTasks;
            if (tasks == null) {
                Tasks tasks2 = new Tasks(next);
                tasks = tasks2;
                next.invokeTasks = tasks2;
            }
            safeExecute(executor, tasks.invokeFlushTask, channel().voidPromise(), null, false);
        }
        return this;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invokeFlush() {
        if (invokeHandler()) {
            invokeFlush0();
        } else {
            flush();
        }
    }

    private void invokeFlush0() {
        try {
            ChannelHandler handler = handler();
            DefaultChannelPipeline.HeadContext headContext = this.pipeline.head;
            if (handler == headContext) {
                headContext.flush(this);
            } else if (handler instanceof ChannelDuplexHandler) {
                ((ChannelDuplexHandler) handler).flush(this);
            } else if (handler instanceof ChannelOutboundHandlerAdapter) {
                ((ChannelOutboundHandlerAdapter) handler).flush(this);
            } else {
                ((ChannelOutboundHandler) handler).flush(this);
            }
        } catch (Throwable t) {
            invokeExceptionCaught(t);
        }
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture writeAndFlush(Object msg, ChannelPromise promise) {
        write(msg, true, promise);
        return promise;
    }

    void invokeWriteAndFlush(Object msg, ChannelPromise promise) {
        if (invokeHandler()) {
            invokeWrite0(msg, promise);
            invokeFlush0();
        } else {
            writeAndFlush(msg, promise);
        }
    }

    private void write(Object msg, boolean flush, ChannelPromise promise) {
        ObjectUtil.checkNotNull(msg, NotificationCompat.CATEGORY_MESSAGE);
        try {
            if (isNotValidPromise(promise, true)) {
                ReferenceCountUtil.release(msg);
                return;
            }
            AbstractChannelHandlerContext next = findContextOutbound(flush ? 98304 : 32768);
            Object m = this.pipeline.touch(msg, next);
            EventExecutor executor = next.executor();
            if (executor.inEventLoop()) {
                if (flush) {
                    next.invokeWriteAndFlush(m, promise);
                    return;
                } else {
                    next.invokeWrite(m, promise);
                    return;
                }
            }
            WriteTask task = WriteTask.newInstance(next, m, promise, flush);
            if (!safeExecute(executor, task, promise, m, !flush)) {
                task.cancel();
            }
        } catch (RuntimeException e) {
            ReferenceCountUtil.release(msg);
            throw e;
        }
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture writeAndFlush(Object msg) {
        return writeAndFlush(msg, newPromise());
    }

    private static void notifyOutboundHandlerException(Throwable cause, ChannelPromise promise) {
        PromiseNotificationUtil.tryFailure(promise, cause, promise instanceof VoidChannelPromise ? null : logger);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelPromise newPromise() {
        return new DefaultChannelPromise(channel(), executor());
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelProgressivePromise newProgressivePromise() {
        return new DefaultChannelProgressivePromise(channel(), executor());
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture newSucceededFuture() {
        ChannelFuture succeededFuture = this.succeededFuture;
        if (succeededFuture == null) {
            ChannelFuture succeededFuture2 = new SucceededChannelFuture(channel(), executor());
            this.succeededFuture = succeededFuture2;
            return succeededFuture2;
        }
        return succeededFuture;
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture newFailedFuture(Throwable cause) {
        return new FailedChannelFuture(channel(), executor(), cause);
    }

    private boolean isNotValidPromise(ChannelPromise promise, boolean allowVoidPromise) {
        ObjectUtil.checkNotNull(promise, "promise");
        if (promise.isDone()) {
            if (promise.isCancelled()) {
                return true;
            }
            throw new IllegalArgumentException("promise already done: " + promise);
        }
        if (promise.channel() != channel()) {
            throw new IllegalArgumentException(String.format("promise.channel does not match: %s (expected: %s)", promise.channel(), channel()));
        }
        if (promise.getClass() == DefaultChannelPromise.class) {
            return false;
        }
        if (!allowVoidPromise && (promise instanceof VoidChannelPromise)) {
            throw new IllegalArgumentException(StringUtil.simpleClassName((Class<?>) VoidChannelPromise.class) + " not allowed for this operation");
        }
        if (promise instanceof AbstractChannel.CloseFuture) {
            throw new IllegalArgumentException(StringUtil.simpleClassName((Class<?>) AbstractChannel.CloseFuture.class) + " not allowed in a pipeline");
        }
        return false;
    }

    private AbstractChannelHandlerContext findContextInbound(int mask) {
        AbstractChannelHandlerContext ctx = this;
        EventExecutor currentExecutor = executor();
        do {
            ctx = ctx.next;
        } while (skipContext(ctx, currentExecutor, mask, 510));
        return ctx;
    }

    private AbstractChannelHandlerContext findContextOutbound(int mask) {
        AbstractChannelHandlerContext ctx = this;
        EventExecutor currentExecutor = executor();
        do {
            ctx = ctx.prev;
        } while (skipContext(ctx, currentExecutor, mask, 130560));
        return ctx;
    }

    private static boolean skipContext(AbstractChannelHandlerContext ctx, EventExecutor currentExecutor, int mask, int onlyMask) {
        return (ctx.executionMask & (onlyMask | mask)) == 0 || (ctx.executor() == currentExecutor && (ctx.executionMask & mask) == 0);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelPromise voidPromise() {
        return channel().voidPromise();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void setRemoved() {
        this.handlerState = 3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean setAddComplete() {
        int oldState;
        do {
            oldState = this.handlerState;
            if (oldState == 3) {
                return false;
            }
        } while (!HANDLER_STATE_UPDATER.compareAndSet(this, oldState, 2));
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void setAddPending() {
        boolean updated = HANDLER_STATE_UPDATER.compareAndSet(this, 0, 1);
        if (!updated) {
            throw new AssertionError();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void callHandlerAdded() throws Exception {
        if (setAddComplete()) {
            handler().handlerAdded(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void callHandlerRemoved() throws Exception {
        try {
            if (this.handlerState == 2) {
                handler().handlerRemoved(this);
            }
        } finally {
            setRemoved();
        }
    }

    private boolean invokeHandler() {
        int handlerState = this.handlerState;
        if (handlerState != 2) {
            return !this.ordered && handlerState == 1;
        }
        return true;
    }

    @Override // io.netty.channel.ChannelHandlerContext
    public boolean isRemoved() {
        return this.handlerState == 3;
    }

    @Override // io.netty.channel.ChannelHandlerContext, io.netty.util.AttributeMap
    public <T> Attribute<T> attr(AttributeKey<T> key) {
        return channel().attr(key);
    }

    @Override // io.netty.channel.ChannelHandlerContext, io.netty.util.AttributeMap
    public <T> boolean hasAttr(AttributeKey<T> key) {
        return channel().hasAttr(key);
    }

    private static boolean safeExecute(EventExecutor executor, Runnable runnable, ChannelPromise promise, Object msg, boolean lazy) {
        if (lazy) {
            try {
                if (executor instanceof AbstractEventExecutor) {
                    ((AbstractEventExecutor) executor).lazyExecute(runnable);
                    return true;
                }
            } catch (Throwable cause) {
                if (msg != null) {
                    try {
                        ReferenceCountUtil.release(msg);
                    } catch (Throwable th) {
                        promise.setFailure(cause);
                        throw th;
                    }
                }
                promise.setFailure(cause);
                return false;
            }
        }
        executor.execute(runnable);
        return true;
    }

    @Override // io.netty.util.ResourceLeakHint
    public String toHintString() {
        return '\'' + this.name + "' will handle the message from this point.";
    }

    public String toString() {
        return StringUtil.simpleClassName((Class<?>) ChannelHandlerContext.class) + '(' + this.name + ", " + channel() + ')';
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static final class WriteTask implements Runnable {
        private AbstractChannelHandlerContext ctx;
        private final ObjectPool.Handle<WriteTask> handle;
        private Object msg;
        private ChannelPromise promise;
        private int size;
        private static final ObjectPool<WriteTask> RECYCLER = ObjectPool.newPool(new ObjectPool.ObjectCreator<WriteTask>() { // from class: io.netty.channel.AbstractChannelHandlerContext.WriteTask.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // io.netty.util.internal.ObjectPool.ObjectCreator
            public WriteTask newObject(ObjectPool.Handle<WriteTask> handle) {
                return new WriteTask(handle);
            }
        });
        private static final boolean ESTIMATE_TASK_SIZE_ON_SUBMIT = SystemPropertyUtil.getBoolean("io.netty.transport.estimateSizeOnSubmit", true);
        private static final int WRITE_TASK_OVERHEAD = SystemPropertyUtil.getInt("io.netty.transport.writeTaskSizeOverhead", 32);

        static WriteTask newInstance(AbstractChannelHandlerContext ctx, Object msg, ChannelPromise promise, boolean flush) {
            WriteTask task = RECYCLER.get();
            init(task, ctx, msg, promise, flush);
            return task;
        }

        /* JADX WARN: Multi-variable type inference failed */
        private WriteTask(ObjectPool.Handle<? extends WriteTask> handle) {
            this.handle = handle;
        }

        protected static void init(WriteTask task, AbstractChannelHandlerContext ctx, Object msg, ChannelPromise promise, boolean flush) {
            task.ctx = ctx;
            task.msg = msg;
            task.promise = promise;
            if (ESTIMATE_TASK_SIZE_ON_SUBMIT) {
                task.size = ctx.pipeline.estimatorHandle().size(msg) + WRITE_TASK_OVERHEAD;
                ctx.pipeline.incrementPendingOutboundBytes(task.size);
            } else {
                task.size = 0;
            }
            if (flush) {
                task.size |= Integer.MIN_VALUE;
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                decrementPendingOutboundBytes();
                if (this.size >= 0) {
                    this.ctx.invokeWrite(this.msg, this.promise);
                } else {
                    this.ctx.invokeWriteAndFlush(this.msg, this.promise);
                }
            } finally {
                recycle();
            }
        }

        void cancel() {
            try {
                decrementPendingOutboundBytes();
            } finally {
                recycle();
            }
        }

        private void decrementPendingOutboundBytes() {
            if (ESTIMATE_TASK_SIZE_ON_SUBMIT) {
                this.ctx.pipeline.decrementPendingOutboundBytes(this.size & Integer.MAX_VALUE);
            }
        }

        private void recycle() {
            this.ctx = null;
            this.msg = null;
            this.promise = null;
            this.handle.recycle(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class Tasks {
        private final AbstractChannelHandlerContext next;
        private final Runnable invokeChannelReadCompleteTask = new Runnable() { // from class: io.netty.channel.AbstractChannelHandlerContext.Tasks.1
            @Override // java.lang.Runnable
            public void run() {
                Tasks.this.next.invokeChannelReadComplete();
            }
        };
        private final Runnable invokeReadTask = new Runnable() { // from class: io.netty.channel.AbstractChannelHandlerContext.Tasks.2
            @Override // java.lang.Runnable
            public void run() {
                Tasks.this.next.invokeRead();
            }
        };
        private final Runnable invokeChannelWritableStateChangedTask = new Runnable() { // from class: io.netty.channel.AbstractChannelHandlerContext.Tasks.3
            @Override // java.lang.Runnable
            public void run() {
                Tasks.this.next.invokeChannelWritabilityChanged();
            }
        };
        private final Runnable invokeFlushTask = new Runnable() { // from class: io.netty.channel.AbstractChannelHandlerContext.Tasks.4
            @Override // java.lang.Runnable
            public void run() {
                Tasks.this.next.invokeFlush();
            }
        };

        Tasks(AbstractChannelHandlerContext next) {
            this.next = next;
        }
    }
}
