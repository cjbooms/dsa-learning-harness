package com.cjbooms.prep.dsa

/**
 * K-Sum generalization: count k-element subsets of nums that sum to target.
 *
 * Sort + recursive k-sum reducing to two-sum with two pointers.
 * O(n^(k-1)) worst case. Duplicates in input are treated as distinct positions.
 */
fun kSumCount(nums: IntArray, k: Int, target: Long): Long {
    if (k < 2 || nums.size < k) return 0
    val sorted = nums.sorted()
    return kSum(sorted, 0, k, target)
}

private fun kSum(nums: List<Int>, start: Int, k: Int, target: Long): Long {
    if (k == 2) return twoSum(nums, start, target)
    var count = 0L
    for (i in start..nums.size - k) {
        count += kSum(nums, i + 1, k - 1, target - nums[i])
    }
    return count
}

private fun twoSum(nums: List<Int>, start: Int, target: Long): Long {
    var lo = start
    var hi = nums.size - 1
    var count = 0L
    while (lo < hi) {
        val sum = nums[lo].toLong() + nums[hi]
        when {
            sum < target -> lo++
            sum > target -> hi--
            else -> {
                count++
                lo++
                hi--
            }
        }
    }
    return count
}
