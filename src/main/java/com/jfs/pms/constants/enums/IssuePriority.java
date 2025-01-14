package com.jfs.pms.constants.enums;

public enum IssuePriority {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High"),
    CRITICAL("Critical");

    private final String priority;

    IssuePriority(String priority) {
        this.priority = priority;
    }

    public String value() {
        return priority;
    }
}
