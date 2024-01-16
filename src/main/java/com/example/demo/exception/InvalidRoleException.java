package com.example.demo.exception;

public class InvalidRoleException extends RuntimeException {
    public InvalidRoleException() {
    }

    public InvalidRoleException(String message) {
        super(message);
    }

    public InvalidRoleException(String message, Throwable cause) {
        super(message, cause);
    }
}
