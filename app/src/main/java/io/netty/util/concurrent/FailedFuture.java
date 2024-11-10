package io.netty.util.concurrent;

import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;

/* loaded from: classes4.dex */
public final class FailedFuture<V> extends CompleteFuture<V> {
    private final Throwable cause;

    public FailedFuture(EventExecutor executor, Throwable cause) {
        super(executor);
        this.cause = (Throwable) ObjectUtil.checkNotNull(cause, "cause");
    }

    @Override // io.netty.util.concurrent.Future
    public Throwable cause() {
        return this.cause;
    }

    @Override // io.netty.util.concurrent.Future
    public boolean isSuccess() {
        return false;
    }

    @Override // io.netty.util.concurrent.CompleteFuture, io.netty.util.concurrent.Future
    public Future<V> sync() {
        PlatformDependent.throwException(this.cause);
        return this;
    }

    @Override // io.netty.util.concurrent.CompleteFuture, io.netty.util.concurrent.Future
    public Future<V> syncUninterruptibly() {
        PlatformDependent.throwException(this.cause);
        return this;
    }

    @Override // io.netty.util.concurrent.Future
    public V getNow() {
        return null;
    }
}
