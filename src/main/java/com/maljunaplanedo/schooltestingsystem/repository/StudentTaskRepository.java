package com.maljunaplanedo.schooltestingsystem.repository;

import com.maljunaplanedo.schooltestingsystem.model.StudentTask;
import com.maljunaplanedo.schooltestingsystem.model.StudentTaskStatus;
import com.maljunaplanedo.schooltestingsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentTaskRepository extends JpaRepository<StudentTask, Long> {
    Optional<StudentTask> findByStudentAndStatus(User student, StudentTaskStatus status);
}
