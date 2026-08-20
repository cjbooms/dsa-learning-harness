package com.example.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * PR #731 — "Cluster maintenance job scheduler"
 *
 * Runs cluster maintenance jobs (index rebuilds, backups, stats refreshes)
 * respecting dependency order, with retries on failure. A dispatcher thread
 * polls for runnable jobs and submits them to a worker pool.
 *
 * Business rules (from the ticket):
 *  - A job runs only after all its dependencies COMPLETED.
 *  - Failed jobs are retried with backoff, up to a cap.
 *  - The scheduler must reject job graphs containing dependency cycles.
 *  - Shutdown must be graceful: in-flight jobs finish, nothing new starts.
 */
public class JobScheduler {

    private static final int WORKER_THREADS = 4;

    private final JobStore store;
    private final DependencyResolver resolver;
    private final RetryPolicy retryPolicy;
    private final ExecutorService workers = Executors.newFixedThreadPool(WORKER_THREADS);

    private volatile boolean running = true;
    private Thread dispatcherThread;

    public JobScheduler(JobStore store, DependencyResolver resolver, RetryPolicy retryPolicy) {
        this.store = store;
        this.resolver = resolver;
        this.retryPolicy = retryPolicy;
    }

    /** Register and validate a batch of jobs, then start the dispatcher. */
    public void submit(List<Job> jobs) {
        if (resolver.hasCycle(jobs)) {
            throw new IllegalArgumentException("dependency cycle detected");
        }
        store.saveBatch(jobs);
        startDispatcher();
    }

    private void startDispatcher() {
        // TODO - THis looks suspect. Are we starting a dispatcher thread every time? Should be using a pool
        dispatcherThread = new Thread(this::dispatchLoop, "job-dispatcher");
        dispatcherThread.setDaemon(true);  / TODO what is the meaning of daemon threads again?
        dispatcherThread.start();
    }

    private void dispatchLoop() {
        while (running) {
            List<Job> runnable = resolver.findRunnable(store.findAll()); // TODO - Racey
            List<Job> toRun = new ArrayList<>();
            for (Job job : runnable) {
                if (job.getAttempts() == 0 || retryPolicy.isDue(job, System.currentTimeMillis())) {
                    job.setStatus(Job.Status.RUNNING);
                    job.incrementAttempts();
                    job.setLastRunAt(System.currentTimeMillis());
                    toRun.add(job);
                }
            }
            store.saveBatch(toRun); // TODO - We save the batch as running before we execute it, that sounds wrong
            for (Job job : toRun) {
                workers.submit(() -> execute(job));
            }
            sleepQuietly(500);
        }
    }

    private void execute(Job job) {
        try {
            runJob(job);
            job.setStatus(Job.Status.COMPLETED);
        } catch (Exception e) { // TODO Should we be more targetted in our exception catching here?
            if (retryPolicy.shouldRetry(job)) {
                job.setStatus(Job.Status.PENDING); // back to the queue for retry
            } else {
                job.setStatus(Job.Status.FAILED);
            }
        }
        store.save(job);
    }

    /** Placeholder for the real maintenance work (shell out, driver call, ...). */
    private void runJob(Job job) throws Exception {
        // simulated work
        Thread.sleep(100);
    }

    /** Graceful shutdown: stop dispatching, wait for in-flight jobs. */
    public void shutdown() {
        running = false;
        workers.shutdown(); // TODO - DO we need error handling here?
    }

    public List<Job> jobs() {
        return store.findAll();
    }

    private static void sleepQuietly(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
            // TODO THis is a swallowed exception that will prevent graceful shutdown
        }
    }
}
