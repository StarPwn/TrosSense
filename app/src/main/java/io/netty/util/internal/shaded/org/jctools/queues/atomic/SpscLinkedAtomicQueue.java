package io.netty.util.internal.shaded.org.jctools.queues.atomic;

import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueueUtil;

/* loaded from: classes4.dex */
public class SpscLinkedAtomicQueue<E> extends BaseLinkedAtomicQueue<E> {
    @Override // io.netty.util.internal.shaded.org.jctools.queues.atomic.BaseLinkedAtomicQueue, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public /* bridge */ /* synthetic */ int capacity() {
        return super.capacity();
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.atomic.BaseLinkedAtomicQueue, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public /* bridge */ /* synthetic */ int drain(MessagePassingQueue.Consumer consumer) {
        return super.drain(consumer);
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.atomic.BaseLinkedAtomicQueue, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public /* bridge */ /* synthetic */ int drain(MessagePassingQueue.Consumer consumer, int i) {
        return super.drain(consumer, i);
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.atomic.BaseLinkedAtomicQueue, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public /* bridge */ /* synthetic */ void drain(MessagePassingQueue.Consumer consumer, MessagePassingQueue.WaitStrategy waitStrategy, MessagePassingQueue.ExitCondition exitCondition) {
        super.drain(consumer, waitStrategy, exitCondition);
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.atomic.BaseLinkedAtomicQueue, java.util.AbstractCollection, java.util.Collection, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.atomic.BaseLinkedAtomicQueue, java.util.Queue, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public /* bridge */ /* synthetic */ Object peek() {
        return super.peek();
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.atomic.BaseLinkedAtomicQueue, java.util.Queue, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public /* bridge */ /* synthetic */ Object poll() {
        return super.poll();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.util.internal.shaded.org.jctools.queues.atomic.BaseLinkedAtomicQueue, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public /* bridge */ /* synthetic */ boolean relaxedOffer(Object obj) {
        return super.relaxedOffer(obj);
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.atomic.BaseLinkedAtomicQueue, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public /* bridge */ /* synthetic */ Object relaxedPeek() {
        return super.relaxedPeek();
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.atomic.BaseLinkedAtomicQueue, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public /* bridge */ /* synthetic */ Object relaxedPoll() {
        return super.relaxedPoll();
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.atomic.BaseLinkedAtomicQueue, java.util.AbstractCollection
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    public SpscLinkedAtomicQueue() {
        LinkedQueueAtomicNode<E> node = newNode();
        spProducerNode(node);
        spConsumerNode(node);
        node.soNext(null);
    }

    @Override // java.util.Queue, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public boolean offer(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        LinkedQueueAtomicNode<E> nextNode = newNode(e);
        LinkedQueueAtomicNode<E> oldNode = lpProducerNode();
        soProducerNode(nextNode);
        oldNode.soNext(nextNode);
        return true;
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public int fill(MessagePassingQueue.Supplier<E> s) {
        return MessagePassingQueueUtil.fillUnbounded(this, s);
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public int fill(MessagePassingQueue.Supplier<E> s, int limit) {
        if (s == null) {
            throw new IllegalArgumentException("supplier is null");
        }
        if (limit < 0) {
            throw new IllegalArgumentException("limit is negative:" + limit);
        }
        if (limit == 0) {
            return 0;
        }
        LinkedQueueAtomicNode<E> tail = newNode(s.get());
        for (int i = 1; i < limit; i++) {
            LinkedQueueAtomicNode<E> temp = newNode(s.get());
            tail.spNext(temp);
            tail = temp;
        }
        LinkedQueueAtomicNode<E> oldPNode = lpProducerNode();
        soProducerNode(tail);
        oldPNode.soNext(tail);
        return limit;
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public void fill(MessagePassingQueue.Supplier<E> s, MessagePassingQueue.WaitStrategy wait, MessagePassingQueue.ExitCondition exit) {
        MessagePassingQueueUtil.fill(this, s, wait, exit);
    }
}
