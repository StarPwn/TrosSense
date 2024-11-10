package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntHash;
import it.unimi.dsi.fastutil.objects.AbstractObjectCollection;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSpliterator;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.IntFunction;

/* loaded from: classes4.dex */
public class Int2ObjectOpenCustomHashMap<V> extends AbstractInt2ObjectMap<V> implements Serializable, Cloneable, Hash {
    private static final boolean ASSERTS = false;
    private static final long serialVersionUID = 0;
    protected transient boolean containsNullKey;
    protected transient Int2ObjectMap.FastEntrySet<V> entries;
    protected final float f;
    protected transient int[] key;
    protected transient IntSet keys;
    protected transient int mask;
    protected transient int maxFill;
    protected final transient int minN;
    protected transient int n;
    protected int size;
    protected IntHash.Strategy strategy;
    protected transient V[] value;
    protected transient ObjectCollection<V> values;

    public Int2ObjectOpenCustomHashMap(int i, float f, IntHash.Strategy strategy) {
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
        this.key = new int[this.n + 1];
        this.value = (V[]) new Object[this.n + 1];
    }

    public Int2ObjectOpenCustomHashMap(int expected, IntHash.Strategy strategy) {
        this(expected, 0.75f, strategy);
    }

    public Int2ObjectOpenCustomHashMap(IntHash.Strategy strategy) {
        this(16, 0.75f, strategy);
    }

    public Int2ObjectOpenCustomHashMap(Map<? extends Integer, ? extends V> m, float f, IntHash.Strategy strategy) {
        this(m.size(), f, strategy);
        putAll(m);
    }

    public Int2ObjectOpenCustomHashMap(Map<? extends Integer, ? extends V> m, IntHash.Strategy strategy) {
        this(m, 0.75f, strategy);
    }

    public Int2ObjectOpenCustomHashMap(Int2ObjectMap<V> m, float f, IntHash.Strategy strategy) {
        this(m.size(), f, strategy);
        putAll(m);
    }

    public Int2ObjectOpenCustomHashMap(Int2ObjectMap<V> m, IntHash.Strategy strategy) {
        this((Int2ObjectMap) m, 0.75f, strategy);
    }

    public Int2ObjectOpenCustomHashMap(int[] k, V[] v, float f, IntHash.Strategy strategy) {
        this(k.length, f, strategy);
        if (k.length != v.length) {
            throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
        }
        for (int i = 0; i < k.length; i++) {
            put(k[i], (int) v[i]);
        }
    }

    public Int2ObjectOpenCustomHashMap(int[] k, V[] v, IntHash.Strategy strategy) {
        this(k, v, 0.75f, strategy);
    }

