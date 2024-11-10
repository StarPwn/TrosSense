package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.ints.AbstractIntList;
import it.unimi.dsi.fastutil.ints.IntCollections;
import it.unimi.dsi.fastutil.ints.IntIterators;
import it.unimi.dsi.fastutil.ints.IntSpliterators;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/* loaded from: classes4.dex */
public class IntArrayList extends AbstractIntList implements RandomAccess, Cloneable, Serializable {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final int DEFAULT_INITIAL_CAPACITY = 10;
    private static final long serialVersionUID = -7046029254386353130L;
    protected transient int[] a;
    protected int size;

    private static final int[] copyArraySafe(int[] a, int length) {
        return length == 0 ? IntArrays.EMPTY_ARRAY : Arrays.copyOf(a, length);
    }

    private static final int[] copyArrayFromSafe(IntArrayList l) {
        return copyArraySafe(l.a, l.size);
    }

    protected IntArrayList(int[] a, boolean wrapped) {
        this.a = a;
    }

    private void initArrayFromCapacity(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Initial capacity (" + capacity + ") is negative");
        }
        if (capacity != 0) {
            this.a = new int[capacity];
        } else {
            this.a = IntArrays.EMPTY_ARRAY;
        }
    }

    public IntArrayList(int capacity) {
        initArrayFromCapacity(capacity);
    }

    public IntArrayList() {
        this.a = IntArrays.DEFAULT_EMPTY_ARRAY;
    }

    public IntArrayList(Collection<? extends Integer> c) {
        if (c instanceof IntArrayList) {
            this.a = copyArrayFromSafe((IntArrayList) c);
            this.size = this.a.length;
            return;
        }
        initArrayFromCapacity(c.size());
        if (c instanceof IntList) {
            int[] iArr = this.a;
            int size = c.size();
            this.size = size;
            ((IntList) c).getElements(0, iArr, 0, size);
            return;
        }
        this.size = IntIterators.unwrap(IntIterators.asIntIterator(c.iterator()), this.a);
    }

    public IntArrayList(IntCollection c) {
        if (c instanceof IntArrayList) {
            this.a = copyArrayFromSafe((IntArrayList) c);
            this.size = this.a.length;
            return;
        }
        initArrayFromCapacity(c.size());
        if (c instanceof IntList) {
            int[] iArr = this.a;
            int size = c.size();
            this.size = size;
            ((IntList) c).getElements(0, iArr, 0, size);
            return;
        }
        this.size = IntIterators.unwrap(c.iterator(), this.a);
    }

    public IntArrayList(IntList l) {
        if (l instanceof IntArrayList) {
            this.a = copyArrayFromSafe((IntArrayList) l);
            this.size = this.a.length;
            return;
        }
        initArrayFromCapacity(l.size());
        int[] iArr = this.a;
        int size = l.size();
        this.size = size;
        l.getElements(0, iArr, 0, size);
    }

    public IntArrayList(int[] a) {
        this(a, 0, a.length);
    }

    public IntArrayList(int[] a, int offset, int length) {
        this(length);
        System.arraycopy(a, offset, this.a, 0, length);
        this.size = length;
    }

    public IntArrayList(Iterator<? extends Integer> i) {
        this();
        while (i.hasNext()) {
            add(i.next().intValue());
        }
    }

    public IntArrayList(IntIterator i) {
        this();
        while (i.hasNext()) {
            add(i.nextInt());
        }
    }

    public int[] elements() {
        return this.a;
    }

    public static IntArrayList wrap(int[] a, int length) {
        if (length > a.length) {
            throw new IllegalArgumentException("The specified length (" + length + ") is greater than the array size (" + a.length + ")");
        }
        IntArrayList l = new IntArrayList(a, true);
        l.size = length;
        return l;
    }

    public static IntArrayList wrap(int[] a) {
        return wrap(a, a.length);
    }

    public static IntArrayList of() {
        return new IntArrayList();
    }

    public static IntArrayList of(int... init) {
        return wrap(init);
    }

    public static IntArrayList toList(IntStream stream) {
        return (IntArrayList) stream.collect(new Supplier() { // from class: it.unimi.dsi.fastutil.ints.IntArrayList$$ExternalSyntheticLambda3
            @Override // java.util.function.Supplier
            public final Object get() {
                return new IntArrayList();
            }
        }, new IntArrayList$$ExternalSyntheticLambda1(), new IntArrayList$$ExternalSyntheticLambda2());
    }

    public static IntArrayList toListWithExpectedSize(IntStream stream, int expectedSize) {
        if (expectedSize <= 10) {
            return toList(stream);
        }
        return (IntArrayList) stream.collect(new IntCollections.SizeDecreasingSupplier(expectedSize, new IntFunction() { // from class: it.unimi.dsi.fastutil.ints.IntArrayList$$ExternalSyntheticLambda0
            @Override // java.util.function.IntFunction
            public final Object apply(int i) {
                return IntArrayList.lambda$toListWithExpectedSize$0(i);
            }
        }), new IntArrayList$$ExternalSyntheticLambda1(), new IntArrayList$$ExternalSyntheticLambda2());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ IntArrayList lambda$toListWithExpectedSize$0(int size) {
        return size <= 10 ? new IntArrayList() : new IntArrayList(size);
    }

    public void ensureCapacity(int capacity) {
        if (capacity > this.a.length) {
            if (this.a == IntArrays.DEFAULT_EMPTY_ARRAY && capacity <= 10) {
                return;
            }
            this.a = IntArrays.ensureCapacity(this.a, capacity, this.size);
            if (this.size > this.a.length) {
                throw new AssertionError();
            }
        }
    }

    private void grow(int capacity) {
        if (capacity <= this.a.length) {
            return;
        }
        if (this.a != IntArrays.DEFAULT_EMPTY_ARRAY) {
            capacity = (int) Math.max(Math.min(this.a.length + (this.a.length >> 1), 2147483639L), capacity);
        } else if (capacity < 10) {
            capacity = 10;
        }
        this.a = IntArrays.forceCapacity(this.a, capacity, this.size);
        if (this.size > this.a.length) {
            throw new AssertionError();
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
    public void add(int index, int k) {
        ensureIndex(index);
        grow(this.size + 1);
        if (index != this.size) {
            System.arraycopy(this.a, index, this.a, index + 1, this.size - index);
        }
        this.a[index] = k;
        this.size++;
        if (this.size > this.a.length) {
            throw new AssertionError();
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
    public boolean add(int k) {
        grow(this.size + 1);
        int[] iArr = this.a;
        int i = this.size;
        this.size = i + 1;
        iArr[i] = k;
        if (this.size <= this.a.length) {
            return true;
        }
        throw new AssertionError();
    }

    @Override // it.unimi.dsi.fastutil.ints.IntList
    public int getInt(int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        return this.a[index];
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
    public int indexOf(int k) {
        for (int i = 0; i < this.size; i++) {
            if (k == this.a[i]) {
                return i;
            }
        }
        return -1;
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
    public int lastIndexOf(int k) {
        int i = this.size;
        while (true) {
            int i2 = i - 1;
            if (i == 0) {
                return -1;
            }
            if (k == this.a[i2]) {
                return i2;
            }
            i = i2;
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
    public int removeInt(int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        int old = this.a[index];
        this.size--;
        if (index != this.size) {
            System.arraycopy(this.a, index + 1, this.a, index, this.size - index);
        }
        if (this.size > this.a.length) {
            throw new AssertionError();
        }
        return old;
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
    public boolean rem(int k) {
        int index = indexOf(k);
        if (index == -1) {
            return false;
        }
        removeInt(index);
        if (this.size > this.a.length) {
            throw new AssertionError();
        }
        return true;
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
    public int set(int index, int k) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        int old = this.a[index];
        this.a[index] = k;
        return old;
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        this.size = 0;
        if (this.size > this.a.length) {
            throw new AssertionError();
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return this.size;
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
    public void size(int size) {
        if (size > this.a.length) {
            this.a = IntArrays.forceCapacity(this.a, size, this.size);
        }
        if (size > this.size) {
            Arrays.fill(this.a, this.size, size, 0);
        }
        this.size = size;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List, it.unimi.dsi.fastutil.Stack
    public boolean isEmpty() {
        return this.size == 0;
    }

    public void trim() {
        trim(0);
    }

    public void trim(int n) {
        if (n >= this.a.length || this.size == this.a.length) {
            return;
        }
        int[] t = new int[Math.max(n, this.size)];
        System.arraycopy(this.a, 0, t, 0, this.size);
        this.a = t;
        if (this.size > this.a.length) {
            throw new AssertionError();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class SubList extends AbstractIntList.IntRandomAccessSubList {
        private static final long serialVersionUID = -3185226345314976296L;

        protected SubList(int from, int to) {
            super(IntArrayList.this, from, to);
        }

        private int[] getParentArray() {
            return IntArrayList.this.a;
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList.IntSubList, it.unimi.dsi.fastutil.ints.IntList
        public int getInt(int i) {
            ensureRestrictedIndex(i);
            return IntArrayList.this.a[this.from + i];
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public final class SubListIterator extends IntIterators.AbstractIndexBasedListIterator {
            SubListIterator(int index) {
                super(0, index);
            }

            @Override // it.unimi.dsi.fastutil.ints.IntIterators.AbstractIndexBasedIterator
            protected final int get(int i) {
                return IntArrayList.this.a[SubList.this.from + i];
            }

            @Override // it.unimi.dsi.fastutil.ints.IntIterators.AbstractIndexBasedListIterator
            protected final void add(int i, int k) {
                SubList.this.add(i, k);
            }

            @Override // it.unimi.dsi.fastutil.ints.IntIterators.AbstractIndexBasedListIterator
            protected final void set(int i, int k) {
                SubList.this.set(i, k);
            }

            @Override // it.unimi.dsi.fastutil.ints.IntIterators.AbstractIndexBasedIterator
            protected final void remove(int i) {
                SubList.this.removeInt(i);
            }

            @Override // it.unimi.dsi.fastutil.ints.IntIterators.AbstractIndexBasedIterator
            protected final int getMaxPos() {
                return SubList.this.to - SubList.this.from;
            }

            @Override // it.unimi.dsi.fastutil.ints.IntIterators.AbstractIndexBasedIterator, it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt
            public int nextInt() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                int[] iArr = IntArrayList.this.a;
                int i = SubList.this.from;
                int i2 = this.pos;
                this.pos = i2 + 1;
                this.lastReturned = i2;
                return iArr[i + i2];
            }

            @Override // it.unimi.dsi.fastutil.ints.IntIterators.AbstractIndexBasedListIterator, it.unimi.dsi.fastutil.ints.IntBidirectionalIterator
            public int previousInt() {
                if (!hasPrevious()) {
                    throw new NoSuchElementException();
                }
                int[] iArr = IntArrayList.this.a;
                int i = SubList.this.from;
                int i2 = this.pos - 1;
                this.pos = i2;
                this.lastReturned = i2;
                return iArr[i + i2];
            }

            @Override // it.unimi.dsi.fastutil.ints.IntIterators.AbstractIndexBasedIterator, java.util.PrimitiveIterator
            public void forEachRemaining(java.util.function.IntConsumer action) {
                int max = SubList.this.to - SubList.this.from;
                while (this.pos < max) {
                    int[] iArr = IntArrayList.this.a;
                    int i = SubList.this.from;
                    int i2 = this.pos;
                    this.pos = i2 + 1;
                    this.lastReturned = i2;
                    action.accept(iArr[i + i2]);
                }
            }
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList.IntSubList, it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList, java.util.List
        /* renamed from: listIterator, reason: merged with bridge method [inline-methods] */
        public ListIterator<Integer> listIterator2(int index) {
            return new SubListIterator(index);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public final class SubListSpliterator extends IntSpliterators.LateBindingSizeIndexBasedSpliterator {
            SubListSpliterator() {
                super(SubList.this.from);
            }

            private SubListSpliterator(int pos, int maxPos) {
                super(pos, maxPos);
            }

            @Override // it.unimi.dsi.fastutil.ints.IntSpliterators.LateBindingSizeIndexBasedSpliterator
            protected final int getMaxPosFromBackingStore() {
                return SubList.this.to;
            }

            @Override // it.unimi.dsi.fastutil.ints.IntSpliterators.AbstractIndexBasedSpliterator
            protected final int get(int i) {
                return IntArrayList.this.a[i];
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // it.unimi.dsi.fastutil.ints.IntSpliterators.AbstractIndexBasedSpliterator
            public final SubListSpliterator makeForSplit(int pos, int maxPos) {
                return new SubListSpliterator(pos, maxPos);
            }

            @Override // it.unimi.dsi.fastutil.ints.IntSpliterators.AbstractIndexBasedSpliterator, java.util.Spliterator.OfPrimitive
            public boolean tryAdvance(java.util.function.IntConsumer action) {
                if (this.pos >= getMaxPos()) {
                    return false;
                }
                int[] iArr = IntArrayList.this.a;
                int i = this.pos;
                this.pos = i + 1;
                action.accept(iArr[i]);
                return true;
            }

            @Override // it.unimi.dsi.fastutil.ints.IntSpliterators.AbstractIndexBasedSpliterator, java.util.Spliterator.OfPrimitive
            public void forEachRemaining(java.util.function.IntConsumer action) {
                int max = getMaxPos();
                while (this.pos < max) {
                    int[] iArr = IntArrayList.this.a;
                    int i = this.pos;
                    this.pos = i + 1;
                    action.accept(iArr[i]);
                }
            }
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList.IntSubList, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
        public IntSpliterator spliterator() {
            return new SubListSpliterator();
        }

        boolean contentsEquals(int[] otherA, int otherAFrom, int otherATo) {
            if (IntArrayList.this.a == otherA && this.from == otherAFrom && this.to == otherATo) {
                return true;
            }
            if (otherATo - otherAFrom != size()) {
                return false;
            }
            int pos = this.from;
            int otherPos = otherAFrom;
            while (pos < this.to) {
                int pos2 = pos + 1;
                int otherPos2 = otherPos + 1;
                if (IntArrayList.this.a[pos] != otherA[otherPos]) {
                    return false;
                }
                otherPos = otherPos2;
                pos = pos2;
            }
            return true;
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, java.util.Collection, java.util.List
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o == null || !(o instanceof List)) {
                return false;
            }
            if (o instanceof IntArrayList) {
                IntArrayList other = (IntArrayList) o;
                return contentsEquals(other.a, 0, other.size());
            }
            if (o instanceof SubList) {
                SubList other2 = (SubList) o;
                return contentsEquals(other2.getParentArray(), other2.from, other2.to);
            }
            return super.equals(o);
        }

        int contentsCompareTo(int[] otherA, int otherAFrom, int otherATo) {
            if (IntArrayList.this.a == otherA && this.from == otherAFrom && this.to == otherATo) {
                return 0;
            }
            int i = this.from;
            int j = otherAFrom;
            while (i < this.to && i < otherATo) {
                int e1 = IntArrayList.this.a[i];
                int e2 = otherA[j];
                int r = Integer.compare(e1, e2);
                if (r != 0) {
                    return r;
                }
                i++;
                j++;
            }
            if (i < otherATo) {
                return -1;
            }
            return i < this.to ? 1 : 0;
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList.IntSubList, it.unimi.dsi.fastutil.ints.AbstractIntList, java.lang.Comparable
        public int compareTo(List<? extends Integer> l) {
            if (l instanceof IntArrayList) {
                IntArrayList other = (IntArrayList) l;
                return contentsCompareTo(other.a, 0, other.size());
            }
            if (l instanceof SubList) {
                SubList other2 = (SubList) l;
                return contentsCompareTo(other2.getParentArray(), other2.from, other2.to);
            }
            return super.compareTo(l);
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList, java.util.List
    /* renamed from: subList, reason: merged with bridge method [inline-methods] */
    public List<Integer> subList2(int from, int to) {
        if (from == 0 && to == size()) {
            return this;
        }
        ensureIndex(from);
        ensureIndex(to);
        if (from > to) {
            throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        return new SubList(from, to);
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
    public void getElements(int from, int[] a, int offset, int length) {
        IntArrays.ensureOffsetLength(a, offset, length);
        System.arraycopy(this.a, from, a, offset, length);
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
    public void removeElements(int from, int to) {
        it.unimi.dsi.fastutil.Arrays.ensureFromTo(this.size, from, to);
        System.arraycopy(this.a, to, this.a, from, this.size - to);
        this.size -= to - from;
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
    public void addElements(int index, int[] a, int offset, int length) {
        ensureIndex(index);
        IntArrays.ensureOffsetLength(a, offset, length);
        grow(this.size + length);
        System.arraycopy(this.a, index, this.a, index + length, this.size - index);
        System.arraycopy(a, offset, this.a, index, length);
        this.size += length;
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
    public void setElements(int index, int[] a, int offset, int length) {
        ensureIndex(index);
        IntArrays.ensureOffsetLength(a, offset, length);
        if (index + length > this.size) {
            throw new IndexOutOfBoundsException("End index (" + (index + length) + ") is greater than list size (" + this.size + ")");
        }
        System.arraycopy(a, offset, this.a, index, length);
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntIterable
    public void forEach(java.util.function.IntConsumer action) {
        for (int i = 0; i < this.size; i++) {
            action.accept(this.a[i]);
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
    public boolean addAll(int index, IntCollection c) {
        if (c instanceof IntList) {
            return addAll(index, (IntList) c);
        }
        ensureIndex(index);
        int n = c.size();
        if (n == 0) {
            return false;
        }
        grow(this.size + n);
        System.arraycopy(this.a, index, this.a, index + n, this.size - index);
        IntIterator i = c.iterator();
        this.size += n;
        while (true) {
            int n2 = n - 1;
            if (n == 0) {
                break;
            }
            this.a[index] = i.nextInt();
            n = n2;
            index++;
        }
        if (this.size > this.a.length) {
            throw new AssertionError();
        }
        return true;
    }

    @Override // it.unimi.dsi.fastutil.ints.IntList
    public boolean addAll(int index, IntList l) {
        ensureIndex(index);
        int n = l.size();
        if (n == 0) {
            return false;
        }
        grow(this.size + n);
        System.arraycopy(this.a, index, this.a, index + n, this.size - index);
        l.getElements(0, this.a, index, n);
        this.size += n;
        if (this.size > this.a.length) {
            throw new AssertionError();
        }
        return true;
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
    public boolean removeAll(IntCollection c) {
        int[] a = this.a;
        int j = 0;
        for (int i = 0; i < this.size; i++) {
            if (!c.contains(a[i])) {
                a[j] = a[i];
                j++;
            }
        }
        int i2 = this.size;
        boolean modified = i2 != j;
        this.size = j;
        return modified;
    }

    @Override // it.unimi.dsi.fastutil.ints.IntCollection
    public boolean removeIf(java.util.function.IntPredicate filter) {
        int[] a = this.a;
        int j = 0;
        for (int i = 0; i < this.size; i++) {
            if (!filter.test(a[i])) {
                a[j] = a[i];
                j++;
            }
        }
        int i2 = this.size;
        boolean modified = i2 != j;
        this.size = j;
        return modified;
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
    public int[] toArray(int[] a) {
        if (a == null || a.length < this.size) {
            a = Arrays.copyOf(a, this.size);
        }
        System.arraycopy(this.a, 0, a, 0, this.size);
        return a;
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList, java.util.List
    /* renamed from: listIterator */
    public ListIterator<Integer> listIterator2(final int index) {
        ensureIndex(index);
        return new IntListIterator() { // from class: it.unimi.dsi.fastutil.ints.IntArrayList.1
            int last = -1;
            int pos;

            {
                this.pos = index;
            }

            @Override // java.util.Iterator, java.util.ListIterator
            public boolean hasNext() {
                return this.pos < IntArrayList.this.size;
            }

            @Override // it.unimi.dsi.fastutil.BidirectionalIterator
            public boolean hasPrevious() {
                return this.pos > 0;
            }

            @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt
            public int nextInt() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                int[] iArr = IntArrayList.this.a;
                int i = this.pos;
                this.pos = i + 1;
                this.last = i;
                return iArr[i];
            }

            @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator
            public int previousInt() {
                if (!hasPrevious()) {
                    throw new NoSuchElementException();
                }
                int[] iArr = IntArrayList.this.a;
                int i = this.pos - 1;
                this.pos = i;
                this.last = i;
                return iArr[i];
            }

            @Override // java.util.ListIterator
            public int nextIndex() {
                return this.pos;
            }

            @Override // java.util.ListIterator
            public int previousIndex() {
                return this.pos - 1;
            }

            @Override // it.unimi.dsi.fastutil.ints.IntListIterator
            public void add(int k) {
                IntArrayList intArrayList = IntArrayList.this;
                int i = this.pos;
                this.pos = i + 1;
                intArrayList.add(i, k);
                this.last = -1;
            }

            @Override // it.unimi.dsi.fastutil.ints.IntListIterator
            public void set(int k) {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                IntArrayList.this.set(this.last, k);
            }

            @Override // it.unimi.dsi.fastutil.ints.IntListIterator, java.util.Iterator, java.util.ListIterator
            public void remove() {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                IntArrayList.this.removeInt(this.last);
                if (this.last < this.pos) {
                    this.pos--;
                }
                this.last = -1;
            }

            @Override // java.util.PrimitiveIterator
            public void forEachRemaining(java.util.function.IntConsumer action) {
                while (this.pos < IntArrayList.this.size) {
                    int[] iArr = IntArrayList.this.a;
                    int i = this.pos;
                    this.pos = i + 1;
                    this.last = i;
                    action.accept(iArr[i]);
                }
            }

            @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator
            public int back(int n) {
                if (n < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n);
                }
                int remaining = IntArrayList.this.size - this.pos;
                if (n < remaining) {
                    this.pos -= n;
                } else {
                    n = remaining;
                    this.pos = 0;
                }
                this.last = this.pos;
                return n;
            }

            @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator, it.unimi.dsi.fastutil.ints.IntIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
            public int skip(int n) {
                if (n < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n);
                }
                int remaining = IntArrayList.this.size - this.pos;
                if (n < remaining) {
                    this.pos += n;
                } else {
                    n = remaining;
                    this.pos = IntArrayList.this.size;
                }
                this.last = this.pos - 1;
                return n;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class Spliterator implements IntSpliterator {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        boolean hasSplit;
        int max;
        int pos;

        public Spliterator(IntArrayList intArrayList) {
            this(0, intArrayList.size, false);
        }

        private Spliterator(int pos, int max, boolean hasSplit) {
            this.hasSplit = false;
            if (pos > max) {
                throw new AssertionError("pos " + pos + " must be <= max " + max);
            }
            this.pos = pos;
            this.max = max;
            this.hasSplit = hasSplit;
        }

        private int getWorkingMax() {
            return this.hasSplit ? this.max : IntArrayList.this.size;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return 16720;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return getWorkingMax() - this.pos;
        }

        @Override // java.util.Spliterator.OfPrimitive
        public boolean tryAdvance(java.util.function.IntConsumer action) {
            if (this.pos >= getWorkingMax()) {
                return false;
            }
            int[] iArr = IntArrayList.this.a;
            int i = this.pos;
            this.pos = i + 1;
            action.accept(iArr[i]);
            return true;
        }

        @Override // java.util.Spliterator.OfPrimitive
        public void forEachRemaining(java.util.function.IntConsumer action) {
            int max = getWorkingMax();
            while (this.pos < max) {
                action.accept(IntArrayList.this.a[this.pos]);
                this.pos++;
            }
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSpliterator
        public long skip(long n) {
            if (n < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            int max = getWorkingMax();
            if (this.pos >= max) {
                return 0L;
            }
            int remaining = max - this.pos;
            if (n < remaining) {
                this.pos = SafeMath.safeLongToInt(this.pos + n);
                return n;
            }
            long n2 = remaining;
            this.pos = max;
            return n2;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSpliterator, java.util.Spliterator.OfInt, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public IntSpliterator trySplit() {
            int max = getWorkingMax();
            int retLen = (max - this.pos) >> 1;
            if (retLen <= 1) {
                return null;
            }
            this.max = max;
            int myNewPos = this.pos + retLen;
            int oldPos = this.pos;
            this.pos = myNewPos;
            this.hasSplit = true;
            return new Spliterator(oldPos, myNewPos, true);
        }
    }

    @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
    public IntSpliterator spliterator() {
        return new Spliterator(this);
    }

    @Override // it.unimi.dsi.fastutil.ints.IntList
    public void sort(IntComparator comp) {
        if (comp == null) {
            IntArrays.stableSort(this.a, 0, this.size);
        } else {
            IntArrays.stableSort(this.a, 0, this.size, comp);
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.IntList
    public void unstableSort(IntComparator comp) {
        if (comp == null) {
            IntArrays.unstableSort(this.a, 0, this.size);
        } else {
            IntArrays.unstableSort(this.a, 0, this.size, comp);
        }
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public IntArrayList m228clone() {
        if (getClass() == IntArrayList.class) {
            IntArrayList cloned = new IntArrayList(copyArraySafe(this.a, this.size), false);
            cloned.size = this.size;
            return cloned;
        }
        try {
            IntArrayList cloned2 = (IntArrayList) super.clone();
            cloned2.a = copyArraySafe(this.a, this.size);
            return cloned2;
        } catch (CloneNotSupportedException err) {
            throw new InternalError(err);
        }
    }

    public boolean equals(IntArrayList l) {
        if (l == this) {
            return true;
        }
        int s = size();
        if (s != l.size()) {
            return false;
        }
        int[] a1 = this.a;
        int[] a2 = l.a;
        if (a1 == a2 && s == l.size()) {
            return true;
        }
        while (true) {
            int s2 = s - 1;
            if (s == 0) {
                return true;
            }
            if (a1[s2] != a2[s2]) {
                return false;
            }
            s = s2;
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, java.util.Collection, java.util.List
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || !(o instanceof List)) {
            return false;
        }
        if (o instanceof IntArrayList) {
            return equals((IntArrayList) o);
        }
        if (o instanceof SubList) {
            return ((SubList) o).equals(this);
        }
        return super.equals(o);
    }

    public int compareTo(IntArrayList l) {
        int s1 = size();
        int s2 = l.size();
        int[] a1 = this.a;
        int[] a2 = l.a;
        if (a1 == a2 && s1 == s2) {
            return 0;
        }
        int i = 0;
        while (i < s1 && i < s2) {
            int e1 = a1[i];
            int e2 = a2[i];
            int r = Integer.compare(e1, e2);
            if (r != 0) {
                return r;
            }
            i++;
        }
        if (i < s2) {
            return -1;
        }
        return i < s1 ? 1 : 0;
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, java.lang.Comparable
    public int compareTo(List<? extends Integer> l) {
        if (l instanceof IntArrayList) {
            return compareTo((IntArrayList) l);
        }
        if (l instanceof SubList) {
            return -((SubList) l).compareTo((List<? extends Integer>) this);
        }
        return super.compareTo(l);
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        for (int i = 0; i < this.size; i++) {
            s.writeInt(this.a[i]);
        }
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.a = new int[this.size];
        for (int i = 0; i < this.size; i++) {
            this.a[i] = s.readInt();
        }
    }
}
