package com.cjbooms.prep.solutions.stage10

/**
 * Stage 10.2 — Sliding Window.
 *
 * Why this matters: operation log tailing windows, event stream resumability,
 * aggregation windowed aggregation, and time-bucketed counters in time-bucketed grouping all
 * reduce to "maintain state over the last K elements of a stream". The
 * hit-counter and rate-limiter stages are sliding-window siblings.
 *
 * Structure-selection ritual: when does a sliding window win?
 *   - You need a contiguous range of elements with a property
 *     (sum / length / "all distinct").
 *   - You can update the window in O(1) when you slide one step.
 *   - You want O(n) instead of O(n*k) brute force.
 * If the window size is fixed, the variant is a fixed-size rolling aggregate.
 * If the window size varies, you typically need two pointers with a
 * validity check and a "shrink until valid" inner loop.
 *
 * Estimated time: 20 minutes. Two exercises.
 */

/**
 * Longest substring without repeating characters (LC 3).
 *
 * Approach: variable window with a "last seen index" map. Expand `right`.
 * If `text[right]` is already inside the window, jump `left` past its previous
 * occurrence. Track the maximum window length seen.
 *
 * Time:  O(n) — each index is visited at most twice.
 * Space: O(min(n, alphabet)) for the last-seen map.
 */
fun longestSubstringWithoutRepeats(text: String): Int {
    val lastSeen = hashMapOf<Char, Int>()
    var best = 0
    var leftIndex = 0
    for ((rightIndex, character) in text.withIndex()) {
        val previouslySeenAtIndex = lastSeen[character]
        // if char is already inside the window, jump left past its old slot
        if (previouslySeenAtIndex != null && previouslySeenAtIndex >= leftIndex) {
            leftIndex = previouslySeenAtIndex + 1
        }
        lastSeen[character] = rightIndex
        val length = rightIndex - leftIndex + 1
        if (length > best) best = length
    }
    return best
}

/**
 * Minimum Window Substring (LC 76): shortest substring of `text` that contains
 * every character of `pattern` (including multiplicities). Return "" if none exists.
 *
 * Approach: variable window with a "need vs have" counter. Expand `right`
 * until the window satisfies the requirement, then shrink `left` as far as
 * possible while still satisfying it, recording the best window. Repeat.
 *
 * Time:  O(n + |pattern|).
 * Space: O(|pattern|) for the frequency tables (capped at the alphabet in practice).
 */
fun minWindowSubstring(text: String, pattern: String): String {
    if (pattern.isEmpty() || text.length < pattern.length) return ""

    val need = hashMapOf<Char, Int>()
    for (character in pattern) need[character] = (need[character] ?: 0) + 1

    val window = hashMapOf<Char, Int>()
    var have = 0
    val required = need.size
    var left = 0
    var bestLeft = 0
    var bestLen = Int.MAX_VALUE

    for (right in text.indices) {
        val rightChar = text[right]
        if (rightChar in need) {
            window[rightChar] = (window[rightChar] ?: 0) + 1
            if (window[rightChar] == need[rightChar]) have++
        }

        // shrink from the left while the window still satisfies the pattern
        while (have == required) {
            val length = right - left + 1
            if (length < bestLen) {
                bestLen = length
                bestLeft = left
            }
            val leftChar = text[left]
            if (leftChar in need) {
                val count = window[leftChar]!! - 1
                window[leftChar] = count
                if (count < need[leftChar]!!) have--
            }
            left++
        }
    }

    return if (bestLen == Int.MAX_VALUE) "" else text.substring(bestLeft, bestLeft + bestLen)
}

fun main() {
    data class Test(val case: String, val expected: String, val actual: String) {
        init {
            if (expected == actual) println("PASSED: $this")
            else println("FAILED: $this")
        }
    }

    // longestSubstringWithoutRepeats: happy path, all-unique, empty, repeat-shrink
    Test(
        case = "Longest substring with repeats - classic",
        expected = "3",
        actual = longestSubstringWithoutRepeats("abcabcbb").toString()
    )
    Test(
        case = "Longest substring all unique",
        expected = "5",
        actual = longestSubstringWithoutRepeats("abcde").toString()
    )
    Test(
        case = "Longest substring empty input",
        expected = "0",
        actual = longestSubstringWithoutRepeats("").toString()
    )
    Test(
        case = "Longest substring with whitespace and symbols",
        expected = "4",
        actual = longestSubstringWithoutRepeats("pwwkew ").toString()
    )

    // minWindowSubstring: happy path, no match, exact match, empty pattern
    Test(
        case = "Min window substring happy path",
        expected = "BANC",
        actual = minWindowSubstring("ADOBECODEBANC", "ABC")
    )
    Test(
        case = "Min window substring no match",
        expected = "",
        actual = minWindowSubstring("a", "aa")
    )
    Test(
        case = "Min window substring exact match",
        expected = "a",
        actual = minWindowSubstring("a", "a")
    )
    Test(
        case = "Min window substring empty pattern",
        expected = "",
        actual = minWindowSubstring("hello", "")
    )
}
