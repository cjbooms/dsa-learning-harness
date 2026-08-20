package com.cjbooms.prep.stages.stage1

/**
 * Stage 1.3 — Connected components (REPORTED question).
 *
 * n nodes labeled 0..<n, undirected edges. Count the connected components.
 *
 * countComponents(5, listOf(0 to 1, 1 to 2, 3 to 4)) == 2
 *
 * Ritual: DFS/BFS + visited set, or union-find? Both are O(~V+E).
 * When is union-find actually worth the extra machinery?
 * (Hint: what if edges ARRIVE one at a time and you re-query repeatedly?)
 */
fun countComponents(n: Int, edges: List<Pair<Int, Int>>): Int {
    val graph = hashMapOf<Int, MutableSet<Int>>()

    edges.forEach {
        graph.getOrPut(it.first) {mutableSetOf()}.add(it.second)
        graph.getOrPut(it.second) {mutableSetOf()}.add(it.first)
    }
    val visited = mutableSetOf<Int>()
    var uniqueComponenets = 0
    for (current in 0..<n) {
        if (!visited.contains(current)) {
            uniqueComponenets++
            countNeighbours(current, graph, visited)
        }
    }

    return uniqueComponenets
}

fun countNeighbours(node: Int, graph: HashMap<Int, MutableSet<Int>>, visited: MutableSet<Int>) {
    if (!visited.contains(node)) {
        visited.add(node)
        val neighbours = graph[node].orEmpty()
        neighbours.forEach { countNeighbours(it, graph, visited) }
    }
}
