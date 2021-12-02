package com.codesoom.assignment;

public enum HttpStatusCode {
    OK(200),
    CREATED(201),
    BAD_REQUEST(400),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500);

    private final int value;

    HttpStatusCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
