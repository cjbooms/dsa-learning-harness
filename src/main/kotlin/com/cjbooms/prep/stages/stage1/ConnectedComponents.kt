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
    TODO("your pick — defend it aloud first")
}
