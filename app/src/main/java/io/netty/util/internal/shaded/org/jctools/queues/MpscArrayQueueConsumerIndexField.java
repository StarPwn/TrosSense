package io.netty.util.internal.shaded.org.jctools.queues;

import io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess;

/* compiled from: MpscArrayQueue.java */
/* loaded from: classes4.dex */
abstract class MpscArrayQueueConsumerIndexField<E> extends MpscArrayQueueL2Pad<E> {
    private static final long C_INDEX_OFFSET = UnsafeAccess.fieldOffset(MpscArrayQueueConsumerIndexField.class, "consumerIndex");
    private volatile long consumerIndex;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MpscArrayQueueConsumerIndexField(int capacity) {
        super(capacity);
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.IndexedQueueSizeUtil.IndexedQueue
    public final long lvConsumerIndex() {
        return this.consumerIndex;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final long lpConsumerIndex() {
        return UnsafeAccess.UNSAFE.getLong(this, C_INDEX_OFFSET);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void soConsumerIndex(long newValue) {
        UnsafeAccess.UNSAFE.putOrderedLong(this, C_INDEX_OFFSET, newValue);
    }
}
