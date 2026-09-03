package com.cjbooms.prep.stages.stage10

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class SlidingWindowTest {

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
