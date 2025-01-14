package com.jfs.pms.service.user;
import com.jfs.pms.domain.User;
import com.jfs.pms.dto.auth.AuthRequest;
import com.jfs.pms.exception.AlreadyExistsException;
import com.jfs.pms.exception.NotFoundException;
import com.jfs.pms.repository.UserRepository;
import com.jfs.pms.security.JwtService;
import com.jfs.pms.utility.EmailService;
import com.jfs.pms.utility.Mapper;
import com.jfs.pms.utility.PmsUtils;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.jfs.pms.constants.Constants.PASSWORD_RESET_SUBJECT;
import static com.jfs.pms.constants.enums.EmailTemplates.RESET_PASSWORD;
import static org.springframework.http.HttpStatus.CONFLICT;

@Service
public class AuthService implements IAuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    public AuthService(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtService jwtService, EmailService emailService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    @Override
    public User register(User user) {
        log.info("Registering new user with email :: {}", user.getEmail());
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
        } catch (AlreadyExistsException exception) {
            log.error("Error occurred while registering user :: {}", exception.getMessage());
            throw exception;
        } catch (Exception exception) {
            log.error("Error occurred while executing register :: {}", exception.getMessage());
            throw exception;
        }
    }

    @Override
    public Map<String, Object> login(AuthRequest request) {
        log.info("Authenticating user :: {}", request.getEmail());
        try {
            Map<String, Object> response = new HashMap<>();
            var auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            ));
            if(!auth.isAuthenticated()) {
                throw new BadCredentialsException("Invalid username or password, Please check again");
            }
            var user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Invalid request"));
            var profileInfo = Mapper.fromUserToAuthResponse(user);
            var token = jwtService.generateToken(user);
            response.put("token", token);
            response.put("user", profileInfo);
            return response;
        } catch (Exception exception) {
            log.error("Error occurred while executing login :: {}", exception.getMessage());
            throw exception;
        }
    }

    @Override
    public void resetPassword(String email) throws MessagingException {
        log.info("Resetting password for email ID :: {}", email);
        try {
            var user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new NotFoundException("Email ID " + email + " is not registered", HttpStatus.NOT_FOUND));

            var password = PmsUtils.generatePassword();
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);

            Map<String, Object> mailContext = new HashMap<>();
            mailContext.put("displayName", user.getName());
            mailContext.put("email", email);
            mailContext.put("password", password);

            emailService.sendEmail(email,PASSWORD_RESET_SUBJECT, RESET_PASSWORD, mailContext);
        } catch (MessagingException exception) {
            log.error("Error occurred while triggering email for Reset password for user :: {}", email);
            throw exception;
        } catch (Exception exception) {
            log.error("Error occurred while executing resetPassword :: {}", exception.getMessage());
            throw exception;
        }

    }
}
