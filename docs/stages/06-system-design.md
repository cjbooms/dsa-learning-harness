# Stage 6 — System Design (light, MongoDB-informed)

**Time budget: 45 min** (flex: compressible to 30 if earlier stages overrun)

## Exercises

### 6.1 Answer skeleton (10 min)
Read `docs/design-skeleton.md`. It gives the fixed narration order:
requirements → API → data model → scaling → failure modes → capacity math.
This is your whiteboard spine — the fast-paced format demands structure.

### 6.2 Observability pipeline walkthrough (20 min) — VERIFIED Senior Staff prompt
"Design a distributed observability system for a database platform: low
overhead, scalable, OpenTelemetry integration."
- [ ] Read `docs/designs/observability-pipeline.md`
- [ ] Then cover it and narrate the design yourself in 10 minutes, skeleton order
- [ ] Use Stage 4 vocabulary: low-overhead agents ON the mongod hosts,
  oplog/metrics as sources, write concerns don't apply — why? (it's not the
  transactional path)

### 6.3 Multi-region read-from-nearest (15 min)
- [ ] Read `docs/designs/multi-region-reads.md`
- [ ] Narrate: tunable consistency = WHICH knobs? (read preference, read
  concern, write concern — Stage 4 pays off here)
- [ ] Applied networking (your strength): L4 vs L7 LB, DNS geo-routing,
  connection-pool sizing for cross-region links

## Done when
- [ ] You can deliver either design in 10 minutes, skeleton order, no notes
- [ ] You can answer "what breaks when a region is cut off?" for both

## Commit points
None — reading + narration stage. (Notes optional.)
