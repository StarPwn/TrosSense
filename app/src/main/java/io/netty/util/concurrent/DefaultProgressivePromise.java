package io.netty.util.concurrent;

import androidx.core.app.NotificationCompat;
import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public class DefaultProgressivePromise<V> extends DefaultPromise<V> implements ProgressivePromise<V> {
    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Promise, io.netty.util.concurrent.ProgressivePromise
    public /* bridge */ /* synthetic */ Promise setSuccess(Object obj) {
        return setSuccess((DefaultProgressivePromise<V>) obj);
    }

    public DefaultProgressivePromise(EventExecutor executor) {
        super(executor);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DefaultProgressivePromise() {
    }

    public ProgressivePromise<V> setProgress(long progress, long total) {
        if (total < 0) {
            total = -1;
            ObjectUtil.checkPositiveOrZero(progress, NotificationCompat.CATEGORY_PROGRESS);
        } else if (progress < 0 || progress > total) {
            throw new IllegalArgumentException("progress: " + progress + " (expected: 0 <= progress <= total (" + total + "))");
        }
        if (isDone()) {
            throw new IllegalStateException("complete already");
        }
        notifyProgressiveListeners(progress, total);
        return this;
    }

    @Override // io.netty.util.concurrent.ProgressivePromise
    public boolean tryProgress(long progress, long total) {
        if (total < 0) {
            total = -1;
            if (progress < 0 || isDone()) {
                return false;
            }
        } else if (progress < 0 || progress > total || isDone()) {
            return false;
        }
        notifyProgressiveListeners(progress, total);
        return true;
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    public ProgressivePromise<V> addListener(GenericFutureListener<? extends Future<? super V>> listener) {
        super.addListener((GenericFutureListener) listener);
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    public ProgressivePromise<V> addListeners(GenericFutureListener<? extends Future<? super V>>... listeners) {
        super.addListeners((GenericFutureListener[]) listeners);
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    public ProgressivePromise<V> removeListener(GenericFutureListener<? extends Future<? super V>> listener) {
        super.removeListener((GenericFutureListener) listener);
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    public ProgressivePromise<V> removeListeners(GenericFutureListener<? extends Future<? super V>>... listeners) {
        super.removeListeners((GenericFutureListener[]) listeners);
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    public ProgressivePromise<V> sync() throws InterruptedException {
        super.sync();
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    public ProgressivePromise<V> syncUninterruptibly() {
        super.syncUninterruptibly();
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    public ProgressivePromise<V> await() throws InterruptedException {
        super.await();
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    public ProgressivePromise<V> awaitUninterruptibly() {
        super.awaitUninterruptibly();
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Promise, io.netty.util.concurrent.ProgressivePromise
    public ProgressivePromise<V> setSuccess(V result) {
        super.setSuccess((DefaultProgressivePromise<V>) result);
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Promise, io.netty.channel.ChannelPromise
    public ProgressivePromise<V> setFailure(Throwable cause) {
        super.setFailure(cause);
        return this;
    }
}
