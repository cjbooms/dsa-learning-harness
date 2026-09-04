package com.cjbooms.prep.stages.stage15

import kotlin.math.max

/**
 * Merges a list of half-open intervals `[start, end)` into the minimal set of
 * non-overlapping intervals. Intervals that overlap or touch are combined into
 * a single interval.
 *
 * @param intervals the input intervals
 * @return a sorted list containing the minimal set of merged intervals; an
 *   empty list when [intervals] is empty
 */
fun mergeIntervals(intervals: List<IntRange>): List<IntRange> {
    TODO("implement")
}

/**
 * Maintains the current minimal set of merged half-open `[start, end)`
 * intervals as new intervals are added one at a time.
 */
class StreamingIntervalMerger {

    /**
     * Adds [interval] to the maintained set, merging it with any stored
     * intervals that overlap or touch it.
     *
     * @param interval the interval to add
     */
    fun add(interval: IntRange) {
        TODO("implement")
    }

    /**
     * Returns the current minimal set of merged intervals.
     *
     * @return the merged intervals at this moment
     */
    fun merged(): List<IntRange> {
        TODO("implement")
    }
}
