package io.netty.util.internal.shaded.org.jctools.queues;

import io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess;

/* loaded from: classes4.dex */
final class LinkedQueueNode<E> {
    private static final long NEXT_OFFSET = UnsafeAccess.fieldOffset(LinkedQueueNode.class, "next");
    private volatile LinkedQueueNode<E> next;
    private E value;

    /* JADX INFO: Access modifiers changed from: package-private */
    public LinkedQueueNode() {
        this(null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LinkedQueueNode(E val) {
        spValue(val);
    }

    public E getAndNullValue() {
        E temp = lpValue();
        spValue(null);
        return temp;
    }

    public E lpValue() {
        return this.value;
    }

    public void spValue(E newValue) {
        this.value = newValue;
    }

    public void soNext(LinkedQueueNode<E> n) {
        UnsafeAccess.UNSAFE.putOrderedObject(this, NEXT_OFFSET, n);
    }

    public void spNext(LinkedQueueNode<E> n) {
        UnsafeAccess.UNSAFE.putObject(this, NEXT_OFFSET, n);
    }

    public LinkedQueueNode<E> lvNext() {
        return this.next;
    }
}
