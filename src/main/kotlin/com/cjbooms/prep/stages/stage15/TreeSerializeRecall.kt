package com.cjbooms.prep.stages.stage15

/**
 * Learn first: see docs/learning-resources.md
 * A node of a binary tree carrying an integer value and optional left and
 * right children.
 *
 * @param value the integer stored at this node
 * @param left the left child, or `null` if absent
 * @param right the right child, or `null` if absent
 */
data class RecallTreeNode(val value: Int, var left: RecallTreeNode? = null, var right: RecallTreeNode? = null)

/**
 * Serializes a binary tree into a string that can be passed to [deserialize]
 * to reconstruct the original tree, including the position of any `null`
 * children.
 *
 * @param root the root node of the tree, or `null` to represent an empty tree
 * @return a string representation of the tree
 */
fun serialize(root: RecallTreeNode?): String {
    TODO("implement")
}

/**
 * Deserializes a string previously produced by [serialize] back into the
 * original tree shape.
 *
 * @param data the serialized form produced by [serialize]
 * @return the reconstructed tree root, or `null` if [data] represents an
 *   empty tree
 */
fun deserialize(data: String): RecallTreeNode? {
    TODO("implement")
}
