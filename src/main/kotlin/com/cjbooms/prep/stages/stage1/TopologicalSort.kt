package com.cjbooms.prep.stages.stage1

import java.util.ArrayDeque

/**
 * Topological sort (Kahn's algorithm) — THE answer to "ordering with
 * dependencies". Build systems, course prerequisites, job schedulers.
 *
 * When you hear "X must come before Y, give me an order (or say impossible)":
 *   adjacency list + in-degree counts + queue of zero-in-degree nodes.
 *
 * The key insight: never SCAN for the next schedulable task (that's O(V^2)).
 * A task becomes schedulable exactly when its in-degree hits 0 — so enqueue
 * it at that moment and nowhere else.
 *
 * Cycle = "no valid schedule": if the graph has a cycle, those nodes never
 * reach in-degree 0, so the output comes out short.
 */
fun scheduleTasks(tasks: List<String>, dependencies: List<Pair<String, String>>): List<String> {
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
