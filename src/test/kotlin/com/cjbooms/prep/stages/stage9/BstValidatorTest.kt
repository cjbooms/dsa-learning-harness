package com.cjbooms.prep.stages.stage9

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertFalse

class BstValidatorTest {

    @Test
    fun `valid simple bst`() {
        //       2
        //      / \
        //     1   3
        val root = TreeNode(2, TreeNode(1), TreeNode(3))
        assertTrue(isValidBst(root))
        assertTrue(isValidBstInOrder(root))
    }

    @Test
    fun `invalid due to left-right violation`() {
        //       5
        //      / \
        //     1   4
        //        / \
        //       3   6
        // 4 is in the right subtree of 5 but 3 is < 5 and left of 4 — invalid.
        val root = TreeNode(
            5,
            TreeNode(1),
            TreeNode(4, TreeNode(3), TreeNode(6)),
        )
        assertFalse(isValidBst(root))
        assertFalse(isValidBstInOrder(root))
    }

    @Test
    fun `single node and null are valid`() {
        assertTrue(isValidBst(null))
        assertTrue(isValidBst(TreeNode(42)))
        assertTrue(isValidBstInOrder(null))
        assertTrue(isValidBstInOrder(TreeNode(42)))
    }

    @Test
    fun `strictly increasing in-order`() {
        //           4
        //         /   \
        //        2     6
        //       / \   / \
        //      1   3 5   7
        val root = TreeNode(
            4,
            TreeNode(2, TreeNode(1), TreeNode(3)),
            TreeNode(6, TreeNode(5), TreeNode(7)),
        )
        assertTrue(isValidBst(root))
        assertTrue(isValidBstInOrder(root))
    }
}
