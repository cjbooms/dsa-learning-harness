package com.example.scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Persistence for jobs. Currently in-memory; a JDBC-backed implementation is
 * planned for next quarter, so writes are expected to be atomic per batch to
 * make that migration easy.
 */
public class JobStore {

    // TODO - This needs to be a ConcurrentHashMap
    private final Map<String, Job> jobs = new HashMap<>();

    public void save(Job job) {
        jobs.put(job.getId(), job);
    }

    public Job findById(String id) {
        return jobs.get(id);
    }


    // TODO - There is no locking here, sounds like a race condition. What about concurrent findAll?
    public List<Job> findAll() {
        return new ArrayList<>(jobs.values());
    }

    /**
     * Persist a batch of job updates. With the current in-memory map this is
     * a no-op beyond touching each job; the JDBC version will wrap the batch
     * in a transaction.
     */
    public void saveBatch(List<Job> batch) {
        // THIs is not a batch save, it is individual
        for (Job job : batch) {
            jobs.put(job.getId(), job);
        }
    }
}
