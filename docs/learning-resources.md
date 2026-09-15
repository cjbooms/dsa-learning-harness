# Learning Resources by Stage

Watch or read these **before** implementing the matching stub. Each resource matches the pattern the exercise is built around.

## Stage 1
- `KvWithTtl.kt` — Design an expiring key-value store. Reading: "LRU Cache" pattern (NeetCode 146) covers the TTL/laziness trade-off mindset.

## Stage 3
- `Iterators.kt` — Merge K sorted lists / sorted iterators. Video: NeetCode "Merge K Sorted Lists" (LeetCode 23).
- `InvertedIndex.kt` — No single canonical LeetCode problem; read any "inverted index" systems-design article for the AND/OR semantics.
- `JsonParser.kt` — Recursive descent parsing. Reading: any "build a JSON parser" walkthrough.

## Stage 4
- `docs/database-internals.md` — any good database internals text or your chosen DB's replication docs for write concerns and change streams.

## Stage 8 — Graph algorithms
- `TaskScheduler.kt` (Kahn's) — NeetCode "Course Schedule" (LeetCode 207) or Abdul Bari "Topological Sort (Kahn's)".
- `TaskSchedulerDfs.kt` — Abdul Bari "Topological Sort using DFS".
- `CycleDetector.kt` — NeetCode "Course Schedule" (LeetCode 207) cycle discussion.
- `ShortestPath.kt` — NeetCode "Shortest Path in Binary Matrix" (LeetCode 1091) or any BFS shortest-path tutorial.
- `UnionFind.kt` — NeetCode "Number of Provinces" (LeetCode 547) or WilliamFiset union-find playlist.
- `CourseSchedule.kt` — NeetCode "Course Schedule II" (LeetCode 210).
- `AlienDictionary.kt` — NeetCode "Alien Dictionary" (LeetCode 269).
- `GridTraversal.kt` — NeetCode "Number of Islands" (LeetCode 200) and "Rotting Oranges" (LeetCode 994).
- `CloneGraph.kt` — NeetCode "Clone Graph" (LeetCode 133).

## Stage 9 — Trees
- `BstSerializer.kt` — NeetCode "Serialize and Deserialize Binary Tree" (LeetCode 297); the BST version drops nulls.

## Stage 10 — Array/string patterns
- `TwoPointers.kt` — NeetCode "Two Pointers" playlist; specifically "Container With Most Water" (LeetCode 11) and "Two Sum II" (LeetCode 167).
- `SlidingWindow.kt` — NeetCode "Longest Substring Without Repeating Characters" (LeetCode 3) and "Minimum Window Substring" (LeetCode 76).
- `BinarySearchVariants.kt` — NeetCode "Binary Search" playlist; "Search in Rotated Sorted Array" (LeetCode 33) and "Find Minimum in Rotated Sorted Array" (LeetCode 153).
- `PrefixSums.kt` — NeetCode "Range Sum Query" (LeetCode 303) and "Subarray Sum Equals K" (LeetCode 560).
- `TextJustification.kt` — LeetCode 68 editorial; mostly a string-simulation exercise.

## Stage 11 — Heaps
- `KthLargest.kt` — NeetCode "Kth Largest Element in a Stream" (LeetCode 703).
- `MedianFinder.kt` — NeetCode "Find Median from Data Stream" (LeetCode 295).
- `MergeKLists.kt` — NeetCode "Merge K Sorted Lists" (LeetCode 23).
- `SlidingWindowMaximum.kt` — NeetCode "Sliding Window Maximum" (LeetCode 239).
- `TaskSchedulerWithCooldown.kt` — NeetCode "Task Scheduler" (LeetCode 621).

## Stage 12 — Concurrency
- `BoundedBlockingQueue.kt` — Any Java producer/consumer Condition tutorial.
- `WebCrawler.kt` — LeetCode 1242 editorial plus a thread-pool tutorial.
- `LockFreeCounter.kt` — Any `AtomicLong` / CAS tutorial.

## Stage 13 — System-adjacent DSA
- `LruCache.kt` — NeetCode "LRU Cache" (LeetCode 146).
- `LfuCache.kt` — NeetCode "LFU Cache" (LeetCode 460).
- `ConsistentHashing.kt` — Any "consistent hashing" systems-design article.
- `HitCounter.kt` — LeetCode 362 editorial.
- `RandomizedSet.kt` — NeetCode "Insert Delete GetRandom O(1)" (LeetCode 380).
- `SnapshotArray.kt` — LeetCode 1146 editorial.
- `VersionedKvRecall.kt` — Same pattern as SnapshotArray / "Time Based Key-Value Store" (LeetCode 981).

## Stage 14 — Dynamic programming
- `BasicDp.kt` — NeetCode "Climbing Stairs" (LeetCode 70) and "House Robber" (LeetCode 198).
- `StringDp.kt` — NeetCode "Longest Common Subsequence" (LeetCode 1143) and "Edit Distance" (LeetCode 72).
- `WordBreak.kt` — NeetCode "Word Break" (LeetCode 139) and "Word Break II" (LeetCode 140).

## Stage 15 — Cold recall
- These are the same patterns as Stages 0, 1, 2, 8. Re-watch the relevant video above, then close it and implement from memory.
