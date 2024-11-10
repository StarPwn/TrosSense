package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.longs.AbstractLong2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongSpliterators;
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
public class Long2ObjectArrayMap<V> extends AbstractLong2ObjectMap<V> implements Serializable, Cloneable {
    private static final long serialVersionUID = 1;
    protected transient Long2ObjectMap.FastEntrySet<V> entries;
    protected transient long[] key;
    protected transient LongSet keys;
    protected int size;
    protected transient Object[] value;
    protected transient ObjectCollection<V> values;

    public Long2ObjectArrayMap(long[] key, Object[] value) {
        this.key = key;
        this.value = value;
        this.size = key.length;
        if (key.length != value.length) {
            throw new IllegalArgumentException("Keys and values have different lengths (" + key.length + ", " + value.length + ")");
        }
    }

    public Long2ObjectArrayMap() {
        this.key = LongArrays.EMPTY_ARRAY;
        this.value = ObjectArrays.EMPTY_ARRAY;
    }

    public Long2ObjectArrayMap(int capacity) {
        this.key = new long[capacity];
        this.value = new Object[capacity];
    }

    public Long2ObjectArrayMap(Long2ObjectMap<V> m) {
        this(m.size());
        int i = 0;
        ObjectIterator<Long2ObjectMap.Entry<V>> it2 = m.long2ObjectEntrySet().iterator();
        while (it2.hasNext()) {
            Long2ObjectMap.Entry<V> e = it2.next();
            this.key[i] = e.getLongKey();
            this.value[i] = e.getValue();
            i++;
        }
        this.size = i;
    }

    public Long2ObjectArrayMap(Map<? extends Long, ? extends V> m) {
        this(m.size());
        int i = 0;
        for (Map.Entry<? extends Long, ? extends V> e : m.entrySet()) {
            this.key[i] = e.getKey().longValue();
            this.value[i] = e.getValue();
            i++;
        }
        this.size = i;
    }

