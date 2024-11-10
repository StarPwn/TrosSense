package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
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
public final class Int2ObjectSortedMaps {
    public static final EmptySortedMap EMPTY_MAP = new EmptySortedMap();

    private Int2ObjectSortedMaps() {
    }

    public static Comparator<? super Map.Entry<Integer, ?>> entryComparator(final IntComparator comparator) {
        return new Comparator() { // from class: it.unimi.dsi.fastutil.ints.Int2ObjectSortedMaps$$ExternalSyntheticLambda1
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int compare;
                compare = IntComparator.this.compare(((Integer) ((Map.Entry) obj).getKey()).intValue(), ((Integer) ((Map.Entry) obj2).getKey()).intValue());
                return compare;
            }
        };
    }

    public static <V> ObjectBidirectionalIterator<Int2ObjectMap.Entry<V>> fastIterator(Int2ObjectSortedMap<V> map) {
        ObjectSortedSet<Int2ObjectMap.Entry<V>> entries = map.int2ObjectEntrySet();
        return entries instanceof Int2ObjectSortedMap.FastSortedEntrySet ? ((Int2ObjectSortedMap.FastSortedEntrySet) entries).fastIterator() : entries.iterator();
    }

    public static <V> ObjectBidirectionalIterable<Int2ObjectMap.Entry<V>> fastIterable(Int2ObjectSortedMap<V> map) {
        ObjectSortedSet<Int2ObjectMap.Entry<V>> entries = map.int2ObjectEntrySet();
        if (!(entries instanceof Int2ObjectSortedMap.FastSortedEntrySet)) {
            return entries;
        }
        final Int2ObjectSortedMap.FastSortedEntrySet fastSortedEntrySet = (Int2ObjectSortedMap.FastSortedEntrySet) entries;
        fastSortedEntrySet.getClass();
        return new ObjectBidirectionalIterable() { // from class: it.unimi.dsi.fastutil.ints.Int2ObjectSortedMaps$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterable, it.unimi.dsi.fastutil.objects.ObjectIterable, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection
            public final ObjectBidirectionalIterator iterator() {
                return Int2ObjectSortedMap.FastSortedEntrySet.this.fastIterator();
            }
        };
    }

    /* loaded from: classes4.dex */
    public static class EmptySortedMap<V> extends Int2ObjectMaps.EmptyMap<V> implements Int2ObjectSortedMap<V>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;

        protected EmptySortedMap() {
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap, java.util.SortedMap
        /* renamed from: comparator, reason: merged with bridge method [inline-methods] */
        public Comparator<? super Integer> comparator2() {
            return null;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMaps.EmptyMap, it.unimi.dsi.fastutil.ints.Int2ObjectMap, it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public ObjectSortedSet<Int2ObjectMap.Entry<V>> int2ObjectEntrySet() {
            return ObjectSortedSets.EMPTY_SET;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        @Deprecated
        public ObjectSortedSet<Map.Entry<Integer, V>> entrySet() {
            return ObjectSortedSets.EMPTY_SET;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMaps.EmptyMap, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        /* renamed from: keySet, reason: avoid collision after fix types in other method */
        public Set<Integer> keySet2() {
            return IntSortedSets.EMPTY_SET;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public Int2ObjectSortedMap<V> subMap(int from, int to) {
            return Int2ObjectSortedMaps.EMPTY_MAP;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public Int2ObjectSortedMap<V> headMap(int to) {
            return Int2ObjectSortedMaps.EMPTY_MAP;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public Int2ObjectSortedMap<V> tailMap(int from) {
            return Int2ObjectSortedMaps.EMPTY_MAP;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public int firstIntKey() {
            throw new NoSuchElementException();
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public int lastIntKey() {
            throw new NoSuchElementException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Int2ObjectSortedMap<V> headMap(Integer oto) {
            return headMap(oto.intValue());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Int2ObjectSortedMap<V> tailMap(Integer ofrom) {
            return tailMap(ofrom.intValue());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Int2ObjectSortedMap<V> subMap(Integer ofrom, Integer oto) {
            return subMap(ofrom.intValue(), oto.intValue());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Integer firstKey() {
            return Integer.valueOf(firstIntKey());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Integer lastKey() {
            return Integer.valueOf(lastIntKey());
        }
    }

    public static <V> Int2ObjectSortedMap<V> emptyMap() {
        return EMPTY_MAP;
    }

    /* loaded from: classes4.dex */
    public static class Singleton<V> extends Int2ObjectMaps.Singleton<V> implements Int2ObjectSortedMap<V>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final IntComparator comparator;

        protected Singleton(int key, V value, IntComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }

        protected Singleton(int key, V value) {
            this(key, value, null);
        }

        final int compare(int k1, int k2) {
            return this.comparator == null ? Integer.compare(k1, k2) : this.comparator.compare(k1, k2);
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap, java.util.SortedMap
        /* renamed from: comparator */
        public Comparator<? super Integer> comparator2() {
            return this.comparator;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMaps.Singleton, it.unimi.dsi.fastutil.ints.Int2ObjectMap, it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public ObjectSortedSet<Int2ObjectMap.Entry<V>> int2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractInt2ObjectMap.BasicEntry(this.key, this.value), Int2ObjectSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet) this.entries;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMaps.Singleton, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        @Deprecated
        public ObjectSortedSet<Map.Entry<Integer, V>> entrySet() {
            return int2ObjectEntrySet();
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMaps.Singleton, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        /* renamed from: keySet, reason: avoid collision after fix types in other method */
        public Set<Integer> keySet2() {
            if (this.keys == null) {
                this.keys = IntSortedSets.singleton(this.key, this.comparator);
            }
            return (IntSortedSet) this.keys;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public Int2ObjectSortedMap<V> subMap(int from, int to) {
            return (compare(from, this.key) > 0 || compare(this.key, to) >= 0) ? Int2ObjectSortedMaps.EMPTY_MAP : this;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public Int2ObjectSortedMap<V> headMap(int to) {
            return compare(this.key, to) < 0 ? this : Int2ObjectSortedMaps.EMPTY_MAP;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public Int2ObjectSortedMap<V> tailMap(int from) {
            return compare(from, this.key) <= 0 ? this : Int2ObjectSortedMaps.EMPTY_MAP;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public int firstIntKey() {
            return this.key;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public int lastIntKey() {
            return this.key;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Int2ObjectSortedMap<V> headMap(Integer oto) {
            return headMap(oto.intValue());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Int2ObjectSortedMap<V> tailMap(Integer ofrom) {
            return tailMap(ofrom.intValue());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Int2ObjectSortedMap<V> subMap(Integer ofrom, Integer oto) {
            return subMap(ofrom.intValue(), oto.intValue());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Integer firstKey() {
            return Integer.valueOf(firstIntKey());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Integer lastKey() {
            return Integer.valueOf(lastIntKey());
        }
    }

    public static <V> Int2ObjectSortedMap<V> singleton(Integer key, V value) {
        return new Singleton(key.intValue(), value);
    }

    public static <V> Int2ObjectSortedMap<V> singleton(Integer key, V value, IntComparator comparator) {
        return new Singleton(key.intValue(), value, comparator);
    }

    public static <V> Int2ObjectSortedMap<V> singleton(int key, V value) {
        return new Singleton(key, value);
    }

    public static <V> Int2ObjectSortedMap<V> singleton(int key, V value, IntComparator comparator) {
        return new Singleton(key, value, comparator);
    }

    /* loaded from: classes4.dex */
    public static class SynchronizedSortedMap<V> extends Int2ObjectMaps.SynchronizedMap<V> implements Int2ObjectSortedMap<V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Int2ObjectSortedMap<V> sortedMap;

        protected SynchronizedSortedMap(Int2ObjectSortedMap<V> m, Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }

        protected SynchronizedSortedMap(Int2ObjectSortedMap<V> m) {
            super(m);
            this.sortedMap = m;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap, java.util.SortedMap
        /* renamed from: comparator */
        public Comparator<? super Integer> comparator2() {
            Comparator<? super Integer> comparator2;
            synchronized (this.sync) {
                comparator2 = this.sortedMap.comparator2();
            }
            return comparator2;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMaps.SynchronizedMap, it.unimi.dsi.fastutil.ints.Int2ObjectMap, it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public ObjectSortedSet<Int2ObjectMap.Entry<V>> int2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.synchronize(this.sortedMap.int2ObjectEntrySet(), this.sync);
            }
            return (ObjectSortedSet) this.entries;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMaps.SynchronizedMap, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        @Deprecated
        public ObjectSortedSet<Map.Entry<Integer, V>> entrySet() {
            return int2ObjectEntrySet();
        }

        /* JADX WARN: Type inference failed for: r0v4, types: [it.unimi.dsi.fastutil.ints.IntSortedSet] */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMaps.SynchronizedMap, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        /* renamed from: keySet, reason: avoid collision after fix types in other method */
        public Set<Integer> keySet2() {
            if (this.keys == null) {
                this.keys = IntSortedSets.synchronize(this.sortedMap.keySet2(), this.sync);
            }
            return (IntSortedSet) this.keys;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public Int2ObjectSortedMap<V> subMap(int from, int to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public Int2ObjectSortedMap<V> headMap(int to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public Int2ObjectSortedMap<V> tailMap(int from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public int firstIntKey() {
            int firstIntKey;
            synchronized (this.sync) {
                firstIntKey = this.sortedMap.firstIntKey();
            }
            return firstIntKey;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public int lastIntKey() {
            int lastIntKey;
            synchronized (this.sync) {
                lastIntKey = this.sortedMap.lastIntKey();
            }
            return lastIntKey;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Integer firstKey() {
            Integer firstKey;
            synchronized (this.sync) {
                firstKey = this.sortedMap.firstKey();
            }
            return firstKey;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Integer lastKey() {
            Integer lastKey;
            synchronized (this.sync) {
                lastKey = this.sortedMap.lastKey();
            }
            return lastKey;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Int2ObjectSortedMap<V> subMap(Integer from, Integer to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Int2ObjectSortedMap<V> headMap(Integer to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Int2ObjectSortedMap<V> tailMap(Integer from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }
    }

    public static <V> Int2ObjectSortedMap<V> synchronize(Int2ObjectSortedMap<V> m) {
        return new SynchronizedSortedMap(m);
    }

    public static <V> Int2ObjectSortedMap<V> synchronize(Int2ObjectSortedMap<V> m, Object sync) {
        return new SynchronizedSortedMap(m, sync);
    }

    /* loaded from: classes4.dex */
    public static class UnmodifiableSortedMap<V> extends Int2ObjectMaps.UnmodifiableMap<V> implements Int2ObjectSortedMap<V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Int2ObjectSortedMap<? extends V> sortedMap;

        protected UnmodifiableSortedMap(Int2ObjectSortedMap<? extends V> m) {
            super(m);
            this.sortedMap = m;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap, java.util.SortedMap
        /* renamed from: comparator */
        public Comparator<? super Integer> comparator2() {
            return this.sortedMap.comparator2();
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMaps.UnmodifiableMap, it.unimi.dsi.fastutil.ints.Int2ObjectMap, it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public ObjectSortedSet<Int2ObjectMap.Entry<V>> int2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.unmodifiable(this.sortedMap.int2ObjectEntrySet());
            }
            return (ObjectSortedSet) this.entries;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMaps.UnmodifiableMap, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        @Deprecated
        public ObjectSortedSet<Map.Entry<Integer, V>> entrySet() {
            return int2ObjectEntrySet();
        }

        /* JADX WARN: Type inference failed for: r0v4, types: [it.unimi.dsi.fastutil.ints.IntSortedSet] */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMaps.UnmodifiableMap, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        /* renamed from: keySet, reason: avoid collision after fix types in other method */
        public Set<Integer> keySet2() {
            if (this.keys == null) {
                this.keys = IntSortedSets.unmodifiable(this.sortedMap.keySet2());
            }
            return (IntSortedSet) this.keys;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public Int2ObjectSortedMap<V> subMap(int from, int to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public Int2ObjectSortedMap<V> headMap(int to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public Int2ObjectSortedMap<V> tailMap(int from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public int firstIntKey() {
            return this.sortedMap.firstIntKey();
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public int lastIntKey() {
            return this.sortedMap.lastIntKey();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Integer firstKey() {
            return this.sortedMap.firstKey();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Integer lastKey() {
            return this.sortedMap.lastKey();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Int2ObjectSortedMap<V> subMap(Integer from, Integer to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Int2ObjectSortedMap<V> headMap(Integer to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap, java.util.SortedMap
        @Deprecated
        public Int2ObjectSortedMap<V> tailMap(Integer from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }
    }

    public static <V> Int2ObjectSortedMap<V> unmodifiable(Int2ObjectSortedMap<? extends V> m) {
        return new UnmodifiableSortedMap(m);
    }
}
