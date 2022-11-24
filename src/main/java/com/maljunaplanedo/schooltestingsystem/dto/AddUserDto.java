package com.maljunaplanedo.schooltestingsystem.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserDto {
    private String firstName;
    private String lastName;
    private Long classId;
}
