package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.IntBinaryOperator;
import java.util.function.ToIntFunction;

/* loaded from: classes4.dex */
public interface Object2IntMap<K> extends Object2IntFunction<K>, Map<K, Integer> {
    @Override // it.unimi.dsi.fastutil.Function
    boolean containsKey(Object obj);

    boolean containsValue(int i);

    @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
    int defaultReturnValue();

    @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
    void defaultReturnValue(int i);

    @Override // java.util.Map
    ObjectSet<K> keySet();

    ObjectSet<Entry<K>> object2IntEntrySet();

    @Override // it.unimi.dsi.fastutil.Function, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
    int size();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Map
    Collection<Integer> values();

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Map
    @Deprecated
    /* bridge */ /* synthetic */ default Integer merge(Object obj, Integer num, BiFunction<? super Integer, ? super Integer, ? extends Integer> biFunction) {
        return merge2((Object2IntMap<K>) obj, num, biFunction);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction, it.unimi.dsi.fastutil.Function
    @Deprecated
    /* bridge */ /* synthetic */ default Integer put(Object obj, Integer num) {
        return put((Object2IntMap<K>) obj, num);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Map
    @Deprecated
    /* bridge */ /* synthetic */ default Integer putIfAbsent(Object obj, Integer num) {
        return putIfAbsent2((Object2IntMap<K>) obj, num);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Map
    @Deprecated
    /* bridge */ /* synthetic */ default Integer replace(Object obj, Integer num) {
        return replace2((Object2IntMap<K>) obj, num);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Map
    @Deprecated
    /* bridge */ /* synthetic */ default boolean replace(Object obj, Integer num, Integer num2) {
        return replace2((Object2IntMap<K>) obj, num, num2);
    }

    /* loaded from: classes4.dex */
    public interface FastEntrySet<K> extends ObjectSet<Entry<K>> {
        ObjectIterator<Entry<K>> fastIterator();

        default void fastForEach(Consumer<? super Entry<K>> consumer) {
            forEach(consumer);
        }
    }

    @Override // it.unimi.dsi.fastutil.Function, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
    default void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    @Deprecated
    default ObjectSet<Map.Entry<K, Integer>> entrySet() {
        return object2IntEntrySet();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
    @Deprecated
    default Integer put(K key, Integer value) {
        return super.put2((Object2IntMap<K>) key, value);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction, it.unimi.dsi.fastutil.Function
    @Deprecated
    default Integer get(Object key) {
        return super.get(key);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction, it.unimi.dsi.fastutil.Function
    @Deprecated
    default Integer remove(Object key) {
        return super.remove(key);
    }

    @Override // java.util.Map
    @Deprecated
    default boolean containsValue(Object value) {
        if (value == null) {
            return false;
        }
        return containsValue(((Integer) value).intValue());
    }

    @Override // java.util.Map
    default void forEach(final BiConsumer<? super K, ? super Integer> consumer) {
        ObjectSet<Entry<K>> entrySet = object2IntEntrySet();
        Consumer<Entry<K>> wrappingConsumer = new Consumer() { // from class: it.unimi.dsi.fastutil.objects.Object2IntMap$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                consumer.accept(r2.getKey(), Integer.valueOf(((Object2IntMap.Entry) obj).getIntValue()));
            }
        };
        if (entrySet instanceof FastEntrySet) {
            ((FastEntrySet) entrySet).fastForEach(wrappingConsumer);
        } else {
            entrySet.forEach(wrappingConsumer);
        }
    }

    @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
    default int getOrDefault(Object key, int defaultValue) {
        int v = getInt(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction, it.unimi.dsi.fastutil.Function
    @Deprecated
    default Integer getOrDefault(Object key, Integer defaultValue) {
        return (Integer) super.getOrDefault(key, (Object) defaultValue);
    }

    default int putIfAbsent(K key, int value) {
        int v = getInt(key);
        int drv = defaultReturnValue();
        if (v != drv || containsKey(key)) {
            return v;
        }
        put((Object2IntMap<K>) key, value);
        return drv;
    }

    default boolean remove(Object key, int value) {
        int curValue = getInt(key);
        if (curValue != value) {
            return false;
        }
        if (curValue == defaultReturnValue() && !containsKey(key)) {
            return false;
        }
        removeInt(key);
        return true;
    }

    default boolean replace(K key, int oldValue, int newValue) {
        int curValue = getInt(key);
        if (curValue != oldValue) {
            return false;
        }
        if (curValue == defaultReturnValue() && !containsKey(key)) {
            return false;
        }
        put((Object2IntMap<K>) key, newValue);
        return true;
    }

    default int replace(K key, int value) {
        return containsKey(key) ? put((Object2IntMap<K>) key, value) : defaultReturnValue();
    }

    default int computeIfAbsent(K key, ToIntFunction<? super K> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        int v = getInt(key);
        if (v != defaultReturnValue() || containsKey(key)) {
            return v;
        }
        int newValue = mappingFunction.applyAsInt(key);
        put((Object2IntMap<K>) key, newValue);
        return newValue;
    }

    @Deprecated
    default int computeIntIfAbsent(K key, ToIntFunction<? super K> mappingFunction) {
        return computeIfAbsent((Object2IntMap<K>) key, (ToIntFunction<? super Object2IntMap<K>>) mappingFunction);
    }

    default int computeIfAbsent(K key, Object2IntFunction<? super K> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        int v = getInt(key);
        int drv = defaultReturnValue();
        if (v != drv || containsKey(key)) {
            return v;
        }
        if (!mappingFunction.containsKey(key)) {
            return drv;
        }
        int newValue = mappingFunction.getInt(key);
        put((Object2IntMap<K>) key, newValue);
        return newValue;
    }

    @Deprecated
    default int computeIntIfAbsentPartial(K key, Object2IntFunction<? super K> mappingFunction) {
        return computeIfAbsent((Object2IntMap<K>) key, (Object2IntFunction<? super Object2IntMap<K>>) mappingFunction);
    }

    default int computeIntIfPresent(K key, BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        int oldValue = getInt(key);
        int drv = defaultReturnValue();
        if (oldValue == drv && !containsKey(key)) {
            return drv;
        }
        Integer newValue = remappingFunction.apply(key, Integer.valueOf(oldValue));
        if (newValue == null) {
            removeInt(key);
            return drv;
        }
        int newVal = newValue.intValue();
        put((Object2IntMap<K>) key, newVal);
        return newVal;
    }

    default int computeInt(K key, BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        int oldValue = getInt(key);
        int drv = defaultReturnValue();
        boolean contained = oldValue != drv || containsKey(key);
        Integer newValue = remappingFunction.apply(key, contained ? Integer.valueOf(oldValue) : null);
        if (newValue == null) {
            if (contained) {
                removeInt(key);
            }
            return drv;
        }
        int newVal = newValue.intValue();
        put((Object2IntMap<K>) key, newVal);
        return newVal;
    }

    default int merge(K key, int value, BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        int newValue;
        Objects.requireNonNull(remappingFunction);
        int oldValue = getInt(key);
        int drv = defaultReturnValue();
        if (oldValue != drv || containsKey(key)) {
            Integer mergedValue = remappingFunction.apply(Integer.valueOf(oldValue), Integer.valueOf(value));
            if (mergedValue == null) {
                removeInt(key);
                return drv;
            }
            newValue = mergedValue.intValue();
        } else {
            newValue = value;
        }
        put((Object2IntMap<K>) key, newValue);
        return newValue;
    }

    default int mergeInt(K key, int value, IntBinaryOperator remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        int oldValue = getInt(key);
        int drv = defaultReturnValue();
        int newValue = (oldValue != drv || containsKey(key)) ? remappingFunction.applyAsInt(oldValue, value) : value;
        put((Object2IntMap<K>) key, newValue);
        return newValue;
    }

    default int mergeInt(K key, int value, it.unimi.dsi.fastutil.ints.IntBinaryOperator remappingFunction) {
        return mergeInt((Object2IntMap<K>) key, value, (IntBinaryOperator) remappingFunction);
    }

    @Deprecated
    default int mergeInt(K key, int value, BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        return merge((Object2IntMap<K>) key, value, remappingFunction);
    }

    @Deprecated
    /* renamed from: putIfAbsent, reason: avoid collision after fix types in other method */
    default Integer putIfAbsent2(K key, Integer value) {
        return (Integer) super.putIfAbsent((Object2IntMap<K>) key, (K) value);
    }

    @Override // java.util.Map
    @Deprecated
    default boolean remove(Object key, Object value) {
        return super.remove(key, value);
    }

    @Deprecated
    /* renamed from: replace, reason: avoid collision after fix types in other method */
    default boolean replace2(K key, Integer oldValue, Integer newValue) {
        return super.replace((Object2IntMap<K>) key, oldValue, newValue);
    }

    @Deprecated
    /* renamed from: replace, reason: avoid collision after fix types in other method */
    default Integer replace2(K key, Integer value) {
        return (Integer) super.replace((Object2IntMap<K>) key, (K) value);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Deprecated
    /* renamed from: merge, reason: avoid collision after fix types in other method */
    default Integer merge2(K key, Integer value, BiFunction<? super Integer, ? super Integer, ? extends Integer> biFunction) {
        return (Integer) super.merge((Object2IntMap<K>) key, (K) value, (BiFunction<? super K, ? super K, ? extends K>) biFunction);
    }

    /* loaded from: classes4.dex */
    public interface Entry<K> extends Map.Entry<K, Integer> {
        int getIntValue();

        int setValue(int i);

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Map.Entry
        @Deprecated
        default Integer getValue() {
            return Integer.valueOf(getIntValue());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Map.Entry
        @Deprecated
        default Integer setValue(Integer value) {
            return Integer.valueOf(setValue(value.intValue()));
        }
    }
}
