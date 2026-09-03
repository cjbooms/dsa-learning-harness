package com.cjbooms.prep.stages.stage14

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

/**
 * Returns true if [s] can be segmented into a sequence of one or more
 * dictionary words.
 */
fun wordBreak(s: String, wordDict: Set<String>): Boolean {
    TODO("implement")
}

/**
 * Returns all possible ways to segment [s] into dictionary words.
 * Each result string joins the words with a single space.
 */
fun wordBreakIi(s: String, wordDict: Set<String>): List<String> {
    TODO("implement")
}
