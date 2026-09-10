# Final Prep — Friday Review (Interview Friday 6pm)

## Where you stand (Thursday night, verified)

**Reported MongoDB questions: 7 of 8 banked.**
Versioned KV ✅ · KV-with-TTL ✅ · Blocking queue ✅ · LRU ✅ · Rate limiter/RW-lock ✅ · WordBreak ✅ · TextJustification ✅ — **InvertedIndex ❌ (only one open)**

**Also done tonight:** BstSerializer repaired, MinStack drilled, BinarySearchVariants ×3 earlier.

**First thing in the AM:** InvertedIndex — the last reported question. Block 1 below; solution reference at `solutions/stage3/InvertedIndex.kt` (in your style) if you want a pattern check *after* attempting it yourself.

**Open (narrate-only after that):** TwoPointers, SlidingWindow, PrefixSums, HitCounter, ConsistentHashing, SlidingWindowMaximum, TaskScheduler, WebCrawler, BasicDp, StringDp, LFU, wordBreakIi, Dijkstra, AlienDictionary.

---

## Friday Morning — InvertedIndex, then review only (~3h)

### Block 1 — InvertedIndex, for real (45 min)
The last reported question — code it yourself in `stages/stage3/InvertedIndex.kt` against its tests. The shape: `hashMapOf<String, MutableSet<String>>` term → docIds; `search` = bucket lookup; `searchAll` = AND via smallest-set iteration; `delete` needs a docId → terms reverse map to stay O(terms in doc). Narrate the map-direction choice aloud as you go — that IS the interview answer. If you're still fighting it at 45m, read `solutions/stage3/InvertedIndex.kt` (your style), close it, and re-attempt the tricky part from memory. Then run the tests and commit.

### Block 2 — Master pattern tables (30 min)
Read the two tables below aloud. For each row: name the cue, name the pattern, name your exercise. Any row where you hesitate → that's Block 4's material.

### Master table — Algorithms

| Algorithm | Applications | Your exercises |
|---|---|---|
| **BFS** | Shortest unweighted path · level-order · Kahn's topo sort · crawl frontier. **Trap:** mark visited at enqueue time | ShortestPath, GridTraversal, CourseSchedule |
| **DFS** | Cycle detection (three-color) · graph clone (old→new map = visited set) · islands on implicit grids | CycleDetector, CloneGraph, TaskSchedulerDfs, GridTraversal |
| **Union-Find** | Component counting without building the graph · redundant edge · path compression + union by rank | UnionFind, ConnectedComponents, RedundantConnection |
| **Topological sort** | Ordering with prerequisites · cycle → empty result · seed indegree-0 | CourseSchedule, TaskScheduler |
| **Dijkstra** | Weighted shortest path (non-negative) · min-heap of (dist, node) · skip stale entries | stage 8 grid work |
| **Binary search** | Rotated array · peak via slope · sorted matrix two-phase · works whenever a probe eliminates half with certainty — **ask the constraint question first** | searchRotated, findPeakElement, search2DMatrix |
| **Two pointers** | Pair sum on sorted · container max · in-place dedupe · move the pointer that can't be in the answer | narrate (open) |
| **Sliding window** | Longest-no-repeat · min-window · expand right, shrink left `while` broken | narrate (open) |
| **Prefix sums** | Range sums · count subarrays = K via running-sum frequency map | narrate (open) |
| **Greedy** | Line packing (pack words first, render second — **the TextJustification lesson**) · interval folding after sort-by-start | TextJustification ✅, IntervalMerge |
| **1D DP over prefixes** | Reachability marking: dp[i] = "s[0..i) segmentable" · mark landings only, never mid-word · the future depends on where you are, not how you got there | WordBreak ✅ |
| **Tree recursion with ranges** | BST validate/serialize/LCA via min-max bounds · BST = range problem; general tree = sentinel problem | BstValidator, BstSerializer ✅, LCA, TreeSerialize |

### Master table — Data structures

| Data structure | Applications | Your exercises |
|---|---|---|
| **Hash map** | Default tool: counting, seen-before, grouping · O(1) average | KvWithTtl, RandomizedSet |
| **TreeMap** | "As of time t" via floorEntry · consistent-hash ring · query relative to a value | VersionedKvRecall, SnapshotArray |
| **Heap** | Kth largest (min-heap size K) · median (two heaps) · merge K sorted · trap: min-heap for kth LARGEST | KthLargest, MedianFinder, MergeKLists |
| **Stack** | Nested/matching · paired stack carrying aggregate (O(1) min) | StackQueueDrill (MinStack) ✅ |
| **Deque (monotonic)** | Sliding-window max · indices not values, pop-smaller from back | narrate (open) |
| **Queue** | FIFO distribution · BFS frontier · two-stack queue, amortized O(1) | narrate (open) |
| **HashMap + doubly-linked list** | LRU — O(1) get/put · canonical design answer | LruCache |
| **HashMap + array** | O(1) insert/delete/random — swap-with-last delete | RandomizedSet |
| **HashMap&lt;K, TreeMap&gt;** | Versioned/time-series store — **the exact reported question** | VersionedKvRecall |
| **HashMap of sets** | Inverted index — AND = smallest-set intersection | InvertedIndex (Block 1) |
| **Trie** | Prefix search · children map + terminal flag · (also: the WordBreak detour — right instinct, wrong problem) | Trie |
| **Linked list** | Merge K lists via heap of heads · LRU's ordering backbone | MergeKLists, LruCache |
| **Lock + conditions** | One ReentrantLock, notFull/notEmpty, `while`-around-`await` · RW-lock readers-shared | BoundedBlockingQueue, ReadWriteLock, RateLimiter |

