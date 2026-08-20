package com.example.backups;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC-backed BackupStore against the service's Postgres database.
 */
public class JdbcBackupStore implements BackupStore {

    private final String jdbcUrl;
    private final String jdbcUser;
    private final String jdbcPassword;

    public JdbcBackupStore(String jdbcUrl, String jdbcUser, String jdbcPassword) {
        this.jdbcUrl = jdbcUrl;
        this.jdbcUser = jdbcUser;
        this.jdbcPassword = jdbcPassword;
    }

    @Override
    public void insert(Backup backup) throws Exception {
        Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(
            "INSERT INTO backups (id, cluster_id, status, snapshot_id, requested_at) VALUES ('"
                + backup.getId() + "', '" + backup.getClusterId() + "', '" + backup.getStatus()
                + "', " + (backup.getSnapshotId() == null ? "NULL" : "'" + backup.getSnapshotId() + "'")
                + ", NOW())"
        );
        stmt.close();
        conn.close();
    }

    @Override
    public void update(Backup backup) throws Exception {
        Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(
            "UPDATE backups SET status = '" + backup.getStatus() + "', snapshot_id = "
                + (backup.getSnapshotId() == null ? "NULL" : "'" + backup.getSnapshotId() + "'")
                + (backup.getCompletedAt() != null ? ", completed_at = NOW()" : "")
                + " WHERE id = '" + backup.getId() + "'"
        );
        stmt.close();
        conn.close();
    }

    @Override
    public Backup findById(String id) throws Exception {
        Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM backups WHERE id = '" + id + "'");
        Backup backup = null;
        if (rs.next()) {
            backup = new Backup(rs.getString("id"), rs.getString("cluster_id"));
            backup.setStatus(rs.getString("status"));
            backup.setSnapshotId(rs.getString("snapshot_id"));
        }
        rs.close();
        stmt.close();
        conn.close();
        return backup;
    }

    @Override
    public List<Backup> findByCluster(String clusterId, String statusFilter) throws Exception {
        Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
        Statement stmt = conn.createStatement();
        String sql = "SELECT * FROM backups WHERE cluster_id = '" + clusterId + "'";
        if (statusFilter != null && !statusFilter.isEmpty()) {
            sql += " AND status = '" + statusFilter + "'";
        }
        sql += " ORDER BY requested_at DESC";
        ResultSet rs = stmt.executeQuery(sql);
        List<Backup> backups = new ArrayList<>();
        while (rs.next()) {
            Backup backup = new Backup(rs.getString("id"), rs.getString("cluster_id"));
            backup.setStatus(rs.getString("status"));
            backup.setSnapshotId(rs.getString("snapshot_id"));
            backups.add(backup);
        }
        rs.close();
        stmt.close();
        conn.close();
        return backups;
    }
}
