package io.netty.util.internal.shaded.org.jctools.queues.atomic;

import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueueUtil;
import io.netty.util.internal.shaded.org.jctools.queues.QueueProgressIndicators;
import io.netty.util.internal.shaded.org.jctools.util.PortableJvmInfo;
import io.netty.util.internal.shaded.org.jctools.util.Pow2;
import io.netty.util.internal.shaded.org.jctools.util.RangeUtil;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReferenceArray;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public abstract class BaseMpscLinkedAtomicArrayQueue<E> extends BaseMpscLinkedAtomicArrayQueueColdProducerFields<E> implements MessagePassingQueue<E>, QueueProgressIndicators {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int CONTINUE_TO_P_INDEX_CAS = 0;
    private static final int QUEUE_FULL = 2;
    private static final int QUEUE_RESIZE = 3;
    private static final int RETRY = 1;
    private static final Object JUMP = new Object();
    private static final Object BUFFER_CONSUMED = new Object();

    protected abstract long availableInQueue(long j, long j2);

    @Override // io.netty.util.internal.shaded.org.jctools.queues.IndexedQueueSizeUtil.IndexedQueue, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public abstract int capacity();

    protected abstract long getCurrentBufferCapacity(long j);

    protected abstract int getNextBufferSize(AtomicReferenceArray<E> atomicReferenceArray);

    public BaseMpscLinkedAtomicArrayQueue(int initialCapacity) {
        RangeUtil.checkGreaterThanOrEqual(initialCapacity, 2, "initialCapacity");
        int p2capacity = Pow2.roundToPowerOfTwo(initialCapacity);
        long mask = (p2capacity - 1) << 1;
        AtomicReferenceArray<E> buffer = AtomicQueueUtil.allocateRefArray(p2capacity + 1);
        this.producerBuffer = buffer;
        this.producerMask = mask;
        this.consumerBuffer = buffer;
        this.consumerMask = mask;
        soProducerLimit(mask);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public int size() {
        long before;
        long currentProducerIndex;
        long after = lvConsumerIndex();
        do {
            before = after;
            currentProducerIndex = lvProducerIndex();
            after = lvConsumerIndex();
        } while (before != after);
        long size = (currentProducerIndex - after) >> 1;
        if (size > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int) size;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public boolean isEmpty() {
        return lvConsumerIndex() == lvProducerIndex();
    }

    @Override // java.util.AbstractCollection
    public String toString() {
        return getClass().getName();
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:9:0x002a. Please report as an issue. */
    @Override // java.util.Queue, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public boolean offer(E e) {
        AtomicReferenceArray<E> buffer;
        if (e == null) {
            throw new NullPointerException();
        }
        while (true) {
            long producerLimit = lvProducerLimit();
            long pIndex = lvProducerIndex();
            if ((pIndex & 1) != 1) {
                long mask = this.producerMask;
                AtomicReferenceArray<E> buffer2 = this.producerBuffer;
                if (producerLimit <= pIndex) {
                    int result = offerSlowPath(mask, pIndex, producerLimit);
                    switch (result) {
                        case 0:
                            buffer = buffer2;
                            break;
                        case 1:
                            break;
                        case 2:
                            return false;
                        case 3:
                            resize(mask, buffer2, pIndex, e, null);
                            return true;
                        default:
                            buffer = buffer2;
                            break;
                    }
                } else {
                    buffer = buffer2;
                }
                if (casProducerIndex(pIndex, 2 + pIndex)) {
                    int offset = AtomicQueueUtil.modifiedCalcCircularRefElementOffset(pIndex, mask);
                    AtomicQueueUtil.soRefElement(buffer, offset, e);
                    return true;
                }
            }
        }
    }

    @Override // java.util.Queue, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public E poll() {
        AtomicReferenceArray<E> atomicReferenceArray = this.consumerBuffer;
        long lpConsumerIndex = lpConsumerIndex();
        long j = this.consumerMask;
        int modifiedCalcCircularRefElementOffset = AtomicQueueUtil.modifiedCalcCircularRefElementOffset(lpConsumerIndex, j);
        E e = (E) AtomicQueueUtil.lvRefElement(atomicReferenceArray, modifiedCalcCircularRefElementOffset);
        if (e == null) {
            if (lpConsumerIndex == lvProducerIndex()) {
                return null;
            }
            do {
                e = (E) AtomicQueueUtil.lvRefElement(atomicReferenceArray, modifiedCalcCircularRefElementOffset);
            } while (e == null);
        }
        if (e == JUMP) {
            return newBufferPoll(nextBuffer(atomicReferenceArray, j), lpConsumerIndex);
        }
        AtomicQueueUtil.soRefElement(atomicReferenceArray, modifiedCalcCircularRefElementOffset, null);
        soConsumerIndex(2 + lpConsumerIndex);
        return e;
    }

    /* JADX WARN: Code restructure failed: missing block: B:4:0x0018, code lost:            if (r1 != lvProducerIndex()) goto L6;     */
    /* JADX WARN: Code restructure failed: missing block: B:5:0x001a, code lost:            r6 = (E) io.netty.util.internal.shaded.org.jctools.queues.atomic.AtomicQueueUtil.lvRefElement(r0, r5);     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x001e, code lost:            if (r6 == null) goto L14;     */
    @Override // java.util.Queue, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public E peek() {
        /*
            r9 = this;
            java.util.concurrent.atomic.AtomicReferenceArray<E> r0 = r9.consumerBuffer
            long r1 = r9.lpConsumerIndex()
            long r3 = r9.consumerMask
            int r5 = io.netty.util.internal.shaded.org.jctools.queues.atomic.AtomicQueueUtil.modifiedCalcCircularRefElementOffset(r1, r3)
            java.lang.Object r6 = io.netty.util.internal.shaded.org.jctools.queues.atomic.AtomicQueueUtil.lvRefElement(r0, r5)
            if (r6 != 0) goto L20
            long r7 = r9.lvProducerIndex()
            int r7 = (r1 > r7 ? 1 : (r1 == r7 ? 0 : -1))
            if (r7 == 0) goto L20
        L1a:
            java.lang.Object r6 = io.netty.util.internal.shaded.org.jctools.queues.atomic.AtomicQueueUtil.lvRefElement(r0, r5)
            if (r6 == 0) goto L1a
        L20:
            java.lang.Object r7 = io.netty.util.internal.shaded.org.jctools.queues.atomic.BaseMpscLinkedAtomicArrayQueue.JUMP
            if (r6 != r7) goto L2d
            java.util.concurrent.atomic.AtomicReferenceArray r7 = r9.nextBuffer(r0, r3)
            java.lang.Object r7 = r9.newBufferPeek(r7, r1)
            return r7
        L2d:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.util.internal.shaded.org.jctools.queues.atomic.BaseMpscLinkedAtomicArrayQueue.peek():java.lang.Object");
    }

    private int offerSlowPath(long mask, long pIndex, long producerLimit) {
        long cIndex = lvConsumerIndex();
        long bufferCapacity = getCurrentBufferCapacity(mask);
        if (cIndex + bufferCapacity > pIndex) {
            return !casProducerLimit(producerLimit, cIndex + bufferCapacity) ? 1 : 0;
        }
        if (availableInQueue(pIndex, cIndex) <= 0) {
            return 2;
        }
        return casProducerIndex(pIndex, 1 + pIndex) ? 3 : 1;
    }

    private AtomicReferenceArray<E> nextBuffer(AtomicReferenceArray<E> buffer, long mask) {
        int offset = nextArrayOffset(mask);
        AtomicReferenceArray<E> nextBuffer = (AtomicReferenceArray) AtomicQueueUtil.lvRefElement(buffer, offset);
        this.consumerBuffer = nextBuffer;
        this.consumerMask = (AtomicQueueUtil.length(nextBuffer) - 2) << 1;
        AtomicQueueUtil.soRefElement(buffer, offset, BUFFER_CONSUMED);
        return nextBuffer;
    }

    private static int nextArrayOffset(long mask) {
        return AtomicQueueUtil.modifiedCalcCircularRefElementOffset(2 + mask, Long.MAX_VALUE);
    }

    private E newBufferPoll(AtomicReferenceArray<E> atomicReferenceArray, long j) {
        int modifiedCalcCircularRefElementOffset = AtomicQueueUtil.modifiedCalcCircularRefElementOffset(j, this.consumerMask);
        E e = (E) AtomicQueueUtil.lvRefElement(atomicReferenceArray, modifiedCalcCircularRefElementOffset);
        if (e == null) {
            throw new IllegalStateException("new buffer must have at least one element");
        }
        AtomicQueueUtil.soRefElement(atomicReferenceArray, modifiedCalcCircularRefElementOffset, null);
        soConsumerIndex(2 + j);
        return e;
    }

    private E newBufferPeek(AtomicReferenceArray<E> atomicReferenceArray, long j) {
        E e = (E) AtomicQueueUtil.lvRefElement(atomicReferenceArray, AtomicQueueUtil.modifiedCalcCircularRefElementOffset(j, this.consumerMask));
        if (e == null) {
            throw new IllegalStateException("new buffer must have at least one element");
        }
        return e;
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.QueueProgressIndicators
    public long currentProducerIndex() {
        return lvProducerIndex() / 2;
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.QueueProgressIndicators
    public long currentConsumerIndex() {
        return lvConsumerIndex() / 2;
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public boolean relaxedOffer(E e) {
        return offer(e);
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public E relaxedPoll() {
        AtomicReferenceArray<E> atomicReferenceArray = this.consumerBuffer;
        long lpConsumerIndex = lpConsumerIndex();
        long j = this.consumerMask;
        int modifiedCalcCircularRefElementOffset = AtomicQueueUtil.modifiedCalcCircularRefElementOffset(lpConsumerIndex, j);
        E e = (E) AtomicQueueUtil.lvRefElement(atomicReferenceArray, modifiedCalcCircularRefElementOffset);
        if (e == null) {
            return null;
        }
        if (e == JUMP) {
            return newBufferPoll(nextBuffer(atomicReferenceArray, j), lpConsumerIndex);
        }
        AtomicQueueUtil.soRefElement(atomicReferenceArray, modifiedCalcCircularRefElementOffset, null);
        soConsumerIndex(2 + lpConsumerIndex);
        return e;
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public E relaxedPeek() {
        AtomicReferenceArray<E> atomicReferenceArray = this.consumerBuffer;
        long lpConsumerIndex = lpConsumerIndex();
        long j = this.consumerMask;
        E e = (E) AtomicQueueUtil.lvRefElement(atomicReferenceArray, AtomicQueueUtil.modifiedCalcCircularRefElementOffset(lpConsumerIndex, j));
        if (e == JUMP) {
            return newBufferPeek(nextBuffer(atomicReferenceArray, j), lpConsumerIndex);
        }
        return e;
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public int fill(MessagePassingQueue.Supplier<E> s) {
        long result = 0;
        int capacity = capacity();
        do {
            int filled = fill(s, PortableJvmInfo.RECOMENDED_OFFER_BATCH);
            if (filled == 0) {
                return (int) result;
            }
            result += filled;
        } while (result <= capacity);
        return (int) result;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:14:0x003c. Please report as an issue. */
    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public int fill(MessagePassingQueue.Supplier<E> s, int limit) {
        long batchIndex;
        AtomicReferenceArray<E> buffer;
        if (s == null) {
            throw new IllegalArgumentException("supplier is null");
        }
        if (limit < 0) {
            throw new IllegalArgumentException("limit is negative:" + limit);
        }
        if (limit == 0) {
            return 0;
        }
        while (true) {
            long producerLimit = lvProducerLimit();
            long pIndex = lvProducerIndex();
            if ((pIndex & 1) != 1) {
                long mask = this.producerMask;
                AtomicReferenceArray<E> buffer2 = this.producerBuffer;
                long batchIndex2 = Math.min(producerLimit, (limit * 2) + pIndex);
                if (pIndex < producerLimit) {
                    batchIndex = batchIndex2;
                    buffer = buffer2;
                } else {
                    batchIndex = batchIndex2;
                    buffer = buffer2;
                    int result = offerSlowPath(mask, pIndex, producerLimit);
                    switch (result) {
                        case 2:
                            return 0;
                        case 3:
                            resize(mask, buffer, pIndex, null, s);
                            return 1;
                    }
                }
                long batchIndex3 = batchIndex;
                if (casProducerIndex(pIndex, batchIndex3)) {
                    int claimedSlots = (int) ((batchIndex3 - pIndex) / 2);
                    for (int i = 0; i < claimedSlots; i++) {
                        int offset = AtomicQueueUtil.modifiedCalcCircularRefElementOffset((i * 2) + pIndex, mask);
                        AtomicQueueUtil.soRefElement(buffer, offset, s.get());
                    }
                    return claimedSlots;
                }
            }
        }
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public void fill(MessagePassingQueue.Supplier<E> s, MessagePassingQueue.WaitStrategy wait, MessagePassingQueue.ExitCondition exit) {
        MessagePassingQueueUtil.fill(this, s, wait, exit);
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public int drain(MessagePassingQueue.Consumer<E> c) {
        return drain(c, capacity());
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public int drain(MessagePassingQueue.Consumer<E> c, int limit) {
        return MessagePassingQueueUtil.drain(this, c, limit);
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public void drain(MessagePassingQueue.Consumer<E> c, MessagePassingQueue.WaitStrategy wait, MessagePassingQueue.ExitCondition exit) {
        MessagePassingQueueUtil.drain(this, c, wait, exit);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    public Iterator<E> iterator() {
        return new WeakIterator(this.consumerBuffer, lvConsumerIndex(), lvProducerIndex());
    }

    /* loaded from: classes4.dex */
    private static class WeakIterator<E> implements Iterator<E> {
        private AtomicReferenceArray<E> currentBuffer;
        private int mask;
        private E nextElement;
        private long nextIndex;
        private final long pIndex;

        WeakIterator(AtomicReferenceArray<E> currentBuffer, long cIndex, long pIndex) {
            this.pIndex = pIndex >> 1;
            this.nextIndex = cIndex >> 1;
            setBuffer(currentBuffer);
            this.nextElement = getNext();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("remove");
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.nextElement != null;
        }

        @Override // java.util.Iterator
        public E next() {
            E e = this.nextElement;
            if (e == null) {
                throw new NoSuchElementException();
            }
            this.nextElement = getNext();
            return e;
        }

        private void setBuffer(AtomicReferenceArray<E> buffer) {
            this.currentBuffer = buffer;
            this.mask = AtomicQueueUtil.length(buffer) - 2;
        }

        private E getNext() {
            while (this.nextIndex < this.pIndex) {
                long j = this.nextIndex;
                this.nextIndex = 1 + j;
                E e = (E) AtomicQueueUtil.lvRefElement(this.currentBuffer, AtomicQueueUtil.calcCircularRefElementOffset(j, this.mask));
                if (e != null) {
                    if (e != BaseMpscLinkedAtomicArrayQueue.JUMP) {
                        return e;
                    }
                    Object lvRefElement = AtomicQueueUtil.lvRefElement(this.currentBuffer, AtomicQueueUtil.calcRefElementOffset(this.mask + 1));
                    if (lvRefElement == BaseMpscLinkedAtomicArrayQueue.BUFFER_CONSUMED || lvRefElement == null) {
                        return null;
                    }
                    setBuffer((AtomicReferenceArray) lvRefElement);
                    E e2 = (E) AtomicQueueUtil.lvRefElement(this.currentBuffer, AtomicQueueUtil.calcCircularRefElementOffset(j, this.mask));
                    if (e2 != null) {
                        return e2;
                    }
                }
            }
            return null;
        }
    }

    private void resize(long oldMask, AtomicReferenceArray<E> oldBuffer, long pIndex, E e, MessagePassingQueue.Supplier<E> s) {
        if ((e == null || s != null) && e != null && s == null) {
            throw new AssertionError();
        }
        int newBufferLength = getNextBufferSize(oldBuffer);
        try {
            AtomicReferenceArray<E> newBuffer = AtomicQueueUtil.allocateRefArray(newBufferLength);
            this.producerBuffer = newBuffer;
            int newMask = (newBufferLength - 2) << 1;
            this.producerMask = newMask;
            int offsetInOld = AtomicQueueUtil.modifiedCalcCircularRefElementOffset(pIndex, oldMask);
            int offsetInNew = AtomicQueueUtil.modifiedCalcCircularRefElementOffset(pIndex, newMask);
            AtomicQueueUtil.soRefElement(newBuffer, offsetInNew, e == null ? s.get() : e);
            AtomicQueueUtil.soRefElement(oldBuffer, nextArrayOffset(oldMask), newBuffer);
            long cIndex = lvConsumerIndex();
            long availableInQueue = availableInQueue(pIndex, cIndex);
            RangeUtil.checkPositive(availableInQueue, "availableInQueue");
            soProducerLimit(Math.min(newMask, availableInQueue) + pIndex);
            soProducerIndex(2 + pIndex);
            AtomicQueueUtil.soRefElement(oldBuffer, offsetInOld, JUMP);
        } catch (OutOfMemoryError oom) {
            if (lvProducerIndex() != 1 + pIndex) {
                throw new AssertionError();
            }
            soProducerIndex(pIndex);
            throw oom;
        }
    }
}
