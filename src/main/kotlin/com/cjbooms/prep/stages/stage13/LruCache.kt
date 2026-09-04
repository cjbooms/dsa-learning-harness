package com.cjbooms.prep.stages.stage13

/**
 * Learn first: see docs/learning-resources.md
 * LRU (Least Recently Used) cache with a fixed capacity.
 *
 * Supports `get` and `put` in O(1). A successful `get` marks the entry as
 * most-recently-used. When `put` would exceed capacity, the
 * least-recently-used entry is evicted. `get` of an absent key returns
 * `null` and does not change recency ordering. This implementation is not
 * thread-safe.
 *
 * @param K key type.
 * @param V value type.
 * @property capacity maximum number of entries the cache can hold. Must be
 *   positive.
 */
class LruCache<K, V>(private val capacity: Int) {

    init {
        require(capacity > 0) { "capacity must be positive, was $capacity" }
    }

    /**
     * Returns the value for [key] and marks it as most-recently-used,
     * or `null` if [key] is not present.
     */
    fun get(key: K): V? {
        TODO("implement")
    }

    /**
     * Inserts or refreshes the mapping [key] = [value]. If the cache is
     * at capacity afterwards, the least-recently-used entry is evicted.
     */
    fun put(key: K, value: V) {
        TODO("implement")
    }

    /** Current entry count. */
    val size: Int
        get() {
            TODO("implement")
        }
}
