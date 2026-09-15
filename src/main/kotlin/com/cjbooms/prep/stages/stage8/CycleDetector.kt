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
fun hasCycle(n: Int, edges: List<Pair<Int, Int>>): Boolean {

    var hasCycle = false
    val graph = mutableMapOf<Int, MutableSet<Int>>()

    for(i in 0 until n) {
        graph.computeIfAbsent(i) { mutableSetOf<Int>() }
    }

    edges.forEach { (node, child) ->
        graph[node]!!.add(child)
    }
    println("Graph: $graph")

    val visited = mutableSetOf<Int>()
    val ok = ArrayDeque<Int>()

    fun evaluateNode(n: Int) {
        if (ok.contains(n) || hasCycle) return
        if (visited.contains(n)) {
            println("Cycle Detected with: $n")
            hasCycle = true
            return
        }
        visited.add(n)
        if (graph[n]?.isNotEmpty() ?: false) {
            println("Children found for node $n, children: ${graph[n]}")
            graph[n]!!.forEach { child ->
                evaluateNode(child)
            }
        }
        ok.add(n)
    }

    graph.forEach { (node, _), ->
        evaluateNode(node)
        visited.clear()
    }

    return hasCycle
}


fun main() {
    println(
        "No Cycle Present: " +
                hasCycle(
                    4,
                    listOf(
                        0 to 2,
                        0 to 1,
                        1 to 2,
                    )
                )
    )
    println(
        "Cycle Present: " +
                hasCycle(
                    4,
                    listOf(
                        0 to 2,
                        0 to 1,
                        1 to 0,
                    )
                )

    )

}


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
fun findCycle(n: Int, edges: List<Pair<Int, Int>>): List<Int> {
    var hasCycle = false
    val graph = mutableMapOf<Int, MutableSet<Int>>()
    for(i in 0 until n) {
        graph.computeIfAbsent(i) { mutableSetOf<Int>() }
    }

    edges.forEach { (node, child) ->
        graph[node]!!.add(child)
    }
    println("Graph: $graph")

    val visited = mutableSetOf<Int>()
    val ok = ArrayDeque<Int>()
    val cycle = ArrayDeque<Int>()
    var cycleStart: Int? = null

    fun evaluateNode(n: Int) {
        if (ok.contains(n) || hasCycle) return
        if (visited.contains(n)) {
            println("Cycle Detected with: $n")
            hasCycle = true
            cycleStart = n
            return
        }
        cycle.addLast(n)
        visited.add(n)
        if (graph[n]?.isNotEmpty() ?: false) {
            println("Children found for node $n, children: ${graph[n]}")
            graph[n]!!.forEach { child ->
                evaluateNode(child)
            }
        }
        ok.add(n)
    }

    graph.forEach { (node, _), ->
        evaluateNode(node)
        if (hasCycle) {
            while (true) {
                println("Cycle: $cycle")
                if (cycleStart != null && cycle.first() != cycleStart) {
                    println("Cycle removing $: ${cycle.first()}")
                    cycle.removeFirstOrNull()


                }
                else return cycle
            }
        }
        cycle.clear()
        visited.clear()
    }

    return emptyList()
}
