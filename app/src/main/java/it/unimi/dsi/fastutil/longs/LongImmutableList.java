package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.longs.LongLists;
import it.unimi.dsi.fastutil.longs.LongSpliterators;
import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import java.util.stream.LongStream;

/* loaded from: classes4.dex */
public class LongImmutableList extends LongLists.ImmutableListBase implements LongList, RandomAccess, Cloneable, Serializable {
    static final LongImmutableList EMPTY = new LongImmutableList(LongArrays.EMPTY_ARRAY);
    private static final long serialVersionUID = 0;
    private final long[] a;

    public LongImmutableList(long[] a) {
        this.a = a;
    }

    public LongImmutableList(Collection<? extends Long> c) {
        this(c.isEmpty() ? LongArrays.EMPTY_ARRAY : LongIterators.unwrap(LongIterators.asLongIterator(c.iterator())));
    }

    public LongImmutableList(LongCollection c) {
        this(c.isEmpty() ? LongArrays.EMPTY_ARRAY : LongIterators.unwrap(c.iterator()));
    }

    public LongImmutableList(LongList l) {
        this(l.isEmpty() ? LongArrays.EMPTY_ARRAY : new long[l.size()]);
        l.getElements(0, this.a, 0, l.size());
    }

    public LongImmutableList(long[] a, int offset, int length) {
        this(length == 0 ? LongArrays.EMPTY_ARRAY : new long[length]);
        System.arraycopy(a, offset, this.a, 0, length);
    }

    public LongImmutableList(LongIterator i) {
        this(i.hasNext() ? LongIterators.unwrap(i) : LongArrays.EMPTY_ARRAY);
    }

    public static LongImmutableList of() {
        return EMPTY;
    }

    public static LongImmutableList of(long... init) {
        return init.length == 0 ? of() : new LongImmutableList(init);
    }

    private static LongImmutableList convertTrustedToImmutableList(LongArrayList arrayList) {
        if (arrayList.isEmpty()) {
            return of();
        }
        long[] backingArray = arrayList.elements();
        if (arrayList.size() != backingArray.length) {
            backingArray = Arrays.copyOf(backingArray, arrayList.size());
        }
        return new LongImmutableList(backingArray);
    }

    public static LongImmutableList toList(LongStream stream) {
        return convertTrustedToImmutableList(LongArrayList.toList(stream));
    }

    public static LongImmutableList toListWithExpectedSize(LongStream stream, int expectedSize) {
        return convertTrustedToImmutableList(LongArrayList.toListWithExpectedSize(stream, expectedSize));
    }

