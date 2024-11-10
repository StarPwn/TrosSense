package it.unimi.dsi.fastutil.ints;

import java.util.ListIterator;

/* loaded from: classes4.dex */
public interface IntListIterator extends IntBidirectionalIterator, ListIterator<Integer> {
    default void set(int k) {
        throw new UnsupportedOperationException();
    }

    default void add(int k) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Iterator, java.util.ListIterator
    default void remove() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.ListIterator
    @Deprecated
    default void set(Integer k) {
        set(k.intValue());
    }

    @Override // java.util.ListIterator
    @Deprecated
    default void add(Integer k) {
        add(k.intValue());
    }

    @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt, java.util.Iterator
    @Deprecated
    default Integer next() {
        return super.next();
    }

    @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator, it.unimi.dsi.fastutil.BidirectionalIterator, java.util.ListIterator
    @Deprecated
    default Integer previous() {
        return super.previous();
    }
}
