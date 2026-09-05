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
fun longestCommonSubsequence(first: String, second: String): Int {
    val lengthA = first.length
    val lengthB = second.length
    if (lengthA == 0 || lengthB == 0) return 0
    // Single-row rolling buffer keeps it O(min(lengthA,lengthB)) space.
    var prev = IntArray(lengthB + 1)
    var curr = IntArray(lengthB + 1)
    for (indexA in 1..lengthA) {
        for (indexB in 1..lengthB) {
            curr[indexB] = if (first[indexA - 1] == second[indexB - 1]) {
                prev[indexB - 1] + 1
            } else {
                maxOf(prev[indexB], curr[indexB - 1])
            }
        }
        val tmp = prev
        prev = curr
        curr = tmp
    }
    return prev[lengthB]
}

/**
 * Levenshtein edit distance with uniform cost 1 for insert, delete,
 * replace. dp[i][j] = cost of transforming a[0..i) into b[0..j).
 * If last chars match: dp[i][j] = dp[i-1][j-1].
 * Else:                dp[i][j] = 1 + min(dp[i-1][j],   // delete
 *                                          dp[i][j-1],   // insert
 *                                          dp[i-1][j-1]) // replace
 */
fun editDistance(first: String, second: String): Int {
    val lengthA = first.length
    val lengthB = second.length
    if (lengthA == 0) return lengthB
    if (lengthB == 0) return lengthA
    var prev = IntArray(lengthB + 1) { it } // distance from "" to second[0..j) = j
    var curr = IntArray(lengthB + 1)
    for (indexA in 1..lengthA) {
        curr[0] = indexA // distance from first[0..i) to "" = i
        for (indexB in 1..lengthB) {
            curr[indexB] = if (first[indexA - 1] == second[indexB - 1]) {
                prev[indexB - 1]
            } else {
                1 + minOf(prev[indexB], curr[indexB - 1], prev[indexB - 1])
            }
        }
        val tmp = prev
        prev = curr
        curr = tmp
    }
    return prev[lengthB]
}
