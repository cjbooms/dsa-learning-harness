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
 *     The cooldown queue is essential: it is what enforces the n-cycle gap
 *     between consecutive runs of the same task.
 *
 * Closed-form greedy (LeetCode 621) used here:
 *   frame length = (maxCount - 1) * (cooldown + 1) slots
 *     -> one slot per max-frequency task, plus cooldown-sized gaps between.
 *   tail width = number of distinct tasks sharing the max count
 *     -> they all fit in the final row of the frame.
 *   answer = max(frame + tail, tasks.size).
 */
class TaskSchedulerWithCooldown {

    /**
     * Return the minimum number of intervals required to finish all tasks
     * with at least `cooldown` idle cycles between any two runs of the same
     * task.
     */
    fun leastInterval(tasks: CharArray, cooldown: Int): Int {
        if (tasks.isEmpty()) return 0
        if (cooldown == 0) return tasks.size

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

fun main() {
    data class Test(val case: String, val expected: Int, val actual: Int) {
        init {
            if (expected != actual) println("FAILED: $this")
            else println("PASSED: $this")
        }
    }

    // LeetCode 621 classic AAABBB n=2: max count=3, num max=2, frame=6, withFrame=8
    Test(
        "leetcocode example AAABBB n=2",
        8,
        TaskSchedulerWithCooldown().leastInterval("AAABBB".toCharArray(), 2),
    )

    // cooldown=0 means no gaps, answer is just the task count
    Test(
        "cooldown zero no gaps",
        6,
        TaskSchedulerWithCooldown().leastInterval("ABCDEF".toCharArray(), 0),
    )

    // all same task: frame is forced, length = (count-1)*(n+1) + 1
    Test(
        "all same task AAAA n=2",
        10,
        TaskSchedulerWithCooldown().leastInterval("AAAA".toCharArray(), 2),
    )

    // remaining tasks fill every gap: max(...) picks tasks.size, not frame
    Test(
        "fills every slot no idle",
        6,
        TaskSchedulerWithCooldown().leastInterval("ABCABC".toCharArray(), 2),
    )

    // empty input: nothing to schedule
    Test(
        "empty tasks",
        0,
        TaskSchedulerWithCooldown().leastInterval("".toCharArray(), 2),
    )

    // cooldown=1 with two of the same task: A idle A
    Test(
        "single task cooldown one",
        3,
        TaskSchedulerWithCooldown().leastInterval("AA".toCharArray(), 1),
    )
}
