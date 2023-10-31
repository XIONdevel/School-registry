package com.example.demo.exception;

public class ExistsException extends RuntimeException{
    public ExistsException(String message) {
        super(message);
    }

    public ExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
