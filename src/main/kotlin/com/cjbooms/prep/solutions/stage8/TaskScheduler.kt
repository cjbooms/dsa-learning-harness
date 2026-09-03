package com.cjbooms.prep.solutions.stage8

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
    val dependents = mutableMapOf<String, MutableList<String>>()  // task -> tasks that depend ON it
    val inDegree = hashMapOf<String, Int>()                       // task -> remaining prereq count

    tasks.forEach {
        dependents.getOrPut(it) { mutableListOf() }
        inDegree[it] = 0
    }

    dependencies.forEach { (prereq, dependent) ->
        dependents.getOrPut(prereq) { mutableListOf() }.add(dependent)
        inDegree[dependent] = inDegree.getOrDefault(dependent, 0) + 1
    }

    // Frontier: tasks with zero remaining prerequisites — schedulable NOW.
    val ready = ArrayDeque<String>()
    inDegree.forEach { (task, count) ->
        if (count == 0) ready.addLast(task)
    }

    while (ready.isNotEmpty()) {
        val current = ready.removeFirst()
        output.add(current)

        // "Remove it as a dep from all dependents" — the moment one hits zero, it joins the frontier.
        dependents[current]?.forEach { dependent ->
            val remaining = inDegree[dependent]!! - 1
            inDegree[dependent] = remaining
            if (remaining == 0) ready.addLast(dependent)
        }
    }

    // Cycle detection: nodes still pinned at in-degree > 0 -> no valid schedule.
    return if (output.size == tasks.size) output else emptyList()
}
