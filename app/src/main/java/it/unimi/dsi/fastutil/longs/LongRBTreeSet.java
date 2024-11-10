package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.HashCommon;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedSet;

/* loaded from: classes4.dex */
public class LongRBTreeSet extends AbstractLongSortedSet implements Serializable, Cloneable, LongSortedSet {
    private static final long serialVersionUID = -7046029254386353130L;
    protected transient LongComparator actualComparator;
    protected int count;
    private transient boolean[] dirPath;
    protected transient Entry firstEntry;
    protected transient Entry lastEntry;
    private transient Entry[] nodePath;
    protected Comparator<? super Long> storedComparator;
    protected transient Entry tree;

    public LongRBTreeSet() {
        allocatePaths();
        this.tree = null;
        this.count = 0;
    }

    private void setActualComparator() {
        this.actualComparator = LongComparators.asLongComparator(this.storedComparator);
    }

    public LongRBTreeSet(Comparator<? super Long> c) {
        this();
        this.storedComparator = c;
        setActualComparator();
    }

    public LongRBTreeSet(Collection<? extends Long> c) {
        this();
        addAll(c);
    }

    public LongRBTreeSet(SortedSet<Long> s) {
        this(s.comparator());
        addAll(s);
    }

    public LongRBTreeSet(LongCollection c) {
        this();
        addAll(c);
    }

    public LongRBTreeSet(LongSortedSet s) {
        this(s.comparator2());
        addAll((LongCollection) s);
    }

    public LongRBTreeSet(LongIterator i) {
        allocatePaths();
        while (i.hasNext()) {
            add(i.nextLong());
        }
    }

    public LongRBTreeSet(Iterator<?> i) {
        this(LongIterators.asLongIterator(i));
    }

    public LongRBTreeSet(long[] a, int offset, int length, Comparator<? super Long> c) {
        this(c);
        LongArrays.ensureOffsetLength(a, offset, length);
        for (int i = 0; i < length; i++) {
            add(a[offset + i]);
        }
    }

    public LongRBTreeSet(long[] a, int offset, int length) {
        this(a, offset, length, null);
    }

    public LongRBTreeSet(long[] a) {
        this();
        int i = a.length;
        while (true) {
            int i2 = i - 1;
            if (i == 0) {
                return;
            }
            add(a[i2]);
            i = i2;
        }
    }

    public LongRBTreeSet(long[] a, Comparator<? super Long> c) {
        this(c);
        int i = a.length;
        while (true) {
            int i2 = i - 1;
            if (i == 0) {
                return;
            }
            add(a[i2]);
            i = i2;
        }
    }

    final int compare(long k1, long k2) {
        return this.actualComparator == null ? Long.compare(k1, k2) : this.actualComparator.compare(k1, k2);
    }

    private Entry findKey(long k) {
        Entry e = this.tree;
        while (e != null) {
            int cmp = compare(k, e.key);
            if (cmp == 0) {
                break;
            }
            e = cmp < 0 ? e.left() : e.right();
        }
        return e;
    }

    final Entry locateKey(long k) {
        Entry e = this.tree;
        Entry last = this.tree;
        int cmp = 0;
        while (e != null) {
            int compare = compare(k, e.key);
            cmp = compare;
            if (compare == 0) {
                break;
            }
            last = e;
            e = cmp < 0 ? e.left() : e.right();
        }
        return cmp == 0 ? e : last;
    }

