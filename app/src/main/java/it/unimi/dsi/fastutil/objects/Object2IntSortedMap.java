package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;

/* loaded from: classes4.dex */
public interface Object2IntSortedMap<K> extends Object2IntMap<K>, SortedMap<K, Integer> {

    /* loaded from: classes4.dex */
    public interface FastSortedEntrySet<K> extends ObjectSortedSet<Object2IntMap.Entry<K>>, Object2IntMap.FastEntrySet<K> {
        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap.FastEntrySet
        ObjectBidirectionalIterator<Object2IntMap.Entry<K>> fastIterator();

        ObjectBidirectionalIterator<Object2IntMap.Entry<K>> fastIterator(Object2IntMap.Entry<K> entry);
    }

    @Override // java.util.SortedMap
    Comparator<? super K> comparator();

    @Override // java.util.SortedMap
    Object2IntSortedMap<K> headMap(K k);

    @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
    ObjectSortedSet<K> keySet();

    @Override // 
    ObjectSortedSet<Object2IntMap.Entry<K>> object2IntEntrySet();

    @Override // java.util.SortedMap
    Object2IntSortedMap<K> subMap(K k, K k2);

    @Override // java.util.SortedMap
    Object2IntSortedMap<K> tailMap(K k);

    @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
    /* renamed from: values */
    Collection<Integer> values2();

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.SortedMap
    /* bridge */ /* synthetic */ default SortedMap headMap(Object obj) {
        return headMap((Object2IntSortedMap<K>) obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.SortedMap
    /* bridge */ /* synthetic */ default SortedMap tailMap(Object obj) {
        return tailMap((Object2IntSortedMap<K>) obj);
    }

    @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
    @Deprecated
    default ObjectSortedSet<Map.Entry<K, Integer>> entrySet() {
        return object2IntEntrySet();
    }
}
