package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.Size64;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap;
import it.unimi.dsi.fastutil.objects.AbstractObjectCollection;
import it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectSpliterator;
import it.unimi.dsi.fastutil.objects.ObjectSpliterators;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.LongFunction;

/* loaded from: classes4.dex */
public class Long2ObjectLinkedOpenHashMap<V> extends AbstractLong2ObjectSortedMap<V> implements Serializable, Cloneable, Hash {
    private static final boolean ASSERTS = false;
    private static final long serialVersionUID = 0;
    protected transient boolean containsNullKey;
    protected transient Long2ObjectSortedMap.FastSortedEntrySet<V> entries;
    protected final float f;
    protected transient int first;
    protected transient long[] key;
    protected transient LongSortedSet keys;
    protected transient int last;
    protected transient long[] link;
    protected transient int mask;
    protected transient int maxFill;
    protected final transient int minN;
    protected transient int n;
    protected int size;
    protected transient V[] value;
    protected transient ObjectCollection<V> values;

    public Long2ObjectLinkedOpenHashMap(int i, float f) {
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
        this.key = new long[this.n + 1];
        this.value = (V[]) new Object[this.n + 1];
        this.link = new long[this.n + 1];
    }

    public Long2ObjectLinkedOpenHashMap(int expected) {
        this(expected, 0.75f);
    }

    public Long2ObjectLinkedOpenHashMap() {
        this(16, 0.75f);
    }

    public Long2ObjectLinkedOpenHashMap(Map<? extends Long, ? extends V> m, float f) {
        this(m.size(), f);
        putAll(m);
    }

    public Long2ObjectLinkedOpenHashMap(Map<? extends Long, ? extends V> m) {
        this(m, 0.75f);
    }

    public Long2ObjectLinkedOpenHashMap(Long2ObjectMap<V> m, float f) {
        this(m.size(), f);
        putAll(m);
    }

    public Long2ObjectLinkedOpenHashMap(Long2ObjectMap<V> m) {
        this((Long2ObjectMap) m, 0.75f);
    }

    public Long2ObjectLinkedOpenHashMap(long[] k, V[] v, float f) {
        this(k.length, f);
        if (k.length != v.length) {
            throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
        }
        for (int i = 0; i < k.length; i++) {
            put(k[i], (long) v[i]);
        }
    }

