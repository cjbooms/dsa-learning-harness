package com.cjbooms.prep.solutions.stage8

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
fun hasCycle(n: Int, edges: List<Pair<Int, Int>>): Boolean =
    findCycle(n, edges).isNotEmpty()

fun findCycle(n: Int, edges: List<Pair<Int, Int>>): List<Int> {
    val adj = Array(n) { mutableListOf<Int>() }
    for ((u, v) in edges) {
        if (u in 0 until n && v in 0 until n) adj[u].add(v)
    }

    val color = IntArray(n) // 0 WHITE, 1 GRAY, 2 BLACK
    val parent = IntArray(n) { -1 }

    for (start in 0 until n) {
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
            var cur = tail
            var safety = 0
            while (cur != head) {
                cur = parent[cur]
                if (cur == -1 || ++safety > n) return cycle // defensive
                cycle.add(cur)
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
    for (next in adj[start]) {
        if (color[next] == 1) {
            // Back edge: `next` is on the current DFS stack.
            return start to next
        }
        if (color[next] == 0) {
            parent[next] = start
            val found = dfsCycle(next, adj, color, parent)
            if (found != null) return found
        }
    }
    color[start] = 2 // BLACK
    return null
}
