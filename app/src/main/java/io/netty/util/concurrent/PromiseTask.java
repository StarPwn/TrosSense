package io.netty.util.concurrent;

import io.netty.util.internal.StringUtil;
import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public class PromiseTask<V> extends DefaultPromise<V> implements RunnableFuture<V> {
    private Object task;
    private static final Runnable COMPLETED = new SentinelRunnable("COMPLETED");
    private static final Runnable CANCELLED = new SentinelRunnable("CANCELLED");
    private static final Runnable FAILED = new SentinelRunnable("FAILED");

    /* loaded from: classes4.dex */
    private static final class RunnableAdapter<T> implements Callable<T> {
        final T result;
        final Runnable task;

        RunnableAdapter(Runnable task, T result) {
            this.task = task;
            this.result = result;
        }

        @Override // java.util.concurrent.Callable
        public T call() {
            this.task.run();
            return this.result;
        }

        public String toString() {
            return "Callable(task: " + this.task + ", result: " + this.result + ')';
        }
    }

    /* loaded from: classes4.dex */
    private static class SentinelRunnable implements Runnable {
        private final String name;

        SentinelRunnable(String name) {
            this.name = name;
        }

        @Override // java.lang.Runnable
        public void run() {
        }

        public String toString() {
            return this.name;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PromiseTask(EventExecutor executor, Runnable runnable, V result) {
        super(executor);
        this.task = result == null ? runnable : new RunnableAdapter(runnable, result);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PromiseTask(EventExecutor executor, Runnable runnable) {
        super(executor);
        this.task = runnable;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PromiseTask(EventExecutor executor, Callable<V> callable) {
        super(executor);
        this.task = callable;
    }

    public final int hashCode() {
        return System.identityHashCode(this);
    }

    public final boolean equals(Object obj) {
        return this == obj;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public V runTask() throws Throwable {
        Object obj = this.task;
        if (obj instanceof Callable) {
            return (V) ((Callable) obj).call();
        }
        ((Runnable) obj).run();
        return null;
    }

    @Override // java.util.concurrent.RunnableFuture, java.lang.Runnable
    public void run() {
        try {
            if (setUncancellableInternal()) {
                V result = runTask();
                setSuccessInternal(result);
            }
        } catch (Throwable e) {
            setFailureInternal(e);
        }
    }

    private boolean clearTaskAfterCompletion(boolean done, Runnable result) {
        if (done) {
            this.task = result;
        }
        return done;
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Promise, io.netty.channel.ChannelPromise
    public final Promise<V> setFailure(Throwable cause) {
        throw new IllegalStateException();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final Promise<V> setFailureInternal(Throwable cause) {
        super.setFailure(cause);
        clearTaskAfterCompletion(true, FAILED);
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Promise
    public final boolean tryFailure(Throwable cause) {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean tryFailureInternal(Throwable cause) {
        return clearTaskAfterCompletion(super.tryFailure(cause), FAILED);
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Promise, io.netty.util.concurrent.ProgressivePromise
    public final Promise<V> setSuccess(V result) {
        throw new IllegalStateException();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final Promise<V> setSuccessInternal(V result) {
        super.setSuccess(result);
        clearTaskAfterCompletion(true, COMPLETED);
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Promise
    public final boolean trySuccess(V result) {
        return false;
    }

    protected final boolean trySuccessInternal(V result) {
        return clearTaskAfterCompletion(super.trySuccess(result), COMPLETED);
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Promise
    public final boolean setUncancellable() {
        throw new IllegalStateException();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean setUncancellableInternal() {
        return super.setUncancellable();
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future, java.util.concurrent.Future
    public boolean cancel(boolean mayInterruptIfRunning) {
        return clearTaskAfterCompletion(super.cancel(mayInterruptIfRunning), CANCELLED);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.util.concurrent.DefaultPromise
    public StringBuilder toStringBuilder() {
        StringBuilder buf = super.toStringBuilder();
        buf.setCharAt(buf.length() - 1, StringUtil.COMMA);
        return buf.append(" task: ").append(this.task).append(')');
    }
}
