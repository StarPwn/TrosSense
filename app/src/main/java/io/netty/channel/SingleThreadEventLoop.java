package io.netty.channel;

import io.netty.util.concurrent.RejectedExecutionHandler;
import io.netty.util.concurrent.RejectedExecutionHandlers;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.SystemPropertyUtil;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

/* loaded from: classes4.dex */
public abstract class SingleThreadEventLoop extends SingleThreadEventExecutor implements EventLoop {
    protected static final int DEFAULT_MAX_PENDING_TASKS = Math.max(16, SystemPropertyUtil.getInt("io.netty.eventLoop.maxPendingTasks", Integer.MAX_VALUE));
    private final Queue<Runnable> tailTasks;

    /* JADX INFO: Access modifiers changed from: protected */
    public SingleThreadEventLoop(EventLoopGroup parent, ThreadFactory threadFactory, boolean addTaskWakesUp) {
        this(parent, threadFactory, addTaskWakesUp, DEFAULT_MAX_PENDING_TASKS, RejectedExecutionHandlers.reject());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public SingleThreadEventLoop(EventLoopGroup parent, Executor executor, boolean addTaskWakesUp) {
        this(parent, executor, addTaskWakesUp, DEFAULT_MAX_PENDING_TASKS, RejectedExecutionHandlers.reject());
    }

    protected SingleThreadEventLoop(EventLoopGroup parent, ThreadFactory threadFactory, boolean addTaskWakesUp, int maxPendingTasks, RejectedExecutionHandler rejectedExecutionHandler) {
        super(parent, threadFactory, addTaskWakesUp, maxPendingTasks, rejectedExecutionHandler);
        this.tailTasks = newTaskQueue(maxPendingTasks);
    }

    protected SingleThreadEventLoop(EventLoopGroup parent, Executor executor, boolean addTaskWakesUp, int maxPendingTasks, RejectedExecutionHandler rejectedExecutionHandler) {
        super(parent, executor, addTaskWakesUp, maxPendingTasks, rejectedExecutionHandler);
        this.tailTasks = newTaskQueue(maxPendingTasks);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public SingleThreadEventLoop(EventLoopGroup parent, Executor executor, boolean addTaskWakesUp, Queue<Runnable> taskQueue, Queue<Runnable> tailTaskQueue, RejectedExecutionHandler rejectedExecutionHandler) {
        super(parent, executor, addTaskWakesUp, taskQueue, rejectedExecutionHandler);
        this.tailTasks = (Queue) ObjectUtil.checkNotNull(tailTaskQueue, "tailTaskQueue");
    }

    @Override // io.netty.util.concurrent.AbstractEventExecutor, io.netty.util.concurrent.EventExecutor, io.netty.channel.EventLoop
    public EventLoopGroup parent() {
        return (EventLoopGroup) super.parent();
    }

    @Override // io.netty.util.concurrent.AbstractEventExecutor, io.netty.util.concurrent.EventExecutor, io.netty.util.concurrent.EventExecutorGroup, io.netty.channel.EventLoopGroup
    public EventLoop next() {
        return (EventLoop) super.next();
    }

    @Override // io.netty.channel.EventLoopGroup
    public ChannelFuture register(Channel channel) {
        return register(new DefaultChannelPromise(channel, this));
    }

    @Override // io.netty.channel.EventLoopGroup
    public ChannelFuture register(ChannelPromise promise) {
        ObjectUtil.checkNotNull(promise, "promise");
        promise.channel().unsafe().register(this, promise);
        return promise;
    }

    @Override // io.netty.channel.EventLoopGroup
    @Deprecated
    public ChannelFuture register(Channel channel, ChannelPromise promise) {
        ObjectUtil.checkNotNull(promise, "promise");
        ObjectUtil.checkNotNull(channel, "channel");
        channel.unsafe().register(this, promise);
        return promise;
    }

    public final void executeAfterEventLoopIteration(Runnable task) {
        ObjectUtil.checkNotNull(task, "task");
        if (isShutdown()) {
            reject();
        }
        if (!this.tailTasks.offer(task)) {
            reject(task);
        }
        if (wakesUpForTask(task)) {
            wakeup(inEventLoop());
        }
    }

    final boolean removeAfterEventLoopIterationTask(Runnable task) {
        return this.tailTasks.remove(ObjectUtil.checkNotNull(task, "task"));
    }

    @Override // io.netty.util.concurrent.SingleThreadEventExecutor
    protected void afterRunningAllTasks() {
        runAllTasksFrom(this.tailTasks);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.util.concurrent.SingleThreadEventExecutor
    public boolean hasTasks() {
        return super.hasTasks() || !this.tailTasks.isEmpty();
    }

    @Override // io.netty.util.concurrent.SingleThreadEventExecutor
    public int pendingTasks() {
        return super.pendingTasks() + this.tailTasks.size();
    }

    public int registeredChannels() {
        return -1;
    }

    public Iterator<Channel> registeredChannelsIterator() {
        throw new UnsupportedOperationException("registeredChannelsIterator");
    }

    /* loaded from: classes4.dex */
    protected static final class ChannelsReadOnlyIterator<T extends Channel> implements Iterator<Channel> {
        private static final Iterator<Object> EMPTY = new Iterator<Object>() { // from class: io.netty.channel.SingleThreadEventLoop.ChannelsReadOnlyIterator.1
            @Override // java.util.Iterator
            public boolean hasNext() {
                return false;
            }

            @Override // java.util.Iterator
            public Object next() {
                throw new NoSuchElementException();
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException("remove");
            }
        };
        private final Iterator<T> channelIterator;

        public ChannelsReadOnlyIterator(Iterable<T> channelIterable) {
            this.channelIterator = ((Iterable) ObjectUtil.checkNotNull(channelIterable, "channelIterable")).iterator();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.channelIterator.hasNext();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public Channel next() {
            return this.channelIterator.next();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("remove");
        }

        public static <T> Iterator<T> empty() {
            return (Iterator<T>) EMPTY;
        }
    }
}
