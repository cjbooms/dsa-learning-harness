package com.cjbooms.prep.stages.stage11

import java.util.PriorityQueue


/**
 * Learn first: see docs/learning-resources.md
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

    val queue = PriorityQueue<Int>(k)
    /**
     * Record a new [value] from the stream.
     *
     * @param value the next integer from the data stream.
     */
    fun add(value: Int) {
        if (queue.size < k) queue.add(value)
        else {
            val currentKthLargest = queue.peek()
            if (value > currentKthLargest) {
                queue.poll()
                queue.add(value)
            }
        }
    }

    /**
     * @return the current kth largest value among everything added so far.
     * If fewer than `k` values have been added, returns the smallest value
     * seen so far.
     */
    fun peek(): Int {
        return queue.peek() ?: throw IllegalArgumentException("No numbers tracked")
    }
}

fun main() {
    val cud = KthLargest(4)
    cud.add(1)
    cud.add(2)
    cud.add(3)
    cud.add(4)
    cud.add(5)
    cud.add(6)

    println("Expected 3 actual: " + cud.peek())

    val cud2 = KthLargest(1)
    cud2.add(1)
    cud2.add(2)
    cud2.add(3)
    cud2.add(4)
    cud2.add(5)
    cud2.add(6)

    println("Expected 6 actual: " + cud2.peek())
}
