package com.google.common.util.concurrent;

import androidx.concurrent.futures.AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: classes.dex */
abstract class InterruptibleTask implements Runnable {
    private static final AtomicHelper ATOMIC_HELPER;
    private static final Logger log = Logger.getLogger(InterruptibleTask.class.getName());
    private volatile boolean doneInterrupting;
    private volatile Thread runner;

    abstract void runInterruptibly();

    abstract boolean wasInterrupted();

    static {
        AtomicHelper helper;
        try {
            helper = new SafeAtomicHelper(AtomicReferenceFieldUpdater.newUpdater(InterruptibleTask.class, Thread.class, "runner"));
        } catch (Throwable reflectionFailure) {
            log.log(Level.SEVERE, "SafeAtomicHelper is broken!", reflectionFailure);
            helper = new SynchronizedAtomicHelper();
        }
        ATOMIC_HELPER = helper;
    }

    @Override // java.lang.Runnable
    public final void run() {
        if (!ATOMIC_HELPER.compareAndSetRunner(this, null, Thread.currentThread())) {
            return;
        }
        try {
            runInterruptibly();
        } finally {
            if (wasInterrupted()) {
                while (!this.doneInterrupting) {
                    Thread.yield();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void interruptTask() {
        Thread currentRunner = this.runner;
        if (currentRunner != null) {
            currentRunner.interrupt();
        }
        this.doneInterrupting = true;
    }

    /* loaded from: classes.dex */
    private static abstract class AtomicHelper {
        abstract boolean compareAndSetRunner(InterruptibleTask interruptibleTask, Thread thread, Thread thread2);

        private AtomicHelper() {
        }
    }

    /* loaded from: classes.dex */
    private static final class SafeAtomicHelper extends AtomicHelper {
        final AtomicReferenceFieldUpdater<InterruptibleTask, Thread> runnerUpdater;

        SafeAtomicHelper(AtomicReferenceFieldUpdater runnerUpdater) {
            super();
            this.runnerUpdater = runnerUpdater;
        }

        @Override // com.google.common.util.concurrent.InterruptibleTask.AtomicHelper
        boolean compareAndSetRunner(InterruptibleTask task, Thread expect, Thread update) {
            return AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(this.runnerUpdater, task, expect, update);
        }
    }

    /* loaded from: classes.dex */
    private static final class SynchronizedAtomicHelper extends AtomicHelper {
        private SynchronizedAtomicHelper() {
            super();
        }

        @Override // com.google.common.util.concurrent.InterruptibleTask.AtomicHelper
        boolean compareAndSetRunner(InterruptibleTask task, Thread expect, Thread update) {
            synchronized (task) {
                if (task.runner == expect) {
                    task.runner = update;
                }
            }
            return true;
        }
    }
}
