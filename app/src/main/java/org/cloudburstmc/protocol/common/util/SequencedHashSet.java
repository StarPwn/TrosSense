package org.cloudburstmc.protocol.common.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/* loaded from: classes5.dex */
public class SequencedHashSet<E> implements List<E> {
    private final Object2IntMap<E> map = new Object2IntLinkedOpenHashMap();
    private final Int2ObjectMap<E> inverse = new Int2ObjectLinkedOpenHashMap();
    private int index = 0;

    @Override // java.util.List
    public int indexOf(Object o) {
        return this.map.getInt(o);
    }

    @Override // java.util.List
    public int lastIndexOf(Object o) {
        return this.map.getInt(o);
    }

    @Override // java.util.List
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public int size() {
        return this.map.size();
    }

    @Override // java.util.List, java.util.Collection
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override // java.util.List, java.util.Collection
    public boolean contains(Object o) {
        return this.map.containsKey(o);
    }

    @Override // java.util.List, java.util.Collection, java.lang.Iterable
    public Iterator<E> iterator() {
        return this.map.keySet().iterator();
    }

    @Override // java.util.List, java.util.Collection
    public Object[] toArray() {
        return this.map.keySet().toArray();
    }

    @Override // java.util.List, java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        return (T[]) this.map.keySet().toArray(tArr);
    }

    @Override // java.util.List, java.util.Collection
    public boolean add(E e) {
        if (!this.map.containsKey(e)) {
            int index = this.index;
            this.index = index + 1;
            this.map.put((Object2IntMap<E>) e, index);
            this.inverse.put(index, (int) e);
            return true;
        }
        return false;
    }

    @Override // java.util.List, java.util.Collection
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public boolean containsAll(Collection<?> c) {
        return this.map.keySet().containsAll(c);
    }

    @Override // java.util.List, java.util.Collection
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) {
            add(e);
        }
        return true;
    }

    @Override // java.util.List
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public E get(int index) {
        return this.inverse.get(index);
    }

    @Override // java.util.List
    public E set(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public E remove(int index) {
        throw new UnsupportedOperationException();
    }

    public String toString() {
        return this.map.keySet().toString();
    }
}
