package com.cjbooms.prep.stages.stage11

import java.util.PriorityQueue

/**
 * Stage 11.4 — Find median from a data stream (30 min).
 *
 * Why this matters for MongoDB: streaming latency percentiles from the
 * profiler, real-time aggregation of query durations, and rolling statistics
 * over the oplog. Median is harder than mean because you need order, not
 * just a running sum.
 *
 * Structure-selection ritual:
 *   - Full sort on every insert: O(n log n) — only OK if the stream is tiny.
 *   - Two heaps: a max-heap for the lower half and a min-heap for the upper
 *     half. Rebalance so their sizes differ by at most one.
 *   - Invariant: every element in the lower half <= every element in the upper
 *     half. The heaps only need to know their own max/min, so each rebalance
 *     is O(log n).
 *
 * The invariant IS the answer — say it before coding.
 *
 * Time budget: 30 min.
 */
class MedianFinder {

    private val lowerHalf = PriorityQueue<Int>(compareByDescending { it })
    private val upperHalf = PriorityQueue<Int>()

    /**
     * Adds [num] to the stream. O(log n).
     *
     * Invariant: lowerHalf.size == upperHalf.size, or lowerHalf.size is one
     * larger. lowerHalf.peek() is the largest value in the lower half;
     * upperHalf.peek() is the smallest value in the upper half.
     */
    fun addNum(num: Int) {
        TODO("implement")
    }

    /**
     * Returns the median of all numbers seen so far. O(1).
     *
     * If the total count is odd, the larger heap's root is the median.
     * If even, the median is the average of the two roots.
     */
    fun findMedian(): Double {
        TODO("implement")
    }
}
