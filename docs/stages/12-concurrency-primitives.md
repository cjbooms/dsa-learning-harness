# Stage 12 — Concurrency Primitives

**Time budget: 45 min** · Package: `stages/stage12`

## The meta-skill

Three primitives cover the concurrency round: lock + conditions (arbitrary
predicates), semaphore (counting resource), atomics/CAS (single-word state).
The interview move is naming WHY one and not the others — a lock caps to one
holder, a semaphore to N, CAS blocks nobody. Say the while-loop-around-await
rule unprompted: spurious wakeup + re-check is the only safe form.

## Exercises

### 12.1 Producer-consumer bounded queue (20 min)
`put(item)` / `take()` in `BoundedBlockingQueue.kt`
- [ ] One ReentrantLock, TWO conditions (notFull / notEmpty) — why two?
- [ ] `while` around `await()`, never `if` — say why aloud
- [ ] `signal()` not `signalAll()` — homogeneous waiters, one slot per change
- [ ] Mutation drills: offer(item, timeoutMs); fair lock; synchronized variant

### 12.2 Semaphore connection pool (15 min)
`lease()` / `release(conn)` in `ConnectionPool.kt`
- [ ] Semaphore(maxConnections) bounds concurrent holders
- [ ] Idle deque for reuse — acquire permit, then poll idle or open new
- [ ] Release order: return to idle, then release permit — why this order?
- [ ] Aloud: semaphore vs lock (N vs 1); semaphore vs bounded queue (active vs waiting)

### 12.3 Lock-free counter (10 min)
`incrementAndGet(delta)` / `get()` / `getAndReset()` in `LockFreeCounter.kt`
- [ ] AtomicLong + CAS retry loop (updateAndGet)
- [ ] Aloud: CAS vs lock under contention; when lock-free starves
- [ ] Follow-up: striped counters (LongAdder) for very hot paths

## Done when
- [ ] Tests green (`./gradlew test --tests '*stages.stage12*'`); each exercise committed separately
- [ ] You can narrate the two-conditions and while-loop justifications from memory

## Commit points
After 12.1, after 12.3.
