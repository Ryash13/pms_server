package com.jfs.pms.constants;

public enum SprintStatus {
    BACKLOG("BACKLOG"),
    IN_PROGRESS("IN PROGRESS"),
    COMPLETE("COMPLETE");

    private final String status;

    SprintStatus(String status) {
        this.status = status;
    }

    public String value() {
        return status;
    }
}
