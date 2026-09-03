package com.cjbooms.prep.stages.stage15

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull

class TreeSerializeRecallTest {

    @Test
    fun `round trip preserves shape and values`() {
        val tree = RecallTreeNode(
            value = 1,
            left = RecallTreeNode(2),
            right = RecallTreeNode(3, left = RecallTreeNode(4), right = null),
        )
        val restored = deserialize(serialize(tree))
        // Serializing the restored tree must produce the same string — proves
        // shape was reconstructed, not just values.
        assertEquals(serialize(tree), serialize(restored))
    }

    @Test
    fun `null tree round trips to null`() {
        assertNull(deserialize(serialize(null)))
    }

    @Test
    fun `null children are encoded so structure survives`() {
        // Tree: 1 with only a left child of 2. The right child of 1 and both
        // children of 2 are null. If those nulls are dropped, the rebuild
        // shape will differ from the original.
        val tree = RecallTreeNode(value = 1, left = RecallTreeNode(value = 2))
        val restored = deserialize(serialize(tree))!!
        assertEquals(1, restored.value)
        assertEquals(2, restored.left?.value)
        assertNull(restored.right)
        assertNull(restored.left?.left)
        assertNull(restored.left?.right)
    }
}
