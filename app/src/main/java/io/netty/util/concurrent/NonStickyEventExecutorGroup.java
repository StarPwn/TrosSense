package io.netty.util.concurrent;

import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/* loaded from: classes4.dex */
public final class NonStickyEventExecutorGroup implements EventExecutorGroup {
    private final EventExecutorGroup group;
    private final int maxTaskExecutePerRun;

    @Override // java.util.concurrent.ExecutorService
    public /* bridge */ /* synthetic */ java.util.concurrent.Future submit(Runnable runnable, Object obj) {
        return submit(runnable, (Runnable) obj);
    }

    public NonStickyEventExecutorGroup(EventExecutorGroup group) {
        this(group, 1024);
    }

    public NonStickyEventExecutorGroup(EventExecutorGroup group, int maxTaskExecutePerRun) {
        this.group = verify(group);
        this.maxTaskExecutePerRun = ObjectUtil.checkPositive(maxTaskExecutePerRun, "maxTaskExecutePerRun");
    }

    private static EventExecutorGroup verify(EventExecutorGroup group) {
        for (EventExecutor executor : (EventExecutorGroup) ObjectUtil.checkNotNull(group, "group")) {
            if (executor instanceof OrderedEventExecutor) {
                throw new IllegalArgumentException("EventExecutorGroup " + group + " contains OrderedEventExecutors: " + executor);
            }
        }
        return group;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public NonStickyOrderedEventExecutor newExecutor(EventExecutor executor) {
        return new NonStickyOrderedEventExecutor(executor, this.maxTaskExecutePerRun);
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup
    public boolean isShuttingDown() {
        return this.group.isShuttingDown();
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup
    public Future<?> shutdownGracefully() {
        return this.group.shutdownGracefully();
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup
    public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit) {
        return this.group.shutdownGracefully(quietPeriod, timeout, unit);
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup
    public Future<?> terminationFuture() {
        return this.group.terminationFuture();
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup, java.util.concurrent.ExecutorService
    public void shutdown() {
        this.group.shutdown();
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup
    public List<Runnable> shutdownNow() {
        return this.group.shutdownNow();
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup, io.netty.channel.EventLoopGroup
    public EventExecutor next() {
        return newExecutor(this.group.next());
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup, java.lang.Iterable
    public Iterator<EventExecutor> iterator() {
        final Iterator<EventExecutor> itr = this.group.iterator();
        return new Iterator<EventExecutor>() { // from class: io.netty.util.concurrent.NonStickyEventExecutorGroup.1
            @Override // java.util.Iterator
            public boolean hasNext() {
                return itr.hasNext();
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            public EventExecutor next() {
                return NonStickyEventExecutorGroup.this.newExecutor((EventExecutor) itr.next());
            }

            @Override // java.util.Iterator
            public void remove() {
                itr.remove();
            }
        };
    }

    @Override // java.util.concurrent.ExecutorService
    public Future<?> submit(Runnable task) {
        return this.group.submit(task);
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup, java.util.concurrent.ExecutorService
    public <T> Future<T> submit(Runnable task, T result) {
        return this.group.submit(task, (Runnable) result);
    }

    @Override // java.util.concurrent.ExecutorService
    public <T> Future<T> submit(Callable<T> task) {
        return this.group.submit((Callable) task);
    }

    @Override // java.util.concurrent.ScheduledExecutorService
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return this.group.schedule(command, delay, unit);
    }

    @Override // java.util.concurrent.ScheduledExecutorService
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        return this.group.schedule((Callable) callable, delay, unit);
    }

    @Override // java.util.concurrent.ScheduledExecutorService
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return this.group.scheduleAtFixedRate(command, initialDelay, period, unit);
    }

    @Override // java.util.concurrent.ScheduledExecutorService
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return this.group.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isShutdown() {
        return this.group.isShutdown();
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isTerminated() {
        return this.group.isTerminated();
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return this.group.awaitTermination(timeout, unit);
    }

    @Override // java.util.concurrent.ExecutorService
    public <T> List<java.util.concurrent.Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return this.group.invokeAll(tasks);
    }

    @Override // java.util.concurrent.ExecutorService
    public <T> List<java.util.concurrent.Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return this.group.invokeAll(tasks, timeout, unit);
    }

    @Override // java.util.concurrent.ExecutorService
    public <T> T invokeAny(Collection<? extends Callable<T>> collection) throws InterruptedException, ExecutionException {
        return (T) this.group.invokeAny(collection);
    }

    @Override // java.util.concurrent.ExecutorService
    public <T> T invokeAny(Collection<? extends Callable<T>> collection, long j, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        return (T) this.group.invokeAny(collection, j, timeUnit);
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable command) {
        this.group.execute(command);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class NonStickyOrderedEventExecutor extends AbstractEventExecutor implements Runnable, OrderedEventExecutor {
        private static final int NONE = 0;
        private static final int RUNNING = 2;
        private static final int SUBMITTED = 1;
        private final AtomicReference<Thread> executingThread;
        private final EventExecutor executor;
        private final int maxTaskExecutePerRun;
        private final AtomicInteger state;
        private final Queue<Runnable> tasks;

        NonStickyOrderedEventExecutor(EventExecutor executor, int maxTaskExecutePerRun) {
            super(executor);
            this.tasks = PlatformDependent.newMpscQueue();
            this.state = new AtomicInteger();
            this.executingThread = new AtomicReference<>();
            this.executor = executor;
            this.maxTaskExecutePerRun = maxTaskExecutePerRun;
        }

        /* JADX WARN: Code restructure failed: missing block: B:34:0x005e, code lost:            androidx.lifecycle.LifecycleKt$$ExternalSyntheticBackportWithForwarding0.m(r8.executingThread, r0, null);     */
        /* JADX WARN: Code restructure failed: missing block: B:35:0x0063, code lost:            return;     */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void run() {
            /*
                r8 = this;
                java.util.concurrent.atomic.AtomicInteger r0 = r8.state
                r1 = 1
                r2 = 2
                boolean r0 = r0.compareAndSet(r1, r2)
                if (r0 != 0) goto Lb
                return
            Lb:
                java.lang.Thread r0 = java.lang.Thread.currentThread()
                java.util.concurrent.atomic.AtomicReference<java.lang.Thread> r3 = r8.executingThread
                r3.set(r0)
            L14:
                r3 = 0
            L15:
                r4 = 0
                r5 = 0
                int r6 = r8.maxTaskExecutePerRun     // Catch: java.lang.Throwable -> L64
                if (r3 >= r6) goto L2c
                java.util.Queue<java.lang.Runnable> r6 = r8.tasks     // Catch: java.lang.Throwable -> L64
                java.lang.Object r6 = r6.poll()     // Catch: java.lang.Throwable -> L64
                java.lang.Runnable r6 = (java.lang.Runnable) r6     // Catch: java.lang.Throwable -> L64
                if (r6 != 0) goto L26
                goto L2c
            L26:
                safeExecute(r6)     // Catch: java.lang.Throwable -> L64
                int r3 = r3 + 1
                goto L15
            L2c:
                int r6 = r8.maxTaskExecutePerRun
                if (r3 != r6) goto L47
                java.util.concurrent.atomic.AtomicInteger r5 = r8.state     // Catch: java.lang.Throwable -> L40
                r5.set(r1)     // Catch: java.lang.Throwable -> L40
                java.util.concurrent.atomic.AtomicReference<java.lang.Thread> r5 = r8.executingThread     // Catch: java.lang.Throwable -> L40
                androidx.lifecycle.LifecycleKt$$ExternalSyntheticBackportWithForwarding0.m(r5, r0, r4)     // Catch: java.lang.Throwable -> L40
                io.netty.util.concurrent.EventExecutor r4 = r8.executor     // Catch: java.lang.Throwable -> L40
                r4.execute(r8)     // Catch: java.lang.Throwable -> L40
                return
            L40:
                r4 = move-exception
                java.util.concurrent.atomic.AtomicInteger r5 = r8.state
                r5.set(r2)
                goto L5d
            L47:
                java.util.concurrent.atomic.AtomicInteger r6 = r8.state
                r6.set(r5)
                java.util.Queue<java.lang.Runnable> r6 = r8.tasks
                boolean r6 = r6.isEmpty()
                if (r6 != 0) goto L5e
                java.util.concurrent.atomic.AtomicInteger r6 = r8.state
                boolean r5 = r6.compareAndSet(r5, r2)
                if (r5 != 0) goto L5d
                goto L5e
            L5d:
                goto L14
            L5e:
                java.util.concurrent.atomic.AtomicReference<java.lang.Thread> r1 = r8.executingThread
                androidx.lifecycle.LifecycleKt$$ExternalSyntheticBackportWithForwarding0.m(r1, r0, r4)
            L63:
                return
            L64:
                r6 = move-exception
                int r7 = r8.maxTaskExecutePerRun
                if (r3 != r7) goto L80
                java.util.concurrent.atomic.AtomicInteger r5 = r8.state     // Catch: java.lang.Throwable -> L79
                r5.set(r1)     // Catch: java.lang.Throwable -> L79
                java.util.concurrent.atomic.AtomicReference<java.lang.Thread> r1 = r8.executingThread     // Catch: java.lang.Throwable -> L79
                androidx.lifecycle.LifecycleKt$$ExternalSyntheticBackportWithForwarding0.m(r1, r0, r4)     // Catch: java.lang.Throwable -> L79
                io.netty.util.concurrent.EventExecutor r1 = r8.executor     // Catch: java.lang.Throwable -> L79
                r1.execute(r8)     // Catch: java.lang.Throwable -> L79
                return
            L79:
                r1 = move-exception
                java.util.concurrent.atomic.AtomicInteger r4 = r8.state
                r4.set(r2)
                goto L96
            L80:
                java.util.concurrent.atomic.AtomicInteger r1 = r8.state
                r1.set(r5)
                java.util.Queue<java.lang.Runnable> r1 = r8.tasks
                boolean r1 = r1.isEmpty()
                if (r1 != 0) goto L97
                java.util.concurrent.atomic.AtomicInteger r1 = r8.state
                boolean r1 = r1.compareAndSet(r5, r2)
                if (r1 != 0) goto L96
                goto L97
            L96:
                throw r6
            L97:
                java.util.concurrent.atomic.AtomicReference<java.lang.Thread> r1 = r8.executingThread
                androidx.lifecycle.LifecycleKt$$ExternalSyntheticBackportWithForwarding0.m(r1, r0, r4)
                goto L63
            */
            throw new UnsupportedOperationException("Method not decompiled: io.netty.util.concurrent.NonStickyEventExecutorGroup.NonStickyOrderedEventExecutor.run():void");
        }

        @Override // io.netty.util.concurrent.EventExecutor
        public boolean inEventLoop(Thread thread) {
            return this.executingThread.get() == thread;
        }

        @Override // io.netty.util.concurrent.EventExecutorGroup
        public boolean isShuttingDown() {
            return this.executor.isShutdown();
        }

        @Override // io.netty.util.concurrent.EventExecutorGroup
        public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit) {
            return this.executor.shutdownGracefully(quietPeriod, timeout, unit);
        }

        @Override // io.netty.util.concurrent.EventExecutorGroup
        public Future<?> terminationFuture() {
            return this.executor.terminationFuture();
        }

        @Override // io.netty.util.concurrent.AbstractEventExecutor, java.util.concurrent.ExecutorService, io.netty.util.concurrent.EventExecutorGroup
        public void shutdown() {
            this.executor.shutdown();
        }

        @Override // java.util.concurrent.ExecutorService
        public boolean isShutdown() {
            return this.executor.isShutdown();
        }

        @Override // java.util.concurrent.ExecutorService
        public boolean isTerminated() {
            return this.executor.isTerminated();
        }

        @Override // java.util.concurrent.ExecutorService
        public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
            return this.executor.awaitTermination(timeout, unit);
        }

        @Override // java.util.concurrent.Executor
        public void execute(Runnable command) {
            if (!this.tasks.offer(command)) {
                throw new RejectedExecutionException();
            }
            if (this.state.compareAndSet(0, 1)) {
                this.executor.execute(this);
            }
        }
    }
}
