package com.cjbooms.prep.stages.stage13

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull

class VersionedKvRecallTest {

    @Test
    fun `out of order puts resolve by floor timestamp`() {
        val store = VersionedKvRecall()
        store.put("doc1", "later", 20L)
        store.put("doc1", "earlier", 5L)

        assertEquals("earlier", store.get("doc1", 10L))
        assertEquals("later", store.get("doc1", 20L))
        assertEquals("later", store.get("doc1", 25L))
    }

    @Test
    fun `get before any version returns null`() {
        val store = VersionedKvRecall()
        store.put("doc1", "v1", 10L)

        assertNull(store.get("doc1", 9L))
    }

    @Test
    fun `missing doc returns null`() {
        val store = VersionedKvRecall()
        assertNull(store.get("missing", 100L))
    }

    @Test
    fun `multiple docs are isolated`() {
        val store = VersionedKvRecall()
        store.put("a", "alpha", 1L)
        store.put("b", "beta", 2L)

        assertEquals("alpha", store.get("a", 5L))
        assertEquals("beta", store.get("b", 5L))
    }
}
