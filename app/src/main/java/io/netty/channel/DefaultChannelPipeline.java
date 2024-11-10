package io.netty.channel;

import androidx.concurrent.futures.AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0;
import io.netty.channel.Channel;
import io.netty.channel.MessageSizeEstimator;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.WeakHashMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/* loaded from: classes4.dex */
public class DefaultChannelPipeline implements ChannelPipeline {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Channel channel;
    private Map<EventExecutorGroup, EventExecutor> childExecutors;
    private volatile MessageSizeEstimator.Handle estimatorHandle;
    private PendingHandlerCallback pendingHandlerCallbackHead;
    private boolean registered;
    private final ChannelFuture succeededFuture;
    private final VoidChannelPromise voidPromise;
    static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) DefaultChannelPipeline.class);
    private static final String HEAD_NAME = generateName0(HeadContext.class);
    private static final String TAIL_NAME = generateName0(TailContext.class);
    private static final FastThreadLocal<Map<Class<?>, String>> nameCaches = new FastThreadLocal<Map<Class<?>, String>>() { // from class: io.netty.channel.DefaultChannelPipeline.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // io.netty.util.concurrent.FastThreadLocal
        public Map<Class<?>, String> initialValue() {
            return new WeakHashMap();
        }
    };
    private static final AtomicReferenceFieldUpdater<DefaultChannelPipeline, MessageSizeEstimator.Handle> ESTIMATOR = AtomicReferenceFieldUpdater.newUpdater(DefaultChannelPipeline.class, MessageSizeEstimator.Handle.class, "estimatorHandle");
    private final boolean touch = ResourceLeakDetector.isEnabled();
    private boolean firstRegistration = true;
    final TailContext tail = new TailContext(this);
    final HeadContext head = new HeadContext(this);

    /* JADX INFO: Access modifiers changed from: protected */
    public DefaultChannelPipeline(Channel channel) {
        this.channel = (Channel) ObjectUtil.checkNotNull(channel, "channel");
        this.succeededFuture = new SucceededChannelFuture(channel, null);
        this.voidPromise = new VoidChannelPromise(channel, true);
        this.head.next = this.tail;
        this.tail.prev = this.head;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final MessageSizeEstimator.Handle estimatorHandle() {
        MessageSizeEstimator.Handle handle = this.estimatorHandle;
        if (handle == null) {
            MessageSizeEstimator.Handle handle2 = this.channel.config().getMessageSizeEstimator().newHandle();
            if (!AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(ESTIMATOR, this, null, handle2)) {
                return this.estimatorHandle;
            }
            return handle2;
        }
        return handle;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Object touch(Object msg, AbstractChannelHandlerContext next) {
        return this.touch ? ReferenceCountUtil.touch(msg, next) : msg;
    }

    private AbstractChannelHandlerContext newContext(EventExecutorGroup group, String name, ChannelHandler handler) {
        return new DefaultChannelHandlerContext(this, childExecutor(group), name, handler);
    }

    private EventExecutor childExecutor(EventExecutorGroup group) {
        if (group == null) {
            return null;
        }
        Boolean pinEventExecutor = (Boolean) this.channel.config().getOption(ChannelOption.SINGLE_EVENTEXECUTOR_PER_GROUP);
        if (pinEventExecutor != null && !pinEventExecutor.booleanValue()) {
            return group.next();
        }
        Map<EventExecutorGroup, EventExecutor> childExecutors = this.childExecutors;
        if (childExecutors == null) {
            IdentityHashMap identityHashMap = new IdentityHashMap(4);
            this.childExecutors = identityHashMap;
            childExecutors = identityHashMap;
        }
        EventExecutor childExecutor = childExecutors.get(group);
        if (childExecutor == null) {
            EventExecutor childExecutor2 = group.next();
            childExecutors.put(group, childExecutor2);
            return childExecutor2;
        }
        return childExecutor;
    }

    @Override // io.netty.channel.ChannelPipeline
    public final Channel channel() {
        return this.channel;
    }

    @Override // io.netty.channel.ChannelPipeline
    public final ChannelPipeline addFirst(String name, ChannelHandler handler) {
        return addFirst(null, name, handler);
    }

    @Override // io.netty.channel.ChannelPipeline
    public final ChannelPipeline addFirst(EventExecutorGroup group, String name, ChannelHandler handler) {
        synchronized (this) {
            checkMultiplicity(handler);
            AbstractChannelHandlerContext newCtx = newContext(group, filterName(name, handler), handler);
            addFirst0(newCtx);
            if (!this.registered) {
                newCtx.setAddPending();
                callHandlerCallbackLater(newCtx, true);
                return this;
            }
            EventExecutor executor = newCtx.executor();
            if (!executor.inEventLoop()) {
                callHandlerAddedInEventLoop(newCtx, executor);
                return this;
            }
            callHandlerAdded0(newCtx);
            return this;
        }
    }

    private void addFirst0(AbstractChannelHandlerContext newCtx) {
        AbstractChannelHandlerContext nextCtx = this.head.next;
        newCtx.prev = this.head;
        newCtx.next = nextCtx;
        this.head.next = newCtx;
        nextCtx.prev = newCtx;
    }

    @Override // io.netty.channel.ChannelPipeline
    public final ChannelPipeline addLast(String name, ChannelHandler handler) {
        return addLast(null, name, handler);
    }

    @Override // io.netty.channel.ChannelPipeline
    public final ChannelPipeline addLast(EventExecutorGroup group, String name, ChannelHandler handler) {
        synchronized (this) {
            checkMultiplicity(handler);
            AbstractChannelHandlerContext newCtx = newContext(group, filterName(name, handler), handler);
            addLast0(newCtx);
            if (!this.registered) {
                newCtx.setAddPending();
                callHandlerCallbackLater(newCtx, true);
                return this;
            }
            EventExecutor executor = newCtx.executor();
            if (!executor.inEventLoop()) {
                callHandlerAddedInEventLoop(newCtx, executor);
                return this;
            }
            callHandlerAdded0(newCtx);
            return this;
        }
    }

    private void addLast0(AbstractChannelHandlerContext newCtx) {
        AbstractChannelHandlerContext prev = this.tail.prev;
        newCtx.prev = prev;
        newCtx.next = this.tail;
        prev.next = newCtx;
        this.tail.prev = newCtx;
    }

    @Override // io.netty.channel.ChannelPipeline
    public final ChannelPipeline addBefore(String baseName, String name, ChannelHandler handler) {
        return addBefore(null, baseName, name, handler);
    }

    @Override // io.netty.channel.ChannelPipeline
    public final ChannelPipeline addBefore(EventExecutorGroup group, String baseName, String name, ChannelHandler handler) {
        synchronized (this) {
            checkMultiplicity(handler);
            String name2 = filterName(name, handler);
            AbstractChannelHandlerContext ctx = getContextOrDie(baseName);
            AbstractChannelHandlerContext newCtx = newContext(group, name2, handler);
            addBefore0(ctx, newCtx);
            if (!this.registered) {
                newCtx.setAddPending();
                callHandlerCallbackLater(newCtx, true);
                return this;
            }
            EventExecutor executor = newCtx.executor();
            if (!executor.inEventLoop()) {
                callHandlerAddedInEventLoop(newCtx, executor);
                return this;
            }
            callHandlerAdded0(newCtx);
            return this;
        }
    }

    private static void addBefore0(AbstractChannelHandlerContext ctx, AbstractChannelHandlerContext newCtx) {
        newCtx.prev = ctx.prev;
        newCtx.next = ctx;
        ctx.prev.next = newCtx;
        ctx.prev = newCtx;
    }

    private String filterName(String name, ChannelHandler handler) {
        if (name == null) {
            return generateName(handler);
        }
        checkDuplicateName(name);
        return name;
    }

    @Override // io.netty.channel.ChannelPipeline
    public final ChannelPipeline addAfter(String baseName, String name, ChannelHandler handler) {
        return addAfter(null, baseName, name, handler);
    }

    @Override // io.netty.channel.ChannelPipeline
    public final ChannelPipeline addAfter(EventExecutorGroup group, String baseName, String name, ChannelHandler handler) {
        synchronized (this) {
            checkMultiplicity(handler);
            String name2 = filterName(name, handler);
            AbstractChannelHandlerContext ctx = getContextOrDie(baseName);
            AbstractChannelHandlerContext newCtx = newContext(group, name2, handler);
            addAfter0(ctx, newCtx);
            if (!this.registered) {
                newCtx.setAddPending();
                callHandlerCallbackLater(newCtx, true);
                return this;
            }
            EventExecutor executor = newCtx.executor();
            if (!executor.inEventLoop()) {
                callHandlerAddedInEventLoop(newCtx, executor);
                return this;
            }
            callHandlerAdded0(newCtx);
            return this;
        }
    }

    private static void addAfter0(AbstractChannelHandlerContext ctx, AbstractChannelHandlerContext newCtx) {
        newCtx.prev = ctx;
        newCtx.next = ctx.next;
        ctx.next.prev = newCtx;
        ctx.next = newCtx;
    }

    public final ChannelPipeline addFirst(ChannelHandler handler) {
        return addFirst((String) null, handler);
    }

    @Override // io.netty.channel.ChannelPipeline
    public final ChannelPipeline addFirst(ChannelHandler... handlers) {
        return addFirst((EventExecutorGroup) null, handlers);
    }

    @Override // io.netty.channel.ChannelPipeline
    public final ChannelPipeline addFirst(EventExecutorGroup executor, ChannelHandler... handlers) {
        ObjectUtil.checkNotNull(handlers, "handlers");
        if (handlers.length == 0 || handlers[0] == null) {
            return this;
        }
        int size = 1;
        while (size < handlers.length && handlers[size] != null) {
            size++;
        }
        for (int i = size - 1; i >= 0; i--) {
            ChannelHandler h = handlers[i];
            addFirst(executor, null, h);
        }
        return this;
    }

    public final ChannelPipeline addLast(ChannelHandler handler) {
        return addLast((String) null, handler);
    }

    @Override // io.netty.channel.ChannelPipeline
    public final ChannelPipeline addLast(ChannelHandler... handlers) {
        return addLast((EventExecutorGroup) null, handlers);
    }

    @Override // io.netty.channel.ChannelPipeline
    public final ChannelPipeline addLast(EventExecutorGroup executor, ChannelHandler... handlers) {
        ObjectUtil.checkNotNull(handlers, "handlers");
        for (ChannelHandler h : handlers) {
            if (h == null) {
                break;
            }
            addLast(executor, null, h);
        }
        return this;
    }

    private String generateName(ChannelHandler handler) {
        Map<Class<?>, String> cache = nameCaches.get();
        Class<?> handlerType = handler.getClass();
        String name = cache.get(handlerType);
        if (name == null) {
            name = generateName0(handlerType);
            cache.put(handlerType, name);
        }
        if (context0(name) != null) {
            String baseName = name.substring(0, name.length() - 1);
            int i = 1;
            while (true) {
                String newName = baseName + i;
                if (context0(newName) != null) {
                    i++;
                } else {
                    return newName;
                }
            }
        } else {
            return name;
        }
    }

    private static String generateName0(Class<?> handlerType) {
        return StringUtil.simpleClassName(handlerType) + "#0";
    }

    @Override // io.netty.channel.ChannelPipeline
    public final ChannelPipeline remove(ChannelHandler handler) {
        remove(getContextOrDie(handler));
        return this;
    }

    @Override // io.netty.channel.ChannelPipeline
    public final ChannelHandler remove(String name) {
        return remove(getContextOrDie(name)).handler();
    }

    @Override // io.netty.channel.ChannelPipeline
    public final <T extends ChannelHandler> T remove(Class<T> cls) {
        return (T) remove(getContextOrDie((Class<? extends ChannelHandler>) cls)).handler();
    }

    public final <T extends ChannelHandler> T removeIfExists(String str) {
        return (T) removeIfExists(context(str));
    }

    public final <T extends ChannelHandler> T removeIfExists(Class<T> cls) {
        return (T) removeIfExists(context((Class<? extends ChannelHandler>) cls));
    }

    public final <T extends ChannelHandler> T removeIfExists(ChannelHandler channelHandler) {
        return (T) removeIfExists(context(channelHandler));
    }

    private <T extends ChannelHandler> T removeIfExists(ChannelHandlerContext channelHandlerContext) {
        if (channelHandlerContext == null) {
            return null;
        }
        return (T) remove((AbstractChannelHandlerContext) channelHandlerContext).handler();
    }

    private AbstractChannelHandlerContext remove(final AbstractChannelHandlerContext ctx) {
        if (ctx == this.head || ctx == this.tail) {
            throw new AssertionError();
        }
        synchronized (this) {
            atomicRemoveFromHandlerList(ctx);
            if (!this.registered) {
                callHandlerCallbackLater(ctx, false);
                return ctx;
            }
            EventExecutor executor = ctx.executor();
            if (!executor.inEventLoop()) {
                executor.execute(new Runnable() { // from class: io.netty.channel.DefaultChannelPipeline.2
                    @Override // java.lang.Runnable
                    public void run() {
                        DefaultChannelPipeline.this.callHandlerRemoved0(ctx);
                    }
                });
                return ctx;
            }
            callHandlerRemoved0(ctx);
            return ctx;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void atomicRemoveFromHandlerList(AbstractChannelHandlerContext ctx) {
        AbstractChannelHandlerContext prev = ctx.prev;
        AbstractChannelHandlerContext next = ctx.next;
        prev.next = next;
        next.prev = prev;
    }

    @Override // io.netty.channel.ChannelPipeline
    public final ChannelHandler removeFirst() {
        if (this.head.next == this.tail) {
            throw new NoSuchElementException();
        }
        return remove(this.head.next).handler();
    }

    @Override // io.netty.channel.ChannelPipeline
    public final ChannelHandler removeLast() {
        if (this.head.next == this.tail) {
            throw new NoSuchElementException();
        }
        return remove(this.tail.prev).handler();
    }

    @Override // io.netty.channel.ChannelPipeline
    public final ChannelPipeline replace(ChannelHandler oldHandler, String newName, ChannelHandler newHandler) {
        replace(getContextOrDie(oldHandler), newName, newHandler);
        return this;
    }

    @Override // io.netty.channel.ChannelPipeline
    public final ChannelHandler replace(String oldName, String newName, ChannelHandler newHandler) {
        return replace(getContextOrDie(oldName), newName, newHandler);
    }

    @Override // io.netty.channel.ChannelPipeline
    public final <T extends ChannelHandler> T replace(Class<T> cls, String str, ChannelHandler channelHandler) {
        return (T) replace(getContextOrDie((Class<? extends ChannelHandler>) cls), str, channelHandler);
    }

    private ChannelHandler replace(final AbstractChannelHandlerContext ctx, String newName, ChannelHandler newHandler) {
        if (ctx == this.head || ctx == this.tail) {
            throw new AssertionError();
        }
        synchronized (this) {
            checkMultiplicity(newHandler);
            if (newName == null) {
                newName = generateName(newHandler);
            } else {
                boolean sameName = ctx.name().equals(newName);
                if (!sameName) {
                    checkDuplicateName(newName);
                }
            }
            final AbstractChannelHandlerContext newCtx = newContext(ctx.executor, newName, newHandler);
            replace0(ctx, newCtx);
            if (!this.registered) {
                callHandlerCallbackLater(newCtx, true);
                callHandlerCallbackLater(ctx, false);
                return ctx.handler();
            }
            EventExecutor executor = ctx.executor();
            if (!executor.inEventLoop()) {
                executor.execute(new Runnable() { // from class: io.netty.channel.DefaultChannelPipeline.3
                    @Override // java.lang.Runnable
                    public void run() {
                        DefaultChannelPipeline.this.callHandlerAdded0(newCtx);
                        DefaultChannelPipeline.this.callHandlerRemoved0(ctx);
                    }
                });
                return ctx.handler();
            }
            callHandlerAdded0(newCtx);
            callHandlerRemoved0(ctx);
            return ctx.handler();
        }
    }

    private static void replace0(AbstractChannelHandlerContext oldCtx, AbstractChannelHandlerContext newCtx) {
        AbstractChannelHandlerContext prev = oldCtx.prev;
        AbstractChannelHandlerContext next = oldCtx.next;
        newCtx.prev = prev;
        newCtx.next = next;
        prev.next = newCtx;
        next.prev = newCtx;
        oldCtx.prev = newCtx;
        oldCtx.next = newCtx;
    }

    private static void checkMultiplicity(ChannelHandler handler) {
        if (handler instanceof ChannelHandlerAdapter) {
            ChannelHandlerAdapter h = (ChannelHandlerAdapter) handler;
            if (!h.isSharable() && h.added) {
                throw new ChannelPipelineException(h.getClass().getName() + " is not a @Sharable handler, so can't be added or removed multiple times.");
            }
            h.added = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void callHandlerAdded0(AbstractChannelHandlerContext ctx) {
        try {
            ctx.callHandlerAdded();
        } catch (Throwable t) {
            boolean removed = false;
            try {
                atomicRemoveFromHandlerList(ctx);
                ctx.callHandlerRemoved();
                removed = true;
            } catch (Throwable t2) {
                if (logger.isWarnEnabled()) {
                    logger.warn("Failed to remove a handler: " + ctx.name(), t2);
                }
            }
            if (removed) {
                fireExceptionCaught((Throwable) new ChannelPipelineException(ctx.handler().getClass().getName() + ".handlerAdded() has thrown an exception; removed.", t));
            } else {
                fireExceptionCaught((Throwable) new ChannelPipelineException(ctx.handler().getClass().getName() + ".handlerAdded() has thrown an exception; also failed to remove.", t));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void callHandlerRemoved0(AbstractChannelHandlerContext ctx) {
        try {
            ctx.callHandlerRemoved();
        } catch (Throwable t) {
            fireExceptionCaught((Throwable) new ChannelPipelineException(ctx.handler().getClass().getName() + ".handlerRemoved() has thrown an exception.", t));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void invokeHandlerAddedIfNeeded() {
        if (!this.channel.eventLoop().inEventLoop()) {
            throw new AssertionError();
        }
        if (this.firstRegistration) {
            this.firstRegistration = false;
            callHandlerAddedForAllHandlers();
        }
    }

    @Override // io.netty.channel.ChannelPipeline
    public final ChannelHandler first() {
        ChannelHandlerContext first = firstContext();
        if (first == null) {
            return null;
        }
        return first.handler();
    }

    @Override // io.netty.channel.ChannelPipeline
    public final ChannelHandlerContext firstContext() {
        AbstractChannelHandlerContext first = this.head.next;
        if (first == this.tail) {
            return null;
        }
        return this.head.next;
    }

    @Override // io.netty.channel.ChannelPipeline
    public final ChannelHandler last() {
        AbstractChannelHandlerContext last = this.tail.prev;
        if (last == this.head) {
            return null;
        }
        return last.handler();
    }

    @Override // io.netty.channel.ChannelPipeline
    public final ChannelHandlerContext lastContext() {
        AbstractChannelHandlerContext last = this.tail.prev;
        if (last == this.head) {
            return null;
        }
        return last;
    }

    @Override // io.netty.channel.ChannelPipeline
    public final ChannelHandler get(String name) {
        ChannelHandlerContext ctx = context(name);
        if (ctx == null) {
            return null;
        }
        return ctx.handler();
    }

    @Override // io.netty.channel.ChannelPipeline
    public final <T extends ChannelHandler> T get(Class<T> cls) {
        ChannelHandlerContext context = context((Class<? extends ChannelHandler>) cls);
        if (context == null) {
            return null;
        }
        return (T) context.handler();
    }

    @Override // io.netty.channel.ChannelPipeline
    public final ChannelHandlerContext context(String name) {
        return context0((String) ObjectUtil.checkNotNull(name, "name"));
    }

    @Override // io.netty.channel.ChannelPipeline
    public final ChannelHandlerContext context(ChannelHandler handler) {
        ObjectUtil.checkNotNull(handler, "handler");
        for (AbstractChannelHandlerContext ctx = this.head.next; ctx != null; ctx = ctx.next) {
            if (ctx.handler() == handler) {
                return ctx;
            }
        }
        return null;
    }

    @Override // io.netty.channel.ChannelPipeline
    public final ChannelHandlerContext context(Class<? extends ChannelHandler> handlerType) {
        ObjectUtil.checkNotNull(handlerType, "handlerType");
        for (AbstractChannelHandlerContext ctx = this.head.next; ctx != null; ctx = ctx.next) {
            if (handlerType.isAssignableFrom(ctx.handler().getClass())) {
                return ctx;
            }
        }
        return null;
    }

    @Override // io.netty.channel.ChannelPipeline
    public final List<String> names() {
        List<String> list = new ArrayList<>();
        for (AbstractChannelHandlerContext ctx = this.head.next; ctx != null; ctx = ctx.next) {
            list.add(ctx.name());
        }
        return list;
    }

    @Override // io.netty.channel.ChannelPipeline
    public final Map<String, ChannelHandler> toMap() {
        Map<String, ChannelHandler> map = new LinkedHashMap<>();
        for (AbstractChannelHandlerContext ctx = this.head.next; ctx != this.tail; ctx = ctx.next) {
            map.put(ctx.name(), ctx.handler());
        }
        return map;
    }

    @Override // java.lang.Iterable
    public final Iterator<Map.Entry<String, ChannelHandler>> iterator() {
        return toMap().entrySet().iterator();
    }

    public final String toString() {
        StringBuilder buf = new StringBuilder().append(StringUtil.simpleClassName(this)).append('{');
        AbstractChannelHandlerContext ctx = this.head.next;
        while (ctx != this.tail) {
            buf.append('(').append(ctx.name()).append(" = ").append(ctx.handler().getClass().getName()).append(')');
            ctx = ctx.next;
            if (ctx == this.tail) {
                break;
            }
            buf.append(", ");
        }
        buf.append('}');
        return buf.toString();
    }

    @Override // io.netty.channel.ChannelInboundInvoker
    public final ChannelPipeline fireChannelRegistered() {
        AbstractChannelHandlerContext.invokeChannelRegistered(this.head);
        return this;
    }

    @Override // io.netty.channel.ChannelInboundInvoker
    public final ChannelPipeline fireChannelUnregistered() {
        AbstractChannelHandlerContext.invokeChannelUnregistered(this.head);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void destroy() {
        destroyUp(this.head.next, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void destroyUp(AbstractChannelHandlerContext ctx, boolean inEventLoop) {
        Thread currentThread = Thread.currentThread();
        AbstractChannelHandlerContext tail = this.tail;
        while (ctx != tail) {
            EventExecutor executor = ctx.executor();
            if (!inEventLoop && !executor.inEventLoop(currentThread)) {
                final AbstractChannelHandlerContext finalCtx = ctx;
                executor.execute(new Runnable() { // from class: io.netty.channel.DefaultChannelPipeline.4
                    @Override // java.lang.Runnable
                    public void run() {
                        DefaultChannelPipeline.this.destroyUp(finalCtx, true);
                    }
                });
                return;
            } else {
                ctx = ctx.next;
                inEventLoop = false;
            }
        }
        destroyDown(currentThread, tail.prev, inEventLoop);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void destroyDown(Thread currentThread, AbstractChannelHandlerContext ctx, boolean inEventLoop) {
        AbstractChannelHandlerContext head = this.head;
        while (ctx != head) {
            EventExecutor executor = ctx.executor();
            if (inEventLoop || executor.inEventLoop(currentThread)) {
                atomicRemoveFromHandlerList(ctx);
                callHandlerRemoved0(ctx);
                ctx = ctx.prev;
                inEventLoop = false;
            } else {
                final AbstractChannelHandlerContext finalCtx = ctx;
                executor.execute(new Runnable() { // from class: io.netty.channel.DefaultChannelPipeline.5
                    @Override // java.lang.Runnable
                    public void run() {
                        DefaultChannelPipeline.this.destroyDown(Thread.currentThread(), finalCtx, true);
                    }
                });
                return;
            }
        }
    }

    @Override // io.netty.channel.ChannelInboundInvoker
    public final ChannelPipeline fireChannelActive() {
        AbstractChannelHandlerContext.invokeChannelActive(this.head);
        return this;
    }

    @Override // io.netty.channel.ChannelInboundInvoker
    public final ChannelPipeline fireChannelInactive() {
        AbstractChannelHandlerContext.invokeChannelInactive(this.head);
        return this;
    }

    @Override // io.netty.channel.ChannelInboundInvoker
    public final ChannelPipeline fireExceptionCaught(Throwable cause) {
        AbstractChannelHandlerContext.invokeExceptionCaught(this.head, cause);
        return this;
    }

    @Override // io.netty.channel.ChannelInboundInvoker
    public final ChannelPipeline fireUserEventTriggered(Object event) {
        AbstractChannelHandlerContext.invokeUserEventTriggered(this.head, event);
        return this;
    }

    @Override // io.netty.channel.ChannelInboundInvoker
    public final ChannelPipeline fireChannelRead(Object msg) {
        AbstractChannelHandlerContext.invokeChannelRead(this.head, msg);
        return this;
    }

    @Override // io.netty.channel.ChannelInboundInvoker
    public final ChannelPipeline fireChannelReadComplete() {
        AbstractChannelHandlerContext.invokeChannelReadComplete(this.head);
        return this;
    }

    @Override // io.netty.channel.ChannelInboundInvoker
    public final ChannelPipeline fireChannelWritabilityChanged() {
        AbstractChannelHandlerContext.invokeChannelWritabilityChanged(this.head);
        return this;
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public final ChannelFuture bind(SocketAddress localAddress) {
        return this.tail.bind(localAddress);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public final ChannelFuture connect(SocketAddress remoteAddress) {
        return this.tail.connect(remoteAddress);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public final ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress) {
        return this.tail.connect(remoteAddress, localAddress);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public final ChannelFuture disconnect() {
        return this.tail.disconnect();
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public final ChannelFuture close() {
        return this.tail.close();
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public final ChannelFuture deregister() {
        return this.tail.deregister();
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public final ChannelPipeline flush() {
        this.tail.flush();
        return this;
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public final ChannelFuture bind(SocketAddress localAddress, ChannelPromise promise) {
        return this.tail.bind(localAddress, promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public final ChannelFuture connect(SocketAddress remoteAddress, ChannelPromise promise) {
        return this.tail.connect(remoteAddress, promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public final ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
        return this.tail.connect(remoteAddress, localAddress, promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public final ChannelFuture disconnect(ChannelPromise promise) {
        return this.tail.disconnect(promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public final ChannelFuture close(ChannelPromise promise) {
        return this.tail.close(promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public final ChannelFuture deregister(ChannelPromise promise) {
        return this.tail.deregister(promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public final ChannelPipeline read() {
        this.tail.read();
        return this;
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public final ChannelFuture write(Object msg) {
        return this.tail.write(msg);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public final ChannelFuture write(Object msg, ChannelPromise promise) {
        return this.tail.write(msg, promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public final ChannelFuture writeAndFlush(Object msg, ChannelPromise promise) {
        return this.tail.writeAndFlush(msg, promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public final ChannelFuture writeAndFlush(Object msg) {
        return this.tail.writeAndFlush(msg);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public final ChannelPromise newPromise() {
        return new DefaultChannelPromise(this.channel);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public final ChannelProgressivePromise newProgressivePromise() {
        return new DefaultChannelProgressivePromise(this.channel);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public final ChannelFuture newSucceededFuture() {
        return this.succeededFuture;
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public final ChannelFuture newFailedFuture(Throwable cause) {
        return new FailedChannelFuture(this.channel, null, cause);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public final ChannelPromise voidPromise() {
        return this.voidPromise;
    }

    private void checkDuplicateName(String name) {
        if (context0(name) != null) {
            throw new IllegalArgumentException("Duplicate handler name: " + name);
        }
    }

    private AbstractChannelHandlerContext context0(String name) {
        for (AbstractChannelHandlerContext context = this.head.next; context != this.tail; context = context.next) {
            if (context.name().equals(name)) {
                return context;
            }
        }
        return null;
    }

    private AbstractChannelHandlerContext getContextOrDie(String name) {
        AbstractChannelHandlerContext ctx = (AbstractChannelHandlerContext) context(name);
        if (ctx == null) {
            throw new NoSuchElementException(name);
        }
        return ctx;
    }

    private AbstractChannelHandlerContext getContextOrDie(ChannelHandler handler) {
        AbstractChannelHandlerContext ctx = (AbstractChannelHandlerContext) context(handler);
        if (ctx == null) {
            throw new NoSuchElementException(handler.getClass().getName());
        }
        return ctx;
    }

    private AbstractChannelHandlerContext getContextOrDie(Class<? extends ChannelHandler> handlerType) {
        AbstractChannelHandlerContext ctx = (AbstractChannelHandlerContext) context(handlerType);
        if (ctx == null) {
            throw new NoSuchElementException(handlerType.getName());
        }
        return ctx;
    }

    private void callHandlerAddedForAllHandlers() {
        PendingHandlerCallback pendingHandlerCallbackHead;
        synchronized (this) {
            if (this.registered) {
                throw new AssertionError();
            }
            this.registered = true;
            pendingHandlerCallbackHead = this.pendingHandlerCallbackHead;
            this.pendingHandlerCallbackHead = null;
        }
        for (PendingHandlerCallback task = pendingHandlerCallbackHead; task != null; task = task.next) {
            task.execute();
        }
    }

    private void callHandlerCallbackLater(AbstractChannelHandlerContext ctx, boolean added) {
        if (this.registered) {
            throw new AssertionError();
        }
        PendingHandlerCallback task = added ? new PendingHandlerAddedTask(ctx) : new PendingHandlerRemovedTask(ctx);
        PendingHandlerCallback pending = this.pendingHandlerCallbackHead;
        if (pending == null) {
            this.pendingHandlerCallbackHead = task;
            return;
        }
        while (pending.next != null) {
            pending = pending.next;
        }
        pending.next = task;
    }

    private void callHandlerAddedInEventLoop(final AbstractChannelHandlerContext newCtx, EventExecutor executor) {
        newCtx.setAddPending();
        executor.execute(new Runnable() { // from class: io.netty.channel.DefaultChannelPipeline.6
            @Override // java.lang.Runnable
            public void run() {
                DefaultChannelPipeline.this.callHandlerAdded0(newCtx);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onUnhandledInboundException(Throwable cause) {
        try {
            logger.warn("An exceptionCaught() event was fired, and it reached at the tail of the pipeline. It usually means the last handler in the pipeline did not handle the exception.", cause);
        } finally {
            ReferenceCountUtil.release(cause);
        }
    }

    protected void onUnhandledInboundChannelActive() {
    }

    protected void onUnhandledInboundChannelInactive() {
    }

    protected void onUnhandledInboundMessage(Object msg) {
        try {
            logger.debug("Discarded inbound message {} that reached at the tail of the pipeline. Please check your pipeline configuration.", msg);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    protected void onUnhandledInboundMessage(ChannelHandlerContext ctx, Object msg) {
        onUnhandledInboundMessage(msg);
        if (logger.isDebugEnabled()) {
            logger.debug("Discarded message pipeline : {}. Channel : {}.", ctx.pipeline().names(), ctx.channel());
        }
    }

    protected void onUnhandledInboundChannelReadComplete() {
    }

    protected void onUnhandledInboundUserEventTriggered(Object evt) {
        ReferenceCountUtil.release(evt);
    }

    protected void onUnhandledChannelWritabilityChanged() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void incrementPendingOutboundBytes(long size) {
        ChannelOutboundBuffer buffer = this.channel.unsafe().outboundBuffer();
        if (buffer != null) {
            buffer.incrementPendingOutboundBytes(size);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void decrementPendingOutboundBytes(long size) {
        ChannelOutboundBuffer buffer = this.channel.unsafe().outboundBuffer();
        if (buffer != null) {
            buffer.decrementPendingOutboundBytes(size);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public final class TailContext extends AbstractChannelHandlerContext implements ChannelInboundHandler {
        TailContext(DefaultChannelPipeline pipeline) {
            super(pipeline, null, DefaultChannelPipeline.TAIL_NAME, TailContext.class);
            setAddComplete();
        }

        @Override // io.netty.channel.ChannelHandlerContext
        public ChannelHandler handler() {
            return this;
        }

        @Override // io.netty.channel.ChannelInboundHandler
        public void channelRegistered(ChannelHandlerContext ctx) {
        }

        @Override // io.netty.channel.ChannelInboundHandler
        public void channelUnregistered(ChannelHandlerContext ctx) {
        }

        @Override // io.netty.channel.ChannelInboundHandler
        public void channelActive(ChannelHandlerContext ctx) {
            DefaultChannelPipeline.this.onUnhandledInboundChannelActive();
        }

        @Override // io.netty.channel.ChannelInboundHandler
        public void channelInactive(ChannelHandlerContext ctx) {
            DefaultChannelPipeline.this.onUnhandledInboundChannelInactive();
        }

        @Override // io.netty.channel.ChannelInboundHandler
        public void channelWritabilityChanged(ChannelHandlerContext ctx) {
            DefaultChannelPipeline.this.onUnhandledChannelWritabilityChanged();
        }

        @Override // io.netty.channel.ChannelHandler
        public void handlerAdded(ChannelHandlerContext ctx) {
        }

        @Override // io.netty.channel.ChannelHandler
        public void handlerRemoved(ChannelHandlerContext ctx) {
        }

        @Override // io.netty.channel.ChannelInboundHandler
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
            DefaultChannelPipeline.this.onUnhandledInboundUserEventTriggered(evt);
        }

        @Override // io.netty.channel.ChannelInboundHandler
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            DefaultChannelPipeline.this.onUnhandledInboundException(cause);
        }

        @Override // io.netty.channel.ChannelInboundHandler
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            DefaultChannelPipeline.this.onUnhandledInboundMessage(ctx, msg);
        }

        @Override // io.netty.channel.ChannelInboundHandler
        public void channelReadComplete(ChannelHandlerContext ctx) {
            DefaultChannelPipeline.this.onUnhandledInboundChannelReadComplete();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public final class HeadContext extends AbstractChannelHandlerContext implements ChannelOutboundHandler, ChannelInboundHandler {
        private final Channel.Unsafe unsafe;

        HeadContext(DefaultChannelPipeline pipeline) {
            super(pipeline, null, DefaultChannelPipeline.HEAD_NAME, HeadContext.class);
            this.unsafe = pipeline.channel().unsafe();
            setAddComplete();
        }

        @Override // io.netty.channel.ChannelHandlerContext
        public ChannelHandler handler() {
            return this;
        }

        @Override // io.netty.channel.ChannelHandler
        public void handlerAdded(ChannelHandlerContext ctx) {
        }

        @Override // io.netty.channel.ChannelHandler
        public void handlerRemoved(ChannelHandlerContext ctx) {
        }

        @Override // io.netty.channel.ChannelOutboundHandler
        public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) {
            this.unsafe.bind(localAddress, promise);
        }

        @Override // io.netty.channel.ChannelOutboundHandler
        public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
            this.unsafe.connect(remoteAddress, localAddress, promise);
        }

        @Override // io.netty.channel.ChannelOutboundHandler
        public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) {
            this.unsafe.disconnect(promise);
        }

        @Override // io.netty.channel.ChannelOutboundHandler
        public void close(ChannelHandlerContext ctx, ChannelPromise promise) {
            this.unsafe.close(promise);
        }

        @Override // io.netty.channel.ChannelOutboundHandler
        public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) {
            this.unsafe.deregister(promise);
        }

        @Override // io.netty.channel.ChannelOutboundHandler
        public void read(ChannelHandlerContext ctx) {
            this.unsafe.beginRead();
        }

        @Override // io.netty.channel.ChannelOutboundHandler
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
            this.unsafe.write(msg, promise);
        }

        @Override // io.netty.channel.ChannelOutboundHandler
        public void flush(ChannelHandlerContext ctx) {
            this.unsafe.flush();
        }

        @Override // io.netty.channel.ChannelHandler, io.netty.channel.ChannelInboundHandler
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            ctx.fireExceptionCaught(cause);
        }

        @Override // io.netty.channel.ChannelInboundHandler
        public void channelRegistered(ChannelHandlerContext ctx) {
            DefaultChannelPipeline.this.invokeHandlerAddedIfNeeded();
            ctx.fireChannelRegistered();
        }

        @Override // io.netty.channel.ChannelInboundHandler
        public void channelUnregistered(ChannelHandlerContext ctx) {
            ctx.fireChannelUnregistered();
            if (!DefaultChannelPipeline.this.channel.isOpen()) {
                DefaultChannelPipeline.this.destroy();
            }
        }

        @Override // io.netty.channel.ChannelInboundHandler
        public void channelActive(ChannelHandlerContext ctx) {
            ctx.fireChannelActive();
            readIfIsAutoRead();
        }

        @Override // io.netty.channel.ChannelInboundHandler
        public void channelInactive(ChannelHandlerContext ctx) {
            ctx.fireChannelInactive();
        }

        @Override // io.netty.channel.ChannelInboundHandler
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            ctx.fireChannelRead(msg);
        }

        @Override // io.netty.channel.ChannelInboundHandler
        public void channelReadComplete(ChannelHandlerContext ctx) {
            ctx.fireChannelReadComplete();
            readIfIsAutoRead();
        }

        private void readIfIsAutoRead() {
            if (DefaultChannelPipeline.this.channel.config().isAutoRead()) {
                DefaultChannelPipeline.this.channel.read();
            }
        }

        @Override // io.netty.channel.ChannelInboundHandler
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
            ctx.fireUserEventTriggered(evt);
        }

        @Override // io.netty.channel.ChannelInboundHandler
        public void channelWritabilityChanged(ChannelHandlerContext ctx) {
            ctx.fireChannelWritabilityChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static abstract class PendingHandlerCallback implements Runnable {
        final AbstractChannelHandlerContext ctx;
        PendingHandlerCallback next;

        abstract void execute();

        PendingHandlerCallback(AbstractChannelHandlerContext ctx) {
            this.ctx = ctx;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class PendingHandlerAddedTask extends PendingHandlerCallback {
        PendingHandlerAddedTask(AbstractChannelHandlerContext ctx) {
            super(ctx);
        }

        @Override // java.lang.Runnable
        public void run() {
            DefaultChannelPipeline.this.callHandlerAdded0(this.ctx);
        }

        @Override // io.netty.channel.DefaultChannelPipeline.PendingHandlerCallback
        void execute() {
            EventExecutor executor = this.ctx.executor();
            if (executor.inEventLoop()) {
                DefaultChannelPipeline.this.callHandlerAdded0(this.ctx);
                return;
            }
            try {
                executor.execute(this);
            } catch (RejectedExecutionException e) {
                if (DefaultChannelPipeline.logger.isWarnEnabled()) {
                    DefaultChannelPipeline.logger.warn("Can't invoke handlerAdded() as the EventExecutor {} rejected it, removing handler {}.", executor, this.ctx.name(), e);
                }
                DefaultChannelPipeline.this.atomicRemoveFromHandlerList(this.ctx);
                this.ctx.setRemoved();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class PendingHandlerRemovedTask extends PendingHandlerCallback {
        PendingHandlerRemovedTask(AbstractChannelHandlerContext ctx) {
            super(ctx);
        }

        @Override // java.lang.Runnable
        public void run() {
            DefaultChannelPipeline.this.callHandlerRemoved0(this.ctx);
        }

        @Override // io.netty.channel.DefaultChannelPipeline.PendingHandlerCallback
        void execute() {
            EventExecutor executor = this.ctx.executor();
            if (executor.inEventLoop()) {
                DefaultChannelPipeline.this.callHandlerRemoved0(this.ctx);
                return;
            }
            try {
                executor.execute(this);
            } catch (RejectedExecutionException e) {
                if (DefaultChannelPipeline.logger.isWarnEnabled()) {
                    DefaultChannelPipeline.logger.warn("Can't invoke handlerRemoved() as the EventExecutor {} rejected it, removing handler {}.", executor, this.ctx.name(), e);
                }
                this.ctx.setRemoved();
            }
        }
    }
}
