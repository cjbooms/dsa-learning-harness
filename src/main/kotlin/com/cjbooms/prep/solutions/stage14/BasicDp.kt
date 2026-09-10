package com.cjbooms.prep.solutions.stage14

/**
 * Learn first: see docs/learning-resources.md
 * Counts the number of distinct ways to reach the top of a staircase with [n]
 * steps when each move may take either 1 or 2 steps.
 *
 * @param n the number of steps (non-negative)
 * @return the number of distinct ways to climb [n] steps
 */
fun climbStairs(n: Int): Int {
    if (n <= 1) return 1
    // recurrence looks back at most 2 steps, so two rolling variables are enough
    var prevPrev = 1 // f(0)
    var prev = 1     // f(1)
    for (stepIndex in 2..n) {
        val curr = prev + prevPrev
        prevPrev = prev
        prev = curr
    }
    return prev
}

/**
 * Computes the maximum amount of money that can be robbed from a row of houses
 * without robbing two adjacent houses.
 *
 * @param nums the amount of money at each house, in order
 * @return the maximum total that can be robbed
 */
fun houseRobber(nums: List<Int>): Int {
    if (nums.isEmpty()) return 0
    if (nums.size == 1) return nums[0]
    // f(k) = max(f(k-1), f(k-2) + nums[k]); only the last two values matter
    var prevPrev = nums[0]
    var prev = maxOf(nums[0], nums[1])
    for (stepIndex in 2 until nums.size) {
        val curr = maxOf(prev, prevPrev + nums[stepIndex])
        prevPrev = prev
        prev = curr
    }
    return prev
}

fun main() {
    data class Test(val case: String, val expected: Int, val actual: Int) {
        init {
            if (expected != actual) println("FAILED: $this")
            else println("PASSED: $this")
        }
    }

    Test("Single step only one way", 1, climbStairs(1))
    Test("Two steps two ways", 2, climbStairs(2))
    Test("Five steps eight ways", 8, climbStairs(5))

    Test("Empty street robs nothing", 0, houseRobber(emptyList()))
    Test("Single house takes it all", 7, houseRobber(listOf(7)))
    Test("Two houses take the bigger", 2, houseRobber(listOf(1, 2)))
    Test("Classic three house case", 4, houseRobber(listOf(1, 2, 3, 1)))
    Test("Classic five house case", 12, houseRobber(listOf(2, 7, 9, 3, 1)))
}
