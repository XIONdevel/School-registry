package com.example.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EmailTakenException extends RuntimeException{
    public EmailTakenException() {
    }

    public EmailTakenException(String message) {
        super(message);
    }
}
