package com.jfs.pms.dto;

import java.time.LocalDateTime;

public class ApiErrorResponse {

    private String error;
    private String path;
    private LocalDateTime timestamp;

    public ApiErrorResponse(String error, String path, LocalDateTime timestamp) {
        this.error = error;
        this.path = path;
        this.timestamp = timestamp;
    }

   public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ApiErrorResponse{" +
                "error='" + error + '\'' +
                ", path='" + path + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
