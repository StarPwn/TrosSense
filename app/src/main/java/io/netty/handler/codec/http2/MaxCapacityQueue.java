package io.netty.handler.codec.http2;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

/* loaded from: classes4.dex */
final class MaxCapacityQueue<E> implements Queue<E> {
    private final int maxCapacity;
    private final Queue<E> queue;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MaxCapacityQueue(Queue<E> queue, int maxCapacity) {
        this.queue = queue;
        this.maxCapacity = maxCapacity;
    }

    @Override // java.util.Queue, java.util.Collection
    public boolean add(E element) {
        if (offer(element)) {
            return true;
        }
        throw new IllegalStateException();
    }

    @Override // java.util.Queue
    public boolean offer(E element) {
        if (this.maxCapacity <= this.queue.size()) {
            return false;
        }
        return this.queue.offer(element);
    }

    @Override // java.util.Queue
    public E remove() {
        return this.queue.remove();
    }

    @Override // java.util.Queue
    public E poll() {
        return this.queue.poll();
    }

    @Override // java.util.Queue
    public E element() {
        return this.queue.element();
    }

    @Override // java.util.Queue
    public E peek() {
        return this.queue.peek();
    }

    @Override // java.util.Collection
    public int size() {
        return this.queue.size();
    }

    @Override // java.util.Collection
    public boolean isEmpty() {
        return this.queue.isEmpty();
    }

    @Override // java.util.Collection
    public boolean contains(Object o) {
        return this.queue.contains(o);
    }

    @Override // java.util.Collection, java.lang.Iterable
    public Iterator<E> iterator() {
        return this.queue.iterator();
    }

    @Override // java.util.Collection
    public Object[] toArray() {
        return this.queue.toArray();
    }

    @Override // java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        return (T[]) this.queue.toArray(tArr);
    }

    @Override // java.util.Collection
    public boolean remove(Object o) {
        return this.queue.remove(o);
    }

    @Override // java.util.Collection
    public boolean containsAll(Collection<?> c) {
        return this.queue.containsAll(c);
    }

    @Override // java.util.Collection
    public boolean addAll(Collection<? extends E> c) {
        if (this.maxCapacity >= size() + c.size()) {
            return this.queue.addAll(c);
        }
        throw new IllegalStateException();
    }

    @Override // java.util.Collection
    public boolean removeAll(Collection<?> c) {
        return this.queue.removeAll(c);
    }

    @Override // java.util.Collection
    public boolean retainAll(Collection<?> c) {
        return this.queue.retainAll(c);
    }

    @Override // java.util.Collection
    public void clear() {
        this.queue.clear();
    }
}
