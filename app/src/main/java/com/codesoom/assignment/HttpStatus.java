package com.codesoom.assignment;

public enum HttpStatus {
    OK(200),
    CREATED(201),
    NO_CONTENT(204),
    NOT_FOUND(404);

    private final int value;

    HttpStatus(int value) {
        this.value = value;
    }

    public int code() {
        return value;
    }
}
