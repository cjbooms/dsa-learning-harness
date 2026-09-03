package com.cjbooms.prep.stages.stage10

/**
 * Stage 10.1 — Two Pointers.
 *
 * MongoDB relevance: query planners scan sorted index ranges with two-pointer
 * walks (merge-join in $lookup, sorted union in find().sort(), aggregation
 * pipeline stages that merge pre-sorted shards). Two-pointer also shows up in
 * range intersection for index intersection planning.
 *
 * Structure-selection ritual: when is two-pointer the right hammer?
 *   - The data is already sorted (or you can afford to sort once).
 *   - You are looking for pairs / a partition satisfying an order property.
 *   - O(n) after sorting beats O(n) hash space or O(n^2) nested loops.
 * If the array is unsorted and order is not invariant, fall back to hashmap
 * (single-pair sum) or sort first (k-sum).
 *
 * Time budget: 15 minutes. Three exercises, ~5 min each.
 */

/**
 * Pair sum in a sorted array: return the 0-based indices [i, j] of the two
 * numbers that add up to `target`, or null if no such pair exists.
 *
 * Approach: place one pointer at each end. If the sum is too small, advance
 * the left pointer (only way to increase the sum against a sorted array).
 * If too big, retreat the right pointer. Match returns the indices.
 *
 * Time:  O(n) after the array is sorted (no sort needed if input already sorted).
 * Space: O(1).
 */
fun pairSumSorted(numbers: IntArray, target: Int): IntArray? {
    TODO("implement")
}

/**
 * Container With Most Water (LC 11): given heights where each element is a
 * vertical line on the x-axis, find two lines that together with the x-axis
 * form a container holding the most water.
 *
 * Approach: greedy two-pointer. Start with the widest container (left=0,
 * right=n-1). The area is min(height[left], height[right]) * (right - left).
 * Move the pointer at the SHORTER line inward — only that move can possibly
 * increase the limiting height.
 *
 * Time:  O(n) — one pass.
 * Space: O(1).
 */
fun maxArea(heights: IntArray): Int {
    TODO("implement")
}

/**
 * Remove Duplicates from Sorted Array (LC 26): in-place dedup of a sorted
 * array, returning the number of unique elements. The first `k` entries of
 * the input should hold the unique values in their original order; the rest
 * is unspecified.
 *
 * Approach: write-pointer / read-pointer. `write` marks where the next unique
 * value belongs; `read` scans ahead. When `nums[read]` differs from the last
 * written value, copy it forward.
 *
 * Time:  O(n).
 * Space: O(1) — in-place.
 */
fun removeDuplicatesSorted(numbers: IntArray): Int {
    TODO("implement")
}
