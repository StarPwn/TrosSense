package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Size64;
import java.util.Set;

/* loaded from: classes4.dex */
public interface IntSet extends IntCollection, Set<Integer> {
    @Override // it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
    IntIterator iterator();

    boolean remove(int i);

    @Override // it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
    default IntSpliterator spliterator() {
        return IntSpliterators.asSpliterator(iterator(), Size64.sizeOf(this), 321);
    }

    @Override // it.unimi.dsi.fastutil.ints.IntCollection, java.util.Collection
    @Deprecated
    default boolean remove(Object o) {
        return super.remove(o);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.ints.IntCollection, java.util.Collection
    @Deprecated
    default boolean add(Integer o) {
        return super.add(o);
    }

    @Override // it.unimi.dsi.fastutil.ints.IntCollection, java.util.Collection
    @Deprecated
    default boolean contains(Object o) {
        return super.contains(o);
    }

    @Override // it.unimi.dsi.fastutil.ints.IntCollection
    @Deprecated
    default boolean rem(int k) {
        return remove(k);
    }

    static IntSet of() {
        return IntSets.UNMODIFIABLE_EMPTY_SET;
    }

    static IntSet of(int e) {
        return IntSets.singleton(e);
    }

    static IntSet of(int e0, int e1) {
        IntArraySet innerSet = new IntArraySet(2);
        innerSet.add(e0);
        if (!innerSet.add(e1)) {
            throw new IllegalArgumentException("Duplicate element: " + e1);
        }
        return IntSets.unmodifiable(innerSet);
    }

    static IntSet of(int e0, int e1, int e2) {
        IntArraySet innerSet = new IntArraySet(3);
        innerSet.add(e0);
        if (!innerSet.add(e1)) {
            throw new IllegalArgumentException("Duplicate element: " + e1);
        }
        if (!innerSet.add(e2)) {
            throw new IllegalArgumentException("Duplicate element: " + e2);
        }
        return IntSets.unmodifiable(innerSet);
    }

    static IntSet of(int... a) {
        switch (a.length) {
            case 0:
                return of();
            case 1:
                return of(a[0]);
            case 2:
                return of(a[0], a[1]);
            case 3:
                return of(a[0], a[1], a[2]);
            default:
                IntSet innerSet = a.length <= 4 ? new IntArraySet(a.length) : new IntOpenHashSet(a.length);
                for (int element : a) {
                    if (!innerSet.add(element)) {
                        throw new IllegalArgumentException("Duplicate element: " + element);
                    }
                }
                return IntSets.unmodifiable(innerSet);
        }
    }
}
