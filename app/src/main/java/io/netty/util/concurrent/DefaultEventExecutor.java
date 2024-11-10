package io.netty.util.concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

/* loaded from: classes4.dex */
public final class DefaultEventExecutor extends SingleThreadEventExecutor {
    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public DefaultEventExecutor() {
        this((EventExecutorGroup) null);
    }

    public DefaultEventExecutor(ThreadFactory threadFactory) {
        this((EventExecutorGroup) null, threadFactory);
    }

    public DefaultEventExecutor(Executor executor) {
        this((EventExecutorGroup) null, executor);
    }

    public DefaultEventExecutor(EventExecutorGroup parent) {
        this(parent, new DefaultThreadFactory((Class<?>) DefaultEventExecutor.class));
    }

    public DefaultEventExecutor(EventExecutorGroup parent, ThreadFactory threadFactory) {
        super(parent, threadFactory, true);
    }

    public DefaultEventExecutor(EventExecutorGroup parent, Executor executor) {
        super(parent, executor, true);
    }

    public DefaultEventExecutor(EventExecutorGroup parent, ThreadFactory threadFactory, int maxPendingTasks, RejectedExecutionHandler rejectedExecutionHandler) {
        super(parent, threadFactory, true, maxPendingTasks, rejectedExecutionHandler);
    }

    public DefaultEventExecutor(EventExecutorGroup parent, Executor executor, int maxPendingTasks, RejectedExecutionHandler rejectedExecutionHandler) {
        super(parent, executor, true, maxPendingTasks, rejectedExecutionHandler);
    }

    @Override // io.netty.util.concurrent.SingleThreadEventExecutor
    protected void run() {
        do {
            Runnable task = takeTask();
            if (task != null) {
                runTask(task);
                updateLastExecutionTime();
            }
        } while (!confirmShutdown());
    }
}
