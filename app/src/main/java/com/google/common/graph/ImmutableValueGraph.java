package com.google.common.graph;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.graph.ImmutableGraph;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public final class ImmutableValueGraph<N, V> extends ImmutableGraph.ValueBackedImpl<N, V> implements ValueGraph<N, V> {
    private ImmutableValueGraph(ValueGraph<N, V> graph) {
        super(ValueGraphBuilder.from(graph), getNodeConnections((ValueGraph) graph), graph.edges().size());
    }

    public static <N, V> ImmutableValueGraph<N, V> copyOf(ValueGraph<N, V> graph) {
        return graph instanceof ImmutableValueGraph ? (ImmutableValueGraph) graph : new ImmutableValueGraph<>(graph);
    }

    @Deprecated
    public static <N, V> ImmutableValueGraph<N, V> copyOf(ImmutableValueGraph<N, V> graph) {
        return (ImmutableValueGraph) Preconditions.checkNotNull(graph);
    }

    private static <N, V> ImmutableMap<N, GraphConnections<N, V>> getNodeConnections(ValueGraph<N, V> graph) {
        ImmutableMap.Builder<N, GraphConnections<N, V>> nodeConnections = ImmutableMap.builder();
        for (N node : graph.nodes()) {
            nodeConnections.put(node, connectionsOf((ValueGraph) graph, (Object) node));
        }
        return nodeConnections.build();
    }

    private static <N, V> GraphConnections<N, V> connectionsOf(final ValueGraph<N, V> graph, final N node) {
        Function<N, V> successorNodeToValueFn = new Function<N, V>() { // from class: com.google.common.graph.ImmutableValueGraph.1
            @Override // com.google.common.base.Function, java.util.function.Function
            public V apply(N n) {
                return (V) ValueGraph.this.edgeValue(node, n);
            }
        };
        if (graph.isDirected()) {
            return DirectedGraphConnections.ofImmutable(graph.predecessors(node), Maps.asMap(graph.successors(node), successorNodeToValueFn));
        }
        return UndirectedGraphConnections.ofImmutable(Maps.asMap(graph.adjacentNodes(node), successorNodeToValueFn));
    }

    @Override // com.google.common.graph.ValueGraph
    public V edgeValue(Object nodeU, Object nodeV) {
        return this.backingValueGraph.edgeValue(nodeU, nodeV);
    }

    @Override // com.google.common.graph.ValueGraph
    public V edgeValueOrDefault(Object nodeU, Object nodeV, @Nullable V defaultValue) {
        return this.backingValueGraph.edgeValueOrDefault(nodeU, nodeV, defaultValue);
    }

    @Override // com.google.common.graph.AbstractGraph
    public String toString() {
        return this.backingValueGraph.toString();
    }
}
