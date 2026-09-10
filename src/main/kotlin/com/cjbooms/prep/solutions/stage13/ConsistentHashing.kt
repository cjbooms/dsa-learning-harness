package com.cjbooms.prep.solutions.stage13

import java.util.TreeMap

/**
 * Learn first: see docs/learning-resources.md
 * Consistent hashing with virtual nodes.
 *
 * Maintains a hash ring over a set of nodes. Each real node is placed on the
 * ring multiple times (virtual replicas) so that keys are distributed evenly
 * and adding or removing a node only reshuffles roughly 1/N of the keys,
 * rather than almost all of them as a naive `key.hashCode() % N` would.
 *
 * @param T the node type stored on the ring.
 * @property replicasPerNode number of virtual replicas placed per real node.
 *   Must be positive.
 */
class ConsistentHashing<T>(
    private val replicasPerNode: Int = 200,
) {

    init {
        require(replicasPerNode > 0) { "replicasPerNode must be positive, was $replicasPerNode" }
    }

    // virtual-node hash -> owning real node.
    private val ring = TreeMap<Int, T>()
    // real node -> its virtual hashes so removal scans only replicasPerNode entries.
    private val hashesByNode = HashMap<T, MutableList<Int>>()
    // real nodes in insertion order; stable for nodeCount and iteration.
    private val realNodes = LinkedHashSet<T>()

    /**
     * Adds [node] to the ring. Subsequent lookups may now return it.
     * Re-adding an already-present node is a no-op.
     */
    fun add(node: T) {
        if (!realNodes.add(node)) return
        val hashes = ArrayList<Int>(replicasPerNode)
        for (replica in 0 until replicasPerNode) {
            val hash = stableHash("$node#$replica")
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
     * least one node has been added.
     *
     * Pre-condition: at least one node has been added; behaviour is
     * undefined otherwise.
     */
    fun getNode(key: String): T {
        check(realNodes.isNotEmpty()) { "getNode called on empty ring" }
        val keyHash = stableHash(key)
        val ceiling = ring.ceilingEntry(keyHash)
        if (ceiling != null) return ceiling.value
        // wrap: first entry on the ring owns everything past the largest hash.
        return ring.firstEntry()!!.value
    }

    /** Number of real nodes currently in the ring. */
    val nodeCount: Int
        get() = realNodes.size

    /** Total number of entries on the hash ring (real + virtual). */
    val ringSize: Int
        get() = ring.size

    // stable 32-bit hash so the ring layout is deterministic across runs.
    private fun stableHash(input: String): Int {
        val digest = md5(input)
        return ((digest[3].toInt() and 0xFF) shl 24) or
            ((digest[2].toInt() and 0xFF) shl 16) or
            ((digest[1].toInt() and 0xFF) shl 8) or
            (digest[0].toInt() and 0xFF)
    }

    companion object {
        private fun md5(input: String): ByteArray {
            val md = java.security.MessageDigest.getInstance("MD5")
            return md.digest(input.toByteArray(Charsets.UTF_8))
        }
    }
}

fun main() {
    data class Test(val case: String, val expected: Any?, val actual: Any?) {
        init {
            if (expected != actual) println("FAILED: $this")
            else println("PASSED: $this")
        }
    }

    val ring = ConsistentHashing<String>(replicasPerNode = 4)
    listOf("n1", "n2", "n3").forEach { ring.add(it) }
    Test("nodeCount after three adds", 3, ring.nodeCount)
    Test("ringSize = nodes * replicasPerNode", 12, ring.ringSize)

    // virtual nodes spread the ring: with enough replicas each node owns some keys.
    val distributed = ConsistentHashing<String>()
    listOf("n1", "n2", "n3").forEach { distributed.add(it) }
    val owners = (0 until 600).map { distributed.getNode("key-$it") }.toSet()
    Test("virtual nodes spread the ring across all nodes", setOf("n1", "n2", "n3"), owners)

    // re-adding an already-present node is a no-op.
    distributed.add("n1")
    Test("re-adding same node leaves nodeCount unchanged", 3, distributed.nodeCount)
    Test("re-adding same node leaves ringSize unchanged", 600, distributed.ringSize)

    // after removing a node, its keys must move to a remaining node.
    val beforeRemove = mutableMapOf<String, MutableList<String>>()
    for (i in 0 until 600) {
        val key = "key-$i"
        val owner = distributed.getNode(key)
        beforeRemove.getOrPut(owner) { mutableListOf() }.add(key)
    }
    val keysForRemovedNode = beforeRemove["n2"] ?: emptyList()
    Test("nodeCount shrinks after remove", 2, distributed.nodeCount)
    Test("ringSize shrinks by replicasPerNode", 400, distributed.ringSize)
    // every key that n2 used to own must now belong to one of the remaining nodes.
    val reassignedToRemoved = keysForRemovedNode.count { distributed.getNode(it) == "n2" }
    Test("no key of the removed node still maps to it", 0, reassignedToRemoved)

    // removing a missing node is a no-op.
    distributed.remove("n2")
    Test("removing an already-removed node is a no-op", 2, distributed.nodeCount)
    distributed.remove("never-added")
    Test("removing a never-added node is a no-op", 2, distributed.nodeCount)
}
