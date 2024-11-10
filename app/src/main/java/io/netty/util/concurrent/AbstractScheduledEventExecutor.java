package io.netty.util.concurrent;

import io.netty.util.internal.DefaultPriorityQueue;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PriorityQueue;
import java.util.Comparator;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/* loaded from: classes4.dex */
public abstract class AbstractScheduledEventExecutor extends AbstractEventExecutor {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final Comparator<ScheduledFutureTask<?>> SCHEDULED_FUTURE_TASK_COMPARATOR = new Comparator<ScheduledFutureTask<?>>() { // from class: io.netty.util.concurrent.AbstractScheduledEventExecutor.1
        @Override // java.util.Comparator
        public int compare(ScheduledFutureTask<?> o1, ScheduledFutureTask<?> o2) {
            return o1.compareTo((Delayed) o2);
        }
    };
    private static final long START_TIME = System.nanoTime();
    static final Runnable WAKEUP_TASK = new Runnable() { // from class: io.netty.util.concurrent.AbstractScheduledEventExecutor.2
        @Override // java.lang.Runnable
        public void run() {
        }
    };
    long nextTaskId;
    PriorityQueue<ScheduledFutureTask<?>> scheduledTaskQueue;

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractScheduledEventExecutor() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractScheduledEventExecutor(EventExecutorGroup parent) {
        super(parent);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public long getCurrentTimeNanos() {
        return defaultCurrentTimeNanos();
    }

    @Deprecated
    protected static long nanoTime() {
        return defaultCurrentTimeNanos();
    }

    static long defaultCurrentTimeNanos() {
        return System.nanoTime() - START_TIME;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long deadlineNanos(long nanoTime, long delay) {
        long deadlineNanos = nanoTime + delay;
        if (deadlineNanos < 0) {
            return Long.MAX_VALUE;
        }
        return deadlineNanos;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static long deadlineToDelayNanos(long deadlineNanos) {
        return ScheduledFutureTask.deadlineToDelayNanos(defaultCurrentTimeNanos(), deadlineNanos);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static long initialNanoTime() {
        return START_TIME;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PriorityQueue<ScheduledFutureTask<?>> scheduledTaskQueue() {
        if (this.scheduledTaskQueue == null) {
            this.scheduledTaskQueue = new DefaultPriorityQueue(SCHEDULED_FUTURE_TASK_COMPARATOR, 11);
        }
        return this.scheduledTaskQueue;
    }

    private static boolean isNullOrEmpty(Queue<ScheduledFutureTask<?>> queue) {
        return queue == null || queue.isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void cancelScheduledTasks() {
        if (!inEventLoop()) {
            throw new AssertionError();
        }
        PriorityQueue<ScheduledFutureTask<?>> scheduledTaskQueue = this.scheduledTaskQueue;
        if (isNullOrEmpty(scheduledTaskQueue)) {
            return;
        }
        ScheduledFutureTask<?>[] scheduledTasks = (ScheduledFutureTask[]) scheduledTaskQueue.toArray(new ScheduledFutureTask[0]);
        for (ScheduledFutureTask<?> task : scheduledTasks) {
            task.cancelWithoutRemove(false);
        }
        scheduledTaskQueue.clearIgnoringIndexes();
    }

    protected final Runnable pollScheduledTask() {
        return pollScheduledTask(getCurrentTimeNanos());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final Runnable pollScheduledTask(long nanoTime) {
        if (!inEventLoop()) {
            throw new AssertionError();
        }
        ScheduledFutureTask<?> scheduledTask = peekScheduledTask();
        if (scheduledTask == null || scheduledTask.deadlineNanos() - nanoTime > 0) {
            return null;
        }
        this.scheduledTaskQueue.remove();
        scheduledTask.setConsumed();
        return scheduledTask;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final long nextScheduledTaskNano() {
        ScheduledFutureTask<?> scheduledTask = peekScheduledTask();
        if (scheduledTask != null) {
            return scheduledTask.delayNanos();
        }
        return -1L;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final long nextScheduledTaskDeadlineNanos() {
        ScheduledFutureTask<?> scheduledTask = peekScheduledTask();
        if (scheduledTask != null) {
            return scheduledTask.deadlineNanos();
        }
        return -1L;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final ScheduledFutureTask<?> peekScheduledTask() {
        Queue<ScheduledFutureTask<?>> scheduledTaskQueue = this.scheduledTaskQueue;
        if (scheduledTaskQueue != null) {
            return scheduledTaskQueue.peek();
        }
        return null;
    }

    protected final boolean hasScheduledTasks() {
        ScheduledFutureTask<?> scheduledTask = peekScheduledTask();
        return scheduledTask != null && scheduledTask.deadlineNanos() <= getCurrentTimeNanos();
    }

    @Override // io.netty.util.concurrent.AbstractEventExecutor, java.util.concurrent.ScheduledExecutorService
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        ObjectUtil.checkNotNull(command, "command");
        ObjectUtil.checkNotNull(unit, "unit");
        if (delay < 0) {
            delay = 0;
        }
        validateScheduled0(delay, unit);
        return schedule(new ScheduledFutureTask(this, command, deadlineNanos(getCurrentTimeNanos(), unit.toNanos(delay))));
    }

    @Override // io.netty.util.concurrent.AbstractEventExecutor, java.util.concurrent.ScheduledExecutorService
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        ObjectUtil.checkNotNull(callable, "callable");
        ObjectUtil.checkNotNull(unit, "unit");
        if (delay < 0) {
            delay = 0;
        }
        validateScheduled0(delay, unit);
        return schedule(new ScheduledFutureTask<>(this, callable, deadlineNanos(getCurrentTimeNanos(), unit.toNanos(delay))));
    }

    @Override // io.netty.util.concurrent.AbstractEventExecutor, java.util.concurrent.ScheduledExecutorService
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        ObjectUtil.checkNotNull(command, "command");
        ObjectUtil.checkNotNull(unit, "unit");
        if (initialDelay < 0) {
            throw new IllegalArgumentException(String.format("initialDelay: %d (expected: >= 0)", Long.valueOf(initialDelay)));
        }
        if (period <= 0) {
            throw new IllegalArgumentException(String.format("period: %d (expected: > 0)", Long.valueOf(period)));
        }
        validateScheduled0(initialDelay, unit);
        validateScheduled0(period, unit);
        return schedule(new ScheduledFutureTask(this, command, deadlineNanos(getCurrentTimeNanos(), unit.toNanos(initialDelay)), unit.toNanos(period)));
    }

    @Override // io.netty.util.concurrent.AbstractEventExecutor, java.util.concurrent.ScheduledExecutorService
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        ObjectUtil.checkNotNull(command, "command");
        ObjectUtil.checkNotNull(unit, "unit");
        if (initialDelay < 0) {
            throw new IllegalArgumentException(String.format("initialDelay: %d (expected: >= 0)", Long.valueOf(initialDelay)));
        }
        if (delay <= 0) {
            throw new IllegalArgumentException(String.format("delay: %d (expected: > 0)", Long.valueOf(delay)));
        }
        validateScheduled0(initialDelay, unit);
        validateScheduled0(delay, unit);
        return schedule(new ScheduledFutureTask(this, command, deadlineNanos(getCurrentTimeNanos(), unit.toNanos(initialDelay)), -unit.toNanos(delay)));
    }

    private void validateScheduled0(long amount, TimeUnit unit) {
        validateScheduled(amount, unit);
    }

    @Deprecated
    protected void validateScheduled(long amount, TimeUnit unit) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void scheduleFromEventLoop(ScheduledFutureTask<?> task) {
        PriorityQueue<ScheduledFutureTask<?>> scheduledTaskQueue = scheduledTaskQueue();
        long j = this.nextTaskId + 1;
        this.nextTaskId = j;
        scheduledTaskQueue.add(task.setId(j));
    }

    private <V> ScheduledFuture<V> schedule(ScheduledFutureTask<V> task) {
        if (inEventLoop()) {
            scheduleFromEventLoop(task);
        } else {
            long deadlineNanos = task.deadlineNanos();
            if (beforeScheduledTaskSubmitted(deadlineNanos)) {
                execute(task);
            } else {
                lazyExecute(task);
                if (afterScheduledTaskSubmitted(deadlineNanos)) {
                    execute(WAKEUP_TASK);
                }
            }
        }
        return task;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void removeScheduled(ScheduledFutureTask<?> task) {
        if (!task.isCancelled()) {
            throw new AssertionError();
        }
        if (inEventLoop()) {
            scheduledTaskQueue().removeTyped(task);
        } else {
            lazyExecute(task);
        }
    }

    protected boolean beforeScheduledTaskSubmitted(long deadlineNanos) {
        return true;
    }

    protected boolean afterScheduledTaskSubmitted(long deadlineNanos) {
        return true;
    }
}
