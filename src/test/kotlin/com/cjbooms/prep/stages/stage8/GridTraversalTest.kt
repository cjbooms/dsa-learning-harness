package com.cjbooms.prep.stages.stage8

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class GridTraversalTest {

    @Test
    fun `empty grid has zero islands`() {
        assertEquals(0, countIslands(emptyArray()))
    }

    @Test
    fun `single land cell is one island`() {
        val grid = arrayOf(charArrayOf('1'))
        assertEquals(1, countIslands(grid))
    }

    @Test
    fun `all water has zero islands`() {
        val grid = arrayOf(
            charArrayOf('0', '0'),
            charArrayOf('0', '0'),
        )
        assertEquals(0, countIslands(grid))
    }

    @Test
    fun `diagonal land cells are separate islands`() {
        val grid = arrayOf(
            charArrayOf('1', '0'),
            charArrayOf('0', '1'),
        )
        assertEquals(2, countIslands(grid))
    }

    @Test
    fun `single connected land mass is one island`() {
        val grid = arrayOf(
            charArrayOf('1', '1', '0'),
            charArrayOf('1', '1', '0'),
            charArrayOf('0', '0', '1'),
        )
        assertEquals(2, countIslands(grid))
    }

    @Test
    fun `all rotten oranges with no fresh takes zero minutes`() {
        val grid = arrayOf(
            intArrayOf(2, 2),
            intArrayOf(2, 2),
        )
        assertEquals(0, rottingOranges(grid))
    }

    @Test
    fun `rotting oranges spread to all fresh in four minutes`() {
        val grid = arrayOf(
            intArrayOf(2, 1, 1),
            intArrayOf(1, 1, 0),
            intArrayOf(0, 1, 1),
        )
        assertEquals(4, rottingOranges(grid))
    }

    @Test
    fun `unreachable fresh orange returns minus one`() {
        val grid = arrayOf(
            intArrayOf(2, 1),
            intArrayOf(1, 0),
            intArrayOf(0, 1),
        )
        assertEquals(-1, rottingOranges(grid))
    }

    @Test
    fun `single fresh orange with no rot is impossible`() {
        val grid = arrayOf(intArrayOf(1))
        assertEquals(-1, rottingOranges(grid))
    }

    @Test
    fun `single rotten orange takes zero minutes`() {
        val grid = arrayOf(intArrayOf(2))
        assertEquals(0, rottingOranges(grid))
    }
}
