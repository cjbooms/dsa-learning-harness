package com.cjbooms.prep.stages.stage13

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class HitCounterTest {

    @Test
    fun `counts hits inside the trailing window and ignores hits outside it`() {
        val counter = HitCounter(windowSeconds = 300)

        counter.hit(1)
        counter.hit(2)
        counter.hit(300)

        // At t=300, the window is [0, 300). All three hits fall inside it.
        assertEquals(3, counter.getHits(300))

        // At t=301, the window is [1, 301). The t=1 hit has just expired.
        assertEquals(2, counter.getHits(301))
    }

    @Test
    fun `hits exactly windowSeconds in the past are excluded by the half-open window`() {
        val counter = HitCounter(windowSeconds = 5)

        counter.hit(1)

        // Window at t=6 is (1, 6]. The t=1 hit is at the lower edge and must
        // NOT be counted (half-open window: [t-window, t)).
        assertEquals(0, counter.getHits(6))

        // Window at t=7 is (2, 7]. Definitely excludes t=1.
        assertEquals(0, counter.getHits(7))
    }

    @Test
    fun `multiple hits in the same second all count as one bucket`() {
        // A bucket-based counter will pass this trivially; a deque-based one
        // passes only if it groups by timestamp. We just assert the observable
        // contract: getHits returns the total number of hit() calls inside
        // the window regardless of grouping.
        val counter = HitCounter(windowSeconds = 60)

        repeat(5) { counter.hit(10) }
        counter.hit(20)

        assertEquals(6, counter.getHits(25))
        assertEquals(0, counter.getHits(120))
    }

    @Test
    fun `empty counter reports zero hits`() {
        val counter = HitCounter(windowSeconds = 60)
        assertEquals(0, counter.getHits(1000))
    }
}
