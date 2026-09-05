package com.cjbooms.prep.solutions.stage9

/**
 * Stage 9.1 — Validate a binary search tree.
 *
 * MongoDB relevance: index ordering invariants. A corrupted B-tree page or a
 * mis-ordered index entry violates the BST property just like a mis-ordered
 * node here. Validation queries (sanity checks during recovery, or in tests)
 * use the same recursive min/max bound argument.
 *
 * Structure-selection ritual:
 *   - Do we trust the in-order traversal to be sorted? Then a single pass
 *     tracking the previous emitted value gives O(n) time, O(h) stack
 *     (or O(1) extra via Morris, but don't bother for the interview).
 *   - Do we want local reasoning at each node without holding state? Then
 *     recurse with (minBound, maxBound) and tighten the bound on each side.
 *   - Empty tree and single-node tree are always valid — make sure your
 *     base case doesn't reject them.
 *
 * Time budget: 15 min.
 */

data class TreeNode(val value: Int, var left: TreeNode? = null, var right: TreeNode? = null)

/** Returns true iff the tree rooted at [root] satisfies the BST property. */
fun isValidBst(root: TreeNode?): Boolean {
    // Recursive min/max bounds. Each node must lie strictly inside
    // (lower, upper). Tighter bounds are pushed down to children.
    fun walk(node: TreeNode?, lower: Int?, upper: Int?): Boolean {
        if (node == null) return true
        if (lower != null && node.value <= lower) return false
        if (upper != null && node.value >= upper) return false
        return walk(node.left, lower, node.value) &&
               walk(node.right, node.value, upper)
    }
    return walk(root, null, null)
}

/**
 * Variant: in-order traversal should yield a strictly increasing sequence.
 * Useful when you want a single O(n) pass without threading min/max bounds
 * through the recursion. State (the previous emitted value) is held in a
 * single-cell holder because Kotlin can't mutate a captured `var` cleanly.
 */
fun isValidBstInOrder(root: TreeNode?): Boolean {
    // Holder for the previous in-order value; null means "none seen yet".
    val previous = arrayOfNulls<Int>(1)
    fun walk(node: TreeNode?): Boolean {
        if (node == null) return true
        if (!walk(node.left)) return false
        if (previous[0] != null && node.value <= previous[0]!!) return false
        previous[0] = node.value
        return walk(node.right)
    }
    return walk(root)
}
