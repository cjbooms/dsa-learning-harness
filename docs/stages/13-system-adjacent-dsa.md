# Stage 13 — System-Adjacent DSA

**Time budget: 60 min** · Package: `stages/stage13`

## The meta-skill

These are the "implement a data structure" questions that map directly to
MongoDB internals: query caches, metrics windows, shard routing. The structure
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
- [ ] Aloud: why MongoDB sharding cares (minimal remapping on node add/remove)

## Done when
- [ ] Tests green (`./gradlew test --tests '*stages.stage13*'`); each exercise committed separately
- [ ] For each structure you can state the map direction and per-op cost unprompted

## Commit points
After 13.1, after 13.3.
