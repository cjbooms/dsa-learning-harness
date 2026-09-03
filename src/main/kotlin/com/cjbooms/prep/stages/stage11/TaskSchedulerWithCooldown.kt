package com.cjbooms.prep.stages.stage11

/**
 * Stage 11.3 — Task scheduler with cooldown (15 min).
 *
 * MongoDB relevance: scheduling operations on a hot collection with a
 * cooldown between repeated writes (rate-shaped writes, deduplicated
 * retries, Atlas trigger throttling). The greedy "always run the most
 * frequent remaining task" is the LeetCode 621 formulation.
 *
 * Structure-selection ritual:
 *   - Sort + interval scheduling: O(n log n) but ignores task identity.
 *   - Max-heap of (count, taskId): each tick pops the most frequent available
 *     task, decrements it, and parks it in a FIFO "cooldown queue" sized by n.
 *     When the queue's head's cooldown expires, it re-enters the heap.
 *   The cooldown queue is essential: it is what enforces the n-cycle gap
 *   between consecutive runs of the same task.
 *
 * Tie-breaker (matters when two tasks have equal count): pop whichever task
 * the heap returns. Determinism needs a stable secondary key — encode the
 * task's identity into the heap entry so equal counts compare consistently.
 */
class TaskSchedulerWithCooldown {

    /**
     * Return the minimum number of intervals required to finish all tasks
     * with at least `cooldown` idle cycles between any two runs of the same
     * task.
     *
     * If cooldown == 0, no gaps are needed — the answer is just tasks.size.
     * If cooldown > 0, runs of the same task must be separated by `cooldown`
     * other tasks (or idle cycles).
     */
    fun leastInterval(tasks: CharArray, cooldown: Int): Int {
        TODO("implement")
    }
}
