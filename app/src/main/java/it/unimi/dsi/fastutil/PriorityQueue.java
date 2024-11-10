package it.unimi.dsi.fastutil;

import java.util.Comparator;

/* loaded from: classes4.dex */
public interface PriorityQueue<K> {
    void clear();

    Comparator<? super K> comparator();

    K dequeue();

    void enqueue(K k);

    K first();

    int size();

    default boolean isEmpty() {
        return size() == 0;
    }

    default K last() {
        throw new UnsupportedOperationException();
    }

    default void changed() {
        throw new UnsupportedOperationException();
    }
}
