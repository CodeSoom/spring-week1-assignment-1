package com.codesoom.assignment.http;

public enum HttpStatus {
    // 100: Informational

    // 200: Successful
    OK(200, "OK"),

    CREATE(201, "Create"),

    NO_CONTENT(204, "No Content"),

    // 300: Redirection

    // 400: Client Error
    BAD_REQUEST(400, "Bad Request"),

    NOT_FOUND(404, "Not Found"),

    // 500: Server Error
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    private int code;
    private String message;

    HttpStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public String message() {
        return this.message;
    }
}
