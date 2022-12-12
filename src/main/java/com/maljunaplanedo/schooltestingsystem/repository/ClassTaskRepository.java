package com.maljunaplanedo.schooltestingsystem.repository;

import com.maljunaplanedo.schooltestingsystem.model.ClassTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassTaskRepository extends JpaRepository<ClassTask, Long> {
}
