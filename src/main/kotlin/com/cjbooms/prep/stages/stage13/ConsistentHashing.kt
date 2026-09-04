package com.cjbooms.prep.stages.stage13

/**
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

    /**
     * Adds [node] to the ring. Subsequent lookups may now return it.
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
     * least one node has been added.
     *
     * Pre-condition: at least one node has been added; behaviour is
     * undefined otherwise.
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
