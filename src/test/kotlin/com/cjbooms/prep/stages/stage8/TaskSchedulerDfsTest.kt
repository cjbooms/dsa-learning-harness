package com.cjbooms.prep.stages.stage8

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class TaskSchedulerDfsTest {

    @Test
    fun `linear chain produces dependency order`() {
        val order = scheduleTasksDfs(
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
    fun `all independent tasks are scheduled`() {
        val order = scheduleTasksDfs(
            tasks = listOf("a", "b", "c"),
            dependencies = emptyList(),
        )
        assertEquals(setOf("a", "b", "c"), order.toSet())
        assertEquals(3, order.size)
    }

    @Test
    fun `cycle returns empty list`() {
        val order = scheduleTasksDfs(
            tasks = listOf("a", "b", "c"),
            dependencies = listOf("a" to "b", "b" to "c", "c" to "a"),
        )
        assertEquals(emptyList<String>(), order)
    }

    @Test
    fun `diamond dependency returns a valid schedule`() {
        val order = scheduleTasksDfs(
            tasks = listOf("a", "b", "c", "d"),
            dependencies = listOf(
                "a" to "b",
                "a" to "c",
                "b" to "d",
                "c" to "d",
            ),
        )
        assertEquals(4, order.size)
        assertEquals("a", order.first())
        assertEquals("d", order.last())
    }
}
