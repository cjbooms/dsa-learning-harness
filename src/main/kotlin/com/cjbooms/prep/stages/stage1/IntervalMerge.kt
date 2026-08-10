package com.cjbooms.prep.stages.stage1

/**
 * Stage 1.1 — Interval merging (REPORTED MongoDB question).
 *
 * Atlas framing: consolidate overlapping backup windows.
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
    TODO("sort + single pass, or something else? Justify first")
}
