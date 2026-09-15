package com.cjbooms.prep.solutions.stage15

/**
 * Stage 15.3 — Cold-recall rebuild: serialize / deserialize a binary tree.
 *
 * Real-world framing: durable storage of plan/explain tree nodes — round-trip a
 * tree through a string without losing shape (null children must survive).
 *
 * Design choices (pick one and defend):
 *   - which traversal makes the format UNAMBIGUOUS?
 *   - how do you represent null children so structure is recoverable?
 *
 * serialize(root)  -> String (round-trip safe)
 * deserialize(str) -> RecallTreeNode? (null str for null root)
 *
 * Ritual before coding (speak aloud):
 *   1. Pre-order is the natural recursive choice — why is it self-delimiting
 *      with a null marker?
 *   2. Where is the state kept during deserialize? (Hint: index pointer vs queue.)
 *
 * Estimated time: 6 minutes cold.
 */
data class RecallTreeNode(val value: Int, var left: RecallTreeNode? = null, var right: RecallTreeNode? = null)

/**
 * Pre-order traversal with an explicit "null" marker for absent children. The
 * marker is what makes the format unambiguous: without it, a node with one
 * missing child could not be distinguished from a leaf in the same position.
 */
fun serialize(root: RecallTreeNode?): String {
    if (root == null) return "null"
    val parts = mutableListOf<String>()
    serializeInto(root, parts)
    return parts.joinToString(",")
}

private fun serializeInto(node: RecallTreeNode?, parts: MutableList<String>) {
    if (node == null) {
        parts += "null"
        return
    }
    parts += node.value.toString()
    serializeInto(node.left, parts)
    serializeInto(node.right, parts)
}

/**
 * Deserialize by sharing a single index pointer via a one-element array — the
 * classic walk-pointer pattern. Cheaper than a queue and mirrors the recursion
 * order of the serializer exactly.
 */
fun deserialize(data: String): RecallTreeNode? {
    val tokens = data.split(",")
    val cursor = intArrayOf(0)
    return deserializeWalk(tokens, cursor)
}

private fun deserializeWalk(tokens: List<String>, cursor: IntArray): RecallTreeNode? {
    if (cursor[0] >= tokens.size) return null
    val token = tokens[cursor[0]]
    cursor[0]++
    if (token == "null") return null
    val node = RecallTreeNode(token.toInt())
    node.left = deserializeWalk(tokens, cursor)
    node.right = deserializeWalk(tokens, cursor)
    return node
}
