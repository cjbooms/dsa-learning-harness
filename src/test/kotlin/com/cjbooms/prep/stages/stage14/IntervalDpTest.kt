package com.cjbooms.prep.stages.stage14

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class IntervalDpTest {

    @Test
    fun `matrix chain classic example`() {
        // A=10x30, B=30x5, C=5x60 -> optimal ((AB)C) costs 10*30*5 + 10*5*60 = 4500.
        assertEquals(4500, matrixChainOrder(intArrayOf(10, 30, 5, 60)))
        // A=40x20, B=20x30, C=30x10, D=10x30 -> optimal (A(BC))(CD) costs 26000.
        assertEquals(26000, matrixChainOrder(intArrayOf(40, 20, 30, 10, 30)))
    }

    @Test
    fun `matrix chain single matrix is zero cost`() {
        assertEquals(0, matrixChainOrder(intArrayOf(5, 10)))
    }

    @Test
    fun `burst balloons classic example`() {
        // [3,1,5,8] -> optimal 167.
        assertEquals(167, maxCoinsBurst(intArrayOf(3, 1, 5, 8)))
    }

    @Test
    fun `burst balloons edge cases`() {
        assertEquals(0, maxCoinsBurst(intArrayOf()))
        assertEquals(5, maxCoinsBurst(intArrayOf(5)))
    }
}
