package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.ints.IntLists;
import it.unimi.dsi.fastutil.ints.IntSpliterators;
import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import java.util.stream.IntStream;

/* loaded from: classes4.dex */
public class IntImmutableList extends IntLists.ImmutableListBase implements IntList, RandomAccess, Cloneable, Serializable {
    static final IntImmutableList EMPTY = new IntImmutableList(IntArrays.EMPTY_ARRAY);
    private static final long serialVersionUID = 0;
    private final int[] a;

    public IntImmutableList(int[] a) {
        this.a = a;
    }

    public IntImmutableList(Collection<? extends Integer> c) {
        this(c.isEmpty() ? IntArrays.EMPTY_ARRAY : IntIterators.unwrap(IntIterators.asIntIterator(c.iterator())));
    }

    public IntImmutableList(IntCollection c) {
        this(c.isEmpty() ? IntArrays.EMPTY_ARRAY : IntIterators.unwrap(c.iterator()));
    }

    public IntImmutableList(IntList l) {
        this(l.isEmpty() ? IntArrays.EMPTY_ARRAY : new int[l.size()]);
        l.getElements(0, this.a, 0, l.size());
    }

    public IntImmutableList(int[] a, int offset, int length) {
        this(length == 0 ? IntArrays.EMPTY_ARRAY : new int[length]);
        System.arraycopy(a, offset, this.a, 0, length);
    }

    public IntImmutableList(IntIterator i) {
        this(i.hasNext() ? IntIterators.unwrap(i) : IntArrays.EMPTY_ARRAY);
    }

    public static IntImmutableList of() {
        return EMPTY;
    }

    public static IntImmutableList of(int... init) {
        return init.length == 0 ? of() : new IntImmutableList(init);
    }

    private static IntImmutableList convertTrustedToImmutableList(IntArrayList arrayList) {
        if (arrayList.isEmpty()) {
            return of();
        }
        int[] backingArray = arrayList.elements();
        if (arrayList.size() != backingArray.length) {
            backingArray = Arrays.copyOf(backingArray, arrayList.size());
        }
        return new IntImmutableList(backingArray);
    }

    public static IntImmutableList toList(IntStream stream) {
        return convertTrustedToImmutableList(IntArrayList.toList(stream));
    }

    public static IntImmutableList toListWithExpectedSize(IntStream stream, int expectedSize) {
        return convertTrustedToImmutableList(IntArrayList.toListWithExpectedSize(stream, expectedSize));
    }

