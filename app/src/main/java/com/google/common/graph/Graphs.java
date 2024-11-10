package com.google.common.graph;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public final class Graphs {

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public enum NodeVisitState {
        PENDING,
        COMPLETE
    }

    private Graphs() {
    }

    public static boolean hasCycle(Graph<?> graph) {
        int numEdges = graph.edges().size();
        if (numEdges == 0) {
            return false;
        }
        if (!graph.isDirected() && numEdges >= graph.nodes().size()) {
            return true;
        }
        Map<Object, NodeVisitState> visitedNodes = Maps.newHashMapWithExpectedSize(graph.nodes().size());
        for (Object node : graph.nodes()) {
            if (subgraphHasCycle(graph, visitedNodes, node, null)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasCycle(Network<?, ?> network) {
        if (!network.isDirected() && network.allowsParallelEdges() && network.edges().size() > network.asGraph().edges().size()) {
            return true;
        }
        return hasCycle(network.asGraph());
    }

    private static boolean subgraphHasCycle(Graph<?> graph, Map<Object, NodeVisitState> visitedNodes, Object node, @Nullable Object previousNode) {
        NodeVisitState state = visitedNodes.get(node);
        if (state == NodeVisitState.COMPLETE) {
            return false;
        }
        if (state == NodeVisitState.PENDING) {
            return true;
        }
        visitedNodes.put(node, NodeVisitState.PENDING);
        for (Object nextNode : graph.successors(node)) {
            if (canTraverseWithoutReusingEdge(graph, nextNode, previousNode) && subgraphHasCycle(graph, visitedNodes, nextNode, node)) {
                return true;
            }
        }
        visitedNodes.put(node, NodeVisitState.COMPLETE);
        return false;
    }

    private static boolean canTraverseWithoutReusingEdge(Graph<?> graph, Object nextNode, @Nullable Object previousNode) {
        if (graph.isDirected() || !Objects.equal(previousNode, nextNode)) {
            return true;
        }
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <N> Graph<N> transitiveClosure(Graph<N> graph) {
        ConfigurableMutableGraph build = GraphBuilder.from(graph).allowsSelfLoops(true).build();
        if (graph.isDirected()) {
            for (N n : graph.nodes()) {
                Iterator it2 = reachableNodes(graph, n).iterator();
                while (it2.hasNext()) {
                    build.putEdge(n, it2.next());
                }
            }
        } else {
            HashSet hashSet = new HashSet();
            for (N n2 : graph.nodes()) {
                if (!hashSet.contains(n2)) {
                    Set reachableNodes = reachableNodes(graph, n2);
                    hashSet.addAll(reachableNodes);
                    int i = 1;
                    for (Object obj : reachableNodes) {
                        int i2 = i + 1;
                        Iterator it3 = Iterables.limit(reachableNodes, i).iterator();
                        while (it3.hasNext()) {
                            build.putEdge(obj, it3.next());
                        }
                        i = i2;
                    }
                }
            }
        }
        return build;
    }

    public static <N> Set<N> reachableNodes(Graph<N> graph, Object node) {
        Preconditions.checkArgument(graph.nodes().contains(node), "Node %s is not an element of this graph.", node);
        Set<N> visitedNodes = new LinkedHashSet<>();
        Queue<N> queuedNodes = new ArrayDeque<>();
        visitedNodes.add(node);
        queuedNodes.add(node);
        while (!queuedNodes.isEmpty()) {
            N currentNode = queuedNodes.remove();
            for (N successor : graph.successors(currentNode)) {
                if (visitedNodes.add(successor)) {
                    queuedNodes.add(successor);
                }
            }
        }
        return Collections.unmodifiableSet(visitedNodes);
    }

    public static boolean equivalent(@Nullable Graph<?> graphA, @Nullable Graph<?> graphB) {
        if (graphA == graphB) {
            return true;
        }
        if (graphA == null || graphB == null) {
            return false;
        }
        if (graphA.isDirected() == graphB.isDirected() && graphA.nodes().equals(graphB.nodes()) && graphA.edges().equals(graphB.edges())) {
            return true;
        }
        return false;
    }

    public static boolean equivalent(@Nullable ValueGraph<?, ?> graphA, @Nullable ValueGraph<?, ?> graphB) {
        if (graphA == graphB) {
            return true;
        }
        if (graphA == null || graphB == null || graphA.isDirected() != graphB.isDirected() || !graphA.nodes().equals(graphB.nodes()) || !graphA.edges().equals(graphB.edges())) {
            return false;
        }
        for (EndpointPair<?> edge : graphA.edges()) {
            if (!graphA.edgeValue(edge.nodeU(), edge.nodeV()).equals(graphB.edgeValue(edge.nodeU(), edge.nodeV()))) {
                return false;
            }
        }
        return true;
    }

    public static boolean equivalent(@Nullable Network<?, ?> networkA, @Nullable Network<?, ?> networkB) {
        if (networkA == networkB) {
            return true;
        }
        if (networkA == null || networkB == null || networkA.isDirected() != networkB.isDirected() || !networkA.nodes().equals(networkB.nodes()) || !networkA.edges().equals(networkB.edges())) {
            return false;
        }
        for (Object edge : networkA.edges()) {
            if (!networkA.incidentNodes(edge).equals(networkB.incidentNodes(edge))) {
                return false;
            }
        }
        return true;
    }

    public static <N> Graph<N> transpose(Graph<N> graph) {
        if (!graph.isDirected()) {
            return graph;
        }
        if (!(graph instanceof TransposedGraph)) {
            return new TransposedGraph(graph);
        }
        return ((TransposedGraph) graph).graph;
    }

    /* loaded from: classes.dex */
    private static class TransposedGraph<N> extends AbstractGraph<N> {
        private final Graph<N> graph;

        TransposedGraph(Graph<N> graph) {
            this.graph = graph;
        }

        @Override // com.google.common.graph.Graph
        public Set<N> nodes() {
            return this.graph.nodes();
        }

        @Override // com.google.common.graph.AbstractGraph
        protected long edgeCount() {
            return this.graph.edges().size();
        }

        @Override // com.google.common.graph.Graph
        public boolean isDirected() {
            return this.graph.isDirected();
        }

        @Override // com.google.common.graph.Graph
        public boolean allowsSelfLoops() {
            return this.graph.allowsSelfLoops();
        }

        @Override // com.google.common.graph.Graph
        public ElementOrder<N> nodeOrder() {
            return this.graph.nodeOrder();
        }

        @Override // com.google.common.graph.Graph
        public Set<N> adjacentNodes(Object node) {
            return this.graph.adjacentNodes(node);
        }

        @Override // com.google.common.graph.Graph
        public Set<N> predecessors(Object node) {
            return this.graph.successors(node);
        }

        @Override // com.google.common.graph.Graph
        public Set<N> successors(Object node) {
            return this.graph.predecessors(node);
        }
    }

    public static <N, V> ValueGraph<N, V> transpose(ValueGraph<N, V> graph) {
        if (!graph.isDirected()) {
            return graph;
        }
        if (!(graph instanceof TransposedValueGraph)) {
            return new TransposedValueGraph(graph);
        }
        return ((TransposedValueGraph) graph).graph;
    }

    /* loaded from: classes.dex */
    private static class TransposedValueGraph<N, V> extends AbstractValueGraph<N, V> {
        private final ValueGraph<N, V> graph;

        TransposedValueGraph(ValueGraph<N, V> graph) {
            this.graph = graph;
        }

        @Override // com.google.common.graph.Graph
        public Set<N> nodes() {
            return this.graph.nodes();
        }

        @Override // com.google.common.graph.AbstractGraph
        protected long edgeCount() {
            return this.graph.edges().size();
        }

        @Override // com.google.common.graph.Graph
        public boolean isDirected() {
            return this.graph.isDirected();
        }

        @Override // com.google.common.graph.Graph
        public boolean allowsSelfLoops() {
            return this.graph.allowsSelfLoops();
        }

        @Override // com.google.common.graph.Graph
        public ElementOrder<N> nodeOrder() {
            return this.graph.nodeOrder();
        }

        @Override // com.google.common.graph.Graph
        public Set<N> adjacentNodes(Object node) {
            return this.graph.adjacentNodes(node);
        }

        @Override // com.google.common.graph.Graph
        public Set<N> predecessors(Object node) {
            return this.graph.successors(node);
        }

        @Override // com.google.common.graph.Graph
        public Set<N> successors(Object node) {
            return this.graph.predecessors(node);
        }

        @Override // com.google.common.graph.AbstractValueGraph, com.google.common.graph.ValueGraph
        public V edgeValue(Object nodeU, Object nodeV) {
            return this.graph.edgeValue(nodeV, nodeU);
        }

        @Override // com.google.common.graph.ValueGraph
        public V edgeValueOrDefault(Object nodeU, Object nodeV, @Nullable V defaultValue) {
            return this.graph.edgeValueOrDefault(nodeV, nodeU, defaultValue);
        }
    }

    public static <N, E> Network<N, E> transpose(Network<N, E> network) {
        if (!network.isDirected()) {
            return network;
        }
        if (!(network instanceof TransposedNetwork)) {
            return new TransposedNetwork(network);
        }
        return ((TransposedNetwork) network).network;
    }

    /* loaded from: classes.dex */
    private static class TransposedNetwork<N, E> extends AbstractNetwork<N, E> {
        private final Network<N, E> network;

        TransposedNetwork(Network<N, E> network) {
            this.network = network;
        }

        @Override // com.google.common.graph.Network
        public Set<N> nodes() {
            return this.network.nodes();
        }

        @Override // com.google.common.graph.Network
        public Set<E> edges() {
            return this.network.edges();
        }

        @Override // com.google.common.graph.Network
        public boolean isDirected() {
            return this.network.isDirected();
        }

        @Override // com.google.common.graph.Network
        public boolean allowsParallelEdges() {
            return this.network.allowsParallelEdges();
        }

        @Override // com.google.common.graph.Network
        public boolean allowsSelfLoops() {
            return this.network.allowsSelfLoops();
        }

        @Override // com.google.common.graph.Network
        public ElementOrder<N> nodeOrder() {
            return this.network.nodeOrder();
        }

        @Override // com.google.common.graph.Network
        public ElementOrder<E> edgeOrder() {
            return this.network.edgeOrder();
        }

        @Override // com.google.common.graph.Network
        public Set<N> adjacentNodes(Object node) {
            return this.network.adjacentNodes(node);
        }

        @Override // com.google.common.graph.Network
        public Set<N> predecessors(Object node) {
            return this.network.successors(node);
        }

        @Override // com.google.common.graph.Network
        public Set<N> successors(Object node) {
            return this.network.predecessors(node);
        }

        @Override // com.google.common.graph.Network
        public Set<E> incidentEdges(Object node) {
            return this.network.incidentEdges(node);
        }

        @Override // com.google.common.graph.Network
        public Set<E> inEdges(Object node) {
            return this.network.outEdges(node);
        }

        @Override // com.google.common.graph.Network
        public Set<E> outEdges(Object node) {
            return this.network.inEdges(node);
        }

        @Override // com.google.common.graph.Network
        public EndpointPair<N> incidentNodes(Object edge) {
            EndpointPair<N> endpointPair = this.network.incidentNodes(edge);
            return EndpointPair.of((Network<?, ?>) this.network, (Object) endpointPair.nodeV(), (Object) endpointPair.nodeU());
        }

        @Override // com.google.common.graph.AbstractNetwork, com.google.common.graph.Network
        public Set<E> adjacentEdges(Object edge) {
            return this.network.adjacentEdges(edge);
        }

        @Override // com.google.common.graph.Network
        public Set<E> edgesConnecting(Object nodeU, Object nodeV) {
            return this.network.edgesConnecting(nodeV, nodeU);
        }
    }

    public static <N> MutableGraph<N> inducedSubgraph(Graph<N> graph, Iterable<? extends N> nodes) {
        MutableGraph<N> subgraph = GraphBuilder.from(graph).build();
        Iterator<? extends N> it2 = nodes.iterator();
        while (it2.hasNext()) {
            subgraph.addNode(it2.next());
        }
        for (N node : subgraph.nodes()) {
            for (N successorNode : graph.successors(node)) {
                if (subgraph.nodes().contains(successorNode)) {
                    subgraph.putEdge(node, successorNode);
                }
            }
        }
        return subgraph;
    }

    public static <N, V> MutableValueGraph<N, V> inducedSubgraph(ValueGraph<N, V> graph, Iterable<? extends N> nodes) {
        MutableValueGraph<N, V> subgraph = ValueGraphBuilder.from(graph).build();
        Iterator<? extends N> it2 = nodes.iterator();
        while (it2.hasNext()) {
            subgraph.addNode(it2.next());
        }
        for (N node : subgraph.nodes()) {
            for (N successorNode : graph.successors(node)) {
                if (subgraph.nodes().contains(successorNode)) {
                    subgraph.putEdgeValue(node, successorNode, graph.edgeValue(node, successorNode));
                }
            }
        }
        return subgraph;
    }

    public static <N, E> MutableNetwork<N, E> inducedSubgraph(Network<N, E> network, Iterable<? extends N> nodes) {
        MutableNetwork<N, E> subgraph = NetworkBuilder.from(network).build();
        Iterator<? extends N> it2 = nodes.iterator();
        while (it2.hasNext()) {
            subgraph.addNode(it2.next());
        }
        for (N node : subgraph.nodes()) {
            for (E edge : network.outEdges(node)) {
                N successorNode = network.incidentNodes(edge).adjacentNode(node);
                if (subgraph.nodes().contains(successorNode)) {
                    subgraph.addEdge(node, successorNode, edge);
                }
            }
        }
        return subgraph;
    }

    public static <N> MutableGraph<N> copyOf(Graph<N> graph) {
        MutableGraph<N> mutableGraph = (MutableGraph<N>) GraphBuilder.from(graph).expectedNodeCount(graph.nodes().size()).build();
        Iterator<N> it2 = graph.nodes().iterator();
        while (it2.hasNext()) {
            mutableGraph.addNode(it2.next());
        }
        for (EndpointPair<N> endpointPair : graph.edges()) {
            mutableGraph.putEdge(endpointPair.nodeU(), endpointPair.nodeV());
        }
        return mutableGraph;
    }

    public static <N, V> MutableValueGraph<N, V> copyOf(ValueGraph<N, V> valueGraph) {
        MutableValueGraph<N, V> mutableValueGraph = (MutableValueGraph<N, V>) ValueGraphBuilder.from(valueGraph).expectedNodeCount(valueGraph.nodes().size()).build();
        Iterator<N> it2 = valueGraph.nodes().iterator();
        while (it2.hasNext()) {
            mutableValueGraph.addNode(it2.next());
        }
        for (EndpointPair<N> endpointPair : valueGraph.edges()) {
            mutableValueGraph.putEdgeValue(endpointPair.nodeU(), endpointPair.nodeV(), valueGraph.edgeValue(endpointPair.nodeU(), endpointPair.nodeV()));
        }
        return mutableValueGraph;
    }

    public static <N, E> MutableNetwork<N, E> copyOf(Network<N, E> network) {
        MutableNetwork<N, E> mutableNetwork = (MutableNetwork<N, E>) NetworkBuilder.from(network).expectedNodeCount(network.nodes().size()).expectedEdgeCount(network.edges().size()).build();
        Iterator<N> it2 = network.nodes().iterator();
        while (it2.hasNext()) {
            mutableNetwork.addNode(it2.next());
        }
        for (E e : network.edges()) {
            EndpointPair<N> incidentNodes = network.incidentNodes(e);
            mutableNetwork.addEdge(incidentNodes.nodeU(), incidentNodes.nodeV(), e);
        }
        return mutableNetwork;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int checkNonNegative(int value) {
        Preconditions.checkArgument(value >= 0, "Not true that %s is non-negative.", value);
        return value;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int checkPositive(int value) {
        Preconditions.checkArgument(value > 0, "Not true that %s is positive.", value);
        return value;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long checkNonNegative(long value) {
        Preconditions.checkArgument(value >= 0, "Not true that %s is non-negative.", value);
        return value;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long checkPositive(long value) {
        Preconditions.checkArgument(value > 0, "Not true that %s is positive.", value);
        return value;
    }
}
