# ANSWER KEY 2 — do not open until your review is done

Planted issues, severity-ranked, with analysis and fix direction.

---

## SEVERE

### S1. SQL injection throughout JdbcBackupStore
**File:** `JdbcBackupStore` — every method
All SQL is built by string concatenation with values that flow from the HTTP
layer: `clusterId`, `statusFilter`, `id`. `findByCluster("x' OR '1'='1", ...)`
dumps every backup record; `statusFilter = "'; DROP TABLE backups; --"` is
worse. This is THE textbook severe issue and it's in the data layer of a
service whose whole job is protecting data.
Note the layering angle: the controller does no sanitization either, and the
status filter is *user-supplied via query param* — the brief says
`?status=`. There is no layer at which input is safe.
**Fix direction:** `PreparedStatement` with bound parameters everywhere;
validate statusFilter against the known status set at the boundary.

### S2. JDBC connection (and statement/resultset) leaks on every exception path
**File:** `JdbcBackupStore` — every method
`conn.close()` / `stmt.close()` only run on the happy path. Any exception
(bad SQL, DB down, constraint violation) skips them → leaked connections.
Under failure load the pool exhausts and the service hangs — the classic
slow-motion outage. Every method has this bug; finding it ONCE and naming
the pattern is the depth move (don't enumerate all four).
**Fix direction:** try-with-resources (`try (Connection conn = ...)`) —
which also fixes the statement/resultset leaks in the same stroke.

### S3. Backup reported COMPLETE before it's verified restorable
**File:** `BackupService.refreshStatus`
The requirement: "COMPLETE means restorable — a false COMPLETE is worse than
none." But the code sets `status = COMPLETE` and persists it the moment the
snapshot API *says* complete, and only THEN kicks off verification —
fire-and-forget in a raw `new Thread`, with failures swallowed ("best-effort").
A snapshot that reports COMPLETE but is corrupt/unreadable is recorded as a
good backup. SREs will trust it until the day they need it.
Also the verification thread: unbounded thread creation per completion, no
pool, no failure handling, updates nothing — if verification fails, the
record STILL says COMPLETE.
**Fix direction:** status VERIFIED only after verification passes (or
COMPLETE_PENDING_VERIFICATION → COMPLETE), verification result persisted,
failures alert.

---

## MEDIUM

### M1. API key logged in plaintext at startup
**File:** `BackupConfig.logConfig`
`System.out.println("  snapshot api key: " + snapshotApiKey)` — the secret
goes to stdout, which in any real deployment lands in log aggregation,
retained and searchable by half the company. Secrets must never be logged;
also `System.out` instead of a logger is its own smell.
**Fix direction:** never log secrets; if you must show config, redact
(`***` or last-4).

### M2. SnapshotClient has no timeouts — a hung snapshot API hangs the service
**File:** `SnapshotClient.openConnection`
`HttpURLConnection` with no `setConnectTimeout` / `setReadTimeout`. The
brief says the API "must be treated as unreliable: calls may hang." A hung
GET holds the calling thread forever; the polling loop stalls, and if
callers are request threads, the pool exhausts — one sick dependency takes
down the service. This is the applied-resilience bug a Staff reviewer
catches first in client code.
**Fix direction:** explicit connect/read timeouts + circuit breaking;
also `cancelSnapshot` ignores the response code entirely.

### M3. Errors returned as HTTP 200
**File:** `BackupController` — every catch block
`catch (Exception e) { return json(200, "{\"error\":...}") }` — failures
look like successes to anything monitoring status codes: load balancer
health checks, alerting, client retries. The requirement says "appropriate
status codes." (The 404 for unknown backup shows they knew the pattern —
the blanket 200s are the bug.)
**Fix direction:** map exception types to 4xx/5xx; 500 for unexpected.

### M4. Naive JSON parsing breaks on any real payload
**File:** `SnapshotClient.readJsonField`
Substring search for `"field":"` — breaks on whitespace in the JSON
(`"status": "RUNNING"` with a space), on nested objects containing the same
field name, on escaped quotes. The snapshot API is third-party; its
formatting is not a contract.
**Fix direction:** a real JSON parser (Jackson/Gson); treat the substring
approach as unacceptable for external APIs.

---

## TRIVIAL (group as one line)

- Stringly-typed status everywhere ("RUNNING", "COMPLETE") — should be an
  enum; typos compile fine. (`Backup` even documents the set in a comment.)
- `java.util.Date` (mutable, legacy) in `Backup`.
- `verifySnapshotReadable` is a `Thread.sleep` TODO — left in a PR.
- Raw `new Thread` in refreshStatus (see S3) — also worth a trivial mention.
- `cancelSnapshot` ignores response code (see M2).
- `readJsonField` never closes the reader (minor leak, folds into S2 pattern).

---

## TRAPS — things that look wrong but are (arguably) right

### T1. `cancelBackup` sets CANCELLED even when not running
Looks like a state-machine bug (cancelling a COMPLETE backup?), but the
requirement only says cancel a backup "if it is still running" — and the
code only calls the API when RUNNING. Marking the record CANCELLED for an
already-finished backup is a defensible idempotent API choice. A strong
reviewer *asks* ("should cancel on a completed backup 409?") rather than
asserting.

### T2. `createBackup` persists REQUESTED before calling the snapshot API
Looks like it could leave orphan records if startSnapshot throws — and it
can — but persisting intent first is a defensible outbox-style choice (the
record exists for reconciliation). The bug would be NOT having a
reconciler; the ordering itself is legitimate. Ask before flagging.

---

## Scoring yourself

- **S1, S2, S3** lead the review. S1 should be instant; S2 named once as a
  pattern; S3 is the subtle one — it requires holding the requirement
  ("COMPLETE means restorable") against the code.
- **M1 (secret logging) and M2 (no timeouts)** are the real-world Staff
  signals — they're not algorithmic, they're operational.
- Naming **T1/T2 as questions** rather than bugs is, again, the strongest
  signal in the exercise.
