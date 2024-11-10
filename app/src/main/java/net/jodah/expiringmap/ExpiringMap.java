package net.jodah.expiringmap;

import java.lang.ref.WeakReference;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import net.jodah.expiringmap.internal.Assert;
import net.jodah.expiringmap.internal.NamedThreadFactory;

/* loaded from: classes5.dex */
public class ExpiringMap<K, V> implements ConcurrentMap<K, V> {
    static volatile ScheduledExecutorService EXPIRER;
    static volatile ThreadPoolExecutor LISTENER_SERVICE;
    static ThreadFactory THREAD_FACTORY;
    List<ExpirationListener<K, V>> asyncExpirationListeners;
    private final EntryMap<K, V> entries;
    private final EntryLoader<? super K, ? extends V> entryLoader;
    List<ExpirationListener<K, V>> expirationListeners;
    private AtomicLong expirationNanos;
    private final AtomicReference<ExpirationPolicy> expirationPolicy;
    private final ExpiringEntryLoader<? super K, ? extends V> expiringEntryLoader;
    private int maxSize;
    private final Lock readLock;
    private final ReadWriteLock readWriteLock;
    private final boolean variableExpiration;
    private final Lock writeLock;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public interface EntryMap<K, V> extends Map<K, ExpiringEntry<K, V>> {
        ExpiringEntry<K, V> first();

        void reorder(ExpiringEntry<K, V> expiringEntry);

        Iterator<ExpiringEntry<K, V>> valuesIterator();
    }

    public static void setThreadFactory(ThreadFactory threadFactory) {
        THREAD_FACTORY = (ThreadFactory) Assert.notNull(threadFactory, "threadFactory");
    }

    private ExpiringMap(Builder<K, V> builder) {
        this.readWriteLock = new ReentrantReadWriteLock();
        this.readLock = this.readWriteLock.readLock();
        this.writeLock = this.readWriteLock.writeLock();
        if (EXPIRER == null) {
            synchronized (ExpiringMap.class) {
                if (EXPIRER == null) {
                    EXPIRER = Executors.newSingleThreadScheduledExecutor(THREAD_FACTORY == null ? new NamedThreadFactory("ExpiringMap-Expirer") : THREAD_FACTORY);
                }
            }
        }
        if (LISTENER_SERVICE == null && ((Builder) builder).asyncExpirationListeners != null) {
            initListenerService();
        }
        this.variableExpiration = ((Builder) builder).variableExpiration;
        this.entries = this.variableExpiration ? new EntryTreeHashMap<>() : new EntryLinkedHashMap<>();
        if (((Builder) builder).expirationListeners != null) {
            this.expirationListeners = new CopyOnWriteArrayList(((Builder) builder).expirationListeners);
        }
        if (((Builder) builder).asyncExpirationListeners != null) {
            this.asyncExpirationListeners = new CopyOnWriteArrayList(((Builder) builder).asyncExpirationListeners);
        }
        this.expirationPolicy = new AtomicReference<>(((Builder) builder).expirationPolicy);
        this.expirationNanos = new AtomicLong(TimeUnit.NANOSECONDS.convert(((Builder) builder).duration, ((Builder) builder).timeUnit));
        this.maxSize = ((Builder) builder).maxSize;
        this.entryLoader = ((Builder) builder).entryLoader;
        this.expiringEntryLoader = ((Builder) builder).expiringEntryLoader;
    }

    /* loaded from: classes5.dex */
    public static final class Builder<K, V> {
        private List<ExpirationListener<K, V>> asyncExpirationListeners;
        private long duration;
        private EntryLoader<K, V> entryLoader;
        private List<ExpirationListener<K, V>> expirationListeners;
        private ExpirationPolicy expirationPolicy;
        private ExpiringEntryLoader<K, V> expiringEntryLoader;
        private int maxSize;
        private TimeUnit timeUnit;
        private boolean variableExpiration;

        private Builder() {
            this.expirationPolicy = ExpirationPolicy.CREATED;
            this.timeUnit = TimeUnit.SECONDS;
            this.duration = 60L;
            this.maxSize = Integer.MAX_VALUE;
        }

        public <K1 extends K, V1 extends V> ExpiringMap<K1, V1> build() {
            return new ExpiringMap<>(this);
        }

        public Builder<K, V> expiration(long duration, TimeUnit timeUnit) {
            this.duration = duration;
            this.timeUnit = (TimeUnit) Assert.notNull(timeUnit, "timeUnit");
            return this;
        }

