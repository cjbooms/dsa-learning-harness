package com.cjbooms.prep.stages.stage13

/**
 * Stage 13.3 — Consistent hashing with virtual nodes.
 *
 * Why this matters for MongoDB: chunk distribution across shards,
 * config server routing, the CSRS (Config Server Replica Set) hash ring,
 * and any cache layer where adding/removing a node should only
 * reshuffle ~1/N of keys (NOT almost all of them, as naive `key.hashCode() % N`
 * would). It is also the foundation for "mongoS routes a query to the
 * right shard without a central lookup table".
 *
 * Structure-selection ritual:
 *   1. Sorted array of hash -> node, plus binary search for lookup. O(log N)
 *      per key, O(N) to rebalance on add/remove.
 *   2. TreeMap (or SkipListMap) of hash -> node. O(log N) for lookup AND
 *      O(log N) for add/remove. The right tool.
 *   3. Virtual nodes (replicas per real node): hash each real node with
 *      N suffixes (e.g. "nodeA#0" … "nodeA#199"). Without replicas, the
 *      ring is lumpy and one node can own a huge slice; replicas smooth
 *      the distribution at the cost of more entries.
 *
 * Time budget: 15 minutes. See stage doc 13.
 *
 * Implementation:
 *   - TreeMap<Int, T> mapping virtual-node hash -> owning real node.
 *   - On `getNode(key)`: hash the key, find the ceiling entry; if absent,
 *     wrap to the first entry. TreeMap.ceilingEntry is O(log N).
 *   - On `add(node)`: insert `replicasPerNode` entries, hashing
 *     `node#i` for `i in 0 until replicasPerNode`.
 *   - On `remove(node)`: we keep an inverse map (realNode -> list of
 *     virtual hashes) so removal is O(replicasPerNode * log N), not
 *     a full scan of the ring.
 */
class ConsistentHashing<T>(
    private val replicasPerNode: Int = 200,
) {

    init {
        require(replicasPerNode > 0) { "replicasPerNode must be positive, was $replicasPerNode" }
    }

    /**
     * Adds a node [node] to the ring. Subsequent lookups may now return it.
     * Re-adding an already-present node is a no-op.
     */
    fun add(node: T) {
        TODO("implement")
    }

    /**
     * Removes [node] (and all its virtual replicas) from the ring.
     * Removing a node that isn't present is a no-op.
     */
    fun remove(node: T) {
        TODO("implement")
    }

    /**
     * Returns the node responsible for [key]. Always non-null once at
     * least one node has been added. O(log N).
     *
     * Pre-condition: at least one node has been added (caller's responsibility
     * per the KDoc; throwing makes misuse loud rather than silent).
     */
    fun getNode(key: String): T {
        TODO("implement")
    }

    /** Number of real nodes currently in the ring. */
    val nodeCount: Int
        get() {
            TODO("implement")
        }

    /** Total number of entries on the hash ring (real + virtual). */
    val ringSize: Int
        get() {
            TODO("implement")
        }
}
