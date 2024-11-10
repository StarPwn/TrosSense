package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.ints.IntCollections;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.RandomAccess;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import okhttp3.HttpUrl;

/* loaded from: classes4.dex */
public final class IntLists {
    public static final EmptyList EMPTY_LIST = new EmptyList();

    private IntLists() {
    }

    public static IntList shuffle(IntList l, Random random) {
        int p = l.size();
        while (true) {
            int i = p - 1;
            if (p != 0) {
                int p2 = random.nextInt(i + 1);
                int t = l.getInt(i);
                l.set(i, l.getInt(p2));
                l.set(p2, t);
                p = i;
            } else {
                return l;
            }
        }
    }

    /* loaded from: classes4.dex */
    public static class EmptyList extends IntCollections.EmptyCollection implements IntList, RandomAccess, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;

        protected EmptyList() {
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public int getInt(int i) {
            throw new IndexOutOfBoundsException();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public boolean rem(int k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public int removeInt(int i) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void add(int index, int k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public int set(int index, int k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public int indexOf(int k) {
            return -1;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public int lastIndexOf(int k) {
            return -1;
        }

        @Override // java.util.List
        public boolean addAll(int i, Collection<? extends Integer> c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        @Deprecated
        public void replaceAll(UnaryOperator<Integer> operator) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void replaceAll(java.util.function.IntUnaryOperator operator) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public boolean addAll(IntList c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public boolean addAll(int i, IntCollection c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public boolean addAll(int i, IntList c) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        @Deprecated
        public void add(int index, Integer k) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        @Deprecated
        public Integer get(int index) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection, it.unimi.dsi.fastutil.ints.IntCollection
        @Deprecated
        public boolean add(Integer k) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        @Deprecated
        public Integer set(int index, Integer k) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        @Deprecated
        public Integer remove(int k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        @Deprecated
        public int indexOf(Object k) {
            return -1;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        @Deprecated
        public int lastIndexOf(Object k) {
            return -1;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void sort(IntComparator comparator) {
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void unstableSort(IntComparator comparator) {
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        @Deprecated
        public void sort(Comparator<? super Integer> comparator) {
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        @Deprecated
        public void unstableSort(Comparator<? super Integer> comparator) {
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        public ListIterator<Integer> listIterator2() {
            return IntIterators.EMPTY_ITERATOR;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.EmptyCollection, it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
        public IntListIterator iterator() {
            return IntIterators.EMPTY_ITERATOR;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        /* renamed from: listIterator */
        public ListIterator<Integer> listIterator2(int i) {
            if (i == 0) {
                return IntIterators.EMPTY_ITERATOR;
            }
            throw new IndexOutOfBoundsException(String.valueOf(i));
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        /* renamed from: subList */
        public List<Integer> subList(int from, int to) {
            if (from == 0 && to == 0) {
                return this;
            }
            throw new IndexOutOfBoundsException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void getElements(int from, int[] a, int offset, int length) {
            if (from != 0 || length != 0 || offset < 0 || offset > a.length) {
                throw new IndexOutOfBoundsException();
            }
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void removeElements(int from, int to) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void addElements(int index, int[] a, int offset, int length) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void addElements(int index, int[] a) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void setElements(int[] a) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void setElements(int index, int[] a) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void setElements(int index, int[] a, int offset, int length) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void size(int s) {
            throw new UnsupportedOperationException();
        }

        @Override // java.lang.Comparable
        public int compareTo(List<? extends Integer> o) {
            return (o == this || o.isEmpty()) ? 0 : -1;
        }

        public Object clone() {
            return IntLists.EMPTY_LIST;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.EmptyCollection, java.util.Collection
        public int hashCode() {
            return 1;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.EmptyCollection, java.util.Collection
        public boolean equals(Object o) {
            return (o instanceof List) && ((List) o).isEmpty();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection
        public String toString() {
            return HttpUrl.PATH_SEGMENT_ENCODE_SET_URI;
        }

        private Object readResolve() {
            return IntLists.EMPTY_LIST;
        }
    }

    public static IntList emptyList() {
        return EMPTY_LIST;
    }

    /* loaded from: classes4.dex */
    public static class Singleton extends AbstractIntList implements RandomAccess, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        private final int element;

        protected Singleton(int element) {
            this.element = element;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public int getInt(int i) {
            if (i == 0) {
                return this.element;
            }
            throw new IndexOutOfBoundsException();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public boolean rem(int k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
        public int removeInt(int i) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public boolean contains(int k) {
            return k == this.element;
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
        public int indexOf(int k) {
            return k == this.element ? 0 : -1;
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public int[] toIntArray() {
            return new int[]{this.element};
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList, java.util.List
        public ListIterator<Integer> listIterator2() {
            return IntIterators.singleton(this.element);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [it.unimi.dsi.fastutil.ints.IntListIterator] */
        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
        public IntListIterator iterator() {
            return listIterator();
        }

        @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
        public IntSpliterator spliterator() {
            return IntSpliterators.singleton(this.element);
        }

        /* JADX WARN: Type inference failed for: r1v0, types: [java.util.ListIterator<java.lang.Integer>, it.unimi.dsi.fastutil.ints.IntListIterator] */
        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList, java.util.List
        /* renamed from: listIterator */
        public ListIterator<Integer> listIterator2(int i) {
            if (i > 1 || i < 0) {
                throw new IndexOutOfBoundsException();
            }
            ?? listIterator = listIterator();
            if (i == 1) {
                listIterator.nextInt();
            }
            return listIterator;
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList, java.util.List
        /* renamed from: subList */
        public List<Integer> subList(int from, int to) {
            ensureIndex(from);
            ensureIndex(to);
            if (from > to) {
                throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            if (from != 0 || to != 1) {
                return IntLists.EMPTY_LIST;
            }
            return this;
        }

        @Override // java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntIterable
        @Deprecated
        public void forEach(Consumer<? super Integer> action) {
            action.accept(Integer.valueOf(this.element));
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, java.util.List
        public boolean addAll(int i, Collection<? extends Integer> c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection
        public boolean addAll(Collection<? extends Integer> c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Collection, it.unimi.dsi.fastutil.ints.IntCollection
        @Deprecated
        public boolean removeIf(Predicate<? super Integer> filter) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        @Deprecated
        public void replaceAll(UnaryOperator<Integer> operator) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void replaceAll(java.util.function.IntUnaryOperator operator) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntIterable
        public void forEach(java.util.function.IntConsumer action) {
            action.accept(this.element);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public boolean addAll(IntList c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public boolean addAll(int i, IntList c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
        public boolean addAll(int i, IntCollection c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public boolean addAll(IntCollection c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public boolean removeAll(IntCollection c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public boolean retainAll(IntCollection c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollection
        public boolean removeIf(java.util.function.IntPredicate filter) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        @Deprecated
        public Object[] toArray() {
            return new Object[]{Integer.valueOf(this.element)};
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void sort(IntComparator comparator) {
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void unstableSort(IntComparator comparator) {
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        @Deprecated
        public void sort(Comparator<? super Integer> comparator) {
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        @Deprecated
        public void unstableSort(Comparator<? super Integer> comparator) {
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
        public void getElements(int from, int[] a, int offset, int length) {
            if (offset < 0) {
                throw new ArrayIndexOutOfBoundsException("Offset (" + offset + ") is negative");
            }
            if (offset + length > a.length) {
                throw new ArrayIndexOutOfBoundsException("End index (" + (offset + length) + ") is greater than array length (" + a.length + ")");
            }
            if (from + length > size()) {
                throw new IndexOutOfBoundsException("End index (" + (from + length) + ") is greater than list size (" + size() + ")");
            }
            if (length <= 0) {
                return;
            }
            a[offset] = this.element;
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
        public void removeElements(int from, int to) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
        public void addElements(int index, int[] a) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
        public void addElements(int index, int[] a, int offset, int length) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void setElements(int[] a) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void setElements(int index, int[] a) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
        public void setElements(int index, int[] a, int offset, int length) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return 1;
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
        public void size(int size) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            throw new UnsupportedOperationException();
        }

        public Object clone() {
            return this;
        }
    }

    public static IntList singleton(int element) {
        return new Singleton(element);
    }

    public static IntList singleton(Object element) {
        return new Singleton(((Integer) element).intValue());
    }

    /* loaded from: classes4.dex */
    public static class SynchronizedList extends IntCollections.SynchronizedCollection implements IntList, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final IntList list;

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ boolean add(int i) {
            return super.add(i);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection
        @Deprecated
        public /* bridge */ /* synthetic */ boolean add(Integer num) {
            return super.add(num);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ boolean addAll(IntCollection intCollection) {
            return super.addAll(intCollection);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean addAll(Collection<? extends Integer> collection) {
            return super.addAll(collection);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ void clear() {
            super.clear();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ boolean contains(int i) {
            return super.contains(i);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection, java.util.Collection
        @Deprecated
        public /* bridge */ /* synthetic */ boolean contains(Object obj) {
            return super.contains(obj);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ boolean containsAll(IntCollection intCollection) {
            return super.containsAll(intCollection);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean containsAll(Collection collection) {
            return super.containsAll((Collection<?>) collection);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntIterable
        public /* bridge */ /* synthetic */ void forEach(java.util.function.IntConsumer intConsumer) {
            super.forEach(intConsumer);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable
        public /* bridge */ /* synthetic */ IntIterator intIterator() {
            return super.intIterator();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ IntStream intParallelStream() {
            return super.intParallelStream();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable
        public /* bridge */ /* synthetic */ IntSpliterator intSpliterator() {
            return super.intSpliterator();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ IntStream intStream() {
            return super.intStream();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean isEmpty() {
            return super.isEmpty();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection, java.util.Collection
        @Deprecated
        public /* bridge */ /* synthetic */ Stream<Integer> parallelStream() {
            return super.parallelStream();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ boolean rem(int i) {
            return super.rem(i);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection, java.util.Collection
        @Deprecated
        public /* bridge */ /* synthetic */ boolean remove(Object obj) {
            return super.remove(obj);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ boolean removeAll(IntCollection intCollection) {
            return super.removeAll(intCollection);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean removeAll(Collection collection) {
            return super.removeAll((Collection<?>) collection);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ boolean retainAll(IntCollection intCollection) {
            return super.retainAll(intCollection);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean retainAll(Collection collection) {
            return super.retainAll((Collection<?>) collection);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ int size() {
            return super.size();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
        public /* bridge */ /* synthetic */ IntSpliterator spliterator() {
            return super.spliterator();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection, java.util.Collection
        @Deprecated
        public /* bridge */ /* synthetic */ Stream<Integer> stream() {
            return super.stream();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ int[] toArray(int[] iArr) {
            return super.toArray(iArr);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ Object[] toArray() {
            return super.toArray();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ Object[] toArray(Object[] objArr) {
            return super.toArray(objArr);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ int[] toIntArray() {
            return super.toIntArray();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection
        @Deprecated
        public /* bridge */ /* synthetic */ int[] toIntArray(int[] iArr) {
            return super.toIntArray(iArr);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection
        public /* bridge */ /* synthetic */ String toString() {
            return super.toString();
        }

        protected SynchronizedList(IntList l, Object sync) {
            super(l, sync);
            this.list = l;
        }

        protected SynchronizedList(IntList l) {
            super(l);
            this.list = l;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public int getInt(int i) {
            int i2;
            synchronized (this.sync) {
                i2 = this.list.getInt(i);
            }
            return i2;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public int set(int i, int k) {
            int i2;
            synchronized (this.sync) {
                i2 = this.list.set(i, k);
            }
            return i2;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void add(int i, int k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public int removeInt(int i) {
            int removeInt;
            synchronized (this.sync) {
                removeInt = this.list.removeInt(i);
            }
            return removeInt;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public int indexOf(int k) {
            int indexOf;
            synchronized (this.sync) {
                indexOf = this.list.indexOf(k);
            }
            return indexOf;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public int lastIndexOf(int k) {
            int lastIndexOf;
            synchronized (this.sync) {
                lastIndexOf = this.list.lastIndexOf(k);
            }
            return lastIndexOf;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public boolean removeIf(java.util.function.IntPredicate filter) {
            boolean removeIf;
            synchronized (this.sync) {
                removeIf = this.list.removeIf(filter);
            }
            return removeIf;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void replaceAll(java.util.function.IntUnaryOperator operator) {
            synchronized (this.sync) {
                this.list.replaceAll(operator);
            }
        }

        @Override // java.util.List
        public boolean addAll(int index, Collection<? extends Integer> c) {
            boolean addAll;
            synchronized (this.sync) {
                addAll = this.list.addAll(index, c);
            }
            return addAll;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void getElements(int from, int[] a, int offset, int length) {
            synchronized (this.sync) {
                this.list.getElements(from, a, offset, length);
            }
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void removeElements(int from, int to) {
            synchronized (this.sync) {
                this.list.removeElements(from, to);
            }
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void addElements(int index, int[] a, int offset, int length) {
            synchronized (this.sync) {
                this.list.addElements(index, a, offset, length);
            }
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void addElements(int index, int[] a) {
            synchronized (this.sync) {
                this.list.addElements(index, a);
            }
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void setElements(int[] a) {
            synchronized (this.sync) {
                this.list.setElements(a);
            }
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void setElements(int index, int[] a) {
            synchronized (this.sync) {
                this.list.setElements(index, a);
            }
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void setElements(int index, int[] a, int offset, int length) {
            synchronized (this.sync) {
                this.list.setElements(index, a, offset, length);
            }
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void size(int size) {
            synchronized (this.sync) {
                this.list.size(size);
            }
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        public ListIterator<Integer> listIterator2() {
            return this.list.listIterator();
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [it.unimi.dsi.fastutil.ints.IntListIterator] */
        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
        public IntListIterator iterator() {
            return listIterator();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        /* renamed from: listIterator */
        public ListIterator<Integer> listIterator2(int i) {
            return this.list.listIterator2(i);
        }

        /* JADX WARN: Type inference failed for: r2v1, types: [it.unimi.dsi.fastutil.ints.IntList] */
        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        /* renamed from: subList */
        public List<Integer> subList(int from, int to) {
            SynchronizedList synchronizedList;
            synchronized (this.sync) {
                synchronizedList = new SynchronizedList(this.list.subList2(from, to), this.sync);
            }
            return synchronizedList;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, java.util.Collection
        public boolean equals(Object o) {
            boolean equals;
            if (o == this) {
                return true;
            }
            synchronized (this.sync) {
                equals = this.collection.equals(o);
            }
            return equals;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, java.util.Collection
        public int hashCode() {
            int hashCode;
            synchronized (this.sync) {
                hashCode = this.collection.hashCode();
            }
            return hashCode;
        }

        @Override // java.lang.Comparable
        public int compareTo(List<? extends Integer> o) {
            int compareTo;
            synchronized (this.sync) {
                compareTo = this.list.compareTo(o);
            }
            return compareTo;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public boolean addAll(int index, IntCollection c) {
            boolean addAll;
            synchronized (this.sync) {
                addAll = this.list.addAll(index, c);
            }
            return addAll;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public boolean addAll(int index, IntList l) {
            boolean addAll;
            synchronized (this.sync) {
                addAll = this.list.addAll(index, l);
            }
            return addAll;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public boolean addAll(IntList l) {
            boolean addAll;
            synchronized (this.sync) {
                addAll = this.list.addAll(l);
            }
            return addAll;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        @Deprecated
        public Integer get(int i) {
            Integer num;
            synchronized (this.sync) {
                num = this.list.get(i);
            }
            return num;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        @Deprecated
        public void add(int i, Integer k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        @Deprecated
        public Integer set(int index, Integer k) {
            Integer num;
            synchronized (this.sync) {
                num = this.list.set(index, k);
            }
            return num;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        @Deprecated
        public Integer remove(int i) {
            Integer remove;
            synchronized (this.sync) {
                remove = this.list.remove(i);
            }
            return remove;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        @Deprecated
        public int indexOf(Object o) {
            int indexOf;
            synchronized (this.sync) {
                indexOf = this.list.indexOf(o);
            }
            return indexOf;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        @Deprecated
        public int lastIndexOf(Object o) {
            int lastIndexOf;
            synchronized (this.sync) {
                lastIndexOf = this.list.lastIndexOf(o);
            }
            return lastIndexOf;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void sort(IntComparator comparator) {
            synchronized (this.sync) {
                this.list.sort(comparator);
            }
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void unstableSort(IntComparator comparator) {
            synchronized (this.sync) {
                this.list.unstableSort(comparator);
            }
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        @Deprecated
        public void sort(Comparator<? super Integer> comparator) {
            synchronized (this.sync) {
                this.list.sort(comparator);
            }
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        @Deprecated
        public void unstableSort(Comparator<? super Integer> comparator) {
            synchronized (this.sync) {
                this.list.unstableSort(comparator);
            }
        }

        private void writeObject(ObjectOutputStream s) throws IOException {
            synchronized (this.sync) {
                s.defaultWriteObject();
            }
        }
    }

    /* loaded from: classes4.dex */
    public static class SynchronizedRandomAccessList extends SynchronizedList implements RandomAccess, Serializable {
        private static final long serialVersionUID = 0;

        protected SynchronizedRandomAccessList(IntList l, Object sync) {
            super(l, sync);
        }

        protected SynchronizedRandomAccessList(IntList l) {
            super(l);
        }

        /* JADX WARN: Type inference failed for: r2v1, types: [it.unimi.dsi.fastutil.ints.IntList] */
        @Override // it.unimi.dsi.fastutil.ints.IntLists.SynchronizedList, it.unimi.dsi.fastutil.ints.IntList, java.util.List
        /* renamed from: subList */
        public List<Integer> subList(int from, int to) {
            SynchronizedRandomAccessList synchronizedRandomAccessList;
            synchronized (this.sync) {
                synchronizedRandomAccessList = new SynchronizedRandomAccessList(this.list.subList2(from, to), this.sync);
            }
            return synchronizedRandomAccessList;
        }
    }

    public static IntList synchronize(IntList l) {
        return l instanceof RandomAccess ? new SynchronizedRandomAccessList(l) : new SynchronizedList(l);
    }

    public static IntList synchronize(IntList l, Object sync) {
        return l instanceof RandomAccess ? new SynchronizedRandomAccessList(l, sync) : new SynchronizedList(l, sync);
    }

    /* loaded from: classes4.dex */
    public static class UnmodifiableList extends IntCollections.UnmodifiableCollection implements IntList, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final IntList list;

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ boolean add(int i) {
            return super.add(i);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection
        @Deprecated
        public /* bridge */ /* synthetic */ boolean add(Integer num) {
            return super.add(num);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ boolean addAll(IntCollection intCollection) {
            return super.addAll(intCollection);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean addAll(Collection<? extends Integer> collection) {
            return super.addAll(collection);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ void clear() {
            super.clear();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ boolean contains(int i) {
            return super.contains(i);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection, java.util.Collection
        @Deprecated
        public /* bridge */ /* synthetic */ boolean contains(Object obj) {
            return super.contains(obj);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ boolean containsAll(IntCollection intCollection) {
            return super.containsAll(intCollection);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean containsAll(Collection collection) {
            return super.containsAll((Collection<?>) collection);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntIterable
        public /* bridge */ /* synthetic */ void forEach(java.util.function.IntConsumer intConsumer) {
            super.forEach(intConsumer);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable
        public /* bridge */ /* synthetic */ IntIterator intIterator() {
            return super.intIterator();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ IntStream intParallelStream() {
            return super.intParallelStream();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable
        public /* bridge */ /* synthetic */ IntSpliterator intSpliterator() {
            return super.intSpliterator();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ IntStream intStream() {
            return super.intStream();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean isEmpty() {
            return super.isEmpty();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection, java.util.Collection
        @Deprecated
        public /* bridge */ /* synthetic */ Stream<Integer> parallelStream() {
            return super.parallelStream();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ boolean rem(int i) {
            return super.rem(i);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection, java.util.Collection
        @Deprecated
        public /* bridge */ /* synthetic */ boolean remove(Object obj) {
            return super.remove(obj);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ boolean removeAll(IntCollection intCollection) {
            return super.removeAll(intCollection);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean removeAll(Collection collection) {
            return super.removeAll((Collection<?>) collection);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ boolean removeIf(java.util.function.IntPredicate intPredicate) {
            return super.removeIf(intPredicate);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ boolean retainAll(IntCollection intCollection) {
            return super.retainAll(intCollection);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean retainAll(Collection collection) {
            return super.retainAll((Collection<?>) collection);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ int size() {
            return super.size();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
        public /* bridge */ /* synthetic */ IntSpliterator spliterator() {
            return super.spliterator();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection, java.util.Collection
        @Deprecated
        public /* bridge */ /* synthetic */ Stream<Integer> stream() {
            return super.stream();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ int[] toArray(int[] iArr) {
            return super.toArray(iArr);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ Object[] toArray() {
            return super.toArray();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ Object[] toArray(Object[] objArr) {
            return super.toArray(objArr);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ int[] toIntArray() {
            return super.toIntArray();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection
        @Deprecated
        public /* bridge */ /* synthetic */ int[] toIntArray(int[] iArr) {
            return super.toIntArray(iArr);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection
        public /* bridge */ /* synthetic */ String toString() {
            return super.toString();
        }

        protected UnmodifiableList(IntList l) {
            super(l);
            this.list = l;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public int getInt(int i) {
            return this.list.getInt(i);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public int set(int i, int k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void add(int i, int k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public int removeInt(int i) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public int indexOf(int k) {
            return this.list.indexOf(k);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public int lastIndexOf(int k) {
            return this.list.lastIndexOf(k);
        }

        @Override // java.util.List
        public boolean addAll(int index, Collection<? extends Integer> c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        @Deprecated
        public void replaceAll(UnaryOperator<Integer> operator) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void getElements(int from, int[] a, int offset, int length) {
            this.list.getElements(from, a, offset, length);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void removeElements(int from, int to) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void addElements(int index, int[] a, int offset, int length) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void addElements(int index, int[] a) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void setElements(int[] a) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void setElements(int index, int[] a) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void setElements(int index, int[] a, int offset, int length) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void size(int size) {
            this.list.size(size);
        }

        /* JADX WARN: Type inference failed for: r0v1, types: [it.unimi.dsi.fastutil.ints.IntListIterator] */
        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        public ListIterator<Integer> listIterator2() {
            return IntIterators.unmodifiable((IntListIterator) this.list.listIterator());
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [it.unimi.dsi.fastutil.ints.IntListIterator] */
        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
        public IntListIterator iterator() {
            return listIterator();
        }

        /* JADX WARN: Type inference failed for: r0v1, types: [it.unimi.dsi.fastutil.ints.IntListIterator] */
        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        /* renamed from: listIterator */
        public ListIterator<Integer> listIterator2(int i) {
            return IntIterators.unmodifiable((IntListIterator) this.list.listIterator2(i));
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [it.unimi.dsi.fastutil.ints.IntList] */
        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        /* renamed from: subList */
        public List<Integer> subList(int from, int to) {
            return new UnmodifiableList(this.list.subList2(from, to));
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, java.util.Collection
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            return this.collection.equals(o);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, java.util.Collection
        public int hashCode() {
            return this.collection.hashCode();
        }

        @Override // java.lang.Comparable
        public int compareTo(List<? extends Integer> o) {
            return this.list.compareTo(o);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public boolean addAll(int index, IntCollection c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public boolean addAll(IntList l) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public boolean addAll(int index, IntList l) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void replaceAll(java.util.function.IntUnaryOperator operator) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        @Deprecated
        public Integer get(int i) {
            return this.list.get(i);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        @Deprecated
        public void add(int i, Integer k) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        @Deprecated
        public Integer set(int index, Integer k) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        @Deprecated
        public Integer remove(int i) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        @Deprecated
        public int indexOf(Object o) {
            return this.list.indexOf(o);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        @Deprecated
        public int lastIndexOf(Object o) {
            return this.list.lastIndexOf(o);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void sort(IntComparator comparator) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public void unstableSort(IntComparator comparator) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        @Deprecated
        public void sort(Comparator<? super Integer> comparator) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        @Deprecated
        public void unstableSort(Comparator<? super Integer> comparator) {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: classes4.dex */
    public static class UnmodifiableRandomAccessList extends UnmodifiableList implements RandomAccess, Serializable {
        private static final long serialVersionUID = 0;

        protected UnmodifiableRandomAccessList(IntList l) {
            super(l);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [it.unimi.dsi.fastutil.ints.IntList] */
        @Override // it.unimi.dsi.fastutil.ints.IntLists.UnmodifiableList, it.unimi.dsi.fastutil.ints.IntList, java.util.List
        /* renamed from: subList */
        public List<Integer> subList(int from, int to) {
            return new UnmodifiableRandomAccessList(this.list.subList2(from, to));
        }
    }

    public static IntList unmodifiable(IntList l) {
        return l instanceof RandomAccess ? new UnmodifiableRandomAccessList(l) : new UnmodifiableList(l);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static abstract class ImmutableListBase extends AbstractIntList implements IntList {
        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
        @Deprecated
        public final void add(int index, int k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
        @Deprecated
        public final boolean add(int k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection
        @Deprecated
        public final boolean addAll(Collection<? extends Integer> c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, java.util.List
        @Deprecated
        public final boolean addAll(int index, Collection<? extends Integer> c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
        @Deprecated
        public final int removeInt(int index) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
        @Deprecated
        public final boolean rem(int k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection
        @Deprecated
        public final boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection
        @Deprecated
        public final boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Collection, it.unimi.dsi.fastutil.ints.IntCollection
        @Deprecated
        public final boolean removeIf(Predicate<? super Integer> c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollection
        @Deprecated
        public final boolean removeIf(java.util.function.IntPredicate c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        @Deprecated
        public final void replaceAll(UnaryOperator<Integer> operator) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        @Deprecated
        public final void replaceAll(java.util.function.IntUnaryOperator operator) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        @Deprecated
        public final void add(int index, Integer k) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection, it.unimi.dsi.fastutil.ints.IntCollection
        @Deprecated
        public final boolean add(Integer k) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        @Deprecated
        public final Integer remove(int index) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection, it.unimi.dsi.fastutil.ints.IntCollection
        @Deprecated
        public final boolean remove(Object k) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        @Deprecated
        public final Integer set(int index, Integer k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
        @Deprecated
        public final boolean addAll(IntCollection c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        @Deprecated
        public final boolean addAll(IntList c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
        @Deprecated
        public final boolean addAll(int index, IntCollection c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        @Deprecated
        public final boolean addAll(int index, IntList c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
        @Deprecated
        public final boolean removeAll(IntCollection c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
        @Deprecated
        public final boolean retainAll(IntCollection c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
        @Deprecated
        public final int set(int index, int k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, java.util.AbstractCollection, java.util.Collection, java.util.List
        @Deprecated
        public final void clear() {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
        @Deprecated
        public final void size(int size) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
        @Deprecated
        public final void removeElements(int from, int to) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
        @Deprecated
        public final void addElements(int index, int[] a, int offset, int length) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
        @Deprecated
        public final void setElements(int index, int[] a, int offset, int length) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        @Deprecated
        public final void sort(IntComparator comp) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        @Deprecated
        public final void unstableSort(IntComparator comp) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
        @Deprecated
        public final void sort(Comparator<? super Integer> comparator) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        @Deprecated
        public final void unstableSort(Comparator<? super Integer> comparator) {
            throw new UnsupportedOperationException();
        }
    }
}
