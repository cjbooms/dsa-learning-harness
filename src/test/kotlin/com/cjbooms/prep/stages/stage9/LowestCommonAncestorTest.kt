package com.cjbooms.prep.stages.stage9

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class LowestCommonAncestorTest {

    @Test
    fun `bst lca when nodes straddle root`() {
        // BST:
        //          6
        //        /   \
        //       2     8
        //      / \   / \
        //     0   4 7   9
        //        / \
        //       3   5
        val n0 = LcaNode(0)
        val n3 = LcaNode(3)
        val n5 = LcaNode(5)
        val n4 = LcaNode(4, n3, n5)
        val n2 = LcaNode(2, n0, n4)
        val n7 = LcaNode(7)
        val n9 = LcaNode(9)
        val n8 = LcaNode(8, n7, n9)
        val root = LcaNode(6, n2, n8)

        // 2 and 8 straddle the root → LCA is root.
        assertEquals(6, lowestCommonAncestorBst(root, n2, n8).value)
        // Both under left subtree (2): LCA is 2.
        assertEquals(2, lowestCommonAncestorBst(root, n0, n4).value)
        // 7 and 9 under right subtree (8): LCA is 8.
        assertEquals(8, lowestCommonAncestorBst(root, n7, n9).value)
        // 3 and 5 → LCA is 4 (a deeper split).
        assertEquals(4, lowestCommonAncestorBst(root, n3, n5).value)
    }

    @Test
    fun `generic binary tree lca`() {
        // Generic tree (NOT a BST):
        //          3
        //        /   \
        //       5     1
        //      / \   / \
        //     6   2 0   8
        //        / \
        //       7   4
        val n7 = LcaNode(7)
        val n4 = LcaNode(4)
        val n2 = LcaNode(2, n7, n4)
        val n5 = LcaNode(5, LcaNode(6), n2)
        val n0 = LcaNode(0)
        val n8 = LcaNode(8)
        val n1 = LcaNode(1, n0, n8)
        val root = LcaNode(3, n5, n1)

        // 7 and 4 → LCA is 2.
        assertEquals(2, lowestCommonAncestor(root, n7, n4).value)
        // 7 and 8 → LCA is 3 (the root).
        assertEquals(3, lowestCommonAncestor(root, n7, n8).value)
        // 5 and 1 → LCA is 3.
        assertEquals(3, lowestCommonAncestor(root, n5, n1).value)
    }

    @Test
    fun `one node is the ancestor of the other`() {
        // LCA(a, b) where a is b's ancestor should be a.
        //          1
        //        /   \
        //       2     3
        //      / \
        //     4   5
        val n5 = LcaNode(5)
        val n4 = LcaNode(4)
        val n2 = LcaNode(2, n4, n5)
        val n3 = LcaNode(3)
        val root = LcaNode(1, n2, n3)

        // For the BST version: BST with 4<5<2 left side, right=3 >1 invalid,
        // so use a generic call. LCA of 2 and 4 is 2.
        assertEquals(2, lowestCommonAncestor(root, n2, n4).value)
        // LCA of 1 and 5 is 1 (the root is ancestor).
        assertEquals(1, lowestCommonAncestor(root, root, n5).value)
    }
}
