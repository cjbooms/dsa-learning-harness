package com.cjbooms.prep.stages.stage8

/**
 * Stage 8.2 — Topological sort via DFS post-order.
 *
 * Why this matters for MongoDB: alternative ordering lens for dependency
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
    TODO("implement")
}
