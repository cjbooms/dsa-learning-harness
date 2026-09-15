# Database Internals — Interview Primer

One paragraph per concept + "why it matters in an interview answer." Read
actively in Stage 4: annotate each section with a question you might be asked.

## Storage engine — B-trees and caching

A typical disk-oriented database stores rows/documents in **B-trees** (one per
table/collection, one per index), with **document/page-level concurrency control**
rather than global locks. Data is compressed on disk and buffered in an in-memory
cache — cache pressure, not dataset size, is often the real scaling limit.

**Why it matters:** "Why is my write throughput degrading?" → cache eviction +
checkpoint I/O. Knowing the engine exists below the query layer is table stakes.

## Journaling & crash recovery

The engine writes a **write-ahead journal** (WAL): every write hits the journal
before it's acknowledged (with `fsync`), and periodic checkpoints flush dirty
pages to the data files. After a crash, recovery replays the journal from the
last checkpoint.

**Why it matters:** durability vocabulary. "What happens if the process dies?"
→ acknowledged-with-journal writes survive; anything else is a maybe.

## The replication log

A **logical operation log** (oplog, binlog, WAL, etc.) on each replica records
every data-modifying operation. Secondaries replicate by **tailing the primary's
log** and applying entries in order. Log retention size = your replication-lag
tolerance window: if a secondary falls further behind than the log retains, it
must full-resync.

**Why it matters:** it's the answer to "how does replication actually work?"
and the foundation of change-data capture (CDC), event streaming, and migration
exercises.

## Replica set elections

One **primary**, multiple **secondaries** (+ optional witness/arbiter that votes
but holds no data). Heartbeats detect failure; if the primary is unreachable for
an election timeout, an eligible node calls an election. A node wins with a
**majority of votes** — this is why you deploy odd numbers and why a 2-node set
can't tolerate a failure. Terms (like Raft) prevent split-brain: a node that
can't see a majority steps DOWN.

**Why it matters:** "What happens when the primary dies?" is a guaranteed
question somewhere in your loop. Answer: detection, election, rollback of
un-replicated writes, driver/client auto-reconnects.

## Write concerns — what each GUARANTEES

- `acknowledged on primary` — primary has it. Lost if primary dies before
  replicating. Fast.
- `majority` — on a majority of voting members. **Survives failover** (a new
  primary must have it, because elections require majority overlap). Slower.
- `fsynced/journaled` — in the primary's journal (durability, orthogonal to
  replication).

**Why it matters:** this IS the "tunable consistency" knob in the multi-region
design prompt. State the guarantee, then the latency cost — in that order.

## Read concerns & read preferences

Read concern = how committed the data you read is: `uncommitted` (fastest, may
read un-replicated), `majority` (only majority-committed data — no dirty reads of
data that could roll back), `linearizable`/`serializable` (strongest, slowest),
`snapshot` (multi-version transactions).

Read preference = WHERE you read: primary (default), nearest,
secondary-preferred — the read-from-nearest knob for geo-distribution.

**Why it matters:** "read-from-nearest with tunable consistency" = read
preference `nearest` + read concern `majority` + write concern `majority`,
and the trade is staleness vs latency. Say exactly that.

## Sharding / partitioning

Data partitioned by a **shard/partition key** into chunks distributed across
nodes; a balancer moves chunks to equalize load. A router tier routes queries:
targeted queries (key present) vs **scatter-gather** (broadcast + merge).
Shard-key choice is THE hard-to-change decision: high cardinality, even write
distribution, matching query patterns. Monotonic keys (timestamps, sequence IDs)
funnel all writes to one shard — the classic mistake.

**Why it matters:** horizontal scaling story + "why is one shard hot?"
debugging narrative. Chunk migration is also how the balancer drains a node
for maintenance — ties to zero-downtime ops.

## Change streams / CDC

The supported API for **tailing the replication log**: `watch()` emits ordered
change events (insert/update/delete/replace) with resume tokens for
exactly-once-ish consumption (resume after failure = at-least-once + idempotent
consumer). Works on replica sets and sharded clusters (router tier merges
per-shard streams with a total order guarantee where possible).

**Why it matters:** the real CDC mechanism. Your ReplicationLagAlerter and
migration-platform designs are change-stream consumers in disguise — say so.

## Multi-document / distributed transactions

ACID transactions across records via snapshot isolation + two-phase commit on
sharded clusters. Costs: runtime limits, log entry size limits, performance
overhead — the design guidance remains "model related data together" because
single-record writes are always atomic and cheap.

**Why it matters:** "when would you NOT use transactions?" is the interesting
answer — data modeling usually removes the need.

## Control plane vs data plane

Data plane = the database processes serving your data. Control plane =
automation: provisioning, monitoring, backups, upgrades, failover orchestration
across cloud regions. The system design round lives at this seam: agents on each
host report metrics upward; the control plane makes placement/healing decisions.

**Why it matters:** the observability prompt is literally "design the control
plane's eyes." Walk in knowing this vocabulary.
