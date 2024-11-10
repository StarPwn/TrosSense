package io.netty.channel.embedded;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultChannelPromise;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.AbstractScheduledEventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.internal.ObjectUtil;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/* loaded from: classes4.dex */
final class EmbeddedEventLoop extends AbstractScheduledEventExecutor implements EventLoop {
    private long frozenTimestamp;
    private long startTime = initialNanoTime();
    private final Queue<Runnable> tasks = new ArrayDeque(2);
    private boolean timeFrozen;

    @Override // io.netty.util.concurrent.AbstractEventExecutor, io.netty.util.concurrent.EventExecutor, io.netty.channel.EventLoop
    public EventLoopGroup parent() {
        return (EventLoopGroup) super.parent();
    }

    @Override // io.netty.util.concurrent.AbstractEventExecutor, io.netty.util.concurrent.EventExecutor, io.netty.util.concurrent.EventExecutorGroup, io.netty.channel.EventLoopGroup
    public EventLoop next() {
        return (EventLoop) super.next();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.concurrent.Executor
    public void execute(Runnable command) {
        this.tasks.add(ObjectUtil.checkNotNull(command, "command"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void runTasks() {
        while (true) {
            Runnable task = this.tasks.poll();
            if (task != null) {
                task.run();
            } else {
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasPendingNormalTasks() {
        return !this.tasks.isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long runScheduledTasks() {
        long time = getCurrentTimeNanos();
        while (true) {
            Runnable task = pollScheduledTask(time);
            if (task == null) {
                return nextScheduledTaskNano();
            }
            task.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long nextScheduledTask() {
        return nextScheduledTaskNano();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.util.concurrent.AbstractScheduledEventExecutor
    public long getCurrentTimeNanos() {
        if (this.timeFrozen) {
            return this.frozenTimestamp;
        }
        return System.nanoTime() - this.startTime;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void advanceTimeBy(long nanos) {
        if (this.timeFrozen) {
            this.frozenTimestamp += nanos;
        } else {
            this.startTime -= nanos;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void freezeTime() {
        if (!this.timeFrozen) {
            this.frozenTimestamp = getCurrentTimeNanos();
            this.timeFrozen = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unfreezeTime() {
        if (this.timeFrozen) {
            this.startTime = System.nanoTime() - this.frozenTimestamp;
            this.timeFrozen = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.util.concurrent.AbstractScheduledEventExecutor
    public void cancelScheduledTasks() {
        super.cancelScheduledTasks();
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup
    public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit) {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup
    public Future<?> terminationFuture() {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.util.concurrent.AbstractEventExecutor, java.util.concurrent.ExecutorService, io.netty.util.concurrent.EventExecutorGroup
    @Deprecated
    public void shutdown() {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup
    public boolean isShuttingDown() {
        return false;
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isShutdown() {
        return false;
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isTerminated() {
        return false;
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean awaitTermination(long timeout, TimeUnit unit) {
        return false;
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
        channel.unsafe().register(this, promise);
        return promise;
    }

    @Override // io.netty.util.concurrent.AbstractEventExecutor, io.netty.util.concurrent.EventExecutor
    public boolean inEventLoop() {
        return true;
    }

    @Override // io.netty.util.concurrent.EventExecutor
    public boolean inEventLoop(Thread thread) {
        return true;
    }
}
