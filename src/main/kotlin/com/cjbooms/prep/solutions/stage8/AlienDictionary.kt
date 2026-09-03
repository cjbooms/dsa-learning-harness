package com.cjbooms.prep.solutions.stage8

import java.util.ArrayDeque

/**
 * Stage 8.5.2 — Alien dictionary: infer character ordering from a sorted
 * alien word list.
 *
 * Why this matters for MongoDB: collation rules for non-default locales, sort
 * comparators for custom indexes, ordering discovery between custom types.
 * The interview form: "given a dictionary of words in an unknown alphabet,
 * derive the alphabet order (or detect an invalid dictionary)".
 *
 * Structure-selection ritual:
 *   - Compare adjacent words left-to-right; the first differing character
 *     gives an edge u -> v ("u comes before v").
 *   - Run topological sort on the resulting graph.
 *   - Edge cases:
 *       * Duplicate adjacent words: fine, no new info.
 *       * Prefix relationship where later word is SHORTER and a prefix of
 *         the earlier word (e.g. ["abc", "ab"]): INVALID dictionary.
 *       * Disconnected components: alphabetical order is still meaningful as
 *         long as no cycle exists.
 *   - Return empty string ("") for invalid / cyclic dictionaries.
 *
 * Time budget: 15 min. Defend aloud: why topological sort, not just pairwise
 * comparisons? (Pairwise only gives local info; the graph captures global
 * ordering and exposes cycles.)
 */
fun alienOrder(words: List<String>): String {
    if (words.isEmpty()) return ""
    if (words.size == 1) {
        // No pairwise constraints; return the word's distinct characters as-is.
        return words[0].toSet().joinToString("")
    }

    // Adjacency: u -> set of v's ("u must come before v").
    val adj = mutableMapOf<Char, MutableSet<Char>>()
    // Track all characters that appear in any word.
    val allChars = mutableSetOf<Char>()
    for (w in words) {
        for (c in w) {
            allChars.add(c)
            adj.getOrPut(c) { mutableSetOf() }
        }
    }

    for (i in 0 until words.size - 1) {
        val a = words[i]
        val b = words[i + 1]
        // Invalid prefix: longer word that starts with a SHORTER next word.
        if (a.length > b.length && a.startsWith(b)) return ""
        // Find the first differing character to derive one ordering constraint.
        val minLen = minOf(a.length, b.length)
        var foundDiff = false
        for (k in 0 until minLen) {
            if (a[k] != b[k]) {
                val from = a[k]
                val to = b[k]
                // Skip duplicate edges — set semantics dedupe.
                adj.getOrPut(from) { mutableSetOf() }.add(to)
                foundDiff = true
                break
            }
        }
        // If all minLen characters match and the shorter is a prefix, that's
        // already covered above; if no diff and same length, it's a duplicate —
        // no constraint, continue.
        if (!foundDiff && a.length == b.length) continue
    }

    // Kahn's BFS over character graph.
    val inDegree = mutableMapOf<Char, Int>()
    for (c in allChars) inDegree[c] = 0
    for ((_, neighbours) in adj) {
        for (v in neighbours) inDegree[v] = inDegree.getOrDefault(v, 0) + 1
    }

    val ready = ArrayDeque<Char>()
    for ((c, d) in inDegree) if (d == 0) ready.addLast(c)

    val result = StringBuilder()
    while (ready.isNotEmpty()) {
        val c = ready.removeFirst()
        result.append(c)
        for (next in adj[c].orEmpty()) {
            val remaining = inDegree[next]!! - 1
            inDegree[next] = remaining
            if (remaining == 0) ready.addLast(next)
        }
    }

    // Cycle => not all characters emitted.
    return if (result.length == allChars.size) result.toString() else ""
}
