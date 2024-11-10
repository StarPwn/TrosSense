package io.netty.util.collection;

import io.netty.util.collection.ByteObjectMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/* loaded from: classes4.dex */
public final class ByteCollections {
    private static final ByteObjectMap<Object> EMPTY_MAP = new EmptyMap();

    private ByteCollections() {
    }

    public static <V> ByteObjectMap<V> emptyMap() {
        return (ByteObjectMap<V>) EMPTY_MAP;
    }

    public static <V> ByteObjectMap<V> unmodifiableMap(ByteObjectMap<V> map) {
        return new UnmodifiableMap(map);
    }

    /* loaded from: classes4.dex */
    private static final class EmptyMap implements ByteObjectMap<Object> {
        private EmptyMap() {
        }

        @Override // io.netty.util.collection.ByteObjectMap
        public Object get(byte key) {
            return null;
        }

        @Override // io.netty.util.collection.ByteObjectMap
        public Object put(byte key, Object value) {
            throw new UnsupportedOperationException("put");
        }

        @Override // io.netty.util.collection.ByteObjectMap
        public Object remove(byte key) {
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
        public Set<Byte> keySet() {
            return Collections.emptySet();
        }

        @Override // io.netty.util.collection.ByteObjectMap
        public boolean containsKey(byte key) {
            return false;
        }

        @Override // java.util.Map
        public boolean containsValue(Object value) {
            return false;
        }

        @Override // io.netty.util.collection.ByteObjectMap
        public Iterable<ByteObjectMap.PrimitiveEntry<Object>> entries() {
            return Collections.emptySet();
        }

        @Override // java.util.Map
        public Object get(Object key) {
            return null;
        }

        @Override // java.util.Map
        public Object put(Byte key, Object value) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public Object remove(Object key) {
            return null;
        }

        @Override // java.util.Map
        public void putAll(Map<? extends Byte, ?> m) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public Collection<Object> values() {
            return Collections.emptyList();
        }

        @Override // java.util.Map
        public Set<Map.Entry<Byte, Object>> entrySet() {
            return Collections.emptySet();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class UnmodifiableMap<V> implements ByteObjectMap<V> {
        private Iterable<ByteObjectMap.PrimitiveEntry<V>> entries;
        private Set<Map.Entry<Byte, V>> entrySet;
        private Set<Byte> keySet;
        private final ByteObjectMap<V> map;
        private Collection<V> values;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Map
        public /* bridge */ /* synthetic */ Object put(Byte b, Object obj) {
            return put2(b, (Byte) obj);
        }

        UnmodifiableMap(ByteObjectMap<V> map) {
            this.map = map;
        }

        @Override // io.netty.util.collection.ByteObjectMap
        public V get(byte key) {
            return this.map.get(key);
        }

        @Override // io.netty.util.collection.ByteObjectMap
        public V put(byte key, V value) {
            throw new UnsupportedOperationException("put");
        }

        @Override // io.netty.util.collection.ByteObjectMap
        public V remove(byte key) {
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

        @Override // io.netty.util.collection.ByteObjectMap
        public boolean containsKey(byte key) {
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
        public V put2(Byte key, V value) {
            throw new UnsupportedOperationException("put");
        }

        @Override // java.util.Map
        public V remove(Object key) {
            throw new UnsupportedOperationException("remove");
        }

        @Override // java.util.Map
        public void putAll(Map<? extends Byte, ? extends V> m) {
            throw new UnsupportedOperationException("putAll");
        }

        @Override // io.netty.util.collection.ByteObjectMap
        public Iterable<ByteObjectMap.PrimitiveEntry<V>> entries() {
            if (this.entries == null) {
                this.entries = new Iterable<ByteObjectMap.PrimitiveEntry<V>>() { // from class: io.netty.util.collection.ByteCollections.UnmodifiableMap.1
                    @Override // java.lang.Iterable
                    public Iterator<ByteObjectMap.PrimitiveEntry<V>> iterator() {
                        return new IteratorImpl(UnmodifiableMap.this.map.entries().iterator());
                    }
                };
            }
            return this.entries;
        }

        @Override // java.util.Map
        public Set<Byte> keySet() {
            if (this.keySet == null) {
                this.keySet = Collections.unmodifiableSet(this.map.keySet());
            }
            return this.keySet;
        }

        @Override // java.util.Map
        public Set<Map.Entry<Byte, V>> entrySet() {
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
        private class IteratorImpl implements Iterator<ByteObjectMap.PrimitiveEntry<V>> {
            final Iterator<ByteObjectMap.PrimitiveEntry<V>> iter;

            IteratorImpl(Iterator<ByteObjectMap.PrimitiveEntry<V>> iter) {
                this.iter = iter;
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.iter.hasNext();
            }

            @Override // java.util.Iterator
            public ByteObjectMap.PrimitiveEntry<V> next() {
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
        public class EntryImpl implements ByteObjectMap.PrimitiveEntry<V> {
            private final ByteObjectMap.PrimitiveEntry<V> entry;

            EntryImpl(ByteObjectMap.PrimitiveEntry<V> entry) {
                this.entry = entry;
            }

            @Override // io.netty.util.collection.ByteObjectMap.PrimitiveEntry
            public byte key() {
                return this.entry.key();
            }

            @Override // io.netty.util.collection.ByteObjectMap.PrimitiveEntry
            public V value() {
                return this.entry.value();
            }

            @Override // io.netty.util.collection.ByteObjectMap.PrimitiveEntry
            public void setValue(V value) {
                throw new UnsupportedOperationException("setValue");
            }
        }
    }
}
