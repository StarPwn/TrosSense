package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Size64;
import it.unimi.dsi.fastutil.ints.AbstractIntList;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.function.UnaryOperator;

/* loaded from: classes4.dex */
public interface IntList extends List<Integer>, Comparable<List<? extends Integer>>, IntCollection {
    void add(int i, int i2);

    @Override // it.unimi.dsi.fastutil.ints.IntCollection
    boolean add(int i);

    boolean addAll(int i, IntCollection intCollection);

    void addElements(int i, int[] iArr);

    void addElements(int i, int[] iArr, int i2, int i3);

    void getElements(int i, int[] iArr, int i2, int i3);

    int getInt(int i);

    int indexOf(int i);

    @Override // java.util.List, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
    IntListIterator iterator();

    int lastIndexOf(int i);

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.List
    ListIterator<Integer> listIterator();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.List
    ListIterator<Integer> listIterator(int i);

    void removeElements(int i, int i2);

    int removeInt(int i);

    int set(int i, int i2);

    void size(int i);

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.List
    List<Integer> subList(int i, int i2);

    @Override // java.util.List, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
    default IntSpliterator spliterator() {
        if (this instanceof RandomAccess) {
            return new AbstractIntList.IndexBasedSpliterator(this, 0);
        }
        return IntSpliterators.asSpliterator(iterator(), Size64.sizeOf(this), 16720);
    }

    default void setElements(int[] a) {
        setElements(0, a);
    }

    default void setElements(int index, int[] a) {
        setElements(index, a, 0, a.length);
    }

    /* JADX WARN: Type inference failed for: r0v13, types: [it.unimi.dsi.fastutil.ints.IntListIterator] */
    default void setElements(int index, int[] a, int offset, int length) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is negative");
        }
        if (index > size()) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than list size (" + size() + ")");
        }
        IntArrays.ensureOffsetLength(a, offset, length);
        if (index + length > size()) {
            throw new IndexOutOfBoundsException("End index (" + (index + length) + ") is greater than list size (" + size() + ")");
        }
        ?? listIterator = listIterator(index);
        for (int i = 0; i < length; i++) {
            listIterator.nextInt();
            listIterator.set(a[i + offset]);
        }
    }

    @Override // java.util.List
    @Deprecated
    default void add(int index, Integer key) {
        add(index, key.intValue());
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [it.unimi.dsi.fastutil.ints.IntListIterator] */
    default void replaceAll(java.util.function.IntUnaryOperator operator) {
        ?? listIterator = listIterator();
        while (listIterator.hasNext()) {
            listIterator.set(operator.applyAsInt(listIterator.nextInt()));
        }
    }

    default void replaceAll(IntUnaryOperator operator) {
        replaceAll((java.util.function.IntUnaryOperator) operator);
    }

    @Override // java.util.List
    @Deprecated
    default void replaceAll(final UnaryOperator<Integer> operator) {
        java.util.function.IntUnaryOperator intUnaryOperator;
        Objects.requireNonNull(operator);
        if (operator instanceof java.util.function.IntUnaryOperator) {
            intUnaryOperator = (java.util.function.IntUnaryOperator) operator;
        } else {
            operator.getClass();
            intUnaryOperator = new java.util.function.IntUnaryOperator() { // from class: it.unimi.dsi.fastutil.ints.IntList$$ExternalSyntheticLambda0
                @Override // java.util.function.IntUnaryOperator
                public final int applyAsInt(int i) {
                    return ((Integer) operator.apply(Integer.valueOf(i))).intValue();
                }
            };
        }
        replaceAll(intUnaryOperator);
    }

    @Override // java.util.List, java.util.Collection, it.unimi.dsi.fastutil.ints.IntCollection
    @Deprecated
    default boolean contains(Object key) {
        return super.contains(key);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.List
    @Deprecated
    default Integer get(int index) {
        return Integer.valueOf(getInt(index));
    }

    @Override // java.util.List
    @Deprecated
    default int indexOf(Object o) {
        return indexOf(((Integer) o).intValue());
    }

    @Override // java.util.List
    @Deprecated
    default int lastIndexOf(Object o) {
        return lastIndexOf(((Integer) o).intValue());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.List, java.util.Collection, it.unimi.dsi.fastutil.ints.IntCollection
    @Deprecated
    default boolean add(Integer k) {
        return add(k.intValue());
    }

    @Override // java.util.List, java.util.Collection, it.unimi.dsi.fastutil.ints.IntCollection
    @Deprecated
    default boolean remove(Object key) {
        return super.remove(key);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.List
    @Deprecated
    default Integer remove(int index) {
        return Integer.valueOf(removeInt(index));
    }

    @Override // java.util.List
    @Deprecated
    default Integer set(int index, Integer k) {
        return Integer.valueOf(set(index, k.intValue()));
    }

    default boolean addAll(int index, IntList l) {
        return addAll(index, (IntCollection) l);
    }

    default boolean addAll(IntList l) {
        return addAll(size(), l);
    }

    static IntList of() {
        return IntImmutableList.of();
    }

    static IntList of(int e) {
        return IntLists.singleton(e);
    }

    static IntList of(int e0, int e1) {
        return IntImmutableList.of(e0, e1);
    }

    static IntList of(int e0, int e1, int e2) {
        return IntImmutableList.of(e0, e1, e2);
    }

    static IntList of(int... a) {
        switch (a.length) {
            case 0:
                return of();
            case 1:
                return of(a[0]);
            default:
                return IntImmutableList.of(a);
        }
    }

    @Override // java.util.List
    @Deprecated
    default void sort(Comparator<? super Integer> comparator) {
        sort(IntComparators.asIntComparator(comparator));
    }

    default void sort(IntComparator comparator) {
        if (comparator == null) {
            unstableSort(comparator);
            return;
        }
        int[] elements = toIntArray();
        IntArrays.stableSort(elements, comparator);
        setElements(elements);
    }

    @Deprecated
    default void unstableSort(Comparator<? super Integer> comparator) {
        unstableSort(IntComparators.asIntComparator(comparator));
    }

    default void unstableSort(IntComparator comparator) {
        int[] elements = toIntArray();
        if (comparator == null) {
            IntArrays.unstableSort(elements);
        } else {
            IntArrays.unstableSort(elements, comparator);
        }
        setElements(elements);
    }
}
