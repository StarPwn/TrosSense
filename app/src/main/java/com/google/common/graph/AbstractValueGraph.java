package com.google.common.graph;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Map;

/* loaded from: classes.dex */
public abstract class AbstractValueGraph<N, V> extends AbstractGraph<N> implements ValueGraph<N, V> {
    @Override // com.google.common.graph.ValueGraph
    public V edgeValue(Object nodeU, Object nodeV) {
        V value = edgeValueOrDefault(nodeU, nodeV, null);
        if (value == null) {
            Preconditions.checkArgument(nodes().contains(nodeU), "Node %s is not an element of this graph.", nodeU);
            Preconditions.checkArgument(nodes().contains(nodeV), "Node %s is not an element of this graph.", nodeV);
            throw new IllegalArgumentException(String.format("Edge connecting %s to %s is not present in this graph.", nodeU, nodeV));
        }
        return value;
    }

    @Override // com.google.common.graph.AbstractGraph
    public String toString() {
        String propertiesString = String.format("isDirected: %s, allowsSelfLoops: %s", Boolean.valueOf(isDirected()), Boolean.valueOf(allowsSelfLoops()));
        return String.format("%s, nodes: %s, edges: %s", propertiesString, nodes(), edgeValueMap());
    }

    private Map<EndpointPair<N>, V> edgeValueMap() {
        Function<EndpointPair<N>, V> edgeToValueFn = new Function<EndpointPair<N>, V>() { // from class: com.google.common.graph.AbstractValueGraph.1
            @Override // com.google.common.base.Function, java.util.function.Function
            public V apply(EndpointPair<N> endpointPair) {
                return (V) AbstractValueGraph.this.edgeValue(endpointPair.nodeU(), endpointPair.nodeV());
            }
        };
        return Maps.asMap(edges(), edgeToValueFn);
    }
}
