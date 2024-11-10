package io.netty.util.concurrent;

import io.netty.util.concurrent.Future;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PromiseNotificationUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/* loaded from: classes4.dex */
public class PromiseNotifier<V, F extends Future<V>> implements GenericFutureListener<F> {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) PromiseNotifier.class);
    private final boolean logNotifyFailure;
    private final Promise<? super V>[] promises;

    @SafeVarargs
    public PromiseNotifier(Promise<? super V>... promises) {
        this(true, promises);
    }

    @SafeVarargs
    public PromiseNotifier(boolean logNotifyFailure, Promise<? super V>... promises) {
        ObjectUtil.checkNotNull(promises, "promises");
        for (Promise<? super V> promise : promises) {
            ObjectUtil.checkNotNullWithIAE(promise, "promise");
        }
        this.promises = (Promise[]) promises.clone();
        this.logNotifyFailure = logNotifyFailure;
    }

    public static <V, F extends Future<V>> F cascade(F f, Promise<? super V> promise) {
        return (F) cascade(true, f, promise);
    }

    public static <V, F extends Future<V>> F cascade(boolean logNotifyFailure, final F future, final Promise<? super V> promise) {
        promise.addListener((GenericFutureListener<? extends Future<? super Object>>) new FutureListener() { // from class: io.netty.util.concurrent.PromiseNotifier.1
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(Future f) {
                if (f.isCancelled()) {
                    Future.this.cancel(false);
                }
            }
        });
        future.addListener(new PromiseNotifier(logNotifyFailure, new Promise[]{promise}) { // from class: io.netty.util.concurrent.PromiseNotifier.2
            /* JADX WARN: Multi-variable type inference failed */
            @Override // io.netty.util.concurrent.PromiseNotifier, io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(Future f) throws Exception {
                if (promise.isCancelled() && f.isCancelled()) {
                    return;
                }
                super.operationComplete(future);
            }
        });
        return future;
    }

    @Override // io.netty.util.concurrent.GenericFutureListener
    public void operationComplete(F future) throws Exception {
        InternalLogger internalLogger = this.logNotifyFailure ? logger : null;
        int i = 0;
        if (future.isSuccess()) {
            Object obj = future.get();
            Promise<? super V>[] promiseArr = this.promises;
            int length = promiseArr.length;
            while (i < length) {
                Promise<? super V> p = promiseArr[i];
                PromiseNotificationUtil.trySuccess(p, obj, internalLogger);
                i++;
            }
            return;
        }
        if (future.isCancelled()) {
            Promise<? super V>[] promiseArr2 = this.promises;
            int length2 = promiseArr2.length;
            while (i < length2) {
                Promise<? super V> p2 = promiseArr2[i];
                PromiseNotificationUtil.tryCancel(p2, internalLogger);
                i++;
            }
            return;
        }
        Throwable cause = future.cause();
        Promise<? super V>[] promiseArr3 = this.promises;
        int length3 = promiseArr3.length;
        while (i < length3) {
            Promise<? super V> p3 = promiseArr3[i];
            PromiseNotificationUtil.tryFailure(p3, cause, internalLogger);
            i++;
        }
    }
}
