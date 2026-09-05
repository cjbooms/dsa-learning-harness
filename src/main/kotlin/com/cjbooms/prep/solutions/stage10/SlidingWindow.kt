package com.cjbooms.prep.solutions.stage10

/**
 * Stage 10.2 — Sliding Window.
 *
 * MongoDB relevance: oplog tailing windows, change-stream resumability,
 * aggregation $setWindowFields, and time-bucketed counters in $bucket all
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
 * Time budget: 20 minutes. Three exercises.
 */

/**
 * Maximum sum of any contiguous subarray of length `windowSize`.
 *
 * Approach: compute the sum of the first windowSize elements, then slide one position
 * at a time — subtract the element leaving the window, add the one entering.
 * Track the maximum observed sum.
 *
 * Time:  O(n).
 * Space: O(1).
 */
fun maxSumSubarrayK(numbers: IntArray, windowSize: Int): Int {
    require(windowSize <= numbers.size) { "windowSize must not exceed array size" }
    var windowSum = 0
    for (index in 0 until windowSize) windowSum += numbers[index]
    var best = windowSum
    for (index in windowSize until numbers.size) {
        windowSum += numbers[index] - numbers[index - windowSize]
        if (windowSum > best) best = windowSum
    }
    return best
}

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
    val lastSeen = HashMap<Char, Int>()
    var best = 0
    var left = 0
    for ((right, character) in text.withIndex()) {
        val previous = lastSeen[character]
        if (previous != null && previous >= left) {
            left = previous + 1
        }
        lastSeen[character] = right
        val length = right - left + 1
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
    val need = HashMap<Char, Int>()
    for (character in pattern) need[character] = (need[character] ?: 0) + 1
    val window = HashMap<Char, Int>()
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
