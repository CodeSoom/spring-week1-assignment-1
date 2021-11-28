package com.codesoom.assignment;

public enum HttpStatus {

    HTTP_STATUS_OK(200),
    HTTP_STATUS_CREATED(201),
    HTTP_STATUS_CREATED_NO_CONTENT(204),
    HTTP_STATUS_NOT_FOUND(404);

    private int code;

    private HttpStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
