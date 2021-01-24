package com.codesoom.assignment;

public enum HttpStatusCode {
    OK(200),
    Created(201),
    NoContent(204),
    NotFound(404);

    int value;
    HttpStatusCode(int value) {
        this.value = value;
    }

    int getValue() {
        return value;
    }
}