    public Long2ObjectArrayMap(long[] key, Object[] value, int size) {
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
    public final class EntrySet extends AbstractObjectSet<Long2ObjectMap.Entry<V>> implements Long2ObjectMap.FastEntrySet<V> {
        private EntrySet() {
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectSet, it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        public ObjectIterator<Long2ObjectMap.Entry<V>> iterator() {
            return new ObjectIterator<Long2ObjectMap.Entry<V>>() { // from class: it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap.EntrySet.1
                int curr = -1;
                int next = 0;

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return this.next < Long2ObjectArrayMap.this.size;
                }

                @Override // java.util.Iterator
                public Long2ObjectMap.Entry<V> next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    long[] jArr = Long2ObjectArrayMap.this.key;
                    int i = this.next;
                    this.curr = i;
                    long j = jArr[i];
                    Object[] objArr = Long2ObjectArrayMap.this.value;
                    int i2 = this.next;
                    this.next = i2 + 1;
                    return new AbstractLong2ObjectMap.BasicEntry(j, objArr[i2]);
                }

                @Override // java.util.Iterator
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    Long2ObjectArrayMap long2ObjectArrayMap = Long2ObjectArrayMap.this;
                    int i = long2ObjectArrayMap.size;
                    long2ObjectArrayMap.size = i - 1;
                    int i2 = this.next;
                    this.next = i2 - 1;
                    int tail = i - i2;
                    System.arraycopy(Long2ObjectArrayMap.this.key, this.next + 1, Long2ObjectArrayMap.this.key, this.next, tail);
                    System.arraycopy(Long2ObjectArrayMap.this.value, this.next + 1, Long2ObjectArrayMap.this.value, this.next, tail);
                    Long2ObjectArrayMap.this.value[Long2ObjectArrayMap.this.size] = null;
                }

                @Override // java.util.Iterator
                public void forEachRemaining(Consumer<? super Long2ObjectMap.Entry<V>> action) {
                    int max = Long2ObjectArrayMap.this.size;
                    while (this.next < max) {
                        long[] jArr = Long2ObjectArrayMap.this.key;
                        int i = this.next;
                        this.curr = i;
                        long j = jArr[i];
                        Object[] objArr = Long2ObjectArrayMap.this.value;
                        int i2 = this.next;
                        this.next = i2 + 1;
                        action.accept(new AbstractLong2ObjectMap.BasicEntry(j, objArr[i2]));
                    }
                }
            };
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap.FastEntrySet
        public ObjectIterator<Long2ObjectMap.Entry<V>> fastIterator() {
            return new ObjectIterator<Long2ObjectMap.Entry<V>>() { // from class: it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap.EntrySet.2
                int next = 0;
                int curr = -1;
                final AbstractLong2ObjectMap.BasicEntry<V> entry = new AbstractLong2ObjectMap.BasicEntry<>();

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return this.next < Long2ObjectArrayMap.this.size;
                }

                @Override // java.util.Iterator
                public Long2ObjectMap.Entry<V> next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    AbstractLong2ObjectMap.BasicEntry<V> basicEntry = this.entry;
                    long[] jArr = Long2ObjectArrayMap.this.key;
                    int i = this.next;
                    this.curr = i;
                    basicEntry.key = jArr[i];
                    AbstractLong2ObjectMap.BasicEntry<V> basicEntry2 = this.entry;
                    Object[] objArr = Long2ObjectArrayMap.this.value;
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
                    Long2ObjectArrayMap long2ObjectArrayMap = Long2ObjectArrayMap.this;
                    int i = long2ObjectArrayMap.size;
                    long2ObjectArrayMap.size = i - 1;
                    int i2 = this.next;
                    this.next = i2 - 1;
                    int tail = i - i2;
                    System.arraycopy(Long2ObjectArrayMap.this.key, this.next + 1, Long2ObjectArrayMap.this.key, this.next, tail);
                    System.arraycopy(Long2ObjectArrayMap.this.value, this.next + 1, Long2ObjectArrayMap.this.value, this.next, tail);
                    Long2ObjectArrayMap.this.value[Long2ObjectArrayMap.this.size] = null;
                }

                @Override // java.util.Iterator
                public void forEachRemaining(Consumer<? super Long2ObjectMap.Entry<V>> consumer) {
                    int i = Long2ObjectArrayMap.this.size;
                    while (this.next < i) {
                        AbstractLong2ObjectMap.BasicEntry<V> basicEntry = this.entry;
                        long[] jArr = Long2ObjectArrayMap.this.key;
                        int i2 = this.next;
                        this.curr = i2;
                        basicEntry.key = jArr[i2];
                        AbstractLong2ObjectMap.BasicEntry<V> basicEntry2 = this.entry;
                        Object[] objArr = Long2ObjectArrayMap.this.value;
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
        public final class EntrySetSpliterator extends ObjectSpliterators.EarlyBindingSizeIndexBasedSpliterator<Long2ObjectMap.Entry<V>> implements ObjectSpliterator<Long2ObjectMap.Entry<V>> {
            EntrySetSpliterator(int pos, int maxPos) {
                super(pos, maxPos);
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterators.AbstractIndexBasedSpliterator, java.util.Spliterator
            public int characteristics() {
                return 16465;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterators.AbstractIndexBasedSpliterator
            public final Long2ObjectMap.Entry<V> get(int location) {
                return new AbstractLong2ObjectMap.BasicEntry(Long2ObjectArrayMap.this.key[location], Long2ObjectArrayMap.this.value[location]);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterators.AbstractIndexBasedSpliterator
            public final Long2ObjectArrayMap<V>.EntrySet.EntrySetSpliterator makeForSplit(int pos, int maxPos) {
                return new EntrySetSpliterator(pos, maxPos);
            }
        }

        @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        /* renamed from: spliterator */
        public ObjectSpliterator<Long2ObjectMap.Entry<V>> mo221spliterator() {
            return new EntrySetSpliterator(0, Long2ObjectArrayMap.this.size);
        }

        @Override // java.lang.Iterable
        public void forEach(Consumer<? super Long2ObjectMap.Entry<V>> action) {
            int max = Long2ObjectArrayMap.this.size;
            for (int i = 0; i < max; i++) {
                action.accept(new AbstractLong2ObjectMap.BasicEntry(Long2ObjectArrayMap.this.key[i], Long2ObjectArrayMap.this.value[i]));
            }
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap.FastEntrySet
        public void fastForEach(Consumer<? super Long2ObjectMap.Entry<V>> consumer) {
            AbstractLong2ObjectMap.BasicEntry basicEntry = new AbstractLong2ObjectMap.BasicEntry();
            int i = Long2ObjectArrayMap.this.size;
            for (int i2 = 0; i2 < i; i2++) {
                basicEntry.key = Long2ObjectArrayMap.this.key[i2];
                basicEntry.value = (V) Long2ObjectArrayMap.this.value[i2];
                consumer.accept(basicEntry);
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return Long2ObjectArrayMap.this.size;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> e = (Map.Entry) o;
            if (e.getKey() == null || !(e.getKey() instanceof Long)) {
                return false;
            }
            long k = ((Long) e.getKey()).longValue();
            return Long2ObjectArrayMap.this.containsKey(k) && Objects.equals(Long2ObjectArrayMap.this.get(k), e.getValue());
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
            int oldPos = Long2ObjectArrayMap.this.findKey(k);
            if (oldPos == -1 || !Objects.equals(value, Long2ObjectArrayMap.this.value[oldPos])) {
                return false;
            }
            int tail = (Long2ObjectArrayMap.this.size - oldPos) - 1;
            System.arraycopy(Long2ObjectArrayMap.this.key, oldPos + 1, Long2ObjectArrayMap.this.key, oldPos, tail);
            System.arraycopy(Long2ObjectArrayMap.this.value, oldPos + 1, Long2ObjectArrayMap.this.value, oldPos, tail);
            Long2ObjectArrayMap.this.size--;
            Long2ObjectArrayMap.this.value[Long2ObjectArrayMap.this.size] = null;
            return true;
        }
    }

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap, it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap
    public Long2ObjectMap.FastEntrySet<V> long2ObjectEntrySet() {
        if (this.entries == null) {
            this.entries = new EntrySet();
        }
        return this.entries;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int findKey(long k) {
        long[] key = this.key;
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

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunction
    public V get(long j) {
        long[] jArr = this.key;
        int i = this.size;
        while (true) {
            int i2 = i - 1;
            if (i == 0) {
                return this.defRetValue;
            }
            if (jArr[i2] == j) {
                return (V) this.value[i2];
            }
            i = i2;
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

    @Override // it.unimi.dsi.fastutil.longs.AbstractLong2ObjectMap, it.unimi.dsi.fastutil.longs.Long2ObjectFunction, it.unimi.dsi.fastutil.longs.Long2ObjectMap
    public boolean containsKey(long k) {
        return findKey(k) != -1;
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLong2ObjectMap, java.util.Map
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

    @Override // it.unimi.dsi.fastutil.longs.AbstractLong2ObjectMap, java.util.Map
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunction
    public V put(long j, V v) {
        int findKey = findKey(j);
        if (findKey != -1) {
            V v2 = (V) this.value[findKey];
            this.value[findKey] = v;
            return v2;
        }
        if (this.size == this.key.length) {
            long[] jArr = new long[this.size == 0 ? 2 : this.size * 2];
            Object[] objArr = new Object[this.size != 0 ? 2 * this.size : 2];
            int i = this.size;
            while (true) {
                int i2 = i - 1;
                if (i == 0) {
                    break;
                }
                jArr[i2] = this.key[i2];
                objArr[i2] = this.value[i2];
                i = i2;
            }
            this.key = jArr;
            this.value = objArr;
        }
        this.key[this.size] = j;
        this.value[this.size] = v;
        this.size++;
        return this.defRetValue;
    }

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunction
    public V remove(long j) {
        int findKey = findKey(j);
        if (findKey == -1) {
            return this.defRetValue;
        }
        V v = (V) this.value[findKey];
        int i = (this.size - findKey) - 1;
        System.arraycopy(this.key, findKey + 1, this.key, findKey, i);
        System.arraycopy(this.value, findKey + 1, this.value, findKey, i);
        this.size--;
        this.value[this.size] = null;
        return v;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class KeySet extends AbstractLongSet {
        private KeySet() {
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public boolean contains(long k) {
            return Long2ObjectArrayMap.this.findKey(k) != -1;
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongSet, it.unimi.dsi.fastutil.longs.LongSet
        public boolean remove(long k) {
            int oldPos = Long2ObjectArrayMap.this.findKey(k);
            if (oldPos == -1) {
                return false;
            }
            int tail = (Long2ObjectArrayMap.this.size - oldPos) - 1;
            System.arraycopy(Long2ObjectArrayMap.this.key, oldPos + 1, Long2ObjectArrayMap.this.key, oldPos, tail);
            System.arraycopy(Long2ObjectArrayMap.this.value, oldPos + 1, Long2ObjectArrayMap.this.value, oldPos, tail);
            Long2ObjectArrayMap.this.size--;
            Long2ObjectArrayMap.this.value[Long2ObjectArrayMap.this.size] = null;
            return true;
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongSet, it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
        public LongIterator iterator() {
            return new LongIterator() { // from class: it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap.KeySet.1
                int pos = 0;

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return this.pos < Long2ObjectArrayMap.this.size;
                }

                @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong
                public long nextLong() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    long[] jArr = Long2ObjectArrayMap.this.key;
                    int i = this.pos;
                    this.pos = i + 1;
                    return jArr[i];
                }

                @Override // java.util.Iterator
                public void remove() {
                    if (this.pos == 0) {
                        throw new IllegalStateException();
                    }
                    int tail = Long2ObjectArrayMap.this.size - this.pos;
                    System.arraycopy(Long2ObjectArrayMap.this.key, this.pos, Long2ObjectArrayMap.this.key, this.pos - 1, tail);
                    System.arraycopy(Long2ObjectArrayMap.this.value, this.pos, Long2ObjectArrayMap.this.value, this.pos - 1, tail);
                    Long2ObjectArrayMap long2ObjectArrayMap = Long2ObjectArrayMap.this;
                    long2ObjectArrayMap.size--;
                    this.pos--;
                    Long2ObjectArrayMap.this.value[Long2ObjectArrayMap.this.size] = null;
                }

                @Override // java.util.PrimitiveIterator
                public void forEachRemaining(java.util.function.LongConsumer action) {
                    int max = Long2ObjectArrayMap.this.size;
                    while (this.pos < max) {
                        long[] jArr = Long2ObjectArrayMap.this.key;
                        int i = this.pos;
                        this.pos = i + 1;
                        action.accept(jArr[i]);
                    }
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes4.dex */
        public final class KeySetSpliterator extends LongSpliterators.EarlyBindingSizeIndexBasedSpliterator implements LongSpliterator {
            KeySetSpliterator(int pos, int maxPos) {
                super(pos, maxPos);
            }

            @Override // it.unimi.dsi.fastutil.longs.LongSpliterators.AbstractIndexBasedSpliterator, java.util.Spliterator
            public int characteristics() {
                return 16721;
            }

            @Override // it.unimi.dsi.fastutil.longs.LongSpliterators.AbstractIndexBasedSpliterator
            protected final long get(int location) {
                return Long2ObjectArrayMap.this.key[location];
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // it.unimi.dsi.fastutil.longs.LongSpliterators.AbstractIndexBasedSpliterator
            public final Long2ObjectArrayMap<V>.KeySet.KeySetSpliterator makeForSplit(int pos, int maxPos) {
                return new KeySetSpliterator(pos, maxPos);
            }

            @Override // it.unimi.dsi.fastutil.longs.LongSpliterators.AbstractIndexBasedSpliterator, java.util.Spliterator.OfPrimitive
            public void forEachRemaining(java.util.function.LongConsumer action) {
                int max = Long2ObjectArrayMap.this.size;
                while (this.pos < max) {
                    long[] jArr = Long2ObjectArrayMap.this.key;
                    int i = this.pos;
                    this.pos = i + 1;
                    action.accept(jArr[i]);
                }
            }
        }

        @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
        public LongSpliterator spliterator() {
            return new KeySetSpliterator(0, Long2ObjectArrayMap.this.size);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongIterable
        public void forEach(java.util.function.LongConsumer action) {
            int max = Long2ObjectArrayMap.this.size;
            for (int i = 0; i < max; i++) {
                action.accept(Long2ObjectArrayMap.this.key[i]);
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return Long2ObjectArrayMap.this.size;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            Long2ObjectArrayMap.this.clear();
        }
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLong2ObjectMap, it.unimi.dsi.fastutil.longs.Long2ObjectMap, java.util.Map
    /* renamed from: keySet */
    public Set<Long> keySet2() {
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
            return Long2ObjectArrayMap.this.containsValue(v);
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        public ObjectIterator<V> iterator() {
            return new ObjectIterator<V>() { // from class: it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap.ValuesCollection.1
                int pos = 0;

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return this.pos < Long2ObjectArrayMap.this.size;
                }

                @Override // java.util.Iterator
                public V next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    Object[] objArr = Long2ObjectArrayMap.this.value;
                    int i = this.pos;
                    this.pos = i + 1;
                    return (V) objArr[i];
                }

                @Override // java.util.Iterator
                public void remove() {
                    if (this.pos == 0) {
                        throw new IllegalStateException();
                    }
                    int tail = Long2ObjectArrayMap.this.size - this.pos;
                    System.arraycopy(Long2ObjectArrayMap.this.key, this.pos, Long2ObjectArrayMap.this.key, this.pos - 1, tail);
                    System.arraycopy(Long2ObjectArrayMap.this.value, this.pos, Long2ObjectArrayMap.this.value, this.pos - 1, tail);
                    Long2ObjectArrayMap long2ObjectArrayMap = Long2ObjectArrayMap.this;
                    long2ObjectArrayMap.size--;
                    this.pos--;
                    Long2ObjectArrayMap.this.value[Long2ObjectArrayMap.this.size] = null;
                }

                @Override // java.util.Iterator
                public void forEachRemaining(Consumer<? super V> action) {
                    int max = Long2ObjectArrayMap.this.size;
                    while (this.pos < max) {
                        Object[] objArr = Long2ObjectArrayMap.this.value;
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
                return (V) Long2ObjectArrayMap.this.value[i];
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterators.AbstractIndexBasedSpliterator
            public final Long2ObjectArrayMap<V>.ValuesCollection.ValuesSpliterator makeForSplit(int pos, int maxPos) {
                return new ValuesSpliterator(pos, maxPos);
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterators.AbstractIndexBasedSpliterator, java.util.Spliterator
            public void forEachRemaining(Consumer<? super V> action) {
                int max = Long2ObjectArrayMap.this.size;
                while (this.pos < max) {
                    Object[] objArr = Long2ObjectArrayMap.this.value;
                    int i = this.pos;
                    this.pos = i + 1;
                    action.accept(objArr[i]);
                }
            }
        }

        @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        /* renamed from: spliterator */
        public ObjectSpliterator<V> mo221spliterator() {
            return new ValuesSpliterator(0, Long2ObjectArrayMap.this.size);
        }

        @Override // java.lang.Iterable
        public void forEach(Consumer<? super V> action) {
            int max = Long2ObjectArrayMap.this.size;
            for (int i = 0; i < max; i++) {
                action.accept(Long2ObjectArrayMap.this.value[i]);
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return Long2ObjectArrayMap.this.size;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            Long2ObjectArrayMap.this.clear();
        }
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLong2ObjectMap, it.unimi.dsi.fastutil.longs.Long2ObjectMap, java.util.Map
    public ObjectCollection<V> values() {
        if (this.values == null) {
            this.values = new ValuesCollection();
        }
        return this.values;
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public Long2ObjectArrayMap<V> m239clone() {
        try {
            Long2ObjectArrayMap<V> c = (Long2ObjectArrayMap) super.clone();
            c.key = (long[]) this.key.clone();
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
            s.writeLong(this.key[i]);
            s.writeObject(this.value[i]);
        }
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.key = new long[this.size];
        this.value = new Object[this.size];
        for (int i = 0; i < this.size; i++) {
            this.key[i] = s.readLong();
            this.value[i] = s.readObject();
        }
    }
}
