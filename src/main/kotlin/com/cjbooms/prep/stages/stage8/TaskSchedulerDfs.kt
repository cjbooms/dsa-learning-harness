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

    val inDegree = mutableMapOf<String, Int>()
    val dependentTasks = mutableMapOf<String, MutableSet<String>>()
    tasks.forEach {
        inDegree[it] = 0
        dependentTasks[it] = mutableSetOf()
    }

    dependencies.forEach { (dependency, task) ->
        val count = inDegree.getOrDefault(task, 0)
        inDegree[task] = count + 1
        dependentTasks[dependency]!!.add(task)
    }
    println("Tasks with dep count: $inDegree")
    println("Dependent Tasks : $dependentTasks")

    var zeroDependencyTasks = inDegree.filter { it.value == 0 }.keys

    val outputTasks = ArrayDeque<String>()

    while (zeroDependencyTasks.isNotEmpty()) {
        println("Tasks with no deps : $zeroDependencyTasks")

        zeroDependencyTasks.forEach { readyTask ->
            outputTasks.addLast(readyTask)
            val tasksNeedingAdjustment = dependentTasks[readyTask] ?: emptyList()
            tasksNeedingAdjustment.forEach {
                inDegree[it] = inDegree[it]?.let { it - 1 } ?: 0
            }
            dependentTasks.remove(readyTask)
            inDegree.remove(readyTask)
            println("Tasks with dep count after removal: $inDegree")
            println("Dependent Tasks after removal : $dependentTasks")
        }
        zeroDependencyTasks = inDegree.filter { it.value == 0 }.keys
    }
    if (dependentTasks.size > 0) {
        println("Cycle detected")
        return emptyList()
    }
    return outputTasks


}
