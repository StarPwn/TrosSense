package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.longs.AbstractLong2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.AbstractObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Comparator;
import java.util.Set;

/* loaded from: classes4.dex */
public abstract class AbstractLong2ObjectSortedMap<V> extends AbstractLong2ObjectMap<V> implements Long2ObjectSortedMap<V> {
    private static final long serialVersionUID = -1773560792952436569L;

    @Override // it.unimi.dsi.fastutil.longs.AbstractLong2ObjectMap, it.unimi.dsi.fastutil.longs.Long2ObjectMap, java.util.Map
    /* renamed from: keySet, reason: avoid collision after fix types in other method */
    public Set<Long> keySet2() {
        return new KeySet();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public class KeySet extends AbstractLongSortedSet {
        /* JADX INFO: Access modifiers changed from: protected */
        public KeySet() {
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public boolean contains(long k) {
            return AbstractLong2ObjectSortedMap.this.containsKey(k);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return AbstractLong2ObjectSortedMap.this.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            AbstractLong2ObjectSortedMap.this.clear();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet, java.util.SortedSet
        /* renamed from: comparator */
        public Comparator<? super Long> comparator2() {
            return AbstractLong2ObjectSortedMap.this.comparator2();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public long firstLong() {
            return AbstractLong2ObjectSortedMap.this.firstLongKey();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public long lastLong() {
            return AbstractLong2ObjectSortedMap.this.lastLongKey();
        }

        /* JADX WARN: Type inference failed for: r0v2, types: [it.unimi.dsi.fastutil.longs.LongSortedSet] */
        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public LongSortedSet headSet(long to) {
            return AbstractLong2ObjectSortedMap.this.headMap(to).keySet2();
        }

        /* JADX WARN: Type inference failed for: r0v2, types: [it.unimi.dsi.fastutil.longs.LongSortedSet] */
        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public LongSortedSet tailSet(long from) {
            return AbstractLong2ObjectSortedMap.this.tailMap(from).keySet2();
        }

        /* JADX WARN: Type inference failed for: r0v2, types: [it.unimi.dsi.fastutil.longs.LongSortedSet] */
        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public LongSortedSet subSet(long from, long to) {
            return AbstractLong2ObjectSortedMap.this.subMap(from, to).keySet2();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSortedSet
        public LongBidirectionalIterator iterator(long j) {
            return new KeySetIterator(AbstractLong2ObjectSortedMap.this.long2ObjectEntrySet().iterator(new AbstractLong2ObjectMap.BasicEntry(j, (Object) null)));
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongSortedSet, it.unimi.dsi.fastutil.longs.AbstractLongSet, it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
        public LongBidirectionalIterator iterator() {
            return new KeySetIterator(Long2ObjectSortedMaps.fastIterator(AbstractLong2ObjectSortedMap.this));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class KeySetIterator<V> implements LongBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Long2ObjectMap.Entry<V>> i;

        public KeySetIterator(ObjectBidirectionalIterator<Long2ObjectMap.Entry<V>> i) {
            this.i = i;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong
        public long nextLong() {
            return ((Long2ObjectMap.Entry) this.i.next()).getLongKey();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongBidirectionalIterator
        public long previousLong() {
            return this.i.previous().getLongKey();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.i.hasNext();
        }

        @Override // it.unimi.dsi.fastutil.BidirectionalIterator
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLong2ObjectMap, it.unimi.dsi.fastutil.longs.Long2ObjectMap, java.util.Map
    public ObjectCollection<V> values() {
        return new ValuesCollection();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public class ValuesCollection extends AbstractObjectCollection<V> {
        protected ValuesCollection() {
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        public ObjectIterator<V> iterator() {
            return new ValuesIterator(Long2ObjectSortedMaps.fastIterator(AbstractLong2ObjectSortedMap.this));
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object k) {
            return AbstractLong2ObjectSortedMap.this.containsValue(k);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return AbstractLong2ObjectSortedMap.this.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            AbstractLong2ObjectSortedMap.this.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class ValuesIterator<V> implements ObjectIterator<V> {
        protected final ObjectBidirectionalIterator<Long2ObjectMap.Entry<V>> i;

        public ValuesIterator(ObjectBidirectionalIterator<Long2ObjectMap.Entry<V>> i) {
            this.i = i;
        }

        @Override // java.util.Iterator
        public V next() {
            return ((Long2ObjectMap.Entry) this.i.next()).getValue();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
