package com.cjbooms.prep.solutions.stage13

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

    private class Node<K, V>(val key: K, var value: V, var freq: Int = 1)

    private val keyMap = HashMap<K, Node<K, V>>()
    private val freqMap = HashMap<Int, LinkedHashSet<K>>()
    private var minFreq: Int = 0

    private fun bucket(freq: Int): LinkedHashSet<K> = freqMap.getOrPut(freq) { LinkedHashSet() }

    private fun increment(node: Node<K, V>) {
        val oldBucket = freqMap[node.freq]!!
        oldBucket.remove(node.key)
        if (oldBucket.isEmpty() && node.freq == minFreq) {
            minFreq++
        }
        node.freq++
        bucket(node.freq).add(node.key)
    }

    private fun reset(node: Node<K, V>) {
        freqMap[node.freq]?.remove(node.key)
        node.freq = 1
        bucket(1).add(node.key)
        minFreq = 1
    }

    private fun evict() {
        val victimBucket = freqMap[minFreq] ?: return
        val victimKey = victimBucket.first()
        victimBucket.remove(victimKey)
        keyMap.remove(victimKey)
        if (victimBucket.isEmpty()) {
            freqMap.remove(minFreq)
        }
    }

    fun get(key: K): V? {
        val node = keyMap[key] ?: return null
        increment(node)
        return node.value
    }

    fun put(key: K, value: V) {
        val existing = keyMap[key]
        if (existing != null) {
            existing.value = value
            reset(existing)
            return
        }
        if (keyMap.size == capacity) {
            evict()
        }
        val node = Node(key = key, value = value, freq = 1)
        keyMap[key] = node
        bucket(1).add(key)
        minFreq = 1
    }
}
