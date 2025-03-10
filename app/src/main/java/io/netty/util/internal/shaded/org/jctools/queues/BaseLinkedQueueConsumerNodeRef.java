package io.netty.util.internal.shaded.org.jctools.queues;

import io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess;

/* compiled from: BaseLinkedQueue.java */
/* loaded from: classes4.dex */
abstract class BaseLinkedQueueConsumerNodeRef<E> extends BaseLinkedQueuePad1<E> {
    private static final long C_NODE_OFFSET = UnsafeAccess.fieldOffset(BaseLinkedQueueConsumerNodeRef.class, "consumerNode");
    private LinkedQueueNode<E> consumerNode;

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void spConsumerNode(LinkedQueueNode<E> newValue) {
        this.consumerNode = newValue;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final LinkedQueueNode<E> lvConsumerNode() {
        return (LinkedQueueNode) UnsafeAccess.UNSAFE.getObjectVolatile(this, C_NODE_OFFSET);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final LinkedQueueNode<E> lpConsumerNode() {
        return this.consumerNode;
    }
}
