package com.maljunaplanedo.schooltestingsystem.controller;

import com.maljunaplanedo.schooltestingsystem.dto.AddClassResponseDto;
import com.maljunaplanedo.schooltestingsystem.dto.RegistrationFailureCause;
import com.maljunaplanedo.schooltestingsystem.dto.RegistrationResponseDto;
import com.maljunaplanedo.schooltestingsystem.exception.BadDataFormatException;
import com.maljunaplanedo.schooltestingsystem.exception.ClassNameAlreadyUsedException;
import com.maljunaplanedo.schooltestingsystem.exception.UsernameAlreadyUsedException;
import com.maljunaplanedo.schooltestingsystem.exception.WrongInviteCodeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(UsernameAlreadyUsedException.class)
    public RegistrationResponseDto usernameAlreadyUsed() {
        return RegistrationResponseDto.failure(RegistrationFailureCause.USERNAME_ALREADY_USED);
    }

    @ExceptionHandler(WrongInviteCodeException.class)
    public RegistrationResponseDto wrongInviteCode() {
        return RegistrationResponseDto.failure(RegistrationFailureCause.WRONG_INVITE_CODE);
    }

    @ExceptionHandler(ClassNameAlreadyUsedException.class)
    public AddClassResponseDto classNameAlreadyUsed() {
        return AddClassResponseDto.failure();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadDataFormatException.class)
    public void badFormat() {

    }
}
