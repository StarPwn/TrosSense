package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.math.IntMath;
import com.google.common.primitives.Ints;
import java.util.AbstractSet;
import java.util.Set;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public abstract class AbstractGraph<N> implements Graph<N> {
    protected long edgeCount() {
        long degreeSum = 0;
        for (N node : nodes()) {
            degreeSum += degree(node);
        }
        Preconditions.checkState((1 & degreeSum) == 0);
        return degreeSum >>> 1;
    }

    @Override // com.google.common.graph.Graph
    public Set<EndpointPair<N>> edges() {
        return new AbstractSet<EndpointPair<N>>() { // from class: com.google.common.graph.AbstractGraph.1
            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public UnmodifiableIterator<EndpointPair<N>> iterator() {
                return EndpointPairIterator.of(AbstractGraph.this);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return Ints.saturatedCast(AbstractGraph.this.edgeCount());
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean contains(@Nullable Object obj) {
                if (!(obj instanceof EndpointPair)) {
                    return false;
                }
                EndpointPair<?> endpointPair = (EndpointPair) obj;
                return AbstractGraph.this.isDirected() == endpointPair.isOrdered() && AbstractGraph.this.nodes().contains(endpointPair.nodeU()) && AbstractGraph.this.successors(endpointPair.nodeU()).contains(endpointPair.nodeV());
            }
        };
    }

    @Override // com.google.common.graph.Graph
    public int degree(Object node) {
        if (isDirected()) {
            return IntMath.saturatedAdd(predecessors(node).size(), successors(node).size());
        }
        Set<N> neighbors = adjacentNodes(node);
        int selfLoopCount = (allowsSelfLoops() && neighbors.contains(node)) ? 1 : 0;
        return IntMath.saturatedAdd(neighbors.size(), selfLoopCount);
    }

    @Override // com.google.common.graph.Graph
    public int inDegree(Object node) {
        return isDirected() ? predecessors(node).size() : degree(node);
    }

    @Override // com.google.common.graph.Graph
    public int outDegree(Object node) {
        return isDirected() ? successors(node).size() : degree(node);
    }

    public String toString() {
        String propertiesString = String.format("isDirected: %s, allowsSelfLoops: %s", Boolean.valueOf(isDirected()), Boolean.valueOf(allowsSelfLoops()));
        return String.format("%s, nodes: %s, edges: %s", propertiesString, nodes(), edges());
    }
}
