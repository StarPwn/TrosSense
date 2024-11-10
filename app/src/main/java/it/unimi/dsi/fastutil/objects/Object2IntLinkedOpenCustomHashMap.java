package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Size64;
import it.unimi.dsi.fastutil.ints.AbstractIntCollection;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import it.unimi.dsi.fastutil.ints.IntSpliterator;
import it.unimi.dsi.fastutil.ints.IntSpliterators;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntSortedMap;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.ToIntFunction;

/* loaded from: classes4.dex */
public class Object2IntLinkedOpenCustomHashMap<K> extends AbstractObject2IntSortedMap<K> implements Serializable, Cloneable, Hash {
    private static final boolean ASSERTS = false;
    private static final long serialVersionUID = 0;
    protected transient boolean containsNullKey;
    protected transient Object2IntSortedMap.FastSortedEntrySet<K> entries;
    protected final float f;
    protected transient int first;
    protected transient K[] key;
    protected transient ObjectSortedSet<K> keys;
    protected transient int last;
    protected transient long[] link;
    protected transient int mask;
    protected transient int maxFill;
    protected final transient int minN;
    protected transient int n;
    protected int size;
    protected Hash.Strategy<? super K> strategy;
    protected transient int[] value;
    protected transient IntCollection values;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.objects.Object2IntSortedMap, java.util.SortedMap
    public /* bridge */ /* synthetic */ SortedMap headMap(Object obj) {
        return headMap((Object2IntLinkedOpenCustomHashMap<K>) obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.objects.Object2IntSortedMap, java.util.SortedMap
    public /* bridge */ /* synthetic */ SortedMap tailMap(Object obj) {
        return tailMap((Object2IntLinkedOpenCustomHashMap<K>) obj);
    }

    public Object2IntLinkedOpenCustomHashMap(int i, float f, Hash.Strategy<? super K> strategy) {
        this.first = -1;
        this.last = -1;
        this.strategy = strategy;
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
        this.value = new int[this.n + 1];
        this.link = new long[this.n + 1];
    }

    public Object2IntLinkedOpenCustomHashMap(int expected, Hash.Strategy<? super K> strategy) {
        this(expected, 0.75f, strategy);
    }

    public Object2IntLinkedOpenCustomHashMap(Hash.Strategy<? super K> strategy) {
        this(16, 0.75f, strategy);
    }

    public Object2IntLinkedOpenCustomHashMap(Map<? extends K, ? extends Integer> m, float f, Hash.Strategy<? super K> strategy) {
        this(m.size(), f, strategy);
        putAll(m);
    }

    public Object2IntLinkedOpenCustomHashMap(Map<? extends K, ? extends Integer> m, Hash.Strategy<? super K> strategy) {
        this(m, 0.75f, strategy);
    }

    public Object2IntLinkedOpenCustomHashMap(Object2IntMap<K> m, float f, Hash.Strategy<? super K> strategy) {
        this(m.size(), f, strategy);
        putAll(m);
    }

    public Object2IntLinkedOpenCustomHashMap(Object2IntMap<K> m, Hash.Strategy<? super K> strategy) {
        this((Object2IntMap) m, 0.75f, (Hash.Strategy) strategy);
    }

    public Object2IntLinkedOpenCustomHashMap(K[] k, int[] v, float f, Hash.Strategy<? super K> strategy) {
        this(k.length, f, strategy);
        if (k.length != v.length) {
            throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
        }
        for (int i = 0; i < k.length; i++) {
            put((Object2IntLinkedOpenCustomHashMap<K>) k[i], v[i]);
        }
    }

    public Object2IntLinkedOpenCustomHashMap(K[] k, int[] v, Hash.Strategy<? super K> strategy) {
        this(k, v, 0.75f, strategy);
    }

    public Hash.Strategy<? super K> strategy() {
        return this.strategy;
    }

    private int realSize() {
        return this.containsNullKey ? this.size - 1 : this.size;
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

    /* JADX INFO: Access modifiers changed from: private */
    public int removeEntry(int pos) {
        int oldValue = this.value[pos];
        this.size--;
        fixPointers(pos);
        shiftKeys(pos);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            rehash(this.n / 2);
        }
        return oldValue;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int removeNullEntry() {
        this.containsNullKey = false;
        this.key[this.n] = null;
        int oldValue = this.value[this.n];
        this.size--;
        fixPointers(this.n);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            rehash(this.n / 2);
        }
        return oldValue;
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObject2IntMap, java.util.Map
    public void putAll(Map<? extends K, ? extends Integer> m) {
        if (this.f <= 0.5d) {
            ensureCapacity(m.size());
        } else {
            tryCapacity(size() + m.size());
        }
        super.putAll(m);
    }

    private int find(K k) {
        K curr;
        if (this.strategy.equals(k, null)) {
            return this.containsNullKey ? this.n : -(this.n + 1);
        }
        K[] key = this.key;
        int mix = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
        int pos = mix;
        K curr2 = key[mix];
        if (curr2 == null) {
            return -(pos + 1);
        }
        if (this.strategy.equals(k, curr2)) {
            return pos;
        }
        do {
            int i = (pos + 1) & this.mask;
            pos = i;
            curr = key[i];
            if (curr == null) {
                return -(pos + 1);
            }
        } while (!this.strategy.equals(k, curr));
        return pos;
    }

    private void insert(int pos, K k, int v) {
        if (pos == this.n) {
            this.containsNullKey = true;
        }
        this.key[pos] = k;
        this.value[pos] = v;
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
            rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
    }

    @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
    public int put(K k, int v) {
        int pos = find(k);
        if (pos < 0) {
            insert((-pos) - 1, k, v);
            return this.defRetValue;
        }
        int oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }

    private int addToValue(int pos, int incr) {
        int oldValue = this.value[pos];
        this.value[pos] = oldValue + incr;
        return oldValue;
    }

    public int addTo(K k, int incr) {
        int pos;
        K curr;
        if (this.strategy.equals(k, null)) {
            if (this.containsNullKey) {
                return addToValue(this.n, incr);
            }
            pos = this.n;
            this.containsNullKey = true;
        } else {
            K[] key = this.key;
            int mix = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
            int pos2 = mix;
            K curr2 = key[mix];
            if (curr2 == null) {
                pos = pos2;
            } else {
                if (this.strategy.equals(curr2, k)) {
                    return addToValue(pos2, incr);
                }
                do {
                    int i = (pos2 + 1) & this.mask;
                    pos2 = i;
                    curr = key[i];
                    if (curr == null) {
                        pos = pos2;
                    }
                } while (!this.strategy.equals(curr, k));
                return addToValue(pos2, incr);
            }
        }
        this.key[pos] = k;
        this.value[pos] = this.defRetValue + incr;
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
        return this.defRetValue;
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
            this.value[last] = this.value[pos];
            fixPointers(pos, last);
        }
    }

    @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
    public int removeInt(Object k) {
        K curr;
        if (this.strategy.equals(k, null)) {
            return this.containsNullKey ? removeNullEntry() : this.defRetValue;
        }
        K[] key = this.key;
        int mix = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
        int pos = mix;
        K curr2 = key[mix];
        if (curr2 == null) {
            return this.defRetValue;
        }
        if (this.strategy.equals(k, curr2)) {
            return removeEntry(pos);
        }
        do {
            int i = (pos + 1) & this.mask;
            pos = i;
            curr = key[i];
            if (curr == null) {
                return this.defRetValue;
            }
        } while (!this.strategy.equals(k, curr));
        return removeEntry(pos);
    }

    private int setValue(int pos, int v) {
        int oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
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
        this.size--;
        int v = this.value[pos];
        if (pos == this.n) {
            this.containsNullKey = false;
            this.key[this.n] = null;
        } else {
            shiftKeys(pos);
        }
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            rehash(this.n / 2);
        }
        return v;
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
        this.size--;
        int v = this.value[pos];
        if (pos == this.n) {
            this.containsNullKey = false;
            this.key[this.n] = null;
        } else {
            shiftKeys(pos);
        }
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            rehash(this.n / 2);
        }
        return v;
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

    public int getAndMoveToFirst(K k) {
        K curr;
        if (this.strategy.equals(k, null)) {
            if (this.containsNullKey) {
                moveIndexToFirst(this.n);
                return this.value[this.n];
            }
            return this.defRetValue;
        }
        K[] key = this.key;
        int mix = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
        int pos = mix;
        K curr2 = key[mix];
        if (curr2 == null) {
            return this.defRetValue;
        }
        if (this.strategy.equals(k, curr2)) {
            moveIndexToFirst(pos);
            return this.value[pos];
        }
        do {
            int i = (pos + 1) & this.mask;
            pos = i;
            curr = key[i];
            if (curr == null) {
                return this.defRetValue;
            }
        } while (!this.strategy.equals(k, curr));
        moveIndexToFirst(pos);
        return this.value[pos];
    }

    public int getAndMoveToLast(K k) {
        K curr;
        if (this.strategy.equals(k, null)) {
            if (this.containsNullKey) {
                moveIndexToLast(this.n);
                return this.value[this.n];
            }
            return this.defRetValue;
        }
        K[] key = this.key;
        int mix = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
        int pos = mix;
        K curr2 = key[mix];
        if (curr2 == null) {
            return this.defRetValue;
        }
        if (this.strategy.equals(k, curr2)) {
            moveIndexToLast(pos);
            return this.value[pos];
        }
        do {
            int i = (pos + 1) & this.mask;
            pos = i;
            curr = key[i];
            if (curr == null) {
                return this.defRetValue;
            }
        } while (!this.strategy.equals(k, curr));
        moveIndexToLast(pos);
        return this.value[pos];
    }

    public int putAndMoveToFirst(K k, int v) {
        int pos;
        K curr;
        if (this.strategy.equals(k, null)) {
            if (this.containsNullKey) {
                moveIndexToFirst(this.n);
                return setValue(this.n, v);
            }
            this.containsNullKey = true;
            pos = this.n;
        } else {
            K[] key = this.key;
            int mix = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
            int pos2 = mix;
            K curr2 = key[mix];
            if (curr2 == null) {
                pos = pos2;
            } else {
                if (this.strategy.equals(curr2, k)) {
                    moveIndexToFirst(pos2);
                    return setValue(pos2, v);
                }
                do {
                    int i = (pos2 + 1) & this.mask;
                    pos2 = i;
                    curr = key[i];
                    if (curr == null) {
                        pos = pos2;
                    }
                } while (!this.strategy.equals(curr, k));
                moveIndexToFirst(pos2);
                return setValue(pos2, v);
            }
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size == 0) {
            this.last = pos;
            this.first = pos;
            this.link[pos] = -1;
        } else {
            long[] jArr = this.link;
            int i2 = this.first;
            jArr[i2] = jArr[i2] ^ ((this.link[this.first] ^ ((pos & 4294967295L) << 32)) & (-4294967296L));
            this.link[pos] = (this.first & 4294967295L) | (-4294967296L);
            this.first = pos;
        }
        int i3 = this.size;
        this.size = i3 + 1;
        if (i3 >= this.maxFill) {
            rehash(HashCommon.arraySize(this.size, this.f));
        }
        return this.defRetValue;
    }

    public int putAndMoveToLast(K k, int v) {
        int pos;
        K curr;
        if (this.strategy.equals(k, null)) {
            if (this.containsNullKey) {
                moveIndexToLast(this.n);
                return setValue(this.n, v);
            }
            this.containsNullKey = true;
            pos = this.n;
        } else {
            K[] key = this.key;
            int mix = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
            int pos2 = mix;
            K curr2 = key[mix];
            if (curr2 == null) {
                pos = pos2;
            } else {
                if (this.strategy.equals(curr2, k)) {
                    moveIndexToLast(pos2);
                    return setValue(pos2, v);
                }
                do {
                    int i = (pos2 + 1) & this.mask;
                    pos2 = i;
                    curr = key[i];
                    if (curr == null) {
                        pos = pos2;
                    }
                } while (!this.strategy.equals(curr, k));
                moveIndexToLast(pos2);
                return setValue(pos2, v);
            }
        }
        this.key[pos] = k;
        this.value[pos] = v;
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
            rehash(HashCommon.arraySize(this.size, this.f));
        }
        return this.defRetValue;
    }

