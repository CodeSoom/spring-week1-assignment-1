package com.codesoom.assignment.enums;

public enum HttpResponse {
    OK(200),
    CREATED(201),
    BAD_REQUEST(400),
    NO_CONTENT(204),
    NOT_FOUND(404);

    private final int code;

    HttpResponse(int code) {
        this.code = code;
    }

    public int getCode(){
        return code;
    }
}
