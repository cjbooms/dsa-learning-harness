# Exercise Prompts — What the Interviewer Gives You

Each prompt below is written the way it would actually arrive: a short verbal
framing plus a starter signature in CoderPad. **Do not read the solution files in
`src/main/kotlin/` until after you've attempted the drill.**

The "Follow-ups" section is what the interviewer springs on you *after* your first
solution works — expect at least one per round.

---

## DSA-1: Versioned Document Store ⭐

> "We're building a document database. Every document has an ID, and we keep every
> version of it with a timestamp. I need two operations: write a version of a
> document, and read a document *as of* a timestamp — give me the most recent
> version that existed at that time. Oh, and writes can arrive out of order —
> timestamps aren't monotonic."

```kotlin
class VersionedKVStore {
    fun put(docId: String, contents: String, timestamp: Long) { TODO() }
    fun get(docId: String, timestamp: Long): String? { TODO() }
}
```

**Examples they'd walk through:**
```
put("doc1", "v1", ts=1); put("doc1", "v2", ts=5)
get("doc1", 5)  -> "v2"
get("doc1", 3)  -> "v1"
get("doc1", 0)  -> null        // nothing existed yet
get("doc2", 9)  -> null        // unknown doc
put("doc1", "v3", ts=3)        // out of order!
get("doc1", 4)  -> "v3"
```

**Follow-ups:** add `delete(docId, ts)` (tombstones) · bound memory by keeping only
the last N versions · make it thread-safe · what if timestamps collide?

---

## DSA-2: Group Anagrams

> "Given a list of words, group the ones that are anagrams of each other."

```kotlin
fun groupAnagrams(words: List<String>): List<List<String>>
```

```
["eat","tea","tan","ate","nat","bat"] -> [["eat","tea","ate"],["tan","nat"],["bat"]]
```

**Follow-ups:** case-insensitivity · streaming input (words arrive one at a time) ·
what's the cost of your key function — can you avoid sorting each word?

---

## DSA-3: Word Break

> "Given a string and a dictionary of words, can the string be segmented into a
> sequence of dictionary words? Words can be reused."

```kotlin
fun wordBreak(s: String, wordDict: List<String>): Boolean
```

```
("leetcode", ["leet","code"])                 -> true
("catsandog", ["cats","dog","sand","and","cat"]) -> false
```

**Follow-ups:** return *one* valid segmentation · return *all* segmentations ·
what's the worst case and what input triggers it?

---

## DSA-4: K-Sum

> "Count how many k-element subsets of this array sum to the target."

```kotlin
fun kSumCount(nums: IntArray, k: Int, target: Long): Long
```

```
([1,2,3,4,5], k=2, target=6) -> 2    // (1,5), (2,4)
```

**Follow-ups:** return the tuples themselves · dedupe identical tuples · k=2 must be
O(n) — how?

---

## CONC-1: Bounded Blocking Queue ⭐

> "Implement a bounded queue. `put` blocks when it's full, `take` blocks when it's
> empty. Multiple producer and consumer threads will use it. Don't use
> `java.util.concurrent.BlockingQueue` — build it from primitives."

```kotlin
class BoundedBlockingQueue<T>(private val capacity: Int) {
    fun put(item: T) { TODO() }
    fun take(): T { TODO() }
}
```

**What they're really testing:** `while` loops around `await()` (not `if`), which
condition(s) you signal, whether you can explain *why*.

**Follow-ups:** add `offer(item, timeoutMs)` · "now the queue feeds a thread pool —
make `submit()` block when the executor's queue is full" · fairness: wake waiters
FIFO · do it again with only `synchronized`/`wait`/`notify` — why does that version
need `notifyAll`?

---

## CONC-2: Thread-Safe LRU Cache

> "Implement an LRU cache — fixed capacity, evict least-recently-used. It will be
> called from many threads concurrently."

```kotlin
class ThreadSafeLruCache<K, V>(private val maxSize: Int) {
    fun get(key: K): V? { TODO() }
    fun put(key: K, value: V) { TODO() }
}
```

**The trap to mention unprompted:** `get` mutates recency order, so a read-write
lock doesn't let `get` take the *read* lock. Say this before they ask.

**Follow-ups:** why not just `ConcurrentHashMap`? · lock striping for throughput ·
add per-entry TTL · make it a loading cache (compute-on-miss, single-flight).

---

## CONC-3: Find and Fix the Race

> *(They paste broken code into the pad.)* "This counter/account is misbehaving in
> production. What's wrong, and how do you fix it?"

```kotlin
class Counter {
    var count = 0
        private set
    fun increment() { count++ }
}

class BankAccount(var balance: Long) {
    fun withdraw(amount: Long): Boolean {
        if (balance >= amount) { balance -= amount; return true }
        return false
    }
}
```

**What they're testing:** can you name the exact interleaving that breaks it (draw
the two-thread timeline), atomicity vs visibility, and when you'd pick
`AtomicInteger` vs a lock.

**Follow-ups:** fix it three ways and compare · what does the JMM guarantee without
synchronization? · where else in this codebase pattern would this bug hide?

---

## REAL-1: Replication Lag Alerter ⭐

> "We're migrating data from a primary database to a secondary. An event fires when
> a record leaves the primary, and another when it arrives at the secondary. If any
> record takes more than k seconds to replicate, raise an alert. Events can arrive
> out of order."

```kotlin
class ReplicationLagAlerter(private val maxLagSeconds: Long) {
    fun onPrimaryLeave(recordId: String, timestampSeconds: Long) { TODO() }
    fun onSecondaryArrive(recordId: String, timestampSeconds: Long): Long? { TODO() }
    fun poll(nowSeconds: Long): List<String> { TODO() }  // records breaching the lag
}
```

**Follow-ups:** alert exactly once per record · events stream from multiple shards
interleaved · report *average* lag over a sliding window instead · what if the leave
event is lost entirely?

---

## How interviewers score these (Staff+ bar)

- Clarify before coding — restate, pin down edge cases, agree on the signature.
- Narrate the plan and complexity *before* implementing.
- Working, readable code beats clever code.
- Walk through their examples, then your own edge cases, unprompted.
- Take hints gracefully — collaboration is explicitly part of the evaluation.
