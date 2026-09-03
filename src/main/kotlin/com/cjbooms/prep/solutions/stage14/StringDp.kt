package com.cjbooms.prep.solutions.stage14

/**
 * Stage 14.2 — String dynamic programming.
 *
 * MongoDB relevance: edit distance and LCS are how MongoDB Atlas
 * Search does fuzzy matching and how schema-migration tools diff
 * config documents. The classic 2D DP table is a workhorse.
 *
 * Structure-selection ritual:
 *   - 2D DP indexed by [i, j] over prefixes of the two strings.
 *   - Base row/column for empty prefix — get this right or every
 *     test fails.
 *   - Edit distance and LCS differ by one cell recurrence — say
 *     both aloud to the interviewer when they ask.
 *
 * Time budget: 15 min.
 */

/**
 * Length of the longest common subsequence of a and b.
 * dp[i][j] over (a[0..i), b[0..j)).
 * If a[i-1] == b[j-1]: dp[i][j] = dp[i-1][j-1] + 1.
 * Else:                dp[i][j] = max(dp[i-1][j], dp[i][j-1]).
 */
fun longestCommonSubsequence(a: String, b: String): Int {
    val m = a.length
    val n = b.length
    if (m == 0 || n == 0) return 0
    // Single-row rolling buffer keeps it O(min(m,n)) space.
    var prev = IntArray(n + 1)
    var curr = IntArray(n + 1)
    for (i in 1..m) {
        for (j in 1..n) {
            curr[j] = if (a[i - 1] == b[j - 1]) {
                prev[j - 1] + 1
            } else {
                maxOf(prev[j], curr[j - 1])
            }
        }
        val tmp = prev
        prev = curr
        curr = tmp
    }
    return prev[n]
}

/**
 * Levenshtein edit distance with uniform cost 1 for insert, delete,
 * replace. dp[i][j] = cost of transforming a[0..i) into b[0..j).
 * If last chars match: dp[i][j] = dp[i-1][j-1].
 * Else:                dp[i][j] = 1 + min(dp[i-1][j],   // delete
 *                                          dp[i][j-1],   // insert
 *                                          dp[i-1][j-1]) // replace
 */
fun editDistance(a: String, b: String): Int {
    val m = a.length
    val n = b.length
    if (m == 0) return n
    if (n == 0) return m
    var prev = IntArray(n + 1) { it } // distance from "" to b[0..j) = j
    var curr = IntArray(n + 1)
    for (i in 1..m) {
        curr[0] = i // distance from a[0..i) to "" = i
        for (j in 1..n) {
            curr[j] = if (a[i - 1] == b[j - 1]) {
                prev[j - 1]
            } else {
                1 + minOf(prev[j], curr[j - 1], prev[j - 1])
            }
        }
        val tmp = prev
        prev = curr
        curr = tmp
    }
    return prev[n]
}
