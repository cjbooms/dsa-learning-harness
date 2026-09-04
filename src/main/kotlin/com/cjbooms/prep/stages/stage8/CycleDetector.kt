package com.cjbooms.prep.stages.stage8

/**
 * Learn first: see docs/learning-resources.md
 * Cycle detection in a directed graph.
 *
 * Given a directed graph defined by a count of nodes and a list of directed
 * edges (u, v) meaning u -> v, determine whether the graph contains a cycle.
 *
 * @param n the number of nodes, labelled 0..n - 1.
 * @param edges directed edges of the form (from, to).
 * @return true if the graph contains a directed cycle, false otherwise.
 */

/**
 * Find a directed cycle in a directed graph.
 *
 * Given a directed graph defined by a count of nodes and a list of directed
 * edges (u, v) meaning u -> v, return the nodes of any directed cycle in
 * the order they appear along the cycle.
 *
 * @param n the number of nodes, labelled 0..n - 1.
 * @param edges directed edges of the form (from, to).
 * @return a list of node ids forming a directed cycle (the first and last
 *   ids need not be repeated), or an empty list if the graph is acyclic.
 */
fun hasCycle(n: Int, edges: List<Pair<Int, Int>>): Boolean {
    TODO("implement")
}

fun findCycle(n: Int, edges: List<Pair<Int, Int>>): List<Int> {
    TODO("implement")
}

private fun dfsCycle(
    start: Int,
    adj: Array<MutableList<Int>>,
    color: IntArray,
    parent: IntArray,
): Pair<Int, Int>? {
    TODO("implement")
}
