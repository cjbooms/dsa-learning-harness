package com.cjbooms.prep.dsa

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue

class WordBreakTest {

    @Test
    fun `segmentable examples`() {
        assertTrue(wordBreak("leetcode", listOf("leet", "code")))
        assertTrue(wordBreak("applepenapple", listOf("apple", "pen")))
        assertTrue(wordBreak("aaaaaaa", listOf("aaaa", "aaa")))
    }

    @Test
    fun `not segmentable`() {
        assertFalse(wordBreak("catsandog", listOf("cats", "dog", "sand", "and", "cat")))
    }

    @Test
    fun `empty string is segmentable`() {
        assertTrue(wordBreak("", listOf("a")))
    }

    @Test
    fun `empty dict`() {
        assertFalse(wordBreak("abc", emptyList()))
    }

    @Test
    fun `word reuse allowed`() {
        assertTrue(wordBreak("abcabc", listOf("abc")))
    }
}
