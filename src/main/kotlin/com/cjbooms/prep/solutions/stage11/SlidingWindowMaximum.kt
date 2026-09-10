package com.cjbooms.prep.solutions.stage11

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
fun maxSlidingWindow(nums: IntArray, windowSize: Int): IntArray {
    if (windowSize <= 0) return intArrayOf()
    if (nums.isEmpty()) return intArrayOf()

    val deque = ArrayDeque<Int>()
    val result = IntArray(nums.size - windowSize + 1)
    var outputIndex = 0

    for (index in nums.indices) {
        // Drop indices that have slid out of the window.
        if (deque.isNotEmpty() && deque.first() <= index - windowSize) {
            deque.removeFirst()
        }

        // Maintain decreasing values: incoming value is larger, so anything
        // smaller in the back can never become a max for a future window.
        while (deque.isNotEmpty() && nums[deque.last()] <= nums[index]) {
            deque.removeLast()
        }

        deque.addLast(index)

        // First complete window is at index windowSize - 1.
        if (index >= windowSize - 1) {
            result[outputIndex++] = nums[deque.first()]
        }
    }

    return result
}

fun main() {
    data class Test(val case: String, val expected: IntArray, val actual: IntArray) {
        init {
            if (!expected.contentEquals(actual)) println("FAILED: $this")
            else println("PASSED: $this")
        }
    }

    // standard LeetCode example
    Test(
        "basic sliding window",
        intArrayOf(3, 3, 5, 5, 6, 7),
        maxSlidingWindow(intArrayOf(1, 3, -1, -3, 5, 3, 6, 7), 3),
    )

    // window of size 1 returns each element as its own window max
    Test(
        "window size one",
        intArrayOf(9, 11),
        maxSlidingWindow(intArrayOf(9, 11), 1),
    )

    // monotonic decreasing input: front of deque stays the running max
    Test(
        "strictly decreasing",
        intArrayOf(5, 4, 3, 2),
        maxSlidingWindow(intArrayOf(5, 4, 3, 2, 1), 2),
    )

    // all-equal input: each window collapses to the shared value, single output
    Test(
        "all equal values",
        intArrayOf(2),
        maxSlidingWindow(intArrayOf(2, 2, 2), 3),
    )

    // window equal to array length: single output, the global max
    Test(
        "window equals array size",
        intArrayOf(7),
        maxSlidingWindow(intArrayOf(1, 3, 7, -3, 5), 5),
    )

    // empty input: no windows fit
    Test(
        "empty input",
        intArrayOf(),
        maxSlidingWindow(intArrayOf(), 3),
    )

    // out-of-range window size: no complete window fits
    Test(
        "window larger than array",
        intArrayOf(),
        maxSlidingWindow(intArrayOf(1, 2, 3), 4),
    )
}
