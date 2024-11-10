package org.cloudburstmc.netty.util;

import io.netty.util.ReferenceCountUtil;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* loaded from: classes5.dex */
public class RoundRobinArray<E> implements Collection<E> {
    private final Object[] elements;
    private final int mask;

    public RoundRobinArray(int fixedCapacity) {
        int fixedCapacity2 = RakUtils.powerOfTwoCeiling(fixedCapacity);
        this.elements = new Object[fixedCapacity2];
        this.mask = fixedCapacity2 - 1;
    }

    public E get(int i) {
        return (E) this.elements[this.mask & i];
    }

    public void set(int index, E value) {
        int idx = this.mask & index;
        Object element = this.elements[idx];
        this.elements[idx] = value;
        ReferenceCountUtil.release(element);
    }

    public void remove(int index, E expected) {
        int idx = this.mask & index;
        Object element = this.elements[idx];
        if (element == expected) {
            this.elements[idx] = null;
            ReferenceCountUtil.release(element);
        }
    }

    @Override // java.util.Collection
    public int size() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Collection
    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Collection
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Collection, java.lang.Iterable
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override // java.util.Collection
    public Object[] toArray() {
        return Arrays.copyOf(this.elements, this.elements.length);
    }

    @Override // java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        if (tArr.length < this.elements.length) {
            return (T[]) Arrays.copyOf(this.elements, this.elements.length, tArr.getClass());
        }
        System.arraycopy(this.elements, 0, tArr, 0, this.elements.length);
        if (tArr.length > this.elements.length) {
            tArr[this.elements.length] = null;
        }
        return tArr;
    }

    @Override // java.util.Collection
    public boolean add(E e) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Collection
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Collection
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Collection
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Collection
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Collection
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Collection
    public void clear() {
        Arrays.fill(this.elements, (Object) null);
    }

    /* loaded from: classes5.dex */
    private class Itr implements Iterator<E> {
        int cursor;
        int lastRet = -1;

        Itr() {
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.cursor != RoundRobinArray.this.elements.length;
        }

        @Override // java.util.Iterator
        public E next() {
            int i = this.cursor;
            if (i >= RoundRobinArray.this.elements.length) {
                throw new NoSuchElementException();
            }
            this.cursor = i + 1;
            Object[] objArr = RoundRobinArray.this.elements;
            this.lastRet = i;
            return (E) objArr[i];
        }

        @Override // java.util.Iterator
        public void remove() {
            if (this.lastRet >= 0) {
                Object object = RoundRobinArray.this.elements[this.lastRet];
                RoundRobinArray.this.elements[this.lastRet] = null;
                ReferenceCountUtil.release(object);
                this.cursor = this.lastRet;
                this.lastRet = -1;
                return;
            }
            throw new IllegalStateException();
        }
    }
}
