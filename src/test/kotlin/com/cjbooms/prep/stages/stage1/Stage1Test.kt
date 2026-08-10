package com.cjbooms.prep.stages.stage1

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull

class IntervalMergeTest {
    @Test
    fun `merges overlapping and touching`() {
        assertEquals(listOf(1..5, 8..9), mergeIntervals(listOf(1..3, 2..5, 8..9)))
    }

    @Test
    fun `unsorted input and full containment`() {
        assertEquals(listOf(1..10), mergeIntervals(listOf(5..10, 1..6, 2..3)))
    }
}

class TreeSerializeTest {
    @Test
    fun `round trip`() {
        val tree = TreeNode(1, TreeNode(2), TreeNode(3, TreeNode(4), null))
        val restored = deserialize(serialize(tree))
        assertEquals(serialize(tree), serialize(restored))
    }

    @Test
    fun `null tree`() {
        assertNull(deserialize(serialize(null)))
    }
}

class ConnectedComponentsTest {
    @Test
    fun `two components`() {
        assertEquals(2, countComponents(5, listOf(0 to 1, 1 to 2, 3 to 4)))
    }

    @Test
    fun `isolated nodes count individually`() {
        assertEquals(3, countComponents(3, emptyList()))
    }
}

class KvWithTtlTest {
    @Test
    fun `visible before expiry, invisible after`() {
        val kv = KvWithTtl()
        kv.put("k", "v", ttlMillis = 1000, nowMillis = 0)
        assertEquals("v", kv.get("k", 999))
        assertNull(kv.get("k", 1001))
    }

    @Test
    fun `re-put with new ttl wins`() {
        val kv = KvWithTtl()
        kv.put("k", "v1", ttlMillis = 100, nowMillis = 0)
        kv.put("k", "v2", ttlMillis = 10_000, nowMillis = 50)
        assertEquals("v2", kv.get("k", 5000))
    }
}
