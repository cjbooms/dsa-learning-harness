# Interview Cheat Sheet — Kotlin/JVM Flash Cards

**This is the read-BEFORE doc — morning-of, then close it.** Concepts, traps,
and the *why*. (Keep `implementation-recipes.md` open during the interview for
copy-paste skeletons.)

Kotlin standard library first, then `java.util` / `java.util.concurrent`
(what CoderPad Kotlin actually runs on).

---

## Core Data Structures

| Structure | When to reach for it | Key operations |
|---|---|---|
| `HashMap` / `HashSet` | Default lookup / remove-duplicates. O(1) average. No order. | `getOrPut(k){...}`, `computeIfAbsent`, `contains` |
| `LinkedHashMap` | Insertion order — or **LRU (least-recently-used) cache** via `accessOrder=true` + `removeEldestEntry` | same as HashMap |
| `TreeMap` / `TreeSet` | Sorted keys, range queries, "greatest ≤ x". O(log n). | `floorEntry(x)`, `ceilingEntry(x)`, `headMap(k)`, `tailMap(k)`, `firstKey()` |
| `PriorityQueue` | "Smallest/largest first" — top-K, merge K sorted lists, schedulers. O(log n) push/pop. | `add`, `peek` (min), `poll`. Max-heap: `PriorityQueue(compareByDescending{it})` |
| `ArrayDeque` | Stack AND queue AND circular buffer. **Kotlin's (`kotlin.collections`), no import — never `java.util.ArrayDeque`, never `Stack`/`LinkedList`.** | `addLast/removeFirst` (queue), `addLast/removeLast` (stack), `removeFirstOrNull`/`removeLastOrNull` (Kotlin-only safe pops) |
| `ArrayList` | Random access, two-pointer walks | `list[i]`, amortized O(1) append |
| `IntArray`/`BooleanArray` | Dynamic programming tables, letter counts — primitives, no boxing | `IntArray(26)`, `BooleanArray(n+1)` |

### Choosing between the maps (the interview question behind the question)

- **HashMap — hash table.** Key → bucket via hashCode. O(1) average get/put, O(n)
  worst (hash collisions). No order at all. Reach for it by DEFAULT.
- **LinkedHashMap — hash table + doubly-linked list threading the entries.**
  Same O(1), but iterates in insertion order (or access order). Costs: more
  memory per entry, slightly slower writes. Reach for it when ORDER = arrival
  order matters (least-recently-used cache, "first inserted wins", deterministic iteration).
- **TreeMap — red-black tree** (a self-balancing binary SEARCH tree). Keys kept
  SORTED, always. O(log n) everything — no O(1) anywhere. Reach for it when you
  need order by KEY VALUE: range queries, "greatest ≤ x" (`floorEntry`),
  "smallest ≥ x" (`ceilingEntry`), head/tail splits.

**Red-black tree vs plain binary search tree:** a plain binary search tree degrades to a linked
list on sorted input (O(n) operations). Red-black adds rebalancing rules
(recoloring + rotations on insert/delete) that keep height ≈ log n — that's how
TreeMap guarantees O(log n) worst case. You will never implement one; you say
"self-balancing binary search tree, rotations keep it O(log n)" and move on.
**Binary search tree vs binary heap:** the search tree = total order (in-order traversal yields sorted),
supports floor/ceiling. Heap = partial order (parent ≤ children only), supports
min/max in O(1) peek — but no search, no ranges, no floor.

**The pattern that keeps recurring:** sorted map + binary-search-to-a-neighborhood
+ linear scan within it. floorEntry/ceilingEntry/headMap — versioned reads,
streaming interval merge, lag alerter. Recognize the shape, name it aloud.

**Complexity anchors:** HashMap O(1) · TreeMap O(log n) · heap push/pop O(log n) ·
deque ends O(1) · binary search O(log n) · sort O(n log n).

---

## Algorithms Toolkit

### Binary search

*Sorted data, or "smallest value where a predicate becomes true".*

```kotlin
var left = 0
var right = lastValidIndex
while (left < right) {
    val middle = (left + right) / 2
    if (predicateHolds(middle)) right = middle else left = middle + 1
}
// left == right is the answer
```

> On a `TreeMap`, `floorEntry` / `ceilingEntry` **is** binary search — don't hand-roll it.

