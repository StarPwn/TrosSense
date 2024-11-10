package com.google.common.graph;

import java.util.Set;

/* loaded from: classes.dex */
abstract class ForwardingGraph<N> extends AbstractGraph<N> {
    protected abstract Graph<N> delegate();

    @Override // com.google.common.graph.Graph
    public Set<N> nodes() {
        return delegate().nodes();
    }

    @Override // com.google.common.graph.AbstractGraph, com.google.common.graph.Graph
    public Set<EndpointPair<N>> edges() {
        return delegate().edges();
    }

    @Override // com.google.common.graph.Graph
    public boolean isDirected() {
        return delegate().isDirected();
    }

    @Override // com.google.common.graph.Graph
    public boolean allowsSelfLoops() {
        return delegate().allowsSelfLoops();
    }

    @Override // com.google.common.graph.Graph
    public ElementOrder<N> nodeOrder() {
        return delegate().nodeOrder();
    }

    @Override // com.google.common.graph.Graph
    public Set<N> adjacentNodes(Object node) {
        return delegate().adjacentNodes(node);
    }

    @Override // com.google.common.graph.Graph
    public Set<N> predecessors(Object node) {
        return delegate().predecessors(node);
    }

    @Override // com.google.common.graph.Graph
    public Set<N> successors(Object node) {
        return delegate().successors(node);
    }

    @Override // com.google.common.graph.AbstractGraph, com.google.common.graph.Graph
    public int degree(Object node) {
        return delegate().degree(node);
    }

    @Override // com.google.common.graph.AbstractGraph, com.google.common.graph.Graph
    public int inDegree(Object node) {
        return delegate().inDegree(node);
    }

    @Override // com.google.common.graph.AbstractGraph, com.google.common.graph.Graph
    public int outDegree(Object node) {
        return delegate().outDegree(node);
    }
}
