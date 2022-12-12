package com.maljunaplanedo.schooltestingsystem.service;

import com.maljunaplanedo.schooltestingsystem.exception.BadDataFormatException;
import com.maljunaplanedo.schooltestingsystem.model.ClassTask;
import com.maljunaplanedo.schooltestingsystem.model.StudentTask;
import com.maljunaplanedo.schooltestingsystem.model.StudentTaskStatus;
import com.maljunaplanedo.schooltestingsystem.repository.ClassTaskRepository;
import com.maljunaplanedo.schooltestingsystem.service.dto.ClassDto;
import com.maljunaplanedo.schooltestingsystem.service.dto.ClassTaskDto;
import com.maljunaplanedo.schooltestingsystem.service.dto.TaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@Transactional
public class ClassTaskService {
    private ClassTaskRepository classTaskRepository;

    private ClassService classService;

    private TaskService taskService;

    @Autowired
    public void setClassTaskRepository(ClassTaskRepository classTaskRepository) {
        this.classTaskRepository = classTaskRepository;
    }

    @Autowired
    public void setClassRepository(ClassService classService) {
        this.classService = classService;
    }

    @Autowired
    public void setTaskRepository(TaskService taskService) {
        this.taskService = taskService;
    }

    private ClassTask findById(long id) throws BadDataFormatException {
        return classTaskRepository
            .findById(id)
            .orElseThrow(() -> new BadDataFormatException("Class task does not exist"));
    }

    public ClassTaskDto getClassTask(long id) throws BadDataFormatException {
        return ClassTaskDto.full(findById(id));
    }

    private void saveWithDeadline(ClassTask classTask, ClassTaskDto classTaskInfo) throws BadDataFormatException {
        var deadline = classTaskInfo.getDeadline();
        if (deadline == null) {
            throw new BadDataFormatException("Deadline is null");
        }

        var oldDeadline = classTask.getDeadline();

        if (oldDeadline != null && oldDeadline > deadline) {
            throw new BadDataFormatException("Deadline cannot be moved back");
        }

        classTask.setDeadline(deadline);
        classTaskRepository.save(classTask);
    }

    private boolean badClass(@Nullable ClassDto classInfo) {
        return classInfo == null || classInfo.getId() == null;
    }

    private boolean badTask(@Nullable TaskDto taskInfo) {
        return taskInfo == null || taskInfo.getId() == null;
    }

    public void assign(ClassTaskDto classTaskInfo) throws BadDataFormatException {
        var classTask = new ClassTask();

        var classInfo = classTaskInfo.getSchoolClass();
        var taskInfo = classTaskInfo.getTask();

        if (badClass(classInfo) || badTask(taskInfo)) {
            throw new BadDataFormatException("Bad data format");
        }

        var schoolClass = classService.findById(classInfo.getId());
        var task = taskService.findById(taskInfo.getId());

        classTask.setSchoolClass(schoolClass);
        classTask.setTask(task);

        classTask.setStudentTasks(
            schoolClass
                .getStudents()
                .stream()
                .map(student -> {
                    var studentTask = new StudentTask();
                    studentTask.setStudent(student);
                    studentTask.setStatus(StudentTaskStatus.NOT_STARTED);
                    studentTask.setClassTask(classTask);
                    studentTask.setCurrentMark(0.);
                    return studentTask;
                }).toList()
        );
        saveWithDeadline(classTask, classTaskInfo);
    }

    public void delay(long id, ClassTaskDto classTaskInfo) throws BadDataFormatException {
        saveWithDeadline(findById(id), classTaskInfo);
    }
}
