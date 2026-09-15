package com.cjbooms.prep.solutions.stage8

/**
 * Stage 8.2 — Topological sort via DFS post-order.
 *
 * Why this matters: alternative ordering lens for dependency
 * chains. Useful when you'd naturally think recursively (deep dependency
 * chains first, leaves of the dep graph pulled last) and when the question
 * emphasises "detect a cycle while you traverse" — DFS naturally exposes a
 * recursion stack that Kahn cannot.
 *
 * Structure-selection ritual:
 *   - States: WHITE (unseen) / GRAY (on current DFS stack) / BLACK (done).
 *   - DFS from every unvisited node; on exit, push the node onto the OUTPUT
 *     FRONT (or onto a stack and reverse at the end).
 *   - Cycle detection: hitting a GRAY node from the current DFS stack means a
 *     back edge -> cycle. Return empty list or report the cycle.
 *
 * Time budget: 20 min. Compare with Kahn's: which is more intuitive for you?
 * State aloud: Kahn is BFS-style and produces "ready now" orders naturally;
 * DFS is recursive and exposes cycles via the recursion stack.
 */
fun scheduleTasksDfs(
    tasks: List<String>,
    dependencies: List<Pair<String, String>>,
): List<String> {
    // Build adjacency list: prereq -> [dependents].
    val dependents = mutableMapOf<String, MutableList<String>>()
    tasks.forEach { dependents.getOrPut(it) { mutableListOf() } }
    dependencies.forEach { (prereq, dependent) ->
        dependents.getOrPut(prereq) { mutableListOf() }.add(dependent)
        dependents.getOrPut(dependent) { mutableListOf() }
    }

    val color = mutableMapOf<String, Int>()  // 0 = WHITE, 1 = GRAY, 2 = BLACK
    val finished = ArrayDeque<String>()     // post-order: push on finish, reverse at end
    var hasCycle = false

    fun dfs(node: String) {
        color[node] = 1 // GRAY
        for (dependent in dependents[node].orEmpty()) {
            when (color[dependent] ?: 0) {
                0 -> dfs(dependent)         // WHITE — descend
                1 -> hasCycle = true        // GRAY — back edge, cycle
                else -> {}                  // BLACK — already fully explored
            }
            if (hasCycle) return
        }
        color[node] = 2 // BLACK
        finished.addLast(node)
    }

    for (task in tasks) {
        if ((color[task] ?: 0) == 0) {
            dfs(task)
            if (hasCycle) return emptyList()
        }
    }

    // Reverse post-order = topological sort.
    return finished.reversed()
}
