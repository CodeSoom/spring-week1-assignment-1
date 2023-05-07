package com.codesoom.assignment.common;

public class CreatedStatus implements HttpStatus {

    private static final int HTTP_CREATED_CODE = 201;
    private static final String HTTP_CREATED_MESSAGE = "Created";

    @Override
    public int getCode() {
        return HTTP_CREATED_CODE;
    }

    @Override
    public String getMessage() {
        return HTTP_CREATED_MESSAGE;
    }

}
