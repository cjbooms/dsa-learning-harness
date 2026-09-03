package com.cjbooms.prep.stages.stage8

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class AlienDictionaryTest {

    /**
     * Helper: verify that [order] is a valid alphabet ordering for [words].
     * For each pair of adjacent words, the first differing character in the
     * output string must respect the source ordering (lexicographic using
     * the output alphabet).
     */
    private fun isValidOrder(words: List<String>, order: String): Boolean {
        if (order.isEmpty()) return words.isEmpty() || words.any { it.isEmpty() }
        // All characters in `words` must appear in `order`
        val distinct = words.flatMap { it.toSet() }.toSet()
        if (!distinct.all { it in order }) return false
        if (distinct.size != order.length) return false
        val rank = order.withIndex().associate { (i, c) -> c to i }
        for (i in 0 until words.size - 1) {
            val a = words[i]
            val b = words[i + 1]
            // Invalid prefix: shorter word that is a prefix of the next
            if (a.length > b.length && a.startsWith(b)) return false
            val minLen = minOf(a.length, b.length)
            for (k in 0 until minLen) {
                if (a[k] != b[k]) {
                    if (rank[a[k]]!! > rank[b[k]]!!) return false
                    break
                }
            }
        }
        return true
    }

    @Test
    fun `classic example yields a valid alphabet`() {
        val words = listOf("wrt", "wrf", "er", "ett", "rftt")
        val order = alienOrder(words)
        assertTrue(order.isNotEmpty(), "expected a non-empty alphabet")
        assertTrue(isValidOrder(words, order), "ordering invalid for input: $order")
    }

    @Test
    fun `single word returns its distinct characters`() {
        val order = alienOrder(listOf("abc"))
        // The order must contain a, b, c in some sequence (no constraints
        // between them since they never differ across words).
        assertEquals(3, order.length)
        assertEquals(setOf('a', 'b', 'c'), order.toSet())
    }

    @Test
    fun `empty input returns empty string`() {
        assertEquals("", alienOrder(emptyList()))
    }

    @Test
    fun `invalid prefix returns empty string`() {
        // "abc" before "ab" — a valid alphabet would have to make "abc"
        // precede "ab" lexicographically, which is impossible since "ab" is
        // a prefix of "abc".
        val order = alienOrder(listOf("abc", "ab"))
        assertEquals("", order)
    }

    @Test
    fun `cyclic ordering returns empty string`() {
        // z < x (from "z","x"), x < z (from "x","z") -> cycle
        val order = alienOrder(listOf("z", "x", "z"))
        assertEquals("", order)
    }
}
