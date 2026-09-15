package com.cjbooms.prep.solutions.stage13

/**
 * Stage 13.6 — Snapshot array (15 min).
 *
 * Why this matters: point-in-time reads of mutable arrays or
 * per-shard state, versioned config metadata, and "as of timestamp" operation log
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

    private val versions = Array<MutableList<Pair<Int, Int>>>(length) { mutableListOf() }
    private var snapId: Int = 0

    fun set(index: Int, value: Int) {
        require(index in versions.indices)
        val history = versions[index]
        if (history.isNotEmpty() && history.last().first == snapId) {
            history[history.size - 1] = snapId to value
        } else {
            history.add(snapId to value)
        }
    }

    fun snap(): Int {
        return snapId++
    }

    fun get(index: Int, snapId: Int): Int {
        require(index in versions.indices)
        val history = versions[index]
        if (history.isEmpty()) return 0

        val idx = history.binarySearchBy(snapId) { it.first }
        return if (idx >= 0) {
            history[idx].second
        } else {
            val insertionPoint = -idx - 1
            if (insertionPoint == 0) 0 else history[insertionPoint - 1].second
        }
    }
}
