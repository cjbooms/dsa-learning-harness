# Stage 11 — Heap / Priority Queue

**Time budget: 45 min** · Package: `stages/stage11`

## The meta-skill

"Top k of a stream" or "repeatedly take the min/max" is a heap. The interview
move is sizing the heap: min-heap of size k for kth-LARGEST (root is the
answer), max-heap for kth-smallest. Say the invariant aloud: what's on top and
why is it exactly the answer?

## Exercises

### 11.1 Kth largest in a stream (15 min)
`add(value)` / `peek()` in `KthLargest.kt`
- [ ] Min-heap of size k; admit then evict-min when over capacity
- [ ] Root = kth largest once k seen; smallest-seen-so-far before that
- [ ] Aloud: O(n log k) vs sort's O(n log n) — when does it matter?

### 11.2 Merge k sorted lists (15 min)
`mergeKLists(lists)` in `MergeKLists.kt`
- [ ] Min-heap seeded with each list head; pop min, push that list's next
- [ ] Tie-break deterministically (value, listIndex, elementIndex)
- [ ] Compare with the Stage 3.2 iterator merge — when heap vs divide-and-conquer?

### 11.3 Task scheduler with cooldown (15 min)
`leastInterval(tasks, cooldown)` in `TaskSchedulerWithCooldown.kt`
- [ ] Greedy: most-frequent task first, cooldown gap between repeats
- [ ] Closed form: (maxCount - 1) * (cooldown + 1) + numMaxTasks, floored at n
- [ ] MongoDB framing: rate-shaped writes, throttled retries

## Done when
- [ ] Tests green (`./gradlew test --tests '*stages.stage11*'`); each exercise committed separately
- [ ] You can state the heap invariant for each exercise in one sentence

## Commit points
After each exercise.
