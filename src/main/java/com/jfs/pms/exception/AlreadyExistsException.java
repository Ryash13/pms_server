package com.jfs.pms.exception;

import org.springframework.http.HttpStatus;

public class AlreadyExistsException extends RuntimeException {
    private final String message;
    private final HttpStatus status;

    public AlreadyExistsException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
