package com.cjbooms.prep.stages.stage11

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertArrayEquals

class SlidingWindowMaximumTest {

    @Test
    fun `canonical leetcode example`() {
        val nums = intArrayOf(1, 3, -1, -3, 5, 3, 6, 7)
        val expected = intArrayOf(3, 3, 5, 5, 6, 7)
        assertArrayEquals(expected, maxSlidingWindow(nums, 3))
    }

    @Test
    fun `window of one returns the input`() {
        val nums = intArrayOf(4, 2, 7, 1)
        assertArrayEquals(nums, maxSlidingWindow(nums, 1))
    }

    @Test
    fun `window equals array length returns single max`() {
        val nums = intArrayOf(4, 2, 7, 1)
        assertArrayEquals(intArrayOf(7), maxSlidingWindow(nums, 4))
    }

    @Test
    fun `strictly decreasing input`() {
        val nums = intArrayOf(5, 4, 3, 2, 1)
        assertArrayEquals(intArrayOf(5, 4, 3), maxSlidingWindow(nums, 3))
    }

    @Test
    fun `strictly increasing input`() {
        val nums = intArrayOf(1, 2, 3, 4, 5)
        assertArrayEquals(intArrayOf(3, 4, 5), maxSlidingWindow(nums, 3))
    }
}
