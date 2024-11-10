package io.netty.util.internal.shaded.org.jctools.queues.atomic;

import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueueUtil;
import java.util.concurrent.atomic.AtomicReferenceArray;

/* loaded from: classes4.dex */
public class MpscAtomicArrayQueue<E> extends MpscAtomicArrayQueueL3Pad<E> {
    @Override // io.netty.util.internal.shaded.org.jctools.queues.atomic.AtomicReferenceArrayQueue, java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public /* bridge */ /* synthetic */ void clear() {
        super.clear();
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.atomic.AtomicReferenceArrayQueue, java.util.AbstractCollection
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    public MpscAtomicArrayQueue(int capacity) {
        super(capacity);
    }

    public boolean offerIfBelowThreshold(E e, int threshold) {
        long pIndex;
        if (e == null) {
            throw new NullPointerException();
        }
        int mask = this.mask;
        long capacity = mask + 1;
        long producerLimit = lvProducerLimit();
        do {
            pIndex = lvProducerIndex();
            long available = producerLimit - pIndex;
            long size = capacity - available;
            if (size >= threshold) {
                long cIndex = lvConsumerIndex();
                long size2 = pIndex - cIndex;
                long producerLimit2 = threshold;
                if (size2 >= producerLimit2) {
                    return false;
                }
                producerLimit = cIndex + capacity;
                soProducerLimit(producerLimit);
            }
        } while (!casProducerIndex(pIndex, 1 + pIndex));
        int offset = AtomicQueueUtil.calcCircularRefElementOffset(pIndex, mask);
        AtomicQueueUtil.soRefElement(this.buffer, offset, e);
        return true;
    }

