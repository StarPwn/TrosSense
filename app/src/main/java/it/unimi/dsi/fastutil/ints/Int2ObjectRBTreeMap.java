package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.objects.AbstractObjectCollection;
import it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;

/* loaded from: classes4.dex */
public class Int2ObjectRBTreeMap<V> extends AbstractInt2ObjectSortedMap<V> implements Serializable, Cloneable {
    private static final long serialVersionUID = -7046029254386353129L;
    protected transient IntComparator actualComparator;
    protected int count;
    private transient boolean[] dirPath;
    protected transient ObjectSortedSet<Int2ObjectMap.Entry<V>> entries;
    protected transient Entry<V> firstEntry;
    protected transient IntSortedSet keys;
    protected transient Entry<V> lastEntry;
    protected transient boolean modified;
    private transient Entry<V>[] nodePath;
    protected Comparator<? super Integer> storedComparator;
    protected transient Entry<V> tree;
    protected transient ObjectCollection<V> values;

    public Int2ObjectRBTreeMap() {
        allocatePaths();
        this.tree = null;
        this.count = 0;
    }

    private void setActualComparator() {
        this.actualComparator = IntComparators.asIntComparator(this.storedComparator);
    }

    public Int2ObjectRBTreeMap(Comparator<? super Integer> c) {
        this();
        this.storedComparator = c;
        setActualComparator();
    }

    public Int2ObjectRBTreeMap(Map<? extends Integer, ? extends V> m) {
        this();
        putAll(m);
    }

    public Int2ObjectRBTreeMap(SortedMap<Integer, V> m) {
        this(m.comparator());
        putAll(m);
    }

    public Int2ObjectRBTreeMap(Int2ObjectMap<? extends V> m) {
        this();
        putAll(m);
    }

    public Int2ObjectRBTreeMap(Int2ObjectSortedMap<V> m) {
        this(m.comparator2());
        putAll(m);
    }

    public Int2ObjectRBTreeMap(int[] k, V[] v, Comparator<? super Integer> c) {
        this(c);
        if (k.length != v.length) {
            throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
        }
        for (int i = 0; i < k.length; i++) {
            put(k[i], (int) v[i]);
        }
    }

    public Int2ObjectRBTreeMap(int[] k, V[] v) {
        this(k, v, null);
    }

    final int compare(int k1, int k2) {
        return this.actualComparator == null ? Integer.compare(k1, k2) : this.actualComparator.compare(k1, k2);
    }

    final Entry<V> findKey(int k) {
        Entry<V> e = this.tree;
        while (e != null) {
            int cmp = compare(k, e.key);
            if (cmp == 0) {
                break;
            }
            e = cmp < 0 ? e.left() : e.right();
        }
        return e;
    }

