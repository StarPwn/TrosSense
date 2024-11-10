package io.netty.util.concurrent;

import io.netty.util.internal.ObjectUtil;
import java.util.concurrent.TimeUnit;

/* loaded from: classes4.dex */
public abstract class CompleteFuture<V> extends AbstractFuture<V> {
    private final EventExecutor executor;

    /* JADX INFO: Access modifiers changed from: protected */
    public CompleteFuture(EventExecutor executor) {
        this.executor = executor;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public EventExecutor executor() {
        return this.executor;
    }

    @Override // io.netty.util.concurrent.Future
    public Future<V> addListener(GenericFutureListener<? extends Future<? super V>> listener) {
        DefaultPromise.notifyListener(executor(), this, (GenericFutureListener) ObjectUtil.checkNotNull(listener, "listener"));
        return this;
    }

    @Override // io.netty.util.concurrent.Future
    public Future<V> addListeners(GenericFutureListener<? extends Future<? super V>>... listeners) {
        for (GenericFutureListener<? extends Future<? super V>> l : (GenericFutureListener[]) ObjectUtil.checkNotNull(listeners, "listeners")) {
            if (l == null) {
                break;
            }
            DefaultPromise.notifyListener(executor(), this, l);
        }
        return this;
    }

    @Override // io.netty.util.concurrent.Future
    public Future<V> removeListener(GenericFutureListener<? extends Future<? super V>> listener) {
        return this;
    }

    @Override // io.netty.util.concurrent.Future
    public Future<V> removeListeners(GenericFutureListener<? extends Future<? super V>>... listeners) {
        return this;
    }

    @Override // io.netty.util.concurrent.Future
    public Future<V> await() throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        return this;
    }

    @Override // io.netty.util.concurrent.Future
    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        return true;
    }

    @Override // io.netty.util.concurrent.Future
    public Future<V> sync() throws InterruptedException {
        return this;
    }

    @Override // io.netty.util.concurrent.Future
    public Future<V> syncUninterruptibly() {
        return this;
    }

    @Override // io.netty.util.concurrent.Future
    public boolean await(long timeoutMillis) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        return true;
    }

    @Override // io.netty.util.concurrent.Future
    public Future<V> awaitUninterruptibly() {
        return this;
    }

    @Override // io.netty.util.concurrent.Future
    public boolean awaitUninterruptibly(long timeout, TimeUnit unit) {
        return true;
    }

    @Override // io.netty.util.concurrent.Future
    public boolean awaitUninterruptibly(long timeoutMillis) {
        return true;
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        return true;
    }

    @Override // io.netty.util.concurrent.Future
    public boolean isCancellable() {
        return false;
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        return false;
    }

    @Override // io.netty.util.concurrent.Future, java.util.concurrent.Future
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }
}
