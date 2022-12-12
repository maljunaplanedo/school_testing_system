package com.maljunaplanedo.schooltestingsystem.controller.dto;

import lombok.Getter;

@Getter
public class UpdateClassResponseDto {
    private final boolean success;

    private UpdateClassResponseDto(boolean success) {
        this.success = success;
    }

    public static UpdateClassResponseDto success() {
        return new UpdateClassResponseDto(true);
    }

    public static UpdateClassResponseDto failure() {
        return new UpdateClassResponseDto(false);
    }
}
