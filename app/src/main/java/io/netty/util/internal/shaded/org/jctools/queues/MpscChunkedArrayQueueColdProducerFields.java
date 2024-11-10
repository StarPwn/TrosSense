package io.netty.util.internal.shaded.org.jctools.queues;

import io.netty.util.internal.shaded.org.jctools.util.Pow2;
import io.netty.util.internal.shaded.org.jctools.util.RangeUtil;

/* compiled from: MpscChunkedArrayQueue.java */
/* loaded from: classes4.dex */
abstract class MpscChunkedArrayQueueColdProducerFields<E> extends BaseMpscLinkedArrayQueue<E> {
    protected final long maxQueueCapacity;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MpscChunkedArrayQueueColdProducerFields(int initialCapacity, int maxCapacity) {
        super(initialCapacity);
        RangeUtil.checkGreaterThanOrEqual(maxCapacity, 4, "maxCapacity");
        RangeUtil.checkLessThan(Pow2.roundToPowerOfTwo(initialCapacity), Pow2.roundToPowerOfTwo(maxCapacity), "initialCapacity");
        this.maxQueueCapacity = Pow2.roundToPowerOfTwo(maxCapacity) << 1;
    }
}
