package it.unimi.dsi.fastutil;

import it.unimi.dsi.fastutil.objects.ObjectObjectImmutableSortedPair;
import java.lang.Comparable;
import java.util.Objects;

/* loaded from: classes4.dex */
public interface SortedPair<K extends Comparable<K>> extends Pair<K, K> {
    static <K extends Comparable<K>> SortedPair<K> of(K l, K r) {
        return ObjectObjectImmutableSortedPair.of((Comparable) l, (Comparable) r);
    }

    default boolean contains(Object o) {
        return Objects.equals(o, left()) || Objects.equals(o, right());
    }
}
