package com.pharma.flow.adapter.web.exception;

public class ValidationException extends ApplicationException {

    public ValidationException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return "VALIDATION_ERROR";
    }
}
