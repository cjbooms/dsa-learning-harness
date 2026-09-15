package com.cjbooms.prep.solutions.stage8

import java.util.ArrayDeque

/**
 * Stage 8.5.2 — Alien dictionary: infer character ordering from a sorted
 * alien word list.
 *
 * Why this matters: collation rules for non-default locales, sort
 * comparators for custom indexes, ordering discovery between custom types.
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
 * Estimated time: 15 min. Defend aloud: why topological sort, not just pairwise
 * comparisons? (Pairwise only gives local info; the graph captures global
 * ordering and exposes cycles.)
 */
fun alienOrder(words: List<String>): String {
    if (words.isEmpty()) return ""

    // Adjacency: u -> set of v's ("u must come before v").
    val adj = hashMapOf<Char, MutableSet<Char>>()
    // All characters seen anywhere in the input, in first-occurrence order.
    val allChars = linkedSetOf<Char>()
    for (word in words) {
        for (char in word) {
            if (allChars.add(char)) {
                adj.getOrPut(char) { mutableSetOf() }
            }
        }
    }

    for (index in 0 until words.size - 1) {
        val current = words[index]
        val next = words[index + 1]
        // Invalid prefix: a longer word cannot come before its own prefix.
        if (current.length > next.length && current.startsWith(next)) return ""
        // Walk both words until the first differing character.
        val minLen = minOf(current.length, next.length)
        for (charIndex in 0 until minLen) {
            if (current[charIndex] != next[charIndex]) {
                val from = current[charIndex]
                val to = next[charIndex]
                adj.getOrPut(from) { mutableSetOf() }.add(to)
                break
            }
        }
    }

    // Kahn's BFS: seed the queue with every indegree-zero character, then peel
    // them off while decrementing neighbours' indegrees.
    val inDegree = hashMapOf<Char, Int>()
    for (char in allChars) inDegree[char] = 0
    for ((_, neighbours) in adj) {
        for (v in neighbours) inDegree[v] = inDegree.getOrDefault(v, 0) + 1
    }

    // Sort the ready queue by first-occurrence position so the topo order is
    // deterministic regardless of hash-iteration order on the JVM.
    val readyOrder = allChars.filter { inDegree[it] == 0 }
    val ready = ArrayDeque<Char>(readyOrder)

    val result = StringBuilder()
    while (ready.isNotEmpty()) {
        val char = ready.removeFirst()
        result.append(char)
        for (nextChar in adj[char].orEmpty()) {
            val remaining = inDegree[nextChar]!! - 1
            inDegree[nextChar] = remaining
            if (remaining == 0) ready.addLast(nextChar)
        }
    }

    // Cycle => not all characters emitted.
    return if (result.length == allChars.size) result.toString() else ""
}

fun main() {
    data class Test(val case: String, val expected: String, val actual: String) {
        init {
            if (expected != actual) println("FAILED: $this")
            else println("PASSED: $this")
        }
    }

    // Classic ordering: "wrt" -> "wrf" gives t < f; "wrf" -> "er" gives w < e;
    // "er" -> "ett" gives r < t; "ett" -> "rftc" gives e < r. First-occurrence
    // order over all characters is w, r, t, f, e, c. Constraints force
    // w < e < r < t < f; 'c' has no constraints so it lands right after w
    // in the deterministic BFS order: "wcertf".
    Test(
        case = "Classic example produces a valid ordering",
        expected = "wcertf",
        actual = alienOrder(listOf("wrt", "wrf", "er", "ett", "rftc")),
    )

    // Cyclic ordering: a < b < c < a — no consistent alphabet exists.
    Test(
        case = "Cyclic ordering returns empty string",
        expected = "",
        actual = alienOrder(listOf("a", "b", "c", "a")),
    )

    // Invalid prefix: "abc" before "ab" cannot happen in any alphabet.
    Test(
        case = "Invalid prefix ordering returns empty string",
        expected = "",
        actual = alienOrder(listOf("abc", "ab")),
    )

    // Single-word input: no constraints, every distinct letter is fine; output
    // follows first-occurrence order, so "abc".
    Test(
        case = "Single word yields its distinct characters in order",
        expected = "abc",
        actual = alienOrder(listOf("abc")),
    )

    // Duplicate adjacent words contribute no new constraint.
    Test(
        case = "Duplicate words do not break the ordering",
        expected = "ab",
        actual = alienOrder(listOf("a", "a", "b")),
    )
}
