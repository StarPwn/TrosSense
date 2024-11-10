package com.google.common.collect;

import java.util.Map;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public interface MapDifference<K, V> {

    /* loaded from: classes.dex */
    public interface ValueDifference<V> {
        boolean equals(@Nullable Object obj);

        int hashCode();

        V leftValue();

        V rightValue();
    }

    boolean areEqual();

    Map<K, ValueDifference<V>> entriesDiffering();

    Map<K, V> entriesInCommon();

    Map<K, V> entriesOnlyOnLeft();

    Map<K, V> entriesOnlyOnRight();

    boolean equals(@Nullable Object obj);

    int hashCode();
}
