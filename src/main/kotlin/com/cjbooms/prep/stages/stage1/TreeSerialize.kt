package com.cjbooms.prep.stages.stage1


/**
 * Stage 1.2 — Serialize / deserialize a binary tree (REPORTED question).
 *
 * Design a format where deserialize(serialize(tree)) rebuilds an identical tree.
 * Key decision to narrate: which traversal makes the format UNAMBIGUOUS,
 * and how do you represent null children so structure is recoverable?
 */

data class TreeNode(val value: Int, var left: TreeNode? = null, var right: TreeNode? = null)

fun serialize(root: TreeNode?): String {
    if (root == null) return "null"
    val left = serialize(root.left)
    val current = root.value.toString()
    val right = serialize(root.right)
    return listOf(current, left, right).joinToString()
}


fun deserialize(data: String): TreeNode? {
    val values = ArrayDeque(data.split(",").map { it.trim().toIntOrNull() })

    return recursiveBuild(values)

}

fun recursiveBuild(values: ArrayDeque<Int?>): TreeNode? {
    val rootNode = values.removeFirst()?.let { TreeNode(it) }
    if (rootNode != null) {
        rootNode.left = recursiveBuild(values)
        rootNode.right = recursiveBuild(values)
    }
    return rootNode
}



