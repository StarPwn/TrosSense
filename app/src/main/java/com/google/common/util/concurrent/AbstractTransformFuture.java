package com.google.common.util.concurrent;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.AbstractFuture;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class AbstractTransformFuture<I, O, F, T> extends AbstractFuture.TrustedFuture<O> implements Runnable {

    @Nullable
    F function;

    @Nullable
    ListenableFuture<? extends I> inputFuture;

    @Nullable
    abstract T doTransform(F f, @Nullable I i) throws Exception;

    abstract void setResult(@Nullable T t);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <I, O> ListenableFuture<O> create(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function) {
        AsyncTransformFuture<I, O> output = new AsyncTransformFuture<>(input, function);
        input.addListener(output, MoreExecutors.directExecutor());
        return output;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <I, O> ListenableFuture<O> create(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function, Executor executor) {
        Preconditions.checkNotNull(executor);
        AsyncTransformFuture<I, O> output = new AsyncTransformFuture<>(input, function);
        input.addListener(output, MoreExecutors.rejectionPropagatingExecutor(executor, output));
        return output;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <I, O> ListenableFuture<O> create(ListenableFuture<I> input, Function<? super I, ? extends O> function) {
        Preconditions.checkNotNull(function);
        TransformFuture<I, O> output = new TransformFuture<>(input, function);
        input.addListener(output, MoreExecutors.directExecutor());
        return output;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <I, O> ListenableFuture<O> create(ListenableFuture<I> input, Function<? super I, ? extends O> function, Executor executor) {
        Preconditions.checkNotNull(function);
        TransformFuture<I, O> output = new TransformFuture<>(input, function);
        input.addListener(output, MoreExecutors.rejectionPropagatingExecutor(executor, output));
        return output;
    }

    AbstractTransformFuture(ListenableFuture<? extends I> listenableFuture, F f) {
        this.inputFuture = (ListenableFuture) Preconditions.checkNotNull(listenableFuture);
        this.function = (F) Preconditions.checkNotNull(f);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.lang.Runnable
    public final void run() {
        ListenableFuture<? extends I> localInputFuture = this.inputFuture;
        F localFunction = this.function;
        if (isCancelled() | (localInputFuture == null) | (localFunction == null)) {
            return;
        }
        this.inputFuture = null;
        this.function = null;
        try {
            try {
                setResult(doTransform(localFunction, Futures.getDone(localInputFuture)));
            } catch (UndeclaredThrowableException e) {
                setException(e.getCause());
            } catch (Throwable t) {
                setException(t);
            }
        } catch (Error e2) {
            setException(e2);
        } catch (CancellationException e3) {
            cancel(false);
        } catch (RuntimeException e4) {
            setException(e4);
        } catch (ExecutionException e5) {
            setException(e5.getCause());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.common.util.concurrent.AbstractFuture
    public final void afterDone() {
        maybePropagateCancellation(this.inputFuture);
        this.inputFuture = null;
        this.function = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class AsyncTransformFuture<I, O> extends AbstractTransformFuture<I, O, AsyncFunction<? super I, ? extends O>, ListenableFuture<? extends O>> {
        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.util.concurrent.AbstractTransformFuture
        /* bridge */ /* synthetic */ Object doTransform(Object obj, @Nullable Object obj2) throws Exception {
            return doTransform((AsyncFunction<? super AsyncFunction<? super I, ? extends O>, ? extends O>) obj, (AsyncFunction<? super I, ? extends O>) obj2);
        }

        AsyncTransformFuture(ListenableFuture<? extends I> inputFuture, AsyncFunction<? super I, ? extends O> function) {
            super(inputFuture, function);
        }

        /* JADX WARN: Multi-variable type inference failed */
        ListenableFuture<? extends O> doTransform(AsyncFunction<? super I, ? extends O> function, @Nullable I i) throws Exception {
            ListenableFuture<? extends O> outputFuture = function.apply(i);
            Preconditions.checkNotNull(outputFuture, "AsyncFunction.apply returned null instead of a Future. Did you mean to return immediateFuture(null)?");
            return outputFuture;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.util.concurrent.AbstractTransformFuture
        public void setResult(ListenableFuture<? extends O> listenableFuture) {
            setFuture(listenableFuture);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class TransformFuture<I, O> extends AbstractTransformFuture<I, O, Function<? super I, ? extends O>, O> {
        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.util.concurrent.AbstractTransformFuture
        @Nullable
        /* bridge */ /* synthetic */ Object doTransform(Object obj, @Nullable Object obj2) throws Exception {
            return doTransform((Function<? super Function<? super I, ? extends O>, ? extends O>) obj, (Function<? super I, ? extends O>) obj2);
        }

        TransformFuture(ListenableFuture<? extends I> inputFuture, Function<? super I, ? extends O> function) {
            super(inputFuture, function);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Nullable
        O doTransform(Function<? super I, ? extends O> function, @Nullable I i) {
            return function.apply(i);
        }

        @Override // com.google.common.util.concurrent.AbstractTransformFuture
        void setResult(@Nullable O result) {
            set(result);
        }
    }
}
