package com.codesoom.assignment;

public enum HttpStatus {
    Ok(200),
    Created(201),
    NoContent(204),
    NotFound(404);

    private final int code;

    HttpStatus(int code) {
        this.code = code;
    }

    public int code() {
        return this.code;
    }
}
