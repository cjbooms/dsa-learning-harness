package com.example.backups;

import java.util.List;

/**
 * Persistence for backup records.
 */
public interface BackupStore {

    void insert(Backup backup) throws Exception;

    void update(Backup backup) throws Exception;

    Backup findById(String id) throws Exception;

    /** Lists backups for a cluster, optionally filtered by status. */
    List<Backup> findByCluster(String clusterId, String statusFilter) throws Exception;
}
