package com.cjbooms.prep.stages.stage8

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class TaskSchedulerTest {

    @Test
    fun `linear chain returns in dependency order`() {
        // build -> compile -> test -> deploy (each depends on the previous)
        val order = scheduleTasksKahn(
            tasks = listOf("build", "compile", "test", "deploy"),
            dependencies = listOf(
                "build" to "compile",
                "compile" to "test",
                "test" to "deploy",
            ),
        )
        assertEquals(listOf("build", "compile", "test", "deploy"), order)
    }

    @Test
    fun `independent tasks may appear in any order but all are scheduled`() {
        val order = scheduleTasksKahn(
            tasks = listOf("a", "b", "c"),
            dependencies = emptyList(),
        )
        assertEquals(3, order.size)
        assertEquals(setOf("a", "b", "c"), order.toSet())
    }

    @Test
    fun `cycle returns empty list`() {
        // a -> b -> c -> a is cyclic
        val order = scheduleTasksKahn(
            tasks = listOf("a", "b", "c"),
            dependencies = listOf("a" to "b", "b" to "c", "c" to "a"),
        )
        assertEquals(emptyList<String>(), order)
    }

    @Test
    fun `diamond dependency resolves to a valid schedule`() {
        // a is a prerequisite for b and c, both required for d
        val order = scheduleTasksKahn(
            tasks = listOf("a", "b", "c", "d"),
            dependencies = listOf(
                "a" to "b",
                "a" to "c",
                "b" to "d",
                "c" to "d",
            ),
        )
        // a must come first, d must come last; b and c are interchangeable
        assertEquals(4, order.size)
        assertEquals("a", order.first())
        assertEquals("d", order.last())
        val aIdx = order.indexOf("a")
        val bIdx = order.indexOf("b")
        val cIdx = order.indexOf("c")
        val dIdx = order.indexOf("d")
        assertTrue(aIdx < bIdx)
        assertTrue(aIdx < cIdx)
        assertTrue(bIdx < dIdx)
        assertTrue(cIdx < dIdx)
    }
}
