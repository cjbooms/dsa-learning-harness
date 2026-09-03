package com.cjbooms.prep.stages.stage8

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue

class CycleDetectorTest {

    @Test
    fun `DAG has no cycle`() {
        val dag = listOf(0 to 1, 1 to 2, 2 to 3)
        assertFalse(hasCycle(4, dag))
        assertEquals(emptyList<Int>(), findCycle(4, dag))
    }

    @Test
    fun `simple 3-node cycle is detected`() {
        val cyclic = listOf(0 to 1, 1 to 2, 2 to 0)
        assertTrue(hasCycle(3, cyclic))
        val cycle = findCycle(3, cyclic)
        // The returned cycle should contain all 3 nodes — direction matches
        // traversal order, so we check membership not exact ordering.
        assertEquals(setOf(0, 1, 2), cycle.toSet())
        assertEquals(3, cycle.size)
    }

    @Test
    fun `self loop is a cycle`() {
        assertTrue(hasCycle(2, listOf(0 to 0)))
        val cycle = findCycle(2, listOf(0 to 0))
        assertTrue(cycle.contains(0))
    }

    @Test
    fun `cycle nested inside larger graph is detected`() {
        // 0 -> 1 -> 2 -> 3 -> 1  (1-2-3 form a cycle; 0 leads into it)
        val edges = listOf(0 to 1, 1 to 2, 2 to 3, 3 to 1)
        assertTrue(hasCycle(4, edges))
        val cycle = findCycle(4, edges)
        assertEquals(setOf(1, 2, 3), cycle.toSet())
        assertEquals(3, cycle.size)
    }
}
