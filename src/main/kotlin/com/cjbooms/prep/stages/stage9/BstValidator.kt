package com.cjbooms.prep.stages.stage9

/**
 * Stage 9.1 — Validate a binary search tree.
 *
 * Why this matters: index ordering invariants. A corrupted B-tree page or a
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
    if (root == null) return true


    fun checkNode(node: TreeNode?, min: Long, max: Long): Boolean {
        if (node == null) return true
        if (node.value < min || node.value > max) return false

        return checkNode(node.left, min, node.value.toLong()) &&
            checkNode(node.right, node.value.toLong(), max)

    }
    return checkNode(root, Long.MIN_VALUE, Long.MAX_VALUE)
}

/**
 * Variant: in-order traversal should yield a strictly increasing sequence.
 * Useful when you want a single O(n) pass without threading min/max bounds
 * through the recursion. State (the previous emitted value) is held in a
 * single-cell holder because Kotlin can't mutate a captured `var` cleanly.
 */
fun isValidBstInOrder(root: TreeNode?): Boolean {
    if (root == null) return true
    var initial = Long.MIN_VALUE
    var valid = true
    fun checkNode(node: TreeNode) {
        if (node.left != null) {
            checkNode(node.left!!)
        }
        if (node.value < initial) valid = false
        initial = node.value.toLong()
        if (node.right != null) {
            checkNode(node.right!!)
        }
    }
    checkNode(root)
    return valid
}
