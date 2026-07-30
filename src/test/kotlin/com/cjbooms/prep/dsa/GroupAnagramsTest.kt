package com.cjbooms.prep.dsa

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class GroupAnagramsTest {

    private fun normalize(groups: List<List<String>>): Set<Set<String>> =
        groups.map { it.toSet() }.toSet()

    @Test
    fun `classic example`() {
        val result = groupAnagrams(listOf("eat", "tea", "tan", "ate", "nat", "bat"))
        assertEquals(
            setOf(setOf("eat", "tea", "ate"), setOf("tan", "nat"), setOf("bat")),
            normalize(result)
        )
    }

    @Test
    fun `empty input`() {
        assertTrue(groupAnagrams(emptyList()).isEmpty())
    }

    @Test
    fun `single word`() {
        assertEquals(setOf(setOf("a")), normalize(groupAnagrams(listOf("a"))))
    }

    @Test
    fun `no anagrams`() {
        assertEquals(setOf(setOf("abc"), setOf("def")), normalize(groupAnagrams(listOf("abc", "def"))))
    }

    @Test
    fun `empty strings group together`() {
        assertEquals(setOf(setOf("", "")), normalize(groupAnagrams(listOf("", ""))))
    }
}
