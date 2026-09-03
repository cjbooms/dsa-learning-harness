package com.cjbooms.prep.stages.stage14

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class BasicDpTest {

    @Test
    fun `climb stairs baseline`() {
        // 1 -> 1, 2 -> 2, 3 -> 3, 4 -> 5 (Fibonacci shifted by one).
        assertEquals(1, climbStairs(1))
        assertEquals(2, climbStairs(2))
        assertEquals(3, climbStairs(3))
        assertEquals(5, climbStairs(4))
    }

    @Test
    fun `climb stairs larger n`() {
        // n=10 -> 89.
        assertEquals(89, climbStairs(10))
    }

    @Test
    fun `house robber adjacent houses excluded`() {
        // Pick houses 0, 2, 4 -> 2 + 9 + 1 = 12 from [2, 7, 9, 3, 1].
        assertEquals(12, houseRobber(listOf(2, 7, 9, 3, 1)))
        assertEquals(4, houseRobber(listOf(1, 2, 3, 1)))
    }

    @Test
    fun `house robber empty and single`() {
        assertEquals(0, houseRobber(emptyList()))
        assertEquals(5, houseRobber(listOf(5)))
    }
}
