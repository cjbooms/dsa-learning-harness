package com.cjbooms.prep.stages.stage8

import kotlin.collections.ArrayDeque

/**
 * Stage 8.1 — Topological sort via Kahn's algorithm (BFS over in-degrees).
 *
 * Why this matters for MongoDB: dependency-ordered background tasks — index
 * build jobs, migration steps, oplog appliers, change-stream consumer chains.
 * "Build the document for me but only after its prerequisites land" is exactly
 * the question that defeated the previous interview.
 *
 * Structure-selection ritual:
 *   - State: adjacency list (task -> tasks that depend on it) + in-degree
 *     counts per task.
 *   - Frontier: queue of zero-in-degree nodes ("ready NOW").
 *   - Decrement as you emit; new zero-in-degree nodes join the frontier.
 *   - Cycle detector: if output.size < tasks.size, the graph had a cycle and
 *     no valid schedule exists.
 *
 * Time budget: 25 min. If you go over, you skipped a beat: the moment a node's
 * in-degree hits 0 is the moment it joins the queue. Don't rescan.
 */
fun scheduleTasksKahn(
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


fun main() {

    println(
        "No cycle: " + scheduleTasksKahn(
            listOf("t1", "t2", "t3"),
            listOf("t1" to "t2", "t1" to "t3")
        )
    )

    // Cyclic Dependency
    println(
        "Cycle: " +
                scheduleTasksKahn(
                    listOf("t1", "t2", "t3"),
                    listOf("t2" to "t1", "t1" to "t2")
                )
    )
}

