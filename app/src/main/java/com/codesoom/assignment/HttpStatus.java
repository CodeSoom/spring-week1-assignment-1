package com.codesoom.assignment;

public enum HttpStatus {
    OK(200), CREATE(201), NO_CONTENT(204), NOT_FOUND(404);

    private int code;

    HttpStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