    @Override // java.util.Queue, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public boolean offer(E e) {
        long pIndex;
        if (e == null) {
            throw new NullPointerException();
        }
        int mask = this.mask;
        long producerLimit = lvProducerLimit();
        do {
            pIndex = lvProducerIndex();
            if (pIndex >= producerLimit) {
                long cIndex = lvConsumerIndex();
                long producerLimit2 = mask + cIndex + 1;
                if (pIndex >= producerLimit2) {
                    return false;
                }
                soProducerLimit(producerLimit2);
                producerLimit = producerLimit2;
            }
        } while (!casProducerIndex(pIndex, 1 + pIndex));
        int offset = AtomicQueueUtil.calcCircularRefElementOffset(pIndex, mask);
        AtomicQueueUtil.soRefElement(this.buffer, offset, e);
        return true;
    }

    public final int failFastOffer(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        int mask = this.mask;
        long capacity = mask + 1;
        long pIndex = lvProducerIndex();
        if (pIndex >= lvProducerLimit()) {
            long cIndex = lvConsumerIndex();
            long producerLimit = cIndex + capacity;
            if (pIndex >= producerLimit) {
                return 1;
            }
            soProducerLimit(producerLimit);
        }
        if (!casProducerIndex(pIndex, 1 + pIndex)) {
            return -1;
        }
        int offset = AtomicQueueUtil.calcCircularRefElementOffset(pIndex, mask);
        AtomicQueueUtil.soRefElement(this.buffer, offset, e);
        return 0;
    }

    @Override // java.util.Queue, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public E poll() {
        long lpConsumerIndex = lpConsumerIndex();
        int calcCircularRefElementOffset = AtomicQueueUtil.calcCircularRefElementOffset(lpConsumerIndex, this.mask);
        AtomicReferenceArray<E> atomicReferenceArray = this.buffer;
        E e = (E) AtomicQueueUtil.lvRefElement(atomicReferenceArray, calcCircularRefElementOffset);
        if (e == null) {
            if (lpConsumerIndex == lvProducerIndex()) {
                return null;
            }
            do {
                e = (E) AtomicQueueUtil.lvRefElement(atomicReferenceArray, calcCircularRefElementOffset);
            } while (e == null);
        }
        AtomicQueueUtil.spRefElement(atomicReferenceArray, calcCircularRefElementOffset, null);
        soConsumerIndex(1 + lpConsumerIndex);
        return e;
    }

    @Override // java.util.Queue, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public E peek() {
        AtomicReferenceArray<E> atomicReferenceArray = this.buffer;
        long lpConsumerIndex = lpConsumerIndex();
        int calcCircularRefElementOffset = AtomicQueueUtil.calcCircularRefElementOffset(lpConsumerIndex, this.mask);
        E e = (E) AtomicQueueUtil.lvRefElement(atomicReferenceArray, calcCircularRefElementOffset);
        if (e == null) {
            if (lpConsumerIndex == lvProducerIndex()) {
                return null;
            }
            do {
                e = (E) AtomicQueueUtil.lvRefElement(atomicReferenceArray, calcCircularRefElementOffset);
            } while (e == null);
        }
        return e;
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public boolean relaxedOffer(E e) {
        return offer(e);
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public E relaxedPoll() {
        AtomicReferenceArray<E> atomicReferenceArray = this.buffer;
        long lpConsumerIndex = lpConsumerIndex();
        int calcCircularRefElementOffset = AtomicQueueUtil.calcCircularRefElementOffset(lpConsumerIndex, this.mask);
        E e = (E) AtomicQueueUtil.lvRefElement(atomicReferenceArray, calcCircularRefElementOffset);
        if (e == null) {
            return null;
        }
        AtomicQueueUtil.spRefElement(atomicReferenceArray, calcCircularRefElementOffset, null);
        soConsumerIndex(1 + lpConsumerIndex);
        return e;
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public E relaxedPeek() {
        return (E) AtomicQueueUtil.lvRefElement(this.buffer, AtomicQueueUtil.calcCircularRefElementOffset(lpConsumerIndex(), this.mask));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public int drain(MessagePassingQueue.Consumer<E> consumer, int limit) {
        if (consumer == 0) {
            throw new IllegalArgumentException("c is null");
        }
        if (limit < 0) {
            throw new IllegalArgumentException("limit is negative: " + limit);
        }
        if (limit == 0) {
            return 0;
        }
        AtomicReferenceArray<E> buffer = this.buffer;
        int mask = this.mask;
        long cIndex = lpConsumerIndex();
        for (int i = 0; i < limit; i++) {
            long index = i + cIndex;
            int offset = AtomicQueueUtil.calcCircularRefElementOffset(index, mask);
            Object lvRefElement = AtomicQueueUtil.lvRefElement(buffer, offset);
            if (lvRefElement == null) {
                return i;
            }
            AtomicQueueUtil.spRefElement(buffer, offset, null);
            soConsumerIndex(1 + index);
            consumer.accept(lvRefElement);
        }
        return limit;
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public int fill(MessagePassingQueue.Supplier<E> s, int limit) {
        long pIndex;
        int actualLimit;
        if (s == null) {
            throw new IllegalArgumentException("supplier is null");
        }
        if (limit < 0) {
            throw new IllegalArgumentException("limit is negative:" + limit);
        }
        if (limit == 0) {
            return 0;
        }
        int mask = this.mask;
        long capacity = mask + 1;
        long producerLimit = lvProducerLimit();
        do {
            pIndex = lvProducerIndex();
            long available = producerLimit - pIndex;
            if (available <= 0) {
                long cIndex = lvConsumerIndex();
                producerLimit = cIndex + capacity;
                available = producerLimit - pIndex;
                if (available <= 0) {
                    return 0;
                }
                soProducerLimit(producerLimit);
            }
            actualLimit = Math.min((int) available, limit);
        } while (!casProducerIndex(pIndex, actualLimit + pIndex));
        AtomicReferenceArray<E> buffer = this.buffer;
        for (int i = 0; i < actualLimit; i++) {
            int offset = AtomicQueueUtil.calcCircularRefElementOffset(i + pIndex, mask);
            AtomicQueueUtil.soRefElement(buffer, offset, s.get());
        }
        return actualLimit;
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public int drain(MessagePassingQueue.Consumer<E> c) {
        return drain(c, capacity());
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public int fill(MessagePassingQueue.Supplier<E> s) {
        return MessagePassingQueueUtil.fillBounded(this, s);
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public void drain(MessagePassingQueue.Consumer<E> c, MessagePassingQueue.WaitStrategy w, MessagePassingQueue.ExitCondition exit) {
        MessagePassingQueueUtil.drain(this, c, w, exit);
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public void fill(MessagePassingQueue.Supplier<E> s, MessagePassingQueue.WaitStrategy wait, MessagePassingQueue.ExitCondition exit) {
        MessagePassingQueueUtil.fill(this, s, wait, exit);
    }

    @Deprecated
    public int weakOffer(E e) {
        return failFastOffer(e);
    }
}
