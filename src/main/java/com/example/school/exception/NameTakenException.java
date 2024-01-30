package com.example.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class NameTakenException extends RuntimeException{
    public NameTakenException() {
    }

    public NameTakenException(String message) {
        super(message);
    }
}
