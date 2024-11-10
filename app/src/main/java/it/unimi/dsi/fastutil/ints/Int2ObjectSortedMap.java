package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/* loaded from: classes4.dex */
public interface Int2ObjectSortedMap<V> extends Int2ObjectMap<V>, SortedMap<Integer, V> {

    /* loaded from: classes4.dex */
    public interface FastSortedEntrySet<V> extends ObjectSortedSet<Int2ObjectMap.Entry<V>>, Int2ObjectMap.FastEntrySet<V> {
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap.FastEntrySet
        ObjectBidirectionalIterator<Int2ObjectMap.Entry<V>> fastIterator();

        ObjectBidirectionalIterator<Int2ObjectMap.Entry<V>> fastIterator(Int2ObjectMap.Entry<V> entry);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.SortedMap
    Comparator<? super Integer> comparator();

    int firstIntKey();

    Int2ObjectSortedMap<V> headMap(int i);

    @Override // 
    ObjectSortedSet<Int2ObjectMap.Entry<V>> int2ObjectEntrySet();

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
    Set<Integer> keySet();

    int lastIntKey();

    Int2ObjectSortedMap<V> subMap(int i, int i2);

    Int2ObjectSortedMap<V> tailMap(int i);

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
    ObjectCollection<V> values();

    @Override // java.util.SortedMap
    @Deprecated
    default Int2ObjectSortedMap<V> subMap(Integer from, Integer to) {
        return subMap(from.intValue(), to.intValue());
    }

    @Override // java.util.SortedMap
    @Deprecated
    default Int2ObjectSortedMap<V> headMap(Integer to) {
        return headMap(to.intValue());
    }

    @Override // java.util.SortedMap
    @Deprecated
    default Int2ObjectSortedMap<V> tailMap(Integer from) {
        return tailMap(from.intValue());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.SortedMap
    @Deprecated
    default Integer firstKey() {
        return Integer.valueOf(firstIntKey());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.SortedMap
    @Deprecated
    default Integer lastKey() {
        return Integer.valueOf(lastIntKey());
    }

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
    @Deprecated
    default ObjectSortedSet<Map.Entry<Integer, V>> entrySet() {
        return int2ObjectEntrySet();
    }
}
