package com.google.common.graph;

import java.util.Set;

/* loaded from: classes.dex */
interface NetworkConnections<N, E> {
    void addInEdge(E e, N n, boolean z);

    void addOutEdge(E e, N n);

    Set<N> adjacentNodes();

    Set<E> edgesConnecting(Object obj);

    Set<E> inEdges();

    Set<E> incidentEdges();

    N oppositeNode(Object obj);

    Set<E> outEdges();

    Set<N> predecessors();

    N removeInEdge(Object obj, boolean z);

    N removeOutEdge(Object obj);

    Set<N> successors();
}
