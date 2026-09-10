package com.cjbooms.prep.solutions.stage13

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

    private class Node<K, V>(val key: K, var value: V, var freq: Int = 1)

    private val nodes = hashMapOf<K, Node<K, V>>()
    // freq -> keys in insertion order; insertion order gives LRU within a bucket.
    private val bucket = HashMap<Int, LinkedHashSet<K>>()
    // the next victim is always in the minFreq bucket; first() of that set.
    private var minFreq = 0

    private fun bucket(freq: Int): LinkedHashSet<K> = bucket.getOrPut(freq) { LinkedHashSet() }

    private fun touch(node: Node<K, V>) {
        val oldBucket = bucket[node.freq]!!
        oldBucket.remove(node.key)
        if (oldBucket.isEmpty() && node.freq == minFreq) {
            // no key left at the current minFreq; bump it.
            minFreq += 1
        }
        node.freq += 1
        bucket(node.freq).add(node.key)
    }

    private fun evictLeastFrequent() {
        val victims = bucket[minFreq] ?: return
        val victimKey = victims.first()
        victims.remove(victimKey)
        nodes.remove(victimKey)
        if (victims.isEmpty()) bucket.remove(minFreq)
    }

    /**
     * Returns the value for [key] and increments its access frequency, or
     * `null` if [key] is not present.
     */
    fun get(key: K): V? {
        val node = nodes[key] ?: return null
        touch(node)
        return node.value
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
        val existing = nodes[key]
        if (existing != null) {
            existing.value = value
            // re-put resets frequency to 1: drop from current bucket, add to freq 1.
            bucket[existing.freq]?.remove(existing.key)
            existing.freq = 1
            bucket(1).add(existing.key)
            minFreq = 1
            return
        }
        if (nodes.size == capacity) evictLeastFrequent()
        val node = Node(key = key, value = value, freq = 1)
        nodes[key] = node
        bucket(1).add(key)
        minFreq = 1
    }
}

fun main() {
    data class Test(val case: String, val expected: Any?, val actual: Any?) {
        init {
            if (expected != actual) println("FAILED: $this")
            else println("PASSED: $this")
        }
    }

    // least-frequent eviction: key 1 has freq 2, key 2 has freq 1; inserting 3 evicts key 2.
    val cut = LfuCache<Int, String>(capacity = 2)
    cut.put(1, "a")
    cut.put(2, "b")
    cut.get(1)
    cut.put(3, "c")
    Test("least-frequently-used key is evicted", null, cut.get(2))
    Test("higher-frequency key survives", "a", cut.get(1))
    Test("newly inserted key is present", "c", cut.get(3))

    // tie on frequency: both keys at freq 1; key 1 was put first, so it is LRU and gets evicted.
    val tied = LfuCache<Int, String>(capacity = 2)
    tied.put(1, "a")
    tied.put(2, "b")
    tied.put(3, "c")
    Test("LRU tie-break evicts oldest within the freq-1 bucket", null, tied.get(1))
    Test("later-inserted tied-frequency key survives", "b", tied.get(2))

    // re-put resets frequency to 1: after re-put, key 1 is the most-recently touched at freq 1,
    // so key 2 is the LRU within the freq-1 bucket and gets evicted.
    val reset = LfuCache<Int, String>(capacity = 2)
    reset.put(1, "a")
    reset.put(2, "b")
    reset.get(1)
    reset.get(1)
    reset.put(1, "a2")
    reset.put(3, "c")
    Test("re-put resets freq to 1; re-put key survives", "a2", reset.get(1))
    Test("LRU tie-break evicts the key that was not re-put", null, reset.get(2))
    Test("newly inserted key is present", "c", reset.get(3))

    // absent key: get returns null without disturbing anything.
    val sparse = LfuCache<Int, String>(capacity = 2)
    sparse.put(1, "a")
    Test("get on missing key returns null", null, sparse.get(2))
    Test("get on missing key does not affect existing entries", "a", sparse.get(1))
}
