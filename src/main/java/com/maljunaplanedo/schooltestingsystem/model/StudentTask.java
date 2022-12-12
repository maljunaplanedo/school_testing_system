package com.maljunaplanedo.schooltestingsystem.model;

import com.maljunaplanedo.schooltestingsystem.service.StudentTaskService;
import com.maljunaplanedo.schooltestingsystem.service.StudentTaskUtil;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_tasks")
@EntityListeners(StudentTaskUtil.class)
@Getter
@Setter
public class StudentTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ClassTask classTask;

    @ManyToOne(fetch = FetchType.LAZY)
    private User student;

    private StudentTaskStatus status;

    private Long beginTime;

    private Long limitTime;

    private Long endTime;

    private String currentAnswer;

    private Double currentMark;
}
