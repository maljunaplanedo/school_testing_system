package com.maljunaplanedo.schooltestingsystem.repository;

import com.maljunaplanedo.schooltestingsystem.model.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRepository extends JpaRepository<SchoolClass, Long> {
    boolean existsByName(String name);
}
