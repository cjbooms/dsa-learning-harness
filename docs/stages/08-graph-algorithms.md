# Stage 8 — Graph Algorithms Deep Dive

**Time budget: 185 min** ⭐ top priority · Package: `stages/stage8`

## The meta-skill

"X must come before Y — give me an order (or say impossible)" is topological
sort. The trap is scanning for the next schedulable node (O(V²)); the move is
tracking in-degrees and enqueuing a node the moment its count hits zero.
Structure ritual first, every time: name two candidate approaches, cost each,
defend the pick.

## Exercises

### 8.1 Topological sort — Kahn's BFS (25 min)
`scheduleTasksKahn(tasks, dependencies): List<String>` in `TaskScheduler.kt`
- [ ] Adjacency list + in-degree counts + queue of zero-in-degree nodes
- [ ] Never rescan: a task joins the frontier exactly when its in-degree hits 0
- [ ] Cycle detection: output shorter than task list means no valid schedule
- [ ] Mutation drill: tasks have priorities — what replaces the queue? (min-heap)

### 8.2 Topological sort — DFS post-order (20 min)
`scheduleTasksDfs(tasks, dependencies): List<String>` in `TaskSchedulerDfs.kt`
- [ ] WHITE/GRAY/BLACK coloring; emit on finish, reverse at the end
- [ ] Cycle = back edge to a GRAY node
- [ ] Aloud: when is DFS better than Kahn's? (no explicit in-degree tracking)

### 8.3 Cycle detection with the cycle itself (15 min)
`hasCycle(...)` / `findCycle(...)` in `CycleDetector.kt`
- [ ] DFS with recursion-stack (GRAY) tracking
- [ ] Return the cycle nodes, not just a boolean — parent-chain reconstruction
- [ ] MongoDB framing: deadlock detection, circular dependencies

### 8.4 Shortest path in unweighted graph (15 min)
`shortestPathGrid(...)` in `ShortestPath.kt`
- [ ] BFS from source; why BFS and not DFS for shortest path?
- [ ] Reconstruct the path via a predecessor map
- [ ] Grid encoding: (r, c) -> r * cols + c to use flat arrays

### 8.5 Union-Find deep dive (15 min)
`find` / `union` / `connected` / `componentCount` in `UnionFind.kt`
- [ ] Path compression + union by rank
- [ ] Aloud: when union-find beats DFS/BFS (dynamic connectivity, offline queries)

## Stage 8.5 — Practice (30 min, same package)

### 8.6 Course schedule II (15 min)
`findCourseOrder(numCourses, prerequisites)` in `CourseSchedule.kt`
- [ ] Return the ordering, not just feasibility
- [ ] Multiple valid orderings exist — any is fine; impossible -> empty

### 8.7 Alien dictionary (15 min)
`alienOrder(words)` in `AlienDictionary.kt`
- [ ] Build the char graph from adjacent-word first-difference
- [ ] Edge cases: invalid prefix order ("abc" before "ab"), cycles

### 8.8 Grid flood-fill + rotting oranges (40 min)
`countIslands(grid)` / `rottingOranges(grid)` in `GridTraversal.kt`
- [ ] Flood-fill DFS/BFS for islands; sink visited cells in place
- [ ] Multi-source BFS for oranges: seed the queue with ALL initially rotten cells
- [ ] Return minutes elapsed or -1 if any fresh orange is unreachable
- [ ] Aloud: why multi-source BFS, not single-source, for spreading-state problems?

### 8.9 Clone graph (25 min)
`cloneGraph(node)` in `CloneGraph.kt`
- [ ] HashMap<original Node, copied Node> is both memo and cycle guard
- [ ] DFS creates the copy before recursing into neighbors
- [ ] Undirected edges appear twice; the map collapses them into the same cloned node
- [ ] Aloud: what breaks if you use a boolean visited set instead of a map?

## Done when
- [ ] Tests green (`./gradlew test --tests '*stages.stage8*'`); each exercise committed separately
- [ ] You can do Kahn's cold in under 10 minutes, narrating the frontier invariant
- [ ] You can state when DFS-topo beats Kahn's and when union-find beats both
- [ ] You can explain why multi-source BFS fits rotting oranges but single-source BFS fits point-to-point shortest path

## Commit points
After 8.1, after 8.3, after 8.5, after 8.7, after 8.8, after 8.9.
