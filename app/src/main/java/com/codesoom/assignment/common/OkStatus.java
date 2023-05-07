package com.codesoom.assignment.common;

public class OkStatus implements HttpStatus {

    private static final int HTTP_OK_CODE = 200;
    private static final String HTTP_OK_MESSAGE = "OK";

    @Override
    public int getCode() {
        return HTTP_OK_CODE;
    }

    @Override
    public String getMessage() {
        return HTTP_OK_MESSAGE;
    }

}
