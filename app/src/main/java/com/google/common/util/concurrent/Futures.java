package com.google.common.util.concurrent;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.AbstractFuture;
import com.google.common.util.concurrent.CollectionFuture;
import com.google.common.util.concurrent.ImmediateFuture;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public final class Futures extends GwtFuturesCatchingSpecialization {
    private static final AsyncFunction<ListenableFuture<Object>, Object> DEREFERENCER = new AsyncFunction<ListenableFuture<Object>, Object>() { // from class: com.google.common.util.concurrent.Futures.2
        @Override // com.google.common.util.concurrent.AsyncFunction
        public ListenableFuture<Object> apply(ListenableFuture<Object> input) {
            return input;
        }
    };

    private Futures() {
    }

    public static <V, X extends Exception> CheckedFuture<V, X> makeChecked(ListenableFuture<V> future, Function<? super Exception, X> mapper) {
        return new MappingCheckedFuture((ListenableFuture) Preconditions.checkNotNull(future), mapper);
    }

    public static <V> ListenableFuture<V> immediateFuture(@Nullable V value) {
        if (value == null) {
            ListenableFuture<V> typedNull = ImmediateFuture.ImmediateSuccessfulFuture.NULL;
            return typedNull;
        }
        return new ImmediateFuture.ImmediateSuccessfulFuture(value);
    }

    public static <V, X extends Exception> CheckedFuture<V, X> immediateCheckedFuture(@Nullable V value) {
        return new ImmediateFuture.ImmediateSuccessfulCheckedFuture(value);
    }

    public static <V> ListenableFuture<V> immediateFailedFuture(Throwable throwable) {
        Preconditions.checkNotNull(throwable);
        return new ImmediateFuture.ImmediateFailedFuture(throwable);
    }

    public static <V> ListenableFuture<V> immediateCancelledFuture() {
        return new ImmediateFuture.ImmediateCancelledFuture();
    }

    public static <V, X extends Exception> CheckedFuture<V, X> immediateFailedCheckedFuture(X exception) {
        Preconditions.checkNotNull(exception);
        return new ImmediateFuture.ImmediateFailedCheckedFuture(exception);
    }

    public static <V, X extends Throwable> ListenableFuture<V> catching(ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback) {
        return AbstractCatchingFuture.create(input, exceptionType, fallback);
    }

    public static <V, X extends Throwable> ListenableFuture<V> catching(ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback, Executor executor) {
        return AbstractCatchingFuture.create(input, exceptionType, fallback, executor);
    }

    public static <V, X extends Throwable> ListenableFuture<V> catchingAsync(ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback) {
        return AbstractCatchingFuture.create(input, exceptionType, fallback);
    }

    public static <V, X extends Throwable> ListenableFuture<V> catchingAsync(ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback, Executor executor) {
        return AbstractCatchingFuture.create(input, exceptionType, fallback, executor);
    }

    public static <V> ListenableFuture<V> withTimeout(ListenableFuture<V> delegate, long time, TimeUnit unit, ScheduledExecutorService scheduledExecutor) {
        return TimeoutFuture.create(delegate, time, unit, scheduledExecutor);
    }

    public static <I, O> ListenableFuture<O> transformAsync(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function) {
        return AbstractTransformFuture.create(input, function);
    }

    public static <I, O> ListenableFuture<O> transformAsync(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function, Executor executor) {
        return AbstractTransformFuture.create(input, function, executor);
    }

    public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, Function<? super I, ? extends O> function) {
        return AbstractTransformFuture.create(input, function);
    }

    public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, Function<? super I, ? extends O> function, Executor executor) {
        return AbstractTransformFuture.create(input, function, executor);
    }

    public static <I, O> Future<O> lazyTransform(final Future<I> input, final Function<? super I, ? extends O> function) {
        Preconditions.checkNotNull(input);
        Preconditions.checkNotNull(function);
        return new Future<O>() { // from class: com.google.common.util.concurrent.Futures.1
            @Override // java.util.concurrent.Future
            public boolean cancel(boolean mayInterruptIfRunning) {
                return input.cancel(mayInterruptIfRunning);
            }

            @Override // java.util.concurrent.Future
            public boolean isCancelled() {
                return input.isCancelled();
            }

            @Override // java.util.concurrent.Future
            public boolean isDone() {
                return input.isDone();
            }

            @Override // java.util.concurrent.Future
            public O get() throws InterruptedException, ExecutionException {
                return applyTransformation(input.get());
            }

            @Override // java.util.concurrent.Future
            public O get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return applyTransformation(input.get(timeout, unit));
            }

            private O applyTransformation(I i) throws ExecutionException {
                try {
                    return (O) function.apply(i);
                } catch (Throwable th) {
                    throw new ExecutionException(th);
                }
            }
        };
    }

    public static <V> ListenableFuture<V> dereference(ListenableFuture<? extends ListenableFuture<? extends V>> nested) {
        return transformAsync(nested, DEREFERENCER);
    }

    @SafeVarargs
    public static <V> ListenableFuture<List<V>> allAsList(ListenableFuture<? extends V>... futures) {
        return new CollectionFuture.ListFuture(ImmutableList.copyOf(futures), true);
    }

    public static <V> ListenableFuture<List<V>> allAsList(Iterable<? extends ListenableFuture<? extends V>> futures) {
        return new CollectionFuture.ListFuture(ImmutableList.copyOf(futures), true);
    }

    @SafeVarargs
    public static <V> FutureCombiner<V> whenAllComplete(ListenableFuture<? extends V>... futures) {
        return new FutureCombiner<>(false, ImmutableList.copyOf(futures));
    }

    public static <V> FutureCombiner<V> whenAllComplete(Iterable<? extends ListenableFuture<? extends V>> futures) {
        return new FutureCombiner<>(false, ImmutableList.copyOf(futures));
    }

    @SafeVarargs
    public static <V> FutureCombiner<V> whenAllSucceed(ListenableFuture<? extends V>... futures) {
        return new FutureCombiner<>(true, ImmutableList.copyOf(futures));
    }

    public static <V> FutureCombiner<V> whenAllSucceed(Iterable<? extends ListenableFuture<? extends V>> futures) {
        return new FutureCombiner<>(true, ImmutableList.copyOf(futures));
    }

    /* loaded from: classes.dex */
    public static final class FutureCombiner<V> {
        private final boolean allMustSucceed;
        private final ImmutableList<ListenableFuture<? extends V>> futures;

        private FutureCombiner(boolean allMustSucceed, ImmutableList<ListenableFuture<? extends V>> futures) {
            this.allMustSucceed = allMustSucceed;
            this.futures = futures;
        }

        public <C> ListenableFuture<C> callAsync(AsyncCallable<C> combiner, Executor executor) {
            return new CombinedFuture(this.futures, this.allMustSucceed, executor, combiner);
        }

        public <C> ListenableFuture<C> callAsync(AsyncCallable<C> combiner) {
            return callAsync(combiner, MoreExecutors.directExecutor());
        }

        public <C> ListenableFuture<C> call(Callable<C> combiner, Executor executor) {
            return new CombinedFuture(this.futures, this.allMustSucceed, executor, combiner);
        }

        public <C> ListenableFuture<C> call(Callable<C> combiner) {
            return call(combiner, MoreExecutors.directExecutor());
        }
    }

    public static <V> ListenableFuture<V> nonCancellationPropagating(ListenableFuture<V> future) {
        if (future.isDone()) {
            return future;
        }
        return new NonCancellationPropagatingFuture(future);
    }

    /* loaded from: classes.dex */
    private static final class NonCancellationPropagatingFuture<V> extends AbstractFuture.TrustedFuture<V> {
        NonCancellationPropagatingFuture(final ListenableFuture<V> delegate) {
            delegate.addListener(new Runnable() { // from class: com.google.common.util.concurrent.Futures.NonCancellationPropagatingFuture.1
                @Override // java.lang.Runnable
                public void run() {
                    NonCancellationPropagatingFuture.this.setFuture(delegate);
                }
            }, MoreExecutors.directExecutor());
        }
    }

    @SafeVarargs
    public static <V> ListenableFuture<List<V>> successfulAsList(ListenableFuture<? extends V>... futures) {
        return new CollectionFuture.ListFuture(ImmutableList.copyOf(futures), false);
    }

    public static <V> ListenableFuture<List<V>> successfulAsList(Iterable<? extends ListenableFuture<? extends V>> futures) {
        return new CollectionFuture.ListFuture(ImmutableList.copyOf(futures), false);
    }

    public static <T> ImmutableList<ListenableFuture<T>> inCompletionOrder(Iterable<? extends ListenableFuture<? extends T>> futures) {
        final ConcurrentLinkedQueue<SettableFuture<T>> delegates = Queues.newConcurrentLinkedQueue();
        ImmutableList.Builder<ListenableFuture<T>> listBuilder = ImmutableList.builder();
        SerializingExecutor executor = new SerializingExecutor(MoreExecutors.directExecutor());
        for (final ListenableFuture<? extends T> future : futures) {
            SettableFuture<T> delegate = SettableFuture.create();
            delegates.add(delegate);
            future.addListener(new Runnable() { // from class: com.google.common.util.concurrent.Futures.3
                @Override // java.lang.Runnable
                public void run() {
                    ((SettableFuture) delegates.remove()).setFuture(future);
                }
            }, executor);
            listBuilder.add((ImmutableList.Builder<ListenableFuture<T>>) delegate);
        }
        return listBuilder.build();
    }

    public static <V> void addCallback(ListenableFuture<V> future, FutureCallback<? super V> callback) {
        addCallback(future, callback, MoreExecutors.directExecutor());
    }

    public static <V> void addCallback(final ListenableFuture<V> future, final FutureCallback<? super V> callback, Executor executor) {
        Preconditions.checkNotNull(callback);
        Runnable callbackListener = new Runnable() { // from class: com.google.common.util.concurrent.Futures.4
            @Override // java.lang.Runnable
            public void run() {
                try {
                    callback.onSuccess(Futures.getDone(ListenableFuture.this));
                } catch (Error e) {
                    callback.onFailure(e);
                } catch (RuntimeException e2) {
                    callback.onFailure(e2);
                } catch (ExecutionException e3) {
                    callback.onFailure(e3.getCause());
                }
            }
        };
        future.addListener(callbackListener, executor);
    }

    public static <V> V getDone(Future<V> future) throws ExecutionException {
        Preconditions.checkState(future.isDone(), "Future was expected to be done: %s", future);
        return (V) Uninterruptibles.getUninterruptibly(future);
    }

    public static <V, X extends Exception> V getChecked(Future<V> future, Class<X> cls) throws Exception {
        return (V) FuturesGetChecked.getChecked(future, cls);
    }

    public static <V, X extends Exception> V getChecked(Future<V> future, Class<X> cls, long j, TimeUnit timeUnit) throws Exception {
        return (V) FuturesGetChecked.getChecked(future, cls, j, timeUnit);
    }

    public static <V> V getUnchecked(Future<V> future) {
        Preconditions.checkNotNull(future);
        try {
            return (V) Uninterruptibles.getUninterruptibly(future);
        } catch (ExecutionException e) {
            wrapAndThrowUnchecked(e.getCause());
            throw new AssertionError();
        }
    }

    private static void wrapAndThrowUnchecked(Throwable cause) {
        if (cause instanceof Error) {
            throw new ExecutionError((Error) cause);
        }
        throw new UncheckedExecutionException(cause);
    }

    /* loaded from: classes.dex */
    private static class MappingCheckedFuture<V, X extends Exception> extends AbstractCheckedFuture<V, X> {
        final Function<? super Exception, X> mapper;

        MappingCheckedFuture(ListenableFuture<V> delegate, Function<? super Exception, X> mapper) {
            super(delegate);
            this.mapper = (Function) Preconditions.checkNotNull(mapper);
        }

        @Override // com.google.common.util.concurrent.AbstractCheckedFuture
        protected X mapException(Exception e) {
            return this.mapper.apply(e);
        }
    }
}
