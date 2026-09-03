package com.cjbooms.prep.stages.stage13

/**
 * Stage 13.4 — LFU Cache (LeetCode 460).
 *
 * Why this matters for MongoDB: query-cache eviction by access *frequency*
 * (not just recency), hot-index pinning, and buffer-pool page replacement where
 * a page touched many times should survive a one-off scan.
 *
 * Structure-selection ritual:
 *   - LRU cache (see `LruCache.kt`) uses one list ordered by recency. LFU needs
 *     many lists: one per frequency bucket.
 *   - HashMap<K, Node>: O(1) key lookup and stores current frequency.
 *   - HashMap<Int, LinkedHashSet<K>>: frequency -> keys, with insertion order
 *     giving LRU *within* a bucket. The first key in the set is the next victim.
 *   - `minFreq` tracker: the eviction candidate is always in the `minFreq`
 *     bucket. Update `minFreq` on every operation that could lower it.
 *
 * Invariant: after a `put` that replaces an existing key, the key's frequency
 * resets to 1. This differs from the LeetCode default (where re-put keeps the
 * frequency) — the test suite checks the reset behaviour.
 *
 * Time budget: 30 min.
 */
class LfuCache<K, V>(private val capacity: Int) {

    init {
        require(capacity > 0) { "capacity must be positive, was $capacity" }
    }

    /**
     * Returns the value for [key] and increments its frequency, or null if absent. O(1).
     */
    fun get(key: K): V? {
        TODO("implement")
    }

    /**
     * Inserts or refreshes [key] = [value]. If [key] already exists, its value is
     * updated and its frequency resets to 1. If capacity is exceeded, evicts the
     * least-frequently-used key, breaking ties by LRU within the lowest frequency
     * bucket. O(1).
     */
    fun put(key: K, value: V) {
        TODO("implement")
    }
}
