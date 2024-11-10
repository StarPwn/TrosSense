package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import javax.annotation.Nullable;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class ImmutableMapEntrySet<K, V> extends ImmutableSet<Map.Entry<K, V>> {
    abstract ImmutableMap<K, V> map();

    /* loaded from: classes.dex */
    static final class RegularEntrySet<K, V> extends ImmutableMapEntrySet<K, V> {
        private final transient Map.Entry<K, V>[] entries;
        private final transient ImmutableMap<K, V> map;

        /* JADX INFO: Access modifiers changed from: package-private */
        public RegularEntrySet(ImmutableMap<K, V> map, Map.Entry<K, V>[] entries) {
            this.map = map;
            this.entries = entries;
        }

        @Override // com.google.common.collect.ImmutableMapEntrySet
        ImmutableMap<K, V> map() {
            return this.map;
        }

        @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
        public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
            return Iterators.forArray(this.entries);
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.lang.Iterable
        public Spliterator<Map.Entry<K, V>> spliterator() {
            return Spliterators.spliterator(this.entries, 1297);
        }

        @Override // java.lang.Iterable
        public void forEach(Consumer<? super Map.Entry<K, V>> action) {
            Preconditions.checkNotNull(action);
            for (Map.Entry<K, V> entry : this.entries) {
                action.accept(entry);
            }
        }

        @Override // com.google.common.collect.ImmutableSet
        ImmutableList<Map.Entry<K, V>> createAsList() {
            return new RegularImmutableAsList(this, this.entries);
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return map().size();
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(@Nullable Object object) {
        if (!(object instanceof Map.Entry)) {
            return false;
        }
        Map.Entry<?, ?> entry = (Map.Entry) object;
        V value = map().get(entry.getKey());
        return value != null && value.equals(entry.getValue());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableCollection
    public boolean isPartialView() {
        return map().isPartialView();
    }

    @Override // com.google.common.collect.ImmutableSet
    boolean isHashCodeFast() {
        return map().isHashCodeFast();
    }

    @Override // com.google.common.collect.ImmutableSet, java.util.Collection, java.util.Set
    public int hashCode() {
        return map().hashCode();
    }

    @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
    Object writeReplace() {
        return new EntrySetSerializedForm(map());
    }

    /* loaded from: classes.dex */
    private static class EntrySetSerializedForm<K, V> implements Serializable {
        private static final long serialVersionUID = 0;
        final ImmutableMap<K, V> map;

        EntrySetSerializedForm(ImmutableMap<K, V> map) {
            this.map = map;
        }

        Object readResolve() {
            return this.map.entrySet();
        }
    }
}
