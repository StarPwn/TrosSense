package com.google.common.graph;

/* loaded from: classes.dex */
public interface MutableValueGraph<N, V> extends ValueGraph<N, V> {
    boolean addNode(N n);

    V putEdgeValue(N n, N n2, V v);

    V removeEdge(Object obj, Object obj2);

    boolean removeNode(Object obj);
}