> **Ask the constraint question BEFORE choosing the algorithm.** Binary search on
> unsorted-looking data works whenever each probe eliminates half with certainty —
> but the certainty usually rests on an unstated constraint. Rotated array: elements
> distinct? Peak search: neighbors distinct? (equals kill the slope argument — a peak
> may not even exist). Sorted matrix: rows all the same length? (flattening breaks on
> jagged input; two-phase row-then-column search doesn't need it). Say the assumption
> aloud, then code to it.

### Two pointers

*Sorted array: pairs/triples summing to a target, palindrome checks.*

```kotlin
var left = 0
var right = numbers.size - 1
while (left < right) {
    val sum = numbers[left] + numbers[right]
    when {
        sum < target -> left++    // need a bigger sum
        sum > target -> right--   // need a smaller sum
        else -> { /* found a pair */ left++; right-- }
    }
}
```

### Sliding window

*"Longest/shortest contiguous subarray satisfying some property".*

```kotlin
var windowStart = 0
var best = 0
for (windowEnd in items.indices) {
    // expand: add items[windowEnd] to the window state
    while (windowIsInvalid()) {
        // shrink: remove items[windowStart] from the window state
        windowStart++
    }
    best = maxOf(best, windowEnd - windowStart + 1)
}
```

### Graph traversal

- **BFS** (breadth-first search): shortest path on *unweighted* graphs, level-order. Use `ArrayDeque` as a queue + a visited set.
- **DFS** (depth-first search): exhaustive search, cycle detection, backtracking. Use recursion + a visited set.

```kotlin
// BFS skeleton
val queue = ArrayDeque<Node>()
val visited = HashSet<Node>()
queue.add(start); visited.add(start)
while (queue.isNotEmpty()) {
    val node = queue.removeFirst()
    for (neighbor in node.neighbors) {
        if (visited.add(neighbor)) queue.addLast(neighbor)
    }
}
```

### Backtracking

*Generate all combinations / permutations / subsets — "try, recurse, undo".*

```kotlin
fun backtrack(currentChoices: MutableList<T>) {
    if (solutionIsComplete(currentChoices)) {
        record(currentChoices.toList())   // copy!
        return
    }
    for (candidate in remainingCandidates()) {
        currentChoices.add(candidate)     // choose
        backtrack(currentChoices)         // explore
        currentChoices.removeAt(currentChoices.lastIndex)  // un-choose
    }
}
```

### Dynamic programming

*Overlapping subproblems. Say the recurrence OUT LOUD before coding:*

1. **State** — what does `dp[i]` mean, in one sentence?
2. **Base case** — usually the empty/single-element input.
3. **Transition** — how `dp[i]` builds on earlier entries.
4. **Answer** — which entry holds the result?

```kotlin
// Prefix DP / reachability pattern (word break, climbing stairs, ...):
// dp[i] = "the first i elements are achievable"
// Only extend from REACHABLE positions — a marker means "some path got here",
// never "the path I took to get here". The future depends on where you are,
// not how you arrived.
val reachable = BooleanArray(input.length + 1)
reachable[0] = true                       // empty prefix
for (i in 0 until input.length) {
    if (!reachable[i]) continue           // can't launch from an unreachable position
    for (j in i until input.length) {
        if (isValid(input, i, j + 1)) {
            reachable[j + 1] = true       // mark the LANDING only, never mid-word
        }
    }
}
// answer: reachable[input.length]
```

> **Simulation / rendering problems (text justification, spiral order):**
> decide first, render second. Collect the whole line/group/chunk before
> placing a single output character — you can't redistribute spaces you've
> already placed. Two loops, not one.

### Top-K (the K largest items)

*Min-heap of size K — the heap's root is the smallest of the current best K.*

```kotlin
val heap = PriorityQueue<Item>()   // keeps the K largest seen so far
for (item in items) {
    heap.add(item)
    if (heap.size > k) heap.poll() // evict smallest -> heap holds top K
}
// O(n log k) — better than sorting when k << n
```

### Merge K sorted lists

```kotlin
val heap = PriorityQueue<Entry>(compareBy { it.value })
// seed heap with the head of each list
while (heap.isNotEmpty()) {
    val smallest = heap.poll()
    emit(smallest.value)
    smallest.nextFromSameList?.let { heap.add(it) }
}
```

---

## Concurrency Primitives (`java.util.concurrent`)

### Locks

```kotlin
val lock = ReentrantLock()
lock.withLock { /* critical section */ }          // always withLock — auto-unlock

// ReadWriteLock: many readers OR one writer.
val rw = ReentrantReadWriteLock()
rw.read { }  /  rw.write { }
// TRAP: if your "read" mutates (least-recently-used access-order get reorders entries),
// it needs the WRITE lock.

// Conditions — the producer/consumer tool. One lock, MULTIPLE wait-sets.
val notFull = lock.newCondition(); val notEmpty = lock.newCondition()
lock.withLock {
    while (buffer.size == capacity) notFull.await()  // while, NEVER if (spurious wakeup)
    buffer.add(item)
    notEmpty.signal()                                // wake ONE waiter
}
```

### Old idiom (know it, they ask)

```kotlin
@Synchronized fun put(item: T) {
    while (isFull) (this as Object).wait()       // wait() releases the monitor
    // ...
    (this as Object).notifyAll()                 // MUST be notifyAll: one wait-set
}                                                // mixes producers + consumers
```

### Atomics — lock-free single values

```kotlin
val count = AtomicInteger(0)
count.incrementAndGet()                          // CAS loop, atomic read-modify-write
count.compareAndSet(expect, update)              // CAS = compare-and-swap: "set to
                                                 // `update` only if current == expect";
                                                 // the primitive everything builds on
count.getAndAdd(delta)
val flag = AtomicBoolean(); val ref = AtomicReference<MyType>()
// Use when: ONE independent value. Multiple related fields -> lock instead.
```

### Semaphore — "N permits" (connection pools, rate limits, bounded concurrency)

```kotlin
val semaphore = Semaphore(3)                     // 3 concurrent holders max
semaphore.acquire()                              // blocks until a permit is free
try { /* use resource */ } finally { semaphore.release() }
semaphore.tryAcquire()                           // non-blocking -> Boolean
semaphore.tryAcquire(100, MILLISECONDS)          // timed -> Boolean
// acquire = take a permit (blocks), release = give it back (always in finally)
// Semaphore(1) is roughly a lock, but NOT reentrant and has no owner.
```

### CountDownLatch — one-shot "wait for N things to finish"

```kotlin
val done = CountDownLatch(workerCount)
// in each worker:  done.countDown()
// in main:         done.await()   // blocks until count hits 0
// One-shot: can't reset. Great for tests (hammer from N threads).
```

### Executors

```kotlin
val pool = Executors.newFixedThreadPool(threadCount)
pool.submit { task() }
pool.shutdown(); pool.awaitTermination(10, SECONDS)
// EXPECT the follow-up: "executor queue is unbounded by default —
// how do you make submit() block when full?" -> wrap with your BoundedBlockingQueue.
```

### Concurrent collections

| Class | Use |
|---|---|
| `ConcurrentHashMap` | shared map, no full lock. `computeIfAbsent`, `merge`, `putIfAbsent` are atomic |
| `CopyOnWriteArrayList` | read-heavy listener lists; writes copy the array |
| `LinkedBlockingQueue` | ready-made blocking queue (but interviews want YOU to build one) |

**ConcurrentHashMap trap:** `get` + `put` as two calls is NOT atomic — use `compute`/`merge`.

---

## Race Spotting — the three bug archetypes

```
LOST UPDATE:     x++ is read/add/write. Two threads read 5, both write 6.
CHECK-THEN-ACT:  if (balance >= amount) balance -= amount
                 — the check's result is stale by the time the act runs.
VISIBILITY:      without synchronization/volatile/atomic, thread B may never see
                 thread A's write (the Java Memory Model allows per-thread caching).

Fixes:  AtomicInteger & friends (single value)
        lock (multi-field invariant)
        volatile (simple flag only)
```

## Sequential bug archetypes (single-threaded, just as deadly)

```
PEEK-WITHOUT-CONSUME:  reading queue.first() / peek() to decide, returning early,
                       and forgetting to remove the token — the same sentinel gets
                       re-read by the next consumer. (BstSerializer: one null
                       sentinel served three consumers; right children vanished.)
                       Rule: one token, one consumer. Peek-then-branch means the
                       consume must happen on EVERY branch.
STALE WRITE-BEFORE-READ: overwriting a record without cleaning up its old
                       side-effects. (InvertedIndex: re-insert with new text left
                       the docId in the OLD terms' buckets — search("old term")
                       still hit.) Rule: replace = delete-then-insert, or evict
                       before overwrite.
```

## Kotlin notes for CoderPad

- `list.sorted()`, `list.groupBy{}` are fine, but **say the complexity** — and know
  the manual loop version.
- `?.let`, `?: return null`, `withLock {}` are idiomatic and read well aloud.
- Avoid bleeding-edge syntax; CoderPad's Kotlin version lags.
- `PriorityQueue`, `TreeMap` need `import java.util.*`. `ArrayDeque` does NOT — it's `kotlin.collections.ArrayDeque`, implicit; never import the java.util one.

## Complexity answers to have ready

| Operation | Cost |
|---|---|
| hash get/put | O(1) average |
| tree floor/insert | O(log n) |
| heap offer/poll | O(log n), peek O(1) |
| sort | O(n log n) |
| BFS/DFS on graph | O(V + E) — vertices + edges |
| prefix DP (word break) | O(n²) |
| k-sum | O(n^(k-1)) |
