# Interview Cheat Sheet — Kotlin/JVM Flash Cards

Glance-level reminders. Kotlin standard library first, then `java.util` /
`java.util.concurrent` (what CoderPad Kotlin actually runs on).

---

## Core Data Structures

| Structure | When to reach for it | Key operations |
|---|---|---|
| `HashMap` / `HashSet` | Default lookup / remove-duplicates. O(1) average. No order. | `getOrPut(k){...}`, `computeIfAbsent`, `contains` |
| `LinkedHashMap` | Insertion order — or **LRU (least-recently-used) cache** via `accessOrder=true` + `removeEldestEntry` | same as HashMap |
| `TreeMap` / `TreeSet` | Sorted keys, range queries, "greatest ≤ x". O(log n). | `floorEntry(x)`, `ceilingEntry(x)`, `headMap(k)`, `tailMap(k)`, `firstKey()` |
| `PriorityQueue` | "Smallest/largest first" — top-K, merge K sorted lists, schedulers. O(log n) push/pop. | `add`, `peek` (min), `poll`. Max-heap: `PriorityQueue(compareByDescending{it})` |
| `ArrayDeque` | Stack AND queue AND circular buffer. Never `Stack`/`LinkedList`. | `addLast/removeFirst` (queue), `addLast/removeLast` (stack) |
| `ArrayList` | Random access, two-pointer walks | `list[i]`, amortized O(1) append |
| `IntArray`/`BooleanArray` | Dynamic programming tables, letter counts — primitives, no boxing | `IntArray(26)`, `BooleanArray(n+1)` |

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
// Prefix DP pattern (word break, climbing stairs, ...):
// dp[i] = "answer for the first i elements"
val dp = BooleanArray(input.length + 1)
dp[0] = true                       // empty prefix
for (end in 1..input.length) {
    for (split in 0..<end) {
        if (dp[split] && isValid(input, split, end)) { dp[end] = true; break }
    }
}
```

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
// TRAP: if your "read" mutates (LRU access-order get reorders entries),
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

## Kotlin notes for CoderPad

- `list.sorted()`, `list.groupBy{}` are fine, but **say the complexity** — and know
  the manual loop version.
- `?.let`, `?: return null`, `withLock {}` are idiomatic and read well aloud.
- Avoid bleeding-edge syntax; CoderPad's Kotlin version lags.
- `ArrayDeque`, `PriorityQueue`, `TreeMap` need `import java.util.*`.

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
