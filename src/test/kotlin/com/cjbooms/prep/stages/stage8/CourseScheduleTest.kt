package com.cjbooms.prep.stages.stage8

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class CourseScheduleTest {

    @Test
    fun `simple linear chain returns courses in order`() {
        // KDoc: (a, b) means course a depends on course b (b before a).
        // 0 depends on 1, 1 depends on 2, 2 depends on 3 -> order 3, 2, 1, 0
        val order = findCourseOrder(4, listOf(0 to 1, 1 to 2, 2 to 3))
        assertEquals(4, order.size)
        assertEquals(listOf(3, 2, 1, 0), order)
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
        // KDoc: (a, b) means course a depends on course b (b before a).
        // 0 depends on 1, 2, 3 -> 1, 2, 3 may be in any order before 0.
        val order = findCourseOrder(4, listOf(0 to 1, 0 to 2, 0 to 3))
        assertEquals(4, order.size)
        assertEquals(0, order.last())
        assertEquals(setOf(1, 2, 3), order.dropLast(1).toSet())
        // Ensure every prerequisite appears before the course that depends on it.
        val indexInOrder = { c: Int -> order.indexOf(c) }
        for ((course, prereq) in listOf(0 to 1, 0 to 2, 0 to 3)) {
            assertTrue(indexInOrder(prereq) < indexInOrder(course))
        }
    }
}
