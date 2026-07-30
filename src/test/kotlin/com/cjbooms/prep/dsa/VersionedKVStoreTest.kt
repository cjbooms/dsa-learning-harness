package com.cjbooms.prep.dsa

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull

class VersionedKVStoreTest {

    @Test
    fun `get returns latest version at or before timestamp`() {
        val store = VersionedKVStore()
        store.put("doc1", "v1", 1)
        store.put("doc1", "v2", 5)
        store.put("doc1", "v3", 10)

        assertEquals("v1", store.get("doc1", 1))
        assertEquals("v1", store.get("doc1", 4))
        assertEquals("v2", store.get("doc1", 5))
        assertEquals("v2", store.get("doc1", 9))
        assertEquals("v3", store.get("doc1", 10))
        assertEquals("v3", store.get("doc1", 100))
    }

    @Test
    fun `puts arriving out of order are still versioned correctly`() {
        val store = VersionedKVStore()
        store.put("doc1", "v3", 10)
        store.put("doc1", "v1", 1)
        store.put("doc1", "v2", 5)

        assertEquals("v1", store.get("doc1", 3))
        assertEquals("v2", store.get("doc1", 7))
        assertEquals("v3", store.get("doc1", 15))
    }

    @Test
    fun `unknown doc returns null`() {
        val store = VersionedKVStore()
        store.put("doc1", "v1", 1)
        assertNull(store.get("doc2", 10))
    }

    @Test
    fun `timestamp before first version returns null`() {
        val store = VersionedKVStore()
        store.put("doc1", "v1", 5)
        assertNull(store.get("doc1", 4))
    }

    @Test
    fun `empty store returns null`() {
        assertNull(VersionedKVStore().get("doc1", 1))
    }

    @Test
    fun `docs are independent`() {
        val store = VersionedKVStore()
        store.put("a", "a1", 1)
        store.put("b", "b1", 2)
        assertEquals("a1", store.get("a", 100))
        assertEquals("b1", store.get("b", 100))
    }
}
