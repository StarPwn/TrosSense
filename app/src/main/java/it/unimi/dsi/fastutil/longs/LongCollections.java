package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.objects.ObjectArrays;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/* loaded from: classes4.dex */
public final class LongCollections {
    private LongCollections() {
    }

    /* loaded from: classes4.dex */
    public static abstract class EmptyCollection extends AbstractLongCollection {
        @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public boolean contains(long k) {
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public Object[] toArray() {
            return ObjectArrays.EMPTY_ARRAY;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public <T> T[] toArray(T[] array) {
            if (array.length > 0) {
                array[0] = null;
            }
            return array;
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
        public LongBidirectionalIterator iterator() {
            return LongIterators.EMPTY_ITERATOR;
        }

        @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
        public LongSpliterator spliterator() {
            return LongSpliterators.EMPTY_SPLITERATOR;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return 0;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
        }

        @Override // java.util.Collection
        public int hashCode() {
            return 0;
        }

        @Override // java.util.Collection
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof Collection) {
                return ((Collection) o).isEmpty();
            }
            return false;
        }

        @Override // java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongIterable
        @Deprecated
        public void forEach(Consumer<? super Long> action) {
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection, java.util.Collection
        public boolean containsAll(Collection<?> c) {
            return c.isEmpty();
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
            Objects.requireNonNull(filter);
            return false;
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public long[] toLongArray() {
            return LongArrays.EMPTY_ARRAY;
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
        @Deprecated
        public long[] toLongArray(long[] a) {
            return a;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongIterable
        public void forEach(java.util.function.LongConsumer action) {
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public boolean containsAll(LongCollection c) {
            return c.isEmpty();
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
            Objects.requireNonNull(filter);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static class SynchronizedCollection implements LongCollection, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final LongCollection collection;
        protected final Object sync;

        /* JADX INFO: Access modifiers changed from: protected */
        public SynchronizedCollection(LongCollection c, Object sync) {
            this.collection = (LongCollection) Objects.requireNonNull(c);
            this.sync = sync;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public SynchronizedCollection(LongCollection c) {
            this.collection = (LongCollection) Objects.requireNonNull(c);
            this.sync = this;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection
        public boolean add(long k) {
            boolean add;
            synchronized (this.sync) {
                add = this.collection.add(k);
            }
            return add;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection
        public boolean contains(long k) {
            boolean contains;
            synchronized (this.sync) {
                contains = this.collection.contains(k);
            }
            return contains;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection
        public boolean rem(long k) {
            boolean rem;
            synchronized (this.sync) {
                rem = this.collection.rem(k);
            }
            return rem;
        }

        @Override // java.util.Collection
        public int size() {
            int size;
            synchronized (this.sync) {
                size = this.collection.size();
            }
            return size;
        }

        @Override // java.util.Collection
        public boolean isEmpty() {
            boolean isEmpty;
            synchronized (this.sync) {
                isEmpty = this.collection.isEmpty();
            }
            return isEmpty;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection
        public long[] toLongArray() {
            long[] longArray;
            synchronized (this.sync) {
                longArray = this.collection.toLongArray();
            }
            return longArray;
        }

        @Override // java.util.Collection
        public Object[] toArray() {
            Object[] array;
            synchronized (this.sync) {
                array = this.collection.toArray();
            }
            return array;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection
        @Deprecated
        public long[] toLongArray(long[] a) {
            return toArray(a);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection
        public long[] toArray(long[] a) {
            long[] array;
            synchronized (this.sync) {
                array = this.collection.toArray(a);
            }
            return array;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection
        public boolean addAll(LongCollection c) {
            boolean addAll;
            synchronized (this.sync) {
                addAll = this.collection.addAll(c);
            }
            return addAll;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection
        public boolean containsAll(LongCollection c) {
            boolean containsAll;
            synchronized (this.sync) {
                containsAll = this.collection.containsAll(c);
            }
            return containsAll;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection
        public boolean removeAll(LongCollection c) {
            boolean removeAll;
            synchronized (this.sync) {
                removeAll = this.collection.removeAll(c);
            }
            return removeAll;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection
        public boolean retainAll(LongCollection c) {
            boolean retainAll;
            synchronized (this.sync) {
                retainAll = this.collection.retainAll(c);
            }
            return retainAll;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongCollection, java.util.Collection
        @Deprecated
        public boolean add(Long k) {
            boolean add;
            synchronized (this.sync) {
                add = this.collection.add(k);
            }
            return add;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection, java.util.Collection
        @Deprecated
        public boolean contains(Object k) {
            boolean contains;
            synchronized (this.sync) {
                contains = this.collection.contains(k);
            }
            return contains;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection, java.util.Collection
        @Deprecated
        public boolean remove(Object k) {
            boolean remove;
            synchronized (this.sync) {
                remove = this.collection.remove(k);
            }
            return remove;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable
        public LongIterator longIterator() {
            return this.collection.longIterator();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable
        public LongSpliterator longSpliterator() {
            return this.collection.longSpliterator();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection
        public LongStream longStream() {
            return this.collection.longStream();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection
        public LongStream longParallelStream() {
            return this.collection.longParallelStream();
        }

        @Override // java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            T[] tArr2;
            synchronized (this.sync) {
                tArr2 = (T[]) this.collection.toArray(tArr);
            }
            return tArr2;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
        public LongIterator iterator() {
            return this.collection.iterator();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
        public LongSpliterator spliterator() {
            return this.collection.spliterator();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection, java.util.Collection
        @Deprecated
        public Stream<Long> stream() {
            return this.collection.stream();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection, java.util.Collection
        @Deprecated
        public Stream<Long> parallelStream() {
            return this.collection.parallelStream();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongIterable
        public void forEach(java.util.function.LongConsumer action) {
            synchronized (this.sync) {
                this.collection.forEach(action);
            }
        }

        @Override // java.util.Collection
        public boolean addAll(Collection<? extends Long> c) {
            boolean addAll;
            synchronized (this.sync) {
                addAll = this.collection.addAll(c);
            }
            return addAll;
        }

        @Override // java.util.Collection
        public boolean containsAll(Collection<?> c) {
            boolean containsAll;
            synchronized (this.sync) {
                containsAll = this.collection.containsAll(c);
            }
            return containsAll;
        }

        @Override // java.util.Collection
        public boolean removeAll(Collection<?> c) {
            boolean removeAll;
            synchronized (this.sync) {
                removeAll = this.collection.removeAll(c);
            }
            return removeAll;
        }

        @Override // java.util.Collection
        public boolean retainAll(Collection<?> c) {
            boolean retainAll;
            synchronized (this.sync) {
                retainAll = this.collection.retainAll(c);
            }
            return retainAll;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection
        public boolean removeIf(java.util.function.LongPredicate filter) {
            boolean removeIf;
            synchronized (this.sync) {
                removeIf = this.collection.removeIf(filter);
            }
            return removeIf;
        }

        @Override // java.util.Collection
        public void clear() {
            synchronized (this.sync) {
                this.collection.clear();
            }
        }

        public String toString() {
            String obj;
            synchronized (this.sync) {
                obj = this.collection.toString();
            }
            return obj;
        }

        @Override // java.util.Collection
        public int hashCode() {
            int hashCode;
            synchronized (this.sync) {
                hashCode = this.collection.hashCode();
            }
            return hashCode;
        }

        @Override // java.util.Collection
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

        private void writeObject(ObjectOutputStream s) throws IOException {
            synchronized (this.sync) {
                s.defaultWriteObject();
            }
        }
    }

    public static LongCollection synchronize(LongCollection c) {
        return new SynchronizedCollection(c);
    }

    public static LongCollection synchronize(LongCollection c, Object sync) {
        return new SynchronizedCollection(c, sync);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static class UnmodifiableCollection implements LongCollection, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final LongCollection collection;

        /* JADX INFO: Access modifiers changed from: protected */
        public UnmodifiableCollection(LongCollection c) {
            this.collection = (LongCollection) Objects.requireNonNull(c);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection
        public boolean add(long k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection
        public boolean rem(long k) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Collection
        public int size() {
            return this.collection.size();
        }

        @Override // java.util.Collection
        public boolean isEmpty() {
            return this.collection.isEmpty();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection
        public boolean contains(long o) {
            return this.collection.contains(o);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
        public LongIterator iterator() {
            return LongIterators.unmodifiable(this.collection.iterator());
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
        public LongSpliterator spliterator() {
            return this.collection.spliterator();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection, java.util.Collection
        @Deprecated
        public Stream<Long> stream() {
            return this.collection.stream();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection, java.util.Collection
        @Deprecated
        public Stream<Long> parallelStream() {
            return this.collection.parallelStream();
        }

        @Override // java.util.Collection
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            return (T[]) this.collection.toArray(tArr);
        }

        @Override // java.util.Collection
        public Object[] toArray() {
            return this.collection.toArray();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongIterable
        public void forEach(java.util.function.LongConsumer action) {
            this.collection.forEach(action);
        }

        @Override // java.util.Collection
        public boolean containsAll(Collection<?> c) {
            return this.collection.containsAll(c);
        }

        @Override // java.util.Collection
        public boolean addAll(Collection<? extends Long> c) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Collection
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Collection
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection
        public boolean removeIf(java.util.function.LongPredicate filter) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongCollection, java.util.Collection
        @Deprecated
        public boolean add(Long k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection, java.util.Collection
        @Deprecated
        public boolean contains(Object k) {
            return this.collection.contains(k);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection, java.util.Collection
        @Deprecated
        public boolean remove(Object k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection
        public long[] toLongArray() {
            return this.collection.toLongArray();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection
        @Deprecated
        public long[] toLongArray(long[] a) {
            return toArray(a);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection
        public long[] toArray(long[] a) {
            return this.collection.toArray(a);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection
        public boolean containsAll(LongCollection c) {
            return this.collection.containsAll(c);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection
        public boolean addAll(LongCollection c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection
        public boolean removeAll(LongCollection c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection
        public boolean retainAll(LongCollection c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable
        public LongIterator longIterator() {
            return this.collection.longIterator();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable
        public LongSpliterator longSpliterator() {
            return this.collection.longSpliterator();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection
        public LongStream longStream() {
            return this.collection.longStream();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection
        public LongStream longParallelStream() {
            return this.collection.longParallelStream();
        }

        public String toString() {
            return this.collection.toString();
        }

        @Override // java.util.Collection
        public int hashCode() {
            return this.collection.hashCode();
        }

        @Override // java.util.Collection
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            return this.collection.equals(o);
        }
    }

    public static LongCollection unmodifiable(LongCollection c) {
        return new UnmodifiableCollection(c);
    }

    /* loaded from: classes4.dex */
    public static class IterableCollection extends AbstractLongCollection implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final LongIterable iterable;

        protected IterableCollection(LongIterable iterable) {
            this.iterable = (LongIterable) Objects.requireNonNull(iterable);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            long size = this.iterable.spliterator().getExactSizeIfKnown();
            if (size >= 0) {
                return (int) Math.min(2147483647L, size);
            }
            int c = 0;
            LongIterator iterator = iterator();
            while (iterator.hasNext()) {
                iterator.nextLong();
                c++;
            }
            return c;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return !this.iterable.iterator().hasNext();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
        public LongIterator iterator() {
            return this.iterable.iterator();
        }

        @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
        public LongSpliterator spliterator() {
            return this.iterable.spliterator();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable
        public LongIterator longIterator() {
            return this.iterable.longIterator();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable
        public LongSpliterator longSpliterator() {
            return this.iterable.longSpliterator();
        }
    }

    public static LongCollection asCollection(LongIterable iterable) {
        return iterable instanceof LongCollection ? (LongCollection) iterable : new IterableCollection(iterable);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static class SizeDecreasingSupplier<C extends LongCollection> implements Supplier<C> {
        static final int RECOMMENDED_MIN_SIZE = 8;
        final IntFunction<C> builder;
        final int expectedFinalSize;
        final AtomicInteger suppliedCount = new AtomicInteger(0);

        /* JADX INFO: Access modifiers changed from: package-private */
        public SizeDecreasingSupplier(int expectedFinalSize, IntFunction<C> builder) {
            this.expectedFinalSize = expectedFinalSize;
            this.builder = builder;
        }

        @Override // java.util.function.Supplier
        public C get() {
            int expectedNeededNextSize = ((this.expectedFinalSize - 1) / this.suppliedCount.incrementAndGet()) + 1;
            if (expectedNeededNextSize < 0) {
                expectedNeededNextSize = 8;
            }
            return this.builder.apply(expectedNeededNextSize);
        }
    }
}
