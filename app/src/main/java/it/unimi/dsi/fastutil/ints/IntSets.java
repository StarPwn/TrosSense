package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.ints.IntCollections;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/* loaded from: classes4.dex */
public final class IntSets {
    static final int ARRAY_SET_CUTOFF = 4;
    public static final EmptySet EMPTY_SET = new EmptySet();
    static final IntSet UNMODIFIABLE_EMPTY_SET = unmodifiable(new IntArraySet(IntArrays.EMPTY_ARRAY));

    private IntSets() {
    }

    /* loaded from: classes4.dex */
    public static class EmptySet extends IntCollections.EmptyCollection implements IntSet, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;

        @Override // it.unimi.dsi.fastutil.ints.IntSet
        public boolean remove(int ok) {
            throw new UnsupportedOperationException();
        }

        public Object clone() {
            return IntSets.EMPTY_SET;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.EmptyCollection, java.util.Collection
        public boolean equals(Object o) {
            return (o instanceof Set) && ((Set) o).isEmpty();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
        @Deprecated
        public boolean rem(int k) {
            return super.rem(k);
        }

        private Object readResolve() {
            return IntSets.EMPTY_SET;
        }
    }

    public static IntSet emptySet() {
        return EMPTY_SET;
    }

    /* loaded from: classes4.dex */
    public static class Singleton extends AbstractIntSet implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final int element;

        /* JADX INFO: Access modifiers changed from: protected */
        public Singleton(int element) {
            this.element = element;
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public boolean contains(int k) {
            return k == this.element;
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntSet, it.unimi.dsi.fastutil.ints.IntSet
        public boolean remove(int k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntSet, it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
        public IntListIterator iterator() {
            return IntIterators.singleton(this.element);
        }

        @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
        public IntSpliterator spliterator() {
            return IntSpliterators.singleton(this.element);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return 1;
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public int[] toIntArray() {
            return new int[]{this.element};
        }

        @Override // java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntIterable
        @Deprecated
        public void forEach(Consumer<? super Integer> action) {
            action.accept(Integer.valueOf(this.element));
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection
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

        @Override // it.unimi.dsi.fastutil.ints.IntIterable
        public void forEach(java.util.function.IntConsumer action) {
            action.accept(this.element);
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
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

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        @Deprecated
        public Object[] toArray() {
            return new Object[]{Integer.valueOf(this.element)};
        }

        public Object clone() {
            return this;
        }
    }

    public static IntSet singleton(int element) {
        return new Singleton(element);
    }

    public static IntSet singleton(Integer element) {
        return new Singleton(element.intValue());
    }

    /* loaded from: classes4.dex */
    public static class SynchronizedSet extends IntCollections.SynchronizedCollection implements IntSet, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ boolean add(int x0) {
            return super.add(x0);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection
        @Deprecated
        public /* bridge */ /* synthetic */ boolean add(Integer x0) {
            return super.add(x0);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ boolean addAll(IntCollection x0) {
            return super.addAll(x0);
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
        public /* bridge */ /* synthetic */ boolean contains(int x0) {
            return super.contains(x0);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection, java.util.Collection
        @Deprecated
        public /* bridge */ /* synthetic */ boolean contains(Object x0) {
            return super.contains(x0);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ boolean containsAll(IntCollection x0) {
            return super.containsAll(x0);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean containsAll(Collection x0) {
            return super.containsAll((Collection<?>) x0);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean equals(Object x0) {
            return super.equals(x0);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntIterable
        public /* bridge */ /* synthetic */ void forEach(java.util.function.IntConsumer x0) {
            super.forEach(x0);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ int hashCode() {
            return super.hashCode();
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

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
        public /* bridge */ /* synthetic */ IntIterator iterator() {
            return super.iterator();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection, java.util.Collection
        @Deprecated
        public /* bridge */ /* synthetic */ Stream<Integer> parallelStream() {
            return super.parallelStream();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection, java.util.Collection
        @Deprecated
        public /* bridge */ /* synthetic */ boolean remove(Object x0) {
            return super.remove(x0);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ boolean removeAll(IntCollection x0) {
            return super.removeAll(x0);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean removeAll(Collection x0) {
            return super.removeAll((Collection<?>) x0);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ boolean removeIf(java.util.function.IntPredicate x0) {
            return super.removeIf(x0);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ boolean retainAll(IntCollection x0) {
            return super.retainAll(x0);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean retainAll(Collection x0) {
            return super.retainAll((Collection<?>) x0);
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
        public /* bridge */ /* synthetic */ int[] toArray(int[] x0) {
            return super.toArray(x0);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ Object[] toArray() {
            return super.toArray();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ Object[] toArray(Object[] x0) {
            return super.toArray(x0);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ int[] toIntArray() {
            return super.toIntArray();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection
        @Deprecated
        public /* bridge */ /* synthetic */ int[] toIntArray(int[] x0) {
            return super.toIntArray(x0);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection
        public /* bridge */ /* synthetic */ String toString() {
            return super.toString();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public SynchronizedSet(IntSet s, Object sync) {
            super(s, sync);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public SynchronizedSet(IntSet s) {
            super(s);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSet
        public boolean remove(int k) {
            boolean rem;
            synchronized (this.sync) {
                rem = this.collection.rem(k);
            }
            return rem;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection
        @Deprecated
        public boolean rem(int k) {
            return super.rem(k);
        }
    }

    public static IntSet synchronize(IntSet s) {
        return new SynchronizedSet(s);
    }

    public static IntSet synchronize(IntSet s, Object sync) {
        return new SynchronizedSet(s, sync);
    }

    /* loaded from: classes4.dex */
    public static class UnmodifiableSet extends IntCollections.UnmodifiableCollection implements IntSet, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ boolean add(int x0) {
            return super.add(x0);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection
        @Deprecated
        public /* bridge */ /* synthetic */ boolean add(Integer x0) {
            return super.add(x0);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ boolean addAll(IntCollection x0) {
            return super.addAll(x0);
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
        public /* bridge */ /* synthetic */ boolean contains(int x0) {
            return super.contains(x0);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection, java.util.Collection
        @Deprecated
        public /* bridge */ /* synthetic */ boolean contains(Object x0) {
            return super.contains(x0);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ boolean containsAll(IntCollection x0) {
            return super.containsAll(x0);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean containsAll(Collection x0) {
            return super.containsAll((Collection<?>) x0);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntIterable
        public /* bridge */ /* synthetic */ void forEach(java.util.function.IntConsumer x0) {
            super.forEach(x0);
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

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
        public /* bridge */ /* synthetic */ IntIterator iterator() {
            return super.iterator();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection, java.util.Collection
        @Deprecated
        public /* bridge */ /* synthetic */ Stream<Integer> parallelStream() {
            return super.parallelStream();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection, java.util.Collection
        @Deprecated
        public /* bridge */ /* synthetic */ boolean remove(Object x0) {
            return super.remove(x0);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ boolean removeAll(IntCollection x0) {
            return super.removeAll(x0);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean removeAll(Collection x0) {
            return super.removeAll((Collection<?>) x0);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ boolean removeIf(java.util.function.IntPredicate x0) {
            return super.removeIf(x0);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ boolean retainAll(IntCollection x0) {
            return super.retainAll(x0);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean retainAll(Collection x0) {
            return super.retainAll((Collection<?>) x0);
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
        public /* bridge */ /* synthetic */ int[] toArray(int[] x0) {
            return super.toArray(x0);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ Object[] toArray() {
            return super.toArray();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ Object[] toArray(Object[] x0) {
            return super.toArray(x0);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public /* bridge */ /* synthetic */ int[] toIntArray() {
            return super.toIntArray();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection
        @Deprecated
        public /* bridge */ /* synthetic */ int[] toIntArray(int[] x0) {
            return super.toIntArray(x0);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection
        public /* bridge */ /* synthetic */ String toString() {
            return super.toString();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public UnmodifiableSet(IntSet s) {
            super(s);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSet
        public boolean remove(int k) {
            throw new UnsupportedOperationException();
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

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection
        @Deprecated
        public boolean rem(int k) {
            return super.rem(k);
        }
    }

    public static IntSet unmodifiable(IntSet s) {
        return new UnmodifiableSet(s);
    }

    public static IntSet fromTo(final int from, final int to) {
        return new AbstractIntSet() { // from class: it.unimi.dsi.fastutil.ints.IntSets.1
            @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
            public boolean contains(int x) {
                return x >= from && x < to;
            }

            @Override // it.unimi.dsi.fastutil.ints.AbstractIntSet, it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
            public IntIterator iterator() {
                return IntIterators.fromTo(from, to);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                long size = to - from;
                if (size < 0 || size > 2147483647L) {
                    return Integer.MAX_VALUE;
                }
                return (int) size;
            }
        };
    }

    public static IntSet from(final int from) {
        return new AbstractIntSet() { // from class: it.unimi.dsi.fastutil.ints.IntSets.2
            @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
            public boolean contains(int x) {
                return x >= from;
            }

            @Override // it.unimi.dsi.fastutil.ints.AbstractIntSet, it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
            public IntIterator iterator() {
                return IntIterators.concat(IntIterators.fromTo(from, Integer.MAX_VALUE), IntSets.singleton(Integer.MAX_VALUE).iterator());
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                long size = (2147483647L - from) + 1;
                if (size < 0 || size > 2147483647L) {
                    return Integer.MAX_VALUE;
                }
                return (int) size;
            }
        };
    }

    public static IntSet to(final int to) {
        return new AbstractIntSet() { // from class: it.unimi.dsi.fastutil.ints.IntSets.3
            @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
            public boolean contains(int x) {
                return x < to;
            }

            @Override // it.unimi.dsi.fastutil.ints.AbstractIntSet, it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
            public IntIterator iterator() {
                return IntIterators.fromTo(Integer.MIN_VALUE, to);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                long size = to - (-2147483648L);
                if (size < 0 || size > 2147483647L) {
                    return Integer.MAX_VALUE;
                }
                return (int) size;
            }
        };
    }
}
