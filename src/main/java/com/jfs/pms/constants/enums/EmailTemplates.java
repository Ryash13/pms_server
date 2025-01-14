package com.jfs.pms.constants.enums;

public enum EmailTemplates {
    RESET_PASSWORD("reset_password"),
    PASSWORD_CHANGE("password_change");

    private final String template;

    EmailTemplates(String template) {
        this.template = template;
    }

    public String value() {
        return template;
    }
}
