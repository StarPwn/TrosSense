package io.netty.util.collection;

import io.netty.util.collection.CharObjectMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/* loaded from: classes4.dex */
public final class CharCollections {
    private static final CharObjectMap<Object> EMPTY_MAP = new EmptyMap();

    private CharCollections() {
    }

    public static <V> CharObjectMap<V> emptyMap() {
        return (CharObjectMap<V>) EMPTY_MAP;
    }

    public static <V> CharObjectMap<V> unmodifiableMap(CharObjectMap<V> map) {
        return new UnmodifiableMap(map);
    }

    /* loaded from: classes4.dex */
    private static final class EmptyMap implements CharObjectMap<Object> {
        private EmptyMap() {
        }

        @Override // io.netty.util.collection.CharObjectMap
        public Object get(char key) {
            return null;
        }

        @Override // io.netty.util.collection.CharObjectMap
        public Object put(char key, Object value) {
            throw new UnsupportedOperationException("put");
        }

        @Override // io.netty.util.collection.CharObjectMap
        public Object remove(char key) {
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
        public Set<Character> keySet() {
            return Collections.emptySet();
        }

        @Override // io.netty.util.collection.CharObjectMap
        public boolean containsKey(char key) {
            return false;
        }

        @Override // java.util.Map
        public boolean containsValue(Object value) {
            return false;
        }

        @Override // io.netty.util.collection.CharObjectMap
        public Iterable<CharObjectMap.PrimitiveEntry<Object>> entries() {
            return Collections.emptySet();
        }

        @Override // java.util.Map
        public Object get(Object key) {
            return null;
        }

        @Override // java.util.Map
        public Object put(Character key, Object value) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public Object remove(Object key) {
            return null;
        }

        @Override // java.util.Map
        public void putAll(Map<? extends Character, ?> m) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public Collection<Object> values() {
            return Collections.emptyList();
        }

        @Override // java.util.Map
        public Set<Map.Entry<Character, Object>> entrySet() {
            return Collections.emptySet();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class UnmodifiableMap<V> implements CharObjectMap<V> {
        private Iterable<CharObjectMap.PrimitiveEntry<V>> entries;
        private Set<Map.Entry<Character, V>> entrySet;
        private Set<Character> keySet;
        private final CharObjectMap<V> map;
        private Collection<V> values;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Map
        public /* bridge */ /* synthetic */ Object put(Character ch, Object obj) {
            return put2(ch, (Character) obj);
        }

        UnmodifiableMap(CharObjectMap<V> map) {
            this.map = map;
        }

        @Override // io.netty.util.collection.CharObjectMap
        public V get(char key) {
            return this.map.get(key);
        }

        @Override // io.netty.util.collection.CharObjectMap
        public V put(char key, V value) {
            throw new UnsupportedOperationException("put");
        }

        @Override // io.netty.util.collection.CharObjectMap
        public V remove(char key) {
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

        @Override // io.netty.util.collection.CharObjectMap
        public boolean containsKey(char key) {
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
        public V put2(Character key, V value) {
            throw new UnsupportedOperationException("put");
        }

        @Override // java.util.Map
        public V remove(Object key) {
            throw new UnsupportedOperationException("remove");
        }

        @Override // java.util.Map
        public void putAll(Map<? extends Character, ? extends V> m) {
            throw new UnsupportedOperationException("putAll");
        }

        @Override // io.netty.util.collection.CharObjectMap
        public Iterable<CharObjectMap.PrimitiveEntry<V>> entries() {
            if (this.entries == null) {
                this.entries = new Iterable<CharObjectMap.PrimitiveEntry<V>>() { // from class: io.netty.util.collection.CharCollections.UnmodifiableMap.1
                    @Override // java.lang.Iterable
                    public Iterator<CharObjectMap.PrimitiveEntry<V>> iterator() {
                        return new IteratorImpl(UnmodifiableMap.this.map.entries().iterator());
                    }
                };
            }
            return this.entries;
        }

        @Override // java.util.Map
        public Set<Character> keySet() {
            if (this.keySet == null) {
                this.keySet = Collections.unmodifiableSet(this.map.keySet());
            }
            return this.keySet;
        }

        @Override // java.util.Map
        public Set<Map.Entry<Character, V>> entrySet() {
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
        private class IteratorImpl implements Iterator<CharObjectMap.PrimitiveEntry<V>> {
            final Iterator<CharObjectMap.PrimitiveEntry<V>> iter;

            IteratorImpl(Iterator<CharObjectMap.PrimitiveEntry<V>> iter) {
                this.iter = iter;
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.iter.hasNext();
            }

            @Override // java.util.Iterator
            public CharObjectMap.PrimitiveEntry<V> next() {
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
        public class EntryImpl implements CharObjectMap.PrimitiveEntry<V> {
            private final CharObjectMap.PrimitiveEntry<V> entry;

            EntryImpl(CharObjectMap.PrimitiveEntry<V> entry) {
                this.entry = entry;
            }

            @Override // io.netty.util.collection.CharObjectMap.PrimitiveEntry
            public char key() {
                return this.entry.key();
            }

            @Override // io.netty.util.collection.CharObjectMap.PrimitiveEntry
            public V value() {
                return this.entry.value();
            }

            @Override // io.netty.util.collection.CharObjectMap.PrimitiveEntry
            public void setValue(V value) {
                throw new UnsupportedOperationException("setValue");
            }
        }
    }
}
