package com.codesoom.assignment.enums;

public enum HttpResponse {
    OK(200),
    CREATED(201);

    private final int code;

    HttpResponse(int code) {
        this.code = code;
    }

    public int getCode(){
        return code;
    }
}
