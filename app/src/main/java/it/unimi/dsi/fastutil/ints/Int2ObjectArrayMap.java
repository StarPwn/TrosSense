package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntSpliterators;
import it.unimi.dsi.fastutil.objects.AbstractObjectCollection;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectArrays;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSpliterator;
import it.unimi.dsi.fastutil.objects.ObjectSpliterators;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/* loaded from: classes4.dex */
public class Int2ObjectArrayMap<V> extends AbstractInt2ObjectMap<V> implements Serializable, Cloneable {
    private static final long serialVersionUID = 1;
    protected transient Int2ObjectMap.FastEntrySet<V> entries;
    protected transient int[] key;
    protected transient IntSet keys;
    protected int size;
    protected transient Object[] value;
    protected transient ObjectCollection<V> values;

    public Int2ObjectArrayMap(int[] key, Object[] value) {
        this.key = key;
        this.value = value;
        this.size = key.length;
        if (key.length != value.length) {
            throw new IllegalArgumentException("Keys and values have different lengths (" + key.length + ", " + value.length + ")");
        }
    }

    public Int2ObjectArrayMap() {
        this.key = IntArrays.EMPTY_ARRAY;
        this.value = ObjectArrays.EMPTY_ARRAY;
    }

    public Int2ObjectArrayMap(int capacity) {
        this.key = new int[capacity];
        this.value = new Object[capacity];
    }

    public Int2ObjectArrayMap(Int2ObjectMap<V> m) {
        this(m.size());
        int i = 0;
        ObjectIterator<Int2ObjectMap.Entry<V>> it2 = m.int2ObjectEntrySet().iterator();
        while (it2.hasNext()) {
            Int2ObjectMap.Entry<V> e = it2.next();
            this.key[i] = e.getIntKey();
            this.value[i] = e.getValue();
            i++;
        }
        this.size = i;
    }

    public Int2ObjectArrayMap(Map<? extends Integer, ? extends V> m) {
        this(m.size());
        int i = 0;
        for (Map.Entry<? extends Integer, ? extends V> e : m.entrySet()) {
            this.key[i] = e.getKey().intValue();
            this.value[i] = e.getValue();
            i++;
        }
        this.size = i;
    }

