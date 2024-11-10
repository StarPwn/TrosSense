package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public interface Multimap<K, V> {
    Map<K, Collection<V>> asMap();

    void clear();

    boolean containsEntry(@Nullable Object obj, @Nullable Object obj2);

    boolean containsKey(@Nullable Object obj);

    boolean containsValue(@Nullable Object obj);

    Collection<Map.Entry<K, V>> entries();

    boolean equals(@Nullable Object obj);

    Collection<V> get(@Nullable K k);

    int hashCode();

    boolean isEmpty();

    Set<K> keySet();

    Multiset<K> keys();

    boolean put(@Nullable K k, @Nullable V v);

    boolean putAll(Multimap<? extends K, ? extends V> multimap);

    boolean putAll(@Nullable K k, Iterable<? extends V> iterable);

    boolean remove(@Nullable Object obj, @Nullable Object obj2);

    Collection<V> removeAll(@Nullable Object obj);

    Collection<V> replaceValues(@Nullable K k, Iterable<? extends V> iterable);

    int size();

    Collection<V> values();

    default void forEach(final BiConsumer<? super K, ? super V> action) {
        Preconditions.checkNotNull(action);
        entries().forEach(new Consumer() { // from class: com.google.common.collect.Multimap$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                action.accept(r2.getKey(), ((Map.Entry) obj).getValue());
            }
        });
    }
}
