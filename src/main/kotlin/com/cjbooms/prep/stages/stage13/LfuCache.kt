package com.cjbooms.prep.stages.stage13

/**
 * Learn first: see docs/learning-resources.md
 * LFU (Least Frequently Used) cache with a fixed capacity.
 *
 * Supports `get` and `put` in O(1). Eviction removes the entry with the
 * lowest access frequency; ties are broken by least-recently-used among
 * entries that share that lowest frequency. When `put` updates an existing
 * key's value, the key's frequency resets to 1.
 *
 * @param K key type.
 * @param V value type.
 * @property capacity maximum number of entries the cache can hold. Must be
 *   positive.
 */
class LfuCache<K, V>(private val capacity: Int) {

    init {
        require(capacity > 0) { "capacity must be positive, was $capacity" }
    }

    /**
     * Returns the value for [key] and increments its access frequency, or
     * `null` if [key] is not present.
     */
    fun get(key: K): V? {
        TODO("implement")
    }

    /**
     * Inserts or refreshes the mapping [key] = [value].
     *
     * - If [key] is already present, its value is replaced and its frequency
     *   resets to 1.
     * - If inserting would exceed [capacity], evicts the least-frequently-used
     *   entry; ties are broken by least-recently-used within the lowest
     *   frequency bucket.
     */
    fun put(key: K, value: V) {
        TODO("implement")
    }
}