    final Entry<V> locateKey(int k) {
        Entry<V> e = this.tree;
        Entry<V> last = this.tree;
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

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
    public V put(int k, V v) {
        Entry<V> e = add(k);
        V oldValue = e.value;
        e.value = v;
        return oldValue;
    }

    private Entry<V> add(int k) {
        int i;
        Entry<V> e;
        Entry<V> e2;
        Entry<V> y;
        Entry<V> y2;
        this.modified = false;
        int maxDepth = 0;
        if (this.tree == null) {
            this.count++;
            e2 = new Entry<>(k, this.defRetValue);
            this.firstEntry = e2;
            this.lastEntry = e2;
            this.tree = e2;
        } else {
            Entry<V> p = this.tree;
            int i2 = 0;
            while (true) {
                int cmp = compare(k, p.key);
                if (cmp == 0) {
                    while (true) {
                        int i3 = i2 - 1;
                        if (i2 == 0) {
                            return p;
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
                            e = new Entry<>(k, this.defRetValue);
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
                        e = new Entry<>(k, this.defRetValue);
                        if (p.left == null) {
                            this.firstEntry = e;
                        }
                        e.right = p;
                        e.left = p.left;
                        p.left(e);
                    } else {
                        p = p.left;
                        i2 = i;
                    }
                }
            }
            this.modified = true;
            int i4 = i - 1;
            maxDepth = i;
            while (i4 > 0 && !this.nodePath[i4].black()) {
                if (!this.dirPath[i4 - 1]) {
                    Entry<V> y3 = this.nodePath[i4 - 1].right;
                    if (!this.nodePath[i4 - 1].succ() && !y3.black()) {
                        this.nodePath[i4].black(true);
                        y3.black(true);
                        this.nodePath[i4 - 1].black(false);
                        i4 -= 2;
                    } else {
                        if (this.dirPath[i4]) {
                            Entry<V> x = this.nodePath[i4];
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
                        Entry<V> x2 = this.nodePath[i4 - 1];
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
                    Entry<V> y4 = this.nodePath[i4 - 1].left;
                    if (!this.nodePath[i4 - 1].pred() && !y4.black()) {
                        this.nodePath[i4].black(true);
                        y4.black(true);
                        this.nodePath[i4 - 1].black(false);
                        i4 -= 2;
                    } else {
                        if (this.dirPath[i4]) {
                            y2 = this.nodePath[i4];
                        } else {
                            Entry<V> x3 = this.nodePath[i4];
                            y2 = x3.left;
                            x3.left = y2.right;
                            y2.right = x3;
                            this.nodePath[i4 - 1].right = y2;
                            if (y2.succ()) {
                                y2.succ(false);
                                x3.pred(y2);
                            }
                        }
                        Entry<V> x4 = this.nodePath[i4 - 1];
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
            e2 = e;
        }
        this.tree.black(true);
        while (true) {
            int maxDepth2 = maxDepth - 1;
            if (maxDepth == 0) {
                return e2;
            }
            this.nodePath[maxDepth2] = null;
            maxDepth = maxDepth2;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:100:0x02ba, code lost:            r13.nodePath[r2 - 2].left = r8;     */
    /* JADX WARN: Code restructure failed: missing block: B:101:0x0252, code lost:            r10 = r8.left;        r10.black(true);        r8.black(false);        r8.left = r10.right;        r10.right = r8;        r13.nodePath[r2 - 1].right = r10;        r8 = r10;     */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x026d, code lost:            if (r8.succ() == false) goto L111;     */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x026f, code lost:            r8.succ(false);        r8.right.pred(r8);     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x0248, code lost:            if (r8.succ() != false) goto L108;     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x0250, code lost:            if (r8.right.black() == false) goto L111;     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x0277, code lost:            r8.black(r13.nodePath[r2 - 1].black());        r13.nodePath[r2 - 1].black(true);        r8.right.black(true);        r13.nodePath[r2 - 1].right = r8.left;        r8.left = r13.nodePath[r2 - 1];     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x02a4, code lost:            if (r2 >= 2) goto L114;     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x02a6, code lost:            r13.tree = r8;     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x02c6, code lost:            if (r8.pred() == false) goto L163;     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x02c8, code lost:            r8.pred(false);        r13.nodePath[r2 - 1].succ(r8);     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x02af, code lost:            if (r13.dirPath[r2 - 2] == false) goto L117;     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x02b1, code lost:            r13.nodePath[r2 - 2].right = r8;     */
    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public V remove(int r14) {
        /*
            Method dump skipped, instructions count: 1121
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap.remove(int):java.lang.Object");
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap, java.util.Map
    public boolean containsValue(Object v) {
        Int2ObjectRBTreeMap<V>.ValueIterator i = new ValueIterator(this, null);
        int j = this.count;
        while (true) {
            int j2 = j - 1;
            if (j != 0) {
                Object ev = i.next();
                if (Objects.equals(ev, v)) {
                    return true;
                }
                j = j2;
            } else {
                return false;
            }
        }
    }

    @Override // it.unimi.dsi.fastutil.Function, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
    public void clear() {
        this.count = 0;
        this.tree = null;
        this.entries = null;
        this.values = null;
        this.keys = null;
        this.lastEntry = null;
        this.firstEntry = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class Entry<V> extends AbstractInt2ObjectMap.BasicEntry<V> implements Cloneable {
        private static final int BLACK_MASK = 1;
        private static final int PRED_MASK = 1073741824;
        private static final int SUCC_MASK = Integer.MIN_VALUE;
        int info;
        Entry<V> left;
        Entry<V> right;

        Entry() {
            super(0, (Object) null);
        }

        Entry(int k, V v) {
            super(k, v);
            this.info = -1073741824;
        }

        Entry<V> left() {
            if ((this.info & 1073741824) != 0) {
                return null;
            }
            return this.left;
        }

        Entry<V> right() {
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

        void pred(Entry<V> pred) {
            this.info |= 1073741824;
            this.left = pred;
        }

        void succ(Entry<V> succ) {
            this.info |= Integer.MIN_VALUE;
            this.right = succ;
        }

        void left(Entry<V> left) {
            this.info &= -1073741825;
            this.left = left;
        }

        void right(Entry<V> right) {
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

        Entry<V> next() {
            Entry<V> next = this.right;
            if ((this.info & Integer.MIN_VALUE) == 0) {
                while ((next.info & 1073741824) == 0) {
                    next = next.left;
                }
            }
            return next;
        }

        Entry<V> prev() {
            Entry<V> prev = this.left;
            if ((this.info & 1073741824) == 0) {
                while ((prev.info & Integer.MIN_VALUE) == 0) {
                    prev = prev.right;
                }
            }
            return prev;
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap.BasicEntry, java.util.Map.Entry
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        /* renamed from: clone, reason: merged with bridge method [inline-methods] */
        public Entry<V> m226clone() {
            try {
                Entry<V> c = (Entry) super.clone();
                c.key = this.key;
                c.value = this.value;
                c.info = this.info;
                return c;
            } catch (CloneNotSupportedException e) {
                throw new InternalError();
            }
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap.BasicEntry, java.util.Map.Entry
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<Integer, V> e = (Map.Entry) o;
            return this.key == e.getKey().intValue() && Objects.equals(this.value, e.getValue());
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap.BasicEntry, java.util.Map.Entry
        public int hashCode() {
            return this.key ^ (this.value == null ? 0 : this.value.hashCode());
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap.BasicEntry
        public String toString() {
            return this.key + "=>" + this.value;
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap, it.unimi.dsi.fastutil.ints.Int2ObjectFunction, it.unimi.dsi.fastutil.ints.Int2ObjectMap
    public boolean containsKey(int k) {
        return findKey(k) != null;
    }

    @Override // it.unimi.dsi.fastutil.Function, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
    public int size() {
        return this.count;
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap, java.util.Map
    public boolean isEmpty() {
        return this.count == 0;
    }

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
    public V get(int k) {
        Entry<V> e = findKey(k);
        return e == null ? this.defRetValue : e.value;
    }

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
    public int firstIntKey() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.firstEntry.key;
    }

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
    public int lastIntKey() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.lastEntry.key;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class TreeIterator {
        Entry<V> curr;
        int index = 0;
        Entry<V> next;
        Entry<V> prev;

        TreeIterator() {
            this.next = Int2ObjectRBTreeMap.this.firstEntry;
        }

        TreeIterator(int k) {
            Entry<V> locateKey = Int2ObjectRBTreeMap.this.locateKey(k);
            this.next = locateKey;
            if (locateKey != null) {
                if (Int2ObjectRBTreeMap.this.compare(this.next.key, k) <= 0) {
                    this.prev = this.next;
                    this.next = this.next.next();
                } else {
                    this.prev = this.next.prev();
                }
            }
        }

        public boolean hasNext() {
            return this.next != null;
        }

        public boolean hasPrevious() {
            return this.prev != null;
        }

        void updateNext() {
            this.next = this.next.next();
        }

        Entry<V> nextEntry() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Entry<V> entry = this.next;
            this.prev = entry;
            this.curr = entry;
            this.index++;
            updateNext();
            return this.curr;
        }

        void updatePrevious() {
            this.prev = this.prev.prev();
        }

        Entry<V> previousEntry() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            Entry<V> entry = this.prev;
            this.next = entry;
            this.curr = entry;
            this.index--;
            updatePrevious();
            return this.curr;
        }

        public int nextIndex() {
            return this.index;
        }

        public int previousIndex() {
            return this.index - 1;
        }

        public void remove() {
            if (this.curr == null) {
                throw new IllegalStateException();
            }
            if (this.curr == this.prev) {
                this.index--;
            }
            Entry<V> entry = this.curr;
            this.prev = entry;
            this.next = entry;
            updatePrevious();
            updateNext();
            Int2ObjectRBTreeMap.this.remove(this.curr.key);
            this.curr = null;
        }

        public int skip(int n) {
            int i;
            int i2 = n;
            while (true) {
                i = i2 - 1;
                if (i2 == 0 || !hasNext()) {
                    break;
                }
                nextEntry();
                i2 = i;
            }
            return (n - i) - 1;
        }

        public int back(int n) {
            int i;
            int i2 = n;
            while (true) {
                i = i2 - 1;
                if (i2 == 0 || !hasPrevious()) {
                    break;
                }
                previousEntry();
                i2 = i;
            }
            return (n - i) - 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class EntryIterator extends Int2ObjectRBTreeMap<V>.TreeIterator implements ObjectListIterator<Int2ObjectMap.Entry<V>> {
        EntryIterator() {
            super();
        }

        EntryIterator(int k) {
            super(k);
        }

        @Override // java.util.Iterator, java.util.ListIterator
        public Int2ObjectMap.Entry<V> next() {
            return nextEntry();
        }

        @Override // it.unimi.dsi.fastutil.BidirectionalIterator, java.util.ListIterator
        public Int2ObjectMap.Entry<V> previous() {
            return previousEntry();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap$1, reason: invalid class name */
    /* loaded from: classes4.dex */
    public class AnonymousClass1 extends AbstractObjectSortedSet<Int2ObjectMap.Entry<V>> {
        final Comparator<? super Int2ObjectMap.Entry<V>> comparator;

        AnonymousClass1() {
            this.comparator = Int2ObjectRBTreeMap.this.actualComparator == null ? new Comparator() { // from class: it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap$1$$ExternalSyntheticLambda0
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    int compare;
                    compare = Integer.compare(((Int2ObjectMap.Entry) obj).getIntKey(), ((Int2ObjectMap.Entry) obj2).getIntKey());
                    return compare;
                }
            } : new Comparator() { // from class: it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap$1$$ExternalSyntheticLambda1
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    return Int2ObjectRBTreeMap.AnonymousClass1.this.m225lambda$$1$itunimidsifastutilintsInt2ObjectRBTreeMap$1((Int2ObjectMap.Entry) obj, (Int2ObjectMap.Entry) obj2);
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$$1$it-unimi-dsi-fastutil-ints-Int2ObjectRBTreeMap$1, reason: not valid java name */
        public /* synthetic */ int m225lambda$$1$itunimidsifastutilintsInt2ObjectRBTreeMap$1(Int2ObjectMap.Entry x, Int2ObjectMap.Entry y) {
            return Int2ObjectRBTreeMap.this.actualComparator.compare(x.getIntKey(), y.getIntKey());
        }

        @Override // java.util.SortedSet
        public Comparator<? super Int2ObjectMap.Entry<V>> comparator() {
            return this.comparator;
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet, it.unimi.dsi.fastutil.objects.AbstractObjectSet, it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        public ObjectBidirectionalIterator<Int2ObjectMap.Entry<V>> iterator() {
            return new EntryIterator();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet
        public ObjectBidirectionalIterator<Int2ObjectMap.Entry<V>> iterator(Int2ObjectMap.Entry<V> from) {
            return new EntryIterator(from.getIntKey());
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            if (o == null || !(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> e = (Map.Entry) o;
            if (e.getKey() == null || !(e.getKey() instanceof Integer)) {
                return false;
            }
            Entry<V> f = Int2ObjectRBTreeMap.this.findKey(((Integer) e.getKey()).intValue());
            return e.equals(f);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            Entry<V> f;
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> e = (Map.Entry) o;
            if (e.getKey() == null || !(e.getKey() instanceof Integer) || (f = Int2ObjectRBTreeMap.this.findKey(((Integer) e.getKey()).intValue())) == null || !Objects.equals(f.getValue(), e.getValue())) {
                return false;
            }
            Int2ObjectRBTreeMap.this.remove(f.key);
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return Int2ObjectRBTreeMap.this.count;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            Int2ObjectRBTreeMap.this.clear();
        }

        @Override // java.util.SortedSet
        public Int2ObjectMap.Entry<V> first() {
            return Int2ObjectRBTreeMap.this.firstEntry;
        }

        @Override // java.util.SortedSet
        public Int2ObjectMap.Entry<V> last() {
            return Int2ObjectRBTreeMap.this.lastEntry;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public ObjectSortedSet<Int2ObjectMap.Entry<V>> subSet(Int2ObjectMap.Entry<V> from, Int2ObjectMap.Entry<V> to) {
            return Int2ObjectRBTreeMap.this.subMap(from.getIntKey(), to.getIntKey()).int2ObjectEntrySet();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public ObjectSortedSet<Int2ObjectMap.Entry<V>> headSet(Int2ObjectMap.Entry<V> to) {
            return Int2ObjectRBTreeMap.this.headMap(to.getIntKey()).int2ObjectEntrySet();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public ObjectSortedSet<Int2ObjectMap.Entry<V>> tailSet(Int2ObjectMap.Entry<V> from) {
            return Int2ObjectRBTreeMap.this.tailMap(from.getIntKey()).int2ObjectEntrySet();
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap, it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
    public ObjectSortedSet<Int2ObjectMap.Entry<V>> int2ObjectEntrySet() {
        if (this.entries == null) {
            this.entries = new AnonymousClass1();
        }
        return this.entries;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class KeyIterator extends Int2ObjectRBTreeMap<V>.TreeIterator implements IntListIterator {
        public KeyIterator() {
            super();
        }

        public KeyIterator(int k) {
            super(k);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt
        public int nextInt() {
            return nextEntry().key;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator
        public int previousInt() {
            return previousEntry().key;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class KeySet extends AbstractInt2ObjectSortedMap<V>.KeySet {
        private KeySet() {
            super();
        }

        /* synthetic */ KeySet(Int2ObjectRBTreeMap x0, AnonymousClass1 x1) {
            this();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectSortedMap.KeySet, it.unimi.dsi.fastutil.ints.AbstractIntSortedSet, it.unimi.dsi.fastutil.ints.AbstractIntSet, it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
        public IntBidirectionalIterator iterator() {
            return new KeyIterator();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectSortedMap.KeySet, it.unimi.dsi.fastutil.ints.IntSortedSet
        public IntBidirectionalIterator iterator(int from) {
            return new KeyIterator(from);
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectSortedMap, it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
    /* renamed from: keySet, reason: avoid collision after fix types in other method */
    public Set<Integer> keySet2() {
        if (this.keys == null) {
            this.keys = new KeySet(this, null);
        }
        return this.keys;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class ValueIterator extends Int2ObjectRBTreeMap<V>.TreeIterator implements ObjectListIterator<V> {
        private ValueIterator() {
            super();
        }

        /* synthetic */ ValueIterator(Int2ObjectRBTreeMap x0, AnonymousClass1 x1) {
            this();
        }

        @Override // java.util.Iterator, java.util.ListIterator
        public V next() {
            return nextEntry().value;
        }

        @Override // it.unimi.dsi.fastutil.BidirectionalIterator, java.util.ListIterator
        public V previous() {
            return previousEntry().value;
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectSortedMap, it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
    public ObjectCollection<V> values() {
        if (this.values == null) {
            this.values = new AbstractObjectCollection<V>() { // from class: it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap.2
                @Override // it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
                public ObjectIterator<V> iterator() {
                    return new ValueIterator(Int2ObjectRBTreeMap.this, null);
                }

                @Override // java.util.AbstractCollection, java.util.Collection
                public boolean contains(Object k) {
                    return Int2ObjectRBTreeMap.this.containsValue(k);
                }

                @Override // java.util.AbstractCollection, java.util.Collection
                public int size() {
                    return Int2ObjectRBTreeMap.this.count;
                }

                @Override // java.util.AbstractCollection, java.util.Collection
                public void clear() {
                    Int2ObjectRBTreeMap.this.clear();
                }
            };
        }
        return this.values;
    }

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap, java.util.SortedMap
    /* renamed from: comparator */
    public Comparator<? super Integer> comparator2() {
        return this.actualComparator;
    }

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
    public Int2ObjectSortedMap<V> headMap(int to) {
        return new Submap(0, true, to, false);
    }

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
    public Int2ObjectSortedMap<V> tailMap(int from) {
        return new Submap(from, false, 0, true);
    }

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
    public Int2ObjectSortedMap<V> subMap(int from, int to) {
        return new Submap(from, false, to, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class Submap extends AbstractInt2ObjectSortedMap<V> implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        boolean bottom;
        protected transient ObjectSortedSet<Int2ObjectMap.Entry<V>> entries;
        int from;
        protected transient IntSortedSet keys;
        int to;
        boolean top;
        protected transient ObjectCollection<V> values;

        public Submap(int from, boolean bottom, int to, boolean top) {
            if (!bottom && !top && Int2ObjectRBTreeMap.this.compare(from, to) > 0) {
                throw new IllegalArgumentException("Start key (" + from + ") is larger than end key (" + to + ")");
            }
            this.from = from;
            this.bottom = bottom;
            this.to = to;
            this.top = top;
            this.defRetValue = Int2ObjectRBTreeMap.this.defRetValue;
        }

        @Override // it.unimi.dsi.fastutil.Function, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        public void clear() {
            Int2ObjectRBTreeMap<V>.Submap.SubmapIterator i = new SubmapIterator();
            while (i.hasNext()) {
                i.nextEntry();
                i.remove();
            }
        }

        final boolean in(int k) {
            return (this.bottom || Int2ObjectRBTreeMap.this.compare(k, this.from) >= 0) && (this.top || Int2ObjectRBTreeMap.this.compare(k, this.to) < 0);
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap, it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public ObjectSortedSet<Int2ObjectMap.Entry<V>> int2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = new AbstractObjectSortedSet<Int2ObjectMap.Entry<V>>() { // from class: it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap.Submap.1
                    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet, it.unimi.dsi.fastutil.objects.AbstractObjectSet, it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
                    public ObjectBidirectionalIterator<Int2ObjectMap.Entry<V>> iterator() {
                        return new SubmapEntryIterator();
                    }

                    @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet
                    public ObjectBidirectionalIterator<Int2ObjectMap.Entry<V>> iterator(Int2ObjectMap.Entry<V> from) {
                        return new SubmapEntryIterator(from.getIntKey());
                    }

                    @Override // java.util.SortedSet
                    public Comparator<? super Int2ObjectMap.Entry<V>> comparator() {
                        return Int2ObjectRBTreeMap.this.int2ObjectEntrySet().comparator();
                    }

                    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                    public boolean contains(Object o) {
                        Entry<V> f;
                        if (!(o instanceof Map.Entry)) {
                            return false;
                        }
                        Map.Entry<?, ?> e = (Map.Entry) o;
                        return e.getKey() != null && (e.getKey() instanceof Integer) && (f = Int2ObjectRBTreeMap.this.findKey(((Integer) e.getKey()).intValue())) != null && Submap.this.in(f.key) && e.equals(f);
                    }

                    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                    public boolean remove(Object o) {
                        if (!(o instanceof Map.Entry)) {
                            return false;
                        }
                        Map.Entry<?, ?> e = (Map.Entry) o;
                        if (e.getKey() == null || !(e.getKey() instanceof Integer)) {
                            return false;
                        }
                        Entry<V> f = Int2ObjectRBTreeMap.this.findKey(((Integer) e.getKey()).intValue());
                        if (f != null && Submap.this.in(f.key)) {
                            Submap.this.remove(f.key);
                        }
                        return f != null;
                    }

                    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                    public int size() {
                        int c = 0;
                        Iterator<?> i = iterator();
                        while (i.hasNext()) {
                            c++;
                            i.next();
                        }
                        return c;
                    }

                    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                    public boolean isEmpty() {
                        return !new SubmapIterator().hasNext();
                    }

                    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                    public void clear() {
                        Submap.this.clear();
                    }

                    @Override // java.util.SortedSet
                    public Int2ObjectMap.Entry<V> first() {
                        return Submap.this.firstEntry();
                    }

                    @Override // java.util.SortedSet
                    public Int2ObjectMap.Entry<V> last() {
                        return Submap.this.lastEntry();
                    }

                    @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
                    public ObjectSortedSet<Int2ObjectMap.Entry<V>> subSet(Int2ObjectMap.Entry<V> from, Int2ObjectMap.Entry<V> to) {
                        return Submap.this.subMap(from.getIntKey(), to.getIntKey()).int2ObjectEntrySet();
                    }

                    @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
                    public ObjectSortedSet<Int2ObjectMap.Entry<V>> headSet(Int2ObjectMap.Entry<V> to) {
                        return Submap.this.headMap(to.getIntKey()).int2ObjectEntrySet();
                    }

                    @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
                    public ObjectSortedSet<Int2ObjectMap.Entry<V>> tailSet(Int2ObjectMap.Entry<V> from) {
                        return Submap.this.tailMap(from.getIntKey()).int2ObjectEntrySet();
                    }
                };
            }
            return this.entries;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public class KeySet extends AbstractInt2ObjectSortedMap<V>.KeySet {
            private KeySet() {
                super();
            }

            /* synthetic */ KeySet(Submap x0, AnonymousClass1 x1) {
                this();
            }

            @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectSortedMap.KeySet, it.unimi.dsi.fastutil.ints.AbstractIntSortedSet, it.unimi.dsi.fastutil.ints.AbstractIntSet, it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
            public IntBidirectionalIterator iterator() {
                return new SubmapKeyIterator();
            }

            @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectSortedMap.KeySet, it.unimi.dsi.fastutil.ints.IntSortedSet
            public IntBidirectionalIterator iterator(int from) {
                return new SubmapKeyIterator(from);
            }
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectSortedMap, it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        /* renamed from: keySet, reason: avoid collision after fix types in other method */
        public Set<Integer> keySet2() {
            if (this.keys == null) {
                this.keys = new KeySet(this, null);
            }
            return this.keys;
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectSortedMap, it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        public ObjectCollection<V> values() {
            if (this.values == null) {
                this.values = new AbstractObjectCollection<V>() { // from class: it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap.Submap.2
                    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
                    public ObjectIterator<V> iterator() {
                        return new SubmapValueIterator(Submap.this, null);
                    }

                    @Override // java.util.AbstractCollection, java.util.Collection
                    public boolean contains(Object k) {
                        return Submap.this.containsValue(k);
                    }

                    @Override // java.util.AbstractCollection, java.util.Collection
                    public int size() {
                        return Submap.this.size();
                    }

                    @Override // java.util.AbstractCollection, java.util.Collection
                    public void clear() {
                        Submap.this.clear();
                    }
                };
            }
            return this.values;
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap, it.unimi.dsi.fastutil.ints.Int2ObjectFunction, it.unimi.dsi.fastutil.ints.Int2ObjectMap
        public boolean containsKey(int k) {
            return in(k) && Int2ObjectRBTreeMap.this.containsKey(k);
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap, java.util.Map
        public boolean containsValue(Object v) {
            Int2ObjectRBTreeMap<V>.Submap.SubmapIterator i = new SubmapIterator();
            while (i.hasNext()) {
                Object ev = i.nextEntry().value;
                if (Objects.equals(ev, v)) {
                    return true;
                }
            }
            return false;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
        public V get(int k) {
            Entry<V> e;
            return (!in(k) || (e = Int2ObjectRBTreeMap.this.findKey(k)) == null) ? this.defRetValue : e.value;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
        public V put(int i, V v) {
            Int2ObjectRBTreeMap.this.modified = false;
            if (in(i)) {
                return Int2ObjectRBTreeMap.this.modified ? this.defRetValue : (V) Int2ObjectRBTreeMap.this.put(i, (int) v);
            }
            throw new IllegalArgumentException("Key (" + i + ") out of range [" + (this.bottom ? "-" : String.valueOf(this.from)) + ", " + (this.top ? "-" : String.valueOf(this.to)) + ")");
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
        public V remove(int i) {
            Int2ObjectRBTreeMap.this.modified = false;
            if (in(i)) {
                return Int2ObjectRBTreeMap.this.modified ? (V) Int2ObjectRBTreeMap.this.remove(i) : this.defRetValue;
            }
            return this.defRetValue;
        }

        @Override // it.unimi.dsi.fastutil.Function, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        public int size() {
            Int2ObjectRBTreeMap<V>.Submap.SubmapIterator i = new SubmapIterator();
            int n = 0;
            while (i.hasNext()) {
                n++;
                i.nextEntry();
            }
            return n;
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap, java.util.Map
        public boolean isEmpty() {
            return !new SubmapIterator().hasNext();
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap, java.util.SortedMap
        /* renamed from: comparator */
        public Comparator<? super Integer> comparator2() {
            return Int2ObjectRBTreeMap.this.actualComparator;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public Int2ObjectSortedMap<V> headMap(int to) {
            if (!this.top && Int2ObjectRBTreeMap.this.compare(to, this.to) >= 0) {
                return this;
            }
            return new Submap(this.from, this.bottom, to, false);
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public Int2ObjectSortedMap<V> tailMap(int from) {
            if (!this.bottom && Int2ObjectRBTreeMap.this.compare(from, this.from) <= 0) {
                return this;
            }
            return new Submap(from, false, this.to, this.top);
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public Int2ObjectSortedMap<V> subMap(int from, int to) {
            if (this.top && this.bottom) {
                return new Submap(from, false, to, false);
            }
            if (!this.top) {
                to = Int2ObjectRBTreeMap.this.compare(to, this.to) < 0 ? to : this.to;
            }
            if (!this.bottom) {
                from = Int2ObjectRBTreeMap.this.compare(from, this.from) > 0 ? from : this.from;
            }
            return (this.top || this.bottom || from != this.from || to != this.to) ? new Submap(from, false, to, false) : this;
        }

        public Entry<V> firstEntry() {
            Entry<V> e;
            if (Int2ObjectRBTreeMap.this.tree == null) {
                return null;
            }
            if (this.bottom) {
                e = Int2ObjectRBTreeMap.this.firstEntry;
            } else {
                e = Int2ObjectRBTreeMap.this.locateKey(this.from);
                if (Int2ObjectRBTreeMap.this.compare(e.key, this.from) < 0) {
                    e = e.next();
                }
            }
            if (e == null || (!this.top && Int2ObjectRBTreeMap.this.compare(e.key, this.to) >= 0)) {
                return null;
            }
            return e;
        }

        public Entry<V> lastEntry() {
            Entry<V> e;
            if (Int2ObjectRBTreeMap.this.tree == null) {
                return null;
            }
            if (this.top) {
                e = Int2ObjectRBTreeMap.this.lastEntry;
            } else {
                e = Int2ObjectRBTreeMap.this.locateKey(this.to);
                if (Int2ObjectRBTreeMap.this.compare(e.key, this.to) >= 0) {
                    e = e.prev();
                }
            }
            if (e == null || (!this.bottom && Int2ObjectRBTreeMap.this.compare(e.key, this.from) < 0)) {
                return null;
            }
            return e;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public int firstIntKey() {
            Entry<V> e = firstEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public int lastIntKey() {
            Entry<V> e = lastEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public class SubmapIterator extends Int2ObjectRBTreeMap<V>.TreeIterator {
            SubmapIterator() {
                super();
                this.next = Submap.this.firstEntry();
            }

            SubmapIterator(Submap submap, int k) {
                this();
                if (this.next != null) {
                    if (submap.bottom || Int2ObjectRBTreeMap.this.compare(k, this.next.key) >= 0) {
                        if (!submap.top) {
                            Int2ObjectRBTreeMap int2ObjectRBTreeMap = Int2ObjectRBTreeMap.this;
                            Entry<V> lastEntry = submap.lastEntry();
                            this.prev = lastEntry;
                            if (int2ObjectRBTreeMap.compare(k, lastEntry.key) >= 0) {
                                this.next = null;
                                return;
                            }
                        }
                        this.next = Int2ObjectRBTreeMap.this.locateKey(k);
                        if (Int2ObjectRBTreeMap.this.compare(this.next.key, k) <= 0) {
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

            @Override // it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap.TreeIterator
            void updatePrevious() {
                this.prev = this.prev.prev();
                if (Submap.this.bottom || this.prev == null || Int2ObjectRBTreeMap.this.compare(this.prev.key, Submap.this.from) >= 0) {
                    return;
                }
                this.prev = null;
            }

            @Override // it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap.TreeIterator
            void updateNext() {
                this.next = this.next.next();
                if (Submap.this.top || this.next == null || Int2ObjectRBTreeMap.this.compare(this.next.key, Submap.this.to) < 0) {
                    return;
                }
                this.next = null;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public class SubmapEntryIterator extends Int2ObjectRBTreeMap<V>.Submap.SubmapIterator implements ObjectListIterator<Int2ObjectMap.Entry<V>> {
            SubmapEntryIterator() {
                super();
            }

            SubmapEntryIterator(int k) {
                super(Submap.this, k);
            }

            @Override // java.util.Iterator, java.util.ListIterator
            public Int2ObjectMap.Entry<V> next() {
                return nextEntry();
            }

            @Override // it.unimi.dsi.fastutil.BidirectionalIterator, java.util.ListIterator
            public Int2ObjectMap.Entry<V> previous() {
                return previousEntry();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public final class SubmapKeyIterator extends Int2ObjectRBTreeMap<V>.Submap.SubmapIterator implements IntListIterator {
            public SubmapKeyIterator() {
                super();
            }

            public SubmapKeyIterator(int from) {
                super(Submap.this, from);
            }

            @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt
            public int nextInt() {
                return nextEntry().key;
            }

            @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator
            public int previousInt() {
                return previousEntry().key;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public final class SubmapValueIterator extends Int2ObjectRBTreeMap<V>.Submap.SubmapIterator implements ObjectListIterator<V> {
            private SubmapValueIterator() {
                super();
            }

            /* synthetic */ SubmapValueIterator(Submap x0, AnonymousClass1 x1) {
                this();
            }

            @Override // java.util.Iterator, java.util.ListIterator
            public V next() {
                return nextEntry().value;
            }

            @Override // it.unimi.dsi.fastutil.BidirectionalIterator, java.util.ListIterator
            public V previous() {
                return previousEntry().value;
            }
        }
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public Int2ObjectRBTreeMap<V> m224clone() {
        try {
            Int2ObjectRBTreeMap<V> c = (Int2ObjectRBTreeMap) super.clone();
            c.keys = null;
            c.values = null;
            c.entries = null;
            c.allocatePaths();
            if (this.count != 0) {
                Entry<V> rp = new Entry<>();
                Entry<V> rq = new Entry<>();
                Entry<V> p = rp;
                rp.left(this.tree);
                Entry<V> q = rq;
                rq.pred((Entry) null);
                loop0: while (true) {
                    if (!p.pred()) {
                        Entry<V> e = p.left.m226clone();
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
                        Entry<V> e2 = p.right.m226clone();
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
        Int2ObjectRBTreeMap<V>.EntryIterator i = new EntryIterator();
        s.defaultWriteObject();
        while (true) {
            int n2 = n - 1;
            if (n != 0) {
                Entry<V> e = i.nextEntry();
                s.writeInt(e.key);
                s.writeObject(e.value);
                n = n2;
            } else {
                return;
            }
        }
    }

    private Entry<V> readTree(ObjectInputStream objectInputStream, int i, Entry<V> entry, Entry<V> entry2) throws IOException, ClassNotFoundException {
        if (i == 1) {
            Entry<V> entry3 = new Entry<>(objectInputStream.readInt(), objectInputStream.readObject());
            entry3.pred(entry);
            entry3.succ(entry2);
            entry3.black(true);
            return entry3;
        }
        if (i == 2) {
            Entry<V> entry4 = new Entry<>(objectInputStream.readInt(), objectInputStream.readObject());
            entry4.black(true);
            entry4.right(new Entry<>(objectInputStream.readInt(), objectInputStream.readObject()));
            entry4.right.pred(entry4);
            entry4.pred(entry);
            entry4.right.succ(entry2);
            return entry4;
        }
        int i2 = i / 2;
        Entry<V> entry5 = new Entry<>();
        entry5.left(readTree(objectInputStream, (i - i2) - 1, entry, entry5));
        entry5.key = objectInputStream.readInt();
        entry5.value = (V) objectInputStream.readObject();
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
            Entry<V> e = this.tree;
            while (e.left() != null) {
                e = e.left();
            }
            this.firstEntry = e;
            Entry<V> e2 = this.tree;
            while (e2.right() != null) {
                e2 = e2.right();
            }
            this.lastEntry = e2;
        }
    }
}
