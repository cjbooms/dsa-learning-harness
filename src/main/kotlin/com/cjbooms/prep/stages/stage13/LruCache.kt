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

    // 1. The custom Node for your Doubly-Linked List
    class Node<K, V>(val key: K?, var value: V?) {
        var prev: Node<K, V>? = null
        var next: Node<K, V>? = null
    }

    // 2. The Map for O(1) lookups
    val cache = HashMap<K, Node<K, V>>()

    // 3. The Sentinels (dummy head and tail)
    private val head = Node<K, V>(null, null) // Most Recently Used end
    private val tail = Node<K, V>(null, null) // Least Recently Used end

    init {
        // Wire the sentinels together initially
        head.next = tail
        tail.prev = head
    }

    // Helper 1: Always add new/accessed nodes right after the dummy head
    private fun moveToHead(node: Node<K, V>) {
        detachNode(node)
        // Insert Node
        node.next = head.next
        node.prev = head
        head.next?.prev = node
        head.next = node

    }

    private fun detachNode(node: Node<K, V>) {
        node.prev?.next = node.next
        node.next?.prev = node.prev
    }

    // Helper 2: Sever a node's prev/next pointers to extract it
    private fun removeNode(node: Node<K, V>) {
        detachNode(node)
        cache.remove(node.key!!)
    }

    // Helper 3: Pop the node right before the dummy tail
    private fun removeTail(): Node<K, V> {
        val actualTail = tail.prev
        removeNode(actualTail!!) // Tail's previous will always point to something
        cache.remove(actualTail.key)
        return actualTail
    }


    /**
     * Returns the value for [key] and marks it as most-recently-used,
     * or `null` if [key] is not present.
     */
    fun get(key: K): V? {
        val value = cache[key]
        if (value != null) {
            moveToHead(value)
        }
        return value?.value
    }

    /**
     * Inserts or refreshes the mapping [key] = [value]. If the cache is
     * at capacity afterwards, the least-recently-used entry is evicted.
     */
    fun put(key: K, value: V) {
        if (cache.containsKey(key)) {
            val existing = cache[key]
            existing!!.value = value
            moveToHead(existing)
        } else {
            while (cache.size >= capacity) {
                removeTail()
            }
            val newNode = Node(key, value)
            cache[key] = newNode
            moveToHead(newNode)
        }
    }

    /** Current entry count. */
    val size: Int
        get() {
            return cache.size
        }
}


fun main() {
    val cud = LruCache<Int, String>(4)

    cud.put(1, "one")
    cud.put(1, "one")
    cud.put(1, "one")
    cud.put(2, "two")
    cud.put(3, "three")
    cud.put(4, "four")
    println("Expect 4, Actual" + cud.size)
    cud.put(5, "five")
    cud.put(6, "six")
    println("Expect 4, Actual" + cud.size)
    println("Expected 3,4,5,6, Actual" + cud.cache.keys)
    cud.get(3)
    cud.put(7, "sevent")
    println("Expected 3,5,6,7 Actual" + cud.cache.keys)


}
