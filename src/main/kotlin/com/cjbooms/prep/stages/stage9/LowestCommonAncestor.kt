package com.cjbooms.prep.stages.stage9

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
    TODO("implement")
}

/** Generic binary tree LCA: [p] and [q] are guaranteed to be present in [root]. */
fun lowestCommonAncestor(root: LcaNode?, p: LcaNode, q: LcaNode): LcaNode {
    TODO("implement")
}
