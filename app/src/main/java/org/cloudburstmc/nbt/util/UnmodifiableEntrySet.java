package org.cloudburstmc.nbt.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.cloudburstmc.nbt.util.UnmodifiableEntrySet;

/* loaded from: classes5.dex */
public class UnmodifiableEntrySet<K, V> implements Set<Map.Entry<K, V>>, Collection<Map.Entry<K, V>> {
    private final Set<? extends Map.Entry<K, V>> entrySet;

    public UnmodifiableEntrySet(Set<? extends Map.Entry<K, V>> entrySet) {
        this.entrySet = entrySet;
    }

    static <K, V> Consumer<Map.Entry<K, V>> entryConsumer(final Consumer<? super Map.Entry<K, V>> action) {
        return new Consumer() { // from class: org.cloudburstmc.nbt.util.UnmodifiableEntrySet$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                action.accept(new UnmodifiableEntrySet.UnmodifiableEntry((Map.Entry) obj));
            }
        };
    }

    @Override // java.lang.Iterable
    public void forEach(Consumer<? super Map.Entry<K, V>> action) {
        Objects.requireNonNull(action);
        this.entrySet.forEach(entryConsumer(action));
    }

    @Override // java.util.Set, java.util.Collection
    public int hashCode() {
        return this.entrySet.hashCode();
    }

    @Override // java.util.Set, java.util.Collection
    public int size() {
        return this.entrySet.size();
    }

    @Override // java.util.Set, java.util.Collection
    public boolean isEmpty() {
        return this.entrySet.isEmpty();
    }

    public String toString() {
        return this.entrySet.toString();
    }

    @Override // java.util.Set, java.util.Collection
    public boolean add(Map.Entry<K, V> e) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Set, java.util.Collection
    public boolean addAll(Collection<? extends Map.Entry<K, V>> c) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Set, java.util.Collection
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Set, java.util.Collection
    public boolean removeAll(Collection<?> coll) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Set, java.util.Collection
    public boolean retainAll(Collection<?> coll) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Set, java.util.Collection
    public void clear() {
        throw new UnsupportedOperationException();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes5.dex */
    public static final class UnmodifiableEntrySetSpliterator<K, V> implements Spliterator<Map.Entry<K, V>> {
        final Spliterator<Map.Entry<K, V>> s;

        UnmodifiableEntrySetSpliterator(Spliterator<Map.Entry<K, V>> s) {
            this.s = s;
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super Map.Entry<K, V>> action) {
            Objects.requireNonNull(action);
            return this.s.tryAdvance(UnmodifiableEntrySet.entryConsumer(action));
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super Map.Entry<K, V>> action) {
            Objects.requireNonNull(action);
            this.s.forEachRemaining(UnmodifiableEntrySet.entryConsumer(action));
        }

        @Override // java.util.Spliterator
        public Spliterator<Map.Entry<K, V>> trySplit() {
            Spliterator<Map.Entry<K, V>> split = this.s.trySplit();
            if (split == null) {
                return null;
            }
            return new UnmodifiableEntrySetSpliterator(split);
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.s.estimateSize();
        }

        @Override // java.util.Spliterator
        public long getExactSizeIfKnown() {
            return this.s.getExactSizeIfKnown();
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.s.characteristics();
        }

        @Override // java.util.Spliterator
        public boolean hasCharacteristics(int characteristics) {
            return this.s.hasCharacteristics(characteristics);
        }

        @Override // java.util.Spliterator
        public Comparator<? super Map.Entry<K, V>> getComparator() {
            return this.s.getComparator();
        }
    }

    @Override // java.util.Set, java.util.Collection, java.lang.Iterable
    public Spliterator<Map.Entry<K, V>> spliterator() {
        return new UnmodifiableEntrySetSpliterator(this.entrySet.spliterator());
    }

    @Override // java.util.Collection
    public Stream<Map.Entry<K, V>> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    @Override // java.util.Collection
    public Stream<Map.Entry<K, V>> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }

    @Override // java.util.Set, java.util.Collection, java.lang.Iterable
    public Iterator<Map.Entry<K, V>> iterator() {
        return new Iterator<Map.Entry<K, V>>() { // from class: org.cloudburstmc.nbt.util.UnmodifiableEntrySet.1
            private final Iterator<? extends Map.Entry<? extends K, ? extends V>> i;

            {
                this.i = UnmodifiableEntrySet.this.entrySet.iterator();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.i.hasNext();
            }

            @Override // java.util.Iterator
            public Map.Entry<K, V> next() {
                return new UnmodifiableEntry(this.i.next());
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // java.util.Set, java.util.Collection
    public Object[] toArray() {
        Object[] a = this.entrySet.toArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = new UnmodifiableEntry((Map.Entry) a[i]);
        }
        return a;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Set, java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        T[] tArr2 = (T[]) this.entrySet.toArray(tArr.length == 0 ? tArr : Arrays.copyOf(tArr, 0));
        for (int i = 0; i < tArr2.length; i++) {
            tArr2[i] = new UnmodifiableEntry((Map.Entry) tArr2[i]);
        }
        if (tArr2.length > tArr.length) {
            return tArr2;
        }
        System.arraycopy(tArr2, 0, tArr, 0, tArr2.length);
        if (tArr.length > tArr2.length) {
            tArr[tArr2.length] = null;
        }
        return tArr;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean contains(Object o) {
        if (!(o instanceof Map.Entry)) {
            return false;
        }
        return this.entrySet.contains(new UnmodifiableEntry((Map.Entry) o));
    }

    @Override // java.util.Set, java.util.Collection
    public boolean containsAll(Collection<?> coll) {
        for (Object e : coll) {
            if (!contains(e)) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Set)) {
            return false;
        }
        Set<?> s = (Set) o;
        if (s.size() != this.entrySet.size()) {
            return false;
        }
        return containsAll(s);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class UnmodifiableEntry<K, V> implements Map.Entry<K, V> {
        private Map.Entry<? extends K, ? extends V> e;

        UnmodifiableEntry(Map.Entry<? extends K, ? extends V> e) {
            this.e = (Map.Entry) Objects.requireNonNull(e);
        }

        @Override // java.util.Map.Entry
        public K getKey() {
            return this.e.getKey();
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            return this.e.getValue();
        }

        @Override // java.util.Map.Entry
        public V setValue(V value) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return this.e.hashCode();
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> t = (Map.Entry) o;
            return Objects.equals(this.e.getKey(), t.getKey()) && Objects.equals(this.e.getValue(), t.getValue());
        }

        public String toString() {
            return this.e.toString();
        }
    }
}