    private void allocatePaths() {
        this.dirPath = new boolean[64];
        this.nodePath = new Entry[64];
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
    public boolean add(long k) {
        int i;
        Entry y;
        Entry y2;
        int maxDepth = 0;
        if (this.tree == null) {
            this.count++;
            Entry entry = new Entry(k);
            this.firstEntry = entry;
            this.lastEntry = entry;
            this.tree = entry;
        } else {
            Entry p = this.tree;
            int i2 = 0;
            while (true) {
                int cmp = compare(k, p.key);
                if (cmp == 0) {
                    while (true) {
                        int i3 = i2 - 1;
                        if (i2 == 0) {
                            return false;
                        }
                        this.nodePath[i3] = null;
                        i2 = i3;
                    }
                } else {
                    this.nodePath[i2] = p;
                    boolean[] zArr = this.dirPath;
                    i = i2 + 1;
                    boolean z = cmp > 0;
                    zArr[i2] = z;
                    if (z) {
                        if (p.succ()) {
                            this.count++;
                            Entry e = new Entry(k);
                            if (p.right == null) {
                                this.lastEntry = e;
                            }
                            e.left = p;
                            e.right = p.right;
                            p.right(e);
                        } else {
                            p = p.right;
                            i2 = i;
                        }
                    } else if (p.pred()) {
                        this.count++;
                        Entry e2 = new Entry(k);
                        if (p.left == null) {
                            this.firstEntry = e2;
                        }
                        e2.right = p;
                        e2.left = p.left;
                        p.left(e2);
                    } else {
                        p = p.left;
                        i2 = i;
                    }
                }
            }
            int i4 = i - 1;
            maxDepth = i;
            while (i4 > 0 && !this.nodePath[i4].black()) {
                if (!this.dirPath[i4 - 1]) {
                    Entry y3 = this.nodePath[i4 - 1].right;
                    if (!this.nodePath[i4 - 1].succ() && !y3.black()) {
                        this.nodePath[i4].black(true);
                        y3.black(true);
                        this.nodePath[i4 - 1].black(false);
                        i4 -= 2;
                    } else {
                        if (this.dirPath[i4]) {
                            Entry x = this.nodePath[i4];
                            y = x.right;
                            x.right = y.left;
                            y.left = x;
                            this.nodePath[i4 - 1].left = y;
                            if (y.pred()) {
                                y.pred(false);
                                x.succ(y);
                            }
                        } else {
                            y = this.nodePath[i4];
                        }
                        Entry x2 = this.nodePath[i4 - 1];
                        x2.black(false);
                        y.black(true);
                        x2.left = y.right;
                        y.right = x2;
                        if (i4 < 2) {
                            this.tree = y;
                        } else if (this.dirPath[i4 - 2]) {
                            this.nodePath[i4 - 2].right = y;
                        } else {
                            this.nodePath[i4 - 2].left = y;
                        }
                        if (y.succ()) {
                            y.succ(false);
                            x2.pred(y);
                        }
                    }
                } else {
                    Entry y4 = this.nodePath[i4 - 1].left;
                    if (!this.nodePath[i4 - 1].pred() && !y4.black()) {
                        this.nodePath[i4].black(true);
                        y4.black(true);
                        this.nodePath[i4 - 1].black(false);
                        i4 -= 2;
                    } else {
                        if (this.dirPath[i4]) {
                            y2 = this.nodePath[i4];
                        } else {
                            Entry x3 = this.nodePath[i4];
                            y2 = x3.left;
                            x3.left = y2.right;
                            y2.right = x3;
                            this.nodePath[i4 - 1].right = y2;
                            if (y2.succ()) {
                                y2.succ(false);
                                x3.pred(y2);
                            }
                        }
                        Entry x4 = this.nodePath[i4 - 1];
                        x4.black(false);
                        y2.black(true);
                        x4.right = y2.left;
                        y2.left = x4;
                        if (i4 < 2) {
                            this.tree = y2;
                        } else if (this.dirPath[i4 - 2]) {
                            this.nodePath[i4 - 2].right = y2;
                        } else {
                            this.nodePath[i4 - 2].left = y2;
                        }
                        if (y2.pred()) {
                            y2.pred(false);
                            x4.succ(y2);
                        }
                    }
                }
            }
        }
        Entry p2 = this.tree;
        p2.black(true);
        while (true) {
            int maxDepth2 = maxDepth - 1;
            if (maxDepth == 0) {
                return true;
            }
            this.nodePath[maxDepth2] = null;
            maxDepth = maxDepth2;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:100:0x026d, code lost:            r10.succ(false);        r10.right.pred(r10);     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x0246, code lost:            if (r10.succ() != false) goto L107;     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x024e, code lost:            if (r10.right.black() == false) goto L110;     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x0275, code lost:            r10.black(r15.nodePath[r3 - 1].black());        r15.nodePath[r3 - 1].black(true);        r10.right.black(true);        r15.nodePath[r3 - 1].right = r10.left;        r10.left = r15.nodePath[r3 - 1];     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x02a2, code lost:            if (r3 >= 2) goto L113;     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x02a4, code lost:            r15.tree = r10;     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x02c4, code lost:            if (r10.pred() == false) goto L162;     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x02c6, code lost:            r10.pred(false);        r15.nodePath[r3 - 1].succ(r10);     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x02ad, code lost:            if (r15.dirPath[r3 - 2] == false) goto L116;     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x02af, code lost:            r15.nodePath[r3 - 2].right = r10;     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x02b8, code lost:            r15.nodePath[r3 - 2].left = r10;     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x0250, code lost:            r12 = r10.left;        r12.black(true);        r10.black(false);        r10.left = r12.right;        r12.right = r10;        r15.nodePath[r3 - 1].right = r12;        r10 = r12;     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x026b, code lost:            if (r10.succ() == false) goto L110;     */
    @Override // it.unimi.dsi.fastutil.longs.AbstractLongSet, it.unimi.dsi.fastutil.longs.LongSet
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean remove(long r16) {
        /*
            Method dump skipped, instructions count: 1111
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.longs.LongRBTreeSet.remove(long):boolean");
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
    public boolean contains(long k) {
        return findKey(k) != null;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public void clear() {
        this.count = 0;
        this.tree = null;
        this.lastEntry = null;
        this.firstEntry = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class Entry implements Cloneable {
        private static final int BLACK_MASK = 1;
        private static final int PRED_MASK = 1073741824;
        private static final int SUCC_MASK = Integer.MIN_VALUE;
        int info;
        long key;
        Entry left;
        Entry right;

        Entry() {
        }

        Entry(long k) {
            this.key = k;
            this.info = -1073741824;
        }

        Entry left() {
            if ((this.info & 1073741824) != 0) {
                return null;
            }
            return this.left;
        }

        Entry right() {
            if ((this.info & Integer.MIN_VALUE) != 0) {
                return null;
            }
            return this.right;
        }

        boolean pred() {
            return (this.info & 1073741824) != 0;
        }

        boolean succ() {
            return (this.info & Integer.MIN_VALUE) != 0;
        }

        void pred(boolean pred) {
            if (!pred) {
                this.info &= -1073741825;
            } else {
                this.info |= 1073741824;
            }
        }

        void succ(boolean succ) {
            if (!succ) {
                this.info &= Integer.MAX_VALUE;
            } else {
                this.info |= Integer.MIN_VALUE;
            }
        }

        void pred(Entry pred) {
            this.info |= 1073741824;
            this.left = pred;
        }

        void succ(Entry succ) {
            this.info |= Integer.MIN_VALUE;
            this.right = succ;
        }

        void left(Entry left) {
            this.info &= -1073741825;
            this.left = left;
        }

        void right(Entry right) {
            this.info &= Integer.MAX_VALUE;
            this.right = right;
        }

        boolean black() {
            return (this.info & 1) != 0;
        }

        void black(boolean black) {
            if (!black) {
                this.info &= -2;
            } else {
                this.info |= 1;
            }
        }

        Entry next() {
            Entry next = this.right;
            if ((this.info & Integer.MIN_VALUE) == 0) {
                while ((next.info & 1073741824) == 0) {
                    next = next.left;
                }
            }
            return next;
        }

        Entry prev() {
            Entry prev = this.left;
            if ((this.info & 1073741824) == 0) {
                while ((prev.info & Integer.MIN_VALUE) == 0) {
                    prev = prev.right;
                }
            }
            return prev;
        }

        /* renamed from: clone, reason: merged with bridge method [inline-methods] */
        public Entry m254clone() {
            try {
                Entry c = (Entry) super.clone();
                c.key = this.key;
                c.info = this.info;
                return c;
            } catch (CloneNotSupportedException e) {
                throw new InternalError();
            }
        }

        public boolean equals(Object o) {
            if (!(o instanceof Entry)) {
                return false;
            }
            Entry e = (Entry) o;
            return this.key == e.key;
        }

        public int hashCode() {
            return HashCommon.long2int(this.key);
        }

        public String toString() {
            return String.valueOf(this.key);
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.count;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean isEmpty() {
        return this.count == 0;
    }

    @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
    public long firstLong() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.firstEntry.key;
    }

    @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
    public long lastLong() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.lastEntry.key;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class SetIterator implements LongListIterator {
        Entry curr;
        int index = 0;
        Entry next;
        Entry prev;

        SetIterator() {
            this.next = LongRBTreeSet.this.firstEntry;
        }

        SetIterator(long k) {
            Entry locateKey = LongRBTreeSet.this.locateKey(k);
            this.next = locateKey;
            if (locateKey != null) {
                if (LongRBTreeSet.this.compare(this.next.key, k) <= 0) {
                    this.prev = this.next;
                    this.next = this.next.next();
                } else {
                    this.prev = this.next.prev();
                }
            }
        }

        @Override // java.util.Iterator, java.util.ListIterator
        public boolean hasNext() {
            return this.next != null;
        }

        @Override // it.unimi.dsi.fastutil.BidirectionalIterator
        public boolean hasPrevious() {
            return this.prev != null;
        }

        void updateNext() {
            this.next = this.next.next();
        }

        void updatePrevious() {
            this.prev = this.prev.prev();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong
        public long nextLong() {
            return nextEntry().key;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongBidirectionalIterator
        public long previousLong() {
            return previousEntry().key;
        }

        Entry nextEntry() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Entry entry = this.next;
            this.prev = entry;
            this.curr = entry;
            this.index++;
            updateNext();
            return this.curr;
        }

        Entry previousEntry() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            Entry entry = this.prev;
            this.next = entry;
            this.curr = entry;
            this.index--;
            updatePrevious();
            return this.curr;
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return this.index;
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return this.index - 1;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongListIterator, java.util.Iterator, java.util.ListIterator
        public void remove() {
            if (this.curr == null) {
                throw new IllegalStateException();
            }
            if (this.curr == this.prev) {
                this.index--;
            }
            Entry entry = this.curr;
            this.prev = entry;
            this.next = entry;
            updatePrevious();
            updateNext();
            LongRBTreeSet.this.remove(this.curr.key);
            this.curr = null;
        }
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongSortedSet, it.unimi.dsi.fastutil.longs.AbstractLongSet, it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
    public LongBidirectionalIterator iterator() {
        return new SetIterator();
    }

    @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
    public LongBidirectionalIterator iterator(long from) {
        return new SetIterator(from);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongSortedSet, java.util.SortedSet
    /* renamed from: comparator */
    public Comparator<? super Long> comparator2() {
        return this.actualComparator;
    }

    @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
    public LongSortedSet headSet(long to) {
        return new Subset(0L, true, to, false);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
    public LongSortedSet tailSet(long from) {
        return new Subset(from, false, 0L, true);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
    public LongSortedSet subSet(long from, long to) {
        return new Subset(from, false, to, false);
    }

    /* loaded from: classes4.dex */
    private final class Subset extends AbstractLongSortedSet implements Serializable, LongSortedSet {
        private static final long serialVersionUID = -7046029254386353129L;
        boolean bottom;
        long from;
        long to;
        boolean top;

        public Subset(long from, boolean bottom, long to, boolean top) {
            if (!bottom && !top && LongRBTreeSet.this.compare(from, to) > 0) {
                throw new IllegalArgumentException("Start element (" + from + ") is larger than end element (" + to + ")");
            }
            this.from = from;
            this.bottom = bottom;
            this.to = to;
            this.top = top;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            SubsetIterator i = new SubsetIterator();
            while (i.hasNext()) {
                i.nextLong();
                i.remove();
            }
        }

        final boolean in(long k) {
            return (this.bottom || LongRBTreeSet.this.compare(k, this.from) >= 0) && (this.top || LongRBTreeSet.this.compare(k, this.to) < 0);
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public boolean contains(long k) {
            return in(k) && LongRBTreeSet.this.contains(k);
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public boolean add(long k) {
            if (in(k)) {
                return LongRBTreeSet.this.add(k);
            }
            throw new IllegalArgumentException("Element (" + k + ") out of range [" + (this.bottom ? "-" : String.valueOf(this.from)) + ", " + (this.top ? "-" : String.valueOf(this.to)) + ")");
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongSet, it.unimi.dsi.fastutil.longs.LongSet
        public boolean remove(long k) {
            if (in(k)) {
                return LongRBTreeSet.this.remove(k);
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            SubsetIterator i = new SubsetIterator();
            int n = 0;
            while (i.hasNext()) {
                n++;
                i.nextLong();
            }
            return n;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return !new SubsetIterator().hasNext();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet, java.util.SortedSet
        /* renamed from: comparator, reason: merged with bridge method [inline-methods] */
        public Comparator<? super Long> comparator2() {
            return LongRBTreeSet.this.actualComparator;
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongSortedSet, it.unimi.dsi.fastutil.longs.AbstractLongSet, it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
        public LongBidirectionalIterator iterator() {
            return new SubsetIterator();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public LongBidirectionalIterator iterator(long from) {
            return new SubsetIterator(this, from);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public LongSortedSet headSet(long to) {
            if (!this.top && LongRBTreeSet.this.compare(to, this.to) >= 0) {
                return this;
            }
            return new Subset(this.from, this.bottom, to, false);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public LongSortedSet tailSet(long from) {
            if (!this.bottom && LongRBTreeSet.this.compare(from, this.from) <= 0) {
                return this;
            }
            return new Subset(from, false, this.to, this.top);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public LongSortedSet subSet(long from, long to) {
            if (this.top && this.bottom) {
                return new Subset(from, false, to, false);
            }
            if (!this.top) {
                to = LongRBTreeSet.this.compare(to, this.to) < 0 ? to : this.to;
            }
            if (!this.bottom) {
                from = LongRBTreeSet.this.compare(from, this.from) > 0 ? from : this.from;
            }
            return (this.top || this.bottom || from != this.from || to != this.to) ? new Subset(from, false, to, false) : this;
        }

        public Entry firstEntry() {
            Entry e;
            if (LongRBTreeSet.this.tree == null) {
                return null;
            }
            if (this.bottom) {
                e = LongRBTreeSet.this.firstEntry;
            } else {
                e = LongRBTreeSet.this.locateKey(this.from);
                if (LongRBTreeSet.this.compare(e.key, this.from) < 0) {
                    e = e.next();
                }
            }
            if (e == null || (!this.top && LongRBTreeSet.this.compare(e.key, this.to) >= 0)) {
                return null;
            }
            return e;
        }

        public Entry lastEntry() {
            Entry e;
            if (LongRBTreeSet.this.tree == null) {
                return null;
            }
            if (this.top) {
                e = LongRBTreeSet.this.lastEntry;
            } else {
                e = LongRBTreeSet.this.locateKey(this.to);
                if (LongRBTreeSet.this.compare(e.key, this.to) >= 0) {
                    e = e.prev();
                }
            }
            if (e == null || (!this.bottom && LongRBTreeSet.this.compare(e.key, this.from) < 0)) {
                return null;
            }
            return e;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public long firstLong() {
            Entry e = firstEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public long lastLong() {
            Entry e = lastEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public final class SubsetIterator extends SetIterator {
            SubsetIterator() {
                super();
                this.next = Subset.this.firstEntry();
            }

            SubsetIterator(Subset subset, long k) {
                this();
                if (this.next != null) {
                    if (subset.bottom || LongRBTreeSet.this.compare(k, this.next.key) >= 0) {
                        if (!subset.top) {
                            LongRBTreeSet longRBTreeSet = LongRBTreeSet.this;
                            Entry lastEntry = subset.lastEntry();
                            this.prev = lastEntry;
                            if (longRBTreeSet.compare(k, lastEntry.key) >= 0) {
                                this.next = null;
                                return;
                            }
                        }
                        this.next = LongRBTreeSet.this.locateKey(k);
                        if (LongRBTreeSet.this.compare(this.next.key, k) <= 0) {
                            this.prev = this.next;
                            this.next = this.next.next();
                            return;
                        } else {
                            this.prev = this.next.prev();
                            return;
                        }
                    }
                    this.prev = null;
                }
            }

            @Override // it.unimi.dsi.fastutil.longs.LongRBTreeSet.SetIterator
            void updatePrevious() {
                this.prev = this.prev.prev();
                if (Subset.this.bottom || this.prev == null || LongRBTreeSet.this.compare(this.prev.key, Subset.this.from) >= 0) {
                    return;
                }
                this.prev = null;
            }

            @Override // it.unimi.dsi.fastutil.longs.LongRBTreeSet.SetIterator
            void updateNext() {
                this.next = this.next.next();
                if (Subset.this.top || this.next == null || LongRBTreeSet.this.compare(this.next.key, Subset.this.to) < 0) {
                    return;
                }
                this.next = null;
            }
        }
    }

    public Object clone() {
        try {
            LongRBTreeSet c = (LongRBTreeSet) super.clone();
            c.allocatePaths();
            if (this.count != 0) {
                Entry rp = new Entry();
                Entry rq = new Entry();
                Entry p = rp;
                rp.left(this.tree);
                Entry q = rq;
                rq.pred((Entry) null);
                loop0: while (true) {
                    if (!p.pred()) {
                        Entry e = p.left.m254clone();
                        e.pred(q.left);
                        e.succ(q);
                        q.left(e);
                        p = p.left;
                        q = q.left;
                    } else {
                        while (p.succ()) {
                            p = p.right;
                            if (p == null) {
                                break loop0;
                            }
                            q = q.right;
                        }
                        p = p.right;
                        q = q.right;
                    }
                    if (!p.succ()) {
                        Entry e2 = p.right.m254clone();
                        e2.succ(q.right);
                        e2.pred(q);
                        q.right(e2);
                    }
                }
                q.right = null;
                c.tree = rq.left;
                c.firstEntry = c.tree;
                while (c.firstEntry.left != null) {
                    c.firstEntry = c.firstEntry.left;
                }
                c.lastEntry = c.tree;
                while (c.lastEntry.right != null) {
                    c.lastEntry = c.lastEntry.right;
                }
                return c;
            }
            return c;
        } catch (CloneNotSupportedException e3) {
            throw new InternalError();
        }
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        int n = this.count;
        SetIterator i = new SetIterator();
        s.defaultWriteObject();
        while (true) {
            int n2 = n - 1;
            if (n == 0) {
                return;
            }
            s.writeLong(i.nextLong());
            n = n2;
        }
    }

    private Entry readTree(ObjectInputStream s, int n, Entry pred, Entry succ) throws IOException, ClassNotFoundException {
        if (n == 1) {
            Entry top = new Entry(s.readLong());
            top.pred(pred);
            top.succ(succ);
            top.black(true);
            return top;
        }
        if (n == 2) {
            Entry top2 = new Entry(s.readLong());
            top2.black(true);
            top2.right(new Entry(s.readLong()));
            top2.right.pred(top2);
            top2.pred(pred);
            top2.right.succ(succ);
            return top2;
        }
        int rightN = n / 2;
        int leftN = (n - rightN) - 1;
        Entry top3 = new Entry();
        top3.left(readTree(s, leftN, pred, top3));
        top3.key = s.readLong();
        top3.black(true);
        top3.right(readTree(s, rightN, top3, succ));
        if (n + 2 == ((n + 2) & (-(n + 2)))) {
            top3.right.black(false);
        }
        return top3;
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        setActualComparator();
        allocatePaths();
        if (this.count != 0) {
            this.tree = readTree(s, this.count, null, null);
            Entry e = this.tree;
            while (e.left() != null) {
                e = e.left();
            }
            this.firstEntry = e;
            Entry e2 = this.tree;
            while (e2.right() != null) {
                e2 = e2.right();
            }
            this.lastEntry = e2;
        }
    }
}
