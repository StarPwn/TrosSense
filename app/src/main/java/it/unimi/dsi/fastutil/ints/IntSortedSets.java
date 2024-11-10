package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.ints.IntSets;
import java.io.Serializable;
import java.util.Comparator;
import java.util.NoSuchElementException;

/* loaded from: classes4.dex */
public final class IntSortedSets {
    public static final EmptySet EMPTY_SET = new EmptySet();

    private IntSortedSets() {
    }

    /* loaded from: classes4.dex */
    public static class EmptySet extends IntSets.EmptySet implements IntSortedSet, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;

        protected EmptySet() {
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
        public IntBidirectionalIterator iterator(int from) {
            return IntIterators.EMPTY_ITERATOR;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
        public IntSortedSet subSet(int from, int to) {
            return IntSortedSets.EMPTY_SET;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
        public IntSortedSet headSet(int from) {
            return IntSortedSets.EMPTY_SET;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
        public IntSortedSet tailSet(int to) {
            return IntSortedSets.EMPTY_SET;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
        public int firstInt() {
            throw new NoSuchElementException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
        public int lastInt() {
            throw new NoSuchElementException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet, java.util.SortedSet
        /* renamed from: comparator */
        public Comparator<? super Integer> comparator2() {
            return null;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet, java.util.SortedSet
        @Deprecated
        public IntSortedSet subSet(Integer from, Integer to) {
            return IntSortedSets.EMPTY_SET;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet, java.util.SortedSet
        @Deprecated
        public IntSortedSet headSet(Integer from) {
            return IntSortedSets.EMPTY_SET;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet, java.util.SortedSet
        @Deprecated
        public IntSortedSet tailSet(Integer to) {
            return IntSortedSets.EMPTY_SET;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet, java.util.SortedSet
        @Deprecated
        public Integer first() {
            throw new NoSuchElementException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet, java.util.SortedSet
        @Deprecated
        public Integer last() {
            throw new NoSuchElementException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSets.EmptySet
        public Object clone() {
            return IntSortedSets.EMPTY_SET;
        }

        private Object readResolve() {
            return IntSortedSets.EMPTY_SET;
        }
    }

    /* loaded from: classes4.dex */
    public static class Singleton extends IntSets.Singleton implements IntSortedSet, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        final IntComparator comparator;

        @Override // it.unimi.dsi.fastutil.ints.IntSets.Singleton, it.unimi.dsi.fastutil.ints.AbstractIntSet, it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
        public /* bridge */ /* synthetic */ IntBidirectionalIterator iterator() {
            return super.iterator();
        }

        protected Singleton(int element, IntComparator comparator) {
            super(element);
            this.comparator = comparator;
        }

        Singleton(int element) {
            this(element, null);
        }

        final int compare(int k1, int k2) {
            return this.comparator == null ? Integer.compare(k1, k2) : this.comparator.compare(k1, k2);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
        public IntBidirectionalIterator iterator(int from) {
            IntBidirectionalIterator i = iterator();
            if (compare(this.element, from) <= 0) {
                i.nextInt();
            }
            return i;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet, java.util.SortedSet
        /* renamed from: comparator */
        public Comparator<? super Integer> comparator2() {
            return this.comparator;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSets.Singleton, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
        public IntSpliterator spliterator() {
            return IntSpliterators.singleton(this.element, this.comparator);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
        public IntSortedSet subSet(int from, int to) {
            return (compare(from, this.element) > 0 || compare(this.element, to) >= 0) ? IntSortedSets.EMPTY_SET : this;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
        public IntSortedSet headSet(int to) {
            return compare(this.element, to) < 0 ? this : IntSortedSets.EMPTY_SET;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
        public IntSortedSet tailSet(int from) {
            return compare(from, this.element) <= 0 ? this : IntSortedSets.EMPTY_SET;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
        public int firstInt() {
            return this.element;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
        public int lastInt() {
            return this.element;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet, java.util.SortedSet
        @Deprecated
        public IntSortedSet subSet(Integer from, Integer to) {
            return subSet(from.intValue(), to.intValue());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet, java.util.SortedSet
        @Deprecated
        public IntSortedSet headSet(Integer to) {
            return headSet(to.intValue());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet, java.util.SortedSet
        @Deprecated
        public IntSortedSet tailSet(Integer from) {
            return tailSet(from.intValue());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet, java.util.SortedSet
        @Deprecated
        public Integer first() {
            return Integer.valueOf(this.element);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet, java.util.SortedSet
        @Deprecated
        public Integer last() {
            return Integer.valueOf(this.element);
        }
    }

    public static IntSortedSet singleton(int element) {
        return new Singleton(element);
    }

    public static IntSortedSet singleton(int element, IntComparator comparator) {
        return new Singleton(element, comparator);
    }

    public static IntSortedSet singleton(Object element) {
        return new Singleton(((Integer) element).intValue());
    }

    public static IntSortedSet singleton(Object element, IntComparator comparator) {
        return new Singleton(((Integer) element).intValue(), comparator);
    }

    /* loaded from: classes4.dex */
    public static class SynchronizedSortedSet extends IntSets.SynchronizedSet implements IntSortedSet, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final IntSortedSet sortedSet;

        protected SynchronizedSortedSet(IntSortedSet s, Object sync) {
            super(s, sync);
            this.sortedSet = s;
        }

        protected SynchronizedSortedSet(IntSortedSet s) {
            super(s);
            this.sortedSet = s;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet, java.util.SortedSet
        /* renamed from: comparator */
        public Comparator<? super Integer> comparator2() {
            Comparator<? super Integer> comparator2;
            synchronized (this.sync) {
                comparator2 = this.sortedSet.comparator2();
            }
            return comparator2;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
        public IntSortedSet subSet(int from, int to) {
            return new SynchronizedSortedSet(this.sortedSet.subSet(from, to), this.sync);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
        public IntSortedSet headSet(int to) {
            return new SynchronizedSortedSet(this.sortedSet.headSet(to), this.sync);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
        public IntSortedSet tailSet(int from) {
            return new SynchronizedSortedSet(this.sortedSet.tailSet(from), this.sync);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.SynchronizedCollection, it.unimi.dsi.fastutil.ints.IntCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
        public IntBidirectionalIterator iterator() {
            return this.sortedSet.iterator();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
        public IntBidirectionalIterator iterator(int from) {
            return this.sortedSet.iterator(from);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
        public int firstInt() {
            int firstInt;
            synchronized (this.sync) {
                firstInt = this.sortedSet.firstInt();
            }
            return firstInt;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
        public int lastInt() {
            int lastInt;
            synchronized (this.sync) {
                lastInt = this.sortedSet.lastInt();
            }
            return lastInt;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet, java.util.SortedSet
        @Deprecated
        public Integer first() {
            Integer first;
            synchronized (this.sync) {
                first = this.sortedSet.first();
            }
            return first;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet, java.util.SortedSet
        @Deprecated
        public Integer last() {
            Integer last;
            synchronized (this.sync) {
                last = this.sortedSet.last();
            }
            return last;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet, java.util.SortedSet
        @Deprecated
        public IntSortedSet subSet(Integer from, Integer to) {
            return new SynchronizedSortedSet(this.sortedSet.subSet(from, to), this.sync);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet, java.util.SortedSet
        @Deprecated
        public IntSortedSet headSet(Integer to) {
            return new SynchronizedSortedSet(this.sortedSet.headSet(to), this.sync);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet, java.util.SortedSet
        @Deprecated
        public IntSortedSet tailSet(Integer from) {
            return new SynchronizedSortedSet(this.sortedSet.tailSet(from), this.sync);
        }
    }

    public static IntSortedSet synchronize(IntSortedSet s) {
        return new SynchronizedSortedSet(s);
    }

    public static IntSortedSet synchronize(IntSortedSet s, Object sync) {
        return new SynchronizedSortedSet(s, sync);
    }

    /* loaded from: classes4.dex */
    public static class UnmodifiableSortedSet extends IntSets.UnmodifiableSet implements IntSortedSet, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final IntSortedSet sortedSet;

        protected UnmodifiableSortedSet(IntSortedSet s) {
            super(s);
            this.sortedSet = s;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet, java.util.SortedSet
        /* renamed from: comparator */
        public Comparator<? super Integer> comparator2() {
            return this.sortedSet.comparator2();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
        public IntSortedSet subSet(int from, int to) {
            return new UnmodifiableSortedSet(this.sortedSet.subSet(from, to));
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
        public IntSortedSet headSet(int to) {
            return new UnmodifiableSortedSet(this.sortedSet.headSet(to));
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
        public IntSortedSet tailSet(int from) {
            return new UnmodifiableSortedSet(this.sortedSet.tailSet(from));
        }

        @Override // it.unimi.dsi.fastutil.ints.IntCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.ints.IntCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
        public IntBidirectionalIterator iterator() {
            return IntIterators.unmodifiable(this.sortedSet.iterator());
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
        public IntBidirectionalIterator iterator(int from) {
            return IntIterators.unmodifiable(this.sortedSet.iterator(from));
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
        public int firstInt() {
            return this.sortedSet.firstInt();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
        public int lastInt() {
            return this.sortedSet.lastInt();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet, java.util.SortedSet
        @Deprecated
        public Integer first() {
            return this.sortedSet.first();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet, java.util.SortedSet
        @Deprecated
        public Integer last() {
            return this.sortedSet.last();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet, java.util.SortedSet
        @Deprecated
        public IntSortedSet subSet(Integer from, Integer to) {
            return new UnmodifiableSortedSet(this.sortedSet.subSet(from, to));
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet, java.util.SortedSet
        @Deprecated
        public IntSortedSet headSet(Integer to) {
            return new UnmodifiableSortedSet(this.sortedSet.headSet(to));
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet, java.util.SortedSet
        @Deprecated
        public IntSortedSet tailSet(Integer from) {
            return new UnmodifiableSortedSet(this.sortedSet.tailSet(from));
        }
    }

    public static IntSortedSet unmodifiable(IntSortedSet s) {
        return new UnmodifiableSortedSet(s);
    }
}
