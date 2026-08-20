package com.example.scheduler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Answers dependency questions: which jobs are runnable right now, and
 * whether the dependency graph is acyclic.
 */
public class DependencyResolver {

    /**
     * Jobs whose dependencies have all COMPLETED. Note: a job that already
     * ran (COMPLETED or FAILED) is not returned again.
     */
    public List<Job> findRunnable(List<Job> jobs) {
        // TODO - THis is Java, so we might have to null check on the list of jobs! Saem comment for all arguments
        Set<String> completed = new HashSet<>();
        for (Job job : jobs) {
            if (job.getStatus() == Job.Status.COMPLETED) {
                completed.add(job.getId());
            }
        }

        List<Job> runnable = new ArrayList<>();
        // TODO - Second Array interation, could be merged into first
        for (Job job : jobs) {
            if (job.getStatus() != Job.Status.PENDING) {
                continue; // TODO is this continue ncessary? WHat is the intent
            }
            if (completed.containsAll(job.getDependencies())) {
                runnable.add(job);
            }
        }
        return runnable;
    }

    /**
     * Cycle check via DFS from each job following dependency edges.
     */
    public boolean hasCycle(List<Job> jobs) {
        Set<String> visited = new HashSet<>();
        for (Job job : jobs) {
            if (detectCycle(job.getId(), jobs, visited)) {
                return true;
            }
        }
        // This also returns false for empty list
        return false;
    }

    private boolean detectCycle(String jobId, List<Job> jobs, Set<String> visited) {
        if (visited.contains(jobId)) {
            return true; // ok, this appears to be returning true if we have seen this job already
        }
        visited.add(jobId);
        Job job = findJob(jobId, jobs);
        if (job != null) {
            for (String dep : job.getDependencies()) {
                if (detectCycle(dep, jobs, visited)) { // Recurrsive call to check for cycles. Stack overflow warning?
                    return true;
                }
            }
        }
        return false;
    }

    private Job findJob(String id, List<Job> jobs) {
        for (Job job : jobs) {
            if (job.getId().equals(id)) {
                return job;
            }
        }
        return null;
    }
}
