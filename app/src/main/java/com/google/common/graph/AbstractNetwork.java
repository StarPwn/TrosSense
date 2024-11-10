package com.google.common.graph;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.math.IntMath;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public abstract class AbstractNetwork<N, E> implements Network<N, E> {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.google.common.graph.AbstractNetwork$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends AbstractGraph<N> {
        AnonymousClass1() {
        }

        @Override // com.google.common.graph.Graph
        public Set<N> nodes() {
            return AbstractNetwork.this.nodes();
        }

        @Override // com.google.common.graph.AbstractGraph, com.google.common.graph.Graph
        public Set<EndpointPair<N>> edges() {
            if (AbstractNetwork.this.allowsParallelEdges()) {
                return super.edges();
            }
            return new AbstractSet<EndpointPair<N>>() { // from class: com.google.common.graph.AbstractNetwork.1.1
                @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
                public Iterator<EndpointPair<N>> iterator() {
                    return Iterators.transform(AbstractNetwork.this.edges().iterator(), new Function<E, EndpointPair<N>>() { // from class: com.google.common.graph.AbstractNetwork.1.1.1
                        @Override // com.google.common.base.Function, java.util.function.Function
                        public /* bridge */ /* synthetic */ Object apply(Object obj) {
                            return apply((C00211) obj);
                        }

                        @Override // com.google.common.base.Function, java.util.function.Function
                        public EndpointPair<N> apply(E edge) {
                            return AbstractNetwork.this.incidentNodes(edge);
                        }
                    });
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                public int size() {
                    return AbstractNetwork.this.edges().size();
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                public boolean contains(@Nullable Object obj) {
                    if (!(obj instanceof EndpointPair)) {
                        return false;
                    }
                    EndpointPair<?> endpointPair = (EndpointPair) obj;
                    return AnonymousClass1.this.isDirected() == endpointPair.isOrdered() && AnonymousClass1.this.nodes().contains(endpointPair.nodeU()) && AnonymousClass1.this.successors(endpointPair.nodeU()).contains(endpointPair.nodeV());
                }
            };
        }

        @Override // com.google.common.graph.Graph
        public ElementOrder<N> nodeOrder() {
            return AbstractNetwork.this.nodeOrder();
        }

        @Override // com.google.common.graph.Graph
        public boolean isDirected() {
            return AbstractNetwork.this.isDirected();
        }

        @Override // com.google.common.graph.Graph
        public boolean allowsSelfLoops() {
            return AbstractNetwork.this.allowsSelfLoops();
        }

        @Override // com.google.common.graph.Graph
        public Set<N> adjacentNodes(Object node) {
            return AbstractNetwork.this.adjacentNodes(node);
        }

        @Override // com.google.common.graph.Graph
        public Set<N> predecessors(Object node) {
            return AbstractNetwork.this.predecessors(node);
        }

        @Override // com.google.common.graph.Graph
        public Set<N> successors(Object node) {
            return AbstractNetwork.this.successors(node);
        }
    }

    @Override // com.google.common.graph.Network
    public Graph<N> asGraph() {
        return new AnonymousClass1();
    }

    @Override // com.google.common.graph.Network
    public int degree(Object node) {
        if (isDirected()) {
            return IntMath.saturatedAdd(inEdges(node).size(), outEdges(node).size());
        }
        return IntMath.saturatedAdd(incidentEdges(node).size(), edgesConnecting(node, node).size());
    }

    @Override // com.google.common.graph.Network
    public int inDegree(Object node) {
        return isDirected() ? inEdges(node).size() : degree(node);
    }

    @Override // com.google.common.graph.Network
    public int outDegree(Object node) {
        return isDirected() ? outEdges(node).size() : degree(node);
    }

    @Override // com.google.common.graph.Network
    public Set<E> adjacentEdges(Object edge) {
        EndpointPair<N> incidentNodes = incidentNodes(edge);
        Set<E> endpointPairIncidentEdges = Sets.union(incidentEdges(incidentNodes.nodeU()), incidentEdges(incidentNodes.nodeV()));
        return Sets.difference(endpointPairIncidentEdges, ImmutableSet.of(edge));
    }

    public String toString() {
        String propertiesString = String.format("isDirected: %s, allowsParallelEdges: %s, allowsSelfLoops: %s", Boolean.valueOf(isDirected()), Boolean.valueOf(allowsParallelEdges()), Boolean.valueOf(allowsSelfLoops()));
        return String.format("%s, nodes: %s, edges: %s", propertiesString, nodes(), edgeIncidentNodesMap());
    }

    private Map<E, EndpointPair<N>> edgeIncidentNodesMap() {
        Function<E, EndpointPair<N>> edgeToIncidentNodesFn = new Function<E, EndpointPair<N>>() { // from class: com.google.common.graph.AbstractNetwork.2
            @Override // com.google.common.base.Function, java.util.function.Function
            public /* bridge */ /* synthetic */ Object apply(Object obj) {
                return apply((AnonymousClass2) obj);
            }

            @Override // com.google.common.base.Function, java.util.function.Function
            public EndpointPair<N> apply(E edge) {
                return AbstractNetwork.this.incidentNodes(edge);
            }
        };
        return Maps.asMap(edges(), edgeToIncidentNodesFn);
    }
}
