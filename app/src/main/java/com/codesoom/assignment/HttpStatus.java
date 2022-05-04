package com.codesoom.assignment;

public enum HttpStatus {
    NOT_FOUND(404),
    OK(200),
    CREATED(201),
    NO_CONTENT(204);

    private int value;

    HttpStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
