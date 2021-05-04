package com.codesoom.assignment;

public enum HttpStatus {
    OK(200, "OK");

    private int code;
    private String description;

    HttpStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int code() {
        return code;
    }
}
