package io.netty.util.internal.shaded.org.jctools.queues.atomic;

import java.util.concurrent.atomic.AtomicLongFieldUpdater;

/* compiled from: MpscAtomicArrayQueue.java */
/* loaded from: classes4.dex */
abstract class MpscAtomicArrayQueueConsumerIndexField<E> extends MpscAtomicArrayQueueL2Pad<E> {
    private static final AtomicLongFieldUpdater<MpscAtomicArrayQueueConsumerIndexField> C_INDEX_UPDATER = AtomicLongFieldUpdater.newUpdater(MpscAtomicArrayQueueConsumerIndexField.class, "consumerIndex");
    private volatile long consumerIndex;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MpscAtomicArrayQueueConsumerIndexField(int capacity) {
        super(capacity);
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.IndexedQueueSizeUtil.IndexedQueue
    public final long lvConsumerIndex() {
        return this.consumerIndex;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final long lpConsumerIndex() {
        return this.consumerIndex;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void soConsumerIndex(long newValue) {
        C_INDEX_UPDATER.lazySet(this, newValue);
    }
}
