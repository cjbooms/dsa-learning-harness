# Data Structures and Algorithms Learning Harness

Kotlin practice workspace for DSA and system-adjacent drills.

## How this repo works

13 stages. Each stage has a doc in `docs/stages/` (goals, exercises, and
notes) and a matching package in `src/main/kotlin/com/cjbooms/prep/stages/`
with skeletons — **signatures + KDoc only, no solutions in stages/**. You
implement the exercises and JUnit5 tests provide feedback. Solved reference
implementations live in `src/main/kotlin/com/cjbooms/prep/solutions/` for
post-exercise comparison — don't peek before solving.

| Stage | Content | Est. time |
|---|---|---|
| [0](docs/stages/00-rate-limiter-ladder.md) | Rate-limiter retrospective + escalation ladder | 30m |
| [1](docs/stages/01-dsa-structures.md) | DSA set A: choosing the right structure | 90m |
| [2](docs/stages/02-read-write-lock.md) | Read-Write Lock | 45m |
| [3](docs/stages/03-iterators-parsers.md) | DSA set B: iterators, JSON parser, inverted index | 90m |
| [4](docs/stages/04-database-fluency.md) | Database internals fluency | 75m |
| [8](docs/stages/08-graph-algorithms.md) | Graph algorithms: topo sort, cycle detect, shortest path, union-find, grid flood-fill / rotting oranges, clone graph (+8.5 practice) | 185m |
| [9](docs/stages/09-tree-algorithms.md) | Tree algorithms: BST validate, LCA, Trie, BST serialize | 60m |
| [10](docs/stages/10-array-string-patterns.md) | Array/string: two pointers, sliding window, binary search, prefix sums, text justification | 75m |
| [11](docs/stages/11-heap-priority-queue.md) | Heap: kth largest, merge k lists, task scheduler, median stream, sliding window max | 100m |
| [12](docs/stages/12-concurrency-primitives.md) | Concurrency: blocking queue, multithreaded web crawler, lock-free counter | 45m |
| [13](docs/stages/13-system-adjacent-dsa.md) | System DSA: LRU cache, hit counter, consistent hashing, LFU cache, randomized set, snapshot array, versioned KV recall | 140m |
| [14](docs/stages/14-dynamic-programming.md) | Dynamic programming: basic, string, word break | 45m |
| [15](docs/stages/15-cold-recall.md) | Cold-recall refreshers of earlier stages | 75m |

## Full drill inventory

Each active stage below lists its stub files in `src/main/kotlin/com/cjbooms/prep/stages/`,
matching tests in `src/test/kotlin/com/cjbooms/prep/stages/`, and reference solutions in
`src/main/kotlin/com/cjbooms/prep/solutions/`.

### Stage 0 — Rate Limiter Retrospective + Escalation Ladder (30m)
- `RateLimiter.kt` — `allow(requestId, nowMillis)`
- `PerUserRateLimiterConventional.kt` — escalation rungs (thread-safe, per-user, memory-bound, token bucket)

### Stage 1 — DSA Drill Set A (90m)
- `IntervalMerge.kt` — `mergeIntervals`
- `TreeSerialize.kt` — `serialize` / `deserialize`
- `ConnectedComponents.kt` — `countComponents`
- `KvWithTtl.kt` — `put` / `get` with TTL

### Stage 2 — Read-Write Lock (45m)
- `ReadWriteLock.kt` — `SimpleReadWriteLock` with writer preference

### Stage 3 — Iterators, Parsers, Indexes (90m)
- `Iterators.kt` — `unionSorted`, `mergeKSorted`
- `JsonParser.kt` — `parse(json): JsonValue`
- `InvertedIndex.kt` — `insert`, `search`, `searchAll`

### Stage 4 — Database Internals Fluency (75m)
- Reading + conceptual lab in `docs/database-internals.md`

### Stage 8 — Graph Algorithms (185m)
- `TaskScheduler.kt` — Kahn's BFS topological sort
- `TaskSchedulerDfs.kt` — DFS post-order topological sort
- `CycleDetector.kt` — cycle detection
- `ShortestPath.kt` — grid BFS shortest path
- `UnionFind.kt` — union-find
- `CourseSchedule.kt` — course ordering
- `AlienDictionary.kt` — alien dictionary order
- `GridTraversal.kt` — `countIslands`, `rottingOranges`
- `CloneGraph.kt` — `cloneGraph`

### Stage 9 — Tree Algorithms (60m)
- `BstValidator.kt` — `isValidBst`
- `LowestCommonAncestor.kt` — BST and generic binary-tree LCA
- `Trie.kt` — `insert`, `search`, `startsWith`
- `BstSerializer.kt` — `serializeBst` / `deserializeBst`

### Stage 10 — Array/String Patterns + Text Justification (75m)
- `TwoPointers.kt` — pair sum, container with most water, remove duplicates
- `SlidingWindow.kt` — `longestSubstringWithoutRepeats`, `minWindowSubstring`
- `BinarySearchVariants.kt` — rotated search, peak element, 2D matrix search
- `PrefixSums.kt` — range sum, subarray sum equals K
- `TextJustification.kt` — `textJustify`

### Stage 11 — Heap / Priority Queue (100m)
- `KthLargest.kt` — `add` / `peek`
- `MergeKLists.kt` — `mergeKLists`
- `TaskSchedulerWithCooldown.kt` — `leastInterval`
- `MedianFinder.kt` — `addNum` / `findMedian`
- `SlidingWindowMaximum.kt` — `maxSlidingWindow`

### Stage 12 — Concurrency Primitives + Multithreaded Crawler (45m)
- `BoundedBlockingQueue.kt` — `put` / `take`
- `WebCrawler.kt` — `crawl(startUrl)` (BFS + worker pool + poll-time visited set)
- `LockFreeCounter.kt` — `incrementAndGet`, `get`, `getAndReset`

### Stage 13 — System-Adjacent DSA (140m)
- `LruCache.kt`
- `HitCounter.kt`
- `ConsistentHashing.kt`
- `LfuCache.kt`
- `RandomizedSet.kt`
- `SnapshotArray.kt`
- `VersionedKvRecall.kt`

### Stage 14 — Dynamic Programming (45m)
- `BasicDp.kt` — `climbStairs`, `houseRobber`
- `StringDp.kt` — `longestCommonSubsequence`, `editDistance`
- `WordBreak.kt` — `wordBreak`, `wordBreakIi`

### Stage 15 — Cold Recall (75m)
- `RateLimiterRecall.kt`
- `IntervalMergeRecall.kt`
- `TreeSerializeRecall.kt`
- `RwLockRecall.kt`

### Reference-only drills (solutions kept for spare-time reading)
- `solutions/stage10/SlidingWindow.kt` — still contains `maxSumSubarrayK`
- `solutions/stage12/ConnectionPool.kt`
- `solutions/stage14/IntervalDp.kt`
- `concurrency/ThreadSafeLruCache.kt` + `concurrency/ThreadSafeLruCacheTest.kt`

## Reference docs

- `docs/cheat-sheet.md` — DS/algorithms/concurrency flash cards
- `docs/database-internals.md` — internals primer (Stage 4 reading)
- `docs/implementation-recipes.md` — skeletons for common patterns
- `docs/exercise-prompts.md` — exercise prompts in plain English

## Commands

```bash
./gradlew build                                # compile + all tests
./gradlew test --tests '*stages.stage1*'       # one stage's tests
```

## Warm-up exercises

`dsa/`, `concurrency/`, `realworld/` packages with solved, commented exercises
(VersionedKVStore, BoundedBlockingQueue, ReplicationLagAlerter...) — reference
material and warm-up reps.
