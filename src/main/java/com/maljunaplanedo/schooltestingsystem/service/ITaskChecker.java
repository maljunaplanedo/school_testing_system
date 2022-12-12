package com.maljunaplanedo.schooltestingsystem.service;

import com.maljunaplanedo.schooltestingsystem.exception.BadDataFormatException;
import com.maljunaplanedo.schooltestingsystem.model.StudentTask;
import com.maljunaplanedo.schooltestingsystem.model.Task;

public interface ITaskChecker {
    double computeMaxMark(Task task) throws BadDataFormatException;

    TaskCheckResult handle(StudentTask task, String request) throws BadDataFormatException;
}
