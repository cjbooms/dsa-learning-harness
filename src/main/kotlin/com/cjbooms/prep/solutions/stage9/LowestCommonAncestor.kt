package com.cjbooms.prep.solutions.stage9

/**
 * Stage 9.2 — Lowest common ancestor of two nodes.
 *
 * MongoDB relevance: hierarchical data (org chart, taxonomy, document
 * revision history) and any tree-shaped index (B+ tree, $graphLookup
 * traversal). "Find the nearest shared ancestor" is the same shape as
 * "find the nearest shard key prefix shared by two documents".
 *
 * Structure-selection ritual:
 *   - BST version: leverage ordering — if both first and second are < current, LCA
 *     is in the left subtree; if both are > current, LCA is in the right;
 *     otherwise current is the split point. O(h), no extra space.
 *   - Generic binary tree: cannot use ordering. Either recurse and let
 *     the first node that sees BOTH children as descendants be the LCA
 *     (post-order, O(n)), or parent-pointer map for O(h) with O(n) extra.
 *   - Decide: which variant is the question asking for? In an interview,
 *     narrate which structure property you are exploiting.
 *
 * Time budget: 15 min.
 */

data class LcaNode(val value: Int, var left: LcaNode? = null, var right: LcaNode? = null)

/**
 * BST LCA: [first] and [second] are guaranteed to be present in [root] and first.value <= second.value
 * is a convention you can impose at the call site if you like. The split-point
 * rule still works without it.
 */
fun lowestCommonAncestorBst(root: LcaNode?, first: LcaNode, second: LcaNode): LcaNode {
    // Walk down exploiting BST ordering. The first node where first and second
    // straddle it (one <= current, one >= current) is the LCA.
    var current = root ?: error("BST LCA called with null root")
    while (true) {
        if (first.value < current.value && second.value < current.value) {
            current = current.left ?: error("BST LCA: first/second not present in tree")
        } else if (first.value > current.value && second.value > current.value) {
            current = current.right ?: error("BST LCA: first/second not present in tree")
        } else {
            // Split point (or one equals current, which is itself the LCA).
            return current
        }
    }
}

/** Generic binary tree LCA: [first] and [second] are guaranteed to be present in [root]. */
fun lowestCommonAncestor(root: LcaNode?, first: LcaNode, second: LcaNode): LcaNode {
    // Post-order recursion. Each subtree returns either:
    //   - the LCA it discovered, if both first and second are within it;
    //   - first or second, if exactly one of them is within it;
    //   - null, if neither is within it.
    // The first node that gets two non-null returns is the LCA.
    fun walk(node: LcaNode?): LcaNode? {
        if (node == null) return null
        if (node === first || node === second) return node
        val leftResult = walk(node.left)
        val rightResult = walk(node.right)
        return when {
            leftResult != null && rightResult != null -> node
            else -> leftResult ?: rightResult
        }
    }
    return walk(root) ?: error("LCA: first/second not present in tree")
}
