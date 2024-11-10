package io.netty.resolver.dns;

import io.netty.resolver.NameResolver;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

/* loaded from: classes4.dex */
final class InflightNameResolver<T> implements NameResolver<T> {
    private final NameResolver<T> delegate;
    private final EventExecutor executor;
    private final ConcurrentMap<String, Promise<List<T>>> resolveAllsInProgress;
    private final ConcurrentMap<String, Promise<T>> resolvesInProgress;

    /* JADX INFO: Access modifiers changed from: package-private */
    public InflightNameResolver(EventExecutor executor, NameResolver<T> delegate, ConcurrentMap<String, Promise<T>> resolvesInProgress, ConcurrentMap<String, Promise<List<T>>> resolveAllsInProgress) {
        this.executor = (EventExecutor) ObjectUtil.checkNotNull(executor, "executor");
        this.delegate = (NameResolver) ObjectUtil.checkNotNull(delegate, "delegate");
        this.resolvesInProgress = (ConcurrentMap) ObjectUtil.checkNotNull(resolvesInProgress, "resolvesInProgress");
        this.resolveAllsInProgress = (ConcurrentMap) ObjectUtil.checkNotNull(resolveAllsInProgress, "resolveAllsInProgress");
    }

    @Override // io.netty.resolver.NameResolver
    public Future<T> resolve(String inetHost) {
        return resolve(inetHost, (Promise) this.executor.newPromise());
    }

    @Override // io.netty.resolver.NameResolver
    public Future<List<T>> resolveAll(String inetHost) {
        return resolveAll(inetHost, (Promise) this.executor.newPromise());
    }

    @Override // io.netty.resolver.NameResolver, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.delegate.close();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.resolver.NameResolver
    public Promise<T> resolve(String str, Promise<T> promise) {
        return (Promise<T>) resolve(this.resolvesInProgress, str, promise, false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.resolver.NameResolver
    public Promise<List<T>> resolveAll(String str, Promise<List<T>> promise) {
        return (Promise<List<T>>) resolve(this.resolveAllsInProgress, str, promise, true);
    }

    private <U> Promise<U> resolve(final ConcurrentMap<String, Promise<U>> resolveMap, final String inetHost, final Promise<U> promise, boolean resolveAll) {
        Promise<U> earlyPromise = resolveMap.putIfAbsent(inetHost, promise);
        if (earlyPromise != null) {
            if (earlyPromise.isDone()) {
                transferResult(earlyPromise, promise);
            } else {
                earlyPromise.addListener((GenericFutureListener<? extends Future<? super U>>) new FutureListener<U>() { // from class: io.netty.resolver.dns.InflightNameResolver.1
                    @Override // io.netty.util.concurrent.GenericFutureListener
                    public void operationComplete(Future<U> f) throws Exception {
                        InflightNameResolver.transferResult(f, promise);
                    }
                });
            }
        } else {
            try {
                if (resolveAll) {
                    this.delegate.resolveAll(inetHost, promise);
                } else {
                    this.delegate.resolve(inetHost, promise);
                }
                if (promise.isDone()) {
                    resolveMap.remove(inetHost);
                } else {
                    promise.addListener((GenericFutureListener<? extends Future<? super U>>) new FutureListener<U>() { // from class: io.netty.resolver.dns.InflightNameResolver.2
                        @Override // io.netty.util.concurrent.GenericFutureListener
                        public void operationComplete(Future<U> f) throws Exception {
                            resolveMap.remove(inetHost);
                        }
                    });
                }
            } catch (Throwable th) {
                if (promise.isDone()) {
                    resolveMap.remove(inetHost);
                } else {
                    promise.addListener((GenericFutureListener<? extends Future<? super U>>) new FutureListener<U>() { // from class: io.netty.resolver.dns.InflightNameResolver.2
                        @Override // io.netty.util.concurrent.GenericFutureListener
                        public void operationComplete(Future<U> f) throws Exception {
                            resolveMap.remove(inetHost);
                        }
                    });
                }
                throw th;
            }
        }
        return promise;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T> void transferResult(Future<T> src, Promise<T> dst) {
        if (src.isSuccess()) {
            dst.trySuccess(src.getNow());
        } else {
            dst.tryFailure(src.cause());
        }
    }

    public String toString() {
        return StringUtil.simpleClassName(this) + '(' + this.delegate + ')';
    }
}
