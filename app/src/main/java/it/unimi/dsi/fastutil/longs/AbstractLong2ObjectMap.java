package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Size64;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.AbstractObjectCollection;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSpliterator;
import it.unimi.dsi.fastutil.objects.ObjectSpliterators;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/* loaded from: classes4.dex */
public abstract class AbstractLong2ObjectMap<V> extends AbstractLong2ObjectFunction<V> implements Long2ObjectMap<V>, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunction, it.unimi.dsi.fastutil.longs.Long2ObjectMap
    public boolean containsKey(long k) {
        ObjectIterator<Long2ObjectMap.Entry<V>> i = long2ObjectEntrySet().iterator();
        while (i.hasNext()) {
            if (i.next().getLongKey() == k) {
                return true;
            }
        }
        return false;
    }

    @Override // java.util.Map
    public boolean containsValue(Object v) {
        ObjectIterator<Long2ObjectMap.Entry<V>> i = long2ObjectEntrySet().iterator();
        while (i.hasNext()) {
            if (i.next().getValue() == v) {
                return true;
            }
        }
        return false;
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return size() == 0;
    }

    /* loaded from: classes4.dex */
    public static class BasicEntry<V> implements Long2ObjectMap.Entry<V> {
        protected long key;
        protected V value;

        public BasicEntry() {
        }

        public BasicEntry(Long key, V value) {
            this.key = key.longValue();
            this.value = value;
        }

        public BasicEntry(long key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap.Entry
        public long getLongKey() {
            return this.key;
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            return this.value;
        }

        @Override // java.util.Map.Entry
        public V setValue(V value) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Long2ObjectMap.Entry) {
                Long2ObjectMap.Entry<V> e = (Long2ObjectMap.Entry) o;
                return this.key == e.getLongKey() && Objects.equals(this.value, e.getValue());
            }
            Map.Entry<?, ?> e2 = (Map.Entry) o;
            Object key = e2.getKey();
            if (key == null || !(key instanceof Long)) {
                return false;
            }
            Object value = e2.getValue();
            return this.key == ((Long) key).longValue() && Objects.equals(this.value, value);
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return HashCommon.long2int(this.key) ^ (this.value == null ? 0 : this.value.hashCode());
        }

        public String toString() {
            return this.key + "->" + this.value;
        }
    }

    /* loaded from: classes4.dex */
    public static abstract class BasicEntrySet<V> extends AbstractObjectSet<Long2ObjectMap.Entry<V>> {
        protected final Long2ObjectMap<V> map;

        public BasicEntrySet(Long2ObjectMap<V> map) {
            this.map = map;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Long2ObjectMap.Entry) {
                Long2ObjectMap.Entry<V> e = (Long2ObjectMap.Entry) o;
                long k = e.getLongKey();
                return this.map.containsKey(k) && Objects.equals(this.map.get(k), e.getValue());
            }
            Map.Entry<?, ?> e2 = (Map.Entry) o;
            Object key = e2.getKey();
            if (key == null || !(key instanceof Long)) {
                return false;
            }
            long k2 = ((Long) key).longValue();
            Object value = e2.getValue();
            return this.map.containsKey(k2) && Objects.equals(this.map.get(k2), value);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Long2ObjectMap.Entry) {
                Long2ObjectMap.Entry<V> e = (Long2ObjectMap.Entry) o;
                return this.map.remove(e.getLongKey(), e.getValue());
            }
            Map.Entry<?, ?> e2 = (Map.Entry) o;
            Object key = e2.getKey();
            if (key == null || !(key instanceof Long)) {
                return false;
            }
            long k = ((Long) key).longValue();
            Object v = e2.getValue();
            return this.map.remove(k, v);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.map.size();
        }

        @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        /* renamed from: spliterator */
        public ObjectSpliterator<Long2ObjectMap.Entry<V>> mo221spliterator() {
            return ObjectSpliterators.asSpliterator(iterator(), Size64.sizeOf(this.map), 65);
        }
    }

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap, java.util.Map
    public Set<Long> keySet2() {
        return new AbstractLongSet() { // from class: it.unimi.dsi.fastutil.longs.AbstractLong2ObjectMap.1
            @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
            public boolean contains(long k) {
                return AbstractLong2ObjectMap.this.containsKey(k);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return AbstractLong2ObjectMap.this.size();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public void clear() {
                AbstractLong2ObjectMap.this.clear();
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* renamed from: it.unimi.dsi.fastutil.longs.AbstractLong2ObjectMap$1$1, reason: invalid class name and collision with other inner class name */
            /* loaded from: classes4.dex */
            public class C00351 implements LongIterator {
                private final ObjectIterator<Long2ObjectMap.Entry<V>> i;

                C00351() {
                    this.i = Long2ObjectMaps.fastIterator(AbstractLong2ObjectMap.this);
                }

                @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong
                public long nextLong() {
                    return this.i.next().getLongKey();
                }

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return this.i.hasNext();
                }

                @Override // java.util.Iterator
                public void remove() {
                    this.i.remove();
                }

                @Override // java.util.PrimitiveIterator
                public void forEachRemaining(final java.util.function.LongConsumer action) {
                    this.i.forEachRemaining(new Consumer() { // from class: it.unimi.dsi.fastutil.longs.AbstractLong2ObjectMap$1$1$$ExternalSyntheticLambda0
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            action.accept(((Long2ObjectMap.Entry) obj).getLongKey());
                        }
                    });
                }
            }

            @Override // it.unimi.dsi.fastutil.longs.AbstractLongSet, it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
            public LongIterator iterator() {
                return new C00351();
            }

            @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
            public LongSpliterator spliterator() {
                return LongSpliterators.asSpliterator(iterator(), Size64.sizeOf(AbstractLong2ObjectMap.this), 321);
            }
        };
    }

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectMap, java.util.Map
    public ObjectCollection<V> values() {
        return new AbstractObjectCollection<V>() { // from class: it.unimi.dsi.fastutil.longs.AbstractLong2ObjectMap.2
            @Override // java.util.AbstractCollection, java.util.Collection
            public boolean contains(Object k) {
                return AbstractLong2ObjectMap.this.containsValue(k);
            }

            @Override // java.util.AbstractCollection, java.util.Collection
            public int size() {
                return AbstractLong2ObjectMap.this.size();
            }

            @Override // java.util.AbstractCollection, java.util.Collection
            public void clear() {
                AbstractLong2ObjectMap.this.clear();
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* renamed from: it.unimi.dsi.fastutil.longs.AbstractLong2ObjectMap$2$1, reason: invalid class name */
            /* loaded from: classes4.dex */
            public class AnonymousClass1 implements ObjectIterator<V> {
                private final ObjectIterator<Long2ObjectMap.Entry<V>> i;

                AnonymousClass1() {
                    this.i = Long2ObjectMaps.fastIterator(AbstractLong2ObjectMap.this);
                }

                @Override // java.util.Iterator
                public V next() {
                    return this.i.next().getValue();
                }

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return this.i.hasNext();
                }

                @Override // java.util.Iterator
                public void remove() {
                    this.i.remove();
                }

                @Override // java.util.Iterator
                public void forEachRemaining(final Consumer<? super V> action) {
                    this.i.forEachRemaining(new Consumer() { // from class: it.unimi.dsi.fastutil.longs.AbstractLong2ObjectMap$2$1$$ExternalSyntheticLambda0
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            action.accept(((Long2ObjectMap.Entry) obj).getValue());
                        }
                    });
                }
            }

            @Override // it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
            public ObjectIterator<V> iterator() {
                return new AnonymousClass1();
            }

            @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
            /* renamed from: spliterator */
            public ObjectSpliterator<V> mo221spliterator() {
                return ObjectSpliterators.asSpliterator(iterator(), Size64.sizeOf(AbstractLong2ObjectMap.this), 64);
            }
        };
    }

    @Override // java.util.Map
    public void putAll(Map<? extends Long, ? extends V> map) {
        if (map instanceof Long2ObjectMap) {
            ObjectIterator fastIterator = Long2ObjectMaps.fastIterator((Long2ObjectMap) map);
            while (fastIterator.hasNext()) {
                Long2ObjectMap.Entry entry = (Long2ObjectMap.Entry<V>) fastIterator.next();
                put(entry.getLongKey(), (long) entry.getValue());
            }
            return;
        }
        int size = map.size();
        Iterator<Map.Entry<? extends Long, ? extends V>> it2 = map.entrySet().iterator();
        while (true) {
            int i = size - 1;
            if (size != 0) {
                Map.Entry<? extends Long, ? extends V> next = it2.next();
                put2(next.getKey(), (Long) next.getValue());
                size = i;
            } else {
                return;
            }
        }
    }

    @Override // java.util.Map
    public int hashCode() {
        int i = 0;
        int size = size();
        ObjectIterator fastIterator = Long2ObjectMaps.fastIterator(this);
        while (true) {
            int i2 = size - 1;
            if (size == 0) {
                return i;
            }
            i += ((Long2ObjectMap.Entry<V>) fastIterator.next()).hashCode();
            size = i2;
        }
    }

    @Override // java.util.Map
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Map)) {
            return false;
        }
        Map<?, ?> m = (Map) o;
        if (m.size() != size()) {
            return false;
        }
        return long2ObjectEntrySet().containsAll(m.entrySet());
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        ObjectIterator<Long2ObjectMap.Entry<V>> i = Long2ObjectMaps.fastIterator(this);
        int n = size();
        boolean first = true;
        s.append("{");
        while (true) {
            int n2 = n - 1;
            if (n != 0) {
                if (first) {
                    first = false;
                } else {
                    s.append(", ");
                }
                Long2ObjectMap.Entry<V> e = i.next();
                s.append(String.valueOf(e.getLongKey()));
                s.append("=>");
                if (this == e.getValue()) {
                    s.append("(this map)");
                } else {
                    s.append(String.valueOf(e.getValue()));
                }
                n = n2;
            } else {
                s.append("}");
                return s.toString();
            }
        }
    }
}
