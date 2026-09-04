package com.cjbooms.prep.stages.stage10

/**
 * Stage 10.4 — Prefix Sums.
 */

/**
 * Range Sum Query - Immutable (LC 303). Given an integer array that is fixed
 * at construction time, answer `sumRange(left, right)` queries returning the
 * sum of `numbers[left..=right]`. Each query must run in O(1) after an O(n)
 * build performed in the constructor.
 *
 * @param numbers the immutable input array; indices outside `[0,
 *                numbers.size)` must not be queried
 */
class ImmutableArraySum(private val numbers: IntArray) {

    /**
     * @param left 0-based inclusive left index
     * @param right 0-based inclusive right index; must satisfy `left <= right`
     * @return the sum of `numbers[left] + numbers[left + 1] + ... +
     *         numbers[right]`
     */
    fun sumRange(left: Int, right: Int): Int {
        TODO("implement")
    }
}

/**
 * Subarray Sum Equals K (LC 560). Count the number of contiguous (non-empty)
 * subarrays whose elements sum to exactly `k`. Elements may be negative or
 * zero.
 *
 * @param numbers the input array (may contain negative numbers and zeros);
 *                `numbers.length >= 1`
 * @param k the target subarray sum
 * @return the number of contiguous subarrays whose elements sum to `k`
 */
fun subarraySumEqualsK(numbers: IntArray, k: Int): Int {
    TODO("implement")
}
