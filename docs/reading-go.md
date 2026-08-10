# Reading Go — One-Pager (in case the review sample is Go)

You won't be asked to WRITE Go. You may need to READ it. Mapping:

| Go | What it means | JVM analog |
|---|---|---|
| `go f()` | spawn goroutine (lightweight thread) | `thread { f() }` / coroutine |
| `ch <- x` / `x := <-ch` | channel send / receive (blocks if unbuffered & no partner) | `BlockingQueue.put/take` |
| `make(chan T, n)` | buffered channel, capacity n | `ArrayBlockingQueue(n)` |
| `defer f()` | run f() when the function returns (LIFO) | `finally` / `.use {}` |
| `if err != nil` | explicit error return — CHECK whether the code handles it | exceptions, but manual |
| `sync.Mutex` / `.Lock()` | plain mutex | `ReentrantLock` |
| `sync.WaitGroup` | wait for N goroutines | `CountDownLatch` |
| `select` | wait on multiple channel ops | no direct analog |

## Planted-bug patterns to recognize in Go

1. **Unbuffered channel deadlock**: sender blocks until a receiver is ready —
   if the receiver already exited (or errors out first), the sender parks
   forever. Look for sends with no guaranteed receiver.
2. **Unchecked `err`**: `result, _ := call()` — the blank identifier discards
   the error. In review code, that's almost always planted.
3. **Shared map without mutex**: Go maps panic on concurrent write. Look for
   a struct field map touched by multiple goroutines.
4. **`defer` in a loop**: defers run at FUNCTION return, not loop iteration —
   resources pile up until the function ends.
5. **Goroutine leak**: goroutine blocked on a channel nobody reads — it never
   dies. "How does this goroutine ever exit?" is a strong review question.
