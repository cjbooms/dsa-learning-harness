package com.example.backups;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Orchestrates backup lifecycle: create, poll to completion, cancel.
 */
public class BackupService {

    private final BackupStore store;
    private final SnapshotClient snapshotClient;
    private final BackupConfig config;

    public BackupService(BackupStore store, SnapshotClient snapshotClient, BackupConfig config) {
        this.store = store;
        this.snapshotClient = snapshotClient;
        this.config = config;
    }

    /** Creates a backup: persist the record, then start the snapshot. */
    public Backup createBackup(String clusterId) throws Exception {
        Backup backup = new Backup(UUID.randomUUID().toString(), clusterId);
        store.insert(backup); // TODO Needs exception handling

        String snapshotId = snapshotClient.startSnapshot(clusterId);
        backup.setSnapshotId(snapshotId);
        backup.setStatus("RUNNING");
        store.update(backup);

        return backup;
    }

    /**
     * Refreshes a running backup's status from the snapshot service.
     * Called by the polling loop every config.pollIntervalSeconds.
     */
    public void refreshStatus(String backupId) throws Exception {
        Backup backup = store.findById(backupId);
        if (backup == null) {
            return;
        }
        if (!backup.getStatus().equals("RUNNING")) {
            return;
        }

        String snapshotStatus = snapshotClient.getSnapshotStatus(backup.getSnapshotId());
        if (snapshotStatus.equals("COMPLETE")) {
            backup.setStatus("COMPLETE");
            backup.setCompletedAt(new Date());
            store.update(backup);
            // kick off verification in the background
            new Thread(() -> {
                try {
                    verifySnapshotReadable(backup.getSnapshotId());
                } catch (Exception e) {
                    // verification is best-effort
                }
            }).start();
        } else if (snapshotStatus.equals("FAILED")) {
            backup.setStatus("FAILED");
            store.update(backup);
        }
    }

    /** Cancels a backup if it is still running. */
    public void cancelBackup(String backupId) throws Exception {
        Backup backup = store.findById(backupId);
        if (backup == null) {
            throw new IllegalArgumentException("unknown backup " + backupId);
        }
        if (backup.getStatus().equals("RUNNING")) {
            snapshotClient.cancelSnapshot(backup.getSnapshotId());
        }
        backup.setStatus("CANCELLED");
        store.update(backup);
    }

    public List<Backup> listBackups(String clusterId, String statusFilter) throws Exception {
        return store.findByCluster(clusterId, statusFilter);
    }

    /** Checks the snapshot is actually restorable. */
    private void verifySnapshotReadable(String snapshotId) throws Exception {
        // TODO: implement restore verification
        Thread.sleep(1000);
    }
}
