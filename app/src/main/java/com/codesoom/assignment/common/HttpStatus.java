package com.codesoom.assignment.common;

public enum HttpStatus {
    OK(200),
    CREATED(201),
    NOT_FOUND(404);

    private final int code;

    HttpStatus(final int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
