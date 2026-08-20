package com.example.backups;

import java.util.List;

/**
 * HTTP layer for the backup API. Framework details omitted — methods map to:
 *   POST   /clusters/{clusterId}/backups          -> createBackup
 *   GET    /clusters/{clusterId}/backups?status=  -> listBackups
 *   GET    /backups/{backupId}                    -> getBackup
 *   POST   /backups/{backupId}/cancel             -> cancelBackup
 */
public class BackupController {

    private final BackupService service;

    public BackupController(BackupService service) {
        this.service = service;
    }

    /** POST /clusters/{clusterId}/backups */
    public String createBackup(String clusterId) {
        try {
            Backup backup = service.createBackup(clusterId);
            return json(201, "{\"id\":\"" + backup.getId() + "\",\"status\":\"" + backup.getStatus() + "\"}");
        } catch (Exception e) {
            return json(200, "{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    /** GET /clusters/{clusterId}/backups?status=<filter> */
    public String listBackups(String clusterId, String statusFilter) {
        try {
            List<Backup> backups = service.listBackups(clusterId, statusFilter);
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < backups.size(); i++) {
                Backup b = backups.get(i);
                sb.append("{\"id\":\"").append(b.getId())
                  .append("\",\"status\":\"").append(b.getStatus())
                  .append("\"}");
                if (i < backups.size() - 1) {
                    sb.append(",");
                }
            }
            sb.append("]");
            return json(200, sb.toString());
        } catch (Exception e) {
            return json(200, "{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    /** POST /backups/{backupId}/cancel */
    public String cancelBackup(String backupId) {
        try {
            service.cancelBackup(backupId);
            return json(200, "{\"status\":\"CANCELLED\"}");
        } catch (IllegalArgumentException e) {
            return json(404, "{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return json(200, "{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    private String json(int status, String body) {
        // framework stand-in: in the real service this builds the HTTP response
        return "HTTP " + status + " " + body;
    }
}
