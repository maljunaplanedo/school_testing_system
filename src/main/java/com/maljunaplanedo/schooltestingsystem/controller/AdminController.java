package com.maljunaplanedo.schooltestingsystem.controller;

import com.maljunaplanedo.schooltestingsystem.dto.AddUserDto;
import com.maljunaplanedo.schooltestingsystem.dto.AddUserResponseDto;
import com.maljunaplanedo.schooltestingsystem.exception.BadDataFormatException;
import com.maljunaplanedo.schooltestingsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/teacher")
    public AddUserResponseDto addTeacher(@RequestBody AddUserDto userInfo) throws BadDataFormatException {
        return userService.addTeacher(userInfo);
    }

    @DeleteMapping("/teacher/{id}")
    public void removeTeacher(@PathVariable long id) throws BadDataFormatException {
        userService.removeTeacher(id);
    }
}
