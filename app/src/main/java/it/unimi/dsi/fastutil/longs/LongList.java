package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Size64;
import it.unimi.dsi.fastutil.longs.AbstractLongList;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.function.UnaryOperator;

/* loaded from: classes4.dex */
public interface LongList extends List<Long>, Comparable<List<? extends Long>>, LongCollection {
    void add(int i, long j);

    @Override // it.unimi.dsi.fastutil.longs.LongCollection
    boolean add(long j);

    boolean addAll(int i, LongCollection longCollection);

    void addElements(int i, long[] jArr);

    void addElements(int i, long[] jArr, int i2, int i3);

    void getElements(int i, long[] jArr, int i2, int i3);

    long getLong(int i);

    int indexOf(long j);

    @Override // java.util.List, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
    LongListIterator iterator();

    int lastIndexOf(long j);

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.List
    ListIterator<Long> listIterator();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.List
    ListIterator<Long> listIterator(int i);

    void removeElements(int i, int i2);

    long removeLong(int i);

    long set(int i, long j);

    void size(int i);

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.List
    List<Long> subList(int i, int i2);

    @Override // java.util.List, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
    default LongSpliterator spliterator() {
        if (this instanceof RandomAccess) {
            return new AbstractLongList.IndexBasedSpliterator(this, 0);
        }
        return LongSpliterators.asSpliterator(iterator(), Size64.sizeOf(this), 16720);
    }

    default void setElements(long[] a) {
        setElements(0, a);
    }

    default void setElements(int index, long[] a) {
        setElements(index, a, 0, a.length);
    }

    /* JADX WARN: Type inference failed for: r0v13, types: [it.unimi.dsi.fastutil.longs.LongListIterator] */
    default void setElements(int index, long[] a, int offset, int length) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is negative");
        }
        if (index > size()) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than list size (" + size() + ")");
        }
        LongArrays.ensureOffsetLength(a, offset, length);
        if (index + length > size()) {
            throw new IndexOutOfBoundsException("End index (" + (index + length) + ") is greater than list size (" + size() + ")");
        }
        ?? listIterator = listIterator(index);
        for (int i = 0; i < length; i++) {
            listIterator.nextLong();
            listIterator.set(a[i + offset]);
        }
    }

    @Override // java.util.List
    @Deprecated
    default void add(int index, Long key) {
        add(index, key.longValue());
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [it.unimi.dsi.fastutil.longs.LongListIterator] */
    default void replaceAll(java.util.function.LongUnaryOperator operator) {
        ?? listIterator = listIterator();
        while (listIterator.hasNext()) {
            listIterator.set(operator.applyAsLong(listIterator.nextLong()));
        }
    }

    default void replaceAll(LongUnaryOperator operator) {
        replaceAll((java.util.function.LongUnaryOperator) operator);
    }

    @Override // java.util.List
    @Deprecated
    default void replaceAll(final UnaryOperator<Long> operator) {
        java.util.function.LongUnaryOperator longUnaryOperator;
        Objects.requireNonNull(operator);
        if (operator instanceof java.util.function.LongUnaryOperator) {
            longUnaryOperator = (java.util.function.LongUnaryOperator) operator;
        } else {
            operator.getClass();
            longUnaryOperator = new java.util.function.LongUnaryOperator() { // from class: it.unimi.dsi.fastutil.longs.LongList$$ExternalSyntheticLambda0
                @Override // java.util.function.LongUnaryOperator
                public final long applyAsLong(long j) {
                    return ((Long) operator.apply(Long.valueOf(j))).longValue();
                }
            };
        }
        replaceAll(longUnaryOperator);
    }

    @Override // java.util.List, java.util.Collection, it.unimi.dsi.fastutil.longs.LongCollection
    @Deprecated
    default boolean contains(Object key) {
        return super.contains(key);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.List
    @Deprecated
    default Long get(int index) {
        return Long.valueOf(getLong(index));
    }

    @Override // java.util.List
    @Deprecated
    default int indexOf(Object o) {
        return indexOf(((Long) o).longValue());
    }

    @Override // java.util.List
    @Deprecated
    default int lastIndexOf(Object o) {
        return lastIndexOf(((Long) o).longValue());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.List, java.util.Collection, it.unimi.dsi.fastutil.longs.LongCollection
    @Deprecated
    default boolean add(Long k) {
        return add(k.longValue());
    }

    @Override // java.util.List, java.util.Collection, it.unimi.dsi.fastutil.longs.LongCollection
    @Deprecated
    default boolean remove(Object key) {
        return super.remove(key);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.List
    @Deprecated
    default Long remove(int index) {
        return Long.valueOf(removeLong(index));
    }

    @Override // java.util.List
    @Deprecated
    default Long set(int index, Long k) {
        return Long.valueOf(set(index, k.longValue()));
    }

    default boolean addAll(int index, LongList l) {
        return addAll(index, (LongCollection) l);
    }

    default boolean addAll(LongList l) {
        return addAll(size(), l);
    }

    static LongList of() {
        return LongImmutableList.of();
    }

    static LongList of(long e) {
        return LongLists.singleton(e);
    }

    static LongList of(long e0, long e1) {
        return LongImmutableList.of(e0, e1);
    }

    static LongList of(long e0, long e1, long e2) {
        return LongImmutableList.of(e0, e1, e2);
    }

    static LongList of(long... a) {
        switch (a.length) {
            case 0:
                return of();
            case 1:
                return of(a[0]);
            default:
                return LongImmutableList.of(a);
        }
    }

    @Override // java.util.List
    @Deprecated
    default void sort(Comparator<? super Long> comparator) {
        sort(LongComparators.asLongComparator(comparator));
    }

    default void sort(LongComparator comparator) {
        if (comparator == null) {
            unstableSort(comparator);
            return;
        }
        long[] elements = toLongArray();
        LongArrays.stableSort(elements, comparator);
        setElements(elements);
    }

    @Deprecated
    default void unstableSort(Comparator<? super Long> comparator) {
        unstableSort(LongComparators.asLongComparator(comparator));
    }

    default void unstableSort(LongComparator comparator) {
        long[] elements = toLongArray();
        if (comparator == null) {
            LongArrays.unstableSort(elements);
        } else {
            LongArrays.unstableSort(elements, comparator);
        }
        setElements(elements);
    }
}
