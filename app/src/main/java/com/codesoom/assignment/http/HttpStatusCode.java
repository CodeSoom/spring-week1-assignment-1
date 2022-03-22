package com.codesoom.assignment.http;

public enum HttpStatusCode {

    OK(200),
    CREATED(201),
    NOT_FOUND(404);

    private final int value;

    HttpStatusCode(int value) {
        this.value = value;
    }

    public int getCode() {
        return this.value;
    }
}
