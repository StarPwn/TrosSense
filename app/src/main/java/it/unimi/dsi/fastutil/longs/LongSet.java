package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Size64;
import java.util.Set;

/* loaded from: classes4.dex */
public interface LongSet extends LongCollection, Set<Long> {
    @Override // it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
    LongIterator iterator();

    boolean remove(long j);

    @Override // it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
    default LongSpliterator spliterator() {
        return LongSpliterators.asSpliterator(iterator(), Size64.sizeOf(this), 321);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongCollection, java.util.Collection
    @Deprecated
    default boolean remove(Object o) {
        return super.remove(o);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.longs.LongCollection, java.util.Collection
    @Deprecated
    default boolean add(Long o) {
        return super.add(o);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongCollection, java.util.Collection
    @Deprecated
    default boolean contains(Object o) {
        return super.contains(o);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongCollection
    @Deprecated
    default boolean rem(long k) {
        return remove(k);
    }

    static LongSet of() {
        return LongSets.UNMODIFIABLE_EMPTY_SET;
    }

    static LongSet of(long e) {
        return LongSets.singleton(e);
    }

    static LongSet of(long e0, long e1) {
        LongArraySet innerSet = new LongArraySet(2);
        innerSet.add(e0);
        if (!innerSet.add(e1)) {
            throw new IllegalArgumentException("Duplicate element: " + e1);
        }
        return LongSets.unmodifiable(innerSet);
    }

    static LongSet of(long e0, long e1, long e2) {
        LongArraySet innerSet = new LongArraySet(3);
        innerSet.add(e0);
        if (!innerSet.add(e1)) {
            throw new IllegalArgumentException("Duplicate element: " + e1);
        }
        if (!innerSet.add(e2)) {
            throw new IllegalArgumentException("Duplicate element: " + e2);
        }
        return LongSets.unmodifiable(innerSet);
    }

    static LongSet of(long... a) {
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
                LongSet innerSet = a.length <= 4 ? new LongArraySet(a.length) : new LongOpenHashSet(a.length);
                for (long element : a) {
                    if (!innerSet.add(element)) {
                        throw new IllegalArgumentException("Duplicate element: " + element);
                    }
                }
                return LongSets.unmodifiable(innerSet);
        }
    }
}
