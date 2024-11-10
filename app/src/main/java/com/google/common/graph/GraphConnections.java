package com.google.common.graph;

import java.util.Set;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
interface GraphConnections<N, V> {
    void addPredecessor(N n, V v);

    V addSuccessor(N n, V v);

    Set<N> adjacentNodes();

    Set<N> predecessors();

    void removePredecessor(Object obj);

    V removeSuccessor(Object obj);

    Set<N> successors();

    @Nullable
    V value(Object obj);
}
