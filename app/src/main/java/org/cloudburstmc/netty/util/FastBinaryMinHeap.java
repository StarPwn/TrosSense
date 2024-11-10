package org.cloudburstmc.netty.util;

import io.netty.util.AbstractReferenceCounted;
import io.netty.util.internal.ObjectPool;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;

/* loaded from: classes5.dex */
public class FastBinaryMinHeap<E> extends AbstractReferenceCounted {
    private static final Entry INFIMUM;
    private static final ObjectPool<Entry> RECYCLER = ObjectPool.newPool(new ObjectPool.ObjectCreator() { // from class: org.cloudburstmc.netty.util.FastBinaryMinHeap$$ExternalSyntheticLambda0
        @Override // io.netty.util.internal.ObjectPool.ObjectCreator
        public final Object newObject(ObjectPool.Handle handle) {
            return FastBinaryMinHeap.lambda$static$0(handle);
        }
    });
    private static final Entry SUPREMUM;
    private Entry[] heap;
    private int size;

    static {
        INFIMUM = new Entry(Long.MAX_VALUE);
        SUPREMUM = new Entry(Long.MIN_VALUE);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ Entry lambda$static$0(ObjectPool.Handle x$0) {
        return new Entry(x$0);
    }

    public FastBinaryMinHeap() {
        this(8);
    }

    public FastBinaryMinHeap(int initialCapacity) {
        this.heap = new Entry[initialCapacity + 1];
        Arrays.fill(this.heap, INFIMUM);
        this.heap[0] = SUPREMUM;
    }

    private static Entry newEntry(Object element, long weight) {
        Entry entry = RECYCLER.get();
        entry.element = element;
        entry.weight = weight;
        return entry;
    }

    private void resize(int capacity) {
        int adjustedSize = this.size + 1;
        int copyLength = Math.min(this.heap.length, adjustedSize);
        Entry[] newHeap = new Entry[capacity];
        System.arraycopy(this.heap, 0, newHeap, 0, copyLength);
        if (capacity > adjustedSize) {
            Arrays.fill(newHeap, adjustedSize, capacity, INFIMUM);
        }
        this.heap = newHeap;
    }

    public void insert(long weight, E element) {
        Objects.requireNonNull(element, "element");
        ensureCapacity(this.size + 1);
        insert0(weight, element);
    }

    private void ensureCapacity(int size) {
        if (size + 1 >= this.heap.length) {
            resize(RakUtils.powerOfTwoCeiling(size + 1));
        }
    }

    public E peek() {
        Entry entry = this.heap[1];
        if (entry != null) {
            return (E) entry.element;
        }
        return null;
    }

    private void insert0(long weight, E element) {
        int hole = this.size + 1;
        this.size = hole;
        int pred = hole >> 1;
        long predWeight = this.heap[pred].weight;
        while (predWeight > weight) {
            this.heap[hole] = this.heap[pred];
            hole = pred;
            pred >>= 1;
            predWeight = this.heap[pred].weight;
        }
        this.heap[hole] = newEntry(element, weight);
    }

    public void insertSeries(long weight, E[] elements) {
        Objects.requireNonNull(elements, "elements");
        if (elements.length == 0) {
            return;
        }
        ensureCapacity(this.size + elements.length);
        int i = 0;
        boolean optimized = this.size == 0;
        if (!optimized) {
            optimized = true;
            int parentIdx = 0;
            int currentIdx = this.size;
            while (true) {
                if (parentIdx >= currentIdx) {
                    break;
                }
                if (weight >= this.heap[parentIdx].weight) {
                    parentIdx++;
                } else {
                    optimized = false;
                    break;
                }
            }
        }
        if (optimized) {
            int length = elements.length;
            while (i < length) {
                E element = elements[i];
                Objects.requireNonNull(element, "element");
                Entry[] entryArr = this.heap;
                int i2 = this.size + 1;
                this.size = i2;
                entryArr[i2] = newEntry(element, weight);
                i++;
            }
            return;
        }
        int length2 = elements.length;
        while (i < length2) {
            E element2 = elements[i];
            Objects.requireNonNull(element2, "element");
            insert0(weight, element2);
            i++;
        }
    }

    public E poll() {
        if (this.size <= 0) {
            return null;
        }
        E e = (E) this.heap[1].element;
        remove();
        return e;
    }

    public int size() {
        return this.size;
    }

    public void remove() {
        if (this.size == 0) {
            throw new NoSuchElementException("Heap is empty");
        }
        this.heap[1].release();
        int hole = 1;
        int succ = 2;
        int sz = this.size;
        while (succ < sz) {
            Entry entry1 = this.heap[succ];
            Entry entry2 = this.heap[succ + 1];
            if (entry1.weight > entry2.weight) {
                this.heap[hole] = entry2;
                succ++;
            } else {
                this.heap[hole] = entry1;
            }
            hole = succ;
            succ <<= 1;
        }
        Entry bubble = this.heap[sz];
        for (int pred = hole >> 1; this.heap[pred].weight > bubble.weight; pred >>= 1) {
            this.heap[hole] = this.heap[pred];
            hole = pred;
        }
        this.heap[hole] = bubble;
        this.heap[sz] = INFIMUM;
        this.size--;
        if ((this.size << 2) < this.heap.length && this.size > 4) {
            resize(this.size << 1);
        }
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override // io.netty.util.AbstractReferenceCounted
    protected void deallocate() {
        while (this.size > 0) {
            Entry entry = this.heap[1];
            remove();
            entry.release();
        }
    }

    @Override // io.netty.util.ReferenceCounted
    public FastBinaryMinHeap<E> touch(Object hint) {
        return this;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class Entry extends AbstractReferenceCounted {
        private Object element;
        private final ObjectPool.Handle<Entry> handle;
        private long weight;

        private Entry(long weight) {
            this.weight = weight;
            this.handle = null;
        }

        private Entry(ObjectPool.Handle<Entry> handle) {
            this.handle = handle;
        }

        @Override // io.netty.util.AbstractReferenceCounted
        protected void deallocate() {
            setRefCnt(1);
            if (this.handle == null) {
                return;
            }
            this.element = null;
            this.weight = 0L;
            this.handle.recycle(this);
        }

        @Override // io.netty.util.ReferenceCounted
        public Entry touch(Object hint) {
            return this;
        }
    }
}
