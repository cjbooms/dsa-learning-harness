# Stage 4 — Database Internals Fluency

**Estimated time: 75 min** · Package: `stages/stage4` · Evidence: study how modern
DBs work in depth.

## Why this stage

At a database company or on a storage team, generic distributed-systems talk
doesn't differentiate you. Database vocabulary and hands-on reasoning do.
Output of this stage: `docs/lab-notes.md` with YOUR observations — those become
talking points in every other round.

## Part A — Active reading (25 min)

Read `docs/database-internals.md`. Don't skim — for each section:
- [ ] Write one question an reviewers might ask about it
- [ ] Write the 1-sentence answer you'd give
(Annotate directly in a scratch file or the margins)

## Part B — Conceptual lab / flash-cards (45 min)

No running code this stage. Pick a database you know (PostgreSQL, MySQL,
CockroachDB, DynamoDB, etc.) and map each section in `docs/database-internals.md`
to that system's terminology.

### Lab 1 — write concerns (15 min)
- [ ] For your chosen DB, what does "primary acknowledged" vs "majority acknowledged"
      look like?
- [ ] What latency difference do you expect?
- [ ] Explain: what does majority actually guarantee? (survives failover)

### Lab 2 — change streams / CDC (15 min)
- [ ] How do you tail the replication log in your chosen DB?
- [ ] Recognize: this IS the CDC mechanism behind your migration exercises
- [ ] Record one sample change event format in lab-notes.md

### Lab 3 — failover (15 min)
- [ ] Walk through primary death end-to-end: detection, election, rollback of
      un-replicated writes, client reconnect
- [ ] Did any acknowledged writes disappear? (rollback of un-replicated writes)
- [ ] THIS is the failover-orchestrator design question, reasoned firsthand

## Extensions
- [ ] Sketch an indexed vs unindexed query plan: full scan vs index seek

## Check your understanding
- [ ] lab-notes.md has: your chosen DB's terminology map, majority-vs-primary
      latency story, one change-event sketch, one failover walkthrough
- [ ] You can explain replication log vs WAL vs change stream without notes

## Suggested checkpoints
After lab-notes.md is complete.
