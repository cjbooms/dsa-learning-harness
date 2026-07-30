package com.cjbooms.prep.concurrency

import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

/**
 * Thread-safe LRU cache — the classic "implement a thread-safe data structure" prompt.
 *
 * Building blocks:
 *   LinkedHashMap with accessOrder = true gives us LRU ordering for free:
 *   every get() moves the entry to the "most recent" end, and the head is
 *   always the least-recently-used entry. removeEldestEntry() is the eviction hook.
 *
 * THE INTERVIEW TRAP (say this unprompted, it lands well):
 *   get() is NOT a read-only operation here — it mutates the recency order.
 *   So even with a ReadWriteLock, get() must take the WRITE lock. Taking the
 *   read lock "because it's a getter" would corrupt the linked list under
 *   concurrent access. This is why the RWLock buys us nothing in this design;
 *   it's shown to make that exact point.
 *
 * Follow-ups to expect:
 *   - "Why not just ConcurrentHashMap?" -> it can't atomically do
 *     get-and-move-to-front, and eviction must be atomic w.r.t. concurrent puts.
 *   - "How would you scale it?" -> lock striping: N independent segments each
 *     with its own small LRU (loses global LRU, wins throughput — name the trade).
 *   - "Add per-entry TTL" -> store expiry beside the value; check on get,
 *     sweep on put.
 */
class ThreadSafeLruCache<K, V>(private val maxSize: Int) {

    init {
        require(maxSize > 0) { "maxSize must be positive, was $maxSize" }
    }

    private val lock = ReentrantReadWriteLock()

    private val entriesByKey = object : LinkedHashMap<K, V>(
        /* initialCapacity = */ maxSize,
        /* loadFactor = */ 0.75f,
        /* accessOrder = */ true, // iteration/insertion order tracks ACCESS, not insertion
    ) {
        // Called after every put; returning true evicts the LRU (head) entry.
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, V>?): Boolean =
            size > maxSize
    }

    fun get(key: K): V? {
        // WRITE lock on purpose: with accessOrder=true, a hit re-links the entry
        // to the most-recently-used position — a structural mutation.
        return lock.write { entriesByKey[key] }
    }

    fun put(key: K, value: V) {
        lock.write {
            // Assignment inserts or refreshes; if size now exceeds maxSize,
            // removeEldestEntry above evicts the LRU entry within the same
            // atomic section.
            entriesByKey[key] = value
        }
    }

    val size: Int
        get() = lock.read { entriesByKey.size } // size is a genuine read
}
