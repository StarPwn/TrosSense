package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Size64;
import java.util.Comparator;
import java.util.SortedSet;

/* loaded from: classes4.dex */
public interface IntSortedSet extends IntSet, SortedSet<Integer>, IntBidirectionalIterable {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.SortedSet
    Comparator<? super Integer> comparator();

    int firstInt();

    IntSortedSet headSet(int i);

    @Override // it.unimi.dsi.fastutil.ints.IntSet, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, java.util.Set
    IntBidirectionalIterator iterator();

    IntBidirectionalIterator iterator(int i);

    int lastInt();

    IntSortedSet subSet(int i, int i2);

    IntSortedSet tailSet(int i);

    /* JADX WARN: Type inference failed for: r4v0, types: [it.unimi.dsi.fastutil.ints.IntComparator] */
    @Override // it.unimi.dsi.fastutil.ints.IntSet, it.unimi.dsi.fastutil.ints.IntIterable, java.util.Set
    default IntSpliterator spliterator() {
        return IntSpliterators.asSpliteratorFromSorted(iterator(), Size64.sizeOf(this), 341, comparator());
    }

    @Override // java.util.SortedSet
    @Deprecated
    default IntSortedSet subSet(Integer from, Integer to) {
        return subSet(from.intValue(), to.intValue());
    }

    @Override // java.util.SortedSet
    @Deprecated
    default IntSortedSet headSet(Integer to) {
        return headSet(to.intValue());
    }

    @Override // java.util.SortedSet
    @Deprecated
    default IntSortedSet tailSet(Integer from) {
        return tailSet(from.intValue());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.SortedSet
    @Deprecated
    default Integer first() {
        return Integer.valueOf(firstInt());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.SortedSet
    @Deprecated
    default Integer last() {
        return Integer.valueOf(lastInt());
    }
}
