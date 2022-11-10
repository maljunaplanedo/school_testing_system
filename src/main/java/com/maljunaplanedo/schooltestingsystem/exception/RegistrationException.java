package com.maljunaplanedo.schooltestingsystem.exception;

public abstract class RegistrationException extends Exception {
    RegistrationException(String errorMessage) {
        super(errorMessage);
    }
}
