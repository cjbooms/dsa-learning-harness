# Code Review Practice 2 — Scenario Brief

## Scenario

A junior colleague has opened **PR #902: "Backup/restore API service"**.

SREs need to trigger and track backups of database clusters through an
internal HTTP API instead of filing tickets. This PR adds the service:
a REST layer, an orchestration service, a JDBC store, and a client for the
cloud provider's snapshot API.

## Requirements (from the ticket)

- `POST /clusters/{id}/backups` starts a backup and returns 201 with its id.
- Backups progress REQUESTED → RUNNING → COMPLETE (or FAILED/CANCELLED).
- A backup may only be reported COMPLETE when it is **restorable** — an
  entry that says COMPLETE but isn't restorable is worse than no entry.
- The snapshot API is third-party and **must be treated as unreliable**:
  calls may hang or fail. The service must not fall over when it does.
- API keys and DB credentials are secrets and must be handled as such.
- Errors must return appropriate HTTP status codes.

## Scope for review

- **In scope:** all seven files in
  `review-practice-2/src/main/java/com/example/backups/`.
- **Focus:** `BackupService`, `JdbcBackupStore`, `SnapshotClient`.
- **Out of scope:** the web framework (the `json()` stand-in), the actual
  restore-verification logic (explicitly a TODO), the polling loop driver.

## Your drill

1. Read this brief (2 min).
2. **10–15 min silent review**, `//` comments in the code as you go.
3. **20 min narrating aloud**, severe first: concrete failure, requirement
   it violates, one-line fix direction.
4. Only then open `answer-key.md`.

Depth over breadth. Verify before asserting — not everything that looks
wrong is wrong.
