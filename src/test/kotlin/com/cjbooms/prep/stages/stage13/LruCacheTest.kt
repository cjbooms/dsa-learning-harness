package com.cjbooms.prep.stages.stage13

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull

class LruCacheTest {

    @Test
    fun `put then get returns the value and marks it as most recently used`() {
        val cache = LruCache<String, Int>(capacity = 2)
        cache.put("a", 1)
        cache.put("b", 2)

        assertEquals(1, cache.get("a"))
        assertEquals(2, cache.get("b"))
        assertEquals(2, cache.size)
    }

    @Test
    fun `get on a missing key returns null`() {
        val cache = LruCache<String, Int>(capacity = 2)
        cache.put("a", 1)

        assertNull(cache.get("nope"))
    }

    @Test
    fun `exceeding capacity evicts the least recently used entry`() {
        val cache = LruCache<String, Int>(capacity = 2)
        cache.put("a", 1)
        cache.put("b", 2)
        cache.put("c", 3) // should evict "a"

        assertNull(cache.get("a"))
        assertEquals(2, cache.get("b"))
        assertEquals(3, cache.get("c"))
    }

    @Test
    fun `get refreshes recency so the entry is not the next eviction`() {
        val cache = LruCache<String, Int>(capacity = 2)
        cache.put("a", 1)
        cache.put("b", 2)
        // Touch "a" — it becomes most-recently-used, so "b" is now the LRU.
        assertEquals(1, cache.get("a"))
        cache.put("c", 3) // should evict "b", not "a"

        assertEquals(1, cache.get("a"))
        assertNull(cache.get("b"))
        assertEquals(3, cache.get("c"))
    }

    @Test
    fun `re-putting an existing key updates the value without growing size`() {
        val cache = LruCache<String, Int>(capacity = 2)
        cache.put("a", 1)
        cache.put("b", 2)
        cache.put("a", 99) // update, not insert

        assertEquals(2, cache.size)
        assertEquals(99, cache.get("a"))
    }
}
