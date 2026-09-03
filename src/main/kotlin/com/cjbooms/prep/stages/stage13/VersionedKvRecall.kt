package com.cjbooms.prep.stages.stage13

/**
 * Stage 13.7 — Versioned KV store cold recall (15 min).
 *
 * Why this matters for MongoDB: versioned document reads, oplog replay,
 * "as of" timestamp queries, and change-stream semantics all rely on retrieving
 * the most recent value at or before a given timestamp.
 *
 * Reference implementation:
 *   `src/main/kotlin/com/cjbooms/prep/dsa/VersionedKVStore.kt`
 *
 * Structure-selection ritual:
 *   - Outer HashMap<String, TreeMap<Long, String>>: O(1) doc lookup, then a
 *     balanced BST ordered by timestamp.
 *   - Out-of-order puts are fine because the TreeMap sorts by key on insert.
 *   - `floorEntry(timestamp)` returns the newest version whose timestamp is <=
 *     the requested one — exactly "as of" semantics.
 *
 * Time budget: 15 min. Do not peek at the reference until after you attempt it.
 */
class VersionedKvRecall {

    /**
     * Records a version of [docId] with [contents] at [timestamp]. Puts may arrive
     * out of order. O(log v) where v = versions of this doc.
     */
    fun put(docId: String, contents: String, timestamp: Long) {
        TODO("implement")
    }

    /**
     * Returns the contents of the latest version of [docId] with timestamp <=
     * [timestamp], or null if the doc has no such version. O(log v).
     */
    fun get(docId: String, timestamp: Long): String? {
        TODO("implement")
    }
}
