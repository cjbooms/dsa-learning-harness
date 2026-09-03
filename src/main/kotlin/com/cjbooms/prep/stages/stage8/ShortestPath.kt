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
): Int = shortestPathGridPath(grid, start, target).let { path ->
    if (path.isEmpty()) -1 else path.size - 1
}

fun shortestPathGridPath(
    grid: Array<IntArray>,
    start: Pair<Int, Int>,
    target: Pair<Int, Int>,
): List<Pair<Int, Int>> {
    if (grid.isEmpty() || grid[0].isEmpty()) return emptyList()
    val rows = grid.size
    val cols = grid[0].size
    val (sr, sc) = start
    val (tr, tc) = target
    if (grid[sr][sc] != 0 || grid[tr][tc] != 0) return emptyList()

    val visited = Array(rows) { BooleanArray(cols) }
    val parent = Array(rows) { IntArray(cols) { -1 } }
    val queue: ArrayDeque<Int> = ArrayDeque()
    queue.addLast(sr * cols + sc)
    visited[sr][sc] = true

    // 4-neighbour moves: up, down, left, right.
    val dr = intArrayOf(-1, 1, 0, 0)
    val dc = intArrayOf(0, 0, -1, 1)

    while (queue.isNotEmpty()) {
        val code = queue.removeFirst()
        val r = code / cols
        val c = code % cols
        if (r == tr && c == tc) {
            // Reconstruct path: walk parent[] back to start.
            val path = mutableListOf<Pair<Int, Int>>()
            var curR = tr
            var curC = tc
            while (curR != sr || curC != sc) {
                path.add(curR to curC)
                val prev = parent[curR][curC]
                curR = prev / cols
                curC = prev % cols
            }
            path.add(sr to sc)
            return path.reversed()
        }
        for (k in 0 until 4) {
            val nr = r + dr[k]
            val nc = c + dc[k]
            if (nr in 0 until rows && nc in 0 until cols &&
                !visited[nr][nc] && grid[nr][nc] == 0
            ) {
                visited[nr][nc] = true
                parent[nr][nc] = r * cols + c
                queue.addLast(nr * cols + nc)
            }
        }
    }
    return emptyList()
}
