package com.maljunaplanedo.schooltestingsystem.exception;

public class UsernameAlreadyUsedException extends RegistrationException {
    public UsernameAlreadyUsedException(String errorMessage) {
        super(errorMessage);
    }
}