    @Override // it.unimi.dsi.fastutil.longs.LongList
    public long getLong(int index) {
        if (index >= this.a.length) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.a.length + ")");
        }
        return this.a[index];
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
    public int indexOf(long k) {
        int size = this.a.length;
        for (int i = 0; i < size; i++) {
            if (k == this.a[i]) {
                return i;
            }
        }
        return -1;
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
    public int lastIndexOf(long k) {
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

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
    public void getElements(int from, long[] a, int offset, int length) {
        LongArrays.ensureOffsetLength(a, offset, length);
        System.arraycopy(this.a, from, a, offset, length);
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongIterable
    public void forEach(java.util.function.LongConsumer action) {
        for (int i = 0; i < this.a.length; i++) {
            action.accept(this.a[i]);
        }
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
    public long[] toLongArray() {
        return this.a.length == 0 ? LongArrays.EMPTY_ARRAY : (long[]) this.a.clone();
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
    public long[] toArray(long[] a) {
        if (a == null || a.length < size()) {
            a = new long[this.a.length];
        }
        System.arraycopy(this.a, 0, a, 0, a.length);
        return a;
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList, java.util.List
    /* renamed from: listIterator */
    public ListIterator<Long> listIterator2(final int index) {
        ensureIndex(index);
        return new LongListIterator() { // from class: it.unimi.dsi.fastutil.longs.LongImmutableList.1
            int pos;

            {
                this.pos = index;
            }

            @Override // java.util.Iterator, java.util.ListIterator
            public boolean hasNext() {
                return this.pos < LongImmutableList.this.a.length;
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
                long[] jArr = LongImmutableList.this.a;
                int i = this.pos;
                this.pos = i + 1;
                return jArr[i];
            }

            @Override // it.unimi.dsi.fastutil.longs.LongBidirectionalIterator
            public long previousLong() {
                if (!hasPrevious()) {
                    throw new NoSuchElementException();
                }
                long[] jArr = LongImmutableList.this.a;
                int i = this.pos - 1;
                this.pos = i;
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

            @Override // java.util.PrimitiveIterator
            public void forEachRemaining(java.util.function.LongConsumer action) {
                while (this.pos < LongImmutableList.this.a.length) {
                    long[] jArr = LongImmutableList.this.a;
                    int i = this.pos;
                    this.pos = i + 1;
                    action.accept(jArr[i]);
                }
            }

            @Override // it.unimi.dsi.fastutil.longs.LongListIterator
            public void add(long k) {
                throw new UnsupportedOperationException();
            }

            @Override // it.unimi.dsi.fastutil.longs.LongListIterator
            public void set(long k) {
                throw new UnsupportedOperationException();
            }

            @Override // it.unimi.dsi.fastutil.longs.LongListIterator, java.util.Iterator, java.util.ListIterator
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override // it.unimi.dsi.fastutil.longs.LongBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator
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

            @Override // it.unimi.dsi.fastutil.longs.LongBidirectionalIterator, it.unimi.dsi.fastutil.longs.LongIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
            public int skip(int n) {
                if (n >= 0) {
                    int remaining = LongImmutableList.this.a.length - this.pos;
                    if (n >= remaining) {
                        this.pos = LongImmutableList.this.a.length;
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
    public final class Spliterator implements LongSpliterator {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        int max;
        int pos;

        public Spliterator(LongImmutableList longImmutableList) {
            this(0, longImmutableList.a.length);
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
        public boolean tryAdvance(java.util.function.LongConsumer action) {
            if (this.pos >= this.max) {
                return false;
            }
            long[] jArr = LongImmutableList.this.a;
            int i = this.pos;
            this.pos = i + 1;
            action.accept(jArr[i]);
            return true;
        }

        @Override // java.util.Spliterator.OfPrimitive
        public void forEachRemaining(java.util.function.LongConsumer action) {
            while (this.pos < this.max) {
                action.accept(LongImmutableList.this.a[this.pos]);
                this.pos++;
            }
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator
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

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator, java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public LongSpliterator trySplit() {
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

    @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
    public LongSpliterator spliterator() {
        return new Spliterator(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class ImmutableSubList extends LongLists.ImmutableListBase implements RandomAccess, Serializable {
        private static final long serialVersionUID = 7054639518438982401L;
        final transient long[] a;
        final int from;
        final LongImmutableList innerList;
        final int to;

        ImmutableSubList(LongImmutableList innerList, int from, int to) {
            this.innerList = innerList;
            this.from = from;
            this.to = to;
            this.a = innerList.a;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public long getLong(int index) {
            ensureRestrictedIndex(index);
            return this.a[this.from + index];
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
        public int indexOf(long k) {
            for (int i = this.from; i < this.to; i++) {
                if (k == this.a[i]) {
                    return i - this.from;
                }
            }
            return -1;
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
        public int lastIndexOf(long k) {
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

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
        public void getElements(int fromSublistIndex, long[] a, int offset, int length) {
            LongArrays.ensureOffsetLength(a, offset, length);
            ensureRestrictedIndex(fromSublistIndex);
            if (this.from + length > this.to) {
                throw new IndexOutOfBoundsException("Final index " + (this.from + length) + " (startingIndex: " + this.from + " + length: " + length + ") is greater then list length " + size());
            }
            System.arraycopy(this.a, this.from + fromSublistIndex, a, offset, length);
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongIterable
        public void forEach(java.util.function.LongConsumer action) {
            for (int i = this.from; i < this.to; i++) {
                action.accept(this.a[i]);
            }
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public long[] toLongArray() {
            return Arrays.copyOfRange(this.a, this.from, this.to);
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public long[] toArray(long[] a) {
            if (a == null || a.length < size()) {
                a = new long[size()];
            }
            System.arraycopy(this.a, this.from, a, 0, size());
            return a;
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList, java.util.List
        /* renamed from: listIterator */
        public ListIterator<Long> listIterator2(final int index) {
            ensureRestrictedIndex(this.from + index);
            return new LongListIterator() { // from class: it.unimi.dsi.fastutil.longs.LongImmutableList.ImmutableSubList.1
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

                @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong
                public long nextLong() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    long[] jArr = ImmutableSubList.this.a;
                    int i = this.pos;
                    this.pos = i + 1;
                    return jArr[i];
                }

                @Override // it.unimi.dsi.fastutil.longs.LongBidirectionalIterator
                public long previousLong() {
                    if (!hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    long[] jArr = ImmutableSubList.this.a;
                    int i = this.pos - 1;
                    this.pos = i;
                    return jArr[i];
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
                public void forEachRemaining(java.util.function.LongConsumer action) {
                    while (this.pos < ImmutableSubList.this.to) {
                        long[] jArr = ImmutableSubList.this.a;
                        int i = this.pos;
                        this.pos = i + 1;
                        action.accept(jArr[i]);
                    }
                }

                @Override // it.unimi.dsi.fastutil.longs.LongListIterator
                public void add(long k) {
                    throw new UnsupportedOperationException();
                }

                @Override // it.unimi.dsi.fastutil.longs.LongListIterator
                public void set(long k) {
                    throw new UnsupportedOperationException();
                }

                @Override // it.unimi.dsi.fastutil.longs.LongListIterator, java.util.Iterator, java.util.ListIterator
                public void remove() {
                    throw new UnsupportedOperationException();
                }

                @Override // it.unimi.dsi.fastutil.longs.LongBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator
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

                @Override // it.unimi.dsi.fastutil.longs.LongBidirectionalIterator, it.unimi.dsi.fastutil.longs.LongIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
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
        public final class SubListSpliterator extends LongSpliterators.EarlyBindingSizeIndexBasedSpliterator {
            SubListSpliterator() {
                super(ImmutableSubList.this.from, ImmutableSubList.this.to);
            }

            private SubListSpliterator(int pos, int maxPos) {
                super(pos, maxPos);
            }

            @Override // it.unimi.dsi.fastutil.longs.LongSpliterators.AbstractIndexBasedSpliterator
            protected final long get(int i) {
                return ImmutableSubList.this.a[i];
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // it.unimi.dsi.fastutil.longs.LongSpliterators.AbstractIndexBasedSpliterator
            public final SubListSpliterator makeForSplit(int pos, int maxPos) {
                return new SubListSpliterator(pos, maxPos);
            }

            @Override // it.unimi.dsi.fastutil.longs.LongSpliterators.AbstractIndexBasedSpliterator, java.util.Spliterator.OfPrimitive
            public boolean tryAdvance(java.util.function.LongConsumer action) {
                if (this.pos >= this.maxPos) {
                    return false;
                }
                long[] jArr = ImmutableSubList.this.a;
                int i = this.pos;
                this.pos = i + 1;
                action.accept(jArr[i]);
                return true;
            }

            @Override // it.unimi.dsi.fastutil.longs.LongSpliterators.AbstractIndexBasedSpliterator, java.util.Spliterator.OfPrimitive
            public void forEachRemaining(java.util.function.LongConsumer action) {
                int max = this.maxPos;
                while (this.pos < max) {
                    long[] jArr = ImmutableSubList.this.a;
                    int i = this.pos;
                    this.pos = i + 1;
                    action.accept(jArr[i]);
                }
            }

            @Override // it.unimi.dsi.fastutil.longs.LongSpliterators.AbstractIndexBasedSpliterator, java.util.Spliterator
            public int characteristics() {
                return 17744;
            }
        }

        @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
        public LongSpliterator spliterator() {
            return new SubListSpliterator();
        }

        boolean contentsEquals(long[] otherA, int otherAFrom, int otherATo) {
            if (this.a == otherA && this.from == otherAFrom && this.to == otherATo) {
                return true;
            }
            if (otherATo - otherAFrom != size()) {
                return false;
            }
            int otherPos = this.from;
            int otherPos2 = otherAFrom;
            while (otherPos < this.to) {
                int pos = otherPos + 1;
                long j = this.a[otherPos];
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
            if (o instanceof LongImmutableList) {
                LongImmutableList other = (LongImmutableList) o;
                return contentsEquals(other.a, 0, other.size());
            }
            if (o instanceof ImmutableSubList) {
                ImmutableSubList other2 = (ImmutableSubList) o;
                return contentsEquals(other2.a, other2.from, other2.to);
            }
            return super.equals(o);
        }

        int contentsCompareTo(long[] otherA, int otherAFrom, int otherATo) {
            if (this.a == otherA && this.from == otherAFrom && this.to == otherATo) {
                return 0;
            }
            int i = this.from;
            int j = otherAFrom;
            while (i < this.to && i < otherATo) {
                long e1 = this.a[i];
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

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, java.lang.Comparable
        public int compareTo(List<? extends Long> l) {
            if (l instanceof LongImmutableList) {
                LongImmutableList other = (LongImmutableList) l;
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

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList, java.util.List
        /* renamed from: subList */
        public List<Long> subList(int from, int to) {
            ensureIndex(from);
            ensureIndex(to);
            if (from == to) {
                return LongImmutableList.EMPTY;
            }
            if (from > to) {
                throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            return new ImmutableSubList(this.innerList, this.from + from, this.from + to);
        }
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList, java.util.List
    /* renamed from: subList */
    public List<Long> subList(int from, int to) {
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
    public LongImmutableList m249clone() {
        return this;
    }

    public boolean equals(LongImmutableList l) {
        if (l == this || this.a == l.a) {
            return true;
        }
        int s = size();
        if (s != l.size()) {
            return false;
        }
        long[] a1 = this.a;
        long[] a2 = l.a;
        return Arrays.equals(a1, a2);
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, java.util.Collection, java.util.List
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || !(o instanceof List)) {
            return false;
        }
        if (o instanceof LongImmutableList) {
            return equals((LongImmutableList) o);
        }
        if (o instanceof ImmutableSubList) {
            return ((ImmutableSubList) o).equals(this);
        }
        return super.equals(o);
    }

    public int compareTo(LongImmutableList l) {
        if (this.a == l.a) {
            return 0;
        }
        int s1 = size();
        int s2 = l.size();
        long[] a1 = this.a;
        long[] a2 = l.a;
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
        if (l instanceof LongImmutableList) {
            return compareTo((LongImmutableList) l);
        }
        if (l instanceof ImmutableSubList) {
            ImmutableSubList other = (ImmutableSubList) l;
            return -other.compareTo((List<? extends Long>) this);
        }
        return super.compareTo(l);
    }
}
