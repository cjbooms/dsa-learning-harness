package com.cjbooms.prep.stages.stage9

/**
 * Stage 9.4 — Serialize / deserialize a BST using pre-order only.
 */

data class BstNode(val value: Int, var left: BstNode? = null, var right: BstNode? = null)

/**
 * Returns the pre-order serialization of [root] as a comma-separated string of
 * node values, or the string `"null"` when [root] is `null`.
 *
 * @param root the root of the BST, or `null` to represent an empty tree
 * @return the encoded pre-order representation of the BST
 */
fun serializeBst(root: BstNode?): String {
    TODO("implement")
}

/**
 * Parses [data] (the output of `serializeBst`) back into a BST.
 *
 * @param data the encoded BST produced by `serializeBst`; must equal `"null"`
 *             for an empty tree
 * @return the reconstructed BST root, or `null` if [data] is the empty-tree
 *         sentinel
 */
fun deserializeBst(data: String): BstNode? {
    TODO("implement")
}
