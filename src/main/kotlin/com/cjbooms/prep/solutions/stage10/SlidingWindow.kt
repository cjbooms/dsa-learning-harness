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
 * Maximum sum of any contiguous subarray of length `k`.
 *
 * Approach: compute the sum of the first k elements, then slide one position
 * at a time — subtract the element leaving the window, add the one entering.
 * Track the maximum observed sum.
 *
 * Time:  O(n).
 * Space: O(1).
 */
fun maxSumSubarrayK(numbers: IntArray, k: Int): Int {
    require(k <= numbers.size) { "k must not exceed array size" }
    var windowSum = 0
    for (i in 0 until k) windowSum += numbers[i]
    var best = windowSum
    for (i in k until numbers.size) {
        windowSum += numbers[i] - numbers[i - k]
        if (windowSum > best) best = windowSum
    }
    return best
}

/**
 * Longest substring without repeating characters (LC 3).
 *
 * Approach: variable window with a "last seen index" map. Expand `right`.
 * If `s[right]` is already inside the window, jump `left` past its previous
 * occurrence. Track the maximum window length seen.
 *
 * Time:  O(n) — each index is visited at most twice.
 * Space: O(min(n, alphabet)) for the last-seen map.
 */
fun longestSubstringWithoutRepeats(s: String): Int {
    val lastSeen = HashMap<Char, Int>()
    var best = 0
    var left = 0
    for ((right, c) in s.withIndex()) {
        val prev = lastSeen[c]
        if (prev != null && prev >= left) {
            left = prev + 1
        }
        lastSeen[c] = right
        val len = right - left + 1
        if (len > best) best = len
    }
    return best
}

/**
 * Minimum Window Substring (LC 76): shortest substring of `s` that contains
 * every character of `t` (including multiplicities). Return "" if none exists.
 *
 * Approach: variable window with a "need vs have" counter. Expand `right`
 * until the window satisfies the requirement, then shrink `left` as far as
 * possible while still satisfying it, recording the best window. Repeat.
 *
 * Time:  O(n + |t|).
 * Space: O(|t|) for the frequency tables (capped at the alphabet in practice).
 */
fun minWindowSubstring(s: String, t: String): String {
    if (t.isEmpty() || s.length < t.length) return ""
    val need = HashMap<Char, Int>()
    for (c in t) need[c] = (need[c] ?: 0) + 1
    val window = HashMap<Char, Int>()
    var have = 0
    val required = need.size
    var left = 0
    var bestLeft = 0
    var bestLen = Int.MAX_VALUE
    for (right in s.indices) {
        val rc = s[right]
        if (rc in need) {
            window[rc] = (window[rc] ?: 0) + 1
            if (window[rc] == need[rc]) have++
        }
        while (have == required) {
            val len = right - left + 1
            if (len < bestLen) {
                bestLen = len
                bestLeft = left
            }
            val lc = s[left]
            if (lc in need) {
                val cnt = window[lc]!! - 1
                window[lc] = cnt
                if (cnt < need[lc]!!) have--
            }
            left++
        }
    }
    return if (bestLen == Int.MAX_VALUE) "" else s.substring(bestLeft, bestLeft + bestLen)
}
