package com.cjbooms.prep.stages.stage11

import java.util.PriorityQueue

/**
 * Learn first: see docs/learning-resources.md
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
