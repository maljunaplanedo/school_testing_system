package com.maljunaplanedo.schooltestingsystem.exception;

public class WrongInviteCodeException extends RegistrationException {
    public WrongInviteCodeException(String errorMessage) {
        super(errorMessage);
    }
}
