package com.cjbooms.prep.solutions.stage9

/**
 * Stage 9.3 — Trie (prefix tree).
 *
 * Why this matters: full-text search relevance (autocomplete, prefix queries),
 * text index token lookups, command parsing (`db.collection.aggregate`
 * dispatched on prefix). The "startsWith" operation is the literal query
 * pattern.
 *
 * Structure-selection ritual:
 *   - Why a trie over a hashmap? Sharing prefixes saves space on large
 *     dictionaries and makes prefix enumeration O(prefix-length) instead
 *     of O(dictionary-size).
 *   - Children: Array<TrieNode?> sized to the alphabet (26 for lowercase
 *     a-z; faster, less allocation) vs HashMap<Char, TrieNode> (sparse,
 *     general). Pick Array when the alphabet is fixed and small.
 *   - End-of-word marker: a Boolean `isWord` flag on the node. Don't
 *     terminate strings with a sentinel child — that conflates "word
 *     ends here" with "branch ends here".
 *
 * Time budget: 20 min.
 */

private class TrieNode {
    // Fixed-size 26-element array for lowercase a-z. Faster than a HashMap
    // and no allocation churn on insert. `isWord` distinguishes "a word
    // ends at this node" from "we just pass through here".
    val children: Array<TrieNode?> = arrayOfNulls(26)
    var isWord: Boolean = false
}

class Trie {

    private val root = TrieNode()

    /** Inserts [word] into the trie. Lowercase a-z assumed; document the contract. */
    fun insert(word: String) {
        var current = root
        for (ch in word) {
            val index = ch - 'a'
            val next = current.children[index] ?: TrieNode().also { current.children[index] = it }
            current = next
        }
        current.isWord = true
    }

    /** Returns true iff [word] was previously inserted (full word, not just a prefix). */
    fun search(word: String): Boolean {
        val node = walkTo(word) ?: return false
        return node.isWord
    }

    /** Returns true iff some previously inserted word starts with [prefix]. */
    fun startsWith(prefix: String): Boolean {
        // A prefix matches if we can walk all its characters without falling
        // off the trie — whether or not the node itself is a word.
        return walkTo(prefix) != null
    }

    /** Walks the trie character by character; returns the node, or null if any step is missing. */
    private fun walkTo(text: String): TrieNode? {
        var current: TrieNode? = root
        for (ch in text) {
            current = current?.children[ch - 'a'] ?: return null
        }
        return current
    }
}
