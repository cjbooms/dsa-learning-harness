# Implementation Recipes — DSA Skeletons for CoderPad

**GLANCE-AT doc — open during the interview.** Skeletons only, no prose.
Read `cheat-sheet.md` beforehand for the why.

## Imports (paste, delete unused)

```kotlin
import kotlin.collections.ArrayDeque   // Kotlin's — never java.util.ArrayDeque
import java.util.PriorityQueue
import java.util.TreeMap
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.locks.ReentrantLock
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.thread
import kotlin.concurrent.withLock
import kotlin.concurrent.write
// NO import: HashMap, HashSet, ArrayList, mutableListOf, mutableMapOf/SetOf, List/Set/Map
```

## ArrayDeque — stack / queue / deque (no import)

```kotlin
val d = ArrayDeque<Int>()
d.addLast(x)              // push / enqueue
d.removeLast()            // pop (stack)          — throws if empty
d.removeFirst()           // dequeue (queue)      — throws if empty
d.removeLastOrNull()      // safe pop
d.removeFirstOrNull()     // safe dequeue
d.first() / d.last()      // peek front / back
d.firstOrNull() / d.lastOrNull()
```

## PriorityQueue — heap

```kotlin
val minHeap = PriorityQueue<Int>()                           // smallest on top
val maxHeap = PriorityQueue<Int>(compareByDescending { it })
val byFirst = PriorityQueue<Pair<Int,Int>>(compareBy { it.first })
heap.add(x); heap.peek(); heap.poll()                        // poll removes min
// top-K: keep size K, poll when size > K — heap holds the K largest
// trap: kth LARGEST wants a MIN-heap of size K
```

## TreeMap — sorted map

```kotlin
val map = TreeMap<Long, String>()
map[k] = v; map[k]
map.floorEntry(k)         // greatest key <= k — "as of time t", versioned KV
map.ceilingEntry(k)       // smallest key >= k
map.firstKey(); map.lastKey()
map.headMap(k, true)      // live view, keys <= k — .clear() removes from parent
map.tailMap(k, false)     // keys > k
```

## HashMap — no import

```kotlin
map.getOrPut(k) { 0 }
map.getOrDefault(k, 0)
map.computeIfAbsent(k) { expensive() }
for ((k, v) in map) { }
```

## Map of sets — inverted index

```kotlin
val termToDocIds = hashMapOf<String, MutableSet<String>>()
val docIdToTerms = hashMapOf<String, Set<String>>()   // reverse map: O(terms) delete

// insert (REPLACE semantics — evict old terms first or they go stale)
docIdToTerms[docId]?.forEach { termToDocIds[it]?.remove(docId) }
val terms = text.split(Regex("\\s+")).map { it.lowercase() }.toSet()
docIdToTerms[docId] = terms
for (t in terms) termToDocIds.getOrPut(t) { mutableSetOf() }.add(docId)

// search one      -> termToDocIds[term] ?: emptySet()
// search ALL      -> intersect smallest-first
val buckets = terms.mapNotNull { termToDocIds[it] }.sortedBy { it.size }
if (buckets.size < terms.size) emptySet() else buckets.reduce { a, b -> a.intersect(b) }

// delete
docIdToTerms.remove(docId)?.forEach { termToDocIds[it]?.remove(docId) }
```

## Binary search

```kotlin
var left = 0; var right = lastValidIndex            // NOT size — size - 1
while (left <= right) {
    val mid = left + (right - left) / 2
    if (a[mid] == target) return mid
    if (a[mid] < target) left = mid + 1 else right = mid - 1
}
// "first true" variant: while (left < right) { if (pred(mid)) right = mid else left = mid + 1 }
```

## Two pointers

```kotlin
var l = 0; var r = a.size - 1
while (l < r) {
    val sum = a[l] + a[r]
    when {
        sum < target -> l++
        sum > target -> r--
        else -> return l to r
    }
}
```

## Sliding window

```kotlin
var start = 0; var best = 0
val state = hashMapOf<Char, Int>()
for (end in s.indices) {
    // expand: add s[end] to state
    while (invalid()) { /* remove s[start] from state */ start++ }
    best = maxOf(best, end - start + 1)
}
```

## Prefix DP / reachability — word break shape