    public Int2ObjectArrayMap(int[] key, Object[] value, int size) {
        this.key = key;
        this.value = value;
        this.size = size;
        if (key.length != value.length) {
            throw new IllegalArgumentException("Keys and values have different lengths (" + key.length + ", " + value.length + ")");
        }
        if (size > key.length) {
            throw new IllegalArgumentException("The provided size (" + size + ") is larger than or equal to the backing-arrays size (" + key.length + ")");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class EntrySet extends AbstractObjectSet<Int2ObjectMap.Entry<V>> implements Int2ObjectMap.FastEntrySet<V> {
        private EntrySet() {
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectSet, it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        public ObjectIterator<Int2ObjectMap.Entry<V>> iterator() {
            return new ObjectIterator<Int2ObjectMap.Entry<V>>() { // from class: it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap.EntrySet.1
                int curr = -1;
                int next = 0;

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return this.next < Int2ObjectArrayMap.this.size;
                }

                @Override // java.util.Iterator
                public Int2ObjectMap.Entry<V> next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    int[] iArr = Int2ObjectArrayMap.this.key;
                    int i = this.next;
                    this.curr = i;
                    int i2 = iArr[i];
                    Object[] objArr = Int2ObjectArrayMap.this.value;
                    int i3 = this.next;
                    this.next = i3 + 1;
                    return new AbstractInt2ObjectMap.BasicEntry(i2, objArr[i3]);
                }

                @Override // java.util.Iterator
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    Int2ObjectArrayMap int2ObjectArrayMap = Int2ObjectArrayMap.this;
                    int i = int2ObjectArrayMap.size;
                    int2ObjectArrayMap.size = i - 1;
                    int i2 = this.next;
                    this.next = i2 - 1;
                    int tail = i - i2;
                    System.arraycopy(Int2ObjectArrayMap.this.key, this.next + 1, Int2ObjectArrayMap.this.key, this.next, tail);
                    System.arraycopy(Int2ObjectArrayMap.this.value, this.next + 1, Int2ObjectArrayMap.this.value, this.next, tail);
                    Int2ObjectArrayMap.this.value[Int2ObjectArrayMap.this.size] = null;
                }

                @Override // java.util.Iterator
                public void forEachRemaining(Consumer<? super Int2ObjectMap.Entry<V>> action) {
                    int max = Int2ObjectArrayMap.this.size;
                    while (this.next < max) {
                        int[] iArr = Int2ObjectArrayMap.this.key;
                        int i = this.next;
                        this.curr = i;
                        int i2 = iArr[i];
                        Object[] objArr = Int2ObjectArrayMap.this.value;
                        int i3 = this.next;
                        this.next = i3 + 1;
                        action.accept(new AbstractInt2ObjectMap.BasicEntry(i2, objArr[i3]));
                    }
                }
            };
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap.FastEntrySet
        public ObjectIterator<Int2ObjectMap.Entry<V>> fastIterator() {
            return new ObjectIterator<Int2ObjectMap.Entry<V>>() { // from class: it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap.EntrySet.2
                int next = 0;
                int curr = -1;
                final AbstractInt2ObjectMap.BasicEntry<V> entry = new AbstractInt2ObjectMap.BasicEntry<>();

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return this.next < Int2ObjectArrayMap.this.size;
                }

                @Override // java.util.Iterator
                public Int2ObjectMap.Entry<V> next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    AbstractInt2ObjectMap.BasicEntry<V> basicEntry = this.entry;
                    int[] iArr = Int2ObjectArrayMap.this.key;
                    int i = this.next;
                    this.curr = i;
                    basicEntry.key = iArr[i];
                    AbstractInt2ObjectMap.BasicEntry<V> basicEntry2 = this.entry;
                    Object[] objArr = Int2ObjectArrayMap.this.value;
                    int i2 = this.next;
                    this.next = i2 + 1;
                    basicEntry2.value = (V) objArr[i2];
                    return this.entry;
                }

                @Override // java.util.Iterator
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    Int2ObjectArrayMap int2ObjectArrayMap = Int2ObjectArrayMap.this;
                    int i = int2ObjectArrayMap.size;
                    int2ObjectArrayMap.size = i - 1;
                    int i2 = this.next;
                    this.next = i2 - 1;
                    int tail = i - i2;
                    System.arraycopy(Int2ObjectArrayMap.this.key, this.next + 1, Int2ObjectArrayMap.this.key, this.next, tail);
                    System.arraycopy(Int2ObjectArrayMap.this.value, this.next + 1, Int2ObjectArrayMap.this.value, this.next, tail);
                    Int2ObjectArrayMap.this.value[Int2ObjectArrayMap.this.size] = null;
                }

                @Override // java.util.Iterator
                public void forEachRemaining(Consumer<? super Int2ObjectMap.Entry<V>> consumer) {
                    int i = Int2ObjectArrayMap.this.size;
                    while (this.next < i) {
                        AbstractInt2ObjectMap.BasicEntry<V> basicEntry = this.entry;
                        int[] iArr = Int2ObjectArrayMap.this.key;
                        int i2 = this.next;
                        this.curr = i2;
                        basicEntry.key = iArr[i2];
                        AbstractInt2ObjectMap.BasicEntry<V> basicEntry2 = this.entry;
                        Object[] objArr = Int2ObjectArrayMap.this.value;
                        int i3 = this.next;
                        this.next = i3 + 1;
                        basicEntry2.value = (V) objArr[i3];
                        consumer.accept(this.entry);
                    }
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes4.dex */
        public final class EntrySetSpliterator extends ObjectSpliterators.EarlyBindingSizeIndexBasedSpliterator<Int2ObjectMap.Entry<V>> implements ObjectSpliterator<Int2ObjectMap.Entry<V>> {
            EntrySetSpliterator(int pos, int maxPos) {
                super(pos, maxPos);
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterators.AbstractIndexBasedSpliterator, java.util.Spliterator
            public int characteristics() {
                return 16465;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterators.AbstractIndexBasedSpliterator
            public final Int2ObjectMap.Entry<V> get(int location) {
                return new AbstractInt2ObjectMap.BasicEntry(Int2ObjectArrayMap.this.key[location], Int2ObjectArrayMap.this.value[location]);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterators.AbstractIndexBasedSpliterator
            public final Int2ObjectArrayMap<V>.EntrySet.EntrySetSpliterator makeForSplit(int pos, int maxPos) {
                return new EntrySetSpliterator(pos, maxPos);
            }
        }

        @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        /* renamed from: spliterator */
        public ObjectSpliterator<Int2ObjectMap.Entry<V>> mo221spliterator() {
            return new EntrySetSpliterator(0, Int2ObjectArrayMap.this.size);
        }

        @Override // java.lang.Iterable
        public void forEach(Consumer<? super Int2ObjectMap.Entry<V>> action) {
            int max = Int2ObjectArrayMap.this.size;
            for (int i = 0; i < max; i++) {
                action.accept(new AbstractInt2ObjectMap.BasicEntry(Int2ObjectArrayMap.this.key[i], Int2ObjectArrayMap.this.value[i]));
            }
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap.FastEntrySet
        public void fastForEach(Consumer<? super Int2ObjectMap.Entry<V>> consumer) {
            AbstractInt2ObjectMap.BasicEntry basicEntry = new AbstractInt2ObjectMap.BasicEntry();
            int i = Int2ObjectArrayMap.this.size;
            for (int i2 = 0; i2 < i; i2++) {
                basicEntry.key = Int2ObjectArrayMap.this.key[i2];
                basicEntry.value = (V) Int2ObjectArrayMap.this.value[i2];
                consumer.accept(basicEntry);
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return Int2ObjectArrayMap.this.size;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> e = (Map.Entry) o;
            if (e.getKey() == null || !(e.getKey() instanceof Integer)) {
                return false;
            }
            int k = ((Integer) e.getKey()).intValue();
            return Int2ObjectArrayMap.this.containsKey(k) && Objects.equals(Int2ObjectArrayMap.this.get(k), e.getValue());
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
            int oldPos = Int2ObjectArrayMap.this.findKey(k);
            if (oldPos == -1 || !Objects.equals(value, Int2ObjectArrayMap.this.value[oldPos])) {
                return false;
            }
            int tail = (Int2ObjectArrayMap.this.size - oldPos) - 1;
            System.arraycopy(Int2ObjectArrayMap.this.key, oldPos + 1, Int2ObjectArrayMap.this.key, oldPos, tail);
            System.arraycopy(Int2ObjectArrayMap.this.value, oldPos + 1, Int2ObjectArrayMap.this.value, oldPos, tail);
            Int2ObjectArrayMap.this.size--;
            Int2ObjectArrayMap.this.value[Int2ObjectArrayMap.this.size] = null;
            return true;
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap, it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
    public Int2ObjectMap.FastEntrySet<V> int2ObjectEntrySet() {
        if (this.entries == null) {
            this.entries = new EntrySet();
        }
        return this.entries;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int findKey(int k) {
        int[] key = this.key;
        int i = this.size;
        while (true) {
            int i2 = i - 1;
            if (i == 0) {
                return -1;
            }
            if (key[i2] == k) {
                return i2;
            }
            i = i2;
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
    public V get(int i) {
        int[] iArr = this.key;
        int i2 = this.size;
        while (true) {
            int i3 = i2 - 1;
            if (i2 == 0) {
                return this.defRetValue;
            }
            if (iArr[i3] == i) {
                return (V) this.value[i3];
            }
            i2 = i3;
        }
    }

    @Override // it.unimi.dsi.fastutil.Function, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
    public int size() {
        return this.size;
    }

    @Override // it.unimi.dsi.fastutil.Function, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
    public void clear() {
        int i = this.size;
        while (true) {
            int i2 = i - 1;
            if (i != 0) {
                this.value[i2] = null;
                i = i2;
            } else {
                this.size = 0;
                return;
            }
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap, it.unimi.dsi.fastutil.ints.Int2ObjectFunction, it.unimi.dsi.fastutil.ints.Int2ObjectMap
    public boolean containsKey(int k) {
        return findKey(k) != -1;
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap, java.util.Map
    public boolean containsValue(Object v) {
        int i = this.size;
        while (true) {
            int i2 = i - 1;
            if (i == 0) {
                return false;
            }
            if (Objects.equals(this.value[i2], v)) {
                return true;
            }
            i = i2;
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap, java.util.Map
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
    public V put(int i, V v) {
        int findKey = findKey(i);
        if (findKey != -1) {
            V v2 = (V) this.value[findKey];
            this.value[findKey] = v;
            return v2;
        }
        if (this.size == this.key.length) {
            int[] iArr = new int[this.size == 0 ? 2 : this.size * 2];
            Object[] objArr = new Object[this.size != 0 ? 2 * this.size : 2];
            int i2 = this.size;
            while (true) {
                int i3 = i2 - 1;
                if (i2 == 0) {
                    break;
                }
                iArr[i3] = this.key[i3];
                objArr[i3] = this.value[i3];
                i2 = i3;
            }
            this.key = iArr;
            this.value = objArr;
        }
        this.key[this.size] = i;
        this.value[this.size] = v;
        this.size++;
        return this.defRetValue;
    }

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
    public V remove(int i) {
        int findKey = findKey(i);
        if (findKey == -1) {
            return this.defRetValue;
        }
        V v = (V) this.value[findKey];
        int i2 = (this.size - findKey) - 1;
        System.arraycopy(this.key, findKey + 1, this.key, findKey, i2);
        System.arraycopy(this.value, findKey + 1, this.value, findKey, i2);
        this.size--;
        this.value[this.size] = null;
        return v;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class KeySet extends AbstractIntSet {
        private KeySet() {
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public boolean contains(int k) {
            return Int2ObjectArrayMap.this.findKey(k) != -1;
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntSet, it.unimi.dsi.fastutil.ints.IntSet
        public boolean remove(int k) {
            int oldPos = Int2ObjectArrayMap.this.findKey(k);
            if (oldPos == -1) {
                return false;
            }
            int tail = (Int2ObjectArrayMap.this.size - oldPos) - 1;
            System.arraycopy(Int2ObjectArrayMap.this.key, oldPos + 1, Int2ObjectArrayMap.this.key, oldPos, tail);
            System.arraycopy(Int2ObjectArrayMap.this.value, oldPos + 1, Int2ObjectArrayMap.this.value, oldPos, tail);
            Int2ObjectArrayMap.this.size--;
            Int2ObjectArrayMap.this.value[Int2ObjectArrayMap.this.size] = null;
            return true;
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntSet, it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
        public IntIterator iterator() {
            return new IntIterator() { // from class: it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap.KeySet.1
                int pos = 0;

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return this.pos < Int2ObjectArrayMap.this.size;
                }

                @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt
                public int nextInt() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    int[] iArr = Int2ObjectArrayMap.this.key;
                    int i = this.pos;
                    this.pos = i + 1;
                    return iArr[i];
                }

                @Override // java.util.Iterator
                public void remove() {
                    if (this.pos == 0) {
                        throw new IllegalStateException();
                    }
                    int tail = Int2ObjectArrayMap.this.size - this.pos;
                    System.arraycopy(Int2ObjectArrayMap.this.key, this.pos, Int2ObjectArrayMap.this.key, this.pos - 1, tail);
                    System.arraycopy(Int2ObjectArrayMap.this.value, this.pos, Int2ObjectArrayMap.this.value, this.pos - 1, tail);
                    Int2ObjectArrayMap int2ObjectArrayMap = Int2ObjectArrayMap.this;
                    int2ObjectArrayMap.size--;
                    this.pos--;
                    Int2ObjectArrayMap.this.value[Int2ObjectArrayMap.this.size] = null;
                }

                @Override // java.util.PrimitiveIterator
                public void forEachRemaining(java.util.function.IntConsumer action) {
                    int max = Int2ObjectArrayMap.this.size;
                    while (this.pos < max) {
                        int[] iArr = Int2ObjectArrayMap.this.key;
                        int i = this.pos;
                        this.pos = i + 1;
                        action.accept(iArr[i]);
                    }
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes4.dex */
        public final class KeySetSpliterator extends IntSpliterators.EarlyBindingSizeIndexBasedSpliterator implements IntSpliterator {
            KeySetSpliterator(int pos, int maxPos) {
                super(pos, maxPos);
            }

            @Override // it.unimi.dsi.fastutil.ints.IntSpliterators.AbstractIndexBasedSpliterator, java.util.Spliterator
            public int characteristics() {
                return 16721;
            }

            @Override // it.unimi.dsi.fastutil.ints.IntSpliterators.AbstractIndexBasedSpliterator
            protected final int get(int location) {
                return Int2ObjectArrayMap.this.key[location];
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // it.unimi.dsi.fastutil.ints.IntSpliterators.AbstractIndexBasedSpliterator
            public final Int2ObjectArrayMap<V>.KeySet.KeySetSpliterator makeForSplit(int pos, int maxPos) {
                return new KeySetSpliterator(pos, maxPos);
            }

            @Override // it.unimi.dsi.fastutil.ints.IntSpliterators.AbstractIndexBasedSpliterator, java.util.Spliterator.OfPrimitive
            public void forEachRemaining(java.util.function.IntConsumer action) {
                int max = Int2ObjectArrayMap.this.size;
                while (this.pos < max) {
                    int[] iArr = Int2ObjectArrayMap.this.key;
                    int i = this.pos;
                    this.pos = i + 1;
                    action.accept(iArr[i]);
                }
            }
        }

        @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
        public IntSpliterator spliterator() {
            return new KeySetSpliterator(0, Int2ObjectArrayMap.this.size);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntIterable
        public void forEach(java.util.function.IntConsumer action) {
            int max = Int2ObjectArrayMap.this.size;
            for (int i = 0; i < max; i++) {
                action.accept(Int2ObjectArrayMap.this.key[i]);
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return Int2ObjectArrayMap.this.size;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            Int2ObjectArrayMap.this.clear();
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
    /* renamed from: keySet, reason: merged with bridge method [inline-methods] */
    public Set<Integer> keySet2() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class ValuesCollection extends AbstractObjectCollection<V> {
        private ValuesCollection() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object v) {
            return Int2ObjectArrayMap.this.containsValue(v);
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        public ObjectIterator<V> iterator() {
            return new ObjectIterator<V>() { // from class: it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap.ValuesCollection.1
                int pos = 0;

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return this.pos < Int2ObjectArrayMap.this.size;
                }

                @Override // java.util.Iterator
                public V next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    Object[] objArr = Int2ObjectArrayMap.this.value;
                    int i = this.pos;
                    this.pos = i + 1;
                    return (V) objArr[i];
                }

                @Override // java.util.Iterator
                public void remove() {
                    if (this.pos == 0) {
                        throw new IllegalStateException();
                    }
                    int tail = Int2ObjectArrayMap.this.size - this.pos;
                    System.arraycopy(Int2ObjectArrayMap.this.key, this.pos, Int2ObjectArrayMap.this.key, this.pos - 1, tail);
                    System.arraycopy(Int2ObjectArrayMap.this.value, this.pos, Int2ObjectArrayMap.this.value, this.pos - 1, tail);
                    Int2ObjectArrayMap int2ObjectArrayMap = Int2ObjectArrayMap.this;
                    int2ObjectArrayMap.size--;
                    this.pos--;
                    Int2ObjectArrayMap.this.value[Int2ObjectArrayMap.this.size] = null;
                }

                @Override // java.util.Iterator
                public void forEachRemaining(Consumer<? super V> action) {
                    int max = Int2ObjectArrayMap.this.size;
                    while (this.pos < max) {
                        Object[] objArr = Int2ObjectArrayMap.this.value;
                        int i = this.pos;
                        this.pos = i + 1;
                        action.accept(objArr[i]);
                    }
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes4.dex */
        public final class ValuesSpliterator extends ObjectSpliterators.EarlyBindingSizeIndexBasedSpliterator<V> implements ObjectSpliterator<V> {
            ValuesSpliterator(int pos, int maxPos) {
                super(pos, maxPos);
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterators.AbstractIndexBasedSpliterator, java.util.Spliterator
            public int characteristics() {
                return ObjectSpliterators.LIST_SPLITERATOR_CHARACTERISTICS;
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterators.AbstractIndexBasedSpliterator
            protected final V get(int i) {
                return (V) Int2ObjectArrayMap.this.value[i];
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterators.AbstractIndexBasedSpliterator
            public final Int2ObjectArrayMap<V>.ValuesCollection.ValuesSpliterator makeForSplit(int pos, int maxPos) {
                return new ValuesSpliterator(pos, maxPos);
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterators.AbstractIndexBasedSpliterator, java.util.Spliterator
            public void forEachRemaining(Consumer<? super V> action) {
                int max = Int2ObjectArrayMap.this.size;
                while (this.pos < max) {
                    Object[] objArr = Int2ObjectArrayMap.this.value;
                    int i = this.pos;
                    this.pos = i + 1;
                    action.accept(objArr[i]);
                }
            }
        }

        @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        /* renamed from: spliterator */
        public ObjectSpliterator<V> mo221spliterator() {
            return new ValuesSpliterator(0, Int2ObjectArrayMap.this.size);
        }

        @Override // java.lang.Iterable
        public void forEach(Consumer<? super V> action) {
            int max = Int2ObjectArrayMap.this.size;
            for (int i = 0; i < max; i++) {
                action.accept(Int2ObjectArrayMap.this.value[i]);
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return Int2ObjectArrayMap.this.size;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            Int2ObjectArrayMap.this.clear();
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
    public ObjectCollection<V> values() {
        if (this.values == null) {
            this.values = new ValuesCollection();
        }
        return this.values;
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public Int2ObjectArrayMap<V> m219clone() {
        try {
            Int2ObjectArrayMap<V> c = (Int2ObjectArrayMap) super.clone();
            c.key = (int[]) this.key.clone();
            c.value = (Object[]) this.value.clone();
            c.entries = null;
            c.keys = null;
            c.values = null;
            return c;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        int max = this.size;
        for (int i = 0; i < max; i++) {
            s.writeInt(this.key[i]);
            s.writeObject(this.value[i]);
        }
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.key = new int[this.size];
        this.value = new Object[this.size];
        for (int i = 0; i < this.size; i++) {
            this.key[i] = s.readInt();
            this.value[i] = s.readObject();
        }
    }
}
