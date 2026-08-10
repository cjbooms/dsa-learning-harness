package com.cjbooms.prep.stages.stage1

/**
 * Stage 1.4 — In-memory KV with TTL (REPORTED question; cousin of the
 * expiring queue from Stage 0).
 *
 * put(key, value, ttlMillis): key is readable for ttlMillis from NOW.
 * get(key, nowMillis): null if missing OR expired.
 *
 * The structure ritual matters most here:
 *   - Lazy expiry (check on read) vs eager expiry (background/scan on write)?
 *   - What does each cost? What happens with 10M keys, 1% read rate?
 *   - If you keep an expiry-ordered index alongside the map — how do you
 *     handle a key being re-put with a different TTL? (You've solved this
 *     shape before — Stage: ReplicationLagAlerter.)
 */
class KvWithTtl {

    fun put(key: String, value: String, ttlMillis: Long, nowMillis: Long) {
        TODO()
    }

    fun get(key: String, nowMillis: Long): String? {
        TODO()
    }
}
