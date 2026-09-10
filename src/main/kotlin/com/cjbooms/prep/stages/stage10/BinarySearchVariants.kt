package com.cjbooms.prep.stages.stage10

/**
 * Learn first: see docs/learning-resources.md
 * Stage 10.3 — Binary Search Variants.
 */

/**
 * Search in a rotated sorted array (LC 33). The input array was sorted in
 * ascending order, then rotated at some unknown pivot. Every element is
 * unique. Return the 0-based index of `target`, or `-1` if it is not present.
 *
 * @param numbers a strictly increasing array of distinct integers that has
 *                been rotated by some (possibly zero) positions
 * @param target the value to search for
 * @return the index of `target` in `numbers`, or `-1` if `target` is not
 *         present
 */
fun searchRotated(numbers: IntArray, target: Int): Int {
    var left = 0
    var right = numbers.size - 1

    while (left <= right) {
        val middle = left + (right - left) / 2
        if (numbers[middle] == target) return middle
        if (numbers[left] <= numbers[middle]) {
            // Left is definitely sorted
            if (numbers[middle] > target && numbers[left] <= target) {
                // Must be in Left
                right = middle - 1
            } else {
                // Must be in Left
                left = middle + 1
            }
        } else {
            // Right is definitely sorted
            if (numbers[middle] < target && numbers[right] >= target) {
                // Must be in Right
                left = middle + 1
            } else {
                // Must be in Left
                right = middle - 1
            }
        }
    }

    return -1
}

/**
 * Find Peak Element (LC 162). An element is a peak if it is strictly greater
 * than its neighbours; endpoints count as a peak when they are greater than
 * their single neighbour. Return the index of any peak.
 *
 * @param numbers an array of integers of length at least 1
 * @return the index of any peak element; multiple valid answers are accepted
 */
fun findPeakElement(numbers: IntArray): Int {
    if (numbers.isEmpty()) return -1
    if (numbers.size == 1) return 0
    for (i in 0 until numbers.size) {
        // First Peak Check
        if (i == 0) {
            if (numbers[i] > numbers[i + 1]) return i
        }
        // Last Peak Check
        else if (i == numbers.size - 1) {
            if (numbers[i] > numbers[i - 1]) return i
        } else if (numbers[i] > numbers[i + 1] && numbers[i] > numbers[i - 1]) return i
    }
    return -1
}

/**
 * Search a 2D Matrix (LC 74). Given an `m x n` integer matrix where each row
 * is sorted in ascending order and the first element of each row is greater
 * than the last element of the previous row, return whether `target` is
 * present anywhere in the matrix.
 *
 * @param matrix an `m x n` matrix with the row-wise and inter-row ordering
 *               described above
 * @param target the value to search for
 * @return `true` if `target` appears in `matrix`, `false` otherwise
 */
fun search2DMatrix(matrix: Array<IntArray>, target: Int): Boolean {
    if (matrix.isEmpty() || matrix[0].isEmpty()) return false
    val rows = matrix.size
    val cols = matrix[0].size

    // Binary Search For array

    fun findArray(): Int {
        var left = 0
        var right = rows - 1
        while (left <= right) {
            val middle = left + ((right - left) / 2)
            if (matrix[middle][0] <= target && matrix[middle][cols - 1] >= target) return middle
            if (matrix[middle][0] > target) right = middle - 1
            else left =  middle + 1

        }
        return -1
    }

    fun findValue(searchArray: IntArray): Boolean {
        var left = 0
        var right = cols - 1
        while (left <= right) {
            val middle = left + ((right - left) / 2)
            if (searchArray[middle] == target) return true
            if (searchArray[middle] > target) right = middle - 1
            else left =  middle + 1

        }
        return false
    }

    val arrayToSearch = findArray()
    if (arrayToSearch == -1) return false
    return findValue(matrix[arrayToSearch])
}

fun main() {
    data class Test(val test: String, val expected: String, val actual: String) {
        fun check() {
            if (expected.trim() != actual.trim()) println("FAILED $this")
            else println("PASSED $this")
        }
    }
    Test(
        test = "Matrix search when present",
        expected = "true",
        actual = search2DMatrix(
            arrayOf(
                intArrayOf(1, 2, 3),
                intArrayOf(4, 5, 6),
                intArrayOf(8, 9, 10),
            ), 3
        ).toString()
    ).check()
    Test(
        test = "Matrix search when empty",
        expected = "false",
        actual = search2DMatrix(
            arrayOf(), 3
        ).toString()
    ).check()



    Test(
        test = "Empty array passes",
        expected = "-1",
        actual = searchRotated(intArrayOf(), 2).toString()
    ).check()
    Test(
        test = "Single item, array passes",
        expected = "0",
        actual = searchRotated(intArrayOf(1), 1).toString()
    ).check()
    Test(
        test = "Non rotated passes",
        expected = "1",
        actual = searchRotated(intArrayOf(1, 2, 3, 4, 5), 2).toString()
    ).check()
    Test(
        test = "Rotated array passes",
        expected = "2",
        actual = searchRotated(intArrayOf(4, 5, 2, 3), 2).toString()
    ).check()

    Test(
        test = "Find Peak when none exist",
        expected = "-1",
        actual = findPeakElement(intArrayOf(1, 2, 3, 3, 3)).toString()
    ).check()
    Test(
        test = "Find Peak empty input",
        expected = "-1",
        actual = findPeakElement(intArrayOf()).toString()
    ).check()
    Test(
        test = "Find Peak happy path",
        expected = "1",
        actual = findPeakElement(intArrayOf(4, 5, 2, 3)).toString()
    ).check()
    Test(
        test = "Find Last Peak",
        expected = "0",
        actual = findPeakElement(intArrayOf(5, 4)).toString()
    ).check()
    Test(
        test = "Find First Peak",
        expected = "0",
        actual = findPeakElement(intArrayOf(5, 4)).toString()
    ).check()
    Test(
        test = "Find No Peaks",
        expected = "-1",
        actual = findPeakElement(intArrayOf(1, 1, 1, 2, 2)).toString()
    ).check()

}