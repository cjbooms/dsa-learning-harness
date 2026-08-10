package com.cjbooms.prep.stages.stage1

/**
 * Stage 1.2 — Serialize / deserialize a binary tree (REPORTED question).
 *
 * Design a format where deserialize(serialize(tree)) rebuilds an identical tree.
 * Key decision to narrate: which traversal makes the format UNAMBIGUOUS,
 * and how do you represent null children so structure is recoverable?
 */

class TreeNode(val value: Int, var left: TreeNode? = null, var right: TreeNode? = null)

fun serialize(root: TreeNode?): String {
    TODO("pick a traversal; encode nulls explicitly")
}

fun deserialize(data: String): TreeNode? {
    TODO("must perfectly invert serialize")
}
