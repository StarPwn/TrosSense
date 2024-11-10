package io.netty.util.concurrent;

import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.ThreadExecutorMap;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes4.dex */
public final class GlobalEventExecutor extends AbstractScheduledEventExecutor implements OrderedEventExecutor {
    public static final GlobalEventExecutor INSTANCE;
    private static final long SCHEDULE_QUIET_PERIOD_INTERVAL;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) GlobalEventExecutor.class);
    volatile Thread thread;
    final ThreadFactory threadFactory;
    final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue();
    final ScheduledFutureTask<Void> quietPeriodTask = new ScheduledFutureTask<>(this, Executors.callable(new Runnable() { // from class: io.netty.util.concurrent.GlobalEventExecutor.1
        @Override // java.lang.Runnable
        public void run() {
        }
    }, null), deadlineNanos(getCurrentTimeNanos(), SCHEDULE_QUIET_PERIOD_INTERVAL), -SCHEDULE_QUIET_PERIOD_INTERVAL);
    private final TaskRunner taskRunner = new TaskRunner();
    private final AtomicBoolean started = new AtomicBoolean();
    private final Future<?> terminationFuture = new FailedFuture(this, new UnsupportedOperationException());

    static {
        int quietPeriod = SystemPropertyUtil.getInt("io.netty.globalEventExecutor.quietPeriodSeconds", 1);
        if (quietPeriod <= 0) {
            quietPeriod = 1;
        }
        logger.debug("-Dio.netty.globalEventExecutor.quietPeriodSeconds: {}", Integer.valueOf(quietPeriod));
        SCHEDULE_QUIET_PERIOD_INTERVAL = TimeUnit.SECONDS.toNanos(quietPeriod);
        INSTANCE = new GlobalEventExecutor();
    }

    private GlobalEventExecutor() {
        scheduledTaskQueue().add(this.quietPeriodTask);
        this.threadFactory = ThreadExecutorMap.apply(new DefaultThreadFactory(DefaultThreadFactory.toPoolName(getClass()), false, 5, null), this);
    }

    Runnable takeTask() {
        Runnable task;
        BlockingQueue<Runnable> taskQueue = this.taskQueue;
        do {
            ScheduledFutureTask<?> scheduledTask = peekScheduledTask();
            if (scheduledTask == null) {
                try {
                    Runnable task2 = taskQueue.take();
                    return task2;
                } catch (InterruptedException e) {
                    return null;
                }
            }
            long delayNanos = scheduledTask.delayNanos();
            task = null;
            if (delayNanos > 0) {
                try {
                    task = taskQueue.poll(delayNanos, TimeUnit.NANOSECONDS);
                } catch (InterruptedException e2) {
                    return null;
                }
            }
            if (task == null) {
                fetchFromScheduledTaskQueue();
                Runnable task3 = taskQueue.poll();
                task = task3;
            }
        } while (task == null);
        return task;
    }

    private void fetchFromScheduledTaskQueue() {
        long nanoTime = getCurrentTimeNanos();
        Runnable scheduledTask = pollScheduledTask(nanoTime);
        while (scheduledTask != null) {
            this.taskQueue.add(scheduledTask);
            scheduledTask = pollScheduledTask(nanoTime);
        }
    }

    public int pendingTasks() {
        return this.taskQueue.size();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void addTask(Runnable task) {
        this.taskQueue.add(ObjectUtil.checkNotNull(task, "task"));
    }

    @Override // io.netty.util.concurrent.EventExecutor
    public boolean inEventLoop(Thread thread) {
        return thread == this.thread;
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup
    public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit) {
        return terminationFuture();
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup
    public Future<?> terminationFuture() {
        return this.terminationFuture;
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

    public boolean awaitInactivity(long timeout, TimeUnit unit) throws InterruptedException {
        ObjectUtil.checkNotNull(unit, "unit");
        Thread thread = this.thread;
        if (thread == null) {
            throw new IllegalStateException("thread was not started");
        }
        thread.join(unit.toMillis(timeout));
        return !thread.isAlive();
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable task) {
        execute0(task);
    }

    private void execute0(Runnable task) {
        addTask((Runnable) ObjectUtil.checkNotNull(task, "task"));
        if (!inEventLoop()) {
            startThread();
        }
    }

    private void startThread() {
        if (this.started.compareAndSet(false, true)) {
            final Thread t = this.threadFactory.newThread(this.taskRunner);
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: io.netty.util.concurrent.GlobalEventExecutor.2
                @Override // java.security.PrivilegedAction
                public Void run() {
                    t.setContextClassLoader(null);
                    return null;
                }
            });
            this.thread = t;
            t.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public final class TaskRunner implements Runnable {
        static final /* synthetic */ boolean $assertionsDisabled = false;

        TaskRunner() {
        }

        @Override // java.lang.Runnable
        public void run() {
            while (true) {
                Runnable task = GlobalEventExecutor.this.takeTask();
                if (task != null) {
                    try {
                        AbstractEventExecutor.runTask(task);
                    } catch (Throwable t) {
                        GlobalEventExecutor.logger.warn("Unexpected exception from the global event executor: ", t);
                    }
                    if (task != GlobalEventExecutor.this.quietPeriodTask) {
                        continue;
                    }
                }
                Queue<ScheduledFutureTask<?>> scheduledTaskQueue = GlobalEventExecutor.this.scheduledTaskQueue;
                if (GlobalEventExecutor.this.taskQueue.isEmpty() && (scheduledTaskQueue == null || scheduledTaskQueue.size() == 1)) {
                    boolean stopped = GlobalEventExecutor.this.started.compareAndSet(true, false);
                    if (!stopped) {
                        throw new AssertionError();
                    }
                    if (GlobalEventExecutor.this.taskQueue.isEmpty() || !GlobalEventExecutor.this.started.compareAndSet(false, true)) {
                        return;
                    }
                }
            }
        }
    }
}
