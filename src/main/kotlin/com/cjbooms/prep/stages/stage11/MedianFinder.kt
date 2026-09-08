package com.cjbooms.prep.stages.stage11

import java.util.PriorityQueue

/**
 * Learn first: see docs/learning-resources.md
 * Find median from a data stream.
 *
 * Design a class that supports two operations on an integer stream:
 * inserting a number and reading the current median of all numbers seen.
 *
 * Behavior:
 *  - `addNum(num)` appends a number to the stream.
 *  - `findMedian()` returns the median of all numbers added so far.
 *
 * Edge cases:
 *  - If the total count is odd, the median is the middle value after sorting.
 *  - If the total count is even, the median is the average of the two middle
 *    values.
 *  - On an empty stream, `findMedian()` is undefined.
 */
class MedianFinder {

    val minHeap = PriorityQueue<Int>()
    val maxHeap = PriorityQueue<Int>(compareByDescending { it })
    var count = 0

    /**
     * Append [num] to the stream.
     *
     * @param num the next integer from the data stream.
     */
    fun addNum(num: Int) {
        maxHeap.add(num)
        minHeap.add(maxHeap.poll())
        if (minHeap.size > maxHeap.size) {
            maxHeap.add(minHeap.poll())
        }
    }

    /**
     * @return the median of all numbers added so far. If the count is odd,
     * returns the middle value; if even, returns the average of the two
     * middle values.
     */
    fun findMedian(): Double {
        if (minHeap.size < maxHeap.size) {
            return maxHeap.peek().toDouble()
        }
        return  (minHeap.peek().toDouble() + maxHeap.peek().toDouble()) / 2
    }
}

fun main() {
    val cud = MedianFinder()
    cud.addNum(1)
    cud.addNum(20)
    cud.addNum(3)
    println("Expect 3, Actual:" + cud.findMedian())
    cud.addNum(4)
    println("Expect 3.5, Actual:" + cud.findMedian())
}