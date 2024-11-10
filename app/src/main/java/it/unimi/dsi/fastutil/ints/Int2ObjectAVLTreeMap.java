package it.unimi.dsi.fastutil.ints;

import androidx.core.view.InputDeviceCompat;
import it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
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
public class Int2ObjectAVLTreeMap<V> extends AbstractInt2ObjectSortedMap<V> implements Serializable, Cloneable {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final long serialVersionUID = -7046029254386353129L;
    protected transient IntComparator actualComparator;
    protected int count;
    private transient boolean[] dirPath;
    protected transient ObjectSortedSet<Int2ObjectMap.Entry<V>> entries;
    protected transient Entry<V> firstEntry;
    protected transient IntSortedSet keys;
    protected transient Entry<V> lastEntry;
    protected transient boolean modified;
    protected Comparator<? super Integer> storedComparator;
    protected transient Entry<V> tree;
    protected transient ObjectCollection<V> values;

    public Int2ObjectAVLTreeMap() {
        allocatePaths();
        this.tree = null;
        this.count = 0;
    }

    private void setActualComparator() {
        this.actualComparator = IntComparators.asIntComparator(this.storedComparator);
    }

    public Int2ObjectAVLTreeMap(Comparator<? super Integer> c) {
        this();
        this.storedComparator = c;
        setActualComparator();
    }

    public Int2ObjectAVLTreeMap(Map<? extends Integer, ? extends V> m) {
        this();
        putAll(m);
    }

    public Int2ObjectAVLTreeMap(SortedMap<Integer, V> m) {
        this(m.comparator());
        putAll(m);
    }

    public Int2ObjectAVLTreeMap(Int2ObjectMap<? extends V> m) {
        this();
        putAll(m);
    }

    public Int2ObjectAVLTreeMap(Int2ObjectSortedMap<V> m) {
        this(m.comparator2());
        putAll(m);
    }

    public Int2ObjectAVLTreeMap(int[] k, V[] v, Comparator<? super Integer> c) {
        this(c);
        if (k.length != v.length) {
            throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
        }
        for (int i = 0; i < k.length; i++) {
            put(k[i], (int) v[i]);
        }
    }

    public Int2ObjectAVLTreeMap(int[] k, V[] v) {
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
        this.dirPath = new boolean[48];
    }

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
    public V put(int k, V v) {
        Entry<V> e = add(k);
        V oldValue = e.value;
        e.value = v;
        return oldValue;
    }

