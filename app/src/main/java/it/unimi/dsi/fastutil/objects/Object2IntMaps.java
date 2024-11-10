package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntCollections;
import it.unimi.dsi.fastutil.ints.IntSets;
import it.unimi.dsi.fastutil.objects.AbstractObject2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntFunctions;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.ToIntFunction;

/* loaded from: classes4.dex */
public final class Object2IntMaps {
    public static final EmptyMap EMPTY_MAP = new EmptyMap();

    private Object2IntMaps() {
    }

    public static <K> ObjectIterator<Object2IntMap.Entry<K>> fastIterator(Object2IntMap<K> map) {
        ObjectSet<Object2IntMap.Entry<K>> entries = map.object2IntEntrySet();
        return entries instanceof Object2IntMap.FastEntrySet ? ((Object2IntMap.FastEntrySet) entries).fastIterator() : entries.iterator();
    }

    public static <K> void fastForEach(Object2IntMap<K> map, Consumer<? super Object2IntMap.Entry<K>> consumer) {
        ObjectSet<Object2IntMap.Entry<K>> entries = map.object2IntEntrySet();
        if (!(entries instanceof Object2IntMap.FastEntrySet)) {
            entries.forEach(consumer);
        } else {
            ((Object2IntMap.FastEntrySet) entries).fastForEach(consumer);
        }
    }

