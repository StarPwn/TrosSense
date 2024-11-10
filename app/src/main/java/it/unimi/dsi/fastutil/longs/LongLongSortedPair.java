package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.SortedPair;
import java.io.Serializable;

/* loaded from: classes4.dex */
public interface LongLongSortedPair extends LongLongPair, SortedPair<Long>, Serializable {
    static LongLongSortedPair of(long left, long right) {
        return LongLongImmutableSortedPair.of(left, right);
    }

    default boolean contains(long e) {
        return e == leftLong() || e == rightLong();
    }

    @Override // it.unimi.dsi.fastutil.SortedPair
    @Deprecated
    default boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        return contains(((Long) o).longValue());
    }
}
