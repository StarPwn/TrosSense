package io.netty.util.collection;

import io.netty.util.collection.CharObjectMap;
import io.netty.util.internal.MathUtil;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/* loaded from: classes4.dex */
public class CharObjectHashMap<V> implements CharObjectMap<V> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final int DEFAULT_CAPACITY = 8;
    public static final float DEFAULT_LOAD_FACTOR = 0.5f;
    private static final Object NULL_VALUE = new Object();
    private final Iterable<CharObjectMap.PrimitiveEntry<V>> entries;
    private final Set<Map.Entry<Character, V>> entrySet;
    private final Set<Character> keySet;
    private char[] keys;
    private final float loadFactor;
    private int mask;
    private int maxSize;
    private int size;
    private V[] values;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Map
    public /* bridge */ /* synthetic */ Object put(Character ch, Object obj) {
        return put2(ch, (Character) obj);
    }

    public CharObjectHashMap() {
        this(8, 0.5f);
    }

    public CharObjectHashMap(int initialCapacity) {
        this(initialCapacity, 0.5f);
    }

    public CharObjectHashMap(int i, float f) {
        this.keySet = new KeySet();
        this.entrySet = new EntrySet();
        this.entries = new Iterable<CharObjectMap.PrimitiveEntry<V>>() { // from class: io.netty.util.collection.CharObjectHashMap.1
            @Override // java.lang.Iterable
            public Iterator<CharObjectMap.PrimitiveEntry<V>> iterator() {
                return new PrimitiveIterator();
            }
        };
        if (f <= 0.0f || f > 1.0f) {
            throw new IllegalArgumentException("loadFactor must be > 0 and <= 1");
        }
        this.loadFactor = f;
        int safeFindNextPositivePowerOfTwo = MathUtil.safeFindNextPositivePowerOfTwo(i);
        this.mask = safeFindNextPositivePowerOfTwo - 1;
        this.keys = new char[safeFindNextPositivePowerOfTwo];
        this.values = (V[]) new Object[safeFindNextPositivePowerOfTwo];
        this.maxSize = calcMaxSize(safeFindNextPositivePowerOfTwo);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T> T toExternal(T value) {
        if (value == null) {
            throw new AssertionError("null is not a legitimate internal value. Concurrent Modification?");
        }
        if (value == NULL_VALUE) {
            return null;
        }
        return value;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T> T toInternal(T t) {
        return t == null ? (T) NULL_VALUE : t;
    }

    @Override // io.netty.util.collection.CharObjectMap
    public V get(char c) {
        int indexOf = indexOf(c);
        if (indexOf == -1) {
            return null;
        }
        return (V) toExternal(this.values[indexOf]);
    }

    @Override // io.netty.util.collection.CharObjectMap
    public V put(char c, V v) {
        int hashIndex = hashIndex(c);
        int i = hashIndex;
        while (this.values[i] != null) {
            if (this.keys[i] == c) {
                V v2 = this.values[i];
                ((V[]) this.values)[i] = toInternal(v);
                return (V) toExternal(v2);
            }
            int probeNext = probeNext(i);
            i = probeNext;
            if (probeNext == hashIndex) {
                throw new IllegalStateException("Unable to insert");
            }
        }
        this.keys[i] = c;
        ((V[]) this.values)[i] = toInternal(v);
        growSize();
        return null;
    }

    @Override // java.util.Map
    public void putAll(Map<? extends Character, ? extends V> sourceMap) {
        if (sourceMap instanceof CharObjectHashMap) {
            CharObjectHashMap<V> source = (CharObjectHashMap) sourceMap;
            for (int i = 0; i < source.values.length; i++) {
                V sourceValue = source.values[i];
                if (sourceValue != null) {
                    put(source.keys[i], (char) sourceValue);
                }
            }
            return;
        }
        for (Map.Entry<? extends Character, ? extends V> entry : sourceMap.entrySet()) {
            put2(entry.getKey(), (Character) entry.getValue());
        }
    }

    @Override // io.netty.util.collection.CharObjectMap
    public V remove(char c) {
        int indexOf = indexOf(c);
        if (indexOf == -1) {
            return null;
        }
        V v = this.values[indexOf];
        removeAt(indexOf);
        return (V) toExternal(v);
    }

    @Override // java.util.Map
    public int size() {
        return this.size;
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override // java.util.Map
    public void clear() {
        Arrays.fill(this.keys, (char) 0);
        Arrays.fill(this.values, (Object) null);
        this.size = 0;
    }

    @Override // io.netty.util.collection.CharObjectMap
    public boolean containsKey(char key) {
        return indexOf(key) >= 0;
    }

    @Override // java.util.Map
    public boolean containsValue(Object value) {
        Object internal = toInternal(value);
        for (V v2 : this.values) {
            if (v2 != null && v2.equals(internal)) {
                return true;
            }
        }
        return false;
    }

    @Override // io.netty.util.collection.CharObjectMap
    public Iterable<CharObjectMap.PrimitiveEntry<V>> entries() {
        return this.entries;
    }

    @Override // java.util.Map
    public Collection<V> values() {
        return new AbstractCollection<V>() { // from class: io.netty.util.collection.CharObjectHashMap.2
            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
            public Iterator<V> iterator() {
                return new Iterator<V>() { // from class: io.netty.util.collection.CharObjectHashMap.2.1
                    final CharObjectHashMap<V>.PrimitiveIterator iter;

                    {
                        this.iter = new PrimitiveIterator();
                    }

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return this.iter.hasNext();
                    }

                    @Override // java.util.Iterator
                    public V next() {
                        return this.iter.next().value();
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        this.iter.remove();
                    }
                };
            }

            @Override // java.util.AbstractCollection, java.util.Collection
            public int size() {
                return CharObjectHashMap.this.size;
            }
        };
    }

    @Override // java.util.Map
    public int hashCode() {
        int hash = this.size;
        for (char key : this.keys) {
            hash ^= hashCode(key);
        }
        return hash;
    }

    @Override // java.util.Map
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CharObjectMap)) {
            return false;
        }
        CharObjectMap other = (CharObjectMap) obj;
        if (this.size != other.size()) {
            return false;
        }
        for (int i = 0; i < this.values.length; i++) {
            V value = this.values[i];
            if (value != null) {
                char key = this.keys[i];
                Object otherValue = other.get(key);
                if (value == NULL_VALUE) {
                    if (otherValue != null) {
                        return false;
                    }
                } else if (!value.equals(otherValue)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override // java.util.Map
    public boolean containsKey(Object key) {
        return containsKey(objectToKey(key));
    }

    @Override // java.util.Map
    public V get(Object key) {
        return get(objectToKey(key));
    }

    /* renamed from: put, reason: avoid collision after fix types in other method */
    public V put2(Character key, V value) {
        return put(objectToKey(key), (char) value);
    }

    @Override // java.util.Map
    public V remove(Object key) {
        return remove(objectToKey(key));
    }

    @Override // java.util.Map
    public Set<Character> keySet() {
        return this.keySet;
    }

    @Override // java.util.Map
    public Set<Map.Entry<Character, V>> entrySet() {
        return this.entrySet;
    }

    private char objectToKey(Object key) {
        return ((Character) key).charValue();
    }

    private int indexOf(char key) {
        int startIndex = hashIndex(key);
        int index = startIndex;
        while (this.values[index] != null) {
            if (key == this.keys[index]) {
                return index;
            }
            int probeNext = probeNext(index);
            index = probeNext;
            if (probeNext == startIndex) {
                return -1;
            }
        }
        return -1;
    }

    private int hashIndex(char key) {
        return hashCode(key) & this.mask;
    }

    private static int hashCode(char key) {
        return key;
    }

    private int probeNext(int index) {
        return (index + 1) & this.mask;
    }

    private void growSize() {
        this.size++;
        if (this.size > this.maxSize) {
            if (this.keys.length == Integer.MAX_VALUE) {
                throw new IllegalStateException("Max capacity reached at size=" + this.size);
            }
            rehash(this.keys.length << 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean removeAt(int index) {
        this.size--;
        this.keys[index] = 0;
        this.values[index] = null;
        int nextFree = index;
        int i = probeNext(index);
        V value = this.values[i];
        while (value != null) {
            char key = this.keys[i];
            int bucket = hashIndex(key);
            if ((i < bucket && (bucket <= nextFree || nextFree <= i)) || (bucket <= nextFree && nextFree <= i)) {
                this.keys[nextFree] = key;
                this.values[nextFree] = value;
                this.keys[i] = 0;
                this.values[i] = null;
                nextFree = i;
            }
            V[] vArr = this.values;
            int probeNext = probeNext(i);
            i = probeNext;
            value = vArr[probeNext];
        }
        return nextFree != index;
    }

    private int calcMaxSize(int capacity) {
        int upperBound = capacity - 1;
        return Math.min(upperBound, (int) (capacity * this.loadFactor));
    }

    private void rehash(int i) {
        char[] cArr = this.keys;
        V[] vArr = this.values;
        this.keys = new char[i];
        this.values = (V[]) new Object[i];
        this.maxSize = calcMaxSize(i);
        this.mask = i - 1;
        for (int i2 = 0; i2 < vArr.length; i2++) {
            V v = vArr[i2];
            if (v != null) {
                char c = cArr[i2];
                int hashIndex = hashIndex(c);
                while (this.values[hashIndex] != null) {
                    hashIndex = probeNext(hashIndex);
                }
                this.keys[hashIndex] = c;
                this.values[hashIndex] = v;
            }
        }
    }

    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder(this.size * 4);
        sb.append('{');
        boolean first = true;
        for (int i = 0; i < this.values.length; i++) {
            V value = this.values[i];
            if (value != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(keyToString(this.keys[i])).append('=').append(value == this ? "(this Map)" : toExternal(value));
                first = false;
            }
        }
        return sb.append('}').toString();
    }

    protected String keyToString(char key) {
        return Character.toString(key);
    }

    /* loaded from: classes4.dex */
    private final class EntrySet extends AbstractSet<Map.Entry<Character, V>> {
        private EntrySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<Character, V>> iterator() {
            return new MapIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return CharObjectHashMap.this.size();
        }
    }

    /* loaded from: classes4.dex */
    private final class KeySet extends AbstractSet<Character> {
        private KeySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return CharObjectHashMap.this.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            return CharObjectHashMap.this.containsKey(o);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            return CharObjectHashMap.this.remove(o) != null;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean retainAll(Collection<?> retainedKeys) {
            boolean changed = false;
            Iterator<CharObjectMap.PrimitiveEntry<V>> iter = CharObjectHashMap.this.entries().iterator();
            while (iter.hasNext()) {
                CharObjectMap.PrimitiveEntry<V> entry = iter.next();
                if (!retainedKeys.contains(Character.valueOf(entry.key()))) {
                    changed = true;
                    iter.remove();
                }
            }
            return changed;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            CharObjectHashMap.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Character> iterator() {
            return new Iterator<Character>() { // from class: io.netty.util.collection.CharObjectHashMap.KeySet.1
                private final Iterator<Map.Entry<Character, V>> iter;

                {
                    this.iter = CharObjectHashMap.this.entrySet.iterator();
                }

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return this.iter.hasNext();
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.Iterator
                public Character next() {
                    return this.iter.next().getKey();
                }

                @Override // java.util.Iterator
                public void remove() {
                    this.iter.remove();
                }
            };
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class PrimitiveIterator implements Iterator<CharObjectMap.PrimitiveEntry<V>>, CharObjectMap.PrimitiveEntry<V> {
        private int entryIndex;
        private int nextIndex;
        private int prevIndex;

        private PrimitiveIterator() {
            this.prevIndex = -1;
            this.nextIndex = -1;
            this.entryIndex = -1;
        }

        private void scanNext() {
            do {
                int i = this.nextIndex + 1;
                this.nextIndex = i;
                if (i == CharObjectHashMap.this.values.length) {
                    return;
                }
            } while (CharObjectHashMap.this.values[this.nextIndex] == null);
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.nextIndex == -1) {
                scanNext();
            }
            return this.nextIndex != CharObjectHashMap.this.values.length;
        }

        @Override // java.util.Iterator
        public CharObjectMap.PrimitiveEntry<V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            this.prevIndex = this.nextIndex;
            scanNext();
            this.entryIndex = this.prevIndex;
            return this;
        }

        @Override // java.util.Iterator
        public void remove() {
            if (this.prevIndex != -1) {
                if (CharObjectHashMap.this.removeAt(this.prevIndex)) {
                    this.nextIndex = this.prevIndex;
                }
                this.prevIndex = -1;
                return;
            }
            throw new IllegalStateException("next must be called before each remove.");
        }

        @Override // io.netty.util.collection.CharObjectMap.PrimitiveEntry
        public char key() {
            return CharObjectHashMap.this.keys[this.entryIndex];
        }

        @Override // io.netty.util.collection.CharObjectMap.PrimitiveEntry
        public V value() {
            return (V) CharObjectHashMap.toExternal(CharObjectHashMap.this.values[this.entryIndex]);
        }

        @Override // io.netty.util.collection.CharObjectMap.PrimitiveEntry
        public void setValue(V value) {
            CharObjectHashMap.this.values[this.entryIndex] = CharObjectHashMap.toInternal(value);
        }
    }

    /* loaded from: classes4.dex */
    private final class MapIterator implements Iterator<Map.Entry<Character, V>> {
        private final CharObjectHashMap<V>.PrimitiveIterator iter;

        private MapIterator() {
            this.iter = new PrimitiveIterator();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.iter.hasNext();
        }

        @Override // java.util.Iterator
        public Map.Entry<Character, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            this.iter.next();
            return new MapEntry(((PrimitiveIterator) this.iter).entryIndex);
        }

        @Override // java.util.Iterator
        public void remove() {
            this.iter.remove();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public final class MapEntry implements Map.Entry<Character, V> {
        private final int entryIndex;

        MapEntry(int entryIndex) {
            this.entryIndex = entryIndex;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Map.Entry
        public Character getKey() {
            verifyExists();
            return Character.valueOf(CharObjectHashMap.this.keys[this.entryIndex]);
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            verifyExists();
            return (V) CharObjectHashMap.toExternal(CharObjectHashMap.this.values[this.entryIndex]);
        }

        @Override // java.util.Map.Entry
        public V setValue(V v) {
            verifyExists();
            V v2 = (V) CharObjectHashMap.toExternal(CharObjectHashMap.this.values[this.entryIndex]);
            CharObjectHashMap.this.values[this.entryIndex] = CharObjectHashMap.toInternal(v);
            return v2;
        }

        private void verifyExists() {
            if (CharObjectHashMap.this.values[this.entryIndex] == null) {
                throw new IllegalStateException("The map entry has been removed");
            }
        }
    }
}
