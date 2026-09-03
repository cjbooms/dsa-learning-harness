package com.cjbooms.prep.stages.stage10

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class PrefixSumsTest {

    @Test
    fun `sumRange returns the inclusive sum for a normal range`() {
        // normal case
        val sums = ImmutableArraySum(intArrayOf(-2, 0, 3, -5, 2, -1))
        assertEquals(1, sums.sumRange(0, 2))   // -2 + 0 + 3
        assertEquals(-1, sums.sumRange(2, 5))  // 3 + -5 + 2 + -1
        assertEquals(-3, sums.sumRange(0, 5))  // whole array
    }

    @Test
    fun `sumRange on a single-element range`() {
        // edge case
        val sums = ImmutableArraySum(intArrayOf(5, 10, 15))
        assertEquals(10, sums.sumRange(1, 1))
    }

    @Test
    fun `sumRange on an empty array`() {
        // edge case: construction is fine, every query must be guarded by the caller
        val sums = ImmutableArraySum(intArrayOf())
        // behaviour for an invalid query is unspecified; the contract is "don't call it"
        // but constructing must not throw, which is the actual invariant we test here.
        assertEquals(0, sums.sumRange(0, -1)) // vacuously empty range -> 0
    }

    @Test
    fun `subarraySumEqualsK counts contiguous runs on the canonical example`() {
        // normal case: [1,1,1], k=2 -> [1,1] starting at 0 and at 1 -> 2
        assertEquals(2, subarraySumEqualsK(intArrayOf(1, 1, 1), 2))
    }

    @Test
    fun `subarraySumEqualsK handles negative numbers`() {
        // normal case with negatives: prefix-sum hashmap is required
        assertEquals(6, subarraySumEqualsK(intArrayOf(1, -1, 1, -1, 1), 0))
    }

    @Test
    fun `subarraySumEqualsK returns zero when no subarray matches`() {
        // edge case: k unreachable
        assertEquals(0, subarraySumEqualsK(intArrayOf(1, 2, 3), 100))
    }

    @Test
    fun `subarraySumEqualsK on empty array`() {
        // edge case
        assertEquals(0, subarraySumEqualsK(intArrayOf(), 0))
    }
}
