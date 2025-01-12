package com.jfs.pms.service.user;

import com.jfs.pms.domain.User;
import com.jfs.pms.dto.auth.ChangePasswordDto;
import jakarta.mail.MessagingException;

public interface IUserService {

    User addUser(User user);
    User updateUser(User user);
    boolean deleteProfile(String publicId);
    void changePassword(String publicId, ChangePasswordDto changePassword) throws MessagingException;
}
