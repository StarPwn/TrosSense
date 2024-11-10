package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Size64;
import it.unimi.dsi.fastutil.ints.IntHash;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* loaded from: classes4.dex */
public class IntLinkedOpenCustomHashSet extends AbstractIntSortedSet implements Serializable, Cloneable, Hash {
    private static final boolean ASSERTS = false;
    private static final int SPLITERATOR_CHARACTERISTICS = 337;
    private static final long serialVersionUID = 0;
    protected transient boolean containsNull;
    protected final float f;
    protected transient int first;
    protected transient int[] key;
    protected transient int last;
    protected transient long[] link;
    protected transient int mask;
    protected transient int maxFill;
    protected final transient int minN;
    protected transient int n;
    protected int size;
    protected IntHash.Strategy strategy;

    public IntLinkedOpenCustomHashSet(int expected, float f, IntHash.Strategy strategy) {
        this.first = -1;
        this.last = -1;
        this.strategy = strategy;
        if (f <= 0.0f || f >= 1.0f) {
            throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than 1");
        }
        if (expected < 0) {
            throw new IllegalArgumentException("The expected number of elements must be nonnegative");
        }
        this.f = f;
        int arraySize = HashCommon.arraySize(expected, f);
        this.n = arraySize;
        this.minN = arraySize;
        this.mask = this.n - 1;
        this.maxFill = HashCommon.maxFill(this.n, f);
        this.key = new int[this.n + 1];
        this.link = new long[this.n + 1];
    }

    public IntLinkedOpenCustomHashSet(int expected, IntHash.Strategy strategy) {
        this(expected, 0.75f, strategy);
    }

    public IntLinkedOpenCustomHashSet(IntHash.Strategy strategy) {
        this(16, 0.75f, strategy);
    }

    public IntLinkedOpenCustomHashSet(Collection<? extends Integer> c, float f, IntHash.Strategy strategy) {
        this(c.size(), f, strategy);
        addAll(c);
    }

    public IntLinkedOpenCustomHashSet(Collection<? extends Integer> c, IntHash.Strategy strategy) {
        this(c, 0.75f, strategy);
    }

    public IntLinkedOpenCustomHashSet(IntCollection c, float f, IntHash.Strategy strategy) {
        this(c.size(), f, strategy);
        addAll(c);
    }

    public IntLinkedOpenCustomHashSet(IntCollection c, IntHash.Strategy strategy) {
        this(c, 0.75f, strategy);
    }

    public IntLinkedOpenCustomHashSet(IntIterator i, float f, IntHash.Strategy strategy) {
        this(16, f, strategy);
        while (i.hasNext()) {
            add(i.nextInt());
        }
    }

    public IntLinkedOpenCustomHashSet(IntIterator i, IntHash.Strategy strategy) {
        this(i, 0.75f, strategy);
    }

    public IntLinkedOpenCustomHashSet(Iterator<?> i, float f, IntHash.Strategy strategy) {
        this(IntIterators.asIntIterator(i), f, strategy);
    }

    public IntLinkedOpenCustomHashSet(Iterator<?> i, IntHash.Strategy strategy) {
        this(IntIterators.asIntIterator(i), strategy);
    }

    public IntLinkedOpenCustomHashSet(int[] a, int offset, int length, float f, IntHash.Strategy strategy) {
        this(length < 0 ? 0 : length, f, strategy);
        IntArrays.ensureOffsetLength(a, offset, length);
        for (int i = 0; i < length; i++) {
            add(a[offset + i]);
        }
    }

    public IntLinkedOpenCustomHashSet(int[] a, int offset, int length, IntHash.Strategy strategy) {
        this(a, offset, length, 0.75f, strategy);
    }

    public IntLinkedOpenCustomHashSet(int[] a, float f, IntHash.Strategy strategy) {
        this(a, 0, a.length, f, strategy);
    }

    public IntLinkedOpenCustomHashSet(int[] a, IntHash.Strategy strategy) {
        this(a, 0.75f, strategy);
    }

    public IntHash.Strategy strategy() {
        return this.strategy;
    }

    private int realSize() {
        return this.containsNull ? this.size - 1 : this.size;
    }

    public void ensureCapacity(int capacity) {
        int needed = HashCommon.arraySize(capacity, this.f);
        if (needed > this.n) {
            rehash(needed);
        }
    }

