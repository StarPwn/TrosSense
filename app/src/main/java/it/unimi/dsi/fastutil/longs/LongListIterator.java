package it.unimi.dsi.fastutil.longs;

import java.util.ListIterator;

/* loaded from: classes4.dex */
public interface LongListIterator extends LongBidirectionalIterator, ListIterator<Long> {
    default void set(long k) {
        throw new UnsupportedOperationException();
    }

    default void add(long k) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Iterator, java.util.ListIterator
    default void remove() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.ListIterator
    @Deprecated
    default void set(Long k) {
        set(k.longValue());
    }

    @Override // java.util.ListIterator
    @Deprecated
    default void add(Long k) {
        add(k.longValue());
    }

    @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong, java.util.Iterator
    @Deprecated
    default Long next() {
        return super.next();
    }

    @Override // it.unimi.dsi.fastutil.longs.LongBidirectionalIterator, it.unimi.dsi.fastutil.BidirectionalIterator, java.util.ListIterator
    @Deprecated
    default Long previous() {
        return super.previous();
    }
}