    public IntHash.Strategy strategy() {
        return this.strategy;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int realSize() {
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
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            rehash(this.n / 2);
        }
        return oldValue;
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap, java.util.Map
    public void putAll(Map<? extends Integer, ? extends V> m) {
        if (this.f <= 0.5d) {
            ensureCapacity(m.size());
        } else {
            tryCapacity(size() + m.size());
        }
        super.putAll(m);
    }

    private int find(int k) {
        int curr;
        if (this.strategy.equals(k, 0)) {
            return this.containsNullKey ? this.n : -(this.n + 1);
        }
        int[] key = this.key;
        int mix = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
        int pos = mix;
        int curr2 = key[mix];
        if (curr2 == 0) {
            return -(pos + 1);
        }
        if (this.strategy.equals(k, curr2)) {
            return pos;
        }
        do {
            int i = (pos + 1) & this.mask;
            pos = i;
            curr = key[i];
            if (curr == 0) {
                return -(pos + 1);
            }
        } while (!this.strategy.equals(k, curr));
        return pos;
    }

    private void insert(int pos, int k, V v) {
        if (pos == this.n) {
            this.containsNullKey = true;
        }
        this.key[pos] = k;
        this.value[pos] = v;
        int i = this.size;
        this.size = i + 1;
        if (i >= this.maxFill) {
            rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
    public V put(int k, V v) {
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
        int curr;
        int[] key = this.key;
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
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
    public V remove(int k) {
        int curr;
        if (this.strategy.equals(k, 0)) {
            return this.containsNullKey ? removeNullEntry() : this.defRetValue;
        }
        int[] key = this.key;
        int mix = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
        int pos = mix;
        int curr2 = key[mix];
        if (curr2 == 0) {
            return this.defRetValue;
        }
        if (this.strategy.equals(k, curr2)) {
            return removeEntry(pos);
        }
        do {
            int i = (pos + 1) & this.mask;
            pos = i;
            curr = key[i];
            if (curr == 0) {
                return this.defRetValue;
            }
        } while (!this.strategy.equals(k, curr));
        return removeEntry(pos);
    }

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
    public V get(int k) {
        int curr;
        if (this.strategy.equals(k, 0)) {
            return this.containsNullKey ? this.value[this.n] : this.defRetValue;
        }
        int[] key = this.key;
        int mix = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
        int pos = mix;
        int curr2 = key[mix];
        if (curr2 == 0) {
            return this.defRetValue;
        }
        if (this.strategy.equals(k, curr2)) {
            return this.value[pos];
        }
        do {
            int i = (pos + 1) & this.mask;
            pos = i;
            curr = key[i];
            if (curr == 0) {
                return this.defRetValue;
            }
        } while (!this.strategy.equals(k, curr));
        return this.value[pos];
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap, it.unimi.dsi.fastutil.ints.Int2ObjectFunction, it.unimi.dsi.fastutil.ints.Int2ObjectMap
    public boolean containsKey(int k) {
        int curr;
        if (this.strategy.equals(k, 0)) {
            return this.containsNullKey;
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

    @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap, java.util.Map
    public boolean containsValue(Object v) {
        V[] value = this.value;
        int[] key = this.key;
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

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
    public V getOrDefault(int k, V defaultValue) {
        int curr;
        if (this.strategy.equals(k, 0)) {
            return this.containsNullKey ? this.value[this.n] : defaultValue;
        }
        int[] key = this.key;
        int mix = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
        int pos = mix;
        int curr2 = key[mix];
        if (curr2 == 0) {
            return defaultValue;
        }
        if (this.strategy.equals(k, curr2)) {
            return this.value[pos];
        }
        do {
            int i = (pos + 1) & this.mask;
            pos = i;
            curr = key[i];
            if (curr == 0) {
                return defaultValue;
            }
        } while (!this.strategy.equals(k, curr));
        return this.value[pos];
    }

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap
    public V putIfAbsent(int k, V v) {
        int pos = find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        insert((-pos) - 1, k, v);
        return this.defRetValue;
    }

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap
    public boolean remove(int k, Object v) {
        if (this.strategy.equals(k, 0)) {
            if (!this.containsNullKey || !Objects.equals(v, this.value[this.n])) {
                return false;
            }
            removeNullEntry();
            return true;
        }
        int[] key = this.key;
        int mix = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
        int pos = mix;
        int curr = key[mix];
        if (curr == 0) {
            return false;
        }
        if (this.strategy.equals(k, curr) && Objects.equals(v, this.value[pos])) {
            removeEntry(pos);
            return true;
        }
        while (true) {
            int i = (pos + 1) & this.mask;
            pos = i;
            int curr2 = key[i];
            if (curr2 == 0) {
                return false;
            }
            if (this.strategy.equals(k, curr2) && Objects.equals(v, this.value[pos])) {
                removeEntry(pos);
                return true;
            }
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap
    public boolean replace(int k, V oldValue, V v) {
        int pos = find(k);
        if (pos < 0 || !Objects.equals(oldValue, this.value[pos])) {
            return false;
        }
        this.value[pos] = v;
        return true;
    }

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap
    public V replace(int k, V v) {
        int pos = find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        V oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap
    public V computeIfAbsent(int k, IntFunction<? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        int pos = find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        V newValue = mappingFunction.apply(k);
        insert((-pos) - 1, k, newValue);
        return newValue;
    }

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap
    public V computeIfAbsent(int key, Int2ObjectFunction<? extends V> mappingFunction) {
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

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap
    public V computeIfPresent(int i, BiFunction<? super Integer, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        int find = find(i);
        if (find >= 0 && this.value[find] != null) {
            V apply = biFunction.apply(Integer.valueOf(i), this.value[find]);
            if (apply == null) {
                if (this.strategy.equals(i, 0)) {
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

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap
    public V compute(int i, BiFunction<? super Integer, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        int find = find(i);
        V apply = biFunction.apply(Integer.valueOf(i), find >= 0 ? this.value[find] : null);
        if (apply == null) {
            if (find >= 0) {
                if (this.strategy.equals(i, 0)) {
                    removeNullEntry();
                } else {
                    removeEntry(find);
                }
            }
            return this.defRetValue;
        }
        if (find < 0) {
            insert((-find) - 1, i, apply);
            return apply;
        }
        this.value[find] = apply;
        return apply;
    }

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap
    public V merge(int i, V v, BiFunction<? super V, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        Objects.requireNonNull(v);
        int find = find(i);
        if (find < 0 || this.value[find] == null) {
            if (find < 0) {
                insert((-find) - 1, i, v);
            } else {
                this.value[find] = v;
            }
            return v;
        }
        V apply = biFunction.apply(this.value[find], v);
        if (apply == null) {
            if (this.strategy.equals(i, 0)) {
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
        Arrays.fill(this.key, 0);
        Arrays.fill(this.value, (Object) null);
    }

    @Override // it.unimi.dsi.fastutil.Function, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
    public int size() {
        return this.size;
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap, java.util.Map
    public boolean isEmpty() {
        return this.size == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public final class MapEntry implements Int2ObjectMap.Entry<V>, Map.Entry<Integer, V>, IntObjectPair<V> {
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

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry
        public int getIntKey() {
            return Int2ObjectOpenCustomHashMap.this.key[this.index];
        }

        @Override // it.unimi.dsi.fastutil.ints.IntObjectPair
        public int leftInt() {
            return Int2ObjectOpenCustomHashMap.this.key[this.index];
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            return Int2ObjectOpenCustomHashMap.this.value[this.index];
        }

        @Override // it.unimi.dsi.fastutil.Pair
        public V right() {
            return Int2ObjectOpenCustomHashMap.this.value[this.index];
        }

        @Override // java.util.Map.Entry
        public V setValue(V v) {
            V oldValue = Int2ObjectOpenCustomHashMap.this.value[this.index];
            Int2ObjectOpenCustomHashMap.this.value[this.index] = v;
            return oldValue;
        }

        @Override // it.unimi.dsi.fastutil.Pair
        public IntObjectPair<V> right(V v) {
            Int2ObjectOpenCustomHashMap.this.value[this.index] = v;
            return this;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry, java.util.Map.Entry
        @Deprecated
        public Integer getKey() {
            return Integer.valueOf(Int2ObjectOpenCustomHashMap.this.key[this.index]);
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<Integer, V> e = (Map.Entry) o;
            return Int2ObjectOpenCustomHashMap.this.strategy.equals(Int2ObjectOpenCustomHashMap.this.key[this.index], e.getKey().intValue()) && Objects.equals(Int2ObjectOpenCustomHashMap.this.value[this.index], e.getValue());
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return Int2ObjectOpenCustomHashMap.this.strategy.hashCode(Int2ObjectOpenCustomHashMap.this.key[this.index]) ^ (Int2ObjectOpenCustomHashMap.this.value[this.index] == null ? 0 : Int2ObjectOpenCustomHashMap.this.value[this.index].hashCode());
        }

        public String toString() {
            return Int2ObjectOpenCustomHashMap.this.key[this.index] + "=>" + Int2ObjectOpenCustomHashMap.this.value[this.index];
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public abstract class MapIterator<ConsumerType> {
        int c;
        int last;
        boolean mustReturnNullKey;
        int pos;
        IntArrayList wrapped;

        abstract void acceptOnIndex(ConsumerType consumertype, int i);

        private MapIterator() {
            this.pos = Int2ObjectOpenCustomHashMap.this.n;
            this.last = -1;
            this.c = Int2ObjectOpenCustomHashMap.this.size;
            this.mustReturnNullKey = Int2ObjectOpenCustomHashMap.this.containsNullKey;
        }

        public boolean hasNext() {
            return this.c != 0;
        }

        public int nextEntry() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            this.c--;
            if (this.mustReturnNullKey) {
                this.mustReturnNullKey = false;
                int i = Int2ObjectOpenCustomHashMap.this.n;
                this.last = i;
                return i;
            }
            int[] key = Int2ObjectOpenCustomHashMap.this.key;
            do {
                int i2 = this.pos - 1;
                this.pos = i2;
                if (i2 < 0) {
                    this.last = Integer.MIN_VALUE;
                    int k = this.wrapped.getInt((-this.pos) - 1);
                    int p = HashCommon.mix(Int2ObjectOpenCustomHashMap.this.strategy.hashCode(k)) & Int2ObjectOpenCustomHashMap.this.mask;
                    while (!Int2ObjectOpenCustomHashMap.this.strategy.equals(k, key[p])) {
                        p = (p + 1) & Int2ObjectOpenCustomHashMap.this.mask;
                    }
                    return p;
                }
            } while (key[this.pos] == 0);
            int i3 = this.pos;
            this.last = i3;
            return i3;
        }

        public void forEachRemaining(ConsumerType action) {
            if (this.mustReturnNullKey) {
                this.mustReturnNullKey = false;
                int i = Int2ObjectOpenCustomHashMap.this.n;
                this.last = i;
                acceptOnIndex(action, i);
                this.c--;
            }
            int[] key = Int2ObjectOpenCustomHashMap.this.key;
            while (this.c != 0) {
                int i2 = this.pos - 1;
                this.pos = i2;
                if (i2 < 0) {
                    this.last = Integer.MIN_VALUE;
                    int k = this.wrapped.getInt((-this.pos) - 1);
                    int p = HashCommon.mix(Int2ObjectOpenCustomHashMap.this.strategy.hashCode(k)) & Int2ObjectOpenCustomHashMap.this.mask;
                    while (!Int2ObjectOpenCustomHashMap.this.strategy.equals(k, key[p])) {
                        p = (p + 1) & Int2ObjectOpenCustomHashMap.this.mask;
                    }
                    acceptOnIndex(action, p);
                    this.c--;
                } else if (key[this.pos] != 0) {
                    int i3 = this.pos;
                    this.last = i3;
                    acceptOnIndex(action, i3);
                    this.c--;
                }
            }
        }

        private void shiftKeys(int pos) {
            int curr;
            int[] key = Int2ObjectOpenCustomHashMap.this.key;
            while (true) {
                int last = pos;
                pos = (pos + 1) & Int2ObjectOpenCustomHashMap.this.mask;
                while (true) {
                    curr = key[pos];
                    if (curr == 0) {
                        key[last] = 0;
                        Int2ObjectOpenCustomHashMap.this.value[last] = null;
                        return;
                    }
                    int slot = HashCommon.mix(Int2ObjectOpenCustomHashMap.this.strategy.hashCode(curr)) & Int2ObjectOpenCustomHashMap.this.mask;
                    if (last > pos) {
                        if (last >= slot && slot > pos) {
                            break;
                        }
                        pos = (pos + 1) & Int2ObjectOpenCustomHashMap.this.mask;
                    } else if (last >= slot || slot > pos) {
                        break;
                    } else {
                        pos = (pos + 1) & Int2ObjectOpenCustomHashMap.this.mask;
                    }
                }
                if (pos < last) {
                    if (this.wrapped == null) {
                        this.wrapped = new IntArrayList(2);
                    }
                    this.wrapped.add(key[pos]);
                }
                key[last] = curr;
                Int2ObjectOpenCustomHashMap.this.value[last] = Int2ObjectOpenCustomHashMap.this.value[pos];
            }
        }

        public void remove() {
            if (this.last == -1) {
                throw new IllegalStateException();
            }
            if (this.last == Int2ObjectOpenCustomHashMap.this.n) {
                Int2ObjectOpenCustomHashMap.this.containsNullKey = false;
                Int2ObjectOpenCustomHashMap.this.value[Int2ObjectOpenCustomHashMap.this.n] = null;
            } else {
                if (this.pos < 0) {
                    Int2ObjectOpenCustomHashMap.this.remove(this.wrapped.getInt((-this.pos) - 1));
                    this.last = -1;
                    return;
                }
                shiftKeys(this.last);
            }
            Int2ObjectOpenCustomHashMap int2ObjectOpenCustomHashMap = Int2ObjectOpenCustomHashMap.this;
            int2ObjectOpenCustomHashMap.size--;
            this.last = -1;
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
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class EntryIterator extends Int2ObjectOpenCustomHashMap<V>.MapIterator<Consumer<? super Int2ObjectMap.Entry<V>>> implements ObjectIterator<Int2ObjectMap.Entry<V>> {
        private Int2ObjectOpenCustomHashMap<V>.MapEntry entry;

        private EntryIterator() {
            super();
        }

        @Override // java.util.Iterator
        public /* bridge */ /* synthetic */ void forEachRemaining(Consumer consumer) {
            super.forEachRemaining((EntryIterator) consumer);
        }

        @Override // java.util.Iterator
        public Int2ObjectOpenCustomHashMap<V>.MapEntry next() {
            Int2ObjectOpenCustomHashMap<V>.MapEntry mapEntry = new MapEntry(nextEntry());
            this.entry = mapEntry;
            return mapEntry;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectOpenCustomHashMap.MapIterator
        public final void acceptOnIndex(Consumer<? super Int2ObjectMap.Entry<V>> action, int index) {
            Int2ObjectOpenCustomHashMap<V>.MapEntry mapEntry = new MapEntry(index);
            this.entry = mapEntry;
            action.accept(mapEntry);
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectOpenCustomHashMap.MapIterator, java.util.Iterator
        public void remove() {
            super.remove();
            this.entry.index = -1;
        }
    }

    /* loaded from: classes4.dex */
    private final class FastEntryIterator extends Int2ObjectOpenCustomHashMap<V>.MapIterator<Consumer<? super Int2ObjectMap.Entry<V>>> implements ObjectIterator<Int2ObjectMap.Entry<V>> {
        private final Int2ObjectOpenCustomHashMap<V>.MapEntry entry;

        private FastEntryIterator() {
            super();
            this.entry = new MapEntry();
        }

        @Override // java.util.Iterator
        public /* bridge */ /* synthetic */ void forEachRemaining(Consumer consumer) {
            super.forEachRemaining((FastEntryIterator) consumer);
        }

        @Override // java.util.Iterator
        public Int2ObjectOpenCustomHashMap<V>.MapEntry next() {
            this.entry.index = nextEntry();
            return this.entry;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectOpenCustomHashMap.MapIterator
        public final void acceptOnIndex(Consumer<? super Int2ObjectMap.Entry<V>> action, int index) {
            this.entry.index = index;
            action.accept(this.entry);
        }
    }

    /* loaded from: classes4.dex */
    private abstract class MapSpliterator<ConsumerType, SplitType extends Int2ObjectOpenCustomHashMap<V>.MapSpliterator<ConsumerType, SplitType>> {
        int c;
        boolean hasSplit;
        int max;
        boolean mustReturnNull;
        int pos;

        abstract void acceptOnIndex(ConsumerType consumertype, int i);

        abstract SplitType makeForSplit(int i, int i2, boolean z);

        MapSpliterator() {
            this.pos = 0;
            this.max = Int2ObjectOpenCustomHashMap.this.n;
            this.c = 0;
            this.mustReturnNull = Int2ObjectOpenCustomHashMap.this.containsNullKey;
            this.hasSplit = false;
        }

        MapSpliterator(int pos, int max, boolean mustReturnNull, boolean hasSplit) {
            this.pos = 0;
            this.max = Int2ObjectOpenCustomHashMap.this.n;
            this.c = 0;
            this.mustReturnNull = Int2ObjectOpenCustomHashMap.this.containsNullKey;
            this.hasSplit = false;
            this.pos = pos;
            this.max = max;
            this.mustReturnNull = mustReturnNull;
            this.hasSplit = hasSplit;
        }

        public boolean tryAdvance(ConsumerType action) {
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                this.c++;
                acceptOnIndex(action, Int2ObjectOpenCustomHashMap.this.n);
                return true;
            }
            int[] key = Int2ObjectOpenCustomHashMap.this.key;
            while (this.pos < this.max) {
                if (key[this.pos] != 0) {
                    this.c++;
                    int i = this.pos;
                    this.pos = i + 1;
                    acceptOnIndex(action, i);
                    return true;
                }
                this.pos++;
            }
            return false;
        }

        public void forEachRemaining(ConsumerType action) {
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                this.c++;
                acceptOnIndex(action, Int2ObjectOpenCustomHashMap.this.n);
            }
            int[] key = Int2ObjectOpenCustomHashMap.this.key;
            while (this.pos < this.max) {
                if (key[this.pos] != 0) {
                    acceptOnIndex(action, this.pos);
                    this.c++;
                }
                this.pos++;
            }
        }

        public long estimateSize() {
            if (this.hasSplit) {
                return Math.min(Int2ObjectOpenCustomHashMap.this.size - this.c, ((long) ((Int2ObjectOpenCustomHashMap.this.realSize() / Int2ObjectOpenCustomHashMap.this.n) * (this.max - this.pos))) + (this.mustReturnNull ? 1L : 0L));
            }
            return Int2ObjectOpenCustomHashMap.this.size - this.c;
        }

        public SplitType trySplit() {
            int retLen;
            if (this.pos >= this.max - 1 || (retLen = (this.max - this.pos) >> 1) <= 1) {
                return null;
            }
            int myNewPos = this.pos + retLen;
            int retPos = this.pos;
            SplitType split = makeForSplit(retPos, myNewPos, this.mustReturnNull);
            this.pos = myNewPos;
            this.mustReturnNull = false;
            this.hasSplit = true;
            return split;
        }

        public long skip(long n) {
            if (n < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            if (n == 0) {
                return 0L;
            }
            long skipped = 0;
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                skipped = 0 + 1;
                n--;
            }
            int[] key = Int2ObjectOpenCustomHashMap.this.key;
            while (this.pos < this.max && n > 0) {
                int i = this.pos;
                this.pos = i + 1;
                if (key[i] != 0) {
                    skipped++;
                    n--;
                }
            }
            return skipped;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class EntrySpliterator extends Int2ObjectOpenCustomHashMap<V>.MapSpliterator<Consumer<? super Int2ObjectMap.Entry<V>>, Int2ObjectOpenCustomHashMap<V>.EntrySpliterator> implements ObjectSpliterator<Int2ObjectMap.Entry<V>> {
        private static final int POST_SPLIT_CHARACTERISTICS = 1;

        @Override // java.util.Spliterator
        public /* bridge */ /* synthetic */ void forEachRemaining(Consumer consumer) {
            super.forEachRemaining((EntrySpliterator) consumer);
        }

        @Override // java.util.Spliterator
        public /* bridge */ /* synthetic */ boolean tryAdvance(Consumer consumer) {
            return super.tryAdvance((EntrySpliterator) consumer);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterator, java.util.Spliterator
        public /* bridge */ /* synthetic */ ObjectSpliterator trySplit() {
            return (ObjectSpliterator) super.trySplit();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterator, java.util.Spliterator
        public /* bridge */ /* synthetic */ Spliterator trySplit() {
            return (Spliterator) super.trySplit();
        }

        EntrySpliterator() {
            super();
        }

        EntrySpliterator(int pos, int max, boolean mustReturnNull, boolean hasSplit) {
            super(pos, max, mustReturnNull, hasSplit);
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.hasSplit ? 1 : 65;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectOpenCustomHashMap.MapSpliterator
        public final void acceptOnIndex(Consumer<? super Int2ObjectMap.Entry<V>> action, int index) {
            action.accept(new MapEntry(index));
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectOpenCustomHashMap.MapSpliterator
        public final Int2ObjectOpenCustomHashMap<V>.EntrySpliterator makeForSplit(int pos, int max, boolean mustReturnNull) {
            return new EntrySpliterator(pos, max, mustReturnNull, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class MapEntrySet extends AbstractObjectSet<Int2ObjectMap.Entry<V>> implements Int2ObjectMap.FastEntrySet<V> {
        private MapEntrySet() {
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectSet, it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        public ObjectIterator<Int2ObjectMap.Entry<V>> iterator() {
            return new EntryIterator();
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap.FastEntrySet
        public ObjectIterator<Int2ObjectMap.Entry<V>> fastIterator() {
            return new FastEntryIterator();
        }

        @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        /* renamed from: spliterator */
        public ObjectSpliterator<Int2ObjectMap.Entry<V>> mo221spliterator() {
            return new EntrySpliterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            int curr;
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> e = (Map.Entry) o;
            if (e.getKey() == null || !(e.getKey() instanceof Integer)) {
                return false;
            }
            int k = ((Integer) e.getKey()).intValue();
            Object value = e.getValue();
            if (Int2ObjectOpenCustomHashMap.this.strategy.equals(k, 0)) {
                return Int2ObjectOpenCustomHashMap.this.containsNullKey && Objects.equals(Int2ObjectOpenCustomHashMap.this.value[Int2ObjectOpenCustomHashMap.this.n], value);
            }
            int[] key = Int2ObjectOpenCustomHashMap.this.key;
            int mix = HashCommon.mix(Int2ObjectOpenCustomHashMap.this.strategy.hashCode(k)) & Int2ObjectOpenCustomHashMap.this.mask;
            int pos = mix;
            int curr2 = key[mix];
            if (curr2 == 0) {
                return false;
            }
            if (Int2ObjectOpenCustomHashMap.this.strategy.equals(k, curr2)) {
                return Objects.equals(Int2ObjectOpenCustomHashMap.this.value[pos], value);
            }
            do {
                int i = (pos + 1) & Int2ObjectOpenCustomHashMap.this.mask;
                pos = i;
                curr = key[i];
                if (curr == 0) {
                    return false;
                }
            } while (!Int2ObjectOpenCustomHashMap.this.strategy.equals(k, curr));
            return Objects.equals(Int2ObjectOpenCustomHashMap.this.value[pos], value);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> e = (Map.Entry) o;
            if (e.getKey() == null || !(e.getKey() instanceof Integer)) {
                return false;
            }
            int k = ((Integer) e.getKey()).intValue();
            Object value = e.getValue();
            if (Int2ObjectOpenCustomHashMap.this.strategy.equals(k, 0)) {
                if (!Int2ObjectOpenCustomHashMap.this.containsNullKey || !Objects.equals(Int2ObjectOpenCustomHashMap.this.value[Int2ObjectOpenCustomHashMap.this.n], value)) {
                    return false;
                }
                Int2ObjectOpenCustomHashMap.this.removeNullEntry();
                return true;
            }
            int[] key = Int2ObjectOpenCustomHashMap.this.key;
            int mix = HashCommon.mix(Int2ObjectOpenCustomHashMap.this.strategy.hashCode(k)) & Int2ObjectOpenCustomHashMap.this.mask;
            int pos = mix;
            int curr = key[mix];
            if (curr == 0) {
                return false;
            }
            if (Int2ObjectOpenCustomHashMap.this.strategy.equals(curr, k)) {
                if (!Objects.equals(Int2ObjectOpenCustomHashMap.this.value[pos], value)) {
                    return false;
                }
                Int2ObjectOpenCustomHashMap.this.removeEntry(pos);
                return true;
            }
            while (true) {
                int i = (pos + 1) & Int2ObjectOpenCustomHashMap.this.mask;
                pos = i;
                int curr2 = key[i];
                if (curr2 == 0) {
                    return false;
                }
                if (Int2ObjectOpenCustomHashMap.this.strategy.equals(curr2, k) && Objects.equals(Int2ObjectOpenCustomHashMap.this.value[pos], value)) {
                    Int2ObjectOpenCustomHashMap.this.removeEntry(pos);
                    return true;
                }
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return Int2ObjectOpenCustomHashMap.this.size;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            Int2ObjectOpenCustomHashMap.this.clear();
        }

        @Override // java.lang.Iterable
        public void forEach(Consumer<? super Int2ObjectMap.Entry<V>> consumer) {
            if (Int2ObjectOpenCustomHashMap.this.containsNullKey) {
                consumer.accept(new MapEntry(Int2ObjectOpenCustomHashMap.this.n));
            }
            int pos = Int2ObjectOpenCustomHashMap.this.n;
            while (true) {
                int pos2 = pos - 1;
                if (pos != 0) {
                    if (Int2ObjectOpenCustomHashMap.this.key[pos2] != 0) {
                        consumer.accept(new MapEntry(pos2));
                    }
                    pos = pos2;
                } else {
                    return;
                }
            }
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap.FastEntrySet
        public void fastForEach(Consumer<? super Int2ObjectMap.Entry<V>> consumer) {
            Int2ObjectOpenCustomHashMap<V>.MapEntry entry = new MapEntry();
            if (Int2ObjectOpenCustomHashMap.this.containsNullKey) {
                entry.index = Int2ObjectOpenCustomHashMap.this.n;
                consumer.accept(entry);
            }
            int pos = Int2ObjectOpenCustomHashMap.this.n;
            while (true) {
                int pos2 = pos - 1;
                if (pos != 0) {
                    if (Int2ObjectOpenCustomHashMap.this.key[pos2] != 0) {
                        entry.index = pos2;
                        consumer.accept(entry);
                    }
                    pos = pos2;
                } else {
                    return;
                }
            }
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap, it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
    public Int2ObjectMap.FastEntrySet<V> int2ObjectEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet();
        }
        return this.entries;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class KeyIterator extends Int2ObjectOpenCustomHashMap<V>.MapIterator<java.util.function.IntConsumer> implements IntIterator {
        @Override // java.util.PrimitiveIterator.OfInt
        public /* bridge */ /* synthetic */ void forEachRemaining(java.util.function.IntConsumer intConsumer) {
            super.forEachRemaining((KeyIterator) intConsumer);
        }

        public KeyIterator() {
            super();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectOpenCustomHashMap.MapIterator
        public final void acceptOnIndex(java.util.function.IntConsumer action, int index) {
            action.accept(Int2ObjectOpenCustomHashMap.this.key[index]);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt
        public int nextInt() {
            return Int2ObjectOpenCustomHashMap.this.key[nextEntry()];
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class KeySpliterator extends Int2ObjectOpenCustomHashMap<V>.MapSpliterator<java.util.function.IntConsumer, Int2ObjectOpenCustomHashMap<V>.KeySpliterator> implements IntSpliterator {
        private static final int POST_SPLIT_CHARACTERISTICS = 257;

        @Override // java.util.Spliterator.OfInt
        public /* bridge */ /* synthetic */ void forEachRemaining(java.util.function.IntConsumer intConsumer) {
            super.forEachRemaining((KeySpliterator) intConsumer);
        }

        @Override // java.util.Spliterator.OfInt
        public /* bridge */ /* synthetic */ boolean tryAdvance(java.util.function.IntConsumer intConsumer) {
            return super.tryAdvance((KeySpliterator) intConsumer);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSpliterator, java.util.Spliterator.OfInt, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public /* bridge */ /* synthetic */ IntSpliterator trySplit() {
            return (IntSpliterator) super.trySplit();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSpliterator, java.util.Spliterator.OfInt, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public /* bridge */ /* synthetic */ Spliterator.OfInt trySplit() {
            return (Spliterator.OfInt) super.trySplit();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSpliterator, java.util.Spliterator.OfInt, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public /* bridge */ /* synthetic */ Spliterator.OfPrimitive trySplit() {
            return (Spliterator.OfPrimitive) super.trySplit();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSpliterator, java.util.Spliterator.OfInt, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public /* bridge */ /* synthetic */ Spliterator trySplit() {
            return (Spliterator) super.trySplit();
        }

        KeySpliterator() {
            super();
        }

        KeySpliterator(int pos, int max, boolean mustReturnNull, boolean hasSplit) {
            super(pos, max, mustReturnNull, hasSplit);
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.hasSplit ? 257 : 321;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectOpenCustomHashMap.MapSpliterator
        public final void acceptOnIndex(java.util.function.IntConsumer action, int index) {
            action.accept(Int2ObjectOpenCustomHashMap.this.key[index]);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectOpenCustomHashMap.MapSpliterator
        public final Int2ObjectOpenCustomHashMap<V>.KeySpliterator makeForSplit(int pos, int max, boolean mustReturnNull) {
            return new KeySpliterator(pos, max, mustReturnNull, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class KeySet extends AbstractIntSet {
        private KeySet() {
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntSet, it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
        public IntIterator iterator() {
            return new KeyIterator();
        }

        @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
        public IntSpliterator spliterator() {
            return new KeySpliterator();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntIterable
        public void forEach(java.util.function.IntConsumer consumer) {
            if (Int2ObjectOpenCustomHashMap.this.containsNullKey) {
                consumer.accept(Int2ObjectOpenCustomHashMap.this.key[Int2ObjectOpenCustomHashMap.this.n]);
            }
            int k = Int2ObjectOpenCustomHashMap.this.n;
            while (true) {
                int pos = k - 1;
                if (k != 0) {
                    int k2 = Int2ObjectOpenCustomHashMap.this.key[pos];
                    if (k2 != 0) {
                        consumer.accept(k2);
                    }
                    k = pos;
                } else {
                    return;
                }
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return Int2ObjectOpenCustomHashMap.this.size;
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public boolean contains(int k) {
            return Int2ObjectOpenCustomHashMap.this.containsKey(k);
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntSet, it.unimi.dsi.fastutil.ints.IntSet
        public boolean remove(int k) {
            int oldSize = Int2ObjectOpenCustomHashMap.this.size;
            Int2ObjectOpenCustomHashMap.this.remove(k);
            return Int2ObjectOpenCustomHashMap.this.size != oldSize;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            Int2ObjectOpenCustomHashMap.this.clear();
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
    /* renamed from: keySet */
    public Set<Integer> keySet2() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class ValueIterator extends Int2ObjectOpenCustomHashMap<V>.MapIterator<Consumer<? super V>> implements ObjectIterator<V> {
        @Override // java.util.Iterator
        public /* bridge */ /* synthetic */ void forEachRemaining(Consumer consumer) {
            super.forEachRemaining((ValueIterator) consumer);
        }

        public ValueIterator() {
            super();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectOpenCustomHashMap.MapIterator
        public final void acceptOnIndex(Consumer<? super V> consumer, int i) {
            consumer.accept(Int2ObjectOpenCustomHashMap.this.value[i]);
        }

        @Override // java.util.Iterator
        public V next() {
            return Int2ObjectOpenCustomHashMap.this.value[nextEntry()];
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class ValueSpliterator extends Int2ObjectOpenCustomHashMap<V>.MapSpliterator<Consumer<? super V>, Int2ObjectOpenCustomHashMap<V>.ValueSpliterator> implements ObjectSpliterator<V> {
        private static final int POST_SPLIT_CHARACTERISTICS = 0;

        @Override // java.util.Spliterator
        public /* bridge */ /* synthetic */ void forEachRemaining(Consumer consumer) {
            super.forEachRemaining((ValueSpliterator) consumer);
        }

        @Override // java.util.Spliterator
        public /* bridge */ /* synthetic */ boolean tryAdvance(Consumer consumer) {
            return super.tryAdvance((ValueSpliterator) consumer);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterator, java.util.Spliterator
        public /* bridge */ /* synthetic */ ObjectSpliterator trySplit() {
            return (ObjectSpliterator) super.trySplit();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterator, java.util.Spliterator
        public /* bridge */ /* synthetic */ Spliterator trySplit() {
            return (Spliterator) super.trySplit();
        }

        ValueSpliterator() {
            super();
        }

        ValueSpliterator(int pos, int max, boolean mustReturnNull, boolean hasSplit) {
            super(pos, max, mustReturnNull, hasSplit);
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.hasSplit ? 0 : 64;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectOpenCustomHashMap.MapSpliterator
        public final void acceptOnIndex(Consumer<? super V> consumer, int i) {
            consumer.accept(Int2ObjectOpenCustomHashMap.this.value[i]);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectOpenCustomHashMap.MapSpliterator
        public final Int2ObjectOpenCustomHashMap<V>.ValueSpliterator makeForSplit(int pos, int max, boolean mustReturnNull) {
            return new ValueSpliterator(pos, max, mustReturnNull, true);
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
    public ObjectCollection<V> values() {
        if (this.values == null) {
            this.values = new AbstractObjectCollection<V>() { // from class: it.unimi.dsi.fastutil.ints.Int2ObjectOpenCustomHashMap.1
                @Override // it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
                public ObjectIterator<V> iterator() {
                    return new ValueIterator();
                }

                @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
                /* renamed from: spliterator */
                public ObjectSpliterator<V> mo221spliterator() {
                    return new ValueSpliterator();
                }

                @Override // java.lang.Iterable
                public void forEach(Consumer<? super V> consumer) {
                    if (Int2ObjectOpenCustomHashMap.this.containsNullKey) {
                        consumer.accept(Int2ObjectOpenCustomHashMap.this.value[Int2ObjectOpenCustomHashMap.this.n]);
                    }
                    int i = Int2ObjectOpenCustomHashMap.this.n;
                    while (true) {
                        int i2 = i - 1;
                        if (i != 0) {
                            if (Int2ObjectOpenCustomHashMap.this.key[i2] != 0) {
                                consumer.accept(Int2ObjectOpenCustomHashMap.this.value[i2]);
                            }
                            i = i2;
                        } else {
                            return;
                        }
                    }
                }

                @Override // java.util.AbstractCollection, java.util.Collection
                public int size() {
                    return Int2ObjectOpenCustomHashMap.this.size;
                }

                @Override // java.util.AbstractCollection, java.util.Collection
                public boolean contains(Object v) {
                    return Int2ObjectOpenCustomHashMap.this.containsValue(v);
                }

                @Override // java.util.AbstractCollection, java.util.Collection
                public void clear() {
                    Int2ObjectOpenCustomHashMap.this.clear();
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
        int i2;
        int[] iArr = this.key;
        V[] vArr = this.value;
        int i3 = i - 1;
        int[] iArr2 = new int[i + 1];
        V[] vArr2 = (V[]) new Object[i + 1];
        int i4 = this.n;
        int realSize = realSize();
        while (true) {
            int i5 = realSize - 1;
            if (realSize == 0) {
                vArr2[i] = vArr[this.n];
                this.n = i;
                this.mask = i3;
                this.maxFill = HashCommon.maxFill(this.n, this.f);
                this.key = iArr2;
                this.value = vArr2;
                return;
            }
            do {
                i4--;
            } while (iArr[i4] == 0);
            int mix = HashCommon.mix(this.strategy.hashCode(iArr[i4])) & i3;
            int i6 = mix;
            if (iArr2[mix] == 0) {
                iArr2[i6] = iArr[i4];
                vArr2[i6] = vArr[i4];
                realSize = i5;
            }
            do {
                i2 = (i6 + 1) & i3;
                i6 = i2;
            } while (iArr2[i2] != 0);
            iArr2[i6] = iArr[i4];
            vArr2[i6] = vArr[i4];
            realSize = i5;
        }
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public Int2ObjectOpenCustomHashMap<V> m222clone() {
        try {
            Int2ObjectOpenCustomHashMap<V> int2ObjectOpenCustomHashMap = (Int2ObjectOpenCustomHashMap) super.clone();
            int2ObjectOpenCustomHashMap.keys = null;
            int2ObjectOpenCustomHashMap.values = null;
            int2ObjectOpenCustomHashMap.entries = null;
            int2ObjectOpenCustomHashMap.containsNullKey = this.containsNullKey;
            int2ObjectOpenCustomHashMap.key = (int[]) this.key.clone();
            int2ObjectOpenCustomHashMap.value = (V[]) ((Object[]) this.value.clone());
            int2ObjectOpenCustomHashMap.strategy = this.strategy;
            return int2ObjectOpenCustomHashMap;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap, java.util.Map
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
            int t = this.strategy.hashCode(this.key[i]);
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
        int[] key = this.key;
        V[] value = this.value;
        Int2ObjectOpenCustomHashMap<V>.EntryIterator i = new EntryIterator();
        s.defaultWriteObject();
        int e = this.size;
        while (true) {
            int j = e - 1;
            if (e != 0) {
                int e2 = i.nextEntry();
                s.writeInt(key[e2]);
                s.writeObject(value[e2]);
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
        this.mask = this.n - 1;
        int[] iArr = new int[this.n + 1];
        this.key = iArr;
        V[] vArr = (V[]) new Object[this.n + 1];
        this.value = vArr;
        int i = this.size;
        while (true) {
            int i2 = i - 1;
            if (i != 0) {
                int readInt = objectInputStream.readInt();
                Object readObject = objectInputStream.readObject();
                if (this.strategy.equals(readInt, 0)) {
                    mix = this.n;
                    this.containsNullKey = true;
                } else {
                    mix = HashCommon.mix(this.strategy.hashCode(readInt)) & this.mask;
                    while (iArr[mix] != 0) {
                        mix = (mix + 1) & this.mask;
                    }
                }
                iArr[mix] = readInt;
                vArr[mix] = readObject;
                i = i2;
            } else {
                return;
            }
        }
    }

    private void checkTable() {
    }
}
