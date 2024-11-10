package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Size64;
import it.unimi.dsi.fastutil.objects.ObjectCollections;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.SortedSet;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Collector;

/* loaded from: classes4.dex */
public class ObjectLinkedOpenHashSet<K> extends AbstractObjectSortedSet<K> implements Serializable, Cloneable, Hash {
    private static final boolean ASSERTS = false;
    private static final int SPLITERATOR_CHARACTERISTICS = 81;
    private static final Collector<Object, ?, ObjectLinkedOpenHashSet<Object>> TO_SET_COLLECTOR = Collector.of(new Supplier() { // from class: it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ObjectLinkedOpenHashSet();
        }
    }, new ObjectLinkedOpenHashSet$$ExternalSyntheticLambda1(), new ObjectLinkedOpenHashSet$$ExternalSyntheticLambda2(), new Collector.Characteristics[0]);
    private static final long serialVersionUID = 0;
    protected transient boolean containsNull;
    protected final float f;
    protected transient int first;
    protected transient K[] key;
    protected transient int last;
    protected transient long[] link;
    protected transient int mask;
    protected transient int maxFill;
    protected final transient int minN;
    protected transient int n;
    protected int size;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
    public /* bridge */ /* synthetic */ SortedSet headSet(Object obj) {
        return headSet((ObjectLinkedOpenHashSet<K>) obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet
    public /* bridge */ /* synthetic */ ObjectBidirectionalIterator iterator(Object obj) {
        return iterator((ObjectLinkedOpenHashSet<K>) obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
    public /* bridge */ /* synthetic */ SortedSet tailSet(Object obj) {
        return tailSet((ObjectLinkedOpenHashSet<K>) obj);
    }

    public ObjectLinkedOpenHashSet(int i, float f) {
        this.first = -1;
        this.last = -1;
        if (f <= 0.0f || f >= 1.0f) {
            throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than 1");
        }
        if (i < 0) {
            throw new IllegalArgumentException("The expected number of elements must be nonnegative");
        }
        this.f = f;
        int arraySize = HashCommon.arraySize(i, f);
        this.n = arraySize;
        this.minN = arraySize;
        this.mask = this.n - 1;
        this.maxFill = HashCommon.maxFill(this.n, f);
        this.key = (K[]) new Object[this.n + 1];
        this.link = new long[this.n + 1];
    }

    public ObjectLinkedOpenHashSet(int expected) {
        this(expected, 0.75f);
    }

    public ObjectLinkedOpenHashSet() {
        this(16, 0.75f);
    }

    public ObjectLinkedOpenHashSet(Collection<? extends K> c, float f) {
        this(c.size(), f);
        addAll(c);
    }

    public ObjectLinkedOpenHashSet(Collection<? extends K> c) {
        this(c, 0.75f);
    }

    public ObjectLinkedOpenHashSet(ObjectCollection<? extends K> c, float f) {
        this(c.size(), f);
        addAll(c);
    }

    public ObjectLinkedOpenHashSet(ObjectCollection<? extends K> c) {
        this((ObjectCollection) c, 0.75f);
    }

    public ObjectLinkedOpenHashSet(Iterator<? extends K> i, float f) {
        this(16, f);
        while (i.hasNext()) {
            add(i.next());
        }
    }

    public ObjectLinkedOpenHashSet(Iterator<? extends K> i) {
        this(i, 0.75f);
    }

    public ObjectLinkedOpenHashSet(K[] a, int offset, int length, float f) {
        this(length < 0 ? 0 : length, f);
        ObjectArrays.ensureOffsetLength(a, offset, length);
        for (int i = 0; i < length; i++) {
            add(a[offset + i]);
        }
    }

    public ObjectLinkedOpenHashSet(K[] a, int offset, int length) {
        this(a, offset, length, 0.75f);
    }

    public ObjectLinkedOpenHashSet(K[] a, float f) {
        this(a, 0, a.length, f);
    }

    public ObjectLinkedOpenHashSet(K[] a) {
        this(a, 0.75f);
    }

    public static <K> ObjectLinkedOpenHashSet<K> of() {
        return new ObjectLinkedOpenHashSet<>();
    }

    public static <K> ObjectLinkedOpenHashSet<K> of(K e) {
        ObjectLinkedOpenHashSet<K> result = new ObjectLinkedOpenHashSet<>(1, 0.75f);
        result.add(e);
        return result;
    }

    public static <K> ObjectLinkedOpenHashSet<K> of(K e0, K e1) {
        ObjectLinkedOpenHashSet<K> result = new ObjectLinkedOpenHashSet<>(2, 0.75f);
        result.add(e0);
        if (!result.add(e1)) {
            throw new IllegalArgumentException("Duplicate element: " + e1);
        }
        return result;
    }

    public static <K> ObjectLinkedOpenHashSet<K> of(K e0, K e1, K e2) {
        ObjectLinkedOpenHashSet<K> result = new ObjectLinkedOpenHashSet<>(3, 0.75f);
        result.add(e0);
        if (!result.add(e1)) {
            throw new IllegalArgumentException("Duplicate element: " + e1);
        }
        if (!result.add(e2)) {
            throw new IllegalArgumentException("Duplicate element: " + e2);
        }
        return result;
    }

    @SafeVarargs
    public static <K> ObjectLinkedOpenHashSet<K> of(K... a) {
        ObjectLinkedOpenHashSet<K> result = new ObjectLinkedOpenHashSet<>(a.length, 0.75f);
        for (K element : a) {
            if (!result.add(element)) {
                throw new IllegalArgumentException("Duplicate element " + element);
            }
        }
        return result;
    }

    public ObjectLinkedOpenHashSet<K> combine(ObjectLinkedOpenHashSet<? extends K> toAddFrom) {
        addAll(toAddFrom);
        return this;
    }

    public static <K> Collector<K, ?, ObjectLinkedOpenHashSet<K>> toSet() {
        return (Collector<K, ?, ObjectLinkedOpenHashSet<K>>) TO_SET_COLLECTOR;
    }

    public static <K> Collector<K, ?, ObjectLinkedOpenHashSet<K>> toSetWithExpectedSize(int expectedSize) {
        if (expectedSize <= 16) {
            return toSet();
        }
        return Collector.of(new ObjectCollections.SizeDecreasingSupplier(expectedSize, new IntFunction() { // from class: it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet$$ExternalSyntheticLambda3
            @Override // java.util.function.IntFunction
            public final Object apply(int i) {
                return ObjectLinkedOpenHashSet.lambda$toSetWithExpectedSize$0(i);
            }
        }), new ObjectLinkedOpenHashSet$$ExternalSyntheticLambda1(), new ObjectLinkedOpenHashSet$$ExternalSyntheticLambda2(), new Collector.Characteristics[0]);
    }

    public static /* synthetic */ ObjectLinkedOpenHashSet lambda$toSetWithExpectedSize$0(int size) {
        return size <= 16 ? new ObjectLinkedOpenHashSet() : new ObjectLinkedOpenHashSet(size);
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

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean addAll(Collection<? extends K> c) {
        if (this.f <= 0.5d) {
            ensureCapacity(c.size());
        } else {
            tryCapacity(size() + c.size());
        }
        return super.addAll(c);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean add(K k) {
        int pos;
        K curr;
        if (k == null) {
            if (this.containsNull) {
                return false;
            }
            pos = this.n;
            this.containsNull = true;
        } else {
            K[] key = this.key;
            int mix = HashCommon.mix(k.hashCode()) & this.mask;
            int pos2 = mix;
            K curr2 = key[mix];
            if (curr2 == null) {
                pos = pos2;
            } else {
                if (curr2.equals(k)) {
                    return false;
                }
                do {
                    int i = (pos2 + 1) & this.mask;
                    pos2 = i;
                    curr = key[i];
                    if (curr == null) {
                        pos = pos2;
                    }
                } while (!curr.equals(k));
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

    public K addOrGet(K k) {
        int pos;
        K curr;
        if (k == null) {
            if (this.containsNull) {
                return this.key[this.n];
            }
            pos = this.n;
            this.containsNull = true;
        } else {
            K[] key = this.key;
            int mix = HashCommon.mix(k.hashCode()) & this.mask;
            int pos2 = mix;
            K curr2 = key[mix];
            if (curr2 != null) {
                if (curr2.equals(k)) {
                    return curr2;
                }
                do {
                    int i = (pos2 + 1) & this.mask;
                    pos2 = i;
                    curr = key[i];
                    if (curr != null) {
                    }
                } while (!curr.equals(k));
                return curr;
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
        return k;
    }

    protected final void shiftKeys(int pos) {
        K curr;
        K[] key = this.key;
        while (true) {
            int last = pos;
            pos = (pos + 1) & this.mask;
            while (true) {
                curr = key[pos];
                if (curr == null) {
                    key[last] = null;
                    return;
                }
                int slot = HashCommon.mix(curr.hashCode()) & this.mask;
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
        this.key[this.n] = null;
        this.size--;
        fixPointers(this.n);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            rehash(this.n / 2);
        }
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object k) {
        K curr;
        if (k == null) {
            if (this.containsNull) {
                return removeNullEntry();
            }
            return false;
        }
        K[] key = this.key;
        int mix = HashCommon.mix(k.hashCode()) & this.mask;
        int pos = mix;
        K curr2 = key[mix];
        if (curr2 == null) {
            return false;
        }
        if (k.equals(curr2)) {
            return removeEntry(pos);
        }
        do {
            int i = (pos + 1) & this.mask;
            pos = i;
            curr = key[i];
            if (curr == null) {
                return false;
            }
        } while (!k.equals(curr));
        return removeEntry(pos);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object k) {
        K curr;
        if (k == null) {
            return this.containsNull;
        }
        K[] key = this.key;
        int mix = HashCommon.mix(k.hashCode()) & this.mask;
        int pos = mix;
        K curr2 = key[mix];
        if (curr2 == null) {
            return false;
        }
        if (k.equals(curr2)) {
            return true;
        }
        do {
            int i = (pos + 1) & this.mask;
            pos = i;
            curr = key[i];
            if (curr == null) {
                return false;
            }
        } while (!k.equals(curr));
        return true;
    }

    public K get(Object k) {
        K curr;
        if (k == null) {
            return this.key[this.n];
        }
        K[] key = this.key;
        int mix = HashCommon.mix(k.hashCode()) & this.mask;
        int pos = mix;
        K curr2 = key[mix];
        if (curr2 == null) {
            return null;
        }
        if (k.equals(curr2)) {
            return curr2;
        }
        do {
            int i = (pos + 1) & this.mask;
            pos = i;
            curr = key[i];
            if (curr == null) {
                return null;
            }
        } while (!k.equals(curr));
        return curr;
    }

    public K removeFirst() {
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
        K k = this.key[pos];
        this.size--;
        if (k == null) {
            this.containsNull = false;
            this.key[this.n] = null;
        } else {
            shiftKeys(pos);
        }
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            rehash(this.n / 2);
        }
        return k;
    }

    public K removeLast() {
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
        K k = this.key[pos];
        this.size--;
        if (k == null) {
            this.containsNull = false;
            this.key[this.n] = null;
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

    public boolean addAndMoveToFirst(K k) {
        int pos;
        if (k == null) {
            if (this.containsNull) {
                moveIndexToFirst(this.n);
                return false;
            }
            this.containsNull = true;
            pos = this.n;
        } else {
            K[] key = this.key;
            int pos2 = HashCommon.mix(k.hashCode()) & this.mask;
            while (key[pos2] != null) {
                if (k.equals(key[pos2])) {
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

    public boolean addAndMoveToLast(K k) {
        int pos;
        if (k == null) {
            if (this.containsNull) {
                moveIndexToLast(this.n);
                return false;
            }
            this.containsNull = true;
            pos = this.n;
        } else {
            K[] key = this.key;
            int pos2 = HashCommon.mix(k.hashCode()) & this.mask;
            while (key[pos2] != null) {
                if (k.equals(key[pos2])) {
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
        Arrays.fill(this.key, (Object) null);
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

    @Override // java.util.SortedSet
    public K first() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.first];
    }

    @Override // java.util.SortedSet
    public K last() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.last];
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
    public ObjectSortedSet<K> tailSet(K from) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
    public ObjectSortedSet<K> headSet(K to) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
    public ObjectSortedSet<K> subSet(K from, K to) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.SortedSet
    public Comparator<? super K> comparator() {
        return null;
    }

    /* loaded from: classes4.dex */
    public final class SetIterator implements ObjectListIterator<K> {
        int curr;
        int index;
        int next;
        int prev;

        SetIterator() {
            this.prev = -1;
            this.next = -1;
            this.curr = -1;
            this.index = -1;
            this.next = ObjectLinkedOpenHashSet.this.first;
            this.index = 0;
        }

        SetIterator(K from) {
            this.prev = -1;
            this.next = -1;
            this.curr = -1;
            this.index = -1;
            if (from == null) {
                if (ObjectLinkedOpenHashSet.this.containsNull) {
                    this.next = (int) ObjectLinkedOpenHashSet.this.link[ObjectLinkedOpenHashSet.this.n];
                    this.prev = ObjectLinkedOpenHashSet.this.n;
                    return;
                }
                throw new NoSuchElementException("The key " + from + " does not belong to this set.");
            }
            if (Objects.equals(ObjectLinkedOpenHashSet.this.key[ObjectLinkedOpenHashSet.this.last], from)) {
                this.prev = ObjectLinkedOpenHashSet.this.last;
                this.index = ObjectLinkedOpenHashSet.this.size;
                return;
            }
            K[] key = ObjectLinkedOpenHashSet.this.key;
            int pos = HashCommon.mix(from.hashCode()) & ObjectLinkedOpenHashSet.this.mask;
            while (key[pos] != null) {
                if (key[pos].equals(from)) {
                    this.next = (int) ObjectLinkedOpenHashSet.this.link[pos];
                    this.prev = pos;
                    return;
                }
                pos = (pos + 1) & ObjectLinkedOpenHashSet.this.mask;
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

        @Override // java.util.Iterator, java.util.ListIterator
        public K next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = this.next;
            this.next = (int) ObjectLinkedOpenHashSet.this.link[this.curr];
            this.prev = this.curr;
            if (this.index >= 0) {
                this.index++;
            }
            return ObjectLinkedOpenHashSet.this.key[this.curr];
        }

        @Override // it.unimi.dsi.fastutil.BidirectionalIterator, java.util.ListIterator
        public K previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            this.curr = this.prev;
            this.prev = (int) (ObjectLinkedOpenHashSet.this.link[this.curr] >>> 32);
            this.next = this.curr;
            if (this.index >= 0) {
                this.index--;
            }
            return ObjectLinkedOpenHashSet.this.key[this.curr];
        }

        @Override // java.util.Iterator
        public void forEachRemaining(Consumer<? super K> consumer) {
            K[] kArr = ObjectLinkedOpenHashSet.this.key;
            long[] jArr = ObjectLinkedOpenHashSet.this.link;
            while (this.next != -1) {
                this.curr = this.next;
                this.next = (int) jArr[this.curr];
                this.prev = this.curr;
                if (this.index >= 0) {
                    this.index++;
                }
                consumer.accept(kArr[this.curr]);
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
                this.index = ObjectLinkedOpenHashSet.this.size;
                return;
            }
            int pos = ObjectLinkedOpenHashSet.this.first;
            this.index = 1;
            while (pos != this.prev) {
                pos = (int) ObjectLinkedOpenHashSet.this.link[pos];
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

        @Override // it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.Iterator, java.util.ListIterator
        public void remove() {
            K curr;
            ensureIndexKnown();
            if (this.curr == -1) {
                throw new IllegalStateException();
            }
            if (this.curr == this.prev) {
                this.index--;
                this.prev = (int) (ObjectLinkedOpenHashSet.this.link[this.curr] >>> 32);
            } else {
                this.next = (int) ObjectLinkedOpenHashSet.this.link[this.curr];
            }
            ObjectLinkedOpenHashSet objectLinkedOpenHashSet = ObjectLinkedOpenHashSet.this;
            objectLinkedOpenHashSet.size--;
            if (this.prev == -1) {
                ObjectLinkedOpenHashSet.this.first = this.next;
            } else {
                long[] jArr = ObjectLinkedOpenHashSet.this.link;
                int i = this.prev;
                jArr[i] = jArr[i] ^ ((ObjectLinkedOpenHashSet.this.link[this.prev] ^ (this.next & 4294967295L)) & 4294967295L);
            }
            if (this.next == -1) {
                ObjectLinkedOpenHashSet.this.last = this.prev;
            } else {
                long[] jArr2 = ObjectLinkedOpenHashSet.this.link;
                int i2 = this.next;
                jArr2[i2] = ((((4294967295L & this.prev) << 32) ^ ObjectLinkedOpenHashSet.this.link[this.next]) & (-4294967296L)) ^ jArr2[i2];
            }
            int pos = this.curr;
            this.curr = -1;
            if (pos == ObjectLinkedOpenHashSet.this.n) {
                ObjectLinkedOpenHashSet.this.containsNull = false;
                ObjectLinkedOpenHashSet.this.key[ObjectLinkedOpenHashSet.this.n] = null;
                return;
            }
            K[] key = ObjectLinkedOpenHashSet.this.key;
            while (true) {
                int last = pos;
                pos = (pos + 1) & ObjectLinkedOpenHashSet.this.mask;
                while (true) {
                    curr = key[pos];
                    if (curr == null) {
                        key[last] = null;
                        return;
                    }
                    int slot = HashCommon.mix(curr.hashCode()) & ObjectLinkedOpenHashSet.this.mask;
                    if (last > pos) {
                        if (last >= slot && slot > pos) {
                            break;
                        }
                        pos = (pos + 1) & ObjectLinkedOpenHashSet.this.mask;
                    } else if (last >= slot || slot > pos) {
                        break;
                    } else {
                        pos = (pos + 1) & ObjectLinkedOpenHashSet.this.mask;
                    }
                }
                key[last] = curr;
                if (this.next == pos) {
                    this.next = last;
                }
                if (this.prev == pos) {
                    this.prev = last;
                }
                ObjectLinkedOpenHashSet.this.fixPointers(pos, last);
            }
        }
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet
    public ObjectListIterator<K> iterator(K from) {
        return new SetIterator(from);
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet, it.unimi.dsi.fastutil.objects.AbstractObjectSet, it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
    public ObjectListIterator<K> iterator() {
        return new SetIterator();
    }

    @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
    /* renamed from: spliterator */
    public ObjectSpliterator<K> mo221spliterator() {
        return ObjectSpliterators.asSpliterator(iterator(), Size64.sizeOf(this), 81);
    }

    @Override // java.lang.Iterable
    public void forEach(Consumer<? super K> consumer) {
        int i = this.first;
        while (i != -1) {
            int i2 = i;
            i = (int) this.link[i2];
            consumer.accept(this.key[i2]);
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

    protected void rehash(int i) {
        int mix;
        K[] kArr;
        int i2;
        K[] kArr2 = this.key;
        int i3 = i - 1;
        K[] kArr3 = (K[]) new Object[i + 1];
        int i4 = this.first;
        int i5 = -1;
        int i6 = -1;
        long[] jArr = this.link;
        long[] jArr2 = new long[i + 1];
        int i7 = -1;
        this.first = -1;
        int i8 = this.size;
        while (true) {
            int i9 = i8 - 1;
            if (i8 == 0) {
                break;
            }
            if (kArr2[i4] == null) {
                mix = i;
            } else {
                mix = HashCommon.mix(kArr2[i4].hashCode()) & i3;
                while (kArr3[mix] != null) {
                    mix = (mix + 1) & i3;
                }
            }
            kArr3[mix] = kArr2[i4];
            if (i5 != i7) {
                kArr = kArr2;
                i2 = i3;
                jArr2[i6] = ((jArr2[i6] ^ (mix & 4294967295L)) & 4294967295L) ^ jArr2[i6];
                int i10 = mix;
                jArr2[i10] = jArr2[mix] ^ ((((i6 & 4294967295L) << 32) ^ jArr2[mix]) & (-4294967296L));
                i6 = i10;
            } else {
                kArr = kArr2;
                i2 = i3;
                this.first = mix;
                jArr2[mix] = -1;
                i6 = mix;
            }
            int i11 = i4;
            i4 = (int) jArr[i4];
            i5 = i11;
            i8 = i9;
            kArr2 = kArr;
            i3 = i2;
            i7 = -1;
        }
        int i12 = i3;
        this.link = jArr2;
        this.last = i6;
        if (i6 != -1) {
            jArr2[i6] = jArr2[i6] | 4294967295L;
        }
        this.n = i;
        this.mask = i12;
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.key = kArr3;
    }

    /* renamed from: clone */
    public ObjectLinkedOpenHashSet<K> m272clone() {
        try {
            ObjectLinkedOpenHashSet<K> objectLinkedOpenHashSet = (ObjectLinkedOpenHashSet) super.clone();
            objectLinkedOpenHashSet.key = (K[]) ((Object[]) this.key.clone());
            objectLinkedOpenHashSet.containsNull = this.containsNull;
            objectLinkedOpenHashSet.link = (long[]) this.link.clone();
            return objectLinkedOpenHashSet;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectSet, java.util.Collection, java.util.Set
    public int hashCode() {
        int h = 0;
        int j = realSize();
        int i = 0;
        while (true) {
            int j2 = j - 1;
            if (j != 0) {
                while (this.key[i] == null) {
                    i++;
                }
                if (this != this.key[i]) {
                    h += this.key[i].hashCode();
                }
                i++;
                j = j2;
            } else {
                return h;
            }
        }
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        ObjectIterator<K> i = iterator();
        s.defaultWriteObject();
        int j = this.size;
        while (true) {
            int j2 = j - 1;
            if (j == 0) {
                return;
            }
            s.writeObject(i.next());
            j = j2;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        int i;
        int i2;
        objectInputStream.defaultReadObject();
        this.n = HashCommon.arraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        boolean z = true;
        this.mask = this.n - 1;
        K[] kArr = (K[]) new Object[this.n + 1];
        this.key = kArr;
        long[] jArr = new long[this.n + 1];
        this.link = jArr;
        int i3 = -1;
        int i4 = -1;
        this.last = -1;
        this.first = -1;
        int i5 = this.size;
        while (true) {
            int i6 = i5 - 1;
            if (i5 == 0) {
                break;
            }
            Object readObject = objectInputStream.readObject();
            if (readObject != null) {
                int mix = HashCommon.mix(readObject.hashCode()) & this.mask;
                int i7 = mix;
                if (kArr[mix] == 0) {
                    i = i7;
                }
                do {
                    i2 = (i7 + 1) & this.mask;
                    i7 = i2;
                } while (kArr[i2] != 0);
                i = i7;
            } else {
                i = this.n;
                this.containsNull = z;
            }
            kArr[i] = readObject;
            if (this.first != i4) {
                jArr[i3] = ((jArr[i3] ^ (i & 4294967295L)) & 4294967295L) ^ jArr[i3];
                long[] jArr2 = jArr;
                jArr2[i] = ((((i3 & 4294967295L) << 32) ^ jArr[i]) & (-4294967296L)) ^ jArr[i];
                i3 = i;
                i5 = i6;
                jArr = jArr2;
                z = true;
                i4 = -1;
            } else {
                long[] jArr3 = jArr;
                this.first = i;
                i3 = i;
                jArr3[i] = jArr3[i] | (-4294967296L);
                i5 = i6;
                jArr = jArr3;
                z = true;
                i4 = -1;
            }
        }
        long[] jArr4 = jArr;
        this.last = i3;
        if (i3 != -1) {
            jArr4[i3] = jArr4[i3] | 4294967295L;
        }
    }

    private void checkTable() {
    }
}
