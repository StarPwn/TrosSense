package io.netty.util.collection;

import io.netty.util.collection.ShortObjectMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/* loaded from: classes4.dex */
public final class ShortCollections {
    private static final ShortObjectMap<Object> EMPTY_MAP = new EmptyMap();

    private ShortCollections() {
    }

    public static <V> ShortObjectMap<V> emptyMap() {
        return (ShortObjectMap<V>) EMPTY_MAP;
    }

    public static <V> ShortObjectMap<V> unmodifiableMap(ShortObjectMap<V> map) {
        return new UnmodifiableMap(map);
    }

    /* loaded from: classes4.dex */
    private static final class EmptyMap implements ShortObjectMap<Object> {
        private EmptyMap() {
        }

        @Override // io.netty.util.collection.ShortObjectMap
        public Object get(short key) {
            return null;
        }

        @Override // io.netty.util.collection.ShortObjectMap
        public Object put(short key, Object value) {
            throw new UnsupportedOperationException("put");
        }

        @Override // io.netty.util.collection.ShortObjectMap
        public Object remove(short key) {
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
        public Set<Short> keySet() {
            return Collections.emptySet();
        }

        @Override // io.netty.util.collection.ShortObjectMap
        public boolean containsKey(short key) {
            return false;
        }

        @Override // java.util.Map
        public boolean containsValue(Object value) {
            return false;
        }

        @Override // io.netty.util.collection.ShortObjectMap
        public Iterable<ShortObjectMap.PrimitiveEntry<Object>> entries() {
            return Collections.emptySet();
        }

        @Override // java.util.Map
        public Object get(Object key) {
            return null;
        }

        @Override // java.util.Map
        public Object put(Short key, Object value) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public Object remove(Object key) {
            return null;
        }

        @Override // java.util.Map
        public void putAll(Map<? extends Short, ?> m) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public Collection<Object> values() {
            return Collections.emptyList();
        }

        @Override // java.util.Map
        public Set<Map.Entry<Short, Object>> entrySet() {
            return Collections.emptySet();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class UnmodifiableMap<V> implements ShortObjectMap<V> {
        private Iterable<ShortObjectMap.PrimitiveEntry<V>> entries;
        private Set<Map.Entry<Short, V>> entrySet;
        private Set<Short> keySet;
        private final ShortObjectMap<V> map;
        private Collection<V> values;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Map
        public /* bridge */ /* synthetic */ Object put(Short sh, Object obj) {
            return put2(sh, (Short) obj);
        }

        UnmodifiableMap(ShortObjectMap<V> map) {
            this.map = map;
        }

        @Override // io.netty.util.collection.ShortObjectMap
        public V get(short key) {
            return this.map.get(key);
        }

        @Override // io.netty.util.collection.ShortObjectMap
        public V put(short key, V value) {
            throw new UnsupportedOperationException("put");
        }

        @Override // io.netty.util.collection.ShortObjectMap
        public V remove(short key) {
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

        @Override // io.netty.util.collection.ShortObjectMap
        public boolean containsKey(short key) {
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
        public V put2(Short key, V value) {
            throw new UnsupportedOperationException("put");
        }

        @Override // java.util.Map
        public V remove(Object key) {
            throw new UnsupportedOperationException("remove");
        }

        @Override // java.util.Map
        public void putAll(Map<? extends Short, ? extends V> m) {
            throw new UnsupportedOperationException("putAll");
        }

        @Override // io.netty.util.collection.ShortObjectMap
        public Iterable<ShortObjectMap.PrimitiveEntry<V>> entries() {
            if (this.entries == null) {
                this.entries = new Iterable<ShortObjectMap.PrimitiveEntry<V>>() { // from class: io.netty.util.collection.ShortCollections.UnmodifiableMap.1
                    @Override // java.lang.Iterable
                    public Iterator<ShortObjectMap.PrimitiveEntry<V>> iterator() {
                        return new IteratorImpl(UnmodifiableMap.this.map.entries().iterator());
                    }
                };
            }
            return this.entries;
        }

        @Override // java.util.Map
        public Set<Short> keySet() {
            if (this.keySet == null) {
                this.keySet = Collections.unmodifiableSet(this.map.keySet());
            }
            return this.keySet;
        }

        @Override // java.util.Map
        public Set<Map.Entry<Short, V>> entrySet() {
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
        private class IteratorImpl implements Iterator<ShortObjectMap.PrimitiveEntry<V>> {
            final Iterator<ShortObjectMap.PrimitiveEntry<V>> iter;

            IteratorImpl(Iterator<ShortObjectMap.PrimitiveEntry<V>> iter) {
                this.iter = iter;
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.iter.hasNext();
            }

            @Override // java.util.Iterator
            public ShortObjectMap.PrimitiveEntry<V> next() {
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
        public class EntryImpl implements ShortObjectMap.PrimitiveEntry<V> {
            private final ShortObjectMap.PrimitiveEntry<V> entry;

            EntryImpl(ShortObjectMap.PrimitiveEntry<V> entry) {
                this.entry = entry;
            }

            @Override // io.netty.util.collection.ShortObjectMap.PrimitiveEntry
            public short key() {
                return this.entry.key();
            }

            @Override // io.netty.util.collection.ShortObjectMap.PrimitiveEntry
            public V value() {
                return this.entry.value();
            }

            @Override // io.netty.util.collection.ShortObjectMap.PrimitiveEntry
            public void setValue(V value) {
                throw new UnsupportedOperationException("setValue");
            }
        }
    }
}
