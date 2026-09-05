package com.cjbooms.prep.solutions.stage11

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
        if (tasks.isEmpty()) return 0
        if (cooldown == 0) return tasks.size

        // Closed-form greedy (LeetCode 621):
        //   frame length = (maxCount - 1) * (cooldown + 1) slots
        //     -> one slot per max-frequency task, plus cooldown-sized gaps
        //        between them.
        //   tail width = number of distinct tasks sharing the max count
        //     -> they all fit in the final row of the frame.
        // The total schedule is max(frame + tail, tasks.size): if the remaining
        // tasks can fully fill every gap, no idle slots are needed and we just
        // do all tasks back-to-back.
        val counts = IntArray(26)
        var maxCount = 0
        for (task in tasks) {
            val bucket = task.code - 'A'.code
            counts[bucket]++
            if (counts[bucket] > maxCount) maxCount = counts[bucket]
        }
        val numMaxTasks = counts.count { it == maxCount }
        val frameLength = (maxCount - 1) * (cooldown + 1)
        val withFrame = frameLength + numMaxTasks
        return maxOf(withFrame, tasks.size)
    }
}