    public static <K> ObjectIterable<Object2IntMap.Entry<K>> fastIterable(Object2IntMap<K> map) {
        final ObjectSet<Object2IntMap.Entry<K>> entries = map.object2IntEntrySet();
        return entries instanceof Object2IntMap.FastEntrySet ? new ObjectIterable<Object2IntMap.Entry<K>>() { // from class: it.unimi.dsi.fastutil.objects.Object2IntMaps.1
            @Override // it.unimi.dsi.fastutil.objects.ObjectIterable, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection
            public ObjectIterator<Object2IntMap.Entry<K>> iterator() {
                return ((Object2IntMap.FastEntrySet) ObjectSet.this).fastIterator();
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectIterable, it.unimi.dsi.fastutil.objects.ObjectCollection
            /* renamed from: spliterator */
            public ObjectSpliterator<Object2IntMap.Entry<K>> mo221spliterator() {
                return ObjectSet.this.mo221spliterator();
            }

            @Override // java.lang.Iterable
            public void forEach(Consumer<? super Object2IntMap.Entry<K>> consumer) {
                ((Object2IntMap.FastEntrySet) ObjectSet.this).fastForEach(consumer);
            }
        } : entries;
    }

    /* loaded from: classes4.dex */
    public static class EmptyMap<K> extends Object2IntFunctions.EmptyFunction<K> implements Object2IntMap<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
        public boolean containsValue(int v) {
            return false;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction, it.unimi.dsi.fastutil.Function
        @Deprecated
        public Integer getOrDefault(Object key, Integer defaultValue) {
            return defaultValue;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunctions.EmptyFunction, it.unimi.dsi.fastutil.objects.Object2IntFunction
        public int getOrDefault(Object key, int defaultValue) {
            return defaultValue;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
        @Deprecated
        public boolean containsValue(Object ov) {
            return false;
        }

        @Override // java.util.Map
        public void putAll(Map<? extends K, ? extends Integer> m) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, it.unimi.dsi.fastutil.objects.Object2IntSortedMap
        public ObjectSet<Object2IntMap.Entry<K>> object2IntEntrySet() {
            return ObjectSets.EMPTY_SET;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
        public ObjectSet<K> keySet() {
            return ObjectSets.EMPTY_SET;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
        /* renamed from: values, reason: merged with bridge method [inline-methods] */
        public Collection<Integer> values2() {
            return IntSets.EMPTY_SET;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
        public void forEach(BiConsumer<? super K, ? super Integer> consumer) {
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunctions.EmptyFunction
        public Object clone() {
            return Object2IntMaps.EMPTY_MAP;
        }

        @Override // java.util.Map
        public boolean isEmpty() {
            return true;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunctions.EmptyFunction, java.util.Map
        public int hashCode() {
            return 0;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunctions.EmptyFunction, java.util.Map
        public boolean equals(Object o) {
            if (o instanceof Map) {
                return ((Map) o).isEmpty();
            }
            return false;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunctions.EmptyFunction
        public String toString() {
            return "{}";
        }
    }

    public static <K> Object2IntMap<K> emptyMap() {
        return EMPTY_MAP;
    }

    /* loaded from: classes4.dex */
    public static class Singleton<K> extends Object2IntFunctions.Singleton<K> implements Object2IntMap<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Object2IntMap.Entry<K>> entries;
        protected transient ObjectSet<K> keys;
        protected transient IntCollection values;

        /* JADX INFO: Access modifiers changed from: protected */
        public Singleton(K key, int value) {
            super(key, value);
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
        public boolean containsValue(int v) {
            return this.value == v;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
        @Deprecated
        public boolean containsValue(Object ov) {
            return ((Integer) ov).intValue() == this.value;
        }

        @Override // java.util.Map
        public void putAll(Map<? extends K, ? extends Integer> m) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, it.unimi.dsi.fastutil.objects.Object2IntSortedMap
        public ObjectSet<Object2IntMap.Entry<K>> object2IntEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.singleton(new AbstractObject2IntMap.BasicEntry(this.key, this.value));
            }
            return this.entries;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
        @Deprecated
        public ObjectSet<Map.Entry<K, Integer>> entrySet() {
            return object2IntEntrySet();
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
        public ObjectSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ObjectSets.singleton(this.key);
            }
            return this.keys;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
        /* renamed from: values */
        public Collection<Integer> values2() {
            if (this.values == null) {
                this.values = IntSets.singleton(this.value);
            }
            return this.values;
        }

        @Override // java.util.Map
        public boolean isEmpty() {
            return false;
        }

        @Override // java.util.Map
        public int hashCode() {
            return (this.key == null ? 0 : this.key.hashCode()) ^ this.value;
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

    public static <K> Object2IntMap<K> singleton(K key, int value) {
        return new Singleton(key, value);
    }

    public static <K> Object2IntMap<K> singleton(K key, Integer value) {
        return new Singleton(key, value.intValue());
    }

    /* loaded from: classes4.dex */
    public static class SynchronizedMap<K> extends Object2IntFunctions.SynchronizedFunction<K> implements Object2IntMap<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Object2IntMap.Entry<K>> entries;
        protected transient ObjectSet<K> keys;
        protected final Object2IntMap<K> map;
        protected transient IntCollection values;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Map
        public /* bridge */ /* synthetic */ Integer compute(Object obj, BiFunction biFunction) {
            return compute((SynchronizedMap<K>) obj, (BiFunction<? super SynchronizedMap<K>, ? super Integer, ? extends Integer>) biFunction);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Map
        public /* bridge */ /* synthetic */ Integer computeIfAbsent(Object obj, Function function) {
            return computeIfAbsent((SynchronizedMap<K>) obj, (Function<? super SynchronizedMap<K>, ? extends Integer>) function);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Map
        public /* bridge */ /* synthetic */ Integer computeIfPresent(Object obj, BiFunction biFunction) {
            return computeIfPresent((SynchronizedMap<K>) obj, (BiFunction<? super SynchronizedMap<K>, ? super Integer, ? extends Integer>) biFunction);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
        @Deprecated
        public /* bridge */ /* synthetic */ Integer merge(Object obj, Integer num, BiFunction<? super Integer, ? super Integer, ? extends Integer> biFunction) {
            return merge((SynchronizedMap<K>) obj, num, biFunction);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
        @Deprecated
        public /* bridge */ /* synthetic */ Integer putIfAbsent(Object obj, Integer num) {
            return putIfAbsent((SynchronizedMap<K>) obj, num);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
        @Deprecated
        public /* bridge */ /* synthetic */ Integer replace(Object obj, Integer num) {
            return replace((SynchronizedMap<K>) obj, num);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
        @Deprecated
        public /* bridge */ /* synthetic */ boolean replace(Object obj, Integer num, Integer num2) {
            return replace((SynchronizedMap<K>) obj, num, num2);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public SynchronizedMap(Object2IntMap<K> m, Object sync) {
            super(m, sync);
            this.map = m;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public SynchronizedMap(Object2IntMap<K> m) {
            super(m);
            this.map = m;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
        public boolean containsValue(int v) {
            boolean containsValue;
            synchronized (this.sync) {
                containsValue = this.map.containsValue(v);
            }
            return containsValue;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
        @Deprecated
        public boolean containsValue(Object ov) {
            boolean containsValue;
            synchronized (this.sync) {
                containsValue = this.map.containsValue(ov);
            }
            return containsValue;
        }

        @Override // java.util.Map
        public void putAll(Map<? extends K, ? extends Integer> m) {
            synchronized (this.sync) {
                this.map.putAll(m);
            }
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, it.unimi.dsi.fastutil.objects.Object2IntSortedMap
        public ObjectSet<Object2IntMap.Entry<K>> object2IntEntrySet() {
            ObjectSet<Object2IntMap.Entry<K>> objectSet;
            synchronized (this.sync) {
                if (this.entries == null) {
                    this.entries = ObjectSets.synchronize(this.map.object2IntEntrySet(), this.sync);
                }
                objectSet = this.entries;
            }
            return objectSet;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
        @Deprecated
        public ObjectSet<Map.Entry<K, Integer>> entrySet() {
            return object2IntEntrySet();
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
        public ObjectSet<K> keySet() {
            ObjectSet<K> objectSet;
            synchronized (this.sync) {
                if (this.keys == null) {
                    this.keys = ObjectSets.synchronize(this.map.keySet(), this.sync);
                }
                objectSet = this.keys;
            }
            return objectSet;
        }

        /* JADX WARN: Type inference failed for: r1v4, types: [it.unimi.dsi.fastutil.ints.IntCollection] */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
        /* renamed from: values */
        public Collection<Integer> values2() {
            IntCollection intCollection;
            synchronized (this.sync) {
                if (this.values == null) {
                    this.values = IntCollections.synchronize(this.map.values2(), this.sync);
                }
                intCollection = this.values;
            }
            return intCollection;
        }

        @Override // java.util.Map
        public boolean isEmpty() {
            boolean isEmpty;
            synchronized (this.sync) {
                isEmpty = this.map.isEmpty();
            }
            return isEmpty;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunctions.SynchronizedFunction, java.util.Map
        public int hashCode() {
            int hashCode;
            synchronized (this.sync) {
                hashCode = this.map.hashCode();
            }
            return hashCode;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunctions.SynchronizedFunction, java.util.Map
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

        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunctions.SynchronizedFunction, it.unimi.dsi.fastutil.objects.Object2IntFunction
        public int getOrDefault(Object key, int defaultValue) {
            int orDefault;
            synchronized (this.sync) {
                orDefault = this.map.getOrDefault(key, defaultValue);
            }
            return orDefault;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
        public void forEach(BiConsumer<? super K, ? super Integer> action) {
            synchronized (this.sync) {
                this.map.forEach(action);
            }
        }

        @Override // java.util.Map
        public void replaceAll(BiFunction<? super K, ? super Integer, ? extends Integer> function) {
            synchronized (this.sync) {
                this.map.replaceAll(function);
            }
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
        public int putIfAbsent(K key, int value) {
            int putIfAbsent;
            synchronized (this.sync) {
                putIfAbsent = this.map.putIfAbsent((Object2IntMap<K>) key, value);
            }
            return putIfAbsent;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
        public boolean remove(Object key, int value) {
            boolean remove;
            synchronized (this.sync) {
                remove = this.map.remove(key, value);
            }
            return remove;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
        public int replace(K key, int value) {
            int replace;
            synchronized (this.sync) {
                replace = this.map.replace((Object2IntMap<K>) key, value);
            }
            return replace;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
        public boolean replace(K key, int oldValue, int newValue) {
            boolean replace;
            synchronized (this.sync) {
                replace = this.map.replace((Object2IntMap<K>) key, oldValue, newValue);
            }
            return replace;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
        public int computeIfAbsent(K key, ToIntFunction<? super K> mappingFunction) {
            int computeIfAbsent;
            synchronized (this.sync) {
                computeIfAbsent = this.map.computeIfAbsent((Object2IntMap<K>) key, (ToIntFunction<? super Object2IntMap<K>>) mappingFunction);
            }
            return computeIfAbsent;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
        public int computeIfAbsent(K key, Object2IntFunction<? super K> mappingFunction) {
            int computeIfAbsent;
            synchronized (this.sync) {
                computeIfAbsent = this.map.computeIfAbsent((Object2IntMap<K>) key, (Object2IntFunction<? super Object2IntMap<K>>) mappingFunction);
            }
            return computeIfAbsent;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
        public int computeIntIfPresent(K key, BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
            int computeIntIfPresent;
            synchronized (this.sync) {
                computeIntIfPresent = this.map.computeIntIfPresent(key, remappingFunction);
            }
            return computeIntIfPresent;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
        public int computeInt(K key, BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
            int computeInt;
            synchronized (this.sync) {
                computeInt = this.map.computeInt(key, remappingFunction);
            }
            return computeInt;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
        public int merge(K key, int value, BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
            int merge;
            synchronized (this.sync) {
                merge = this.map.merge((Object2IntMap<K>) key, value, remappingFunction);
            }
            return merge;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunctions.SynchronizedFunction, it.unimi.dsi.fastutil.objects.Object2IntFunction, it.unimi.dsi.fastutil.Function
        @Deprecated
        public Integer getOrDefault(Object key, Integer defaultValue) {
            Integer orDefault;
            synchronized (this.sync) {
                orDefault = this.map.getOrDefault(key, defaultValue);
            }
            return orDefault;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
        @Deprecated
        public boolean remove(Object key, Object value) {
            boolean remove;
            synchronized (this.sync) {
                remove = this.map.remove(key, value);
            }
            return remove;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
        @Deprecated
        public Integer replace(K key, Integer value) {
            Integer replace2;
            synchronized (this.sync) {
                replace2 = this.map.replace2((Object2IntMap<K>) key, value);
            }
            return replace2;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
        @Deprecated
        public boolean replace(K key, Integer oldValue, Integer newValue) {
            boolean replace2;
            synchronized (this.sync) {
                replace2 = this.map.replace2((Object2IntMap<K>) key, oldValue, newValue);
            }
            return replace2;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
        @Deprecated
        public Integer putIfAbsent(K key, Integer value) {
            Integer putIfAbsent2;
            synchronized (this.sync) {
                putIfAbsent2 = this.map.putIfAbsent2((Object2IntMap<K>) key, value);
            }
            return putIfAbsent2;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Map
        public Integer computeIfAbsent(K key, Function<? super K, ? extends Integer> mappingFunction) {
            Integer computeIfAbsent;
            synchronized (this.sync) {
                computeIfAbsent = this.map.computeIfAbsent((Object2IntMap<K>) key, (Function<? super Object2IntMap<K>, ? extends Integer>) mappingFunction);
            }
            return computeIfAbsent;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Map
        public Integer computeIfPresent(K key, BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
            Integer computeIfPresent;
            synchronized (this.sync) {
                computeIfPresent = this.map.computeIfPresent(key, remappingFunction);
            }
            return computeIfPresent;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Map
        public Integer compute(K key, BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
            Integer compute;
            synchronized (this.sync) {
                compute = this.map.compute(key, remappingFunction);
            }
            return compute;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
        @Deprecated
        public Integer merge(K key, Integer value, BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
            Integer merge2;
            synchronized (this.sync) {
                merge2 = this.map.merge2((Object2IntMap<K>) key, value, remappingFunction);
            }
            return merge2;
        }
    }

    public static <K> Object2IntMap<K> synchronize(Object2IntMap<K> m) {
        return new SynchronizedMap(m);
    }

    public static <K> Object2IntMap<K> synchronize(Object2IntMap<K> m, Object sync) {
        return new SynchronizedMap(m, sync);
    }

    /* loaded from: classes4.dex */
    public static class UnmodifiableMap<K> extends Object2IntFunctions.UnmodifiableFunction<K> implements Object2IntMap<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Object2IntMap.Entry<K>> entries;
        protected transient ObjectSet<K> keys;
        protected final Object2IntMap<? extends K> map;
        protected transient IntCollection values;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Map
        public /* bridge */ /* synthetic */ Integer compute(Object obj, BiFunction biFunction) {
            return compute((UnmodifiableMap<K>) obj, (BiFunction<? super UnmodifiableMap<K>, ? super Integer, ? extends Integer>) biFunction);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Map
        public /* bridge */ /* synthetic */ Integer computeIfAbsent(Object obj, Function function) {
            return computeIfAbsent((UnmodifiableMap<K>) obj, (Function<? super UnmodifiableMap<K>, ? extends Integer>) function);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Map
        public /* bridge */ /* synthetic */ Integer computeIfPresent(Object obj, BiFunction biFunction) {
            return computeIfPresent((UnmodifiableMap<K>) obj, (BiFunction<? super UnmodifiableMap<K>, ? super Integer, ? extends Integer>) biFunction);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
        @Deprecated
        public /* bridge */ /* synthetic */ Integer merge(Object obj, Integer num, BiFunction<? super Integer, ? super Integer, ? extends Integer> biFunction) {
            return merge((UnmodifiableMap<K>) obj, num, biFunction);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
        @Deprecated
        public /* bridge */ /* synthetic */ Integer putIfAbsent(Object obj, Integer num) {
            return putIfAbsent((UnmodifiableMap<K>) obj, num);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
        @Deprecated
        public /* bridge */ /* synthetic */ Integer replace(Object obj, Integer num) {
            return replace((UnmodifiableMap<K>) obj, num);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
        @Deprecated
        public /* bridge */ /* synthetic */ boolean replace(Object obj, Integer num, Integer num2) {
            return replace((UnmodifiableMap<K>) obj, num, num2);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public UnmodifiableMap(Object2IntMap<? extends K> m) {
            super(m);
            this.map = m;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
        public boolean containsValue(int v) {
            return this.map.containsValue(v);
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
        @Deprecated
        public boolean containsValue(Object ov) {
            return this.map.containsValue(ov);
        }

        @Override // java.util.Map
        public void putAll(Map<? extends K, ? extends Integer> m) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, it.unimi.dsi.fastutil.objects.Object2IntSortedMap
        public ObjectSet<Object2IntMap.Entry<K>> object2IntEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.unmodifiable(this.map.object2IntEntrySet());
            }
            return this.entries;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
        @Deprecated
        public ObjectSet<Map.Entry<K, Integer>> entrySet() {
            return object2IntEntrySet();
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
        public ObjectSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ObjectSets.unmodifiable(this.map.keySet());
            }
            return this.keys;
        }

        /* JADX WARN: Type inference failed for: r0v3, types: [it.unimi.dsi.fastutil.ints.IntCollection] */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
        /* renamed from: values */
        public Collection<Integer> values2() {
            if (this.values == null) {
                this.values = IntCollections.unmodifiable(this.map.values2());
            }
            return this.values;
        }

        @Override // java.util.Map
        public boolean isEmpty() {
            return this.map.isEmpty();
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunctions.UnmodifiableFunction, java.util.Map
        public int hashCode() {
            return this.map.hashCode();
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunctions.UnmodifiableFunction, java.util.Map
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            return this.map.equals(o);
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunctions.UnmodifiableFunction, it.unimi.dsi.fastutil.objects.Object2IntFunction
        public int getOrDefault(Object key, int defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
        public void forEach(BiConsumer<? super K, ? super Integer> action) {
            this.map.forEach(action);
        }

        @Override // java.util.Map
        public void replaceAll(BiFunction<? super K, ? super Integer, ? extends Integer> function) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
        public int putIfAbsent(K key, int value) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
        public boolean remove(Object key, int value) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
        public int replace(K key, int value) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
        public boolean replace(K key, int oldValue, int newValue) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
        public int computeIfAbsent(K key, ToIntFunction<? super K> mappingFunction) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
        public int computeIfAbsent(K key, Object2IntFunction<? super K> mappingFunction) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
        public int computeIntIfPresent(K key, BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
        public int computeInt(K key, BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
        public int merge(K key, int value, BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunctions.UnmodifiableFunction, it.unimi.dsi.fastutil.objects.Object2IntFunction, it.unimi.dsi.fastutil.Function
        @Deprecated
        public Integer getOrDefault(Object key, Integer defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
        @Deprecated
        public boolean remove(Object key, Object value) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
        @Deprecated
        public Integer replace(K key, Integer value) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
        @Deprecated
        public boolean replace(K key, Integer oldValue, Integer newValue) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
        @Deprecated
        public Integer putIfAbsent(K key, Integer value) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Map
        public Integer computeIfAbsent(K key, Function<? super K, ? extends Integer> mappingFunction) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Map
        public Integer computeIfPresent(K key, BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Map
        public Integer compute(K key, BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
        @Deprecated
        public Integer merge(K key, Integer value, BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }

    public static <K> Object2IntMap<K> unmodifiable(Object2IntMap<? extends K> m) {
        return new UnmodifiableMap(m);
    }
}
