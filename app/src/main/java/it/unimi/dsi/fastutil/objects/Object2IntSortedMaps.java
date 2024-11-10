package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.objects.AbstractObject2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntSortedMap;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.SortedMap;

/* loaded from: classes4.dex */
public final class Object2IntSortedMaps {
    public static final EmptySortedMap EMPTY_MAP = new EmptySortedMap();

    private Object2IntSortedMaps() {
    }

    public static <K> Comparator<? super Map.Entry<K, ?>> entryComparator(final Comparator<? super K> comparator) {
        return new Comparator() { // from class: it.unimi.dsi.fastutil.objects.Object2IntSortedMaps$$ExternalSyntheticLambda1
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int compare;
                compare = comparator.compare(((Map.Entry) obj).getKey(), ((Map.Entry) obj2).getKey());
                return compare;
            }
        };
    }

    public static <K> ObjectBidirectionalIterator<Object2IntMap.Entry<K>> fastIterator(Object2IntSortedMap<K> map) {
        ObjectSortedSet<Object2IntMap.Entry<K>> entries = map.object2IntEntrySet();
        return entries instanceof Object2IntSortedMap.FastSortedEntrySet ? ((Object2IntSortedMap.FastSortedEntrySet) entries).fastIterator() : entries.iterator();
    }

    public static <K> ObjectBidirectionalIterable<Object2IntMap.Entry<K>> fastIterable(Object2IntSortedMap<K> map) {
        ObjectSortedSet<Object2IntMap.Entry<K>> entries = map.object2IntEntrySet();
        if (!(entries instanceof Object2IntSortedMap.FastSortedEntrySet)) {
            return entries;
        }
        final Object2IntSortedMap.FastSortedEntrySet fastSortedEntrySet = (Object2IntSortedMap.FastSortedEntrySet) entries;
        fastSortedEntrySet.getClass();
        return new ObjectBidirectionalIterable() { // from class: it.unimi.dsi.fastutil.objects.Object2IntSortedMaps$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterable, it.unimi.dsi.fastutil.objects.ObjectIterable, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection
            public final ObjectBidirectionalIterator iterator() {
                return Object2IntSortedMap.FastSortedEntrySet.this.fastIterator();
            }
        };
    }

    /* loaded from: classes4.dex */
    public static class EmptySortedMap<K> extends Object2IntMaps.EmptyMap<K> implements Object2IntSortedMap<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntSortedMap, java.util.SortedMap
        public /* bridge */ /* synthetic */ SortedMap headMap(Object obj) {
            return headMap((EmptySortedMap<K>) obj);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntSortedMap, java.util.SortedMap
        public /* bridge */ /* synthetic */ SortedMap tailMap(Object obj) {
            return tailMap((EmptySortedMap<K>) obj);
        }

        protected EmptySortedMap() {
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntSortedMap, java.util.SortedMap
        public Comparator<? super K> comparator() {
            return null;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMaps.EmptyMap, it.unimi.dsi.fastutil.objects.Object2IntMap, it.unimi.dsi.fastutil.objects.Object2IntSortedMap
        public ObjectSortedSet<Object2IntMap.Entry<K>> object2IntEntrySet() {
            return ObjectSortedSets.EMPTY_SET;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
        @Deprecated
        public ObjectSortedSet<Map.Entry<K, Integer>> entrySet() {
            return ObjectSortedSets.EMPTY_SET;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMaps.EmptyMap, it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
        public ObjectSortedSet<K> keySet() {
            return ObjectSortedSets.EMPTY_SET;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntSortedMap, java.util.SortedMap
        public Object2IntSortedMap<K> subMap(K from, K to) {
            return Object2IntSortedMaps.EMPTY_MAP;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntSortedMap, java.util.SortedMap
        public Object2IntSortedMap<K> headMap(K to) {
            return Object2IntSortedMaps.EMPTY_MAP;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntSortedMap, java.util.SortedMap
        public Object2IntSortedMap<K> tailMap(K from) {
            return Object2IntSortedMaps.EMPTY_MAP;
        }

        @Override // java.util.SortedMap
        public K firstKey() {
            throw new NoSuchElementException();
        }

        @Override // java.util.SortedMap
        public K lastKey() {
            throw new NoSuchElementException();
        }
    }

    public static <K> Object2IntSortedMap<K> emptyMap() {
        return EMPTY_MAP;
    }

    /* loaded from: classes4.dex */
    public static class Singleton<K> extends Object2IntMaps.Singleton<K> implements Object2IntSortedMap<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Comparator<? super K> comparator;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntSortedMap, java.util.SortedMap
        public /* bridge */ /* synthetic */ SortedMap headMap(Object obj) {
            return headMap((Singleton<K>) obj);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntSortedMap, java.util.SortedMap
        public /* bridge */ /* synthetic */ SortedMap tailMap(Object obj) {
            return tailMap((Singleton<K>) obj);
        }

        protected Singleton(K key, int value, Comparator<? super K> comparator) {
            super(key, value);
            this.comparator = comparator;
        }

        protected Singleton(K key, int value) {
            this(key, value, null);
        }

        final int compare(K k1, K k2) {
            return this.comparator == null ? ((Comparable) k1).compareTo(k2) : this.comparator.compare(k1, k2);
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntSortedMap, java.util.SortedMap
        public Comparator<? super K> comparator() {
            return this.comparator;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMaps.Singleton, it.unimi.dsi.fastutil.objects.Object2IntMap, it.unimi.dsi.fastutil.objects.Object2IntSortedMap
        public ObjectSortedSet<Object2IntMap.Entry<K>> object2IntEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractObject2IntMap.BasicEntry(this.key, this.value), Object2IntSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet) this.entries;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMaps.Singleton, it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
        @Deprecated
        public ObjectSortedSet<Map.Entry<K, Integer>> entrySet() {
            return object2IntEntrySet();
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMaps.Singleton, it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
        public ObjectSortedSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ObjectSortedSets.singleton(this.key, this.comparator);
            }
            return (ObjectSortedSet) this.keys;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntSortedMap, java.util.SortedMap
        public Object2IntSortedMap<K> subMap(K from, K to) {
            return (compare(from, this.key) > 0 || compare(this.key, to) >= 0) ? Object2IntSortedMaps.EMPTY_MAP : this;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntSortedMap, java.util.SortedMap
        public Object2IntSortedMap<K> headMap(K to) {
            return compare(this.key, to) < 0 ? this : Object2IntSortedMaps.EMPTY_MAP;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntSortedMap, java.util.SortedMap
        public Object2IntSortedMap<K> tailMap(K from) {
            return compare(from, this.key) <= 0 ? this : Object2IntSortedMaps.EMPTY_MAP;
        }

        @Override // java.util.SortedMap
        public K firstKey() {
            return this.key;
        }

        @Override // java.util.SortedMap
        public K lastKey() {
            return this.key;
        }
    }

    public static <K> Object2IntSortedMap<K> singleton(K key, Integer value) {
        return new Singleton(key, value.intValue());
    }

    public static <K> Object2IntSortedMap<K> singleton(K key, Integer value, Comparator<? super K> comparator) {
        return new Singleton(key, value.intValue(), comparator);
    }

    public static <K> Object2IntSortedMap<K> singleton(K key, int value) {
        return new Singleton(key, value);
    }

    public static <K> Object2IntSortedMap<K> singleton(K key, int value, Comparator<? super K> comparator) {
        return new Singleton(key, value, comparator);
    }

    /* loaded from: classes4.dex */
    public static class SynchronizedSortedMap<K> extends Object2IntMaps.SynchronizedMap<K> implements Object2IntSortedMap<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2IntSortedMap<K> sortedMap;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntSortedMap, java.util.SortedMap
        public /* bridge */ /* synthetic */ SortedMap headMap(Object obj) {
            return headMap((SynchronizedSortedMap<K>) obj);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntSortedMap, java.util.SortedMap
        public /* bridge */ /* synthetic */ SortedMap tailMap(Object obj) {
            return tailMap((SynchronizedSortedMap<K>) obj);
        }

        protected SynchronizedSortedMap(Object2IntSortedMap<K> m, Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }

        protected SynchronizedSortedMap(Object2IntSortedMap<K> m) {
            super(m);
            this.sortedMap = m;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntSortedMap, java.util.SortedMap
        public Comparator<? super K> comparator() {
            Comparator<? super K> comparator;
            synchronized (this.sync) {
                comparator = this.sortedMap.comparator();
            }
            return comparator;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMaps.SynchronizedMap, it.unimi.dsi.fastutil.objects.Object2IntMap, it.unimi.dsi.fastutil.objects.Object2IntSortedMap
        public ObjectSortedSet<Object2IntMap.Entry<K>> object2IntEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.synchronize(this.sortedMap.object2IntEntrySet(), this.sync);
            }
            return (ObjectSortedSet) this.entries;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMaps.SynchronizedMap, it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
        @Deprecated
        public ObjectSortedSet<Map.Entry<K, Integer>> entrySet() {
            return object2IntEntrySet();
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMaps.SynchronizedMap, it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
        public ObjectSortedSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ObjectSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (ObjectSortedSet) this.keys;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntSortedMap, java.util.SortedMap
        public Object2IntSortedMap<K> subMap(K from, K to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap((Object) from, (Object) to), this.sync);
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntSortedMap, java.util.SortedMap
        public Object2IntSortedMap<K> headMap(K to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap((Object2IntSortedMap<K>) to), this.sync);
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntSortedMap, java.util.SortedMap
        public Object2IntSortedMap<K> tailMap(K from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap((Object2IntSortedMap<K>) from), this.sync);
        }

        @Override // java.util.SortedMap
        public K firstKey() {
            K firstKey;
            synchronized (this.sync) {
                firstKey = this.sortedMap.firstKey();
            }
            return firstKey;
        }

        @Override // java.util.SortedMap
        public K lastKey() {
            K lastKey;
            synchronized (this.sync) {
                lastKey = this.sortedMap.lastKey();
            }
            return lastKey;
        }
    }

    public static <K> Object2IntSortedMap<K> synchronize(Object2IntSortedMap<K> m) {
        return new SynchronizedSortedMap(m);
    }

    public static <K> Object2IntSortedMap<K> synchronize(Object2IntSortedMap<K> m, Object sync) {
        return new SynchronizedSortedMap(m, sync);
    }

    /* loaded from: classes4.dex */
    public static class UnmodifiableSortedMap<K> extends Object2IntMaps.UnmodifiableMap<K> implements Object2IntSortedMap<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2IntSortedMap<K> sortedMap;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntSortedMap, java.util.SortedMap
        public /* bridge */ /* synthetic */ SortedMap headMap(Object obj) {
            return headMap((UnmodifiableSortedMap<K>) obj);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntSortedMap, java.util.SortedMap
        public /* bridge */ /* synthetic */ SortedMap tailMap(Object obj) {
            return tailMap((UnmodifiableSortedMap<K>) obj);
        }

        protected UnmodifiableSortedMap(Object2IntSortedMap<K> m) {
            super(m);
            this.sortedMap = m;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntSortedMap, java.util.SortedMap
        public Comparator<? super K> comparator() {
            return this.sortedMap.comparator();
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMaps.UnmodifiableMap, it.unimi.dsi.fastutil.objects.Object2IntMap, it.unimi.dsi.fastutil.objects.Object2IntSortedMap
        public ObjectSortedSet<Object2IntMap.Entry<K>> object2IntEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.unmodifiable(this.sortedMap.object2IntEntrySet());
            }
            return (ObjectSortedSet) this.entries;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMaps.UnmodifiableMap, it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
        @Deprecated
        public ObjectSortedSet<Map.Entry<K, Integer>> entrySet() {
            return object2IntEntrySet();
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMaps.UnmodifiableMap, it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
        public ObjectSortedSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ObjectSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (ObjectSortedSet) this.keys;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntSortedMap, java.util.SortedMap
        public Object2IntSortedMap<K> subMap(K from, K to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap((Object) from, (Object) to));
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntSortedMap, java.util.SortedMap
        public Object2IntSortedMap<K> headMap(K to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap((Object2IntSortedMap<K>) to));
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntSortedMap, java.util.SortedMap
        public Object2IntSortedMap<K> tailMap(K from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap((Object2IntSortedMap<K>) from));
        }

        @Override // java.util.SortedMap
        public K firstKey() {
            return this.sortedMap.firstKey();
        }

        @Override // java.util.SortedMap
        public K lastKey() {
            return this.sortedMap.lastKey();
        }
    }

    public static <K> Object2IntSortedMap<K> unmodifiable(Object2IntSortedMap<K> m) {
        return new UnmodifiableSortedMap(m);
    }
}
