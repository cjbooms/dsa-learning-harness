package com.cjbooms.prep.solutions.stage9

/**
 * Stage 9.4 — Serialize / deserialize a BST using pre-order only.
 *
 * Why this matters: compact storage / wire format for index keys. BST
 * pre-order is enough to reconstruct the tree because the BST ordering
 * tells you exactly where each subtree boundary is — you don't need to
 * encode nulls. Smaller payloads over the wire, fewer round-trips.
 *
 * Structure-selection ritual:
 *   - Generic binary tree serialization needs nulls to mark structure
 *     (see Stage 1.2). BST serialization does NOT, because the ordering
 *     lets you split pre-order into left/right subtrees by upper bound.
 *   - Serialize: just pre-order traversal, values separated by a delimiter.
 *   - Deserialize: recursively consume values <= maxBound into the left
 *     subtree and > maxBound for the right; the bound threads through
 *     the recursion so you know where each subtree ends.
 *   - Empty tree: pick a sentinel string ("null", or an empty string) and
 *     document it. Null root is a common edge case in tests.
 *
 * Estimated time: 10 min.
 */

data class BstNode(val value: Int, var left: BstNode? = null, var right: BstNode? = null)

/** Returns the pre-order serialization of [root], or a sentinel for null. */
fun serializeBst(root: BstNode?): String {
    // Sentinel for an empty tree. Documented as part of the wire contract.
    if (root == null) return ""
    val builder = StringBuilder()
    fun walk(node: BstNode) {
        builder.append(node.value).append(',')
        node.left?.let { walk(it) }
        node.right?.let { walk(it) }
    }
    walk(root)
    // Strip the trailing comma for tidiness. Empty result still encodes "null root".
    return builder.toString().trimEnd(',')
}

/** Parses [data] (the output of [serializeBst]) back into a BST. */
fun deserializeBst(data: String): BstNode? {
    if (data.isEmpty()) return null
    // We use an iterator so the recursive consumer can advance the cursor
    // by side effect — exactly the trick used in the recursive-descent
    // JSON parser in Stage 3.
    val tokens = data.split(',').map { it.toInt() }
    var cursor = 0
    fun consume(upperBound: Int?): BstNode? {
        if (cursor >= tokens.size) return null
        val value = tokens[cursor]
        // If the next value violates the bound for this subtree, this subtree is empty.
        if (upperBound != null && value >= upperBound) return null
        cursor++
        // Left subtree must be strictly less than value; right subtree must be
        // >= value but < upperBound (i.e. < parent's bound). Since the BST is
        // built strictly with unique keys per the contract, left is < value and
        // right is > value; we encode ">" as the new upperBound on the right.
        val leftChild = consume(value)
        val rightChild = consume(upperBound)
        return BstNode(value, leftChild, rightChild)
    }
    return consume(null)
}
