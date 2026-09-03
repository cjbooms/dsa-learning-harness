package com.cjbooms.prep.solutions.stage8

/**
 * Stage 8.9 — Clone an undirected connected graph (25 min).
 *
 * Why this matters for MongoDB: query-plan graph transformation, dependency
 * graph copying (e.g. for speculative optimization), and serialization of
 * connected metadata.
 *
 * Structure-selection ritual:
 *   - A graph can contain cycles, so a plain DFS that only tracks "have I been
 *     here?" by node reference is not enough — you must also create the copy.
 *   - HashMap<original Node, copied Node> serves two roles: it is the memo that
 *     avoids allocating a node twice, AND it is the cycle guard. When DFS
 *     returns to an already-seen original node, it returns the existing copy.
 *   - Because the graph is undirected, every edge appears twice (A in B's
 *     neighbors and B in A's neighbors); the map collapses both copies into the
 *     same cloned node.
 */
class Node(val value: Int, val neighbors: MutableList<Node> = mutableListOf())

fun cloneGraph(node: Node?): Node? {
    if (node == null) return null
    val copies = HashMap<Node, Node>()
    return dfsClone(node, copies)
}

private fun dfsClone(original: Node, copies: HashMap<Node, Node>): Node {
    val existing = copies[original]
    if (existing != null) return existing

    val copy = Node(original.value)
    copies[original] = copy
    for (neighbor in original.neighbors) {
        copy.neighbors.add(dfsClone(neighbor, copies))
    }
    return copy
}
