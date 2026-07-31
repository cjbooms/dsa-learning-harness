# Interview Cheat Sheet — Kotlin/JVM Flash Cards

Glance-level reminders. Kotlin standard library first, then `java.util` /
`java.util.concurrent` (what CoderPad Kotlin actually runs on).

---

## Core Data Structures

| Structure | When to reach for it | Key ops (all on JVM) |
|---|---|---|
| `HashMap` / `HashSet` | Default lookup / remove-duplicates. O(1) average. No order. | `getOrPut(k){...}`, `computeIfAbsent`, `contains` |
| `LinkedHashMap` | Need insertion order — or **LRU (least-recently-used) cache** via `accessOrder=true` + `removeEldestEntry` | same as HashMap |
| `TreeMap` / `TreeSet` | Sorted keys, range queries, "greatest ≤ x". O(log n). | `floorEntry(x)`, `ceilingEntry(x)`, `headMap(k)`, `tailMap(k)`, `firstKey()` |
| `PriorityQueue` | "Smallest/largest first" — top-K (the K largest/smallest items), merge K sorted lists, schedulers. O(log n) push/pop. | `add`, `peek` (min), `poll`. Max-heap: `PriorityQueue(compareByDescending{it})` |
| `ArrayDeque` | Stack AND queue AND circular buffer. Never `Stack`/`LinkedList`. | `addLast/removeFirst` (queue), `addLast/removeLast` (stack) |
| `ArrayList` | Random access, two-pointer walks | `list[i]`, amortized O(1) append |
| `IntArray`/`BooleanArray` | DP (dynamic programming) tables, letter counts — primitives, no boxing | `IntArray(26)`, `BooleanArray(n+1)` |

**Complexity anchors:** HashMap O(1) · TreeMap O(log n) · heap push/pop O(log n) ·
deque ends O(1) · binary search O(log n) · sort O(n log n).

---

## Algorithms Toolkit

```
BINARY SEARCH (sorted data, or "min value satisfying predicate")
  var lo=0; var hi=last
  while (lo<hi) { val mid=(lo+hi)/2; if (ok(mid)) hi=mid else lo=mid+1 }
  // on TreeMap: floorEntry/ceilingEntry IS binary search — don't hand-roll

TWO POINTERS (sorted array, pairs/triples, palindromes)
  lo=0, hi=n-1; while(lo<hi): sum<target -> lo++; sum>target -> hi--

SLIDING WINDOW ("longest/shortest subarray with property P")
  expand right; while(windowInvalid) shrink left; record best

BFS = breadth-first search (shortest path unweighted, level order) -> ArrayDeque as queue
DFS = depth-first search (exhaustive, backtracking, cycles)        -> recursion + visited set

BACKTRACKING template
  fun bt(state) { if (done) record; for (choice in choices) { choose; bt; unchoose } }

DP (dynamic programming) — say the recurrence BEFORE coding:
  1. define state  2. base case  3. transition  4. answer location
  prefix DP: dp[i] = "answer for first i elements", dp[0] = empty = true/0

TOP-K (the K largest) -> min-heap of size K: offer, if size>K poll. O(n log k)
MERGE K SORTED -> heap of (value, listIdx); poll min, advance that list
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
    while (buffer.size == cap) notFull.await()   // while, NEVER if (spurious wakeup)
    buffer.add(x)
    notEmpty.signal()                            // wake ONE waiter
}
```

### Old idiom (know it, they ask)

```kotlin
@Synchronized fun put(x: T) {
    while (full) (this as Object).wait()         // wait() releases the monitor
    ...
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
val flag = AtomicBoolean(); val ref = AtomicReference<T>()
// Use when: ONE independent value. Multiple related fields -> lock instead.
```

### Semaphore — "N permits" (connection pools, rate limits, bounded concurrency)

```kotlin
val sem = Semaphore(3)                           // 3 concurrent holders max
sem.acquire()                                    // blocks until a permit free
try { /* use resource */ } finally { sem.release() }
sem.tryAcquire()                                 // non-blocking -> Boolean
sem.tryAcquire(100, MILLISECONDS)                // timed -> Boolean
// acquire = take a permit (blocks), release = give it back (always in finally)
// Semaphore(1) is roughly a lock, but NOT reentrant and has no owner.
```

### CountDownLatch — one-shot "wait for N things to finish"

```kotlin
val done = CountDownLatch(workerCount)
worker:  done.countDown()
main:    done.await()                            // blocks until count hits 0
// One-shot: can't reset. Great for tests (hammer from N threads).
```

### Executors

```kotlin
val pool = Executors.newFixedThreadPool(n)
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
LOST UPDATE:  x++  is read/add/write. Two threads read 5, both write 6.
CHECK-THEN-ACT: if (balance >= amt) balance -= amt  — check is stale by act time.
VISIBILITY: without synchronization/volatile/atomic, thread B may never see
            thread A's write (the Java Memory Model allows caching per thread).
Fixes: AtomicInteger & friends (single value) | lock (multi-field invariant) | volatile (flag only)
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
| DP prefix (word break) | O(n²) |
| k-sum | O(n^(k-1)) |
