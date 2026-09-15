package com.cjbooms.prep.solutions.stage14

/**
 * Stage 14.3 — Interval dynamic programming.
 *
 * Why this matters: query planner cost for nested-loop joins is
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
 * Estimated time: 15 min.
 */

/**
 * Minimum scalar multiplication cost for parenthesising the chain
 * A_1 * A_2 * ... * A_n where A_i is dimensions[i-1] x dimensions[i].
 * dp[i][j] = min over split k in [i, j) of dp[i][k] + dp[k+1][j]
 *                                          + dimensions[i-1]*dimensions[k]*dimensions[j].
 * O(n^3) time, O(n^2) space. n < 2 -> zero cost (nothing to multiply).
 */
fun matrixChainOrder(dimensions: IntArray): Int {
    val matrixCount = dimensions.size - 1 // number of matrices
    if (matrixCount < 2) return 0
    val dp = Array(matrixCount) { IntArray(matrixCount) }
    // len is the chain length we are solving for; start at 2.
    for (len in 2..matrixCount) {
        for (startIndex in 0..(matrixCount - len)) {
            val endIndex = startIndex + len - 1
            var best = Int.MAX_VALUE
            for (splitIndex in startIndex until endIndex) {
                val cost = dp[startIndex][splitIndex] + dp[splitIndex + 1][endIndex] +
                    dimensions[startIndex] * dimensions[splitIndex + 1] * dimensions[endIndex + 1]
                if (cost < best) best = cost
            }
            dp[startIndex][endIndex] = best
        }
    }
    return dp[0][matrixCount - 1]
}

/**
 * Max coins from bursting all balloons in nums. Burst balloon k last
 * on interval (l, r) (exclusive, sentinel-bounded) to get
 * nums[l]*nums[k]*nums[r] + dp[l][k] + dp[k][r].
 * Virtual 1-sentinels bookend the array so the last burst is always
 * well-defined. O(n^3) time, O(n^2) space.
 */
fun maxCoinsBurst(nums: IntArray): Int {
    val balloonCount = nums.size
    if (balloonCount == 0) return 0
    // Pad with virtual 1-sentinels; padded indices 0..n+1 inclusive.
    val padded = IntArray(balloonCount + 2).also {
        it[0] = 1
        it[balloonCount + 1] = 1
        for (index in 0 until balloonCount) it[index + 1] = nums[index]
    }
    // dp[leftIndex][rightIndex] where leftIndex, rightIndex are positions in `padded` (inclusive bounds).
    // Size must be (n+2) x (n+2) so the trailing sentinel index n+1 is valid.
    val size = balloonCount + 2
    val dp = Array(size) { IntArray(size) }
    // len = number of balloons enclosed between leftIndex and rightIndex (exclusive of leftIndex, rightIndex).
    // With len real balloons inside (leftIndex, rightIndex), rightIndex = leftIndex + len + 1.
    for (len in 1..balloonCount) {
        for (leftIndex in 0..(size - 1 - len - 1)) {
            val rightIndex = leftIndex + len + 1
            var best = 0
            for (burstIndex in (leftIndex + 1) until rightIndex) {
                val coins = dp[leftIndex][burstIndex] + dp[burstIndex][rightIndex] +
                    padded[leftIndex] * padded[burstIndex] * padded[rightIndex]
                if (coins > best) best = coins
            }
            dp[leftIndex][rightIndex] = best
        }
    }
    return dp[0][size - 1]
}
