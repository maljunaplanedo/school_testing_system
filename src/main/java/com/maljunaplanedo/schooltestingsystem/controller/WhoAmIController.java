package com.maljunaplanedo.schooltestingsystem.controller;

import com.maljunaplanedo.schooltestingsystem.service.UserService;
import com.maljunaplanedo.schooltestingsystem.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/whoami")
public class WhoAmIController {
    private static final String UNAUTHORIZED_RESPONSE = "UNAUTHORIZED";

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public UserDto whoAmI() {
        return userService.whoAmI();
    }
}
