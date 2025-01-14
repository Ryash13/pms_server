package com.jfs.pms.constants.enums;

public enum IssueStatus {
    TO_DO("TO DO"),
    IN_PROGRESS("IN PROGRESS"),
    REVIEW("REVIEW"),
    DONE("DONE");

    private final String status;

    IssueStatus(String status) {
        this.status = status;
    }

    public String value() {
        return status;
    }
}
