# Stage 3 — DSA Drill Set B: Iterators, Parsers, Indexes

**Estimated time: 90 min** · Package: `stages/stage3`

## Exercises

### 3.1 Union of 2 sorted iterators (15 min) — CLASSIC shape 
`unionSorted(a: Iterator<Int>, b: Iterator<Int>): Iterator<Int>`
- Sorted inputs, sorted output, no duplicates, LAZY (don't materialize)
- The lazy part is the point: `hasNext()`/`next()` must do the work

### 3.2 Merge K sorted iterators (15 min) — the common follow-up
`mergeKSorted(iterators: List<Iterator<Int>>): Iterator<Int>`
- Structure ritual: what holds the "current head of each iterator"?
- O(log k) per element — Explain why

### 3.3 JSON parser (35 min) — advanced exercise 
`parse(json: String): JsonValue` — sealed class skeleton provided
- Tokenizer first (or single-pass with an index — your call, explain it)
- Recursive descent: object, array, string, number, true/false/null
- Errors: what do you do on malformed input? (decide BEFORE coding)
- This one is long because it's the advanced exercise. Take the time.

### 3.4 Inverted index (25 min) — Common 
`insert(docId: String, text: String)` / `search(term: String): Set<String>`
- Then the common follow-ups: AND query (`searchAll("a", "b")`), delete(docId)
- Structure ritual: what maps to what?

## Extensions
- [ ] Intersection iterator (common "intersection of two sets" exercise)
- [ ] Regex-lite matcher: `.` matches any, `*` matches zero+ of preceding

## Check your understanding
- [ ] Tests green, each committed separately
- [ ] For 3.1/3.2: explain why laziness matters (infinite/large streams, memory)

## Suggested checkpoints
After each exercise.
