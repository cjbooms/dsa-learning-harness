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
 *   - BST version: leverage ordering — if both p and q are < current, LCA
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
 * BST LCA: [p] and [q] are guaranteed to be present in [root] and p.value <= q.value
 * is a convention you can impose at the call site if you like. The split-point
 * rule still works without it.
 */
fun lowestCommonAncestorBst(root: LcaNode?, p: LcaNode, q: LcaNode): LcaNode {
    // Walk down exploiting BST ordering. The first node where p and q
    // straddle it (one <= current, one >= current) is the LCA.
    var cur = root ?: error("BST LCA called with null root")
    while (true) {
        if (p.value < cur.value && q.value < cur.value) {
            cur = cur.left ?: error("BST LCA: p/q not present in tree")
        } else if (p.value > cur.value && q.value > cur.value) {
            cur = cur.right ?: error("BST LCA: p/q not present in tree")
        } else {
            // Split point (or one equals cur, which is itself the LCA).
            return cur
        }
    }
}

/** Generic binary tree LCA: [p] and [q] are guaranteed to be present in [root]. */
fun lowestCommonAncestor(root: LcaNode?, p: LcaNode, q: LcaNode): LcaNode {
    // Post-order recursion. Each subtree returns either:
    //   - the LCA it discovered, if both p and q are within it;
    //   - p or q, if exactly one of them is within it;
    //   - null, if neither is within it.
    // The first node that gets two non-null returns is the LCA.
    fun walk(node: LcaNode?): LcaNode? {
        if (node == null) return null
        if (node === p || node === q) return node
        val left = walk(node.left)
        val right = walk(node.right)
        return when {
            left != null && right != null -> node
            else -> left ?: right
        }
    }
    return walk(root) ?: error("LCA: p/q not present in tree")
}
