# Stage 9 — Tree Algorithms

**Estimated time: 60 min** · Package: `stages/stage9`

## The meta-skill

Tree questions are won or lost on the traversal contract: what does each
recursive call RETURN, and what does it GUARANTEE about the subtree? State the
contract aloud before coding — half the bugs are contract bugs, not pointer bugs.

## Exercises

### 9.1 BST validation (15 min)
`isValidBst(root)` / `isValidBstInOrder(root)` in `BstValidator.kt`
- [ ] Recursive with (lower, upper) bounds — not "compare with parent"
- [ ] In-order alternative: strictly increasing sequence check
- [ ] Explain: why the naive left<root<right check fails (deep violations)

### 9.2 Lowest common ancestor (15 min)
`lowestCommonAncestorBst(root, p, q)` / `lowestCommonAncestor(root, p, q)` in `LowestCommonAncestor.kt`
- [ ] BST version: iterative walk on ordering — O(h), no recursion
- [ ] General binary tree: post-order; first node whose two sides both hit
- [ ] Real-world framing: hierarchical data, org structures

### 9.3 Trie (20 min)
`insert` / `search` / `startsWith` in `Trie.kt`
- [ ] Fixed 26-slot children array (a-z contract — say it aloud)
- [ ] Shared private walkTo for search vs startsWith
- [ ] Search relevance: autocomplete, prefix queries

### 9.4 Serialize a BST (10 min)
`serializeBst(root)` / `deserializeBst(data)` in `BstSerializer.kt`
- [ ] Pre-order only, NO null markers — the BST invariant disambiguates
- [ ] Deserialize with bounded recursion (upper bound from parent)
- [ ] Explain: why a plain binary tree needs nulls but a BST doesn't

## Check your understanding
- [ ] Tests green (`./gradlew test --tests '*stages.stage9*'`); each exercise committed separately
- [ ] You can state each recursion's return contract in one sentence

## Suggested checkpoints
After 9.2, after 9.4.
