package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Size64;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
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
public abstract class AbstractInt2ObjectMap<V> extends AbstractInt2ObjectFunction<V> implements Int2ObjectMap<V>, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction, it.unimi.dsi.fastutil.ints.Int2ObjectMap
    public boolean containsKey(int k) {
        ObjectIterator<Int2ObjectMap.Entry<V>> i = int2ObjectEntrySet().iterator();
        while (i.hasNext()) {
            if (i.next().getIntKey() == k) {
                return true;
            }
        }
        return false;
    }

    @Override // java.util.Map
    public boolean containsValue(Object v) {
        ObjectIterator<Int2ObjectMap.Entry<V>> i = int2ObjectEntrySet().iterator();
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
    public static class BasicEntry<V> implements Int2ObjectMap.Entry<V> {
        protected int key;
        protected V value;

        public BasicEntry() {
        }

        public BasicEntry(Integer key, V value) {
            this.key = key.intValue();
            this.value = value;
        }

        public BasicEntry(int key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry
        public int getIntKey() {
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
            if (o instanceof Int2ObjectMap.Entry) {
                Int2ObjectMap.Entry<V> e = (Int2ObjectMap.Entry) o;
                return this.key == e.getIntKey() && Objects.equals(this.value, e.getValue());
            }
            Map.Entry<?, ?> e2 = (Map.Entry) o;
            Object key = e2.getKey();
            if (key == null || !(key instanceof Integer)) {
                return false;
            }
            Object value = e2.getValue();
            return this.key == ((Integer) key).intValue() && Objects.equals(this.value, value);
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return this.key ^ (this.value == null ? 0 : this.value.hashCode());
        }

        public String toString() {
            return this.key + "->" + this.value;
        }
    }

    /* loaded from: classes4.dex */
    public static abstract class BasicEntrySet<V> extends AbstractObjectSet<Int2ObjectMap.Entry<V>> {
        protected final Int2ObjectMap<V> map;

        public BasicEntrySet(Int2ObjectMap<V> map) {
            this.map = map;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Int2ObjectMap.Entry) {
                Int2ObjectMap.Entry<V> e = (Int2ObjectMap.Entry) o;
                int k = e.getIntKey();
                return this.map.containsKey(k) && Objects.equals(this.map.get(k), e.getValue());
            }
            Map.Entry<?, ?> e2 = (Map.Entry) o;
            Object key = e2.getKey();
            if (key == null || !(key instanceof Integer)) {
                return false;
            }
            int k2 = ((Integer) key).intValue();
            Object value = e2.getValue();
            return this.map.containsKey(k2) && Objects.equals(this.map.get(k2), value);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Int2ObjectMap.Entry) {
                Int2ObjectMap.Entry<V> e = (Int2ObjectMap.Entry) o;
                return this.map.remove(e.getIntKey(), e.getValue());
            }
            Map.Entry<?, ?> e2 = (Map.Entry) o;
            Object key = e2.getKey();
            if (key == null || !(key instanceof Integer)) {
                return false;
            }
            int k = ((Integer) key).intValue();
            Object v = e2.getValue();
            return this.map.remove(k, v);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.map.size();
        }

        @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        /* renamed from: spliterator */
        public ObjectSpliterator<Int2ObjectMap.Entry<V>> mo221spliterator() {
            return ObjectSpliterators.asSpliterator(iterator(), Size64.sizeOf(this.map), 65);
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
    /* renamed from: keySet */
    public Set<Integer> keySet2() {
        return new AbstractIntSet() { // from class: it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap.1
            @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
            public boolean contains(int k) {
                return AbstractInt2ObjectMap.this.containsKey(k);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return AbstractInt2ObjectMap.this.size();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public void clear() {
                AbstractInt2ObjectMap.this.clear();
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* renamed from: it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap$1$1, reason: invalid class name and collision with other inner class name */
            /* loaded from: classes4.dex */
            public class C00341 implements IntIterator {
                private final ObjectIterator<Int2ObjectMap.Entry<V>> i;

                C00341() {
                    this.i = Int2ObjectMaps.fastIterator(AbstractInt2ObjectMap.this);
                }

                @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt
                public int nextInt() {
                    return this.i.next().getIntKey();
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
                public void forEachRemaining(final java.util.function.IntConsumer action) {
                    this.i.forEachRemaining(new Consumer() { // from class: it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap$1$1$$ExternalSyntheticLambda0
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            action.accept(((Int2ObjectMap.Entry) obj).getIntKey());
                        }
                    });
                }
            }

            @Override // it.unimi.dsi.fastutil.ints.AbstractIntSet, it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
            public IntIterator iterator() {
                return new C00341();
            }

            @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
            public IntSpliterator spliterator() {
                return IntSpliterators.asSpliterator(iterator(), Size64.sizeOf(AbstractInt2ObjectMap.this), 321);
            }
        };
    }

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
    public ObjectCollection<V> values() {
        return new AbstractObjectCollection<V>() { // from class: it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap.2
            @Override // java.util.AbstractCollection, java.util.Collection
            public boolean contains(Object k) {
                return AbstractInt2ObjectMap.this.containsValue(k);
            }

            @Override // java.util.AbstractCollection, java.util.Collection
            public int size() {
                return AbstractInt2ObjectMap.this.size();
            }

            @Override // java.util.AbstractCollection, java.util.Collection
            public void clear() {
                AbstractInt2ObjectMap.this.clear();
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* renamed from: it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap$2$1, reason: invalid class name */
            /* loaded from: classes4.dex */
            public class AnonymousClass1 implements ObjectIterator<V> {
                private final ObjectIterator<Int2ObjectMap.Entry<V>> i;

                AnonymousClass1() {
                    this.i = Int2ObjectMaps.fastIterator(AbstractInt2ObjectMap.this);
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
                    this.i.forEachRemaining(new Consumer() { // from class: it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap$2$1$$ExternalSyntheticLambda0
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            action.accept(((Int2ObjectMap.Entry) obj).getValue());
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
                return ObjectSpliterators.asSpliterator(iterator(), Size64.sizeOf(AbstractInt2ObjectMap.this), 64);
            }
        };
    }

    @Override // java.util.Map
    public void putAll(Map<? extends Integer, ? extends V> map) {
        if (map instanceof Int2ObjectMap) {
            ObjectIterator fastIterator = Int2ObjectMaps.fastIterator((Int2ObjectMap) map);
            while (fastIterator.hasNext()) {
                Int2ObjectMap.Entry entry = (Int2ObjectMap.Entry<V>) fastIterator.next();
                put(entry.getIntKey(), (int) entry.getValue());
            }
            return;
        }
        int size = map.size();
        Iterator<Map.Entry<? extends Integer, ? extends V>> it2 = map.entrySet().iterator();
        while (true) {
            int i = size - 1;
            if (size != 0) {
                Map.Entry<? extends Integer, ? extends V> next = it2.next();
                put2(next.getKey(), (Integer) next.getValue());
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
        ObjectIterator fastIterator = Int2ObjectMaps.fastIterator(this);
        while (true) {
            int i2 = size - 1;
            if (size == 0) {
                return i;
            }
            i += ((Int2ObjectMap.Entry<V>) fastIterator.next()).hashCode();
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
        return int2ObjectEntrySet().containsAll(m.entrySet());
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        ObjectIterator<Int2ObjectMap.Entry<V>> i = Int2ObjectMaps.fastIterator(this);
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
                Int2ObjectMap.Entry<V> e = i.next();
                s.append(String.valueOf(e.getIntKey()));
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
