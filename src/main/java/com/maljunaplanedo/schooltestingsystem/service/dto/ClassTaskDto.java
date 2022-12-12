package com.maljunaplanedo.schooltestingsystem.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.maljunaplanedo.schooltestingsystem.model.ClassTask;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZoneOffset;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class ClassTaskDto {
    private Long id;

    private TaskDto task;

    private ClassDto schoolClass;

    private Long deadline;

    private List<StudentTaskDto> studentTasks;

    private ClassTaskDto(ClassTask classTask, boolean includeStudentTasks,
                         boolean includeStatement, boolean includeAnswer) {
        this.id = classTask.getId();

        if (includeStatement) {
            if (includeAnswer) {
                this.task = TaskDto.full(classTask.getTask());
            } else {
                this.task = TaskDto.withoutAnswer(classTask.getTask());
            }
        } else {
            this.task = TaskDto.brief(classTask.getTask());
        }
        this.deadline = classTask.getDeadline();
        this.schoolClass = ClassDto.brief(classTask.getSchoolClass());

        if (includeStudentTasks) {
            this.studentTasks = classTask
                .getStudentTasks()
                .stream()
                .map(StudentTaskDto::brief)
                .toList();
        }
    }

    public static ClassTaskDto full(ClassTask classTask) {
        return new ClassTaskDto(classTask, true, false, false);
    }

    public static ClassTaskDto brief(ClassTask classTask) {
        return new ClassTaskDto(classTask, false, false, false);
    }

    public static ClassTaskDto withStatement(ClassTask classTask) {
        return new ClassTaskDto(classTask, false, true, false);
    }

    public static ClassTaskDto withAnswer(ClassTask classTask) {
        return new ClassTaskDto(classTask, false, true, true);
    }
}