    @Override // it.unimi.dsi.fastutil.ints.IntList
    public int getInt(int index) {
        if (index >= this.a.length) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.a.length + ")");
        }
        return this.a[index];
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
    public int indexOf(int k) {
        int size = this.a.length;
        for (int i = 0; i < size; i++) {
            if (k == this.a[i]) {
                return i;
            }
        }
        return -1;
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
    public int lastIndexOf(int k) {
        int i = this.a.length;
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

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return this.a.length;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List, it.unimi.dsi.fastutil.Stack
    public boolean isEmpty() {
        return this.a.length == 0;
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
    public void getElements(int from, int[] a, int offset, int length) {
        IntArrays.ensureOffsetLength(a, offset, length);
        System.arraycopy(this.a, from, a, offset, length);
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntIterable
    public void forEach(java.util.function.IntConsumer action) {
        for (int i = 0; i < this.a.length; i++) {
            action.accept(this.a[i]);
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
    public int[] toIntArray() {
        return this.a.length == 0 ? IntArrays.EMPTY_ARRAY : (int[]) this.a.clone();
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
    public int[] toArray(int[] a) {
        if (a == null || a.length < size()) {
            a = new int[this.a.length];
        }
        System.arraycopy(this.a, 0, a, 0, a.length);
        return a;
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList, java.util.List
    /* renamed from: listIterator */
    public ListIterator<Integer> listIterator2(final int index) {
        ensureIndex(index);
        return new IntListIterator() { // from class: it.unimi.dsi.fastutil.ints.IntImmutableList.1
            int pos;

            {
                this.pos = index;
            }

            @Override // java.util.Iterator, java.util.ListIterator
            public boolean hasNext() {
                return this.pos < IntImmutableList.this.a.length;
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
                int[] iArr = IntImmutableList.this.a;
                int i = this.pos;
                this.pos = i + 1;
                return iArr[i];
            }

            @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator
            public int previousInt() {
                if (!hasPrevious()) {
                    throw new NoSuchElementException();
                }
                int[] iArr = IntImmutableList.this.a;
                int i = this.pos - 1;
                this.pos = i;
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

            @Override // java.util.PrimitiveIterator
            public void forEachRemaining(java.util.function.IntConsumer action) {
                while (this.pos < IntImmutableList.this.a.length) {
                    int[] iArr = IntImmutableList.this.a;
                    int i = this.pos;
                    this.pos = i + 1;
                    action.accept(iArr[i]);
                }
            }

            @Override // it.unimi.dsi.fastutil.ints.IntListIterator
            public void add(int k) {
                throw new UnsupportedOperationException();
            }

            @Override // it.unimi.dsi.fastutil.ints.IntListIterator
            public void set(int k) {
                throw new UnsupportedOperationException();
            }

            @Override // it.unimi.dsi.fastutil.ints.IntListIterator, java.util.Iterator, java.util.ListIterator
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator
            public int back(int n) {
                if (n < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n);
                }
                int remaining = this.pos;
                if (n < remaining) {
                    this.pos -= n;
                    return n;
                }
                this.pos = 0;
                return remaining;
            }

            @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator, it.unimi.dsi.fastutil.ints.IntIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
            public int skip(int n) {
                if (n >= 0) {
                    int remaining = IntImmutableList.this.a.length - this.pos;
                    if (n >= remaining) {
                        this.pos = IntImmutableList.this.a.length;
                        return remaining;
                    }
                    this.pos += n;
                    return n;
                }
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class Spliterator implements IntSpliterator {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        int max;
        int pos;

        public Spliterator(IntImmutableList intImmutableList) {
            this(0, intImmutableList.a.length);
        }

        private Spliterator(int pos, int max) {
            if (pos > max) {
                throw new AssertionError("pos " + pos + " must be <= max " + max);
            }
            this.pos = pos;
            this.max = max;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return 17744;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.max - this.pos;
        }

        @Override // java.util.Spliterator.OfPrimitive
        public boolean tryAdvance(java.util.function.IntConsumer action) {
            if (this.pos >= this.max) {
                return false;
            }
            int[] iArr = IntImmutableList.this.a;
            int i = this.pos;
            this.pos = i + 1;
            action.accept(iArr[i]);
            return true;
        }

        @Override // java.util.Spliterator.OfPrimitive
        public void forEachRemaining(java.util.function.IntConsumer action) {
            while (this.pos < this.max) {
                action.accept(IntImmutableList.this.a[this.pos]);
                this.pos++;
            }
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSpliterator
        public long skip(long n) {
            if (n < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            if (this.pos >= this.max) {
                return 0L;
            }
            int remaining = this.max - this.pos;
            if (n < remaining) {
                this.pos = SafeMath.safeLongToInt(this.pos + n);
                return n;
            }
            long n2 = remaining;
            this.pos = this.max;
            return n2;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSpliterator, java.util.Spliterator.OfInt, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public IntSpliterator trySplit() {
            int retLen = (this.max - this.pos) >> 1;
            if (retLen <= 1) {
                return null;
            }
            int myNewPos = this.pos + retLen;
            int oldPos = this.pos;
            this.pos = myNewPos;
            return new Spliterator(oldPos, myNewPos);
        }
    }

    @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
    public IntSpliterator spliterator() {
        return new Spliterator(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class ImmutableSubList extends IntLists.ImmutableListBase implements RandomAccess, Serializable {
        private static final long serialVersionUID = 7054639518438982401L;
        final transient int[] a;
        final int from;
        final IntImmutableList innerList;
        final int to;

        ImmutableSubList(IntImmutableList innerList, int from, int to) {
            this.innerList = innerList;
            this.from = from;
            this.to = to;
            this.a = innerList.a;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public int getInt(int index) {
            ensureRestrictedIndex(index);
            return this.a[this.from + index];
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
        public int indexOf(int k) {
            for (int i = this.from; i < this.to; i++) {
                if (k == this.a[i]) {
                    return i - this.from;
                }
            }
            return -1;
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
        public int lastIndexOf(int k) {
            int i = this.to;
            while (true) {
                int i2 = i - 1;
                if (i == this.from) {
                    return -1;
                }
                if (k == this.a[i2]) {
                    return i2 - this.from;
                }
                i = i2;
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.to - this.from;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List, it.unimi.dsi.fastutil.Stack
        public boolean isEmpty() {
            return this.to <= this.from;
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
        public void getElements(int fromSublistIndex, int[] a, int offset, int length) {
            IntArrays.ensureOffsetLength(a, offset, length);
            ensureRestrictedIndex(fromSublistIndex);
            if (this.from + length > this.to) {
                throw new IndexOutOfBoundsException("Final index " + (this.from + length) + " (startingIndex: " + this.from + " + length: " + length + ") is greater then list length " + size());
            }
            System.arraycopy(this.a, this.from + fromSublistIndex, a, offset, length);
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntIterable
        public void forEach(java.util.function.IntConsumer action) {
            for (int i = this.from; i < this.to; i++) {
                action.accept(this.a[i]);
            }
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public int[] toIntArray() {
            return Arrays.copyOfRange(this.a, this.from, this.to);
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public int[] toArray(int[] a) {
            if (a == null || a.length < size()) {
                a = new int[size()];
            }
            System.arraycopy(this.a, this.from, a, 0, size());
            return a;
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList, java.util.List
        /* renamed from: listIterator */
        public ListIterator<Integer> listIterator2(final int index) {
            ensureRestrictedIndex(this.from + index);
            return new IntListIterator() { // from class: it.unimi.dsi.fastutil.ints.IntImmutableList.ImmutableSubList.1
                int pos;

                {
                    this.pos = index + ImmutableSubList.this.from;
                }

                @Override // java.util.Iterator, java.util.ListIterator
                public boolean hasNext() {
                    return this.pos < ImmutableSubList.this.to;
                }

                @Override // it.unimi.dsi.fastutil.BidirectionalIterator
                public boolean hasPrevious() {
                    return this.pos > ImmutableSubList.this.from;
                }

                @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt
                public int nextInt() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    int[] iArr = ImmutableSubList.this.a;
                    int i = this.pos;
                    this.pos = i + 1;
                    return iArr[i];
                }

                @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator
                public int previousInt() {
                    if (!hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    int[] iArr = ImmutableSubList.this.a;
                    int i = this.pos - 1;
                    this.pos = i;
                    return iArr[i];
                }

                @Override // java.util.ListIterator
                public int nextIndex() {
                    return this.pos - ImmutableSubList.this.from;
                }

                @Override // java.util.ListIterator
                public int previousIndex() {
                    return (this.pos - ImmutableSubList.this.from) - 1;
                }

                @Override // java.util.PrimitiveIterator
                public void forEachRemaining(java.util.function.IntConsumer action) {
                    while (this.pos < ImmutableSubList.this.to) {
                        int[] iArr = ImmutableSubList.this.a;
                        int i = this.pos;
                        this.pos = i + 1;
                        action.accept(iArr[i]);
                    }
                }

                @Override // it.unimi.dsi.fastutil.ints.IntListIterator
                public void add(int k) {
                    throw new UnsupportedOperationException();
                }

                @Override // it.unimi.dsi.fastutil.ints.IntListIterator
                public void set(int k) {
                    throw new UnsupportedOperationException();
                }

                @Override // it.unimi.dsi.fastutil.ints.IntListIterator, java.util.Iterator, java.util.ListIterator
                public void remove() {
                    throw new UnsupportedOperationException();
                }

                @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator
                public int back(int n) {
                    if (n < 0) {
                        throw new IllegalArgumentException("Argument must be nonnegative: " + n);
                    }
                    int remaining = this.pos - ImmutableSubList.this.from;
                    if (n < remaining) {
                        this.pos -= n;
                        return n;
                    }
                    this.pos = ImmutableSubList.this.from;
                    return remaining;
                }

                @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator, it.unimi.dsi.fastutil.ints.IntIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
                public int skip(int n) {
                    if (n < 0) {
                        throw new IllegalArgumentException("Argument must be nonnegative: " + n);
                    }
                    int remaining = ImmutableSubList.this.to - this.pos;
                    if (n < remaining) {
                        this.pos += n;
                        return n;
                    }
                    this.pos = ImmutableSubList.this.to;
                    return remaining;
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public final class SubListSpliterator extends IntSpliterators.EarlyBindingSizeIndexBasedSpliterator {
            SubListSpliterator() {
                super(ImmutableSubList.this.from, ImmutableSubList.this.to);
            }

            private SubListSpliterator(int pos, int maxPos) {
                super(pos, maxPos);
            }

            @Override // it.unimi.dsi.fastutil.ints.IntSpliterators.AbstractIndexBasedSpliterator
            protected final int get(int i) {
                return ImmutableSubList.this.a[i];
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // it.unimi.dsi.fastutil.ints.IntSpliterators.AbstractIndexBasedSpliterator
            public final SubListSpliterator makeForSplit(int pos, int maxPos) {
                return new SubListSpliterator(pos, maxPos);
            }

            @Override // it.unimi.dsi.fastutil.ints.IntSpliterators.AbstractIndexBasedSpliterator, java.util.Spliterator.OfPrimitive
            public boolean tryAdvance(java.util.function.IntConsumer action) {
                if (this.pos >= this.maxPos) {
                    return false;
                }
                int[] iArr = ImmutableSubList.this.a;
                int i = this.pos;
                this.pos = i + 1;
                action.accept(iArr[i]);
                return true;
            }

            @Override // it.unimi.dsi.fastutil.ints.IntSpliterators.AbstractIndexBasedSpliterator, java.util.Spliterator.OfPrimitive
            public void forEachRemaining(java.util.function.IntConsumer action) {
                int max = this.maxPos;
                while (this.pos < max) {
                    int[] iArr = ImmutableSubList.this.a;
                    int i = this.pos;
                    this.pos = i + 1;
                    action.accept(iArr[i]);
                }
            }

            @Override // it.unimi.dsi.fastutil.ints.IntSpliterators.AbstractIndexBasedSpliterator, java.util.Spliterator
            public int characteristics() {
                return 17744;
            }
        }

        @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
        public IntSpliterator spliterator() {
            return new SubListSpliterator();
        }

        boolean contentsEquals(int[] otherA, int otherAFrom, int otherATo) {
            if (this.a == otherA && this.from == otherAFrom && this.to == otherATo) {
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
                if (this.a[pos] != otherA[otherPos]) {
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
            if (o instanceof IntImmutableList) {
                IntImmutableList other = (IntImmutableList) o;
                return contentsEquals(other.a, 0, other.size());
            }
            if (o instanceof ImmutableSubList) {
                ImmutableSubList other2 = (ImmutableSubList) o;
                return contentsEquals(other2.a, other2.from, other2.to);
            }
            return super.equals(o);
        }

        int contentsCompareTo(int[] otherA, int otherAFrom, int otherATo) {
            if (this.a == otherA && this.from == otherAFrom && this.to == otherATo) {
                return 0;
            }
            int i = this.from;
            int j = otherAFrom;
            while (i < this.to && i < otherATo) {
                int e1 = this.a[i];
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

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, java.lang.Comparable
        public int compareTo(List<? extends Integer> l) {
            if (l instanceof IntImmutableList) {
                IntImmutableList other = (IntImmutableList) l;
                return contentsCompareTo(other.a, 0, other.size());
            }
            if (l instanceof ImmutableSubList) {
                ImmutableSubList other2 = (ImmutableSubList) l;
                return contentsCompareTo(other2.a, other2.from, other2.to);
            }
            return super.compareTo(l);
        }

        private Object readResolve() throws ObjectStreamException {
            try {
                return this.innerList.subList2(this.from, this.to);
            } catch (IllegalArgumentException | IndexOutOfBoundsException ex) {
                throw ((InvalidObjectException) new InvalidObjectException(ex.getMessage()).initCause(ex));
            }
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList, java.util.List
        /* renamed from: subList */
        public List<Integer> subList(int from, int to) {
            ensureIndex(from);
            ensureIndex(to);
            if (from == to) {
                return IntImmutableList.EMPTY;
            }
            if (from > to) {
                throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            return new ImmutableSubList(this.innerList, this.from + from, this.from + to);
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList, java.util.List
    /* renamed from: subList */
    public List<Integer> subList(int from, int to) {
        if (from == 0 && to == size()) {
            return this;
        }
        ensureIndex(from);
        ensureIndex(to);
        if (from == to) {
            return EMPTY;
        }
        if (from > to) {
            throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        return new ImmutableSubList(this, from, to);
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public IntImmutableList m230clone() {
        return this;
    }

    public boolean equals(IntImmutableList l) {
        if (l == this || this.a == l.a) {
            return true;
        }
        int s = size();
        if (s != l.size()) {
            return false;
        }
        int[] a1 = this.a;
        int[] a2 = l.a;
        return Arrays.equals(a1, a2);
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, java.util.Collection, java.util.List
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || !(o instanceof List)) {
            return false;
        }
        if (o instanceof IntImmutableList) {
            return equals((IntImmutableList) o);
        }
        if (o instanceof ImmutableSubList) {
            return ((ImmutableSubList) o).equals(this);
        }
        return super.equals(o);
    }

    public int compareTo(IntImmutableList l) {
        if (this.a == l.a) {
            return 0;
        }
        int s1 = size();
        int s2 = l.size();
        int[] a1 = this.a;
        int[] a2 = l.a;
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
        if (l instanceof IntImmutableList) {
            return compareTo((IntImmutableList) l);
        }
        if (l instanceof ImmutableSubList) {
            ImmutableSubList other = (ImmutableSubList) l;
            return -other.compareTo((List<? extends Integer>) this);
        }
        return super.compareTo(l);
    }
}
