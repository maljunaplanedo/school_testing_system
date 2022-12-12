package com.maljunaplanedo.schooltestingsystem.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "classes")
@Getter
@Setter
public class SchoolClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "schoolClass", cascade = CascadeType.REMOVE)
    private List<User> students;

    @OneToMany(mappedBy = "schoolClass", cascade = CascadeType.REMOVE)
    private List<ClassTask> assignedTasks;
}
