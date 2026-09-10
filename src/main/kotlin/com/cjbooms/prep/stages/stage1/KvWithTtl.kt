package com.cjbooms.prep.stages.stage1

import java.util.PriorityQueue

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

    val cache = hashMapOf<String, Pair<String, Long>>()
    val expiryQueue = PriorityQueue<Pair<Long, String>>(compareBy { it.first})
    val periodicCleanPeriod = 10_000L
    var nextCleanTime: Long = Long.MAX_VALUE

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
        if (nextCleanTime == Long.MAX_VALUE) nextCleanTime = nowMillis + periodicCleanPeriod
        val expiryTime = nowMillis + ttlMillis
        cache[key] = value to expiryTime
        expiryQueue.add(expiryTime to key)
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
        if (cache.containsKey(key)) {
            maybeEvictCacheEntry(key, nowMillis)
        }
        if (nextCleanTime <= nowMillis) removeExpiredKeys(nowMillis)
        return cache[key]?.first
    }

    fun removeExpiredKeys(nowMills: Long) {
        while (expiryQueue.peek()?.first?.let { it <= nowMills } ?: false ) {
            val potentiallyExpiredEntry = expiryQueue.poll()
            maybeEvictCacheEntry(potentiallyExpiredEntry.second, nowMills)
        }
        nextCleanTime = nextCleanTime + periodicCleanPeriod
    }

    private fun maybeEvictCacheEntry(key: String, nowMills: Long) {
        val entry = cache[key]
        if (entry != null && entry.second <= nowMills) {
            cache.remove(key)
        }
    }
}

fun main() {
    data class Test(val desc: String, val expected: String?, val actual: String?) {
        fun check() =
            if (expected?.trim() != actual?.trim()) println("FAILED: $this")
            else println("PASSED: $this")
    }

    val cud = KvWithTtl()

    cud.put("1", "one", 1000, 0L)
    Test("Should Find", "one", cud.get("1", 0L)).check()
    Test("Should not be expired on boundary - 1", "one", cud.get("1", 999L)).check()
    Test("Should be expired on boundary", null, cud.get("1", 1000L)).check()
    cud.put("1", "one", 1000, 1000L)

}
