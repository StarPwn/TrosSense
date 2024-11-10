package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import javax.annotation.Nullable;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class CombinedFuture<V> extends AggregateFuture<Object, V> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public CombinedFuture(ImmutableCollection<? extends ListenableFuture<?>> futures, boolean allMustSucceed, Executor listenerExecutor, AsyncCallable<V> callable) {
        init(new CombinedFutureRunningState(futures, allMustSucceed, new AsyncCallableInterruptibleTask(callable, listenerExecutor)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CombinedFuture(ImmutableCollection<? extends ListenableFuture<?>> futures, boolean allMustSucceed, Executor listenerExecutor, Callable<V> callable) {
        init(new CombinedFutureRunningState(futures, allMustSucceed, new CallableInterruptibleTask(callable, listenerExecutor)));
    }

    /* loaded from: classes.dex */
    private final class CombinedFutureRunningState extends AggregateFuture<Object, V>.RunningState {
        private CombinedFuture<V>.CombinedFutureInterruptibleTask task;

        CombinedFutureRunningState(ImmutableCollection<? extends ListenableFuture<? extends Object>> futures, boolean allMustSucceed, CombinedFuture<V>.CombinedFutureInterruptibleTask task) {
            super(futures, allMustSucceed, false);
            this.task = task;
        }

        @Override // com.google.common.util.concurrent.AggregateFuture.RunningState
        void collectOneValue(boolean allMustSucceed, int index, @Nullable Object returnValue) {
        }

        @Override // com.google.common.util.concurrent.AggregateFuture.RunningState
        void handleAllCompleted() {
            CombinedFuture<V>.CombinedFutureInterruptibleTask localTask = this.task;
            if (localTask != null) {
                localTask.execute();
            } else {
                Preconditions.checkState(CombinedFuture.this.isDone());
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.util.concurrent.AggregateFuture.RunningState
        public void releaseResourcesAfterFailure() {
            super.releaseResourcesAfterFailure();
            this.task = null;
        }

        @Override // com.google.common.util.concurrent.AggregateFuture.RunningState
        void interruptTask() {
            CombinedFuture<V>.CombinedFutureInterruptibleTask localTask = this.task;
            if (localTask != null) {
                localTask.interruptTask();
            }
        }
    }

    /* loaded from: classes.dex */
    private abstract class CombinedFutureInterruptibleTask extends InterruptibleTask {
        private final Executor listenerExecutor;
        volatile boolean thrownByExecute = true;

        abstract void setValue() throws Exception;

        public CombinedFutureInterruptibleTask(Executor listenerExecutor) {
            this.listenerExecutor = (Executor) Preconditions.checkNotNull(listenerExecutor);
        }

        @Override // com.google.common.util.concurrent.InterruptibleTask
        final void runInterruptibly() {
            this.thrownByExecute = false;
            if (!CombinedFuture.this.isDone()) {
                try {
                    setValue();
                } catch (CancellationException e) {
                    CombinedFuture.this.cancel(false);
                } catch (ExecutionException e2) {
                    CombinedFuture.this.setException(e2.getCause());
                } catch (Throwable e3) {
                    CombinedFuture.this.setException(e3);
                }
            }
        }

        @Override // com.google.common.util.concurrent.InterruptibleTask
        final boolean wasInterrupted() {
            return CombinedFuture.this.wasInterrupted();
        }

        final void execute() {
            try {
                this.listenerExecutor.execute(this);
            } catch (RejectedExecutionException e) {
                if (this.thrownByExecute) {
                    CombinedFuture.this.setException(e);
                }
            }
        }
    }

    /* loaded from: classes.dex */
    private final class AsyncCallableInterruptibleTask extends CombinedFuture<V>.CombinedFutureInterruptibleTask {
        private final AsyncCallable<V> callable;

        public AsyncCallableInterruptibleTask(AsyncCallable<V> callable, Executor listenerExecutor) {
            super(listenerExecutor);
            this.callable = (AsyncCallable) Preconditions.checkNotNull(callable);
        }

        @Override // com.google.common.util.concurrent.CombinedFuture.CombinedFutureInterruptibleTask
        void setValue() throws Exception {
            CombinedFuture.this.setFuture(this.callable.call());
        }
    }

    /* loaded from: classes.dex */
    private final class CallableInterruptibleTask extends CombinedFuture<V>.CombinedFutureInterruptibleTask {
        private final Callable<V> callable;

        public CallableInterruptibleTask(Callable<V> callable, Executor listenerExecutor) {
            super(listenerExecutor);
            this.callable = (Callable) Preconditions.checkNotNull(callable);
        }

        @Override // com.google.common.util.concurrent.CombinedFuture.CombinedFutureInterruptibleTask
        void setValue() throws Exception {
            CombinedFuture.this.set(this.callable.call());
        }
    }
}
