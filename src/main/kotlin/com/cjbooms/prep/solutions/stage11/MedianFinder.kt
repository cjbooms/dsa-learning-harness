package com.cjbooms.prep.solutions.stage11

import java.util.PriorityQueue

/**
 * Stage 11.4 — Find median from a data stream (30 min).
 *
 * Why this matters: streaming latency percentiles from the
 * profiler, real-time aggregation of query durations, and rolling statistics
 * over the operation log. Median is harder than mean because you need order, not
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

    fun addNum(num: Int) {
        if (lowerHalf.isEmpty() || num <= lowerHalf.peek()) {
            lowerHalf.add(num)
        } else {
            upperHalf.add(num)
        }

        // Rebalance: lowerHalf may be at most one larger than upperHalf.
        if (lowerHalf.size > upperHalf.size + 1) {
            upperHalf.add(lowerHalf.poll())
        } else if (upperHalf.size > lowerHalf.size) {
            lowerHalf.add(upperHalf.poll())
        }
    }

    /**
     * Returns the median of all numbers seen so far. O(1).
     *
     * Widen each root to Double before averaging to avoid Int overflow with
     * large values.
     */
    fun findMedian(): Double {
        check(lowerHalf.isNotEmpty()) { "findMedian called with no numbers" }
        return if (lowerHalf.size > upperHalf.size) {
            lowerHalf.peek().toDouble()
        } else {
            (lowerHalf.peek() / 2.0) + (upperHalf.peek() / 2.0)
        }
    }
}
