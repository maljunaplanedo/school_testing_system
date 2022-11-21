package com.maljunaplanedo.schooltestingsystem.exception;

// Is thrown for errors which must be prevented by the frontend
// So this is the only exception because if it is thrown
// This means the service is used in a wrong way (direct access to the API without UI)
public class BadDataFormatException extends Exception {
    public BadDataFormatException(String errorMessage) {
        super(errorMessage);
    }
}
