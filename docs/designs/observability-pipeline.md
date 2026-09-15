# Design: Distributed Observability for a Database Platform

Senior Staff prompt: "low overhead, scalable, OpenTelemetry integration."
This is the control plane's eyes.

## Requirements
- Collect metrics/logs/traces from ~100k database hosts across AWS/GCP/Azure
- LOW OVERHEAD: the observer must not hurt the observed (CPU/heap on db hosts
  is the customer's money)
- OTel-compatible ingestion (customer-facing requirement, not internal choice)
- Queryable: fleet-wide dashboards, per-cluster drill-down, alerting

## Shape (skeleton order)

**API**: ingest (OTLP gRPC/HTTP), query (PromQL-ish), alert rules CRUD.

**Data model**: metrics = time series (name, labels, ts, value) → purpose-built
TSDB, NOT the transactional database itself for the hot path (say why:
write-amplification, compression, downsampling are TSDB-native — and "we don't
dogfood the transactional store for telemetry" is a fine opinion to defend).
Traces/logs → columnar object storage.

**Collection**: agent per host (DaemonSet-style), scraping local database
metrics + reading logs. LOW OVERHEAD answers: pull model with local
buffering, sampling for traces (head-based at agent, tail-based at
collector tier), batch + compress before egress, hard CPU/heap caps on the
agent itself.

**Pipeline**: agents → regional collectors (OTel Collector: receive, batch,
sample, route) → central TSDB + object storage. Regional tier absorbs
cross-region egress cost and gives a buffer when the central tier is down
(store-and-forward).

**Scaling**: partition by cluster-id hash at the collector tier; TSDB shards
by metric-name+labels. What breaks first at 10x: cardinality (label explosion)
— name it, it's the classic observability failure.

**Failure modes**: collector down → agents buffer locally (bounded, drop
oldest); region cut off → telemetry delayed, NOT lost (bounded); alerting
must degrade to heartbeat-watchdog ("no data IS an alert").

**Capacity math**: 100k hosts × 1k series × 15s interval ≈ 6.7M samples/s;
× 16 bytes ≈ 100MB/s ingest before compression. Say it, then halve it with
compression/downsampling.

## Narration notes
- The "why not write-acknowledgment durability here" question: telemetry is not the
  transactional path — loss tolerance is explicit and bounded; that's the
  difference between observability data and customer data.
