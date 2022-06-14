package com.codesoom.assignment.models;

public enum HttpStatus {
    OK(200),
    CREATED(201),
    NOT_FOUND(404),
    BAD_REQUEST(400),
    DELETE_SUCCESS(204),
    ;
    int value;

    HttpStatus(int value) {
        this.value = value;
    }
    int value(){
        return value;
    }
}
