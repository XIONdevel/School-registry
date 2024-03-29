package com.example.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PhoneTakenException extends RuntimeException{
    public PhoneTakenException() {
    }

    public PhoneTakenException(String message) {
        super(message);
    }
}
