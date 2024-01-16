package com.example.demo.security.auth;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/x")
public class TempController {

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String x() {
        return "Hi!";
    }

}
