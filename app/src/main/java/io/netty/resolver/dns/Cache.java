package io.netty.resolver.dns;

import androidx.concurrent.futures.AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0;
import io.netty.channel.EventLoop;
import io.netty.util.internal.PlatformDependent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Delayed;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public abstract class Cache<E> {
    private final ConcurrentMap<String, Cache<E>.Entries> resolveCache = PlatformDependent.newConcurrentHashMap();
    private static final AtomicReferenceFieldUpdater<Entries, ScheduledFuture> FUTURE_UPDATER = AtomicReferenceFieldUpdater.newUpdater(Entries.class, ScheduledFuture.class, "expirationFuture");
    private static final ScheduledFuture<?> CANCELLED = new ScheduledFuture<Object>() { // from class: io.netty.resolver.dns.Cache.1
        @Override // java.util.concurrent.Future
        public boolean cancel(boolean mayInterruptIfRunning) {
            return false;
        }

        @Override // java.util.concurrent.Delayed
        public long getDelay(TimeUnit unit) {
            return Long.MIN_VALUE;
        }

        @Override // java.lang.Comparable
        public int compareTo(Delayed o) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.concurrent.Future
        public boolean isCancelled() {
            return true;
        }

        @Override // java.util.concurrent.Future
        public boolean isDone() {
            return true;
        }

        @Override // java.util.concurrent.Future
        public Object get() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.concurrent.Future
        public Object get(long timeout, TimeUnit unit) {
            throw new UnsupportedOperationException();
        }
    };
    static final int MAX_SUPPORTED_TTL_SECS = (int) TimeUnit.DAYS.toSeconds(730);

    protected abstract boolean equals(E e, E e2);

    protected abstract boolean shouldReplaceAll(E e);

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void clear() {
        while (!this.resolveCache.isEmpty()) {
            Iterator<Map.Entry<String, Cache<E>.Entries>> i = this.resolveCache.entrySet().iterator();
            while (i.hasNext()) {
                Map.Entry<String, Cache<E>.Entries> e = i.next();
                i.remove();
                e.getValue().clearAndCancel();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean clear(String hostname) {
        Cache<E>.Entries entries = this.resolveCache.remove(hostname);
        return entries != null && entries.clearAndCancel();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final List<? extends E> get(String str) {
        Cache<E>.Entries entries = this.resolveCache.get(str);
        if (entries == null) {
            return null;
        }
        return entries.get();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void cache(String hostname, E value, int ttl, EventLoop loop) {
        Cache<E>.Entries oldEntries;
        Cache<E>.Entries entries = this.resolveCache.get(hostname);
        if (entries == null && (oldEntries = this.resolveCache.putIfAbsent(hostname, (entries = new Entries(hostname)))) != null) {
            entries = oldEntries;
        }
        entries.add(value, ttl, loop);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int size() {
        return this.resolveCache.size();
    }

    protected void sortEntries(String hostname, List<E> entries) {
    }

    /* loaded from: classes4.dex */
    private final class Entries extends AtomicReference<List<E>> implements Runnable {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        volatile ScheduledFuture<?> expirationFuture;
        private final String hostname;

        Entries(String hostname) {
            super(Collections.emptyList());
            this.hostname = hostname;
        }

        void add(E e, int ttl, EventLoop loop) {
            if (Cache.this.shouldReplaceAll(e)) {
                set(Collections.singletonList(e));
                scheduleCacheExpirationIfNeeded(ttl, loop);
                return;
            }
            while (true) {
                List<E> entries = get();
                if (!entries.isEmpty()) {
                    E firstEntry = entries.get(0);
                    if (Cache.this.shouldReplaceAll(firstEntry)) {
                        if (entries.size() != 1) {
                            throw new AssertionError();
                        }
                        if (compareAndSet(entries, Collections.singletonList(e))) {
                            scheduleCacheExpirationIfNeeded(ttl, loop);
                            return;
                        }
                    } else {
                        ArrayList arrayList = new ArrayList(entries.size() + 1);
                        int i = 0;
                        E replacedEntry = null;
                        while (true) {
                            E entry = entries.get(i);
                            if (!Cache.this.equals(e, entry)) {
                                arrayList.add(entry);
                                i++;
                                if (i >= entries.size()) {
                                    break;
                                }
                            } else {
                                replacedEntry = entry;
                                arrayList.add(e);
                                while (true) {
                                    i++;
                                    if (i >= entries.size()) {
                                        break;
                                    } else {
                                        arrayList.add(entries.get(i));
                                    }
                                }
                            }
                        }
                        if (replacedEntry == null) {
                            arrayList.add(e);
                        }
                        Cache.this.sortEntries(this.hostname, arrayList);
                        if (compareAndSet(entries, Collections.unmodifiableList(arrayList))) {
                            scheduleCacheExpirationIfNeeded(ttl, loop);
                            return;
                        }
                    }
                } else if (compareAndSet(entries, Collections.singletonList(e))) {
                    scheduleCacheExpirationIfNeeded(ttl, loop);
                    return;
                }
            }
        }

        private void scheduleCacheExpirationIfNeeded(int ttl, EventLoop loop) {
            while (true) {
                ScheduledFuture<?> oldFuture = (ScheduledFuture) Cache.FUTURE_UPDATER.get(this);
                if (oldFuture == null || oldFuture.getDelay(TimeUnit.SECONDS) > ttl) {
                    ScheduledFuture<?> newFuture = loop.schedule((Runnable) this, ttl, TimeUnit.SECONDS);
                    if (AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(Cache.FUTURE_UPDATER, this, oldFuture, newFuture)) {
                        if (oldFuture != null) {
                            oldFuture.cancel(true);
                            return;
                        }
                        return;
                    }
                    newFuture.cancel(true);
                } else {
                    return;
                }
            }
        }

        boolean clearAndCancel() {
            List<E> entries = getAndSet(Collections.emptyList());
            if (entries.isEmpty()) {
                return false;
            }
            ScheduledFuture<?> expirationFuture = (ScheduledFuture) Cache.FUTURE_UPDATER.getAndSet(this, Cache.CANCELLED);
            if (expirationFuture != null) {
                expirationFuture.cancel(false);
                return true;
            }
            return true;
        }

        @Override // java.lang.Runnable
        public void run() {
            Cache.this.resolveCache.remove(this.hostname, this);
            clearAndCancel();
        }
    }
}
