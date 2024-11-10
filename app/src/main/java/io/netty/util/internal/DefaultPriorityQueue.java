package io.netty.util.internal;

import io.netty.util.internal.PriorityQueueNode;
import java.util.AbstractQueue;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* loaded from: classes4.dex */
public final class DefaultPriorityQueue<T extends PriorityQueueNode> extends AbstractQueue<T> implements PriorityQueue<T> {
    private static final PriorityQueueNode[] EMPTY_ARRAY = new PriorityQueueNode[0];
    private final Comparator<T> comparator;
    private T[] queue;
    private int size;

    public DefaultPriorityQueue(Comparator<T> comparator, int i) {
        this.comparator = (Comparator) ObjectUtil.checkNotNull(comparator, "comparator");
        this.queue = (T[]) (i != 0 ? new PriorityQueueNode[i] : EMPTY_ARRAY);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public int size() {
        return this.size;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean contains(Object o) {
        if (!(o instanceof PriorityQueueNode)) {
            return false;
        }
        PriorityQueueNode node = (PriorityQueueNode) o;
        return contains(node, node.priorityQueueIndex(this));
    }

    @Override // io.netty.util.internal.PriorityQueue
    public boolean containsTyped(T node) {
        return contains(node, node.priorityQueueIndex(this));
    }

    @Override // java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection
    public void clear() {
        for (int i = 0; i < this.size; i++) {
            T node = this.queue[i];
            if (node != null) {
                node.priorityQueueIndex(this, -1);
                this.queue[i] = null;
            }
        }
        this.size = 0;
    }

    @Override // io.netty.util.internal.PriorityQueue
    public void clearIgnoringIndexes() {
        this.size = 0;
    }

    @Override // java.util.Queue
    public boolean offer(T t) {
        if (t.priorityQueueIndex(this) != -1) {
            throw new IllegalArgumentException("e.priorityQueueIndex(): " + t.priorityQueueIndex(this) + " (expected: -1) + e: " + t);
        }
        if (this.size >= this.queue.length) {
            this.queue = (T[]) ((PriorityQueueNode[]) Arrays.copyOf(this.queue, this.queue.length + (this.queue.length < 64 ? this.queue.length + 2 : this.queue.length >>> 1)));
        }
        int i = this.size;
        this.size = i + 1;
        bubbleUp(i, t);
        return true;
    }

    @Override // java.util.Queue
    public T poll() {
        if (this.size == 0) {
            return null;
        }
        T result = this.queue[0];
        result.priorityQueueIndex(this, -1);
        T[] tArr = this.queue;
        int i = this.size - 1;
        this.size = i;
        T last = tArr[i];
        this.queue[this.size] = null;
        if (this.size != 0) {
            bubbleDown(0, last);
        }
        return result;
    }

    @Override // java.util.Queue
    public T peek() {
        if (this.size == 0) {
            return null;
        }
        return this.queue[0];
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean remove(Object o) {
        try {
            return removeTyped((DefaultPriorityQueue<T>) o);
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override // io.netty.util.internal.PriorityQueue
    public boolean removeTyped(T node) {
        int i = node.priorityQueueIndex(this);
        if (!contains(node, i)) {
            return false;
        }
        node.priorityQueueIndex(this, -1);
        int i2 = this.size - 1;
        this.size = i2;
        if (i2 == 0 || this.size == i) {
            this.queue[i] = null;
            return true;
        }
        T[] tArr = this.queue;
        T moved = this.queue[this.size];
        tArr[i] = moved;
        this.queue[this.size] = null;
        if (this.comparator.compare(node, moved) < 0) {
            bubbleDown(i, moved);
        } else {
            bubbleUp(i, moved);
        }
        return true;
    }

    @Override // io.netty.util.internal.PriorityQueue
    public void priorityChanged(T node) {
        int i = node.priorityQueueIndex(this);
        if (!contains(node, i)) {
            return;
        }
        if (i == 0) {
            bubbleDown(i, node);
            return;
        }
        int iParent = (i - 1) >>> 1;
        T parent = this.queue[iParent];
        if (this.comparator.compare(node, parent) < 0) {
            bubbleUp(i, node);
        } else {
            bubbleDown(i, node);
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public Object[] toArray() {
        return Arrays.copyOf(this.queue, this.size);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public <X> X[] toArray(X[] xArr) {
        if (xArr.length < this.size) {
            return (X[]) Arrays.copyOf(this.queue, this.size, xArr.getClass());
        }
        System.arraycopy(this.queue, 0, xArr, 0, this.size);
        if (xArr.length > this.size) {
            xArr[this.size] = null;
        }
        return xArr;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    public Iterator<T> iterator() {
        return new PriorityQueueIterator();
    }

    /* loaded from: classes4.dex */
    private final class PriorityQueueIterator implements Iterator<T> {
        private int index;

        private PriorityQueueIterator() {
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.index < DefaultPriorityQueue.this.size;
        }

        @Override // java.util.Iterator
        public T next() {
            if (this.index < DefaultPriorityQueue.this.size) {
                PriorityQueueNode[] priorityQueueNodeArr = DefaultPriorityQueue.this.queue;
                int i = this.index;
                this.index = i + 1;
                return (T) priorityQueueNodeArr[i];
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("remove");
        }
    }

    private boolean contains(PriorityQueueNode node, int i) {
        return i >= 0 && i < this.size && node.equals(this.queue[i]);
    }

    private void bubbleDown(int k, T node) {
        int half = this.size >>> 1;
        while (k < half) {
            int iChild = (k << 1) + 1;
            T child = this.queue[iChild];
            int rightChild = iChild + 1;
            if (rightChild < this.size && this.comparator.compare(child, this.queue[rightChild]) > 0) {
                iChild = rightChild;
                child = this.queue[rightChild];
            }
            if (this.comparator.compare(node, child) <= 0) {
                break;
            }
            this.queue[k] = child;
            child.priorityQueueIndex(this, k);
            k = iChild;
        }
        this.queue[k] = node;
        node.priorityQueueIndex(this, k);
    }

    private void bubbleUp(int k, T node) {
        while (k > 0) {
            int iParent = (k - 1) >>> 1;
            T parent = this.queue[iParent];
            if (this.comparator.compare(node, parent) >= 0) {
                break;
            }
            this.queue[k] = parent;
            parent.priorityQueueIndex(this, k);
            k = iParent;
        }
        this.queue[k] = node;
        node.priorityQueueIndex(this, k);
    }
}