    private Entry<V> add(int k) {
        Entry<V> e;
        Entry<V> w;
        this.modified = false;
        if (this.tree == null) {
            this.count++;
            Entry<V> e2 = new Entry<>(k, this.defRetValue);
            this.firstEntry = e2;
            this.lastEntry = e2;
            this.tree = e2;
            this.modified = true;
            return e2;
        }
        Entry<V> p = this.tree;
        Entry<V> q = null;
        Entry<V> y = this.tree;
        Entry<V> z = null;
        int i = 0;
        while (true) {
            int cmp = compare(k, p.key);
            if (cmp == 0) {
                return p;
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
                    e = new Entry<>(k, this.defRetValue);
                    this.modified = true;
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
                e = new Entry<>(k, this.defRetValue);
                this.modified = true;
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
        Entry<V> p2 = y;
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
            Entry<V> x = y.left;
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
                Entry<V> w2 = x.right;
                x.right = w2.left;
                w2.left = x;
                y.left = w2.right;
                w2.right = y;
                if (w2.balance() == -1) {
                    x.balance(0);
                    y.balance(1);
                } else if (w2.balance() == 0) {
                    x.balance(0);
                    y.balance(0);
                } else {
                    x.balance(-1);
                    y.balance(0);
                }
                w2.balance(0);
                if (w2.pred()) {
                    x.succ(w2);
                    w2.pred(false);
                }
                if (w2.succ()) {
                    y.pred(w2);
                    w2.succ(false);
                }
                w = w2;
            }
        } else if (y.balance() == 2) {
            Entry<V> x2 = y.right;
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
                Entry<V> w3 = x2.left;
                x2.left = w3.right;
                w3.right = x2;
                y.right = w3.left;
                w3.left = y;
                if (w3.balance() == 1) {
                    x2.balance(0);
                    y.balance(-1);
                } else if (w3.balance() == 0) {
                    x2.balance(0);
                    y.balance(0);
                } else {
                    x2.balance(1);
                    y.balance(0);
                }
                w3.balance(0);
                if (w3.pred()) {
                    y.succ(w3);
                    w3.pred(false);
                }
                if (w3.succ()) {
                    x2.pred(w3);
                    w3.succ(false);
                }
                w = w3;
            }
        } else {
            return e;
        }
        if (z == null) {
            this.tree = w;
        } else if (z.left == y) {
            z.left = w;
        } else {
            z.right = w;
        }
        return e;
    }

    private Entry<V> parent(Entry<V> e) {
        if (e == this.tree) {
            return null;
        }
        Entry<V> y = e;
        Entry<V> x = e;
        while (!y.succ()) {
            if (x.pred()) {
                Entry<V> p = x.left;
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
        Entry<V> p2 = y.right;
        if (p2 == null || p2.left != e) {
            while (!x.pred()) {
                x = x.left;
            }
            return x.left;
        }
        return p2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:80:0x02b9, code lost:            r12.modified = true;        r12.count--;     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x02c2, code lost:            return r1.value;     */
    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public V remove(int r13) {
        /*
            Method dump skipped, instructions count: 739
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap.remove(int):java.lang.Object");
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap, java.util.Map
    public boolean containsValue(Object v) {
        Int2ObjectAVLTreeMap<V>.ValueIterator i = new ValueIterator(this, null);
        int j = this.count;
        while (true) {
            int j2 = j - 1;
            if (j != 0) {
                V ev = i.next();
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
        private static final int BALANCE_MASK = 255;
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
        public Entry<V> m218clone() {
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
            this.next = Int2ObjectAVLTreeMap.this.firstEntry;
        }

        TreeIterator(int k) {
            Entry<V> locateKey = Int2ObjectAVLTreeMap.this.locateKey(k);
            this.next = locateKey;
            if (locateKey != null) {
                if (Int2ObjectAVLTreeMap.this.compare(this.next.key, k) <= 0) {
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
            Int2ObjectAVLTreeMap.this.remove(this.curr.key);
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
    public class EntryIterator extends Int2ObjectAVLTreeMap<V>.TreeIterator implements ObjectListIterator<Int2ObjectMap.Entry<V>> {
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

        @Override // it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.ListIterator
        public void set(Int2ObjectMap.Entry<V> ok) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.ListIterator
        public void add(Int2ObjectMap.Entry<V> ok) {
            throw new UnsupportedOperationException();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap$1, reason: invalid class name */
    /* loaded from: classes4.dex */
    public class AnonymousClass1 extends AbstractObjectSortedSet<Int2ObjectMap.Entry<V>> {
        final Comparator<? super Int2ObjectMap.Entry<V>> comparator;

        AnonymousClass1() {
            this.comparator = Int2ObjectAVLTreeMap.this.actualComparator == null ? new Comparator() { // from class: it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap$1$$ExternalSyntheticLambda0
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    int compare;
                    compare = Integer.compare(((Int2ObjectMap.Entry) obj).getIntKey(), ((Int2ObjectMap.Entry) obj2).getIntKey());
                    return compare;
                }
            } : new Comparator() { // from class: it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap$1$$ExternalSyntheticLambda1
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    return Int2ObjectAVLTreeMap.AnonymousClass1.this.m217lambda$$1$itunimidsifastutilintsInt2ObjectAVLTreeMap$1((Int2ObjectMap.Entry) obj, (Int2ObjectMap.Entry) obj2);
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$$1$it-unimi-dsi-fastutil-ints-Int2ObjectAVLTreeMap$1, reason: not valid java name */
        public /* synthetic */ int m217lambda$$1$itunimidsifastutilintsInt2ObjectAVLTreeMap$1(Int2ObjectMap.Entry x, Int2ObjectMap.Entry y) {
            return Int2ObjectAVLTreeMap.this.actualComparator.compare(x.getIntKey(), y.getIntKey());
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
            Entry<V> f = Int2ObjectAVLTreeMap.this.findKey(((Integer) e.getKey()).intValue());
            return e.equals(f);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            Entry<V> f;
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> e = (Map.Entry) o;
            if (e.getKey() == null || e.getKey() == null || !(e.getKey() instanceof Integer) || (f = Int2ObjectAVLTreeMap.this.findKey(((Integer) e.getKey()).intValue())) == null || !Objects.equals(f.getValue(), e.getValue())) {
                return false;
            }
            Int2ObjectAVLTreeMap.this.remove(f.key);
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return Int2ObjectAVLTreeMap.this.count;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            Int2ObjectAVLTreeMap.this.clear();
        }

        @Override // java.util.SortedSet
        public Int2ObjectMap.Entry<V> first() {
            return Int2ObjectAVLTreeMap.this.firstEntry;
        }

        @Override // java.util.SortedSet
        public Int2ObjectMap.Entry<V> last() {
            return Int2ObjectAVLTreeMap.this.lastEntry;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public ObjectSortedSet<Int2ObjectMap.Entry<V>> subSet(Int2ObjectMap.Entry<V> from, Int2ObjectMap.Entry<V> to) {
            return Int2ObjectAVLTreeMap.this.subMap(from.getIntKey(), to.getIntKey()).int2ObjectEntrySet();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public ObjectSortedSet<Int2ObjectMap.Entry<V>> headSet(Int2ObjectMap.Entry<V> to) {
            return Int2ObjectAVLTreeMap.this.headMap(to.getIntKey()).int2ObjectEntrySet();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public ObjectSortedSet<Int2ObjectMap.Entry<V>> tailSet(Int2ObjectMap.Entry<V> from) {
            return Int2ObjectAVLTreeMap.this.tailMap(from.getIntKey()).int2ObjectEntrySet();
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
    public final class KeyIterator extends Int2ObjectAVLTreeMap<V>.TreeIterator implements IntListIterator {
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

        /* synthetic */ KeySet(Int2ObjectAVLTreeMap x0, AnonymousClass1 x1) {
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
    public final class ValueIterator extends Int2ObjectAVLTreeMap<V>.TreeIterator implements ObjectListIterator<V> {
        private ValueIterator() {
            super();
        }

        /* synthetic */ ValueIterator(Int2ObjectAVLTreeMap x0, AnonymousClass1 x1) {
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
            this.values = new AbstractObjectCollection<V>() { // from class: it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap.2
                @Override // it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
                public ObjectIterator<V> iterator() {
                    return new ValueIterator(Int2ObjectAVLTreeMap.this, null);
                }

                @Override // java.util.AbstractCollection, java.util.Collection
                public boolean contains(Object k) {
                    return Int2ObjectAVLTreeMap.this.containsValue(k);
                }

                @Override // java.util.AbstractCollection, java.util.Collection
                public int size() {
                    return Int2ObjectAVLTreeMap.this.count;
                }

                @Override // java.util.AbstractCollection, java.util.Collection
                public void clear() {
                    Int2ObjectAVLTreeMap.this.clear();
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
            if (!bottom && !top && Int2ObjectAVLTreeMap.this.compare(from, to) > 0) {
                throw new IllegalArgumentException("Start key (" + from + ") is larger than end key (" + to + ")");
            }
            this.from = from;
            this.bottom = bottom;
            this.to = to;
            this.top = top;
            this.defRetValue = Int2ObjectAVLTreeMap.this.defRetValue;
        }

        @Override // it.unimi.dsi.fastutil.Function, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        public void clear() {
            Int2ObjectAVLTreeMap<V>.Submap.SubmapIterator i = new SubmapIterator();
            while (i.hasNext()) {
                i.nextEntry();
                i.remove();
            }
        }

        final boolean in(int k) {
            return (this.bottom || Int2ObjectAVLTreeMap.this.compare(k, this.from) >= 0) && (this.top || Int2ObjectAVLTreeMap.this.compare(k, this.to) < 0);
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap, it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public ObjectSortedSet<Int2ObjectMap.Entry<V>> int2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = new AbstractObjectSortedSet<Int2ObjectMap.Entry<V>>() { // from class: it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap.Submap.1
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
                        return Int2ObjectAVLTreeMap.this.int2ObjectEntrySet().comparator();
                    }

                    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                    public boolean contains(Object o) {
                        Entry<V> f;
                        if (!(o instanceof Map.Entry)) {
                            return false;
                        }
                        Map.Entry<?, ?> e = (Map.Entry) o;
                        return e.getKey() != null && (e.getKey() instanceof Integer) && (f = Int2ObjectAVLTreeMap.this.findKey(((Integer) e.getKey()).intValue())) != null && Submap.this.in(f.key) && e.equals(f);
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
                        Entry<V> f = Int2ObjectAVLTreeMap.this.findKey(((Integer) e.getKey()).intValue());
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
        /* renamed from: keySet, reason: avoid collision after fix types in other method and merged with bridge method [inline-methods] */
        public Set<Integer> keySet2() {
            if (this.keys == null) {
                this.keys = new KeySet(this, null);
            }
            return this.keys;
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectSortedMap, it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        public ObjectCollection<V> values() {
            if (this.values == null) {
                this.values = new AbstractObjectCollection<V>() { // from class: it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap.Submap.2
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
            return in(k) && Int2ObjectAVLTreeMap.this.containsKey(k);
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap, java.util.Map
        public boolean containsValue(Object v) {
            Int2ObjectAVLTreeMap<V>.Submap.SubmapIterator i = new SubmapIterator();
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
            return (!in(k) || (e = Int2ObjectAVLTreeMap.this.findKey(k)) == null) ? this.defRetValue : e.value;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
        public V put(int i, V v) {
            Int2ObjectAVLTreeMap.this.modified = false;
            if (in(i)) {
                return Int2ObjectAVLTreeMap.this.modified ? this.defRetValue : (V) Int2ObjectAVLTreeMap.this.put(i, (int) v);
            }
            throw new IllegalArgumentException("Key (" + i + ") out of range [" + (this.bottom ? "-" : String.valueOf(this.from)) + ", " + (this.top ? "-" : String.valueOf(this.to)) + ")");
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
        public V remove(int i) {
            Int2ObjectAVLTreeMap.this.modified = false;
            if (in(i)) {
                return Int2ObjectAVLTreeMap.this.modified ? (V) Int2ObjectAVLTreeMap.this.remove(i) : this.defRetValue;
            }
            return this.defRetValue;
        }

        @Override // it.unimi.dsi.fastutil.Function, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        public int size() {
            Int2ObjectAVLTreeMap<V>.Submap.SubmapIterator i = new SubmapIterator();
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
        /* renamed from: comparator, reason: merged with bridge method [inline-methods] */
        public Comparator<? super Integer> comparator2() {
            return Int2ObjectAVLTreeMap.this.actualComparator;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public Int2ObjectSortedMap<V> headMap(int to) {
            if (!this.top && Int2ObjectAVLTreeMap.this.compare(to, this.to) >= 0) {
                return this;
            }
            return new Submap(this.from, this.bottom, to, false);
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
        public Int2ObjectSortedMap<V> tailMap(int from) {
            if (!this.bottom && Int2ObjectAVLTreeMap.this.compare(from, this.from) <= 0) {
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
                to = Int2ObjectAVLTreeMap.this.compare(to, this.to) < 0 ? to : this.to;
            }
            if (!this.bottom) {
                from = Int2ObjectAVLTreeMap.this.compare(from, this.from) > 0 ? from : this.from;
            }
            return (this.top || this.bottom || from != this.from || to != this.to) ? new Submap(from, false, to, false) : this;
        }

        public Entry<V> firstEntry() {
            Entry<V> e;
            if (Int2ObjectAVLTreeMap.this.tree == null) {
                return null;
            }
            if (this.bottom) {
                e = Int2ObjectAVLTreeMap.this.firstEntry;
            } else {
                e = Int2ObjectAVLTreeMap.this.locateKey(this.from);
                if (Int2ObjectAVLTreeMap.this.compare(e.key, this.from) < 0) {
                    e = e.next();
                }
            }
            if (e == null || (!this.top && Int2ObjectAVLTreeMap.this.compare(e.key, this.to) >= 0)) {
                return null;
            }
            return e;
        }

        public Entry<V> lastEntry() {
            Entry<V> e;
            if (Int2ObjectAVLTreeMap.this.tree == null) {
                return null;
            }
            if (this.top) {
                e = Int2ObjectAVLTreeMap.this.lastEntry;
            } else {
                e = Int2ObjectAVLTreeMap.this.locateKey(this.to);
                if (Int2ObjectAVLTreeMap.this.compare(e.key, this.to) >= 0) {
                    e = e.prev();
                }
            }
            if (e == null || (!this.bottom && Int2ObjectAVLTreeMap.this.compare(e.key, this.from) < 0)) {
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
        public class SubmapIterator extends Int2ObjectAVLTreeMap<V>.TreeIterator {
            SubmapIterator() {
                super();
                this.next = Submap.this.firstEntry();
            }

            SubmapIterator(Submap submap, int k) {
                this();
                if (this.next != null) {
                    if (submap.bottom || Int2ObjectAVLTreeMap.this.compare(k, this.next.key) >= 0) {
                        if (!submap.top) {
                            Int2ObjectAVLTreeMap int2ObjectAVLTreeMap = Int2ObjectAVLTreeMap.this;
                            Entry<V> lastEntry = submap.lastEntry();
                            this.prev = lastEntry;
                            if (int2ObjectAVLTreeMap.compare(k, lastEntry.key) >= 0) {
                                this.next = null;
                                return;
                            }
                        }
                        this.next = Int2ObjectAVLTreeMap.this.locateKey(k);
                        if (Int2ObjectAVLTreeMap.this.compare(this.next.key, k) <= 0) {
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

            @Override // it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap.TreeIterator
            void updatePrevious() {
                this.prev = this.prev.prev();
                if (Submap.this.bottom || this.prev == null || Int2ObjectAVLTreeMap.this.compare(this.prev.key, Submap.this.from) >= 0) {
                    return;
                }
                this.prev = null;
            }

            @Override // it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap.TreeIterator
            void updateNext() {
                this.next = this.next.next();
                if (Submap.this.top || this.next == null || Int2ObjectAVLTreeMap.this.compare(this.next.key, Submap.this.to) < 0) {
                    return;
                }
                this.next = null;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public class SubmapEntryIterator extends Int2ObjectAVLTreeMap<V>.Submap.SubmapIterator implements ObjectListIterator<Int2ObjectMap.Entry<V>> {
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
        public final class SubmapKeyIterator extends Int2ObjectAVLTreeMap<V>.Submap.SubmapIterator implements IntListIterator {
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
        public final class SubmapValueIterator extends Int2ObjectAVLTreeMap<V>.Submap.SubmapIterator implements ObjectListIterator<V> {
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
    public Int2ObjectAVLTreeMap<V> m216clone() {
        try {
            Int2ObjectAVLTreeMap<V> c = (Int2ObjectAVLTreeMap) super.clone();
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
                        Entry<V> e = p.left.m218clone();
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
                        Entry<V> e2 = p.right.m218clone();
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
        Int2ObjectAVLTreeMap<V>.EntryIterator i = new EntryIterator();
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
            return entry3;
        }
        if (i == 2) {
            Entry<V> entry4 = new Entry<>(objectInputStream.readInt(), objectInputStream.readObject());
            entry4.right(new Entry<>(objectInputStream.readInt(), objectInputStream.readObject()));
            entry4.right.pred(entry4);
            entry4.balance(1);
            entry4.pred(entry);
            entry4.right.succ(entry2);
            return entry4;
        }
        int i2 = i / 2;
        Entry<V> entry5 = new Entry<>();
        entry5.left(readTree(objectInputStream, (i - i2) - 1, entry, entry5));
        entry5.key = objectInputStream.readInt();
        entry5.value = (V) objectInputStream.readObject();
        entry5.right(readTree(objectInputStream, i2, entry5, entry2));
        if (i == ((-i) & i)) {
            entry5.balance(1);
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
