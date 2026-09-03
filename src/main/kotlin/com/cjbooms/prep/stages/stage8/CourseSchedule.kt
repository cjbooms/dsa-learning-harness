package com.cjbooms.prep.stages.stage8

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
    TODO("implement")
}
