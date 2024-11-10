package com.google.common.graph;

import javax.annotation.Nullable;

/* loaded from: classes.dex */
public interface ValueGraph<N, V> extends Graph<N> {
    V edgeValue(Object obj, Object obj2);

    V edgeValueOrDefault(Object obj, Object obj2, @Nullable V v);

    @Override // com.google.common.graph.Graph
    boolean equals(@Nullable Object obj);

    @Override // com.google.common.graph.Graph
    int hashCode();
}
