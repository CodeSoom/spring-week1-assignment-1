package com.codesoom.assignment;

public enum HttpStatus {
    OK(200),
    CREATED(201),
    NO_CONTENT(204),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500);

    private final int code;

    HttpStatus(int code) {
        this.code = code;
    }

    public int code() {
        return this.code;
    }
}