    @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
    public int getInt(Object k) {
        K curr;
        if (this.strategy.equals(k, null)) {
            return this.containsNullKey ? this.value[this.n] : this.defRetValue;
        }
        K[] key = this.key;
        int mix = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
        int pos = mix;
        K curr2 = key[mix];
        if (curr2 == null) {
            return this.defRetValue;
        }
        if (this.strategy.equals(k, curr2)) {
            return this.value[pos];
        }
        do {
            int i = (pos + 1) & this.mask;
            pos = i;
            curr = key[i];
            if (curr == null) {
                return this.defRetValue;
            }
        } while (!this.strategy.equals(k, curr));
        return this.value[pos];
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObject2IntMap, it.unimi.dsi.fastutil.Function
    public boolean containsKey(Object k) {
        K curr;
        if (this.strategy.equals(k, null)) {
            return this.containsNullKey;
        }
        K[] key = this.key;
        int mix = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
        int pos = mix;
        K curr2 = key[mix];
        if (curr2 == null) {
            return false;
        }
        if (this.strategy.equals(k, curr2)) {
            return true;
        }
        do {
            int i = (pos + 1) & this.mask;
            pos = i;
            curr = key[i];
            if (curr == null) {
                return false;
            }
        } while (!this.strategy.equals(k, curr));
        return true;
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObject2IntMap, it.unimi.dsi.fastutil.objects.Object2IntMap
    public boolean containsValue(int v) {
        int[] value = this.value;
        K[] key = this.key;
        if (this.containsNullKey && value[this.n] == v) {
            return true;
        }
        int i = this.n;
        while (true) {
            int i2 = i - 1;
            if (i == 0) {
                return false;
            }
            if (key[i2] != null && value[i2] == v) {
                return true;
            }
            i = i2;
        }
    }

    @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
    public int getOrDefault(Object k, int defaultValue) {
        K curr;
        if (this.strategy.equals(k, null)) {
            return this.containsNullKey ? this.value[this.n] : defaultValue;
        }
        K[] key = this.key;
        int mix = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
        int pos = mix;
        K curr2 = key[mix];
        if (curr2 == null) {
            return defaultValue;
        }
        if (this.strategy.equals(k, curr2)) {
            return this.value[pos];
        }
        do {
            int i = (pos + 1) & this.mask;
            pos = i;
            curr = key[i];
            if (curr == null) {
                return defaultValue;
            }
        } while (!this.strategy.equals(k, curr));
        return this.value[pos];
    }

    @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
    public int putIfAbsent(K k, int v) {
        int pos = find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        insert((-pos) - 1, k, v);
        return this.defRetValue;
    }

    @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
    public boolean remove(Object k, int v) {
        if (this.strategy.equals(k, null)) {
            if (!this.containsNullKey || v != this.value[this.n]) {
                return false;
            }
            removeNullEntry();
            return true;
        }
        K[] key = this.key;
        int mix = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
        int pos = mix;
        K curr = key[mix];
        if (curr == null) {
            return false;
        }
        if (this.strategy.equals(k, curr) && v == this.value[pos]) {
            removeEntry(pos);
            return true;
        }
        while (true) {
            int i = (pos + 1) & this.mask;
            pos = i;
            K curr2 = key[i];
            if (curr2 == null) {
                return false;
            }
            if (this.strategy.equals(k, curr2) && v == this.value[pos]) {
                removeEntry(pos);
                return true;
            }
        }
    }

    @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
    public boolean replace(K k, int oldValue, int v) {
        int pos = find(k);
        if (pos < 0 || oldValue != this.value[pos]) {
            return false;
        }
        this.value[pos] = v;
        return true;
    }

    @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
    public int replace(K k, int v) {
        int pos = find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        int oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }

    @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
    public int computeIfAbsent(K k, ToIntFunction<? super K> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        int pos = find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        int newValue = mappingFunction.applyAsInt(k);
        insert((-pos) - 1, k, newValue);
        return newValue;
    }

    @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
    public int computeIfAbsent(K key, Object2IntFunction<? super K> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        int pos = find(key);
        if (pos >= 0) {
            return this.value[pos];
        }
        if (!mappingFunction.containsKey(key)) {
            return this.defRetValue;
        }
        int newValue = mappingFunction.getInt(key);
        insert((-pos) - 1, key, newValue);
        return newValue;
    }

    @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
    public int computeIntIfPresent(K k, BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        int pos = find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        Integer newValue = remappingFunction.apply(k, Integer.valueOf(this.value[pos]));
        if (newValue == null) {
            if (this.strategy.equals(k, null)) {
                removeNullEntry();
            } else {
                removeEntry(pos);
            }
            return this.defRetValue;
        }
        int[] iArr = this.value;
        int intValue = newValue.intValue();
        iArr[pos] = intValue;
        return intValue;
    }

    @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
    public int computeInt(K k, BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        int pos = find(k);
        Integer newValue = remappingFunction.apply(k, pos >= 0 ? Integer.valueOf(this.value[pos]) : null);
        if (newValue == null) {
            if (pos >= 0) {
                if (this.strategy.equals(k, null)) {
                    removeNullEntry();
                } else {
                    removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
        int newVal = newValue.intValue();
        if (pos < 0) {
            insert((-pos) - 1, k, newVal);
            return newVal;
        }
        this.value[pos] = newVal;
        return newVal;
    }

    @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
    public int merge(K k, int v, BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        int pos = find(k);
        if (pos < 0) {
            if (pos < 0) {
                insert((-pos) - 1, k, v);
            } else {
                this.value[pos] = v;
            }
            return v;
        }
        Integer newValue = remappingFunction.apply(Integer.valueOf(this.value[pos]), Integer.valueOf(v));
        if (newValue == null) {
            if (this.strategy.equals(k, null)) {
                removeNullEntry();
            } else {
                removeEntry(pos);
            }
            return this.defRetValue;
        }
        int[] iArr = this.value;
        int intValue = newValue.intValue();
        iArr[pos] = intValue;
        return intValue;
    }

    @Override // it.unimi.dsi.fastutil.Function, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
    public void clear() {
        if (this.size == 0) {
            return;
        }
        this.size = 0;
        this.containsNullKey = false;
        Arrays.fill(this.key, (Object) null);
        this.last = -1;
        this.first = -1;
    }

    @Override // it.unimi.dsi.fastutil.Function, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
    public int size() {
        return this.size;
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObject2IntMap, java.util.Map
    public boolean isEmpty() {
        return this.size == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public final class MapEntry implements Object2IntMap.Entry<K>, Map.Entry<K, Integer>, ObjectIntPair<K> {
        int index;

        MapEntry(int index) {
            this.index = index;
        }

        MapEntry() {
        }

        @Override // java.util.Map.Entry
        public K getKey() {
            return Object2IntLinkedOpenCustomHashMap.this.key[this.index];
        }

        @Override // it.unimi.dsi.fastutil.Pair
        public K left() {
            return Object2IntLinkedOpenCustomHashMap.this.key[this.index];
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap.Entry
        public int getIntValue() {
            return Object2IntLinkedOpenCustomHashMap.this.value[this.index];
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectIntPair
        public int rightInt() {
            return Object2IntLinkedOpenCustomHashMap.this.value[this.index];
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap.Entry
        public int setValue(int v) {
            int oldValue = Object2IntLinkedOpenCustomHashMap.this.value[this.index];
            Object2IntLinkedOpenCustomHashMap.this.value[this.index] = v;
            return oldValue;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectIntPair
        public ObjectIntPair<K> right(int v) {
            Object2IntLinkedOpenCustomHashMap.this.value[this.index] = v;
            return this;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap.Entry, java.util.Map.Entry
        @Deprecated
        public Integer getValue() {
            return Integer.valueOf(Object2IntLinkedOpenCustomHashMap.this.value[this.index]);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap.Entry, java.util.Map.Entry
        @Deprecated
        public Integer setValue(Integer v) {
            return Integer.valueOf(setValue(v.intValue()));
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            return Object2IntLinkedOpenCustomHashMap.this.strategy.equals(Object2IntLinkedOpenCustomHashMap.this.key[this.index], (K) entry.getKey()) && Object2IntLinkedOpenCustomHashMap.this.value[this.index] == ((Integer) entry.getValue()).intValue();
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return Object2IntLinkedOpenCustomHashMap.this.strategy.hashCode(Object2IntLinkedOpenCustomHashMap.this.key[this.index]) ^ Object2IntLinkedOpenCustomHashMap.this.value[this.index];
        }

        public String toString() {
            return Object2IntLinkedOpenCustomHashMap.this.key[this.index] + "=>" + Object2IntLinkedOpenCustomHashMap.this.value[this.index];
        }
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

    @Override // java.util.SortedMap
    public K firstKey() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.first];
    }

    @Override // java.util.SortedMap
    public K lastKey() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.last];
    }

    @Override // it.unimi.dsi.fastutil.objects.Object2IntSortedMap, java.util.SortedMap
    public Object2IntSortedMap<K> tailMap(K from) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.objects.Object2IntSortedMap, java.util.SortedMap
    public Object2IntSortedMap<K> headMap(K to) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.objects.Object2IntSortedMap, java.util.SortedMap
    public Object2IntSortedMap<K> subMap(K from, K to) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.objects.Object2IntSortedMap, java.util.SortedMap
    public Comparator<? super K> comparator() {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public abstract class MapIterator<ConsumerType> {
        int curr;
        int index;
        int next;
        int prev;

        abstract void acceptOnIndex(ConsumerType consumertype, int i);

        protected MapIterator() {
            this.prev = -1;
            this.next = -1;
            this.curr = -1;
            this.index = -1;
            this.next = Object2IntLinkedOpenCustomHashMap.this.first;
            this.index = 0;
        }

        private MapIterator(K k) {
            this.prev = -1;
            this.next = -1;
            this.curr = -1;
            this.index = -1;
            if (Object2IntLinkedOpenCustomHashMap.this.strategy.equals(k, null)) {
                if (Object2IntLinkedOpenCustomHashMap.this.containsNullKey) {
                    this.next = (int) Object2IntLinkedOpenCustomHashMap.this.link[Object2IntLinkedOpenCustomHashMap.this.n];
                    this.prev = Object2IntLinkedOpenCustomHashMap.this.n;
                    return;
                }
                throw new NoSuchElementException("The key " + k + " does not belong to this map.");
            }
            if (Object2IntLinkedOpenCustomHashMap.this.strategy.equals(Object2IntLinkedOpenCustomHashMap.this.key[Object2IntLinkedOpenCustomHashMap.this.last], k)) {
                this.prev = Object2IntLinkedOpenCustomHashMap.this.last;
                this.index = Object2IntLinkedOpenCustomHashMap.this.size;
                return;
            }
            int mix = HashCommon.mix(Object2IntLinkedOpenCustomHashMap.this.strategy.hashCode(k)) & Object2IntLinkedOpenCustomHashMap.this.mask;
            while (Object2IntLinkedOpenCustomHashMap.this.key[mix] != null) {
                if (Object2IntLinkedOpenCustomHashMap.this.strategy.equals(Object2IntLinkedOpenCustomHashMap.this.key[mix], k)) {
                    this.next = (int) Object2IntLinkedOpenCustomHashMap.this.link[mix];
                    this.prev = mix;
                    return;
                }
                mix = (mix + 1) & Object2IntLinkedOpenCustomHashMap.this.mask;
            }
            throw new NoSuchElementException("The key " + k + " does not belong to this map.");
        }

        public boolean hasNext() {
            return this.next != -1;
        }

        public boolean hasPrevious() {
            return this.prev != -1;
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
                this.index = Object2IntLinkedOpenCustomHashMap.this.size;
                return;
            }
            int pos = Object2IntLinkedOpenCustomHashMap.this.first;
            this.index = 1;
            while (pos != this.prev) {
                pos = (int) Object2IntLinkedOpenCustomHashMap.this.link[pos];
                this.index++;
            }
        }

        public int nextIndex() {
            ensureIndexKnown();
            return this.index;
        }

        public int previousIndex() {
            ensureIndexKnown();
            return this.index - 1;
        }

        public int nextEntry() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = this.next;
            this.next = (int) Object2IntLinkedOpenCustomHashMap.this.link[this.curr];
            this.prev = this.curr;
            if (this.index >= 0) {
                this.index++;
            }
            return this.curr;
        }

        public int previousEntry() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            this.curr = this.prev;
            this.prev = (int) (Object2IntLinkedOpenCustomHashMap.this.link[this.curr] >>> 32);
            this.next = this.curr;
            if (this.index >= 0) {
                this.index--;
            }
            return this.curr;
        }

        public void forEachRemaining(ConsumerType action) {
            while (hasNext()) {
                this.curr = this.next;
                this.next = (int) Object2IntLinkedOpenCustomHashMap.this.link[this.curr];
                this.prev = this.curr;
                if (this.index >= 0) {
                    this.index++;
                }
                acceptOnIndex(action, this.curr);
            }
        }

        public void remove() {
            K curr;
            ensureIndexKnown();
            if (this.curr == -1) {
                throw new IllegalStateException();
            }
            if (this.curr == this.prev) {
                this.index--;
                this.prev = (int) (Object2IntLinkedOpenCustomHashMap.this.link[this.curr] >>> 32);
            } else {
                this.next = (int) Object2IntLinkedOpenCustomHashMap.this.link[this.curr];
            }
            Object2IntLinkedOpenCustomHashMap object2IntLinkedOpenCustomHashMap = Object2IntLinkedOpenCustomHashMap.this;
            object2IntLinkedOpenCustomHashMap.size--;
            if (this.prev == -1) {
                Object2IntLinkedOpenCustomHashMap.this.first = this.next;
            } else {
                long[] jArr = Object2IntLinkedOpenCustomHashMap.this.link;
                int i = this.prev;
                jArr[i] = jArr[i] ^ ((Object2IntLinkedOpenCustomHashMap.this.link[this.prev] ^ (this.next & 4294967295L)) & 4294967295L);
            }
            if (this.next == -1) {
                Object2IntLinkedOpenCustomHashMap.this.last = this.prev;
            } else {
                long[] jArr2 = Object2IntLinkedOpenCustomHashMap.this.link;
                int i2 = this.next;
                jArr2[i2] = ((((4294967295L & this.prev) << 32) ^ Object2IntLinkedOpenCustomHashMap.this.link[this.next]) & (-4294967296L)) ^ jArr2[i2];
            }
            int pos = this.curr;
            this.curr = -1;
            if (pos == Object2IntLinkedOpenCustomHashMap.this.n) {
                Object2IntLinkedOpenCustomHashMap.this.containsNullKey = false;
                Object2IntLinkedOpenCustomHashMap.this.key[Object2IntLinkedOpenCustomHashMap.this.n] = null;
                return;
            }
            K[] key = Object2IntLinkedOpenCustomHashMap.this.key;
            while (true) {
                int last = pos;
                pos = (pos + 1) & Object2IntLinkedOpenCustomHashMap.this.mask;
                while (true) {
                    curr = key[pos];
                    if (curr == null) {
                        key[last] = null;
                        return;
                    }
                    int slot = HashCommon.mix(Object2IntLinkedOpenCustomHashMap.this.strategy.hashCode(curr)) & Object2IntLinkedOpenCustomHashMap.this.mask;
                    if (last > pos) {
                        if (last >= slot && slot > pos) {
                            break;
                        }
                        pos = (pos + 1) & Object2IntLinkedOpenCustomHashMap.this.mask;
                    } else if (last >= slot || slot > pos) {
                        break;
                    } else {
                        pos = (pos + 1) & Object2IntLinkedOpenCustomHashMap.this.mask;
                    }
                }
                key[last] = curr;
                Object2IntLinkedOpenCustomHashMap.this.value[last] = Object2IntLinkedOpenCustomHashMap.this.value[pos];
                if (this.next == pos) {
                    this.next = last;
                }
                if (this.prev == pos) {
                    this.prev = last;
                }
                Object2IntLinkedOpenCustomHashMap.this.fixPointers(pos, last);
            }
        }

        public int skip(int n) {
            int i;
            int i2 = n;
            while (true) {
                i = i2 - 1;
                if (i2 == 0 || !hasNext()) {
                    break;
                }
                nextEntry();
                i2 = i;
            }
            return (n - i) - 1;
        }

        public int back(int n) {
            int i;
            int i2 = n;
            while (true) {
                i = i2 - 1;
                if (i2 == 0 || !hasPrevious()) {
                    break;
                }
                previousEntry();
                i2 = i;
            }
            return (n - i) - 1;
        }

        public void set(Object2IntMap.Entry<K> ok) {
            throw new UnsupportedOperationException();
        }

        public void add(Object2IntMap.Entry<K> ok) {
            throw new UnsupportedOperationException();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class EntryIterator extends Object2IntLinkedOpenCustomHashMap<K>.MapIterator<Consumer<? super Object2IntMap.Entry<K>>> implements ObjectListIterator<Object2IntMap.Entry<K>> {
        private Object2IntLinkedOpenCustomHashMap<K>.MapEntry entry;

        @Override // it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.ListIterator
        public /* bridge */ /* synthetic */ void add(Object obj) {
            super.add((Object2IntMap.Entry) obj);
        }

        @Override // java.util.Iterator
        public /* bridge */ /* synthetic */ void forEachRemaining(Consumer consumer) {
            super.forEachRemaining((EntryIterator) consumer);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.ListIterator
        public /* bridge */ /* synthetic */ void set(Object obj) {
            super.set((Object2IntMap.Entry) obj);
        }

        public EntryIterator() {
            super();
        }

        public EntryIterator(K from) {
            super(from);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenCustomHashMap.MapIterator
        public final void acceptOnIndex(Consumer<? super Object2IntMap.Entry<K>> action, int index) {
            action.accept(new MapEntry(index));
        }

        @Override // java.util.Iterator, java.util.ListIterator
        public Object2IntLinkedOpenCustomHashMap<K>.MapEntry next() {
            Object2IntLinkedOpenCustomHashMap<K>.MapEntry mapEntry = new MapEntry(nextEntry());
            this.entry = mapEntry;
            return mapEntry;
        }

        @Override // it.unimi.dsi.fastutil.BidirectionalIterator, java.util.ListIterator
        public Object2IntLinkedOpenCustomHashMap<K>.MapEntry previous() {
            Object2IntLinkedOpenCustomHashMap<K>.MapEntry mapEntry = new MapEntry(previousEntry());
            this.entry = mapEntry;
            return mapEntry;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenCustomHashMap.MapIterator, it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.Iterator, java.util.ListIterator
        public void remove() {
            super.remove();
            this.entry.index = -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class FastEntryIterator extends Object2IntLinkedOpenCustomHashMap<K>.MapIterator<Consumer<? super Object2IntMap.Entry<K>>> implements ObjectListIterator<Object2IntMap.Entry<K>> {
        final Object2IntLinkedOpenCustomHashMap<K>.MapEntry entry;

        @Override // it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.ListIterator
        public /* bridge */ /* synthetic */ void add(Object obj) {
            super.add((Object2IntMap.Entry) obj);
        }

        @Override // java.util.Iterator
        public /* bridge */ /* synthetic */ void forEachRemaining(Consumer consumer) {
            super.forEachRemaining((FastEntryIterator) consumer);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.ListIterator
        public /* bridge */ /* synthetic */ void set(Object obj) {
            super.set((Object2IntMap.Entry) obj);
        }

        public FastEntryIterator() {
            super();
            this.entry = new MapEntry();
        }

        public FastEntryIterator(K from) {
            super(from);
            this.entry = new MapEntry();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenCustomHashMap.MapIterator
        public final void acceptOnIndex(Consumer<? super Object2IntMap.Entry<K>> action, int index) {
            this.entry.index = index;
            action.accept(this.entry);
        }

        @Override // java.util.Iterator, java.util.ListIterator
        public Object2IntLinkedOpenCustomHashMap<K>.MapEntry next() {
            this.entry.index = nextEntry();
            return this.entry;
        }

        @Override // it.unimi.dsi.fastutil.BidirectionalIterator, java.util.ListIterator
        public Object2IntLinkedOpenCustomHashMap<K>.MapEntry previous() {
            this.entry.index = previousEntry();
            return this.entry;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class MapEntrySet extends AbstractObjectSortedSet<Object2IntMap.Entry<K>> implements Object2IntSortedMap.FastSortedEntrySet<K> {
        private static final int SPLITERATOR_CHARACTERISTICS = 81;

        private MapEntrySet() {
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet, it.unimi.dsi.fastutil.objects.AbstractObjectSet, it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        public ObjectBidirectionalIterator<Object2IntMap.Entry<K>> iterator() {
            return new EntryIterator();
        }

        @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        /* renamed from: spliterator */
        public ObjectSpliterator<Object2IntMap.Entry<K>> mo221spliterator() {
            return ObjectSpliterators.asSpliterator(iterator(), Size64.sizeOf(Object2IntLinkedOpenCustomHashMap.this), 81);
        }

        @Override // java.util.SortedSet
        public Comparator<? super Object2IntMap.Entry<K>> comparator() {
            return null;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public ObjectSortedSet<Object2IntMap.Entry<K>> subSet(Object2IntMap.Entry<K> fromElement, Object2IntMap.Entry<K> toElement) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public ObjectSortedSet<Object2IntMap.Entry<K>> headSet(Object2IntMap.Entry<K> toElement) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public ObjectSortedSet<Object2IntMap.Entry<K>> tailSet(Object2IntMap.Entry<K> fromElement) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.SortedSet
        public Object2IntMap.Entry<K> first() {
            if (Object2IntLinkedOpenCustomHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return new MapEntry(Object2IntLinkedOpenCustomHashMap.this.first);
        }

        @Override // java.util.SortedSet
        public Object2IntMap.Entry<K> last() {
            if (Object2IntLinkedOpenCustomHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return new MapEntry(Object2IntLinkedOpenCustomHashMap.this.last);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            K curr;
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> e = (Map.Entry) o;
            if (e.getValue() == null || !(e.getValue() instanceof Integer)) {
                return false;
            }
            Object key = e.getKey();
            int v = ((Integer) e.getValue()).intValue();
            if (Object2IntLinkedOpenCustomHashMap.this.strategy.equals(key, null)) {
                return Object2IntLinkedOpenCustomHashMap.this.containsNullKey && Object2IntLinkedOpenCustomHashMap.this.value[Object2IntLinkedOpenCustomHashMap.this.n] == v;
            }
            K[] key2 = Object2IntLinkedOpenCustomHashMap.this.key;
            int mix = HashCommon.mix(Object2IntLinkedOpenCustomHashMap.this.strategy.hashCode(key)) & Object2IntLinkedOpenCustomHashMap.this.mask;
            int pos = mix;
            K curr2 = key2[mix];
            if (curr2 == null) {
                return false;
            }
            if (Object2IntLinkedOpenCustomHashMap.this.strategy.equals(key, curr2)) {
                return Object2IntLinkedOpenCustomHashMap.this.value[pos] == v;
            }
            do {
                int i = (pos + 1) & Object2IntLinkedOpenCustomHashMap.this.mask;
                pos = i;
                curr = key2[i];
                if (curr == null) {
                    return false;
                }
            } while (!Object2IntLinkedOpenCustomHashMap.this.strategy.equals(key, curr));
            return Object2IntLinkedOpenCustomHashMap.this.value[pos] == v;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> e = (Map.Entry) o;
            if (e.getValue() == null || !(e.getValue() instanceof Integer)) {
                return false;
            }
            Object key = e.getKey();
            int v = ((Integer) e.getValue()).intValue();
            if (Object2IntLinkedOpenCustomHashMap.this.strategy.equals(key, null)) {
                if (!Object2IntLinkedOpenCustomHashMap.this.containsNullKey || Object2IntLinkedOpenCustomHashMap.this.value[Object2IntLinkedOpenCustomHashMap.this.n] != v) {
                    return false;
                }
                Object2IntLinkedOpenCustomHashMap.this.removeNullEntry();
                return true;
            }
            K[] key2 = Object2IntLinkedOpenCustomHashMap.this.key;
            int mix = HashCommon.mix(Object2IntLinkedOpenCustomHashMap.this.strategy.hashCode(key)) & Object2IntLinkedOpenCustomHashMap.this.mask;
            int pos = mix;
            K curr = key2[mix];
            if (curr == null) {
                return false;
            }
            if (Object2IntLinkedOpenCustomHashMap.this.strategy.equals(curr, key)) {
                if (Object2IntLinkedOpenCustomHashMap.this.value[pos] != v) {
                    return false;
                }
                Object2IntLinkedOpenCustomHashMap.this.removeEntry(pos);
                return true;
            }
            while (true) {
                int i = (pos + 1) & Object2IntLinkedOpenCustomHashMap.this.mask;
                pos = i;
                K curr2 = key2[i];
                if (curr2 == null) {
                    return false;
                }
                if (Object2IntLinkedOpenCustomHashMap.this.strategy.equals(curr2, key) && Object2IntLinkedOpenCustomHashMap.this.value[pos] == v) {
                    Object2IntLinkedOpenCustomHashMap.this.removeEntry(pos);
                    return true;
                }
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return Object2IntLinkedOpenCustomHashMap.this.size;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            Object2IntLinkedOpenCustomHashMap.this.clear();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet
        public ObjectListIterator<Object2IntMap.Entry<K>> iterator(Object2IntMap.Entry<K> from) {
            return new EntryIterator(from.getKey());
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntSortedMap.FastSortedEntrySet, it.unimi.dsi.fastutil.objects.Object2IntMap.FastEntrySet
        public ObjectListIterator<Object2IntMap.Entry<K>> fastIterator() {
            return new FastEntryIterator();
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntSortedMap.FastSortedEntrySet
        public ObjectListIterator<Object2IntMap.Entry<K>> fastIterator(Object2IntMap.Entry<K> from) {
            return new FastEntryIterator(from.getKey());
        }

        @Override // java.lang.Iterable
        public void forEach(Consumer<? super Object2IntMap.Entry<K>> consumer) {
            int curr = Object2IntLinkedOpenCustomHashMap.this.size;
            int next = Object2IntLinkedOpenCustomHashMap.this.first;
            while (true) {
                int i = curr - 1;
                if (curr != 0) {
                    int curr2 = next;
                    next = (int) Object2IntLinkedOpenCustomHashMap.this.link[curr2];
                    consumer.accept(new MapEntry(curr2));
                    curr = i;
                } else {
                    return;
                }
            }
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap.FastEntrySet
        public void fastForEach(Consumer<? super Object2IntMap.Entry<K>> consumer) {
            Object2IntLinkedOpenCustomHashMap<K>.MapEntry entry = new MapEntry();
            int i = Object2IntLinkedOpenCustomHashMap.this.size;
            int next = Object2IntLinkedOpenCustomHashMap.this.first;
            while (true) {
                int i2 = i - 1;
                if (i != 0) {
                    entry.index = next;
                    next = (int) Object2IntLinkedOpenCustomHashMap.this.link[next];
                    consumer.accept(entry);
                    i = i2;
                } else {
                    return;
                }
            }
        }
    }

    @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, it.unimi.dsi.fastutil.objects.Object2IntSortedMap
    public Object2IntSortedMap.FastSortedEntrySet<K> object2IntEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet();
        }
        return this.entries;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class KeyIterator extends Object2IntLinkedOpenCustomHashMap<K>.MapIterator<Consumer<? super K>> implements ObjectListIterator<K> {
        @Override // java.util.Iterator
        public /* bridge */ /* synthetic */ void forEachRemaining(Consumer consumer) {
            super.forEachRemaining((KeyIterator) consumer);
        }

        public KeyIterator(K k) {
            super(k);
        }

        @Override // it.unimi.dsi.fastutil.BidirectionalIterator, java.util.ListIterator
        public K previous() {
            return Object2IntLinkedOpenCustomHashMap.this.key[previousEntry()];
        }

        public KeyIterator() {
            super();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenCustomHashMap.MapIterator
        public final void acceptOnIndex(Consumer<? super K> consumer, int i) {
            consumer.accept(Object2IntLinkedOpenCustomHashMap.this.key[i]);
        }

        @Override // java.util.Iterator, java.util.ListIterator
        public K next() {
            return Object2IntLinkedOpenCustomHashMap.this.key[nextEntry()];
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class KeySet extends AbstractObjectSortedSet<K> {
        private static final int SPLITERATOR_CHARACTERISTICS = 81;

        private KeySet() {
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public /* bridge */ /* synthetic */ SortedSet headSet(Object obj) {
            return headSet((KeySet) obj);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet
        public /* bridge */ /* synthetic */ ObjectBidirectionalIterator iterator(Object obj) {
            return iterator((KeySet) obj);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public /* bridge */ /* synthetic */ SortedSet tailSet(Object obj) {
            return tailSet((KeySet) obj);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet
        public ObjectListIterator<K> iterator(K from) {
            return new KeyIterator(from);
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet, it.unimi.dsi.fastutil.objects.AbstractObjectSet, it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        public ObjectListIterator<K> iterator() {
            return new KeyIterator();
        }

        @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        /* renamed from: spliterator */
        public ObjectSpliterator<K> mo221spliterator() {
            return ObjectSpliterators.asSpliterator(iterator(), Size64.sizeOf(Object2IntLinkedOpenCustomHashMap.this), 81);
        }

        @Override // java.lang.Iterable
        public void forEach(Consumer<? super K> consumer) {
            int i = Object2IntLinkedOpenCustomHashMap.this.size;
            int i2 = Object2IntLinkedOpenCustomHashMap.this.first;
            while (true) {
                int i3 = i - 1;
                if (i != 0) {
                    int i4 = i2;
                    i2 = (int) Object2IntLinkedOpenCustomHashMap.this.link[i4];
                    consumer.accept(Object2IntLinkedOpenCustomHashMap.this.key[i4]);
                    i = i3;
                } else {
                    return;
                }
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return Object2IntLinkedOpenCustomHashMap.this.size;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object k) {
            return Object2IntLinkedOpenCustomHashMap.this.containsKey(k);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object k) {
            int oldSize = Object2IntLinkedOpenCustomHashMap.this.size;
            Object2IntLinkedOpenCustomHashMap.this.removeInt(k);
            return Object2IntLinkedOpenCustomHashMap.this.size != oldSize;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            Object2IntLinkedOpenCustomHashMap.this.clear();
        }

        @Override // java.util.SortedSet
        public K first() {
            if (Object2IntLinkedOpenCustomHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return Object2IntLinkedOpenCustomHashMap.this.key[Object2IntLinkedOpenCustomHashMap.this.first];
        }

        @Override // java.util.SortedSet
        public K last() {
            if (Object2IntLinkedOpenCustomHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return Object2IntLinkedOpenCustomHashMap.this.key[Object2IntLinkedOpenCustomHashMap.this.last];
        }

        @Override // java.util.SortedSet
        public Comparator<? super K> comparator() {
            return null;
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
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObject2IntSortedMap, it.unimi.dsi.fastutil.objects.AbstractObject2IntMap, it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
    public ObjectSortedSet<K> keySet() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class ValueIterator extends Object2IntLinkedOpenCustomHashMap<K>.MapIterator<IntConsumer> implements IntListIterator {
        @Override // java.util.PrimitiveIterator.OfInt
        public /* bridge */ /* synthetic */ void forEachRemaining(IntConsumer intConsumer) {
            super.forEachRemaining((ValueIterator) intConsumer);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator
        public int previousInt() {
            return Object2IntLinkedOpenCustomHashMap.this.value[previousEntry()];
        }

        public ValueIterator() {
            super();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenCustomHashMap.MapIterator
        public final void acceptOnIndex(IntConsumer action, int index) {
            action.accept(Object2IntLinkedOpenCustomHashMap.this.value[index]);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt
        public int nextInt() {
            return Object2IntLinkedOpenCustomHashMap.this.value[nextEntry()];
        }
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObject2IntSortedMap, it.unimi.dsi.fastutil.objects.AbstractObject2IntMap, it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
    /* renamed from: values */
    public Collection<Integer> values2() {
        if (this.values == null) {
            this.values = new AbstractIntCollection() { // from class: it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenCustomHashMap.1
                private static final int SPLITERATOR_CHARACTERISTICS = 336;

                @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
                public IntIterator iterator() {
                    return new ValueIterator();
                }

                @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
                public IntSpliterator spliterator() {
                    return IntSpliterators.asSpliterator(iterator(), Size64.sizeOf(Object2IntLinkedOpenCustomHashMap.this), SPLITERATOR_CHARACTERISTICS);
                }

                @Override // it.unimi.dsi.fastutil.ints.IntIterable
                public void forEach(IntConsumer consumer) {
                    int curr = Object2IntLinkedOpenCustomHashMap.this.size;
                    int next = Object2IntLinkedOpenCustomHashMap.this.first;
                    while (true) {
                        int i = curr - 1;
                        if (curr != 0) {
                            int curr2 = next;
                            next = (int) Object2IntLinkedOpenCustomHashMap.this.link[curr2];
                            consumer.accept(Object2IntLinkedOpenCustomHashMap.this.value[curr2]);
                            curr = i;
                        } else {
                            return;
                        }
                    }
                }

                @Override // java.util.AbstractCollection, java.util.Collection
                public int size() {
                    return Object2IntLinkedOpenCustomHashMap.this.size;
                }

                @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
                public boolean contains(int v) {
                    return Object2IntLinkedOpenCustomHashMap.this.containsValue(v);
                }

                @Override // java.util.AbstractCollection, java.util.Collection
                public void clear() {
                    Object2IntLinkedOpenCustomHashMap.this.clear();
                }
            };
        }
        return this.values;
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
        int[] iArr;
        int i2;
        int i3;
        K[] kArr2 = this.key;
        int[] iArr2 = this.value;
        int i4 = i - 1;
        K[] kArr3 = (K[]) new Object[i + 1];
        int[] iArr3 = new int[i + 1];
        int i5 = this.first;
        int i6 = -1;
        int i7 = -1;
        long[] jArr = this.link;
        long[] jArr2 = new long[i + 1];
        this.first = -1;
        int i8 = this.size;
        while (true) {
            int i9 = i8 - 1;
            if (i8 == 0) {
                break;
            }
            if (this.strategy.equals(kArr2[i5], null)) {
                mix = i;
            } else {
                mix = HashCommon.mix(this.strategy.hashCode(kArr2[i5])) & i4;
                while (kArr3[mix] != null) {
                    mix = (mix + 1) & i4;
                }
            }
            kArr3[mix] = kArr2[i5];
            iArr3[mix] = iArr2[i5];
            if (i6 != -1) {
                iArr = iArr2;
                jArr2[i7] = jArr2[i7] ^ ((jArr2[i7] ^ (mix & 4294967295L)) & 4294967295L);
                kArr = kArr2;
                i2 = i9;
                jArr2[mix] = jArr2[mix] ^ ((jArr2[mix] ^ ((i7 & 4294967295L) << 32)) & (-4294967296L));
                i3 = mix;
            } else {
                kArr = kArr2;
                iArr = iArr2;
                i2 = i9;
                this.first = mix;
                i3 = mix;
                jArr2[mix] = -1;
            }
            i7 = i3;
            int i10 = i5;
            i5 = (int) jArr[i5];
            i6 = i10;
            iArr2 = iArr;
            i8 = i2;
            kArr2 = kArr;
        }
        this.link = jArr2;
        this.last = i7;
        if (i7 != -1) {
            jArr2[i7] = jArr2[i7] | 4294967295L;
        }
        this.n = i;
        this.mask = i4;
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.key = kArr3;
        this.value = iArr3;
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public Object2IntLinkedOpenCustomHashMap<K> m259clone() {
        try {
            Object2IntLinkedOpenCustomHashMap<K> object2IntLinkedOpenCustomHashMap = (Object2IntLinkedOpenCustomHashMap) super.clone();
            object2IntLinkedOpenCustomHashMap.keys = null;
            object2IntLinkedOpenCustomHashMap.values = null;
            object2IntLinkedOpenCustomHashMap.entries = null;
            object2IntLinkedOpenCustomHashMap.containsNullKey = this.containsNullKey;
            object2IntLinkedOpenCustomHashMap.key = (K[]) ((Object[]) this.key.clone());
            object2IntLinkedOpenCustomHashMap.value = (int[]) this.value.clone();
            object2IntLinkedOpenCustomHashMap.link = (long[]) this.link.clone();
            object2IntLinkedOpenCustomHashMap.strategy = this.strategy;
            return object2IntLinkedOpenCustomHashMap;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObject2IntMap, java.util.Map
    public int hashCode() {
        int i = 0;
        int realSize = realSize();
        int i2 = 0;
        int i3 = 0;
        while (true) {
            int i4 = realSize - 1;
            if (realSize == 0) {
                break;
            }
            while (this.key[i2] == null) {
                i2++;
            }
            if (this != this.key[i2]) {
                i3 = this.strategy.hashCode(this.key[i2]);
            }
            i3 ^= this.value[i2];
            i += i3;
            i2++;
            realSize = i4;
        }
        return this.containsNullKey ? i + this.value[this.n] : i;
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        K[] key = this.key;
        int[] value = this.value;
        Object2IntLinkedOpenCustomHashMap<K>.EntryIterator i = new EntryIterator();
        s.defaultWriteObject();
        int e = this.size;
        while (true) {
            int j = e - 1;
            if (e != 0) {
                int e2 = i.nextEntry();
                s.writeObject(key[e2]);
                s.writeInt(value[e2]);
                e = j;
            } else {
                return;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        int mix;
        objectInputStream.defaultReadObject();
        this.n = HashCommon.arraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        boolean z = true;
        this.mask = this.n - 1;
        K[] kArr = (K[]) new Object[this.n + 1];
        this.key = kArr;
        int[] iArr = new int[this.n + 1];
        this.value = iArr;
        long[] jArr = new long[this.n + 1];
        this.link = jArr;
        int i = -1;
        int i2 = -1;
        this.last = -1;
        this.first = -1;
        int i3 = this.size;
        while (true) {
            int i4 = i3 - 1;
            if (i3 == 0) {
                break;
            }
            Object readObject = objectInputStream.readObject();
            int readInt = objectInputStream.readInt();
            if (!this.strategy.equals(readObject, null)) {
                mix = HashCommon.mix(this.strategy.hashCode(readObject)) & this.mask;
                while (kArr[mix] != 0) {
                    mix = (mix + 1) & this.mask;
                }
            } else {
                mix = this.n;
                this.containsNullKey = z;
            }
            kArr[mix] = readObject;
            iArr[mix] = readInt;
            if (this.first == i2) {
                this.first = mix;
                i = mix;
                jArr[mix] = jArr[mix] | (-4294967296L);
                i3 = i4;
                iArr = iArr;
                z = true;
                i2 = -1;
            } else {
                jArr[i] = jArr[i] ^ ((jArr[i] ^ (mix & 4294967295L)) & 4294967295L);
                jArr[mix] = jArr[mix] ^ ((jArr[mix] ^ ((i & 4294967295L) << 32)) & (-4294967296L));
                i = mix;
                i3 = i4;
                iArr = iArr;
                z = true;
                i2 = -1;
            }
        }
        this.last = i;
        if (i != -1) {
            jArr[i] = jArr[i] | 4294967295L;
        }
    }

    private void checkTable() {
    }
}
