package com.cjbooms.prep.stages.stage13

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
class SnapshotArray(length: Int) {

    init {
        require(length >= 0) { "length must be non-negative, was $length" }
    }

    /**
     * Records [value] at [index] for the current snapshot.
     */
    fun set(index: Int, value: Int) {
        TODO("implement")
    }

    /**
     * Takes a snapshot of the current array state and returns its snapshot
     * id. The id is unique per snapshot and monotonically increasing.
     */
    fun snap(): Int {
        TODO("implement")
    }

    /**
     * Returns the value at [index] as of snapshot [snapId]. If nothing was
     * ever set at [index] before that snapshot, returns 0.
     */
    fun get(index: Int, snapId: Int): Int {
        TODO("implement")
    }
}
