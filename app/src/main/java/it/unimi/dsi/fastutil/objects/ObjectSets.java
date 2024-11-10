package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.objects.ObjectCollections;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/* loaded from: classes4.dex */
public final class ObjectSets {
    static final int ARRAY_SET_CUTOFF = 4;
    public static final EmptySet EMPTY_SET = new EmptySet();
    static final ObjectSet UNMODIFIABLE_EMPTY_SET = unmodifiable(new ObjectArraySet(ObjectArrays.EMPTY_ARRAY));

    private ObjectSets() {
    }

    /* loaded from: classes4.dex */
    public static class EmptySet<K> extends ObjectCollections.EmptyCollection<K> implements ObjectSet<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object ok) {
            throw new UnsupportedOperationException();
        }

        public Object clone() {
            return ObjectSets.EMPTY_SET;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.EmptyCollection, java.util.Collection
        public boolean equals(Object o) {
            return (o instanceof Set) && ((Set) o).isEmpty();
        }

        private Object readResolve() {
            return ObjectSets.EMPTY_SET;
        }
    }

    public static <K> ObjectSet<K> emptySet() {
        return EMPTY_SET;
    }

    /* loaded from: classes4.dex */
    public static class Singleton<K> extends AbstractObjectSet<K> implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final K element;

        /* JADX INFO: Access modifiers changed from: protected */
        public Singleton(K element) {
            this.element = element;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object k) {
            return Objects.equals(k, this.element);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectSet, it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        public ObjectListIterator<K> iterator() {
            return ObjectIterators.singleton(this.element);
        }

        @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        /* renamed from: spliterator */
        public ObjectSpliterator<K> mo221spliterator() {
            return ObjectSpliterators.singleton(this.element);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return 1;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public Object[] toArray() {
            return new Object[]{this.element};
        }

        @Override // java.lang.Iterable
        public void forEach(Consumer<? super K> consumer) {
            consumer.accept(this.element);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean addAll(Collection<? extends K> c) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Collection
        public boolean removeIf(Predicate<? super K> filter) {
            throw new UnsupportedOperationException();
        }

        public Object clone() {
            return this;
        }
    }

    public static <K> ObjectSet<K> singleton(K element) {
        return new Singleton(element);
    }

    /* loaded from: classes4.dex */
    public static class SynchronizedSet<K> extends ObjectCollections.SynchronizedCollection<K> implements ObjectSet<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean add(Object obj) {
            return super.add(obj);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean addAll(Collection x0) {
            return super.addAll(x0);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ void clear() {
            super.clear();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean contains(Object x0) {
            return super.contains(x0);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean containsAll(Collection x0) {
            return super.containsAll(x0);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean equals(Object x0) {
            return super.equals(x0);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, java.lang.Iterable
        public /* bridge */ /* synthetic */ void forEach(Consumer x0) {
            super.forEach(x0);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ int hashCode() {
            return super.hashCode();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean isEmpty() {
            return super.isEmpty();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, it.unimi.dsi.fastutil.objects.ObjectCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectIterable
        public /* bridge */ /* synthetic */ ObjectIterator iterator() {
            return super.iterator();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ Stream parallelStream() {
            return super.parallelStream();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean removeAll(Collection x0) {
            return super.removeAll(x0);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean removeIf(Predicate x0) {
            return super.removeIf(x0);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean retainAll(Collection x0) {
            return super.retainAll(x0);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ int size() {
            return super.size();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, it.unimi.dsi.fastutil.objects.ObjectCollection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectIterable
        /* renamed from: spliterator */
        public /* bridge */ /* synthetic */ ObjectSpliterator mo221spliterator() {
            return super.mo221spliterator();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ Stream stream() {
            return super.stream();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ Object[] toArray() {
            return super.toArray();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ Object[] toArray(Object[] x0) {
            return super.toArray(x0);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection
        public /* bridge */ /* synthetic */ String toString() {
            return super.toString();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public SynchronizedSet(ObjectSet<K> s, Object sync) {
            super(s, sync);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public SynchronizedSet(ObjectSet<K> s) {
            super(s);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, java.util.Collection
        public boolean remove(Object k) {
            boolean remove;
            synchronized (this.sync) {
                remove = this.collection.remove(k);
            }
            return remove;
        }
    }

    public static <K> ObjectSet<K> synchronize(ObjectSet<K> s) {
        return new SynchronizedSet(s);
    }

    public static <K> ObjectSet<K> synchronize(ObjectSet<K> s, Object sync) {
        return new SynchronizedSet(s, sync);
    }

    /* loaded from: classes4.dex */
    public static class UnmodifiableSet<K> extends ObjectCollections.UnmodifiableCollection<K> implements ObjectSet<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean add(Object obj) {
            return super.add(obj);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean addAll(Collection x0) {
            return super.addAll(x0);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ void clear() {
            super.clear();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean contains(Object x0) {
            return super.contains(x0);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean containsAll(Collection x0) {
            return super.containsAll(x0);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, java.lang.Iterable
        public /* bridge */ /* synthetic */ void forEach(Consumer x0) {
            super.forEach(x0);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean isEmpty() {
            return super.isEmpty();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.objects.ObjectCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectIterable
        public /* bridge */ /* synthetic */ ObjectIterator iterator() {
            return super.iterator();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ Stream parallelStream() {
            return super.parallelStream();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean removeAll(Collection x0) {
            return super.removeAll(x0);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean removeIf(Predicate x0) {
            return super.removeIf(x0);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean retainAll(Collection x0) {
            return super.retainAll(x0);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ int size() {
            return super.size();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.objects.ObjectCollection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectIterable
        /* renamed from: spliterator */
        public /* bridge */ /* synthetic */ ObjectSpliterator mo221spliterator() {
            return super.mo221spliterator();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ Stream stream() {
            return super.stream();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ Object[] toArray() {
            return super.toArray();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ Object[] toArray(Object[] x0) {
            return super.toArray(x0);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection
        public /* bridge */ /* synthetic */ String toString() {
            return super.toString();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public UnmodifiableSet(ObjectSet<? extends K> s) {
            super(s);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, java.util.Collection
        public boolean remove(Object k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, java.util.Collection
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            return this.collection.equals(o);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, java.util.Collection
        public int hashCode() {
            return this.collection.hashCode();
        }
    }

    public static <K> ObjectSet<K> unmodifiable(ObjectSet<? extends K> s) {
        return new UnmodifiableSet(s);
    }
}
