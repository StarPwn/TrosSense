package com.google.common.graph;

/* loaded from: classes.dex */
public interface MutableGraph<N> extends Graph<N> {
    boolean addNode(N n);

    boolean putEdge(N n, N n2);

    boolean removeEdge(Object obj, Object obj2);

    boolean removeNode(Object obj);
}
