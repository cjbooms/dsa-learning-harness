package com.example.backups;

/**
 * Service configuration, loaded from environment at startup.
 */
public class BackupConfig {

    private final String snapshotApiUrl;
    private final String snapshotApiKey;
    private final int maxConcurrentBackups;
    private final int snapshotPollIntervalSeconds;

    public BackupConfig() {
        this.snapshotApiUrl = requiredEnv("SNAPSHOT_API_URL");
        this.snapshotApiKey = requiredEnv("SNAPSHOT_API_KEY");
        this.maxConcurrentBackups = Integer.parseInt(envOr("MAX_CONCURRENT_BACKUPS", "4"));
        this.snapshotPollIntervalSeconds = Integer.parseInt(envOr("SNAPSHOT_POLL_INTERVAL_SECONDS", "30"));
    }

    public void logConfig() {
        System.out.println("backup-service config:");
        System.out.println("  snapshot api url: " + snapshotApiUrl);
        System.out.println("  snapshot api key: " + snapshotApiKey);
        System.out.println("  max concurrent backups: " + maxConcurrentBackups);
        System.out.println("  poll interval seconds: " + snapshotPollIntervalSeconds);
    }

    private static String requiredEnv(String name) {
        String value = System.getenv(name);
        if (value == null) {
            throw new IllegalStateException("missing required env var " + name);
        }
        return value;
    }

    private static String envOr(String name, String fallback) {
        String value = System.getenv(name);
        return value != null ? value : fallback;
    }

    public String getSnapshotApiUrl() { return snapshotApiUrl; }
    public String getSnapshotApiKey() { return snapshotApiKey; }
    public int getMaxConcurrentBackups() { return maxConcurrentBackups; }
    public int getSnapshotPollIntervalSeconds() { return snapshotPollIntervalSeconds; }
}
