package com.cjbooms.prep.stages.stage1

/**
 * Learn first: see docs/learning-resources.md
 * An in-memory key-value store where each entry expires after a caller-supplied
 * time-to-live.
 *
 * `put(key, value, ttlMillis, nowMillis)` records [value] under [key] and
 * makes it readable for [ttlMillis] milliseconds starting at [nowMillis].
 *
 * `get(key, nowMillis)` returns the stored value if [key] is present and has
 * not expired by [nowMillis], or `null` if [key] is missing or expired.
 */
class KvWithTtl {

    /**
     * Stores [value] under [key] with a time-to-live of [ttlMillis]
     * milliseconds from [nowMillis]. A later `put` for the same key replaces
     * any prior value and resets its expiry.
     *
     * @param key the key to write
     * @param value the value to associate with [key]
     * @param ttlMillis how long, in milliseconds, the entry should remain
     *   readable from [nowMillis]
     * @param nowMillis the current time in milliseconds
     */
    fun put(key: String, value: String, ttlMillis: Long, nowMillis: Long) {
        TODO()
    }

    /**
     * Looks up the value associated with [key], returning `null` if the key
     * is missing or its entry has expired by [nowMillis].
     *
     * @param key the key to look up
     * @param nowMillis the current time in milliseconds
     * @return the stored value, or `null` if missing or expired
     */
    fun get(key: String, nowMillis: Long): String? {
        TODO()
    }
}
