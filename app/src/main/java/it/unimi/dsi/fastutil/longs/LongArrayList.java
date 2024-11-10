package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.longs.AbstractLongList;
import it.unimi.dsi.fastutil.longs.LongCollections;
import it.unimi.dsi.fastutil.longs.LongIterators;
import it.unimi.dsi.fastutil.longs.LongSpliterators;
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
import java.util.stream.LongStream;

/* loaded from: classes4.dex */
public class LongArrayList extends AbstractLongList implements RandomAccess, Cloneable, Serializable {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final int DEFAULT_INITIAL_CAPACITY = 10;
    private static final long serialVersionUID = -7046029254386353130L;
    protected transient long[] a;
    protected int size;

    private static final long[] copyArraySafe(long[] a, int length) {
        return length == 0 ? LongArrays.EMPTY_ARRAY : Arrays.copyOf(a, length);
    }

    private static final long[] copyArrayFromSafe(LongArrayList l) {
        return copyArraySafe(l.a, l.size);
    }

    protected LongArrayList(long[] a, boolean wrapped) {
        this.a = a;
    }

    private void initArrayFromCapacity(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Initial capacity (" + capacity + ") is negative");
        }
        if (capacity != 0) {
            this.a = new long[capacity];
        } else {
            this.a = LongArrays.EMPTY_ARRAY;
        }
    }

    public LongArrayList(int capacity) {
        initArrayFromCapacity(capacity);
    }

    public LongArrayList() {
        this.a = LongArrays.DEFAULT_EMPTY_ARRAY;
    }

    public LongArrayList(Collection<? extends Long> c) {
        if (c instanceof LongArrayList) {
            this.a = copyArrayFromSafe((LongArrayList) c);
            this.size = this.a.length;
            return;
        }
        initArrayFromCapacity(c.size());
        if (c instanceof LongList) {
            long[] jArr = this.a;
            int size = c.size();
            this.size = size;
            ((LongList) c).getElements(0, jArr, 0, size);
            return;
        }
        this.size = LongIterators.unwrap(LongIterators.asLongIterator(c.iterator()), this.a);
    }

    public LongArrayList(LongCollection c) {
        if (c instanceof LongArrayList) {
            this.a = copyArrayFromSafe((LongArrayList) c);
            this.size = this.a.length;
            return;
        }
        initArrayFromCapacity(c.size());
        if (c instanceof LongList) {
            long[] jArr = this.a;
            int size = c.size();
            this.size = size;
            ((LongList) c).getElements(0, jArr, 0, size);
            return;
        }
        this.size = LongIterators.unwrap(c.iterator(), this.a);
    }

    public LongArrayList(LongList l) {
        if (l instanceof LongArrayList) {
            this.a = copyArrayFromSafe((LongArrayList) l);
            this.size = this.a.length;
            return;
        }
        initArrayFromCapacity(l.size());
        long[] jArr = this.a;
        int size = l.size();
        this.size = size;
        l.getElements(0, jArr, 0, size);
    }

    public LongArrayList(long[] a) {
        this(a, 0, a.length);
    }

    public LongArrayList(long[] a, int offset, int length) {
        this(length);
        System.arraycopy(a, offset, this.a, 0, length);
        this.size = length;
    }

    public LongArrayList(Iterator<? extends Long> i) {
        this();
        while (i.hasNext()) {
            add(i.next().longValue());
        }
    }

    public LongArrayList(LongIterator i) {
        this();
        while (i.hasNext()) {
            add(i.nextLong());
        }
    }

    public long[] elements() {
        return this.a;
    }

    public static LongArrayList wrap(long[] a, int length) {
        if (length > a.length) {
            throw new IllegalArgumentException("The specified length (" + length + ") is greater than the array size (" + a.length + ")");
        }
        LongArrayList l = new LongArrayList(a, true);
        l.size = length;
        return l;
    }

    public static LongArrayList wrap(long[] a) {
        return wrap(a, a.length);
    }

    public static LongArrayList of() {
        return new LongArrayList();
    }

    public static LongArrayList of(long... init) {
        return wrap(init);
    }

    public static LongArrayList toList(LongStream stream) {
        return (LongArrayList) stream.collect(new Supplier() { // from class: it.unimi.dsi.fastutil.longs.LongArrayList$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final Object get() {
                return new LongArrayList();
            }
        }, new LongArrayList$$ExternalSyntheticLambda1(), new LongArrayList$$ExternalSyntheticLambda2());
    }

    public static LongArrayList toListWithExpectedSize(LongStream stream, int expectedSize) {
        if (expectedSize <= 10) {
            return toList(stream);
        }
        return (LongArrayList) stream.collect(new LongCollections.SizeDecreasingSupplier(expectedSize, new IntFunction() { // from class: it.unimi.dsi.fastutil.longs.LongArrayList$$ExternalSyntheticLambda3
            @Override // java.util.function.IntFunction
            public final Object apply(int i) {
                return LongArrayList.lambda$toListWithExpectedSize$0(i);
            }
        }), new LongArrayList$$ExternalSyntheticLambda1(), new LongArrayList$$ExternalSyntheticLambda2());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ LongArrayList lambda$toListWithExpectedSize$0(int size) {
        return size <= 10 ? new LongArrayList() : new LongArrayList(size);
    }

    public void ensureCapacity(int capacity) {
        if (capacity > this.a.length) {
            if (this.a == LongArrays.DEFAULT_EMPTY_ARRAY && capacity <= 10) {
                return;
            }
            this.a = LongArrays.ensureCapacity(this.a, capacity, this.size);
            if (this.size > this.a.length) {
                throw new AssertionError();
            }
        }
    }

    private void grow(int capacity) {
        if (capacity <= this.a.length) {
            return;
        }
        if (this.a != LongArrays.DEFAULT_EMPTY_ARRAY) {
            capacity = (int) Math.max(Math.min(this.a.length + (this.a.length >> 1), 2147483639L), capacity);
        } else if (capacity < 10) {
            capacity = 10;
        }
        this.a = LongArrays.forceCapacity(this.a, capacity, this.size);
        if (this.size > this.a.length) {
            throw new AssertionError();
        }
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
    public void add(int index, long k) {
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

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
    public boolean add(long k) {
        grow(this.size + 1);
        long[] jArr = this.a;
        int i = this.size;
        this.size = i + 1;
        jArr[i] = k;
        if (this.size <= this.a.length) {
            return true;
        }
        throw new AssertionError();
    }

    @Override // it.unimi.dsi.fastutil.longs.LongList
    public long getLong(int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        return this.a[index];
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
    public int indexOf(long k) {
        for (int i = 0; i < this.size; i++) {
            if (k == this.a[i]) {
                return i;
            }
        }
        return -1;
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
    public int lastIndexOf(long k) {
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

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
    public long removeLong(int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        long old = this.a[index];
        this.size--;
        if (index != this.size) {
            System.arraycopy(this.a, index + 1, this.a, index, this.size - index);
        }
        if (this.size > this.a.length) {
            throw new AssertionError();
        }
        return old;
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
    public boolean rem(long k) {
        int index = indexOf(k);
        if (index == -1) {
            return false;
        }
        removeLong(index);
        if (this.size > this.a.length) {
            throw new AssertionError();
        }
        return true;
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
    public long set(int index, long k) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        long old = this.a[index];
        this.a[index] = k;
        return old;
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, java.util.AbstractCollection, java.util.Collection, java.util.List
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

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
    public void size(int size) {
        if (size > this.a.length) {
            this.a = LongArrays.forceCapacity(this.a, size, this.size);
        }
        if (size > this.size) {
            Arrays.fill(this.a, this.size, size, 0L);
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
        long[] t = new long[Math.max(n, this.size)];
        System.arraycopy(this.a, 0, t, 0, this.size);
        this.a = t;
        if (this.size > this.a.length) {
            throw new AssertionError();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class SubList extends AbstractLongList.LongRandomAccessSubList {
        private static final long serialVersionUID = -3185226345314976296L;

        protected SubList(int from, int to) {
            super(LongArrayList.this, from, to);
        }

        private long[] getParentArray() {
            return LongArrayList.this.a;
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList.LongSubList, it.unimi.dsi.fastutil.longs.LongList
        public long getLong(int i) {
            ensureRestrictedIndex(i);
            return LongArrayList.this.a[this.from + i];
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public final class SubListIterator extends LongIterators.AbstractIndexBasedListIterator {
            SubListIterator(int index) {
                super(0, index);
            }

            @Override // it.unimi.dsi.fastutil.longs.LongIterators.AbstractIndexBasedIterator
            protected final long get(int i) {
                return LongArrayList.this.a[SubList.this.from + i];
            }

            @Override // it.unimi.dsi.fastutil.longs.LongIterators.AbstractIndexBasedListIterator
            protected final void add(int i, long k) {
                SubList.this.add(i, k);
            }

            @Override // it.unimi.dsi.fastutil.longs.LongIterators.AbstractIndexBasedListIterator
            protected final void set(int i, long k) {
                SubList.this.set(i, k);
            }

            @Override // it.unimi.dsi.fastutil.longs.LongIterators.AbstractIndexBasedIterator
            protected final void remove(int i) {
                SubList.this.removeLong(i);
            }

            @Override // it.unimi.dsi.fastutil.longs.LongIterators.AbstractIndexBasedIterator
            protected final int getMaxPos() {
                return SubList.this.to - SubList.this.from;
            }

            @Override // it.unimi.dsi.fastutil.longs.LongIterators.AbstractIndexBasedIterator, it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong
            public long nextLong() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                long[] jArr = LongArrayList.this.a;
                int i = SubList.this.from;
                int i2 = this.pos;
                this.pos = i2 + 1;
                this.lastReturned = i2;
                return jArr[i + i2];
            }

            @Override // it.unimi.dsi.fastutil.longs.LongIterators.AbstractIndexBasedListIterator, it.unimi.dsi.fastutil.longs.LongBidirectionalIterator
            public long previousLong() {
                if (!hasPrevious()) {
                    throw new NoSuchElementException();
                }
                long[] jArr = LongArrayList.this.a;
                int i = SubList.this.from;
                int i2 = this.pos - 1;
                this.pos = i2;
                this.lastReturned = i2;
                return jArr[i + i2];
            }

            @Override // it.unimi.dsi.fastutil.longs.LongIterators.AbstractIndexBasedIterator, java.util.PrimitiveIterator
            public void forEachRemaining(java.util.function.LongConsumer action) {
                int max = SubList.this.to - SubList.this.from;
                while (this.pos < max) {
                    long[] jArr = LongArrayList.this.a;
                    int i = SubList.this.from;
                    int i2 = this.pos;
                    this.pos = i2 + 1;
                    this.lastReturned = i2;
                    action.accept(jArr[i + i2]);
                }
            }
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList.LongSubList, it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList, java.util.List
        /* renamed from: listIterator, reason: merged with bridge method [inline-methods] */
        public ListIterator<Long> listIterator2(int index) {
            return new SubListIterator(index);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public final class SubListSpliterator extends LongSpliterators.LateBindingSizeIndexBasedSpliterator {
            SubListSpliterator() {
                super(SubList.this.from);
            }

            private SubListSpliterator(int pos, int maxPos) {
                super(pos, maxPos);
            }

            @Override // it.unimi.dsi.fastutil.longs.LongSpliterators.LateBindingSizeIndexBasedSpliterator
            protected final int getMaxPosFromBackingStore() {
                return SubList.this.to;
            }

            @Override // it.unimi.dsi.fastutil.longs.LongSpliterators.AbstractIndexBasedSpliterator
            protected final long get(int i) {
                return LongArrayList.this.a[i];
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // it.unimi.dsi.fastutil.longs.LongSpliterators.AbstractIndexBasedSpliterator
            public final SubListSpliterator makeForSplit(int pos, int maxPos) {
                return new SubListSpliterator(pos, maxPos);
            }

            @Override // it.unimi.dsi.fastutil.longs.LongSpliterators.AbstractIndexBasedSpliterator, java.util.Spliterator.OfPrimitive
            public boolean tryAdvance(java.util.function.LongConsumer action) {
                if (this.pos >= getMaxPos()) {
                    return false;
                }
                long[] jArr = LongArrayList.this.a;
                int i = this.pos;
                this.pos = i + 1;
                action.accept(jArr[i]);
                return true;
            }

            @Override // it.unimi.dsi.fastutil.longs.LongSpliterators.AbstractIndexBasedSpliterator, java.util.Spliterator.OfPrimitive
            public void forEachRemaining(java.util.function.LongConsumer action) {
                int max = getMaxPos();
                while (this.pos < max) {
                    long[] jArr = LongArrayList.this.a;
                    int i = this.pos;
                    this.pos = i + 1;
                    action.accept(jArr[i]);
                }
            }
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList.LongSubList, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
        public LongSpliterator spliterator() {
            return new SubListSpliterator();
        }

        boolean contentsEquals(long[] otherA, int otherAFrom, int otherATo) {
            if (LongArrayList.this.a == otherA && this.from == otherAFrom && this.to == otherATo) {
                return true;
            }
            if (otherATo - otherAFrom != size()) {
                return false;
            }
            int otherPos = this.from;
            int otherPos2 = otherAFrom;
            while (otherPos < this.to) {
                int pos = otherPos + 1;
                long j = LongArrayList.this.a[otherPos];
                int otherPos3 = otherPos2 + 1;
                if (j != otherA[otherPos2]) {
                    return false;
                }
                otherPos2 = otherPos3;
                otherPos = pos;
            }
            return true;
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, java.util.Collection, java.util.List
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o == null || !(o instanceof List)) {
                return false;
            }
            if (o instanceof LongArrayList) {
                LongArrayList other = (LongArrayList) o;
                return contentsEquals(other.a, 0, other.size());
            }
            if (o instanceof SubList) {
                SubList other2 = (SubList) o;
                return contentsEquals(other2.getParentArray(), other2.from, other2.to);
            }
            return super.equals(o);
        }

        int contentsCompareTo(long[] otherA, int otherAFrom, int otherATo) {
            if (LongArrayList.this.a == otherA && this.from == otherAFrom && this.to == otherATo) {
                return 0;
            }
            int i = this.from;
            int j = otherAFrom;
            while (i < this.to && i < otherATo) {
                long e1 = LongArrayList.this.a[i];
                long e2 = otherA[j];
                int r = Long.compare(e1, e2);
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

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList.LongSubList, it.unimi.dsi.fastutil.longs.AbstractLongList, java.lang.Comparable
        public int compareTo(List<? extends Long> l) {
            if (l instanceof LongArrayList) {
                LongArrayList other = (LongArrayList) l;
                return contentsCompareTo(other.a, 0, other.size());
            }
            if (l instanceof SubList) {
                SubList other2 = (SubList) l;
                return contentsCompareTo(other2.getParentArray(), other2.from, other2.to);
            }
            return super.compareTo(l);
        }
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList, java.util.List
    /* renamed from: subList, reason: merged with bridge method [inline-methods] */
    public List<Long> subList2(int from, int to) {
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

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
    public void getElements(int from, long[] a, int offset, int length) {
        LongArrays.ensureOffsetLength(a, offset, length);
        System.arraycopy(this.a, from, a, offset, length);
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
    public void removeElements(int from, int to) {
        it.unimi.dsi.fastutil.Arrays.ensureFromTo(this.size, from, to);
        System.arraycopy(this.a, to, this.a, from, this.size - to);
        this.size -= to - from;
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
    public void addElements(int index, long[] a, int offset, int length) {
        ensureIndex(index);
        LongArrays.ensureOffsetLength(a, offset, length);
        grow(this.size + length);
        System.arraycopy(this.a, index, this.a, index + length, this.size - index);
        System.arraycopy(a, offset, this.a, index, length);
        this.size += length;
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
    public void setElements(int index, long[] a, int offset, int length) {
        ensureIndex(index);
        LongArrays.ensureOffsetLength(a, offset, length);
        if (index + length > this.size) {
            throw new IndexOutOfBoundsException("End index (" + (index + length) + ") is greater than list size (" + this.size + ")");
        }
        System.arraycopy(a, offset, this.a, index, length);
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongIterable
    public void forEach(java.util.function.LongConsumer action) {
        for (int i = 0; i < this.size; i++) {
            action.accept(this.a[i]);
        }
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
    public boolean addAll(int index, LongCollection c) {
        if (c instanceof LongList) {
            return addAll(index, (LongList) c);
        }
        ensureIndex(index);
        int n = c.size();
        if (n == 0) {
            return false;
        }
        grow(this.size + n);
        System.arraycopy(this.a, index, this.a, index + n, this.size - index);
        LongIterator i = c.iterator();
        this.size += n;
        while (true) {
            int n2 = n - 1;
            if (n == 0) {
                break;
            }
            this.a[index] = i.nextLong();
            n = n2;
            index++;
        }
        if (this.size > this.a.length) {
            throw new AssertionError();
        }
        return true;
    }

    @Override // it.unimi.dsi.fastutil.longs.LongList
    public boolean addAll(int index, LongList l) {
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

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
    public boolean removeAll(LongCollection c) {
        long[] a = this.a;
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

    @Override // it.unimi.dsi.fastutil.longs.LongCollection
    public boolean removeIf(java.util.function.LongPredicate filter) {
        long[] a = this.a;
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

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
    public long[] toArray(long[] a) {
        if (a == null || a.length < this.size) {
            a = Arrays.copyOf(a, this.size);
        }
        System.arraycopy(this.a, 0, a, 0, this.size);
        return a;
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList, java.util.List
    /* renamed from: listIterator */
    public ListIterator<Long> listIterator2(final int index) {
        ensureIndex(index);
        return new LongListIterator() { // from class: it.unimi.dsi.fastutil.longs.LongArrayList.1
            int last = -1;
            int pos;

            {
                this.pos = index;
            }

            @Override // java.util.Iterator, java.util.ListIterator
            public boolean hasNext() {
                return this.pos < LongArrayList.this.size;
            }

            @Override // it.unimi.dsi.fastutil.BidirectionalIterator
            public boolean hasPrevious() {
                return this.pos > 0;
            }

            @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong
            public long nextLong() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                long[] jArr = LongArrayList.this.a;
                int i = this.pos;
                this.pos = i + 1;
                this.last = i;
                return jArr[i];
            }

            @Override // it.unimi.dsi.fastutil.longs.LongBidirectionalIterator
            public long previousLong() {
                if (!hasPrevious()) {
                    throw new NoSuchElementException();
                }
                long[] jArr = LongArrayList.this.a;
                int i = this.pos - 1;
                this.pos = i;
                this.last = i;
                return jArr[i];
            }

            @Override // java.util.ListIterator
            public int nextIndex() {
                return this.pos;
            }

            @Override // java.util.ListIterator
            public int previousIndex() {
                return this.pos - 1;
            }

            @Override // it.unimi.dsi.fastutil.longs.LongListIterator
            public void add(long k) {
                LongArrayList longArrayList = LongArrayList.this;
                int i = this.pos;
                this.pos = i + 1;
                longArrayList.add(i, k);
                this.last = -1;
            }

            @Override // it.unimi.dsi.fastutil.longs.LongListIterator
            public void set(long k) {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                LongArrayList.this.set(this.last, k);
            }

            @Override // it.unimi.dsi.fastutil.longs.LongListIterator, java.util.Iterator, java.util.ListIterator
            public void remove() {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                LongArrayList.this.removeLong(this.last);
                if (this.last < this.pos) {
                    this.pos--;
                }
                this.last = -1;
            }

            @Override // java.util.PrimitiveIterator
            public void forEachRemaining(java.util.function.LongConsumer action) {
                while (this.pos < LongArrayList.this.size) {
                    long[] jArr = LongArrayList.this.a;
                    int i = this.pos;
                    this.pos = i + 1;
                    this.last = i;
                    action.accept(jArr[i]);
                }
            }

            @Override // it.unimi.dsi.fastutil.longs.LongBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator
            public int back(int n) {
                if (n < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n);
                }
                int remaining = LongArrayList.this.size - this.pos;
                if (n < remaining) {
                    this.pos -= n;
                } else {
                    n = remaining;
                    this.pos = 0;
                }
                this.last = this.pos;
                return n;
            }

            @Override // it.unimi.dsi.fastutil.longs.LongBidirectionalIterator, it.unimi.dsi.fastutil.longs.LongIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
            public int skip(int n) {
                if (n < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n);
                }
                int remaining = LongArrayList.this.size - this.pos;
                if (n < remaining) {
                    this.pos += n;
                } else {
                    n = remaining;
                    this.pos = LongArrayList.this.size;
                }
                this.last = this.pos - 1;
                return n;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class Spliterator implements LongSpliterator {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        boolean hasSplit;
        int max;
        int pos;

        public Spliterator(LongArrayList longArrayList) {
            this(0, longArrayList.size, false);
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
            return this.hasSplit ? this.max : LongArrayList.this.size;
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
        public boolean tryAdvance(java.util.function.LongConsumer action) {
            if (this.pos >= getWorkingMax()) {
                return false;
            }
            long[] jArr = LongArrayList.this.a;
            int i = this.pos;
            this.pos = i + 1;
            action.accept(jArr[i]);
            return true;
        }

        @Override // java.util.Spliterator.OfPrimitive
        public void forEachRemaining(java.util.function.LongConsumer action) {
            int max = getWorkingMax();
            while (this.pos < max) {
                action.accept(LongArrayList.this.a[this.pos]);
                this.pos++;
            }
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator
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

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator, java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public LongSpliterator trySplit() {
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

    @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
    public LongSpliterator spliterator() {
        return new Spliterator(this);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongList
    public void sort(LongComparator comp) {
        if (comp == null) {
            LongArrays.stableSort(this.a, 0, this.size);
        } else {
            LongArrays.stableSort(this.a, 0, this.size, comp);
        }
    }

    @Override // it.unimi.dsi.fastutil.longs.LongList
    public void unstableSort(LongComparator comp) {
        if (comp == null) {
            LongArrays.unstableSort(this.a, 0, this.size);
        } else {
            LongArrays.unstableSort(this.a, 0, this.size, comp);
        }
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public LongArrayList m247clone() {
        if (getClass() == LongArrayList.class) {
            LongArrayList cloned = new LongArrayList(copyArraySafe(this.a, this.size), false);
            cloned.size = this.size;
            return cloned;
        }
        try {
            LongArrayList cloned2 = (LongArrayList) super.clone();
            cloned2.a = copyArraySafe(this.a, this.size);
            return cloned2;
        } catch (CloneNotSupportedException err) {
            throw new InternalError(err);
        }
    }

    public boolean equals(LongArrayList l) {
        if (l == this) {
            return true;
        }
        int s = size();
        if (s != l.size()) {
            return false;
        }
        long[] a1 = this.a;
        long[] a2 = l.a;
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

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, java.util.Collection, java.util.List
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || !(o instanceof List)) {
            return false;
        }
        if (o instanceof LongArrayList) {
            return equals((LongArrayList) o);
        }
        if (o instanceof SubList) {
            return ((SubList) o).equals(this);
        }
        return super.equals(o);
    }

    public int compareTo(LongArrayList l) {
        int s1 = size();
        int s2 = l.size();
        long[] a1 = this.a;
        long[] a2 = l.a;
        if (a1 == a2 && s1 == s2) {
            return 0;
        }
        int i = 0;
        while (i < s1 && i < s2) {
            long e1 = a1[i];
            long e2 = a2[i];
            int r = Long.compare(e1, e2);
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

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, java.lang.Comparable
    public int compareTo(List<? extends Long> l) {
        if (l instanceof LongArrayList) {
            return compareTo((LongArrayList) l);
        }
        if (l instanceof SubList) {
            return -((SubList) l).compareTo((List<? extends Long>) this);
        }
        return super.compareTo(l);
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        for (int i = 0; i < this.size; i++) {
            s.writeLong(this.a[i]);
        }
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.a = new long[this.size];
        for (int i = 0; i < this.size; i++) {
            this.a[i] = s.readLong();
        }
    }
}
