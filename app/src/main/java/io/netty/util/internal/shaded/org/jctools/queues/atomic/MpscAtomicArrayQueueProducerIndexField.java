package io.netty.util.internal.shaded.org.jctools.queues.atomic;

import java.util.concurrent.atomic.AtomicLongFieldUpdater;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: MpscAtomicArrayQueue.java */
/* loaded from: classes4.dex */
public abstract class MpscAtomicArrayQueueProducerIndexField<E> extends MpscAtomicArrayQueueL1Pad<E> {
    private static final AtomicLongFieldUpdater<MpscAtomicArrayQueueProducerIndexField> P_INDEX_UPDATER = AtomicLongFieldUpdater.newUpdater(MpscAtomicArrayQueueProducerIndexField.class, "producerIndex");
    private volatile long producerIndex;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MpscAtomicArrayQueueProducerIndexField(int capacity) {
        super(capacity);
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.IndexedQueueSizeUtil.IndexedQueue
    public final long lvProducerIndex() {
        return this.producerIndex;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean casProducerIndex(long expect, long newValue) {
        return P_INDEX_UPDATER.compareAndSet(this, expect, newValue);
    }
}
