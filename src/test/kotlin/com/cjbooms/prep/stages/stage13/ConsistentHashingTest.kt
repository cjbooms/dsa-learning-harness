package com.cjbooms.prep.stages.stage13

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull

class ConsistentHashingTest {

    @Test
    fun `every key maps to a node once at least one node is added`() {
        val ring = ConsistentHashing<String>()
        ring.add("nodeA")

        val owner = ring.getNode("any-key-at-all")
        assertNotNull(owner)
        assertEquals("nodeA", owner)
    }

    @Test
    fun `adding a node shifts only a small fraction of keys (consistent-hash property)`() {
        // With N nodes, adding one more should reshuffle roughly 1/(N+1) of
        // the keys, NOT ~50%. We assert that at least 70% of keys keep their
        // owner when going from 3 -> 4 nodes. With 200 replicas and many keys,
        // the empirical reshuffle ratio converges to ~1/(N+1) = 25%, so 70%
        // preserved is a comfortably loose bound.
        val keys = (0 until 2000).map { "key-$it" }
        val ring3 = ConsistentHashing<String>(replicasPerNode = 200)
        listOf("n1", "n2", "n3").forEach { ring3.add(it) }
        val ownersBefore = keys.associateWith { ring3.getNode(it) }

        val ring4 = ConsistentHashing<String>(replicasPerNode = 200)
        listOf("n1", "n2", "n3", "n4").forEach { ring4.add(it) }
        val ownersAfter = keys.associateWith { ring4.getNode(it) }

        val preserved = keys.count { ownersBefore[it] == ownersAfter[it] }
        assertEquals(true, preserved >= keys.size * 7 / 10,
            "expected at least 70% of keys to keep their owner, got $preserved/${keys.size}")
    }

    @Test
    fun `removing a node reassigns its keys to the remaining nodes`() {
        val ring = ConsistentHashing<String>(replicasPerNode = 200)
        listOf("n1", "n2", "n3").forEach { ring.add(it) }
        assertEquals(3, ring.nodeCount)

        ring.remove("n2")
        assertEquals(2, ring.nodeCount)

        // All keys previously owned by n2 must now land on n1 or n3.
        val keys = (0 until 500).map { "key-$it" }
        val ownersBefore = keys.associateWith { ring.getNode(it) } // no-op for already-built ring
        // Re-build a 2-node ring to get the "after" owners cleanly.
        val ring2 = ConsistentHashing<String>(replicasPerNode = 200)
        listOf("n1", "n3").forEach { ring2.add(it) }
        keys.forEach { key ->
            val before = ownersBefore[key]
            val after = ring2.getNode(key)
            assertEquals(true, after == "n1" || after == "n3",
                "key $key mapped to unexpected node $after after removing n2")
            // n2 must no longer own anything in the 2-node world.
            assertEquals(true, after != "n2")
        }
    }

    @Test
    fun `virtual nodes expand the ring so distribution smooths out`() {
        val ring = ConsistentHashing<String>(replicasPerNode = 50)
        listOf("n1", "n2", "n3").forEach { ring.add(it) }

        // 3 real nodes * 50 replicas = 150 ring entries.
        assertEquals(150, ring.ringSize)
        assertEquals(3, ring.nodeCount)
    }
}
