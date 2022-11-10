package com.maljunaplanedo.schooltestingsystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    @GetMapping("")
    public String index() {
        return "some info available only for students";
    }
}
