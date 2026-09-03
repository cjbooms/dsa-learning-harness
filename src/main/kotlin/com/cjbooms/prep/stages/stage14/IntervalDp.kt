package com.cjbooms.prep.stages.stage14

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
    TODO("implement")
}

/**
 * Max coins from bursting all balloons in nums. Burst balloon k last
 * on interval (l, r) (exclusive, sentinel-bounded) to get
 * nums[l]*nums[k]*nums[r] + dp[l][k] + dp[k][r].
 * Virtual 1-sentinels bookend the array so the last burst is always
 * well-defined. O(n^3) time, O(n^2) space.
 */
fun maxCoinsBurst(nums: IntArray): Int {
    TODO("implement")
}
