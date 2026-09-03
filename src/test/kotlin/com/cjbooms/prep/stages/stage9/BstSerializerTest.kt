package com.cjbooms.prep.stages.stage9

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue

class BstSerializerTest {

    @Test
    fun `round trip small tree`() {
        //       2
        //      / \
        //     1   3
        val tree = BstNode(2, BstNode(1), BstNode(3))
        val restored = deserializeBst(serializeBst(tree))
        // BST property: in-order traversal is sorted for both.
        assertEquals(inOrder(tree), inOrder(restored))
    }

    @Test
    fun `null root round trips to null`() {
        assertNull(deserializeBst(serializeBst(null)))
    }

    @Test
    fun `round trip deeper tree`() {
        //           4
        //         /   \
        //        2     6
        //       / \   / \
        //      1   3 5   7
        val tree = BstNode(
            4,
            BstNode(2, BstNode(1), BstNode(3)),
            BstNode(6, BstNode(5), BstNode(7)),
        )
        val restored = deserializeBst(serializeBst(tree))!!
        // Restored tree must still be a valid BST — verify in-order is sorted.
        assertEquals(listOf(1, 2, 3, 4, 5, 6, 7), inOrder(restored))
        // And re-serializing should be deterministic.
        assertEquals(serializeBst(tree), serializeBst(restored))
    }

    @Test
    fun `single node round trips`() {
        val tree = BstNode(42)
        val restored = deserializeBst(serializeBst(tree))
        assertEquals(42, restored?.value)
        assertNull(restored?.left)
        assertNull(restored?.right)
    }

    /** Helpers used by tests above. */
    private fun inOrder(root: BstNode?): List<Int> {
        val out = mutableListOf<Int>()
        fun walk(n: BstNode?) {
            if (n == null) return
            walk(n.left); out.add(n.value); walk(n.right)
        }
        walk(root)
        return out
    }
}
