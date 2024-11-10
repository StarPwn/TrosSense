package io.netty.util.internal.shaded.org.jctools.queues;

import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue;
import io.netty.util.internal.shaded.org.jctools.util.PortableJvmInfo;
import io.netty.util.internal.shaded.org.jctools.util.Pow2;
import io.netty.util.internal.shaded.org.jctools.util.RangeUtil;
import io.netty.util.internal.shaded.org.jctools.util.UnsafeRefArrayAccess;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public abstract class BaseMpscLinkedArrayQueue<E> extends BaseMpscLinkedArrayQueueColdProducerFields<E> implements MessagePassingQueue<E>, QueueProgressIndicators {
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

    protected abstract int getNextBufferSize(E[] eArr);

    public BaseMpscLinkedArrayQueue(int i) {
        RangeUtil.checkGreaterThanOrEqual(i, 2, "initialCapacity");
        int roundToPowerOfTwo = Pow2.roundToPowerOfTwo(i);
        long j = (roundToPowerOfTwo - 1) << 1;
        E[] eArr = (E[]) UnsafeRefArrayAccess.allocateRefArray(roundToPowerOfTwo + 1);
        this.producerBuffer = eArr;
        this.producerMask = j;
        this.consumerBuffer = eArr;
        this.consumerMask = j;
        soProducerLimit(j);
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
        E[] buffer;
        if (e == null) {
            throw new NullPointerException();
        }
        while (true) {
            long producerLimit = lvProducerLimit();
            long pIndex = lvProducerIndex();
            if ((pIndex & 1) != 1) {
                long mask = this.producerMask;
                E[] buffer2 = this.producerBuffer;
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
                    long offset = LinkedArrayQueueUtil.modifiedCalcCircularRefElementOffset(pIndex, mask);
                    UnsafeRefArrayAccess.soRefElement(buffer, offset, e);
                    return true;
                }
            }
        }
    }

    @Override // java.util.Queue, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public E poll() {
        E[] eArr = this.consumerBuffer;
        long lpConsumerIndex = lpConsumerIndex();
        long j = this.consumerMask;
        long modifiedCalcCircularRefElementOffset = LinkedArrayQueueUtil.modifiedCalcCircularRefElementOffset(lpConsumerIndex, j);
        E e = (E) UnsafeRefArrayAccess.lvRefElement(eArr, modifiedCalcCircularRefElementOffset);
        if (e == null) {
            if (lpConsumerIndex == lvProducerIndex()) {
                return null;
            }
            do {
                e = (E) UnsafeRefArrayAccess.lvRefElement(eArr, modifiedCalcCircularRefElementOffset);
            } while (e == null);
        }
        if (e == JUMP) {
            return newBufferPoll(nextBuffer(eArr, j), lpConsumerIndex);
        }
        UnsafeRefArrayAccess.soRefElement(eArr, modifiedCalcCircularRefElementOffset, null);
        soConsumerIndex(2 + lpConsumerIndex);
        return e;
    }

    /* JADX WARN: Code restructure failed: missing block: B:4:0x0018, code lost:            if (r1 != lvProducerIndex()) goto L6;     */
    /* JADX WARN: Code restructure failed: missing block: B:5:0x001a, code lost:            r7 = (E) io.netty.util.internal.shaded.org.jctools.util.UnsafeRefArrayAccess.lvRefElement(r0, r5);     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x001e, code lost:            if (r7 == null) goto L14;     */
    @Override // java.util.Queue, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public E peek() {
        /*
            r10 = this;
            E[] r0 = r10.consumerBuffer
            long r1 = r10.lpConsumerIndex()
            long r3 = r10.consumerMask
            long r5 = io.netty.util.internal.shaded.org.jctools.queues.LinkedArrayQueueUtil.modifiedCalcCircularRefElementOffset(r1, r3)
            java.lang.Object r7 = io.netty.util.internal.shaded.org.jctools.util.UnsafeRefArrayAccess.lvRefElement(r0, r5)
            if (r7 != 0) goto L20
            long r8 = r10.lvProducerIndex()
            int r8 = (r1 > r8 ? 1 : (r1 == r8 ? 0 : -1))
            if (r8 == 0) goto L20
        L1a:
            java.lang.Object r7 = io.netty.util.internal.shaded.org.jctools.util.UnsafeRefArrayAccess.lvRefElement(r0, r5)
            if (r7 == 0) goto L1a
        L20:
            java.lang.Object r8 = io.netty.util.internal.shaded.org.jctools.queues.BaseMpscLinkedArrayQueue.JUMP
            if (r7 != r8) goto L2d
            java.lang.Object[] r8 = r10.nextBuffer(r0, r3)
            java.lang.Object r8 = r10.newBufferPeek(r8, r1)
            return r8
        L2d:
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.util.internal.shaded.org.jctools.queues.BaseMpscLinkedArrayQueue.peek():java.lang.Object");
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

    private E[] nextBuffer(E[] eArr, long j) {
        long nextArrayOffset = nextArrayOffset(j);
        E[] eArr2 = (E[]) ((Object[]) UnsafeRefArrayAccess.lvRefElement(eArr, nextArrayOffset));
        this.consumerBuffer = eArr2;
        this.consumerMask = (LinkedArrayQueueUtil.length(eArr2) - 2) << 1;
        UnsafeRefArrayAccess.soRefElement(eArr, nextArrayOffset, BUFFER_CONSUMED);
        return eArr2;
    }

    private static long nextArrayOffset(long mask) {
        return LinkedArrayQueueUtil.modifiedCalcCircularRefElementOffset(2 + mask, Long.MAX_VALUE);
    }

    private E newBufferPoll(E[] eArr, long j) {
        long modifiedCalcCircularRefElementOffset = LinkedArrayQueueUtil.modifiedCalcCircularRefElementOffset(j, this.consumerMask);
        E e = (E) UnsafeRefArrayAccess.lvRefElement(eArr, modifiedCalcCircularRefElementOffset);
        if (e == null) {
            throw new IllegalStateException("new buffer must have at least one element");
        }
        UnsafeRefArrayAccess.soRefElement(eArr, modifiedCalcCircularRefElementOffset, null);
        soConsumerIndex(2 + j);
        return e;
    }

    private E newBufferPeek(E[] eArr, long j) {
        E e = (E) UnsafeRefArrayAccess.lvRefElement(eArr, LinkedArrayQueueUtil.modifiedCalcCircularRefElementOffset(j, this.consumerMask));
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
        E[] eArr = this.consumerBuffer;
        long lpConsumerIndex = lpConsumerIndex();
        long j = this.consumerMask;
        long modifiedCalcCircularRefElementOffset = LinkedArrayQueueUtil.modifiedCalcCircularRefElementOffset(lpConsumerIndex, j);
        E e = (E) UnsafeRefArrayAccess.lvRefElement(eArr, modifiedCalcCircularRefElementOffset);
        if (e == null) {
            return null;
        }
        if (e == JUMP) {
            return newBufferPoll(nextBuffer(eArr, j), lpConsumerIndex);
        }
        UnsafeRefArrayAccess.soRefElement(eArr, modifiedCalcCircularRefElementOffset, null);
        soConsumerIndex(2 + lpConsumerIndex);
        return e;
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public E relaxedPeek() {
        E[] eArr = this.consumerBuffer;
        long lpConsumerIndex = lpConsumerIndex();
        long j = this.consumerMask;
        E e = (E) UnsafeRefArrayAccess.lvRefElement(eArr, LinkedArrayQueueUtil.modifiedCalcCircularRefElementOffset(lpConsumerIndex, j));
        if (e == JUMP) {
            return newBufferPeek(nextBuffer(eArr, j), lpConsumerIndex);
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
        E[] buffer;
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
                E[] buffer2 = this.producerBuffer;
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
                        long offset = LinkedArrayQueueUtil.modifiedCalcCircularRefElementOffset((i * 2) + pIndex, mask);
                        UnsafeRefArrayAccess.soRefElement(buffer, offset, s.get());
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
        private E[] currentBuffer;
        private int mask;
        private E nextElement;
        private long nextIndex;
        private final long pIndex;

        WeakIterator(E[] currentBuffer, long cIndex, long pIndex) {
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

        private void setBuffer(E[] buffer) {
            this.currentBuffer = buffer;
            this.mask = LinkedArrayQueueUtil.length(buffer) - 2;
        }

        /* JADX WARN: Multi-variable type inference failed */
        private E getNext() {
            while (this.nextIndex < this.pIndex) {
                long j = this.nextIndex;
                this.nextIndex = 1 + j;
                E e = (E) UnsafeRefArrayAccess.lvRefElement(this.currentBuffer, UnsafeRefArrayAccess.calcCircularRefElementOffset(j, this.mask));
                if (e != null) {
                    if (e != BaseMpscLinkedArrayQueue.JUMP) {
                        return e;
                    }
                    Object lvRefElement = UnsafeRefArrayAccess.lvRefElement(this.currentBuffer, UnsafeRefArrayAccess.calcRefElementOffset(this.mask + 1));
                    if (lvRefElement == BaseMpscLinkedArrayQueue.BUFFER_CONSUMED || lvRefElement == null) {
                        return null;
                    }
                    setBuffer((Object[]) lvRefElement);
                    E e2 = (E) UnsafeRefArrayAccess.lvRefElement(this.currentBuffer, UnsafeRefArrayAccess.calcCircularRefElementOffset(j, this.mask));
                    if (e2 != null) {
                        return e2;
                    }
                }
            }
            return null;
        }
    }

    private void resize(long j, E[] eArr, long j2, E e, MessagePassingQueue.Supplier<E> supplier) {
        if ((e == null || supplier != null) && e != null && supplier == null) {
            throw new AssertionError();
        }
        int nextBufferSize = getNextBufferSize(eArr);
        try {
            E[] eArr2 = (E[]) UnsafeRefArrayAccess.allocateRefArray(nextBufferSize);
            this.producerBuffer = eArr2;
            int i = (nextBufferSize - 2) << 1;
            this.producerMask = i;
            long modifiedCalcCircularRefElementOffset = LinkedArrayQueueUtil.modifiedCalcCircularRefElementOffset(j2, j);
            UnsafeRefArrayAccess.soRefElement(eArr2, LinkedArrayQueueUtil.modifiedCalcCircularRefElementOffset(j2, i), e == null ? supplier.get() : e);
            UnsafeRefArrayAccess.soRefElement(eArr, nextArrayOffset(j), eArr2);
            long availableInQueue = availableInQueue(j2, lvConsumerIndex());
            RangeUtil.checkPositive(availableInQueue, "availableInQueue");
            soProducerLimit(Math.min(i, availableInQueue) + j2);
            soProducerIndex(2 + j2);
            UnsafeRefArrayAccess.soRefElement(eArr, modifiedCalcCircularRefElementOffset, JUMP);
        } catch (OutOfMemoryError e2) {
            if (lvProducerIndex() != 1 + j2) {
                throw new AssertionError();
            }
            soProducerIndex(j2);
            throw e2;
        }
    }
}
