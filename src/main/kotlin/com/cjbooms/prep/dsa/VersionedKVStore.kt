package com.cjbooms.prep.dsa

import java.util.TreeMap

/**
 * Classic system-adjacent DSA question.
 *
 * A versioned document store:
 *  - put(docId, contents, timestamp): record a version; puts may arrive OUT OF ORDER.
 *  - get(docId, timestamp): contents of the latest version with versionTs <= timestamp,
 *    or null if the doc didn't exist yet / doesn't exist.
 *
 * Approach:
 *   HashMap<docId, TreeMap<timestamp, contents>>
 *
 *   The outer HashMap gives O(1) doc lookup. The inner TreeMap (a red-black tree)
 *   keeps versions sorted by timestamp, which is exactly what handles out-of-order
 *   puts, and its floorEntry() does a binary search for "greatest key <= timestamp".
 *
 *   put: O(log v), get: O(log v)   where v = number of versions of that doc.
 *
 * Interview note: if they push on "what if there are billions of docs", the shape
 * doesn't change — the TreeMap is per-doc, so its size is versions-of-one-doc,
 * not total docs.
 */
class VersionedKVStore {

    // docId -> (timestamp -> contents), versions kept in ascending timestamp order
    private val versionsByDoc = HashMap<String, TreeMap<Long, String>>()

    fun put(docId: String, contents: String, timestamp: Long) {
        val docVersions = versionsByDoc.getOrPut(docId) { TreeMap() }
        // TreeMap assignment inserts in sorted position regardless of arrival
        // order — this is what makes out-of-order puts a non-issue.
        docVersions[timestamp] = contents
    }

    fun get(docId: String, timestamp: Long): String? {
        val docVersions = versionsByDoc[docId] ?: return null

        // floorEntry = the greatest timestamp <= the requested one, i.e. the
        // version that was current "as of" that moment. Null if every version
        // is newer than the requested timestamp.
        val versionAsOfTimestamp = docVersions.floorEntry(timestamp) ?: return null

        return versionAsOfTimestamp.value
    }
}
