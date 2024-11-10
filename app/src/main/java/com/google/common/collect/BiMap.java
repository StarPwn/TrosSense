package com.google.common.collect;

import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public interface BiMap<K, V> extends Map<K, V> {
    @Nullable
    V forcePut(@Nullable K k, @Nullable V v);

    BiMap<V, K> inverse();

    @Nullable
    V put(@Nullable K k, @Nullable V v);

    void putAll(Map<? extends K, ? extends V> map);

    @Override // com.google.common.collect.BiMap
    Set<V> values();
}
