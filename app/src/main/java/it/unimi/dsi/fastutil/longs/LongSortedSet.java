package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Size64;
import java.util.Comparator;
import java.util.SortedSet;

/* loaded from: classes4.dex */
public interface LongSortedSet extends LongSet, SortedSet<Long>, LongBidirectionalIterable {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.SortedSet
    Comparator<? super Long> comparator();

    long firstLong();

    LongSortedSet headSet(long j);

    @Override // it.unimi.dsi.fastutil.longs.LongSet, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, java.util.Set
    LongBidirectionalIterator iterator();

    LongBidirectionalIterator iterator(long j);

    long lastLong();

    LongSortedSet subSet(long j, long j2);

    LongSortedSet tailSet(long j);

    /* JADX WARN: Type inference failed for: r4v0, types: [it.unimi.dsi.fastutil.longs.LongComparator] */
    @Override // it.unimi.dsi.fastutil.longs.LongSet, it.unimi.dsi.fastutil.longs.LongIterable, java.util.Set
    default LongSpliterator spliterator() {
        return LongSpliterators.asSpliteratorFromSorted(iterator(), Size64.sizeOf(this), 341, comparator());
    }

    @Override // java.util.SortedSet
    @Deprecated
    default LongSortedSet subSet(Long from, Long to) {
        return subSet(from.longValue(), to.longValue());
    }

    @Override // java.util.SortedSet
    @Deprecated
    default LongSortedSet headSet(Long to) {
        return headSet(to.longValue());
    }

    @Override // java.util.SortedSet
    @Deprecated
    default LongSortedSet tailSet(Long from) {
        return tailSet(from.longValue());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.SortedSet
    @Deprecated
    default Long first() {
        return Long.valueOf(firstLong());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.SortedSet
    @Deprecated
    default Long last() {
        return Long.valueOf(lastLong());
    }
}
