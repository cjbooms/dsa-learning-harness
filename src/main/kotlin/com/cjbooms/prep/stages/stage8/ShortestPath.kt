package com.cjbooms.prep.stages.stage8

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
    data class Position(val coords: Pair<Int, Int>, val steps: Int)

    val rows = grid.size
    val columns = grid[0].size

    fun outOfBounds(row: Int, col: Int): Boolean {
        return row < 0 || row >= rows || col < 0 || col >= columns
    }

    fun blocked(row: Int, col: Int): Boolean {
        return grid[row][col] != 0
    }

    if (outOfBounds(start.first, start.second) || blocked(start.first, start.second))
        throw IllegalArgumentException("Start out of bounds or blocked $start")
    if (outOfBounds(target.first, target.second) || blocked(target.first, target.second))
        throw IllegalArgumentException("Target out of bounds or blocked $start")
    val directions = listOf(1 to 0, 0 to 1, -1 to 0, 0 to -1)

    val queue = ArrayDeque<Position>()
    val visited = mutableSetOf<Pair<Int, Int>>()
    queue.add(Position(start, 0))
    visited.add(start)

    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        if (current.coords == target) return current.steps

        directions.forEach { direction ->
            val nextRow = current.coords.first + direction.first
            val nextCol = current.coords.second + direction.second
            if (!outOfBounds(nextRow, nextCol)
                && !blocked(nextRow, nextCol)
                && !visited.contains(nextRow to nextCol)
            ) {
                val pos = nextRow to nextCol
                queue.addLast(Position(pos, current.steps + 1))
                visited.add(pos)
            }
        }

    }
    return -1
}

fun main() {

    println(
        "Expected 3, Actual: " +
                shortestPathGrid(
                    arrayOf(
                        intArrayOf(0, 1, 0, 1),
                        intArrayOf(0, 0, 0, 1),
                        intArrayOf(0, 1, 1, 1),
                        intArrayOf(0, 1, 0, 1),
                        intArrayOf(0, 1, 0, 1),
                    ),
                    0 to 0,
                    1 to 2
                )
    )
    println(
        "Expected 4, Actual: " +
                shortestPathGrid(
                    arrayOf(
                        intArrayOf(0, 1, 0, 1),
                        intArrayOf(0, 0, 0, 1),
                        intArrayOf(0, 1, 1, 1),
                        intArrayOf(0, 1, 0, 1),
                        intArrayOf(0, 1, 0, 1),
                    ),
                    0 to 0,
                    4 to 0
                )
    )


}

