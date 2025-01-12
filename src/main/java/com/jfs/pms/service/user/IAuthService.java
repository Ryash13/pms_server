package com.jfs.pms.service.user;

import com.jfs.pms.domain.User;
import com.jfs.pms.dto.auth.AuthRequest;
import jakarta.mail.MessagingException;

import java.util.Map;

public interface IAuthService {

    User register(User user);
    Map<String, Object> login(AuthRequest request);
    void resetPassword(String email) throws MessagingException;
}
