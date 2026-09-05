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
    fun `costly route is taken when cheapest`() {
        // With 1 treated as a movement cost, the cheapest path from (0,0)
        // to (2,2) goes around the 1s rather than through them.
        val grid = arrayOf(
            intArrayOf(0, 1, 0),
            intArrayOf(1, 0, 0),
            intArrayOf(0, 0, 0),
        )
        val distance = shortestPathGrid(grid, 0 to 0, 2 to 2)
        // Cheapest path has total cost 0 and takes 4 steps.
        assertEquals(4, distance)
    }

    @Test
    fun `weighted path reconstruction returns minimum cost route`() {
        // Cheapest path avoids the 1s even though it is longer in steps.
        val grid = arrayOf(
            intArrayOf(0, 1, 0),
            intArrayOf(0, 1, 0),
            intArrayOf(0, 0, 0),
        )
        val path = shortestPathGridPath(grid, 0 to 0, 0 to 2)
        assertEquals(0 to 0, path.first())
        assertEquals(0 to 2, path.last())
        // Min-cost path: 0,0 -> 1,0 -> 2,0 -> 2,1 -> 2,2 -> 1,2 -> 0,2 (7 cells, cost 0)
        assertEquals(7, path.size)
    }
}
