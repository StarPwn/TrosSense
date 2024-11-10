package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.longs.AbstractLong2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
import it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterable;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectSortedSets;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/* loaded from: classes4.dex */
public final class Long2ObjectSortedMaps {
    public static final EmptySortedMap EMPTY_MAP = new EmptySortedMap();

    private Long2ObjectSortedMaps() {
    }

    public static Comparator<? super Map.Entry<Long, ?>> entryComparator(final LongComparator comparator) {
        return new Comparator() { // from class: it.unimi.dsi.fastutil.longs.Long2ObjectSortedMaps$$ExternalSyntheticLambda1
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int compare;
                compare = LongComparator.this.compare(((Long) ((Map.Entry) obj).getKey()).longValue(), ((Long) ((Map.Entry) obj2).getKey()).longValue());
                return compare;
            }
        };
    }

    public static <V> ObjectBidirectionalIterator<Long2ObjectMap.Entry<V>> fastIterator(Long2ObjectSortedMap<V> map) {
        ObjectSortedSet<Long2ObjectMap.Entry<V>> entries = map.long2ObjectEntrySet();
        return entries instanceof Long2ObjectSortedMap.FastSortedEntrySet ? ((Long2ObjectSortedMap.FastSortedEntrySet) entries).fastIterator() : entries.iterator();
    }

    public static <V> ObjectBidirectionalIterable<Long2ObjectMap.Entry<V>> fastIterable(Long2ObjectSortedMap<V> map) {
        ObjectSortedSet<Long2ObjectMap.Entry<V>> entries = map.long2ObjectEntrySet();
        if (!(entries instanceof Long2ObjectSortedMap.FastSortedEntrySet)) {
            return entries;
        }
        final Long2ObjectSortedMap.FastSortedEntrySet fastSortedEntrySet = (Long2ObjectSortedMap.FastSortedEntrySet) entries;
        fastSortedEntrySet.getClass();
        return new ObjectBidirectionalIterable() { // from class: it.unimi.dsi.fastutil.longs.Long2ObjectSortedMaps$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterable, it.unimi.dsi.fastutil.objects.ObjectIterable, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection
            public final ObjectBidirectionalIterator iterator() {
                return Long2ObjectSortedMap.FastSortedEntrySet.this.fastIterator();
            }
        };
    }

    /* loaded from: classes4.dex */
    public static class EmptySortedMap<V> extends Long2ObjectMaps.EmptyMap<V> implements Long2ObjectSortedMap<V>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;

        protected EmptySortedMap() {
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap, java.util.SortedMap
        /* renamed from: comparator, reason: merged with bridge method [inline-methods] */
        public Comparator<? super Long> comparator2() {
            return null;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMaps.EmptyMap, it.unimi.dsi.fastutil.longs.Long2ObjectMap, it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap
        public ObjectSortedSet<Long2ObjectMap.Entry<V>> long2ObjectEntrySet() {
            return ObjectSortedSets.EMPTY_SET;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap, java.util.Map
        @Deprecated
        public ObjectSortedSet<Map.Entry<Long, V>> entrySet() {
            return ObjectSortedSets.EMPTY_SET;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMaps.EmptyMap, it.unimi.dsi.fastutil.longs.Long2ObjectMap, java.util.Map
        public Set<Long> keySet() {
            return LongSortedSets.EMPTY_SET;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap
        public Long2ObjectSortedMap<V> subMap(long from, long to) {
            return Long2ObjectSortedMaps.EMPTY_MAP;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap
        public Long2ObjectSortedMap<V> headMap(long to) {
            return Long2ObjectSortedMaps.EMPTY_MAP;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap
        public Long2ObjectSortedMap<V> tailMap(long from) {
            return Long2ObjectSortedMaps.EMPTY_MAP;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap
        public long firstLongKey() {
            throw new NoSuchElementException();
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap
        public long lastLongKey() {
            throw new NoSuchElementException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Long2ObjectSortedMap<V> headMap(Long oto) {
            return headMap(oto.longValue());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Long2ObjectSortedMap<V> tailMap(Long ofrom) {
            return tailMap(ofrom.longValue());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Long2ObjectSortedMap<V> subMap(Long ofrom, Long oto) {
            return subMap(ofrom.longValue(), oto.longValue());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Long firstKey() {
            return Long.valueOf(firstLongKey());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Long lastKey() {
            return Long.valueOf(lastLongKey());
        }
    }

    public static <V> Long2ObjectSortedMap<V> emptyMap() {
        return EMPTY_MAP;
    }

    /* loaded from: classes4.dex */
    public static class Singleton<V> extends Long2ObjectMaps.Singleton<V> implements Long2ObjectSortedMap<V>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final LongComparator comparator;

        protected Singleton(long key, V value, LongComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }

        protected Singleton(long key, V value) {
            this(key, value, null);
        }

        final int compare(long k1, long k2) {
            return this.comparator == null ? Long.compare(k1, k2) : this.comparator.compare(k1, k2);
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap, java.util.SortedMap
        /* renamed from: comparator */
        public Comparator<? super Long> comparator2() {
            return this.comparator;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMaps.Singleton, it.unimi.dsi.fastutil.longs.Long2ObjectMap, it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap
        public ObjectSortedSet<Long2ObjectMap.Entry<V>> long2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractLong2ObjectMap.BasicEntry(this.key, this.value), Long2ObjectSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet) this.entries;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMaps.Singleton, it.unimi.dsi.fastutil.longs.Long2ObjectMap, java.util.Map
        @Deprecated
        public ObjectSortedSet<Map.Entry<Long, V>> entrySet() {
            return long2ObjectEntrySet();
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMaps.Singleton, it.unimi.dsi.fastutil.longs.Long2ObjectMap, java.util.Map
        public Set<Long> keySet() {
            if (this.keys == null) {
                this.keys = LongSortedSets.singleton(this.key, this.comparator);
            }
            return (LongSortedSet) this.keys;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap
        public Long2ObjectSortedMap<V> subMap(long from, long to) {
            return (compare(from, this.key) > 0 || compare(this.key, to) >= 0) ? Long2ObjectSortedMaps.EMPTY_MAP : this;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap
        public Long2ObjectSortedMap<V> headMap(long to) {
            return compare(this.key, to) < 0 ? this : Long2ObjectSortedMaps.EMPTY_MAP;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap
        public Long2ObjectSortedMap<V> tailMap(long from) {
            return compare(from, this.key) <= 0 ? this : Long2ObjectSortedMaps.EMPTY_MAP;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap
        public long firstLongKey() {
            return this.key;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap
        public long lastLongKey() {
            return this.key;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Long2ObjectSortedMap<V> headMap(Long oto) {
            return headMap(oto.longValue());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Long2ObjectSortedMap<V> tailMap(Long ofrom) {
            return tailMap(ofrom.longValue());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Long2ObjectSortedMap<V> subMap(Long ofrom, Long oto) {
            return subMap(ofrom.longValue(), oto.longValue());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Long firstKey() {
            return Long.valueOf(firstLongKey());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Long lastKey() {
            return Long.valueOf(lastLongKey());
        }
    }

    public static <V> Long2ObjectSortedMap<V> singleton(Long key, V value) {
        return new Singleton(key.longValue(), value);
    }

    public static <V> Long2ObjectSortedMap<V> singleton(Long key, V value, LongComparator comparator) {
        return new Singleton(key.longValue(), value, comparator);
    }

    public static <V> Long2ObjectSortedMap<V> singleton(long key, V value) {
        return new Singleton(key, value);
    }

    public static <V> Long2ObjectSortedMap<V> singleton(long key, V value, LongComparator comparator) {
        return new Singleton(key, value, comparator);
    }

    /* loaded from: classes4.dex */
    public static class SynchronizedSortedMap<V> extends Long2ObjectMaps.SynchronizedMap<V> implements Long2ObjectSortedMap<V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Long2ObjectSortedMap<V> sortedMap;

        protected SynchronizedSortedMap(Long2ObjectSortedMap<V> m, Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }

        protected SynchronizedSortedMap(Long2ObjectSortedMap<V> m) {
            super(m);
            this.sortedMap = m;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap, java.util.SortedMap
        /* renamed from: comparator */
        public Comparator<? super Long> comparator2() {
            Comparator<? super Long> comparator2;
            synchronized (this.sync) {
                comparator2 = this.sortedMap.comparator2();
            }
            return comparator2;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMaps.SynchronizedMap, it.unimi.dsi.fastutil.longs.Long2ObjectMap, it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap
        public ObjectSortedSet<Long2ObjectMap.Entry<V>> long2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.synchronize(this.sortedMap.long2ObjectEntrySet(), this.sync);
            }
            return (ObjectSortedSet) this.entries;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMaps.SynchronizedMap, it.unimi.dsi.fastutil.longs.Long2ObjectMap, java.util.Map
        @Deprecated
        public ObjectSortedSet<Map.Entry<Long, V>> entrySet() {
            return long2ObjectEntrySet();
        }

        /* JADX WARN: Type inference failed for: r0v4, types: [it.unimi.dsi.fastutil.longs.LongSortedSet] */
        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMaps.SynchronizedMap, it.unimi.dsi.fastutil.longs.Long2ObjectMap, java.util.Map
        public Set<Long> keySet() {
            if (this.keys == null) {
                this.keys = LongSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (LongSortedSet) this.keys;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap
        public Long2ObjectSortedMap<V> subMap(long from, long to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap
        public Long2ObjectSortedMap<V> headMap(long to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap
        public Long2ObjectSortedMap<V> tailMap(long from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap
        public long firstLongKey() {
            long firstLongKey;
            synchronized (this.sync) {
                firstLongKey = this.sortedMap.firstLongKey();
            }
            return firstLongKey;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap
        public long lastLongKey() {
            long lastLongKey;
            synchronized (this.sync) {
                lastLongKey = this.sortedMap.lastLongKey();
            }
            return lastLongKey;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Long firstKey() {
            Long firstKey;
            synchronized (this.sync) {
                firstKey = this.sortedMap.firstKey();
            }
            return firstKey;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Long lastKey() {
            Long lastKey;
            synchronized (this.sync) {
                lastKey = this.sortedMap.lastKey();
            }
            return lastKey;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Long2ObjectSortedMap<V> subMap(Long from, Long to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Long2ObjectSortedMap<V> headMap(Long to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Long2ObjectSortedMap<V> tailMap(Long from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }
    }

    public static <V> Long2ObjectSortedMap<V> synchronize(Long2ObjectSortedMap<V> m) {
        return new SynchronizedSortedMap(m);
    }

    public static <V> Long2ObjectSortedMap<V> synchronize(Long2ObjectSortedMap<V> m, Object sync) {
        return new SynchronizedSortedMap(m, sync);
    }

    /* loaded from: classes4.dex */
    public static class UnmodifiableSortedMap<V> extends Long2ObjectMaps.UnmodifiableMap<V> implements Long2ObjectSortedMap<V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Long2ObjectSortedMap<? extends V> sortedMap;

        protected UnmodifiableSortedMap(Long2ObjectSortedMap<? extends V> m) {
            super(m);
            this.sortedMap = m;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap, java.util.SortedMap
        /* renamed from: comparator */
        public Comparator<? super Long> comparator2() {
            return this.sortedMap.comparator2();
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMaps.UnmodifiableMap, it.unimi.dsi.fastutil.longs.Long2ObjectMap, it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap
        public ObjectSortedSet<Long2ObjectMap.Entry<V>> long2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.unmodifiable(this.sortedMap.long2ObjectEntrySet());
            }
            return (ObjectSortedSet) this.entries;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMaps.UnmodifiableMap, it.unimi.dsi.fastutil.longs.Long2ObjectMap, java.util.Map
        @Deprecated
        public ObjectSortedSet<Map.Entry<Long, V>> entrySet() {
            return long2ObjectEntrySet();
        }

        /* JADX WARN: Type inference failed for: r0v4, types: [it.unimi.dsi.fastutil.longs.LongSortedSet] */
        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMaps.UnmodifiableMap, it.unimi.dsi.fastutil.longs.Long2ObjectMap, java.util.Map
        public Set<Long> keySet() {
            if (this.keys == null) {
                this.keys = LongSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (LongSortedSet) this.keys;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap
        public Long2ObjectSortedMap<V> subMap(long from, long to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap
        public Long2ObjectSortedMap<V> headMap(long to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap
        public Long2ObjectSortedMap<V> tailMap(long from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap
        public long firstLongKey() {
            return this.sortedMap.firstLongKey();
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap
        public long lastLongKey() {
            return this.sortedMap.lastLongKey();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Long firstKey() {
            return this.sortedMap.firstKey();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Long lastKey() {
            return this.sortedMap.lastKey();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Long2ObjectSortedMap<V> subMap(Long from, Long to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Long2ObjectSortedMap<V> headMap(Long to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Long2ObjectSortedMap<V> tailMap(Long from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }
    }

    public static <V> Long2ObjectSortedMap<V> unmodifiable(Long2ObjectSortedMap<? extends V> m) {
        return new UnmodifiableSortedMap(m);
    }
}
