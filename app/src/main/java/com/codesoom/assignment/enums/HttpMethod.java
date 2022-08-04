package com.codesoom.assignment.enums;

public enum HttpMethod {
    GET,
    POST,
    PUT,
    PATCH,
    DELETED;

    public boolean equals(String method){
        return this.name().equals(method);
    }
}
