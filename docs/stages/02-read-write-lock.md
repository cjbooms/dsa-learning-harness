# Stage 2 — Read-Write Lock (CLASSIC exercise)

**Estimated time: 45 min** · Package: `stages/stage2`

## The question (as common)

"Implement a read-write lock using ReentrantLock and conditions."
## Exercises

### 2.1 Core implementation (25 min)
`SimpleReadWriteLock` in `ReadWriteLock.kt` — skeleton provided:
- [ ] `readLock()` / `readUnlock()`: multiple concurrent readers allowed
- [ ] `writeLock()` / `writeUnlock()`: exclusive — no readers, no other writers
- [ ] Track: active readers, active writer (boolean), waiting writers
- [ ] Which condition(s)? One or two? Defend the choice

### 2.2 Policy decision (10 min)
- [ ] Writer preference: when a writer waits, should new readers block?
  Implement it. Explain: what does writer-preference cost? (reader throughput)
  What does reader-preference cost? (writer starvation)

### 2.3 Stress tests (10 min)
- [ ] Make provided tests pass: readers overlap, writer is exclusive,
  no writer starvation under reader load
- [ ] Use latches, not sleeps (you know this drill by now)

## Extensions
- [ ] Lock downgrade: `writeUnlockToRead()` — release write while holding read.
  Why is UPGRADE (read→write) impossible without deadlock?

## Check your understanding
- [ ] Tests green, committed
- [ ] You can explain why `ConcurrentHashMap`-style striping doesn't apply here

## Suggested checkpoints
After 2.1, after 2.3.
