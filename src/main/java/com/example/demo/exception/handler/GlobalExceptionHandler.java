package com.example.demo.exception.handler;

import com.example.demo.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * TODO: implement exception handling
     */
    @ExceptionHandler(EmailTakenException.class)
    public ResponseEntity<Object> handlerEmailTakenException(EmailTakenException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", e.getMessage());
        return ResponseEntity.ok(body);
    }

    @ExceptionHandler(ExistsException.class)
    public ResponseEntity<Object> handlerExistsException(ExistsException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", e.getMessage());
        return ResponseEntity.ok(body);
    }

    @ExceptionHandler(GroupNotFoundException.class)
    public ResponseEntity<Object> handlerGroupNotFoundException(GroupNotFoundException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", e.getMessage());
        return ResponseEntity.ok(body);
    }

    @ExceptionHandler(NameTakenException.class)
    public ResponseEntity<Object> handlerNameTakenException(NameTakenException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", e.getMessage());
        return ResponseEntity.ok(body);
    }

    @ExceptionHandler(ParentNotFoundException.class)
    public ResponseEntity<Object> handlerParentNotFoundException(ParentNotFoundException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", e.getMessage());
        return ResponseEntity.ok(body);
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<Object> handlerStudentNotFoundException(StudentNotFoundException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", e.getMessage());
        return ResponseEntity.ok(body);
    }

    @ExceptionHandler(TeacherNotFoundException.class)
    public ResponseEntity<Object> handlerTeacherNotFoundException(TeacherNotFoundException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", e.getMessage());
        return ResponseEntity.ok(body);
    }

    @ExceptionHandler(SubjectNotFoundException.class)
    public ResponseEntity<Object> handlerSubjectNotFoundException(SubjectNotFoundException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", e.getMessage());
        return ResponseEntity.ok(body);
    }

    @ExceptionHandler(PhoneTakenException.class)
    public ResponseEntity<Object> handlerPhoneTakenException(PhoneTakenException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", e.getMessage());
        return ResponseEntity.ok(body);
    }


}
