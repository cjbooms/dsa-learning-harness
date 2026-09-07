package com.cjbooms.prep.stages.stage8


/**
 * Learn first: see docs/learning-resources.md
 * Course Schedule II.
 *
 * Given a number of courses labelled 0..numCourses - 1 and a list of
 * prerequisite pairs (a, b) meaning course `a` depends on course `b` (b
 * must be completed before a), return any ordering of all courses that
 * satisfies every prerequisite.
 *
 * @param numCourses the total number of courses, in 0..numCourses - 1.
 * @param prerequisites dependency pairs of the form (course, prereq);
 *   may be empty.
 * @return a list of all numCourses course ids in a valid order, or an empty
 *   list if no ordering satisfies the prerequisites (i.e. there is a cycle).
 */
fun findCourseOrder(numCourses: Int, prerequisites: List<Pair<Int, Int>>): List<Int> {
    val result = ArrayDeque<Int>()
    val graph = Array(numCourses) { mutableListOf<Int>() }
    val inDegree = IntArray(numCourses) { 0 }

    prerequisites.forEach { (course, preReq) ->
        graph[preReq].add(course)
        inDegree[course]++
    }

    val queue = ArrayDeque<Int>()
    inDegree.forEachIndexed { course, preReqs ->
        if (preReqs == 0) {
            queue.add(course)
        }
    }

    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        result.addLast(current)

        val dependentCourses = graph[current]

        dependentCourses.forEach { dependent ->
            inDegree[dependent]--
            if (inDegree[dependent] == 0) {
                queue.add(dependent)
            }
        }
    }

    return if (result.size == numCourses) result else emptyList()
}


fun main() {
    println(
        "Expected [2, 4, 7, 8, 9, 1, 3, 5, 6, 0] \nAcutal   " +
                findCourseOrder(
                    10,
                    listOf(
                        0 to 1, 1 to 2, 3 to 4, 5 to 7, 6 to 8
                    )
                )
    )
    println(
        "Expected [] \nAcutal   " +
                findCourseOrder(
                    10,
                    listOf(
                        0 to 1, 1 to 2, 3 to 4, 5 to 7, 6 to 8, 8 to 6
                    )
                )
    )

}
