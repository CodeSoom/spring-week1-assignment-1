package com.codesoom.assignment;

/*
    Written based on RFC2616 section 10
    https://datatracker.ietf.org/doc/html/rfc2616#section-10
*/

public enum HttpStatusCode {

    OK(200),
    CREATED(201),
    BAD_REQUEST(400),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500);

    private final int value;

    HttpStatusCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
