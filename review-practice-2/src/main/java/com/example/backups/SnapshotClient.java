package com.example.backups;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Thin client for the cloud snapshot service's REST API.
 */
public class SnapshotClient {

    private final BackupConfig config;

    public SnapshotClient(BackupConfig config) {
        this.config = config;
    }

    /** Starts a snapshot. Returns the snapshot id assigned by the service. */
    public String startSnapshot(String clusterId) throws Exception {
        HttpURLConnection conn = openConnection("/v1/snapshots");
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        String body = "{\"clusterId\":\"" + clusterId + "\"}";
        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes("UTF-8"));
        }
        return readJsonField(conn, "snapshotId");
    }

    /** Current snapshot status: RUNNING, COMPLETE, or FAILED. */
    public String getSnapshotStatus(String snapshotId) throws Exception {
        HttpURLConnection conn = openConnection("/v1/snapshots/" + snapshotId);
        conn.setRequestMethod("GET");
        return readJsonField(conn, "status");
    }

    /** Cancels a running snapshot. */
    public void cancelSnapshot(String snapshotId) throws Exception {
        HttpURLConnection conn = openConnection("/v1/snapshots/" + snapshotId + "/cancel");
        conn.setRequestMethod("POST");
        conn.getResponseCode();
    }

    private HttpURLConnection openConnection(String path) throws Exception {
        URL url = new URL(config.getSnapshotApiUrl() + path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Authorization", "Bearer " + config.getSnapshotApiKey());
        conn.setRequestProperty("Content-Type", "application/json");
        return conn;
    }

    private String readJsonField(HttpURLConnection conn, String field) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }
        // naive json extraction: find "field":"value"
        String needle = "\"" + field + "\":\"";
        int start = body.indexOf(needle);
        if (start < 0) {
            throw new IllegalStateException("field " + field + " missing in response");
        }
        start += needle.length();
        int end = body.indexOf("\"", start);
        return body.substring(start, end);
    }
}
