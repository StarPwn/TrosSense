package it.unimi.dsi.fastutil;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Comparator;
import java.util.NoSuchElementException;

/* loaded from: classes4.dex */
public class PriorityQueues {
    public static final EmptyPriorityQueue EMPTY_QUEUE = new EmptyPriorityQueue();

    private PriorityQueues() {
    }

    /* loaded from: classes4.dex */
    public static class EmptyPriorityQueue implements PriorityQueue, Serializable {
        private static final long serialVersionUID = 0;

        protected EmptyPriorityQueue() {
        }

        @Override // it.unimi.dsi.fastutil.PriorityQueue
        public void enqueue(Object o) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.PriorityQueue
        public Object dequeue() {
            throw new NoSuchElementException();
        }

        @Override // it.unimi.dsi.fastutil.PriorityQueue
        public boolean isEmpty() {
            return true;
        }

        @Override // it.unimi.dsi.fastutil.PriorityQueue
        public int size() {
            return 0;
        }

        @Override // it.unimi.dsi.fastutil.PriorityQueue
        public void clear() {
        }

        @Override // it.unimi.dsi.fastutil.PriorityQueue
        public Object first() {
            throw new NoSuchElementException();
        }

        @Override // it.unimi.dsi.fastutil.PriorityQueue
        public Object last() {
            throw new NoSuchElementException();
        }

        @Override // it.unimi.dsi.fastutil.PriorityQueue
        public void changed() {
            throw new NoSuchElementException();
        }

        @Override // it.unimi.dsi.fastutil.PriorityQueue
        public Comparator<?> comparator() {
            return null;
        }

        public Object clone() {
            return PriorityQueues.EMPTY_QUEUE;
        }

        public int hashCode() {
            return 0;
        }

        public boolean equals(Object o) {
            return (o instanceof PriorityQueue) && ((PriorityQueue) o).isEmpty();
        }

        private Object readResolve() {
            return PriorityQueues.EMPTY_QUEUE;
        }
    }

    public static <K> PriorityQueue<K> emptyQueue() {
        return EMPTY_QUEUE;
    }

    /* loaded from: classes4.dex */
    public static class SynchronizedPriorityQueue<K> implements PriorityQueue<K>, Serializable {
        public static final long serialVersionUID = -7046029254386353129L;
        protected final PriorityQueue<K> q;
        protected final Object sync;

        protected SynchronizedPriorityQueue(PriorityQueue<K> q, Object sync) {
            this.q = q;
            this.sync = sync;
        }

        protected SynchronizedPriorityQueue(PriorityQueue<K> q) {
            this.q = q;
            this.sync = this;
        }

        @Override // it.unimi.dsi.fastutil.PriorityQueue
        public void enqueue(K x) {
            synchronized (this.sync) {
                this.q.enqueue(x);
            }
        }

        @Override // it.unimi.dsi.fastutil.PriorityQueue
        public K dequeue() {
            K dequeue;
            synchronized (this.sync) {
                dequeue = this.q.dequeue();
            }
            return dequeue;
        }

        @Override // it.unimi.dsi.fastutil.PriorityQueue
        public K first() {
            K first;
            synchronized (this.sync) {
                first = this.q.first();
            }
            return first;
        }

        @Override // it.unimi.dsi.fastutil.PriorityQueue
        public K last() {
            K last;
            synchronized (this.sync) {
                last = this.q.last();
            }
            return last;
        }

        @Override // it.unimi.dsi.fastutil.PriorityQueue
        public boolean isEmpty() {
            boolean isEmpty;
            synchronized (this.sync) {
                isEmpty = this.q.isEmpty();
            }
            return isEmpty;
        }

        @Override // it.unimi.dsi.fastutil.PriorityQueue
        public int size() {
            int size;
            synchronized (this.sync) {
                size = this.q.size();
            }
            return size;
        }

        @Override // it.unimi.dsi.fastutil.PriorityQueue
        public void clear() {
            synchronized (this.sync) {
                this.q.clear();
            }
        }

        @Override // it.unimi.dsi.fastutil.PriorityQueue
        public void changed() {
            synchronized (this.sync) {
                this.q.changed();
            }
        }

        @Override // it.unimi.dsi.fastutil.PriorityQueue
        public Comparator<? super K> comparator() {
            Comparator<? super K> comparator;
            synchronized (this.sync) {
                comparator = this.q.comparator();
            }
            return comparator;
        }

        public String toString() {
            String obj;
            synchronized (this.sync) {
                obj = this.q.toString();
            }
            return obj;
        }

        public int hashCode() {
            int hashCode;
            synchronized (this.sync) {
                hashCode = this.q.hashCode();
            }
            return hashCode;
        }

        public boolean equals(Object o) {
            boolean equals;
            if (o == this) {
                return true;
            }
            synchronized (this.sync) {
                equals = this.q.equals(o);
            }
            return equals;
        }

        private void writeObject(ObjectOutputStream s) throws IOException {
            synchronized (this.sync) {
                s.defaultWriteObject();
            }
        }
    }

    public static <K> PriorityQueue<K> synchronize(PriorityQueue<K> q) {
        return new SynchronizedPriorityQueue(q);
    }

    public static <K> PriorityQueue<K> synchronize(PriorityQueue<K> q, Object sync) {
        return new SynchronizedPriorityQueue(q, sync);
    }
}
