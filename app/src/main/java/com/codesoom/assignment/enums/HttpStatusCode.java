package com.codesoom.assignment.enums;

public enum HttpStatusCode {

    OK(200, "정상적으로 처리되었습니다."),
    CREATED(201, "정상적으로 생성되었습니다."),

    BAD_REQUEST(400, "Bad Request");

    private final int code;

    private final String message;

    HttpStatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
