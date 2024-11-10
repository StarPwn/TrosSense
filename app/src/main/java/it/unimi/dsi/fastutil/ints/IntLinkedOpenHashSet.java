package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Size64;
import it.unimi.dsi.fastutil.ints.IntCollections;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/* loaded from: classes4.dex */
public class IntLinkedOpenHashSet extends AbstractIntSortedSet implements Serializable, Cloneable, Hash {
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

    public IntLinkedOpenHashSet(int expected, float f) {
        this.first = -1;
        this.last = -1;
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

    public IntLinkedOpenHashSet(int expected) {
        this(expected, 0.75f);
    }

    public IntLinkedOpenHashSet() {
        this(16, 0.75f);
    }

    public IntLinkedOpenHashSet(Collection<? extends Integer> c, float f) {
        this(c.size(), f);
        addAll(c);
    }

    public IntLinkedOpenHashSet(Collection<? extends Integer> c) {
        this(c, 0.75f);
    }

    public IntLinkedOpenHashSet(IntCollection c, float f) {
        this(c.size(), f);
        addAll(c);
    }

    public IntLinkedOpenHashSet(IntCollection c) {
        this(c, 0.75f);
    }

    public IntLinkedOpenHashSet(IntIterator i, float f) {
        this(16, f);
        while (i.hasNext()) {
            add(i.nextInt());
        }
    }

    public IntLinkedOpenHashSet(IntIterator i) {
        this(i, 0.75f);
    }

    public IntLinkedOpenHashSet(Iterator<?> i, float f) {
        this(IntIterators.asIntIterator(i), f);
    }

    public IntLinkedOpenHashSet(Iterator<?> i) {
        this(IntIterators.asIntIterator(i));
    }

    public IntLinkedOpenHashSet(int[] a, int offset, int length, float f) {
        this(length < 0 ? 0 : length, f);
        IntArrays.ensureOffsetLength(a, offset, length);
        for (int i = 0; i < length; i++) {
            add(a[offset + i]);
        }
    }

    public IntLinkedOpenHashSet(int[] a, int offset, int length) {
        this(a, offset, length, 0.75f);
    }

    public IntLinkedOpenHashSet(int[] a, float f) {
        this(a, 0, a.length, f);
    }

    public IntLinkedOpenHashSet(int[] a) {
        this(a, 0.75f);
    }

    public static IntLinkedOpenHashSet of() {
        return new IntLinkedOpenHashSet();
    }

    public static IntLinkedOpenHashSet of(int e) {
        IntLinkedOpenHashSet result = new IntLinkedOpenHashSet(1, 0.75f);
        result.add(e);
        return result;
    }

    public static IntLinkedOpenHashSet of(int e0, int e1) {
        IntLinkedOpenHashSet result = new IntLinkedOpenHashSet(2, 0.75f);
        result.add(e0);
        if (!result.add(e1)) {
            throw new IllegalArgumentException("Duplicate element: " + e1);
        }
        return result;
    }

    public static IntLinkedOpenHashSet of(int e0, int e1, int e2) {
        IntLinkedOpenHashSet result = new IntLinkedOpenHashSet(3, 0.75f);
        result.add(e0);
        if (!result.add(e1)) {
            throw new IllegalArgumentException("Duplicate element: " + e1);
        }
        if (!result.add(e2)) {
            throw new IllegalArgumentException("Duplicate element: " + e2);
        }
        return result;
    }

    public static IntLinkedOpenHashSet of(int... a) {
        IntLinkedOpenHashSet result = new IntLinkedOpenHashSet(a.length, 0.75f);
        for (int element : a) {
            if (!result.add(element)) {
                throw new IllegalArgumentException("Duplicate element " + element);
            }
        }
        return result;
    }

    public static IntLinkedOpenHashSet toSet(IntStream stream) {
        return (IntLinkedOpenHashSet) stream.collect(new Supplier() { // from class: it.unimi.dsi.fastutil.ints.IntLinkedOpenHashSet$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final Object get() {
                return new IntLinkedOpenHashSet();
            }
        }, new IntLinkedOpenHashSet$$ExternalSyntheticLambda1(), new IntLinkedOpenHashSet$$ExternalSyntheticLambda2());
    }

