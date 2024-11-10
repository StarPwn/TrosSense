package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.objects.ObjectSets;
import java.io.Serializable;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.SortedSet;

/* loaded from: classes4.dex */
public final class ObjectSortedSets {
    public static final EmptySet EMPTY_SET = new EmptySet();

    private ObjectSortedSets() {
    }

    /* loaded from: classes4.dex */
    public static class EmptySet<K> extends ObjectSets.EmptySet<K> implements ObjectSortedSet<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public /* bridge */ /* synthetic */ SortedSet headSet(Object obj) {
            return headSet((EmptySet<K>) obj);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public /* bridge */ /* synthetic */ SortedSet tailSet(Object obj) {
            return tailSet((EmptySet<K>) obj);
        }

        protected EmptySet() {
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet
        public ObjectBidirectionalIterator<K> iterator(K from) {
            return ObjectIterators.EMPTY_ITERATOR;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public ObjectSortedSet<K> subSet(K from, K to) {
            return ObjectSortedSets.EMPTY_SET;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public ObjectSortedSet<K> headSet(K from) {
            return ObjectSortedSets.EMPTY_SET;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public ObjectSortedSet<K> tailSet(K to) {
            return ObjectSortedSets.EMPTY_SET;
        }

        @Override // java.util.SortedSet
        public K first() {
            throw new NoSuchElementException();
        }

        @Override // java.util.SortedSet
        public K last() {
            throw new NoSuchElementException();
        }

        @Override // java.util.SortedSet
        public Comparator<? super K> comparator() {
            return null;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSets.EmptySet
        public Object clone() {
            return ObjectSortedSets.EMPTY_SET;
        }

        private Object readResolve() {
            return ObjectSortedSets.EMPTY_SET;
        }
    }

    public static <K> ObjectSortedSet<K> emptySet() {
        return EMPTY_SET;
    }

    /* loaded from: classes4.dex */
    public static class Singleton<K> extends ObjectSets.Singleton<K> implements ObjectSortedSet<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        final Comparator<? super K> comparator;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public /* bridge */ /* synthetic */ SortedSet headSet(Object obj) {
            return headSet((Singleton<K>) obj);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSets.Singleton, it.unimi.dsi.fastutil.objects.AbstractObjectSet, it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        public /* bridge */ /* synthetic */ ObjectBidirectionalIterator iterator() {
            return super.iterator();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public /* bridge */ /* synthetic */ SortedSet tailSet(Object obj) {
            return tailSet((Singleton<K>) obj);
        }

        protected Singleton(K element, Comparator<? super K> comparator) {
            super(element);
            this.comparator = comparator;
        }

        Singleton(K element) {
            this(element, null);
        }

        final int compare(K k1, K k2) {
            return this.comparator == null ? ((Comparable) k1).compareTo(k2) : this.comparator.compare(k1, k2);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet
        public ObjectBidirectionalIterator<K> iterator(K from) {
            ObjectBidirectionalIterator<K> i = iterator();
            if (compare(this.element, from) <= 0) {
                i.next();
            }
            return i;
        }

        @Override // java.util.SortedSet
        public Comparator<? super K> comparator() {
            return this.comparator;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSets.Singleton, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        /* renamed from: spliterator */
        public ObjectSpliterator<K> mo221spliterator() {
            return ObjectSpliterators.singleton(this.element, this.comparator);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public ObjectSortedSet<K> subSet(K from, K to) {
            return (compare(from, this.element) > 0 || compare(this.element, to) >= 0) ? ObjectSortedSets.EMPTY_SET : this;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public ObjectSortedSet<K> headSet(K to) {
            return compare(this.element, to) < 0 ? this : ObjectSortedSets.EMPTY_SET;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public ObjectSortedSet<K> tailSet(K from) {
            return compare(from, this.element) <= 0 ? this : ObjectSortedSets.EMPTY_SET;
        }

        @Override // java.util.SortedSet
        public K first() {
            return this.element;
        }

        @Override // java.util.SortedSet
        public K last() {
            return this.element;
        }
    }

    public static <K> ObjectSortedSet<K> singleton(K element) {
        return new Singleton(element);
    }

    public static <K> ObjectSortedSet<K> singleton(K element, Comparator<? super K> comparator) {
        return new Singleton(element, comparator);
    }

    /* loaded from: classes4.dex */
    public static class SynchronizedSortedSet<K> extends ObjectSets.SynchronizedSet<K> implements ObjectSortedSet<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ObjectSortedSet<K> sortedSet;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public /* bridge */ /* synthetic */ SortedSet headSet(Object obj) {
            return headSet((SynchronizedSortedSet<K>) obj);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public /* bridge */ /* synthetic */ SortedSet tailSet(Object obj) {
            return tailSet((SynchronizedSortedSet<K>) obj);
        }

        protected SynchronizedSortedSet(ObjectSortedSet<K> s, Object sync) {
            super(s, sync);
            this.sortedSet = s;
        }

        protected SynchronizedSortedSet(ObjectSortedSet<K> s) {
            super(s);
            this.sortedSet = s;
        }

        @Override // java.util.SortedSet
        public Comparator<? super K> comparator() {
            Comparator<? super K> comparator;
            synchronized (this.sync) {
                comparator = this.sortedSet.comparator();
            }
            return comparator;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public ObjectSortedSet<K> subSet(K from, K to) {
            return new SynchronizedSortedSet(this.sortedSet.subSet((Object) from, (Object) to), this.sync);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public ObjectSortedSet<K> headSet(K to) {
            return new SynchronizedSortedSet(this.sortedSet.headSet((ObjectSortedSet<K>) to), this.sync);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public ObjectSortedSet<K> tailSet(K from) {
            return new SynchronizedSortedSet(this.sortedSet.tailSet((ObjectSortedSet<K>) from), this.sync);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, it.unimi.dsi.fastutil.objects.ObjectCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectIterable
        public ObjectBidirectionalIterator<K> iterator() {
            return this.sortedSet.iterator();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet
        public ObjectBidirectionalIterator<K> iterator(K from) {
            return this.sortedSet.iterator(from);
        }

        @Override // java.util.SortedSet
        public K first() {
            K first;
            synchronized (this.sync) {
                first = this.sortedSet.first();
            }
            return first;
        }

        @Override // java.util.SortedSet
        public K last() {
            K last;
            synchronized (this.sync) {
                last = this.sortedSet.last();
            }
            return last;
        }
    }

    public static <K> ObjectSortedSet<K> synchronize(ObjectSortedSet<K> s) {
        return new SynchronizedSortedSet(s);
    }

    public static <K> ObjectSortedSet<K> synchronize(ObjectSortedSet<K> s, Object sync) {
        return new SynchronizedSortedSet(s, sync);
    }

    /* loaded from: classes4.dex */
    public static class UnmodifiableSortedSet<K> extends ObjectSets.UnmodifiableSet<K> implements ObjectSortedSet<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ObjectSortedSet<K> sortedSet;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public /* bridge */ /* synthetic */ SortedSet headSet(Object obj) {
            return headSet((UnmodifiableSortedSet<K>) obj);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public /* bridge */ /* synthetic */ SortedSet tailSet(Object obj) {
            return tailSet((UnmodifiableSortedSet<K>) obj);
        }

        protected UnmodifiableSortedSet(ObjectSortedSet<K> s) {
            super(s);
            this.sortedSet = s;
        }

        @Override // java.util.SortedSet
        public Comparator<? super K> comparator() {
            return this.sortedSet.comparator();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public ObjectSortedSet<K> subSet(K from, K to) {
            return new UnmodifiableSortedSet(this.sortedSet.subSet((Object) from, (Object) to));
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public ObjectSortedSet<K> headSet(K to) {
            return new UnmodifiableSortedSet(this.sortedSet.headSet((ObjectSortedSet<K>) to));
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public ObjectSortedSet<K> tailSet(K from) {
            return new UnmodifiableSortedSet(this.sortedSet.tailSet((ObjectSortedSet<K>) from));
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.objects.ObjectCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectIterable
        public ObjectBidirectionalIterator<K> iterator() {
            return ObjectIterators.unmodifiable((ObjectBidirectionalIterator) this.sortedSet.iterator());
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet
        public ObjectBidirectionalIterator<K> iterator(K from) {
            return ObjectIterators.unmodifiable((ObjectBidirectionalIterator) this.sortedSet.iterator(from));
        }

        @Override // java.util.SortedSet
        public K first() {
            return this.sortedSet.first();
        }

        @Override // java.util.SortedSet
        public K last() {
            return this.sortedSet.last();
        }
    }

    public static <K> ObjectSortedSet<K> unmodifiable(ObjectSortedSet<K> s) {
        return new UnmodifiableSortedSet(s);
    }
}
