package com.maljunaplanedo.schooltestingsystem.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.maljunaplanedo.schooltestingsystem.model.SchoolClass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class ClassDto {
    private Long id;

    private String name;

    private List<UserDto> students;

    private List<ClassTaskDto> assignedTasks;

    private ClassDto(SchoolClass schoolClass, boolean includeStudentsAndTasks) {
        this.id = schoolClass.getId();
        this.name = schoolClass.getName();
        if (includeStudentsAndTasks) {
            this.students = schoolClass
                .getStudents()
                .stream()
                .map(UserDto::brief)
                .toList();
            this.assignedTasks = schoolClass
                .getAssignedTasks()
                .stream()
                .map(ClassTaskDto::brief)
                .toList();
        }
    }

    public static ClassDto full(SchoolClass schoolClass) {
        return new ClassDto(schoolClass, true);
    }

    public static ClassDto brief(SchoolClass schoolClass) {
        return new ClassDto(schoolClass, false);
    }
}
