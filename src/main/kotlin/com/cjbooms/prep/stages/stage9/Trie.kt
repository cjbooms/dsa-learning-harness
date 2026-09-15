package com.cjbooms.prep.stages.stage9

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
        val wordLength = word.length

        fun processNode(node: TrieNode, index: Int) {
            val letter = word[index] - 'a'
            if (node.children[letter] == null) {
                node.children[letter] = TrieNode()
            }
            if (index == wordLength - 1) {
                node.children[letter]!!.isWord = true
                return
            } else {
                return processNode(node.children[letter]!!, index + 1)
            }
        }
        return processNode(root, 0)
    }



    /** Returns true iff [word] was previously inserted (full word, not just a prefix). */
    fun search(word: String): Boolean {
        val wordLength = word.length

        fun searchNode(node: TrieNode, index: Int): Boolean {
            val letter = word[index] - 'a'
            if (node.children[letter] == null) {
                return false
            }
            if (index == wordLength - 1) {
                return node.children[letter]?.isWord == true
            } else {
                return searchNode(node.children[letter]!!, index + 1)
            }
        }
        return searchNode(root, 0)

    }

    /** Returns true iff some previously inserted word starts with [prefix]. */
    fun startsWith(prefix: String): Boolean {
        val wordLength = prefix.length

        fun searchNode(node: TrieNode, index: Int): Boolean {
            val letter = prefix[index] - 'a'
            if (node.children[letter] == null) {
                return false
            }
            if (index == wordLength - 1) {
                return node.children[letter] != null
            } else {
                return searchNode(node.children[letter]!!, index + 1)
            }
        }
        return searchNode(root, 0)
    }
}


fun main() {
    val trie = Trie()
    trie.insert("cat")
    trie.insert("cab")
    trie.insert("dog")
    println("Expected true, Actual: " + trie.search("cat"))
    println("Expected false, Actual: " + trie.search("cabbage"))
    println("Expected true, Actual: " + trie.startsWith("ca"))
}
