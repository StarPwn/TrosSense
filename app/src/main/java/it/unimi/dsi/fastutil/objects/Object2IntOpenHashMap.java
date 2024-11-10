package it.unimi.dsi.fastutil.objects;

import android.Manifest;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.ints.AbstractIntCollection;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntSpliterator;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.ToIntFunction;

/* loaded from: classes4.dex */
public class Object2IntOpenHashMap<K> extends AbstractObject2IntMap<K> implements Serializable, Cloneable, Hash {
    private static final boolean ASSERTS = false;
    private static final long serialVersionUID = 0;
    protected transient boolean containsNullKey;
    protected transient Object2IntMap.FastEntrySet<K> entries;
    protected final float f;
    protected transient K[] key;
    protected transient ObjectSet<K> keys;
    protected transient int mask;
    protected transient int maxFill;
    protected final transient int minN;
    protected transient int n;
    protected int size;
    protected transient int[] value;
    protected transient IntCollection values;

    public Object2IntOpenHashMap(int i, float f) {
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
    }

    public Object2IntOpenHashMap(int expected) {
        this(expected, 0.75f);
    }

    public Object2IntOpenHashMap() {
        this(16, 0.75f);
    }

    public Object2IntOpenHashMap(Map<? extends K, ? extends Integer> m, float f) {
        this(m.size(), f);
        putAll(m);
    }

    public Object2IntOpenHashMap(Map<? extends K, ? extends Integer> m) {
        this(m, 0.75f);
    }

    public Object2IntOpenHashMap(Object2IntMap<K> m, float f) {
        this(m.size(), f);
        putAll(m);
    }

    public Object2IntOpenHashMap(Object2IntMap<K> m) {
        this((Object2IntMap) m, 0.75f);
    }

    public Object2IntOpenHashMap(K[] k, int[] v, float f) {
        this(k.length, f);
        if (k.length != v.length) {
            throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
        }
        for (int i = 0; i < k.length; i++) {
            put((Object2IntOpenHashMap<K>) k[i], v[i]);
        }
    }

