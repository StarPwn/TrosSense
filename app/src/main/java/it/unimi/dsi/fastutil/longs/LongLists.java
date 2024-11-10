package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.longs.LongCollections;
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
import java.util.stream.LongStream;
import java.util.stream.Stream;
import okhttp3.HttpUrl;

/* loaded from: classes4.dex */
public final class LongLists {
    public static final EmptyList EMPTY_LIST = new EmptyList();

    private LongLists() {
    }

    public static LongList shuffle(LongList l, Random random) {
        int p = l.size();
        while (true) {
            int i = p - 1;
            if (p != 0) {
                int p2 = random.nextInt(i + 1);
                long t = l.getLong(i);
                l.set(i, l.getLong(p2));
                l.set(p2, t);
                p = i;
            } else {
                return l;
            }
        }
    }

    /* loaded from: classes4.dex */
    public static class EmptyList extends LongCollections.EmptyCollection implements LongList, RandomAccess, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;

        protected EmptyList() {
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public long getLong(int i) {
            throw new IndexOutOfBoundsException();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public boolean rem(long k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public long removeLong(int i) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void add(int index, long k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public long set(int index, long k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public int indexOf(long k) {
            return -1;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public int lastIndexOf(long k) {
            return -1;
        }

        @Override // java.util.List
        public boolean addAll(int i, Collection<? extends Long> c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        @Deprecated
        public void replaceAll(UnaryOperator<Long> operator) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void replaceAll(java.util.function.LongUnaryOperator operator) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public boolean addAll(LongList c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public boolean addAll(int i, LongCollection c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public boolean addAll(int i, LongList c) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        @Deprecated
        public void add(int index, Long k) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        @Deprecated
        public Long get(int index) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection, java.util.Collection, it.unimi.dsi.fastutil.longs.LongCollection
        @Deprecated
        public boolean add(Long k) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        @Deprecated
        public Long set(int index, Long k) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        @Deprecated
        public Long remove(int k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        @Deprecated
        public int indexOf(Object k) {
            return -1;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        @Deprecated
        public int lastIndexOf(Object k) {
            return -1;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void sort(LongComparator comparator) {
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void unstableSort(LongComparator comparator) {
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        @Deprecated
        public void sort(Comparator<? super Long> comparator) {
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        @Deprecated
        public void unstableSort(Comparator<? super Long> comparator) {
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        public ListIterator<Long> listIterator2() {
            return LongIterators.EMPTY_ITERATOR;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.EmptyCollection, it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
        public LongListIterator iterator() {
            return LongIterators.EMPTY_ITERATOR;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        /* renamed from: listIterator */
        public ListIterator<Long> listIterator2(int i) {
            if (i == 0) {
                return LongIterators.EMPTY_ITERATOR;
            }
            throw new IndexOutOfBoundsException(String.valueOf(i));
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        /* renamed from: subList */
        public List<Long> subList(int from, int to) {
            if (from == 0 && to == 0) {
                return this;
            }
            throw new IndexOutOfBoundsException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void getElements(int from, long[] a, int offset, int length) {
            if (from != 0 || length != 0 || offset < 0 || offset > a.length) {
                throw new IndexOutOfBoundsException();
            }
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void removeElements(int from, int to) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void addElements(int index, long[] a, int offset, int length) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void addElements(int index, long[] a) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void setElements(long[] a) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void setElements(int index, long[] a) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void setElements(int index, long[] a, int offset, int length) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void size(int s) {
            throw new UnsupportedOperationException();
        }

        @Override // java.lang.Comparable
        public int compareTo(List<? extends Long> o) {
            return (o == this || o.isEmpty()) ? 0 : -1;
        }

        public Object clone() {
            return LongLists.EMPTY_LIST;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.EmptyCollection, java.util.Collection
        public int hashCode() {
            return 1;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.EmptyCollection, java.util.Collection
        public boolean equals(Object o) {
            return (o instanceof List) && ((List) o).isEmpty();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection
        public String toString() {
            return HttpUrl.PATH_SEGMENT_ENCODE_SET_URI;
        }

        private Object readResolve() {
            return LongLists.EMPTY_LIST;
        }
    }

    public static LongList emptyList() {
        return EMPTY_LIST;
    }

    /* loaded from: classes4.dex */
    public static class Singleton extends AbstractLongList implements RandomAccess, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        private final long element;

        protected Singleton(long element) {
            this.element = element;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public long getLong(int i) {
            if (i == 0) {
                return this.element;
            }
            throw new IndexOutOfBoundsException();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public boolean rem(long k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
        public long removeLong(int i) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public boolean contains(long k) {
            return k == this.element;
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
        public int indexOf(long k) {
            return k == this.element ? 0 : -1;
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public long[] toLongArray() {
            return new long[]{this.element};
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList, java.util.List
        public ListIterator<Long> listIterator2() {
            return LongIterators.singleton(this.element);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [it.unimi.dsi.fastutil.longs.LongListIterator] */
        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
        public LongListIterator iterator() {
            return listIterator();
        }

        @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
        public LongSpliterator spliterator() {
            return LongSpliterators.singleton(this.element);
        }

        /* JADX WARN: Type inference failed for: r1v0, types: [it.unimi.dsi.fastutil.longs.LongListIterator, java.util.ListIterator<java.lang.Long>] */
        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList, java.util.List
        /* renamed from: listIterator */
        public ListIterator<Long> listIterator2(int i) {
            if (i > 1 || i < 0) {
                throw new IndexOutOfBoundsException();
            }
            ?? listIterator = listIterator();
            if (i == 1) {
                listIterator.nextLong();
            }
            return listIterator;
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList, java.util.List
        /* renamed from: subList */
        public List<Long> subList(int from, int to) {
            ensureIndex(from);
            ensureIndex(to);
            if (from > to) {
                throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            if (from != 0 || to != 1) {
                return LongLists.EMPTY_LIST;
            }
            return this;
        }

        @Override // java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongIterable
        @Deprecated
        public void forEach(Consumer<? super Long> action) {
            action.accept(Long.valueOf(this.element));
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, java.util.List
        public boolean addAll(int i, Collection<? extends Long> c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection, java.util.Collection
        public boolean addAll(Collection<? extends Long> c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection, java.util.Collection
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection, java.util.Collection
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Collection, it.unimi.dsi.fastutil.longs.LongCollection
        @Deprecated
        public boolean removeIf(Predicate<? super Long> filter) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        @Deprecated
        public void replaceAll(UnaryOperator<Long> operator) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void replaceAll(java.util.function.LongUnaryOperator operator) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongIterable
        public void forEach(java.util.function.LongConsumer action) {
            action.accept(this.element);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public boolean addAll(LongList c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public boolean addAll(int i, LongList c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
        public boolean addAll(int i, LongCollection c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public boolean addAll(LongCollection c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public boolean removeAll(LongCollection c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public boolean retainAll(LongCollection c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection
        public boolean removeIf(java.util.function.LongPredicate filter) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        @Deprecated
        public Object[] toArray() {
            return new Object[]{Long.valueOf(this.element)};
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void sort(LongComparator comparator) {
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void unstableSort(LongComparator comparator) {
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        @Deprecated
        public void sort(Comparator<? super Long> comparator) {
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        @Deprecated
        public void unstableSort(Comparator<? super Long> comparator) {
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
        public void getElements(int from, long[] a, int offset, int length) {
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

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
        public void removeElements(int from, int to) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
        public void addElements(int index, long[] a) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
        public void addElements(int index, long[] a, int offset, int length) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void setElements(long[] a) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void setElements(int index, long[] a) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
        public void setElements(int index, long[] a, int offset, int length) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return 1;
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
        public void size(int size) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            throw new UnsupportedOperationException();
        }

        public Object clone() {
            return this;
        }
    }

    public static LongList singleton(long element) {
        return new Singleton(element);
    }

    public static LongList singleton(Object element) {
        return new Singleton(((Long) element).longValue());
    }

    /* loaded from: classes4.dex */
    public static class SynchronizedList extends LongCollections.SynchronizedCollection implements LongList, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final LongList list;

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ boolean add(long j) {
            return super.add(j);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongCollection
        @Deprecated
        public /* bridge */ /* synthetic */ boolean add(Long l) {
            return super.add(l);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ boolean addAll(LongCollection longCollection) {
            return super.addAll(longCollection);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean addAll(Collection<? extends Long> collection) {
            return super.addAll(collection);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ void clear() {
            super.clear();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ boolean contains(long j) {
            return super.contains(j);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongCollection, java.util.Collection
        @Deprecated
        public /* bridge */ /* synthetic */ boolean contains(Object obj) {
            return super.contains(obj);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ boolean containsAll(LongCollection longCollection) {
            return super.containsAll(longCollection);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean containsAll(Collection collection) {
            return super.containsAll((Collection<?>) collection);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongIterable
        public /* bridge */ /* synthetic */ void forEach(java.util.function.LongConsumer longConsumer) {
            super.forEach(longConsumer);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean isEmpty() {
            return super.isEmpty();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable
        public /* bridge */ /* synthetic */ LongIterator longIterator() {
            return super.longIterator();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ LongStream longParallelStream() {
            return super.longParallelStream();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable
        public /* bridge */ /* synthetic */ LongSpliterator longSpliterator() {
            return super.longSpliterator();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ LongStream longStream() {
            return super.longStream();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongCollection, java.util.Collection
        @Deprecated
        public /* bridge */ /* synthetic */ Stream<Long> parallelStream() {
            return super.parallelStream();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ boolean rem(long j) {
            return super.rem(j);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongCollection, java.util.Collection
        @Deprecated
        public /* bridge */ /* synthetic */ boolean remove(Object obj) {
            return super.remove(obj);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ boolean removeAll(LongCollection longCollection) {
            return super.removeAll(longCollection);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean removeAll(Collection collection) {
            return super.removeAll((Collection<?>) collection);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ boolean retainAll(LongCollection longCollection) {
            return super.retainAll(longCollection);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean retainAll(Collection collection) {
            return super.retainAll((Collection<?>) collection);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ int size() {
            return super.size();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongCollection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
        public /* bridge */ /* synthetic */ LongSpliterator spliterator() {
            return super.spliterator();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongCollection, java.util.Collection
        @Deprecated
        public /* bridge */ /* synthetic */ Stream<Long> stream() {
            return super.stream();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ long[] toArray(long[] jArr) {
            return super.toArray(jArr);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ Object[] toArray() {
            return super.toArray();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ Object[] toArray(Object[] objArr) {
            return super.toArray(objArr);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ long[] toLongArray() {
            return super.toLongArray();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongCollection
        @Deprecated
        public /* bridge */ /* synthetic */ long[] toLongArray(long[] jArr) {
            return super.toLongArray(jArr);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection
        public /* bridge */ /* synthetic */ String toString() {
            return super.toString();
        }

        protected SynchronizedList(LongList l, Object sync) {
            super(l, sync);
            this.list = l;
        }

        protected SynchronizedList(LongList l) {
            super(l);
            this.list = l;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public long getLong(int i) {
            long j;
            synchronized (this.sync) {
                j = this.list.getLong(i);
            }
            return j;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public long set(int i, long k) {
            long j;
            synchronized (this.sync) {
                j = this.list.set(i, k);
            }
            return j;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void add(int i, long k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public long removeLong(int i) {
            long removeLong;
            synchronized (this.sync) {
                removeLong = this.list.removeLong(i);
            }
            return removeLong;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public int indexOf(long k) {
            int indexOf;
            synchronized (this.sync) {
                indexOf = this.list.indexOf(k);
            }
            return indexOf;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public int lastIndexOf(long k) {
            int lastIndexOf;
            synchronized (this.sync) {
                lastIndexOf = this.list.lastIndexOf(k);
            }
            return lastIndexOf;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public boolean removeIf(java.util.function.LongPredicate filter) {
            boolean removeIf;
            synchronized (this.sync) {
                removeIf = this.list.removeIf(filter);
            }
            return removeIf;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void replaceAll(java.util.function.LongUnaryOperator operator) {
            synchronized (this.sync) {
                this.list.replaceAll(operator);
            }
        }

        @Override // java.util.List
        public boolean addAll(int index, Collection<? extends Long> c) {
            boolean addAll;
            synchronized (this.sync) {
                addAll = this.list.addAll(index, c);
            }
            return addAll;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void getElements(int from, long[] a, int offset, int length) {
            synchronized (this.sync) {
                this.list.getElements(from, a, offset, length);
            }
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void removeElements(int from, int to) {
            synchronized (this.sync) {
                this.list.removeElements(from, to);
            }
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void addElements(int index, long[] a, int offset, int length) {
            synchronized (this.sync) {
                this.list.addElements(index, a, offset, length);
            }
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void addElements(int index, long[] a) {
            synchronized (this.sync) {
                this.list.addElements(index, a);
            }
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void setElements(long[] a) {
            synchronized (this.sync) {
                this.list.setElements(a);
            }
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void setElements(int index, long[] a) {
            synchronized (this.sync) {
                this.list.setElements(index, a);
            }
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void setElements(int index, long[] a, int offset, int length) {
            synchronized (this.sync) {
                this.list.setElements(index, a, offset, length);
            }
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void size(int size) {
            synchronized (this.sync) {
                this.list.size(size);
            }
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        public ListIterator<Long> listIterator2() {
            return this.list.listIterator();
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [it.unimi.dsi.fastutil.longs.LongListIterator] */
        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
        public LongListIterator iterator() {
            return listIterator();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        /* renamed from: listIterator */
        public ListIterator<Long> listIterator2(int i) {
            return this.list.listIterator2(i);
        }

        /* JADX WARN: Type inference failed for: r2v1, types: [it.unimi.dsi.fastutil.longs.LongList] */
        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        /* renamed from: subList */
        public List<Long> subList(int from, int to) {
            SynchronizedList synchronizedList;
            synchronized (this.sync) {
                synchronizedList = new SynchronizedList(this.list.subList2(from, to), this.sync);
            }
            return synchronizedList;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, java.util.Collection
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

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, java.util.Collection
        public int hashCode() {
            int hashCode;
            synchronized (this.sync) {
                hashCode = this.collection.hashCode();
            }
            return hashCode;
        }

        @Override // java.lang.Comparable
        public int compareTo(List<? extends Long> o) {
            int compareTo;
            synchronized (this.sync) {
                compareTo = this.list.compareTo(o);
            }
            return compareTo;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public boolean addAll(int index, LongCollection c) {
            boolean addAll;
            synchronized (this.sync) {
                addAll = this.list.addAll(index, c);
            }
            return addAll;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public boolean addAll(int index, LongList l) {
            boolean addAll;
            synchronized (this.sync) {
                addAll = this.list.addAll(index, l);
            }
            return addAll;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public boolean addAll(LongList l) {
            boolean addAll;
            synchronized (this.sync) {
                addAll = this.list.addAll(l);
            }
            return addAll;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        @Deprecated
        public Long get(int i) {
            Long l;
            synchronized (this.sync) {
                l = this.list.get(i);
            }
            return l;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        @Deprecated
        public void add(int i, Long k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        @Deprecated
        public Long set(int index, Long k) {
            Long l;
            synchronized (this.sync) {
                l = this.list.set(index, k);
            }
            return l;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        @Deprecated
        public Long remove(int i) {
            Long remove;
            synchronized (this.sync) {
                remove = this.list.remove(i);
            }
            return remove;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        @Deprecated
        public int indexOf(Object o) {
            int indexOf;
            synchronized (this.sync) {
                indexOf = this.list.indexOf(o);
            }
            return indexOf;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        @Deprecated
        public int lastIndexOf(Object o) {
            int lastIndexOf;
            synchronized (this.sync) {
                lastIndexOf = this.list.lastIndexOf(o);
            }
            return lastIndexOf;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void sort(LongComparator comparator) {
            synchronized (this.sync) {
                this.list.sort(comparator);
            }
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void unstableSort(LongComparator comparator) {
            synchronized (this.sync) {
                this.list.unstableSort(comparator);
            }
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        @Deprecated
        public void sort(Comparator<? super Long> comparator) {
            synchronized (this.sync) {
                this.list.sort(comparator);
            }
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        @Deprecated
        public void unstableSort(Comparator<? super Long> comparator) {
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

        protected SynchronizedRandomAccessList(LongList l, Object sync) {
            super(l, sync);
        }

        protected SynchronizedRandomAccessList(LongList l) {
            super(l);
        }

        /* JADX WARN: Type inference failed for: r2v1, types: [it.unimi.dsi.fastutil.longs.LongList] */
        @Override // it.unimi.dsi.fastutil.longs.LongLists.SynchronizedList, it.unimi.dsi.fastutil.longs.LongList, java.util.List
        /* renamed from: subList */
        public List<Long> subList(int from, int to) {
            SynchronizedRandomAccessList synchronizedRandomAccessList;
            synchronized (this.sync) {
                synchronizedRandomAccessList = new SynchronizedRandomAccessList(this.list.subList2(from, to), this.sync);
            }
            return synchronizedRandomAccessList;
        }
    }

    public static LongList synchronize(LongList l) {
        return l instanceof RandomAccess ? new SynchronizedRandomAccessList(l) : new SynchronizedList(l);
    }

    public static LongList synchronize(LongList l, Object sync) {
        return l instanceof RandomAccess ? new SynchronizedRandomAccessList(l, sync) : new SynchronizedList(l, sync);
    }

    /* loaded from: classes4.dex */
    public static class UnmodifiableList extends LongCollections.UnmodifiableCollection implements LongList, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final LongList list;

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ boolean add(long j) {
            return super.add(j);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongCollection
        @Deprecated
        public /* bridge */ /* synthetic */ boolean add(Long l) {
            return super.add(l);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ boolean addAll(LongCollection longCollection) {
            return super.addAll(longCollection);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean addAll(Collection<? extends Long> collection) {
            return super.addAll(collection);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ void clear() {
            super.clear();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ boolean contains(long j) {
            return super.contains(j);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongCollection, java.util.Collection
        @Deprecated
        public /* bridge */ /* synthetic */ boolean contains(Object obj) {
            return super.contains(obj);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ boolean containsAll(LongCollection longCollection) {
            return super.containsAll(longCollection);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean containsAll(Collection collection) {
            return super.containsAll((Collection<?>) collection);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongIterable
        public /* bridge */ /* synthetic */ void forEach(java.util.function.LongConsumer longConsumer) {
            super.forEach(longConsumer);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean isEmpty() {
            return super.isEmpty();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable
        public /* bridge */ /* synthetic */ LongIterator longIterator() {
            return super.longIterator();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ LongStream longParallelStream() {
            return super.longParallelStream();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable
        public /* bridge */ /* synthetic */ LongSpliterator longSpliterator() {
            return super.longSpliterator();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ LongStream longStream() {
            return super.longStream();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongCollection, java.util.Collection
        @Deprecated
        public /* bridge */ /* synthetic */ Stream<Long> parallelStream() {
            return super.parallelStream();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ boolean rem(long j) {
            return super.rem(j);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongCollection, java.util.Collection
        @Deprecated
        public /* bridge */ /* synthetic */ boolean remove(Object obj) {
            return super.remove(obj);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ boolean removeAll(LongCollection longCollection) {
            return super.removeAll(longCollection);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean removeAll(Collection collection) {
            return super.removeAll((Collection<?>) collection);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ boolean removeIf(java.util.function.LongPredicate longPredicate) {
            return super.removeIf(longPredicate);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ boolean retainAll(LongCollection longCollection) {
            return super.retainAll(longCollection);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean retainAll(Collection collection) {
            return super.retainAll((Collection<?>) collection);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ int size() {
            return super.size();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongCollection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
        public /* bridge */ /* synthetic */ LongSpliterator spliterator() {
            return super.spliterator();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongCollection, java.util.Collection
        @Deprecated
        public /* bridge */ /* synthetic */ Stream<Long> stream() {
            return super.stream();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ long[] toArray(long[] jArr) {
            return super.toArray(jArr);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ Object[] toArray() {
            return super.toArray();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ Object[] toArray(Object[] objArr) {
            return super.toArray(objArr);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ long[] toLongArray() {
            return super.toLongArray();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongCollection
        @Deprecated
        public /* bridge */ /* synthetic */ long[] toLongArray(long[] jArr) {
            return super.toLongArray(jArr);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection
        public /* bridge */ /* synthetic */ String toString() {
            return super.toString();
        }

        protected UnmodifiableList(LongList l) {
            super(l);
            this.list = l;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public long getLong(int i) {
            return this.list.getLong(i);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public long set(int i, long k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void add(int i, long k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public long removeLong(int i) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public int indexOf(long k) {
            return this.list.indexOf(k);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public int lastIndexOf(long k) {
            return this.list.lastIndexOf(k);
        }

        @Override // java.util.List
        public boolean addAll(int index, Collection<? extends Long> c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        @Deprecated
        public void replaceAll(UnaryOperator<Long> operator) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void getElements(int from, long[] a, int offset, int length) {
            this.list.getElements(from, a, offset, length);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void removeElements(int from, int to) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void addElements(int index, long[] a, int offset, int length) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void addElements(int index, long[] a) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void setElements(long[] a) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void setElements(int index, long[] a) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void setElements(int index, long[] a, int offset, int length) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void size(int size) {
            this.list.size(size);
        }

        /* JADX WARN: Type inference failed for: r0v1, types: [it.unimi.dsi.fastutil.longs.LongListIterator] */
        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        public ListIterator<Long> listIterator2() {
            return LongIterators.unmodifiable((LongListIterator) this.list.listIterator());
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [it.unimi.dsi.fastutil.longs.LongListIterator] */
        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
        public LongListIterator iterator() {
            return listIterator();
        }

        /* JADX WARN: Type inference failed for: r0v1, types: [it.unimi.dsi.fastutil.longs.LongListIterator] */
        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        /* renamed from: listIterator */
        public ListIterator<Long> listIterator2(int i) {
            return LongIterators.unmodifiable((LongListIterator) this.list.listIterator2(i));
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [it.unimi.dsi.fastutil.longs.LongList] */
        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        /* renamed from: subList */
        public List<Long> subList(int from, int to) {
            return new UnmodifiableList(this.list.subList2(from, to));
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, java.util.Collection
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            return this.collection.equals(o);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, java.util.Collection
        public int hashCode() {
            return this.collection.hashCode();
        }

        @Override // java.lang.Comparable
        public int compareTo(List<? extends Long> o) {
            return this.list.compareTo(o);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public boolean addAll(int index, LongCollection c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public boolean addAll(LongList l) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public boolean addAll(int index, LongList l) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void replaceAll(java.util.function.LongUnaryOperator operator) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        @Deprecated
        public Long get(int i) {
            return this.list.get(i);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        @Deprecated
        public void add(int i, Long k) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        @Deprecated
        public Long set(int index, Long k) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        @Deprecated
        public Long remove(int i) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        @Deprecated
        public int indexOf(Object o) {
            return this.list.indexOf(o);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        @Deprecated
        public int lastIndexOf(Object o) {
            return this.list.lastIndexOf(o);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void sort(LongComparator comparator) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public void unstableSort(LongComparator comparator) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        @Deprecated
        public void sort(Comparator<? super Long> comparator) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        @Deprecated
        public void unstableSort(Comparator<? super Long> comparator) {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: classes4.dex */
    public static class UnmodifiableRandomAccessList extends UnmodifiableList implements RandomAccess, Serializable {
        private static final long serialVersionUID = 0;

        protected UnmodifiableRandomAccessList(LongList l) {
            super(l);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [it.unimi.dsi.fastutil.longs.LongList] */
        @Override // it.unimi.dsi.fastutil.longs.LongLists.UnmodifiableList, it.unimi.dsi.fastutil.longs.LongList, java.util.List
        /* renamed from: subList */
        public List<Long> subList(int from, int to) {
            return new UnmodifiableRandomAccessList(this.list.subList2(from, to));
        }
    }

    public static LongList unmodifiable(LongList l) {
        return l instanceof RandomAccess ? new UnmodifiableRandomAccessList(l) : new UnmodifiableList(l);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static abstract class ImmutableListBase extends AbstractLongList implements LongList {
        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
        @Deprecated
        public final void add(int index, long k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
        @Deprecated
        public final boolean add(long k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection, java.util.Collection
        @Deprecated
        public final boolean addAll(Collection<? extends Long> c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, java.util.List
        @Deprecated
        public final boolean addAll(int index, Collection<? extends Long> c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
        @Deprecated
        public final long removeLong(int index) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
        @Deprecated
        public final boolean rem(long k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection, java.util.Collection
        @Deprecated
        public final boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection, java.util.Collection
        @Deprecated
        public final boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Collection, it.unimi.dsi.fastutil.longs.LongCollection
        @Deprecated
        public final boolean removeIf(Predicate<? super Long> c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection
        @Deprecated
        public final boolean removeIf(java.util.function.LongPredicate c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        @Deprecated
        public final void replaceAll(UnaryOperator<Long> operator) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        @Deprecated
        public final void replaceAll(java.util.function.LongUnaryOperator operator) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        @Deprecated
        public final void add(int index, Long k) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection, java.util.Collection, it.unimi.dsi.fastutil.longs.LongCollection
        @Deprecated
        public final boolean add(Long k) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        @Deprecated
        public final Long remove(int index) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection, java.util.Collection, it.unimi.dsi.fastutil.longs.LongCollection
        @Deprecated
        public final boolean remove(Object k) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        @Deprecated
        public final Long set(int index, Long k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
        @Deprecated
        public final boolean addAll(LongCollection c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        @Deprecated
        public final boolean addAll(LongList c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
        @Deprecated
        public final boolean addAll(int index, LongCollection c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        @Deprecated
        public final boolean addAll(int index, LongList c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
        @Deprecated
        public final boolean removeAll(LongCollection c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
        @Deprecated
        public final boolean retainAll(LongCollection c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
        @Deprecated
        public final long set(int index, long k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, java.util.AbstractCollection, java.util.Collection, java.util.List
        @Deprecated
        public final void clear() {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
        @Deprecated
        public final void size(int size) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
        @Deprecated
        public final void removeElements(int from, int to) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
        @Deprecated
        public final void addElements(int index, long[] a, int offset, int length) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
        @Deprecated
        public final void setElements(int index, long[] a, int offset, int length) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        @Deprecated
        public final void sort(LongComparator comp) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        @Deprecated
        public final void unstableSort(LongComparator comp) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
        @Deprecated
        public final void sort(Comparator<? super Long> comparator) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        @Deprecated
        public final void unstableSort(Comparator<? super Long> comparator) {
            throw new UnsupportedOperationException();
        }
    }
}
