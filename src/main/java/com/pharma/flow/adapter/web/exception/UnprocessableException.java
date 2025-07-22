package com.pharma.flow.adapter.web.exception;

public class UnprocessableException extends ApplicationException {

    public UnprocessableException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return "UNPROCESSABLE_ENTITY";
    }
}
