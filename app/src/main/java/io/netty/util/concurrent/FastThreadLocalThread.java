package io.netty.util.concurrent;

import io.netty.util.internal.InternalThreadLocalMap;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/* loaded from: classes4.dex */
public class FastThreadLocalThread extends Thread {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) FastThreadLocalThread.class);
    private final boolean cleanupFastThreadLocals;
    private InternalThreadLocalMap threadLocalMap;

    public FastThreadLocalThread() {
        this.cleanupFastThreadLocals = false;
    }

    public FastThreadLocalThread(Runnable target) {
        super(FastThreadLocalRunnable.wrap(target));
        this.cleanupFastThreadLocals = true;
    }

    public FastThreadLocalThread(ThreadGroup group, Runnable target) {
        super(group, FastThreadLocalRunnable.wrap(target));
        this.cleanupFastThreadLocals = true;
    }

    public FastThreadLocalThread(String name) {
        super(name);
        this.cleanupFastThreadLocals = false;
    }

    public FastThreadLocalThread(ThreadGroup group, String name) {
        super(group, name);
        this.cleanupFastThreadLocals = false;
    }

    public FastThreadLocalThread(Runnable target, String name) {
        super(FastThreadLocalRunnable.wrap(target), name);
        this.cleanupFastThreadLocals = true;
    }

    public FastThreadLocalThread(ThreadGroup group, Runnable target, String name) {
        super(group, FastThreadLocalRunnable.wrap(target), name);
        this.cleanupFastThreadLocals = true;
    }

    public FastThreadLocalThread(ThreadGroup group, Runnable target, String name, long stackSize) {
        super(group, FastThreadLocalRunnable.wrap(target), name, stackSize);
        this.cleanupFastThreadLocals = true;
    }

    public final InternalThreadLocalMap threadLocalMap() {
        if (this != Thread.currentThread() && logger.isWarnEnabled()) {
            logger.warn(new RuntimeException("It's not thread-safe to get 'threadLocalMap' which doesn't belong to the caller thread"));
        }
        return this.threadLocalMap;
    }

    public final void setThreadLocalMap(InternalThreadLocalMap threadLocalMap) {
        if (this != Thread.currentThread() && logger.isWarnEnabled()) {
            logger.warn(new RuntimeException("It's not thread-safe to set 'threadLocalMap' which doesn't belong to the caller thread"));
        }
        this.threadLocalMap = threadLocalMap;
    }

    public boolean willCleanupFastThreadLocals() {
        return this.cleanupFastThreadLocals;
    }

    public static boolean willCleanupFastThreadLocals(Thread thread) {
        return (thread instanceof FastThreadLocalThread) && ((FastThreadLocalThread) thread).willCleanupFastThreadLocals();
    }

    public boolean permitBlockingCalls() {
        return false;
    }
}
