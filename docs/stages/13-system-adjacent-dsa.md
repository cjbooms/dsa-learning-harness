# Stage 13 — System-Adjacent DSA

**Time budget: 140 min** · Package: `stages/stage13`

## The meta-skill

These are the "implement a data structure" questions that map directly to
database internals: query caches, metrics windows, shard routing. The structure
ritual is the whole question — the interviewer is listening for the map
direction, the eviction hook, and the cost of each operation.

## Exercises

### 13.1 LRU cache (25 min)
`get(key)` / `put(key, value)` in `LruCache.kt`
- [ ] HashMap + doubly-linked list — O(1) get and put
- [ ] get() on a hit re-links to MRU — it's a MUTATION, not a read
- [ ] Evict the head (LRU) when over capacity; re-link existing node on re-put
- [ ] Aloud: why LinkedHashMap accessOrder is the shortcut; why CHM alone can't

### 13.2 Time-based hit counter (20 min)
`hit(timestampSeconds)` / `getHits(timestampSeconds)` in `HitCounter.kt`
- [ ] Circular buffer of (timestamp, count) buckets, size = window
- [ ] Stale bucket (different timestamp) resets before incrementing
- [ ] Window semantics: (now - window, now] — half-open on the LEFT
- [ ] Compare with the Stage 0 rate limiter — same shape, different question

### 13.3 Consistent hashing (15 min)
`add(node)` / `remove(node)` / `getNode(key)` in `ConsistentHashing.kt`
- [ ] TreeMap ring + virtual nodes (replicasPerNode hashes per real node)
- [ ] Inverse map (node -> virtual hashes) so removal is O(replicas), not O(ring)
- [ ] Lookup: hash key, ceilingEntry, wrap to first — O(log N)
- [ ] Aloud: why distributed sharding cares (minimal remapping on node add/remove)

### 13.4 LFU cache (30 min)
`get(key)` / `put(key, value)` in `LfuCache.kt`
- [ ] HashMap<K, Node> + HashMap<Int, LinkedHashSet<K>> frequency buckets
- [ ] `minFreq` points at the eviction bucket; update it on every operation
- [ ] Re-put resets frequency to 1 (the test suite checks this behaviour)
- [ ] Aloud: how is this different from LRU's single recency list?

### 13.5 Randomized set (20 min)
`insert(v)` / `remove(v)` / `getRandom()` in `RandomizedSet.kt`
- [ ] ArrayList + HashMap<V, Int> for O(1) value-to-index lookup
- [ ] Delete via swap-with-last-then-pop
- [ ] `getRandom` samples uniformly from the list

### 13.6 Snapshot array (15 min)
`set(index, v)` / `snap()` / `get(index, snapId)` in `SnapshotArray.kt`
- [ ] Per-index list of (snapId, value)
- [ ] Binary search for floor snapId
- [ ] Compare with `dsa/VersionedKVStore.kt` — same "as of" shape, different structure choice

### 13.7 Versioned KV recall (15 min)
`put(docId, contents, timestamp)` / `get(docId, timestamp)` in `VersionedKvRecall.kt`
- [ ] Same API as `dsa/VersionedKVStore.kt` — cold recall without peeking
- [ ] TreeMap floorEntry for "as of" timestamp
- [ ] Out-of-order puts are fine because the tree sorts by key

## Done when
- [ ] Tests green (`./gradlew test --tests '*stages.stage13*'`); each exercise committed separately
- [ ] For each structure you can state the map direction and per-op cost unprompted
- [ ] You can contrast LFU buckets with LRU's single list, and explain the swap trick in RandomizedSet

## Commit points
After 13.1, after 13.3, after 13.5, after 13.7.
