package io.netty.util.concurrent;

import androidx.concurrent.futures.AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0;
import io.netty.util.internal.InternalThreadLocalMap;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.ThrowableUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/* loaded from: classes4.dex */
public class DefaultPromise<V> extends AbstractFuture<V> implements Promise<V> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final EventExecutor executor;
    private GenericFutureListener<? extends Future<?>> listener;
    private DefaultFutureListeners listeners;
    private boolean notifyingListeners;
    private volatile Object result;
    private short waiters;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) DefaultPromise.class);
    private static final InternalLogger rejectedExecutionLogger = InternalLoggerFactory.getInstance(DefaultPromise.class.getName() + ".rejectedExecution");
    private static final int MAX_LISTENER_STACK_DEPTH = Math.min(8, SystemPropertyUtil.getInt("io.netty.defaultPromise.maxListenerStackDepth", 8));
    private static final AtomicReferenceFieldUpdater<DefaultPromise, Object> RESULT_UPDATER = AtomicReferenceFieldUpdater.newUpdater(DefaultPromise.class, Object.class, "result");
    private static final Object SUCCESS = new Object();
    private static final Object UNCANCELLABLE = new Object();
    private static final CauseHolder CANCELLATION_CAUSE_HOLDER = new CauseHolder(StacklessCancellationException.newInstance(DefaultPromise.class, "cancel(...)"));
    private static final StackTraceElement[] CANCELLATION_STACK = CANCELLATION_CAUSE_HOLDER.cause.getStackTrace();

    public DefaultPromise(EventExecutor executor) {
        this.executor = (EventExecutor) ObjectUtil.checkNotNull(executor, "executor");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DefaultPromise() {
        this.executor = null;
    }

    public Promise<V> setSuccess(V result) {
        if (setSuccess0(result)) {
            return this;
        }
        throw new IllegalStateException("complete already: " + this);
    }

    public boolean trySuccess(V result) {
        return setSuccess0(result);
    }

    public Promise<V> setFailure(Throwable cause) {
        if (setFailure0(cause)) {
            return this;
        }
        throw new IllegalStateException("complete already: " + this, cause);
    }

    public boolean tryFailure(Throwable cause) {
        return setFailure0(cause);
    }

    @Override // io.netty.util.concurrent.Promise
    public boolean setUncancellable() {
        if (AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(RESULT_UPDATER, this, null, UNCANCELLABLE)) {
            return true;
        }
        Object result = this.result;
        return (isDone0(result) && isCancelled0(result)) ? false : true;
    }

    @Override // io.netty.util.concurrent.Future
    public boolean isSuccess() {
        Object result = this.result;
        return (result == null || result == UNCANCELLABLE || (result instanceof CauseHolder)) ? false : true;
    }

    @Override // io.netty.util.concurrent.Future
    public boolean isCancellable() {
        return this.result == null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class LeanCancellationException extends CancellationException {
        private static final long serialVersionUID = 2794674970981187807L;

        private LeanCancellationException() {
        }

        @Override // java.lang.Throwable
        public Throwable fillInStackTrace() {
            setStackTrace(DefaultPromise.CANCELLATION_STACK);
            return this;
        }

        @Override // java.lang.Throwable
        public String toString() {
            return CancellationException.class.getName();
        }
    }

    @Override // io.netty.util.concurrent.Future
    public Throwable cause() {
        return cause0(this.result);
    }

    private Throwable cause0(Object result) {
        if (!(result instanceof CauseHolder)) {
            return null;
        }
        if (result == CANCELLATION_CAUSE_HOLDER) {
            CancellationException ce = new LeanCancellationException();
            if (AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(RESULT_UPDATER, this, CANCELLATION_CAUSE_HOLDER, new CauseHolder(ce))) {
                return ce;
            }
            result = this.result;
        }
        return ((CauseHolder) result).cause;
    }

    @Override // io.netty.util.concurrent.Future
    public Promise<V> addListener(GenericFutureListener<? extends Future<? super V>> listener) {
        ObjectUtil.checkNotNull(listener, "listener");
        synchronized (this) {
            addListener0(listener);
        }
        if (isDone()) {
            notifyListeners();
        }
        return this;
    }

    @Override // io.netty.util.concurrent.Future
    public Promise<V> addListeners(GenericFutureListener<? extends Future<? super V>>... listeners) {
        ObjectUtil.checkNotNull(listeners, "listeners");
        synchronized (this) {
            for (GenericFutureListener<? extends Future<? super V>> listener : listeners) {
                if (listener == null) {
                    break;
                }
                addListener0(listener);
            }
        }
        if (isDone()) {
            notifyListeners();
        }
        return this;
    }

    @Override // io.netty.util.concurrent.Future
    public Promise<V> removeListener(GenericFutureListener<? extends Future<? super V>> listener) {
        ObjectUtil.checkNotNull(listener, "listener");
        synchronized (this) {
            removeListener0(listener);
        }
        return this;
    }

    @Override // io.netty.util.concurrent.Future
    public Promise<V> removeListeners(GenericFutureListener<? extends Future<? super V>>... listeners) {
        ObjectUtil.checkNotNull(listeners, "listeners");
        synchronized (this) {
            for (GenericFutureListener<? extends Future<? super V>> listener : listeners) {
                if (listener == null) {
                    break;
                }
                removeListener0(listener);
            }
        }
        return this;
    }

    @Override // io.netty.util.concurrent.Future
    public Promise<V> await() throws InterruptedException {
        if (isDone()) {
            return this;
        }
        if (Thread.interrupted()) {
            throw new InterruptedException(toString());
        }
        checkDeadLock();
        synchronized (this) {
            while (!isDone()) {
                incWaiters();
                try {
                    wait();
                    decWaiters();
                } catch (Throwable th) {
                    decWaiters();
                    throw th;
                }
            }
        }
        return this;
    }

    @Override // io.netty.util.concurrent.Future
    public Promise<V> awaitUninterruptibly() {
        if (isDone()) {
            return this;
        }
        checkDeadLock();
        boolean interrupted = false;
        synchronized (this) {
            while (!isDone()) {
                incWaiters();
                try {
                    wait();
                } catch (InterruptedException e) {
                    interrupted = true;
                } catch (Throwable th) {
                    decWaiters();
                    throw th;
                }
                decWaiters();
            }
        }
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
        return this;
    }

    @Override // io.netty.util.concurrent.Future
    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        return await0(unit.toNanos(timeout), true);
    }

    @Override // io.netty.util.concurrent.Future
    public boolean await(long timeoutMillis) throws InterruptedException {
        return await0(TimeUnit.MILLISECONDS.toNanos(timeoutMillis), true);
    }

    @Override // io.netty.util.concurrent.Future
    public boolean awaitUninterruptibly(long timeout, TimeUnit unit) {
        try {
            return await0(unit.toNanos(timeout), false);
        } catch (InterruptedException e) {
            throw new InternalError();
        }
    }

    @Override // io.netty.util.concurrent.Future
    public boolean awaitUninterruptibly(long timeoutMillis) {
        try {
            return await0(TimeUnit.MILLISECONDS.toNanos(timeoutMillis), false);
        } catch (InterruptedException e) {
            throw new InternalError();
        }
    }

    @Override // io.netty.util.concurrent.Future
    public V getNow() {
        V v = (V) this.result;
        if ((v instanceof CauseHolder) || v == SUCCESS || v == UNCANCELLABLE) {
            return null;
        }
        return v;
    }

    @Override // io.netty.util.concurrent.AbstractFuture, java.util.concurrent.Future
    public V get() throws InterruptedException, ExecutionException {
        V v = (V) this.result;
        if (!isDone0(v)) {
            await();
            v = (V) this.result;
        }
        if (v == SUCCESS || v == UNCANCELLABLE) {
            return null;
        }
        Throwable cause0 = cause0(v);
        if (cause0 == null) {
            return v;
        }
        if (cause0 instanceof CancellationException) {
            throw ((CancellationException) cause0);
        }
        throw new ExecutionException(cause0);
    }

    @Override // io.netty.util.concurrent.AbstractFuture, java.util.concurrent.Future
    public V get(long j, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        V v = (V) this.result;
        if (!isDone0(v)) {
            if (!await(j, timeUnit)) {
                throw new TimeoutException();
            }
            v = (V) this.result;
        }
        if (v == SUCCESS || v == UNCANCELLABLE) {
            return null;
        }
        Throwable cause0 = cause0(v);
        if (cause0 == null) {
            return v;
        }
        if (cause0 instanceof CancellationException) {
            throw ((CancellationException) cause0);
        }
        throw new ExecutionException(cause0);
    }

    @Override // io.netty.util.concurrent.Future, java.util.concurrent.Future
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(RESULT_UPDATER, this, null, CANCELLATION_CAUSE_HOLDER)) {
            if (checkNotifyWaiters()) {
                notifyListeners();
                return true;
            }
            return true;
        }
        return false;
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        return isCancelled0(this.result);
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        return isDone0(this.result);
    }

    @Override // io.netty.util.concurrent.Future
    public Promise<V> sync() throws InterruptedException {
        await();
        rethrowIfFailed();
        return this;
    }

    @Override // io.netty.util.concurrent.Future
    public Promise<V> syncUninterruptibly() {
        awaitUninterruptibly();
        rethrowIfFailed();
        return this;
    }

    public String toString() {
        return toStringBuilder().toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public StringBuilder toStringBuilder() {
        StringBuilder buf = new StringBuilder(64).append(StringUtil.simpleClassName(this)).append('@').append(Integer.toHexString(hashCode()));
        Object result = this.result;
        if (result == SUCCESS) {
            buf.append("(success)");
        } else if (result == UNCANCELLABLE) {
            buf.append("(uncancellable)");
        } else if (result instanceof CauseHolder) {
            buf.append("(failure: ").append(((CauseHolder) result).cause).append(')');
        } else if (result != null) {
            buf.append("(success: ").append(result).append(')');
        } else {
            buf.append("(incomplete)");
        }
        return buf;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public EventExecutor executor() {
        return this.executor;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void checkDeadLock() {
        EventExecutor e = executor();
        if (e != null && e.inEventLoop()) {
            throw new BlockingOperationException(toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void notifyListener(EventExecutor eventExecutor, Future<?> future, GenericFutureListener<?> listener) {
        notifyListenerWithStackOverFlowProtection((EventExecutor) ObjectUtil.checkNotNull(eventExecutor, "eventExecutor"), (Future) ObjectUtil.checkNotNull(future, "future"), (GenericFutureListener) ObjectUtil.checkNotNull(listener, "listener"));
    }

    private void notifyListeners() {
        InternalThreadLocalMap threadLocals;
        int stackDepth;
        EventExecutor executor = executor();
        if (executor.inEventLoop() && (stackDepth = (threadLocals = InternalThreadLocalMap.get()).futureListenerStackDepth()) < MAX_LISTENER_STACK_DEPTH) {
            threadLocals.setFutureListenerStackDepth(stackDepth + 1);
            try {
                notifyListenersNow();
                return;
            } finally {
                threadLocals.setFutureListenerStackDepth(stackDepth);
            }
        }
        safeExecute(executor, new Runnable() { // from class: io.netty.util.concurrent.DefaultPromise.1
            @Override // java.lang.Runnable
            public void run() {
                DefaultPromise.this.notifyListenersNow();
            }
        });
    }

    private static void notifyListenerWithStackOverFlowProtection(EventExecutor executor, final Future<?> future, final GenericFutureListener<?> listener) {
        InternalThreadLocalMap threadLocals;
        int stackDepth;
        if (executor.inEventLoop() && (stackDepth = (threadLocals = InternalThreadLocalMap.get()).futureListenerStackDepth()) < MAX_LISTENER_STACK_DEPTH) {
            threadLocals.setFutureListenerStackDepth(stackDepth + 1);
            try {
                notifyListener0(future, listener);
                return;
            } finally {
                threadLocals.setFutureListenerStackDepth(stackDepth);
            }
        }
        safeExecute(executor, new Runnable() { // from class: io.netty.util.concurrent.DefaultPromise.2
            @Override // java.lang.Runnable
            public void run() {
                DefaultPromise.notifyListener0(Future.this, listener);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyListenersNow() {
        synchronized (this) {
            GenericFutureListener listener = this.listener;
            DefaultFutureListeners listeners = this.listeners;
            if (!this.notifyingListeners && (listener != null || listeners != null)) {
                this.notifyingListeners = true;
                if (listener != null) {
                    this.listener = null;
                } else {
                    this.listeners = null;
                }
                while (true) {
                    if (listener != null) {
                        notifyListener0(this, listener);
                    } else {
                        notifyListeners0(listeners);
                    }
                    synchronized (this) {
                        if (this.listener == null && this.listeners == null) {
                            this.notifyingListeners = false;
                            return;
                        }
                        listener = this.listener;
                        listeners = this.listeners;
                        if (listener != null) {
                            this.listener = null;
                        } else {
                            this.listeners = null;
                        }
                    }
                }
            }
        }
    }

    private void notifyListeners0(DefaultFutureListeners listeners) {
        GenericFutureListener<?>[] a = listeners.listeners();
        int size = listeners.size();
        for (int i = 0; i < size; i++) {
            notifyListener0(this, a[i]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void notifyListener0(Future future, GenericFutureListener l) {
        try {
            l.operationComplete(future);
        } catch (Throwable t) {
            if (logger.isWarnEnabled()) {
                logger.warn("An exception was thrown by " + l.getClass().getName() + ".operationComplete()", t);
            }
        }
    }

    private void addListener0(GenericFutureListener<? extends Future<? super V>> listener) {
        if (this.listener == null) {
            if (this.listeners == null) {
                this.listener = listener;
                return;
            } else {
                this.listeners.add(listener);
                return;
            }
        }
        if (this.listeners != null) {
            throw new AssertionError();
        }
        this.listeners = new DefaultFutureListeners(this.listener, listener);
        this.listener = null;
    }

    private void removeListener0(GenericFutureListener<? extends Future<? super V>> toRemove) {
        if (this.listener == toRemove) {
            this.listener = null;
        } else if (this.listeners != null) {
            this.listeners.remove(toRemove);
            if (this.listeners.size() == 0) {
                this.listeners = null;
            }
        }
    }

    private boolean setSuccess0(V result) {
        return setValue0(result == null ? SUCCESS : result);
    }

    private boolean setFailure0(Throwable cause) {
        return setValue0(new CauseHolder((Throwable) ObjectUtil.checkNotNull(cause, "cause")));
    }

    private boolean setValue0(Object objResult) {
        if (AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(RESULT_UPDATER, this, null, objResult) || AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(RESULT_UPDATER, this, UNCANCELLABLE, objResult)) {
            if (checkNotifyWaiters()) {
                notifyListeners();
                return true;
            }
            return true;
        }
        return false;
    }

    private synchronized boolean checkNotifyWaiters() {
        boolean z;
        if (this.waiters > 0) {
            notifyAll();
        }
        if (this.listener == null) {
            z = this.listeners != null;
        }
        return z;
    }

    private void incWaiters() {
        if (this.waiters == Short.MAX_VALUE) {
            throw new IllegalStateException("too many waiters: " + this);
        }
        this.waiters = (short) (this.waiters + 1);
    }

    private void decWaiters() {
        this.waiters = (short) (this.waiters - 1);
    }

    private void rethrowIfFailed() {
        Throwable cause = cause();
        if (cause == null) {
            return;
        }
        PlatformDependent.throwException(cause);
    }

    private boolean await0(long timeoutNanos, boolean interruptable) throws InterruptedException {
        if (isDone()) {
            return true;
        }
        if (timeoutNanos <= 0) {
            return isDone();
        }
        if (interruptable && Thread.interrupted()) {
            throw new InterruptedException(toString());
        }
        checkDeadLock();
        long startTime = System.nanoTime();
        synchronized (this) {
            boolean interrupted = false;
            long waitTime = timeoutNanos;
            while (!isDone() && waitTime > 0) {
                try {
                    incWaiters();
                    try {
                        try {
                            wait(waitTime / 1000000, (int) (waitTime % 1000000));
                        } catch (InterruptedException e) {
                            if (interruptable) {
                                throw e;
                            }
                            interrupted = true;
                        }
                        decWaiters();
                        if (isDone()) {
                            return true;
                        }
                        waitTime = timeoutNanos - (System.nanoTime() - startTime);
                    } catch (Throwable th) {
                        decWaiters();
                        throw th;
                    }
                } finally {
                    if (interrupted) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            boolean isDone = isDone();
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
            return isDone;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    public void notifyProgressiveListeners(final long progress, final long total) {
        Object listeners = progressiveListeners();
        if (listeners == null) {
            return;
        }
        final ProgressiveFuture<V> self = (ProgressiveFuture) this;
        EventExecutor executor = executor();
        if (executor.inEventLoop()) {
            if (listeners instanceof GenericProgressiveFutureListener[]) {
                notifyProgressiveListeners0(self, (GenericProgressiveFutureListener[]) listeners, progress, total);
                return;
            } else {
                notifyProgressiveListener0(self, (GenericProgressiveFutureListener) listeners, progress, total);
                return;
            }
        }
        if (listeners instanceof GenericProgressiveFutureListener[]) {
            final GenericProgressiveFutureListener<?>[] array = (GenericProgressiveFutureListener[]) listeners;
            safeExecute(executor, new Runnable() { // from class: io.netty.util.concurrent.DefaultPromise.3
                @Override // java.lang.Runnable
                public void run() {
                    DefaultPromise.notifyProgressiveListeners0(self, array, progress, total);
                }
            });
        } else {
            final GenericProgressiveFutureListener<ProgressiveFuture<V>> l = (GenericProgressiveFutureListener) listeners;
            safeExecute(executor, new Runnable() { // from class: io.netty.util.concurrent.DefaultPromise.4
                @Override // java.lang.Runnable
                public void run() {
                    DefaultPromise.notifyProgressiveListener0(self, l, progress, total);
                }
            });
        }
    }

    private synchronized Object progressiveListeners() {
        GenericFutureListener listener = this.listener;
        DefaultFutureListeners listeners = this.listeners;
        if (listener == null && listeners == null) {
            return null;
        }
        if (listeners != null) {
            int progressiveSize = listeners.progressiveSize();
            switch (progressiveSize) {
                case 0:
                    return null;
                case 1:
                    for (GenericFutureListener<?> l : listeners.listeners()) {
                        if (l instanceof GenericProgressiveFutureListener) {
                            return l;
                        }
                    }
                    return null;
                default:
                    GenericFutureListener<?>[] array = listeners.listeners();
                    GenericProgressiveFutureListener<?>[] copy = new GenericProgressiveFutureListener[progressiveSize];
                    int i = 0;
                    int j = 0;
                    while (j < progressiveSize) {
                        GenericFutureListener<?> l2 = array[i];
                        if (l2 instanceof GenericProgressiveFutureListener) {
                            int j2 = j + 1;
                            copy[j] = (GenericProgressiveFutureListener) l2;
                            j = j2;
                        }
                        i++;
                    }
                    return copy;
            }
        }
        if (listener instanceof GenericProgressiveFutureListener) {
            return listener;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void notifyProgressiveListeners0(ProgressiveFuture<?> future, GenericProgressiveFutureListener<?>[] listeners, long progress, long total) {
        for (GenericProgressiveFutureListener<?> l : listeners) {
            if (l != null) {
                notifyProgressiveListener0(future, l, progress, total);
            } else {
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void notifyProgressiveListener0(ProgressiveFuture future, GenericProgressiveFutureListener l, long progress, long total) {
        try {
            l.operationProgressed(future, progress, total);
        } catch (Throwable t) {
            if (logger.isWarnEnabled()) {
                logger.warn("An exception was thrown by " + l.getClass().getName() + ".operationProgressed()", t);
            }
        }
    }

    private static boolean isCancelled0(Object result) {
        return (result instanceof CauseHolder) && (((CauseHolder) result).cause instanceof CancellationException);
    }

    private static boolean isDone0(Object result) {
        return (result == null || result == UNCANCELLABLE) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class CauseHolder {
        final Throwable cause;

        CauseHolder(Throwable cause) {
            this.cause = cause;
        }
    }

    private static void safeExecute(EventExecutor executor, Runnable task) {
        try {
            executor.execute(task);
        } catch (Throwable t) {
            rejectedExecutionLogger.error("Failed to submit a listener notification task. Event loop shut down?", t);
        }
    }

    /* loaded from: classes4.dex */
    private static final class StacklessCancellationException extends CancellationException {
        private static final long serialVersionUID = -2974906711413716191L;

        private StacklessCancellationException() {
        }

        @Override // java.lang.Throwable
        public Throwable fillInStackTrace() {
            return this;
        }

        static StacklessCancellationException newInstance(Class<?> clazz, String method) {
            return (StacklessCancellationException) ThrowableUtil.unknownStackTrace(new StacklessCancellationException(), clazz, method);
        }
    }
}
