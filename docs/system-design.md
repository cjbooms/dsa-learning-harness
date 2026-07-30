# System Design Notes — MongoDB Staff+ Prep

Skeleton talking points for the design round. Practice narrating these aloud in
~10 min per design, then defending trade-offs under follow-up questions.

## 1. Sharded key-value store (DB-internals flavored)

- **Data model & API**: get/put by key; document vs pure KV; schema flexibility.
- **Sharding**: consistent hashing vs range-based; shard key choice (cardinality,
  write distribution, query isolation); resharding without downtime (chunk migration).
- **Replication**: replica sets, primary-election (Raft-style), write concerns
  (w:1 / w:majority / w:all), read concerns (local / majority / linearizable).
- **Consistency**: CAP positioning — CP with tunable durability; causal consistency;
  what happens during a partition (stale reads from secondaries?).
- **Storage engine**: B-tree vs LSM; write amplification; journaling/WAL for durability.
- **Hot keys & large docs**: mitigation (split chunks, application-level fan-out).
- **Indexes**: secondary indexes on sharded data — scatter-gather vs covered queries.

## 2. DB migration platform (relational -> NoSQL, reported 2026 round)

- **Phases**: schema mapping -> initial bulk backfill -> CDC (change data capture)
  tail -> dual-write or cutover -> verification -> rollback plan.
- **Backfill**: chunked parallel reads, resumable checkpoints, throttling to protect
  the source.
- **CDC**: oplog/binlog tailing, ordering guarantees, at-least-once delivery +
  idempotent appliers, handling schema drift during migration.
- **Verification**: row counts, checksums per chunk, sampled field-level diffs,
  lag monitoring (see `ReplicationLagAlerter` exercise).
- **Cutover**: read-your-writes during transition, dark-launch reads, traffic ramping.
- **Failure modes**: source outage mid-backfill, CDC gap, clock skew in lag metrics.

## 3. Cross-cutting themes interviewers probe

- **Concurrency**: where are the races in your design? What needs fencing tokens?
- **Scale numbers**: back-of-envelope QPS/storage/throughput — do the math out loud.
- **Operability**: metrics, alerting, deploy/rollback, multi-tenancy noisy neighbors.
- **Trade-off framing**: "I'd choose X because Y; the cost is Z; if requirement A
  changed I'd revisit."
