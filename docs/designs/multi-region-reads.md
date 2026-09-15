# Design: Multi-Region Read-From-Nearest with Tunable Consistency

Prompt: serve reads from the closest region, let customers tune the
consistency/latency trade.

## Requirements
- Data replicated across N regions (say 3: us-east, eu-west, ap-southeast)
- Reads served from nearest region; writes go to the primary region
- Tunable consistency: customer picks staleness vs latency per workload
- Region loss must not lose acknowledged writes or halt reads

## Shape

**The consistency knobs** (Stage 4 pays off — use the vocabulary):
- Write acknowledgment `majority` → acknowledged writes survive any region loss
  (majority overlap guarantees the new primary has them)
- Read target `nearest` → read from the closest node, possibly a secondary
- Read level `majority` → never read data that could roll back
- The TUNE: `nearest + majority` = fast and safe-but-stale; `primary +
  linearizable` = read-your-writes, cross-region latency. Present the spectrum.

**Replication**: one replicated group spanning regions (electable nodes in 2–3
regions), or per-region shard replicas. Election: majority must live in ≥2
regions so single-region loss still elects. Say the number: cross-region RTT
(~60–150ms) bounds election + majority-ack latency.

**Routing** (your applied-networking strength): DNS geo-steering or anycast to
the nearest router/L7 proxy; L7 LB for connection draining during failover;
connection pools sized per-region-link with circuit breaking so a sick region
doesn't exhaust client pools.

**Failure modes**: region cut off → reads continue locally (stale), writes
reroute to remaining majority; primary region lost → election in survivor
region (~10s detection + seconds), un-replicated primary-ack writes roll back —
reference the Stage 4 conceptual failover walkthrough.

**Capacity math**: write throughput bounded by cross-region replication
(RTT × majority ack); read throughput scales with regions. State both.

## Narration notes
- "Tunable" is the word to unpack: it maps to the three knobs above, per
  operation, not a cluster-wide flag.
- Split-brain: impossible by construction (majority elections) — say why in
  one sentence; it's a guaranteed follow-up.
