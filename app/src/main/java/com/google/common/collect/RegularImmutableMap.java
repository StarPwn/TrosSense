package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMapEntry;
import com.google.common.collect.ImmutableMapEntrySet;
import com.google.common.collect.ImmutableSet;
import java.io.Serializable;
import java.util.Map;
import java.util.function.BiConsumer;
import javax.annotation.Nullable;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class RegularImmutableMap<K, V> extends ImmutableMap<K, V> {
    private static final double MAX_LOAD_FACTOR = 1.2d;
    private static final long serialVersionUID = 0;
    private final transient Map.Entry<K, V>[] entries;
    private final transient int mask;
    private final transient ImmutableMapEntry<K, V>[] table;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <K, V> RegularImmutableMap<K, V> fromEntries(Map.Entry<K, V>... entries) {
        return fromEntryArray(entries.length, entries);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <K, V> RegularImmutableMap<K, V> fromEntryArray(int n, Map.Entry<K, V>[] entryArray) {
        Map.Entry<K, V>[] entries;
        ImmutableMapEntry<K, V> newEntry;
        Preconditions.checkPositionIndex(n, entryArray.length);
        if (n == entryArray.length) {
            entries = entryArray;
        } else {
            entries = ImmutableMapEntry.createEntryArray(n);
        }
        int tableSize = Hashing.closedTableSize(n, MAX_LOAD_FACTOR);
        ImmutableMapEntry<K, V>[] table = ImmutableMapEntry.createEntryArray(tableSize);
        int mask = tableSize - 1;
        for (int entryIndex = 0; entryIndex < n; entryIndex++) {
            Map.Entry<K, V> entry = entryArray[entryIndex];
            K key = entry.getKey();
            V value = entry.getValue();
            CollectPreconditions.checkEntryNotNull(key, value);
            int tableIndex = Hashing.smear(key.hashCode()) & mask;
            ImmutableMapEntry<K, V> existing = table[tableIndex];
            if (existing == null) {
                boolean reusable = (entry instanceof ImmutableMapEntry) && ((ImmutableMapEntry) entry).isReusable();
                newEntry = reusable ? (ImmutableMapEntry) entry : new ImmutableMapEntry<>(key, value);
            } else {
                newEntry = new ImmutableMapEntry.NonTerminalImmutableMapEntry<>(key, value, existing);
            }
            table[tableIndex] = newEntry;
            entries[entryIndex] = newEntry;
            checkNoConflictInKeyBucket(key, newEntry, existing);
        }
        return new RegularImmutableMap<>(entries, table, mask);
    }

    private RegularImmutableMap(Map.Entry<K, V>[] entries, ImmutableMapEntry<K, V>[] table, int mask) {
        this.entries = entries;
        this.table = table;
        this.mask = mask;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkNoConflictInKeyBucket(Object key, Map.Entry<?, ?> entry, @Nullable ImmutableMapEntry<?, ?> keyBucketHead) {
        while (keyBucketHead != null) {
            checkNoConflict(!key.equals(keyBucketHead.getKey()), "key", entry, keyBucketHead);
            keyBucketHead = keyBucketHead.getNextInKeyBucket();
        }
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public V get(@Nullable Object obj) {
        return (V) get(obj, this.table, this.mask);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nullable
    public static <V> V get(@Nullable Object key, ImmutableMapEntry<?, V>[] keyTable, int mask) {
        if (key == null) {
            return null;
        }
        int index = Hashing.smear(key.hashCode()) & mask;
        for (ImmutableMapEntry<?, V> entry = keyTable[index]; entry != null; entry = entry.getNextInKeyBucket()) {
            Object candidateKey = entry.getKey();
            if (key.equals(candidateKey)) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override // java.util.Map
    public void forEach(BiConsumer<? super K, ? super V> biConsumer) {
        Preconditions.checkNotNull(biConsumer);
        for (Map.Entry<K, V> entry : this.entries) {
            biConsumer.accept(entry.getKey(), entry.getValue());
        }
    }

    @Override // java.util.Map
    public int size() {
        return this.entries.length;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableMap
    public boolean isPartialView() {
        return false;
    }

    @Override // com.google.common.collect.ImmutableMap
    ImmutableSet<Map.Entry<K, V>> createEntrySet() {
        return new ImmutableMapEntrySet.RegularEntrySet(this, this.entries);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableMap
    public ImmutableSet<K> createKeySet() {
        return new KeySet(this);
    }

    /* loaded from: classes.dex */
    private static final class KeySet<K, V> extends ImmutableSet.Indexed<K> {
        private final RegularImmutableMap<K, V> map;

        KeySet(RegularImmutableMap<K, V> map) {
            this.map = map;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableSet.Indexed
        public K get(int i) {
            return (K) ((RegularImmutableMap) this.map).entries[i].getKey();
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object object) {
            return this.map.containsKey(object);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableCollection
        public boolean isPartialView() {
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.map.size();
        }

        @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
        Object writeReplace() {
            return new SerializedForm(this.map);
        }

        /* loaded from: classes.dex */
        private static class SerializedForm<K> implements Serializable {
            private static final long serialVersionUID = 0;
            final ImmutableMap<K, ?> map;

            SerializedForm(ImmutableMap<K, ?> map) {
                this.map = map;
            }

            Object readResolve() {
                return this.map.keySet();
            }
        }
    }

    @Override // com.google.common.collect.ImmutableMap
    ImmutableCollection<V> createValues() {
        return new Values(this);
    }

    /* loaded from: classes.dex */
    private static final class Values<K, V> extends ImmutableList<V> {
        final RegularImmutableMap<K, V> map;

        Values(RegularImmutableMap<K, V> map) {
            this.map = map;
        }

        @Override // java.util.List
        public V get(int i) {
            return (V) ((RegularImmutableMap) this.map).entries[i].getValue();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.map.size();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableCollection
        public boolean isPartialView() {
            return true;
        }

        @Override // com.google.common.collect.ImmutableList, com.google.common.collect.ImmutableCollection
        Object writeReplace() {
            return new SerializedForm(this.map);
        }

        /* loaded from: classes.dex */
        private static class SerializedForm<V> implements Serializable {
            private static final long serialVersionUID = 0;
            final ImmutableMap<?, V> map;

            SerializedForm(ImmutableMap<?, V> map) {
                this.map = map;
            }

            Object readResolve() {
                return this.map.values();
            }
        }
    }
}
