package com.example.scheduler;

/**
 * Decides whether a failed job should be retried, with exponential backoff.
 */
public class RetryPolicy {

    private final int maxAttempts;
    private final long baseDelayMillis;

    public RetryPolicy(int maxAttempts, long baseDelayMillis) {
        this.maxAttempts = maxAttempts;
        this.baseDelayMillis = baseDelayMillis;
    }

    // TODO - Should be less than or equal to . Off by one error
    public boolean shouldRetry(Job job) {
        return job.getAttempts() < maxAttempts;
    }

    public long nextDelayMillis(Job job) {
        // exponential backoff: base, 2*base, 4*base, ...
        long delay = baseDelayMillis;
        for (int i = 0; i < job.getAttempts(); i++) {
            delay = delay * 2;  // TODO - We need jitter here, to avoid thundering herds
        }
        return delay;
    }

    public boolean isDue(Job job, long nowMillis) {
        return nowMillis - job.getLastRunAt() >= nextDelayMillis(job);
    }
}
