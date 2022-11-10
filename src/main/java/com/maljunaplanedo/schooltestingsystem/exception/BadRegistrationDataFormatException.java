package com.maljunaplanedo.schooltestingsystem.exception;

public class BadRegistrationDataFormatException extends RegistrationException {
    public BadRegistrationDataFormatException(String errorMessage) {
        super(errorMessage);
    }
}
