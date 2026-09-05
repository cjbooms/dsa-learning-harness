package com.cjbooms.prep.solutions.stage14

/**
 * Stage 14.3 — Word Break (15 min).
 *
 * MongoDB relevance: Atlas Search autocomplete, query-token segmentation,
 * and schema-migration tokenization all reduce to "can this string be split
 * into known tokens?".
 *
 * Structure-selection ritual:
 *   - Brute-force backtracking: O(2^n) — too slow for interview-length strings.
 *   - DP on prefix: dp[i] = true if s[0..i) can be segmented. For each j < i,
 *     if dp[j] and s[j..i) is in the dictionary, dp[i] = true. O(n * m) where
 *     m = max word length, or O(n^2) if you scan all prefixes.
 *   - Word Break II: keep the actual sentences, not just booleans. Same DP
 *     shape, but each cell stores the list of valid segmentations for that prefix.
 *
 * Time budget: 15 min.
 */

fun wordBreak(s: String, wordDict: Set<String>): Boolean {
    val length = s.length
    val dp = BooleanArray(length + 1)
    dp[0] = true
    val maxLen = wordDict.maxOfOrNull { it.length } ?: 0

    for (endIndex in 1..length) {
        for (startIndex in (endIndex - maxLen).coerceAtLeast(0) until endIndex) {
            if (dp[startIndex] && s.substring(startIndex, endIndex) in wordDict) {
                dp[endIndex] = true
                break
            }
        }
    }
    return dp[length]
}

fun wordBreakIi(s: String, wordDict: Set<String>): List<String> {
    if (s.isEmpty()) return emptyList()
    val length = s.length
    val dp = Array(length + 1) { mutableListOf<String>() }
    dp[0].add("")
    val maxLen = wordDict.maxOfOrNull { it.length } ?: 0

    for (endIndex in 1..length) {
        for (startIndex in (endIndex - maxLen).coerceAtLeast(0) until endIndex) {
            val word = s.substring(startIndex, endIndex)
            if (word in wordDict && dp[startIndex].isNotEmpty()) {
                for (prefix in dp[startIndex]) {
                    dp[endIndex].add(if (prefix.isEmpty()) word else "$prefix $word")
                }
            }
        }
    }
    return dp[length]
}
