package com.cjbooms.prep.stages.stage9

/**
 * Learn first: see docs/learning-resources.md
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
    if (root == null) return "null"
    val current = root.value.toString()
    val left = serializeBst(root.left)
    val right = serializeBst(root.right)
    return "$current,$left,$right"
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
    if (data == "null" || data.isEmpty()) return null
    val queue = ArrayDeque(data.split(",").map { it.toIntOrNull() })


    fun buildNode(min: Long, max: Long): BstNode? {
        if (queue.isEmpty()) return null

        val currentVal = queue.removeFirst() // peek and validate
        if (currentVal == null || currentVal <= min || currentVal >= max) return null

        val node = BstNode(currentVal)
        node.left = buildNode(min, node.value.toLong())
        node.right = buildNode( node.value.toLong(), max)
        return node
    }


    return buildNode(Long.MIN_VALUE, Long.MAX_VALUE)

}




fun main() {
    val node4 = BstNode(4)
    val node2 = BstNode(2)
    val node3 = BstNode(3)
    val node1 = BstNode(1)

    node4.left = node2
    node2.left = node1
    node2.right = node3

    println("Input: " + node4)
    val serialized = serializeBst(node4)
    println("Searlized: " + serialized)
    val reconstructed = deserializeBst(serialized)
    println("Desearlized: " + reconstructed)
    println("Original: " + node4)


}