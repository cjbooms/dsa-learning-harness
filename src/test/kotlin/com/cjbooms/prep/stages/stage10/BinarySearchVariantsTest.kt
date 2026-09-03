package com.cjbooms.prep.stages.stage10

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertFalse

class BinarySearchVariantsTest {

    @Test
    fun `searchRotated finds target in unsplit array`() {
        // normal case: pivot is in the middle
        assertEquals(4, searchRotated(intArrayOf(4, 5, 6, 7, 0, 1, 2), 0))
    }

    @Test
    fun `searchRotated returns minus one when target absent`() {
        // error case
        assertEquals(-1, searchRotated(intArrayOf(4, 5, 6, 7, 0, 1, 2), 3))
    }

    @Test
    fun `searchRotated handles array with no rotation`() {
        // edge case: still sorted
        assertEquals(3, searchRotated(intArrayOf(1, 2, 3, 4, 5), 4))
    }

    @Test
    fun `findPeakElement returns a valid peak index`() {
        // normal case: index 2 is the only peak
        val numbers = intArrayOf(1, 2, 3, 1)
        val peakIdx = findPeakElement(numbers)
        assertEquals(2, peakIdx)
        // peak must strictly exceed its neighbours
        assertTrue(numbers[peakIdx] > numbers[peakIdx - 1])
        assertTrue(numbers[peakIdx] > numbers[peakIdx + 1])
    }

    @Test
    fun `findPeakElement on strictly increasing array`() {
        // edge case: the last element is the only peak
        val numbers = intArrayOf(1, 2, 3, 4, 5)
        assertEquals(4, findPeakElement(numbers))
    }

    @Test
    fun `findPeakElement on strictly decreasing array`() {
        // edge case: the first element is the only peak
        val numbers = intArrayOf(5, 4, 3, 2, 1)
        assertEquals(0, findPeakElement(numbers))
    }

    @Test
    fun `search2DMatrix finds or rejects target`() {
        // normal case
        val matrix = arrayOf(
            intArrayOf(1, 3, 5, 7),
            intArrayOf(10, 11, 16, 20),
            intArrayOf(23, 30, 34, 60),
        )
        assertTrue(search2DMatrix(matrix, 11))
        assertFalse(search2DMatrix(matrix, 13))
    }

    @Test
    fun `search2DMatrix returns false when target absent`() {
        // error case
        val matrix = arrayOf(intArrayOf(1), intArrayOf(3))
        assertFalse(search2DMatrix(matrix, 2))
    }

    @Test
    fun `search2DMatrix on single-row matrix`() {
        // edge case
        val matrix = arrayOf(intArrayOf(1, 3, 5, 7))
        assertTrue(search2DMatrix(matrix, 5))
        assertFalse(search2DMatrix(matrix, 6))
    }
}