### Block 3 — The six reported shapes, one breath each (30 min)
Structure → rejected alternative → per-op cost → thread-safety answer → first 3 asserts:
1. Versioned KV ✅
2. TextJustification ✅ (narrate the pack-then-render split — you lived it)
3. InvertedIndex (from Block 1)
4. Blocking queue ✅
5. KV-with-TTL ✅
6. WordBreak ✅ (narrate reachability-vs-greedy — you lived this too)

### Block 4 — Hesitation repair (30 min)
Whatever wobbled in Blocks 2–3. Read your own code for that exercise. Don't rewrite — recognize.

### Block 5 — Close (15 min)
Read `docs/cheat-sheet.md` once. Then stop. Rest, eat, arrive calm.

---

## Hard-won lessons from tonight (say these in the room)
1. **"By how far do I progress?" = wrong question.** Mark landings; let later iterations stand on them. (WordBreak)
2. **Separate deciding from rendering.** Pack the line's words, then justify — never place characters before you know the line. (TextJustification)
3. **Ask the constraint question before choosing the algorithm.** Distinct neighbors? Rectangular matrix? (BinarySearchVariants)
4. **Peek without consume drops tokens.** One sentinel, one consumer. (BstSerializer)
5. **Estimates lie; patterns don't.** You landed every problem you finished — the cost was time, not correctness.

---

## Skipped exercises — catalog with solutions

Everything below was deliberately cut. Each has a reference solution in the `solutions/` folder, rewritten in your own style — read them in Block 2/4 if a pattern row wobbles, in this priority order.

| Exercise | Pattern | Why skipped | Priority to review | Solution |
|---|---|---|---|---|
| TwoPointers | Sorted-array pair sum, container max, in-place dedupe | Cut for time; arrays already strong | 2 | `solutions/stage10/TwoPointers.kt` |
| SlidingWindow | Variable window + last-seen map | Cut for time; narrate-only | 2 | `solutions/stage10/SlidingWindow.kt` |
| PrefixSums | Prefix array + running-sum frequency map | Cut for time; narrate-only | 2 | `solutions/stage10/PrefixSums.kt` |
| HitCounter | Circular/deque buckets, half-open window | Cut for time; rate-limiter family already proven | 3 | `solutions/stage13/HitCounter.kt` |
| SlidingWindowMaximum | Monotonic deque of indices | Cut for time; deque family proven in screen | 3 | `solutions/stage11/SlidingWindowMaximum.kt` |
| TaskSchedulerWithCooldown | Greedy most-frequent-first; closed form | Cut for time; not MongoDB-reported | 4 | `solutions/stage11/TaskSchedulerWithCooldown.kt` |
| WebCrawler | BFS + worker pool + dequeue-time visited | Cut for time; concurrency armor already via blocking queue | 4 | `solutions/stage12/WebCrawler.kt` |
| LockFreeCounter | AtomicLong CAS | Dropped to fund stack/queue drill; CAS narration folded into blocking-queue answer | 4 | `solutions/stage12/LockFreeCounter.kt` |
| BasicDp | Rolling O(1)-space recurrence | DP paradigm cut per recruiter steer; WordBreak landed without it | 4 | `solutions/stage14/BasicDp.kt` |
| StringDp | 2D DP tables (LCS, edit distance) | Recruiter steer argues against hard DP | 5 | `solutions/stage14/StringDp.kt` |
| wordBreakIi | Memoized DFS enumeration | Extension of landed question; part I covers the skill | 5 | `solutions/stage14/WordBreak.kt` |
| ConsistentHashing | TreeMap ring + virtual nodes | System-design flavor, not a DSA ask | 5 | `solutions/stage13/ConsistentHashing.kt` |
| LfuCache | Frequency buckets + minFreq | Only LC-hard; rarely asked; LRU done | 5 | `solutions/stage13/LfuCache.kt` |
| AlienDictionary | Topo sort on inferred alphabet | Graphs already 8 deep; rare ask | 6 | `solutions/stage8/AlienDictionary.kt` |
| Dijkstra | Weighted shortest path, min-heap | Redundant with stage 8 shortest-path work | 6 | `solutions/stage11/Dijkstra.kt` |
