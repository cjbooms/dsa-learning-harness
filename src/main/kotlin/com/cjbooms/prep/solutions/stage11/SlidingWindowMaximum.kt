package com.cjbooms.prep.solutions.stage11

import java.util.ArrayDeque

/**
 * Stage 11.5 — Sliding window maximum (25 min).
 *
 * Why this matters for MongoDB: rolling max over streaming metrics (oplog
 * throughput, replication lag, cache hit rate), and windowed aggregations where
 * you need the peak in every fixed-size interval.
 *
 * Structure-selection ritual:
 *   - Brute force: scan each window -> O(n*k).
 *   - Max-heap of window values: O(n log k), but stale elements (indices that
 *     left the window) are expensive to evict.
 *   - Monotonic deque of INDICES: values decrease from front to back. The front
 *     is always the current window maximum. O(n) total.
 *
 * Invariant: for indices in the deque, `nums[deque[0]]` is the max of the
 * current window, and values increase as you move from the back to the front.
 * Pop the back while the incoming value is larger; pop the front when it falls
 * out of the window.
 *
 * Time budget: 25 min.
 */
fun maxSlidingWindow(nums: IntArray, k: Int): IntArray {
    require(k > 0) { "window size must be positive, was $k" }
    if (nums.isEmpty()) return intArrayOf()

    val deque: ArrayDeque<Int> = ArrayDeque()
    val result = IntArray(nums.size - k + 1)
    var outputIndex = 0

    for (i in nums.indices) {
        // Discard indices that slid out of the window.
        if (deque.isNotEmpty() && deque.first() <= i - k) {
            deque.removeFirst()
        }

        // Maintain decreasing values: incoming num is larger than back -> back can never be a max.
        while (deque.isNotEmpty() && nums[deque.last()] <= nums[i]) {
            deque.removeLast()
        }

        deque.addLast(i)

        // First window is complete at index k - 1.
        if (i >= k - 1) {
            result[outputIndex++] = nums[deque.first()]
        }
    }

    return result
}
