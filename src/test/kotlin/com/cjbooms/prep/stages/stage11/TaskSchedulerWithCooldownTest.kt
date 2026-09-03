package com.cjbooms.prep.stages.stage11

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class TaskSchedulerWithCooldownTest {

    @Test
    fun `cooldown zero requires no idle cycles`() {
        // No gap needed: schedule is just every task back-to-back.
        val scheduler = TaskSchedulerWithCooldown()
        assertEquals(6, scheduler.leastInterval(charArrayOf('A', 'A', 'B', 'B', 'C', 'C'), cooldown = 0))
    }

    @Test
    fun `single task type forces full cooldown between each run`() {
        // 3 runs of A, cooldown = 2 -> A _ _ A _ _ A  -> 3 + 2*2 = 7.
        val scheduler = TaskSchedulerWithCooldown()
        assertEquals(7, scheduler.leastInterval(charArrayOf('A', 'A', 'A'), cooldown = 2))
    }

    @Test
    fun `mixed tasks fit when frequencies match the cooldown gap`() {
        // AAABBB, cooldown = 2 -> A B _ A B _ A B -> 8.
        // (classic LeetCode 621 example.)
        val scheduler = TaskSchedulerWithCooldown()
        assertEquals(8, scheduler.leastInterval(charArrayOf('A', 'A', 'A', 'B', 'B', 'B'), cooldown = 2))
    }

    @Test
    fun `idle slots disappear when other tasks can fill the cooldown`() {
        // AAXYYA, cooldown = 2 -> A X Y A X Y A  -> 7. A appears 3x with
        // cooldown 2 -> 2 gaps of length cooldown+1 = 3, plus 1 final A.
        val scheduler = TaskSchedulerWithCooldown()
        val result = scheduler.leastInterval(charArrayOf('A', 'A', 'X', 'Y', 'Y', 'A'), cooldown = 2)
        assertEquals(7, result)
    }

    @Test
    fun `empty input returns zero cycles`() {
        val scheduler = TaskSchedulerWithCooldown()
        assertEquals(0, scheduler.leastInterval(charArrayOf(), cooldown = 5))
    }
}
