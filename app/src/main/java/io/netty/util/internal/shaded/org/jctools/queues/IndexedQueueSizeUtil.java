package io.netty.util.internal.shaded.org.jctools.queues;

/* loaded from: classes4.dex */
public final class IndexedQueueSizeUtil {

    /* loaded from: classes4.dex */
    public interface IndexedQueue {
        int capacity();

        long lvConsumerIndex();

        long lvProducerIndex();
    }

    public static int size(IndexedQueue iq) {
        long before;
        long currentProducerIndex;
        long after = iq.lvConsumerIndex();
        do {
            before = after;
            currentProducerIndex = iq.lvProducerIndex();
            after = iq.lvConsumerIndex();
        } while (before != after);
        long size = currentProducerIndex - after;
        if (size > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        if (size < 0) {
            return 0;
        }
        if (iq.capacity() != -1 && size > iq.capacity()) {
            return iq.capacity();
        }
        return (int) size;
    }

    public static boolean isEmpty(IndexedQueue iq) {
        return iq.lvConsumerIndex() >= iq.lvProducerIndex();
    }
}
