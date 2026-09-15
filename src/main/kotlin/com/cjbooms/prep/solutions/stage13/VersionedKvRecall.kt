package com.cjbooms.prep.solutions.stage13

import java.util.TreeMap

/**
 * Stage 13.7 — Versioned KV store cold recall (15 min).
 *
 * Why this matters: versioned document reads, operation log replay,
 * "as of" timestamp queries, and event stream semantics all rely on retrieving
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

    private val versionsByDoc = HashMap<String, TreeMap<Long, String>>()

    fun put(docId: String, contents: String, timestamp: Long) {
        val docVersions = versionsByDoc.getOrPut(docId) { TreeMap() }
        docVersions[timestamp] = contents
    }

    fun get(docId: String, timestamp: Long): String? {
        val docVersions = versionsByDoc[docId] ?: return null
        return docVersions.floorEntry(timestamp)?.value
    }
}
