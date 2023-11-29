package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hi")
public class SchoolController {
    private static final Logger logger = LoggerFactory.getLogger(SchoolController.class);

    @GetMapping()
    public String mainPage() {
        return "Hello there!";
    }

}
