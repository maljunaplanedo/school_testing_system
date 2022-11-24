package com.maljunaplanedo.schooltestingsystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
public class SmallUserInfoDto {
    @JsonInclude(NON_NULL)
    private String username;

    private String firstName;

    private String lastName;

    @JsonInclude(NON_NULL)
    private String inviteCode;
}
