package com.google.common.graph;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
class ConfigurableNetwork<N, E> extends AbstractNetwork<N, E> {
    private final boolean allowsParallelEdges;
    private final boolean allowsSelfLoops;
    private final ElementOrder<E> edgeOrder;
    protected final MapIteratorCache<E, N> edgeToReferenceNode;
    private final boolean isDirected;
    protected final MapIteratorCache<N, NetworkConnections<N, E>> nodeConnections;
    private final ElementOrder<N> nodeOrder;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConfigurableNetwork(NetworkBuilder<? super N, ? super E> builder) {
        this(builder, builder.nodeOrder.createMap(builder.expectedNodeCount.or((Optional<Integer>) 10).intValue()), builder.edgeOrder.createMap(builder.expectedEdgeCount.or((Optional<Integer>) 20).intValue()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConfigurableNetwork(NetworkBuilder<? super N, ? super E> networkBuilder, Map<N, NetworkConnections<N, E>> map, Map<E, N> map2) {
        this.isDirected = networkBuilder.directed;
        this.allowsParallelEdges = networkBuilder.allowsParallelEdges;
        this.allowsSelfLoops = networkBuilder.allowsSelfLoops;
        this.nodeOrder = (ElementOrder<N>) networkBuilder.nodeOrder.cast();
        this.edgeOrder = (ElementOrder<E>) networkBuilder.edgeOrder.cast();
        this.nodeConnections = map instanceof TreeMap ? new MapRetrievalCache<>(map) : new MapIteratorCache<>(map);
        this.edgeToReferenceNode = new MapIteratorCache<>(map2);
    }

    @Override // com.google.common.graph.Network
    public Set<N> nodes() {
        return this.nodeConnections.unmodifiableKeySet();
    }

    @Override // com.google.common.graph.Network
    public Set<E> edges() {
        return this.edgeToReferenceNode.unmodifiableKeySet();
    }

    @Override // com.google.common.graph.Network
    public boolean isDirected() {
        return this.isDirected;
    }

    @Override // com.google.common.graph.Network
    public boolean allowsParallelEdges() {
        return this.allowsParallelEdges;
    }

    @Override // com.google.common.graph.Network
    public boolean allowsSelfLoops() {
        return this.allowsSelfLoops;
    }

    @Override // com.google.common.graph.Network
    public ElementOrder<N> nodeOrder() {
        return this.nodeOrder;
    }

    @Override // com.google.common.graph.Network
    public ElementOrder<E> edgeOrder() {
        return this.edgeOrder;
    }

    @Override // com.google.common.graph.Network
    public Set<E> incidentEdges(Object node) {
        return checkedConnections(node).incidentEdges();
    }

    @Override // com.google.common.graph.Network
    public EndpointPair<N> incidentNodes(Object edge) {
        N nodeU = checkedReferenceNode(edge);
        N nodeV = this.nodeConnections.get(nodeU).oppositeNode(edge);
        return EndpointPair.of(this, nodeU, nodeV);
    }

    @Override // com.google.common.graph.Network
    public Set<N> adjacentNodes(Object node) {
        return checkedConnections(node).adjacentNodes();
    }

    @Override // com.google.common.graph.Network
    public Set<E> edgesConnecting(Object nodeU, Object nodeV) {
        NetworkConnections<N, E> connectionsU = checkedConnections(nodeU);
        if (!this.allowsSelfLoops && nodeU == nodeV) {
            return ImmutableSet.of();
        }
        Preconditions.checkArgument(containsNode(nodeV), "Node %s is not an element of this graph.", nodeV);
        return connectionsU.edgesConnecting(nodeV);
    }

    @Override // com.google.common.graph.Network
    public Set<E> inEdges(Object node) {
        return checkedConnections(node).inEdges();
    }

    @Override // com.google.common.graph.Network
    public Set<E> outEdges(Object node) {
        return checkedConnections(node).outEdges();
    }

    @Override // com.google.common.graph.Network
    public Set<N> predecessors(Object node) {
        return checkedConnections(node).predecessors();
    }

    @Override // com.google.common.graph.Network
    public Set<N> successors(Object node) {
        return checkedConnections(node).successors();
    }

    protected final NetworkConnections<N, E> checkedConnections(Object node) {
        NetworkConnections<N, E> connections = this.nodeConnections.get(node);
        if (connections == null) {
            Preconditions.checkNotNull(node);
            throw new IllegalArgumentException(String.format("Node %s is not an element of this graph.", node));
        }
        return connections;
    }

    protected final N checkedReferenceNode(Object edge) {
        N referenceNode = this.edgeToReferenceNode.get(edge);
        if (referenceNode == null) {
            Preconditions.checkNotNull(edge);
            throw new IllegalArgumentException(String.format("Edge %s is not an element of this graph.", edge));
        }
        return referenceNode;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean containsNode(@Nullable Object node) {
        return this.nodeConnections.containsKey(node);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean containsEdge(@Nullable Object edge) {
        return this.edgeToReferenceNode.containsKey(edge);
    }
}
