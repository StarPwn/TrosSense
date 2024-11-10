package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Size64;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/* loaded from: classes4.dex */
public interface LongCollection extends Collection<Long>, LongIterable {
    boolean add(long j);

    boolean addAll(LongCollection longCollection);

    boolean contains(long j);

    boolean containsAll(LongCollection longCollection);

    @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
    LongIterator iterator();

    boolean rem(long j);

    boolean removeAll(LongCollection longCollection);

    boolean retainAll(LongCollection longCollection);

    long[] toArray(long[] jArr);

    long[] toLongArray();

    @Override // it.unimi.dsi.fastutil.longs.LongIterable
    default LongIterator longIterator() {
        return iterator();
    }

    @Override // java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
    default LongSpliterator spliterator() {
        return LongSpliterators.asSpliterator(iterator(), Size64.sizeOf(this), 320);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongIterable
    default LongSpliterator longSpliterator() {
        return spliterator();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Collection, it.unimi.dsi.fastutil.longs.LongCollection
    @Deprecated
    default boolean add(Long key) {
        return add(key.longValue());
    }

    @Override // java.util.Collection, it.unimi.dsi.fastutil.longs.LongCollection
    @Deprecated
    default boolean contains(Object key) {
        if (key == null) {
            return false;
        }
        return contains(((Long) key).longValue());
    }

    @Override // java.util.Collection, it.unimi.dsi.fastutil.longs.LongCollection
    @Deprecated
    default boolean remove(Object key) {
        if (key == null) {
            return false;
        }
        return rem(((Long) key).longValue());
    }

    @Deprecated
    default long[] toLongArray(long[] a) {
        return toArray(a);
    }

    @Override // java.util.Collection
    @Deprecated
    default boolean removeIf(final Predicate<? super Long> filter) {
        return removeIf(filter instanceof java.util.function.LongPredicate ? (java.util.function.LongPredicate) filter : new java.util.function.LongPredicate() { // from class: it.unimi.dsi.fastutil.longs.LongCollection$$ExternalSyntheticLambda0
            @Override // java.util.function.LongPredicate
            public final boolean test(long j) {
                boolean test;
                test = filter.test(Long.valueOf(j));
                return test;
            }
        });
    }

    default boolean removeIf(java.util.function.LongPredicate filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        LongIterator each = iterator();
        while (each.hasNext()) {
            if (filter.test(each.nextLong())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }

    default boolean removeIf(LongPredicate filter) {
        return removeIf((java.util.function.LongPredicate) filter);
    }

    @Override // java.util.Collection
    @Deprecated
    default Stream<Long> stream() {
        return super.stream();
    }

    default LongStream longStream() {
        return StreamSupport.longStream(longSpliterator(), false);
    }

    @Override // java.util.Collection
    @Deprecated
    default Stream<Long> parallelStream() {
        return super.parallelStream();
    }

    default LongStream longParallelStream() {
        return StreamSupport.longStream(longSpliterator(), true);
    }
}
