package com.google.common.collect;

import java.io.Serializable;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
class ImmutableEntry<K, V> extends AbstractMapEntry<K, V> implements Serializable {
    private static final long serialVersionUID = 0;
    final K key;
    final V value;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ImmutableEntry(@Nullable K key, @Nullable V value) {
        this.key = key;
        this.value = value;
    }

    @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
    @Nullable
    public final K getKey() {
        return this.key;
    }

    @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
    @Nullable
    public final V getValue() {
        return this.value;
    }

    @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
    public final V setValue(V value) {
        throw new UnsupportedOperationException();
    }
}
