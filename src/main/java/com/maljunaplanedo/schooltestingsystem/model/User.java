package com.maljunaplanedo.schooltestingsystem.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String username;

    private String password;

    private UserRole role;

    @Column(unique = true)
    private String inviteCode;

    private String firstName;

    private String lastName;

    @ManyToOne(fetch = FetchType.LAZY)
    private SchoolClass schoolClass;
}
