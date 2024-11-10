package io.netty.util.concurrent;

import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/* loaded from: classes4.dex */
public final class ImmediateEventExecutor extends AbstractEventExecutor {
    private final Future<?> terminationFuture = new FailedFuture(GlobalEventExecutor.INSTANCE, new UnsupportedOperationException());
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) ImmediateEventExecutor.class);
    public static final ImmediateEventExecutor INSTANCE = new ImmediateEventExecutor();
    private static final FastThreadLocal<Queue<Runnable>> DELAYED_RUNNABLES = new FastThreadLocal<Queue<Runnable>>() { // from class: io.netty.util.concurrent.ImmediateEventExecutor.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // io.netty.util.concurrent.FastThreadLocal
        public Queue<Runnable> initialValue() throws Exception {
            return new ArrayDeque();
        }
    };
    private static final FastThreadLocal<Boolean> RUNNING = new FastThreadLocal<Boolean>() { // from class: io.netty.util.concurrent.ImmediateEventExecutor.2
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // io.netty.util.concurrent.FastThreadLocal
        public Boolean initialValue() throws Exception {
            return false;
        }
    };

    private ImmediateEventExecutor() {
    }

    @Override // io.netty.util.concurrent.AbstractEventExecutor, io.netty.util.concurrent.EventExecutor
    public boolean inEventLoop() {
        return true;
    }

    @Override // io.netty.util.concurrent.EventExecutor
    public boolean inEventLoop(Thread thread) {
        return true;
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup
    public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit) {
        return terminationFuture();
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup
    public Future<?> terminationFuture() {
        return this.terminationFuture;
    }

    @Override // io.netty.util.concurrent.AbstractEventExecutor, java.util.concurrent.ExecutorService, io.netty.util.concurrent.EventExecutorGroup
    @Deprecated
    public void shutdown() {
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup
    public boolean isShuttingDown() {
        return false;
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isShutdown() {
        return false;
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isTerminated() {
        return false;
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean awaitTermination(long timeout, TimeUnit unit) {
        return false;
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable command) {
        Runnable runnable;
        ObjectUtil.checkNotNull(command, "command");
        if (!RUNNING.get().booleanValue()) {
            RUNNING.set(true);
            try {
                command.run();
                while (true) {
                    if (runnable == null) {
                        break;
                    }
                }
            } catch (Throwable cause) {
                try {
                    logger.info("Throwable caught while executing Runnable {}", command, cause);
                    Queue<Runnable> delayedRunnables = DELAYED_RUNNABLES.get();
                    while (true) {
                        Runnable runnable2 = delayedRunnables.poll();
                        if (runnable2 == null) {
                            break;
                        }
                        try {
                            runnable2.run();
                        } catch (Throwable cause2) {
                            logger.info("Throwable caught while executing Runnable {}", runnable2, cause2);
                        }
                    }
                } finally {
                    Queue<Runnable> delayedRunnables2 = DELAYED_RUNNABLES.get();
                    while (true) {
                        runnable = delayedRunnables2.poll();
                        if (runnable == null) {
                            break;
                        }
                        try {
                            runnable.run();
                        } catch (Throwable cause3) {
                            logger.info("Throwable caught while executing Runnable {}", runnable, cause3);
                        }
                    }
                    RUNNING.set(false);
                }
            }
            return;
        }
        DELAYED_RUNNABLES.get().add(command);
    }

    @Override // io.netty.util.concurrent.AbstractEventExecutor, io.netty.util.concurrent.EventExecutor
    public <V> Promise<V> newPromise() {
        return new ImmediatePromise(this);
    }

    @Override // io.netty.util.concurrent.AbstractEventExecutor, io.netty.util.concurrent.EventExecutor
    public <V> ProgressivePromise<V> newProgressivePromise() {
        return new ImmediateProgressivePromise(this);
    }

    /* loaded from: classes4.dex */
    static class ImmediatePromise<V> extends DefaultPromise<V> {
        ImmediatePromise(EventExecutor executor) {
            super(executor);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // io.netty.util.concurrent.DefaultPromise
        public void checkDeadLock() {
        }
    }

    /* loaded from: classes4.dex */
    static class ImmediateProgressivePromise<V> extends DefaultProgressivePromise<V> {
        ImmediateProgressivePromise(EventExecutor executor) {
            super(executor);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // io.netty.util.concurrent.DefaultPromise
        public void checkDeadLock() {
        }
    }
}
