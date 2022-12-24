package com.maljunaplanedo.schooltestingsystem.service;

import com.maljunaplanedo.schooltestingsystem.model.StudentTask;
import com.maljunaplanedo.schooltestingsystem.model.StudentTaskStatus;
import org.springframework.stereotype.Component;

import javax.persistence.PostLoad;

@Component
public class StudentTaskUtil {
    public long currentTime() {
        return System.currentTimeMillis() / 1000L;
    }

    public void finishTaskAt(StudentTask studentTask, long time) {
        studentTask.setEndTime(time);
        studentTask.setStatus(StudentTaskStatus.FINISHED);
    }

    @PostLoad
    public void checkIfTimesUp(StudentTask studentTask) {
        var now = currentTime();
        if (
            (studentTask.getStatus() == StudentTaskStatus.IN_PROGRESS && now > studentTask.getLimitTime()) ||
            (studentTask.getStatus() == StudentTaskStatus.NOT_STARTED && now > studentTask.getClassTask().getDeadline())
        ) {
            finishTaskAt(studentTask, now);
        }
    }
}
