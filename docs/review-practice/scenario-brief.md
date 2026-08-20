# Code Review Practice — Scenario Brief

## Scenario

A junior colleague has opened **PR #731: "Cluster maintenance job scheduler"**.

The team runs periodic maintenance on database clusters: rebuilding indexes,
taking backups, refreshing statistics. Some jobs depend on others (a backup
must finish before an index rebuild starts). This PR introduces a small
scheduler that dispatches jobs in dependency order to a worker pool, with
retries on failure.

## Requirements (from the ticket)

- A job runs only after **all** its dependencies have **COMPLETED**.
- Failed jobs are **retried with exponential backoff**, up to a cap.
- The scheduler must **reject dependency graphs containing cycles**.
- **Shutdown must be graceful**: in-flight jobs finish, nothing new starts.
- `JobStore` is in-memory today; a **JDBC-backed store is planned** next
  quarter, so the batch-write path should be transaction-friendly.

## Scope for review

- **In scope:** all five files in `review-practice/src/main/java/com/example/scheduler/`
  (`Job`, `JobStore`, `RetryPolicy`, `DependencyResolver`, `JobScheduler`).
- **Focus:** `JobScheduler` and `DependencyResolver` carry the most logic.
- **Out of scope:** the actual job execution (`runJob` is a stub), logging/
  metrics frameworks, build tooling.

## Your drill

1. Read this brief (2 min).
2. **10–15 min silent review.** Leave `//` comments in the code as you go —
   they're your memory during discussion.
3. **20 min narrating findings aloud**, severe first. For each: show the
   concrete failure (input or thread schedule), why it matters against the
   requirements, and a one-line fix direction.
4. Only then open `answer-key.md` and score yourself.

Remember: depth over breadth. A subtle cross-file bug explained well beats
five style nits. And verify before asserting — not everything that looks
wrong is wrong.
