package com.cjbooms.prep.stages.stage8

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue

class UnionFindTest {

    @Test
    fun `each node starts in its own component`() {
        val uf = UnionFind(5)
        assertEquals(5, uf.componentCount())
        for (i in 0 until 5) {
            assertEquals(i, uf.find(i))
            for (j in i + 1 until 5) {
                assertFalse(uf.connected(i, j))
            }
        }
    }

    @Test
    fun `union merges components and counts shrink`() {
        val uf = UnionFind(5)
        assertTrue(uf.union(0, 1))
        assertEquals(4, uf.componentCount())
        assertTrue(uf.connected(0, 1))
        assertFalse(uf.connected(0, 2))

        assertTrue(uf.union(2, 3))
        assertEquals(3, uf.componentCount())

        assertTrue(uf.union(1, 2))
        // Now {0,1} and {2,3} are merged, plus {4} -> 2 components
        assertEquals(2, uf.componentCount())
        assertTrue(uf.connected(0, 3))
        assertFalse(uf.connected(0, 4))
    }

    @Test
    fun `union is idempotent and reports false when already connected`() {
        val uf = UnionFind(4)
        assertTrue(uf.union(0, 1))
        // Re-unioning two nodes that are already connected returns false
        assertFalse(uf.union(0, 1))
        assertFalse(uf.union(1, 0))
        assertEquals(3, uf.componentCount())
    }

    @Test
    fun `transitive connectivity holds after a chain of unions`() {
        val uf = UnionFind(6)
        uf.union(0, 1)
        uf.union(1, 2)
        uf.union(2, 3)
        uf.union(3, 4)
        // 5 is still separate
        assertTrue(uf.connected(0, 4))
        assertFalse(uf.connected(0, 5))
        // After linking 4 to 5, everything is one component
        assertTrue(uf.union(4, 5))
        assertEquals(1, uf.componentCount())
    }
}
