package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Size64;
import java.util.SortedSet;

/* loaded from: classes4.dex */
public interface ObjectSortedSet<K> extends ObjectSet<K>, SortedSet<K>, ObjectBidirectionalIterable<K> {
    @Override // java.util.SortedSet
    ObjectSortedSet<K> headSet(K k);

    @Override // it.unimi.dsi.fastutil.objects.ObjectSet, it.unimi.dsi.fastutil.objects.ObjectCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectIterable
    ObjectBidirectionalIterator<K> iterator();

    ObjectBidirectionalIterator<K> iterator(K k);

    @Override // java.util.SortedSet
    ObjectSortedSet<K> subSet(K k, K k2);

    @Override // java.util.SortedSet
    ObjectSortedSet<K> tailSet(K k);

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.SortedSet
    /* bridge */ /* synthetic */ default SortedSet headSet(Object obj) {
        return headSet((ObjectSortedSet<K>) obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.SortedSet
    /* bridge */ /* synthetic */ default SortedSet tailSet(Object obj) {
        return tailSet((ObjectSortedSet<K>) obj);
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectSet, it.unimi.dsi.fastutil.objects.ObjectCollection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectIterable
    /* renamed from: spliterator */
    default ObjectSpliterator<K> mo221spliterator() {
        return ObjectSpliterators.asSpliteratorFromSorted(iterator(), Size64.sizeOf(this), 85, comparator());
    }
}
