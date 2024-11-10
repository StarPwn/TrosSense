package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.AbstractObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Comparator;
import java.util.Set;

/* loaded from: classes4.dex */
public abstract class AbstractInt2ObjectSortedMap<V> extends AbstractInt2ObjectMap<V> implements Int2ObjectSortedMap<V> {
    private static final long serialVersionUID = -1773560792952436569L;

    @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
    /* renamed from: keySet, reason: avoid collision after fix types in other method */
    public Set<Integer> keySet2() {
        return new KeySet();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public class KeySet extends AbstractIntSortedSet {
        /* JADX INFO: Access modifiers changed from: protected */
        public KeySet() {
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public boolean contains(int k) {
            return AbstractInt2ObjectSortedMap.this.containsKey(k);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return AbstractInt2ObjectSortedMap.this.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            AbstractInt2ObjectSortedMap.this.clear();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet, java.util.SortedSet
        /* renamed from: comparator */
        public Comparator<? super Integer> comparator2() {
            return AbstractInt2ObjectSortedMap.this.comparator2();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
        public int firstInt() {
            return AbstractInt2ObjectSortedMap.this.firstIntKey();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
        public int lastInt() {
            return AbstractInt2ObjectSortedMap.this.lastIntKey();
        }

        /* JADX WARN: Type inference failed for: r0v2, types: [it.unimi.dsi.fastutil.ints.IntSortedSet] */
        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
        public IntSortedSet headSet(int to) {
            return AbstractInt2ObjectSortedMap.this.headMap(to).keySet2();
        }

        /* JADX WARN: Type inference failed for: r0v2, types: [it.unimi.dsi.fastutil.ints.IntSortedSet] */
        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
        public IntSortedSet tailSet(int from) {
            return AbstractInt2ObjectSortedMap.this.tailMap(from).keySet2();
        }

        /* JADX WARN: Type inference failed for: r0v2, types: [it.unimi.dsi.fastutil.ints.IntSortedSet] */
        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
        public IntSortedSet subSet(int from, int to) {
            return AbstractInt2ObjectSortedMap.this.subMap(from, to).keySet2();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSortedSet
        public IntBidirectionalIterator iterator(int i) {
            return new KeySetIterator(AbstractInt2ObjectSortedMap.this.int2ObjectEntrySet().iterator(new AbstractInt2ObjectMap.BasicEntry(i, (Object) null)));
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntSortedSet, it.unimi.dsi.fastutil.ints.AbstractIntSet, it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
        public IntBidirectionalIterator iterator() {
            return new KeySetIterator(Int2ObjectSortedMaps.fastIterator(AbstractInt2ObjectSortedMap.this));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class KeySetIterator<V> implements IntBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Int2ObjectMap.Entry<V>> i;

        public KeySetIterator(ObjectBidirectionalIterator<Int2ObjectMap.Entry<V>> i) {
            this.i = i;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt
        public int nextInt() {
            return ((Int2ObjectMap.Entry) this.i.next()).getIntKey();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator
        public int previousInt() {
            return this.i.previous().getIntKey();
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

    @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
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
            return new ValuesIterator(Int2ObjectSortedMaps.fastIterator(AbstractInt2ObjectSortedMap.this));
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object k) {
            return AbstractInt2ObjectSortedMap.this.containsValue(k);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return AbstractInt2ObjectSortedMap.this.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            AbstractInt2ObjectSortedMap.this.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class ValuesIterator<V> implements ObjectIterator<V> {
        protected final ObjectBidirectionalIterator<Int2ObjectMap.Entry<V>> i;

        public ValuesIterator(ObjectBidirectionalIterator<Int2ObjectMap.Entry<V>> i) {
            this.i = i;
        }

        @Override // java.util.Iterator
        public V next() {
            return ((Int2ObjectMap.Entry) this.i.next()).getValue();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
