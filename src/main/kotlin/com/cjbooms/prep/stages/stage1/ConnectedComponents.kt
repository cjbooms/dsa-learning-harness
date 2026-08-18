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
    val componentConnections = hashMapOf<Int, MutableSet<Int>>()

    edges.forEach() {
        if (componentConnections[it.first] == null) componentConnections[it.first] = mutableSetOf(it.second)
        else componentConnections[it.first]!!.add(it.second)
        if (componentConnections[it.second] == null) componentConnections[it.second] = mutableSetOf(it.first)
        else componentConnections[it.second]!!.add(it.first)
    }
    val visited = mutableSetOf<Int>()
    var uniqueComponenets = 0
    for (candidate in 0..<n) {
        if (!visited.contains(candidate)) {
            //visited.add(candidate)
            uniqueComponenets++
            countNeighbours(candidate, componentConnections, visited)
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
