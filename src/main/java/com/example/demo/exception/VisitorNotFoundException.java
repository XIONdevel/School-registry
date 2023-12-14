package com.example.demo.exception;

public class VisitorNotFoundException extends RuntimeException {

    public VisitorNotFoundException() {
    }

    public VisitorNotFoundException(String message) {
        super(message);
    }
}
