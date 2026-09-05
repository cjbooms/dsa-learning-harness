package com.cjbooms.prep.stages.stage8

/**
 * Learn first: see docs/learning-resources.md
 * Shortest path in a 0/1 grid.
 *
 * Given a grid of non-negative integers where each cell holds a movement
 * cost (0 or 1, or any non-negative value) and 4-directional adjacency
 * (up, down, left, right), find the length of the shortest path from
 * [start] to [target].
 *
 * @param grid the m x n grid of non-negative integer cell costs.
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
    TODO("implement")
}

/**
 * Shortest path in a 0/1 grid, returning the path itself.
 *
 * Same problem as the other `shortestPathGrid` function, but returns the
 * sequence of cells along the shortest path from [start] to [target]
 * (inclusive of both endpoints), in order.
 *
 * @param grid the m x n grid of non-negative integer cell costs.
 * @param start the (row, column) of the starting cell.
 * @param target the (row, column) of the destination cell.
 * @return the list of (row, column) cells along one shortest path from
 *   [start] to [target] in order, or an empty list if [target] is
 *   unreachable.
 */
fun shortestPathGridPath(
    grid: Array<IntArray>,
    start: Pair<Int, Int>,
    target: Pair<Int, Int>,
): List<Pair<Int, Int>> {
    TODO("implement")
}
