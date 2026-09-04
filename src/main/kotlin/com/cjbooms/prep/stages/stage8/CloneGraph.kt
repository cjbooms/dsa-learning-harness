package com.cjbooms.prep.stages.stage8

/**
 * Clone an undirected connected graph.
 *
 * Given a reference to a node in an undirected connected graph, return a
 * deep copy of the entire graph. Each node has an integer value and a list
 * of neighbours; the same neighbour relationships must exist between the
 * copied nodes.
 *
 * The graph may contain cycles, so a copy must not be produced by following
 * references and creating new nodes unconditionally.
 *
 * @param node a node in the graph to clone, or null.
 * @return a new node whose value equals [node]'s value and whose neighbours
 *   are deep copies of the original neighbours' subgraphs, or null if
 *   [node] is null.
 */
class Node(val value: Int, val neighbors: MutableList<Node> = mutableListOf())

fun cloneGraph(node: Node?): Node? {
    TODO("implement")
}
