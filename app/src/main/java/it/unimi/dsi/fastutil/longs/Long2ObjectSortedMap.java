package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/* loaded from: classes4.dex */
public interface Long2ObjectSortedMap<V> extends Long2ObjectMap<V>, SortedMap<Long, V> {

    /* loaded from: classes4.dex */
    public interface FastSortedEntrySet<V> extends ObjectSortedSet<Long2ObjectMap.Entry<V>>, Long2ObjectMap.FastEntrySet<V> {
        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap.FastEntrySet
        ObjectBidirectionalIterator<Long2ObjectMap.Entry<V>> fastIterator();

        ObjectBidirectionalIterator<Long2ObjectMap.Entry<V>> fastIterator(Long2ObjectMap.Entry<V> entry);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.SortedMap
    Comparator<? super Long> comparator();

    long firstLongKey();

    Long2ObjectSortedMap<V> headMap(long j);

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap, java.util.Map
    Set<Long> keySet();

    long lastLongKey();

    @Override // 
    ObjectSortedSet<Long2ObjectMap.Entry<V>> long2ObjectEntrySet();

    Long2ObjectSortedMap<V> subMap(long j, long j2);

    Long2ObjectSortedMap<V> tailMap(long j);

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap, java.util.Map
    ObjectCollection<V> values();

    @Override // java.util.SortedMap
    @Deprecated
    default Long2ObjectSortedMap<V> subMap(Long from, Long to) {
        return subMap(from.longValue(), to.longValue());
    }

    @Override // java.util.SortedMap
    @Deprecated
    default Long2ObjectSortedMap<V> headMap(Long to) {
        return headMap(to.longValue());
    }

    @Override // java.util.SortedMap
    @Deprecated
    default Long2ObjectSortedMap<V> tailMap(Long from) {
        return tailMap(from.longValue());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.SortedMap
    @Deprecated
    default Long firstKey() {
        return Long.valueOf(firstLongKey());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.SortedMap
    @Deprecated
    default Long lastKey() {
        return Long.valueOf(lastLongKey());
    }

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap, java.util.Map
    @Deprecated
    default ObjectSortedSet<Map.Entry<Long, V>> entrySet() {
        return long2ObjectEntrySet();
    }
}
