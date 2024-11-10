package io.netty.util.concurrent;

import java.util.Arrays;

/* loaded from: classes4.dex */
final class DefaultFutureListeners {
    private GenericFutureListener<? extends Future<?>>[] listeners = new GenericFutureListener[2];
    private int progressiveSize;
    private int size;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DefaultFutureListeners(GenericFutureListener<? extends Future<?>> first, GenericFutureListener<? extends Future<?>> second) {
        this.listeners[0] = first;
        this.listeners[1] = second;
        this.size = 2;
        if (first instanceof GenericProgressiveFutureListener) {
            this.progressiveSize++;
        }
        if (second instanceof GenericProgressiveFutureListener) {
            this.progressiveSize++;
        }
    }

    public void add(GenericFutureListener<? extends Future<?>> l) {
        GenericFutureListener<? extends Future<?>>[] listeners = this.listeners;
        int size = this.size;
        if (size == listeners.length) {
            GenericFutureListener<? extends Future<?>>[] genericFutureListenerArr = (GenericFutureListener[]) Arrays.copyOf(listeners, size << 1);
            listeners = genericFutureListenerArr;
            this.listeners = genericFutureListenerArr;
        }
        listeners[size] = l;
        this.size = size + 1;
        if (l instanceof GenericProgressiveFutureListener) {
            this.progressiveSize++;
        }
    }

    public void remove(GenericFutureListener<? extends Future<?>> l) {
        GenericFutureListener<? extends Future<?>>[] listeners = this.listeners;
        int size = this.size;
        for (int i = 0; i < size; i++) {
            if (listeners[i] == l) {
                int listenersToMove = (size - i) - 1;
                if (listenersToMove > 0) {
                    System.arraycopy(listeners, i + 1, listeners, i, listenersToMove);
                }
                int size2 = size - 1;
                listeners[size2] = null;
                this.size = size2;
                if (l instanceof GenericProgressiveFutureListener) {
                    this.progressiveSize--;
                    return;
                }
                return;
            }
        }
    }

    public GenericFutureListener<? extends Future<?>>[] listeners() {
        return this.listeners;
    }

    public int size() {
        return this.size;
    }

    public int progressiveSize() {
        return this.progressiveSize;
    }
}
