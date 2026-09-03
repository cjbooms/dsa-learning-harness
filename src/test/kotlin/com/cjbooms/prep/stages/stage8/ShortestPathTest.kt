package com.cjbooms.prep.stages.stage8

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class ShortestPathTest {

    @Test
    fun `open grid returns Manhattan distance`() {
        // 3x3 grid, all zeros, top-left to bottom-right -> 4 steps
        val grid = arrayOf(
            intArrayOf(0, 0, 0),
            intArrayOf(0, 0, 0),
            intArrayOf(0, 0, 0),
        )
        assertEquals(4, shortestPathGrid(grid, 0 to 0, 2 to 2))
    }

    @Test
    fun `blocked cells force a detour`() {
        // 0 0 0
        // 0 1 0
        // 0 0 0
        // Shortest path now goes around the centre: 0,0 -> 0,1 -> 0,2 -> 1,2 -> 2,2 -> length 4
        // The direct L-shape through (1,1) would be 2 hops, but the wall forces detour
        val grid = arrayOf(
            intArrayOf(0, 0, 0),
            intArrayOf(0, 1, 0),
            intArrayOf(0, 0, 0),
        )
        val distance = shortestPathGrid(grid, 0 to 0, 2 to 2)
        assertEquals(4, distance)
    }

    @Test
    fun `unreachable target returns -1`() {
        // Surrounded by walls, target isolated
        val grid = arrayOf(
            intArrayOf(0, 1, 0),
            intArrayOf(1, 0, 0),
            intArrayOf(0, 0, 0),
        )
        // 1,1 -> 2,1 not directly reachable because (1,1) is bordered by 1s on three sides
        // path: (1,1) -> (2,1) -> (2,2) -> 2 steps from (1,1) to (2,2)
        // from (0,0) -> (2,2): need to navigate around the wall — let's just assert non-negative
        val distance = shortestPathGrid(grid, 0 to 0, 2 to 2)
        // (0,0) -> (0,1)? No, (0,1) is a wall. (0,0) -> (1,0)? No, (1,0) is a wall.
        // (0,0) is isolated! Return -1.
        assertEquals(-1, distance)
    }

    @Test
    fun `path reconstruction returns a valid route`() {
        val grid = arrayOf(
            intArrayOf(0, 0, 0),
            intArrayOf(1, 1, 0),
            intArrayOf(0, 0, 0),
        )
        val path = shortestPathGridPath(grid, 0 to 0, 2 to 2)
        assertEquals(0 to 0, path.first())
        assertEquals(2 to 2, path.last())
        // length should be one more than the hop count
        assertEquals(shortestPathGrid(grid, 0 to 0, 2 to 2) + 1, path.size)
    }
}
