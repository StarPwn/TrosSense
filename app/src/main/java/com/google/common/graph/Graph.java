package com.google.common.graph;

import java.util.Set;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public interface Graph<N> {
    Set<N> adjacentNodes(Object obj);

    boolean allowsSelfLoops();

    int degree(Object obj);

    Set<EndpointPair<N>> edges();

    boolean equals(@Nullable Object obj);

    int hashCode();

    int inDegree(Object obj);

    boolean isDirected();

    ElementOrder<N> nodeOrder();

    Set<N> nodes();

    int outDegree(Object obj);

    Set<N> predecessors(Object obj);

    Set<N> successors(Object obj);
}
