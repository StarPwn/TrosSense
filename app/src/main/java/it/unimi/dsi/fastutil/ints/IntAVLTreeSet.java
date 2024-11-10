package it.unimi.dsi.fastutil.ints;

import androidx.core.view.InputDeviceCompat;
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
public class IntAVLTreeSet extends AbstractIntSortedSet implements Serializable, Cloneable, IntSortedSet {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final long serialVersionUID = -7046029254386353130L;
    protected transient IntComparator actualComparator;
    protected int count;
    private transient boolean[] dirPath;
    protected transient Entry firstEntry;
    protected transient Entry lastEntry;
    protected Comparator<? super Integer> storedComparator;
    protected transient Entry tree;

    public IntAVLTreeSet() {
        allocatePaths();
        this.tree = null;
        this.count = 0;
    }

    private void setActualComparator() {
        this.actualComparator = IntComparators.asIntComparator(this.storedComparator);
    }

    public IntAVLTreeSet(Comparator<? super Integer> c) {
        this();
        this.storedComparator = c;
        setActualComparator();
    }

    public IntAVLTreeSet(Collection<? extends Integer> c) {
        this();
        addAll(c);
    }

    public IntAVLTreeSet(SortedSet<Integer> s) {
        this(s.comparator());
        addAll(s);
    }

    public IntAVLTreeSet(IntCollection c) {
        this();
        addAll(c);
    }

    public IntAVLTreeSet(IntSortedSet s) {
        this(s.comparator2());
        addAll((IntCollection) s);
    }

    public IntAVLTreeSet(IntIterator i) {
        allocatePaths();
        while (i.hasNext()) {
            add(i.nextInt());
        }
    }

    public IntAVLTreeSet(Iterator<?> i) {
        this(IntIterators.asIntIterator(i));
    }

    public IntAVLTreeSet(int[] a, int offset, int length, Comparator<? super Integer> c) {
        this(c);
        IntArrays.ensureOffsetLength(a, offset, length);
        for (int i = 0; i < length; i++) {
            add(a[offset + i]);
        }
    }

    public IntAVLTreeSet(int[] a, int offset, int length) {
        this(a, offset, length, null);
    }

