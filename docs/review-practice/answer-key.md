# ANSWER KEY — do not open until your review is done

Planted issues, severity-ranked, with the analysis and fix direction.

---

## SEVERE

### S1. Cycle detection is broken — revisiting nodes across paths is not a cycle
**File:** `DependencyResolver.detectCycle`
The `visited` set is shared across the whole DFS and never cleaned up. A node
reached twice via *different* paths (a diamond: A→B, A→C, B→D, C→D) is
reported as a cycle — D is visited from B, then found "already visited" from C.
**The requirement says cyclic graphs must be rejected; this rejects valid
acyclic graphs AND, worse, the false positive means legitimate batches throw.**
The correct algorithm needs THREE states: unvisited / in-current-path /
done. A back-edge to an in-current-path node is a cycle; re-reaching a done
node is fine. (This is the classic topo-sort-adjacent bug — same family as
the interview question.)
**Fix direction:** three-color DFS (white/gray/black) or Kahn's in-degree
algorithm — "cycle exists iff output is short," which he now knows cold.

### S2. Race: dispatcher marks RUNNING before submitting; job visible as
### RUNNING while still queued — and can be re-dispatched after completion
**File:** `JobScheduler.dispatchLoop` + `execute`
The dispatcher sets `job.setStatus(RUNNING)` *then* submits to the pool.
Between those, a second dispatcher tick (500ms later, or a slow pool queue)
sees the job as RUNNING — fine. But `execute` runs on a worker thread and
mutates the SAME `Job` object the dispatcher thread reads (`findRunnable`
iterates `store.findAll()` with no synchronization). `Job.status` is a plain
field: no volatile, no synchronization. The dispatcher can read a stale
PENDING while a worker writes COMPLETED — a **visibility** race, plus a
lost-update shape if retry resets PENDING as the dispatcher reads.
**Fix direction:** synchronization or volatile on status transitions; better,
a proper state machine with atomic transitions. Note the *pattern*: shared
mutable state across dispatcher + workers with no happens-before edge.

### S3. Graceful shutdown is not graceful — in-flight jobs may be abandoned
**File:** `JobScheduler.shutdown`
`workers.shutdown()` stops accepting new tasks but the method returns
immediately — nobody calls `awaitTermination`. The requirement says
"in-flight jobs finish." As written, the JVM can exit (dispatcher is a
daemon thread, workers' pool threads are the only non-daemon...) — actually
the pool's non-daemon threads keep the JVM alive, but the CALLER gets no
guarantee: shutdown() returns while jobs still run, and any follow-up
teardown (closing the store, flushing logs) races with in-flight `execute`.
Also: `running = false` is volatile (good) but the dispatcher may be
mid-`sleepQuietly(500)` — fine — while `workers.shutdown()` interrupts
NOTHING (shutdown() doesn't interrupt; shutdownNow() would, and would
violate the requirement).
**Fix direction:** `workers.shutdown(); workers.awaitTermination(timeout)`
and document the timeout policy.

---

## MEDIUM

### M1. Retry backoff can overflow and retry-due check misuses wall clock
**File:** `RetryPolicy.nextDelayMillis` / `isDue`
`delay * 2` in a loop over `attempts` — unbounded doubling overflows long
after enough attempts (or a large base), flipping negative: `isDue` then
returns true immediately (negative delay <= elapsed). Also `isDue` compares
`System.currentTimeMillis()` deltas for *duration* — wall-clock moves
(NTP adjustments); `nanoTime` is the duration-correct source. Subtle,
Staff-level: overflow + time-source misuse.
**Fix direction:** cap the shift (`base << min(attempts, MAX_SHIFT)`) or
clamp; use nanoTime for elapsed-time logic.

### M2. Job exposes mutable internals; callers can break invariants
**File:** `Job.getDependencies` returns the internal list directly
A caller can `job.getDependencies().add(...)` and mutate the graph after
validation (after `hasCycle` passed!) — bypassing S1's check entirely.
Also `setStatus` is public, so any caller can mark a job COMPLETED and make
its dependents runnable. Encapsulation is the only thing enforcing the
business rules, and it's wide open.
**Fix direction:** unmodifiable view (`List.copyOf` on construction),
package-private or guarded status transitions.

### M3. `findRunnable` returns FAILED jobs' dependents as never-runnable —
### silent deadlock-by-design with no signal
**File:** `DependencyResolver.findRunnable`
Dependents of a FAILED job can never run (their deps never COMPLETE) — they
sit PENDING forever, and nothing surfaces this. No FAILED-propagation, no
alert, no log. The requirement doesn't specify the semantics, so this is a
**question-to-ask**, not an assertion: "should failure cascade, block with
a warning, or be configurable?" (A trap for reviewers who assert rather
than ask — see T1.)
**Fix direction:** define the policy; at minimum surface blocked jobs.

### M4. `saveBatch` is not atomic — partial batch leaves corrupt state
**File:** `JobStore.saveBatch` (and the JDBC comment)
The class comment SAYS the JDBC version will wrap the batch in a transaction,
but the in-memory version puts jobs one by one — if anything throws mid-batch
(or in the dispatcher's `store.saveBatch(toRun)` after some jobs already
mutated), the store holds a half-updated batch. The dispatcher's own
saveBatch happens AFTER mutating job statuses, so a failure there leaves
jobs marked RUNNING that were never submitted.
**Fix direction:** mutate-then-persist ordering review; make the batch
all-or-nothing even in memory (copy-on-write) to match the future contract.

---

## TRIVIAL (group as one line)

- `WORKER_THREADS = 4` magic number; should be config.
- `sleepQuietly` swallows InterruptedException without restoring the
  interrupt flag (`Thread.currentThread().interrupt()`).
- `dispatcherThread` field is written but never read (dead field).
- `Job.toString` format inconsistent spacing; `attempts` semantics
  (incremented on dispatch, not on actual run) worth a comment.
- `submit()` starts the dispatcher on EVERY call — second submit spawns a
  second dispatcher thread (arguably medium; mention with the trivial
  cluster or as a bonus find).

---

## TRAPS — things that look wrong but are (arguably) right

### T1. `findRunnable` excluding FAILED parents (see M3)
It IS a defensible reading of the requirements — "job runs only after all
dependencies COMPLETED" literally means failed deps block forever. A strong
reviewer *asks* ("is blocking intended?") rather than asserting it's a bug.
The planted lesson: verify against the business goal before flagging.

### T2. `running` as volatile boolean
Looks like it should need more synchronization, but for a single-writer
(shutdown) / single-reader (dispatcher) flag, volatile is sufficient and
idiomatic. Flagging it as a race would be a false positive.

### T3. `save()` after every execute
Looks like redundant writes (dispatcher also saveBatches), but it's the
worker persisting its own status transition — necessary given the design.
Mild inefficiency, not a bug.

---

## Scoring yourself

- **S1, S2, S3** are the three to lead with. Finding all three with concrete
  failure schedules = strong Staff review.
- **M1** (overflow + time source) is the subtle-bug differentiator — the
  round explicitly rewards "a smaller number of subtle issues."
- Naming the **T1 trap as a question** rather than a bug is the single most
  Staff signal in the whole exercise.
