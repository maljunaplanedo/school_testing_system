package com.maljunaplanedo.schooltestingsystem.controller;

import com.maljunaplanedo.schooltestingsystem.exception.BadDataFormatException;
import com.maljunaplanedo.schooltestingsystem.service.StudentTaskService;
import com.maljunaplanedo.schooltestingsystem.service.UserService;
import com.maljunaplanedo.schooltestingsystem.service.dto.StudentTaskDto;
import com.maljunaplanedo.schooltestingsystem.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    private UserService userService;

    private StudentTaskService studentTaskService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setStudentTaskService(StudentTaskService studentTaskService) {
        this.studentTaskService = studentTaskService;
    }

    @GetMapping("")
    public UserDto index() throws BadDataFormatException {
        return userService.getCurrentStudent();
    }

    @GetMapping("/task/{id}")
    public StudentTaskDto getTask(@PathVariable long id) throws BadDataFormatException {
        return studentTaskService.getStudentTaskForStudent(id);
    }

    @PutMapping("/task/{id}")
    public void begin(@PathVariable long id) throws BadDataFormatException {
        studentTaskService.begin(id);
    }

    @PutMapping("/task")
    public void finish() throws BadDataFormatException {
        studentTaskService.finishCurrentTaskIfExists();
    }
}
