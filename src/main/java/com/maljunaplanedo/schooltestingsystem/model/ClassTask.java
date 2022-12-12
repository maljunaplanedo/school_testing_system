package com.maljunaplanedo.schooltestingsystem.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "class_tasks")
@Getter
@Setter
public class ClassTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    private SchoolClass schoolClass;

    private Long deadline;

    @OneToMany(mappedBy = "classTask", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<StudentTask> studentTasks;
}
