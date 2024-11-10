package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.SortedPair;
import java.io.Serializable;

/* loaded from: classes4.dex */
public interface IntIntSortedPair extends IntIntPair, SortedPair<Integer>, Serializable {
    static IntIntSortedPair of(int left, int right) {
        return IntIntImmutableSortedPair.of(left, right);
    }

    default boolean contains(int e) {
        return e == leftInt() || e == rightInt();
    }

    @Override // it.unimi.dsi.fastutil.SortedPair
    @Deprecated
    default boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        return contains(((Integer) o).intValue());
    }
}
