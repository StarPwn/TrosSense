package com.google.common.collect;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public interface ListMultimap<K, V> extends Multimap<K, V> {
    Map<K, Collection<V>> asMap();

    boolean equals(@Nullable Object obj);

    @Override // com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    List<V> get(@Nullable K k);

    @Override // 
    List<V> removeAll(@Nullable Object obj);

    @Override // com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    List<V> replaceValues(K k, Iterable<? extends V> iterable);

    /* JADX WARN: Multi-variable type inference failed */
    /* bridge */ /* synthetic */ default Collection get(@Nullable Object obj) {
        return get((ListMultimap<K, V>) obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* bridge */ /* synthetic */ default Collection replaceValues(Object obj, Iterable iterable) {
        return replaceValues((ListMultimap<K, V>) obj, iterable);
    }
}
