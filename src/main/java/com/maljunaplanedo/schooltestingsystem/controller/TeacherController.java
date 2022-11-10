package com.maljunaplanedo.schooltestingsystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {
    @GetMapping("")
    public String index() {
        return "some info available only for teachers";
    }
}
