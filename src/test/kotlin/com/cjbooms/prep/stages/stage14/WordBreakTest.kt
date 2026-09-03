package com.cjbooms.prep.stages.stage14

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue

class WordBreakTest {

    @Test
    fun `wordBreak canonical cases`() {
        assertTrue(wordBreak("leetcode", setOf("leet", "code")))
        assertTrue(wordBreak("applepenapple", setOf("apple", "pen")))
        assertFalse(wordBreak("catsandog", setOf("cats", "dog", "sand", "and", "cat")))
    }

    @Test
    fun `wordBreak empty or single dictionary word`() {
        assertTrue(wordBreak("", setOf("a")))
        assertTrue(wordBreak("a", setOf("a")))
        assertFalse(wordBreak("a", setOf("b")))
    }

    @Test
    fun `wordBreakIi canonical case`() {
        val result = wordBreakIi("catsanddog", setOf("cat", "cats", "and", "sand", "dog"))
        assertEquals(setOf("cat sand dog", "cats and dog"), result.toSet())
    }

    @Test
    fun `wordBreakIi no valid segmentation returns empty`() {
        assertEquals(emptyList<String>(), wordBreakIi("catsandog", setOf("cats", "dog", "sand", "and", "cat")))
    }

    @Test
    fun `wordBreakIi empty string returns empty list`() {
        assertEquals(emptyList<String>(), wordBreakIi("", setOf("a")))
    }
}
