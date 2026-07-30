package com.cjbooms.prep.dsa

/**
 * Word Break (LC 139): can s be segmented into space-separated dictionary words?
 *
 * DP over prefixes: dp[i] = s[0..<i] segmentable. dp[i] = any dp[j] where
 * s[j..<i] in dict. O(n^2) substring checks.
 */
fun wordBreak(s: String, wordDict: List<String>): Boolean {
    val dict = wordDict.toSet()
    val dp = BooleanArray(s.length + 1)
    dp[0] = true
    for (i in 1..s.length) {
        for (j in 0..<i) {
            if (dp[j] && s.substring(j, i) in dict) {
                dp[i] = true
                break
            }
        }
    }
    return dp[s.length]
}
