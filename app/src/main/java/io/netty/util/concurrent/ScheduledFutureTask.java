package io.netty.util.concurrent;

import io.netty.util.internal.DefaultPriorityQueue;
import io.netty.util.internal.PriorityQueueNode;
import io.netty.util.internal.StringUtil;
import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class ScheduledFutureTask<V> extends PromiseTask<V> implements ScheduledFuture<V>, PriorityQueueNode {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private long deadlineNanos;
    private long id;
    private final long periodNanos;
    private int queueIndex;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ScheduledFutureTask(AbstractScheduledEventExecutor executor, Runnable runnable, long nanoTime) {
        super(executor, runnable);
        this.queueIndex = -1;
        this.deadlineNanos = nanoTime;
        this.periodNanos = 0L;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ScheduledFutureTask(AbstractScheduledEventExecutor executor, Runnable runnable, long nanoTime, long period) {
        super(executor, runnable);
        this.queueIndex = -1;
        this.deadlineNanos = nanoTime;
        this.periodNanos = validatePeriod(period);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ScheduledFutureTask(AbstractScheduledEventExecutor executor, Callable<V> callable, long nanoTime, long period) {
        super(executor, callable);
        this.queueIndex = -1;
        this.deadlineNanos = nanoTime;
        this.periodNanos = validatePeriod(period);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ScheduledFutureTask(AbstractScheduledEventExecutor executor, Callable<V> callable, long nanoTime) {
        super(executor, callable);
        this.queueIndex = -1;
        this.deadlineNanos = nanoTime;
        this.periodNanos = 0L;
    }

    private static long validatePeriod(long period) {
        if (period == 0) {
            throw new IllegalArgumentException("period: 0 (expected: != 0)");
        }
        return period;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ScheduledFutureTask<V> setId(long id) {
        if (this.id == 0) {
            this.id = id;
        }
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.util.concurrent.DefaultPromise
    public EventExecutor executor() {
        return super.executor();
    }

    public long deadlineNanos() {
        return this.deadlineNanos;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setConsumed() {
        if (this.periodNanos == 0) {
            if (scheduledExecutor().getCurrentTimeNanos() < this.deadlineNanos) {
                throw new AssertionError();
            }
            this.deadlineNanos = 0L;
        }
    }

    public long delayNanos() {
        return delayNanos(scheduledExecutor().getCurrentTimeNanos());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long deadlineToDelayNanos(long currentTimeNanos, long deadlineNanos) {
        if (deadlineNanos == 0) {
            return 0L;
        }
        return Math.max(0L, deadlineNanos - currentTimeNanos);
    }

    public long delayNanos(long currentTimeNanos) {
        return deadlineToDelayNanos(currentTimeNanos, this.deadlineNanos);
    }

    @Override // java.util.concurrent.Delayed
    public long getDelay(TimeUnit unit) {
        return unit.convert(delayNanos(), TimeUnit.NANOSECONDS);
    }

    @Override // java.lang.Comparable
    public int compareTo(Delayed o) {
        if (this == o) {
            return 0;
        }
        ScheduledFutureTask<?> that = (ScheduledFutureTask) o;
        long d = deadlineNanos() - that.deadlineNanos();
        if (d < 0) {
            return -1;
        }
        if (d > 0) {
            return 1;
        }
        if (this.id < that.id) {
            return -1;
        }
        if (this.id != that.id) {
            return 1;
        }
        throw new AssertionError();
    }

    @Override // io.netty.util.concurrent.PromiseTask, java.util.concurrent.RunnableFuture, java.lang.Runnable
    public void run() {
        if (!executor().inEventLoop()) {
            throw new AssertionError();
        }
        try {
            if (delayNanos() > 0) {
                if (isCancelled()) {
                    scheduledExecutor().scheduledTaskQueue().removeTyped(this);
                    return;
                } else {
                    scheduledExecutor().scheduleFromEventLoop(this);
                    return;
                }
            }
            if (this.periodNanos == 0) {
                if (setUncancellableInternal()) {
                    V result = runTask();
                    setSuccessInternal(result);
                    return;
                }
                return;
            }
            if (!isCancelled()) {
                runTask();
                if (!executor().isShutdown()) {
                    if (this.periodNanos > 0) {
                        this.deadlineNanos += this.periodNanos;
                    } else {
                        this.deadlineNanos = scheduledExecutor().getCurrentTimeNanos() - this.periodNanos;
                    }
                    if (!isCancelled()) {
                        scheduledExecutor().scheduledTaskQueue().add(this);
                    }
                }
            }
        } catch (Throwable cause) {
            setFailureInternal(cause);
        }
    }

    private AbstractScheduledEventExecutor scheduledExecutor() {
        return (AbstractScheduledEventExecutor) executor();
    }

    @Override // io.netty.util.concurrent.PromiseTask, io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future, java.util.concurrent.Future
    public boolean cancel(boolean mayInterruptIfRunning) {
        boolean canceled = super.cancel(mayInterruptIfRunning);
        if (canceled) {
            scheduledExecutor().removeScheduled(this);
        }
        return canceled;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean cancelWithoutRemove(boolean mayInterruptIfRunning) {
        return super.cancel(mayInterruptIfRunning);
    }

    @Override // io.netty.util.concurrent.PromiseTask, io.netty.util.concurrent.DefaultPromise
    protected StringBuilder toStringBuilder() {
        StringBuilder buf = super.toStringBuilder();
        buf.setCharAt(buf.length() - 1, StringUtil.COMMA);
        return buf.append(" deadline: ").append(this.deadlineNanos).append(", period: ").append(this.periodNanos).append(')');
    }

    @Override // io.netty.util.internal.PriorityQueueNode
    public int priorityQueueIndex(DefaultPriorityQueue<?> queue) {
        return this.queueIndex;
    }

    @Override // io.netty.util.internal.PriorityQueueNode
    public void priorityQueueIndex(DefaultPriorityQueue<?> queue, int i) {
        this.queueIndex = i;
    }
}
