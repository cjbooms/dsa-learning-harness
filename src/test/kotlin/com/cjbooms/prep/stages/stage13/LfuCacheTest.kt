package com.cjbooms.prep.stages.stage13

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull

class LfuCacheTest {

    @Test
    fun `evicts least frequently used key`() {
        val cache = LfuCache<Int, String>(capacity = 2)
        cache.put(1, "a")
        cache.put(2, "b")
        cache.get(1) // key 1 is now at frequency 2, key 2 at frequency 1
        cache.put(3, "c") // evict key 2

        assertNull(cache.get(2))
        assertEquals("a", cache.get(1))
        assertEquals("c", cache.get(3))
    }

    @Test
    fun `tie on frequency evicts least recently used within the bucket`() {
        val cache = LfuCache<Int, String>(capacity = 2)
        cache.put(1, "a")
        cache.put(2, "b")
        cache.put(3, "c") // both keys at freq 1; key 1 is LRU, evict it

        assertNull(cache.get(1))
        assertEquals("b", cache.get(2))
        assertEquals("c", cache.get(3))
    }

    @Test
    fun `re-put resets frequency to one`() {
        val cache = LfuCache<Int, String>(capacity = 2)
        cache.put(1, "a")
        cache.put(2, "b")
        cache.get(1)
        cache.get(1) // key 1 at freq 3, key 2 at freq 1
        cache.put(1, "aa") // reset key 1 to freq 1
        cache.put(3, "c") // both keys now at freq 1; key 2 is LRU, evict it

        assertEquals("aa", cache.get(1))
        assertNull(cache.get(2))
        assertEquals("c", cache.get(3))
    }

    @Test
    fun `get on missing key returns null without affecting frequencies`() {
        val cache = LfuCache<Int, String>(capacity = 2)
        cache.put(1, "a")
        assertNull(cache.get(2))
        assertEquals("a", cache.get(1))
    }
}
