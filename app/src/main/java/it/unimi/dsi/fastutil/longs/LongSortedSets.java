package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.longs.LongSets;
import java.io.Serializable;
import java.util.Comparator;
import java.util.NoSuchElementException;

/* loaded from: classes4.dex */
public final class LongSortedSets {
    public static final EmptySet EMPTY_SET = new EmptySet();

    private LongSortedSets() {
    }

    /* loaded from: classes4.dex */
    public static class EmptySet extends LongSets.EmptySet implements LongSortedSet, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;

        protected EmptySet() {
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public LongBidirectionalIterator iterator(long from) {
            return LongIterators.EMPTY_ITERATOR;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public LongSortedSet subSet(long from, long to) {
            return LongSortedSets.EMPTY_SET;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public LongSortedSet headSet(long from) {
            return LongSortedSets.EMPTY_SET;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public LongSortedSet tailSet(long to) {
            return LongSortedSets.EMPTY_SET;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public long firstLong() {
            throw new NoSuchElementException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public long lastLong() {
            throw new NoSuchElementException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet, java.util.SortedSet
        /* renamed from: comparator */
        public Comparator<? super Long> comparator2() {
            return null;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet, java.util.SortedSet
        @Deprecated
        public LongSortedSet subSet(Long from, Long to) {
            return LongSortedSets.EMPTY_SET;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet, java.util.SortedSet
        @Deprecated
        public LongSortedSet headSet(Long from) {
            return LongSortedSets.EMPTY_SET;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet, java.util.SortedSet
        @Deprecated
        public LongSortedSet tailSet(Long to) {
            return LongSortedSets.EMPTY_SET;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet, java.util.SortedSet
        @Deprecated
        public Long first() {
            throw new NoSuchElementException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet, java.util.SortedSet
        @Deprecated
        public Long last() {
            throw new NoSuchElementException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSets.EmptySet
        public Object clone() {
            return LongSortedSets.EMPTY_SET;
        }

        private Object readResolve() {
            return LongSortedSets.EMPTY_SET;
        }
    }

    /* loaded from: classes4.dex */
    public static class Singleton extends LongSets.Singleton implements LongSortedSet, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        final LongComparator comparator;

        @Override // it.unimi.dsi.fastutil.longs.LongSets.Singleton, it.unimi.dsi.fastutil.longs.AbstractLongSet, it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
        public /* bridge */ /* synthetic */ LongBidirectionalIterator iterator() {
            return super.iterator();
        }

        protected Singleton(long element, LongComparator comparator) {
            super(element);
            this.comparator = comparator;
        }

        Singleton(long element) {
            this(element, null);
        }

        final int compare(long k1, long k2) {
            return this.comparator == null ? Long.compare(k1, k2) : this.comparator.compare(k1, k2);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public LongBidirectionalIterator iterator(long from) {
            LongBidirectionalIterator i = iterator();
            if (compare(this.element, from) <= 0) {
                i.nextLong();
            }
            return i;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet, java.util.SortedSet
        /* renamed from: comparator */
        public Comparator<? super Long> comparator2() {
            return this.comparator;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSets.Singleton, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
        public LongSpliterator spliterator() {
            return LongSpliterators.singleton(this.element, this.comparator);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public LongSortedSet subSet(long from, long to) {
            return (compare(from, this.element) > 0 || compare(this.element, to) >= 0) ? LongSortedSets.EMPTY_SET : this;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public LongSortedSet headSet(long to) {
            return compare(this.element, to) < 0 ? this : LongSortedSets.EMPTY_SET;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public LongSortedSet tailSet(long from) {
            return compare(from, this.element) <= 0 ? this : LongSortedSets.EMPTY_SET;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public long firstLong() {
            return this.element;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public long lastLong() {
            return this.element;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet, java.util.SortedSet
        @Deprecated
        public LongSortedSet subSet(Long from, Long to) {
            return subSet(from.longValue(), to.longValue());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet, java.util.SortedSet
        @Deprecated
        public LongSortedSet headSet(Long to) {
            return headSet(to.longValue());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet, java.util.SortedSet
        @Deprecated
        public LongSortedSet tailSet(Long from) {
            return tailSet(from.longValue());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet, java.util.SortedSet
        @Deprecated
        public Long first() {
            return Long.valueOf(this.element);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet, java.util.SortedSet
        @Deprecated
        public Long last() {
            return Long.valueOf(this.element);
        }
    }

    public static LongSortedSet singleton(long element) {
        return new Singleton(element);
    }

    public static LongSortedSet singleton(long element, LongComparator comparator) {
        return new Singleton(element, comparator);
    }

    public static LongSortedSet singleton(Object element) {
        return new Singleton(((Long) element).longValue());
    }

    public static LongSortedSet singleton(Object element, LongComparator comparator) {
        return new Singleton(((Long) element).longValue(), comparator);
    }

    /* loaded from: classes4.dex */
    public static class SynchronizedSortedSet extends LongSets.SynchronizedSet implements LongSortedSet, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final LongSortedSet sortedSet;

        protected SynchronizedSortedSet(LongSortedSet s, Object sync) {
            super(s, sync);
            this.sortedSet = s;
        }

        protected SynchronizedSortedSet(LongSortedSet s) {
            super(s);
            this.sortedSet = s;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet, java.util.SortedSet
        /* renamed from: comparator */
        public Comparator<? super Long> comparator2() {
            Comparator<? super Long> comparator2;
            synchronized (this.sync) {
                comparator2 = this.sortedSet.comparator2();
            }
            return comparator2;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public LongSortedSet subSet(long from, long to) {
            return new SynchronizedSortedSet(this.sortedSet.subSet(from, to), this.sync);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public LongSortedSet headSet(long to) {
            return new SynchronizedSortedSet(this.sortedSet.headSet(to), this.sync);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public LongSortedSet tailSet(long from) {
            return new SynchronizedSortedSet(this.sortedSet.tailSet(from), this.sync);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.SynchronizedCollection, it.unimi.dsi.fastutil.longs.LongCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
        public LongBidirectionalIterator iterator() {
            return this.sortedSet.iterator();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public LongBidirectionalIterator iterator(long from) {
            return this.sortedSet.iterator(from);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public long firstLong() {
            long firstLong;
            synchronized (this.sync) {
                firstLong = this.sortedSet.firstLong();
            }
            return firstLong;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public long lastLong() {
            long lastLong;
            synchronized (this.sync) {
                lastLong = this.sortedSet.lastLong();
            }
            return lastLong;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet, java.util.SortedSet
        @Deprecated
        public Long first() {
            Long first;
            synchronized (this.sync) {
                first = this.sortedSet.first();
            }
            return first;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet, java.util.SortedSet
        @Deprecated
        public Long last() {
            Long last;
            synchronized (this.sync) {
                last = this.sortedSet.last();
            }
            return last;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet, java.util.SortedSet
        @Deprecated
        public LongSortedSet subSet(Long from, Long to) {
            return new SynchronizedSortedSet(this.sortedSet.subSet(from, to), this.sync);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet, java.util.SortedSet
        @Deprecated
        public LongSortedSet headSet(Long to) {
            return new SynchronizedSortedSet(this.sortedSet.headSet(to), this.sync);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet, java.util.SortedSet
        @Deprecated
        public LongSortedSet tailSet(Long from) {
            return new SynchronizedSortedSet(this.sortedSet.tailSet(from), this.sync);
        }
    }

    public static LongSortedSet synchronize(LongSortedSet s) {
        return new SynchronizedSortedSet(s);
    }

    public static LongSortedSet synchronize(LongSortedSet s, Object sync) {
        return new SynchronizedSortedSet(s, sync);
    }

    /* loaded from: classes4.dex */
    public static class UnmodifiableSortedSet extends LongSets.UnmodifiableSet implements LongSortedSet, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final LongSortedSet sortedSet;

        protected UnmodifiableSortedSet(LongSortedSet s) {
            super(s);
            this.sortedSet = s;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet, java.util.SortedSet
        /* renamed from: comparator */
        public Comparator<? super Long> comparator2() {
            return this.sortedSet.comparator2();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public LongSortedSet subSet(long from, long to) {
            return new UnmodifiableSortedSet(this.sortedSet.subSet(from, to));
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public LongSortedSet headSet(long to) {
            return new UnmodifiableSortedSet(this.sortedSet.headSet(to));
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public LongSortedSet tailSet(long from) {
            return new UnmodifiableSortedSet(this.sortedSet.tailSet(from));
        }

        @Override // it.unimi.dsi.fastutil.longs.LongCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.longs.LongCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
        public LongBidirectionalIterator iterator() {
            return LongIterators.unmodifiable(this.sortedSet.iterator());
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public LongBidirectionalIterator iterator(long from) {
            return LongIterators.unmodifiable(this.sortedSet.iterator(from));
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public long firstLong() {
            return this.sortedSet.firstLong();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public long lastLong() {
            return this.sortedSet.lastLong();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet, java.util.SortedSet
        @Deprecated
        public Long first() {
            return this.sortedSet.first();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet, java.util.SortedSet
        @Deprecated
        public Long last() {
            return this.sortedSet.last();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet, java.util.SortedSet
        @Deprecated
        public LongSortedSet subSet(Long from, Long to) {
            return new UnmodifiableSortedSet(this.sortedSet.subSet(from, to));
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet, java.util.SortedSet
        @Deprecated
        public LongSortedSet headSet(Long to) {
            return new UnmodifiableSortedSet(this.sortedSet.headSet(to));
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet, java.util.SortedSet
        @Deprecated
        public LongSortedSet tailSet(Long from) {
            return new UnmodifiableSortedSet(this.sortedSet.tailSet(from));
        }
    }

    public static LongSortedSet unmodifiable(LongSortedSet s) {
        return new UnmodifiableSortedSet(s);
    }
}
