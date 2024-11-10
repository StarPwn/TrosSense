package io.netty.util.concurrent;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/* loaded from: classes4.dex */
public final class UnorderedThreadPoolEventExecutor extends ScheduledThreadPoolExecutor implements EventExecutor {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) UnorderedThreadPoolEventExecutor.class);
    private final Set<EventExecutor> executorSet;
    private final Promise<?> terminationFuture;

    @Override // java.util.concurrent.ScheduledThreadPoolExecutor, java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
    public /* bridge */ /* synthetic */ java.util.concurrent.Future submit(Runnable runnable, Object obj) {
        return submit(runnable, (Runnable) obj);
    }

    public UnorderedThreadPoolEventExecutor(int corePoolSize) {
        this(corePoolSize, new DefaultThreadFactory((Class<?>) UnorderedThreadPoolEventExecutor.class));
    }

    public UnorderedThreadPoolEventExecutor(int corePoolSize, ThreadFactory threadFactory) {
        super(corePoolSize, threadFactory);
        this.terminationFuture = GlobalEventExecutor.INSTANCE.newPromise();
        this.executorSet = Collections.singleton(this);
    }

    public UnorderedThreadPoolEventExecutor(int corePoolSize, java.util.concurrent.RejectedExecutionHandler handler) {
        this(corePoolSize, new DefaultThreadFactory((Class<?>) UnorderedThreadPoolEventExecutor.class), handler);
    }

    public UnorderedThreadPoolEventExecutor(int corePoolSize, ThreadFactory threadFactory, java.util.concurrent.RejectedExecutionHandler handler) {
        super(corePoolSize, threadFactory, handler);
        this.terminationFuture = GlobalEventExecutor.INSTANCE.newPromise();
        this.executorSet = Collections.singleton(this);
    }

    @Override // io.netty.util.concurrent.EventExecutor, io.netty.util.concurrent.EventExecutorGroup, io.netty.channel.EventLoopGroup
    public EventExecutor next() {
        return this;
    }

    @Override // io.netty.util.concurrent.EventExecutor, io.netty.channel.EventLoop
    public EventExecutorGroup parent() {
        return this;
    }

    @Override // io.netty.util.concurrent.EventExecutor
    public boolean inEventLoop() {
        return false;
    }

    @Override // io.netty.util.concurrent.EventExecutor
    public boolean inEventLoop(Thread thread) {
        return false;
    }

    @Override // io.netty.util.concurrent.EventExecutor
    public <V> Promise<V> newPromise() {
        return new DefaultPromise(this);
    }

    @Override // io.netty.util.concurrent.EventExecutor
    public <V> ProgressivePromise<V> newProgressivePromise() {
        return new DefaultProgressivePromise(this);
    }

    @Override // io.netty.util.concurrent.EventExecutor
    public <V> Future<V> newSucceededFuture(V result) {
        return new SucceededFuture(this, result);
    }

    @Override // io.netty.util.concurrent.EventExecutor
    public <V> Future<V> newFailedFuture(Throwable cause) {
        return new FailedFuture(this, cause);
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup
    public boolean isShuttingDown() {
        return isShutdown();
    }

    @Override // java.util.concurrent.ScheduledThreadPoolExecutor, java.util.concurrent.ThreadPoolExecutor, java.util.concurrent.ExecutorService, io.netty.util.concurrent.EventExecutorGroup
    public List<Runnable> shutdownNow() {
        List<Runnable> tasks = super.shutdownNow();
        this.terminationFuture.trySuccess(null);
        return tasks;
    }

    @Override // java.util.concurrent.ScheduledThreadPoolExecutor, java.util.concurrent.ThreadPoolExecutor, java.util.concurrent.ExecutorService, io.netty.util.concurrent.EventExecutorGroup
    public void shutdown() {
        super.shutdown();
        this.terminationFuture.trySuccess(null);
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup
    public Future<?> shutdownGracefully() {
        return shutdownGracefully(2L, 15L, TimeUnit.SECONDS);
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup
    public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit) {
        shutdown();
        return terminationFuture();
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup
    public Future<?> terminationFuture() {
        return this.terminationFuture;
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup, java.lang.Iterable
    public Iterator<EventExecutor> iterator() {
        return this.executorSet.iterator();
    }

    @Override // java.util.concurrent.ScheduledThreadPoolExecutor
    protected <V> RunnableScheduledFuture<V> decorateTask(Runnable runnable, RunnableScheduledFuture<V> task) {
        return runnable instanceof NonNotifyRunnable ? task : new RunnableScheduledFutureTask(this, task, false);
    }

    @Override // java.util.concurrent.ScheduledThreadPoolExecutor
    protected <V> RunnableScheduledFuture<V> decorateTask(Callable<V> callable, RunnableScheduledFuture<V> task) {
        return new RunnableScheduledFutureTask(this, task, true);
    }

    @Override // java.util.concurrent.ScheduledThreadPoolExecutor, java.util.concurrent.ScheduledExecutorService
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return (ScheduledFuture) super.schedule(command, delay, unit);
    }

    @Override // java.util.concurrent.ScheduledThreadPoolExecutor, java.util.concurrent.ScheduledExecutorService
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        return (ScheduledFuture) super.schedule((Callable) callable, delay, unit);
    }

    @Override // java.util.concurrent.ScheduledThreadPoolExecutor, java.util.concurrent.ScheduledExecutorService
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return (ScheduledFuture) super.scheduleAtFixedRate(command, initialDelay, period, unit);
    }

    @Override // java.util.concurrent.ScheduledThreadPoolExecutor, java.util.concurrent.ScheduledExecutorService
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return (ScheduledFuture) super.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }

    @Override // java.util.concurrent.ScheduledThreadPoolExecutor, java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
    public Future<?> submit(Runnable task) {
        return (Future) super.submit(task);
    }

    @Override // java.util.concurrent.ScheduledThreadPoolExecutor, java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService, io.netty.util.concurrent.EventExecutorGroup
    public <T> Future<T> submit(Runnable task, T result) {
        return (Future) super.submit(task, (Runnable) result);
    }

    @Override // java.util.concurrent.ScheduledThreadPoolExecutor, java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
    public <T> Future<T> submit(Callable<T> task) {
        return (Future) super.submit((Callable) task);
    }

    @Override // java.util.concurrent.ScheduledThreadPoolExecutor, java.util.concurrent.ThreadPoolExecutor, java.util.concurrent.Executor
    public void execute(Runnable command) {
        super.schedule((Runnable) new NonNotifyRunnable(command), 0L, TimeUnit.NANOSECONDS);
    }

    /* loaded from: classes4.dex */
    private static final class RunnableScheduledFutureTask<V> extends PromiseTask<V> implements RunnableScheduledFuture<V>, ScheduledFuture<V> {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private final RunnableScheduledFuture<V> future;
        private final boolean wasCallable;

        RunnableScheduledFutureTask(EventExecutor executor, RunnableScheduledFuture<V> future, boolean wasCallable) {
            super(executor, future);
            this.future = future;
            this.wasCallable = wasCallable;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // io.netty.util.concurrent.PromiseTask
        public V runTask() throws Throwable {
            V v = (V) super.runTask();
            if (v == null && this.wasCallable) {
                if (!this.future.isDone()) {
                    throw new AssertionError();
                }
                try {
                    return (V) this.future.get();
                } catch (ExecutionException e) {
                    throw e.getCause();
                }
            }
            return v;
        }

        @Override // io.netty.util.concurrent.PromiseTask, java.util.concurrent.RunnableFuture, java.lang.Runnable
        public void run() {
            if (!isPeriodic()) {
                super.run();
                return;
            }
            if (!isDone()) {
                try {
                    runTask();
                } catch (Throwable cause) {
                    if (!tryFailureInternal(cause)) {
                        UnorderedThreadPoolEventExecutor.logger.warn("Failure during execution of task", cause);
                    }
                }
            }
        }

        @Override // java.util.concurrent.RunnableScheduledFuture
        public boolean isPeriodic() {
            return this.future.isPeriodic();
        }

        @Override // java.util.concurrent.Delayed
        public long getDelay(TimeUnit unit) {
            return this.future.getDelay(unit);
        }

        @Override // java.lang.Comparable
        public int compareTo(Delayed o) {
            return this.future.compareTo(o);
        }
    }

    /* loaded from: classes4.dex */
    private static final class NonNotifyRunnable implements Runnable {
        private final Runnable task;

        NonNotifyRunnable(Runnable task) {
            this.task = task;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.task.run();
        }
    }
}
