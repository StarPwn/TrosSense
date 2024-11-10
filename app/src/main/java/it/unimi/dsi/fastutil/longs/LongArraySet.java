package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.SafeMath;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Set;

/* loaded from: classes4.dex */
public class LongArraySet extends AbstractLongSet implements Serializable, Cloneable {
    private static final long serialVersionUID = 1;
    protected transient long[] a;
    protected int size;

    public LongArraySet(long[] a) {
        this.a = a;
        this.size = a.length;
    }

    public LongArraySet() {
        this.a = LongArrays.EMPTY_ARRAY;
    }

    public LongArraySet(int capacity) {
        this.a = new long[capacity];
    }

    public LongArraySet(LongCollection c) {
        this(c.size());
        addAll(c);
    }

    public LongArraySet(Collection<? extends Long> c) {
        this(c.size());
        addAll(c);
    }

    public LongArraySet(LongSet c) {
        this(c.size());
        int i = 0;
        LongIterator it2 = c.iterator();
        while (it2.hasNext()) {
            long x = it2.next().longValue();
            this.a[i] = x;
            i++;
        }
        this.size = i;
    }

    public LongArraySet(Set<? extends Long> c) {
        this(c.size());
        int i = 0;
        for (Long x : c) {
            this.a[i] = x.longValue();
            i++;
        }
        this.size = i;
    }

    public LongArraySet(long[] a, int size) {
        this.a = a;
        this.size = size;
        if (size > a.length) {
            throw new IllegalArgumentException("The provided size (" + size + ") is larger than or equal to the array size (" + a.length + ")");
        }
    }

    public static LongArraySet of() {
        return ofUnchecked();
    }

    public static LongArraySet of(long e) {
        return ofUnchecked(e);
    }

    public static LongArraySet of(long... a) {
        if (a.length == 2) {
            if (a[0] == a[1]) {
                throw new IllegalArgumentException("Duplicate element: " + a[1]);
            }
        } else if (a.length > 2) {
            LongOpenHashSet.of(a);
        }
        return ofUnchecked(a);
    }

    public static LongArraySet ofUnchecked() {
        return new LongArraySet();
    }

    public static LongArraySet ofUnchecked(long... a) {
        return new LongArraySet(a);
    }

    private int findKey(long o) {
        int i = this.size;
        while (true) {
            int i2 = i - 1;
            if (i == 0) {
                return -1;
            }
            if (this.a[i2] == o) {
                return i2;
            }
            i = i2;
        }
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongSet, it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
    public LongIterator iterator() {
        return new LongIterator() { // from class: it.unimi.dsi.fastutil.longs.LongArraySet.1
            int next = 0;

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.next < LongArraySet.this.size;
            }

            @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong
            public long nextLong() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                long[] jArr = LongArraySet.this.a;
                int i = this.next;
                this.next = i + 1;
                return jArr[i];
            }

            @Override // java.util.Iterator
            public void remove() {
                LongArraySet longArraySet = LongArraySet.this;
                int i = longArraySet.size;
                longArraySet.size = i - 1;
                int i2 = this.next;
                this.next = i2 - 1;
                int tail = i - i2;
                System.arraycopy(LongArraySet.this.a, this.next + 1, LongArraySet.this.a, this.next, tail);
            }

            @Override // it.unimi.dsi.fastutil.longs.LongIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
            public int skip(int n) {
                if (n < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n);
                }
                int remaining = LongArraySet.this.size - this.next;
                if (n < remaining) {
                    this.next += n;
                    return n;
                }
                this.next = LongArraySet.this.size;
                return remaining;
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

        public Spliterator(LongArraySet longArraySet) {
            this(0, longArraySet.size, false);
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
            return this.hasSplit ? this.max : LongArraySet.this.size;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return 16721;
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
            long[] jArr = LongArraySet.this.a;
            int i = this.pos;
            this.pos = i + 1;
            action.accept(jArr[i]);
            return true;
        }

        @Override // java.util.Spliterator.OfPrimitive
        public void forEachRemaining(java.util.function.LongConsumer action) {
            int max = getWorkingMax();
            while (this.pos < max) {
                action.accept(LongArraySet.this.a[this.pos]);
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

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
    public boolean contains(long k) {
        return findKey(k) != -1;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.size;
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongSet, it.unimi.dsi.fastutil.longs.LongSet
    public boolean remove(long k) {
        int pos = findKey(k);
        if (pos == -1) {
            return false;
        }
        int tail = (this.size - pos) - 1;
        for (int i = 0; i < tail; i++) {
            this.a[pos + i] = this.a[pos + i + 1];
        }
        int i2 = this.size;
        this.size = i2 - 1;
        return true;
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
    public boolean add(long k) {
        int pos = findKey(k);
        if (pos != -1) {
            return false;
        }
        if (this.size == this.a.length) {
            long[] b = new long[this.size != 0 ? 2 * this.size : 2];
            int i = this.size;
            while (true) {
                int i2 = i - 1;
                if (i == 0) {
                    break;
                }
                b[i2] = this.a[i2];
                i = i2;
            }
            this.a = b;
        }
        long[] b2 = this.a;
        int i3 = this.size;
        this.size = i3 + 1;
        b2[i3] = k;
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public void clear() {
        this.size = 0;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
    public long[] toLongArray() {
        return this.size == 0 ? LongArrays.EMPTY_ARRAY : Arrays.copyOf(this.a, this.size);
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
    public long[] toArray(long[] a) {
        if (a == null || a.length < this.size) {
            a = new long[this.size];
        }
        System.arraycopy(this.a, 0, a, 0, this.size);
        return a;
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public LongArraySet m248clone() {
        try {
            LongArraySet c = (LongArraySet) super.clone();
            c.a = (long[]) this.a.clone();
            return c;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
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
