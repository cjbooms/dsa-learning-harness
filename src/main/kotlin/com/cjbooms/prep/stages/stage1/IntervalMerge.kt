package com.cjbooms.prep.stages.stage1

import kotlin.math.max

/**
 * Stage 1.1 — Interval merging (classic intervals question).
 *
 * Real-world framing: consolidate overlapping backup windows, meeting schedules,
 * or any half-open range problem.
 * Given half-open intervals [start, end), merge all overlapping or touching
 * ones and return the minimal sorted list.
 *
 * mergeIntervals(listOf(1..3, 2..5, 8..9)) == listOf(1..5, 8..9)   (IntRange = closed, adapt)
 *
 * Ritual: name 2 candidate approaches (sort + single pass? sweep line?),
 * cost each, defend the pick. THEN code.
 *
 * Mutation drill (after it works): intervals arrive as a STREAM, one at a time —
 * fun add(interval) keeps the merged set current. What structure changes?
 */
fun mergeIntervals(intervals: List<IntRange>): List<IntRange> {
    if (intervals.isEmpty()) return intervals
    val outputIntervals = mutableListOf<IntRange>()
    val sortedIntervals = intervals.map { it.start to (it.endInclusive + 1) }.sortedBy { it.first }

    var currentIntervalStart = sortedIntervals[0].first
    var currentIntervalEnd = sortedIntervals[0].second
    sortedIntervals.forEach { next ->
        if (next.first <= currentIntervalEnd) {
            currentIntervalEnd = max(currentIntervalEnd, next.second)

        } else {
            outputIntervals.add(currentIntervalStart until currentIntervalEnd)
            currentIntervalStart = next.first
            currentIntervalEnd = next.second
        }
    }
    outputIntervals.add(currentIntervalStart until currentIntervalEnd)
    return outputIntervals
}

/**
 * Streaming variant: intervals arrive one at a time via add(); merged() returns
 * the current merged set at any moment. No re-sorting the world per add.
 *
 * Structure: TreeMap<start, end> holding the MERGED intervals, sorted by start.
 * floorEntry(newStart) finds the one interval that could overlap from the left;
 * from there we walk forward absorbing every entry whose start <= newEnd.
 * Same floorEntry + range-scan shape as VersionedKVStore / ReplicationLagAlerter.
 */
class StreamingIntervalMerger {

    // merged intervals keyed by start, sorted by start (half-open [start, end))
    private val mergedByStart = java.util.TreeMap<Int, Int>()

    fun add(interval: IntRange) {
        var currentIntervalStart = interval.start
        var currentIntervalEnd = interval.endInclusive + 1 // half-open, like mergeIntervals

        // The one interval that could overlap from the left: greatest start <= ours.
        val leftNeighbor = mergedByStart.floorEntry(currentIntervalStart)
        if (leftNeighbor != null && leftNeighbor.value >= currentIntervalStart) {
            // It reaches into us: absorb it, extend if it ends beyond us.
            currentIntervalStart = leftNeighbor.key
            currentIntervalEnd = max(currentIntervalEnd, leftNeighbor.value)
            mergedByStart.remove(leftNeighbor.key)
        }

        // Absorb every following interval that starts before we end.
        while (true) {
            val next = mergedByStart.ceilingEntry(currentIntervalStart) ?: break
            if (next.key > currentIntervalEnd) break // starts beyond us: no overlap
            currentIntervalEnd = max(currentIntervalEnd, next.value)
            mergedByStart.remove(next.key)
        }

        mergedByStart[currentIntervalStart] = currentIntervalEnd
    }

    fun merged(): List<IntRange> =
        mergedByStart.entries.map { (start, end) -> start until end }
}
