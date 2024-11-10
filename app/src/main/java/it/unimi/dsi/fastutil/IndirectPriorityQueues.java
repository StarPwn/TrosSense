package it.unimi.dsi.fastutil;

import java.util.Comparator;
import java.util.NoSuchElementException;

/* loaded from: classes4.dex */
public class IndirectPriorityQueues {
    public static final EmptyIndirectPriorityQueue EMPTY_QUEUE = new EmptyIndirectPriorityQueue();

    private IndirectPriorityQueues() {
    }

    /* loaded from: classes4.dex */
    public static class EmptyIndirectPriorityQueue implements IndirectPriorityQueue {
        protected EmptyIndirectPriorityQueue() {
        }

        @Override // it.unimi.dsi.fastutil.IndirectPriorityQueue
        public void enqueue(int i) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.IndirectPriorityQueue
        public int dequeue() {
            throw new NoSuchElementException();
        }

        @Override // it.unimi.dsi.fastutil.IndirectPriorityQueue
        public boolean isEmpty() {
            return true;
        }

        @Override // it.unimi.dsi.fastutil.IndirectPriorityQueue
        public int size() {
            return 0;
        }

        @Override // it.unimi.dsi.fastutil.IndirectPriorityQueue
        public boolean contains(int index) {
            return false;
        }

        @Override // it.unimi.dsi.fastutil.IndirectPriorityQueue
        public void clear() {
        }

        @Override // it.unimi.dsi.fastutil.IndirectPriorityQueue
        public int first() {
            throw new NoSuchElementException();
        }

        @Override // it.unimi.dsi.fastutil.IndirectPriorityQueue
        public int last() {
            throw new NoSuchElementException();
        }

        @Override // it.unimi.dsi.fastutil.IndirectPriorityQueue
        public void changed() {
            throw new NoSuchElementException();
        }

        @Override // it.unimi.dsi.fastutil.IndirectPriorityQueue
        public void allChanged() {
        }

        @Override // it.unimi.dsi.fastutil.IndirectPriorityQueue
        public Comparator<?> comparator() {
            return null;
        }

        @Override // it.unimi.dsi.fastutil.IndirectPriorityQueue
        public void changed(int i) {
            throw new IllegalArgumentException("Index " + i + " is not in the queue");
        }

        @Override // it.unimi.dsi.fastutil.IndirectPriorityQueue
        public boolean remove(int i) {
            return false;
        }

        @Override // it.unimi.dsi.fastutil.IndirectPriorityQueue
        public int front(int[] a) {
            return 0;
        }
    }

    /* loaded from: classes4.dex */
    public static class SynchronizedIndirectPriorityQueue<K> implements IndirectPriorityQueue<K> {
        public static final long serialVersionUID = -7046029254386353129L;
        protected final IndirectPriorityQueue<K> q;
        protected final Object sync;

        protected SynchronizedIndirectPriorityQueue(IndirectPriorityQueue<K> q, Object sync) {
            this.q = q;
            this.sync = sync;
        }

        protected SynchronizedIndirectPriorityQueue(IndirectPriorityQueue<K> q) {
            this.q = q;
            this.sync = this;
        }

        @Override // it.unimi.dsi.fastutil.IndirectPriorityQueue
        public void enqueue(int x) {
            synchronized (this.sync) {
                this.q.enqueue(x);
            }
        }

        @Override // it.unimi.dsi.fastutil.IndirectPriorityQueue
        public int dequeue() {
            int dequeue;
            synchronized (this.sync) {
                dequeue = this.q.dequeue();
            }
            return dequeue;
        }

        @Override // it.unimi.dsi.fastutil.IndirectPriorityQueue
        public boolean contains(int index) {
            boolean contains;
            synchronized (this.sync) {
                contains = this.q.contains(index);
            }
            return contains;
        }

        @Override // it.unimi.dsi.fastutil.IndirectPriorityQueue
        public int first() {
            int first;
            synchronized (this.sync) {
                first = this.q.first();
            }
            return first;
        }

        @Override // it.unimi.dsi.fastutil.IndirectPriorityQueue
        public int last() {
            int last;
            synchronized (this.sync) {
                last = this.q.last();
            }
            return last;
        }

        @Override // it.unimi.dsi.fastutil.IndirectPriorityQueue
        public boolean isEmpty() {
            boolean isEmpty;
            synchronized (this.sync) {
                isEmpty = this.q.isEmpty();
            }
            return isEmpty;
        }

        @Override // it.unimi.dsi.fastutil.IndirectPriorityQueue
        public int size() {
            int size;
            synchronized (this.sync) {
                size = this.q.size();
            }
            return size;
        }

        @Override // it.unimi.dsi.fastutil.IndirectPriorityQueue
        public void clear() {
            synchronized (this.sync) {
                this.q.clear();
            }
        }

        @Override // it.unimi.dsi.fastutil.IndirectPriorityQueue
        public void changed() {
            synchronized (this.sync) {
                this.q.changed();
            }
        }

        @Override // it.unimi.dsi.fastutil.IndirectPriorityQueue
        public void allChanged() {
            synchronized (this.sync) {
                this.q.allChanged();
            }
        }

        @Override // it.unimi.dsi.fastutil.IndirectPriorityQueue
        public void changed(int i) {
            synchronized (this.sync) {
                this.q.changed(i);
            }
        }

        @Override // it.unimi.dsi.fastutil.IndirectPriorityQueue
        public boolean remove(int i) {
            boolean remove;
            synchronized (this.sync) {
                remove = this.q.remove(i);
            }
            return remove;
        }

        @Override // it.unimi.dsi.fastutil.IndirectPriorityQueue
        public Comparator<? super K> comparator() {
            Comparator<? super K> comparator;
            synchronized (this.sync) {
                comparator = this.q.comparator();
            }
            return comparator;
        }

        @Override // it.unimi.dsi.fastutil.IndirectPriorityQueue
        public int front(int[] a) {
            return this.q.front(a);
        }
    }

    public static <K> IndirectPriorityQueue<K> synchronize(IndirectPriorityQueue<K> q) {
        return new SynchronizedIndirectPriorityQueue(q);
    }

    public static <K> IndirectPriorityQueue<K> synchronize(IndirectPriorityQueue<K> q, Object sync) {
        return new SynchronizedIndirectPriorityQueue(q, sync);
    }
}
