package com.cjbooms.prep.stages.stage10

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class SlidingWindowTest {

    @Test
    fun `maxSumSubarrayK returns the best rolling sum`() {
        // normal case: k=3 -> windows [2,1,5]=8, [1,5,1]=7, [5,1,3]=9, [1,3,2]=6 -> max 9
        assertEquals(9, maxSumSubarrayK(intArrayOf(2, 1, 5, 1, 3, 2), 3))
    }

    @Test
    fun `maxSumSubarrayK with k equal to array length`() {
        // edge case: only one window, the whole array
        assertEquals(15, maxSumSubarrayK(intArrayOf(1, 2, 3, 4, 5), 5))
    }

    @Test
    fun `maxSumSubarrayK with all-negative values`() {
        // edge case: pick the least-negative window, never an empty one
        assertEquals(-3, maxSumSubarrayK(intArrayOf(-3, -1, -2, -4), 2))
    }

    @Test
    fun `longestSubstringWithoutRepeats on classic input`() {
        // normal case
        assertEquals(3, longestSubstringWithoutRepeats("abcabcbb"))
    }

    @Test
    fun `longestSubstringWithoutRepeats returns full length when all unique`() {
        // edge case
        assertEquals(5, longestSubstringWithoutRepeats("abcde"))
    }

    @Test
    fun `longestSubstringWithoutRepeats returns zero on empty input`() {
        // edge case
        assertEquals(0, longestSubstringWithoutRepeats(""))
    }

    @Test
    fun `minWindowSubstring finds the canonical example`() {
        // normal case
        assertEquals("BANC", minWindowSubstring("ADOBECODEBANC", "ABC"))
    }

    @Test
    fun `minWindowSubstring returns empty when t is not contained`() {
        // error case: no substring of s contains all chars of t
        assertEquals("", minWindowSubstring("a", "aa"))
    }

    @Test
    fun `minWindowSubstring picks exact match when s equals t`() {
        // edge case: s is the shortest possible window
        assertEquals("a", minWindowSubstring("a", "a"))
    }
}