    public Long2ObjectLinkedOpenHashMap(long[] k, V[] v) {
        this(k, v, 0.75f);
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
    public V removeEntry(int pos) {
        V oldValue = this.value[pos];
        this.value[pos] = null;
        this.size--;
        fixPointers(pos);
        shiftKeys(pos);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            rehash(this.n / 2);
        }
        return oldValue;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public V removeNullEntry() {
        this.containsNullKey = false;
        V oldValue = this.value[this.n];
        this.value[this.n] = null;
        this.size--;
        fixPointers(this.n);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            rehash(this.n / 2);
        }
        return oldValue;
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLong2ObjectMap, java.util.Map
    public void putAll(Map<? extends Long, ? extends V> m) {
        if (this.f <= 0.5d) {
            ensureCapacity(m.size());
        } else {
            tryCapacity(size() + m.size());
        }
        super.putAll(m);
    }

    private int find(long k) {
        long curr;
        if (k == 0) {
            return this.containsNullKey ? this.n : -(this.n + 1);
        }
        long[] key = this.key;
        int mix = ((int) HashCommon.mix(k)) & this.mask;
        int pos = mix;
        long curr2 = key[mix];
        if (curr2 == 0) {
            return -(pos + 1);
        }
        if (k == curr2) {
            return pos;
        }
        do {
            int i = (pos + 1) & this.mask;
            pos = i;
            curr = key[i];
            if (curr == 0) {
                return -(pos + 1);
            }
        } while (k != curr);
        return pos;
    }

    private void insert(int pos, long k, V v) {
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

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunction
    public V put(long k, V v) {
        int pos = find(k);
        if (pos < 0) {
            insert((-pos) - 1, k, v);
            return this.defRetValue;
        }
        V oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
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
                    this.value[last] = null;
                    return;
                }
                int slot = ((int) HashCommon.mix(curr)) & this.mask;
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

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunction
    public V remove(long k) {
        long curr;
        if (k == 0) {
            return this.containsNullKey ? removeNullEntry() : this.defRetValue;
        }
        long[] key = this.key;
        int mix = ((int) HashCommon.mix(k)) & this.mask;
        int pos = mix;
        long curr2 = key[mix];
        if (curr2 == 0) {
            return this.defRetValue;
        }
        if (k == curr2) {
            return removeEntry(pos);
        }
        do {
            int i = (pos + 1) & this.mask;
            pos = i;
            curr = key[i];
            if (curr == 0) {
                return this.defRetValue;
            }
        } while (k != curr);
        return removeEntry(pos);
    }

    private V setValue(int pos, V v) {
        V oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }

    public V removeFirst() {
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
        V v = this.value[pos];
        if (pos == this.n) {
            this.containsNullKey = false;
            this.value[this.n] = null;
        } else {
            shiftKeys(pos);
        }
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            rehash(this.n / 2);
        }
        return v;
    }

    public V removeLast() {
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
        V v = this.value[pos];
        if (pos == this.n) {
            this.containsNullKey = false;
            this.value[this.n] = null;
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

    public V getAndMoveToFirst(long k) {
        long curr;
        if (k == 0) {
            if (this.containsNullKey) {
                moveIndexToFirst(this.n);
                return this.value[this.n];
            }
            return this.defRetValue;
        }
        long[] key = this.key;
        int mix = ((int) HashCommon.mix(k)) & this.mask;
        int pos = mix;
        long curr2 = key[mix];
        if (curr2 == 0) {
            return this.defRetValue;
        }
        if (k == curr2) {
            moveIndexToFirst(pos);
            return this.value[pos];
        }
        do {
            int i = (pos + 1) & this.mask;
            pos = i;
            curr = key[i];
            if (curr == 0) {
                return this.defRetValue;
            }
        } while (k != curr);
        moveIndexToFirst(pos);
        return this.value[pos];
    }

    public V getAndMoveToLast(long k) {
        long curr;
        if (k == 0) {
            if (this.containsNullKey) {
                moveIndexToLast(this.n);
                return this.value[this.n];
            }
            return this.defRetValue;
        }
        long[] key = this.key;
        int mix = ((int) HashCommon.mix(k)) & this.mask;
        int pos = mix;
        long curr2 = key[mix];
        if (curr2 == 0) {
            return this.defRetValue;
        }
        if (k == curr2) {
            moveIndexToLast(pos);
            return this.value[pos];
        }
        do {
            int i = (pos + 1) & this.mask;
            pos = i;
            curr = key[i];
            if (curr == 0) {
                return this.defRetValue;
            }
        } while (k != curr);
        moveIndexToLast(pos);
        return this.value[pos];
    }

    public V putAndMoveToFirst(long k, V v) {
        int pos;
        long curr;
        if (k == 0) {
            if (this.containsNullKey) {
                moveIndexToFirst(this.n);
                return setValue(this.n, v);
            }
            this.containsNullKey = true;
            pos = this.n;
        } else {
            long[] key = this.key;
            int mix = ((int) HashCommon.mix(k)) & this.mask;
            int pos2 = mix;
            long curr2 = key[mix];
            if (curr2 == 0) {
                pos = pos2;
            } else {
                if (curr2 == k) {
                    moveIndexToFirst(pos2);
                    return setValue(pos2, v);
                }
                do {
                    int i = (pos2 + 1) & this.mask;
                    pos2 = i;
                    curr = key[i];
                    if (curr == 0) {
                        pos = pos2;
                    }
                } while (curr != k);
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

    public V putAndMoveToLast(long k, V v) {
        int pos;
        long curr;
        if (k == 0) {
            if (this.containsNullKey) {
                moveIndexToLast(this.n);
                return setValue(this.n, v);
            }
            this.containsNullKey = true;
            pos = this.n;
        } else {
            long[] key = this.key;
            int mix = ((int) HashCommon.mix(k)) & this.mask;
            int pos2 = mix;
            long curr2 = key[mix];
            if (curr2 == 0) {
                pos = pos2;
            } else {
                if (curr2 == k) {
                    moveIndexToLast(pos2);
                    return setValue(pos2, v);
                }
                do {
                    int i = (pos2 + 1) & this.mask;
                    pos2 = i;
                    curr = key[i];
                    if (curr == 0) {
                        pos = pos2;
                    }
                } while (curr != k);
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

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunction
    public V get(long k) {
        long curr;
        if (k == 0) {
            return this.containsNullKey ? this.value[this.n] : this.defRetValue;
        }
        long[] key = this.key;
        int mix = ((int) HashCommon.mix(k)) & this.mask;
        int pos = mix;
        long curr2 = key[mix];
        if (curr2 == 0) {
            return this.defRetValue;
        }
        if (k == curr2) {
            return this.value[pos];
        }
        do {
            int i = (pos + 1) & this.mask;
            pos = i;
            curr = key[i];
            if (curr == 0) {
                return this.defRetValue;
            }
        } while (k != curr);
        return this.value[pos];
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLong2ObjectMap, it.unimi.dsi.fastutil.longs.Long2ObjectFunction, it.unimi.dsi.fastutil.longs.Long2ObjectMap
    public boolean containsKey(long k) {
        long curr;
        if (k == 0) {
            return this.containsNullKey;
        }
        long[] key = this.key;
        int mix = ((int) HashCommon.mix(k)) & this.mask;
        int pos = mix;
        long curr2 = key[mix];
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

    @Override // it.unimi.dsi.fastutil.longs.AbstractLong2ObjectMap, java.util.Map
    public boolean containsValue(Object v) {
        V[] value = this.value;
        long[] key = this.key;
        if (this.containsNullKey && Objects.equals(value[this.n], v)) {
            return true;
        }
        int i = this.n;
        while (true) {
            int i2 = i - 1;
            if (i == 0) {
                return false;
            }
            if (key[i2] != 0 && Objects.equals(value[i2], v)) {
                return true;
            }
            i = i2;
        }
    }

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunction
    public V getOrDefault(long k, V defaultValue) {
        long curr;
        if (k == 0) {
            return this.containsNullKey ? this.value[this.n] : defaultValue;
        }
        long[] key = this.key;
        int mix = ((int) HashCommon.mix(k)) & this.mask;
        int pos = mix;
        long curr2 = key[mix];
        if (curr2 == 0) {
            return defaultValue;
        }
        if (k == curr2) {
            return this.value[pos];
        }
        do {
            int i = (pos + 1) & this.mask;
            pos = i;
            curr = key[i];
            if (curr == 0) {
                return defaultValue;
            }
        } while (k != curr);
        return this.value[pos];
    }

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap
    public V putIfAbsent(long k, V v) {
        int pos = find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        insert((-pos) - 1, k, v);
        return this.defRetValue;
    }

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap
    public boolean remove(long k, Object v) {
        if (k == 0) {
            if (!this.containsNullKey || !Objects.equals(v, this.value[this.n])) {
                return false;
            }
            removeNullEntry();
            return true;
        }
        long[] key = this.key;
        int mix = ((int) HashCommon.mix(k)) & this.mask;
        int pos = mix;
        long curr = key[mix];
        if (curr == 0) {
            return false;
        }
        if (k == curr && Objects.equals(v, this.value[pos])) {
            removeEntry(pos);
            return true;
        }
        while (true) {
            int i = (pos + 1) & this.mask;
            pos = i;
            long curr2 = key[i];
            if (curr2 == 0) {
                return false;
            }
            if (k == curr2 && Objects.equals(v, this.value[pos])) {
                removeEntry(pos);
                return true;
            }
        }
    }

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap
    public boolean replace(long k, V oldValue, V v) {
        int pos = find(k);
        if (pos < 0 || !Objects.equals(oldValue, this.value[pos])) {
            return false;
        }
        this.value[pos] = v;
        return true;
    }

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap
    public V replace(long k, V v) {
        int pos = find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        V oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap
    public V computeIfAbsent(long k, LongFunction<? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        int pos = find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        V newValue = mappingFunction.apply(k);
        insert((-pos) - 1, k, newValue);
        return newValue;
    }

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap
    public V computeIfAbsent(long key, Long2ObjectFunction<? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        int pos = find(key);
        if (pos >= 0) {
            return this.value[pos];
        }
        if (!mappingFunction.containsKey(key)) {
            return this.defRetValue;
        }
        V newValue = mappingFunction.get(key);
        insert((-pos) - 1, key, newValue);
        return newValue;
    }

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap
    public V computeIfPresent(long j, BiFunction<? super Long, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        int find = find(j);
        if (find >= 0 && this.value[find] != null) {
            V apply = biFunction.apply(Long.valueOf(j), this.value[find]);
            if (apply == null) {
                if (j == 0) {
                    removeNullEntry();
                } else {
                    removeEntry(find);
                }
                return this.defRetValue;
            }
            this.value[find] = apply;
            return apply;
        }
        return this.defRetValue;
    }

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap
    public V compute(long j, BiFunction<? super Long, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        int find = find(j);
        V apply = biFunction.apply(Long.valueOf(j), find >= 0 ? this.value[find] : null);
        if (apply == null) {
            if (find >= 0) {
                if (j == 0) {
                    removeNullEntry();
                } else {
                    removeEntry(find);
                }
            }
            return this.defRetValue;
        }
        if (find < 0) {
            insert((-find) - 1, j, apply);
            return apply;
        }
        this.value[find] = apply;
        return apply;
    }

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap
    public V merge(long j, V v, BiFunction<? super V, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        Objects.requireNonNull(v);
        int find = find(j);
        if (find < 0 || this.value[find] == null) {
            if (find < 0) {
                insert((-find) - 1, j, v);
            } else {
                this.value[find] = v;
            }
            return v;
        }
        V apply = biFunction.apply(this.value[find], v);
        if (apply == null) {
            if (j == 0) {
                removeNullEntry();
            } else {
                removeEntry(find);
            }
            return this.defRetValue;
        }
        this.value[find] = apply;
        return apply;
    }

    @Override // it.unimi.dsi.fastutil.Function, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
    public void clear() {
        if (this.size == 0) {
            return;
        }
        this.size = 0;
        this.containsNullKey = false;
        Arrays.fill(this.key, 0L);
        Arrays.fill(this.value, (Object) null);
        this.last = -1;
        this.first = -1;
    }

    @Override // it.unimi.dsi.fastutil.Function, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
    public int size() {
        return this.size;
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLong2ObjectMap, java.util.Map
    public boolean isEmpty() {
        return this.size == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public final class MapEntry implements Long2ObjectMap.Entry<V>, Map.Entry<Long, V>, LongObjectPair<V> {
        int index;

        @Override // it.unimi.dsi.fastutil.Pair
        public /* bridge */ /* synthetic */ Pair right(Object obj) {
            return right((MapEntry) obj);
        }

        MapEntry(int index) {
            this.index = index;
        }

        MapEntry() {
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap.Entry
        public long getLongKey() {
            return Long2ObjectLinkedOpenHashMap.this.key[this.index];
        }

        @Override // it.unimi.dsi.fastutil.longs.LongObjectPair
        public long leftLong() {
            return Long2ObjectLinkedOpenHashMap.this.key[this.index];
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            return Long2ObjectLinkedOpenHashMap.this.value[this.index];
        }

        @Override // it.unimi.dsi.fastutil.Pair
        public V right() {
            return Long2ObjectLinkedOpenHashMap.this.value[this.index];
        }

        @Override // java.util.Map.Entry
        public V setValue(V v) {
            V oldValue = Long2ObjectLinkedOpenHashMap.this.value[this.index];
            Long2ObjectLinkedOpenHashMap.this.value[this.index] = v;
            return oldValue;
        }

        @Override // it.unimi.dsi.fastutil.Pair
        public LongObjectPair<V> right(V v) {
            Long2ObjectLinkedOpenHashMap.this.value[this.index] = v;
            return this;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap.Entry, java.util.Map.Entry
        @Deprecated
        public Long getKey() {
            return Long.valueOf(Long2ObjectLinkedOpenHashMap.this.key[this.index]);
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<Long, V> e = (Map.Entry) o;
            return Long2ObjectLinkedOpenHashMap.this.key[this.index] == e.getKey().longValue() && Objects.equals(Long2ObjectLinkedOpenHashMap.this.value[this.index], e.getValue());
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return HashCommon.long2int(Long2ObjectLinkedOpenHashMap.this.key[this.index]) ^ (Long2ObjectLinkedOpenHashMap.this.value[this.index] == null ? 0 : Long2ObjectLinkedOpenHashMap.this.value[this.index].hashCode());
        }

        public String toString() {
            return Long2ObjectLinkedOpenHashMap.this.key[this.index] + "=>" + Long2ObjectLinkedOpenHashMap.this.value[this.index];
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

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap
    public long firstLongKey() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.first];
    }

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap
    public long lastLongKey() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.last];
    }

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap
    public Long2ObjectSortedMap<V> tailMap(long from) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap
    public Long2ObjectSortedMap<V> headMap(long to) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap
    public Long2ObjectSortedMap<V> subMap(long from, long to) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap, java.util.SortedMap
    /* renamed from: comparator */
    public Comparator<? super Long> comparator2() {
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
            this.next = Long2ObjectLinkedOpenHashMap.this.first;
            this.index = 0;
        }

        private MapIterator(long from) {
            this.prev = -1;
            this.next = -1;
            this.curr = -1;
            this.index = -1;
            if (from == 0) {
                if (Long2ObjectLinkedOpenHashMap.this.containsNullKey) {
                    this.next = (int) Long2ObjectLinkedOpenHashMap.this.link[Long2ObjectLinkedOpenHashMap.this.n];
                    this.prev = Long2ObjectLinkedOpenHashMap.this.n;
                    return;
                }
                throw new NoSuchElementException("The key " + from + " does not belong to this map.");
            }
            if (Long2ObjectLinkedOpenHashMap.this.key[Long2ObjectLinkedOpenHashMap.this.last] == from) {
                this.prev = Long2ObjectLinkedOpenHashMap.this.last;
                this.index = Long2ObjectLinkedOpenHashMap.this.size;
                return;
            }
            int pos = ((int) HashCommon.mix(from)) & Long2ObjectLinkedOpenHashMap.this.mask;
            while (Long2ObjectLinkedOpenHashMap.this.key[pos] != 0) {
                if (Long2ObjectLinkedOpenHashMap.this.key[pos] == from) {
                    this.next = (int) Long2ObjectLinkedOpenHashMap.this.link[pos];
                    this.prev = pos;
                    return;
                }
                pos = (pos + 1) & Long2ObjectLinkedOpenHashMap.this.mask;
            }
            throw new NoSuchElementException("The key " + from + " does not belong to this map.");
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
                this.index = Long2ObjectLinkedOpenHashMap.this.size;
                return;
            }
            int pos = Long2ObjectLinkedOpenHashMap.this.first;
            this.index = 1;
            while (pos != this.prev) {
                pos = (int) Long2ObjectLinkedOpenHashMap.this.link[pos];
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
            this.next = (int) Long2ObjectLinkedOpenHashMap.this.link[this.curr];
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
            this.prev = (int) (Long2ObjectLinkedOpenHashMap.this.link[this.curr] >>> 32);
            this.next = this.curr;
            if (this.index >= 0) {
                this.index--;
            }
            return this.curr;
        }

        public void forEachRemaining(ConsumerType action) {
            while (hasNext()) {
                this.curr = this.next;
                this.next = (int) Long2ObjectLinkedOpenHashMap.this.link[this.curr];
                this.prev = this.curr;
                if (this.index >= 0) {
                    this.index++;
                }
                acceptOnIndex(action, this.curr);
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:26:0x00db, code lost:            r1[r3] = r4;        r13.this$0.value[r3] = r13.this$0.value[r0];     */
        /* JADX WARN: Code restructure failed: missing block: B:27:0x00eb, code lost:            if (r13.next != r0) goto L37;     */
        /* JADX WARN: Code restructure failed: missing block: B:28:0x00ed, code lost:            r13.next = r3;     */
        /* JADX WARN: Code restructure failed: missing block: B:30:0x00f1, code lost:            if (r13.prev != r0) goto L46;     */
        /* JADX WARN: Code restructure failed: missing block: B:31:0x00f3, code lost:            r13.prev = r3;     */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void remove() {
            /*
                Method dump skipped, instructions count: 266
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap.MapIterator.remove():void");
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

        public void set(Long2ObjectMap.Entry<V> ok) {
            throw new UnsupportedOperationException();
        }

        public void add(Long2ObjectMap.Entry<V> ok) {
            throw new UnsupportedOperationException();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class EntryIterator extends Long2ObjectLinkedOpenHashMap<V>.MapIterator<Consumer<? super Long2ObjectMap.Entry<V>>> implements ObjectListIterator<Long2ObjectMap.Entry<V>> {
        private Long2ObjectLinkedOpenHashMap<V>.MapEntry entry;

        @Override // it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.ListIterator
        public /* bridge */ /* synthetic */ void add(Object obj) {
            super.add((Long2ObjectMap.Entry) obj);
        }

        @Override // java.util.Iterator
        public /* bridge */ /* synthetic */ void forEachRemaining(Consumer consumer) {
            super.forEachRemaining((EntryIterator) consumer);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.ListIterator
        public /* bridge */ /* synthetic */ void set(Object obj) {
            super.set((Long2ObjectMap.Entry) obj);
        }

        public EntryIterator() {
            super();
        }

        public EntryIterator(long from) {
            super(from);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap.MapIterator
        public final void acceptOnIndex(Consumer<? super Long2ObjectMap.Entry<V>> action, int index) {
            action.accept(new MapEntry(index));
        }

        @Override // java.util.Iterator, java.util.ListIterator
        public Long2ObjectLinkedOpenHashMap<V>.MapEntry next() {
            Long2ObjectLinkedOpenHashMap<V>.MapEntry mapEntry = new MapEntry(nextEntry());
            this.entry = mapEntry;
            return mapEntry;
        }

        @Override // it.unimi.dsi.fastutil.BidirectionalIterator, java.util.ListIterator
        public Long2ObjectLinkedOpenHashMap<V>.MapEntry previous() {
            Long2ObjectLinkedOpenHashMap<V>.MapEntry mapEntry = new MapEntry(previousEntry());
            this.entry = mapEntry;
            return mapEntry;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap.MapIterator, it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.Iterator, java.util.ListIterator
        public void remove() {
            super.remove();
            this.entry.index = -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class FastEntryIterator extends Long2ObjectLinkedOpenHashMap<V>.MapIterator<Consumer<? super Long2ObjectMap.Entry<V>>> implements ObjectListIterator<Long2ObjectMap.Entry<V>> {
        final Long2ObjectLinkedOpenHashMap<V>.MapEntry entry;

        @Override // it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.ListIterator
        public /* bridge */ /* synthetic */ void add(Object obj) {
            super.add((Long2ObjectMap.Entry) obj);
        }

        @Override // java.util.Iterator
        public /* bridge */ /* synthetic */ void forEachRemaining(Consumer consumer) {
            super.forEachRemaining((FastEntryIterator) consumer);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.ListIterator
        public /* bridge */ /* synthetic */ void set(Object obj) {
            super.set((Long2ObjectMap.Entry) obj);
        }

        public FastEntryIterator() {
            super();
            this.entry = new MapEntry();
        }

        public FastEntryIterator(long from) {
            super(from);
            this.entry = new MapEntry();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap.MapIterator
        public final void acceptOnIndex(Consumer<? super Long2ObjectMap.Entry<V>> action, int index) {
            this.entry.index = index;
            action.accept(this.entry);
        }

        @Override // java.util.Iterator, java.util.ListIterator
        public Long2ObjectLinkedOpenHashMap<V>.MapEntry next() {
            this.entry.index = nextEntry();
            return this.entry;
        }

        @Override // it.unimi.dsi.fastutil.BidirectionalIterator, java.util.ListIterator
        public Long2ObjectLinkedOpenHashMap<V>.MapEntry previous() {
            this.entry.index = previousEntry();
            return this.entry;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class MapEntrySet extends AbstractObjectSortedSet<Long2ObjectMap.Entry<V>> implements Long2ObjectSortedMap.FastSortedEntrySet<V> {
        private static final int SPLITERATOR_CHARACTERISTICS = 81;

        private MapEntrySet() {
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet, it.unimi.dsi.fastutil.objects.AbstractObjectSet, it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        public ObjectBidirectionalIterator<Long2ObjectMap.Entry<V>> iterator() {
            return new EntryIterator();
        }

        @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        /* renamed from: spliterator */
        public ObjectSpliterator<Long2ObjectMap.Entry<V>> mo221spliterator() {
            return ObjectSpliterators.asSpliterator(iterator(), Size64.sizeOf(Long2ObjectLinkedOpenHashMap.this), 81);
        }

        @Override // java.util.SortedSet
        public Comparator<? super Long2ObjectMap.Entry<V>> comparator() {
            return null;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public ObjectSortedSet<Long2ObjectMap.Entry<V>> subSet(Long2ObjectMap.Entry<V> fromElement, Long2ObjectMap.Entry<V> toElement) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public ObjectSortedSet<Long2ObjectMap.Entry<V>> headSet(Long2ObjectMap.Entry<V> toElement) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public ObjectSortedSet<Long2ObjectMap.Entry<V>> tailSet(Long2ObjectMap.Entry<V> fromElement) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.SortedSet
        public Long2ObjectMap.Entry<V> first() {
            if (Long2ObjectLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return new MapEntry(Long2ObjectLinkedOpenHashMap.this.first);
        }

        @Override // java.util.SortedSet
        public Long2ObjectMap.Entry<V> last() {
            if (Long2ObjectLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return new MapEntry(Long2ObjectLinkedOpenHashMap.this.last);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            long curr;
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> e = (Map.Entry) o;
            if (e.getKey() == null || !(e.getKey() instanceof Long)) {
                return false;
            }
            long k = ((Long) e.getKey()).longValue();
            Object value = e.getValue();
            if (k == 0) {
                return Long2ObjectLinkedOpenHashMap.this.containsNullKey && Objects.equals(Long2ObjectLinkedOpenHashMap.this.value[Long2ObjectLinkedOpenHashMap.this.n], value);
            }
            long[] key = Long2ObjectLinkedOpenHashMap.this.key;
            int mix = ((int) HashCommon.mix(k)) & Long2ObjectLinkedOpenHashMap.this.mask;
            int pos = mix;
            long curr2 = key[mix];
            if (curr2 == 0) {
                return false;
            }
            if (k == curr2) {
                return Objects.equals(Long2ObjectLinkedOpenHashMap.this.value[pos], value);
            }
            do {
                int i = (pos + 1) & Long2ObjectLinkedOpenHashMap.this.mask;
                pos = i;
                curr = key[i];
                if (curr == 0) {
                    return false;
                }
            } while (k != curr);
            return Objects.equals(Long2ObjectLinkedOpenHashMap.this.value[pos], value);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> e = (Map.Entry) o;
            if (e.getKey() == null || !(e.getKey() instanceof Long)) {
                return false;
            }
            long k = ((Long) e.getKey()).longValue();
            Object value = e.getValue();
            if (k == 0) {
                if (!Long2ObjectLinkedOpenHashMap.this.containsNullKey || !Objects.equals(Long2ObjectLinkedOpenHashMap.this.value[Long2ObjectLinkedOpenHashMap.this.n], value)) {
                    return false;
                }
                Long2ObjectLinkedOpenHashMap.this.removeNullEntry();
                return true;
            }
            long[] key = Long2ObjectLinkedOpenHashMap.this.key;
            int mix = ((int) HashCommon.mix(k)) & Long2ObjectLinkedOpenHashMap.this.mask;
            int pos = mix;
            long curr = key[mix];
            if (curr == 0) {
                return false;
            }
            if (curr == k) {
                if (!Objects.equals(Long2ObjectLinkedOpenHashMap.this.value[pos], value)) {
                    return false;
                }
                Long2ObjectLinkedOpenHashMap.this.removeEntry(pos);
                return true;
            }
            while (true) {
                int i = (pos + 1) & Long2ObjectLinkedOpenHashMap.this.mask;
                pos = i;
                long curr2 = key[i];
                if (curr2 == 0) {
                    return false;
                }
                if (curr2 == k && Objects.equals(Long2ObjectLinkedOpenHashMap.this.value[pos], value)) {
                    Long2ObjectLinkedOpenHashMap.this.removeEntry(pos);
                    return true;
                }
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return Long2ObjectLinkedOpenHashMap.this.size;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            Long2ObjectLinkedOpenHashMap.this.clear();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet
        public ObjectListIterator<Long2ObjectMap.Entry<V>> iterator(Long2ObjectMap.Entry<V> from) {
            return new EntryIterator(from.getLongKey());
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap.FastSortedEntrySet, it.unimi.dsi.fastutil.longs.Long2ObjectMap.FastEntrySet
        public ObjectListIterator<Long2ObjectMap.Entry<V>> fastIterator() {
            return new FastEntryIterator();
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap.FastSortedEntrySet
        public ObjectListIterator<Long2ObjectMap.Entry<V>> fastIterator(Long2ObjectMap.Entry<V> from) {
            return new FastEntryIterator(from.getLongKey());
        }

        @Override // java.lang.Iterable
        public void forEach(Consumer<? super Long2ObjectMap.Entry<V>> consumer) {
            int curr = Long2ObjectLinkedOpenHashMap.this.size;
            int next = Long2ObjectLinkedOpenHashMap.this.first;
            while (true) {
                int i = curr - 1;
                if (curr != 0) {
                    int curr2 = next;
                    next = (int) Long2ObjectLinkedOpenHashMap.this.link[curr2];
                    consumer.accept(new MapEntry(curr2));
                    curr = i;
                } else {
                    return;
                }
            }
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap.FastEntrySet
        public void fastForEach(Consumer<? super Long2ObjectMap.Entry<V>> consumer) {
            Long2ObjectLinkedOpenHashMap<V>.MapEntry entry = new MapEntry();
            int i = Long2ObjectLinkedOpenHashMap.this.size;
            int next = Long2ObjectLinkedOpenHashMap.this.first;
            while (true) {
                int i2 = i - 1;
                if (i != 0) {
                    entry.index = next;
                    next = (int) Long2ObjectLinkedOpenHashMap.this.link[next];
                    consumer.accept(entry);
                    i = i2;
                } else {
                    return;
                }
            }
        }
    }

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap, it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap
    public Long2ObjectSortedMap.FastSortedEntrySet<V> long2ObjectEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet();
        }
        return this.entries;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class KeyIterator extends Long2ObjectLinkedOpenHashMap<V>.MapIterator<java.util.function.LongConsumer> implements LongListIterator {
        @Override // java.util.PrimitiveIterator.OfLong
        public /* bridge */ /* synthetic */ void forEachRemaining(java.util.function.LongConsumer longConsumer) {
            super.forEachRemaining((KeyIterator) longConsumer);
        }

        public KeyIterator(long k) {
            super(k);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongBidirectionalIterator
        public long previousLong() {
            return Long2ObjectLinkedOpenHashMap.this.key[previousEntry()];
        }

        public KeyIterator() {
            super();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap.MapIterator
        public final void acceptOnIndex(java.util.function.LongConsumer action, int index) {
            action.accept(Long2ObjectLinkedOpenHashMap.this.key[index]);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong
        public long nextLong() {
            return Long2ObjectLinkedOpenHashMap.this.key[nextEntry()];
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class KeySet extends AbstractLongSortedSet {
        private static final int SPLITERATOR_CHARACTERISTICS = 337;

        private KeySet() {
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public LongListIterator iterator(long from) {
            return new KeyIterator(from);
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongSortedSet, it.unimi.dsi.fastutil.longs.AbstractLongSet, it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
        public LongListIterator iterator() {
            return new KeyIterator();
        }

        @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
        public LongSpliterator spliterator() {
            return LongSpliterators.asSpliterator(iterator(), Size64.sizeOf(Long2ObjectLinkedOpenHashMap.this), SPLITERATOR_CHARACTERISTICS);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongIterable
        public void forEach(java.util.function.LongConsumer consumer) {
            int curr = Long2ObjectLinkedOpenHashMap.this.size;
            int next = Long2ObjectLinkedOpenHashMap.this.first;
            while (true) {
                int i = curr - 1;
                if (curr != 0) {
                    int curr2 = next;
                    next = (int) Long2ObjectLinkedOpenHashMap.this.link[curr2];
                    consumer.accept(Long2ObjectLinkedOpenHashMap.this.key[curr2]);
                    curr = i;
                } else {
                    return;
                }
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return Long2ObjectLinkedOpenHashMap.this.size;
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public boolean contains(long k) {
            return Long2ObjectLinkedOpenHashMap.this.containsKey(k);
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongSet, it.unimi.dsi.fastutil.longs.LongSet
        public boolean remove(long k) {
            int oldSize = Long2ObjectLinkedOpenHashMap.this.size;
            Long2ObjectLinkedOpenHashMap.this.remove(k);
            return Long2ObjectLinkedOpenHashMap.this.size != oldSize;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            Long2ObjectLinkedOpenHashMap.this.clear();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public long firstLong() {
            if (Long2ObjectLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return Long2ObjectLinkedOpenHashMap.this.key[Long2ObjectLinkedOpenHashMap.this.first];
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public long lastLong() {
            if (Long2ObjectLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return Long2ObjectLinkedOpenHashMap.this.key[Long2ObjectLinkedOpenHashMap.this.last];
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet, java.util.SortedSet
        /* renamed from: comparator */
        public Comparator<? super Long> comparator2() {
            return null;
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
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLong2ObjectSortedMap, it.unimi.dsi.fastutil.longs.AbstractLong2ObjectMap, it.unimi.dsi.fastutil.longs.Long2ObjectMap, java.util.Map
    /* renamed from: keySet, reason: avoid collision after fix types in other method */
    public Set<Long> keySet2() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class ValueIterator extends Long2ObjectLinkedOpenHashMap<V>.MapIterator<Consumer<? super V>> implements ObjectListIterator<V> {
        @Override // java.util.Iterator
        public /* bridge */ /* synthetic */ void forEachRemaining(Consumer consumer) {
            super.forEachRemaining((ValueIterator) consumer);
        }

        @Override // it.unimi.dsi.fastutil.BidirectionalIterator, java.util.ListIterator
        public V previous() {
            return Long2ObjectLinkedOpenHashMap.this.value[previousEntry()];
        }

        public ValueIterator() {
            super();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap.MapIterator
        public final void acceptOnIndex(Consumer<? super V> consumer, int i) {
            consumer.accept(Long2ObjectLinkedOpenHashMap.this.value[i]);
        }

        @Override // java.util.Iterator, java.util.ListIterator
        public V next() {
            return Long2ObjectLinkedOpenHashMap.this.value[nextEntry()];
        }
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLong2ObjectSortedMap, it.unimi.dsi.fastutil.longs.AbstractLong2ObjectMap, it.unimi.dsi.fastutil.longs.Long2ObjectMap, java.util.Map
    public ObjectCollection<V> values() {
        if (this.values == null) {
            this.values = new AbstractObjectCollection<V>() { // from class: it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap.1
                private static final int SPLITERATOR_CHARACTERISTICS = 80;

                @Override // it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
                public ObjectIterator<V> iterator() {
                    return new ValueIterator();
                }

                @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
                /* renamed from: spliterator */
                public ObjectSpliterator<V> mo221spliterator() {
                    return ObjectSpliterators.asSpliterator(iterator(), Size64.sizeOf(Long2ObjectLinkedOpenHashMap.this), 80);
                }

                @Override // java.lang.Iterable
                public void forEach(Consumer<? super V> consumer) {
                    int i = Long2ObjectLinkedOpenHashMap.this.size;
                    int i2 = Long2ObjectLinkedOpenHashMap.this.first;
                    while (true) {
                        int i3 = i - 1;
                        if (i != 0) {
                            int i4 = i2;
                            i2 = (int) Long2ObjectLinkedOpenHashMap.this.link[i4];
                            consumer.accept(Long2ObjectLinkedOpenHashMap.this.value[i4]);
                            i = i3;
                        } else {
                            return;
                        }
                    }
                }

                @Override // java.util.AbstractCollection, java.util.Collection
                public int size() {
                    return Long2ObjectLinkedOpenHashMap.this.size;
                }

                @Override // java.util.AbstractCollection, java.util.Collection
                public boolean contains(Object v) {
                    return Long2ObjectLinkedOpenHashMap.this.containsValue(v);
                }

                @Override // java.util.AbstractCollection, java.util.Collection
                public void clear() {
                    Long2ObjectLinkedOpenHashMap.this.clear();
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
        long[] jArr;
        V[] vArr;
        long[] jArr2;
        Object[] objArr;
        int i2;
        long[] jArr3 = this.key;
        V[] vArr2 = this.value;
        int i3 = i - 1;
        long[] jArr4 = new long[i + 1];
        Object[] objArr2 = new Object[i + 1];
        int i4 = this.first;
        int i5 = -1;
        int i6 = -1;
        long[] jArr5 = this.link;
        long[] jArr6 = new long[i + 1];
        this.first = -1;
        int i7 = this.size;
        while (true) {
            int i8 = i7 - 1;
            if (i7 == 0) {
                break;
            }
            if (jArr3[i4] == 0) {
                mix = i;
            } else {
                mix = ((int) HashCommon.mix(jArr3[i4])) & i3;
                while (jArr4[mix] != 0) {
                    mix = (mix + 1) & i3;
                }
            }
            jArr4[mix] = jArr3[i4];
            objArr2[mix] = vArr2[i4];
            if (i5 != -1) {
                jArr = jArr3;
                vArr = vArr2;
                jArr6[i6] = jArr6[i6] ^ ((jArr6[i6] ^ (mix & 4294967295L)) & 4294967295L);
                jArr2 = jArr4;
                objArr = objArr2;
                jArr6[mix] = jArr6[mix] ^ ((jArr6[mix] ^ ((i6 & 4294967295L) << 32)) & (-4294967296L));
                i2 = mix;
            } else {
                jArr = jArr3;
                vArr = vArr2;
                jArr2 = jArr4;
                objArr = objArr2;
                this.first = mix;
                i2 = mix;
                jArr6[mix] = -1;
            }
            i6 = i2;
            int i9 = i4;
            i4 = (int) jArr5[i4];
            i5 = i9;
            jArr3 = jArr;
            i7 = i8;
            jArr4 = jArr2;
            objArr2 = objArr;
            vArr2 = vArr;
        }
        long[] jArr7 = jArr4;
        V[] vArr3 = (V[]) objArr2;
        this.link = jArr6;
        this.last = i6;
        if (i6 != -1) {
            jArr6[i6] = jArr6[i6] | 4294967295L;
        }
        this.n = i;
        this.mask = i3;
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.key = jArr7;
        this.value = vArr3;
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public Long2ObjectLinkedOpenHashMap<V> m240clone() {
        try {
            Long2ObjectLinkedOpenHashMap<V> long2ObjectLinkedOpenHashMap = (Long2ObjectLinkedOpenHashMap) super.clone();
            long2ObjectLinkedOpenHashMap.keys = null;
            long2ObjectLinkedOpenHashMap.values = null;
            long2ObjectLinkedOpenHashMap.entries = null;
            long2ObjectLinkedOpenHashMap.containsNullKey = this.containsNullKey;
            long2ObjectLinkedOpenHashMap.key = (long[]) this.key.clone();
            long2ObjectLinkedOpenHashMap.value = (V[]) ((Object[]) this.value.clone());
            long2ObjectLinkedOpenHashMap.link = (long[]) this.link.clone();
            return long2ObjectLinkedOpenHashMap;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLong2ObjectMap, java.util.Map
    public int hashCode() {
        int h = 0;
        int j = realSize();
        int i = 0;
        while (true) {
            int j2 = j - 1;
            if (j == 0) {
                break;
            }
            while (this.key[i] == 0) {
                i++;
            }
            int t = HashCommon.long2int(this.key[i]);
            if (this != this.value[i]) {
                t ^= this.value[i] != null ? this.value[i].hashCode() : 0;
            }
            h += t;
            i++;
            j = j2;
        }
        if (this.containsNullKey) {
            return h + (this.value[this.n] != null ? this.value[this.n].hashCode() : 0);
        }
        return h;
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        long[] key = this.key;
        V[] value = this.value;
        Long2ObjectLinkedOpenHashMap<V>.EntryIterator i = new EntryIterator();
        s.defaultWriteObject();
        int e = this.size;
        while (true) {
            int j = e - 1;
            if (e != 0) {
                int e2 = i.nextEntry();
                s.writeLong(key[e2]);
                s.writeObject(value[e2]);
                e = j;
            } else {
                return;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v3, types: [V[], java.lang.Object[]] */
    /* JADX WARN: Type inference failed for: r3v4 */
    /* JADX WARN: Type inference failed for: r3v5 */
    /* JADX WARN: Type inference failed for: r3v6 */
    /* JADX WARN: Type inference failed for: r3v7 */
    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        int i;
        objectInputStream.defaultReadObject();
        this.n = HashCommon.arraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        boolean z = true;
        this.mask = this.n - 1;
        long[] jArr = new long[this.n + 1];
        this.key = jArr;
        Object obj = (V[]) new Object[this.n + 1];
        this.value = obj;
        long[] jArr2 = new long[this.n + 1];
        this.link = jArr2;
        int i2 = -1;
        int i3 = -1;
        this.last = -1;
        this.first = -1;
        int i4 = this.size;
        while (true) {
            int i5 = i4 - 1;
            if (i4 == 0) {
                break;
            }
            long readLong = objectInputStream.readLong();
            Object readObject = objectInputStream.readObject();
            if (readLong != 0) {
                int mix = ((int) HashCommon.mix(readLong)) & this.mask;
                while (jArr[mix] != 0) {
                    mix = (mix + 1) & this.mask;
                }
                i = mix;
            } else {
                i = this.n;
                this.containsNullKey = z;
            }
            jArr[i] = readLong;
            obj[i] = readObject;
            if (this.first == i3) {
                this.first = i;
                i2 = i;
                jArr2[i] = jArr2[i] | (-4294967296L);
                i4 = i5;
                obj = (V[]) obj;
                z = true;
                i3 = -1;
            } else {
                jArr2[i2] = jArr2[i2] ^ ((jArr2[i2] ^ (i & 4294967295L)) & 4294967295L);
                jArr2[i] = ((jArr2[i] ^ ((i2 & 4294967295L) << 32)) & (-4294967296L)) ^ jArr2[i];
                i2 = i;
                i4 = i5;
                obj = obj;
                z = true;
                i3 = -1;
            }
        }
        this.last = i2;
        if (i2 != -1) {
            jArr2[i2] = jArr2[i2] | 4294967295L;
        }
    }

    private void checkTable() {
    }
}
