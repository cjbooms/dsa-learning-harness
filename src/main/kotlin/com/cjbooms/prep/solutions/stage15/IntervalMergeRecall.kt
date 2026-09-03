package com.cjbooms.prep.solutions.stage15

import kotlin.math.max

/**
 * Stage 15.2 — Cold-recall rebuild: interval merging (batch + streaming variants).
 *
 * MongoDB framing: consolidate overlapping time ranges — backup windows, query
 * plan execution windows, lock-acquisition intervals during deadlock analysis.
 *
 * mergeIntervals(list):
 *   - merge overlapping OR touching half-open [start, end) intervals
 *   - return the minimal sorted merged list
 *   - empty input -> empty output
 *
 * StreamingIntervalMerger:
 *   - add(interval) keeps the merged set current as intervals arrive one at a time
 *   - merged() returns the current merged set at any moment
 *   - no re-sorting the world per add
 *
 * Ritual before coding (speak aloud):
 *   1. Batch: which sort key? what defines "overlap" for half-open ranges?
 *   2. Streaming: which map gives O(log n) floor lookup? Walk the absorbed
 *      neighbors in one direction or both?
 *
 * Time budget: 6 minutes cold.
 */
fun mergeIntervals(intervals: List<IntRange>): List<IntRange> {
    if (intervals.isEmpty()) return emptyList()
    // IntRange is closed [start, endInclusive]; the tests treat values as
    // half-open, so widen by +1 before merging and convert back to IntRange.
    val sorted = intervals
        .map { it.start to (it.endInclusive + 1) }
        .sortedBy { it.first }

    val output = mutableListOf<IntRange>()
    var currentStart = sorted[0].first
    var currentEnd = sorted[0].second
    for (next in sorted) {
        if (next.first <= currentEnd) {
            currentEnd = max(currentEnd, next.second)
        } else {
            output += currentStart until currentEnd
            currentStart = next.first
            currentEnd = next.second
        }
    }
    output += currentStart until currentEnd
    return output
}

class StreamingIntervalMerger {

    // Merged intervals keyed by start, sorted by start (half-open [start, end)).
    private val mergedByStart = java.util.TreeMap<Int, Int>()

    fun add(interval: IntRange) {
        var currentStart = interval.start
        var currentEnd = interval.endInclusive + 1

        // The single interval that could overlap from the left.
        val leftNeighbor = mergedByStart.floorEntry(currentStart)
        if (leftNeighbor != null && leftNeighbor.value >= currentStart) {
            currentStart = leftNeighbor.key
            currentEnd = max(currentEnd, leftNeighbor.value)
            mergedByStart.remove(leftNeighbor.key)
        }

        // Absorb every following interval that starts before we end.
        while (true) {
            val next = mergedByStart.ceilingEntry(currentStart) ?: break
            if (next.key > currentEnd) break
            currentEnd = max(currentEnd, next.value)
            mergedByStart.remove(next.key)
        }

        mergedByStart[currentStart] = currentEnd
    }

    fun merged(): List<IntRange> =
        mergedByStart.entries.map { (start, end) -> start until end }
}
