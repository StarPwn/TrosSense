package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Size64;
import it.unimi.dsi.fastutil.ints.AbstractIntCollection;
import it.unimi.dsi.fastutil.ints.IntBinaryOperator;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntSpliterator;
import it.unimi.dsi.fastutil.ints.IntSpliterators;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

/* loaded from: classes4.dex */
public abstract class AbstractObject2IntMap<K> extends AbstractObject2IntFunction<K> implements Object2IntMap<K>, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;

    @Override // it.unimi.dsi.fastutil.Function
    public boolean containsKey(Object k) {
        ObjectIterator<Object2IntMap.Entry<K>> i = object2IntEntrySet().iterator();
        while (i.hasNext()) {
            if (i.next().getKey() == k) {
                return true;
            }
        }
        return false;
    }

    @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
    public boolean containsValue(int v) {
        ObjectIterator<Object2IntMap.Entry<K>> i = object2IntEntrySet().iterator();
        while (i.hasNext()) {
            if (i.next().getIntValue() == v) {
                return true;
            }
        }
        return false;
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override // it.unimi.dsi.fastutil.objects.Object2IntMap
    public final int mergeInt(K key, int value, IntBinaryOperator remappingFunction) {
        return mergeInt((AbstractObject2IntMap<K>) key, value, (java.util.function.IntBinaryOperator) remappingFunction);
    }

    /* loaded from: classes4.dex */
    public static class BasicEntry<K> implements Object2IntMap.Entry<K> {
        protected K key;
        protected int value;

        public BasicEntry() {
        }

        public BasicEntry(K key, Integer value) {
            this.key = key;
            this.value = value.intValue();
        }

        public BasicEntry(K key, int value) {
            this.key = key;
            this.value = value;
        }

        @Override // java.util.Map.Entry
        public K getKey() {
            return this.key;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap.Entry
        public int getIntValue() {
            return this.value;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntMap.Entry
        public int setValue(int value) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Object2IntMap.Entry) {
                Object2IntMap.Entry<K> e = (Object2IntMap.Entry) o;
                return Objects.equals(this.key, e.getKey()) && this.value == e.getIntValue();
            }
            Map.Entry<?, ?> e2 = (Map.Entry) o;
            Object key = e2.getKey();
            Object value = e2.getValue();
            return value != null && (value instanceof Integer) && Objects.equals(this.key, key) && this.value == ((Integer) value).intValue();
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return (this.key == null ? 0 : this.key.hashCode()) ^ this.value;
        }

        public String toString() {
            return this.key + "->" + this.value;
        }
    }

    /* loaded from: classes4.dex */
    public static abstract class BasicEntrySet<K> extends AbstractObjectSet<Object2IntMap.Entry<K>> {
        protected final Object2IntMap<K> map;

        public BasicEntrySet(Object2IntMap<K> map) {
            this.map = map;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Object2IntMap.Entry) {
                Object2IntMap.Entry<K> e = (Object2IntMap.Entry) o;
                K k = e.getKey();
                return this.map.containsKey(k) && this.map.getInt(k) == e.getIntValue();
            }
            Map.Entry<?, ?> e2 = (Map.Entry) o;
            Object k2 = e2.getKey();
            Object value = e2.getValue();
            return value != null && (value instanceof Integer) && this.map.containsKey(k2) && this.map.getInt(k2) == ((Integer) value).intValue();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Object2IntMap.Entry) {
                Object2IntMap.Entry<K> e = (Object2IntMap.Entry) o;
                return this.map.remove(e.getKey(), e.getIntValue());
            }
            Map.Entry<?, ?> e2 = (Map.Entry) o;
            Object k = e2.getKey();
            Object value = e2.getValue();
            if (value == null || !(value instanceof Integer)) {
                return false;
            }
            int v = ((Integer) value).intValue();
            return this.map.remove(k, v);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.map.size();
        }

        @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        /* renamed from: spliterator */
        public ObjectSpliterator<Object2IntMap.Entry<K>> mo221spliterator() {
            return ObjectSpliterators.asSpliterator(iterator(), Size64.sizeOf(this.map), 65);
        }
    }

    @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
    public ObjectSet<K> keySet() {
        return new AbstractObjectSet<K>() { // from class: it.unimi.dsi.fastutil.objects.AbstractObject2IntMap.1
            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean contains(Object k) {
                return AbstractObject2IntMap.this.containsKey(k);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return AbstractObject2IntMap.this.size();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public void clear() {
                AbstractObject2IntMap.this.clear();
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* renamed from: it.unimi.dsi.fastutil.objects.AbstractObject2IntMap$1$1, reason: invalid class name and collision with other inner class name */
            /* loaded from: classes4.dex */
            public class C00361 implements ObjectIterator<K> {
                private final ObjectIterator<Object2IntMap.Entry<K>> i;

                C00361() {
                    this.i = Object2IntMaps.fastIterator(AbstractObject2IntMap.this);
                }

                @Override // java.util.Iterator
                public K next() {
                    return this.i.next().getKey();
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
                public void forEachRemaining(final Consumer<? super K> action) {
                    this.i.forEachRemaining(new Consumer() { // from class: it.unimi.dsi.fastutil.objects.AbstractObject2IntMap$1$1$$ExternalSyntheticLambda0
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            action.accept(((Object2IntMap.Entry) obj).getKey());
                        }
                    });
                }
            }

            @Override // it.unimi.dsi.fastutil.objects.AbstractObjectSet, it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
            public ObjectIterator<K> iterator() {
                return new C00361();
            }

            @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
            /* renamed from: spliterator */
            public ObjectSpliterator<K> mo221spliterator() {
                return ObjectSpliterators.asSpliterator(iterator(), Size64.sizeOf(AbstractObject2IntMap.this), 65);
            }
        };
    }

    @Override // it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
    /* renamed from: values */
    public Collection<Integer> values2() {
        return new AbstractIntCollection() { // from class: it.unimi.dsi.fastutil.objects.AbstractObject2IntMap.2
            @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
            public boolean contains(int k) {
                return AbstractObject2IntMap.this.containsValue(k);
            }

            @Override // java.util.AbstractCollection, java.util.Collection
            public int size() {
                return AbstractObject2IntMap.this.size();
            }

            @Override // java.util.AbstractCollection, java.util.Collection
            public void clear() {
                AbstractObject2IntMap.this.clear();
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* renamed from: it.unimi.dsi.fastutil.objects.AbstractObject2IntMap$2$1, reason: invalid class name */
            /* loaded from: classes4.dex */
            public class AnonymousClass1 implements IntIterator {
                private final ObjectIterator<Object2IntMap.Entry<K>> i;

                AnonymousClass1() {
                    this.i = Object2IntMaps.fastIterator(AbstractObject2IntMap.this);
                }

                @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt
                public int nextInt() {
                    return this.i.next().getIntValue();
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
                public void forEachRemaining(final IntConsumer action) {
                    this.i.forEachRemaining(new Consumer() { // from class: it.unimi.dsi.fastutil.objects.AbstractObject2IntMap$2$1$$ExternalSyntheticLambda0
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            action.accept(((Object2IntMap.Entry) obj).getIntValue());
                        }
                    });
                }
            }

            @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
            public IntIterator iterator() {
                return new AnonymousClass1();
            }

            @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
            public IntSpliterator spliterator() {
                return IntSpliterators.asSpliterator(iterator(), Size64.sizeOf(AbstractObject2IntMap.this), 320);
            }
        };
    }

    @Override // java.util.Map
    public void putAll(Map<? extends K, ? extends Integer> m) {
        if (m instanceof Object2IntMap) {
            ObjectIterator<Object2IntMap.Entry<K>> i = Object2IntMaps.fastIterator((Object2IntMap) m);
            while (i.hasNext()) {
                Object2IntMap.Entry<? extends K> e = (Object2IntMap.Entry) i.next();
                put((AbstractObject2IntMap<K>) e.getKey(), e.getIntValue());
            }
            return;
        }
        int n = m.size();
        Iterator<? extends Map.Entry<? extends K, ? extends Integer>> i2 = m.entrySet().iterator();
        while (true) {
            int n2 = n - 1;
            if (n != 0) {
                Map.Entry<? extends K, ? extends Integer> e2 = i2.next();
                put2((AbstractObject2IntMap<K>) e2.getKey(), e2.getValue());
                n = n2;
            } else {
                return;
            }
        }
    }

    @Override // java.util.Map
    public int hashCode() {
        int h = 0;
        int n = size();
        ObjectIterator<Object2IntMap.Entry<K>> i = Object2IntMaps.fastIterator(this);
        while (true) {
            int n2 = n - 1;
            if (n == 0) {
                return h;
            }
            h += ((Object2IntMap.Entry) i.next()).hashCode();
            n = n2;
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
        return object2IntEntrySet().containsAll(m.entrySet());
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        ObjectIterator<Object2IntMap.Entry<K>> i = Object2IntMaps.fastIterator(this);
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
                Object2IntMap.Entry<K> e = (Object2IntMap.Entry) i.next();
                if (this == e.getKey()) {
                    s.append("(this map)");
                } else {
                    s.append(String.valueOf(e.getKey()));
                }
                s.append("=>");
                s.append(String.valueOf(e.getIntValue()));
                n = n2;
            } else {
                s.append("}");
                return s.toString();
            }
        }
    }
}
