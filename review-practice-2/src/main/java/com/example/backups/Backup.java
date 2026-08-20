package com.example.backups;

import java.util.Date;

/**
 * A backup of one database cluster, tracked from request to completion.
 */
public class Backup {

    private final String id;
    private final String clusterId;
    private String status;        // REQUESTED, RUNNING, COMPLETE, FAILED, CANCELLED
    private String snapshotId;    // set once the snapshot service accepts the job
    private Date requestedAt;
    private Date completedAt;

    public Backup(String id, String clusterId) {
        this.id = id;
        this.clusterId = clusterId;
        this.status = "REQUESTED";
        this.requestedAt = new Date();
    }

    public String getId() { return id; }
    public String getClusterId() { return clusterId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getSnapshotId() { return snapshotId; }
    public void setSnapshotId(String snapshotId) { this.snapshotId = snapshotId; }

    public Date getRequestedAt() { return requestedAt; }

    public Date getCompletedAt() { return completedAt; }
    public void setCompletedAt(Date completedAt) { this.completedAt = completedAt; }
}
