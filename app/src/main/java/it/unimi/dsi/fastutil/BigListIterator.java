package it.unimi.dsi.fastutil;

/* loaded from: classes4.dex */
public interface BigListIterator<K> extends BidirectionalIterator<K> {
    long nextIndex();

    long previousIndex();

    default void set(K e) {
        throw new UnsupportedOperationException();
    }

    default void add(K e) {
        throw new UnsupportedOperationException();
    }
}
