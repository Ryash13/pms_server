package com.jfs.pms.constants.enums;

public enum ProjectStatus {
    BACKLOG("Backlog"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    ON_HOLD("On Hold"),
    DELAYED("Delayed");

    private final String status;

    ProjectStatus(String status) {
        this.status = status;
    }

    public String value() {
        return status;
    }
}
