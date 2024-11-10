package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Size64;
import it.unimi.dsi.fastutil.longs.LongHash;
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
public class LongLinkedOpenCustomHashSet extends AbstractLongSortedSet implements Serializable, Cloneable, Hash {
    private static final boolean ASSERTS = false;
    private static final int SPLITERATOR_CHARACTERISTICS = 337;
    private static final long serialVersionUID = 0;
    protected transient boolean containsNull;
    protected final float f;
    protected transient int first;
    protected transient long[] key;
    protected transient int last;
    protected transient long[] link;
    protected transient int mask;
    protected transient int maxFill;
    protected final transient int minN;
    protected transient int n;
    protected int size;
    protected LongHash.Strategy strategy;

    public LongLinkedOpenCustomHashSet(int expected, float f, LongHash.Strategy strategy) {
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
        this.key = new long[this.n + 1];
        this.link = new long[this.n + 1];
    }

    public LongLinkedOpenCustomHashSet(int expected, LongHash.Strategy strategy) {
        this(expected, 0.75f, strategy);
    }

    public LongLinkedOpenCustomHashSet(LongHash.Strategy strategy) {
        this(16, 0.75f, strategy);
    }

    public LongLinkedOpenCustomHashSet(Collection<? extends Long> c, float f, LongHash.Strategy strategy) {
        this(c.size(), f, strategy);
        addAll(c);
    }

    public LongLinkedOpenCustomHashSet(Collection<? extends Long> c, LongHash.Strategy strategy) {
        this(c, 0.75f, strategy);
    }

    public LongLinkedOpenCustomHashSet(LongCollection c, float f, LongHash.Strategy strategy) {
        this(c.size(), f, strategy);
        addAll(c);
    }

    public LongLinkedOpenCustomHashSet(LongCollection c, LongHash.Strategy strategy) {
        this(c, 0.75f, strategy);
    }

    public LongLinkedOpenCustomHashSet(LongIterator i, float f, LongHash.Strategy strategy) {
        this(16, f, strategy);
        while (i.hasNext()) {
            add(i.nextLong());
        }
    }

    public LongLinkedOpenCustomHashSet(LongIterator i, LongHash.Strategy strategy) {
        this(i, 0.75f, strategy);
    }

    public LongLinkedOpenCustomHashSet(Iterator<?> i, float f, LongHash.Strategy strategy) {
        this(LongIterators.asLongIterator(i), f, strategy);
    }

    public LongLinkedOpenCustomHashSet(Iterator<?> i, LongHash.Strategy strategy) {
        this(LongIterators.asLongIterator(i), strategy);
    }

    public LongLinkedOpenCustomHashSet(long[] a, int offset, int length, float f, LongHash.Strategy strategy) {
        this(length < 0 ? 0 : length, f, strategy);
        LongArrays.ensureOffsetLength(a, offset, length);
        for (int i = 0; i < length; i++) {
            add(a[offset + i]);
        }
    }

    public LongLinkedOpenCustomHashSet(long[] a, int offset, int length, LongHash.Strategy strategy) {
        this(a, offset, length, 0.75f, strategy);
    }

    public LongLinkedOpenCustomHashSet(long[] a, float f, LongHash.Strategy strategy) {
        this(a, 0, a.length, f, strategy);
    }

    public LongLinkedOpenCustomHashSet(long[] a, LongHash.Strategy strategy) {
        this(a, 0.75f, strategy);
    }