    public Object2IntOpenHashMap(K[] k, int[] v) {
        this(k, v, 0.75f);
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
    public int removeEntry(int pos) {
        int oldValue = this.value[pos];
        this.size--;
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
        if (k == null) {
            return this.containsNullKey ? this.n : -(this.n + 1);
        }
        K[] key = this.key;
        int mix = HashCommon.mix(k.hashCode()) & this.mask;
        int pos = mix;
        K curr2 = key[mix];
        if (curr2 == null) {
            return -(pos + 1);
        }
        if (k.equals(curr2)) {
            return pos;
        }
        do {
            int i = (pos + 1) & this.mask;
            pos = i;
            curr = key[i];
            if (curr == null) {
                return -(pos + 1);
            }
        } while (!k.equals(curr));
        return pos;
    }

    private void insert(int pos, K k, int v) {
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
        if (k == null) {
            if (this.containsNullKey) {
                return addToValue(this.n, incr);
            }
            pos = this.n;
            this.containsNullKey = true;
        } else {
            K[] key = this.key;
            int mix = HashCommon.mix(k.hashCode()) & this.mask;
            int pos2 = mix;
            K curr2 = key[mix];
            if (curr2 == null) {
                pos = pos2;
            } else {
                if (curr2.equals(k)) {
                    return addToValue(pos2, incr);
                }
                do {
                    int i = (pos2 + 1) & this.mask;
                    pos2 = i;
                    curr = key[i];
                    if (curr == null) {
                        pos = pos2;
                    }
                } while (!curr.equals(k));
                return addToValue(pos2, incr);
            }
        }
        this.key[pos] = k;
        this.value[pos] = this.defRetValue + incr;
        int i2 = this.size;
        this.size = i2 + 1;
        if (i2 >= this.maxFill) {
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
            this.value[last] = this.value[pos];
        }
    }

    @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
    public int removeInt(Object k) {
        K curr;
        if (k == null) {
            return this.containsNullKey ? removeNullEntry() : this.defRetValue;
        }
        K[] key = this.key;
        int mix = HashCommon.mix(k.hashCode()) & this.mask;
        int pos = mix;
        K curr2 = key[mix];
        if (curr2 == null) {
            return this.defRetValue;
        }
        if (k.equals(curr2)) {
            return removeEntry(pos);
        }
        do {
            int i = (pos + 1) & this.mask;
            pos = i;
            curr = key[i];
            if (curr == null) {
                return this.defRetValue;
            }
        } while (!k.equals(curr));
        return removeEntry(pos);
    }

    @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
    public int getInt(Object k) {
        K curr;
        if (k == null) {
            return this.containsNullKey ? this.value[this.n] : this.defRetValue;
        }
        K[] key = this.key;
        int mix = HashCommon.mix(k.hashCode()) & this.mask;
        int pos = mix;
        K curr2 = key[mix];
        if (curr2 == null) {
            return this.defRetValue;
        }
        if (k.equals(curr2)) {
            return this.value[pos];
        }
        do {
            int i = (pos + 1) & this.mask;
            pos = i;
            curr = key[i];
            if (curr == null) {
                return this.defRetValue;
            }
        } while (!k.equals(curr));
        return this.value[pos];
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObject2IntMap, it.unimi.dsi.fastutil.Function
    public boolean containsKey(Object k) {
        K curr;
        if (k == null) {
            return this.containsNullKey;
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
        if (k == null) {
            return this.containsNullKey ? this.value[this.n] : defaultValue;
        }
        K[] key = this.key;
        int mix = HashCommon.mix(k.hashCode()) & this.mask;
        int pos = mix;
        K curr2 = key[mix];
        if (curr2 == null) {
            return defaultValue;
        }
        if (k.equals(curr2)) {
            return this.value[pos];
        }
        do {
            int i = (pos + 1) & this.mask;
            pos = i;
            curr = key[i];
            if (curr == null) {
                return defaultValue;
            }
        } while (!k.equals(curr));
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
        if (k == null) {
            if (!this.containsNullKey || v != this.value[this.n]) {
                return false;
            }
            removeNullEntry();
            return true;
        }
        K[] key = this.key;
        int mix = HashCommon.mix(k.hashCode()) & this.mask;
        int pos = mix;
        K curr = key[mix];
        if (curr == null) {
            return false;
        }
        if (k.equals(curr) && v == this.value[pos]) {
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
            if (k.equals(curr2) && v == this.value[pos]) {
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
            if (k == null) {
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
                if (k == null) {
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
            if (k == null) {
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
            return Object2IntOpenHashMap.this.key[this.index];
        }

        @Override // it.unimi.dsi.fastutil.Pair
        public K left() {
            return Object2IntOpenHashMap.this.key[this.index];
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap.Entry
        public int getIntValue() {
            return Object2IntOpenHashMap.this.value[this.index];
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectIntPair
        public int rightInt() {
            return Object2IntOpenHashMap.this.value[this.index];
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap.Entry
        public int setValue(int v) {
            int oldValue = Object2IntOpenHashMap.this.value[this.index];
            Object2IntOpenHashMap.this.value[this.index] = v;
            return oldValue;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectIntPair
        public ObjectIntPair<K> right(int v) {
            Object2IntOpenHashMap.this.value[this.index] = v;
            return this;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap.Entry, java.util.Map.Entry
        @Deprecated
        public Integer getValue() {
            return Integer.valueOf(Object2IntOpenHashMap.this.value[this.index]);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap.Entry, java.util.Map.Entry
        @Deprecated
        public Integer setValue(Integer v) {
            return Integer.valueOf(setValue(v.intValue()));
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<K, Integer> e = (Map.Entry) o;
            return Objects.equals(Object2IntOpenHashMap.this.key[this.index], e.getKey()) && Object2IntOpenHashMap.this.value[this.index] == e.getValue().intValue();
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return (Object2IntOpenHashMap.this.key[this.index] == null ? 0 : Object2IntOpenHashMap.this.key[this.index].hashCode()) ^ Object2IntOpenHashMap.this.value[this.index];
        }

        public String toString() {
            return Object2IntOpenHashMap.this.key[this.index] + "=>" + Object2IntOpenHashMap.this.value[this.index];
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public abstract class MapIterator<ConsumerType> {
        int c;
        int last;
        boolean mustReturnNullKey;
        int pos;
        ObjectArrayList<K> wrapped;

        abstract void acceptOnIndex(ConsumerType consumertype, int i);

        private MapIterator() {
            this.pos = Object2IntOpenHashMap.this.n;
            this.last = -1;
            this.c = Object2IntOpenHashMap.this.size;
            this.mustReturnNullKey = Object2IntOpenHashMap.this.containsNullKey;
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
                int i = Object2IntOpenHashMap.this.n;
                this.last = i;
                return i;
            }
            K[] key = Object2IntOpenHashMap.this.key;
            do {
                int i2 = this.pos - 1;
                this.pos = i2;
                if (i2 < 0) {
                    this.last = Integer.MIN_VALUE;
                    K k = this.wrapped.get((-this.pos) - 1);
                    int p = HashCommon.mix(k.hashCode()) & Object2IntOpenHashMap.this.mask;
                    while (!k.equals(key[p])) {
                        p = (p + 1) & Object2IntOpenHashMap.this.mask;
                    }
                    return p;
                }
            } while (key[this.pos] == null);
            int i3 = this.pos;
            this.last = i3;
            return i3;
        }

        public void forEachRemaining(ConsumerType action) {
            if (this.mustReturnNullKey) {
                this.mustReturnNullKey = false;
                int i = Object2IntOpenHashMap.this.n;
                this.last = i;
                acceptOnIndex(action, i);
                this.c--;
            }
            K[] key = Object2IntOpenHashMap.this.key;
            while (this.c != 0) {
                int i2 = this.pos - 1;
                this.pos = i2;
                if (i2 < 0) {
                    this.last = Integer.MIN_VALUE;
                    K k = this.wrapped.get((-this.pos) - 1);
                    int p = HashCommon.mix(k.hashCode()) & Object2IntOpenHashMap.this.mask;
                    while (!k.equals(key[p])) {
                        p = (p + 1) & Object2IntOpenHashMap.this.mask;
                    }
                    acceptOnIndex(action, p);
                    this.c--;
                } else if (key[this.pos] != null) {
                    int i3 = this.pos;
                    this.last = i3;
                    acceptOnIndex(action, i3);
                    this.c--;
                }
            }
        }

        private void shiftKeys(int pos) {
            K curr;
            K[] key = Object2IntOpenHashMap.this.key;
            while (true) {
                int last = pos;
                pos = (pos + 1) & Object2IntOpenHashMap.this.mask;
                while (true) {
                    curr = key[pos];
                    if (curr == null) {
                        key[last] = null;
                        return;
                    }
                    int slot = HashCommon.mix(curr.hashCode()) & Object2IntOpenHashMap.this.mask;
                    if (last > pos) {
                        if (last >= slot && slot > pos) {
                            break;
                        }
                        pos = (pos + 1) & Object2IntOpenHashMap.this.mask;
                    } else if (last >= slot || slot > pos) {
                        break;
                    } else {
                        pos = (pos + 1) & Object2IntOpenHashMap.this.mask;
                    }
                }
                if (pos < last) {
                    if (this.wrapped == null) {
                        this.wrapped = new ObjectArrayList<>(2);
                    }
                    this.wrapped.add(key[pos]);
                }
                key[last] = curr;
                Object2IntOpenHashMap.this.value[last] = Object2IntOpenHashMap.this.value[pos];
            }
        }

        public void remove() {
            if (this.last == -1) {
                throw new IllegalStateException();
            }
            if (this.last == Object2IntOpenHashMap.this.n) {
                Object2IntOpenHashMap.this.containsNullKey = false;
                Object2IntOpenHashMap.this.key[Object2IntOpenHashMap.this.n] = null;
            } else {
                if (this.pos < 0) {
                    Object2IntOpenHashMap.this.removeInt(this.wrapped.set((-this.pos) - 1, null));
                    this.last = -1;
                    return;
                }
                shiftKeys(this.last);
            }
            Object2IntOpenHashMap object2IntOpenHashMap = Object2IntOpenHashMap.this;
            object2IntOpenHashMap.size--;
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
    public final class EntryIterator extends Object2IntOpenHashMap<K>.MapIterator<Consumer<? super Object2IntMap.Entry<K>>> implements ObjectIterator<Object2IntMap.Entry<K>> {
        private Object2IntOpenHashMap<K>.MapEntry entry;

        private EntryIterator() {
            super();
        }

        @Override // java.util.Iterator
        public /* bridge */ /* synthetic */ void forEachRemaining(Consumer consumer) {
            super.forEachRemaining((EntryIterator) consumer);
        }

        @Override // java.util.Iterator
        public Object2IntOpenHashMap<K>.MapEntry next() {
            Object2IntOpenHashMap<K>.MapEntry mapEntry = new MapEntry(nextEntry());
            this.entry = mapEntry;
            return mapEntry;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap.MapIterator
        public final void acceptOnIndex(Consumer<? super Object2IntMap.Entry<K>> action, int index) {
            Object2IntOpenHashMap<K>.MapEntry mapEntry = new MapEntry(index);
            this.entry = mapEntry;
            action.accept(mapEntry);
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap.MapIterator, java.util.Iterator
        public void remove() {
            super.remove();
            this.entry.index = -1;
        }
    }

    /* loaded from: classes4.dex */
    private final class FastEntryIterator extends Object2IntOpenHashMap<K>.MapIterator<Consumer<? super Object2IntMap.Entry<K>>> implements ObjectIterator<Object2IntMap.Entry<K>> {
        private final Object2IntOpenHashMap<K>.MapEntry entry;

        private FastEntryIterator() {
            super();
            this.entry = new MapEntry();
        }

        @Override // java.util.Iterator
        public /* bridge */ /* synthetic */ void forEachRemaining(Consumer consumer) {
            super.forEachRemaining((FastEntryIterator) consumer);
        }

        @Override // java.util.Iterator
        public Object2IntOpenHashMap<K>.MapEntry next() {
            this.entry.index = nextEntry();
            return this.entry;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap.MapIterator
        public final void acceptOnIndex(Consumer<? super Object2IntMap.Entry<K>> action, int index) {
            this.entry.index = index;
            action.accept(this.entry);
        }
    }

    /* loaded from: classes4.dex */
    private abstract class MapSpliterator<ConsumerType, SplitType extends Object2IntOpenHashMap<K>.MapSpliterator<ConsumerType, SplitType>> {
        int c;
        boolean hasSplit;
        int max;
        boolean mustReturnNull;
        int pos;

        abstract void acceptOnIndex(ConsumerType consumertype, int i);

        abstract SplitType makeForSplit(int i, int i2, boolean z);

        MapSpliterator() {
            this.pos = 0;
            this.max = Object2IntOpenHashMap.this.n;
            this.c = 0;
            this.mustReturnNull = Object2IntOpenHashMap.this.containsNullKey;
            this.hasSplit = false;
        }

        MapSpliterator(int pos, int max, boolean mustReturnNull, boolean hasSplit) {
            this.pos = 0;
            this.max = Object2IntOpenHashMap.this.n;
            this.c = 0;
            this.mustReturnNull = Object2IntOpenHashMap.this.containsNullKey;
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
                acceptOnIndex(action, Object2IntOpenHashMap.this.n);
                return true;
            }
            K[] key = Object2IntOpenHashMap.this.key;
            while (this.pos < this.max) {
                if (key[this.pos] != null) {
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
                acceptOnIndex(action, Object2IntOpenHashMap.this.n);
            }
            K[] key = Object2IntOpenHashMap.this.key;
            while (this.pos < this.max) {
                if (key[this.pos] != null) {
                    acceptOnIndex(action, this.pos);
                    this.c++;
                }
                this.pos++;
            }
        }

        public long estimateSize() {
            if (this.hasSplit) {
                return Math.min(Object2IntOpenHashMap.this.size - this.c, ((long) ((Object2IntOpenHashMap.this.realSize() / Object2IntOpenHashMap.this.n) * (this.max - this.pos))) + (this.mustReturnNull ? 1L : 0L));
            }
            return Object2IntOpenHashMap.this.size - this.c;
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
            K[] key = Object2IntOpenHashMap.this.key;
            while (this.pos < this.max && n > 0) {
                int i = this.pos;
                this.pos = i + 1;
                if (key[i] != null) {
                    skipped++;
                    n--;
                }
            }
            return skipped;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class EntrySpliterator extends Object2IntOpenHashMap<K>.MapSpliterator<Consumer<? super Object2IntMap.Entry<K>>, Object2IntOpenHashMap<K>.EntrySpliterator> implements ObjectSpliterator<Object2IntMap.Entry<K>> {
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
        @Override // it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap.MapSpliterator
        public final void acceptOnIndex(Consumer<? super Object2IntMap.Entry<K>> action, int index) {
            action.accept(new MapEntry(index));
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap.MapSpliterator
        public final Object2IntOpenHashMap<K>.EntrySpliterator makeForSplit(int pos, int max, boolean mustReturnNull) {
            return new EntrySpliterator(pos, max, mustReturnNull, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class MapEntrySet extends AbstractObjectSet<Object2IntMap.Entry<K>> implements Object2IntMap.FastEntrySet<K> {
        private MapEntrySet() {
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectSet, it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        public ObjectIterator<Object2IntMap.Entry<K>> iterator() {
            return new EntryIterator();
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap.FastEntrySet
        public ObjectIterator<Object2IntMap.Entry<K>> fastIterator() {
            return new FastEntryIterator();
        }

        @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        /* renamed from: spliterator */
        public ObjectSpliterator<Object2IntMap.Entry<K>> mo221spliterator() {
            return new EntrySpliterator();
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
            if (key == null) {
                return Object2IntOpenHashMap.this.containsNullKey && Object2IntOpenHashMap.this.value[Object2IntOpenHashMap.this.n] == v;
            }
            K[] key2 = Object2IntOpenHashMap.this.key;
            int mix = HashCommon.mix(key.hashCode()) & Object2IntOpenHashMap.this.mask;
            int pos = mix;
            K curr2 = key2[mix];
            if (curr2 == null) {
                return false;
            }
            if (key.equals(curr2)) {
                return Object2IntOpenHashMap.this.value[pos] == v;
            }
            do {
                int i = (pos + 1) & Object2IntOpenHashMap.this.mask;
                pos = i;
                curr = key2[i];
                if (curr == null) {
                    return false;
                }
            } while (!key.equals(curr));
            return Object2IntOpenHashMap.this.value[pos] == v;
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
            if (key == null) {
                if (!Object2IntOpenHashMap.this.containsNullKey || Object2IntOpenHashMap.this.value[Object2IntOpenHashMap.this.n] != v) {
                    return false;
                }
                Object2IntOpenHashMap.this.removeNullEntry();
                return true;
            }
            K[] key2 = Object2IntOpenHashMap.this.key;
            int mix = HashCommon.mix(key.hashCode()) & Object2IntOpenHashMap.this.mask;
            int pos = mix;
            K curr = key2[mix];
            if (curr == null) {
                return false;
            }
            if (curr.equals(key)) {
                if (Object2IntOpenHashMap.this.value[pos] != v) {
                    return false;
                }
                Object2IntOpenHashMap.this.removeEntry(pos);
                return true;
            }
            while (true) {
                int i = (pos + 1) & Object2IntOpenHashMap.this.mask;
                pos = i;
                K curr2 = key2[i];
                if (curr2 == null) {
                    return false;
                }
                if (curr2.equals(key) && Object2IntOpenHashMap.this.value[pos] == v) {
                    Object2IntOpenHashMap.this.removeEntry(pos);
                    return true;
                }
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return Object2IntOpenHashMap.this.size;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            Object2IntOpenHashMap.this.clear();
        }

        @Override // java.lang.Iterable
        public void forEach(Consumer<? super Object2IntMap.Entry<K>> consumer) {
            if (Object2IntOpenHashMap.this.containsNullKey) {
                consumer.accept(new MapEntry(Object2IntOpenHashMap.this.n));
            }
            int pos = Object2IntOpenHashMap.this.n;
            while (true) {
                int pos2 = pos - 1;
                if (pos != 0) {
                    if (Object2IntOpenHashMap.this.key[pos2] != null) {
                        consumer.accept(new MapEntry(pos2));
                    }
                    pos = pos2;
                } else {
                    return;
                }
            }
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap.FastEntrySet
        public void fastForEach(Consumer<? super Object2IntMap.Entry<K>> consumer) {
            Object2IntOpenHashMap<K>.MapEntry entry = new MapEntry();
            if (Object2IntOpenHashMap.this.containsNullKey) {
                entry.index = Object2IntOpenHashMap.this.n;
                consumer.accept(entry);
            }
            int pos = Object2IntOpenHashMap.this.n;
            while (true) {
                int pos2 = pos - 1;
                if (pos != 0) {
                    if (Object2IntOpenHashMap.this.key[pos2] != null) {
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

    @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, it.unimi.dsi.fastutil.objects.Object2IntSortedMap
    public Object2IntMap.FastEntrySet<K> object2IntEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet();
        }
        return this.entries;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class KeyIterator extends Object2IntOpenHashMap<K>.MapIterator<Consumer<? super K>> implements ObjectIterator<K> {
        @Override // java.util.Iterator
        public /* bridge */ /* synthetic */ void forEachRemaining(Consumer consumer) {
            super.forEachRemaining((KeyIterator) consumer);
        }

        public KeyIterator() {
            super();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap.MapIterator
        public final void acceptOnIndex(Consumer<? super K> consumer, int i) {
            consumer.accept(Object2IntOpenHashMap.this.key[i]);
        }

        @Override // java.util.Iterator
        public K next() {
            return Object2IntOpenHashMap.this.key[nextEntry()];
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class KeySpliterator extends Object2IntOpenHashMap<K>.MapSpliterator<Consumer<? super K>, Object2IntOpenHashMap<K>.KeySpliterator> implements ObjectSpliterator<K> {
        private static final int POST_SPLIT_CHARACTERISTICS = 1;

        @Override // java.util.Spliterator
        public /* bridge */ /* synthetic */ void forEachRemaining(Consumer consumer) {
            super.forEachRemaining((KeySpliterator) consumer);
        }

        @Override // java.util.Spliterator
        public /* bridge */ /* synthetic */ boolean tryAdvance(Consumer consumer) {
            return super.tryAdvance((KeySpliterator) consumer);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterator, java.util.Spliterator
        public /* bridge */ /* synthetic */ ObjectSpliterator trySplit() {
            return (ObjectSpliterator) super.trySplit();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterator, java.util.Spliterator
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
            return this.hasSplit ? 1 : 65;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap.MapSpliterator
        public final void acceptOnIndex(Consumer<? super K> consumer, int i) {
            consumer.accept(Object2IntOpenHashMap.this.key[i]);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap.MapSpliterator
        public final Object2IntOpenHashMap<K>.KeySpliterator makeForSplit(int pos, int max, boolean mustReturnNull) {
            return new KeySpliterator(pos, max, mustReturnNull, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class KeySet extends AbstractObjectSet<K> {
        private KeySet() {
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectSet, it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        public ObjectIterator<K> iterator() {
            return new KeyIterator();
        }

        @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        /* renamed from: spliterator */
        public ObjectSpliterator<K> mo221spliterator() {
            return new KeySpliterator();
        }

        @Override // java.lang.Iterable
        public void forEach(Consumer<? super K> consumer) {
            if (Object2IntOpenHashMap.this.containsNullKey) {
                consumer.accept(Object2IntOpenHashMap.this.key[Object2IntOpenHashMap.this.n]);
            }
            int i = Object2IntOpenHashMap.this.n;
            while (true) {
                int i2 = i - 1;
                if (i != 0) {
                    Manifest manifest = Object2IntOpenHashMap.this.key[i2];
                    if (manifest != null) {
                        consumer.accept(manifest);
                    }
                    i = i2;
                } else {
                    return;
                }
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return Object2IntOpenHashMap.this.size;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object k) {
            return Object2IntOpenHashMap.this.containsKey(k);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object k) {
            int oldSize = Object2IntOpenHashMap.this.size;
            Object2IntOpenHashMap.this.removeInt(k);
            return Object2IntOpenHashMap.this.size != oldSize;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            Object2IntOpenHashMap.this.clear();
        }
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObject2IntMap, it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
    public ObjectSet<K> keySet() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class ValueIterator extends Object2IntOpenHashMap<K>.MapIterator<IntConsumer> implements IntIterator {
        @Override // java.util.PrimitiveIterator.OfInt
        public /* bridge */ /* synthetic */ void forEachRemaining(IntConsumer intConsumer) {
            super.forEachRemaining((ValueIterator) intConsumer);
        }

        public ValueIterator() {
            super();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap.MapIterator
        public final void acceptOnIndex(IntConsumer action, int index) {
            action.accept(Object2IntOpenHashMap.this.value[index]);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt
        public int nextInt() {
            return Object2IntOpenHashMap.this.value[nextEntry()];
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class ValueSpliterator extends Object2IntOpenHashMap<K>.MapSpliterator<IntConsumer, Object2IntOpenHashMap<K>.ValueSpliterator> implements IntSpliterator {
        private static final int POST_SPLIT_CHARACTERISTICS = 256;

        @Override // java.util.Spliterator.OfInt
        public /* bridge */ /* synthetic */ void forEachRemaining(IntConsumer intConsumer) {
            super.forEachRemaining((ValueSpliterator) intConsumer);
        }

        @Override // java.util.Spliterator.OfInt
        public /* bridge */ /* synthetic */ boolean tryAdvance(IntConsumer intConsumer) {
            return super.tryAdvance((ValueSpliterator) intConsumer);
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

        ValueSpliterator() {
            super();
        }

        ValueSpliterator(int pos, int max, boolean mustReturnNull, boolean hasSplit) {
            super(pos, max, mustReturnNull, hasSplit);
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.hasSplit ? 256 : 320;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap.MapSpliterator
        public final void acceptOnIndex(IntConsumer action, int index) {
            action.accept(Object2IntOpenHashMap.this.value[index]);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap.MapSpliterator
        public final Object2IntOpenHashMap<K>.ValueSpliterator makeForSplit(int pos, int max, boolean mustReturnNull) {
            return new ValueSpliterator(pos, max, mustReturnNull, true);
        }
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObject2IntMap, it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
    /* renamed from: values */
    public Collection<Integer> values2() {
        if (this.values == null) {
            this.values = new AbstractIntCollection() { // from class: it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap.1
                @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
                public IntIterator iterator() {
                    return new ValueIterator();
                }

                @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
                public IntSpliterator spliterator() {
                    return new ValueSpliterator();
                }

                @Override // it.unimi.dsi.fastutil.ints.IntIterable
                public void forEach(IntConsumer consumer) {
                    if (Object2IntOpenHashMap.this.containsNullKey) {
                        consumer.accept(Object2IntOpenHashMap.this.value[Object2IntOpenHashMap.this.n]);
                    }
                    int pos = Object2IntOpenHashMap.this.n;
                    while (true) {
                        int pos2 = pos - 1;
                        if (pos != 0) {
                            if (Object2IntOpenHashMap.this.key[pos2] != null) {
                                consumer.accept(Object2IntOpenHashMap.this.value[pos2]);
                            }
                            pos = pos2;
                        } else {
                            return;
                        }
                    }
                }

                @Override // java.util.AbstractCollection, java.util.Collection
                public int size() {
                    return Object2IntOpenHashMap.this.size;
                }

                @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
                public boolean contains(int v) {
                    return Object2IntOpenHashMap.this.containsValue(v);
                }

                @Override // java.util.AbstractCollection, java.util.Collection
                public void clear() {
                    Object2IntOpenHashMap.this.clear();
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
        K[] kArr = this.key;
        int[] iArr = this.value;
        int i3 = i - 1;
        K[] kArr2 = (K[]) new Object[i + 1];
        int[] iArr2 = new int[i + 1];
        int i4 = this.n;
        int realSize = realSize();
        while (true) {
            int i5 = realSize - 1;
            if (realSize == 0) {
                iArr2[i] = iArr[this.n];
                this.n = i;
                this.mask = i3;
                this.maxFill = HashCommon.maxFill(this.n, this.f);
                this.key = kArr2;
                this.value = iArr2;
                return;
            }
            do {
                i4--;
            } while (kArr[i4] == null);
            int mix = HashCommon.mix(kArr[i4].hashCode()) & i3;
            int i6 = mix;
            if (kArr2[mix] == null) {
                kArr2[i6] = kArr[i4];
                iArr2[i6] = iArr[i4];
                realSize = i5;
            }
            do {
                i2 = (i6 + 1) & i3;
                i6 = i2;
            } while (kArr2[i2] != null);
            kArr2[i6] = kArr[i4];
            iArr2[i6] = iArr[i4];
            realSize = i5;
        }
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public Object2IntOpenHashMap<K> m262clone() {
        try {
            Object2IntOpenHashMap<K> object2IntOpenHashMap = (Object2IntOpenHashMap) super.clone();
            object2IntOpenHashMap.keys = null;
            object2IntOpenHashMap.values = null;
            object2IntOpenHashMap.entries = null;
            object2IntOpenHashMap.containsNullKey = this.containsNullKey;
            object2IntOpenHashMap.key = (K[]) ((Object[]) this.key.clone());
            object2IntOpenHashMap.value = (int[]) this.value.clone();
            return object2IntOpenHashMap;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObject2IntMap, java.util.Map
    public int hashCode() {
        int h = 0;
        int j = realSize();
        int i = 0;
        int t = 0;
        while (true) {
            int j2 = j - 1;
            if (j == 0) {
                break;
            }
            while (this.key[i] == null) {
                i++;
            }
            if (this != this.key[i]) {
                t = this.key[i].hashCode();
            }
            t ^= this.value[i];
            h += t;
            i++;
            j = j2;
        }
        return this.containsNullKey ? h + this.value[this.n] : h;
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        K[] key = this.key;
        int[] value = this.value;
        Object2IntOpenHashMap<K>.EntryIterator i = new EntryIterator();
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
        this.mask = this.n - 1;
        K[] kArr = (K[]) new Object[this.n + 1];
        this.key = kArr;
        int[] iArr = new int[this.n + 1];
        this.value = iArr;
        int i = this.size;
        while (true) {
            int i2 = i - 1;
            if (i != 0) {
                Object readObject = objectInputStream.readObject();
                int readInt = objectInputStream.readInt();
                if (readObject == null) {
                    mix = this.n;
                    this.containsNullKey = true;
                } else {
                    mix = HashCommon.mix(readObject.hashCode()) & this.mask;
                    while (kArr[mix] != 0) {
                        mix = (mix + 1) & this.mask;
                    }
                }
                kArr[mix] = readObject;
                iArr[mix] = readInt;
                i = i2;
            } else {
                return;
            }
        }
    }

    private void checkTable() {
    }
}
