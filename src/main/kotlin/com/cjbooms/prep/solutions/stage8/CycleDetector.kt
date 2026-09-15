package com.cjbooms.prep.solutions.stage8

/**
 * Stage 8.3 — Cycle detection in a directed graph, optionally returning the
 * cycle itself.
 *
 * Why this matters: deadlock detection between transactions,
 * circular migrations in distributed databases, import pipelines that loop on each
 * other. Boolean is rarely enough — the cycle is the actionable artefact.
 *
 * Structure-selection ritual:
 *   - DFS with WHITE / GRAY / BLACK colouring.
 *   - A back edge to a GRAY node closes a cycle; walk the recursion stack
 *     from the current node back to that GRAY node to materialise the cycle.
 *   - No cycle => return empty list (the no-cycle answer for both
 *     `findCycle` and the boolean `hasCycle` convenience).
 *
 * Estimated time: 15 min. Defend your choice: why not Kahn's for cycle detection too?
 * (Kahn can — nodes that never reach in-degree 0 — but DFS exposes the cycle
 * edges, which is usually what the reviewer is probing for.)
 */
fun hasCycle(nodeCount: Int, edges: List<Pair<Int, Int>>): Boolean =
    findCycle(nodeCount, edges).isNotEmpty()

fun findCycle(nodeCount: Int, edges: List<Pair<Int, Int>>): List<Int> {
    val adj = Array(nodeCount) { mutableListOf<Int>() }
    for ((from, to) in edges) {
        if (from in 0 until nodeCount && to in 0 until nodeCount) adj[from].add(to)
    }

    val color = IntArray(nodeCount) // 0 WHITE, 1 GRAY, 2 BLACK
    val parent = IntArray(nodeCount) { -1 }

    for (start in 0 until nodeCount) {
        if (color[start] != 0) continue
        // Returns the deepest node of the back edge (the GRAY ancestor + the
        // current node), or null if no cycle in this DFS tree.
        val pair = dfsCycle(start, adj, color, parent)
        if (pair != null) {
            val (tail, head) = pair
            // Walk parent[] from `tail` back through the stack to `head`,
            // then append `head` to close the cycle. The head may appear
            // multiple times in the stack walk if there are multiple paths
            // to it — we stop at the first occurrence.
            val cycle = mutableListOf(tail)
            var current = tail
            var safety = 0
            while (current != head) {
                current = parent[current]
                if (current == -1 || ++safety > nodeCount) return cycle // defensive
                cycle.add(current)
            }
            return cycle
        }
    }
    return emptyList()
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
    color[start] = 1 // GRAY
    for (neighbor in adj[start]) {
        if (color[neighbor] == 1) {
            // Back edge: `neighbor` is on the current DFS stack.
            return start to neighbor
        }
        if (color[neighbor] == 0) {
            parent[neighbor] = start
            val found = dfsCycle(neighbor, adj, color, parent)
            if (found != null) return found
        }
    }
    color[start] = 2 // BLACK
    return null
}
