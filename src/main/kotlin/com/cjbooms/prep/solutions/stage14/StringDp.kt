package com.cjbooms.prep.solutions.stage14

/**
 * Learn first: see docs/learning-resources.md
 * Computes the length of the longest sequence of characters that appears in
 * both [a] and [b] in the same relative order (not necessarily contiguous).
 *
 * @param a the first string
 * @param b the second string
 * @return the length of the longest common subsequence of [a] and [b]
 */
fun longestCommonSubsequence(a: String, b: String): Int {
    val lengthA = a.length
    val lengthB = b.length
    if (lengthA == 0 || lengthB == 0) return 0
    // dp[i][j] = length of LCS over a[0..i) and b[0..j); empty prefix -> 0
    val dp = Array(lengthA + 1) { IntArray(lengthB + 1) }
    for (i in 1..lengthA) {
        for (j in 1..lengthB) {
            dp[i][j] = if (a[i - 1] == b[j - 1]) {
                dp[i - 1][j - 1] + 1
            } else {
                maxOf(dp[i - 1][j], dp[i][j - 1])
            }
        }
    }
    return dp[lengthA][lengthB]
}

/**
 * Computes the Levenshtein edit distance between [a] and [b] using uniform
 * cost 1 for insertion, deletion, and substitution of a single character.
 *
 * @param a the source string
 * @param b the target string
 * @return the minimum number of single-character edits required to transform
 *   [a] into [b]
 */
fun editDistance(a: String, b: String): Int {
    val lengthA = a.length
    val lengthB = b.length
    if (lengthA == 0) return lengthB
    if (lengthB == 0) return lengthA
    // dp[i][j] = min edits to turn a[0..i) into b[0..j); base row = j deletes, base col = i inserts
    val dp = Array(lengthA + 1) { IntArray(lengthB + 1) }
    for (j in 0..lengthB) dp[0][j] = j
    for (i in 1..lengthA) {
        dp[i][0] = i
        for (j in 1..lengthB) {
            dp[i][j] = if (a[i - 1] == b[j - 1]) {
                dp[i - 1][j - 1]
            } else {
                1 + minOf(dp[i - 1][j], dp[i][j - 1], dp[i - 1][j - 1])
            }
        }
    }
    return dp[lengthA][lengthB]
}

fun main() {
    data class Test(val case: String, val expected: Int, val actual: Int) {
        init {
            if (expected != actual) println("FAILED: $this")
            else println("PASSED: $this")
        }
    }

    Test("LCS of empty against anything is zero", 0, longestCommonSubsequence("", "abc"))
    Test("LCS identical strings equals length", 3, longestCommonSubsequence("abc", "abc"))
    Test("LCS classic example is three", 3, longestCommonSubsequence("abcde", "ace"))
    Test("LCS of disjoint strings is zero", 0, longestCommonSubsequence("abc", "xyz"))

    Test("Edit distance empty to non-empty is insert count", 3, editDistance("", "abc"))
    Test("Edit distance non-empty to empty is delete count", 3, editDistance("abc", ""))
    Test("Edit distance identical strings is zero", 0, editDistance("kitten", "kitten"))
    Test("Edit distance classic kitten/sitting is three", 3, editDistance("kitten", "sitting"))
    Test("Edit distance single substitution is one", 1, editDistance("flaw", "lawn"))
}
