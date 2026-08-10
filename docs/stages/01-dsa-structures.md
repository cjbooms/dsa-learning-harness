# Stage 1 — DSA Drill Set A: Choosing the Right Structure

**Time budget: 90 min** ⭐ top priority · Package: `stages/stage1`

## The meta-skill

Every exercise below starts with the same ritual — **before coding**:
1. Name TWO candidate data structures
2. Say what each would cost (time/space)
3. Pick one and defend it in 2–3 sentences

This is what "selecting the correct data structures for the job" (the round's
stated focus) actually looks like from the interviewer's chair.

## Exercises

### 1.1 Interval merging (20 min) — REPORTED question
`mergeIntervals(intervals: List<IntRange>): List<IntRange>`
- Atlas framing: consolidate overlapping backup windows
- Structure ritual first. Then implement. Then: "now intervals arrive as a
  stream, one at a time" (mutation drill — what changes?)

### 1.2 Serialize / deserialize binary tree (20 min) — REPORTED question
`serialize(root: TreeNode?): String` / `deserialize(data: String): TreeNode?`
- Which traversal, and why does it make deserialization unambiguous?
- Skeleton `TreeNode` provided in the file

### 1.3 Connected components (15 min) — REPORTED question
`countComponents(n: Int, edges: List<Pair<Int, Int>>): Int`
- Two valid approaches: DFS/BFS with visited set, or union-find.
  Do the ritual — when is union-find actually worth it?

### 1.4 In-memory KV with TTL (20 min) — REPORTED question
`put(key, value, ttlMillis)` / `get(key, nowMillis)` — expired keys are invisible
- This is the expiring-queue's cousin. Structure ritual matters most here:
  how do you expire lazily vs eagerly, and what's the cost of each?

### 1.5 Narration playback (15 min)
- [ ] Re-explain each of the 4 solutions aloud, structure-choice first, no notes

## Done when
- [ ] Tests green; each exercise committed separately
- [ ] For each problem you can state the rejected structure and why it lost

## Commit points
After each exercise.
