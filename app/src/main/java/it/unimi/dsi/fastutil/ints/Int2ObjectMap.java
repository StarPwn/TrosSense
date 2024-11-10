package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.IntFunction;

/* loaded from: classes4.dex */
public interface Int2ObjectMap<V> extends Int2ObjectFunction<V>, Map<Integer, V> {
    boolean containsKey(int i);

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
    V defaultReturnValue();

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
    void defaultReturnValue(V v);

    ObjectSet<Entry<V>> int2ObjectEntrySet();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Map
    Set<Integer> keySet();

    @Override // java.util.Map
    int size();

    @Override // java.util.Map
    ObjectCollection<V> values();

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction, it.unimi.dsi.fastutil.Function
    @Deprecated
    /* bridge */ /* synthetic */ default Object put(Integer num, Object obj) {
        return put(num, (Integer) obj);
    }

    /* loaded from: classes4.dex */
    public interface FastEntrySet<V> extends ObjectSet<Entry<V>> {
        ObjectIterator<Entry<V>> fastIterator();

        default void fastForEach(Consumer<? super Entry<V>> consumer) {
            forEach(consumer);
        }
    }

    @Override // java.util.Map
    default void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    @Deprecated
    default ObjectSet<Map.Entry<Integer, V>> entrySet() {
        return int2ObjectEntrySet();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
    @Deprecated
    default V put(Integer num, V v) {
        return (V) super.put2(num, (Integer) v);
    }

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction, it.unimi.dsi.fastutil.Function
    @Deprecated
    default V get(Object obj) {
        return (V) super.get(obj);
    }

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction, it.unimi.dsi.fastutil.Function
    @Deprecated
    default V remove(Object obj) {
        return (V) super.remove(obj);
    }

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction, it.unimi.dsi.fastutil.Function
    @Deprecated
    default boolean containsKey(Object key) {
        return super.containsKey(key);
    }

    @Override // java.util.Map
    default void forEach(final BiConsumer<? super Integer, ? super V> consumer) {
        ObjectSet<Entry<V>> entrySet = int2ObjectEntrySet();
        Consumer<Entry<V>> wrappingConsumer = new Consumer() { // from class: it.unimi.dsi.fastutil.ints.Int2ObjectMap$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                consumer.accept(Integer.valueOf(r2.getIntKey()), ((Int2ObjectMap.Entry) obj).getValue());
            }
        };
        if (entrySet instanceof FastEntrySet) {
            ((FastEntrySet) entrySet).fastForEach(wrappingConsumer);
        } else {
            entrySet.forEach(wrappingConsumer);
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
    default V getOrDefault(int key, V defaultValue) {
        V v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction, it.unimi.dsi.fastutil.Function
    @Deprecated
    default V getOrDefault(Object obj, V v) {
        return (V) super.getOrDefault(obj, v);
    }

    default V putIfAbsent(int key, V value) {
        V v = get(key);
        V drv = defaultReturnValue();
        if (v != drv || containsKey(key)) {
            return v;
        }
        put(key, (int) value);
        return drv;
    }

    default boolean remove(int key, Object value) {
        V curValue = get(key);
        if (!Objects.equals(curValue, value)) {
            return false;
        }
        if (curValue == defaultReturnValue() && !containsKey(key)) {
            return false;
        }
        remove(key);
        return true;
    }

    default boolean replace(int key, V oldValue, V newValue) {
        V curValue = get(key);
        if (!Objects.equals(curValue, oldValue)) {
            return false;
        }
        if (curValue == defaultReturnValue() && !containsKey(key)) {
            return false;
        }
        put(key, (int) newValue);
        return true;
    }

    default V replace(int key, V value) {
        return containsKey(key) ? put(key, (int) value) : defaultReturnValue();
    }

    default V computeIfAbsent(int key, IntFunction<? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        V v = get(key);
        if (v != defaultReturnValue() || containsKey(key)) {
            return v;
        }
        V newValue = mappingFunction.apply(key);
        put(key, (int) newValue);
        return newValue;
    }

    default V computeIfAbsent(int key, Int2ObjectFunction<? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        V v = get(key);
        V drv = defaultReturnValue();
        if (v != drv || containsKey(key)) {
            return v;
        }
        if (!mappingFunction.containsKey(key)) {
            return drv;
        }
        V newValue = mappingFunction.get(key);
        put(key, (int) newValue);
        return newValue;
    }

    @Deprecated
    default V computeIfAbsentPartial(int key, Int2ObjectFunction<? extends V> mappingFunction) {
        return computeIfAbsent(key, (Int2ObjectFunction) mappingFunction);
    }

    default V computeIfPresent(int i, BiFunction<? super Integer, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        V v = get(i);
        V defaultReturnValue = defaultReturnValue();
        if (v == defaultReturnValue && !containsKey(i)) {
            return defaultReturnValue;
        }
        V apply = biFunction.apply(Integer.valueOf(i), v);
        if (apply == null) {
            remove(i);
            return defaultReturnValue;
        }
        put(i, (int) apply);
        return apply;
    }

    default V compute(int i, BiFunction<? super Integer, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        V v = get(i);
        V defaultReturnValue = defaultReturnValue();
        boolean z = v != defaultReturnValue || containsKey(i);
        V apply = biFunction.apply(Integer.valueOf(i), z ? (Object) v : null);
        if (apply == null) {
            if (z) {
                remove(i);
            }
            return defaultReturnValue;
        }
        put(i, (int) apply);
        return apply;
    }

    default V merge(int i, V v, BiFunction<? super V, ? super V, ? extends V> biFunction) {
        V apply;
        Objects.requireNonNull(biFunction);
        Objects.requireNonNull(v);
        V v2 = get(i);
        V defaultReturnValue = defaultReturnValue();
        if (v2 != defaultReturnValue || containsKey(i)) {
            apply = biFunction.apply(v2, v);
            if (apply == null) {
                remove(i);
                return defaultReturnValue;
            }
        } else {
            apply = v;
        }
        put(i, (int) apply);
        return apply;
    }

    /* loaded from: classes4.dex */
    public interface Entry<V> extends Map.Entry<Integer, V> {
        int getIntKey();

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Map.Entry
        @Deprecated
        default Integer getKey() {
            return Integer.valueOf(getIntKey());
        }
    }
}
