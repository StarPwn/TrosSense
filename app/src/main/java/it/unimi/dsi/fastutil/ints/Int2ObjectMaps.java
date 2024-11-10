package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunctions;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
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
import java.util.function.IntFunction;

/* loaded from: classes4.dex */
public final class Int2ObjectMaps {
    public static final EmptyMap EMPTY_MAP = new EmptyMap();

    private Int2ObjectMaps() {
    }

    public static <V> ObjectIterator<Int2ObjectMap.Entry<V>> fastIterator(Int2ObjectMap<V> map) {
        ObjectSet<Int2ObjectMap.Entry<V>> entries = map.int2ObjectEntrySet();
        return entries instanceof Int2ObjectMap.FastEntrySet ? ((Int2ObjectMap.FastEntrySet) entries).fastIterator() : entries.iterator();
    }

    public static <V> void fastForEach(Int2ObjectMap<V> map, Consumer<? super Int2ObjectMap.Entry<V>> consumer) {
        ObjectSet<Int2ObjectMap.Entry<V>> entries = map.int2ObjectEntrySet();
        if (!(entries instanceof Int2ObjectMap.FastEntrySet)) {
            entries.forEach(consumer);
        } else {
            ((Int2ObjectMap.FastEntrySet) entries).fastForEach(consumer);
        }
    }

