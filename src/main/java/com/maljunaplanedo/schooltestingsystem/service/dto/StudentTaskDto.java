package com.maljunaplanedo.schooltestingsystem.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.maljunaplanedo.schooltestingsystem.model.StudentTask;
import com.maljunaplanedo.schooltestingsystem.model.StudentTaskStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZoneOffset;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class StudentTaskDto {
    private Long id;

    private ClassTaskDto classTask;

    private UserDto student;

    private StudentTaskStatus status;

    private Long begin;

    private Long limit;

    private Long end;

    private String answer;

    private Double mark;

    private StudentTaskDto(StudentTask studentTask, boolean includeStats, boolean includeAnswer) {
        this.id = studentTask.getId();
        this.status = studentTask.getStatus();

        this.student = UserDto.brief(studentTask.getStudent());

        if (includeStats) {
            if (includeAnswer) {
                this.classTask = ClassTaskDto.withAnswer(studentTask.getClassTask());
            } else {
                this.classTask = ClassTaskDto.withStatement(studentTask.getClassTask());
            }
            this.begin = studentTask.getBeginTime();
            this.end = studentTask.getEndTime();
            this.answer = studentTask.getCurrentAnswer();
            this.limit = studentTask.getLimitTime();
        } else {
            this.classTask = ClassTaskDto.brief(studentTask.getClassTask());
        }

        this.mark = studentTask.getCurrentMark();
    }

    public static StudentTaskDto full(StudentTask studentTask) {
        return new StudentTaskDto(studentTask, true, true);
    }

    public static StudentTaskDto withoutAnswer(StudentTask studentTask) {
        return new StudentTaskDto(studentTask, true, false);
    }

    public static StudentTaskDto brief(StudentTask studentTask) {
        return new StudentTaskDto(studentTask, false, false);
    }
}
