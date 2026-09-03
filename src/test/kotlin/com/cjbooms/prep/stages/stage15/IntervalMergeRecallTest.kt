package com.cjbooms.prep.stages.stage15

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class IntervalMergeRecallTest {

    @Test
    fun `merges overlapping and touching intervals`() {
        assertEquals(
            listOf(1..5, 8..9),
            mergeIntervals(listOf(1..3, 2..5, 8..9)),
        )
    }

    @Test
    fun `unsorted input and full containment collapse to one`() {
        assertEquals(
            listOf(1..10),
            mergeIntervals(listOf(5..10, 1..6, 2..3)),
        )
    }

    @Test
    fun `empty input returns empty output`() {
        assertEquals(emptyList<IntRange>(), mergeIntervals(emptyList()))
    }

    @Test
    fun `streaming merger absorbs neighbors as they arrive`() {
        val merger = StreamingIntervalMerger()
        merger.add(1..3)
        merger.add(8..9)
        assertEquals(listOf(1..3, 8..9), merger.merged())

        merger.add(2..5) // overlaps 1..3 -> 1..5, 8..9
        assertEquals(listOf(1..5, 8..9), merger.merged())

        merger.add(5..9) // overlaps/touches 1..5 and 8..9 -> 1..9
        assertEquals(listOf(1..9), merger.merged())
    }
}
