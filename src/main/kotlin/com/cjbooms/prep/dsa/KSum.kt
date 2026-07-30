package com.cjbooms.prep.dsa

/**
 * K-Sum: count k-element subsets of nums that sum to target.
 *
 * Classic reduction: sort once, then pick elements one at a time (recursion)
 * until only 2 remain, which is solved by the two-pointer two-sum walk.
 *
 *   kSum(k) = for each i: nums[i] + kSum(k-1) on the rest
 *   kSum(2) = two pointers on a sorted array
 *
 * Time:  O(n^(k-1)) worst case — dominated by the recursion; two-sum is O(n).
 * Space: O(k) recursion depth.
 *
 * Duplicated values in the input are treated as distinct positions (so
 * [-1, -1, 2] contains one valid triple for target 0 using BOTH -1s).
 */

fun kSumCount(nums: IntArray, k: Int, target: Long): Long {
    if (k < 2 || nums.size < k) return 0

    // Sorting is what makes the two-pointer base case work: moving the left
    // pointer right strictly increases the sum, moving the right pointer left
    // strictly decreases it.
    val sortedNums = nums.sorted()
    return countKSum(sortedNums, startIndex = 0, k = k, target = target)
}

private fun countKSum(sortedNums: List<Int>, startIndex: Int, k: Int, target: Long): Long {
    // Base case: 2-sum on the sorted remainder, via two pointers.
    if (k == 2) {
        return countTwoSum(sortedNums, startIndex, target)
    }

    var totalCount = 0L

    // Fix nums[i] as the first element of the k-tuple, then recursively count
    // (k-1)-tuples in the suffix after i that sum to (target - nums[i]).
    // The loop bound "size - k" guarantees enough elements remain to complete
    // a k-tuple.
    val lastStartPosition = sortedNums.size - k
    for (i in startIndex..lastStartPosition) {
        val fixedElement = sortedNums[i]
        val remainingTarget = target - fixedElement
        val suffixCount = countKSum(sortedNums, startIndex = i + 1, k = k - 1, target = remainingTarget)
        totalCount += suffixCount
    }

    return totalCount
}

private fun countTwoSum(sortedNums: List<Int>, startIndex: Int, target: Long): Long {
    var leftIndex = startIndex
    var rightIndex = sortedNums.size - 1
    var pairCount = 0L

    while (leftIndex < rightIndex) {
        val leftValue = sortedNums[leftIndex]
        val rightValue = sortedNums[rightIndex]
        val pairSum = leftValue.toLong() + rightValue

        when {
            // Sum too small: only way to increase it is a bigger left element.
            pairSum < target -> leftIndex++
            // Sum too big: only way to decrease it is a smaller right element.
            pairSum > target -> rightIndex--
            else -> {
                pairCount++
                leftIndex++
                rightIndex--
            }
        }
    }

    return pairCount
}
