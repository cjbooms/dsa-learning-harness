package com.cjbooms.prep.solutions.stage8

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
    val (startRow, startCol) = start
    val (targetRow, targetCol) = target
    if (grid[startRow][startCol] != 0 || grid[targetRow][targetCol] != 0) return emptyList()

    val visited = Array(rows) { BooleanArray(cols) }
    val parent = Array(rows) { IntArray(cols) { -1 } }
    val queue: ArrayDeque<Int> = ArrayDeque()
    queue.addLast(startRow * cols + startCol)
    visited[startRow][startCol] = true

    // 4-neighbour moves: up, down, left, right.
    val dr = intArrayOf(-1, 1, 0, 0)
    val dc = intArrayOf(0, 0, -1, 1)

    while (queue.isNotEmpty()) {
        val code = queue.removeFirst()
        val row = code / cols
        val col = code % cols
        if (row == targetRow && col == targetCol) {
            // Reconstruct path: walk parent[] back to start.
            val path = mutableListOf<Pair<Int, Int>>()
            var currentRow = targetRow
            var currentCol = targetCol
            while (currentRow != startRow || currentCol != startCol) {
                path.add(currentRow to currentCol)
                val prev = parent[currentRow][currentCol]
                currentRow = prev / cols
                currentCol = prev % cols
            }
            path.add(startRow to startCol)
            return path.reversed()
        }
        for (directionIndex in 0 until 4) {
            val nextRow = row + dr[directionIndex]
            val nextCol = col + dc[directionIndex]
            if (nextRow in 0 until rows && nextCol in 0 until cols &&
                !visited[nextRow][nextCol] && grid[nextRow][nextCol] == 0
            ) {
                visited[nextRow][nextCol] = true
                parent[nextRow][nextCol] = row * cols + col
                queue.addLast(nextRow * cols + nextCol)
            }
        }
    }
    return emptyList()
}
