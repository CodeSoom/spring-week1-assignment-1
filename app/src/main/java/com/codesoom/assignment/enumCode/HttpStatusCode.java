package com.codesoom.assignment.enumCode;

public enum HttpStatusCode {
    OK(200),
    NOT_FOUND(404),
    CREATED(201),
    BAD_REQUEST(400);

    private final int code;

    HttpStatusCode(int code) {
        this.code = code;
    }

    public int getStatus() {
        return this.code;
    }
}
