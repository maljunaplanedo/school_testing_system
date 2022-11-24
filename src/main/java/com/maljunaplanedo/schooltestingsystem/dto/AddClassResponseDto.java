package com.maljunaplanedo.schooltestingsystem.dto;

import lombok.Getter;

@Getter
public class AddClassResponseDto {
    private final boolean success;

    private AddClassResponseDto(boolean success) {
        this.success = success;
    }

    public static AddClassResponseDto success() {
        return new AddClassResponseDto(true);
    }

    public static AddClassResponseDto failure() {
        return new AddClassResponseDto(false);
    }
}
