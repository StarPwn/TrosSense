package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.AbstractFuture;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.RunnableFuture;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
class TrustedListenableFutureTask<V> extends AbstractFuture.TrustedFuture<V> implements RunnableFuture<V> {
    private TrustedListenableFutureTask<V>.TrustedFutureInterruptibleTask task;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <V> TrustedListenableFutureTask<V> create(Callable<V> callable) {
        return new TrustedListenableFutureTask<>(callable);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <V> TrustedListenableFutureTask<V> create(Runnable runnable, @Nullable V result) {
        return new TrustedListenableFutureTask<>(Executors.callable(runnable, result));
    }

    TrustedListenableFutureTask(Callable<V> callable) {
        this.task = new TrustedFutureInterruptibleTask(callable);
    }

    @Override // java.util.concurrent.RunnableFuture, java.lang.Runnable
    public void run() {
        TrustedListenableFutureTask<V>.TrustedFutureInterruptibleTask localTask = this.task;
        if (localTask != null) {
            localTask.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.common.util.concurrent.AbstractFuture
    public void afterDone() {
        TrustedListenableFutureTask<V>.TrustedFutureInterruptibleTask localTask;
        super.afterDone();
        if (wasInterrupted() && (localTask = this.task) != null) {
            localTask.interruptTask();
        }
        this.task = null;
    }

    public String toString() {
        return super.toString() + " (delegate = " + this.task + ")";
    }

    /* loaded from: classes.dex */
    private final class TrustedFutureInterruptibleTask extends InterruptibleTask {
        private final Callable<V> callable;

        TrustedFutureInterruptibleTask(Callable<V> callable) {
            this.callable = (Callable) Preconditions.checkNotNull(callable);
        }

        @Override // com.google.common.util.concurrent.InterruptibleTask
        void runInterruptibly() {
            if (!TrustedListenableFutureTask.this.isDone()) {
                try {
                    TrustedListenableFutureTask.this.set(this.callable.call());
                } catch (Throwable t) {
                    TrustedListenableFutureTask.this.setException(t);
                }
            }
        }

        @Override // com.google.common.util.concurrent.InterruptibleTask
        boolean wasInterrupted() {
            return TrustedListenableFutureTask.this.wasInterrupted();
        }

        public String toString() {
            return this.callable.toString();
        }
    }
}
