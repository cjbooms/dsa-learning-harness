package com.cjbooms.prep.stages.stage14

/**
 * Stage 14.1 — Basic dynamic programming patterns.
 *
 * MongoDB relevance: query planner cost estimation walks the same
 * shape as classic DP recurrences — f(k) = f(k-1) + f(k-2) style
 * transitions are how a planner picks the cheapest access plan from
 * a DAG of alternative physical operators.
 *
 * Structure-selection ritual:
 *   - Is the subproblem linear (single index)? Memoize in a 1D array.
 *   - Memoization (top-down) vs tabulation (bottom-up)? Tabulation
 *     wins on stack safety for interview-time bounds.
 *   - When do you reduce space to O(1)? Only when the recurrence
 *     looks back a fixed k steps — say that aloud.
 *
 * Time budget: 15 min.
 */

/**
 * Number of distinct ways to climb n steps when you may take 1 or 2
 * at a time. f(k) = f(k-1) + f(k-2) with f(0) = 1, f(1) = 1.
 * Reduces to two rolling variables — O(1) space is fine because the
 * recurrence looks back at most 2 steps.
 */
fun climbStairs(n: Int): Int {
    TODO("implement")
}

/**
 * Max loot without robbing two adjacent houses.
 * f(k) = max(f(k-1), f(k-2) + nums[k]). Same recurrence shape as
 * climbStairs but with a max and the data mixed in.
 */
fun houseRobber(nums: List<Int>): Int {
    TODO("implement")
}
