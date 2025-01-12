package com.jfs.pms.service.user;

import com.jfs.pms.domain.User;
import com.jfs.pms.dto.auth.ChangePasswordDto;
import com.jfs.pms.exception.AlreadyExistsException;
import com.jfs.pms.exception.NotFoundException;
import com.jfs.pms.repository.UserRepository;
import com.jfs.pms.utility.EmailService;
import com.jfs.pms.utility.PmsUtils;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.jfs.pms.constants.Constants.PASSWORD_CHANGE_SUBJECT;
import static com.jfs.pms.constants.EmailTemplates.PASSWORD_CHANGE;
import static org.springframework.http.HttpStatus.*;

@Service
public class UserService implements IUserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    public User addUser(User user) {
        try {
            boolean emailAlreadyRegistered = userRepository.checkIfMailAlreadyExists(user.getEmail()) != 0;
            if(emailAlreadyRegistered) {
                throw new AlreadyExistsException("Email already registered, Try another email", CONFLICT);
            }
            String username = PmsUtils.generateUsername(user.getFirstName(), user.getLastName());
            String profileImage = PmsUtils.generateProfileImage(user.getEmail());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setUsername(username);
            user.setProfileImageUrl(profileImage);
            return userRepository.save(user);
        } catch (Exception exception) {
            log.error("Error occurred while adding user: {}", exception.getMessage());
            throw exception;
        }
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public boolean deleteProfile(String publicId) {
        try {
            var uuid = UUID.fromString(publicId);
            var user = userRepository.findByPublicId(uuid)
                    .orElseThrow(() -> new NotFoundException("User not found with Public ID :: " + publicId, NOT_FOUND));
            userRepository.delete(user);
            return true;
        } catch (NotFoundException exception) {
            log.error("Error occurred while fetching user with Public ID :: {} :: {}", publicId, exception.getMessage());
            throw exception;
        } catch (Exception exception) {
            log.error("Error occurred while deleting user: {}", exception.getMessage());
            throw exception;
        }
    }

    @Override
    public void changePassword(String publicId, ChangePasswordDto changePassword) throws MessagingException {
        log.info("Changing password for User :: {}", publicId);
        try {
            UUID uuid = UUID.fromString(publicId);
            var user = userRepository.findByPublicId(uuid)
                    .orElseThrow(() -> new NotFoundException("Invalid user's Public ID :: " + publicId, HttpStatus.NOT_FOUND));

            if(!passwordEncoder.matches(changePassword.getOldPassword(), user.getPassword())) {
                throw new NotFoundException("Old password is incorrect", BAD_REQUEST);
            }

            user.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
            userRepository.save(user);

            Map<String, Object> mailContext = new HashMap<>();
            mailContext.put("userName", user.getName());

            emailService.sendEmail(user.getEmail(),PASSWORD_CHANGE_SUBJECT, PASSWORD_CHANGE, mailContext);
        } catch (MessagingException exception) {
            log.error("Error occurred while triggering email for Change password Notification for user ID :: {}", publicId);
            throw exception;
        } catch (Exception exception) {
            log.error("Error occurred while executing changePassword :: {}", exception.getMessage());
            throw exception;
        }
    }
}
