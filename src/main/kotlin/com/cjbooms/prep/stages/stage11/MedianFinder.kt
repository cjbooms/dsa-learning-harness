package com.cjbooms.prep.stages.stage11

/**
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

    /**
     * Append [num] to the stream.
     *
     * @param num the next integer from the data stream.
     */
    fun addNum(num: Int) {
        TODO("implement")
    }

    /**
     * @return the median of all numbers added so far. If the count is odd,
     * returns the middle value; if even, returns the average of the two
     * middle values.
     */
    fun findMedian(): Double {
        TODO("implement")
    }
}
