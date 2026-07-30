package com.cjbooms.prep.dsa

import java.util.TreeMap

/**
 * Reported MongoDB interview question (2026, SWE round 1).
 *
 * A versioned document store:
 *  - put(docId, contents, timestamp) records a version; puts may arrive OUT OF ORDER.
 *  - get(docId, timestamp) returns the contents of the latest version with
 *    versionTimestamp <= timestamp, or null if none exists.
 *
 * Approach: per-docId TreeMap<timestamp, contents>; get = floorEntry (binary search).
 * put: O(log v), get: O(log v) where v = versions of that doc.
 */
class VersionedKVStore {
    private val docs = HashMap<String, TreeMap<Long, String>>()

    fun put(docId: String, contents: String, timestamp: Long) {
        docs.getOrPut(docId) { TreeMap() }[timestamp] = contents
    }

    fun get(docId: String, timestamp: Long): String? =
        docs[docId]?.floorEntry(timestamp)?.value
}
