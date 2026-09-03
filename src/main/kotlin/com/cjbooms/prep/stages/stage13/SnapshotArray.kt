package com.cjbooms.prep.stages.stage13

/**
 * Stage 13.6 — Snapshot array (15 min).
 *
 * Why this matters for MongoDB: point-in-time reads of mutable arrays or
 * per-shard state, versioned config metadata, and "as of timestamp" oplog
 * queries. Snap IDs are dense ints, which is a structural hint.
 *
 * Structure-selection ritual:
 *   - A naive copy-on-snap is O(n) per snap and wastes memory.
 *   - Per-index list of (snapId, value) turns each index into a versioned
 *     timeline. `get(index, snapId)` becomes "greatest snapId <= snapId",
 *     i.e. a floor query.
 *   - `java.util.TreeMap.floorEntry` handles this, but snap IDs are dense
 *     integers (0, 1, 2, ...), so a plain ArrayList + binary search is faster
 *     and simpler: the index *is* not the snapId, but the list position gives
 *     the floor in O(log versions) with less overhead than a red-black tree.
 *
 * Time budget: 15 min.
 */
class SnapshotArray(length: Int) {

    init {
        require(length >= 0) { "length must be non-negative, was $length" }
    }

    /** Records [value] at [index] for the current snapId. O(1) amortized. */
    fun set(index: Int, value: Int) {
        TODO("implement")
    }

    /** Takes a snapshot and returns the snapId. O(n) naive; target O(1). */
    fun snap(): Int {
        TODO("implement")
    }

    /**
     * Returns the value at [index] as of [snapId]. If nothing was ever set at
     * that index, returns 0. O(log v) where v = versions for that index.
     */
    fun get(index: Int, snapId: Int): Int {
        TODO("implement")
    }
}
