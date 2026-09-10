package com.cjbooms.prep.stages.stage13

import java.util.PriorityQueue
import java.util.TreeMap

/**
 * Learn first: see docs/learning-resources.md
 * Versioned key-value store with point-in-time ("as of") recall.
 *
 * Each `put` records a version of a document at an associated timestamp.
 * Timestamps may arrive out of order. `get` returns the contents of the
 * latest version of a document whose timestamp is less than or equal to
 * the requested timestamp, or `null` if no such version exists.
 */
class VersionedKvRecall {

    val documentVersions = hashMapOf<String, TreeMap<Long, String>>()

    /**
     * Records a version of [docId] with [contents] at [timestamp]. Puts for
     * the same [docId] may arrive out of order.
     */
    fun put(docId: String, contents: String, timestamp: Long) {
        val docVersions = documentVersions.getOrPut(docId) {
            TreeMap<Long, String>()
        }
        docVersions[timestamp] = contents
    }

    /**
     * Returns the contents of the latest version of [docId] with timestamp
     * `<= [timestamp]`, or `null` if [docId] has no such version.
     */
    fun get(docId: String, timestamp: Long): String? {
        return documentVersions[docId]?.let { docVersions ->
            docVersions.floorEntry(timestamp)?.value
        }
    }
}

fun main() {
    data class Test(val case: String, val expected: String?, val actual: String?) {
        init {
            if (expected != actual) println("FAILED: $this")
            else println("PASSED: $this")
        }
    }

    val cud = VersionedKvRecall()
    cud.put("id1", "one", 0)
    cud.put("id1", "one-1", 2)
    cud.put("id1", "one-2", 1)
    Test("Correct timestamp doc found", "one-2", cud.get("id1", 1))
    Test("Correct timestamp doc found when out of order", "one-1", cud.get("id1", 2))
    cud.put("id1", "one-latest", 3)
    cud.put("id1", "one-3", 2)
    Test("Correct timestamp doc found when overwritten", "one-3", cud.get("id1", 2))

    Test("Nearest timestamp found", "one-latest", cud.get("id1", 100))

    cud.put("id2", "one", 100)
    Test("Not Found Case before time", null, cud.get("id2", 1))
    Test("Not Found Case when absent", null, cud.get("notFound", 0))


}
