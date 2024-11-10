package it.unimi.dsi.fastutil.objects;

import java.util.ListIterator;

/* loaded from: classes4.dex */
public interface ObjectListIterator<K> extends ObjectBidirectionalIterator<K>, ListIterator<K> {
    default void set(K k) {
        throw new UnsupportedOperationException();
    }

    default void add(K k) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Iterator, java.util.ListIterator
    default void remove() {
        throw new UnsupportedOperationException();
    }
}
