package com.cjbooms.prep.stages.stage14

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class StringDpTest {

    @Test
    fun `lcs basic case`() {
        assertEquals(3, longestCommonSubsequence("abcde", "ace"))
        assertEquals(4, longestCommonSubsequence("AGGTAB", "GXTXAYB")) // GTAB
    }

    @Test
    fun `lcs edge cases`() {
        assertEquals(0, longestCommonSubsequence("", "abc"))
        assertEquals(0, longestCommonSubsequence("abc", ""))
        assertEquals(3, longestCommonSubsequence("abc", "abc"))
    }

    @Test
    fun `edit distance classic cases`() {
        assertEquals(3, editDistance("horse", "ros"))
        assertEquals(5, editDistance("intention", "execution"))
        assertEquals(1, editDistance("abc", "abd"))
    }

    @Test
    fun `edit distance edge cases`() {
        assertEquals(0, editDistance("", ""))
        assertEquals(3, editDistance("", "abc"))
        assertEquals(6, editDistance("kitten", ""))
    }
}
