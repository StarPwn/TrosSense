package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.UnmodifiableIterator;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class MapIteratorCache<K, V> {
    private final Map<K, V> backingMap;

    @Nullable
    private transient Map.Entry<K, V> entrySetCache;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MapIteratorCache(Map<K, V> backingMap) {
        this.backingMap = (Map) Preconditions.checkNotNull(backingMap);
    }

    public V put(@Nullable K key, @Nullable V value) {
        clearCache();
        return this.backingMap.put(key, value);
    }

    public V remove(@Nullable Object key) {
        clearCache();
        return this.backingMap.remove(key);
    }

    public void clear() {
        clearCache();
        this.backingMap.clear();
    }

    public V get(@Nullable Object key) {
        V value = getIfCached(key);
        return value != null ? value : getWithoutCaching(key);
    }

    public final V getWithoutCaching(@Nullable Object key) {
        return this.backingMap.get(key);
    }

    public final boolean containsKey(@Nullable Object key) {
        return getIfCached(key) != null || this.backingMap.containsKey(key);
    }

    public final Set<K> unmodifiableKeySet() {
        return new AbstractSet<K>() { // from class: com.google.common.graph.MapIteratorCache.1
            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public UnmodifiableIterator<K> iterator() {
                final Iterator<Map.Entry<K, V>> entryIterator = MapIteratorCache.this.backingMap.entrySet().iterator();
                return new UnmodifiableIterator<K>() { // from class: com.google.common.graph.MapIteratorCache.1.1
                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return entryIterator.hasNext();
                    }

                    @Override // java.util.Iterator
                    public K next() {
                        Map.Entry<K, V> entry = (Map.Entry) entryIterator.next();
                        MapIteratorCache.this.entrySetCache = entry;
                        return entry.getKey();
                    }
                };
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return MapIteratorCache.this.backingMap.size();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean contains(@Nullable Object key) {
                return MapIteratorCache.this.containsKey(key);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public V getIfCached(@Nullable Object key) {
        Map.Entry<K, V> entry = this.entrySetCache;
        if (entry != null && entry.getKey() == key) {
            return entry.getValue();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void clearCache() {
        this.entrySetCache = null;
    }
}
