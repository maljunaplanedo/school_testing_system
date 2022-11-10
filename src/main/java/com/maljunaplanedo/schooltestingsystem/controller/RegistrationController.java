package com.maljunaplanedo.schooltestingsystem.controller;

import com.maljunaplanedo.schooltestingsystem.dto.RegistrationFormDto;
import com.maljunaplanedo.schooltestingsystem.dto.RegistrationResponseDto;
import com.maljunaplanedo.schooltestingsystem.exception.RegistrationException;
import com.maljunaplanedo.schooltestingsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/register")
public class RegistrationController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    public RegistrationResponseDto register(@RequestBody RegistrationFormDto formData) throws RegistrationException {
        userService.register(formData);
        return RegistrationResponseDto.success();
    }
}
