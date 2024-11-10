package com.google.common.graph;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
class ConfigurableValueGraph<N, V> extends AbstractValueGraph<N, V> {
    private final boolean allowsSelfLoops;
    protected long edgeCount;
    private final boolean isDirected;
    protected final MapIteratorCache<N, GraphConnections<N, V>> nodeConnections;
    private final ElementOrder<N> nodeOrder;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConfigurableValueGraph(AbstractGraphBuilder<? super N> builder) {
        this(builder, builder.nodeOrder.createMap(builder.expectedNodeCount.or((Optional<Integer>) 10).intValue()), 0L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConfigurableValueGraph(AbstractGraphBuilder<? super N> abstractGraphBuilder, Map<N, GraphConnections<N, V>> map, long j) {
        this.isDirected = abstractGraphBuilder.directed;
        this.allowsSelfLoops = abstractGraphBuilder.allowsSelfLoops;
        this.nodeOrder = (ElementOrder<N>) abstractGraphBuilder.nodeOrder.cast();
        this.nodeConnections = map instanceof TreeMap ? new MapRetrievalCache<>(map) : new MapIteratorCache<>(map);
        this.edgeCount = Graphs.checkNonNegative(j);
    }

    @Override // com.google.common.graph.Graph
    public Set<N> nodes() {
        return this.nodeConnections.unmodifiableKeySet();
    }

    @Override // com.google.common.graph.Graph
    public boolean isDirected() {
        return this.isDirected;
    }

    @Override // com.google.common.graph.Graph
    public boolean allowsSelfLoops() {
        return this.allowsSelfLoops;
    }

    @Override // com.google.common.graph.Graph
    public ElementOrder<N> nodeOrder() {
        return this.nodeOrder;
    }

    @Override // com.google.common.graph.Graph
    public Set<N> adjacentNodes(Object node) {
        return checkedConnections(node).adjacentNodes();
    }

    @Override // com.google.common.graph.Graph
    public Set<N> predecessors(Object node) {
        return checkedConnections(node).predecessors();
    }

    @Override // com.google.common.graph.Graph
    public Set<N> successors(Object node) {
        return checkedConnections(node).successors();
    }

    @Override // com.google.common.graph.ValueGraph
    public V edgeValueOrDefault(Object nodeU, Object nodeV, @Nullable V defaultValue) {
        GraphConnections<N, V> connectionsU = this.nodeConnections.get(nodeU);
        if (connectionsU == null) {
            return defaultValue;
        }
        V value = connectionsU.value(nodeV);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    @Override // com.google.common.graph.AbstractGraph
    protected long edgeCount() {
        return this.edgeCount;
    }

    protected final GraphConnections<N, V> checkedConnections(Object node) {
        GraphConnections<N, V> connections = this.nodeConnections.get(node);
        if (connections == null) {
            Preconditions.checkNotNull(node);
            throw new IllegalArgumentException(String.format("Node %s is not an element of this graph.", node));
        }
        return connections;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean containsNode(@Nullable Object node) {
        return this.nodeConnections.containsKey(node);
    }
}
