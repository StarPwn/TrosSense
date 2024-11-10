package it.unimi.dsi.fastutil.objects;

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
import java.util.stream.Stream;

/* loaded from: classes4.dex */
public final class ObjectCollections {
    private ObjectCollections() {
    }

    /* loaded from: classes4.dex */
    public static abstract class EmptyCollection<K> extends AbstractObjectCollection<K> {
        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object k) {
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

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        public ObjectBidirectionalIterator<K> iterator() {
            return ObjectIterators.EMPTY_ITERATOR;
        }

        @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        /* renamed from: spliterator */
        public ObjectSpliterator<K> mo221spliterator() {
            return ObjectSpliterators.EMPTY_SPLITERATOR;
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

        @Override // java.lang.Iterable
        public void forEach(Consumer<? super K> action) {
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean containsAll(Collection<?> c) {
            return c.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean addAll(Collection<? extends K> c) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Collection
        public boolean removeIf(Predicate<? super K> filter) {
            Objects.requireNonNull(filter);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static class SynchronizedCollection<K> implements ObjectCollection<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ObjectCollection<K> collection;
        protected final Object sync;

        /* JADX INFO: Access modifiers changed from: protected */
        public SynchronizedCollection(ObjectCollection<K> c, Object sync) {
            this.collection = (ObjectCollection) Objects.requireNonNull(c);
            this.sync = sync;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public SynchronizedCollection(ObjectCollection<K> c) {
            this.collection = (ObjectCollection) Objects.requireNonNull(c);
            this.sync = this;
        }

        @Override // java.util.Collection
        public boolean add(K k) {
            boolean add;
            synchronized (this.sync) {
                add = this.collection.add(k);
            }
            return add;
        }

        @Override // java.util.Collection
        public boolean contains(Object k) {
            boolean contains;
            synchronized (this.sync) {
                contains = this.collection.contains(k);
            }
            return contains;
        }

        @Override // java.util.Collection
        public boolean remove(Object k) {
            boolean remove;
            synchronized (this.sync) {
                remove = this.collection.remove(k);
            }
            return remove;
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

        @Override // java.util.Collection
        public Object[] toArray() {
            Object[] array;
            synchronized (this.sync) {
                array = this.collection.toArray();
            }
            return array;
        }

        @Override // java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            T[] tArr2;
            synchronized (this.sync) {
                tArr2 = (T[]) this.collection.toArray(tArr);
            }
            return tArr2;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectIterable
        public ObjectIterator<K> iterator() {
            return this.collection.iterator();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectIterable
        /* renamed from: spliterator */
        public ObjectSpliterator<K> mo221spliterator() {
            return this.collection.mo221spliterator();
        }

        @Override // java.util.Collection
        public Stream<K> stream() {
            return this.collection.stream();
        }

        @Override // java.util.Collection
        public Stream<K> parallelStream() {
            return this.collection.parallelStream();
        }

        @Override // java.lang.Iterable
        public void forEach(Consumer<? super K> action) {
            synchronized (this.sync) {
                this.collection.forEach(action);
            }
        }

        @Override // java.util.Collection
        public boolean addAll(Collection<? extends K> c) {
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

        @Override // java.util.Collection
        public boolean removeIf(Predicate<? super K> filter) {
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

    public static <K> ObjectCollection<K> synchronize(ObjectCollection<K> c) {
        return new SynchronizedCollection(c);
    }

    public static <K> ObjectCollection<K> synchronize(ObjectCollection<K> c, Object sync) {
        return new SynchronizedCollection(c, sync);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static class UnmodifiableCollection<K> implements ObjectCollection<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ObjectCollection<? extends K> collection;

        /* JADX INFO: Access modifiers changed from: protected */
        public UnmodifiableCollection(ObjectCollection<? extends K> c) {
            this.collection = (ObjectCollection) Objects.requireNonNull(c);
        }

        @Override // java.util.Collection
        public boolean add(K k) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Collection
        public boolean remove(Object k) {
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

        @Override // java.util.Collection
        public boolean contains(Object o) {
            return this.collection.contains(o);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectIterable
        public ObjectIterator<K> iterator() {
            return ObjectIterators.unmodifiable(this.collection.iterator());
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectIterable
        /* renamed from: spliterator */
        public ObjectSpliterator<K> mo221spliterator() {
            return this.collection.mo221spliterator();
        }

        @Override // java.util.Collection
        public Stream<K> stream() {
            return this.collection.stream();
        }

        @Override // java.util.Collection
        public Stream<K> parallelStream() {
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

        @Override // java.lang.Iterable
        public void forEach(Consumer<? super K> action) {
            this.collection.forEach(action);
        }

        @Override // java.util.Collection
        public boolean containsAll(Collection<?> c) {
            return this.collection.containsAll(c);
        }

        @Override // java.util.Collection
        public boolean addAll(Collection<? extends K> c) {
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

        @Override // java.util.Collection
        public boolean removeIf(Predicate<? super K> filter) {
            throw new UnsupportedOperationException();
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

    public static <K> ObjectCollection<K> unmodifiable(ObjectCollection<? extends K> c) {
        return new UnmodifiableCollection(c);
    }

    /* loaded from: classes4.dex */
    public static class IterableCollection<K> extends AbstractObjectCollection<K> implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ObjectIterable<K> iterable;

        protected IterableCollection(ObjectIterable<K> iterable) {
            this.iterable = (ObjectIterable) Objects.requireNonNull(iterable);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            long size = this.iterable.mo221spliterator().getExactSizeIfKnown();
            if (size >= 0) {
                return (int) Math.min(2147483647L, size);
            }
            int c = 0;
            ObjectIterator<K> iterator = iterator();
            while (iterator.hasNext()) {
                iterator.next();
                c++;
            }
            return c;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return !this.iterable.iterator().hasNext();
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        public ObjectIterator<K> iterator() {
            return this.iterable.iterator();
        }

        @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        /* renamed from: spliterator */
        public ObjectSpliterator<K> mo221spliterator() {
            return this.iterable.mo221spliterator();
        }
    }

    public static <K> ObjectCollection<K> asCollection(ObjectIterable<K> iterable) {
        return iterable instanceof ObjectCollection ? (ObjectCollection) iterable : new IterableCollection(iterable);
    }

    /* loaded from: classes4.dex */
    static class SizeDecreasingSupplier<K, C extends ObjectCollection<K>> implements Supplier<C> {
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
