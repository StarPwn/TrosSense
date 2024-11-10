package io.netty.util.concurrent;

import io.netty.util.concurrent.EventExecutorChooserFactory;
import io.netty.util.internal.ObjectUtil;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes4.dex */
public abstract class MultithreadEventExecutorGroup extends AbstractEventExecutorGroup {
    private final EventExecutor[] children;
    private final EventExecutorChooserFactory.EventExecutorChooser chooser;
    private final Set<EventExecutor> readonlyChildren;
    private final AtomicInteger terminatedChildren;
    private final Promise<?> terminationFuture;

    protected abstract EventExecutor newChild(Executor executor, Object... objArr) throws Exception;

    /* JADX INFO: Access modifiers changed from: protected */
    public MultithreadEventExecutorGroup(int nThreads, ThreadFactory threadFactory, Object... args) {
        this(nThreads, threadFactory == null ? null : new ThreadPerTaskExecutor(threadFactory), args);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public MultithreadEventExecutorGroup(int nThreads, Executor executor, Object... args) {
        this(nThreads, executor, DefaultEventExecutorChooserFactory.INSTANCE, args);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public MultithreadEventExecutorGroup(int nThreads, Executor executor, EventExecutorChooserFactory chooserFactory, Object... args) {
        int j;
        this.terminatedChildren = new AtomicInteger();
        this.terminationFuture = new DefaultPromise(GlobalEventExecutor.INSTANCE);
        ObjectUtil.checkPositive(nThreads, "nThreads");
        executor = executor == null ? new ThreadPerTaskExecutor(newDefaultThreadFactory()) : executor;
        this.children = new EventExecutor[nThreads];
        int i = 0;
        while (i < nThreads) {
            boolean success = false;
            try {
                try {
                    this.children[i] = newChild(executor, args);
                    success = true;
                    if (!success) {
                        while (j < i) {
                            while (true) {
                            }
                        }
                    }
                    i++;
                } finally {
                    if (!success) {
                        for (int j2 = 0; j2 < i; j2++) {
                            this.children[j2].shutdownGracefully();
                        }
                        for (j = 0; j < i; j++) {
                            EventExecutor e = this.children[j];
                            while (!e.isTerminated()) {
                                try {
                                    e.awaitTermination(2147483647L, TimeUnit.SECONDS);
                                } catch (InterruptedException e2) {
                                    Thread.currentThread().interrupt();
                                }
                            }
                        }
                    }
                }
            } catch (Exception e3) {
                throw new IllegalStateException("failed to create a child event loop", e3);
            }
        }
        this.chooser = chooserFactory.newChooser(this.children);
        FutureListener<Object> terminationListener = new FutureListener<Object>() { // from class: io.netty.util.concurrent.MultithreadEventExecutorGroup.1
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(Future<Object> future) throws Exception {
                if (MultithreadEventExecutorGroup.this.terminatedChildren.incrementAndGet() == MultithreadEventExecutorGroup.this.children.length) {
                    MultithreadEventExecutorGroup.this.terminationFuture.setSuccess(null);
                }
            }
        };
        for (EventExecutor eventExecutor : this.children) {
            eventExecutor.terminationFuture().addListener(terminationListener);
        }
        Set<EventExecutor> childrenSet = new LinkedHashSet<>(this.children.length);
        Collections.addAll(childrenSet, this.children);
        this.readonlyChildren = Collections.unmodifiableSet(childrenSet);
    }

    protected ThreadFactory newDefaultThreadFactory() {
        return new DefaultThreadFactory(getClass());
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup, io.netty.channel.EventLoopGroup
    public EventExecutor next() {
        return this.chooser.next();
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup, java.lang.Iterable
    public Iterator<EventExecutor> iterator() {
        return this.readonlyChildren.iterator();
    }

    public final int executorCount() {
        return this.children.length;
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup
    public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit) {
        for (EventExecutor l : this.children) {
            l.shutdownGracefully(quietPeriod, timeout, unit);
        }
        return terminationFuture();
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup
    public Future<?> terminationFuture() {
        return this.terminationFuture;
    }

    @Override // io.netty.util.concurrent.AbstractEventExecutorGroup, io.netty.util.concurrent.EventExecutorGroup, java.util.concurrent.ExecutorService
    @Deprecated
    public void shutdown() {
        for (EventExecutor l : this.children) {
            l.shutdown();
        }
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup
    public boolean isShuttingDown() {
        for (EventExecutor l : this.children) {
            if (!l.isShuttingDown()) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isShutdown() {
        for (EventExecutor l : this.children) {
            if (!l.isShutdown()) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isTerminated() {
        for (EventExecutor l : this.children) {
            if (!l.isTerminated()) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        long timeLeft;
        long deadline = System.nanoTime() + unit.toNanos(timeout);
        loop0: for (EventExecutor l : this.children) {
            do {
                timeLeft = deadline - System.nanoTime();
                if (timeLeft <= 0) {
                    break loop0;
                }
            } while (!l.awaitTermination(timeLeft, TimeUnit.NANOSECONDS));
        }
        return isTerminated();
    }
}
