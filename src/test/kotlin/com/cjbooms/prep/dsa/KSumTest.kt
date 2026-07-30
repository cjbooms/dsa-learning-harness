package com.cjbooms.prep.dsa

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class KSumTest {

    @Test
    fun `two sum counts pairs`() {
        // pairs summing to 6: (1,5), (2,4)
        assertEquals(2, kSumCount(intArrayOf(1, 2, 3, 4, 5), 2, 6))
    }

    @Test
    fun `three sum`() {
        // triples summing to 0: (-1,0,1) x2 (two -1s), (-1,-1,2)
        assertEquals(3, kSumCount(intArrayOf(-1, 0, 1, 2, -1), 3, 0))
    }

    @Test
    fun `four sum`() {
        // quadruples summing to 0: (-2,-1,1,2), (-2,0,0,2), (-1,0,0,1)
        assertEquals(3, kSumCount(intArrayOf(1, 0, -1, 0, -2, 2), 4, 0))
    }

    @Test
    fun `k larger than array`() {
        assertEquals(0, kSumCount(intArrayOf(1, 2), 3, 3))
    }

    @Test
    fun `k less than 2`() {
        assertEquals(0, kSumCount(intArrayOf(1, 2), 1, 2))
    }

    @Test
    fun `empty array`() {
        assertEquals(0, kSumCount(intArrayOf(), 2, 0))
    }

    @Test
    fun `negative target`() {
        // pairs summing to -3: (-4,1), (-2,-1)
        assertEquals(2, kSumCount(intArrayOf(-4, -2, -1, 1, 2), 2, -3))
    }
}
