package com.cjbooms.prep.stages.stage8

import java.util.ArrayDeque

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
    val output = mutableListOf<String>()
    val tasksWithDeps = mutableMapOf<String, MutableList<String>>()  // task -> tasks that depend ON it
    val taskWithCount = hashMapOf<String, Int>()                     // task -> how many prereqs remain (in-degree)

    tasks.forEach {
        tasksWithDeps.getOrPut(it) { mutableListOf() }
        taskWithCount[it] = 0
    }

    dependencies.forEach { (prereq, dependent) ->
        tasksWithDeps.getOrPut(prereq) { mutableListOf() }.add(dependent)
        taskWithCount[dependent] = taskWithCount[dependent]!! + 1
    }

    // Frontier: tasks with zero remaining prerequisites — schedulable NOW.
    val readyToSchedule = ArrayDeque<String>()
    taskWithCount.forEach { (task, count) ->
        if (count == 0) readyToSchedule.addLast(task)
    }

    while (readyToSchedule.isNotEmpty()) {
        val currentTask = readyToSchedule.removeFirst()
        output.add(currentTask)

        // "Remove it as a dep from all other tasks" — decrement each dependent;
        // the moment one hits zero, it joins the frontier.
        tasksWithDeps[currentTask]?.forEach { dependent ->
            val remaining = taskWithCount[dependent]!! - 1
            taskWithCount[dependent] = remaining
            if (remaining == 0) readyToSchedule.addLast(dependent)
        }
    }

    // Some tasks never hit zero -> they're on a cycle -> no valid schedule.
    return if (output.size == tasks.size) output else emptyList()
}
