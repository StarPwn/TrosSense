package io.netty.channel;

import io.netty.util.concurrent.AbstractEventExecutorGroup;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.ThreadPerTaskExecutor;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.ReadOnlyIterator;
import java.util.Collections;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@Deprecated
/* loaded from: classes4.dex */
public class ThreadPerChannelEventLoopGroup extends AbstractEventExecutorGroup implements EventLoopGroup {
    final Set<EventLoop> activeChildren;
    private final Object[] childArgs;
    private final FutureListener<Object> childTerminationListener;
    final Executor executor;
    final Queue<EventLoop> idleChildren;
    private final int maxChannels;
    private volatile boolean shuttingDown;
    private final Promise<?> terminationFuture;
    private final ChannelException tooManyChannels;

    protected ThreadPerChannelEventLoopGroup() {
        this(0);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    protected ThreadPerChannelEventLoopGroup(int maxChannels) {
        this(maxChannels, (ThreadFactory) null, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ThreadPerChannelEventLoopGroup(int maxChannels, ThreadFactory threadFactory, Object... args) {
        this(maxChannels, threadFactory == null ? null : new ThreadPerTaskExecutor(threadFactory), args);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ThreadPerChannelEventLoopGroup(int maxChannels, Executor executor, Object... args) {
        this.activeChildren = Collections.newSetFromMap(PlatformDependent.newConcurrentHashMap());
        this.idleChildren = new ConcurrentLinkedQueue();
        this.terminationFuture = new DefaultPromise(GlobalEventExecutor.INSTANCE);
        this.childTerminationListener = new FutureListener<Object>() { // from class: io.netty.channel.ThreadPerChannelEventLoopGroup.1
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(Future<Object> future) throws Exception {
                if (ThreadPerChannelEventLoopGroup.this.isTerminated()) {
                    ThreadPerChannelEventLoopGroup.this.terminationFuture.trySuccess(null);
                }
            }
        };
        ObjectUtil.checkPositiveOrZero(maxChannels, "maxChannels");
        executor = executor == null ? new ThreadPerTaskExecutor(new DefaultThreadFactory(getClass())) : executor;
        if (args == null) {
            this.childArgs = EmptyArrays.EMPTY_OBJECTS;
        } else {
            this.childArgs = (Object[]) args.clone();
        }
        this.maxChannels = maxChannels;
        this.executor = executor;
        this.tooManyChannels = ChannelException.newStatic("too many channels (max: " + maxChannels + ')', ThreadPerChannelEventLoopGroup.class, "nextChild()");
    }

    protected EventLoop newChild(Object... args) throws Exception {
        return new ThreadPerChannelEventLoop(this);
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup, java.lang.Iterable
    public Iterator<EventExecutor> iterator() {
        return new ReadOnlyIterator(this.activeChildren.iterator());
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup, io.netty.channel.EventLoopGroup
    public EventLoop next() {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup
    public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit) {
        this.shuttingDown = true;
        for (EventLoop l : this.activeChildren) {
            l.shutdownGracefully(quietPeriod, timeout, unit);
        }
        for (EventLoop l2 : this.idleChildren) {
            l2.shutdownGracefully(quietPeriod, timeout, unit);
        }
        if (isTerminated()) {
            this.terminationFuture.trySuccess(null);
        }
        return terminationFuture();
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup
    public Future<?> terminationFuture() {
        return this.terminationFuture;
    }

    @Override // io.netty.util.concurrent.AbstractEventExecutorGroup, io.netty.util.concurrent.EventExecutorGroup, java.util.concurrent.ExecutorService
    @Deprecated
    public void shutdown() {
        this.shuttingDown = true;
        for (EventLoop l : this.activeChildren) {
            l.shutdown();
        }
        for (EventLoop l2 : this.idleChildren) {
            l2.shutdown();
        }
        if (isTerminated()) {
            this.terminationFuture.trySuccess(null);
        }
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup
    public boolean isShuttingDown() {
        for (EventLoop l : this.activeChildren) {
            if (!l.isShuttingDown()) {
                return false;
            }
        }
        for (EventLoop l2 : this.idleChildren) {
            if (!l2.isShuttingDown()) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isShutdown() {
        for (EventLoop l : this.activeChildren) {
            if (!l.isShutdown()) {
                return false;
            }
        }
        for (EventLoop l2 : this.idleChildren) {
            if (!l2.isShutdown()) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isTerminated() {
        for (EventLoop l : this.activeChildren) {
            if (!l.isTerminated()) {
                return false;
            }
        }
        for (EventLoop l2 : this.idleChildren) {
            if (!l2.isTerminated()) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        long timeLeft;
        long timeLeft2;
        long deadline = System.nanoTime() + unit.toNanos(timeout);
        for (EventLoop l : this.activeChildren) {
            do {
                timeLeft2 = deadline - System.nanoTime();
                if (timeLeft2 <= 0) {
                    return isTerminated();
                }
            } while (!l.awaitTermination(timeLeft2, TimeUnit.NANOSECONDS));
        }
        for (EventLoop l2 : this.idleChildren) {
            do {
                timeLeft = deadline - System.nanoTime();
                if (timeLeft <= 0) {
                    return isTerminated();
                }
            } while (!l2.awaitTermination(timeLeft, TimeUnit.NANOSECONDS));
        }
        return isTerminated();
    }

    @Override // io.netty.channel.EventLoopGroup
    public ChannelFuture register(Channel channel) {
        ObjectUtil.checkNotNull(channel, "channel");
        try {
            EventLoop l = nextChild();
            return l.register(new DefaultChannelPromise(channel, l));
        } catch (Throwable t) {
            return new FailedChannelFuture(channel, GlobalEventExecutor.INSTANCE, t);
        }
    }

    @Override // io.netty.channel.EventLoopGroup
    public ChannelFuture register(ChannelPromise promise) {
        try {
            return nextChild().register(promise);
        } catch (Throwable t) {
            promise.setFailure(t);
            return promise;
        }
    }

    @Override // io.netty.channel.EventLoopGroup
    @Deprecated
    public ChannelFuture register(Channel channel, ChannelPromise promise) {
        ObjectUtil.checkNotNull(channel, "channel");
        try {
            return nextChild().register(channel, promise);
        } catch (Throwable t) {
            promise.setFailure(t);
            return promise;
        }
    }

    private EventLoop nextChild() throws Exception {
        if (this.shuttingDown) {
            throw new RejectedExecutionException("shutting down");
        }
        EventLoop loop = this.idleChildren.poll();
        if (loop == null) {
            if (this.maxChannels > 0 && this.activeChildren.size() >= this.maxChannels) {
                throw this.tooManyChannels;
            }
            loop = newChild(this.childArgs);
            loop.terminationFuture().addListener(this.childTerminationListener);
        }
        this.activeChildren.add(loop);
        return loop;
    }
}