    public IntAVLTreeSet(int[] a) {
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

    public IntAVLTreeSet(int[] a, Comparator<? super Integer> c) {
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

    final int compare(int k1, int k2) {
        return this.actualComparator == null ? Integer.compare(k1, k2) : this.actualComparator.compare(k1, k2);
    }

    private Entry findKey(int k) {
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

    final Entry locateKey(int k) {
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
        this.dirPath = new boolean[48];
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
    public boolean add(int k) {
        Entry e;
        Entry w;
        if (this.tree == null) {
            this.count++;
            Entry entry = new Entry(k);
            this.firstEntry = entry;
            this.lastEntry = entry;
            this.tree = entry;
        } else {
            Entry p = this.tree;
            Entry q = null;
            Entry y = this.tree;
            Entry z = null;
            int i = 0;
            while (true) {
                int cmp = compare(k, p.key);
                if (cmp == 0) {
                    return false;
                }
                if (p.balance() != 0) {
                    i = 0;
                    z = q;
                    y = p;
                }
                boolean[] zArr = this.dirPath;
                int i2 = i + 1;
                boolean z2 = cmp > 0;
                zArr[i] = z2;
                if (z2) {
                    if (p.succ()) {
                        this.count++;
                        e = new Entry(k);
                        if (p.right == null) {
                            this.lastEntry = e;
                        }
                        e.left = p;
                        e.right = p.right;
                        p.right(e);
                    } else {
                        q = p;
                        p = p.right;
                        i = i2;
                    }
                } else if (p.pred()) {
                    this.count++;
                    e = new Entry(k);
                    if (p.left == null) {
                        this.firstEntry = e;
                    }
                    e.right = p;
                    e.left = p.left;
                    p.left(e);
                } else {
                    q = p;
                    p = p.left;
                    i = i2;
                }
            }
            Entry p2 = y;
            int i3 = 0;
            while (p2 != e) {
                if (this.dirPath[i3]) {
                    p2.incBalance();
                } else {
                    p2.decBalance();
                }
                int i4 = i3 + 1;
                p2 = this.dirPath[i3] ? p2.right : p2.left;
                i3 = i4;
            }
            if (y.balance() == -2) {
                Entry x = y.left;
                if (x.balance() == -1) {
                    w = x;
                    if (x.succ()) {
                        x.succ(false);
                        y.pred(x);
                    } else {
                        y.left = x.right;
                    }
                    x.right = y;
                    x.balance(0);
                    y.balance(0);
                } else {
                    if (x.balance() != 1) {
                        throw new AssertionError();
                    }
                    w = x.right;
                    x.right = w.left;
                    w.left = x;
                    y.left = w.right;
                    w.right = y;
                    if (w.balance() == -1) {
                        x.balance(0);
                        y.balance(1);
                    } else if (w.balance() == 0) {
                        x.balance(0);
                        y.balance(0);
                    } else {
                        x.balance(-1);
                        y.balance(0);
                    }
                    w.balance(0);
                    if (w.pred()) {
                        x.succ(w);
                        w.pred(false);
                    }
                    if (w.succ()) {
                        y.pred(w);
                        w.succ(false);
                    }
                }
            } else {
                if (y.balance() != 2) {
                    return true;
                }
                Entry x2 = y.right;
                if (x2.balance() == 1) {
                    w = x2;
                    if (x2.pred()) {
                        x2.pred(false);
                        y.succ(x2);
                    } else {
                        y.right = x2.left;
                    }
                    x2.left = y;
                    x2.balance(0);
                    y.balance(0);
                } else {
                    if (x2.balance() != -1) {
                        throw new AssertionError();
                    }
                    w = x2.left;
                    x2.left = w.right;
                    w.right = x2;
                    y.right = w.left;
                    w.left = y;
                    if (w.balance() == 1) {
                        x2.balance(0);
                        y.balance(-1);
                    } else if (w.balance() == 0) {
                        x2.balance(0);
                        y.balance(0);
                    } else {
                        x2.balance(1);
                        y.balance(0);
                    }
                    w.balance(0);
                    if (w.pred()) {
                        y.succ(w);
                        w.pred(false);
                    }
                    if (w.succ()) {
                        x2.pred(w);
                        w.succ(false);
                    }
                }
            }
            if (z == null) {
                this.tree = w;
            } else if (z.left == y) {
                z.left = w;
            } else {
                z.right = w;
            }
        }
        return true;
    }

    private Entry parent(Entry e) {
        if (e == this.tree) {
            return null;
        }
        Entry y = e;
        Entry x = e;
        while (!y.succ()) {
            if (x.pred()) {
                Entry p = x.left;
                if (p == null || p.right != e) {
                    while (!y.succ()) {
                        y = y.right;
                    }
                    return y.right;
                }
                return p;
            }
            x = x.left;
            y = y.right;
        }
        Entry p2 = y.right;
        if (p2 == null || p2.left != e) {
            while (!x.pred()) {
                x = x.left;
            }
            return x.left;
        }
        return p2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:77:0x02b5, code lost:            r12.count--;     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x02ba, code lost:            return true;     */
    @Override // it.unimi.dsi.fastutil.ints.AbstractIntSet, it.unimi.dsi.fastutil.ints.IntSet
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean remove(int r13) {
        /*
            Method dump skipped, instructions count: 727
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.ints.IntAVLTreeSet.remove(int):boolean");
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
    public boolean contains(int k) {
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
        private static final int BALANCE_MASK = 255;
        private static final int PRED_MASK = 1073741824;
        private static final int SUCC_MASK = Integer.MIN_VALUE;
        int info;
        int key;
        Entry left;
        Entry right;

        Entry() {
        }

        Entry(int k) {
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

        int balance() {
            return (byte) this.info;
        }

        void balance(int level) {
            this.info &= InputDeviceCompat.SOURCE_ANY;
            this.info |= level & 255;
        }

        void incBalance() {
            this.info = (this.info & InputDeviceCompat.SOURCE_ANY) | ((((byte) this.info) + 1) & 255);
        }

        protected void decBalance() {
            this.info = (this.info & InputDeviceCompat.SOURCE_ANY) | ((((byte) this.info) - 1) & 255);
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
        public Entry m227clone() {
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
            return this.key;
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

    @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
    public int firstInt() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.firstEntry.key;
    }

    @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
    public int lastInt() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.lastEntry.key;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class SetIterator implements IntListIterator {
        Entry curr;
        int index = 0;
        Entry next;
        Entry prev;

        SetIterator() {
            this.next = IntAVLTreeSet.this.firstEntry;
        }

        SetIterator(int k) {
            Entry locateKey = IntAVLTreeSet.this.locateKey(k);
            this.next = locateKey;
            if (locateKey != null) {
                if (IntAVLTreeSet.this.compare(this.next.key, k) <= 0) {
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

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt
        public int nextInt() {
            return nextEntry().key;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator
        public int previousInt() {
            return previousEntry().key;
        }

        void updatePrevious() {
            this.prev = this.prev.prev();
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

        @Override // it.unimi.dsi.fastutil.ints.IntListIterator, java.util.Iterator, java.util.ListIterator
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
            IntAVLTreeSet.this.remove(this.curr.key);
            this.curr = null;
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntSortedSet, it.unimi.dsi.fastutil.ints.AbstractIntSet, it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
    public IntBidirectionalIterator iterator() {
        return new SetIterator();
    }

    @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
    public IntBidirectionalIterator iterator(int from) {
        return new SetIterator(from);
    }

    @Override // it.unimi.dsi.fastutil.ints.IntSortedSet, java.util.SortedSet
    /* renamed from: comparator */
    public Comparator<? super Integer> comparator2() {
        return this.actualComparator;
    }

    @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
    public IntSortedSet headSet(int to) {
        return new Subset(0, true, to, false);
    }

    @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
    public IntSortedSet tailSet(int from) {
        return new Subset(from, false, 0, true);
    }

    @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
    public IntSortedSet subSet(int from, int to) {
        return new Subset(from, false, to, false);
    }

    /* loaded from: classes4.dex */
    private final class Subset extends AbstractIntSortedSet implements Serializable, IntSortedSet {
        private static final long serialVersionUID = -7046029254386353129L;
        boolean bottom;
        int from;
        int to;
        boolean top;

        public Subset(int from, boolean bottom, int to, boolean top) {
            if (!bottom && !top && IntAVLTreeSet.this.compare(from, to) > 0) {
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
                i.nextInt();
                i.remove();
            }
        }

        final boolean in(int k) {
            return (this.bottom || IntAVLTreeSet.this.compare(k, this.from) >= 0) && (this.top || IntAVLTreeSet.this.compare(k, this.to) < 0);
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public boolean contains(int k) {
            return in(k) && IntAVLTreeSet.this.contains(k);
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public boolean add(int k) {
            if (in(k)) {
                return IntAVLTreeSet.this.add(k);
            }
            throw new IllegalArgumentException("Element (" + k + ") out of range [" + (this.bottom ? "-" : String.valueOf(this.from)) + ", " + (this.top ? "-" : String.valueOf(this.to)) + ")");
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntSet, it.unimi.dsi.fastutil.ints.IntSet
        public boolean remove(int k) {
            if (in(k)) {
                return IntAVLTreeSet.this.remove(k);
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            SubsetIterator i = new SubsetIterator();
            int n = 0;
            while (i.hasNext()) {
                n++;
                i.nextInt();
            }
            return n;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return !new SubsetIterator().hasNext();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet, java.util.SortedSet
        /* renamed from: comparator, reason: merged with bridge method [inline-methods] */
        public Comparator<? super Integer> comparator2() {
            return IntAVLTreeSet.this.actualComparator;
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntSortedSet, it.unimi.dsi.fastutil.ints.AbstractIntSet, it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
        public IntBidirectionalIterator iterator() {
            return new SubsetIterator();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
        public IntBidirectionalIterator iterator(int from) {
            return new SubsetIterator(this, from);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
        public IntSortedSet headSet(int to) {
            if (!this.top && IntAVLTreeSet.this.compare(to, this.to) >= 0) {
                return this;
            }
            return new Subset(this.from, this.bottom, to, false);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
        public IntSortedSet tailSet(int from) {
            if (!this.bottom && IntAVLTreeSet.this.compare(from, this.from) <= 0) {
                return this;
            }
            return new Subset(from, false, this.to, this.top);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
        public IntSortedSet subSet(int from, int to) {
            if (this.top && this.bottom) {
                return new Subset(from, false, to, false);
            }
            if (!this.top) {
                to = IntAVLTreeSet.this.compare(to, this.to) < 0 ? to : this.to;
            }
            if (!this.bottom) {
                from = IntAVLTreeSet.this.compare(from, this.from) > 0 ? from : this.from;
            }
            return (this.top || this.bottom || from != this.from || to != this.to) ? new Subset(from, false, to, false) : this;
        }

        public Entry firstEntry() {
            Entry e;
            if (IntAVLTreeSet.this.tree == null) {
                return null;
            }
            if (this.bottom) {
                e = IntAVLTreeSet.this.firstEntry;
            } else {
                e = IntAVLTreeSet.this.locateKey(this.from);
                if (IntAVLTreeSet.this.compare(e.key, this.from) < 0) {
                    e = e.next();
                }
            }
            if (e == null || (!this.top && IntAVLTreeSet.this.compare(e.key, this.to) >= 0)) {
                return null;
            }
            return e;
        }

        public Entry lastEntry() {
            Entry e;
            if (IntAVLTreeSet.this.tree == null) {
                return null;
            }
            if (this.top) {
                e = IntAVLTreeSet.this.lastEntry;
            } else {
                e = IntAVLTreeSet.this.locateKey(this.to);
                if (IntAVLTreeSet.this.compare(e.key, this.to) >= 0) {
                    e = e.prev();
                }
            }
            if (e == null || (!this.bottom && IntAVLTreeSet.this.compare(e.key, this.from) < 0)) {
                return null;
            }
            return e;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
        public int firstInt() {
            Entry e = firstEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
        public int lastInt() {
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

            SubsetIterator(Subset subset, int k) {
                this();
                if (this.next != null) {
                    if (subset.bottom || IntAVLTreeSet.this.compare(k, this.next.key) >= 0) {
                        if (!subset.top) {
                            IntAVLTreeSet intAVLTreeSet = IntAVLTreeSet.this;
                            Entry lastEntry = subset.lastEntry();
                            this.prev = lastEntry;
                            if (intAVLTreeSet.compare(k, lastEntry.key) >= 0) {
                                this.next = null;
                                return;
                            }
                        }
                        this.next = IntAVLTreeSet.this.locateKey(k);
                        if (IntAVLTreeSet.this.compare(this.next.key, k) <= 0) {
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

            @Override // it.unimi.dsi.fastutil.ints.IntAVLTreeSet.SetIterator
            void updatePrevious() {
                this.prev = this.prev.prev();
                if (Subset.this.bottom || this.prev == null || IntAVLTreeSet.this.compare(this.prev.key, Subset.this.from) >= 0) {
                    return;
                }
                this.prev = null;
            }

            @Override // it.unimi.dsi.fastutil.ints.IntAVLTreeSet.SetIterator
            void updateNext() {
                this.next = this.next.next();
                if (Subset.this.top || this.next == null || IntAVLTreeSet.this.compare(this.next.key, Subset.this.to) < 0) {
                    return;
                }
                this.next = null;
            }
        }
    }

    public Object clone() {
        try {
            IntAVLTreeSet c = (IntAVLTreeSet) super.clone();
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
                        Entry e = p.left.m227clone();
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
                        Entry e2 = p.right.m227clone();
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
            s.writeInt(i.nextInt());
            n = n2;
        }
    }

    private Entry readTree(ObjectInputStream s, int n, Entry pred, Entry succ) throws IOException, ClassNotFoundException {
        if (n == 1) {
            Entry top = new Entry(s.readInt());
            top.pred(pred);
            top.succ(succ);
            return top;
        }
        if (n == 2) {
            Entry top2 = new Entry(s.readInt());
            top2.right(new Entry(s.readInt()));
            top2.right.pred(top2);
            top2.balance(1);
            top2.pred(pred);
            top2.right.succ(succ);
            return top2;
        }
        int rightN = n / 2;
        int leftN = (n - rightN) - 1;
        Entry top3 = new Entry();
        top3.left(readTree(s, leftN, pred, top3));
        top3.key = s.readInt();
        top3.right(readTree(s, rightN, top3, succ));
        if (n == ((-n) & n)) {
            top3.balance(1);
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