    public LongHash.Strategy strategy() {
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

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
    public boolean addAll(LongCollection c) {
        if (this.f <= 0.5d) {
            ensureCapacity(c.size());
        } else {
            tryCapacity(size() + c.size());
        }
        return super.addAll(c);
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection, java.util.Collection
    public boolean addAll(Collection<? extends Long> c) {
        if (this.f <= 0.5d) {
            ensureCapacity(c.size());
        } else {
            tryCapacity(size() + c.size());
        }
        return super.addAll(c);
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
    public boolean add(long k) {
        int pos;
        long curr;
        if (this.strategy.equals(k, 0L)) {
            if (this.containsNull) {
                return false;
            }
            pos = this.n;
            this.containsNull = true;
            this.key[this.n] = k;
        } else {
            long[] key = this.key;
            int mix = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
            int pos2 = mix;
            long curr2 = key[mix];
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
        long curr;
        long[] key = this.key;
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

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongSet, it.unimi.dsi.fastutil.longs.LongSet
    public boolean remove(long k) {
        long curr;
        if (this.strategy.equals(k, 0L)) {
            if (this.containsNull) {
                return removeNullEntry();
            }
            return false;
        }
        long[] key = this.key;
        int mix = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
        int pos = mix;
        long curr2 = key[mix];
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

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
    public boolean contains(long k) {
        long curr;
        if (this.strategy.equals(k, 0L)) {
            return this.containsNull;
        }
        long[] key = this.key;
        int mix = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
        int pos = mix;
        long curr2 = key[mix];
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

    public long removeFirstLong() {
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
        long k = this.key[pos];
        this.size--;
        if (this.strategy.equals(k, 0L)) {
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

    public long removeLastLong() {
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
        long k = this.key[pos];
        this.size--;
        if (this.strategy.equals(k, 0L)) {
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

    public boolean addAndMoveToFirst(long k) {
        int pos;
        if (this.strategy.equals(k, 0L)) {
            if (this.containsNull) {
                moveIndexToFirst(this.n);
                return false;
            }
            this.containsNull = true;
            pos = this.n;
        } else {
            long[] key = this.key;
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

    public boolean addAndMoveToLast(long k) {
        int pos;
        if (this.strategy.equals(k, 0L)) {
            if (this.containsNull) {
                moveIndexToLast(this.n);
                return false;
            }
            this.containsNull = true;
            pos = this.n;
        } else {
            long[] key = this.key;
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
        Arrays.fill(this.key, 0L);
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

    @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
    public long firstLong() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.first];
    }

    @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
    public long lastLong() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.last];
    }

    @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
    public LongSortedSet tailSet(long from) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
    public LongSortedSet headSet(long to) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
    public LongSortedSet subSet(long from, long to) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.longs.LongSortedSet, java.util.SortedSet
    /* renamed from: comparator */
    public Comparator<? super Long> comparator2() {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class SetIterator implements LongListIterator {
        int curr;
        int index;
        int next;
        int prev;

        SetIterator() {
            this.prev = -1;
            this.next = -1;
            this.curr = -1;
            this.index = -1;
            this.next = LongLinkedOpenCustomHashSet.this.first;
            this.index = 0;
        }

        SetIterator(long from) {
            this.prev = -1;
            this.next = -1;
            this.curr = -1;
            this.index = -1;
            if (LongLinkedOpenCustomHashSet.this.strategy.equals(from, 0L)) {
                if (LongLinkedOpenCustomHashSet.this.containsNull) {
                    this.next = (int) LongLinkedOpenCustomHashSet.this.link[LongLinkedOpenCustomHashSet.this.n];
                    this.prev = LongLinkedOpenCustomHashSet.this.n;
                    return;
                }
                throw new NoSuchElementException("The key " + from + " does not belong to this set.");
            }
            if (LongLinkedOpenCustomHashSet.this.strategy.equals(LongLinkedOpenCustomHashSet.this.key[LongLinkedOpenCustomHashSet.this.last], from)) {
                this.prev = LongLinkedOpenCustomHashSet.this.last;
                this.index = LongLinkedOpenCustomHashSet.this.size;
                return;
            }
            long[] key = LongLinkedOpenCustomHashSet.this.key;
            int pos = HashCommon.mix(LongLinkedOpenCustomHashSet.this.strategy.hashCode(from)) & LongLinkedOpenCustomHashSet.this.mask;
            while (key[pos] != 0) {
                if (LongLinkedOpenCustomHashSet.this.strategy.equals(key[pos], from)) {
                    this.next = (int) LongLinkedOpenCustomHashSet.this.link[pos];
                    this.prev = pos;
                    return;
                }
                pos = (pos + 1) & LongLinkedOpenCustomHashSet.this.mask;
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

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong
        public long nextLong() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = this.next;
            this.next = (int) LongLinkedOpenCustomHashSet.this.link[this.curr];
            this.prev = this.curr;
            if (this.index >= 0) {
                this.index++;
            }
            return LongLinkedOpenCustomHashSet.this.key[this.curr];
        }

        @Override // it.unimi.dsi.fastutil.longs.LongBidirectionalIterator
        public long previousLong() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            this.curr = this.prev;
            this.prev = (int) (LongLinkedOpenCustomHashSet.this.link[this.curr] >>> 32);
            this.next = this.curr;
            if (this.index >= 0) {
                this.index--;
            }
            return LongLinkedOpenCustomHashSet.this.key[this.curr];
        }

        @Override // java.util.PrimitiveIterator
        public void forEachRemaining(java.util.function.LongConsumer action) {
            long[] key = LongLinkedOpenCustomHashSet.this.key;
            long[] link = LongLinkedOpenCustomHashSet.this.link;
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
                this.index = LongLinkedOpenCustomHashSet.this.size;
                return;
            }
            int pos = LongLinkedOpenCustomHashSet.this.first;
            this.index = 1;
            while (pos != this.prev) {
                pos = (int) LongLinkedOpenCustomHashSet.this.link[pos];
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

        /* JADX WARN: Code restructure failed: missing block: B:26:0x00db, code lost:            r1[r4] = r5;     */
        /* JADX WARN: Code restructure failed: missing block: B:27:0x00df, code lost:            if (r13.next != r0) goto L37;     */
        /* JADX WARN: Code restructure failed: missing block: B:28:0x00e1, code lost:            r13.next = r4;     */
        /* JADX WARN: Code restructure failed: missing block: B:30:0x00e5, code lost:            if (r13.prev != r0) goto L46;     */
        /* JADX WARN: Code restructure failed: missing block: B:31:0x00e7, code lost:            r13.prev = r4;     */
        @Override // it.unimi.dsi.fastutil.longs.LongListIterator, java.util.Iterator, java.util.ListIterator
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void remove() {
            /*
                Method dump skipped, instructions count: 254
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.longs.LongLinkedOpenCustomHashSet.SetIterator.remove():void");
        }
    }

    @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
    public LongListIterator iterator(long from) {
        return new SetIterator(from);
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongSortedSet, it.unimi.dsi.fastutil.longs.AbstractLongSet, it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
    public LongListIterator iterator() {
        return new SetIterator();
    }

    @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
    public LongSpliterator spliterator() {
        return LongSpliterators.asSpliterator(iterator(), Size64.sizeOf(this), SPLITERATOR_CHARACTERISTICS);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongIterable
    public void forEach(java.util.function.LongConsumer action) {
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
        int mask;
        long[] key;
        int newPrev;
        long[] key2 = this.key;
        int t = newN - 1;
        long[] newKey = new long[newN + 1];
        int i = this.first;
        int prev = -1;
        int pos2 = -1;
        long[] link = this.link;
        long[] newLink = new long[newN + 1];
        int i2 = -1;
        this.first = -1;
        int j = this.size;
        while (true) {
            int j2 = j - 1;
            if (j == 0) {
                break;
            }
            int newPrev2 = pos2;
            long[] link2 = link;
            if (this.strategy.equals(key2[i], 0L)) {
                pos = newN;
            } else {
                int pos3 = HashCommon.mix(this.strategy.hashCode(key2[i])) & t;
                while (newKey[pos3] != 0) {
                    pos3 = (pos3 + 1) & t;
                }
                pos = pos3;
            }
            newKey[pos] = key2[i];
            if (prev != i2) {
                newLink[newPrev2] = ((newLink[newPrev2] ^ (pos & 4294967295L)) & 4294967295L) ^ newLink[newPrev2];
                mask = t;
                key = key2;
                newLink[pos] = ((((newPrev2 & 4294967295L) << 32) ^ newLink[pos]) & (-4294967296L)) ^ newLink[pos];
                newPrev = pos;
            } else {
                mask = t;
                key = key2;
                this.first = pos;
                newPrev = pos;
                newLink[pos] = -1;
            }
            int t2 = i;
            i = (int) link2[i];
            prev = t2;
            pos2 = newPrev;
            j = j2;
            link = link2;
            key2 = key;
            t = mask;
            i2 = -1;
        }
        int mask2 = t;
        int newPrev3 = pos2;
        this.link = newLink;
        this.last = newPrev3;
        if (newPrev3 != -1) {
            newLink[newPrev3] = newLink[newPrev3] | 4294967295L;
        }
        this.n = newN;
        this.mask = mask2;
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.key = newKey;
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public LongLinkedOpenCustomHashSet m250clone() {
        try {
            LongLinkedOpenCustomHashSet c = (LongLinkedOpenCustomHashSet) super.clone();
            c.key = (long[]) this.key.clone();
            c.containsNull = this.containsNull;
            c.link = (long[]) this.link.clone();
            c.strategy = this.strategy;
            return c;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongSet, java.util.Collection, java.util.Set
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
        LongIterator i = iterator();
        s.defaultWriteObject();
        int j = this.size;
        while (true) {
            int j2 = j - 1;
            if (j == 0) {
                return;
            }
            s.writeLong(i.nextLong());
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
        long[] key = new long[this.n + 1];
        this.key = key;
        long[] link = new long[this.n + 1];
        this.link = link;
        int prev = -1;
        this.last = -1;
        this.first = -1;
        int pos2 = this.size;
        while (true) {
            int i2 = pos2 - 1;
            if (pos2 == 0) {
                break;
            }
            long k = s.readLong();
            if (!this.strategy.equals(k, 0L)) {
                int mix = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
                int pos3 = mix;
                if (key[mix] == 0) {
                    pos = pos3;
                }
                do {
                    i = (pos3 + 1) & this.mask;
                    pos3 = i;
                } while (key[i] != 0);
                pos = pos3;
            } else {
                pos = this.n;
                this.containsNull = z;
            }
            key[pos] = k;
            if (this.first != -1) {
                link[prev] = ((link[prev] ^ (pos & 4294967295L)) & 4294967295L) ^ link[prev];
                long[] link2 = link;
                link2[pos] = ((((prev & 4294967295L) << 32) ^ link[pos]) & (-4294967296L)) ^ link[pos];
                prev = pos;
                pos2 = i2;
                link = link2;
                z = true;
            } else {
                long[] link3 = link;
                this.first = pos;
                prev = pos;
                link3[pos] = link3[pos] | (-4294967296L);
                pos2 = i2;
                link = link3;
                z = true;
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
