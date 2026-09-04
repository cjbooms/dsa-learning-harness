package com.cjbooms.prep.stages.stage8

import java.util.ArrayDeque

/**
 * Stage 8.4 — Shortest path on an unweighted graph (grid variant).
 *
 * Why this matters for MongoDB: query planning intuition (every hop in a join
 * graph is a cost), geospatial shortest-path reasoning on GeoJSON data,
 * latency reasoning across replica hops. The grid form is a frequent interview
 * shape — "0/1 matrix, find shortest path from top-left to bottom-right".
 *
 * Structure-selection ritual:
 *   - BFS from the source — BFS guarantees first arrival is shortest on an
 *     unweighted graph.
 *   - Grid encoding: 4-neighbour (up/down/left/right). Encode (r, c) as
 *     `r * cols + c` to use a plain IntQueue / array.
 *   - Track distance per cell and the predecessor for path reconstruction.
 *   - Return -1 (or empty list) for unreachable.
 *
 * Time budget: 15 min. Defend aloud: why BFS not DFS for shortest path?
 * (DFS explores depth-first and may find a non-shortest route first; BFS
 * expands by distance layers.)
 */
fun shortestPathGrid(
    grid: Array<IntArray>,
    start: Pair<Int, Int>,
    target: Pair<Int, Int>,
): Int {
    TODO("implement")
}

fun shortestPathGridPath(
    grid: Array<IntArray>,
    start: Pair<Int, Int>,
    target: Pair<Int, Int>,
): List<Pair<Int, Int>> {
    TODO("implement")
}
