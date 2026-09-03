package com.cjbooms.prep.solutions.stage14

/**
 * Stage 14.3 — Interval dynamic programming.
 *
 * MongoDB relevance: query planner cost for nested-loop joins is
 * interval DP in disguise — the optimal ordering of joins over
 * k relations is matrix-chain-multiplication. Worth being able to
 * sketch on a whiteboard.
 *
 * Structure-selection ritual:
 *   - dp[i][j] over the interval [i, j] of "things" (matrices,
 *     balloons).
 *   - Enumerate the split point k in [i, j) and combine left +
 *     right + the cost of merging at k.
 *   - O(n^3) is expected; say it aloud before they ask. Greedy
 *     fails here — that is the whole point of interval DP.
 *
 * Time budget: 15 min.
 */

/**
 * Minimum scalar multiplication cost for parenthesising the chain
 * A_1 * A_2 * ... * A_n where A_i is dimensions[i-1] x dimensions[i].
 * dp[i][j] = min over split k in [i, j) of dp[i][k] + dp[k+1][j]
 *                                          + dimensions[i-1]*dimensions[k]*dimensions[j].
 * O(n^3) time, O(n^2) space. n < 2 -> zero cost (nothing to multiply).
 */
fun matrixChainOrder(dimensions: IntArray): Int {
    val n = dimensions.size - 1 // number of matrices
    if (n < 2) return 0
    val dp = Array(n) { IntArray(n) }
    // Length is the chain length we are solving for; start at 2.
    for (len in 2..n) {
        for (i in 0..(n - len)) {
            val j = i + len - 1
            var best = Int.MAX_VALUE
            for (k in i until j) {
                val cost = dp[i][k] + dp[k + 1][j] +
                    dimensions[i] * dimensions[k + 1] * dimensions[j + 1]
                if (cost < best) best = cost
            }
            dp[i][j] = best
        }
    }
    return dp[0][n - 1]
}

/**
 * Max coins from bursting all balloons in nums. Burst balloon k last
 * on interval (l, r) (exclusive, sentinel-bounded) to get
 * nums[l]*nums[k]*nums[r] + dp[l][k] + dp[k][r].
 * Virtual 1-sentinels bookend the array so the last burst is always
 * well-defined. O(n^3) time, O(n^2) space.
 */
fun maxCoinsBurst(nums: IntArray): Int {
    val n = nums.size
    if (n == 0) return 0
    // Pad with virtual 1-sentinels; padded indices 0..n+1 inclusive.
    val padded = IntArray(n + 2).also {
        it[0] = 1
        it[n + 1] = 1
        for (i in 0 until n) it[i + 1] = nums[i]
    }
    // dp[l][r] where l, r are positions in `padded` (inclusive bounds).
    // Size must be (n+2) x (n+2) so the trailing sentinel index n+1 is valid.
    val size = n + 2
    val dp = Array(size) { IntArray(size) }
    // len = number of balloons enclosed between l and r (exclusive of l, r).
    // With len real balloons inside (l, r), r = l + len + 1.
    for (len in 1..n) {
        for (l in 0..(size - 1 - len - 1)) {
            val r = l + len + 1
            var best = 0
            for (k in (l + 1) until r) {
                val coins = dp[l][k] + dp[k][r] +
                    padded[l] * padded[k] * padded[r]
                if (coins > best) best = coins
            }
            dp[l][r] = best
        }
    }
    return dp[0][size - 1]
}
