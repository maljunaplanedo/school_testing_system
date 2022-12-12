package com.maljunaplanedo.schooltestingsystem.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tasks")
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private TaskType type;

    private String name;

    private String statement;

    private String answer;

    private double maxMark;

    private long duration;

    @OneToMany(mappedBy = "task", cascade = CascadeType.REMOVE)
    private List<ClassTask> classTasks;
}
