package com.jfs.pms.exception;

import com.jfs.pms.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import static com.jfs.pms.constants.Constants.GENERAL_ERROR_MESSAGE;
import static com.jfs.pms.constants.Constants.INVALID_CREDENTIALS;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(final NotFoundException exception, final
    HttpServletRequest request) {
        ApiErrorResponse response = new ApiErrorResponse(
                exception.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, exception.getStatus());
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<?> handleAlreadyExistsException(final AlreadyExistsException exception, final
    HttpServletRequest request) {
        ApiErrorResponse response = new ApiErrorResponse(
                exception.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, exception.getStatus());
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<?> handleUnAuthorizedException(final UnAuthorizedException exception, final
    HttpServletRequest request) {
        ApiErrorResponse response = new ApiErrorResponse(
                exception.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, exception.getStatus());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(final BadCredentialsException exception, final
    HttpServletRequest request) {
        ApiErrorResponse response = new ApiErrorResponse(
                INVALID_CREDENTIALS,
                request.getRequestURI(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllOtherUnknownExceptions(final Exception exception, final
    HttpServletRequest request) {
        ApiErrorResponse response = new ApiErrorResponse(
                GENERAL_ERROR_MESSAGE,
                request.getRequestURI(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
