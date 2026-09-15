package com.cjbooms.prep.solutions.stage13

/**
 * Stage 13.1 — LRU Cache (LeetCode 146).
 *
 * Why this matters: query result caching, session storage,
 * page cache for the storage engine storage engine, and any place we evict
 * "the thing nobody has touched recently" — full-text search query cache,
 * connection-pool idle eviction, Oplog window buffer eviction.
 *
 * Structure-selection ritual:
 *   1. State the operations: get(k) and put(k, v) — both must be O(1).
 *   2. HashMap alone gives O(1) lookup but cannot evict "least recently used"
 *      without scanning the whole key set.
 *   3. Doubly-linked list alone gives O(1) remove-from-middle IF you hold a
 *      node handle — but no O(1) key lookup.
 *   4. COMBINE: HashMap<key, Node> + doubly-linked list of nodes (head = LRU,
 *      tail = MRU). Map gives the node handle, list gives O(1) re-link.
 *
 * Time budget: 25 minutes (implement + 3 tests). See stage doc 13.
 *
 * Implementation notes (this stage is deliberately non-thread-safe — see
 * concurrency/ThreadSafeLruCache.kt for the synchronized variant):
 *   - Doubly-linked list with null endpoints (small, no sentinel allocation).
 *   - `get` returns null AND does NOT touch the list for absent keys.
 *   - `put` of an existing key re-links the existing node (no new allocation).
 */
class LruCache<K, V>(private val capacity: Int) {

    init {
        require(capacity > 0) { "capacity must be positive, was $capacity" }
    }

    private class Node<K, V>(
        val key: K,
        var value: V,
        var prev: Node<K, V>? = null,
        var next: Node<K, V>? = null,
    )

    private val map = HashMap<K, Node<K, V>>(capacity)
    // Doubly-linked list of nodes; head = LRU end, tail = MRU end.
    private var head: Node<K, V>? = null
    private var tail: Node<K, V>? = null

    /** Append [node] at the MRU end. */
    private fun appendMru(node: Node<K, V>) {
        node.prev = tail
        node.next = null
        tail?.next = node
        tail = node
        if (head == null) head = node
    }

    /** Remove [node] from the list; updates head/tail as needed. */
    private fun removeNode(node: Node<K, V>) {
        val prev = node.prev
        val next = node.next
        if (prev != null) prev.next = next else head = next
        if (next != null) next.prev = prev else tail = prev
        node.prev = null
        node.next = null
    }

    /** Move [node] to the MRU end (assumes node is currently in the list). */
    private fun touch(node: Node<K, V>) {
        removeNode(node)
        appendMru(node)
    }

    /**
     * Returns the value for [key] and marks it as most-recently-used,
     * or null if absent. O(1).
     */
    fun get(key: K): V? {
        val node = map[key] ?: return null
        touch(node)
        return node.value
    }

    /**
     * Inserts or refreshes [key] = [value]. If the cache is at capacity
     * afterwards, the least-recently-used entry is evicted. O(1).
     */
    fun put(key: K, value: V) {
        val existing = map[key]
        if (existing != null) {
            existing.value = value
            touch(existing)
            return
        }
        val node = Node(key = key, value = value)
        map[key] = node
        appendMru(node)
        if (map.size > capacity) {
            // Evict LRU (head).
            val lru = head!!
            removeNode(lru)
            map.remove(lru.key)
        }
    }

    /** Current entry count. O(1). */
    val size: Int
        get() = map.size
}
