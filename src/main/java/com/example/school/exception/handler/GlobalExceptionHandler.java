package com.example.school.exception.handler;

import com.example.school.exception.*;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler({EmailTakenException.class})
    public ResponseEntity<Object> handleEmailTakenException(EmailTakenException e) {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException e) {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", e.getMessage());
        return ResponseEntity.status(401).body(responseBody);
    }


}
