package com.cjbooms.prep.stages.stage8

/**
 * Stage 8.3 — Cycle detection in a directed graph, optionally returning the
 * cycle itself.
 *
 * Why this matters for MongoDB: deadlock detection between transactions,
 * circular migrations in sharded clusters, import pipelines that loop on each
 * other. Boolean is rarely enough — the cycle is the actionable artefact.
 *
 * Structure-selection ritual:
 *   - DFS with WHITE / GRAY / BLACK colouring.
 *   - A back edge to a GRAY node closes a cycle; walk the recursion stack
 *     from the current node back to that GRAY node to materialise the cycle.
 *   - No cycle => return empty list (the no-cycle answer for both
 *     `findCycle` and the boolean `hasCycle` convenience).
 *
 * Time budget: 15 min. Defend aloud: why not Kahn's for cycle detection too?
 * (Kahn can — nodes that never reach in-degree 0 — but DFS exposes the cycle
 * edges, which is usually what the interviewer is probing for.)
 */
fun hasCycle(n: Int, edges: List<Pair<Int, Int>>): Boolean {
    TODO("implement")
}

fun findCycle(n: Int, edges: List<Pair<Int, Int>>): List<Int> {
    TODO("implement")
}

/**
 * DFS with WHITE / GRAY / BLACK colouring. Returns the pair (tail, head)
 * where `tail` is the current node when DFS hits a back edge to GRAY `head`,
 * or null if no cycle is found in this DFS tree.
 */
private fun dfsCycle(
    start: Int,
    adj: Array<MutableList<Int>>,
    color: IntArray,
    parent: IntArray,
): Pair<Int, Int>? {
    TODO("implement")
}