    private void tryCapacity(long capacity) {
        int needed = (int) Math.min(1073741824L, Math.max(2L, HashCommon.nextPowerOfTwo((long) Math.ceil(((float) capacity) / this.f))));
        if (needed > this.n) {
            rehash(needed);
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
    public boolean addAll(IntCollection c) {
        if (this.f <= 0.5d) {
            ensureCapacity(c.size());
        } else {
            tryCapacity(size() + c.size());
        }
        return super.addAll(c);
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection
    public boolean addAll(Collection<? extends Integer> c) {
        if (this.f <= 0.5d) {
            ensureCapacity(c.size());
        } else {
            tryCapacity(size() + c.size());
        }
        return super.addAll(c);
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
    public boolean add(int k) {
        int pos;
        int curr;
        if (this.strategy.equals(k, 0)) {
            if (this.containsNull) {
                return false;
            }
            pos = this.n;
            this.containsNull = true;
            this.key[this.n] = k;
        } else {
            int[] key = this.key;
            int mix = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
            int pos2 = mix;
            int curr2 = key[mix];
            if (curr2 != 0) {
                if (this.strategy.equals(curr2, k)) {
                    return false;
                }
                do {
                    int i = (pos2 + 1) & this.mask;
                    pos2 = i;
                    curr = key[i];
                    if (curr != 0) {
                    }
                } while (!this.strategy.equals(curr, k));
                return false;
            }
            key[pos2] = k;
            pos = pos2;
        }
        if (this.size == 0) {
            this.last = pos;
            this.first = pos;
            this.link[pos] = -1;
        } else {
            long[] jArr = this.link;
            int i2 = this.last;
            jArr[i2] = jArr[i2] ^ ((this.link[this.last] ^ (pos & 4294967295L)) & 4294967295L);
            this.link[pos] = ((this.last & 4294967295L) << 32) | 4294967295L;
            this.last = pos;
        }
        int i3 = this.size;
        this.size = i3 + 1;
        if (i3 >= this.maxFill) {
            rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
        return true;
    }

    protected final void shiftKeys(int pos) {
        int curr;
        int[] key = this.key;
        while (true) {
            int last = pos;
            pos = (pos + 1) & this.mask;
            while (true) {
                curr = key[pos];
                if (curr == 0) {
                    key[last] = 0;
                    return;
                }
                int slot = HashCommon.mix(this.strategy.hashCode(curr)) & this.mask;
                if (last > pos) {
                    if (last >= slot && slot > pos) {
                        break;
                    }
                    pos = (pos + 1) & this.mask;
                } else if (last < slot && slot <= pos) {
                    pos = (pos + 1) & this.mask;
                }
            }
            key[last] = curr;
            fixPointers(pos, last);
        }
    }

    private boolean removeEntry(int pos) {
        this.size--;
        fixPointers(pos);
        shiftKeys(pos);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            rehash(this.n / 2);
        }
        return true;
    }

    private boolean removeNullEntry() {
        this.containsNull = false;
        this.key[this.n] = 0;
        this.size--;
        fixPointers(this.n);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            rehash(this.n / 2);
        }
        return true;
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntSet, it.unimi.dsi.fastutil.ints.IntSet
    public boolean remove(int k) {
        int curr;
        if (this.strategy.equals(k, 0)) {
            if (this.containsNull) {
                return removeNullEntry();
            }
            return false;
        }
        int[] key = this.key;
        int mix = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
        int pos = mix;
        int curr2 = key[mix];
        if (curr2 == 0) {
            return false;
        }
        if (this.strategy.equals(k, curr2)) {
            return removeEntry(pos);
        }
        do {
            int i = (pos + 1) & this.mask;
            pos = i;
            curr = key[i];
            if (curr == 0) {
                return false;
            }
        } while (!this.strategy.equals(k, curr));
        return removeEntry(pos);
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
    public boolean contains(int k) {
        int curr;
        if (this.strategy.equals(k, 0)) {
            return this.containsNull;
        }
        int[] key = this.key;
        int mix = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
        int pos = mix;
        int curr2 = key[mix];
        if (curr2 == 0) {
            return false;
        }
        if (this.strategy.equals(k, curr2)) {
            return true;
        }
        do {
            int i = (pos + 1) & this.mask;
            pos = i;
            curr = key[i];
            if (curr == 0) {
                return false;
            }
        } while (!this.strategy.equals(k, curr));
        return true;
    }

    public int removeFirstInt() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        int pos = this.first;
        if (this.size == 1) {
            this.last = -1;
            this.first = -1;
        } else {
            this.first = (int) this.link[pos];
            if (this.first >= 0) {
                long[] jArr = this.link;
                int i = this.first;
                jArr[i] = jArr[i] | (-4294967296L);
            }
        }
        int k = this.key[pos];
        this.size--;
        if (this.strategy.equals(k, 0)) {
            this.containsNull = false;
            this.key[this.n] = 0;
        } else {
            shiftKeys(pos);
        }
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            rehash(this.n / 2);
        }
        return k;
    }

    public int removeLastInt() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        int pos = this.last;
        if (this.size == 1) {
            this.last = -1;
            this.first = -1;
        } else {
            this.last = (int) (this.link[pos] >>> 32);
            if (this.last >= 0) {
                long[] jArr = this.link;
                int i = this.last;
                jArr[i] = jArr[i] | 4294967295L;
            }
        }
        int k = this.key[pos];
        this.size--;
        if (this.strategy.equals(k, 0)) {
            this.containsNull = false;
            this.key[this.n] = 0;
        } else {
            shiftKeys(pos);
        }
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            rehash(this.n / 2);
        }
        return k;
    }

    private void moveIndexToFirst(int i) {
        if (this.size == 1 || this.first == i) {
            return;
        }
        if (this.last == i) {
            this.last = (int) (this.link[i] >>> 32);
            long[] jArr = this.link;
            int i2 = this.last;
            jArr[i2] = jArr[i2] | 4294967295L;
        } else {
            long linki = this.link[i];
            int prev = (int) (linki >>> 32);
            int next = (int) linki;
            long[] jArr2 = this.link;
            jArr2[prev] = jArr2[prev] ^ ((this.link[prev] ^ (linki & 4294967295L)) & 4294967295L);
            long[] jArr3 = this.link;
            jArr3[next] = jArr3[next] ^ ((this.link[next] ^ (linki & (-4294967296L))) & (-4294967296L));
        }
        long[] jArr4 = this.link;
        int i3 = this.first;
        jArr4[i3] = jArr4[i3] ^ ((this.link[this.first] ^ ((i & 4294967295L) << 32)) & (-4294967296L));
        this.link[i] = (4294967295L & this.first) | (-4294967296L);
        this.first = i;
    }

    private void moveIndexToLast(int i) {
        if (this.size == 1 || this.last == i) {
            return;
        }
        if (this.first == i) {
            this.first = (int) this.link[i];
            long[] jArr = this.link;
            int i2 = this.first;
            jArr[i2] = (-4294967296L) | jArr[i2];
        } else {
            long linki = this.link[i];
            int prev = (int) (linki >>> 32);
            int next = (int) linki;
            long[] jArr2 = this.link;
            jArr2[prev] = jArr2[prev] ^ ((this.link[prev] ^ (linki & 4294967295L)) & 4294967295L);
            long[] jArr3 = this.link;
            jArr3[next] = ((-4294967296L) & (this.link[next] ^ (linki & (-4294967296L)))) ^ jArr3[next];
        }
        long[] jArr4 = this.link;
        int i3 = this.last;
        jArr4[i3] = jArr4[i3] ^ ((this.link[this.last] ^ (i & 4294967295L)) & 4294967295L);
        this.link[i] = ((this.last & 4294967295L) << 32) | 4294967295L;
        this.last = i;
    }

    public boolean addAndMoveToFirst(int k) {
        int pos;
        if (this.strategy.equals(k, 0)) {
            if (this.containsNull) {
                moveIndexToFirst(this.n);
                return false;
            }
            this.containsNull = true;
            pos = this.n;
        } else {
            int[] key = this.key;
            int pos2 = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
            while (key[pos2] != 0) {
                if (this.strategy.equals(k, key[pos2])) {
                    moveIndexToFirst(pos2);
                    return false;
                }
                pos2 = (pos2 + 1) & this.mask;
            }
            pos = pos2;
        }
        this.key[pos] = k;
        if (this.size == 0) {
            this.last = pos;
            this.first = pos;
            this.link[pos] = -1;
        } else {
            long[] jArr = this.link;
            int i = this.first;
            jArr[i] = jArr[i] ^ ((this.link[this.first] ^ ((pos & 4294967295L) << 32)) & (-4294967296L));
            this.link[pos] = (this.first & 4294967295L) | (-4294967296L);
            this.first = pos;
        }
        int i2 = this.size;
        this.size = i2 + 1;
        if (i2 >= this.maxFill) {
            rehash(HashCommon.arraySize(this.size, this.f));
        }
        return true;
    }

    public boolean addAndMoveToLast(int k) {
        int pos;
        if (this.strategy.equals(k, 0)) {
            if (this.containsNull) {
                moveIndexToLast(this.n);
                return false;
            }
            this.containsNull = true;
            pos = this.n;
        } else {
            int[] key = this.key;
            int pos2 = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
            while (key[pos2] != 0) {
                if (this.strategy.equals(k, key[pos2])) {
                    moveIndexToLast(pos2);
                    return false;
                }
                pos2 = (pos2 + 1) & this.mask;
            }
            pos = pos2;
        }
        this.key[pos] = k;
        if (this.size == 0) {
            this.last = pos;
            this.first = pos;
            this.link[pos] = -1;
        } else {
            long[] jArr = this.link;
            int i = this.last;
            jArr[i] = jArr[i] ^ ((this.link[this.last] ^ (pos & 4294967295L)) & 4294967295L);
            this.link[pos] = ((this.last & 4294967295L) << 32) | 4294967295L;
            this.last = pos;
        }
        int i2 = this.size;
        this.size = i2 + 1;
        if (i2 >= this.maxFill) {
            rehash(HashCommon.arraySize(this.size, this.f));
        }
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public void clear() {
        if (this.size == 0) {
            return;
        }
        this.size = 0;
        this.containsNull = false;
        Arrays.fill(this.key, 0);
        this.last = -1;
        this.first = -1;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.size;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean isEmpty() {
        return this.size == 0;
    }

    protected void fixPointers(int i) {
        if (this.size == 0) {
            this.last = -1;
            this.first = -1;
            return;
        }
        if (this.first == i) {
            this.first = (int) this.link[i];
            if (this.first >= 0) {
                long[] jArr = this.link;
                int i2 = this.first;
                jArr[i2] = (-4294967296L) | jArr[i2];
                return;
            }
            return;
        }
        if (this.last == i) {
            this.last = (int) (this.link[i] >>> 32);
            if (this.last >= 0) {
                long[] jArr2 = this.link;
                int i3 = this.last;
                jArr2[i3] = jArr2[i3] | 4294967295L;
                return;
            }
            return;
        }
        long linki = this.link[i];
        int prev = (int) (linki >>> 32);
        int next = (int) linki;
        long[] jArr3 = this.link;
        jArr3[prev] = (4294967295L & (this.link[prev] ^ (linki & 4294967295L))) ^ jArr3[prev];
        long[] jArr4 = this.link;
        jArr4[next] = ((-4294967296L) & (this.link[next] ^ (linki & (-4294967296L)))) ^ jArr4[next];
    }

    protected void fixPointers(int s, int d) {
        if (this.size == 1) {
            this.last = d;
            this.first = d;
            this.link[d] = -1;
            return;
        }
        if (this.first == s) {
            this.first = d;
            long[] jArr = this.link;
            int i = (int) this.link[s];
            jArr[i] = ((((d & 4294967295L) << 32) ^ this.link[(int) this.link[s]]) & (-4294967296L)) ^ jArr[i];
            this.link[d] = this.link[s];
            return;
        }
        if (this.last == s) {
            this.last = d;
            long[] jArr2 = this.link;
            int i2 = (int) (this.link[s] >>> 32);
            jArr2[i2] = (((d & 4294967295L) ^ this.link[(int) (this.link[s] >>> 32)]) & 4294967295L) ^ jArr2[i2];
            this.link[d] = this.link[s];
            return;
        }
        long links = this.link[s];
        int prev = (int) (links >>> 32);
        int next = (int) links;
        long[] jArr3 = this.link;
        jArr3[prev] = jArr3[prev] ^ ((this.link[prev] ^ (d & 4294967295L)) & 4294967295L);
        long[] jArr4 = this.link;
        jArr4[next] = ((this.link[next] ^ ((4294967295L & d) << 32)) & (-4294967296L)) ^ jArr4[next];
        this.link[d] = links;
    }

    @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
    public int firstInt() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.first];
    }

    @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
    public int lastInt() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.last];
    }

    @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
    public IntSortedSet tailSet(int from) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
    public IntSortedSet headSet(int to) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
    public IntSortedSet subSet(int from, int to) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.ints.IntSortedSet, java.util.SortedSet
    /* renamed from: comparator */
    public Comparator<? super Integer> comparator2() {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class SetIterator implements IntListIterator {
        int curr;
        int index;
        int next;
        int prev;

        SetIterator() {
            this.prev = -1;
            this.next = -1;
            this.curr = -1;
            this.index = -1;
            this.next = IntLinkedOpenCustomHashSet.this.first;
            this.index = 0;
        }

        SetIterator(int from) {
            this.prev = -1;
            this.next = -1;
            this.curr = -1;
            this.index = -1;
            if (IntLinkedOpenCustomHashSet.this.strategy.equals(from, 0)) {
                if (IntLinkedOpenCustomHashSet.this.containsNull) {
                    this.next = (int) IntLinkedOpenCustomHashSet.this.link[IntLinkedOpenCustomHashSet.this.n];
                    this.prev = IntLinkedOpenCustomHashSet.this.n;
                    return;
                }
                throw new NoSuchElementException("The key " + from + " does not belong to this set.");
            }
            if (IntLinkedOpenCustomHashSet.this.strategy.equals(IntLinkedOpenCustomHashSet.this.key[IntLinkedOpenCustomHashSet.this.last], from)) {
                this.prev = IntLinkedOpenCustomHashSet.this.last;
                this.index = IntLinkedOpenCustomHashSet.this.size;
                return;
            }
            int[] key = IntLinkedOpenCustomHashSet.this.key;
            int pos = HashCommon.mix(IntLinkedOpenCustomHashSet.this.strategy.hashCode(from)) & IntLinkedOpenCustomHashSet.this.mask;
            while (key[pos] != 0) {
                if (IntLinkedOpenCustomHashSet.this.strategy.equals(key[pos], from)) {
                    this.next = (int) IntLinkedOpenCustomHashSet.this.link[pos];
                    this.prev = pos;
                    return;
                }
                pos = (pos + 1) & IntLinkedOpenCustomHashSet.this.mask;
            }
            throw new NoSuchElementException("The key " + from + " does not belong to this set.");
        }

        @Override // java.util.Iterator, java.util.ListIterator
        public boolean hasNext() {
            return this.next != -1;
        }

        @Override // it.unimi.dsi.fastutil.BidirectionalIterator
        public boolean hasPrevious() {
            return this.prev != -1;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt
        public int nextInt() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = this.next;
            this.next = (int) IntLinkedOpenCustomHashSet.this.link[this.curr];
            this.prev = this.curr;
            if (this.index >= 0) {
                this.index++;
            }
            return IntLinkedOpenCustomHashSet.this.key[this.curr];
        }

        @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator
        public int previousInt() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            this.curr = this.prev;
            this.prev = (int) (IntLinkedOpenCustomHashSet.this.link[this.curr] >>> 32);
            this.next = this.curr;
            if (this.index >= 0) {
                this.index--;
            }
            return IntLinkedOpenCustomHashSet.this.key[this.curr];
        }

        @Override // java.util.PrimitiveIterator
        public void forEachRemaining(java.util.function.IntConsumer action) {
            int[] key = IntLinkedOpenCustomHashSet.this.key;
            long[] link = IntLinkedOpenCustomHashSet.this.link;
            while (this.next != -1) {
                this.curr = this.next;
                this.next = (int) link[this.curr];
                this.prev = this.curr;
                if (this.index >= 0) {
                    this.index++;
                }
                action.accept(key[this.curr]);
            }
        }

        private final void ensureIndexKnown() {
            if (this.index >= 0) {
                return;
            }
            if (this.prev == -1) {
                this.index = 0;
                return;
            }
            if (this.next == -1) {
                this.index = IntLinkedOpenCustomHashSet.this.size;
                return;
            }
            int pos = IntLinkedOpenCustomHashSet.this.first;
            this.index = 1;
            while (pos != this.prev) {
                pos = (int) IntLinkedOpenCustomHashSet.this.link[pos];
                this.index++;
            }
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            ensureIndexKnown();
            return this.index;
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            ensureIndexKnown();
            return this.index - 1;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntListIterator, java.util.Iterator, java.util.ListIterator
        public void remove() {
            int curr;
            ensureIndexKnown();
            if (this.curr == -1) {
                throw new IllegalStateException();
            }
            if (this.curr == this.prev) {
                this.index--;
                this.prev = (int) (IntLinkedOpenCustomHashSet.this.link[this.curr] >>> 32);
            } else {
                this.next = (int) IntLinkedOpenCustomHashSet.this.link[this.curr];
            }
            IntLinkedOpenCustomHashSet intLinkedOpenCustomHashSet = IntLinkedOpenCustomHashSet.this;
            intLinkedOpenCustomHashSet.size--;
            if (this.prev == -1) {
                IntLinkedOpenCustomHashSet.this.first = this.next;
            } else {
                long[] jArr = IntLinkedOpenCustomHashSet.this.link;
                int i = this.prev;
                jArr[i] = jArr[i] ^ ((IntLinkedOpenCustomHashSet.this.link[this.prev] ^ (this.next & 4294967295L)) & 4294967295L);
            }
            if (this.next == -1) {
                IntLinkedOpenCustomHashSet.this.last = this.prev;
            } else {
                long[] jArr2 = IntLinkedOpenCustomHashSet.this.link;
                int i2 = this.next;
                jArr2[i2] = ((((4294967295L & this.prev) << 32) ^ IntLinkedOpenCustomHashSet.this.link[this.next]) & (-4294967296L)) ^ jArr2[i2];
            }
            int pos = this.curr;
            this.curr = -1;
            if (pos == IntLinkedOpenCustomHashSet.this.n) {
                IntLinkedOpenCustomHashSet.this.containsNull = false;
                IntLinkedOpenCustomHashSet.this.key[IntLinkedOpenCustomHashSet.this.n] = 0;
                return;
            }
            int[] key = IntLinkedOpenCustomHashSet.this.key;
            while (true) {
                int last = pos;
                pos = (pos + 1) & IntLinkedOpenCustomHashSet.this.mask;
                while (true) {
                    curr = key[pos];
                    if (curr == 0) {
                        key[last] = 0;
                        return;
                    }
                    int slot = HashCommon.mix(IntLinkedOpenCustomHashSet.this.strategy.hashCode(curr)) & IntLinkedOpenCustomHashSet.this.mask;
                    if (last > pos) {
                        if (last >= slot && slot > pos) {
                            break;
                        }
                        pos = (pos + 1) & IntLinkedOpenCustomHashSet.this.mask;
                    } else if (last >= slot || slot > pos) {
                        break;
                    } else {
                        pos = (pos + 1) & IntLinkedOpenCustomHashSet.this.mask;
                    }
                }
                key[last] = curr;
                if (this.next == pos) {
                    this.next = last;
                }
                if (this.prev == pos) {
                    this.prev = last;
                }
                IntLinkedOpenCustomHashSet.this.fixPointers(pos, last);
            }
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
    public IntListIterator iterator(int from) {
        return new SetIterator(from);
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntSortedSet, it.unimi.dsi.fastutil.ints.AbstractIntSet, it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
    public IntListIterator iterator() {
        return new SetIterator();
    }

    @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
    public IntSpliterator spliterator() {
        return IntSpliterators.asSpliterator(iterator(), Size64.sizeOf(this), SPLITERATOR_CHARACTERISTICS);
    }

    @Override // it.unimi.dsi.fastutil.ints.IntIterable
    public void forEach(java.util.function.IntConsumer action) {
        int next = this.first;
        while (next != -1) {
            int curr = next;
            next = (int) this.link[curr];
            action.accept(this.key[curr]);
        }
    }

    public boolean trim() {
        return trim(this.size);
    }

    public boolean trim(int n) {
        int l = HashCommon.nextPowerOfTwo((int) Math.ceil(n / this.f));
        if (l >= this.n || this.size > HashCommon.maxFill(l, this.f)) {
            return true;
        }
        try {
            rehash(l);
            return true;
        } catch (OutOfMemoryError e) {
            return false;
        }
    }

    protected void rehash(int newN) {
        int pos;
        int[] key;
        int mask;
        int[] key2 = this.key;
        int mask2 = newN - 1;
        int[] newKey = new int[newN + 1];
        int i = this.first;
        int prev = -1;
        int newPrev = -1;
        long[] link = this.link;
        long[] newLink = new long[newN + 1];
        int i2 = -1;
        this.first = -1;
        int pos2 = this.size;
        while (true) {
            int j = pos2 - 1;
            if (pos2 == 0) {
                break;
            }
            if (this.strategy.equals(key2[i], 0)) {
                pos = newN;
            } else {
                pos = HashCommon.mix(this.strategy.hashCode(key2[i])) & mask2;
                while (newKey[pos] != 0) {
                    pos = (pos + 1) & mask2;
                }
            }
            newKey[pos] = key2[i];
            if (prev != i2) {
                key = key2;
                mask = mask2;
                newLink[newPrev] = ((newLink[newPrev] ^ (pos & 4294967295L)) & 4294967295L) ^ newLink[newPrev];
                int pos3 = pos;
                newLink[pos3] = newLink[pos] ^ ((((newPrev & 4294967295L) << 32) ^ newLink[pos]) & (-4294967296L));
                newPrev = pos3;
            } else {
                key = key2;
                mask = mask2;
                this.first = pos;
                int newPrev2 = pos;
                newLink[pos] = -1;
                newPrev = newPrev2;
            }
            int newPrev3 = i;
            i = (int) link[i];
            prev = newPrev3;
            pos2 = j;
            key2 = key;
            mask2 = mask;
            i2 = -1;
        }
        int mask3 = mask2;
        this.link = newLink;
        this.last = newPrev;
        if (newPrev != -1) {
            newLink[newPrev] = newLink[newPrev] | 4294967295L;
        }
        this.n = newN;
        this.mask = mask3;
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.key = newKey;
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public IntLinkedOpenCustomHashSet m231clone() {
        try {
            IntLinkedOpenCustomHashSet c = (IntLinkedOpenCustomHashSet) super.clone();
            c.key = (int[]) this.key.clone();
            c.containsNull = this.containsNull;
            c.link = (long[]) this.link.clone();
            c.strategy = this.strategy;
            return c;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntSet, java.util.Collection, java.util.Set
    public int hashCode() {
        int h = 0;
        int j = realSize();
        int i = 0;
        while (true) {
            int j2 = j - 1;
            if (j != 0) {
                while (this.key[i] == 0) {
                    i++;
                }
                h += this.strategy.hashCode(this.key[i]);
                i++;
                j = j2;
            } else {
                return h;
            }
        }
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        IntIterator i = iterator();
        s.defaultWriteObject();
        int j = this.size;
        while (true) {
            int j2 = j - 1;
            if (j == 0) {
                return;
            }
            s.writeInt(i.nextInt());
            j = j2;
        }
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        int pos;
        int i;
        s.defaultReadObject();
        this.n = HashCommon.arraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        boolean z = true;
        this.mask = this.n - 1;
        int[] key = new int[this.n + 1];
        this.key = key;
        long[] link = new long[this.n + 1];
        this.link = link;
        int prev = -1;
        int i2 = -1;
        this.last = -1;
        this.first = -1;
        int i3 = this.size;
        while (true) {
            int i4 = i3 - 1;
            if (i3 == 0) {
                break;
            }
            int k = s.readInt();
            if (!this.strategy.equals(k, 0)) {
                int mix = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
                int pos2 = mix;
                if (key[mix] == 0) {
                    pos = pos2;
                }
                do {
                    i = (pos2 + 1) & this.mask;
                    pos2 = i;
                } while (key[i] != 0);
                pos = pos2;
            } else {
                pos = this.n;
                this.containsNull = z;
            }
            key[pos] = k;
            if (this.first != i2) {
                link[prev] = ((link[prev] ^ (pos & 4294967295L)) & 4294967295L) ^ link[prev];
                long[] link2 = link;
                link2[pos] = ((((prev & 4294967295L) << 32) ^ link[pos]) & (-4294967296L)) ^ link[pos];
                prev = pos;
                i3 = i4;
                link = link2;
                z = true;
                i2 = -1;
            } else {
                long[] link3 = link;
                this.first = pos;
                prev = pos;
                link3[pos] = link3[pos] | (-4294967296L);
                i3 = i4;
                link = link3;
                z = true;
                i2 = -1;
            }
        }
        long[] link4 = link;
        this.last = prev;
        if (prev != -1) {
            link4[prev] = link4[prev] | 4294967295L;
        }
    }

    private void checkTable() {
    }
}
