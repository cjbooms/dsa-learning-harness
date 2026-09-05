package com.cjbooms.prep.stages.stage8

import java.util.PriorityQueue

/**
 * Learn first: see docs/learning-resources.md
 * Shortest path in an unweighted binary grid.
 *
 * 0 marks an open/passable cell, 1 marks a blocked wall. Find the length
 * (in steps) of the shortest 4-directional path from [start] to [target].
 *
 * This is standard BFS: every move costs one step, every visited cell is
 * recorded once when it is first enqueued to avoid queue explosion.
 *
 * @param grid the m x n grid of 0s (open) and 1s (walls).
 * @param start the (row, column) of the starting cell.
 * @param target the (row, column) of the destination cell.
 * @return the number of steps in the shortest path from [start] to
 *   [target], or -1 if [target] is unreachable from [start].
 */
fun shortestPathGrid(
    grid: Array<IntArray>,
    start: Pair<Int, Int>,
    target: Pair<Int, Int>,
): Int {
    TODO("implement BFS here")
}

/**
 * Weighted shortest path in a grid, returning the path itself.
 *
 * Each cell holds a non-negative movement cost. Find a path from [start]
 * to [target] with the lowest total cost and return the sequence of cells
 * along that path (inclusive of both endpoints), in order.
 *
 * Use Dijkstra's algorithm: explore cells in order of total cost, tracking
 * the previous cell on the cheapest path so the route can be reconstructed.
 *
 * @param grid the m x n grid of non-negative integer cell costs.
 * @param start the (row, column) of the starting cell.
 * @param target the (row, column) of the destination cell.
 * @return the list of (row, column) cells along one minimum-cost path
 *   from [start] to [target] in order, or an empty list if [target] is
 *   unreachable.
 */
fun shortestPathGridPath(
    grid: Array<IntArray>,
    start: Pair<Int, Int>,
    target: Pair<Int, Int>,
): List<Pair<Int, Int>> {
    TODO("implement Dijkstra's here")
}

fun main() {
    val grid = arrayOf(
        intArrayOf(0, 0, 0),
        intArrayOf(1, 1, 0),
        intArrayOf(0, 0, 0),
    )
    val distance = shortestPathGrid(grid, 0 to 0, 2 to 0)
    println("Expected: 6, Actual: $distance")
}
