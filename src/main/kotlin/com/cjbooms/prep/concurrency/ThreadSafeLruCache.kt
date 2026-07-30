package com.cjbooms.prep.concurrency

import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

/**
 * Thread-safe LRU cache — the classic "implement a thread-safe data structure" prompt.
 *
 * LinkedHashMap(accessOrder = true) gives O(1) LRU ordering; a lock guards all access.
 *
 * Talking points for follow-ups:
 *  - Single lock vs lock striping (ConcurrentHashMap-style) vs StampedLock.
 *  - Why a plain ConcurrentHashMap alone is NOT enough (atomicity of the
 *    get-and-move-to-front composite operation; eviction must be atomic w.r.t. puts).
 *  - ReadWriteLock shown here: reads still mutate recency order, so get() needs the
 *    WRITE lock with accessOrder=true — a great "gotcha" to mention proactively.
 */
class ThreadSafeLruCache<K, V>(private val maxSize: Int) {
    init {
        require(maxSize > 0) { "maxSize must be positive" }
    }

    private val lock = ReentrantReadWriteLock()
    private val map = object : LinkedHashMap<K, V>(maxSize, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, V>?): Boolean =
            size > maxSize
    }

    fun get(key: K): V? = lock.write { map[key] } // write lock: access reorders entries

    fun put(key: K, value: V) {
        lock.write { map[key] = value }
    }

    val size: Int
        get() = lock.read { map.size }
}
