package com.jfs.pms.dto.auth;

public class ChangePasswordDto {

    private String oldPassword;
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
