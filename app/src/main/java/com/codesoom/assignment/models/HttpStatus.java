package com.codesoom.assignment.models;

public enum HttpStatus {
    SUCCESS(200),
    CREATED(201),
    NOCONTENT(204),
    NOTFOUND(404);

    private int code;
    HttpStatus(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }
}
