package com.codesoom.assignment.common;

public class NotFoundStatus implements HttpStatus {

    private static final int HTTP_NOT_FOUND_CODE = 200;
    private static final String HTTP_NOT_FOUND_MESSAGE = "OK";

    @Override
    public int getCode() {
        return HTTP_NOT_FOUND_CODE;
    }

    @Override
    public String getMessage() {
        return HTTP_NOT_FOUND_MESSAGE;
    }

}
