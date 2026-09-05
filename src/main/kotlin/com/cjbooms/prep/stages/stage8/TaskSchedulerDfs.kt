package com.cjbooms.prep.stages.stage8

/**
 * Learn first: see docs/learning-resources.md
 * Stage 8.2 — Topological sort via DFS post-order.
 *
 * Problem: given a list of [tasks] and [dependencies] (prereq -> dependent),
 * return any valid order in which all tasks can be completed. If the
 * dependencies contain a cycle, return an empty list.
 *
 * Time budget: 20 min.
 */

fun main() {

    println(
        "No cycle: " + scheduleTasksDfs(
            listOf("t1", "t2", "t3"),
            listOf("t1" to "t2", "t1" to "t3")
        )
    )

    // Cyclic Dependency
    println(
        "Cycle: " +
                scheduleTasksDfs(
                    listOf("t1", "t2", "t3"),
                    listOf("t2" to "t1", "t1" to "t2")
                )
    )

}


fun scheduleTasksDfs(
    tasks: List<String>,
    dependencies: List<Pair<String, String>>,
): List<String> {

    val taskDependencies = mutableMapOf<String, MutableSet<String>>()
    tasks.forEach {
        taskDependencies[it] = mutableSetOf()
    }

    dependencies.forEach { (dependency, task) ->
        taskDependencies[task]!!.add(dependency)
    }
    println("Tasks Dependecies : $taskDependencies")

    val visited = mutableSetOf<String>()
    val ready = ArrayDeque<String>()
    var cycleDetected = false


    fun visitTask(task: String) {
        if (ready.contains(task)) return
        if (cycleDetected || visited.contains(task)) {
            println("Cycle Detected, already visited : $task")
            cycleDetected = true
            return
        }
        visited.add(task)
        if (taskDependencies[task]?.isNotEmpty() ?: false) {
            taskDependencies[task]!!.forEach { dependency ->
                println("Visiting $task dependency : $dependency")
                visitTask(dependency)
            }
        }
        ready.add(task)
    }

    tasks.forEach {
        visitTask(it)
    }


    return if (cycleDetected) emptyList()
    else ready


}
