package com.cjbooms.prep.solutions.stage8

import java.util.ArrayDeque

/**
 * Stage 8.8 — Grid traversal patterns: islands and rotting oranges (40 min).
 *
 * Why this matters for MongoDB: geospatial / image-grid analytics, chunk layout
 * reasoning (contiguous shard key ranges), and cluster-health propagation
 * ("how many minutes until a failure spreads to every affected node?").
 *
 * Structure-selection ritual:
 *   - For `countIslands`, every land cell starts a flood-fill if it hasn't been
 *     visited yet. Either DFS recursion or an explicit stack/queue works; the
 *     stub uses in-place mutation (sink visited '1's to '0') so no extra
 *     visited matrix is needed.
 *   - For `rottingOranges`, single-source BFS (see [ShortestPath.kt]) is the
 *     wrong shape: rot spreads from EVERY initially rotten orange simultaneously.
 *     Seed the queue with all rotten cells, then process level by level. Each
 *     level is one elapsed minute.
 *
 * Time budget: 40 min. Defend aloud: why multi-source BFS for spreading-state
 * problems but single-source BFS for point-to-point shortest path?
 */
fun countIslands(grid: Array<CharArray>): Int {
    if (grid.isEmpty() || grid[0].isEmpty()) return 0
    val rows = grid.size
    val cols = grid[0].size
    var islands = 0
    for (r in 0 until rows) {
        for (c in 0 until cols) {
            if (grid[r][c] == '1') {
                islands++
                sink(grid, r, c, rows, cols)
            }
        }
    }
    return islands
}

private fun sink(grid: Array<CharArray>, r: Int, c: Int, rows: Int, cols: Int) {
    if (r !in 0 until rows || c !in 0 until cols || grid[r][c] != '1') return
    grid[r][c] = '0'
    sink(grid, r - 1, c, rows, cols)
    sink(grid, r + 1, c, rows, cols)
    sink(grid, r, c - 1, rows, cols)
    sink(grid, r, c + 1, rows, cols)
}

fun rottingOranges(grid: Array<IntArray>): Int {
    if (grid.isEmpty() || grid[0].isEmpty()) return 0
    val rows = grid.size
    val cols = grid[0].size
    val queue: ArrayDeque<Pair<Int, Int>> = ArrayDeque()
    var fresh = 0
    for (r in 0 until rows) {
        for (c in 0 until cols) {
            when (grid[r][c]) {
                2 -> queue.addLast(r to c)
                1 -> fresh++
            }
        }
    }
    if (fresh == 0) return 0

    var minutes = 0
    val dr = intArrayOf(-1, 1, 0, 0)
    val dc = intArrayOf(0, 0, -1, 1)
    while (queue.isNotEmpty() && fresh > 0) {
        val levelSize = queue.size
        repeat(levelSize) {
            val (r, c) = queue.removeFirst()
            for (k in 0 until 4) {
                val nr = r + dr[k]
                val nc = c + dc[k]
                if (nr in 0 until rows && nc in 0 until cols && grid[nr][nc] == 1) {
                    grid[nr][nc] = 2
                    fresh--
                    queue.addLast(nr to nc)
                }
            }
        }
        minutes++
    }
    return if (fresh == 0) minutes else -1
}
