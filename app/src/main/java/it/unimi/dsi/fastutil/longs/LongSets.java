package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.longs.LongCollections;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/* loaded from: classes4.dex */
public final class LongSets {
    static final int ARRAY_SET_CUTOFF = 4;
    public static final EmptySet EMPTY_SET = new EmptySet();
    static final LongSet UNMODIFIABLE_EMPTY_SET = unmodifiable(new LongArraySet(LongArrays.EMPTY_ARRAY));

    private LongSets() {
    }

    /* loaded from: classes4.dex */
    public static class EmptySet extends LongCollections.EmptyCollection implements LongSet, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;

        @Override // it.unimi.dsi.fastutil.longs.LongSet
        public boolean remove(long ok) {
            throw new UnsupportedOperationException();
        }

        public Object clone() {
            return LongSets.EMPTY_SET;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.EmptyCollection, java.util.Collection
        public boolean equals(Object o) {
            return (o instanceof Set) && ((Set) o).isEmpty();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
        @Deprecated
        public boolean rem(long k) {
            return super.rem(k);
        }

        private Object readResolve() {
            return LongSets.EMPTY_SET;
        }
    }

    public static LongSet emptySet() {
        return EMPTY_SET;
    }

    /* loaded from: classes4.dex */
    public static class Singleton extends AbstractLongSet implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final long element;

        /* JADX INFO: Access modifiers changed from: protected */
        public Singleton(long element) {
            this.element = element;
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public boolean contains(long k) {
            return k == this.element;
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongSet, it.unimi.dsi.fastutil.longs.LongSet
        public boolean remove(long k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongSet, it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
        public LongListIterator iterator() {
            return LongIterators.singleton(this.element);
        }

        @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
        public LongSpliterator spliterator() {
            return LongSpliterators.singleton(this.element);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return 1;
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public long[] toLongArray() {
            return new long[]{this.element};
        }

        @Override // java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongIterable
        @Deprecated
        public void forEach(Consumer<? super Long> action) {
            action.accept(Long.valueOf(this.element));
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection, java.util.Collection
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

        @Override // it.unimi.dsi.fastutil.longs.LongIterable
        public void forEach(java.util.function.LongConsumer action) {
            action.accept(this.element);
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
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

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        @Deprecated
        public Object[] toArray() {
            return new Object[]{Long.valueOf(this.element)};
        }

        public Object clone() {
            return this;
        }
    }

    public static LongSet singleton(long element) {
        return new Singleton(element);
    }

    public static LongSet singleton(Long element) {
        return new Singleton(element.longValue());
    }

    /* loaded from: classes4.dex */
    public static class SynchronizedSet extends LongCollections.SynchronizedCollection implements LongSet, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ boolean add(long x0) {
            return super.add(x0);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongCollection
        @Deprecated
        public /* bridge */ /* synthetic */ boolean add(Long x0) {
            return super.add(x0);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ boolean addAll(LongCollection x0) {
            return super.addAll(x0);
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
        public /* bridge */ /* synthetic */ boolean contains(long x0) {
            return super.contains(x0);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongCollection, java.util.Collection
        @Deprecated
        public /* bridge */ /* synthetic */ boolean contains(Object x0) {
            return super.contains(x0);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ boolean containsAll(LongCollection x0) {
            return super.containsAll(x0);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean containsAll(Collection x0) {
            return super.containsAll((Collection<?>) x0);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean equals(Object x0) {
            return super.equals(x0);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongIterable
        public /* bridge */ /* synthetic */ void forEach(java.util.function.LongConsumer x0) {
            super.forEach(x0);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ int hashCode() {
            return super.hashCode();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean isEmpty() {
            return super.isEmpty();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
        public /* bridge */ /* synthetic */ LongIterator iterator() {
            return super.iterator();
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

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongCollection, java.util.Collection
        @Deprecated
        public /* bridge */ /* synthetic */ boolean remove(Object x0) {
            return super.remove(x0);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ boolean removeAll(LongCollection x0) {
            return super.removeAll(x0);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean removeAll(Collection x0) {
            return super.removeAll((Collection<?>) x0);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ boolean removeIf(java.util.function.LongPredicate x0) {
            return super.removeIf(x0);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ boolean retainAll(LongCollection x0) {
            return super.retainAll(x0);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean retainAll(Collection x0) {
            return super.retainAll((Collection<?>) x0);
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
        public /* bridge */ /* synthetic */ long[] toArray(long[] x0) {
            return super.toArray(x0);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ Object[] toArray() {
            return super.toArray();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ Object[] toArray(Object[] x0) {
            return super.toArray(x0);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ long[] toLongArray() {
            return super.toLongArray();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongCollection
        @Deprecated
        public /* bridge */ /* synthetic */ long[] toLongArray(long[] x0) {
            return super.toLongArray(x0);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection
        public /* bridge */ /* synthetic */ String toString() {
            return super.toString();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public SynchronizedSet(LongSet s, Object sync) {
            super(s, sync);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public SynchronizedSet(LongSet s) {
            super(s);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSet
        public boolean remove(long k) {
            boolean rem;
            synchronized (this.sync) {
                rem = this.collection.rem(k);
            }
            return rem;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongCollection
        @Deprecated
        public boolean rem(long k) {
            return super.rem(k);
        }
    }

    public static LongSet synchronize(LongSet s) {
        return new SynchronizedSet(s);
    }

    public static LongSet synchronize(LongSet s, Object sync) {
        return new SynchronizedSet(s, sync);
    }

    /* loaded from: classes4.dex */
    public static class UnmodifiableSet extends LongCollections.UnmodifiableCollection implements LongSet, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ boolean add(long x0) {
            return super.add(x0);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongCollection
        @Deprecated
        public /* bridge */ /* synthetic */ boolean add(Long x0) {
            return super.add(x0);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ boolean addAll(LongCollection x0) {
            return super.addAll(x0);
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
        public /* bridge */ /* synthetic */ boolean contains(long x0) {
            return super.contains(x0);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongCollection, java.util.Collection
        @Deprecated
        public /* bridge */ /* synthetic */ boolean contains(Object x0) {
            return super.contains(x0);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ boolean containsAll(LongCollection x0) {
            return super.containsAll(x0);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean containsAll(Collection x0) {
            return super.containsAll((Collection<?>) x0);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongIterable
        public /* bridge */ /* synthetic */ void forEach(java.util.function.LongConsumer x0) {
            super.forEach(x0);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean isEmpty() {
            return super.isEmpty();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
        public /* bridge */ /* synthetic */ LongIterator iterator() {
            return super.iterator();
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

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongCollection, java.util.Collection
        @Deprecated
        public /* bridge */ /* synthetic */ boolean remove(Object x0) {
            return super.remove(x0);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ boolean removeAll(LongCollection x0) {
            return super.removeAll(x0);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean removeAll(Collection x0) {
            return super.removeAll((Collection<?>) x0);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ boolean removeIf(java.util.function.LongPredicate x0) {
            return super.removeIf(x0);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ boolean retainAll(LongCollection x0) {
            return super.retainAll(x0);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean retainAll(Collection x0) {
            return super.retainAll((Collection<?>) x0);
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
        public /* bridge */ /* synthetic */ long[] toArray(long[] x0) {
            return super.toArray(x0);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ Object[] toArray() {
            return super.toArray();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ Object[] toArray(Object[] x0) {
            return super.toArray(x0);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public /* bridge */ /* synthetic */ long[] toLongArray() {
            return super.toLongArray();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongCollection
        @Deprecated
        public /* bridge */ /* synthetic */ long[] toLongArray(long[] x0) {
            return super.toLongArray(x0);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection
        public /* bridge */ /* synthetic */ String toString() {
            return super.toString();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public UnmodifiableSet(LongSet s) {
            super(s);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSet
        public boolean remove(long k) {
            throw new UnsupportedOperationException();
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

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongCollection
        @Deprecated
        public boolean rem(long k) {
            return super.rem(k);
        }
    }

    public static LongSet unmodifiable(LongSet s) {
        return new UnmodifiableSet(s);
    }

    public static LongSet fromTo(final long from, final long to) {
        return new AbstractLongSet() { // from class: it.unimi.dsi.fastutil.longs.LongSets.1
            @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
            public boolean contains(long x) {
                return x >= from && x < to;
            }

            @Override // it.unimi.dsi.fastutil.longs.AbstractLongSet, it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
            public LongIterator iterator() {
                return LongIterators.fromTo(from, to);
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

    public static LongSet from(final long from) {
        return new AbstractLongSet() { // from class: it.unimi.dsi.fastutil.longs.LongSets.2
            @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
            public boolean contains(long x) {
                return x >= from;
            }

            @Override // it.unimi.dsi.fastutil.longs.AbstractLongSet, it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
            public LongIterator iterator() {
                return LongIterators.concat(LongIterators.fromTo(from, Long.MAX_VALUE), LongSets.singleton(Long.MAX_VALUE).iterator());
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                long size = (Long.MAX_VALUE - from) + 1;
                if (size < 0 || size > 2147483647L) {
                    return Integer.MAX_VALUE;
                }
                return (int) size;
            }
        };
    }

    public static LongSet to(final long to) {
        return new AbstractLongSet() { // from class: it.unimi.dsi.fastutil.longs.LongSets.3
            @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
            public boolean contains(long x) {
                return x < to;
            }

            @Override // it.unimi.dsi.fastutil.longs.AbstractLongSet, it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
            public LongIterator iterator() {
                return LongIterators.fromTo(Long.MIN_VALUE, to);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                long size = to - Long.MIN_VALUE;
                if (size < 0 || size > 2147483647L) {
                    return Integer.MAX_VALUE;
                }
                return (int) size;
            }
        };
    }
}
