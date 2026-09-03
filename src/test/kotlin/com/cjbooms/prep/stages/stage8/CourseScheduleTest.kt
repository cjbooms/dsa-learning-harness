package com.cjbooms.prep.stages.stage8

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class CourseScheduleTest {

    @Test
    fun `simple linear chain returns courses in order`() {
        // 0 -> 1 -> 2 -> 3 (each prerequisite means "must come before")
        val order = findCourseOrder(4, listOf(0 to 1, 1 to 2, 2 to 3))
        assertEquals(4, order.size)
        assertEquals(listOf(0, 1, 2, 3), order)
    }

    @Test
    fun `no prerequisites returns any order containing all courses`() {
        val order = findCourseOrder(3, emptyList())
        assertEquals(3, order.size)
        assertEquals(setOf(0, 1, 2), order.toSet())
    }

    @Test
    fun `cyclic prerequisites return empty list`() {
        // 0 -> 1 -> 2 -> 0 forms a cycle
        val order = findCourseOrder(3, listOf(0 to 1, 1 to 2, 2 to 0))
        assertEquals(emptyList<Int>(), order)
    }

    @Test
    fun `multiple valid orderings are accepted`() {
        // 1 depends on 0; 2 depends on 0; 3 depends on 0
        // 0 must come first; 1, 2, 3 may be in any order after
        val order = findCourseOrder(4, listOf(0 to 1, 0 to 2, 0 to 3))
        assertEquals(4, order.size)
        assertEquals(0, order.first())
        assertEquals(setOf(1, 2, 3), order.drop(1).toSet())
        // Ensure no course scheduled before its prerequisite
        val indexInOrder = { c: Int -> order.indexOf(c) }
        for ((prereq, dependent) in listOf(0 to 1, 0 to 2, 0 to 3)) {
            assertTrue(indexInOrder(prereq) < indexInOrder(dependent))
        }
    }
}