    public static <V> ObjectIterable<Int2ObjectMap.Entry<V>> fastIterable(Int2ObjectMap<V> map) {
        final ObjectSet<Int2ObjectMap.Entry<V>> entries = map.int2ObjectEntrySet();
        return entries instanceof Int2ObjectMap.FastEntrySet ? new ObjectIterable<Int2ObjectMap.Entry<V>>() { // from class: it.unimi.dsi.fastutil.ints.Int2ObjectMaps.1
            @Override // it.unimi.dsi.fastutil.objects.ObjectIterable, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection
            public ObjectIterator<Int2ObjectMap.Entry<V>> iterator() {
                return ((Int2ObjectMap.FastEntrySet) ObjectSet.this).fastIterator();
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectIterable, it.unimi.dsi.fastutil.objects.ObjectCollection
            /* renamed from: spliterator */
            public ObjectSpliterator<Int2ObjectMap.Entry<V>> mo221spliterator() {
                return ObjectSet.this.mo221spliterator();
            }

            @Override // java.lang.Iterable
            public void forEach(Consumer<? super Int2ObjectMap.Entry<V>> consumer) {
                ((Int2ObjectMap.FastEntrySet) ObjectSet.this).fastForEach(consumer);
            }
        } : entries;
    }

    /* loaded from: classes4.dex */
    public static class EmptyMap<V> extends Int2ObjectFunctions.EmptyFunction<V> implements Int2ObjectMap<V>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;

        @Override // java.util.Map
        public boolean containsValue(Object v) {
            return false;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction, it.unimi.dsi.fastutil.Function
        @Deprecated
        public V getOrDefault(Object key, V defaultValue) {
            return defaultValue;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunctions.EmptyFunction, it.unimi.dsi.fastutil.ints.Int2ObjectFunction
        public V getOrDefault(int key, V defaultValue) {
            return defaultValue;
        }

        @Override // java.util.Map
        public void putAll(Map<? extends Integer, ? extends V> m) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap, it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public ObjectSet<Int2ObjectMap.Entry<V>> int2ObjectEntrySet() {
            return ObjectSets.EMPTY_SET;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        /* renamed from: keySet */
        public Set<Integer> keySet2() {
            return IntSets.EMPTY_SET;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        public ObjectCollection<V> values() {
            return ObjectSets.EMPTY_SET;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        public void forEach(BiConsumer<? super Integer, ? super V> consumer) {
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunctions.EmptyFunction
        public Object clone() {
            return Int2ObjectMaps.EMPTY_MAP;
        }

        @Override // java.util.Map
        public boolean isEmpty() {
            return true;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunctions.EmptyFunction, java.util.Map
        public int hashCode() {
            return 0;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunctions.EmptyFunction, java.util.Map
        public boolean equals(Object o) {
            if (o instanceof Map) {
                return ((Map) o).isEmpty();
            }
            return false;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunctions.EmptyFunction
        public String toString() {
            return "{}";
        }
    }

    public static <V> Int2ObjectMap<V> emptyMap() {
        return EMPTY_MAP;
    }

    /* loaded from: classes4.dex */
    public static class Singleton<V> extends Int2ObjectFunctions.Singleton<V> implements Int2ObjectMap<V>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Int2ObjectMap.Entry<V>> entries;
        protected transient IntSet keys;
        protected transient ObjectCollection<V> values;

        /* JADX INFO: Access modifiers changed from: protected */
        public Singleton(int key, V value) {
            super(key, value);
        }

        @Override // java.util.Map
        public boolean containsValue(Object v) {
            return Objects.equals(this.value, v);
        }

        @Override // java.util.Map
        public void putAll(Map<? extends Integer, ? extends V> m) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap, it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public ObjectSet<Int2ObjectMap.Entry<V>> int2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.singleton(new AbstractInt2ObjectMap.BasicEntry(this.key, this.value));
            }
            return this.entries;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        @Deprecated
        public ObjectSet<Map.Entry<Integer, V>> entrySet() {
            return int2ObjectEntrySet();
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        /* renamed from: keySet */
        public Set<Integer> keySet2() {
            if (this.keys == null) {
                this.keys = IntSets.singleton(this.key);
            }
            return this.keys;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
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
            return this.key ^ (this.value == null ? 0 : this.value.hashCode());
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

    public static <V> Int2ObjectMap<V> singleton(int key, V value) {
        return new Singleton(key, value);
    }

    public static <V> Int2ObjectMap<V> singleton(Integer key, V value) {
        return new Singleton(key.intValue(), value);
    }

    /* loaded from: classes4.dex */
    public static class SynchronizedMap<V> extends Int2ObjectFunctions.SynchronizedFunction<V> implements Int2ObjectMap<V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Int2ObjectMap.Entry<V>> entries;
        protected transient IntSet keys;
        protected final Int2ObjectMap<V> map;
        protected transient ObjectCollection<V> values;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Map
        @Deprecated
        public /* bridge */ /* synthetic */ Object merge(Integer num, Object obj, BiFunction biFunction) {
            return merge2(num, (Integer) obj, (BiFunction<? super Integer, ? super Integer, ? extends Integer>) biFunction);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Map
        @Deprecated
        public /* bridge */ /* synthetic */ Object putIfAbsent(Integer num, Object obj) {
            return putIfAbsent2(num, (Integer) obj);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Map
        @Deprecated
        public /* bridge */ /* synthetic */ Object replace(Integer num, Object obj) {
            return replace2(num, (Integer) obj);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public SynchronizedMap(Int2ObjectMap<V> m, Object sync) {
            super(m, sync);
            this.map = m;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public SynchronizedMap(Int2ObjectMap<V> m) {
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
        public void putAll(Map<? extends Integer, ? extends V> m) {
            synchronized (this.sync) {
                this.map.putAll(m);
            }
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap, it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public ObjectSet<Int2ObjectMap.Entry<V>> int2ObjectEntrySet() {
            ObjectSet<Int2ObjectMap.Entry<V>> objectSet;
            synchronized (this.sync) {
                if (this.entries == null) {
                    this.entries = ObjectSets.synchronize(this.map.int2ObjectEntrySet(), this.sync);
                }
                objectSet = this.entries;
            }
            return objectSet;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        @Deprecated
        public ObjectSet<Map.Entry<Integer, V>> entrySet() {
            return int2ObjectEntrySet();
        }

        /* JADX WARN: Type inference failed for: r1v4, types: [it.unimi.dsi.fastutil.ints.IntSet] */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        /* renamed from: keySet */
        public Set<Integer> keySet2() {
            IntSet intSet;
            synchronized (this.sync) {
                if (this.keys == null) {
                    this.keys = IntSets.synchronize(this.map.keySet2(), this.sync);
                }
                intSet = this.keys;
            }
            return intSet;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
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

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunctions.SynchronizedFunction, java.util.Map
        public int hashCode() {
            int hashCode;
            synchronized (this.sync) {
                hashCode = this.map.hashCode();
            }
            return hashCode;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunctions.SynchronizedFunction, java.util.Map
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

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunctions.SynchronizedFunction, it.unimi.dsi.fastutil.ints.Int2ObjectFunction
        public V getOrDefault(int key, V defaultValue) {
            V orDefault;
            synchronized (this.sync) {
                orDefault = this.map.getOrDefault(key, (int) defaultValue);
            }
            return orDefault;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        public void forEach(BiConsumer<? super Integer, ? super V> action) {
            synchronized (this.sync) {
                this.map.forEach(action);
            }
        }

        @Override // java.util.Map
        public void replaceAll(BiFunction<? super Integer, ? super V, ? extends V> function) {
            synchronized (this.sync) {
                this.map.replaceAll(function);
            }
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap
        public V putIfAbsent(int key, V value) {
            V putIfAbsent;
            synchronized (this.sync) {
                putIfAbsent = this.map.putIfAbsent(key, (int) value);
            }
            return putIfAbsent;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap
        public boolean remove(int key, Object value) {
            boolean remove;
            synchronized (this.sync) {
                remove = this.map.remove(key, value);
            }
            return remove;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap
        public V replace(int key, V value) {
            V replace;
            synchronized (this.sync) {
                replace = this.map.replace(key, (int) value);
            }
            return replace;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap
        public boolean replace(int key, V oldValue, V newValue) {
            boolean replace;
            synchronized (this.sync) {
                replace = this.map.replace(key, oldValue, newValue);
            }
            return replace;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap
        public V computeIfAbsent(int key, IntFunction<? extends V> mappingFunction) {
            V computeIfAbsent;
            synchronized (this.sync) {
                computeIfAbsent = this.map.computeIfAbsent(key, mappingFunction);
            }
            return computeIfAbsent;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap
        public V computeIfAbsent(int key, Int2ObjectFunction<? extends V> mappingFunction) {
            V computeIfAbsent;
            synchronized (this.sync) {
                computeIfAbsent = this.map.computeIfAbsent(key, (Int2ObjectFunction) mappingFunction);
            }
            return computeIfAbsent;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap
        public V computeIfPresent(int key, BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
            V computeIfPresent;
            synchronized (this.sync) {
                computeIfPresent = this.map.computeIfPresent(key, remappingFunction);
            }
            return computeIfPresent;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap
        public V compute(int key, BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
            V compute;
            synchronized (this.sync) {
                compute = this.map.compute(key, remappingFunction);
            }
            return compute;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap
        public V merge(int key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            V merge;
            synchronized (this.sync) {
                merge = this.map.merge(key, (int) value, (BiFunction<? super int, ? super int, ? extends int>) remappingFunction);
            }
            return merge;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunctions.SynchronizedFunction, it.unimi.dsi.fastutil.ints.Int2ObjectFunction, it.unimi.dsi.fastutil.Function
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
        public V replace2(Integer key, V value) {
            V replace;
            synchronized (this.sync) {
                replace = this.map.replace((Int2ObjectMap<V>) key, (Integer) value);
            }
            return replace;
        }

        @Override // java.util.Map
        @Deprecated
        public boolean replace(Integer key, V oldValue, V newValue) {
            boolean replace;
            synchronized (this.sync) {
                replace = this.map.replace((Int2ObjectMap<V>) key, oldValue, newValue);
            }
            return replace;
        }

        @Deprecated
        /* renamed from: putIfAbsent, reason: avoid collision after fix types in other method */
        public V putIfAbsent2(Integer key, V value) {
            V putIfAbsent;
            synchronized (this.sync) {
                putIfAbsent = this.map.putIfAbsent((Int2ObjectMap<V>) key, (Integer) value);
            }
            return putIfAbsent;
        }

        @Override // java.util.Map
        @Deprecated
        public V computeIfAbsent(Integer key, Function<? super Integer, ? extends V> mappingFunction) {
            V computeIfAbsent;
            synchronized (this.sync) {
                computeIfAbsent = this.map.computeIfAbsent((Int2ObjectMap<V>) key, (Function<? super Int2ObjectMap<V>, ? extends V>) mappingFunction);
            }
            return computeIfAbsent;
        }

        @Override // java.util.Map
        @Deprecated
        public V computeIfPresent(Integer key, BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
            V computeIfPresent;
            synchronized (this.sync) {
                computeIfPresent = this.map.computeIfPresent((Int2ObjectMap<V>) key, (BiFunction<? super Int2ObjectMap<V>, ? super V, ? extends V>) remappingFunction);
            }
            return computeIfPresent;
        }

        @Override // java.util.Map
        @Deprecated
        public V compute(Integer key, BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
            V compute;
            synchronized (this.sync) {
                compute = this.map.compute((Int2ObjectMap<V>) key, (BiFunction<? super Int2ObjectMap<V>, ? super V, ? extends V>) remappingFunction);
            }
            return compute;
        }

        @Deprecated
        /* renamed from: merge, reason: avoid collision after fix types in other method */
        public V merge2(Integer key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            V merge;
            synchronized (this.sync) {
                merge = this.map.merge((Int2ObjectMap<V>) key, (Integer) value, (BiFunction<? super Integer, ? super Integer, ? extends Integer>) remappingFunction);
            }
            return merge;
        }
    }

    public static <V> Int2ObjectMap<V> synchronize(Int2ObjectMap<V> m) {
        return new SynchronizedMap(m);
    }

    public static <V> Int2ObjectMap<V> synchronize(Int2ObjectMap<V> m, Object sync) {
        return new SynchronizedMap(m, sync);
    }

    /* loaded from: classes4.dex */
    public static class UnmodifiableMap<V> extends Int2ObjectFunctions.UnmodifiableFunction<V> implements Int2ObjectMap<V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Int2ObjectMap.Entry<V>> entries;
        protected transient IntSet keys;
        protected final Int2ObjectMap<? extends V> map;
        protected transient ObjectCollection<V> values;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Map
        @Deprecated
        public /* bridge */ /* synthetic */ Object merge(Integer num, Object obj, BiFunction biFunction) {
            return merge2(num, (Integer) obj, (BiFunction<? super Integer, ? super Integer, ? extends Integer>) biFunction);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Map
        @Deprecated
        public /* bridge */ /* synthetic */ Object putIfAbsent(Integer num, Object obj) {
            return putIfAbsent2(num, (Integer) obj);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Map
        @Deprecated
        public /* bridge */ /* synthetic */ Object replace(Integer num, Object obj) {
            return replace2(num, (Integer) obj);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public UnmodifiableMap(Int2ObjectMap<? extends V> m) {
            super(m);
            this.map = m;
        }

        @Override // java.util.Map
        public boolean containsValue(Object v) {
            return this.map.containsValue(v);
        }

        @Override // java.util.Map
        public void putAll(Map<? extends Integer, ? extends V> m) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap, it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public ObjectSet<Int2ObjectMap.Entry<V>> int2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.unmodifiable(this.map.int2ObjectEntrySet());
            }
            return this.entries;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        @Deprecated
        public ObjectSet<Map.Entry<Integer, V>> entrySet() {
            return int2ObjectEntrySet();
        }

        /* JADX WARN: Type inference failed for: r0v3, types: [it.unimi.dsi.fastutil.ints.IntSet] */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        /* renamed from: keySet */
        public Set<Integer> keySet2() {
            if (this.keys == null) {
                this.keys = IntSets.unmodifiable(this.map.keySet2());
            }
            return this.keys;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
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

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunctions.UnmodifiableFunction, java.util.Map
        public int hashCode() {
            return this.map.hashCode();
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunctions.UnmodifiableFunction, java.util.Map
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            return this.map.equals(o);
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunctions.UnmodifiableFunction, it.unimi.dsi.fastutil.ints.Int2ObjectFunction
        public V getOrDefault(int key, V defaultValue) {
            return this.map.getOrDefault(key, (int) defaultValue);
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        public void forEach(BiConsumer<? super Integer, ? super V> action) {
            this.map.forEach(action);
        }

        @Override // java.util.Map
        public void replaceAll(BiFunction<? super Integer, ? super V, ? extends V> function) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap
        public V putIfAbsent(int key, V value) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap
        public boolean remove(int key, Object value) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap
        public V replace(int key, V value) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap
        public boolean replace(int key, V oldValue, V newValue) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap
        public V computeIfAbsent(int key, IntFunction<? extends V> mappingFunction) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap
        public V computeIfAbsent(int key, Int2ObjectFunction<? extends V> mappingFunction) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap
        public V computeIfPresent(int key, BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap
        public V compute(int key, BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap
        public V merge(int key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunctions.UnmodifiableFunction, it.unimi.dsi.fastutil.ints.Int2ObjectFunction, it.unimi.dsi.fastutil.Function
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
        public V replace2(Integer key, V value) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        @Deprecated
        public boolean replace(Integer key, V oldValue, V newValue) {
            throw new UnsupportedOperationException();
        }

        @Deprecated
        /* renamed from: putIfAbsent, reason: avoid collision after fix types in other method */
        public V putIfAbsent2(Integer key, V value) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        @Deprecated
        public V computeIfAbsent(Integer key, Function<? super Integer, ? extends V> mappingFunction) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        @Deprecated
        public V computeIfPresent(Integer key, BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        @Deprecated
        public V compute(Integer key, BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        @Deprecated
        /* renamed from: merge, reason: avoid collision after fix types in other method */
        public V merge2(Integer key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }

    public static <V> Int2ObjectMap<V> unmodifiable(Int2ObjectMap<? extends V> m) {
        return new UnmodifiableMap(m);
    }
}
