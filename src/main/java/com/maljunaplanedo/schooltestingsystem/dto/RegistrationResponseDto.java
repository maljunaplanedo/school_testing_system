package com.maljunaplanedo.schooltestingsystem.dto;

import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
public class RegistrationResponseDto {
    private final boolean success;
    private final RegistrationFailureCause cause;

    private RegistrationResponseDto(boolean success, @Nullable RegistrationFailureCause cause) {
        this.success = success;
        this.cause = cause;
    }

    public static RegistrationResponseDto success() {
        return new RegistrationResponseDto(true, null);
    }

    public static RegistrationResponseDto failure(RegistrationFailureCause cause) {
        return new RegistrationResponseDto(false, cause);
    }
}
