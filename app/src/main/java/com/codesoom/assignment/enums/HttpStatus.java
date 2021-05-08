package com.codesoom.assignment.enums;

public enum HttpStatus {
    OK(200),
    CREATED(201),
    BAD_REQUEST(400),
    NOT_FOUND(404);

    private int code;

    HttpStatus(int i) {
        this.code = i;
    }

    public int code() {
        return code;
    }
}
