package com.maljunaplanedo.schooltestingsystem.controller;

import com.maljunaplanedo.schooltestingsystem.dto.AddUserDto;
import com.maljunaplanedo.schooltestingsystem.dto.AddUserResponseDto;
import com.maljunaplanedo.schooltestingsystem.exception.BadDataFormatException;
import com.maljunaplanedo.schooltestingsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String index() {
        return "some info available only for teachers";
    }

    @PostMapping("/student")
    public AddUserResponseDto addStudent(@RequestBody AddUserDto userInfo) throws BadDataFormatException {
        return userService.addStudent(userInfo);
    }

    @DeleteMapping("/student/{id}")
    public void deleteStudent(@PathVariable long id) throws BadDataFormatException {
        userService.removeStudent(id);
    }
}
