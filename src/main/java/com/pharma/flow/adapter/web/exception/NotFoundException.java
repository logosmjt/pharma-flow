package com.pharma.flow.adapter.web.exception;

public class NotFoundException extends ApplicationException {

    public NotFoundException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return "NOT_FOUND";
    }
}
