package com.cjbooms.prep.solutions.stage11

import java.util.PriorityQueue

/**
 * Stage 11.7 — Weighted shortest path in a grid (Dijkstra) with path
 * reconstruction.
 *
 * Why this matters: "find the cheapest route through N cells" is the same
 * shape as routing a query plan across heterogeneous shards, picking the
 * minimum-cost sequence of index seeks, or finding the cheapest walk
 * through a sequence of pipeline stages. The grid is just a concrete
 * instance; the algorithm is what matters.
 *
 * Structure-selection ritual:
 *   - DFS / brute path enumeration: exponential — fail fast.
 *   - BFS with a "first time we visit a cell wins" idea: only works if all
 *     edges have the same cost. Here each cell carries a non-negative cost,
 *     so we need the full Dijkstra.
 *   - Dijkstra with a min-heap of (distance, cell): O(m*n log(m*n)).
 *     Track the predecessor of each cell so the route can be reconstructed
 *     by walking back from target to start.
 *
 * Edge cost convention: stepping INTO cell (r, c) costs grid[r][c]; the
 * start cell's own cost is included, matching a "fee to enter" model. This
 * is the standard formulation when the problem says "find a path with the
 * lowest total cost".
 */
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
    if (sr !in 0 until rows || sc !in 0 until cols) return emptyList()
    if (tr !in 0 until rows || tc !in 0 until cols) return emptyList()

    // best[r][c] = cheapest known cost to reach (r, c) from start.
    val best = Array(rows) { IntArray(cols) { Int.MAX_VALUE } }
    // previous[r][c] = the cell we came from when we first settled (r, c).
    val previous = Array(rows) { arrayOfNulls<Pair<Int, Int>>(cols) }

    best[sr][sc] = grid[sr][sc]
    // Heap entries: (cost, row, col). Tie-break on row/col so equal-cost
    // paths explore deterministically.
    val heap = PriorityQueue(compareBy<Triple<Int, Int, Int>> { it.first }.thenBy { it.second }.thenBy { it.third })
    heap.add(Triple(best[sr][sc], sr, sc))

    // Four-connected neighbours.
    val dirs = arrayOf(intArrayOf(-1, 0), intArrayOf(1, 0), intArrayOf(0, -1), intArrayOf(0, 1))

    while (heap.isNotEmpty()) {
        val (cost, r, c) = heap.poll()
        if (r == tr && c == tc) break
        // skip stale heap entries.
        if (cost != best[r][c]) continue
        for (dir in dirs) {
            val nr = r + dir[0]
            val nc = c + dir[1]
            if (nr !in 0 until rows || nc !in 0 until cols) continue
            // Step into (nr, nc): its own cell cost is added.
            val nextCost = cost + grid[nr][nc]
            if (nextCost < best[nr][nc]) {
                best[nr][nc] = nextCost
                previous[nr][nc] = r to c
                heap.add(Triple(nextCost, nr, nc))
            }
        }
    }

    if (best[tr][tc] == Int.MAX_VALUE) return emptyList()

    // Walk predecessors back from target to start, then reverse.
    val path = ArrayList<Pair<Int, Int>>()
    var cursor: Pair<Int, Int>? = target
    while (cursor != null) {
        path.add(cursor)
        if (cursor == start) break
        cursor = previous[cursor.first][cursor.second]
    }
    path.reverse()
    return path
}

fun main() {
    data class Test(val case: String, val expected: List<Pair<Int, Int>>, val actual: List<Pair<Int, Int>>) {
        init {
            if (expected != actual) println("FAILED: $this")
            else println("PASSED: $this")
        }
    }

    // Classic 3x3 weighted grid. The cheapest route from (0,0) to (2,2):
    //   (0,0) -> (1,0) -> (2,0) -> (2,1) -> (2,2)
    // cost = 1 + 1 + 2 + 1 + 3 = 8. The middle cell costs 20, so any path
    // routing through (1,1) is dominated.
    val grid = arrayOf(
        intArrayOf(1, 3, 1),
        intArrayOf(1, 20, 5),
        intArrayOf(2, 1, 3),
    )
    Test(
        case = "Cheapest path avoids the expensive middle cell",
        expected = listOf(0 to 0, 1 to 0, 2 to 0, 2 to 1, 2 to 2),
        actual = shortestPathGridPath(grid, 0 to 0, 2 to 2),
    )

    // Start == target: the path is just the single cell.
    Test(
        case = "Start equals target returns a single-cell path",
        expected = listOf(1 to 1),
        actual = shortestPathGridPath(grid, 1 to 1, 1 to 1),
    )

    // Target out of bounds: not in the grid at all.
    Test(
        case = "Target outside the grid returns an empty path",
        expected = emptyList(),
        actual = shortestPathGridPath(grid, 0 to 0, 5 to 5),
    )
    // Single-row grid: only one route.
    val row = arrayOf(intArrayOf(1, 2, 3, 4))
    Test(
        case = "Single-row grid takes the only path",
        expected = listOf(0 to 0, 0 to 1, 0 to 2, 0 to 3),
        actual = shortestPathGridPath(row, 0 to 0, 0 to 3),
    )
}
