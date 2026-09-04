package com.cjbooms.prep.stages.stage10

/**
 * Stage 10.4 — Prefix Sums.
 *
 * MongoDB relevance: range-sum acceleration in aggregation pipelines
 * ($densify, custom map-reduce, time-windowed metrics) and shard-balancing
 * scoring all precompute partial sums to turn an O(n) range query into O(1).
 *
 * Structure-selection ritual: when does a prefix-sum table win?
 *   - The underlying array is IMMUTABLE after construction (otherwise the
 *     table has to be rebuilt on every update — usually a deal-breaker).
 *   - Many range-sum queries follow on the same array.
 *   - You want O(1) per range query after an O(n) build.
 * If the array mutates, prefer a Fenwick tree (BIT) or segment tree.
 *
 * Time budget: 10 minutes.
 */

/**
 * Range Sum Query - Immutable (LC 303): pre-build a prefix-sum table once,
 * then answer each sumRange(left, right) in O(1).
 *
 * Approach: prefixSums[i] = sum of nums[0 ..< i]. Then
 *   sumRange(left, right) = prefixSums[right + 1] - prefixSums[left].
 *
 * Time:  O(n) build, O(1) per query.
 * Space: O(n) for the table.
 */
class ImmutableArraySum(private val numbers: IntArray) {
    fun sumRange(left: Int, right: Int): Int {
        TODO("implement")
    }
}

/**
 * Subarray Sum Equals K (LC 560): count the number of continuous subarrays
 * whose elements sum to `k`. Elements may be negative.
 *
 * Approach: as you scan, treat the running prefix sum as the key.
 * If prefixSum[j] - prefixSum[i] == k for some earlier i, then nums[i..<j]
 * sums to k. Count, for each j, how many earlier i's satisfy prefixSum[i] ==
 * prefixSum[j] - k. Hash map of prefix-sum -> frequency.
 *
 * Time:  O(n) average.
 * Space: O(n) for the map.
 *
 * NB: negatives are fine here because we are not relying on monotonicity —
 * the hashmap collapses all prefix sums with the same value, regardless of
 * where they occurred.
 */
fun subarraySumEqualsK(numbers: IntArray, k: Int): Int {
    TODO("implement")
}
