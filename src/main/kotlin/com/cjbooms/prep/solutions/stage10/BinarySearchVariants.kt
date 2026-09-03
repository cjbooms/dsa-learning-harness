package com.cjbooms.prep.solutions.stage10

/**
 * Stage 10.3 — Binary Search Variants.
 *
 * MongoDB relevance: B-tree cursor positioning on indexes is a generalized
 * binary search; the query planner picks scan bounds by binary-searching the
 * index for the requested key range. Rotated-array search mirrors recovery
 * scenarios (compaction, log rotation) where sorted data is shifted.
 *
 * Structure-selection ritual: when does binary search apply?
 *   - The data is sorted by the search key (or monotonic in some derived value).
 *   - You can answer "is the target on the left or right half?" in O(1).
 *   - You want O(log n) instead of O(n) linear scan.
 * Variants below stress the boundary checks — those are where bugs live.
 *
 * Time budget: 15 minutes.
 */

/**
 * Search in a rotated sorted array (LC 33): array was sorted in ascending
 * order then rotated at some unknown pivot. Every element is unique. Return
 * the index of `target`, or -1 if absent.
 *
 * Approach: at every step, one half of [left, right] is normally sorted.
 *   - If nums[left] <= nums[mid], the LEFT half is sorted.
 *   - Otherwise, the RIGHT half is sorted.
 * Check whether the target falls inside the sorted half; if yes, go there,
 * otherwise go to the other half.
 *
 * Time:  O(log n).
 * Space: O(1).
 */
fun searchRotated(numbers: IntArray, target: Int): Int {
    var left = 0
    var right = numbers.size - 1
    while (left <= right) {
        val mid = left + (right - left) / 2
        if (numbers[mid] == target) return mid
        if (numbers[left] <= numbers[mid]) {
            // left half is sorted
            if (target >= numbers[left] && target < numbers[mid]) {
                right = mid - 1
            } else {
                left = mid + 1
            }
        } else {
            // right half is sorted
            if (target > numbers[mid] && target <= numbers[right]) {
                left = mid + 1
            } else {
                right = mid - 1
            }
        }
    }
    return -1
}

/**
 * Find Peak Element (LC 162): an element strictly greater than its
 * neighbours; endpoints count as a peak if larger than their single neighbour.
 * Return the index of any peak. Multiple valid answers are acceptable.
 *
 * Approach: binary search on the slope. If nums[mid] < nums[mid + 1], a
 * peak must exist on the right (the array is increasing somewhere to the
 * right, and the right boundary is finite). Otherwise, the peak is at mid
 * or to the left. This works because of the strict inequality guarantee.
 *
 * Time:  O(log n).
 * Space: O(1).
 */
fun findPeakElement(numbers: IntArray): Int {
    var left = 0
    var right = numbers.size - 1
    while (left < right) {
        val mid = left + (right - left) / 2
        if (numbers[mid] < numbers[mid + 1]) {
            // ascending — peak is to the right
            left = mid + 1
        } else {
            // descending — mid is at or past the peak
            right = mid
        }
    }
    return left
}

/**
 * Search a 2D Matrix (LC 74): matrix of size m x n where each row is sorted
 * in ascending order AND the first element of each row is greater than the
 * last element of the previous row. Treat it as a flat sorted array.
 *
 * Approach: index = row * n + col. Binary search on the flat index space,
 * converting back to row/col at each step.
 *
 * Time:  O(log(m*n)).
 * Space: O(1).
 */
fun search2DMatrix(matrix: Array<IntArray>, target: Int): Boolean {
    if (matrix.isEmpty() || matrix[0].isEmpty()) return false
    val m = matrix.size
    val n = matrix[0].size
    var left = 0
    var right = m * n - 1
    while (left <= right) {
        val mid = left + (right - left) / 2
        val row = mid / n
        val col = mid % n
        val value = matrix[row][col]
        when {
            value == target -> return true
            value < target -> left = mid + 1
            else -> right = mid - 1
        }
    }
    return false
}