```kotlin
val reachable = BooleanArray(s.length + 1)
reachable[0] = true
for (i in 0 until s.length) {
    if (!reachable[i]) continue               // launch only from reachable
    for (j in i until s.length) {
        if (wordDict.contains(s.substring(i, j + 1))) {
            reachable[j + 1] = true           // mark the LANDING only
        }
    }
}
// answer: reachable[s.length]
```

## 2D DP — LCS / edit distance

```kotlin
// dp[i][j] = answer for a[0..i) vs b[0..j) — SAY THIS ALOUD FIRST
val dp = Array(m + 1) { IntArray(n + 1) }
for (i in 1..m) for (j in 1..n) {
    dp[i][j] = if (a[i-1] == b[j-1]) dp[i-1][j-1] + 1
               else maxOf(dp[i-1][j], dp[i][j-1])
}
```

## BFS

```kotlin
val queue = ArrayDeque<Node>()
val visited = hashSetOf<Node>()
queue.addLast(start); visited.add(start)      // mark at ENQUEUE time
while (queue.isNotEmpty()) {
    val node = queue.removeFirst()
    for (next in node.neighbors) {
        if (visited.add(next)) queue.addLast(next)
    }
}
```

## DFS + cycle detection (three-color)

```kotlin
val state = hashMapOf<Node, Int>()            // 0=white 1=gray 2=black
fun dfs(n: Node): Boolean {                   // true = cycle
    if (state[n] == 1) return true
    if (state[n] == 2) return false
    state[n] = 1
    for (next in n.neighbors) if (dfs(next)) return true
    state[n] = 2
    return false
}
```

## Union-Find

```kotlin
val parent = IntArray(n) { it }
fun find(x: Int): Int {
    if (parent[x] != x) parent[x] = find(parent[x])   // path compression
    return parent[x]
}
fun union(a: Int, b: Int): Boolean {                  // false = already connected (cycle)
    val (ra, rb) = find(a) to find(b)
    if (ra == rb) return false
    parent[ra] = rb
    return true
}
```

## Simulation — pack first, render second (text justification)

```kotlin
// 1: pack words per line — decide BEFORE placing anything
//    fits if sum(lengths) + (count - 1) <= maxWidth
// 2: render each line:
//    last line or single word -> join(" ") + padEnd(maxWidth)
//    else pool = maxWidth - sum(lengths); gap = pool / (count-1);
//         extra = pool % (count-1) goes to leftmost gaps, one each
```

## LRU — HashMap + doubly-linked list

```kotlin
class Node(val k: Int, var v: Int) { var prev: Node? = null; var next: Node? = null }
val map = hashMapOf<Int, Node>()
// get: map hit -> move node to head, return v
// put: insert at head; if size > cap, evict tail, remove tail.k from map
// sentinel head/tail nodes kill the null checks
```

## ReentrantLock + conditions — bounded blocking queue

```kotlin
val lock = ReentrantLock()
val notFull = lock.newCondition()
val notEmpty = lock.newCondition()

fun put(item: T) = lock.withLock {
    while (queue.size == capacity) notFull.await()   // WHILE, never if
    queue.addLast(item)
    notEmpty.signal()
}
fun take(): T = lock.withLock {
    while (queue.isEmpty()) notEmpty.await()
    val v = queue.removeFirst()
    notFull.signal()
    return v
}
```

## ReadWriteLock

```kotlin
val rw = ReentrantReadWriteLock()
rw.read { /* shared */ }
rw.write { /* exclusive */ }
// trap: a "read" that mutates (LRU access-order get) needs write { }
```

## Atomics

```kotlin
val count = AtomicLong(0)
count.incrementAndGet()
count.compareAndSet(expect, update)
// one independent value -> atomic; multiple related fields -> lock
```

## Threads + latch — stress-test a concurrent thing

```kotlin
val done = CountDownLatch(workerCount)
repeat(workerCount) {
    thread {                            // kotlin.concurrent.thread — lightweight
        repeat(1000) { counter.incrementAndGet() }
        done.countDown()
    }
}
done.await()                            // blocks until 0
// expected exact total: workers * 1000
```

## ConcurrentHashMap

```kotlin
val map = ConcurrentHashMap<String, Int>()
map.merge(k, 1) { old, _ -> old + 1 }   // atomic read-modify-write
map.computeIfAbsent(k) { expensive() }
// trap: get-then-put as two calls is NOT atomic — use merge/compute
```
