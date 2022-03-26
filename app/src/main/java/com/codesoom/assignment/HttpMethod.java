package com.codesoom.assignment;

public enum HttpMethod {
    GET, POST, PUT, PATCH, DELETE;

    public boolean equals(String method) {
        return this.name().equals(method);
    }
}
