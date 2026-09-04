package com.cjbooms.prep.stages.stage11

/**
 * Kth largest element in a stream.
 *
 * Design a class to find the kth largest element in a stream of integers.
 * The class receives values one at a time and must report the current kth
 * largest after each insertion.
 *
 * Constructor parameter:
 *  - [k]: the rank to track. MUST be positive.
 *
 * Behavior:
 *  - `add(value)` records a new value from the stream.
 *  - `peek()` returns the current kth largest value among everything added.
 *  - If fewer than `k` values have been added, `peek()` returns the smallest
 *    value seen so far.
 */
class KthLargest(private val k: Int) {

    init {
        require(k > 0) { "k must be positive, was $k" }
    }

    /**
     * Record a new [value] from the stream.
     *
     * @param value the next integer from the data stream.
     */
    fun add(value: Int) {
        TODO("implement")
    }

    /**
     * @return the current kth largest value among everything added so far.
     * If fewer than `k` values have been added, returns the smallest value
     * seen so far.
     */
    fun peek(): Int {
        TODO("implement")
    }
}
