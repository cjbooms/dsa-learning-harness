package com.cjbooms.prep.stages.stage8

import kotlin.math.max
import kotlin.math.min

/**
 * Learn first: see docs/learning-resources.md
 * Number of islands.
 *
 * Given a 2D grid of characters where '1' represents land and '0' represents
 * water, count the number of distinct islands. Two land cells belong to the
 * same island if they are connected horizontally or vertically (4-directional
 * adjacency).
 *
 * @param grid the m x n grid of '0' and '1' characters.
 * @return the number of distinct islands in [grid].
 */
fun countIslands(grid: Array<CharArray>): Int {
    if (grid.isEmpty() || grid[0].isEmpty()) return 0
    val land = '1'
    val visited = 'X'
    val rows = grid.size
    val columns = grid[0].size
    fun isInBounds(row: Int, col: Int): Boolean =
        row >= 0 && row < rows && col >= 0 && col < columns

    val directions = listOf(-1 to 0, 0 to -1, 1 to 0, 0 to 1)

    var islands = 0

    fun findConnectedLand(landStart: Pair<Int, Int>) {
        val queue = ArrayDeque<Pair<Int, Int>>()
        queue.add(landStart)
        grid[landStart.first][landStart.second] = visited
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()

            directions.forEach { (row, column) ->
                val nextRow = current.first + row
                val nextColumn = current.second + column
                if (isInBounds(nextRow, nextColumn)
                    && grid[nextRow][nextColumn] == land
                ) {
                    queue.add(nextRow to nextColumn)
                    grid[nextRow][nextColumn] = visited
                }
            }
        }
    }

    grid.forEachIndexed { row, chars ->
        chars.forEachIndexed { col, seaOrLand ->
            if (seaOrLand == land) {
                islands++
                findConnectedLand(row to col)
            }
        }
    }

    return islands

}




/**
 * Rotting oranges.
 *
 * Given a 2D grid of integers where 0 is empty, 1 is a fresh orange, and 2
 * is a rotten orange, every minute any fresh orange that is horizontally or
 * vertically adjacent to a rotten orange becomes rotten. All rotten oranges
 * rot their neighbours simultaneously each minute. Return the number of
 * minutes until every fresh orange has become rotten, or -1 if any fresh
 * orange can never be reached.
 *
 * @param grid the m x n grid of 0, 1, and 2 values.
 * @return the elapsed minutes until no fresh oranges remain, or -1 if at
 *   least one fresh orange is unreachable from any initially rotten cell.
 */
fun rottingOranges(grid: Array<IntArray>): Int {
    if (grid.isEmpty() || grid[0].isEmpty()) return 0
    val numberOfRows = grid.size
    val numberOfColumns = grid[0].size
    val fresh = 1
    val rotten = 2
    val directions = listOf(-1 to 0, 0 to -1, 1 to 0, 0 to 1)
    var minutesToRot = 0
    var freshOrangesCount = 0

    fun isInBounds(row: Int, col: Int) =
        row >= 0 && row < numberOfRows && col >= 0 && col < numberOfColumns
    data class RottenPosition(val pos: Pair<Int, Int>, val atMinute: Int)

    val queue = ArrayDeque<RottenPosition>()

    grid.forEachIndexed { row, chars ->
        chars.forEachIndexed { col, char ->
            if (char == fresh) freshOrangesCount++
            if (char == rotten) queue.add(RottenPosition(row to col, 0))
        }
    }

    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()

        directions.forEach { (row, col) ->
            val nextRow = current.pos.first + row
            val nextCol = current.pos.second + col
            if (isInBounds(nextRow, nextCol)) {
                if (grid[nextRow][nextCol] == fresh) {
                    queue.add(RottenPosition(nextRow to nextCol, current.atMinute + 1))
                    grid[nextRow][nextCol] = rotten
                    freshOrangesCount--
                    minutesToRot = max(minutesToRot, current.atMinute + 1)
                }
            }

        }

    }

    if (freshOrangesCount > 0) return -1
    else return minutesToRot


}


fun main() {
    println(
        "Expected 2, Actual: " +
                rottingOranges(
                    arrayOf(
                        intArrayOf(1, 2, 1, 0),
                        intArrayOf(1, 0, 1, 1),
                        intArrayOf(1, 2, 0, 2),
                    )
                )
    )
    println(
        "Expected -1, Actual: " +
                rottingOranges(
                    arrayOf(
                        intArrayOf(1, 2, 1, 0),
                        intArrayOf(1, 0, 1, 0),
                        intArrayOf(1, 2, 0, 1),
                    )
                )
    )
}
