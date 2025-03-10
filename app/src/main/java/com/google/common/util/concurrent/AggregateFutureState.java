package com.google.common.util.concurrent;

import androidx.concurrent.futures.AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0;
import com.google.common.collect.Sets;
import java.util.Set;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: classes.dex */
abstract class AggregateFutureState {
    private static final AtomicHelper ATOMIC_HELPER;
    private static final Logger log = Logger.getLogger(AggregateFutureState.class.getName());
    private volatile int remaining;
    private volatile Set<Throwable> seenExceptions = null;

    abstract void addInitialException(Set<Throwable> set);

    static /* synthetic */ int access$310(AggregateFutureState x0) {
        int i = x0.remaining;
        x0.remaining = i - 1;
        return i;
    }

    static {
        AtomicHelper helper;
        try {
            helper = new SafeAtomicHelper(AtomicReferenceFieldUpdater.newUpdater(AggregateFutureState.class, Set.class, "seenExceptions"), AtomicIntegerFieldUpdater.newUpdater(AggregateFutureState.class, "remaining"));
        } catch (Throwable reflectionFailure) {
            log.log(Level.SEVERE, "SafeAtomicHelper is broken!", reflectionFailure);
            helper = new SynchronizedAtomicHelper();
        }
        ATOMIC_HELPER = helper;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AggregateFutureState(int remainingFutures) {
        this.remaining = remainingFutures;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Set<Throwable> getOrInitSeenExceptions() {
        Set<Throwable> seenExceptionsLocal = this.seenExceptions;
        if (seenExceptionsLocal == null) {
            Set<Throwable> seenExceptionsLocal2 = Sets.newConcurrentHashSet();
            addInitialException(seenExceptionsLocal2);
            ATOMIC_HELPER.compareAndSetSeenExceptions(this, null, seenExceptionsLocal2);
            return this.seenExceptions;
        }
        return seenExceptionsLocal;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int decrementRemainingAndGet() {
        return ATOMIC_HELPER.decrementAndGetRemainingCount(this);
    }

    /* loaded from: classes.dex */
    private static abstract class AtomicHelper {
        abstract void compareAndSetSeenExceptions(AggregateFutureState aggregateFutureState, Set<Throwable> set, Set<Throwable> set2);

        abstract int decrementAndGetRemainingCount(AggregateFutureState aggregateFutureState);

        private AtomicHelper() {
        }
    }

    /* loaded from: classes.dex */
    private static final class SafeAtomicHelper extends AtomicHelper {
        final AtomicIntegerFieldUpdater<AggregateFutureState> remainingCountUpdater;
        final AtomicReferenceFieldUpdater<AggregateFutureState, Set<Throwable>> seenExceptionsUpdater;

        SafeAtomicHelper(AtomicReferenceFieldUpdater seenExceptionsUpdater, AtomicIntegerFieldUpdater remainingCountUpdater) {
            super();
            this.seenExceptionsUpdater = seenExceptionsUpdater;
            this.remainingCountUpdater = remainingCountUpdater;
        }

        @Override // com.google.common.util.concurrent.AggregateFutureState.AtomicHelper
        void compareAndSetSeenExceptions(AggregateFutureState state, Set<Throwable> expect, Set<Throwable> update) {
            AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(this.seenExceptionsUpdater, state, expect, update);
        }

        @Override // com.google.common.util.concurrent.AggregateFutureState.AtomicHelper
        int decrementAndGetRemainingCount(AggregateFutureState state) {
            return this.remainingCountUpdater.decrementAndGet(state);
        }
    }

    /* loaded from: classes.dex */
    private static final class SynchronizedAtomicHelper extends AtomicHelper {
        private SynchronizedAtomicHelper() {
            super();
        }

        @Override // com.google.common.util.concurrent.AggregateFutureState.AtomicHelper
        void compareAndSetSeenExceptions(AggregateFutureState state, Set<Throwable> expect, Set<Throwable> update) {
            synchronized (state) {
                if (state.seenExceptions == expect) {
                    state.seenExceptions = update;
                }
            }
        }

        @Override // com.google.common.util.concurrent.AggregateFutureState.AtomicHelper
        int decrementAndGetRemainingCount(AggregateFutureState state) {
            int i;
            synchronized (state) {
                AggregateFutureState.access$310(state);
                i = state.remaining;
            }
            return i;
        }
    }
}
