package com.maljunaplanedo.schooltestingsystem.service;

import com.maljunaplanedo.schooltestingsystem.service.dto.TaskDto;
import com.maljunaplanedo.schooltestingsystem.exception.BadDataFormatException;
import com.maljunaplanedo.schooltestingsystem.model.Task;
import com.maljunaplanedo.schooltestingsystem.model.TaskType;
import com.maljunaplanedo.schooltestingsystem.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskService {
    private TaskRepository taskRepository;

    private StudentTaskService studentTaskService;

    @Autowired
    public void setTaskRepository(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Autowired
    public void setStudentTaskService(StudentTaskService studentTaskService) {
        this.studentTaskService = studentTaskService;
    }

    private boolean badName(@Nullable String name) {
        return name == null || name.isEmpty() || name.length() > 128;
    }

    private boolean badStatement(@Nullable String statement) {
        return statement == null || statement.isEmpty() || statement.length() > 10000;
    }

    private boolean badType(@Nullable TaskType type) {
        return type == null;
    }

    private boolean badDuration(@Nullable Long duration) {
        return duration == null || duration > 10 * 60 * 60;
    }

    public Task findById(long id) throws BadDataFormatException {
        return taskRepository
            .findById(id)
            .orElseThrow(() -> new BadDataFormatException("Task does not exist"));
    }

    public TaskDto getTask(long id) throws BadDataFormatException {
        return TaskDto.full(findById(id));
    }

    public List<TaskDto> getAllTasks() {
        return taskRepository
            .findAll()
            .stream()
            .map(TaskDto::brief)
            .collect(Collectors.toList());
    }

    private void saveTask(Task task, TaskDto taskInfo) throws BadDataFormatException {
        var type = taskInfo.getType();
        var name = taskInfo.getName();
        var statement = taskInfo.getStatement();
        var answer = taskInfo.getAnswer();
        var duration = taskInfo.getDuration();

        if (badType(type) || badName(name) || badStatement(statement)
            || badStatement(answer) || badDuration(duration)) {
            throw new BadDataFormatException("Bad data format");
        }

        task.setType(type);
        task.setName(name);
        task.setStatement(statement);
        task.setAnswer(answer);
        task.setDuration(duration);
        task.setMaxMark(studentTaskService.getChecker(type).computeMaxMark(task));

        taskRepository.save(task);
    }

    public void addTask(TaskDto taskInfo) throws BadDataFormatException {
        saveTask(new Task(), taskInfo);
    }

    public void updateTask(long id, TaskDto taskInfo) throws BadDataFormatException {
        saveTask(findById(id), taskInfo);
    }

    public void removeTask(long id) throws BadDataFormatException {
        var task = findById(id);
        taskRepository.delete(task);
    }
}
