package com.cjbooms.prep.stages.stage13

/**
 * Versioned key-value store with point-in-time ("as of") recall.
 *
 * Each `put` records a version of a document at an associated timestamp.
 * Timestamps may arrive out of order. `get` returns the contents of the
 * latest version of a document whose timestamp is less than or equal to
 * the requested timestamp, or `null` if no such version exists.
 */
class VersionedKvRecall {

    /**
     * Records a version of [docId] with [contents] at [timestamp]. Puts for
     * the same [docId] may arrive out of order.
     */
    fun put(docId: String, contents: String, timestamp: Long) {
        TODO("implement")
    }

    /**
     * Returns the contents of the latest version of [docId] with timestamp
     * `<= [timestamp]`, or `null` if [docId] has no such version.
     */
    fun get(docId: String, timestamp: Long): String? {
        TODO("implement")
    }
}
