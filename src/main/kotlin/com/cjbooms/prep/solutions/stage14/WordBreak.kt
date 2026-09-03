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
    val n = s.length
    val dp = BooleanArray(n + 1)
    dp[0] = true
    val maxLen = wordDict.maxOfOrNull { it.length } ?: 0

    for (i in 1..n) {
        for (j in (i - maxLen).coerceAtLeast(0) until i) {
            if (dp[j] && s.substring(j, i) in wordDict) {
                dp[i] = true
                break
            }
        }
    }
    return dp[n]
}

fun wordBreakIi(s: String, wordDict: Set<String>): List<String> {
    if (s.isEmpty()) return emptyList()
    val n = s.length
    val dp = Array(n + 1) { mutableListOf<String>() }
    dp[0].add("")
    val maxLen = wordDict.maxOfOrNull { it.length } ?: 0

    for (i in 1..n) {
        for (j in (i - maxLen).coerceAtLeast(0) until i) {
            val word = s.substring(j, i)
            if (word in wordDict && dp[j].isNotEmpty()) {
                for (prefix in dp[j]) {
                    dp[i].add(if (prefix.isEmpty()) word else "$prefix $word")
                }
            }
        }
    }
    return dp[n]
}