        public Builder<K, V> maxSize(int maxSize) {
            Assert.operation(maxSize > 0, "maxSize");
            this.maxSize = maxSize;
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public <K1 extends K, V1 extends V> Builder<K1, V1> entryLoader(EntryLoader<? super K1, ? super V1> loader) {
            assertNoLoaderSet();
            this.entryLoader = (EntryLoader) Assert.notNull(loader, "loader");
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public <K1 extends K, V1 extends V> Builder<K1, V1> expiringEntryLoader(ExpiringEntryLoader<? super K1, ? super V1> loader) {
            assertNoLoaderSet();
            this.expiringEntryLoader = (ExpiringEntryLoader) Assert.notNull(loader, "loader");
            variableExpiration();
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public <K1 extends K, V1 extends V> Builder<K1, V1> expirationListener(ExpirationListener<? super K1, ? super V1> listener) {
            Assert.notNull(listener, "listener");
            if (this.expirationListeners == null) {
                this.expirationListeners = new ArrayList();
            }
            this.expirationListeners.add(listener);
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public <K1 extends K, V1 extends V> Builder<K1, V1> expirationListeners(List<ExpirationListener<? super K1, ? super V1>> listeners) {
            Assert.notNull(listeners, "listeners");
            if (this.expirationListeners == null) {
                this.expirationListeners = new ArrayList(listeners.size());
            }
            for (ExpirationListener<? super K1, ? super V1> listener : listeners) {
                this.expirationListeners.add(listener);
            }
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public <K1 extends K, V1 extends V> Builder<K1, V1> asyncExpirationListener(ExpirationListener<? super K1, ? super V1> listener) {
            Assert.notNull(listener, "listener");
            if (this.asyncExpirationListeners == null) {
                this.asyncExpirationListeners = new ArrayList();
            }
            this.asyncExpirationListeners.add(listener);
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public <K1 extends K, V1 extends V> Builder<K1, V1> asyncExpirationListeners(List<ExpirationListener<? super K1, ? super V1>> listeners) {
            Assert.notNull(listeners, "listeners");
            if (this.asyncExpirationListeners == null) {
                this.asyncExpirationListeners = new ArrayList(listeners.size());
            }
            for (ExpirationListener<? super K1, ? super V1> listener : listeners) {
                this.asyncExpirationListeners.add(listener);
            }
            return this;
        }

        public Builder<K, V> expirationPolicy(ExpirationPolicy expirationPolicy) {
            this.expirationPolicy = (ExpirationPolicy) Assert.notNull(expirationPolicy, "expirationPolicy");
            return this;
        }

        public Builder<K, V> variableExpiration() {
            this.variableExpiration = true;
            return this;
        }

        private void assertNoLoaderSet() {
            Assert.state(this.entryLoader == null && this.expiringEntryLoader == null, "Either entryLoader or expiringEntryLoader may be set, not both", new Object[0]);
        }
    }

    /* loaded from: classes5.dex */
    private static class EntryLinkedHashMap<K, V> extends LinkedHashMap<K, ExpiringEntry<K, V>> implements EntryMap<K, V> {
        private static final long serialVersionUID = 1;

        private EntryLinkedHashMap() {
        }

        @Override // java.util.LinkedHashMap, java.util.HashMap, java.util.AbstractMap, java.util.Map
        public boolean containsValue(Object obj) {
            Iterator<V> it2 = values().iterator();
            while (it2.hasNext()) {
                V v = ((ExpiringEntry) it2.next()).value;
                if (v == obj) {
                    return true;
                }
                if (obj != null && obj.equals(v)) {
                    return true;
                }
            }
            return false;
        }

        @Override // net.jodah.expiringmap.ExpiringMap.EntryMap
        public ExpiringEntry<K, V> first() {
            if (isEmpty()) {
                return null;
            }
            return (ExpiringEntry) values().iterator().next();
        }

        @Override // net.jodah.expiringmap.ExpiringMap.EntryMap
        public void reorder(ExpiringEntry<K, V> value) {
            remove(value.key);
            value.resetExpiration();
            put(value.key, value);
        }

        @Override // net.jodah.expiringmap.ExpiringMap.EntryMap
        public Iterator<ExpiringEntry<K, V>> valuesIterator() {
            return values().iterator();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes5.dex */
        public abstract class AbstractHashIterator {
            private final Iterator<Map.Entry<K, ExpiringEntry<K, V>>> iterator;
            private ExpiringEntry<K, V> next;

            AbstractHashIterator() {
                this.iterator = EntryLinkedHashMap.this.entrySet().iterator();
            }

            public boolean hasNext() {
                return this.iterator.hasNext();
            }

            public ExpiringEntry<K, V> getNext() {
                this.next = this.iterator.next().getValue();
                return this.next;
            }

            public void remove() {
                this.iterator.remove();
            }
        }

        /* loaded from: classes5.dex */
        final class KeyIterator extends EntryLinkedHashMap<K, V>.AbstractHashIterator implements Iterator<K> {
            KeyIterator() {
                super();
            }

            @Override // java.util.Iterator
            public final K next() {
                return getNext().key;
            }
        }

        /* loaded from: classes5.dex */
        final class ValueIterator extends EntryLinkedHashMap<K, V>.AbstractHashIterator implements Iterator<V> {
            ValueIterator() {
                super();
            }

            @Override // java.util.Iterator
            public final V next() {
                return getNext().value;
            }
        }

        /* loaded from: classes5.dex */
        public final class EntryIterator extends EntryLinkedHashMap<K, V>.AbstractHashIterator implements Iterator<Map.Entry<K, V>> {
            public EntryIterator() {
                super();
            }

            @Override // net.jodah.expiringmap.ExpiringMap.EntryLinkedHashMap.AbstractHashIterator
            public /* bridge */ /* synthetic */ ExpiringEntry getNext() {
                return super.getNext();
            }

            @Override // net.jodah.expiringmap.ExpiringMap.EntryLinkedHashMap.AbstractHashIterator, java.util.Iterator
            public /* bridge */ /* synthetic */ boolean hasNext() {
                return super.hasNext();
            }

            @Override // net.jodah.expiringmap.ExpiringMap.EntryLinkedHashMap.AbstractHashIterator, java.util.Iterator
            public /* bridge */ /* synthetic */ void remove() {
                super.remove();
            }

            @Override // java.util.Iterator
            public final Map.Entry<K, V> next() {
                return ExpiringMap.mapEntryFor(getNext());
            }
        }
    }

    /* loaded from: classes5.dex */
    private static class EntryTreeHashMap<K, V> extends HashMap<K, ExpiringEntry<K, V>> implements EntryMap<K, V> {
        private static final long serialVersionUID = 1;
        SortedSet<ExpiringEntry<K, V>> sortedSet;

        private EntryTreeHashMap() {
            this.sortedSet = new ConcurrentSkipListSet();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
        public /* bridge */ /* synthetic */ Object put(Object obj, Object obj2) {
            return put((EntryTreeHashMap<K, V>) obj, (ExpiringEntry<EntryTreeHashMap<K, V>, V>) obj2);
        }

        @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
        public void clear() {
            super.clear();
            this.sortedSet.clear();
        }

        @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
        public boolean containsValue(Object obj) {
            Iterator<V> it2 = values().iterator();
            while (it2.hasNext()) {
                V v = ((ExpiringEntry) it2.next()).value;
                if (v == obj) {
                    return true;
                }
                if (obj != null && obj.equals(v)) {
                    return true;
                }
            }
            return false;
        }

        @Override // net.jodah.expiringmap.ExpiringMap.EntryMap
        public ExpiringEntry<K, V> first() {
            if (this.sortedSet.isEmpty()) {
                return null;
            }
            return this.sortedSet.first();
        }

        /* JADX WARN: Multi-variable type inference failed */
        public ExpiringEntry<K, V> put(K key, ExpiringEntry<K, V> expiringEntry) {
            this.sortedSet.add(expiringEntry);
            return (ExpiringEntry) super.put((EntryTreeHashMap<K, V>) key, (K) expiringEntry);
        }

        @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
        public ExpiringEntry<K, V> remove(Object key) {
            ExpiringEntry<K, V> entry = (ExpiringEntry) super.remove(key);
            if (entry != null) {
                this.sortedSet.remove(entry);
            }
            return entry;
        }

        @Override // net.jodah.expiringmap.ExpiringMap.EntryMap
        public void reorder(ExpiringEntry<K, V> value) {
            this.sortedSet.remove(value);
            value.resetExpiration();
            this.sortedSet.add(value);
        }

        @Override // net.jodah.expiringmap.ExpiringMap.EntryMap
        public Iterator<ExpiringEntry<K, V>> valuesIterator() {
            return new ExpiringEntryIterator();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes5.dex */
        public abstract class AbstractHashIterator {
            private final Iterator<ExpiringEntry<K, V>> iterator;
            protected ExpiringEntry<K, V> next;

            AbstractHashIterator() {
                this.iterator = EntryTreeHashMap.this.sortedSet.iterator();
            }

            public boolean hasNext() {
                return this.iterator.hasNext();
            }

            public ExpiringEntry<K, V> getNext() {
                this.next = this.iterator.next();
                return this.next;
            }

            public void remove() {
                EntryTreeHashMap.super.remove((Object) this.next.key);
                this.iterator.remove();
            }
        }

        /* loaded from: classes5.dex */
        final class ExpiringEntryIterator extends EntryTreeHashMap<K, V>.AbstractHashIterator implements Iterator<ExpiringEntry<K, V>> {
            ExpiringEntryIterator() {
                super();
            }

            @Override // java.util.Iterator
            public final ExpiringEntry<K, V> next() {
                return getNext();
            }
        }

        /* loaded from: classes5.dex */
        final class KeyIterator extends EntryTreeHashMap<K, V>.AbstractHashIterator implements Iterator<K> {
            KeyIterator() {
                super();
            }

            @Override // java.util.Iterator
            public final K next() {
                return getNext().key;
            }
        }

        /* loaded from: classes5.dex */
        final class ValueIterator extends EntryTreeHashMap<K, V>.AbstractHashIterator implements Iterator<V> {
            ValueIterator() {
                super();
            }

            @Override // java.util.Iterator
            public final V next() {
                return getNext().value;
            }
        }

        /* loaded from: classes5.dex */
        final class EntryIterator extends EntryTreeHashMap<K, V>.AbstractHashIterator implements Iterator<Map.Entry<K, V>> {
            EntryIterator() {
                super();
            }

            @Override // java.util.Iterator
            public final Map.Entry<K, V> next() {
                return ExpiringMap.mapEntryFor(getNext());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes5.dex */
    public static class ExpiringEntry<K, V> implements Comparable<ExpiringEntry<K, V>> {
        volatile Future<?> entryFuture;
        final AtomicLong expectedExpiration = new AtomicLong();
        final AtomicLong expirationNanos;
        final AtomicReference<ExpirationPolicy> expirationPolicy;
        final K key;
        volatile boolean scheduled;
        V value;

        ExpiringEntry(K key, V value, AtomicReference<ExpirationPolicy> expirationPolicy, AtomicLong expirationNanos) {
            this.key = key;
            this.value = value;
            this.expirationPolicy = expirationPolicy;
            this.expirationNanos = expirationNanos;
            resetExpiration();
        }

        @Override // java.lang.Comparable
        public int compareTo(ExpiringEntry<K, V> other) {
            if (this.key.equals(other.key)) {
                return 0;
            }
            return this.expectedExpiration.get() < other.expectedExpiration.get() ? -1 : 1;
        }

        public int hashCode() {
            int result = (1 * 31) + (this.key == null ? 0 : this.key.hashCode());
            return (result * 31) + (this.value != null ? this.value.hashCode() : 0);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ExpiringEntry<?, ?> other = (ExpiringEntry) obj;
            if (!this.key.equals(other.key)) {
                return false;
            }
            if (this.value == null) {
                if (other.value != null) {
                    return false;
                }
            } else if (!this.value.equals(other.value)) {
                return false;
            }
            return true;
        }

        public String toString() {
            return this.value.toString();
        }

        synchronized boolean cancel() {
            boolean result;
            result = this.scheduled;
            if (this.entryFuture != null) {
                this.entryFuture.cancel(false);
            }
            this.entryFuture = null;
            this.scheduled = false;
            return result;
        }

        synchronized V getValue() {
            return this.value;
        }

        void resetExpiration() {
            this.expectedExpiration.set(this.expirationNanos.get() + System.nanoTime());
        }

        synchronized void schedule(Future<?> entryFuture) {
            this.entryFuture = entryFuture;
            this.scheduled = true;
        }

        synchronized void setValue(V value) {
            this.value = value;
        }
    }

    public static Builder<Object, Object> builder() {
        return new Builder<>();
    }

    public static <K, V> ExpiringMap<K, V> create() {
        return new ExpiringMap<>(builder());
    }

    public synchronized void addExpirationListener(ExpirationListener<K, V> listener) {
        Assert.notNull(listener, "listener");
        if (this.expirationListeners == null) {
            this.expirationListeners = new CopyOnWriteArrayList();
        }
        this.expirationListeners.add(listener);
    }

    public synchronized void addAsyncExpirationListener(ExpirationListener<K, V> listener) {
        Assert.notNull(listener, "listener");
        if (this.asyncExpirationListeners == null) {
            this.asyncExpirationListeners = new CopyOnWriteArrayList();
        }
        this.asyncExpirationListeners.add(listener);
        if (LISTENER_SERVICE == null) {
            initListenerService();
        }
    }

    @Override // java.util.Map
    public void clear() {
        this.writeLock.lock();
        try {
            Iterator<V> it2 = this.entries.values().iterator();
            while (it2.hasNext()) {
                ((ExpiringEntry) it2.next()).cancel();
            }
            this.entries.clear();
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override // java.util.Map
    public boolean containsKey(Object key) {
        this.readLock.lock();
        try {
            return this.entries.containsKey(key);
        } finally {
            this.readLock.unlock();
        }
    }

    @Override // java.util.Map
    public boolean containsValue(Object value) {
        this.readLock.lock();
        try {
            return this.entries.containsValue(value);
        } finally {
            this.readLock.unlock();
        }
    }

    @Override // java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        return new AbstractSet<Map.Entry<K, V>>() { // from class: net.jodah.expiringmap.ExpiringMap.1
            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public void clear() {
                ExpiringMap.this.clear();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean contains(Object entry) {
                if (!(entry instanceof Map.Entry)) {
                    return false;
                }
                Map.Entry<?, ?> e = (Map.Entry) entry;
                return ExpiringMap.this.containsKey(e.getKey());
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public Iterator<Map.Entry<K, V>> iterator() {
                if (ExpiringMap.this.entries instanceof EntryLinkedHashMap) {
                    EntryLinkedHashMap entryLinkedHashMap = (EntryLinkedHashMap) ExpiringMap.this.entries;
                    entryLinkedHashMap.getClass();
                    return new EntryLinkedHashMap.EntryIterator();
                }
                EntryTreeHashMap entryTreeHashMap = (EntryTreeHashMap) ExpiringMap.this.entries;
                entryTreeHashMap.getClass();
                return new EntryTreeHashMap.EntryIterator();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean remove(Object entry) {
                if (!(entry instanceof Map.Entry)) {
                    return false;
                }
                Map.Entry<?, ?> e = (Map.Entry) entry;
                return ExpiringMap.this.remove(e.getKey()) != null;
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return ExpiringMap.this.size();
            }
        };
    }

    @Override // java.util.Map
    public boolean equals(Object obj) {
        this.readLock.lock();
        try {
            return this.entries.equals(obj);
        } finally {
            this.readLock.unlock();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Map
    public V get(Object obj) {
        ExpiringEntry<K, V> entry = getEntry(obj);
        if (entry == null) {
            return load(obj);
        }
        if (ExpirationPolicy.ACCESSED.equals(entry.expirationPolicy.get())) {
            resetEntry(entry, false);
        }
        return entry.getValue();
    }

    private V load(K key) {
        if (this.entryLoader == null && this.expiringEntryLoader == null) {
            return null;
        }
        this.writeLock.lock();
        try {
            ExpiringEntry<K, V> entry = getEntry(key);
            if (entry != null) {
                return entry.getValue();
            }
            if (this.entryLoader != null) {
                V value = this.entryLoader.load(key);
                put(key, value);
                return value;
            }
            ExpiringValue<? extends V> expiringValue = this.expiringEntryLoader.load(key);
            if (expiringValue == null) {
                put(key, null);
                return null;
            }
            long duration = expiringValue.getTimeUnit() == null ? this.expirationNanos.get() : expiringValue.getDuration();
            TimeUnit timeUnit = expiringValue.getTimeUnit() == null ? TimeUnit.NANOSECONDS : expiringValue.getTimeUnit();
            put(key, expiringValue.getValue(), expiringValue.getExpirationPolicy() == null ? this.expirationPolicy.get() : expiringValue.getExpirationPolicy(), duration, timeUnit);
            return expiringValue.getValue();
        } finally {
            this.writeLock.unlock();
        }
    }

    public long getExpiration() {
        return TimeUnit.NANOSECONDS.toMillis(this.expirationNanos.get());
    }

    public long getExpiration(K key) {
        Assert.notNull(key, "key");
        ExpiringEntry<K, V> entry = getEntry(key);
        Assert.element(entry, key);
        return TimeUnit.NANOSECONDS.toMillis(entry.expirationNanos.get());
    }

    public ExpirationPolicy getExpirationPolicy(K key) {
        Assert.notNull(key, "key");
        ExpiringEntry<K, V> entry = getEntry(key);
        Assert.element(entry, key);
        return entry.expirationPolicy.get();
    }

    public long getExpectedExpiration(K key) {
        Assert.notNull(key, "key");
        ExpiringEntry<K, V> entry = getEntry(key);
        Assert.element(entry, key);
        return TimeUnit.NANOSECONDS.toMillis(entry.expectedExpiration.get() - System.nanoTime());
    }

    public int getMaxSize() {
        return this.maxSize;
    }

    @Override // java.util.Map
    public int hashCode() {
        this.readLock.lock();
        try {
            return this.entries.hashCode();
        } finally {
            this.readLock.unlock();
        }
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        this.readLock.lock();
        try {
            return this.entries.isEmpty();
        } finally {
            this.readLock.unlock();
        }
    }

    @Override // java.util.Map
    public Set<K> keySet() {
        return new AbstractSet<K>() { // from class: net.jodah.expiringmap.ExpiringMap.2
            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public void clear() {
                ExpiringMap.this.clear();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean contains(Object key) {
                return ExpiringMap.this.containsKey(key);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public Iterator<K> iterator() {
                if (ExpiringMap.this.entries instanceof EntryLinkedHashMap) {
                    EntryLinkedHashMap entryLinkedHashMap = (EntryLinkedHashMap) ExpiringMap.this.entries;
                    entryLinkedHashMap.getClass();
                    return new EntryLinkedHashMap.KeyIterator();
                }
                EntryTreeHashMap entryTreeHashMap = (EntryTreeHashMap) ExpiringMap.this.entries;
                entryTreeHashMap.getClass();
                return new EntryTreeHashMap.KeyIterator();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean remove(Object value) {
                return ExpiringMap.this.remove(value) != null;
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return ExpiringMap.this.size();
            }
        };
    }

    @Override // java.util.Map
    public V put(K key, V value) {
        Assert.notNull(key, "key");
        return putInternal(key, value, this.expirationPolicy.get(), this.expirationNanos.get());
    }

    public V put(K key, V value, ExpirationPolicy expirationPolicy) {
        return put(key, value, expirationPolicy, this.expirationNanos.get(), TimeUnit.NANOSECONDS);
    }

    public V put(K key, V value, long duration, TimeUnit timeUnit) {
        return put(key, value, this.expirationPolicy.get(), duration, timeUnit);
    }

    public V put(K key, V value, ExpirationPolicy expirationPolicy, long duration, TimeUnit timeUnit) {
        Assert.notNull(key, "key");
        Assert.notNull(expirationPolicy, "expirationPolicy");
        Assert.notNull(timeUnit, "timeUnit");
        Assert.operation(this.variableExpiration, "Variable expiration is not enabled");
        return putInternal(key, value, expirationPolicy, TimeUnit.NANOSECONDS.convert(duration, timeUnit));
    }

    @Override // java.util.Map
    public void putAll(Map<? extends K, ? extends V> map) {
        Assert.notNull(map, "map");
        long expiration = this.expirationNanos.get();
        ExpirationPolicy expirationPolicy = this.expirationPolicy.get();
        this.writeLock.lock();
        try {
            for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
                putInternal(entry.getKey(), entry.getValue(), expirationPolicy, expiration);
            }
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override // java.util.concurrent.ConcurrentMap, java.util.Map
    public V putIfAbsent(K k, V v) {
        Assert.notNull(k, "key");
        this.writeLock.lock();
        try {
            if (!this.entries.containsKey(k)) {
                return putInternal(k, v, this.expirationPolicy.get(), this.expirationNanos.get());
            }
            return (V) ((ExpiringEntry) this.entries.get(k)).getValue();
        } finally {
            this.writeLock.unlock();
        }
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [V, java.lang.Object] */
    @Override // java.util.Map
    public V remove(Object obj) {
        Assert.notNull(obj, "key");
        this.writeLock.lock();
        try {
            ExpiringEntry expiringEntry = (ExpiringEntry) this.entries.remove(obj);
            if (expiringEntry != null) {
                if (expiringEntry.cancel()) {
                    scheduleEntry(this.entries.first());
                }
                return expiringEntry.getValue();
            }
            this.writeLock.unlock();
            return null;
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override // java.util.concurrent.ConcurrentMap, java.util.Map
    public boolean remove(Object obj, Object obj2) {
        Assert.notNull(obj, "key");
        this.writeLock.lock();
        try {
            ExpiringEntry expiringEntry = (ExpiringEntry) this.entries.get(obj);
            if (expiringEntry != null && expiringEntry.getValue().equals(obj2)) {
                this.entries.remove(obj);
                if (expiringEntry.cancel()) {
                    scheduleEntry(this.entries.first());
                }
                this.writeLock.unlock();
                return true;
            }
            this.writeLock.unlock();
            return false;
        } catch (Throwable th) {
            this.writeLock.unlock();
            throw th;
        }
    }

    @Override // java.util.concurrent.ConcurrentMap, java.util.Map
    public V replace(K key, V value) {
        Assert.notNull(key, "key");
        this.writeLock.lock();
        try {
            if (this.entries.containsKey(key)) {
                return putInternal(key, value, this.expirationPolicy.get(), this.expirationNanos.get());
            }
            this.writeLock.unlock();
            return null;
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override // java.util.concurrent.ConcurrentMap, java.util.Map
    public boolean replace(K k, V v, V v2) {
        Assert.notNull(k, "key");
        this.writeLock.lock();
        try {
            ExpiringEntry expiringEntry = (ExpiringEntry) this.entries.get(k);
            if (expiringEntry != null && expiringEntry.getValue().equals(v)) {
                putInternal(k, v2, this.expirationPolicy.get(), this.expirationNanos.get());
                this.writeLock.unlock();
                return true;
            }
            this.writeLock.unlock();
            return false;
        } catch (Throwable th) {
            this.writeLock.unlock();
            throw th;
        }
    }

    public void removeExpirationListener(ExpirationListener<K, V> listener) {
        Assert.notNull(listener, "listener");
        for (int i = 0; i < this.expirationListeners.size(); i++) {
            if (this.expirationListeners.get(i).equals(listener)) {
                this.expirationListeners.remove(i);
                return;
            }
        }
    }

    public void removeAsyncExpirationListener(ExpirationListener<K, V> listener) {
        Assert.notNull(listener, "listener");
        for (int i = 0; i < this.asyncExpirationListeners.size(); i++) {
            if (this.asyncExpirationListeners.get(i).equals(listener)) {
                this.asyncExpirationListeners.remove(i);
                return;
            }
        }
    }

    public void resetExpiration(K key) {
        Assert.notNull(key, "key");
        ExpiringEntry<K, V> entry = getEntry(key);
        if (entry != null) {
            resetEntry(entry, false);
        }
    }

    public void setExpiration(K k, long j, TimeUnit timeUnit) {
        Assert.notNull(k, "key");
        Assert.notNull(timeUnit, "timeUnit");
        Assert.operation(this.variableExpiration, "Variable expiration is not enabled");
        this.writeLock.lock();
        try {
            ExpiringEntry<K, V> expiringEntry = (ExpiringEntry) this.entries.get(k);
            if (expiringEntry != null) {
                expiringEntry.expirationNanos.set(TimeUnit.NANOSECONDS.convert(j, timeUnit));
                resetEntry(expiringEntry, true);
            }
        } finally {
            this.writeLock.unlock();
        }
    }

    public void setExpiration(long duration, TimeUnit timeUnit) {
        Assert.notNull(timeUnit, "timeUnit");
        Assert.operation(this.variableExpiration, "Variable expiration is not enabled");
        this.expirationNanos.set(TimeUnit.NANOSECONDS.convert(duration, timeUnit));
    }

    public void setExpirationPolicy(ExpirationPolicy expirationPolicy) {
        Assert.notNull(expirationPolicy, "expirationPolicy");
        this.expirationPolicy.set(expirationPolicy);
    }

    public void setExpirationPolicy(K key, ExpirationPolicy expirationPolicy) {
        Assert.notNull(key, "key");
        Assert.notNull(expirationPolicy, "expirationPolicy");
        Assert.operation(this.variableExpiration, "Variable expiration is not enabled");
        ExpiringEntry<K, V> entry = getEntry(key);
        if (entry != null) {
            entry.expirationPolicy.set(expirationPolicy);
        }
    }

    public void setMaxSize(int maxSize) {
        Assert.operation(maxSize > 0, "maxSize");
        this.maxSize = maxSize;
    }

    @Override // java.util.Map
    public int size() {
        this.readLock.lock();
        try {
            return this.entries.size();
        } finally {
            this.readLock.unlock();
        }
    }

    public String toString() {
        this.readLock.lock();
        try {
            return this.entries.toString();
        } finally {
            this.readLock.unlock();
        }
    }

    @Override // java.util.Map
    public Collection<V> values() {
        return new AbstractCollection<V>() { // from class: net.jodah.expiringmap.ExpiringMap.3
            @Override // java.util.AbstractCollection, java.util.Collection
            public void clear() {
                ExpiringMap.this.clear();
            }

            @Override // java.util.AbstractCollection, java.util.Collection
            public boolean contains(Object value) {
                return ExpiringMap.this.containsValue(value);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
            public Iterator<V> iterator() {
                if (ExpiringMap.this.entries instanceof EntryLinkedHashMap) {
                    EntryLinkedHashMap entryLinkedHashMap = (EntryLinkedHashMap) ExpiringMap.this.entries;
                    entryLinkedHashMap.getClass();
                    return new EntryLinkedHashMap.ValueIterator();
                }
                EntryTreeHashMap entryTreeHashMap = (EntryTreeHashMap) ExpiringMap.this.entries;
                entryTreeHashMap.getClass();
                return new EntryTreeHashMap.ValueIterator();
            }

            @Override // java.util.AbstractCollection, java.util.Collection
            public int size() {
                return ExpiringMap.this.size();
            }
        };
    }

    void notifyListeners(final ExpiringEntry<K, V> entry) {
        if (this.asyncExpirationListeners != null) {
            for (final ExpirationListener<K, V> listener : this.asyncExpirationListeners) {
                LISTENER_SERVICE.execute(new Runnable() { // from class: net.jodah.expiringmap.ExpiringMap.4
                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            listener.expired(entry.key, entry.getValue());
                        } catch (Exception e) {
                        }
                    }
                });
            }
        }
        if (this.expirationListeners != null) {
            for (ExpirationListener<K, V> listener2 : this.expirationListeners) {
                try {
                    listener2.expired(entry.key, entry.getValue());
                } catch (Exception e) {
                }
            }
        }
    }

    ExpiringEntry<K, V> getEntry(Object obj) {
        this.readLock.lock();
        try {
            return (ExpiringEntry) this.entries.get(obj);
        } finally {
            this.readLock.unlock();
        }
    }

    V putInternal(K k, V v, ExpirationPolicy expirationPolicy, long j) {
        this.writeLock.lock();
        try {
            ExpiringEntry<K, V> expiringEntry = (ExpiringEntry) this.entries.get(k);
            V v2 = null;
            if (expiringEntry == null) {
                ExpiringEntry<K, V> expiringEntry2 = new ExpiringEntry<>(k, v, this.variableExpiration ? new AtomicReference<>(expirationPolicy) : this.expirationPolicy, this.variableExpiration ? new AtomicLong(j) : this.expirationNanos);
                if (this.entries.size() >= this.maxSize) {
                    ExpiringEntry<K, V> first = this.entries.first();
                    this.entries.remove(first.key);
                    notifyListeners(first);
                }
                this.entries.put(k, expiringEntry2);
                if (this.entries.size() == 1 || this.entries.first().equals(expiringEntry2)) {
                    scheduleEntry(expiringEntry2);
                }
            } else {
                v2 = expiringEntry.getValue();
                if (!ExpirationPolicy.ACCESSED.equals(expirationPolicy) && ((v2 == null && v == null) || (v2 != null && v2.equals(v)))) {
                    return v;
                }
                expiringEntry.setValue(v);
                resetEntry(expiringEntry, false);
            }
            return v2;
        } finally {
            this.writeLock.unlock();
        }
    }

    void resetEntry(ExpiringEntry<K, V> entry, boolean scheduleFirstEntry) {
        this.writeLock.lock();
        try {
            boolean scheduled = entry.cancel();
            this.entries.reorder(entry);
            if (scheduled || scheduleFirstEntry) {
                scheduleEntry(this.entries.first());
            }
        } finally {
            this.writeLock.unlock();
        }
    }

    void scheduleEntry(ExpiringEntry<K, V> entry) {
        if (entry == null || entry.scheduled) {
            return;
        }
        synchronized (entry) {
            if (entry.scheduled) {
                return;
            }
            final WeakReference<ExpiringEntry<K, V>> entryReference = new WeakReference<>(entry);
            Runnable runnable = new Runnable() { // from class: net.jodah.expiringmap.ExpiringMap.5
                @Override // java.lang.Runnable
                public void run() {
                    ExpiringEntry<K, V> entry2 = (ExpiringEntry) entryReference.get();
                    ExpiringMap.this.writeLock.lock();
                    if (entry2 != null) {
                        try {
                            if (entry2.scheduled) {
                                ExpiringMap.this.entries.remove(entry2.key);
                                ExpiringMap.this.notifyListeners(entry2);
                            }
                        } finally {
                            ExpiringMap.this.writeLock.unlock();
                        }
                    }
                    try {
                        Iterator<ExpiringEntry<K, V>> iterator = ExpiringMap.this.entries.valuesIterator();
                        boolean schedulePending = true;
                        while (iterator.hasNext() && schedulePending) {
                            ExpiringEntry<K, V> nextEntry = iterator.next();
                            if (nextEntry.expectedExpiration.get() <= System.nanoTime()) {
                                iterator.remove();
                                ExpiringMap.this.notifyListeners(nextEntry);
                            } else {
                                ExpiringMap.this.scheduleEntry(nextEntry);
                                schedulePending = false;
                            }
                        }
                    } catch (NoSuchElementException e) {
                    }
                }
            };
            Future<?> entryFuture = EXPIRER.schedule(runnable, entry.expectedExpiration.get() - System.nanoTime(), TimeUnit.NANOSECONDS);
            entry.schedule(entryFuture);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <K, V> Map.Entry<K, V> mapEntryFor(final ExpiringEntry<K, V> entry) {
        return new Map.Entry<K, V>() { // from class: net.jodah.expiringmap.ExpiringMap.6
            @Override // java.util.Map.Entry
            public K getKey() {
                return ExpiringEntry.this.key;
            }

            @Override // java.util.Map.Entry
            public V getValue() {
                return ExpiringEntry.this.value;
            }

            @Override // java.util.Map.Entry
            public V setValue(V value) {
                throw new UnsupportedOperationException();
            }
        };
    }

    private void initListenerService() {
        synchronized (ExpiringMap.class) {
            if (LISTENER_SERVICE == null) {
                LISTENER_SERVICE = (ThreadPoolExecutor) Executors.newCachedThreadPool(THREAD_FACTORY == null ? new NamedThreadFactory("ExpiringMap-Listener-%s") : THREAD_FACTORY);
            }
        }
    }
}
