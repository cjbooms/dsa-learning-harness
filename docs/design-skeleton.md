# System Design Answer Skeleton

Fixed narration order for the whiteboard. The format is fast-paced — structure
is what keeps you coherent under time pressure.

## 1. Requirements (3–4 min)
- Functional: "the system must do X, Y, Z" — enumerate, number them
- Non-functional: scale (QPS? data size? regions?), consistency, latency, durability
- ASK before assuming: "Do we need multi-region from day one?" Negotiating
  requirements is scored — the prompt says so explicitly.

## 2. API / entry points (2 min)
- The 2–4 key operations, as signatures or REST resources. This forces
  concreteness before boxes-and-arrows.

## 3. Data model (3 min)
- Core entities + how they're stored/partitioned. Use the storage story:
  tables/collections, partition/shard keys, indexes — Stage 4 vocabulary.

## 4. Scaling story (4 min)
- The horizontal path: what partitions, what replicates, what balances.
- Bottleneck order: state what breaks FIRST as load grows 10x, then 100x.

## 5. Failure modes (3 min)
- "What breaks when X dies?" for the 3 most important components.
- Partition behavior: what does the system do when split? (consistency knob)

## 6. Capacity math (2 min)
- Back-of-envelope, out loud: QPS × payload × retention = storage;
  per-node throughput → node count. Numbers show judgment, not precision.

## Timing discipline
~18 min of narration leaves room for the interviewer to drill. If they pull
you deep into one area, follow — depth over coverage, same as code review.
