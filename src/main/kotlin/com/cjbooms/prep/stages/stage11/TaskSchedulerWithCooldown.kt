package com.cjbooms.prep.stages.stage11

/**
 * Task scheduler with cooldown.
 *
 * Given a set of tasks represented by characters and a `cooldown` value,
 * schedule the tasks in some order so that the same task never runs twice
 * within `cooldown` intervening slots. Idle slots may be inserted to satisfy
 * the cooldown requirement. Return the minimum number of slots (intervals)
 * needed to finish all tasks.
 *
 * Behavior:
 *  - Each interval holds exactly one task, or one idle slot if no task is
 *    allowed to run.
 *  - If `cooldown == 0`, no gaps are required and the answer is `tasks.size`.
 *  - If `cooldown > 0`, two runs of the same task must be separated by at
 *    least `cooldown` other tasks or idle cycles.
 */
class TaskSchedulerWithCooldown {

    /**
     * Return the minimum number of intervals required to finish all [tasks]
     * with at least [cooldown] idle cycles between any two runs of the same
     * task.
     *
     * @param tasks the characters representing the tasks to run.
     * @param cooldown the minimum number of intervening slots between two
     * runs of the same task.
     * @return the minimum total number of intervals (including any idle
     * slots).
     */
    fun leastInterval(tasks: CharArray, cooldown: Int): Int {
        TODO("implement")
    }
}
