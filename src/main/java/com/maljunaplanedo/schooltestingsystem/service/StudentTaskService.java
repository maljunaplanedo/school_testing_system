package com.maljunaplanedo.schooltestingsystem.service;

import com.maljunaplanedo.schooltestingsystem.annotation.TaskChecker;
import com.maljunaplanedo.schooltestingsystem.exception.BadDataFormatException;
import com.maljunaplanedo.schooltestingsystem.exception.NoTaskExecutingException;
import com.maljunaplanedo.schooltestingsystem.model.StudentTask;
import com.maljunaplanedo.schooltestingsystem.model.StudentTaskStatus;
import com.maljunaplanedo.schooltestingsystem.model.TaskType;
import com.maljunaplanedo.schooltestingsystem.model.User;
import com.maljunaplanedo.schooltestingsystem.repository.StudentTaskRepository;
import com.maljunaplanedo.schooltestingsystem.service.dto.StudentTaskDto;
import com.maljunaplanedo.schooltestingsystem.service.dto.TaskRunnerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentTaskService {
    private static final String PING = "<PING>";

    private StudentTaskRepository studentTaskRepository;

    private UserService userService;

    private EnumMap<TaskType, ITaskChecker> checkers;

    private StudentTaskUtil util;

    @Autowired
    public void setStudentTaskRepository(StudentTaskRepository studentTaskRepository) {
        this.studentTaskRepository = studentTaskRepository;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setTaskCheckers(List<ITaskChecker> taskCheckers) {
        checkers = new EnumMap<>(TaskType.class);
        taskCheckers.forEach(taskChecker -> {
            var annotation = taskChecker.getClass().getAnnotation(TaskChecker.class);
            if (annotation != null) {
                checkers.put(annotation.value(), taskChecker);
            }
        });
    }

    @Autowired
    public void setUtil(StudentTaskUtil util) {
        this.util = util;
    }

    public ITaskChecker getChecker(TaskType taskType) {
        return checkers.get(taskType);
    }

    private StudentTask findById(long id) throws BadDataFormatException {
        return studentTaskRepository
            .findById(id)
            .orElseThrow(() -> new BadDataFormatException("Student task does not exist"));
    }

    private StudentTask findByIdAndCheckIfBelongsToCurrentUser(long id) throws BadDataFormatException {
        var studentTask = findById(id);
        var currentUser = userService.currentUserSafe();

        if (studentTask.getStudent().getId() != currentUser.getId()) {
            throw new BadDataFormatException("Task does not belong to current student");
        }

        return studentTask;
    }

    public StudentTaskDto getStudentTaskForStudent(long id) throws BadDataFormatException {
        var studentTask = findByIdAndCheckIfBelongsToCurrentUser(id);

        if (studentTask.getStatus() == StudentTaskStatus.NOT_STARTED) {
            return StudentTaskDto.brief(studentTask);
        } else {
            return StudentTaskDto.withoutAnswer(studentTask);
        }
    }

    public StudentTaskDto getStudentTaskForTeacher(long id) throws BadDataFormatException {
        var studentTask = findById(id);
        if (studentTask.getStatus() == StudentTaskStatus.FINISHED) {
            return StudentTaskDto.full(studentTask);
        }
        return StudentTaskDto.brief(studentTask);
    }

    public void begin(long id) throws BadDataFormatException {
        var studentTask = findByIdAndCheckIfBelongsToCurrentUser(id);
        if (studentTask.getStatus() != StudentTaskStatus.NOT_STARTED) {
            throw new BadDataFormatException("Task has wrong status");
        }

        var now = util.currentTime();
        var deadline = studentTask.getClassTask().getDeadline();

        var limit = Math.min(now + studentTask.getClassTask().getTask().getDuration(), deadline);

        studentTask.setBeginTime(now);
        studentTask.setLimitTime(limit);
        studentTask.setStatus(StudentTaskStatus.IN_PROGRESS);
    }

    public StudentTask findCurrentTask(User user) throws NoTaskExecutingException {
        return studentTaskRepository
            .findByStudentAndStatus(user, StudentTaskStatus.IN_PROGRESS)
            .orElseThrow(() -> new NoTaskExecutingException("No task executing right now"));
    }

    public void finishCurrentTaskIfExists() throws BadDataFormatException {
        var user = userService.currentUserSafe();
        try {
            var task = findCurrentTask(user);
            util.finishTaskAt(task, util.currentTime());
        } catch (NoTaskExecutingException ignored) {}
    }

    public Optional<TaskRunnerResponse> handleWebsocketCheckRequest(String request, User user)
            throws BadDataFormatException {
        StudentTask task;
        try {
            task = findCurrentTask(user);
        } catch (NoTaskExecutingException exception) {
            return Optional.empty();
        }

        if (task.getStatus() == StudentTaskStatus.FINISHED) {
            return Optional.empty();
        }

        var result = new TaskRunnerResponse();
        if (!PING.equals(request)) {
            var checkResult = getChecker(task.getClassTask().getTask().getType()).handle(task, request);
            if (checkResult.isFinish()) {
                util.finishTaskAt(task, util.currentTime());
                return Optional.empty();
            }
            result.setPayload(checkResult.getResponse());
        }
        result.setTimeLeft(task.getLimitTime() - util.currentTime());
        result.setMark(task.getCurrentMark());

        return Optional.of(result);
    }
}
