package com.cjbooms.prep.solutions.stage13

import java.util.TreeMap

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

    // Virtual-node hash -> real node.
    private val ring = TreeMap<Int, T>()
    // Real node -> its virtual-node hashes (so removal is O(replicas) not O(ring)).
    private val hashesByNode = HashMap<T, MutableList<Int>>()
    // Real nodes in insertion order; used for nodeCount and stable iteration.
    private val realNodes = LinkedHashSet<T>()

    /**
     * Adds a node [node] to the ring. Subsequent lookups may now return it.
     * Re-adding an already-present node is a no-op.
     */
    fun add(node: T) {
        if (!realNodes.add(node)) return
        val hashes = ArrayList<Int>(replicasPerNode)
        for (replicaIndex in 0 until replicasPerNode) {
            val hash = stableHash("$node#$replicaIndex")
            // Hash collisions across replicas are vanishingly rare with MD5,
            // but if one occurs, the later replica overwrites — still correct
            // because both map to the same real node.
            ring[hash] = node
            hashes.add(hash)
        }
        hashesByNode[node] = hashes
    }

    /**
     * Removes [node] (and all its virtual replicas) from the ring.
     * Removing a node that isn't present is a no-op.
     */
    fun remove(node: T) {
        if (!realNodes.remove(node)) return
        val hashes = hashesByNode.remove(node) ?: return
        for (hash in hashes) ring.remove(hash)
    }

    /**
     * Returns the node responsible for [key]. Always non-null once at
     * least one node has been added. O(log N).
     *
     * Pre-condition: at least one node has been added (caller's responsibility
     * per the KDoc; throwing makes misuse loud rather than silent).
     */
    fun getNode(key: String): T {
        check(realNodes.isNotEmpty()) { "getNode called on empty ring" }
        val keyHash = stableHash(key)
        val ceiling = ring.ceilingEntry(keyHash)
        if (ceiling != null) return ceiling.value
        // Wrap around: the first entry on the ring owns everything from the
        // largest virtual hash up to 2^32.
        return ring.firstEntry()!!.value
    }

    /** Number of real nodes currently in the ring. */
    val nodeCount: Int
        get() = realNodes.size

    /** Total number of entries on the hash ring (real + virtual). */
    val ringSize: Int
        get() = ring.size

    /**
     * Stable 32-bit hash. Uses MD5 truncated to 4 bytes so the ring layout
     * is deterministic across runs (String.hashCode would do, but explicit
     * hashing makes the "consistent" part of "consistent hashing" literal).
     */
    private fun stableHash(input: String): Int {
        val digest = md5Digest(input)
        // Take the low 32 bits as the ring position.
        return ((digest[3].toInt() and 0xFF) shl 24) or
            ((digest[2].toInt() and 0xFF) shl 16) or
            ((digest[1].toInt() and 0xFF) shl 8) or
            (digest[0].toInt() and 0xFF)
    }

    companion object {
        private fun md5Digest(input: String): ByteArray {
            // java.security.MessageDigest is overkill for a ring layout, but
            // it's already on the JVM and gives us the determinism we want.
            // We could also use String.hashCode, but cross-JVM the spec only
            // guarantees stability within a JVM run.
            val md = java.security.MessageDigest.getInstance("MD5")
            return md.digest(input.toByteArray(Charsets.UTF_8))
        }
    }
}
