package com.cjbooms.prep.stages.stage9

/**
 * Stage 9.4 — Serialize / deserialize a BST using pre-order only.
 *
 * MongoDB relevance: compact storage / wire format for index keys. BST
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
 * Time budget: 10 min.
 */

data class BstNode(val value: Int, var left: BstNode? = null, var right: BstNode? = null)

/** Returns the pre-order serialization of [root], or a sentinel for null. */
fun serializeBst(root: BstNode?): String {
    TODO("implement")
}

/** Parses [data] (the output of [serializeBst]) back into a BST. */
fun deserializeBst(data: String): BstNode? {
    TODO("implement")
}
