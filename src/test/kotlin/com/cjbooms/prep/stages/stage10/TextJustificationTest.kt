package com.cjbooms.prep.stages.stage10

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class TextJustificationTest {

    @Test
    fun `canonical leetcode example`() {
        val words = arrayOf("This", "is", "an", "example", "of", "text", "justification.")
        val expected = listOf(
            "This    is    an",
            "example  of text",
            "justification.  ",
        )
        assertEquals(expected, textJustify(words, 16))
    }

    @Test
    fun `single word line`() {
        val words = arrayOf("hello", "world")
        assertEquals(listOf("hello     ", "world     "), textJustify(words, 10))
    }

    @Test
    fun `last line is left justified`() {
        val words = arrayOf("a", "b", "c", "d")
        // Greedy packs a,b,c on line 1 ("a b c" is exactly width 5).
        // Last line is left-justified and padded to width 5.
        val expected = listOf("a b c", "d    ")
        assertEquals(expected, textJustify(words, 5))
    }

    @Test
    fun `exactly one word per line`() {
        val words = arrayOf("a", "b", "c")
        assertEquals(listOf("a", "b", "c"), textJustify(words, 1))
    }
}
