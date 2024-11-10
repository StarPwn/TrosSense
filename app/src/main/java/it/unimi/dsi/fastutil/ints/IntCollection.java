package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Size64;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/* loaded from: classes4.dex */
public interface IntCollection extends Collection<Integer>, IntIterable {
    boolean add(int i);

    boolean addAll(IntCollection intCollection);

    boolean contains(int i);

    boolean containsAll(IntCollection intCollection);

    @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
    IntIterator iterator();

    boolean rem(int i);

    boolean removeAll(IntCollection intCollection);

    boolean retainAll(IntCollection intCollection);

    int[] toArray(int[] iArr);

    int[] toIntArray();

    @Override // it.unimi.dsi.fastutil.ints.IntIterable
    default IntIterator intIterator() {
        return iterator();
    }

    @Override // java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
    default IntSpliterator spliterator() {
        return IntSpliterators.asSpliterator(iterator(), Size64.sizeOf(this), 320);
    }

    @Override // it.unimi.dsi.fastutil.ints.IntIterable
    default IntSpliterator intSpliterator() {
        return spliterator();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Collection, it.unimi.dsi.fastutil.ints.IntCollection
    @Deprecated
    default boolean add(Integer key) {
        return add(key.intValue());
    }

    @Override // java.util.Collection, it.unimi.dsi.fastutil.ints.IntCollection
    @Deprecated
    default boolean contains(Object key) {
        if (key == null) {
            return false;
        }
        return contains(((Integer) key).intValue());
    }

    @Override // java.util.Collection, it.unimi.dsi.fastutil.ints.IntCollection
    @Deprecated
    default boolean remove(Object key) {
        if (key == null) {
            return false;
        }
        return rem(((Integer) key).intValue());
    }

    @Deprecated
    default int[] toIntArray(int[] a) {
        return toArray(a);
    }

    @Override // java.util.Collection
    @Deprecated
    default boolean removeIf(final Predicate<? super Integer> filter) {
        return removeIf(filter instanceof java.util.function.IntPredicate ? (java.util.function.IntPredicate) filter : new java.util.function.IntPredicate() { // from class: it.unimi.dsi.fastutil.ints.IntCollection$$ExternalSyntheticLambda0
            @Override // java.util.function.IntPredicate
            public final boolean test(int i) {
                boolean test;
                test = filter.test(Integer.valueOf(i));
                return test;
            }
        });
    }

    default boolean removeIf(java.util.function.IntPredicate filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        IntIterator each = iterator();
        while (each.hasNext()) {
            if (filter.test(each.nextInt())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }

    default boolean removeIf(IntPredicate filter) {
        return removeIf((java.util.function.IntPredicate) filter);
    }

    @Override // java.util.Collection
    @Deprecated
    default Stream<Integer> stream() {
        return super.stream();
    }

    default IntStream intStream() {
        return StreamSupport.intStream(intSpliterator(), false);
    }

    @Override // java.util.Collection
    @Deprecated
    default Stream<Integer> parallelStream() {
        return super.parallelStream();
    }

    default IntStream intParallelStream() {
        return StreamSupport.intStream(intSpliterator(), true);
    }
}
