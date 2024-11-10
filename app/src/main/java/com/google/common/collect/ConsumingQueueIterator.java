package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Queue;

/* loaded from: classes.dex */
class ConsumingQueueIterator<T> extends AbstractIterator<T> {
    private final Queue<T> queue;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConsumingQueueIterator(T... elements) {
        this.queue = new ArrayDeque(elements.length);
        Collections.addAll(this.queue, elements);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConsumingQueueIterator(Queue<T> queue) {
        this.queue = (Queue) Preconditions.checkNotNull(queue);
    }

    @Override // com.google.common.collect.AbstractIterator
    public T computeNext() {
        return this.queue.isEmpty() ? endOfData() : this.queue.remove();
    }
}
