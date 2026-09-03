package com.cjbooms.prep.stages.stage9

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertFalse

class TrieTest {

    @Test
    fun `insert and search exact word`() {
        val trie = Trie()
        trie.insert("apple")
        assertTrue(trie.search("apple"))
        // A prefix is not a complete word unless explicitly inserted.
        assertFalse(trie.search("app"))
    }

    @Test
    fun `startsWith returns true for any inserted prefix`() {
        val trie = Trie()
        trie.insert("apple")
        assertTrue(trie.startsWith("app"))
        assertTrue(trie.startsWith("a"))
        assertTrue(trie.startsWith("apple"))
    }

    @Test
    fun `non-existent prefix returns false`() {
        val trie = Trie()
        trie.insert("apple")
        trie.insert("banana")
        assertFalse(trie.startsWith("cat"))
        assertFalse(trie.search("ban"))
        assertFalse(trie.search("apples"))
    }

    @Test
    fun `overlapping words coexist`() {
        // Inserting "app" then "apple" should make both searchable, and
        // both should be discoverable as prefixes.
        val trie = Trie()
        trie.insert("app")
        trie.insert("apple")
        trie.insert("applesauce")
        assertTrue(trie.search("app"))
        assertTrue(trie.search("apple"))
        assertTrue(trie.search("applesauce"))
        assertTrue(trie.startsWith("app"))
        assertTrue(trie.startsWith("appl"))
        assertFalse(trie.search("appl"))
    }
}
