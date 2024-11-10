package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.longs.AbstractLong2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectFunctions;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectCollections;
import it.unimi.dsi.fastutil.objects.ObjectIterable;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSets;
import it.unimi.dsi.fastutil.objects.ObjectSpliterator;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.LongFunction;

/* loaded from: classes4.dex */
public final class Long2ObjectMaps {
    public static final EmptyMap EMPTY_MAP = new EmptyMap();

    private Long2ObjectMaps() {
    }

    public static <V> ObjectIterator<Long2ObjectMap.Entry<V>> fastIterator(Long2ObjectMap<V> map) {
        ObjectSet<Long2ObjectMap.Entry<V>> entries = map.long2ObjectEntrySet();
        return entries instanceof Long2ObjectMap.FastEntrySet ? ((Long2ObjectMap.FastEntrySet) entries).fastIterator() : entries.iterator();
    }

    public static <V> void fastForEach(Long2ObjectMap<V> map, Consumer<? super Long2ObjectMap.Entry<V>> consumer) {
        ObjectSet<Long2ObjectMap.Entry<V>> entries = map.long2ObjectEntrySet();
        if (!(entries instanceof Long2ObjectMap.FastEntrySet)) {
            entries.forEach(consumer);
        } else {
            ((Long2ObjectMap.FastEntrySet) entries).fastForEach(consumer);
        }
    }

