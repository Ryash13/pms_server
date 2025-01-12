package com.jfs.pms.controller;

import com.jfs.pms.dto.auth.AuthRequest;
import com.jfs.pms.dto.auth.RegisterDto;
import com.jfs.pms.service.user.IAuthService;
import com.jfs.pms.utility.Mapper;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.jfs.pms.constants.Constants.PASSWORD_RESET_SUCCESS;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDto registerDto, HttpServletRequest request) {
        log.info("Executing API :: {}", request.getRequestURI());
        var user = Mapper.fromRegisterToUser(registerDto);
        return new ResponseEntity<>(authService.register(user), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest loginRequest, HttpServletRequest request) {
        log.info("Executing API :: {}", request.getRequestURI());
        return new ResponseEntity<>(authService.login(loginRequest), HttpStatus.OK);
    }

    @GetMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email, HttpServletRequest request) throws MessagingException {
        log.info("Executing API :: {}", request.getRequestURI());
        authService.resetPassword(email);
        return new ResponseEntity<>(PASSWORD_RESET_SUCCESS, HttpStatus.OK);
    }
}
