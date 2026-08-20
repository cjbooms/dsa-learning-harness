package com.example.scheduler;

import java.util.List;

/**
 * A unit of cluster maintenance work (index rebuild, backup, stats refresh...).
 * Jobs may declare dependencies: a job runs only after all its dependencies
 * have completed successfully.
 */
public class Job {

    public enum Status { PENDING, RUNNING, COMPLETED, FAILED }

    private final String id;
    private final List<String> dependencies;
    // TODO: Check if this is a shared instance that needs Atomics
    private Status status;
    private int attempts;
    private long lastRunAt;

    public Job(String id, List<String> dependencies) {
        this.id = id;
        this.dependencies = dependencies;
        this.status = Status.PENDING;
        this.attempts = 0;
    }

    public String getId() { return id; }

    public List<String> getDependencies() { return dependencies; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public int getAttempts() { return attempts; }
    public void incrementAttempts() { attempts++; }

    public long getLastRunAt() { return lastRunAt; }
    public void setLastRunAt(long lastRunAt) { this.lastRunAt = lastRunAt; }

    @Override
    public String toString() {
        return "Job{" + id + "," + status + ",attempts=" + attempts + "}";
    }
}