    public static <V> ObjectIterable<Long2ObjectMap.Entry<V>> fastIterable(Long2ObjectMap<V> map) {
        final ObjectSet<Long2ObjectMap.Entry<V>> entries = map.long2ObjectEntrySet();
        return entries instanceof Long2ObjectMap.FastEntrySet ? new ObjectIterable<Long2ObjectMap.Entry<V>>() { // from class: it.unimi.dsi.fastutil.longs.Long2ObjectMaps.1
            @Override // it.unimi.dsi.fastutil.objects.ObjectIterable, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection
            public ObjectIterator<Long2ObjectMap.Entry<V>> iterator() {
                return ((Long2ObjectMap.FastEntrySet) ObjectSet.this).fastIterator();
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectIterable, it.unimi.dsi.fastutil.objects.ObjectCollection
            /* renamed from: spliterator */
            public ObjectSpliterator<Long2ObjectMap.Entry<V>> mo221spliterator() {
                return ObjectSet.this.mo221spliterator();
            }

            @Override // java.lang.Iterable
            public void forEach(Consumer<? super Long2ObjectMap.Entry<V>> consumer) {
                ((Long2ObjectMap.FastEntrySet) ObjectSet.this).fastForEach(consumer);
            }
        } : entries;
    }

    /* loaded from: classes4.dex */
    public static class EmptyMap<V> extends Long2ObjectFunctions.EmptyFunction<V> implements Long2ObjectMap<V>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;

        @Override // java.util.Map
        public boolean containsValue(Object v) {
            return false;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunction, it.unimi.dsi.fastutil.Function
        @Deprecated
        public V getOrDefault(Object key, V defaultValue) {
            return defaultValue;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunctions.EmptyFunction, it.unimi.dsi.fastutil.longs.Long2ObjectFunction
        public V getOrDefault(long key, V defaultValue) {
            return defaultValue;
        }

        @Override // java.util.Map
        public void putAll(Map<? extends Long, ? extends V> m) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap, it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap
        public ObjectSet<Long2ObjectMap.Entry<V>> long2ObjectEntrySet() {
            return ObjectSets.EMPTY_SET;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap, java.util.Map
        /* renamed from: keySet */
        public Set<Long> keySet2() {
            return LongSets.EMPTY_SET;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap, java.util.Map
        public ObjectCollection<V> values() {
            return ObjectSets.EMPTY_SET;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap, java.util.Map
        public void forEach(BiConsumer<? super Long, ? super V> consumer) {
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunctions.EmptyFunction
        public Object clone() {
            return Long2ObjectMaps.EMPTY_MAP;
        }

        @Override // java.util.Map
        public boolean isEmpty() {
            return true;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunctions.EmptyFunction, java.util.Map
        public int hashCode() {
            return 0;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunctions.EmptyFunction, java.util.Map
        public boolean equals(Object o) {
            if (o instanceof Map) {
                return ((Map) o).isEmpty();
            }
            return false;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunctions.EmptyFunction
        public String toString() {
            return "{}";
        }
    }

    public static <V> Long2ObjectMap<V> emptyMap() {
        return EMPTY_MAP;
    }

    /* loaded from: classes4.dex */
    public static class Singleton<V> extends Long2ObjectFunctions.Singleton<V> implements Long2ObjectMap<V>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Long2ObjectMap.Entry<V>> entries;
        protected transient LongSet keys;
        protected transient ObjectCollection<V> values;

        /* JADX INFO: Access modifiers changed from: protected */
        public Singleton(long key, V value) {
            super(key, value);
        }

        @Override // java.util.Map
        public boolean containsValue(Object v) {
            return Objects.equals(this.value, v);
        }

        @Override // java.util.Map
        public void putAll(Map<? extends Long, ? extends V> m) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap, it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap
        public ObjectSet<Long2ObjectMap.Entry<V>> long2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.singleton(new AbstractLong2ObjectMap.BasicEntry(this.key, this.value));
            }
            return this.entries;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap, java.util.Map
        @Deprecated
        public ObjectSet<Map.Entry<Long, V>> entrySet() {
            return long2ObjectEntrySet();
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap, java.util.Map
        /* renamed from: keySet */
        public Set<Long> keySet2() {
            if (this.keys == null) {
                this.keys = LongSets.singleton(this.key);
            }
            return this.keys;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap, java.util.Map
        public ObjectCollection<V> values() {
            if (this.values == null) {
                this.values = ObjectSets.singleton(this.value);
            }
            return this.values;
        }

        @Override // java.util.Map
        public boolean isEmpty() {
            return false;
        }

        @Override // java.util.Map
        public int hashCode() {
            return HashCommon.long2int(this.key) ^ (this.value == null ? 0 : this.value.hashCode());
        }

        @Override // java.util.Map
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Map)) {
                return false;
            }
            Map<?, ?> m = (Map) o;
            if (m.size() != 1) {
                return false;
            }
            return m.entrySet().iterator().next().equals(entrySet().iterator().next());
        }

        public String toString() {
            return "{" + this.key + "=>" + this.value + "}";
        }
    }

    public static <V> Long2ObjectMap<V> singleton(long key, V value) {
        return new Singleton(key, value);
    }

    public static <V> Long2ObjectMap<V> singleton(Long key, V value) {
        return new Singleton(key.longValue(), value);
    }

    /* loaded from: classes4.dex */
    public static class SynchronizedMap<V> extends Long2ObjectFunctions.SynchronizedFunction<V> implements Long2ObjectMap<V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Long2ObjectMap.Entry<V>> entries;
        protected transient LongSet keys;
        protected final Long2ObjectMap<V> map;
        protected transient ObjectCollection<V> values;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Map
        @Deprecated
        public /* bridge */ /* synthetic */ Object merge(Long l, Object obj, BiFunction biFunction) {
            return merge2(l, (Long) obj, (BiFunction<? super Long, ? super Long, ? extends Long>) biFunction);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Map
        @Deprecated
        public /* bridge */ /* synthetic */ Object putIfAbsent(Long l, Object obj) {
            return putIfAbsent2(l, (Long) obj);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Map
        @Deprecated
        public /* bridge */ /* synthetic */ Object replace(Long l, Object obj) {
            return replace2(l, (Long) obj);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public SynchronizedMap(Long2ObjectMap<V> m, Object sync) {
            super(m, sync);
            this.map = m;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public SynchronizedMap(Long2ObjectMap<V> m) {
            super(m);
            this.map = m;
        }

        @Override // java.util.Map
        public boolean containsValue(Object v) {
            boolean containsValue;
            synchronized (this.sync) {
                containsValue = this.map.containsValue(v);
            }
            return containsValue;
        }

        @Override // java.util.Map
        public void putAll(Map<? extends Long, ? extends V> m) {
            synchronized (this.sync) {
                this.map.putAll(m);
            }
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap, it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap
        public ObjectSet<Long2ObjectMap.Entry<V>> long2ObjectEntrySet() {
            ObjectSet<Long2ObjectMap.Entry<V>> objectSet;
            synchronized (this.sync) {
                if (this.entries == null) {
                    this.entries = ObjectSets.synchronize(this.map.long2ObjectEntrySet(), this.sync);
                }
                objectSet = this.entries;
            }
            return objectSet;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap, java.util.Map
        @Deprecated
        public ObjectSet<Map.Entry<Long, V>> entrySet() {
            return long2ObjectEntrySet();
        }

        /* JADX WARN: Type inference failed for: r1v4, types: [it.unimi.dsi.fastutil.longs.LongSet] */
        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap, java.util.Map
        /* renamed from: keySet */
        public Set<Long> keySet2() {
            LongSet longSet;
            synchronized (this.sync) {
                if (this.keys == null) {
                    this.keys = LongSets.synchronize(this.map.keySet2(), this.sync);
                }
                longSet = this.keys;
            }
            return longSet;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap, java.util.Map
        public ObjectCollection<V> values() {
            ObjectCollection<V> objectCollection;
            synchronized (this.sync) {
                if (this.values == null) {
                    this.values = ObjectCollections.synchronize(this.map.values(), this.sync);
                }
                objectCollection = this.values;
            }
            return objectCollection;
        }

        @Override // java.util.Map
        public boolean isEmpty() {
            boolean isEmpty;
            synchronized (this.sync) {
                isEmpty = this.map.isEmpty();
            }
            return isEmpty;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunctions.SynchronizedFunction, java.util.Map
        public int hashCode() {
            int hashCode;
            synchronized (this.sync) {
                hashCode = this.map.hashCode();
            }
            return hashCode;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunctions.SynchronizedFunction, java.util.Map
        public boolean equals(Object o) {
            boolean equals;
            if (o == this) {
                return true;
            }
            synchronized (this.sync) {
                equals = this.map.equals(o);
            }
            return equals;
        }

        private void writeObject(ObjectOutputStream s) throws IOException {
            synchronized (this.sync) {
                s.defaultWriteObject();
            }
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunctions.SynchronizedFunction, it.unimi.dsi.fastutil.longs.Long2ObjectFunction
        public V getOrDefault(long key, V defaultValue) {
            V orDefault;
            synchronized (this.sync) {
                orDefault = this.map.getOrDefault(key, (long) defaultValue);
            }
            return orDefault;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap, java.util.Map
        public void forEach(BiConsumer<? super Long, ? super V> action) {
            synchronized (this.sync) {
                this.map.forEach(action);
            }
        }

        @Override // java.util.Map
        public void replaceAll(BiFunction<? super Long, ? super V, ? extends V> function) {
            synchronized (this.sync) {
                this.map.replaceAll(function);
            }
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap
        public V putIfAbsent(long key, V value) {
            V putIfAbsent;
            synchronized (this.sync) {
                putIfAbsent = this.map.putIfAbsent(key, (long) value);
            }
            return putIfAbsent;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap
        public boolean remove(long key, Object value) {
            boolean remove;
            synchronized (this.sync) {
                remove = this.map.remove(key, value);
            }
            return remove;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap
        public V replace(long key, V value) {
            V replace;
            synchronized (this.sync) {
                replace = this.map.replace(key, (long) value);
            }
            return replace;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap
        public boolean replace(long key, V oldValue, V newValue) {
            boolean replace;
            synchronized (this.sync) {
                replace = this.map.replace(key, oldValue, newValue);
            }
            return replace;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap
        public V computeIfAbsent(long key, LongFunction<? extends V> mappingFunction) {
            V computeIfAbsent;
            synchronized (this.sync) {
                computeIfAbsent = this.map.computeIfAbsent(key, mappingFunction);
            }
            return computeIfAbsent;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap
        public V computeIfAbsent(long key, Long2ObjectFunction<? extends V> mappingFunction) {
            V computeIfAbsent;
            synchronized (this.sync) {
                computeIfAbsent = this.map.computeIfAbsent(key, (Long2ObjectFunction) mappingFunction);
            }
            return computeIfAbsent;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap
        public V computeIfPresent(long key, BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
            V computeIfPresent;
            synchronized (this.sync) {
                computeIfPresent = this.map.computeIfPresent(key, remappingFunction);
            }
            return computeIfPresent;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap
        public V compute(long key, BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
            V compute;
            synchronized (this.sync) {
                compute = this.map.compute(key, remappingFunction);
            }
            return compute;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap
        public V merge(long key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            V merge;
            synchronized (this.sync) {
                merge = this.map.merge(key, (long) value, (BiFunction<? super long, ? super long, ? extends long>) remappingFunction);
            }
            return merge;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunctions.SynchronizedFunction, it.unimi.dsi.fastutil.longs.Long2ObjectFunction, it.unimi.dsi.fastutil.Function
        @Deprecated
        public V getOrDefault(Object key, V defaultValue) {
            V orDefault;
            synchronized (this.sync) {
                orDefault = this.map.getOrDefault(key, defaultValue);
            }
            return orDefault;
        }

        @Override // java.util.Map
        @Deprecated
        public boolean remove(Object key, Object value) {
            boolean remove;
            synchronized (this.sync) {
                remove = this.map.remove(key, value);
            }
            return remove;
        }

        @Deprecated
        /* renamed from: replace, reason: avoid collision after fix types in other method */
        public V replace2(Long key, V value) {
            V replace;
            synchronized (this.sync) {
                replace = this.map.replace((Long2ObjectMap<V>) key, (Long) value);
            }
            return replace;
        }

        @Override // java.util.Map
        @Deprecated
        public boolean replace(Long key, V oldValue, V newValue) {
            boolean replace;
            synchronized (this.sync) {
                replace = this.map.replace((Long2ObjectMap<V>) key, oldValue, newValue);
            }
            return replace;
        }

        @Deprecated
        /* renamed from: putIfAbsent, reason: avoid collision after fix types in other method */
        public V putIfAbsent2(Long key, V value) {
            V putIfAbsent;
            synchronized (this.sync) {
                putIfAbsent = this.map.putIfAbsent((Long2ObjectMap<V>) key, (Long) value);
            }
            return putIfAbsent;
        }

        @Override // java.util.Map
        @Deprecated
        public V computeIfAbsent(Long key, Function<? super Long, ? extends V> mappingFunction) {
            V computeIfAbsent;
            synchronized (this.sync) {
                computeIfAbsent = this.map.computeIfAbsent((Long2ObjectMap<V>) key, (Function<? super Long2ObjectMap<V>, ? extends V>) mappingFunction);
            }
            return computeIfAbsent;
        }

        @Override // java.util.Map
        @Deprecated
        public V computeIfPresent(Long key, BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
            V computeIfPresent;
            synchronized (this.sync) {
                computeIfPresent = this.map.computeIfPresent((Long2ObjectMap<V>) key, (BiFunction<? super Long2ObjectMap<V>, ? super V, ? extends V>) remappingFunction);
            }
            return computeIfPresent;
        }

        @Override // java.util.Map
        @Deprecated
        public V compute(Long key, BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
            V compute;
            synchronized (this.sync) {
                compute = this.map.compute((Long2ObjectMap<V>) key, (BiFunction<? super Long2ObjectMap<V>, ? super V, ? extends V>) remappingFunction);
            }
            return compute;
        }

        @Deprecated
        /* renamed from: merge, reason: avoid collision after fix types in other method */
        public V merge2(Long key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            V merge;
            synchronized (this.sync) {
                merge = this.map.merge((Long2ObjectMap<V>) key, (Long) value, (BiFunction<? super Long, ? super Long, ? extends Long>) remappingFunction);
            }
            return merge;
        }
    }

    public static <V> Long2ObjectMap<V> synchronize(Long2ObjectMap<V> m) {
        return new SynchronizedMap(m);
    }

    public static <V> Long2ObjectMap<V> synchronize(Long2ObjectMap<V> m, Object sync) {
        return new SynchronizedMap(m, sync);
    }

    /* loaded from: classes4.dex */
    public static class UnmodifiableMap<V> extends Long2ObjectFunctions.UnmodifiableFunction<V> implements Long2ObjectMap<V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Long2ObjectMap.Entry<V>> entries;
        protected transient LongSet keys;
        protected final Long2ObjectMap<? extends V> map;
        protected transient ObjectCollection<V> values;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Map
        @Deprecated
        public /* bridge */ /* synthetic */ Object merge(Long l, Object obj, BiFunction biFunction) {
            return merge2(l, (Long) obj, (BiFunction<? super Long, ? super Long, ? extends Long>) biFunction);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Map
        @Deprecated
        public /* bridge */ /* synthetic */ Object putIfAbsent(Long l, Object obj) {
            return putIfAbsent2(l, (Long) obj);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Map
        @Deprecated
        public /* bridge */ /* synthetic */ Object replace(Long l, Object obj) {
            return replace2(l, (Long) obj);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public UnmodifiableMap(Long2ObjectMap<? extends V> m) {
            super(m);
            this.map = m;
        }

        @Override // java.util.Map
        public boolean containsValue(Object v) {
            return this.map.containsValue(v);
        }

        @Override // java.util.Map
        public void putAll(Map<? extends Long, ? extends V> m) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap, it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap
        public ObjectSet<Long2ObjectMap.Entry<V>> long2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.unmodifiable(this.map.long2ObjectEntrySet());
            }
            return this.entries;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap, java.util.Map
        @Deprecated
        public ObjectSet<Map.Entry<Long, V>> entrySet() {
            return long2ObjectEntrySet();
        }

        /* JADX WARN: Type inference failed for: r0v3, types: [it.unimi.dsi.fastutil.longs.LongSet] */
        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap, java.util.Map
        /* renamed from: keySet */
        public Set<Long> keySet2() {
            if (this.keys == null) {
                this.keys = LongSets.unmodifiable(this.map.keySet2());
            }
            return this.keys;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap, java.util.Map
        public ObjectCollection<V> values() {
            if (this.values == null) {
                this.values = ObjectCollections.unmodifiable(this.map.values());
            }
            return this.values;
        }

        @Override // java.util.Map
        public boolean isEmpty() {
            return this.map.isEmpty();
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunctions.UnmodifiableFunction, java.util.Map
        public int hashCode() {
            return this.map.hashCode();
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunctions.UnmodifiableFunction, java.util.Map
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            return this.map.equals(o);
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunctions.UnmodifiableFunction, it.unimi.dsi.fastutil.longs.Long2ObjectFunction
        public V getOrDefault(long key, V defaultValue) {
            return this.map.getOrDefault(key, (long) defaultValue);
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap, java.util.Map
        public void forEach(BiConsumer<? super Long, ? super V> action) {
            this.map.forEach(action);
        }

        @Override // java.util.Map
        public void replaceAll(BiFunction<? super Long, ? super V, ? extends V> function) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap
        public V putIfAbsent(long key, V value) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap
        public boolean remove(long key, Object value) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap
        public V replace(long key, V value) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap
        public boolean replace(long key, V oldValue, V newValue) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap
        public V computeIfAbsent(long key, LongFunction<? extends V> mappingFunction) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap
        public V computeIfAbsent(long key, Long2ObjectFunction<? extends V> mappingFunction) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap
        public V computeIfPresent(long key, BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap
        public V compute(long key, BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap
        public V merge(long key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunctions.UnmodifiableFunction, it.unimi.dsi.fastutil.longs.Long2ObjectFunction, it.unimi.dsi.fastutil.Function
        @Deprecated
        public V getOrDefault(Object key, V defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }

        @Override // java.util.Map
        @Deprecated
        public boolean remove(Object key, Object value) {
            throw new UnsupportedOperationException();
        }

        @Deprecated
        /* renamed from: replace, reason: avoid collision after fix types in other method */
        public V replace2(Long key, V value) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        @Deprecated
        public boolean replace(Long key, V oldValue, V newValue) {
            throw new UnsupportedOperationException();
        }

        @Deprecated
        /* renamed from: putIfAbsent, reason: avoid collision after fix types in other method */
        public V putIfAbsent2(Long key, V value) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        @Deprecated
        public V computeIfAbsent(Long key, Function<? super Long, ? extends V> mappingFunction) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        @Deprecated
        public V computeIfPresent(Long key, BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        @Deprecated
        public V compute(Long key, BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        @Deprecated
        /* renamed from: merge, reason: avoid collision after fix types in other method */
        public V merge2(Long key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }

    public static <V> Long2ObjectMap<V> unmodifiable(Long2ObjectMap<? extends V> m) {
        return new UnmodifiableMap(m);
    }
}
