package com.codesoom.assignment.models;

public enum HttpStatus {
    OK(200),
    CREATED(201),
    NO_CONTENT(204),
    NOT_FOUND(404);

    private final int code;

    HttpStatus(int code){
        this.code = code;
    }

    public int getStatusCode(){
        return code;
    }
}
