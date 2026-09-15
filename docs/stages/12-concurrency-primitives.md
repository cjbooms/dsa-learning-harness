# Stage 12 — Concurrency Primitives

**Estimated time: 45 min** · Package: `stages/stage12`

## The meta-skill

Three primitives cover the concurrency round: lock + conditions (arbitrary
predicates), semaphore (counting resource), atomics/CAS (single-word state).
The exercise move is naming WHY one and not the others — a lock caps to one
holder, a semaphore to N, CAS blocks nobody. Say the while-loop-around-await
rule unprompted: spurious wakeup + re-check is the only safe form.

## Exercises

### 12.1 Producer-consumer bounded queue (20 min)
`put(item)` / `take()` in `BoundedBlockingQueue.kt`
- [ ] One ReentrantLock, TWO conditions (notFull / notEmpty) — why two?
- [ ] `while` around `await()`, never `if` — Explain why
- [ ] `signal()` not `signalAll()` — homogeneous waiters, one slot per change
- [ ] Mutation drills: offer(item, timeoutMs); fair lock; synchronized variant

### 12.2 Multithreaded web crawler (15 min)
`crawl(startUrl)` in `WebCrawler.kt`
- [ ] Fixed worker pool + shared queue + visited set
- [ ] Mark a URL visited when you DEQUEUE it; enqueue links freely and skip duplicates on dequeue
- [ ] Termination: queue empty AND no in-flight fetches
- [ ] Explain: this is BFS (Stage 8) plus the shared-state rules from concurrency

### 12.3 Lock-free counter (10 min)
`incrementAndGet(delta)` / `get()` / `getAndReset()` in `LockFreeCounter.kt`
- [ ] AtomicLong + CAS retry loop (updateAndGet)
- [ ] Explain: CAS vs lock under contention; when lock-free starves
- [ ] Follow-up: striped counters (LongAdder) for very hot paths

## Check your understanding
- [ ] Tests green (`./gradlew test --tests '*stages.stage12*'`); each exercise committed separately
- [ ] You can narrate the two-conditions and while-loop justifications from memory

## Suggested checkpoints
After 12.1, after 12.3.
