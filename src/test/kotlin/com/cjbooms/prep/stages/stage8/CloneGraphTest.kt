package com.cjbooms.prep.stages.stage8

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNotSame
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue

class CloneGraphTest {

    @Test
    fun `clone of null returns null`() {
        assertNull(cloneGraph(null))
    }

    @Test
    fun `clone of single node returns a distinct node with same value`() {
        val node = Node(1)
        val clone = cloneGraph(node)
        assertNotNull(clone)
        assertNotSame(node, clone)
        assertEquals(1, clone?.value)
        assertTrue(clone?.neighbors?.isEmpty() ?: false)
    }

    @Test
    fun `clone of a cycle preserves structure without reusing original nodes`() {
        val node1 = Node(1)
        val node2 = Node(2)
        val node3 = Node(3)
        node1.neighbors.add(node2)
        node2.neighbors.add(node3)
        node3.neighbors.add(node1)

        val c1 = cloneGraph(node1)
        assertNotNull(c1)
        assertNotSame(node1, c1)
        assertEquals(1, c1?.value)

        val c2 = c1?.neighbors?.single()
        assertNotNull(c2)
        assertNotSame(node2, c2)
        assertEquals(2, c2?.value)

        val c3 = c2?.neighbors?.single()
        assertNotNull(c3)
        assertNotSame(node3, c3)
        assertEquals(3, c3?.value)

        // The copy must close the cycle back to c1, not leak to the originals.
        val backToC1 = c3?.neighbors?.single()
        assertNotNull(backToC1)
        assertEquals(1, backToC1?.value)
        assertNotSame(node1, backToC1)
        assertTrue(backToC1 === c1)
    }
}
