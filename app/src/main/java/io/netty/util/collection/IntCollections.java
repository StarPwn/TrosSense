package io.netty.util.collection;

import io.netty.util.collection.IntObjectMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/* loaded from: classes4.dex */
public final class IntCollections {
    private static final IntObjectMap<Object> EMPTY_MAP = new EmptyMap();

    private IntCollections() {
    }

    public static <V> IntObjectMap<V> emptyMap() {
        return (IntObjectMap<V>) EMPTY_MAP;
    }

    public static <V> IntObjectMap<V> unmodifiableMap(IntObjectMap<V> map) {
        return new UnmodifiableMap(map);
    }

    /* loaded from: classes4.dex */
    private static final class EmptyMap implements IntObjectMap<Object> {
        private EmptyMap() {
        }

        @Override // io.netty.util.collection.IntObjectMap
        public Object get(int key) {
            return null;
        }

        @Override // io.netty.util.collection.IntObjectMap
        public Object put(int key, Object value) {
            throw new UnsupportedOperationException("put");
        }

        @Override // io.netty.util.collection.IntObjectMap
        public Object remove(int key) {
            return null;
        }

        @Override // java.util.Map
        public int size() {
            return 0;
        }

        @Override // java.util.Map
        public boolean isEmpty() {
            return true;
        }

        @Override // java.util.Map
        public boolean containsKey(Object key) {
            return false;
        }

        @Override // java.util.Map
        public void clear() {
        }

        @Override // java.util.Map
        public Set<Integer> keySet() {
            return Collections.emptySet();
        }

        @Override // io.netty.util.collection.IntObjectMap
        public boolean containsKey(int key) {
            return false;
        }

        @Override // java.util.Map
        public boolean containsValue(Object value) {
            return false;
        }

        @Override // io.netty.util.collection.IntObjectMap
        public Iterable<IntObjectMap.PrimitiveEntry<Object>> entries() {
            return Collections.emptySet();
        }

        @Override // java.util.Map
        public Object get(Object key) {
            return null;
        }

        @Override // java.util.Map
        public Object put(Integer key, Object value) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public Object remove(Object key) {
            return null;
        }

        @Override // java.util.Map
        public void putAll(Map<? extends Integer, ?> m) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public Collection<Object> values() {
            return Collections.emptyList();
        }

        @Override // java.util.Map
        public Set<Map.Entry<Integer, Object>> entrySet() {
            return Collections.emptySet();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class UnmodifiableMap<V> implements IntObjectMap<V> {
        private Iterable<IntObjectMap.PrimitiveEntry<V>> entries;
        private Set<Map.Entry<Integer, V>> entrySet;
        private Set<Integer> keySet;
        private final IntObjectMap<V> map;
        private Collection<V> values;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Map
        public /* bridge */ /* synthetic */ Object put(Integer num, Object obj) {
            return put2(num, (Integer) obj);
        }

        UnmodifiableMap(IntObjectMap<V> map) {
            this.map = map;
        }

        @Override // io.netty.util.collection.IntObjectMap
        public V get(int key) {
            return this.map.get(key);
        }

        @Override // io.netty.util.collection.IntObjectMap
        public V put(int key, V value) {
            throw new UnsupportedOperationException("put");
        }

        @Override // io.netty.util.collection.IntObjectMap
        public V remove(int key) {
            throw new UnsupportedOperationException("remove");
        }

        @Override // java.util.Map
        public int size() {
            return this.map.size();
        }

        @Override // java.util.Map
        public boolean isEmpty() {
            return this.map.isEmpty();
        }

        @Override // java.util.Map
        public void clear() {
            throw new UnsupportedOperationException("clear");
        }

        @Override // io.netty.util.collection.IntObjectMap
        public boolean containsKey(int key) {
            return this.map.containsKey(key);
        }

        @Override // java.util.Map
        public boolean containsValue(Object value) {
            return this.map.containsValue(value);
        }

        @Override // java.util.Map
        public boolean containsKey(Object key) {
            return this.map.containsKey(key);
        }

        @Override // java.util.Map
        public V get(Object key) {
            return this.map.get(key);
        }

        /* renamed from: put, reason: avoid collision after fix types in other method */
        public V put2(Integer key, V value) {
            throw new UnsupportedOperationException("put");
        }

        @Override // java.util.Map
        public V remove(Object key) {
            throw new UnsupportedOperationException("remove");
        }

        @Override // java.util.Map
        public void putAll(Map<? extends Integer, ? extends V> m) {
            throw new UnsupportedOperationException("putAll");
        }

        @Override // io.netty.util.collection.IntObjectMap
        public Iterable<IntObjectMap.PrimitiveEntry<V>> entries() {
            if (this.entries == null) {
                this.entries = new Iterable<IntObjectMap.PrimitiveEntry<V>>() { // from class: io.netty.util.collection.IntCollections.UnmodifiableMap.1
                    @Override // java.lang.Iterable
                    public Iterator<IntObjectMap.PrimitiveEntry<V>> iterator() {
                        return new IteratorImpl(UnmodifiableMap.this.map.entries().iterator());
                    }
                };
            }
            return this.entries;
        }

        @Override // java.util.Map
        public Set<Integer> keySet() {
            if (this.keySet == null) {
                this.keySet = Collections.unmodifiableSet(this.map.keySet());
            }
            return this.keySet;
        }

        @Override // java.util.Map
        public Set<Map.Entry<Integer, V>> entrySet() {
            if (this.entrySet == null) {
                this.entrySet = Collections.unmodifiableSet(this.map.entrySet());
            }
            return this.entrySet;
        }

        @Override // java.util.Map
        public Collection<V> values() {
            if (this.values == null) {
                this.values = Collections.unmodifiableCollection(this.map.values());
            }
            return this.values;
        }

        /* loaded from: classes4.dex */
        private class IteratorImpl implements Iterator<IntObjectMap.PrimitiveEntry<V>> {
            final Iterator<IntObjectMap.PrimitiveEntry<V>> iter;

            IteratorImpl(Iterator<IntObjectMap.PrimitiveEntry<V>> iter) {
                this.iter = iter;
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.iter.hasNext();
            }

            @Override // java.util.Iterator
            public IntObjectMap.PrimitiveEntry<V> next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return new EntryImpl(this.iter.next());
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException("remove");
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public class EntryImpl implements IntObjectMap.PrimitiveEntry<V> {
            private final IntObjectMap.PrimitiveEntry<V> entry;

            EntryImpl(IntObjectMap.PrimitiveEntry<V> entry) {
                this.entry = entry;
            }

            @Override // io.netty.util.collection.IntObjectMap.PrimitiveEntry
            public int key() {
                return this.entry.key();
            }

            @Override // io.netty.util.collection.IntObjectMap.PrimitiveEntry
            public V value() {
                return this.entry.value();
            }

            @Override // io.netty.util.collection.IntObjectMap.PrimitiveEntry
            public void setValue(V value) {
                throw new UnsupportedOperationException("setValue");
            }
        }
    }
}
