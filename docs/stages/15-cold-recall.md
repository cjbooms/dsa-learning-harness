# Stage 15 — Cold Recall

**Time budget: 75 min** · Package: `stages/stage15`

## The meta-skill

Rebuild earlier material from a blank buffer, no notes. If you can't, that
area was recognized, not learned — schedule a re-do. Narrate the
structure-selection ritual first, exactly as in the interview.

## Exercises

### 15.1 Rate limiter (8 min)
`allow(requestId, nowMillis)` in `RateLimiterRecall.kt`
- [ ] Sliding-window log: evict expired from the front, reject when full
- [ ] O(1) amortized — say why (each request enters/leaves once)
- [ ] Aloud: deque + count vs token bucket — space, burst, precision

### 15.2 Interval merging (6 min)
`mergeIntervals` / `StreamingIntervalMerger` in `IntervalMergeRecall.kt`
- [ ] Batch: sort by start, single pass, merge overlapping/touching
- [ ] Streaming: TreeMap by start, floorEntry + forward absorb
- [ ] Aloud: why the TreeMap beats re-sorting per add

### 15.3 Tree serialization (6 min)
`serialize` / `deserialize` in `TreeSerializeRecall.kt`
- [ ] Pre-order with explicit null markers, shared cursor on decode
- [ ] Aloud: pre-order vs BFS trade-offs

### 15.4 Read-write lock (8 min)
`readLock/readUnlock/writeLock/writeUnlock` in `RwLockRecall.kt`
- [ ] ReentrantLock + condition(s); readers, activeWriter, waitingWriters
- [ ] Writer preference: block new readers while a writer waits — cost aloud
- [ ] `while` around `await()`, signalAll on state change

### 15.5 Weak-area reinforcement (15 min)
- [ ] Identify the weakest recall above (slowest, most notes-peeking)
- [ ] Re-do that exercise from a blank buffer until it's under time
- [ ] Mock-interview it: narrate aloud while implementing, no pauses

## Done when
- [ ] Tests green (`./gradlew test --tests '*stages.stage15*'`)
- [ ] Each recall completed inside its time budget with narration
- [ ] Weakest area re-done cold a second time

## Commit points
After each exercise.
