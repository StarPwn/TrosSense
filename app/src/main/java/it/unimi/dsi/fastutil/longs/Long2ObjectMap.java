package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.LongFunction;

/* loaded from: classes4.dex */
public interface Long2ObjectMap<V> extends Long2ObjectFunction<V>, Map<Long, V> {
    boolean containsKey(long j);

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunction
    V defaultReturnValue();

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunction
    void defaultReturnValue(V v);

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Map
    Set<Long> keySet();

    ObjectSet<Entry<V>> long2ObjectEntrySet();

    @Override // it.unimi.dsi.fastutil.Function, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
    int size();

    @Override // java.util.Map
    ObjectCollection<V> values();

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunction, it.unimi.dsi.fastutil.Function
    @Deprecated
    /* bridge */ /* synthetic */ default Object put(Long l, Object obj) {
        return put(l, (Long) obj);
    }

    /* loaded from: classes4.dex */
    public interface FastEntrySet<V> extends ObjectSet<Entry<V>> {
        ObjectIterator<Entry<V>> fastIterator();

        default void fastForEach(Consumer<? super Entry<V>> consumer) {
            forEach(consumer);
        }
    }

    @Override // it.unimi.dsi.fastutil.Function, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
    default void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    @Deprecated
    default ObjectSet<Map.Entry<Long, V>> entrySet() {
        return long2ObjectEntrySet();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunction
    @Deprecated
    default V put(Long l, V v) {
        return (V) super.put2(l, (Long) v);
    }

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunction, it.unimi.dsi.fastutil.Function
    @Deprecated
    default V get(Object obj) {
        return (V) super.get(obj);
    }

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunction, it.unimi.dsi.fastutil.Function
    @Deprecated
    default V remove(Object obj) {
        return (V) super.remove(obj);
    }

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunction, it.unimi.dsi.fastutil.Function
    @Deprecated
    default boolean containsKey(Object key) {
        return super.containsKey(key);
    }

    @Override // java.util.Map
    default void forEach(final BiConsumer<? super Long, ? super V> consumer) {
        ObjectSet<Entry<V>> entrySet = long2ObjectEntrySet();
        Consumer<Entry<V>> wrappingConsumer = new Consumer() { // from class: it.unimi.dsi.fastutil.longs.Long2ObjectMap$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                consumer.accept(Long.valueOf(r2.getLongKey()), ((Long2ObjectMap.Entry) obj).getValue());
            }
        };
        if (entrySet instanceof FastEntrySet) {
            ((FastEntrySet) entrySet).fastForEach(wrappingConsumer);
        } else {
            entrySet.forEach(wrappingConsumer);
        }
    }

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunction
    default V getOrDefault(long key, V defaultValue) {
        V v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunction, it.unimi.dsi.fastutil.Function
    @Deprecated
    default V getOrDefault(Object obj, V v) {
        return (V) super.getOrDefault(obj, v);
    }

    default V putIfAbsent(long key, V value) {
        V v = get(key);
        V drv = defaultReturnValue();
        if (v != drv || containsKey(key)) {
            return v;
        }
        put(key, (long) value);
        return drv;
    }

    default boolean remove(long key, Object value) {
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

    default boolean replace(long key, V oldValue, V newValue) {
        V curValue = get(key);
        if (!Objects.equals(curValue, oldValue)) {
            return false;
        }
        if (curValue == defaultReturnValue() && !containsKey(key)) {
            return false;
        }
        put(key, (long) newValue);
        return true;
    }

    default V replace(long key, V value) {
        return containsKey(key) ? put(key, (long) value) : defaultReturnValue();
    }

    default V computeIfAbsent(long key, LongFunction<? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        V v = get(key);
        if (v != defaultReturnValue() || containsKey(key)) {
            return v;
        }
        V newValue = mappingFunction.apply(key);
        put(key, (long) newValue);
        return newValue;
    }

    default V computeIfAbsent(long key, Long2ObjectFunction<? extends V> mappingFunction) {
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
        put(key, (long) newValue);
        return newValue;
    }

    @Deprecated
    default V computeIfAbsentPartial(long key, Long2ObjectFunction<? extends V> mappingFunction) {
        return computeIfAbsent(key, (Long2ObjectFunction) mappingFunction);
    }

    default V computeIfPresent(long j, BiFunction<? super Long, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        V v = get(j);
        V defaultReturnValue = defaultReturnValue();
        if (v == defaultReturnValue && !containsKey(j)) {
            return defaultReturnValue;
        }
        V apply = biFunction.apply(Long.valueOf(j), v);
        if (apply == null) {
            remove(j);
            return defaultReturnValue;
        }
        put(j, (long) apply);
        return apply;
    }

    default V compute(long j, BiFunction<? super Long, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        V v = get(j);
        V defaultReturnValue = defaultReturnValue();
        boolean z = v != defaultReturnValue || containsKey(j);
        V apply = biFunction.apply(Long.valueOf(j), z ? (Object) v : null);
        if (apply == null) {
            if (z) {
                remove(j);
            }
            return defaultReturnValue;
        }
        put(j, (long) apply);
        return apply;
    }

    default V merge(long j, V v, BiFunction<? super V, ? super V, ? extends V> biFunction) {
        V apply;
        Objects.requireNonNull(biFunction);
        Objects.requireNonNull(v);
        V v2 = get(j);
        V defaultReturnValue = defaultReturnValue();
        if (v2 != defaultReturnValue || containsKey(j)) {
            apply = biFunction.apply(v2, v);
            if (apply == null) {
                remove(j);
                return defaultReturnValue;
            }
        } else {
            apply = v;
        }
        put(j, (long) apply);
        return apply;
    }

    /* loaded from: classes4.dex */
    public interface Entry<V> extends Map.Entry<Long, V> {
        long getLongKey();

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Map.Entry
        @Deprecated
        default Long getKey() {
            return Long.valueOf(getLongKey());
        }
    }
}
