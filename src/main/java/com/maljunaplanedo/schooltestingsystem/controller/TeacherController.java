package com.maljunaplanedo.schooltestingsystem.controller;

import com.maljunaplanedo.schooltestingsystem.dto.*;
import com.maljunaplanedo.schooltestingsystem.exception.BadDataFormatException;
import com.maljunaplanedo.schooltestingsystem.exception.ClassNameAlreadyUsedException;
import com.maljunaplanedo.schooltestingsystem.service.ClassService;
import com.maljunaplanedo.schooltestingsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {
    private UserService userService;

    private ClassService classService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setClassService(ClassService classService) {
        this.classService = classService;
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

    @PostMapping("/class")
    public AddClassResponseDto addClass(@RequestBody ClassDto classInfo)
            throws BadDataFormatException, ClassNameAlreadyUsedException {
        classService.addClass(classInfo);
        return AddClassResponseDto.success();
    }

    @DeleteMapping("/class/{id}")
    public void deleteClass(@PathVariable long id) throws BadDataFormatException {
        classService.removeClass(id);
    }

    @GetMapping("/class/{id}")
    public List<SmallUserInfoDto> getStudents(@PathVariable long id) throws BadDataFormatException {
        return classService.getStudents(id);
    }
}