    public static IntLinkedOpenHashSet toSetWithExpectedSize(IntStream stream, int expectedSize) {
        if (expectedSize <= 16) {
            return toSet(stream);
        }
        return (IntLinkedOpenHashSet) stream.collect(new IntCollections.SizeDecreasingSupplier(expectedSize, new IntFunction() { // from class: it.unimi.dsi.fastutil.ints.IntLinkedOpenHashSet$$ExternalSyntheticLambda3
            @Override // java.util.function.IntFunction
            public final Object apply(int i) {
                return IntLinkedOpenHashSet.lambda$toSetWithExpectedSize$0(i);
            }
        }), new IntLinkedOpenHashSet$$ExternalSyntheticLambda1(), new IntLinkedOpenHashSet$$ExternalSyntheticLambda2());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ IntLinkedOpenHashSet lambda$toSetWithExpectedSize$0(int size) {
        return size <= 16 ? new IntLinkedOpenHashSet() : new IntLinkedOpenHashSet(size);
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
        if (k == 0) {
            if (this.containsNull) {
                return false;
            }
            pos = this.n;
            this.containsNull = true;
        } else {
            int[] key = this.key;
            int mix = HashCommon.mix(k) & this.mask;
            int pos2 = mix;
            int curr2 = key[mix];
            if (curr2 == 0) {
                pos = pos2;
            } else {
                if (curr2 == k) {
                    return false;
                }
                do {
                    int i = (pos2 + 1) & this.mask;
                    pos2 = i;
                    curr = key[i];
                    if (curr == 0) {
                        pos = pos2;
                    }
                } while (curr != k);
                return false;
            }
            key[pos] = k;
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
                int slot = HashCommon.mix(curr) & this.mask;
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
        if (k == 0) {
            if (this.containsNull) {
                return removeNullEntry();
            }
            return false;
        }
        int[] key = this.key;
        int mix = HashCommon.mix(k) & this.mask;
        int pos = mix;
        int curr2 = key[mix];
        if (curr2 == 0) {
            return false;
        }
        if (k == curr2) {
            return removeEntry(pos);
        }
        do {
            int i = (pos + 1) & this.mask;
            pos = i;
            curr = key[i];
            if (curr == 0) {
                return false;
            }
        } while (k != curr);
        return removeEntry(pos);
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
    public boolean contains(int k) {
        int curr;
        if (k == 0) {
            return this.containsNull;
        }
        int[] key = this.key;
        int mix = HashCommon.mix(k) & this.mask;
        int pos = mix;
        int curr2 = key[mix];
        if (curr2 == 0) {
            return false;
        }
        if (k == curr2) {
            return true;
        }
        do {
            int i = (pos + 1) & this.mask;
            pos = i;
            curr = key[i];
            if (curr == 0) {
                return false;
            }
        } while (k != curr);
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
        if (k == 0) {
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
        if (k == 0) {
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
        if (k == 0) {
            if (this.containsNull) {
                moveIndexToFirst(this.n);
                return false;
            }
            this.containsNull = true;
            pos = this.n;
        } else {
            int[] key = this.key;
            int pos2 = HashCommon.mix(k) & this.mask;
            while (key[pos2] != 0) {
                if (k == key[pos2]) {
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
        if (k == 0) {
            if (this.containsNull) {
                moveIndexToLast(this.n);
                return false;
            }
            this.containsNull = true;
            pos = this.n;
        } else {
            int[] key = this.key;
            int pos2 = HashCommon.mix(k) & this.mask;
            while (key[pos2] != 0) {
                if (k == key[pos2]) {
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
            this.next = IntLinkedOpenHashSet.this.first;
            this.index = 0;
        }

        SetIterator(int from) {
            this.prev = -1;
            this.next = -1;
            this.curr = -1;
            this.index = -1;
            if (from == 0) {
                if (IntLinkedOpenHashSet.this.containsNull) {
                    this.next = (int) IntLinkedOpenHashSet.this.link[IntLinkedOpenHashSet.this.n];
                    this.prev = IntLinkedOpenHashSet.this.n;
                    return;
                }
                throw new NoSuchElementException("The key " + from + " does not belong to this set.");
            }
            if (IntLinkedOpenHashSet.this.key[IntLinkedOpenHashSet.this.last] == from) {
                this.prev = IntLinkedOpenHashSet.this.last;
                this.index = IntLinkedOpenHashSet.this.size;
                return;
            }
            int[] key = IntLinkedOpenHashSet.this.key;
            int pos = HashCommon.mix(from) & IntLinkedOpenHashSet.this.mask;
            while (key[pos] != 0) {
                if (key[pos] == from) {
                    this.next = (int) IntLinkedOpenHashSet.this.link[pos];
                    this.prev = pos;
                    return;
                }
                pos = (pos + 1) & IntLinkedOpenHashSet.this.mask;
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
            this.next = (int) IntLinkedOpenHashSet.this.link[this.curr];
            this.prev = this.curr;
            if (this.index >= 0) {
                this.index++;
            }
            return IntLinkedOpenHashSet.this.key[this.curr];
        }

        @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator
        public int previousInt() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            this.curr = this.prev;
            this.prev = (int) (IntLinkedOpenHashSet.this.link[this.curr] >>> 32);
            this.next = this.curr;
            if (this.index >= 0) {
                this.index--;
            }
            return IntLinkedOpenHashSet.this.key[this.curr];
        }

        @Override // java.util.PrimitiveIterator
        public void forEachRemaining(java.util.function.IntConsumer action) {
            int[] key = IntLinkedOpenHashSet.this.key;
            long[] link = IntLinkedOpenHashSet.this.link;
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
                this.index = IntLinkedOpenHashSet.this.size;
                return;
            }
            int pos = IntLinkedOpenHashSet.this.first;
            this.index = 1;
            while (pos != this.prev) {
                pos = (int) IntLinkedOpenHashSet.this.link[pos];
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
                this.prev = (int) (IntLinkedOpenHashSet.this.link[this.curr] >>> 32);
            } else {
                this.next = (int) IntLinkedOpenHashSet.this.link[this.curr];
            }
            IntLinkedOpenHashSet intLinkedOpenHashSet = IntLinkedOpenHashSet.this;
            intLinkedOpenHashSet.size--;
            if (this.prev == -1) {
                IntLinkedOpenHashSet.this.first = this.next;
            } else {
                long[] jArr = IntLinkedOpenHashSet.this.link;
                int i = this.prev;
                jArr[i] = jArr[i] ^ ((IntLinkedOpenHashSet.this.link[this.prev] ^ (this.next & 4294967295L)) & 4294967295L);
            }
            if (this.next == -1) {
                IntLinkedOpenHashSet.this.last = this.prev;
            } else {
                long[] jArr2 = IntLinkedOpenHashSet.this.link;
                int i2 = this.next;
                jArr2[i2] = ((((4294967295L & this.prev) << 32) ^ IntLinkedOpenHashSet.this.link[this.next]) & (-4294967296L)) ^ jArr2[i2];
            }
            int pos = this.curr;
            this.curr = -1;
            if (pos == IntLinkedOpenHashSet.this.n) {
                IntLinkedOpenHashSet.this.containsNull = false;
                IntLinkedOpenHashSet.this.key[IntLinkedOpenHashSet.this.n] = 0;
                return;
            }
            int[] key = IntLinkedOpenHashSet.this.key;
            while (true) {
                int last = pos;
                pos = (pos + 1) & IntLinkedOpenHashSet.this.mask;
                while (true) {
                    curr = key[pos];
                    if (curr == 0) {
                        key[last] = 0;
                        return;
                    }
                    int slot = HashCommon.mix(curr) & IntLinkedOpenHashSet.this.mask;
                    if (last > pos) {
                        if (last >= slot && slot > pos) {
                            break;
                        }
                        pos = (pos + 1) & IntLinkedOpenHashSet.this.mask;
                    } else if (last >= slot || slot > pos) {
                        break;
                    } else {
                        pos = (pos + 1) & IntLinkedOpenHashSet.this.mask;
                    }
                }
                key[last] = curr;
                if (this.next == pos) {
                    this.next = last;
                }
                if (this.prev == pos) {
                    this.prev = last;
                }
                IntLinkedOpenHashSet.this.fixPointers(pos, last);
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
            if (key2[i] == 0) {
                pos = newN;
            } else {
                int pos3 = key2[i];
                pos = HashCommon.mix(pos3) & mask2;
                while (newKey[pos] != 0) {
                    pos = (pos + 1) & mask2;
                }
            }
            newKey[pos] = key2[i];
            if (prev != i2) {
                key = key2;
                mask = mask2;
                newLink[newPrev] = ((newLink[newPrev] ^ (pos & 4294967295L)) & 4294967295L) ^ newLink[newPrev];
                int pos4 = pos;
                newLink[pos4] = newLink[pos] ^ ((((newPrev & 4294967295L) << 32) ^ newLink[pos]) & (-4294967296L));
                newPrev = pos4;
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
    public IntLinkedOpenHashSet m232clone() {
        try {
            IntLinkedOpenHashSet c = (IntLinkedOpenHashSet) super.clone();
            c.key = (int[]) this.key.clone();
            c.containsNull = this.containsNull;
            c.link = (long[]) this.link.clone();
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
                h += this.key[i];
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
            if (k != 0) {
                int mix = HashCommon.mix(k) & this.mask;
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
