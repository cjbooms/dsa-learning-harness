package com.cjbooms.prep.stages.stage8

import kotlin.math.max
import kotlin.math.min

/**
 * Learn first: see docs/learning-resources.md
 * Shortest path in a binary grid.
 *
 * Given a grid where 0 marks an open cell and 1 marks a blocked wall,
 * find the length (in steps) of the shortest 4-directional path from
 * [start] to [target].
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
    val length = grid.size
    println("Length: $length")
    data class Position(val x: Int, val y: Int, val cost: Int, val steps: Int)

    println("Processing Grid: \n" + grid.joinToString(separator = "\n") { it.joinToString() })
    println()

    val queue = ArrayDeque<Position>()
    queue.addLast(Position(start.first, start.second, grid[start.first][start.second], 0))

    val visited = mutableSetOf<Pair<Int, Int>>()
    val directions = listOf(1 to 0, 0 to 1, -1 to 0, 0 to -1)

    var result = sortedMapOf<Int, Int>()

    fun isInBounds(x: Int, y: Int) = min(x, y) >= 0 && max(x, y) < length


    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        println("Processing node: $current")
        if (visited.contains(current.x to current.y)) continue
        if (target == current.x to current.y) {
            println("Target found: $current ")
            result.put(current.cost, current.steps)
            continue
        }
        visited.add(current.x to current.y)
        directions.forEach { (x, y) ->
            val newX = current.x + x
            val newY = current.y + y
            if (isInBounds(newX, newY)) {
                val nextPosition = Position(newX, newY, current.cost + grid[newX][newY], current.steps + 1)
                println("Adding next position: $nextPosition")
                queue.addLast(nextPosition)
            } else {
                println("Out of Bounds: $newX, $newY")

            }
        }
    }
    println("Results: $result")

    return if (result.isNotEmpty()) result.firstEntry().value else -1
}


fun main() {


    println(
        "Expected 2, Actual: " + shortestPathGrid(
            arrayOf(
                intArrayOf(0, 2),
                intArrayOf(4, 1)
            ),
            0 to 0,
            1 to 1
        )
    )

    println()


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
