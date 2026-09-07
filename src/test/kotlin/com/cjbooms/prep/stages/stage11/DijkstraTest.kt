package com.cjbooms.prep.stages.stage11

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class DijkstraTest {

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
