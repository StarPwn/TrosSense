package it.unimi.dsi.fastutil.ints;

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
public class IntArraySet extends AbstractIntSet implements Serializable, Cloneable {
    private static final long serialVersionUID = 1;
    protected transient int[] a;
    protected int size;

    public IntArraySet(int[] a) {
        this.a = a;
        this.size = a.length;
    }

    public IntArraySet() {
        this.a = IntArrays.EMPTY_ARRAY;
    }

    public IntArraySet(int capacity) {
        this.a = new int[capacity];
    }

    public IntArraySet(IntCollection c) {
        this(c.size());
        addAll(c);
    }

    public IntArraySet(Collection<? extends Integer> c) {
        this(c.size());
        addAll(c);
    }

    public IntArraySet(IntSet c) {
        this(c.size());
        int i = 0;
        IntIterator it2 = c.iterator();
        while (it2.hasNext()) {
            int x = it2.next().intValue();
            this.a[i] = x;
            i++;
        }
        this.size = i;
    }

    public IntArraySet(Set<? extends Integer> c) {
        this(c.size());
        int i = 0;
        for (Integer x : c) {
            this.a[i] = x.intValue();
            i++;
        }
        this.size = i;
    }

    public IntArraySet(int[] a, int size) {
        this.a = a;
        this.size = size;
        if (size > a.length) {
            throw new IllegalArgumentException("The provided size (" + size + ") is larger than or equal to the array size (" + a.length + ")");
        }
    }

    public static IntArraySet of() {
        return ofUnchecked();
    }

    public static IntArraySet of(int e) {
        return ofUnchecked(e);
    }

    public static IntArraySet of(int... a) {
        if (a.length == 2) {
            if (a[0] == a[1]) {
                throw new IllegalArgumentException("Duplicate element: " + a[1]);
            }
        } else if (a.length > 2) {
            IntOpenHashSet.of(a);
        }
        return ofUnchecked(a);
    }

    public static IntArraySet ofUnchecked() {
        return new IntArraySet();
    }

    public static IntArraySet ofUnchecked(int... a) {
        return new IntArraySet(a);
    }

    private int findKey(int o) {
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

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntSet, it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
    public IntIterator iterator() {
        return new IntIterator() { // from class: it.unimi.dsi.fastutil.ints.IntArraySet.1
            int next = 0;

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.next < IntArraySet.this.size;
            }

            @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt
            public int nextInt() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                int[] iArr = IntArraySet.this.a;
                int i = this.next;
                this.next = i + 1;
                return iArr[i];
            }

            @Override // java.util.Iterator
            public void remove() {
                IntArraySet intArraySet = IntArraySet.this;
                int i = intArraySet.size;
                intArraySet.size = i - 1;
                int i2 = this.next;
                this.next = i2 - 1;
                int tail = i - i2;
                System.arraycopy(IntArraySet.this.a, this.next + 1, IntArraySet.this.a, this.next, tail);
            }

            @Override // it.unimi.dsi.fastutil.ints.IntIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
            public int skip(int n) {
                if (n < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n);
                }
                int remaining = IntArraySet.this.size - this.next;
                if (n < remaining) {
                    this.next += n;
                    return n;
                }
                this.next = IntArraySet.this.size;
                return remaining;
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

        public Spliterator(IntArraySet intArraySet) {
            this(0, intArraySet.size, false);
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
            return this.hasSplit ? this.max : IntArraySet.this.size;
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
        public boolean tryAdvance(java.util.function.IntConsumer action) {
            if (this.pos >= getWorkingMax()) {
                return false;
            }
            int[] iArr = IntArraySet.this.a;
            int i = this.pos;
            this.pos = i + 1;
            action.accept(iArr[i]);
            return true;
        }

        @Override // java.util.Spliterator.OfPrimitive
        public void forEachRemaining(java.util.function.IntConsumer action) {
            int max = getWorkingMax();
            while (this.pos < max) {
                action.accept(IntArraySet.this.a[this.pos]);
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

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
    public boolean contains(int k) {
        return findKey(k) != -1;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.size;
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntSet, it.unimi.dsi.fastutil.ints.IntSet
    public boolean remove(int k) {
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

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
    public boolean add(int k) {
        int pos = findKey(k);
        if (pos != -1) {
            return false;
        }
        if (this.size == this.a.length) {
            int[] b = new int[this.size != 0 ? 2 * this.size : 2];
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
        int[] b2 = this.a;
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

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
    public int[] toIntArray() {
        return this.size == 0 ? IntArrays.EMPTY_ARRAY : Arrays.copyOf(this.a, this.size);
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
    public int[] toArray(int[] a) {
        if (a == null || a.length < this.size) {
            a = new int[this.size];
        }
        System.arraycopy(this.a, 0, a, 0, this.size);
        return a;
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public IntArraySet m229clone() {
        try {
            IntArraySet c = (IntArraySet) super.clone();
            c.a = (int[]) this.a.clone();
            return c;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
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
