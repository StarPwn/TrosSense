package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.ints.AbstractIntCollection;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.objects.AbstractObject2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;

/* loaded from: classes4.dex */
public abstract class AbstractObject2IntSortedMap<K> extends AbstractObject2IntMap<K> implements Object2IntSortedMap<K> {
    private static final long serialVersionUID = -1773560792952436569L;

    @Override // it.unimi.dsi.fastutil.objects.AbstractObject2IntMap, it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
    public ObjectSortedSet<K> keySet() {
        return new KeySet();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public class KeySet extends AbstractObjectSortedSet<K> {
        /* JADX INFO: Access modifiers changed from: protected */
        public KeySet() {
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public /* bridge */ /* synthetic */ SortedSet headSet(Object obj) {
            return headSet((KeySet) obj);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public /* bridge */ /* synthetic */ SortedSet tailSet(Object obj) {
            return tailSet((KeySet) obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object k) {
            return AbstractObject2IntSortedMap.this.containsKey(k);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return AbstractObject2IntSortedMap.this.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            AbstractObject2IntSortedMap.this.clear();
        }

        @Override // java.util.SortedSet
        public Comparator<? super K> comparator() {
            return AbstractObject2IntSortedMap.this.comparator();
        }

        @Override // java.util.SortedSet
        public K first() {
            return (K) AbstractObject2IntSortedMap.this.firstKey();
        }

        @Override // java.util.SortedSet
        public K last() {
            return (K) AbstractObject2IntSortedMap.this.lastKey();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public ObjectSortedSet<K> headSet(K to) {
            return AbstractObject2IntSortedMap.this.headMap((AbstractObject2IntSortedMap) to).keySet();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public ObjectSortedSet<K> tailSet(K from) {
            return AbstractObject2IntSortedMap.this.tailMap((AbstractObject2IntSortedMap) from).keySet();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet, java.util.SortedSet
        public ObjectSortedSet<K> subSet(K from, K to) {
            return AbstractObject2IntSortedMap.this.subMap((Object) from, (Object) to).keySet();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSortedSet
        public ObjectBidirectionalIterator<K> iterator(K k) {
            return new KeySetIterator(AbstractObject2IntSortedMap.this.object2IntEntrySet().iterator(new AbstractObject2IntMap.BasicEntry(k, 0)));
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet, it.unimi.dsi.fastutil.objects.AbstractObjectSet, it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        public ObjectBidirectionalIterator<K> iterator() {
            return new KeySetIterator(Object2IntSortedMaps.fastIterator(AbstractObject2IntSortedMap.this));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class KeySetIterator<K> implements ObjectBidirectionalIterator<K> {
        protected final ObjectBidirectionalIterator<Object2IntMap.Entry<K>> i;

        public KeySetIterator(ObjectBidirectionalIterator<Object2IntMap.Entry<K>> i) {
            this.i = i;
        }

        @Override // java.util.Iterator
        public K next() {
            return ((Object2IntMap.Entry) this.i.next()).getKey();
        }

        @Override // it.unimi.dsi.fastutil.BidirectionalIterator, java.util.ListIterator
        public K previous() {
            return this.i.previous().getKey();
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

    @Override // it.unimi.dsi.fastutil.objects.AbstractObject2IntMap, it.unimi.dsi.fastutil.objects.Object2IntMap, java.util.Map
    /* renamed from: values */
    public Collection<Integer> values2() {
        return new ValuesCollection();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public class ValuesCollection extends AbstractIntCollection {
        protected ValuesCollection() {
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
        public IntIterator iterator() {
            return new ValuesIterator(Object2IntSortedMaps.fastIterator(AbstractObject2IntSortedMap.this));
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public boolean contains(int k) {
            return AbstractObject2IntSortedMap.this.containsValue(k);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return AbstractObject2IntSortedMap.this.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            AbstractObject2IntSortedMap.this.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class ValuesIterator<K> implements IntIterator {
        protected final ObjectBidirectionalIterator<Object2IntMap.Entry<K>> i;

        public ValuesIterator(ObjectBidirectionalIterator<Object2IntMap.Entry<K>> i) {
            this.i = i;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt
        public int nextInt() {
            return ((Object2IntMap.Entry) this.i.next()).getIntValue();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
