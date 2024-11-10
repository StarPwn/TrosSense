package com.google.common.graph;

import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
abstract class AbstractUndirectedNetworkConnections<N, E> implements NetworkConnections<N, E> {
    protected final Map<E, N> incidentEdgeMap;

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractUndirectedNetworkConnections(Map<E, N> incidentEdgeMap) {
        this.incidentEdgeMap = (Map) Preconditions.checkNotNull(incidentEdgeMap);
    }

    @Override // com.google.common.graph.NetworkConnections
    public Set<N> predecessors() {
        return adjacentNodes();
    }

    @Override // com.google.common.graph.NetworkConnections
    public Set<N> successors() {
        return adjacentNodes();
    }

    @Override // com.google.common.graph.NetworkConnections
    public Set<E> incidentEdges() {
        return Collections.unmodifiableSet(this.incidentEdgeMap.keySet());
    }

    @Override // com.google.common.graph.NetworkConnections
    public Set<E> inEdges() {
        return incidentEdges();
    }

    @Override // com.google.common.graph.NetworkConnections
    public Set<E> outEdges() {
        return incidentEdges();
    }

    @Override // com.google.common.graph.NetworkConnections
    public N oppositeNode(Object obj) {
        return (N) Preconditions.checkNotNull(this.incidentEdgeMap.get(obj));
    }

    @Override // com.google.common.graph.NetworkConnections
    public N removeInEdge(Object edge, boolean isSelfLoop) {
        if (!isSelfLoop) {
            return removeOutEdge(edge);
        }
        return null;
    }

    @Override // com.google.common.graph.NetworkConnections
    public N removeOutEdge(Object obj) {
        return (N) Preconditions.checkNotNull(this.incidentEdgeMap.remove(obj));
    }

    @Override // com.google.common.graph.NetworkConnections
    public void addInEdge(E edge, N node, boolean isSelfLoop) {
        if (!isSelfLoop) {
            addOutEdge(edge, node);
        }
    }

    @Override // com.google.common.graph.NetworkConnections
    public void addOutEdge(E edge, N node) {
        N previousNode = this.incidentEdgeMap.put(edge, node);
        Preconditions.checkState(previousNode == null);
    }
}
