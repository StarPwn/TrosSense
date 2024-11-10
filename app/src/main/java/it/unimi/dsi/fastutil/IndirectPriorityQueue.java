package it.unimi.dsi.fastutil;

import java.util.Comparator;

/* loaded from: classes4.dex */
public interface IndirectPriorityQueue<K> {
    void clear();

    Comparator<? super K> comparator();

    int dequeue();

    void enqueue(int i);

    int first();

    int size();

    default boolean isEmpty() {
        return size() == 0;
    }

    default int last() {
        throw new UnsupportedOperationException();
    }

    default void changed() {
        changed(first());
    }

    default void changed(int index) {
        throw new UnsupportedOperationException();
    }

    default void allChanged() {
        throw new UnsupportedOperationException();
    }

    default boolean contains(int index) {
        throw new UnsupportedOperationException();
    }

    default boolean remove(int index) {
        throw new UnsupportedOperationException();
    }

    default int front(int[] a) {
        throw new UnsupportedOperationException();
    }
}
