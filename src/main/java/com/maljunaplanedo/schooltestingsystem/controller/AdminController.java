package com.maljunaplanedo.schooltestingsystem.controller;

import com.maljunaplanedo.schooltestingsystem.service.dto.UserDto;
import com.maljunaplanedo.schooltestingsystem.controller.dto.AddUserResponseDto;
import com.maljunaplanedo.schooltestingsystem.exception.BadDataFormatException;
import com.maljunaplanedo.schooltestingsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/teacher")
    public List<UserDto> getTeachers() {
        return userService.getAllTeachers();
    }

    @GetMapping("/teacher/{id}")
    public UserDto getTeacher(@PathVariable long id) throws BadDataFormatException {
        return userService.getTeacher(id);
    }

    @PostMapping("/teacher")
    public AddUserResponseDto addTeacher(@RequestBody UserDto userInfo) throws BadDataFormatException {
        return new AddUserResponseDto(userService.addTeacher(userInfo));
    }

    @PutMapping("/teacher/{id}")
    public void updateTeacher(@PathVariable long id, @RequestBody UserDto userInfo) throws BadDataFormatException {
        userService.updateTeacher(id, userInfo);
    }

    @DeleteMapping("/teacher/{id}")
    public void removeTeacher(@PathVariable long id) throws BadDataFormatException {
        userService.removeTeacher(id);
    }
}
