package com.example.demo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<Object> handlerStudentNotFoundException(StudentNotFoundException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", e.getMessage());
        return ResponseEntity.ok(body);
    }

    @ExceptionHandler(TeacherNotFoundException.class)
    public ResponseEntity<Object> handlerTeacherNotFoundException(StudentNotFoundException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", e.getMessage());
        return ResponseEntity.ok(body);
    }

    @ExceptionHandler(SubjectNotFoundException.class)
    public ResponseEntity<Object> handlerSubjectNotFoundException(StudentNotFoundException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", e.getMessage());
        return ResponseEntity.ok(body);
    }

}
