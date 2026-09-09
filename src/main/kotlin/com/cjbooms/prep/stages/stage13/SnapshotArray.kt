package com.cjbooms.prep.stages.stage13

import java.util.TreeMap

/**
 * Learn first: see docs/learning-resources.md
 * Fixed-length integer array with snapshot-based point-in-time reads.
 *
 * `set` updates the value at an index for the current snapshot. `snap`
 * freezes the current state and returns a new, monotonically increasing
 * snapshot id. `get(index, snapId)` returns the value at [index] as of the
 * given snapshot, or 0 if that index has never been set.
 *
 * Snapshot ids are dense integers starting from 0 and increment by 1 per
 * `snap()` call.
 *
 * @param length number of indices in the array. Must be non-negative.
 */
class SnapshotArray(val length: Int) {


    val data = Array(length) { TreeMap<Int, Int>() }
    var currentSnapshot = 0

    init {
        require(length >= 0) { "length must be non-negative, was $length" }
    }

    /**
     * Records [value] at [index] for the current snapshot.
     */
    fun set(index: Int, value: Int) {
        data[index][currentSnapshot] = value
    }

    /**
     * Takes a snapshot of the current array state and returns its snapshot
     * id. The id is unique per snapshot and monotonically increasing.
     */
    fun snap(): Int {
        return currentSnapshot++
    }

    /**
     * Returns the value at [index] as of snapshot [snapId]. If nothing was
     * ever set at [index] before that snapshot, returns 0.
     */
    fun get(index: Int, snapId: Int): Int {
        val updates = data.getOrNull(index) ?: return 0
        val entry = updates.floorEntry(snapId) ?: return 0
        println("Requested $snapId acutal last update in ${entry.key}")
        return entry.value
    }


}

fun main() {
    val cud = SnapshotArray(4)
    cud.set(0, 0)
    println("Expected 0 Actual:" + cud.get(0, 0) )
    cud.set(1, 1)
    println("Expected 1 Actual:" + cud.get(1, 0) )

    cud.set(2, 1)
    cud.set(3, 3)
    cud.set(0, 3)
    println("Expected 3 Actual:" + cud.get(0, 0) )
    println("Expected 0 Actual:" + cud.get(7, 0) )
    cud.snap()
    cud.set(0, 4)
    cud.snap()

    println("Expected 3 Actual:" + cud.get(0, 0) )
    println("Expected 4 Actual:" + cud.get(0, 2) )

}
