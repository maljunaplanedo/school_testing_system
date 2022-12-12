package com.maljunaplanedo.schooltestingsystem.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.maljunaplanedo.schooltestingsystem.model.Task;
import com.maljunaplanedo.schooltestingsystem.model.TaskType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class TaskDto {
    private Long id;

    private TaskType type;

    private String name;

    private String statement;

    private String answer;

    private Double maxMark;

    private Long duration;

    private TaskDto(Task task, boolean includeStatement, boolean includeAnswer) {
        this.id = task.getId();
        this.type = task.getType();
        this.name = task.getName();
        this.maxMark = task.getMaxMark();
        this.duration = task.getDuration();

        if (includeStatement) {
            this.statement = task.getStatement();
            if (includeAnswer) {
                this.answer = task.getAnswer();
            }
        }
    }

    public static TaskDto full(Task task) {
        return new TaskDto(task, true, true);
    }

    public static TaskDto withoutAnswer(Task task) {
        return new TaskDto(task, true, false);
    }

    public static TaskDto brief(Task task) {
        return new TaskDto(task, false, false);
    }
}
