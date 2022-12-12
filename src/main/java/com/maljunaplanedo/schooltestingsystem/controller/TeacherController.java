package com.maljunaplanedo.schooltestingsystem.controller;

import com.maljunaplanedo.schooltestingsystem.controller.dto.AddUserResponseDto;
import com.maljunaplanedo.schooltestingsystem.controller.dto.UpdateClassResponseDto;
import com.maljunaplanedo.schooltestingsystem.exception.BadDataFormatException;
import com.maljunaplanedo.schooltestingsystem.exception.ClassNameAlreadyUsedException;
import com.maljunaplanedo.schooltestingsystem.service.*;
import com.maljunaplanedo.schooltestingsystem.service.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {
    private UserService userService;

    private ClassService classService;

    private TaskService taskService;

    private ClassTaskService classTaskService;

    private StudentTaskService studentTaskService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setClassService(ClassService classService) {
        this.classService = classService;
    }

    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Autowired
    public void setClassTaskService(ClassTaskService classTaskService) {
        this.classTaskService = classTaskService;
    }

    @Autowired
    public void setStudentTaskService(StudentTaskService studentTaskService) {
        this.studentTaskService = studentTaskService;
    }

    @GetMapping("/student/{id}")
    public UserDto getStudent(@PathVariable long id) throws BadDataFormatException {
        return userService.getStudent(id);
    }

    @GetMapping("/student")
    public List<UserDto> getAllStudents() {
        return userService.getAllStudents();
    }

    @PostMapping("/student")
    public AddUserResponseDto addStudent(@RequestBody UserDto userInfo) throws BadDataFormatException {
        return new AddUserResponseDto(userService.addStudent(userInfo));
    }

    @PutMapping("/student/{id}")
    public void updateStudent(@PathVariable long id, @RequestBody UserDto userInfo)
            throws BadDataFormatException {
        userService.updateStudent(id, userInfo);
    }

    @DeleteMapping("/student/{id}")
    public void deleteStudent(@PathVariable long id) throws BadDataFormatException {
        userService.removeStudent(id);
    }

    @GetMapping("/class/{id}")
    public ClassDto getClass(@PathVariable long id) throws BadDataFormatException {
        return classService.getClass(id);
    }

    @GetMapping("/class")
    public List<ClassDto> getAllClasses()  {
        return classService.getAllClasses();
    }

    @PostMapping("/class")
    public UpdateClassResponseDto addClass(@RequestBody ClassDto classInfo)
            throws BadDataFormatException, ClassNameAlreadyUsedException {
        classService.addClass(classInfo);
        return UpdateClassResponseDto.success();
    }

    @PutMapping("/class/{id}")
    public UpdateClassResponseDto updateClass(@PathVariable long id, @RequestBody ClassDto classInfo)
            throws BadDataFormatException, ClassNameAlreadyUsedException {
        classService.updateClass(id, classInfo);
        return UpdateClassResponseDto.success();
    }

    @DeleteMapping("/class/{id}")
    public void deleteClass(@PathVariable long id) throws BadDataFormatException {
        classService.removeClass(id);
    }

    @GetMapping("/task/{id}")
    public TaskDto getTask(@PathVariable long id) throws BadDataFormatException {
        return taskService.getTask(id);
    }

    @GetMapping("/task")
    public List<TaskDto> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PostMapping("/task")
    public void addTask(@RequestBody TaskDto taskInfo) throws BadDataFormatException {
        taskService.addTask(taskInfo);
    }

    @PutMapping("/task/{id}")
    public void updateTask(@PathVariable long id, @RequestBody TaskDto taskInfo) throws BadDataFormatException {
        taskService.updateTask(id, taskInfo);
    }

    @DeleteMapping("/task/{id}")
    public void deleteTask(@PathVariable long id) throws BadDataFormatException {
        taskService.removeTask(id);
    }

    @GetMapping("/class_task/{id}")
    public ClassTaskDto getClassTask(@PathVariable long id) throws BadDataFormatException {
        return classTaskService.getClassTask(id);
    }

    @PostMapping("/class_task")
    public void assign(@RequestBody ClassTaskDto classTaskInfo) throws BadDataFormatException {
        classTaskService.assign(classTaskInfo);
    }

    @PutMapping("/class_task/{id}")
    public void delay(@PathVariable long id, @RequestBody ClassTaskDto classTaskInfo) throws BadDataFormatException {
        classTaskService.delay(id, classTaskInfo);
    }

    @GetMapping("/student_task/{id}")
    public StudentTaskDto getStudentTask(@PathVariable long id) throws BadDataFormatException {
        return studentTaskService.getStudentTaskForTeacher(id);
    }
}
