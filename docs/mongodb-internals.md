# MongoDB Internals — Interview Primer

One paragraph per concept + "why it matters in an interview answer." Read
actively in Stage 4: annotate each section with a question you might be asked.

## Storage engine — WiredTiger
MongoDB's default engine stores documents in **B-trees** (one per collection,
one per index), with **document-level concurrency control** (not collection
locks — the old MMAPv1 story is dead; don't repeat it). It compresses data
(block compression, snappy by default) and maintains an in-memory **cache**
(default: ~50% of RAM minus 1GB) — cache pressure, not dataset size, is often
the real scaling limit.
**Why it matters:** "Why is my write throughput degrading?" → cache eviction +
checkpoint I/O. Knowing the engine exists below the query layer is table stakes.

## Journaling & crash recovery
WiredTiger writes a **write-ahead journal** (WAL): every write hits the journal
before it's acknowledged (with `j:true`), and checkpoints (~every 60s) flush
dirty pages to the data files. After a crash, recovery replays the journal from
the last checkpoint.
**Why it matters:** durability vocabulary. "What happens if the process dies?"
→ acknowledged-with-journal writes survive; anything else is a maybe.

## The oplog
A **capped collection** (`local.oplog.rs`) on each replica-set member recording
every operation that modifies data — logical, idempotent operations (not byte
diffs). Secondaries replicate by **tailing the primary's oplog** and applying
entries in order. Oplog size = your replication-lag tolerance window: if a
secondary falls further behind than the oplog retains, it must full-resync.
**Why it matters:** it's the answer to "how does replication actually work?"
and the foundation of change streams, CDC, and your migration exercises.

## Replica set elections
One **primary**, multiple **secondaries** (+ optional arbiter — votes, no data).
Heartbeats every 2s; if the primary is unreachable for ~10s
(`electionTimeoutMillis`), an eligible node calls an election. A node wins with
a **majority of votes** — this is why you deploy odd numbers and why a 2-node
set can't tolerate a failure. Terms (like Raft) prevent split-brain: a node
that can't see a majority steps DOWN.
**Why it matters:** "What happens when the primary dies?" is a guaranteed
question somewhere in your loop. Answer: detection (~10s), election (typically
a few seconds), rollback of un-replicated writes, driver auto-reconnects.

## Write concerns — what each GUARANTEES
- `w:1` — primary has it. Lost if primary dies before replicating. Fast.
- `w:majority` — on a majority of voting members. **Survives failover** (a new
  primary must have it, because elections require majority overlap). Slower.
- `j:true` — in the primary's journal (durability, orthogonal to replication).
**Why it matters:** this IS the "tunable consistency" knob in the multi-region
design prompt. State the guarantee, then the latency cost — in that order.

## Read concerns & read preferences
Read concern = how committed the data you read is: `local` (fastest, may read
un-replicated), `majority` (only majority-committed data — no dirty reads of
data that could roll back), `linearizable` (read-your-writes, primary only,
slowest), `snapshot` (multi-doc transactions).
Read preference = WHERE you read: `primary` (default), `nearest`,
`secondaryPreferred`... — the read-from-nearest knob for geo-distribution.
**Why it matters:** "read-from-nearest with tunable consistency" = read
preference `nearest` + read concern `majority` + write concern `w:majority`,
and the trade is staleness vs latency. Say exactly that.

## Sharding
Data partitioned by **shard key** into **chunks** (~128MB default) distributed
across shards; the **balancer** moves chunks to equalize load. `mongos` routes:
targeted queries (shard key present) vs **scatter-gather** (broadcast + merge).
Shard key choice is THE irreversible decision: high cardinality, even write
distribution, matching query patterns. Monotonic keys (timestamps, ObjectIds)
funnel all writes to one shard — the classic mistake.
**Why it matters:** horizontal scaling story + "why is one shard hot?"
debugging narrative. Chunk migration is also how the balancer drains a node
for maintenance — ties to zero-downtime ops.

## Change streams
The supported API for **tailing the oplog**: `collection.watch()` emits
ordered change events (insert/update/delete/replace) with resume tokens for
exactly-once-ish consumption (resume after failure = at-least-once + idempotent
consumer). Works on replica sets and sharded clusters (mongos merges per-shard
streams with a total order guarantee where possible).
**Why it matters:** the real CDC mechanism. Your ReplicationLagAlerter and
migration-platform designs are change-stream consumers in disguise — say so.

## Multi-document transactions
ACID transactions across documents/collections (since 4.0, sharded since 4.2)
via snapshot isolation + two-phase commit on sharded clusters. Costs: 60s
default runtime limit, oplog entry size limits (16MB), performance overhead —
the design guidance remains "model related data in ONE document" because
single-document writes are always atomic and free.
**Why it matters:** "when would you NOT use transactions?" is the interesting
answer — document modeling usually removes the need.

## Control plane vs data plane (Atlas framing)
Data plane = the mongod/mongos processes serving your data. Control plane =
Atlas's automation: provisioning, monitoring, backups, upgrades, failover
orchestration across AWS/GCP/Azure. The system design round lives at this
seam: agents on each host report metrics upward; the control plane makes
placement/healing decisions.
**Why it matters:** the verified observability prompt is literally "design the
control plane's eyes." Walk in knowing this vocabulary.
