package it.unimi.dsi.fastutil.objects;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.SortedSet;

/* loaded from: classes4.dex */
public class ObjectRBTreeSet<K> extends AbstractObjectSortedSet<K> implements Serializable, Cloneable, ObjectSortedSet<K> {
    private static final long serialVersionUID = -7046029254386353130L;
    protected transient Comparator<? super K> actualComparator;
    protected int count;
    private transient boolean[] dirPath;
    protected transient Entry<K> firstEntry;
    protected transient Entry<K> lastEntry;
    private transient Entry<K>[] nodePath;
    protected Comparator<? super K> storedComparator;
    protected transient Entry<K> tree;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
    public /* bridge */ /* synthetic */ SortedSet headSet(Object obj) {
        return headSet((ObjectRBTreeSet<K>) obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
    public /* bridge */ /* synthetic */ SortedSet tailSet(Object obj) {
        return tailSet((ObjectRBTreeSet<K>) obj);
    }

    public ObjectRBTreeSet() {
        allocatePaths();
        this.tree = null;
        this.count = 0;
    }

    private void setActualComparator() {
        this.actualComparator = this.storedComparator;
    }

    public ObjectRBTreeSet(Comparator<? super K> c) {
        this();
        this.storedComparator = c;
        setActualComparator();
    }

    public ObjectRBTreeSet(Collection<? extends K> c) {
        this();
        addAll(c);
    }

    public ObjectRBTreeSet(SortedSet<K> s) {
        this(s.comparator());
        addAll(s);
    }

    public ObjectRBTreeSet(ObjectCollection<? extends K> c) {
        this();
        addAll(c);
    }

    public ObjectRBTreeSet(ObjectSortedSet<K> s) {
        this(s.comparator());
        addAll(s);
    }

    public ObjectRBTreeSet(Iterator<? extends K> i) {
        allocatePaths();
        while (i.hasNext()) {
            add(i.next());
        }
    }

    public ObjectRBTreeSet(K[] a, int offset, int length, Comparator<? super K> c) {
        this(c);
        ObjectArrays.ensureOffsetLength(a, offset, length);
        for (int i = 0; i < length; i++) {
            add(a[offset + i]);
        }
    }

    public ObjectRBTreeSet(K[] a, int offset, int length) {
        this(a, offset, length, null);
    }

    public ObjectRBTreeSet(K[] a) {
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

    public ObjectRBTreeSet(K[] a, Comparator<? super K> c) {
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

    final int compare(K k1, K k2) {
        return this.actualComparator == null ? ((Comparable) k1).compareTo(k2) : this.actualComparator.compare(k1, k2);
    }

    private Entry<K> findKey(K k) {
        Entry<K> e = this.tree;
        while (e != null) {
            int cmp = compare(k, e.key);
            if (cmp == 0) {
                break;
            }
            e = cmp < 0 ? e.left() : e.right();
        }
        return e;
    }

    final Entry<K> locateKey(K k) {
        Entry<K> e = this.tree;
        Entry<K> last = this.tree;
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

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean add(K k) {
        int i;
        Entry<K> y;
        Entry<K> y2;
        Objects.requireNonNull(k);
        int maxDepth = 0;
        if (this.tree == null) {
            this.count++;
            Entry<K> entry = new Entry<>(k);
            this.firstEntry = entry;
            this.lastEntry = entry;
            this.tree = entry;
        } else {
            Entry<K> p = this.tree;
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
                            Entry<K> e = new Entry<>(k);
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
                        Entry<K> e2 = new Entry<>(k);
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
                    Entry<K> y3 = this.nodePath[i4 - 1].right;
                    if (!this.nodePath[i4 - 1].succ() && !y3.black()) {
                        this.nodePath[i4].black(true);
                        y3.black(true);
                        this.nodePath[i4 - 1].black(false);
                        i4 -= 2;
                    } else {
                        if (this.dirPath[i4]) {
                            Entry<K> x = this.nodePath[i4];
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
                        Entry<K> x2 = this.nodePath[i4 - 1];
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
                    Entry<K> y4 = this.nodePath[i4 - 1].left;
                    if (!this.nodePath[i4 - 1].pred() && !y4.black()) {
                        this.nodePath[i4].black(true);
                        y4.black(true);
                        this.nodePath[i4 - 1].black(false);
                        i4 -= 2;
                    } else {
                        if (this.dirPath[i4]) {
                            y2 = this.nodePath[i4];
                        } else {
                            Entry<K> x3 = this.nodePath[i4];
                            y2 = x3.left;
                            x3.left = y2.right;
                            y2.right = x3;
                            this.nodePath[i4 - 1].right = y2;
                            if (y2.succ()) {
                                y2.succ(false);
                                x3.pred(y2);
                            }
                        }
                        Entry<K> x4 = this.nodePath[i4 - 1];
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
        Entry<K> p2 = this.tree;
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

    /* JADX WARN: Code restructure failed: missing block: B:100:0x026b, code lost:            r8.succ(false);        r8.right.pred(r8);     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x0244, code lost:            if (r8.succ() != false) goto L107;     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x024c, code lost:            if (r8.right.black() == false) goto L110;     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x0273, code lost:            r8.black(r13.nodePath[r2 - 1].black());        r13.nodePath[r2 - 1].black(true);        r8.right.black(true);        r13.nodePath[r2 - 1].right = r8.left;        r8.left = r13.nodePath[r2 - 1];     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x02a0, code lost:            if (r2 >= 2) goto L113;     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x02a2, code lost:            r13.tree = r8;     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x02c2, code lost:            if (r8.pred() == false) goto L162;     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x02c4, code lost:            r8.pred(false);        r13.nodePath[r2 - 1].succ(r8);     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x02ab, code lost:            if (r13.dirPath[r2 - 2] == false) goto L116;     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x02ad, code lost:            r13.nodePath[r2 - 2].right = r8;     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x02b6, code lost:            r13.nodePath[r2 - 2].left = r8;     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x024e, code lost:            r10 = r8.left;        r10.black(true);        r8.black(false);        r8.left = r10.right;        r10.right = r8;        r13.nodePath[r2 - 1].right = r10;        r8 = r10;     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x0269, code lost:            if (r8.succ() == false) goto L110;     */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean remove(java.lang.Object r14) {
        /*
            Method dump skipped, instructions count: 1109
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.objects.ObjectRBTreeSet.remove(java.lang.Object):boolean");
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        return findKey(obj) != null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public K get(Object obj) {
        Entry<K> entry = findKey(obj);
        if (entry == null) {
            return null;
        }
        return entry.key;
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
    public static final class Entry<K> implements Cloneable {
        private static final int BLACK_MASK = 1;
        private static final int PRED_MASK = 1073741824;
        private static final int SUCC_MASK = Integer.MIN_VALUE;
        int info;
        K key;
        Entry<K> left;
        Entry<K> right;

        Entry() {
        }

        Entry(K k) {
            this.key = k;
            this.info = -1073741824;
        }

        Entry<K> left() {
            if ((this.info & 1073741824) != 0) {
                return null;
            }
            return this.left;
        }

        Entry<K> right() {
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

        void pred(Entry<K> pred) {
            this.info |= 1073741824;
            this.left = pred;
        }

        void succ(Entry<K> succ) {
            this.info |= Integer.MIN_VALUE;
            this.right = succ;
        }

        void left(Entry<K> left) {
            this.info &= -1073741825;
            this.left = left;
        }

        void right(Entry<K> right) {
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

        Entry<K> next() {
            Entry<K> next = this.right;
            if ((this.info & Integer.MIN_VALUE) == 0) {
                while ((next.info & 1073741824) == 0) {
                    next = next.left;
                }
            }
            return next;
        }

        Entry<K> prev() {
            Entry<K> prev = this.left;
            if ((this.info & 1073741824) == 0) {
                while ((prev.info & Integer.MIN_VALUE) == 0) {
                    prev = prev.right;
                }
            }
            return prev;
        }

        /* renamed from: clone, reason: merged with bridge method [inline-methods] */
        public Entry<K> m275clone() {
            try {
                Entry<K> c = (Entry) super.clone();
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
            Entry<?> e = (Entry) o;
            return Objects.equals(this.key, e.key);
        }

        public int hashCode() {
            return this.key.hashCode();
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

    @Override // java.util.SortedSet
    public K first() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.firstEntry.key;
    }

    @Override // java.util.SortedSet
    public K last() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.lastEntry.key;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class SetIterator implements ObjectListIterator<K> {
        Entry<K> curr;
        int index = 0;
        Entry<K> next;
        Entry<K> prev;

        SetIterator() {
            this.next = ObjectRBTreeSet.this.firstEntry;
        }

        SetIterator(K k) {
            Entry<K> locateKey = ObjectRBTreeSet.this.locateKey(k);
            this.next = locateKey;
            if (locateKey != null) {
                if (ObjectRBTreeSet.this.compare(this.next.key, k) <= 0) {
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

        @Override // java.util.Iterator, java.util.ListIterator
        public K next() {
            return nextEntry().key;
        }

        @Override // it.unimi.dsi.fastutil.BidirectionalIterator, java.util.ListIterator
        public K previous() {
            return previousEntry().key;
        }

        Entry<K> nextEntry() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Entry<K> entry = this.next;
            this.prev = entry;
            this.curr = entry;
            this.index++;
            updateNext();
            return this.curr;
        }

        Entry<K> previousEntry() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            Entry<K> entry = this.prev;
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

        @Override // it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.Iterator, java.util.ListIterator
        public void remove() {
            if (this.curr == null) {
                throw new IllegalStateException();
            }
            if (this.curr == this.prev) {
                this.index--;
            }
            Entry<K> entry = this.curr;
            this.prev = entry;
            this.next = entry;
            updatePrevious();
            updateNext();
            ObjectRBTreeSet.this.remove(this.curr.key);
            this.curr = null;
        }
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet, it.unimi.dsi.fastutil.objects.AbstractObjectSet, it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
    public ObjectBidirectionalIterator<K> iterator() {
        return new SetIterator();
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet
    public ObjectBidirectionalIterator<K> iterator(K from) {
        return new SetIterator(from);
    }

    @Override // java.util.SortedSet
    public Comparator<? super K> comparator() {
        return this.actualComparator;
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
    public ObjectSortedSet<K> headSet(K to) {
        return new Subset(null, true, to, false);
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
    public ObjectSortedSet<K> tailSet(K from) {
        return new Subset(from, false, null, true);
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
    public ObjectSortedSet<K> subSet(K from, K to) {
        return new Subset(from, false, to, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class Subset extends AbstractObjectSortedSet<K> implements Serializable, ObjectSortedSet<K> {
        private static final long serialVersionUID = -7046029254386353129L;
        boolean bottom;
        K from;
        K to;
        boolean top;

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public /* bridge */ /* synthetic */ SortedSet headSet(Object obj) {
            return headSet((Subset) obj);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public /* bridge */ /* synthetic */ SortedSet tailSet(Object obj) {
            return tailSet((Subset) obj);
        }

        public Subset(K from, boolean bottom, K to, boolean top) {
            if (!bottom && !top && ObjectRBTreeSet.this.compare(from, to) > 0) {
                throw new IllegalArgumentException("Start element (" + from + ") is larger than end element (" + to + ")");
            }
            this.from = from;
            this.bottom = bottom;
            this.to = to;
            this.top = top;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            ObjectRBTreeSet<K>.Subset.SubsetIterator i = new SubsetIterator();
            while (i.hasNext()) {
                i.next();
                i.remove();
            }
        }

        final boolean in(K k) {
            return (this.bottom || ObjectRBTreeSet.this.compare(k, this.from) >= 0) && (this.top || ObjectRBTreeSet.this.compare(k, this.to) < 0);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object k) {
            return in(k) && ObjectRBTreeSet.this.contains(k);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean add(K k) {
            if (in(k)) {
                return ObjectRBTreeSet.this.add(k);
            }
            throw new IllegalArgumentException("Element (" + k + ") out of range [" + (this.bottom ? "-" : String.valueOf(this.from)) + ", " + (this.top ? "-" : String.valueOf(this.to)) + ")");
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object k) {
            if (in(k)) {
                return ObjectRBTreeSet.this.remove(k);
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            ObjectRBTreeSet<K>.Subset.SubsetIterator i = new SubsetIterator();
            int n = 0;
            while (i.hasNext()) {
                n++;
                i.next();
            }
            return n;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return !new SubsetIterator().hasNext();
        }

        @Override // java.util.SortedSet
        public Comparator<? super K> comparator() {
            return ObjectRBTreeSet.this.actualComparator;
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet, it.unimi.dsi.fastutil.objects.AbstractObjectSet, it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        public ObjectBidirectionalIterator<K> iterator() {
            return new SubsetIterator();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet
        public ObjectBidirectionalIterator<K> iterator(K from) {
            return new SubsetIterator(this, from);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public ObjectSortedSet<K> headSet(K to) {
            if (!this.top && ObjectRBTreeSet.this.compare(to, this.to) >= 0) {
                return this;
            }
            return new Subset(this.from, this.bottom, to, false);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public ObjectSortedSet<K> tailSet(K from) {
            if (!this.bottom && ObjectRBTreeSet.this.compare(from, this.from) <= 0) {
                return this;
            }
            return new Subset(from, false, this.to, this.top);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public ObjectSortedSet<K> subSet(K from, K to) {
            if (this.top && this.bottom) {
                return new Subset(from, false, to, false);
            }
            if (!this.top) {
                to = ObjectRBTreeSet.this.compare(to, this.to) < 0 ? to : this.to;
            }
            if (!this.bottom) {
                from = ObjectRBTreeSet.this.compare(from, this.from) > 0 ? from : this.from;
            }
            return (this.top || this.bottom || from != this.from || to != this.to) ? new Subset(from, false, to, false) : this;
        }

        public Entry<K> firstEntry() {
            Entry<K> e;
            if (ObjectRBTreeSet.this.tree == null) {
                return null;
            }
            if (this.bottom) {
                e = ObjectRBTreeSet.this.firstEntry;
            } else {
                e = ObjectRBTreeSet.this.locateKey(this.from);
                if (ObjectRBTreeSet.this.compare(e.key, this.from) < 0) {
                    e = e.next();
                }
            }
            if (e == null || (!this.top && ObjectRBTreeSet.this.compare(e.key, this.to) >= 0)) {
                return null;
            }
            return e;
        }

        public Entry<K> lastEntry() {
            Entry<K> e;
            if (ObjectRBTreeSet.this.tree == null) {
                return null;
            }
            if (this.top) {
                e = ObjectRBTreeSet.this.lastEntry;
            } else {
                e = ObjectRBTreeSet.this.locateKey(this.to);
                if (ObjectRBTreeSet.this.compare(e.key, this.to) >= 0) {
                    e = e.prev();
                }
            }
            if (e == null || (!this.bottom && ObjectRBTreeSet.this.compare(e.key, this.from) < 0)) {
                return null;
            }
            return e;
        }

        @Override // java.util.SortedSet
        public K first() {
            Entry<K> e = firstEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }

        @Override // java.util.SortedSet
        public K last() {
            Entry<K> e = lastEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public final class SubsetIterator extends ObjectRBTreeSet<K>.SetIterator {
            SubsetIterator() {
                super();
                this.next = Subset.this.firstEntry();
            }

            SubsetIterator(Subset subset, K k) {
                this();
                if (this.next != null) {
                    if (subset.bottom || ObjectRBTreeSet.this.compare(k, this.next.key) >= 0) {
                        if (!subset.top) {
                            ObjectRBTreeSet objectRBTreeSet = ObjectRBTreeSet.this;
                            Entry<K> lastEntry = subset.lastEntry();
                            this.prev = lastEntry;
                            if (objectRBTreeSet.compare(k, lastEntry.key) >= 0) {
                                this.next = null;
                                return;
                            }
                        }
                        this.next = ObjectRBTreeSet.this.locateKey(k);
                        if (ObjectRBTreeSet.this.compare(this.next.key, k) <= 0) {
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

            @Override // it.unimi.dsi.fastutil.objects.ObjectRBTreeSet.SetIterator
            void updatePrevious() {
                this.prev = this.prev.prev();
                if (Subset.this.bottom || this.prev == null || ObjectRBTreeSet.this.compare(this.prev.key, Subset.this.from) >= 0) {
                    return;
                }
                this.prev = null;
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectRBTreeSet.SetIterator
            void updateNext() {
                this.next = this.next.next();
                if (Subset.this.top || this.next == null || ObjectRBTreeSet.this.compare(this.next.key, Subset.this.to) < 0) {
                    return;
                }
                this.next = null;
            }
        }
    }

    public Object clone() {
        try {
            ObjectRBTreeSet<K> c = (ObjectRBTreeSet) super.clone();
            c.allocatePaths();
            if (this.count != 0) {
                Entry<K> rp = new Entry<>();
                Entry<K> rq = new Entry<>();
                Entry<K> p = rp;
                rp.left(this.tree);
                Entry<K> q = rq;
                rq.pred((Entry) null);
                loop0: while (true) {
                    if (!p.pred()) {
                        Entry<K> e = p.left.m275clone();
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
                        Entry<K> e2 = p.right.m275clone();
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
        ObjectRBTreeSet<K>.SetIterator i = new SetIterator();
        s.defaultWriteObject();
        while (true) {
            int n2 = n - 1;
            if (n == 0) {
                return;
            }
            s.writeObject(i.next());
            n = n2;
        }
    }

    private Entry<K> readTree(ObjectInputStream objectInputStream, int i, Entry<K> entry, Entry<K> entry2) throws IOException, ClassNotFoundException {
        if (i == 1) {
            Entry<K> entry3 = new Entry<>(objectInputStream.readObject());
            entry3.pred(entry);
            entry3.succ(entry2);
            entry3.black(true);
            return entry3;
        }
        if (i == 2) {
            Entry<K> entry4 = new Entry<>(objectInputStream.readObject());
            entry4.black(true);
            entry4.right(new Entry<>(objectInputStream.readObject()));
            entry4.right.pred(entry4);
            entry4.pred(entry);
            entry4.right.succ(entry2);
            return entry4;
        }
        int i2 = i / 2;
        Entry<K> entry5 = new Entry<>();
        entry5.left(readTree(objectInputStream, (i - i2) - 1, entry, entry5));
        entry5.key = (K) objectInputStream.readObject();
        entry5.black(true);
        entry5.right(readTree(objectInputStream, i2, entry5, entry2));
        if (i + 2 == ((i + 2) & (-(i + 2)))) {
            entry5.right.black(false);
        }
        return entry5;
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        setActualComparator();
        allocatePaths();
        if (this.count != 0) {
            this.tree = readTree(s, this.count, null, null);
            Entry<K> e = this.tree;
            while (e.left() != null) {
                e = e.left();
            }
            this.firstEntry = e;
            Entry<K> e2 = this.tree;
            while (e2.right() != null) {
                e2 = e2.right();
            }
            this.lastEntry = e2;
        }
    }
}
