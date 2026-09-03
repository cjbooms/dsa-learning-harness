package com.cjbooms.prep.stages.stage10

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull

class TwoPointersTest {

    @Test
    fun `pairSumSorted finds the unique matching pair`() {
        // normal case: [2,4,5,6,10] target 11 -> indices [2,3] (5+6)
        assertArrayEquals(intArrayOf(2, 3), pairSumSorted(intArrayOf(2, 4, 5, 6, 10), 11))
    }

    @Test
    fun `pairSumSorted returns null when no pair exists`() {
        // edge case: target unattainable
        assertNull(pairSumSorted(intArrayOf(1, 2, 3, 4), 100))
    }

    @Test
    fun `pairSumSorted handles negatives and mixed signs`() {
        // error-adjacent edge: negatives in a sorted array still respect the two-pointer monotonicity
        assertArrayEquals(intArrayOf(1, 4), pairSumSorted(intArrayOf(-5, -2, 0, 3, 7), 5))
    }

    @Test
    fun `maxArea picks the two best container walls`() {
        // normal case
        assertEquals(49, maxArea(intArrayOf(1, 8, 6, 2, 5, 4, 8, 3, 7)))
    }

    @Test
    fun `maxArea on strictly decreasing heights`() {
        // edge case: the widest container is bounded by the smaller far end
        assertEquals(4, maxArea(intArrayOf(4, 3, 2, 1)))
    }

    @Test
    fun `maxArea on too-small input`() {
        // edge case: fewer than two lines -> zero area
        assertEquals(0, maxArea(intArrayOf(5)))
    }

    @Test
    fun `removeDuplicatesSorted compresses the array in place`() {
        // normal case: dedup length = 5
        val nums = intArrayOf(1, 1, 2, 3, 3, 4, 5, 5)
        val uniqueCount = removeDuplicatesSorted(nums)
        assertEquals(5, uniqueCount)
        assertArrayEquals(intArrayOf(1, 2, 3, 4, 5), nums.copyOfRange(0, uniqueCount))
    }

    @Test
    fun `removeDuplicatesSorted on already-unique array`() {
        // edge case: nothing to remove
        val nums = intArrayOf(1, 2, 3, 4)
        assertEquals(4, removeDuplicatesSorted(nums))
        assertArrayEquals(intArrayOf(1, 2, 3, 4), nums)
    }

    @Test
    fun `removeDuplicatesSorted on all-equal array`() {
        // edge case: single unique value
        val nums = intArrayOf(7, 7, 7, 7, 7)
        assertEquals(1, removeDuplicatesSorted(nums))
        assertArrayEquals(intArrayOf(7), nums.copyOfRange(0, 1))
    }
}
