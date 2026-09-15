# Stage 11 — Heap / Priority Queue

**Estimated time: 100 min** · Package: `stages/stage11`

## The meta-skill

"Top k of a stream" or "repeatedly take the min/max" is a heap. The exercise
move is sizing the heap: min-heap of size k for kth-LARGEST (root is the
answer), max-heap for kth-smallest. Say the invariant aloud: what's on top and
why is it exactly the answer?

## Exercises

### 11.1 Kth largest in a stream (15 min)
`add(value)` / `peek()` in `KthLargest.kt`
- [ ] Min-heap of size k; admit then evict-min when over capacity
- [ ] Root = kth largest once k seen; smallest-seen-so-far before that
- [ ] Explain: O(n log k) vs sort's O(n log n) — when does it matter?

### 11.2 Merge k sorted lists (15 min)
`mergeKLists(lists)` in `MergeKLists.kt`
- [ ] Min-heap seeded with each list head; pop min, push that list's next
- [ ] Tie-break deterministically (value, listIndex, elementIndex)
- [ ] Compare with the Stage 3.2 iterator merge — when heap vs divide-and-conquer?

### 11.3 Task scheduler with cooldown (15 min)
`leastInterval(tasks, cooldown)` in `TaskSchedulerWithCooldown.kt`
- [ ] Greedy: most-frequent task first, cooldown gap between repeats
- [ ] Closed form: (maxCount - 1) * (cooldown + 1) + numMaxTasks, floored at n
- [ ] Real-world framing: rate-shaped writes, throttled retries

### 11.4 Median from a stream (30 min)
`addNum(n)` / `findMedian()` in `MedianFinder.kt`
- [ ] Max-heap for the lower half, min-heap for the upper half
- [ ] Rebalance so their sizes differ by at most one
- [ ] Median = top of larger heap, or average of both tops
- [ ] Explain: the invariant IS the answer

### 11.5 Sliding window maximum (25 min)
`maxSlidingWindow(nums, k)` in `SlidingWindowMaximum.kt`
- [ ] Monotonic deque of indices, values decreasing front to back
- [ ] Pop back while smaller than incoming; pop front when out of window
- [ ] O(n) total; contrast with heap O(n log k)

## Check your understanding
- [ ] Tests green (`./gradlew test --tests '*stages.stage11*'`); each exercise committed separately
- [ ] You can state the heap invariant for each exercise in one sentence
- [ ] You can state the deque invariant for sliding-window maximum and why it beats a heap

## Suggested checkpoints
After each exercise.
