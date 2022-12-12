package com.maljunaplanedo.schooltestingsystem.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class TaskRunnerResponse {
    private Long timeLeft;
    private Double mark;
    private String payload;
}
