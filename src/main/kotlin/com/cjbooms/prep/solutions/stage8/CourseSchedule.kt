package com.cjbooms.prep.solutions.stage8

import java.util.ArrayDeque

/**
 * Stage 8.5.1 — Course Schedule II: return the ordering, not just a boolean.
 *
 * Why this matters for MongoDB: scheduling background jobs in a deterministic
 * order (compaction -> validate -> report), migration step ordering, dependency
 * resolution at startup. Returning the schedule (vs a boolean) is what makes
 * the result useful — and it surfaces the harder edge cases (multiple valid
 * orderings, impossible schedules).
 *
 * Structure-selection ritual:
 *   - Kahn's BFS over in-degrees; emit node when popped; decrement neighbours.
 *   - If the output size is less than numCourses -> impossible schedule ->
 *     return empty list.
 *   - Multiple valid orderings are fine — any topological sort is acceptable.
 *
 * Time budget: 15 min. Defend aloud: would DFS be cleaner here? (It would,
 * but Kahn produces the ordering in arrival order without an extra reverse
 * step, and the "impossible schedule" check is a single length comparison.)
 */
fun findCourseOrder(numCourses: Int, prerequisites: List<Pair<Int, Int>>): List<Int> {
    // prereq -> [courses that depend on it]  (edge direction matches test
    // convention: pair(a, b) means "a must come before b").
    val dependents = Array(numCourses) { mutableListOf<Int>() }
    val inDegree = IntArray(numCourses)

    for ((prereq, course) in prerequisites) {
        if (prereq !in 0 until numCourses || course !in 0 until numCourses) continue
        dependents[prereq].add(course)
        inDegree[course]++
    }

    val ready = ArrayDeque<Int>()
    for (c in 0 until numCourses) if (inDegree[c] == 0) ready.addLast(c)

    val order = mutableListOf<Int>()
    while (ready.isNotEmpty()) {
        val c = ready.removeFirst()
        order.add(c)
        for (next in dependents[c]) {
            if (--inDegree[next] == 0) ready.addLast(next)
        }
    }

    // If any course still has in-degree > 0, it lives on a cycle -> impossible.
    return if (order.size == numCourses) order else emptyList()
}
