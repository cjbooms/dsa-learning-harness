package com.cjbooms.prep.stages.stage8

/**
 * Learn first: see docs/learning-resources.md
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
    if (node == null) return null
    var resultNode: Node? = null

    val originalToCopied = mutableMapOf<Node, Node>()

    fun copyNode(original: Node): Node {
        val copy = Node(original.value)
        originalToCopied[original] = copy
        original.neighbors.forEach { originalNeighbour ->
            if (!originalToCopied.contains(originalNeighbour)) {
                copyNode(originalNeighbour)
            }
            copy.neighbors.add(originalToCopied[originalNeighbour]!!)
        }

        return copy
    }

    resultNode = copyNode(node)


    return resultNode
}



fun main() {
    val node0 = Node(0, mutableListOf())
    val node1 = Node(1, mutableListOf())
    val node2 = Node(2, mutableListOf())
    val node3 = Node(3, mutableListOf())
    node0.neighbors.add(node1)
    node0.neighbors.add(node3)

    node1.neighbors.add(node0)
    node1.neighbors.add(node2)

    node2.neighbors.add(node1)
    node2.neighbors.add(node3)

    node3.neighbors.add(node2)
    node3.neighbors.add(node0)

    println(
        "Expected: ${node1.neighbors.size}, Actual: " +
                cloneGraph(node1)?.neighbors?.size
    )


}