package io.netty.util.concurrent;

import io.netty.util.internal.ObjectUtil;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/* loaded from: classes4.dex */
public final class RejectedExecutionHandlers {
    private static final RejectedExecutionHandler REJECT = new RejectedExecutionHandler() { // from class: io.netty.util.concurrent.RejectedExecutionHandlers.1
        @Override // io.netty.util.concurrent.RejectedExecutionHandler
        public void rejected(Runnable task, SingleThreadEventExecutor executor) {
            throw new RejectedExecutionException();
        }
    };

    private RejectedExecutionHandlers() {
    }

    public static RejectedExecutionHandler reject() {
        return REJECT;
    }

    public static RejectedExecutionHandler backoff(final int retries, long backoffAmount, TimeUnit unit) {
        ObjectUtil.checkPositive(retries, "retries");
        final long backOffNanos = unit.toNanos(backoffAmount);
        return new RejectedExecutionHandler() { // from class: io.netty.util.concurrent.RejectedExecutionHandlers.2
            @Override // io.netty.util.concurrent.RejectedExecutionHandler
            public void rejected(Runnable task, SingleThreadEventExecutor executor) {
                if (!executor.inEventLoop()) {
                    for (int i = 0; i < retries; i++) {
                        executor.wakeup(false);
                        LockSupport.parkNanos(backOffNanos);
                        if (executor.offerTask(task)) {
                            return;
                        }
                    }
                }
                throw new RejectedExecutionException();
            }
        };
    }
}
